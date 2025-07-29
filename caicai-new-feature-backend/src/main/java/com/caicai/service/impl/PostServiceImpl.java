package com.caicai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caicai.entity.dto.PostDTO;
import com.caicai.entity.pojo.Company;
import com.caicai.entity.pojo.Post;
import com.caicai.mapper.PostMapper;
import com.caicai.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {
    @Override
    public PostDTO getPostDetail(Long postId) {
        // 调用Mapper获取岗位详情
        PostDTO postDetail = new PostDTO();
        // 先判断查询到的东西是否为空
        Post post  = this.getById(postId);
        if(post == null){
            log.error("岗位ID: {} 的岗位信息不存在", postId);
            return null;
        }
        postDetail.setPosts(post);
        Company company=this.baseMapper.getCompanyDetail(postId);
        if(company == null)
        {
            log.error("岗位ID: {} 的公司信息不存在", postId);
            return null;
        }
        postDetail.setCompanies(company);
        return postDetail;
    }
}
