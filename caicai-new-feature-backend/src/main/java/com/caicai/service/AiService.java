package com.caicai.service;

import com.caicai.result.R;
import reactor.core.publisher.Flux;

/**
 * @description: Ai的业务接口
 * @author: LiWeihang
 * @create: 2025/5/23 9:28
 **/
public interface AiService {

    /**
    @description: stream流输出岗位总结内容
    @author: LiWeihang
    @create: 2025/6/9 10:28
    **/
    Flux<String> streamJobSummary(String jobDesc);

    /**
    @description: 阻塞式输出岗位总结内容
    @author: LiWeihang
    @create: 2025/6/9 10:29
    **/
    String callJobSummary(String jobDesc);
}
