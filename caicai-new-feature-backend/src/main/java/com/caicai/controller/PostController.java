package com.caicai.controller;

import com.caicai.entity.dto.PostDTO;
import com.caicai.result.R;
import com.caicai.service.JobSummaryService;
import com.caicai.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final JobSummaryService jobSummaryService;
    /**
     * 获取职位详情
     *
     * @param postId 职位ID
     * @return 职位详情
     */
    @RequestMapping("/detail")
    public R<PostDTO> getPostDetail(@RequestParam("postId") Long postId) {
        // 校验postId
        if (postId == null) {
            log.warn("获取职位详情失败: 职位ID为空");
            return R.error("职位ID不能为空");
        }
        return R.success(postService.getPostDetail(postId));
    }
    
    /**
     * 获取职位AI摘要（流式返回）
     *
     * @param postId 职位ID
     * @return 职位AI摘要（流式返回）
     */
    @GetMapping(value = "/summary/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getPostSummaryStream(@RequestParam("postId") Long postId) {
        // 校验postId
        if (postId == null) {
            log.warn("获取职位AI摘要失败: 职位ID为空");
            return Flux.just("职位ID不能为空");
        }
        
        // 获取职位详情
        PostDTO postDTO = postService.getPostDetail(postId);
        if (postDTO == null) {
            log.warn("获取职位AI摘要失败: 职位ID {} 对应的职位不存在", postId);
            return Flux.just("职位不存在");
        }
        
        // 调用AI摘要服务
        log.info("开始生成职位ID: {} 的流式AI摘要", postId);
        return jobSummaryService.streamJobSummary(postDTO);
    }
    
    /**
     * 获取职位AI摘要（阻塞式返回）
     *
     * @param postId 职位ID
     * @return 职位AI摘要
     */
    @GetMapping("/summary")
    public R<String> getPostSummary(@RequestParam("postId") Long postId) {
        // 校验postId
        if (postId == null) {
            log.warn("获取职位AI摘要失败: 职位ID为空");
            return R.error("职位ID不能为空");
        }
        
        // 获取职位详情
        PostDTO postDTO = postService.getPostDetail(postId);
        if (postDTO == null) {
            log.warn("获取职位AI摘要失败: 职位ID {} 对应的职位不存在", postId);
            return R.error("职位不存在");
        }
        
        // 调用AI摘要服务
        log.info("开始生成职位ID: {} 的阻塞式AI摘要", postId);
        String summary = jobSummaryService.generateJobSummary(postDTO);
        return R.success(summary);
    }
}
