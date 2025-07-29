package com.caicai.entity.model.resume;

import lombok.Data;

import java.time.LocalDate;

/**
 * @ClassName: UserEducationModel
 * @Description: AI评分用教育经历基本信息
 * @Author: PCT
 * @Date: 2025/6/24 16:28
 * @Version: 1.0
 */

@Data
public class UserEducationExperienceModel {
    private Long id;
    private String schoolName;
    private String major;
    private LocalDate startTime;
    private LocalDate endTime;
    private String schoolExperience;
    private Integer degree;
    private Boolean isFullTime;
}
