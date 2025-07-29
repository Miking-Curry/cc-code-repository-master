package com.caicai.controller;


import com.caicai.result.R;
import com.caicai.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/credit")
public class CreditController {
    private final CreditService creditService;
    /**
     * 获取用户积分信息
     * @param userId 用户ID
     * @return 积分信息
     */
    @GetMapping
    public R<Integer> getUserScore(@RequestAttribute("userId") Long userId) {
        //校验id
        if (userId == null) {
            log.warn("用户 {} 获取积分信息失败: 用户ID为空", userId);
            return R.error("用户ID不能为空");
        }
        return R.success(creditService.getCreditScore(userId));
    }
}

