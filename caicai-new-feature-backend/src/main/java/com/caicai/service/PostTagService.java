package com.caicai.service;

import com.caicai.entity.pojo.PostTag;
import java.util.List;

/**
 * 岗位标签服务接口
 */
public interface PostTagService {

    /**
     * 保存岗位标签
     * @param postTag 岗位标签
     * @return 是否保存成功
     */
    boolean savePostTag(PostTag postTag);

    /**
     * 批量保存岗位标签
     * @param postTags 岗位标签列表
     * @return 是否保存成功
     */
    boolean batchSavePostTags(List<PostTag> postTags);
    
    /**
     * 根据岗位ID获取标签列表
     * @param postId 岗位ID
     * @return 标签列表
     */
    List<PostTag> getTagsByPostId(Long postId);
    
    /**
     * 删除岗位的所有标签
     * @param postId 岗位ID
     * @return 是否删除成功
     */
    boolean deleteTagsByPostId(Long postId);
    
    /**
     * 为所有岗位生成标签
     * 这将使用向量数据库中的信息为每个岗位生成三个标签
     * @return 处理的岗位数量
     */
    int generateTagsForAllPosts();
} 