package com.caicai.controller;

import com.caicai.entity.pojo.UserPostMatching;
import com.caicai.entity.vo.PostVO;
import com.caicai.result.R;
import com.caicai.service.PostVectorService;
import com.caicai.service.ResumeService;
import com.caicai.service.UserPostMatchingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/postVector")
@RequiredArgsConstructor
public class PostVectorController {
    private final PostVectorService postVectorService;
    private final ResumeService resumeService;
    private final UserPostMatchingService userPostMatchingService;

    /**
     * 加载所有岗位信息到Redis向量数据库
     * @return 加载成功的文档数量
     */
    @PostMapping
    public R<Integer> loadAllPostsToRedis() {
        int count = postVectorService.loadAllPostsToRedis();
        return R.success(count);
    }

    /**
     * 根据关键词搜索相似岗位
     * @param query 搜索关键词
     * @param topK 返回结果数量
     * @return 相似度排序后的文档列表
     */
    @GetMapping("/search")
    public R<List<Document>> searchSimilarPosts(
            @RequestParam String query,
            @RequestParam(defaultValue = "5") int topK) {
        log.info("接收到搜索请求, query={}, topK={}", query, topK);
        List<Document> results = postVectorService.searchSimilarPosts(query, topK);
        return R.success(results);
    }

    /**
     * 获取Redis向量数据库中存储的所有岗位信息
     * @param limit 限制返回的数量，默认100条
     * @return 岗位文档列表
     */
    @GetMapping("/list")
    public R<List<Document>> listAllPosts(
            @RequestParam(defaultValue = "100") int limit) {
        log.info("接收到获取所有岗位信息请求, limit={}", limit);
        List<Document> results = postVectorService.listAllPosts(limit);
        return R.success(results);
    }
    
    /**
     * 根据用户简历摘要推荐匹配度高的岗位
     * @param userId 用户ID
     * @param count 推荐岗位数量，默认3条
     * @return 推荐的岗位列表
     */
//    @GetMapping("/recommend/{userId}")
//    public R<List<Document>> recommendJobsByResume(
//            @PathVariable Long userId,
//            @RequestParam(defaultValue = "3") int count) {
//        log.info("接收到基于简历推荐岗位请求, userId={}, count={}", userId, count);
//
//        // 获取用户简历摘要
//        String resumeSummary = resumeService.getResumeAiConclusion(userId);
//        if (!StringUtils.hasText(resumeSummary)) {
//            log.warn("用户ID: {} 的简历摘要为空，无法进行推荐", userId);
//            return R.error("无法获取简历摘要信息，请先完善简历");
//        }
//
//        // 基于简历摘要推荐岗位
//        List<Document> results = postVectorService.recommendJobsByResume(resumeSummary, count);
//        if (results.isEmpty()) {
//            return R.error("未找到匹配的岗位推荐");
//        }
//
//        return R.success(results);
//    }
    
    /**
     * 根据用户简历AI摘要推荐匹配度高的岗位
     * @param userId 用户ID
     * @param count 推荐岗位数量，默认3条
     * @return 推荐的岗位列表
     */
    @GetMapping
    public R<List<Document>> recommendJobsBySummary(
            @RequestAttribute("userId") Long userId,
            @RequestParam(defaultValue = "3") int count) {
        log.info("接收到基于简历AI摘要推荐岗位请求, userId={}, count={}", userId, count);
        
        // 获取用户简历AI摘要
        String resumeAiSummary = resumeService.getResumeAiSummary(userId);
        if (!StringUtils.hasText(resumeAiSummary)) {
            log.warn("用户ID: {} 的简历AI摘要为空，无法进行推荐", userId);
            return R.error("无法获取简历AI摘要信息，请先生成简历AI摘要");
        }
        
        // 基于简历AI摘要推荐岗位
        List<Document> results = postVectorService.recommendJobsByResume(resumeAiSummary, count);
        if (results.isEmpty()) {
            return R.error("未找到匹配的岗位推荐");
        }
        
        return R.success(results);
    }

    /**
     * 根据用户简历AI摘要推荐匹配度高的岗位并保存匹配度信息
     * @param userId 用户ID
     * @param count 推荐岗位数量，默认3条
     * @return 推荐的岗位VO列表，包含岗位详情和匹配度
     */
    @GetMapping("/recommend/save")
    public R<List<PostVO>> recommendAndSaveMatchings(
            @RequestAttribute("userId") Long userId,
            @RequestParam(defaultValue = "3") int count) {
        log.info("接收到基于简历AI摘要推荐岗位并保存匹配度信息请求, userId={}, count={}", userId, count);
        
        // 每次都重新生成简历AI摘要，而不是从数据库获取
        String resumeAiSummary = resumeService.generateAndSaveResumeSummary(userId);
        if (!StringUtils.hasText(resumeAiSummary)) {
            log.warn("用户ID: {} 的简历AI摘要生成失败，无法进行推荐", userId);
            return R.error("生成简历AI摘要失败，请稍后再试");
        }
        
        // 基于简历AI摘要推荐岗位并保存匹配度
        List<PostVO> results = postVectorService.recommendAndSaveMatchings(userId, resumeAiSummary, count);
        if (results.isEmpty()) {
            return R.error("未找到匹配的岗位推荐");
        }
        
        return R.success(results);
    }
    
    /**
     * 获取用户的岗位匹配度信息
     * @param userId 用户ID
     * @return 用户的岗位匹配度信息列表
     */
    @GetMapping("/matchings")
    public R<List<UserPostMatching>> getUserMatchings(
            @RequestAttribute("userId") Long userId) {
        log.info("接收到获取用户岗位匹配度信息请求, userId={}", userId);
        
        List<UserPostMatching> matchings = userPostMatchingService.getUserMatchings(userId);
        return R.success(matchings);
    }
}
