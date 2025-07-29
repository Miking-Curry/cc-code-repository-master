package com.caicai.service;

import com.caicai.entity.pojo.UserExtension;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;

/**
* @author LiWeihang
* @description 针对表【user_extension(用户扩展表)】的数据库操作Service
* @createDate 2025-05-20 09:22:29
*/
public interface UserExtensionService extends IService<UserExtension> {

    /**
     @description: 获取用户总积分
     @author: LiWeihang
     @create: 2025/5/20 10:44
     **/
    Long getPointByUserId(Long userId);

    /**
     @description: 获取用户信用分
     @author: LiWeihang
     @create: 2025/5/20 14:34
     **/
    Integer getCreditScoreByUserId(Long userId);

    /**
    @description: 使用userId获取用户扩展表信息
    @author: LiWeihang
    @create: 2025/6/6 8:55
    **/
    UserExtension getByUserId(Serializable userId);
    
    String  getUserNameByUserId(Long userId);
}
