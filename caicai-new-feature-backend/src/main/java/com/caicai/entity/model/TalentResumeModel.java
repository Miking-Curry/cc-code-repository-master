package com.caicai.entity.model;

import com.caicai.entity.model.resume.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @ClassName: TalentResumeModel
 * @Description: 人才简历信息模型
 * @Author: PCT
 * @Date: 2025/6/24 10:58
 * @Version: 1.0
 */

@Data
@Accessors(chain = true)
public class TalentResumeModel {
    //简历基本信息
    private Long resumeId;

    //用户基本信息
    private UserInfoModel userInfo;

    //求职期望
    private List<UserJobIntentionModel> jobIntentions;

    //荣誉奖项集合
    private List<AwardHonorModel> awardHonorList;

    //资格证书集合
    private List<CertificateModel> certificateList;

    //社团组织经历集合
    private List<OrganizationExperienceModel> organizationExperienceList;

    //职业技能标签集合
    private List<UserSkillModel> skillTagList;

    //教育经历集合
    private List<UserEducationExperienceModel> userEducationExperienceList;

    //项目经验集合
    private List<UserProjectExperienceModel> userProjectExperienceList;

    //工作经历集合
    private List<UserWorkExperienceModel> userWorkExperienceList;
}
