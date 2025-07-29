package com.caicai.entity.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @ClassName: TalentRatingResult
 * @Description: 人才评分结果模型
 * @Author: PCT
 * @Date: 2025/6/23 11:35
 * @Version: 1.0
 */

@Data
public class TalentRatingResult {
    @JsonProperty("dimension")
    private String dimensionName;
    @JsonProperty("score")
    private Integer score;
    @JsonProperty("level")
    private String level;
    @JsonProperty("reason")
    private String reason;
}
