package com.caicai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caicai.entity.pojo.UserExtension;

public interface CreditService extends IService<UserExtension> {
    //获取用户积分
    Integer getCreditScore(Long userId);
}
