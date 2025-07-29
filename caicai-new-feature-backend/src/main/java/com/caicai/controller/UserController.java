package com.caicai.controller;

import com.caicai.entity.vo.UserVO;
import com.caicai.result.R;
import com.caicai.service.UserExtensionService;
import com.caicai.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: UserExtension的控制器
 * @author: LiWeihang
 * @create: 2025/5/20 10:40
 **/
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserExtensionService userExtensionService;

    /**
     * @description: 获取用户总积分
     * @author: LiWeihang
     * @create: 2025/5/20 10:44
     **/
    @GetMapping("/point")
    public R<Long> getUserPoint(@RequestAttribute("userId") Long userId) {
        return R.success(userExtensionService.getPointByUserId(userId));
    }

    /**
     * @description: 获取用户信用分
     * @author: LiWeihang
     * @create: 2025/5/20 14:34
     **/
    @GetMapping("/score")
    public R<Integer> getUserCreditScore(@RequestAttribute("userId") Long userId) {
        return R.success(userExtensionService.getCreditScoreByUserId(userId));
    }
    /**
    @description: 获取用户真实姓名
    **/
    @GetMapping("/name")
    public R<String> getUserName(@RequestAttribute("userId") Long userId) {
        return R.success(userExtensionService.getUserNameByUserId(userId));
    }

    /**
     * @param userId:
     * @return R<UserVO>:
     * @Author: Panda
     * @Description: 获取用户手机号
     * @Date: 2025/6/3 9:08
     */
    @GetMapping("/phone")
    public R<UserVO> getUserPhone(@RequestAttribute("userId") Long userId) {
        return R.success(userService.getUserPhone(userId));
    }
}