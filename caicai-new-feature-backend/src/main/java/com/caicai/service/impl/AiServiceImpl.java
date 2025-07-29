package com.caicai.service.impl;

import com.caicai.result.R;
import com.caicai.service.AiService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @description: Ai的业务接口实现
 * @author: LiWeihang
 * @create: 2025/5/23 9:28
 **/
@Service
public class AiServiceImpl implements AiService {

    private final ChatClient summarizeJobInfoClient;

    //构造器注入
    public AiServiceImpl(ChatClient summarizeJobInfoClient) {
        this.summarizeJobInfoClient = summarizeJobInfoClient;
    }

    /**
     * @description: stream流输出岗位总结内容
     * @author: LiWeihang
     * @create: 2025/6/9 10:28
     **/
    @Override
    public Flux<String> streamJobSummary(String jobDesc) {
        return summarizeJobInfoClient
                .prompt()
                .user(jobDesc)
                .stream()
                .content()
                .concatWith(Mono.just("[DONE]"))
                ;
    }

    /**
     * @description: 阻塞式输出岗位总结内容
     * @author: LiWeihang
     * @create: 2025/6/9 10:29
     **/
    @Override
    public String callJobSummary(String jobDesc) {
        return summarizeJobInfoClient
                .prompt()
                .user(jobDesc)
                .call()
                .content()
                ;
    }
}
