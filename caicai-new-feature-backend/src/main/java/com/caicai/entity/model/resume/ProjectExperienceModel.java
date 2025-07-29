package com.caicai.entity.model.resume;

import lombok.Data;

import java.time.LocalDate;

/**
 * @ClassName: UserProjectExperienceModel
 * @Description: AI评分用项目经验基本信息
 * @Author: PCT
 * @Date: 2025/6/24 17:08
 * @Version: 1.0
 */

@Data
public class ProjectExperienceModel {
    private Long id;
    private String projectName;
    private String role;
    private String description;
    private LocalDate startTime;
    private LocalDate endTime;
}
