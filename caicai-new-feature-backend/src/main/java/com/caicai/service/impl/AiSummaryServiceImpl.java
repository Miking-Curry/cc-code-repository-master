package com.caicai.service.impl;

import com.caicai.entity.dto.ResumeDetailDTO;
import com.caicai.service.AiSummaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 简历摘要服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiSummaryServiceImpl implements AiSummaryService {
    private final ChatClient summarizeResumeClient;
    
    /**
     * 生成简历摘要（阻塞式）
     * 
     * @param resumeDetailDTO
     * @return
     */
    @Override
    public String generateResumeSummary(ResumeDetailDTO resumeDetailDTO) {
        if (resumeDetailDTO == null || resumeDetailDTO.getResume() == null) {
            log.warn("简历信息为空，无法进行AI摘要分析");
            return "无简历信息";
        }
        
        try {
            String messageContent = buildResumeMessage(resumeDetailDTO);
            log.info("开始对用户ID: {} 的简历进行阻塞式AI摘要分析", resumeDetailDTO.getResume().getUserId());
            
            // 使用阻塞式API
            return summarizeResumeClient
                    .prompt()
                    .user(com.caicai.constant.PromptConstant.RESUME_SUMMARY_PROMPT)
                    .user(messageContent)
                    .call()
                    .content();
        } 
        catch (Exception e) {
            log.error("调用AI生成简历摘要时发生错误: {}", e.getMessage(), e);
            return "系统正在升级中，请稍后再试";
        }
    }
    
    /**
     * 流式返回简历摘要
     * 
     * @param resumeDetailDTO
     * @return
     */
    @Override
    public Flux<String> streamResumeSummary(ResumeDetailDTO resumeDetailDTO) {
        if (resumeDetailDTO == null || resumeDetailDTO.getResume() == null) {
            log.warn("简历信息为空，无法进行AI摘要分析");
            return Flux.empty();
        }
        
        try {
            String messageContent = buildResumeMessage(resumeDetailDTO);
            log.info("开始对用户ID: {} 的简历进行流式AI摘要分析", resumeDetailDTO.getResume().getUserId());
            
            // 使用流式API
            return summarizeResumeClient
                    .prompt()
                    .user(com.caicai.constant.PromptConstant.RESUME_SUMMARY_PROMPT)
                    .user(messageContent)
                    .stream()
                    .content()
                    .concatWith(Mono.just("[DONE]"));
        }
        catch (Exception e) {
            log.error("流式简历摘要分析过程中发生错误", e);
            return Flux.empty();
        }
    }
    
    /**
     * 构建简历信息消息
     * 
     * @param resumeDetailDTO
     * @return
     */
    private String buildResumeMessage(ResumeDetailDTO resumeDetailDTO) {
        if (resumeDetailDTO == null) {
            return "无简历信息";
        }

        StringBuilder message = new StringBuilder();
        
        // 用户基本信息
        if (resumeDetailDTO.getUser() != null) {
            message.append("【个人信息】\n");
            message.append("用户名: ").append(resumeDetailDTO.getUser().getUsername()).append("\n");
            if (resumeDetailDTO.getUser().getEmail() != null) {
                message.append("邮箱: ").append(resumeDetailDTO.getUser().getEmail()).append("\n");
            }
            if (resumeDetailDTO.getUser().getPhone() != null) {
                message.append("手机: ").append(resumeDetailDTO.getUser().getPhone()).append("\n");
            }
            message.append("\n");
        }
        
        // 教育经历
        if (resumeDetailDTO.getUserEducationExperiences() != null && !resumeDetailDTO.getUserEducationExperiences().isEmpty()) {
            message.append("【教育经历】\n");
            resumeDetailDTO.getUserEducationExperiences().forEach(edu -> {
                message.append("学校: ").append(edu.getSchoolName()).append("\n");
                if (edu.getMajor() != null) {
                    message.append("专业: ").append(edu.getMajor()).append("\n");
                }
                if (edu.getDegree() != null) {
                    message.append("学历: ").append(edu.getDegree()).append("\n");
                }
                if (edu.getStartTime() != null) {
                    message.append("入学时间: ").append(edu.getStartTime()).append("\n");
                }
                if (edu.getEndTime() != null) {
                    message.append("毕业时间: ").append(edu.getEndTime()).append("\n");
                }
                if (edu.getSchoolExperience() != null) {
                    message.append("在校经历: ").append(edu.getSchoolExperience()).append("\n");
                }
                message.append("\n");
            });
        }
        
        // 工作经历
        if (resumeDetailDTO.getUserWorkExperiences() != null && !resumeDetailDTO.getUserWorkExperiences().isEmpty()) {
            message.append("【工作经历】\n");
            resumeDetailDTO.getUserWorkExperiences().forEach(work -> {
                message.append("公司: ").append(work.getCompanyName()).append("\n");
                if (work.getIndustry() != null) {
                    message.append("行业: ").append(work.getIndustry()).append("\n");
                }
                if (work.getJob() != null) {
                    message.append("职位: ").append(work.getJob()).append("\n");
                }
                if (work.getStartTime() != null) {
                    message.append("开始时间: ").append(work.getStartTime()).append("\n");
                }
                if (work.getEndTime() != null) {
                    message.append("结束时间: ").append(work.getEndTime()).append("\n");
                }
                if (work.getJobContent() != null) {
                    message.append("工作内容: ").append(work.getJobContent()).append("\n");
                }
                message.append("是否实习: ").append(work.getIsInternship() != null && work.getIsInternship() == 1 ? "是" : "否").append("\n");
                message.append("\n");
            });
        }
        
        // 项目经验
        if (resumeDetailDTO.getProjectExperiences() != null && !resumeDetailDTO.getProjectExperiences().isEmpty()) {
            message.append("【项目经验】\n");
            resumeDetailDTO.getProjectExperiences().forEach(project -> {
                message.append("项目名称: ").append(project.getProjectName()).append("\n");
                if (project.getProjectRole() != null) {
                    message.append("担任角色: ").append(project.getProjectRole()).append("\n");
                }
                if (project.getStartTime() != null) {
                    message.append("开始时间: ").append(project.getStartTime()).append("\n");
                }
                if (project.getEndTime() != null) {
                    message.append("结束时间: ").append(project.getEndTime()).append("\n");
                }
                if (project.getProjectDescription() != null) {
                    message.append("项目描述: ").append(project.getProjectDescription()).append("\n");
                }
                message.append("\n");
            });
        }
        
        // 技能标签
        if (resumeDetailDTO.getSkillTags() != null && !resumeDetailDTO.getSkillTags().isEmpty()) {
            message.append("【技能标签】\n");
            resumeDetailDTO.getSkillTags().forEach(skill -> {
                if (skill.getSkillName() != null) {
                    message.append(skill.getSkillName());
                    
                    // 如果有熟练程度信息，添加
                    if (skill.getSkillProficiency() != null) {
                        message.append(" (分类: ").append(skill.getSkillProficiency()).append(")");
                    }
                    
                    message.append("\n");
                }
            });
            message.append("\n");
        }
        
        // 获奖荣誉
        if (resumeDetailDTO.getAwardHonors() != null && !resumeDetailDTO.getAwardHonors().isEmpty()) {
            message.append("【获奖荣誉】\n");
            resumeDetailDTO.getAwardHonors().forEach(award -> {
                message.append("荣誉名称: ").append(award.getAwardHonorName()).append("\n");
                if (award.getObtainDate() != null) {
                    message.append("获得时间: ").append(award.getObtainDate()).append("\n");
                }
                if (award.getAwardHonorDescription() != null) {
                    message.append("荣誉描述: ").append(award.getAwardHonorDescription()).append("\n");
                }
                message.append("\n");
            });
        }
        
        return message.toString();
    }
}
