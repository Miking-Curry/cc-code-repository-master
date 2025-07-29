package com.caicai.entity.dto;

import com.caicai.entity.pojo.*;
import lombok.Data;

import java.util.List;

@Data
public class ResumeDetailDTO {
    //基本简历信息
    private Resume resume;

    //用户基本信息
    private User user;

    //项目经验列表
    private List<UserProjectExperience> projectExperiences;

    //获奖荣誉列表
    private List<AwardHonor> awardHonors;

    //技能标签列表
    private List<UserSkill> skillTags;

    //工作经历列表
    private List<UserWorkExperience> userWorkExperiences;

    //教育经历列表
    private List<UserEducationExperience> userEducationExperiences;
}
