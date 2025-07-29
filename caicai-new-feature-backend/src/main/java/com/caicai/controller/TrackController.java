package com.caicai.controller;

import com.caicai.entity.dto.JobViewDTO;
import com.caicai.entity.dto.UserKeywordDTO;
import com.caicai.entity.pojo.UserSearchLog;
import com.caicai.result.R;
import com.caicai.service.UserLoginLogService;
import com.caicai.service.UserSearchLogService;
import com.caicai.service.UserViewJobDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * @description: 用户习惯分析数据采集接口
 * @author: LiWeihang
 * @create: 2025/5/21 15:02
 **/
@RestController
@RequestMapping("/track")
@RequiredArgsConstructor
public class TrackController {

    private final UserLoginLogService userLoginLogService;

    private final UserViewJobDetailService userViewJobDetailService;

    private final UserSearchLogService userSearchLogService;

    /**
     * @description: 登录时间数据采集业务，前端传的格式为loginTime=2025-05-21%2010:30:00
     * @author: LiWeihang
     * @create: 2025/5/21 15:51
     **/
    @PostMapping("/login")
    public R<String> trackLogin(@RequestAttribute("userId") Long userId,
                                @RequestParam("loginTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime loginTime) {
        return userLoginLogService.saveLoginLog(userId, loginTime)
                ? R.success("记录成功")
                : R.error("记录失败");
    }

    /**
     * @description: 浏览岗位详情退出后的数据采集接口
     * @author: LiWeihang
     * @create: 2025/5/21 17:39
     **/
    @PostMapping("/job-view")
    public R<String> trackJobView(@RequestAttribute("userId") Long userId,
                                  @RequestBody JobViewDTO jobViewDTO) {

        return userViewJobDetailService.saveJobView(userId, jobViewDTO)
                ? R.success("记录成功")
                : R.error("记录失败");
    }

    /**
    @description: 用户搜索关键词数据采集接口
    @author: LiWeihang
    @create: 2025/6/9 9:51
    **/
    @PostMapping("/user-keyword")
    public R<String> trackKeyword(@RequestAttribute Long userId,
                                  @RequestBody UserKeywordDTO  userKeywordDTO
                                  ){
        return userSearchLogService.saveKeyword(userId, userKeywordDTO)
                ? R.success("记录成功")
                : R.error("记录失败");
    }

}
