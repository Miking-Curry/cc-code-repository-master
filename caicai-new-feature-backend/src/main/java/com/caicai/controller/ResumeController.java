package com.caicai.controller;

import com.caicai.entity.dto.ResumeAiConclusionDTO;
import com.caicai.entity.dto.ResumeDetailDTO;
import com.caicai.result.R;
import com.caicai.service.ResumeService;
import com.caicai.service.AiScoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/resume")
@RequiredArgsConstructor
public class ResumeController {
    private final ResumeService resumeService;
    private final AiScoreService aiScoreService;
    
    /**
     * 获取AI评分
     */
    @GetMapping("/AiScore")
    public R<Integer> getAiScore(@RequestAttribute("userId") Long userId) {
        //校验id
        if (userId == null) {
            log.warn("用户 {} 获取AI评分失败: 用户ID为空", userId);
            return R.error("用户ID不能为空");
        }
        return R.success(resumeService.getAiScore(userId));
    }
    
    /**
     * 获取简历详情
     */
    @GetMapping("/detail")
    public R<ResumeDetailDTO> getResumeDetail(@RequestAttribute("userId") Long userId) {
        //校验id
        if (userId == null) {
            log.warn("用户获取简历详情失败: 用户ID为空");
            return R.error("用户ID不能为空");
        }
        
        ResumeDetailDTO resumeDetail = resumeService.getResumeDetail(userId);
        if (resumeDetail == null) {
            log.warn("用户 {} 获取简历详情失败: 未找到简历信息", userId);
            return R.error("未找到简历信息");
        }
        
        log.info("用户 {} 成功获取简历详情", userId);
        return R.success(resumeDetail);
    }
    
    /**
     * AI评分并保存
     */
    @PostMapping("/score")
    public R<Integer> scoreResume(@RequestAttribute("userId") Long userId) {
        //校验id
        if (userId == null) {
            log.warn("用户AI评分失败: 用户ID为空");
            return R.error("用户ID不能为空");
        }
        
        Integer score = resumeService.scoreAndSaveResume(userId);
        if (score == null) {
            log.warn("用户 {} AI评分失败", userId);
            return R.error("AI评分失败，请稍后再试");
        }
        
        log.info("用户 {} AI评分成功: {}", userId, score);
        return R.success(score);
    }
    
    /**
     * 流式AI评分
     */
    @GetMapping(value = "/score/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamScoreResume(@RequestAttribute("userId") Long userId) {
        //校验id
        if (userId == null) {
            log.warn("用户流式AI评分失败: 用户ID为空");
            return Flux.just("错误: 用户ID不能为空");
        }
        
        ResumeDetailDTO resumeDetail = resumeService.getResumeDetail(userId);
        if (resumeDetail == null) {
            log.warn("用户 {} 流式AI评分失败: 未找到简历信息", userId);
            return Flux.just("错误: 未找到简历信息");
        }
        
        log.info("用户 {} 开始流式AI评分", userId);
        return aiScoreService.streamScoreResume(resumeDetail);
    }
    
    /**
     * 流式AI评分并保存
     */
    @GetMapping(value = "/score/stream/save", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamScoreAndSaveResume(@RequestAttribute("userId") Long userId) {
        //校验id
        if (userId == null) {
            log.warn("用户流式AI评分并保存失败: 用户ID为空");
            return Flux.just("错误: 用户ID不能为空");
        }
        
        log.info("用户 {} 开始流式AI评分并保存", userId);
        return resumeService.streamScoreAndSaveResume(userId);
    }
    
    /**
     * 获取AI评价
     */
    @GetMapping("/conclusion")
    public R<String> getResumeConclusion(@RequestAttribute("userId") Long userId) {
        //校验id
        if (userId == null) {
            log.warn("用户获取AI评价失败: 用户ID为空");
            return R.error("用户ID不能为空");
        }
        
        String conclusion = resumeService.getResumeAiConclusion(userId);
        if (conclusion == null) {
            log.warn("用户 {} 获取AI评价失败: 未找到评价信息", userId);
            return R.error("未找到评价信息");
        }
        
        log.info("用户 {} 成功获取AI评价", userId);
        return R.success(conclusion);
    }
    
    /**
     * 生成AI评价并保存
     */
    @PostMapping("/conclusion/generate")
    public R<ResumeAiConclusionDTO> generateResumeConclusion(@RequestAttribute("userId") Long userId) {
        //校验id
        if (userId == null) {
            log.warn("生成AI评价失败: 用户ID为空");
            return R.error("用户ID不能为空");
        }
        ResumeAiConclusionDTO conclusionDTO = resumeService.generateAndSaveResumeConclusion(userId);
        if (conclusionDTO == null || 
            (conclusionDTO.getConclusion() != null && 
             (conclusionDTO.getConclusion().startsWith("生成评价时发生错误") || 
              conclusionDTO.getConclusion().equals("未找到简历信息")))) {
            log.warn("用户 {} 生成AI评价失败: {}", userId, 
                    conclusionDTO != null ? conclusionDTO.getConclusion() : "结果为空");
            return R.error(conclusionDTO != null ? conclusionDTO.getConclusion() : "生成评价失败");
        }
        
        log.info("用户 {} 成功生成AI评价", userId);
        return R.success(conclusionDTO);
    }
    
    /**
     * 获取AI摘要
     */
    @GetMapping("/summary")
    public R<String> getResumeSummary(@RequestAttribute("userId") Long userId) {
        //校验id
        if (userId == null) {
            log.warn("用户获取AI摘要失败: 用户ID为空");
            return R.error("用户ID不能为空");
        }
        
        String summary = resumeService.getResumeAiSummary(userId);
        if (summary == null) {
            log.warn("用户 {} 获取AI摘要失败: 未找到摘要信息", userId);
            return R.error("未找到摘要信息");
        }
        
        log.info("用户 {} 成功获取AI摘要", userId);
        return R.success(summary);
    }
    
    /**
     * 生成AI摘要并保存
     */
    @PostMapping("/summary/generate")
    public R<String> generateResumeSummary(@RequestAttribute("userId") Long userId) {
        //校验id
        if (userId == null) {
            log.warn("生成AI摘要失败: 用户ID为空");
            return R.error("用户ID不能为空");
        }
        
        String summary = resumeService.generateAndSaveResumeSummary(userId);
        if (summary == null || summary.startsWith("生成摘要时发生错误") || summary.equals("未找到简历信息")) {
            log.warn("用户 {} 生成AI摘要失败: {}", userId, summary);
            return R.error(summary);
        }
        
        log.info("用户 {} 成功生成AI摘要", userId);
        return R.success(summary);
    }
    
    /**
     * 流式生成AI摘要
     */
    @GetMapping(value = "/summary/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamResumeSummary(@RequestAttribute("userId") Long userId) {
        //校验id
        if (userId == null) {
            log.warn("用户流式生成AI摘要失败: 用户ID为空");
            return Flux.just("错误: 用户ID不能为空");
        }
        
        log.info("用户 {} 开始流式生成AI摘要", userId);
        return resumeService.streamResumeSummary(userId);
    }
    
    /**
     * 流式生成AI评价并保存
     */
    @GetMapping(value = "/conclusion/stream/generate", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ResumeAiConclusionDTO> streamGenerateResumeConclusion(@RequestAttribute("userId") Long userId) {
        //校验id
        if (userId == null) {
            log.warn("流式生成AI评价失败: 用户ID为空");
            return Flux.just(ResumeAiConclusionDTO.builder()
                    .conclusion("用户ID不能为空")
                    .build());
        }
        
        log.info("用户 {} 开始流式生成AI评价", userId);
        return resumeService.streamGenerateAndSaveResumeConclusion(userId);
    }
}
