package com.caicai.service;

import com.caicai.entity.dto.ResumeDetailDTO;
import reactor.core.publisher.Flux;

/**
 * AI评分服务接口
 */
public interface AiScoreService {
    

    /**
     * 使用AI对简历进行流式评分
     * @param resumeDetailDTO 简历详情数据
     * @return 流式评分结果
     */
    Flux<String> streamScoreResume(ResumeDetailDTO resumeDetailDTO);
    
    /**
     * 使用AI对简历进行评分和总结（阻塞式）
     * @param resumeDetailDTO 简历详情数据
     * @return 评分和总结文本
     */
    String generateResumeConclusion(ResumeDetailDTO resumeDetailDTO);
} 