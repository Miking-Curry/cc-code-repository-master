package com.caicai.service;

import com.caicai.entity.vo.PostVO;
import org.springframework.ai.document.Document;
import java.util.List;

/**
 * 岗位向量服务接口
 */
public interface PostVectorService {
    
    /**
     * 加载所有岗位信息到Redis向量数据库
     * @return 加载成功的文档数量
     */
    int loadAllPostsToRedis();
    
    /**
     * 根据关键词搜索相似岗位
     * @param query 搜索关键词
     * @param topK 返回结果数量
     * @return 相似度排序后的文档列表
     */
    List<Document> searchSimilarPosts(String query, int topK);
    
    /**
     * 获取Redis向量数据库中存储的所有岗位信息
     * @param limit 限制返回的数量，默认100条
     * @return 岗位文档列表
     */
    List<Document> listAllPosts(int limit);
    
    /**
     * 根据简历摘要推荐匹配度高的岗位
     * @param resumeSummary AI生成的简历摘要
     * @param count 需要推荐的岗位数量
     * @return 匹配度高的岗位文档列表
     */
    List<Document> recommendJobsByResume(String resumeSummary, int count);
    
    /**
     * 根据简历摘要推荐匹配度高的岗位并保存匹配度信息
     * @param userId 用户ID
     * @param resumeSummary AI生成的简历摘要
     * @param count 需要推荐的岗位数量
     * @return 匹配度高的岗位VO列表，包含岗位详情和匹配度
     */
    List<PostVO> recommendAndSaveMatchings(Long userId, String resumeSummary, int count);
}
