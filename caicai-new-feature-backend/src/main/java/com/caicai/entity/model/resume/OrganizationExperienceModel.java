package com.caicai.entity.model.resume;

import lombok.Data;

import java.time.LocalDate;

/**
 * @ClassName: OrganizationExperienceModel
 * @Description: AI评分用社团/组织经历基本信息
 * @Author: PCT
 * @Date: 2025/6/24 16:05
 * @Version: 1.0
 */

@Data
public class OrganizationExperienceModel {
    private Long id;
    private String organizationName;
    private String position;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
}
