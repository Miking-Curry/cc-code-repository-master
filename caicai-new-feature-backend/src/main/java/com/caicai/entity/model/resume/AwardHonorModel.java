package com.caicai.entity.model.resume;

import lombok.Data;

import java.time.LocalDate;

/**
 * @ClassName: AwardHonorModel
 * @Description: AI评分用获奖荣誉基本信息
 * @Author: PCT
 * @Date: 2025/6/24 15:48
 * @Version: 1.0
 */

@Data
public class AwardHonorModel {
    private Long id;
    private String awardHonorName;
    private String awardHonorDescription;
    private LocalDate obtainDate;
}
