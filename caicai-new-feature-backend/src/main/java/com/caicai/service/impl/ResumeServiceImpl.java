package com.caicai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caicai.entity.model.TalentResumeModel;
import com.caicai.entity.model.resume.*;
import com.caicai.entity.pojo.*;
import com.caicai.exception.BusinessException;
import com.caicai.mapper.*;
import com.caicai.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.caicai.entity.dto.ResumeAiConclusionDTO;
import com.caicai.entity.dto.ResumeDetailDTO;
import com.caicai.mapper.ResumeMapper;
import com.caicai.service.AiScoreService;
import com.caicai.service.AiSummaryService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
* @author CCN
* @description 针对表【resume(简历表)】的数据库操作Service实现
* @createDate 2025-06-24 11:05:31
*/
@Service
@Slf4j
@RequiredArgsConstructor
public class ResumeServiceImpl extends ServiceImpl<ResumeMapper, Resume>
    implements ResumeService{

    private final UserInfoMapper userInfoMapper;
    private final AwardHonorMapper awardHonorMapper;
    private final CertificateMapper certificateMapper;
    private final OrganizationExperienceMapper organizationExperienceMapper;
    private final UserSkillMapper userSkillMapper;
    private final UserEducationExperienceMapper userEducationExperienceMapper;
    private final UserWorkExperienceMapper userWorkExperienceMapper;
    private final UserJobIntentionMapper userJobIntentionMapper;
    private final UserProjectExperienceMapper userProjectExperienceMapper;
    private final ResumeMapper resumeMapper;
    private final AiScoreService aiScoreService;
    private final AiSummaryService aiSummaryService;

    /**
     * @param talentId:
     * @return TalentResumeModel:
     * @Author: Panda
     * @Description: 根据人才id获取对应简历详情信息
     * @Date: 2025/6/25 9:58
     */
    @Override
    public TalentResumeModel getTalentResumeDetail(Long talentId) {
        //获取用户基本信息
        UserInfoModel userInfoModel = this.userInfoMapper.selectUserInfoModelByUserId(talentId);
        if(userInfoModel == null) {
            log.error("获取人才简历详情失败 错误原因->用户[" + talentId + "]不存在");
            throw new BusinessException(HttpStatus.BAD_REQUEST.value(), "用户不存在");
        }

        //根据用户id获取简历id
        Long resumeId = resumeMapper.selectIdByUserId(talentId);

        //获取荣誉奖项集合
        List<AwardHonorModel> awardHonorLit =
                this.awardHonorMapper.getAwardHonorModelByResumeId(resumeId);

        //获取资格证书集合
        List<CertificateModel> certificateList =
                this.certificateMapper.selectCertificateModelListByUserId(talentId);

        //获取社团/组织经历集合
        List<OrganizationExperienceModel> organizationExperienceList =
                this.organizationExperienceMapper.selectOrganizationExperienceModelByUserId(talentId);

        //获取技能标签集合
        List<UserSkillModel> skillTagList =
                this.userSkillMapper.selectSkillTagModelByUserId(talentId);

        //获取教育经历集合
        List<UserEducationExperienceModel> userEducationList =
                this.userEducationExperienceMapper.selectUserEducationModelByUserId(talentId);

        //获取求职意向
        List<UserJobIntentionModel> userJobIntentions =
                this.userJobIntentionMapper.selectUserJobIntentionModel(talentId);

        //获取项目经验集合
        List<UserProjectExperienceModel> userProjectExperienceList =
                this.userProjectExperienceMapper.selectProjectExperienceModelByUserId(talentId);

        //获取工作经历集合
        List<UserWorkExperienceModel> userWorkExperienceModels =
                this.userWorkExperienceMapper.selectUserWorkExperienceModel(talentId);

        //构建TalentResumeModel
        return new TalentResumeModel()
                .setUserInfo(userInfoModel)
                .setResumeId(resumeId)
                .setAwardHonorList(awardHonorLit)
                .setCertificateList(certificateList)
                .setOrganizationExperienceList(organizationExperienceList)
                .setSkillTagList(skillTagList)
                .setUserEducationExperienceList(userEducationList)
                .setJobIntentions(userJobIntentions)
                .setUserProjectExperienceList(userProjectExperienceList)
                .setUserWorkExperienceList(userWorkExperienceModels);
    }

    @Override
    public Integer getAiScore(Long userId) {
        //根据userId查询对应的resume信息
        Resume resume = getOne(new LambdaQueryWrapper<Resume>().eq(Resume::getUserId, userId));
        //判断resume信息是否为空,不为空直接返回aiScore
        if (resume != null) {
            log.info("成功找到用户ID: {} 的简历信息。", userId);
            // 如果分数不为空，直接返回
            if (resume.getAiScore() != null) {
            return resume.getAiScore();
            }
            
            // 如果分数为空但有评价内容，尝试从评价中提取分数并更新
            if (StringUtils.hasText(resume.getAiConclusion())) {
                Integer score = extractScore(resume.getAiConclusion());
                if (score != null) {
                    log.info("从已有评价中提取到分数: {}，更新数据库", score);
                    resume.setAiScore(score);
                    updateById(resume);
                    return score;
                }
            }
            
            // 没有分数也没能提取到，返回null
            log.warn("用户ID: {} 的简历没有AI评分。", userId);
            return null;
        }
        //为空则返回null
        log.warn("未找到用户ID: {} 的简历信息。", userId);
        return null;
    }
    
    @Override
    public ResumeDetailDTO getResumeDetail(Long userId) {
        log.info("开始查询用户ID: {} 的简历详情", userId);
        
        // 创建一个简历详情对象
        ResumeDetailDTO resumeDetail = new ResumeDetailDTO();

        // 1. 获取用户信息
        User user = baseMapper.getUserById(userId);
        resumeDetail.setUser(user);
        log.info("用户ID：{} 的信息：{}", userId, user);
            
            // 2. 获取简历基本信息
            Resume resume = baseMapper.getResumeByUserId(userId);
        if (resume == null) {
            // 若无简历信息 则仅组织用户基本信息
            resumeDetail.setUser(user);
            return resumeDetail;
        }
        
                resumeDetail.setResume(resume);
        log.info("成功获取用户ID: {} 的简历基本信息", userId);

        // 3. 获取技能标签（直接从user_skill表联查，确保能获取全部）
        List<UserSkill> skillTags = baseMapper.getSkillTagsByUserId(resume.getId());
        resumeDetail.setSkillTags(skillTags);
        log.info("用户ID: {} 的技能标签数量: {}", userId, skillTags.size());

        // 4. 获取项目经验
                List<UserProjectExperience> projectExperiences =
                    baseMapper.getProjectExperiencesByUserId(resume.getId());
                resumeDetail.setProjectExperiences(projectExperiences);
        log.info("用户ID: {} 的项目经验数量: {}", userId, projectExperiences.size());
                
        // 5. 获取获奖荣誉
                List<AwardHonor> awardHonors = 
                    baseMapper.getAwardHonorsByResumeId(resume.getId());
                resumeDetail.setAwardHonors(awardHonors);
        log.info("用户ID: {} 的获奖荣誉数量: {}", userId, awardHonors.size());
                
        // 6. 获取工作经历
        List<UserWorkExperience> workExperiences = 
                baseMapper.getUserWorkExperiencesByUserId(userId);
        resumeDetail.setUserWorkExperiences(workExperiences);
        log.info("用户ID: {} 的工作经历数量: {}", userId, workExperiences.size());

        // 7. 获取教育经历
        List<UserEducationExperience> educationExperiences = 
                baseMapper.getUserEducationExperiencesByUserId(userId);
        resumeDetail.setUserEducationExperiences(educationExperiences);
        log.info("用户ID: {} 的教育经历数量: {}", userId, educationExperiences.size());

        log.info("成功获取用户ID: {} 的简历详情", userId);
        return resumeDetail;
    }
    
    @Override
    @Transactional
    public Integer scoreAndSaveResume(Long userId) {
        log.info("开始对用户ID: {} 的简历进行AI评分", userId);
        
        // 1. 获取简历详情
        ResumeDetailDTO resumeDetail = getResumeDetail(userId);
        if (resumeDetail == null || resumeDetail.getResume() == null) {
            log.warn("未找到用户ID: {} 的简历信息，无法进行AI评分", userId);
            return null;
        }
        
        // 获取简历对象
        Resume resume = resumeDetail.getResume();

        // 调用AI服务进行评分
        AtomicReference<Integer> scoreRef = new AtomicReference<>();
        
        try {
        // 从流式输出中提取分数并保存
        aiScoreService.streamScoreResume(resumeDetail)
            .collectList()
            .map(chunks -> {
                // 将所有文本片段合并
                String fullResponse = String.join("", chunks);
                
                // 提取分数
                Integer score = extractScore(fullResponse);
                if (score != null) {
                    scoreRef.set(score);
                        
                    // 提取建议文本
                    String suggestion = extractSuggestion(fullResponse);
                    
                    // 提取关键词
                    String keywords = extractKeywords(fullResponse);
                    
                    // 更新简历评分、结论和关键词
                    resume.setAiScore(score);
                    resume.setAiConclusion(suggestion);
                    resume.setAiKeywords(keywords);
                    boolean updated = updateById(resume);
                    if (updated) {
                        log.info("成功更新用户ID: {} 的简历AI评分: {}, 建议文本长度: {}, 关键词: {}", 
                                userId, score, 
                                suggestion != null ? suggestion.length() : 0, 
                                keywords);
                    } else {
                        log.warn("更新用户ID: {} 的简历AI评分、建议和关键词失败", userId);
                    }
                }
                return score;
            })
                .onErrorResume(e -> {
                    log.error("调用AI评分服务发生错误: {}", e.getMessage(), e);
                    return Mono.empty();
            })
            .block(); // 阻塞等待结果
        } catch (Exception e) {
            log.error("AI评分过程中发生异常: {}", e.getMessage(), e);
            return null;
        }
        
        return scoreRef.get();
    }
    
        @Override
        public Flux<String> streamScoreAndSaveResume(Long userId) {
            log.info("开始对用户ID: {} 的简历进行流式AI评分", userId);

            // 1. 获取简历详情
            ResumeDetailDTO resumeDetail = getResumeDetail(userId);
            if (resumeDetail == null || resumeDetail.getResume() == null) {
                log.warn("未找到用户ID: {} 的简历信息，无法进行AI评分", userId);
                return Flux.just("错误: 未找到简历信息");
            }

        // 获取简历对象
        Resume resume = resumeDetail.getResume();

        // 创建一个StringBuilder来收集完整的响应
            StringBuilder fullResponseBuilder = new StringBuilder();

        // 调用AI服务并在最后提取分数保存
            return aiScoreService.streamScoreResume(resumeDetail)
                .doOnNext(chunk -> {
                // 收集响应片段（除了结束标记）
                if (!chunk.equals("[DONE]") && !chunk.startsWith("错误: ")) {
                        fullResponseBuilder.append(chunk);
                    }
                })
                .doOnComplete(() -> {
                    // 在流结束时提取分数并保存
                    String fullResponse = fullResponseBuilder.toString();
                // 如果响应为空或包含错误信息，不进行保存
                if (fullResponse.isEmpty() || fullResponse.startsWith("错误")) {
                    log.warn("用户ID: {} 的AI评分结果为空或包含错误，不进行保存", userId);
                    return;
                }
                
                // 提取分数
                    Integer score = extractScore(fullResponse);
                
                // 提取建议文本
                String suggestion = extractSuggestion(fullResponse);
                
                // 提取关键词
                String keywords = extractKeywords(fullResponse);
                
                // 保存评分、结论和关键词
                resume.setAiScore(score);
                resume.setAiConclusion(suggestion);
                resume.setAiKeywords(keywords);
                boolean updated = updateById(resume);
                
                if (updated) {
                    log.info("成功保存用户ID: {} 的简历AI评分: {}, 建议文本长度: {}, 关键词: {}", 
                            userId, score, 
                            suggestion != null ? suggestion.length() : 0, 
                            keywords);
                                } else {
                    log.warn("保存用户ID: {} 的简历AI评分、建议和关键词失败", userId);
                                }
            })
            .onErrorResume(e -> {
                log.error("流式AI评分过程中发生错误: {}", e.getMessage(), e);
                return Flux.just("系统正在升级中，请稍后再试", "[DONE]");
                });
        }
    
    @Override
    public Mono<Boolean> saveResumeScore(Long userId, Integer score) {
        return Mono.fromCallable(() -> {
            try {
                // 查询简历
                Resume resume = getOne(new LambdaQueryWrapper<Resume>().eq(Resume::getUserId, userId));
                if (resume != null) {
                    // 更新评分
                    resume.setAiScore(score);
                    return updateById(resume);
                }
                return false;
            } catch (Exception e) {
                log.error("保存简历评分时发生错误", e);
                return false;
            }
        });
    }

    @Override
    public String getResumeAiConclusion(Long userId) {
        log.info("获取用户ID: {} 的简历AI评价结论", userId);
        
        Resume resume = lambdaQuery()
                .eq(Resume::getUserId, userId)
                .one();
        
        if (resume == null) {
            log.warn("未找到用户ID: {} 的简历信息", userId);
            return null;
        }
        
        return resume.getAiConclusion();
    }

    /**
     * 获取用户简历的AI摘要
     * @param userId 用户ID
     * @return AI生成的简历摘要，如果不存在则返回null
     */
    @Override
    public String getResumeAiSummary(Long userId) {
        log.info("获取用户ID: {} 的简历AI摘要", userId);
        
        Resume resume = lambdaQuery()
                .eq(Resume::getUserId, userId)
                .one();
        
        if (resume == null) {
            log.warn("未找到用户ID: {} 的简历信息", userId);
            return null;
        }
        
        return resume.getAiSummary();
    }

    /**
     * 使用AI对简历进行评价并保存（不生成分数）
     * @param userId 用户ID
     * @return 生成的AI评价内容和关键词
     */
    @Override
    @Transactional
    public ResumeAiConclusionDTO generateAndSaveResumeConclusion(Long userId) {
        log.info("开始对用户ID: {} 的简历进行AI评价", userId);

        // 获取简历详情
        ResumeDetailDTO resumeDetail = getResumeDetail(userId);
        if (resumeDetail == null || resumeDetail.getResume() == null) {
            log.warn("未找到用户ID: {} 的简历信息，无法进行AI评价", userId);
            return com.caicai.entity.dto.ResumeAiConclusionDTO.builder()
                    .conclusion("未找到简历信息")
                    .build();
        }

        // 获取简历对象
        Resume resume = resumeDetail.getResume();

        // 调用AI服务进行评价
        try {
            // 使用阻塞式API获取评价
            String conclusion = aiScoreService.generateResumeConclusion(resumeDetail);
            
            // 如果评价内容为空或以错误开头，则不保存
            if (conclusion == null || conclusion.isEmpty() || conclusion.startsWith("生成评分时发生错误")) {
                log.warn("用户ID: {} 的AI评价结果为空或包含错误，不进行保存", userId);
                return ResumeAiConclusionDTO.builder()
                        .conclusion(conclusion)
                        .build();
            }
            
            // 提取分数
            Integer score = extractScore(conclusion);
            
            // 提取建议文本
            String suggestion = extractSuggestion(conclusion);
            
            // 提取关键词
            String keywordsJson = extractKeywords(conclusion);
            List<String> keywordsList = null;
            
            // 将JSON格式的关键词转换为List
            if (keywordsJson != null && !keywordsJson.isEmpty()) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    keywordsList = mapper.readValue(keywordsJson, 
                            mapper.getTypeFactory().constructCollectionType(List.class, String.class));
                } catch (Exception e) {
                    log.warn("解析关键词JSON失败: {}", e.getMessage());
                }
            }
            
            // 更新简历结论、分数和关键词
            resume.setAiConclusion(suggestion);
            if (score != null) {
                resume.setAiScore(score);
                log.info("从AI评价中提取到分数: {}", score);
            } else {
                log.warn("未能从AI评价中提取到分数");
            }
            
            if (keywordsJson != null) {
                resume.setAiKeywords(keywordsJson);
                log.info("从AI评价中提取到关键词: {}", keywordsJson);
            } else {
                log.warn("未能从AI评价中提取到关键词");
            }
            
            boolean updated = updateById(resume);
            
            if (updated) {
                log.info("成功更新用户ID: {} 的简历AI评价结论、分数和关键词", userId);
            } else {
                log.warn("更新用户ID: {} 的简历AI评价结论、分数和关键词失败", userId);
            }
            
            // 构建并返回DTO
            return ResumeAiConclusionDTO.builder()
                    .conclusion(suggestion)
                    .keywords(keywordsList)
                    .score(score)
                    .build();
        } catch (Exception e) {
            log.error("AI评价过程中发生异常: {}", e.getMessage(), e);
            return ResumeAiConclusionDTO.builder()
                    .conclusion("系统出现了连接问题，请稍后再试")
                    .build();
        }
    }
    
    /**
     * 从AI响应中提取分数
     */
    private Integer extractScore(String responseText) {
        if (responseText == null || responseText.isEmpty()) {
            log.warn("无法从空文本中提取分数");
            return null;
        }
        
        log.info("开始从文本中提取分数，文本长度: {}", responseText.length());
        
        // 1. 尝试解析JSON格式
        try {
            // 查找JSON格式的开始和结束位置
            int jsonStartIndex = responseText.indexOf('{');
            int jsonEndIndex = responseText.lastIndexOf('}');
            
            if (jsonStartIndex >= 0 && jsonEndIndex > jsonStartIndex) {
                String jsonStr = responseText.substring(jsonStartIndex, jsonEndIndex + 1);
                // 使用Jackson或其他JSON库解析
                ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                JsonNode jsonNode = mapper.readTree(jsonStr);
                
                if (jsonNode.has("score")) {
                    int score = jsonNode.get("score").asInt();
                    log.info("从JSON中成功提取到分数: {}", score);
                    return score;
                }
            }
        } catch (Exception e) {
            log.warn("解析JSON格式失败: {}", e.getMessage());
            // 如果JSON解析失败，继续尝试其他方式提取
        }
        
        // 2. 尝试匹配"总分：XX分"或"总分(0-100分)：XX"等格式
        Pattern pattern = Pattern.compile("总分[（(]?[^)）]*[)）]?[：:] *(\\d+)");
        Matcher matcher = pattern.matcher(responseText);
        
        if (matcher.find()) {
            try {
                String scoreStr = matcher.group(1);
                log.info("匹配到总分格式，提取到分数字符串: {}", scoreStr);
                return Integer.parseInt(scoreStr);
            } catch (NumberFormatException e) {
                log.warn("无法解析AI评分结果中的分数: {}", matcher.group(1));
            }
        }
        
        // 3. 尝试匹配"分数：XX"等格式
        pattern = Pattern.compile("\\b(?:分数|得分|评分|总评分|最终得分|最终分数|score)[：:] *(\\d+)");
        matcher = pattern.matcher(responseText);
        
        if (matcher.find()) {
            try {
                String scoreStr = matcher.group(1);
                log.info("匹配到其他分数格式，提取到分数字符串: {}", scoreStr);
                return Integer.parseInt(scoreStr);
            } catch (NumberFormatException e) {
                log.warn("无法解析AI评分结果中的分数: {}", matcher.group(1));
            }
        }
        
        // 4. 尝试匹配任何位置的"XX分"格式（最后尝试，因为可能误匹配）
        pattern = Pattern.compile("(\\d{2,3})\\s*分");
        matcher = pattern.matcher(responseText);
        
        if (matcher.find()) {
            try {
                String scoreStr = matcher.group(1);
                log.info("匹配到'XX分'格式，提取到分数字符串: {}", scoreStr);
                int score = Integer.parseInt(scoreStr);
                // 验证分数是否在合理范围内(0-100)
                if (score >= 0 && score <= 100) {
                    return score;
                } else {
                    log.warn("提取到的分数 {} 超出合理范围(0-100)", score);
                }
            } catch (NumberFormatException e) {
                log.warn("无法解析AI评分结果中的分数: {}", matcher.group(1));
            }
        }
        
        // 记录未匹配到分数的情况
        log.warn("无法从AI响应中提取分数，原始文本片段: {}", 
                responseText.length() > 200 ? responseText.substring(0, 200) + "..." : responseText);
        return null;
    }

    /**
     * 生成并保存简历AI摘要
     * @param userId 用户ID
     * @return AI摘要内容
     */
    @Override
    @Transactional
    public String generateAndSaveResumeSummary(Long userId) {
        if (userId == null) {
            throw new BusinessException(HttpStatus.BAD_REQUEST.value(), "用户不存在");
        }

        log.info("开始生成并保存用户ID: {} 的简历AI摘要", userId);
        
        // 获取简历详情
        ResumeDetailDTO resumeDetail = getResumeDetail(userId);
        if (resumeDetail == null || resumeDetail.getResume() == null) {
            log.warn("未找到用户ID: {} 的简历信息，无法生成AI摘要", userId);
            return "未找到简历信息";
        }
        
        Resume resume = resumeDetail.getResume();
        
        try {
            // 调用AI服务生成摘要
            String summary = aiSummaryService.generateResumeSummary(resumeDetail);
            
            if (summary == null || summary.isEmpty() || summary.startsWith("生成摘要时发生错误")) {
                log.warn("用户ID: {} 的AI摘要结果为空或包含错误，不进行保存", userId);
                return summary;
            }
            
            // 更新简历摘要字段
            resume.setAiSummary(summary);
            boolean updated = updateById(resume);
            
            if (updated) {
                log.info("成功保存用户ID: {} 的简历AI摘要", userId);
            } else {
                log.warn("保存用户ID: {} 的简历AI摘要失败", userId);
            }
            
            return summary;
        } catch (Exception e) {
            log.error("生成简历AI摘要过程中发生异常: {}", e.getMessage(), e);
            return "生成摘要时发生错误: " + e.getMessage();
        }
    }
    
    /**
     * 流式生成简历AI摘要
     * @param userId 用户ID
     * @return AI摘要流
     */
    @Override
    public Flux<String> streamResumeSummary(Long userId) {
        log.info("开始流式生成用户ID: {} 的简历AI摘要", userId);
        
        // 获取简历详情
        ResumeDetailDTO resumeDetail = getResumeDetail(userId);
        if (resumeDetail == null || resumeDetail.getResume() == null) {
            log.warn("未找到用户ID: {} 的简历信息，无法生成AI摘要", userId);
            return Flux.just("错误: 未找到简历信息");
        }
        
        Resume resume = resumeDetail.getResume();
        
        // 创建一个StringBuilder来收集完整的响应
        StringBuilder fullResponseBuilder = new StringBuilder();
        
        // 调用AI服务并在最后保存摘要
        return aiSummaryService.streamResumeSummary(resumeDetail)
            .doOnNext(chunk -> {
                // 收集响应片段（除了结束标记）
                if (!chunk.equals("[DONE]") && !chunk.startsWith("错误: ")) {
                    fullResponseBuilder.append(chunk);
                }
            })
            .doOnComplete(() -> {
                // 在流结束时保存摘要
                String fullResponse = fullResponseBuilder.toString();
                // 如果响应为空或包含错误信息，不进行保存
                if (fullResponse.isEmpty() || fullResponse.startsWith("错误")) {
                    log.warn("用户ID: {} 的AI摘要结果为空或包含错误，不进行保存", userId);
                    return;
                }
                
                // 更新简历摘要字段
                resume.setAiSummary(fullResponse);
                boolean updated = updateById(resume);
                
                if (updated) {
                    log.info("成功保存用户ID: {} 的简历AI摘要", userId);
                } else {
                    log.warn("保存用户ID: {} 的简历AI摘要失败", userId);
                }
            })
            .onErrorResume(e -> {
                log.error("流式生成简历AI摘要过程中发生错误: {}", e.getMessage(), e);
                return Flux.just("系统正在升级中，请稍后再试", "[DONE]");
            });
    }

    /**
     * 从AI响应中提取关键词
     */
    private String extractKeywords(String responseText) {
        if (responseText == null || responseText.isEmpty()) {
            log.warn("无法从空文本中提取关键词");
            return null;
        }
        
        log.info("开始从文本中提取关键词，文本长度: {}", responseText.length());
        
        try {
            // 查找JSON格式的开始和结束位置
            int jsonStartIndex = responseText.indexOf('{');
            int jsonEndIndex = responseText.lastIndexOf('}');
            
            if (jsonStartIndex >= 0 && jsonEndIndex > jsonStartIndex) {
                String jsonStr = responseText.substring(jsonStartIndex, jsonEndIndex + 1);
                // 使用Jackson解析
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(jsonStr);
                
                if (jsonNode.has("keywords") && jsonNode.get("keywords").isArray()) {
                    // 将关键词数组转换为JSON字符串
                    String keywords = mapper.writeValueAsString(jsonNode.get("keywords"));
                    log.info("从JSON中成功提取到关键词: {}", keywords);
                    return keywords;
                }
            }
        } catch (Exception e) {
            log.warn("解析JSON格式失败: {}", e.getMessage());
        }
        
        log.warn("无法从AI响应中提取关键词");
        return null;
    }

    /**
     * 从AI响应中提取建议文本
     */
    private String extractSuggestion(String responseText) {
        if (responseText == null || responseText.isEmpty()) {
            log.warn("无法从空文本中提取建议文本");
            return responseText;
        }
        
        log.info("开始从文本中提取建议文本，文本长度: {}", responseText.length());
        
        try {
            // 查找JSON格式的开始和结束位置
            int jsonStartIndex = responseText.indexOf('{');
            int jsonEndIndex = responseText.lastIndexOf('}');
            
            if (jsonStartIndex >= 0 && jsonEndIndex > jsonStartIndex) {
                String jsonStr = responseText.substring(jsonStartIndex, jsonEndIndex + 1);
                // 使用Jackson解析
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(jsonStr);
                
                if (jsonNode.has("suggestion")) {
                    String suggestion = jsonNode.get("suggestion").asText();
                    log.info("从JSON中成功提取到建议文本，长度: {}", suggestion.length());
                    return suggestion;
                }
            }
        } catch (Exception e) {
            log.warn("解析JSON格式失败: {}", e.getMessage());
        }
        
        // 如果无法从JSON中提取，则返回原始文本
        return responseText;
    }

    /**
     * 流式生成并保存简历AI评价
     * @param userId 用户ID
     * @return AI评价流
     */
    @Override
    public Flux<ResumeAiConclusionDTO> streamGenerateAndSaveResumeConclusion(Long userId) {
        log.info("开始流式生成用户ID: {} 的简历AI评价", userId);
        
        // 获取简历详情
        ResumeDetailDTO resumeDetail = getResumeDetail(userId);
        if (resumeDetail == null || resumeDetail.getResume() == null) {
            log.warn("未找到用户ID: {} 的简历信息，无法进行AI评价", userId);
            return Flux.just(ResumeAiConclusionDTO.builder()
                    .conclusion("未找到简历信息")
                    .build());
        }
        
        // 获取简历对象
        Resume resume = resumeDetail.getResume();
        
        // 创建一个StringBuilder来收集完整的响应
        StringBuilder fullResponseBuilder = new StringBuilder();
        
        // 调用AI服务进行评价
        return aiScoreService.streamScoreResume(resumeDetail)
            // 过滤掉[DONE]和错误消息
            .filter(chunk -> !chunk.equals("[DONE]") && !chunk.startsWith("错误: "))
            // 收集响应并更新fullResponseBuilder
            .doOnNext(chunk -> fullResponseBuilder.append(chunk))
            // 在流结束时处理完整响应并保存到数据库
            .doOnComplete(() -> {
                String fullResponse = fullResponseBuilder.toString();
                if (fullResponse.isEmpty()) {
                    log.warn("用户ID: {} 的AI评价结果为空，不进行保存", userId);
                    return;
                }
                
                // 提取分数
                Integer score = extractScore(fullResponse);
                
                // 提取建议文本
                String suggestion = extractSuggestion(fullResponse);
                
                // 提取关键词
                String keywordsJson = extractKeywords(fullResponse);
                
                // 更新简历结论、分数和关键词
                resume.setAiConclusion(suggestion);
                resume.setAiScore(score);
                resume.setAiKeywords(keywordsJson);
                
                boolean updated = updateById(resume);
                
                if (updated) {
                    log.info("成功保存用户ID: {} 的简历AI评价结论、分数和关键词", userId);
                } else {
                    log.warn("保存用户ID: {} 的简历AI评价结论、分数和关键词失败", userId);
                }
            })
            // 将每个chunk转换为ResumeAiConclusionDTO
            .map(chunk -> {
                // 当前累积的响应
                String currentResponse = fullResponseBuilder.toString();
                
                // 提取当前可能的完整JSON
                try {
                    int jsonStartIndex = currentResponse.indexOf('{');
                    int jsonEndIndex = currentResponse.lastIndexOf('}');
                    
                    if (jsonStartIndex >= 0 && jsonEndIndex > jsonStartIndex) {
                        String jsonStr = currentResponse.substring(jsonStartIndex, jsonEndIndex + 1);
                        
                        // 尝试解析JSON
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode jsonNode = mapper.readTree(jsonStr);
                        
                        String suggestion = null;
                        List<String> keywords = null;
                        Integer score = null;
                        
                        if (jsonNode.has("suggestion")) {
                            suggestion = jsonNode.get("suggestion").asText();
                        }
                        
                        if (jsonNode.has("keywords") && jsonNode.get("keywords").isArray()) {
                            keywords = mapper.readValue(
                                    jsonNode.get("keywords").toString(),
                                    mapper.getTypeFactory().constructCollectionType(List.class, String.class));
                        }
                        
                        if (jsonNode.has("score")) {
                            score = jsonNode.get("score").asInt();
                        }
                        
                        return ResumeAiConclusionDTO.builder()
                                .conclusion(suggestion)
                                .keywords(keywords)
                                .score(score)
                                .build();
                    }
                } catch (Exception e) {
                    // JSON解析失败，返回部分响应
                    log.debug("解析JSON失败: {}", e.getMessage());
                }
                
                // 如果无法解析完整JSON，则返回当前累积的文本
                return ResumeAiConclusionDTO.builder()
                        .conclusion(currentResponse)
                        .build();
            })
            .onErrorResume(e -> {
                log.error("流式生成简历AI评价过程中发生错误: {}", e.getMessage(), e);
                return Flux.just(ResumeAiConclusionDTO.builder()
                        .conclusion("系统正在升级中，请稍后再试")
                        .build());
            });
    }
}
