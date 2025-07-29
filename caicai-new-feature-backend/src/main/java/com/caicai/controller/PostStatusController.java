package com.caicai.controller;

import com.caicai.result.R;
import com.caicai.service.PostStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 岗位状态控制器
 */
@Slf4j
@RestController
@RequestMapping("/post-status")
@RequiredArgsConstructor
public class PostStatusController {

    private final PostStatusService postStatusService;
    
    /**
     * 为所有岗位设置合理的状态值
     * @return 成功设置状态的岗位数量
     */
    @PostMapping("/set-all")
    public R<Integer> setStatusForAllPosts() {
        log.info("接收到为所有岗位设置状态值的请求");
        int count = postStatusService.setStatusForAllPosts();
        return R.success(count, "成功为" + count + "个岗位设置状态值");
    }
} 