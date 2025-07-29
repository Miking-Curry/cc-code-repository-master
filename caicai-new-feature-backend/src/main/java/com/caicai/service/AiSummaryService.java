package com.caicai.service;

import com.caicai.entity.dto.ResumeDetailDTO;
import reactor.core.publisher.Flux;

/**
 * 简历摘要服务接口
 */
public interface AiSummaryService {
    /**
     * 生成简历摘要
     * @param resumeDetailDTO
     * @return
     */
    String generateResumeSummary(ResumeDetailDTO resumeDetailDTO);
    
    /**
     * 流式返回简历摘要
     * @param resumeDetailDTO
     * @return
     */
    Flux<String> streamResumeSummary(ResumeDetailDTO resumeDetailDTO);
}
