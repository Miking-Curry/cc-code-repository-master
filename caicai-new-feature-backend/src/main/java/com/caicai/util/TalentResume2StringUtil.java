package com.caicai.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.caicai.entity.model.TalentResumeModel;
import com.caicai.entity.model.resume.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.StringJoiner;

/**
 * @ClassName: TalentResume2StringUtil
 * @Description: 根据简历信息组织字符串工具类
 * @Author: PCT
 * @Date: 2025/6/25 15:51
 * @Version: 1.0
 */

@Component
public class TalentResume2StringUtil {
    public String getTalentResumeModel2String(TalentResumeModel talentResumeModel) {
        StringBuilder sb = new StringBuilder(
                """
                        简历内容如下：
                        <<<
                        """);

        String nullRateStr = "暂无信息";

        //获取人才信息字符串
        String talentInfoString = getTalentInfo2String(talentResumeModel.getUserInfo());
        if (!nullRateStr.equals(talentInfoString)) {
            sb.append("======= 人才信息 =======")
                    .append("\n")
                    .append(talentInfoString);
        }
        //获取教育经历字符串
        String educationExperienceString = getEducationExperience2String(talentResumeModel.getUserEducationExperienceList());
        if (!nullRateStr.equals(educationExperienceString)) {
            sb.append("======= 教育经历 =======")
                    .append("\n")
                    .append(educationExperienceString);
        }
        //获取求职意向字符串
        String jobIntentionString = getJobIntention2String(talentResumeModel.getJobIntentions());
        if (!nullRateStr.equals(jobIntentionString)) {
            sb.append("======= 求职意向 =======")
                    .append("\n")
                    .append(jobIntentionString);
        }
        //获取技能标签字符串
        String skillTagString = getSkillTag2String(talentResumeModel.getSkillTagList());
        if (!nullRateStr.equals(skillTagString)) {
            sb.append("======= 技能标签 =======")
                    .append("\n")
                    .append(skillTagString);
        }
        //获取项目经验字符串
        String projectExperienceString = getProjectExperience2String(talentResumeModel.getUserProjectExperienceList());
        if (!nullRateStr.equals(projectExperienceString)) {
            sb.append("======= 工作经历 =======")
                    .append("\n")
                    .append(projectExperienceString);
        }
        //获取证书字符串
        String certificateString = getCertificate2String(talentResumeModel.getCertificateList());
        if (!nullRateStr.equals(certificateString)) {
            sb.append("======= 资格证书 =======")
                    .append("\n")
                    .append(certificateString);
        }
        //获取荣誉奖项字符串
        String awardHonorString = getAwardHonor2String(talentResumeModel.getAwardHonorList());
        if (!nullRateStr.equals(awardHonorString)) {
            sb.append("======= 荣誉奖项 =======")
                    .append("\n")
                    .append(awardHonorString);
        }
        //获取社团/组织经历字符串
        String organizationExperienceString = getOrganizationExperience2String(talentResumeModel.getOrganizationExperienceList());
        if (!nullRateStr.equals(organizationExperienceString)) {
            sb.append("======= 社团/组织经历 =======")
                    .append("\n")
                    .append(organizationExperienceString);
        }
        sb.append(">>>");
        return sb.toString();
    }

    /**
     * @param organizationExperienceList:
     * @return String:
     * @Author: Panda
     * @Description: 根据社团/组织经历组织字符串
     * @Date: 2025/6/25 15:36
     */
    private String getOrganizationExperience2String(List<OrganizationExperienceModel> organizationExperienceList) {
        if (CollUtil.isEmpty(organizationExperienceList)) {
            return "暂无信息";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < organizationExperienceList.size(); i++) {
            OrganizationExperienceModel model = organizationExperienceList.get(i);
            sb.append("社团/组织名称：")
                    .append(nonNull(model.getOrganizationName()))
                    .append("\n");
            sb.append("担任职位：")
                    .append(nonNull(model.getPosition()))
                    .append("\n");
            sb.append("起止时间：")
                    .append(formatDate(model.getStartDate(), formatter))
                    .append(" 至 ")
                    .append(formatDate(model.getEndDate(), formatter));
            if (i < organizationExperienceList.size() - 1) {
                sb.append("------------------------");
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * @param awardHonorList:
     * @return String:
     * @Author: Panda
     * @Description: 根据荣誉奖项组织字符串
     * @Date: 2025/6/25 15:33
     */
    private String getAwardHonor2String(List<AwardHonorModel> awardHonorList) {
        if (CollUtil.isEmpty(awardHonorList)) {
            return "暂无信息";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < awardHonorList.size(); i++) {
            AwardHonorModel model = awardHonorList.get(i);
            sb.append("荣誉/奖项名称：")
                    .append(model.getAwardHonorName())
                    .append("\n");
            sb.append("荣誉/奖项描述：")
                    .append(model.getAwardHonorDescription())
                    .append("\n");
            sb.append("获奖时间：")
                    .append(formatDate(model.getObtainDate(), formatter))
                    .append("\n");
            if (i < awardHonorList.size() - 1) {
                sb.append("------------------------");
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * @param certificateList:
     * @return String:
     * @Author: Panda
     * @Description: 根据证书集合组织对应字符串
     * @Date: 2025/6/25 15:27
     */
    private String getCertificate2String(List<CertificateModel> certificateList) {
        if (CollUtil.isEmpty(certificateList)) {
            return "暂无信息";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < certificateList.size(); i++) {
            CertificateModel model = certificateList.get(i);
            sb.append("证书名称：")
                    .append(nonNull(model.getCertificateName()))
                    .append("\n");
            sb.append("颁发日期：")
                    .append(formatDate(model.getIssueDate(), formatter))
                    .append("\n");
            if (i < certificateList.size() - 1) {
                sb.append("------------------------");
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * @param userProjectExperienceList:
     * @return String:
     * @Author: Panda
     * @Description: 根据项目经验集合组织字符串
     * @Date: 2025/6/25 15:20
     */
    private String getProjectExperience2String(List<UserProjectExperienceModel> userProjectExperienceList) {
        if (CollUtil.isEmpty(userProjectExperienceList)) {
            return "暂无信息";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < userProjectExperienceList.size(); i++) {
            UserProjectExperienceModel model = userProjectExperienceList.get(i);
            sb.append("项目名称：")
                    .append(nonNull(model.getProjectName()))
                    .append("\n");
            sb.append("担任角色：")
                    .append(nonNull(model.getProjectRole()))
                    .append("\n");
            sb.append("项目内容：")
                    .append(nonNull(model.getProjectContent()))
                    .append("\n");
            sb.append("项目描述：")
                    .append(nonNull(model.getProjectDescription()))
                    .append("\n");
            sb.append("项目成果：")
                    .append(nonNull(model.getProjectResult()))
                    .append("\n");
            sb.append("项目技术：")
                    .append(nonNull(model.getProjectTechnology()))
                    .append("\n");
            sb.append("起止时间：")
                    .append(formatDate(model.getStartTime(), formatter))
                    .append(" 至 ")
                    .append(formatDate(model.getEndTime(), formatter))
                    .append("\n");
            if (i < userProjectExperienceList.size() - 1) {
                sb.append("------------------------");
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * @param skillTagList:
     * @return String:
     * @Author: Panda
     * @Description: 根据技能标签集合组织字符串
     * @Date: 2025/6/25 14:53
     */
    private String getSkillTag2String(List<UserSkillModel> skillTagList) {
        if (CollUtil.isEmpty(skillTagList)) {
            return "暂无信息";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < skillTagList.size(); i++) {
            UserSkillModel model = skillTagList.get(i);
            sb.append("技能名称：")
                    .append(nonNull(model.getSkillName()))
                    .append("\n");
            sb.append("掌握时长：")
                    .append(nonNull(model.getSkillTime()))
                    .append("\n");
            sb.append("熟练程度：")
                    .append(nonNull(model.getSkillProficiency()))
                    .append("\n");
            if (i < skillTagList.size() - 1) {
                sb.append("------------------------");
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * @param userJobIntentions:
     * @return String:
     * @Author: Panda
     * @Description: 根据求职意向对象组织字符串
     * @Date: 2025/6/25 14:33
     */
    private String getJobIntention2String(List<UserJobIntentionModel> userJobIntentions) {
        if (userJobIntentions == null || CollUtil.isEmpty(userJobIntentions)) {
            return "暂无信息";
        }
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < userJobIntentions.size(); i++) {
            sb.append("求职意向 [")
                    .append(i)
                    .append("]")
                    .append("\n");

            UserJobIntentionModel userJobIntention = userJobIntentions.get(i);

            if (StrUtil.isNotBlank(userJobIntention.getJob())) {
                sb.append("意向岗位：")
                        .append(userJobIntention.getJob())
                        .append("\n");
            }

            String location = formatLocation(
                    userJobIntention.getProvince(),
                    userJobIntention.getCity(),
                    userJobIntention.getState()
            );

            if (StrUtil.isNotBlank(location)) {
                sb.append("意向城市：")
                        .append(location)
                        .append("\n");
            }

            String minSalary = userJobIntention.getMinSalary().toString();
            String maxSalary = userJobIntention.getMaxSalary().toString();
            sb.append("期望薪资：");
            if (StrUtil.isNotBlank(minSalary) && StrUtil.isNotBlank(maxSalary)) {
                sb.append(minSalary)
                        .append(" 至 ")
                        .append(maxSalary)
                        .append("\n");
            } else if (StrUtil.isNotBlank(minSalary)) {
                sb.append(minSalary)
                        .append("起")
                        .append("\n");
            } else if (StrUtil.isNotBlank(maxSalary)) {
                sb.append("最高")
                        .append(maxSalary)
                        .append("\n");
            } else {
                sb.append("面议")
                        .append("\n");
            }


            if (i < userJobIntentions.size() - 1) {
                sb.append("------------------------");
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    /**
     * @param province:
     * @param city:
     * @param state:
     * @return String:
     * @Author: Panda
     * @Description: 处理省市区
     * @Date: 2025/6/26 11:02
     */
    private String formatLocation(String province, String city, String state) {
        StringJoiner sj = new StringJoiner(" ");
        if (StrUtil.isNotBlank(province)) {
            sj.add(province);
        }
        if (StrUtil.isNotBlank(city)) {
            sj.add(city);
        }
        if (StrUtil.isNotBlank(state)) {
            sj.add(state);
        }
        return sj.toString();
    }

    /**
     * @param educationExperienceList:
     * @return String:
     * @Author: Panda
     * @Description: 根据教育经历集合组织字符串
     * @Date: 2025/6/25 14:25
     */
    private String getEducationExperience2String(List<UserEducationExperienceModel> educationExperienceList) {
        if (CollUtil.isEmpty(educationExperienceList)) {
            return "暂无信息";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < educationExperienceList.size(); i++) {
            UserEducationExperienceModel model = educationExperienceList.get(i);
            sb.append("学校名称：")
                    .append(nonNull(model.getSchoolName()))
                    .append("\n");
            sb.append("专业名称：")
                    .append(nonNull(model.getMajor()))
                    .append("\n");
            sb.append("学历：")
                    .append(getDegree(model.getDegree()))
                    .append("\n");
            sb.append("学制：")
                    .append(getDuration(model.getIsFullTime()))
                    .append("\n");
            sb.append("入学时间：")
                    .append(formatDate(model.getStartTime(), formatter))
                    .append("\n");
            sb.append("毕业时间：")
                    .append(formatDate(model.getEndTime(), formatter))
                    .append("\n");
            sb.append("在校经历：")
                    .append(nonNull(model.getSchoolExperience()))
                    .append("\n");
            if (i < educationExperienceList.size() - 1) {
                sb.append("------------------------");
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * @param userInfoModel:
     * @return String:
     * @Author: Panda
     * @Description: 获取人才信息字符串
     * @Date: 2025/6/25 10:45
     */
    private String getTalentInfo2String(UserInfoModel userInfoModel) {
        if (userInfoModel == null) {
            return "暂无信息";
        }
        StringBuilder sb = new StringBuilder();
        String gender = userInfoModel.getGender();
        if (StrUtil.isNotBlank(gender)) {
            sb.append("性别：")
                    .append(nonNull(gender))
                    .append("\n");
        }

        LocalDate birthday = userInfoModel.getBirthday();
        int age;
        if (birthday != null) {
            age = Period.between(birthday, LocalDate.now()).getYears();
            sb.append("年龄：")
                    .append(nonNull("" + age))
                    .append("\n");
        }

        return sb.toString();
    }

    /**
     * @param value:
     * @return String:
     * @Author: Panda
     * @Description: 判断字符串是否为空
     * @Date: 2025/6/25 14:26
     */
    private String nonNull(String value) {
        return (value == null || value.trim().isEmpty()) ? "暂无信息" : value;
    }

    /**
     * @param date:
     * @param formatter:
     * @return String:
     * @Author: Panda
     * @Description: 格式化时间对象
     * @Date: 2025/6/25 14:26
     */
    private String formatDate(LocalDate date, DateTimeFormatter formatter) {
        if (date == null) {
            return "暂无信息";
        }
        LocalDateTime localDateTime = date.atStartOfDay();
        return localDateTime.format(formatter);
    }

    /**
     * @param degree:
     * @return String:
     * @Author: Panda
     * @Description: 根据学历编码获取字符串
     * @Date: 2025/6/25 14:26
     */
    private String getDegree(Integer degree) {
        if (degree == null) {
            return "暂无信息";
        }
        return switch (degree) {
            case 1 -> "小学";
            case 2 -> "初中";
            case 3 -> "中专";
            case 4 -> "高中";
            case 5 -> "专科";
            case 6 -> "本科";
            case 7 -> "硕士";
            case 8 -> "博士";
            default -> "暂无信息";
        };
    }

    /**
     * @param isFullTime:
     * @return String:
     * @Author: Panda
     * @Description: 根据学制编码获取字符串
     * @Date: 2025/6/25 14:26
     */
    private String getDuration(Boolean isFullTime) {
        if (isFullTime == null) {
            return "暂无信息";
        }
        return isFullTime ? "全日制" : "非全日制";
    }
}
