package com.caicai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caicai.entity.dto.PostDTO;
import com.caicai.entity.pojo.Post;

public interface PostService extends IService<Post> {
    /**
     * 获取职位详情以及公司详情
     * @param postId
     * @return
     */
    PostDTO getPostDetail(Long postId);
}
