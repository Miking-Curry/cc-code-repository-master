package com.caicai.entity.model.resume;

import lombok.Data;

/**
 * @ClassName: UserJobIntentionModel
 * @Description: AI评分用用户求职意向基本信息
 * @Author: PCT
 * @Date: 2025/6/24 16:58
 * @Version: 1.0
 */

@Data
public class UserJobIntentionModel {
    private Long id;
    private String job;
    private String province;
    private String city;
    private String state;
    private Integer minSalary;
    private Integer maxSalary;
}
