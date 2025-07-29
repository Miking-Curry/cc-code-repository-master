package com.caicai.service;

import com.caicai.entity.dto.PostDTO;
import reactor.core.publisher.Flux;

/**
 * 工作岗位摘要服务接口
 */
public interface JobSummaryService {
    /**
     * 流式返回工作岗位摘要
     * @param postDTO 岗位信息
     * @return 流式返回的岗位摘要
     */
    Flux<String> streamJobSummary(PostDTO postDTO);
    
    /**
     * 阻塞式返回工作岗位摘要
     * @param postDTO 岗位信息
     * @return 岗位摘要
     */
    String generateJobSummary(PostDTO postDTO);
} 