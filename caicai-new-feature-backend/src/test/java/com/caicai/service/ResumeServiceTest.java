package com.caicai.service;

import com.caicai.entity.dto.ResumeDetailDTO;
import com.caicai.entity.pojo.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.format.DateTimeFormatter;

@Slf4j
@SpringBootTest
public class ResumeServiceTest {

    @Autowired
    private ResumeService resumeService;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Test
    public void testGetResumeDetail() {
        // 获取简历详情
        ResumeDetailDTO resumeDetailDTO = resumeService.getResumeDetail(1L);
        
        if (resumeDetailDTO == null) {
            log.info("未找到指定用户的简历信息");
            return;
        }
        
        // 打印简历基础信息
        Resume resume = resumeDetailDTO.getResume();
        log.info("===== 简历基础信息 =====");
        log.info("简历ID: {}", resume.getId());
        log.info("用户ID: {}", resume.getUserId());
        log.info("AI评分: {}", resume.getAiScore());
        log.info("创建时间: {}", resume.getCreatedAt() != null ? resume.getCreatedAt().format(DATE_FORMATTER) : "无");
        log.info("更新时间: {}", resume.getUpdatedAt() != null ? resume.getUpdatedAt().format(DATE_FORMATTER) : "无");
        
        // 打印用户信息
        User user = resumeDetailDTO.getUser();
        if (user != null) {
            log.info("===== 用户信息 =====");
            log.info("用户ID: {}", user.getId());
            log.info("用户名: {}", user.getUsername());
            log.info("邮箱: {}", user.getEmail());
            log.info("电话: {}", user.getPhone());
        }
        
        // 打印技能标签
        log.info("===== 技能标签 =====");
        if (resumeDetailDTO.getSkillTags() != null && !resumeDetailDTO.getSkillTags().isEmpty()) {
            for (UserSkill skillTag : resumeDetailDTO.getSkillTags()) {
                log.info("标签: {}, 熟练程度: {}", skillTag.getSkillName(), skillTag.getSkillProficiency());
            }
        } else {
            log.info("没有技能标签信息");
        }
        
        // 打印项目经验
        log.info("===== 项目经验 =====");
        if (resumeDetailDTO.getProjectExperiences() != null && !resumeDetailDTO.getProjectExperiences().isEmpty()) {
            for (UserProjectExperience project : resumeDetailDTO.getProjectExperiences()) {
                log.info("项目名称: {}", project.getProjectName());
                log.info("项目角色: {}", project.getProjectRole());
                log.info("起止时间: {} 至 {}", 
                    project.getStartTime() != null ? project.getStartTime().format(DATE_FORMATTER) : "无",
                    project.getEndTime() != null ? project.getEndTime().format(DATE_FORMATTER) : "无");
                log.info("项目描述: {}", project.getProjectDescription());
                log.info("--------------------");
            }
        } else {
            log.info("没有项目经验信息");
        }
        
        // 打印工作经历
        log.info("===== 工作经历 =====");
        if (resumeDetailDTO.getUserWorkExperiences() != null && !resumeDetailDTO.getUserWorkExperiences().isEmpty()) {
            for (UserWorkExperience work : resumeDetailDTO.getUserWorkExperiences()) {
                log.info("公司名称: {}", work.getCompanyName());
                log.info("行业: {}", work.getIndustry());
                log.info("职位: {}", work.getJob());
                log.info("是否实习: {}", work.getIsInternship() == 1 ? "是" : "否");
                log.info("起止时间: {} 至 {}", 
                    work.getStartTime() != null ? work.getStartTime().format(DATE_FORMATTER) : "无",
                    work.getEndTime() != null ? work.getEndTime().format(DATE_FORMATTER) : "无");
                log.info("工作内容: {}", work.getJobContent());
                log.info("--------------------");
            }
        } else {
            log.info("没有工作经历信息");
        }
        
        // 打印教育经历
        log.info("===== 教育经历 =====");
        if (resumeDetailDTO.getUserEducationExperiences() != null && !resumeDetailDTO.getUserEducationExperiences().isEmpty()) {
            for (UserEducationExperience edu : resumeDetailDTO.getUserEducationExperiences()) {
                log.info("学校名称: {}", edu.getSchoolName());
                log.info("专业: {}", edu.getMajor());
                log.info("学历: {}", edu.getDegree());
                log.info("是否全日制: {}", edu.getIsFullTime() == 1 ? "是" : "否");
                log.info("起止时间: {} 至 {}",edu.getStartTime(), edu.getEndTime());
                log.info("在校经历: {}", edu.getSchoolExperience());
                log.info("--------------------");
            }
        } else {
            log.info("没有教育经历信息");
        }
        
        // 打印获奖荣誉
        log.info("===== 获奖荣誉 =====");
        if (resumeDetailDTO.getAwardHonors() != null && !resumeDetailDTO.getAwardHonors().isEmpty()) {
            for (AwardHonor award : resumeDetailDTO.getAwardHonors()) {
                log.info("奖项名称: {}", award.getAwardHonorName());
                log.info("获得日期: {}", award.getObtainDate() != null ? award.getObtainDate().format(DATE_FORMATTER) : "无");
                log.info("奖项描述: {}", award.getAwardHonorDescription());
                log.info("--------------------");
            }
        } else {
            log.info("没有获奖荣誉信息");
        }
    }
    
    @Test
    public void testSearch() {
        // 按照用户要求的测试方法
        ResumeDetailDTO resumeDetailDTO = resumeService.getResumeDetail(1L);
        log.info("简历信息: {}", resumeDetailDTO);
        
        // 再次获取简历详情并打印
        resumeDetailDTO = resumeService.getResumeDetail(1L);
        log.info("简历信息(再次获取): {}", resumeDetailDTO);
    }
} 