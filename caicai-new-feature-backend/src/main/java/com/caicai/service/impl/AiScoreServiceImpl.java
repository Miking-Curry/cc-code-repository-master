package com.caicai.service.impl;

import com.caicai.entity.dto.ResumeDetailDTO;
import com.caicai.enums.EducationType;
import com.caicai.service.AiScoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * AI评分服务实现
 * 纯粹的AI评分工具，不做数据库判断和保存
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiScoreServiceImpl implements AiScoreService {

    private final ChatClient scoreResumeClient;

    @Override
    public Flux<String> streamScoreResume(ResumeDetailDTO resumeDetailDTO) {
        if (resumeDetailDTO == null || resumeDetailDTO.getResume() == null) {
            log.warn("简历信息为空，无法进行AI评分");
            return Flux.empty();
        }
        
        try {
            // 构建用户消息内容
            String messageContent = buildResumeMessage(resumeDetailDTO);
            
            log.info("开始对用户ID: {} 的简历进行流式AI评分", resumeDetailDTO.getResume().getUserId());
            
            // 使用流式API获取评分和总结
            return scoreResumeClient
                    .prompt()
                    .user(messageContent)
                    .stream()
                    .content()
                    .concatWith(Mono.just("[DONE]"))
                    .onErrorResume(WebClientResponseException.class, e -> {
                        log.error("AI评分服务请求失败: {}, 状态码: {}", e.getMessage(), e.getStatusCode(), e);
                        return Flux.just("系统正在升级中，请稍后再试", "[DONE]");
                    })
                    .onErrorResume(e -> {
                        log.error("AI评分服务异常: {}", e.getMessage(), e);
                        return Flux.just("系统正在升级中，请稍后再试", "[DONE]");
                    });
        } catch (Exception e) {
            log.error("流式AI评分过程中发生错误", e);
            return Flux.just("错误: AI评分服务异常: " + e.getMessage(), "[DONE]");
        }
    }
    
    /**
     * 使用AI对简历进行评分和总结（阻塞式）
     * @param resumeDetailDTO 简历详情数据
     * @return 评分和总结文本
     */
    @Override
    public String generateResumeConclusion(ResumeDetailDTO resumeDetailDTO) {
        if (resumeDetailDTO == null || resumeDetailDTO.getResume() == null) {
            log.warn("简历信息为空，无法进行AI评分");
            return "无简历信息";
        }
        
        try {
            // 构建用户消息内容
            String messageContent = buildResumeMessage(resumeDetailDTO);
            
            log.info("开始对用户ID: {} 的简历进行阻塞式AI评分", resumeDetailDTO.getResume().getUserId());
            log.info("用户消息 -> [{}]", messageContent);
            
            // 使用阻塞式API获取评分和总结
            return scoreResumeClient
                    .prompt()
                    .user(messageContent)
                    .call()
                    .content();
        } catch (Exception e) {
            log.error("调用AI评分服务时发生错误: {}", e.getMessage(), e);
            return "系统正在升级中，请稍后再试";
        }
    }
    
    /**
     * 构建简历信息消息
     */
    private String buildResumeMessage(ResumeDetailDTO resumeDetail) {
        log.info("构建简历信息 -> [{}]", resumeDetail);
        StringBuilder sb = new StringBuilder();
        
        // 添加用户基本信息
        if (resumeDetail.getUser() != null) {
            sb.append("【简历信息】\n");
            sb.append("姓名：").append(resumeDetail.getUser().getUsername()).append("\n");
            sb.append("联系方式：").append(resumeDetail.getUser().getPhone()).append("\n");
            sb.append("邮箱：").append(resumeDetail.getUser().getEmail()).append("\n\n");
        }
        
        // 添加工作经验
        if (resumeDetail.getUserWorkExperiences() != null && !resumeDetail.getUserWorkExperiences().isEmpty()) {
            sb.append("工作经验：\n");
            for (var exp : resumeDetail.getUserWorkExperiences()) {
                sb.append("- 公司名称：").append(exp.getCompanyName()).append("\n");
                sb.append("  行业：").append(exp.getIndustry()).append("\n");
                sb.append("  职位：").append(exp.getJob()).append("\n");
                sb.append("  工作内容：").append(exp.getJobContent()).append("\n");
                sb.append("  起止时间：").append(exp.getStartTime()).append(" 至 ").append(exp.getEndTime()).append("\n");
                sb.append("  是否实习：").append(exp.getIsInternship() == 1 ? "是" : "否").append("\n\n");
            }
        }
        
        // 添加教育经验
        if (resumeDetail.getUserEducationExperiences() != null && !resumeDetail.getUserEducationExperiences().isEmpty()) {
            sb.append("教育经验：\n");
            for (var edu : resumeDetail.getUserEducationExperiences()) {
                sb.append("- 学校名称：").append(edu.getSchoolName()).append("\n");
                sb.append("  专业：").append(edu.getMajor()).append("\n");
                sb.append("  学历：").append(EducationType.getDescByCode(edu.getDegree())).append("\n");
                sb.append("  是否全日制：").append(edu.getIsFullTime() == 1 ? "是" : "否").append("\n");
                sb.append("  起止时间：").append(edu.getStartTime()).append(" 至 ").append(edu.getEndTime()).append("\n");
                sb.append("  在校经历：").append(edu.getSchoolExperience()).append("\n\n");
            }
        }
        
        // 添加项目经验
        if (resumeDetail.getProjectExperiences() != null && !resumeDetail.getProjectExperiences().isEmpty()) {
            sb.append("项目经验：\n");
            for (var exp : resumeDetail.getProjectExperiences()) {
                sb.append("- 项目名称：").append(exp.getProjectName()).append("\n");
                sb.append("  担任角色：").append(exp.getProjectRole()).append("\n");
                sb.append("  起止时间：").append(exp.getStartTime()).append(" 至 ").append(exp.getEndTime()).append("\n");
                sb.append("  项目描述：").append(exp.getProjectDescription()).append("\n\n");
            }
        }
        
        // 添加技能标签
        if (resumeDetail.getSkillTags() != null && !resumeDetail.getSkillTags().isEmpty()) {
            sb.append("技能标签：\n");
            for (var skill : resumeDetail.getSkillTags()) {
                sb.append("- 技能名称：").append(skill.getSkillName()).append("\n");
                sb.append("  掌握时长：").append(skill.getSkillTime()).append("\n");
                sb.append("  熟练程度：").append(skill.getSkillProficiency()).append("\n\n");
            }
        }
        
        // 添加获奖荣誉
        if (resumeDetail.getAwardHonors() != null && !resumeDetail.getAwardHonors().isEmpty()) {
            sb.append("获奖荣誉：\n");
            for (var award : resumeDetail.getAwardHonors()) {
                sb.append("- 奖项名称：").append(award.getAwardHonorName()).append("\n");
                sb.append("  获奖时间：").append(award.getObtainDate()).append("\n");
                sb.append("  奖项描述：").append(award.getAwardHonorDescription()).append("\n\n");
            }
        }
        
        return sb.toString();
    }
} 