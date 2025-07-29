package com.caicai.entity.vo;

import com.caicai.entity.model.TalentRatingResult;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName: TalentRatingVO
 * @Description: 人才评分VO
 * @Author: PCT
 * @Date: 2025/6/23 11:33
 * @Version: 1.0
 */

@Data
public class TalentRatingVO {
    private Long userId;
    private Integer overallScore;
    private String overallLevel;
    private String overallEvaluate;
    private List<TalentRatingResult> dimensionResults;
}
