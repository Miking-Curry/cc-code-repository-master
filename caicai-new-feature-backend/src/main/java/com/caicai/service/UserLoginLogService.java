package com.caicai.service;

import com.caicai.entity.pojo.UserLoginLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;

/**
* @author ccn00
* @description 针对表【user_login_log(用户登录日志表)】的数据库操作Service
* @createDate 2025-05-21 15:16:32
*/
public interface UserLoginLogService extends IService<UserLoginLog> {

    /**
    @description: 登录时间数据采集业务
    @author: LiWeihang
    @create: 2025/5/21 16:06
    **/
    Boolean saveLoginLog(Long userId, LocalDateTime loginTime);
}
