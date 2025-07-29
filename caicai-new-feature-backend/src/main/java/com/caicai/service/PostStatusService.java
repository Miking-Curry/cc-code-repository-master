package com.caicai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caicai.entity.pojo.PostStatus;

/**
 * 岗位状态服务接口
 */
public interface PostStatusService extends IService<PostStatus> {
    
    /**
     * 为所有岗位设置合理的状态值
     * @return 成功设置状态的岗位数量
     */
    int setStatusForAllPosts();
} 