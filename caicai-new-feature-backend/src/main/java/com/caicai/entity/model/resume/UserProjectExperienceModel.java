package com.caicai.entity.model.resume;

import lombok.Data;

import java.time.LocalDate;

/**
 * @ClassName: UserProjectExperienceModel
 * @Description: AI评分用项目经验基本信息
 * @Author: PCT
 * @Date: 2025/6/27 15:26
 * @Version: 1.0
 */

@Data
public class UserProjectExperienceModel {
    private Long id;
    private String projectName;
    private String projectRole;
    private String projectContent;
    private String projectDescription;
    private String projectResult;
    private String projectTechnology;
    private LocalDate startTime;
    private LocalDate endTime;
}
