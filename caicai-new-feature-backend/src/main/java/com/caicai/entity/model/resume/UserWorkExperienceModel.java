package com.caicai.entity.model.resume;

import lombok.Data;

import java.time.LocalDate;

/**
 * @ClassName: UserWorkExperienceModel
 * @Description: AI评分用用户工作经历基本信息
 * @Author: PCT
 * @Date: 2025/6/24 17:22
 * @Version: 1.0
 */

@Data
public class UserWorkExperienceModel {
    private Long id;
    private String companyName;
    private String job;
    private String industry;
    private String jobContent;
    private String department;
    private Boolean isInternship;
    private LocalDate startTime;
    private LocalDate endTime;
}
