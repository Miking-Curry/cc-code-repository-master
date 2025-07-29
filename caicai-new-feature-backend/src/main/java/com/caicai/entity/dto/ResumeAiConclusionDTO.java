package com.caicai.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 简历AI评价结论DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeAiConclusionDTO {
    /**
     * AI评价结论
     */
    private String conclusion;
    /**
     * AI评价关键词
     */
    private List<String> keywords;
    /**
     * AI评分
     */
    private Integer score;
} 