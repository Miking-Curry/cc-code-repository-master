package com.caicai.controller;

import com.caicai.result.R;
import com.caicai.service.AiService;
import com.caicai.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * @description: Ai的Controller层
 * @author: LiWeihang
 * @create: 2025/5/23 9:29
 **/
@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

//    produces = "text/event-stream" 是SpringMVC中@GetMapping注解的一个属性，
//    它的作用是告诉Spring这个接口返回的 响应内容类型（Content-Type）是text/event-stream，
//    即服务器发送事件（Server-Sent Events, SSE）格式。
    @GetMapping(value = "/job-summary-stream", produces = "text/event-stream")
    public Flux<String> getSteamJobSummary(@RequestParam String jobDesc) {
        return aiService.streamJobSummary(jobDesc);
    }

    @GetMapping(value = "/job-summary-call")
    public R<String> callJobSummary(@RequestParam String jobDesc) {
        return R.success(aiService.callJobSummary(jobDesc));
    }

}
