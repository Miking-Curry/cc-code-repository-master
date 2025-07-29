package com.caicai.controller;

import com.caicai.entity.pojo.PostTag;
import com.caicai.result.R;
import com.caicai.service.PostTagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 岗位标签控制器
 */
@Slf4j
@RestController
@RequestMapping("/postTag")
@RequiredArgsConstructor
public class PostTagController {
    
    private final PostTagService postTagService;
    
    /**
     * 根据岗位ID获取标签
     * @param postId 岗位ID
     * @return 标签列表
     */
    @GetMapping("/{postId}")
    public R<List<PostTag>> getTagsByPostId(@PathVariable Long postId) {
        log.info("获取岗位标签, postId={}", postId);
        List<PostTag> tags = postTagService.getTagsByPostId(postId);
        return R.success(tags);
    }
    
    /**
     * 为所有岗位生成标签
     * 这将使用向量数据库中的信息为每个岗位生成三个标签
     * @return 处理的岗位数量
     */
    @PostMapping("/generate")
    public R<Integer> generateTagsForAllPosts() {
        log.info("开始为所有岗位生成标签");
        int count = postTagService.generateTagsForAllPosts();
        return R.success(count);
    }
    
    /**
     * 删除岗位的所有标签
     * @param postId 岗位ID
     * @return 是否删除成功
     */
    @DeleteMapping("/{postId}")
    public R<Boolean> deleteTagsByPostId(@PathVariable Long postId) {
        log.info("删除岗位标签, postId={}", postId);
        boolean success = postTagService.deleteTagsByPostId(postId);
        return R.success(success);
    }
} 