package com.caicai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.caicai.entity.dto.PostDTO;
import com.caicai.entity.pojo.Company;
import com.caicai.entity.pojo.Post;
import com.caicai.entity.vo.PostVO;

import java.util.List;

public interface PostMapper extends BaseMapper<Post> {
    /**
     * 通过职位id获取公司详情信息
     *
     * @param
     * @return
     */
    Company getCompanyDetail(Long postId);
    
    /**
     * 根据岗位ID获取岗位VO，包含公司信息、岗位状态和匹配度
     *
     * @param postId 岗位ID
     * @param userId 用户ID (用于获取匹配度)
     * @return 岗位VO
     */
    PostVO getPostVOById(Long postId, Long userId);
    
    /**
     * 批量根据岗位ID获取岗位VO列表
     *
     * @param postIds 岗位ID列表
     * @param userId 用户ID (用于获取匹配度)
     * @return 岗位VO列表
     */
    List<PostVO> getPostVOByIds(List<Long> postIds, Long userId);
}
