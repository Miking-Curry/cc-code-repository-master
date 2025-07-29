package com.caicai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.caicai.entity.dto.PostDTO;
import com.caicai.entity.pojo.Post;
import com.caicai.entity.pojo.PostTag;
import com.caicai.entity.pojo.UserPostMatching;
import com.caicai.entity.vo.PostVO;
import com.caicai.mapper.PostMapper;
import com.caicai.service.PostService;
import com.caicai.service.PostTagService;
import com.caicai.service.PostVectorService;
import com.caicai.service.UserPostMatchingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPooled;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostVectorServiceImpl implements PostVectorService {
    private final PostService postService;
    private final PostMapper postMapper;
    private final VectorStore vectorStore;
    private final UserPostMatchingService userPostMatchingService;
    private PostTagService postTagService;
    
    @Autowired
    public PostVectorServiceImpl(
            PostService postService,
            PostMapper postMapper,
            VectorStore vectorStore,
            UserPostMatchingService userPostMatchingService,
            @Lazy PostTagService postTagService) {
        this.postService = postService;
        this.postMapper = postMapper;
        this.vectorStore = vectorStore;
        this.userPostMatchingService = userPostMatchingService;
        this.postTagService = postTagService;
    }
    
    @Override
    @Transactional
    public int loadAllPostsToRedis() {
        int count=0;
        try{


            // 查询所有未删除的岗位
            LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.isNull(Post::getDeletedAt);
            List<Post> posts = postMapper.selectList(queryWrapper);
            log.info("开始加载{}条岗位信息到Redis向量库", posts.size());

            List<Document> documents = new ArrayList<>();
            final int BATCH_SIZE = 10; // 阿里云DashScope批量限制为10

            for (int i = 0; i < posts.size(); i++) {
                Post post = posts.get(i);
                log.debug("处理岗位ID: {}, 名称: {}", post.getId(), post.getName());
                PostDTO postDetail = postService.getPostDetail(post.getId());
                if (postDetail != null) {
                    Document document = createDocumentFromPost(postDetail);
                    if (document != null) {
                        documents.add(document);
                        count++;
                        log.debug("创建文档成功: {}", document.getId());

                        // 当积累了BATCH_SIZE个文档或处理到最后一个岗位时，批量添加到向量库
                        if (documents.size() >= BATCH_SIZE || i == posts.size() - 1) {
                            log.info("批量处理{}个文档到向量库, 当前批次IDs: {}", documents.size(),
                                documents.stream().map(Document::getId).toList());
                            try {
            vectorStore.add(documents);
                                log.info("成功将批次文档添加到向量库");
                            } catch (Exception e) {
                                log.error("添加文档到向量库失败: {}", e.getMessage(), e);
                            }
                            documents.clear(); // 清空列表，准备下一批
                        }
                    } else {
                        log.warn("岗位ID: {}无法创建文档", post.getId());
                    }
                } else {
                    log.warn("无法获取岗位详情, ID: {}", post.getId());
                }
            }

            log.info("成功加载{}条岗位信息到Redis向量库", count);

            // 验证数据是否成功加载
            try {
                SearchRequest request = SearchRequest.builder()
                        .query("") // 空查询会匹配所有文档
                        .topK(5) // 只取几条验证
                        .build();
                List<Document> results = vectorStore.similaritySearch(request);
                log.info("验证Redis向量库中有{}条数据", results.size());
                if (!results.isEmpty()) {
                    log.info("第一条数据ID: {}", results.get(0).getId());
                }
            } catch (Exception e) {
                log.error("验证向量库数据失败: {}", e.getMessage());
            }
        }
        catch (Exception e)
        {
            log.error("加载岗位信息到向量库失败: {}", e.getMessage(), e);
            throw e;
        }
        return count;
    }
    // 将岗位信息转换为Document
    private Document createDocumentFromPost(PostDTO postDetail) {
        if (postDetail == null || postDetail.getPosts() == null) {
            return null;
        }

        Post post = postDetail.getPosts();
        StringBuilder content = new StringBuilder();

        // 构建文档内容
        content.append("职位名称: ").append(post.getName()).append("\n");
        if (StringUtils.hasText(post.getDesc())) {
            content.append("职位描述: ").append(post.getDesc()).append("\n");
        }

        if (postDetail.getCompanies() != null) {
            content.append("公司名称: ").append(postDetail.getCompanies().getName()).append("\n");
            if (StringUtils.hasText(postDetail.getCompanies().getIntroduction())) {
                content.append("公司介绍: ").append(postDetail.getCompanies().getIntroduction()).append("\n");
            }
        }
        
        // 添加岗位标签
        List<PostTag> postTags = postTagService.getTagsByPostId(post.getId());
        if (postTags != null && !postTags.isEmpty()) {
            content.append("职位标签: ");
            List<String> tagNames = postTags.stream()
                .map(PostTag::getName)
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());
            
            content.append(String.join(", ", tagNames)).append("\n");
        }

        // 添加薪资、地点等其他信息...

        // 构建元数据
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("id", post.getId());
        metadata.put("name", post.getName());
        metadata.put("company", postDetail.getCompanies() != null ?
                postDetail.getCompanies().getName() : "");
                
        // 添加标签到元数据
        if (postTags != null && !postTags.isEmpty()) {
            List<String> tagNames = postTags.stream()
                .map(PostTag::getName)
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());
            metadata.put("tags", tagNames);
        }

        // 创建文档
        return new Document(
                "post:" + post.getId(),  // 文档ID
                content.toString(),      // 文档内容
                metadata                 // 元数据
        );
    }

    @Override
    public List<Document> searchSimilarPosts(String query, int topK) {
        log.info("开始搜索与[{}]相似的岗位信息, topK={}", query, topK);
        try {
            // 构建搜索请求
            SearchRequest request = SearchRequest.builder()
                    .query(query)
                    .topK(topK)
                    .build();

            log.info("执行向量搜索请求: {}", request);
            // 执行向量相似度搜索
            List<Document> results = vectorStore.similaritySearch(request);
            log.info("找到{}条与[{}]相似的岗位信息", results.size(), query);
            if (results.isEmpty()) {
                log.warn("未找到与查询匹配的结果，可能Redis向量库为空或未正确初始化");
            } else {
                log.info("第一条结果ID: {}", results.get(0).getId());
            }
            return results;
        } catch (Exception e) {
            log.error("搜索相似岗位时发生错误: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<Document> listAllPosts(int limit) {
        log.info("获取Redis向量数据库中的所有岗位信息, limit={}", limit);
        try {

            // 构建一个空查询请求，用于获取所有数据
            SearchRequest request = SearchRequest.builder()
                    .query("") // 空查询会匹配所有文档
                    .topK(limit) // 限制返回的数量
                    .build();
            log.info("执行获取所有向量数据请求: {}", request);
            // 执行查询
            List<Document> results = vectorStore.similaritySearch(request);
            log.info("从Redis向量数据库中获取到{}条岗位信息", results.size());
            if (results.isEmpty()) {
                log.warn("Redis向量库中没有数据，请先调用loadAllPostsToRedis加载数据");
            }
            return results;
        } catch (Exception e) {
            log.error("获取岗位信息列表时发生错误: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    @Override
    public List<Document> recommendJobsByResume(String resumeSummary, int count) {
        log.info("开始基于简历摘要推荐匹配度高的岗位, count={}", count);
        
        if (!StringUtils.hasText(resumeSummary)) {
            log.warn("简历摘要为空，无法进行推荐");
            return Collections.emptyList();
        }
        
        try {
            // 先获取更多的匹配结果，以便从中随机选择
            int searchTopK = Math.max(count * 3, 10); // 至少获取10个结果或者请求数量的3倍
            
            // 构建搜索请求，使用简历摘要作为查询向量
            SearchRequest request = SearchRequest.builder()
                    .query(resumeSummary)
                    .topK(searchTopK)
                    .build();
            
            log.info("执行基于简历摘要的向量搜索请求");
            // 执行向量相似度搜索
            List<Document> allResults = vectorStore.similaritySearch(request);
            log.info("找到{}条与简历摘要相似的岗位信息", allResults.size());
            
            if (allResults.isEmpty()) {
                log.warn("未找到与简历摘要匹配的结果，可能Redis向量库为空或未正确初始化");
                return Collections.emptyList();
            }
            
            // 如果结果数量不足，直接返回所有结果
            if (allResults.size() <= count) {
                log.info("匹配结果数量不足，返回所有{}条结果", allResults.size());
                return allResults;
            }
            
            // 从相似度较高的前半部分结果中随机选择指定数量的岗位
            List<Document> topHalfResults = allResults.subList(0, Math.min(allResults.size(), searchTopK / 2));
            log.info("从前{}条结果中随机选择{}条", topHalfResults.size(), count);
            
            // 使用随机选择算法
            Random random = new Random();
            List<Document> selectedResults = new ArrayList<>();
            
            // Fisher-Yates洗牌算法，随机选择结果
            List<Document> shuffledResults = new ArrayList<>(topHalfResults);
            for (int i = shuffledResults.size() - 1; i > 0; i--) {
                int j = random.nextInt(i + 1);
                Document temp = shuffledResults.get(i);
                shuffledResults.set(i, shuffledResults.get(j));
                shuffledResults.set(j, temp);
            }
            
            // 选择前count个结果
            selectedResults = shuffledResults.stream()
                    .limit(count)
                    .collect(Collectors.toList());
            
            log.info("成功推荐{}条匹配的岗位信息", selectedResults.size());
            for (int i = 0; i < selectedResults.size(); i++) {
                Document doc = selectedResults.get(i);
                log.debug("推荐岗位 #{}: ID={}, 名称={}", 
                    i+1, doc.getId(), doc.getMetadata().getOrDefault("name", "未知"));
            }
            
            return selectedResults;
        } catch (Exception e) {
            log.error("推荐匹配岗位时发生错误: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PostVO> recommendAndSaveMatchings(Long userId, String resumeSummary, int count) {
        log.info("开始基于简历摘要推荐匹配度高的岗位并保存匹配信息, userId={}, count={}", userId, count);
        
        if (!StringUtils.hasText(resumeSummary)) {
            log.warn("简历摘要为空，无法进行推荐");
            return Collections.emptyList();
        }
        
        try {
            // 先获取更多的匹配结果，以便从中随机选择
            int searchTopK = Math.max(count * 3, 10); // 至少获取10个结果或者请求数量的3倍
            
            // 构建搜索请求，使用简历摘要作为查询向量
            SearchRequest request = SearchRequest.builder()
                    .query(resumeSummary)
                    .topK(searchTopK)
                    .build();
            
            log.info("执行基于简历摘要的向量搜索请求");
            // 执行向量相似度搜索
            List<Document> allResults = vectorStore.similaritySearch(request);
            log.info("找到{}条与简历摘要相似的岗位信息", allResults.size());
            
            if (allResults.isEmpty()) {
                log.warn("未找到与简历摘要匹配的结果，可能Redis向量库为空或未正确初始化");
                return Collections.emptyList();
            }
            
            List<Document> selectedResults;
            
            // 如果结果数量不足，直接返回所有结果
            if (allResults.size() <= count) {
                log.info("匹配结果数量不足，返回所有{}条结果", allResults.size());
                selectedResults = allResults;
            } else {
            // 从相似度较高的前半部分结果中随机选择指定数量的岗位
            List<Document> topHalfResults = allResults.subList(0, Math.min(allResults.size(), searchTopK / 2));
            log.info("从前{}条结果中随机选择{}条", topHalfResults.size(), count);
            
            // 使用随机选择算法
            Random random = new Random();
            
            // Fisher-Yates洗牌算法，随机选择结果
            List<Document> shuffledResults = new ArrayList<>(topHalfResults);
            for (int i = shuffledResults.size() - 1; i > 0; i--) {
                int j = random.nextInt(i + 1);
                Document temp = shuffledResults.get(i);
                shuffledResults.set(i, shuffledResults.get(j));
                shuffledResults.set(j, temp);
            }
            
            // 选择前count个结果
                selectedResults = shuffledResults.stream()
                    .limit(count)
                    .collect(Collectors.toList());
            }
            
            log.info("成功推荐{}条匹配的岗位信息", selectedResults.size());
            
            // 保存匹配度信息
            List<Long> postIds = saveMatchingResults(userId, selectedResults);
            
            if (postIds.isEmpty()) {
                log.warn("没有成功保存的匹配记录，无法返回岗位信息");
                return Collections.emptyList();
            }
            
            // 查询岗位VO信息
            List<PostVO> postVOList = postMapper.getPostVOByIds(postIds, userId);
            log.info("成功查询到{}条岗位VO信息", postVOList.size());
            
            // 确保PostVO对象已经正确处理了状态和标签
            // 由于我们已经修改了PostMapper.xml文件和PostVO类
            // 多状态和多标签现在会通过自定义的setter方法自动解析
            
            return postVOList;
        } catch (Exception e) {
            log.error("推荐匹配岗位并保存匹配信息时发生错误: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * 保存用户与岗位的匹配度信息
     * @param userId 用户ID
     * @param results 匹配的岗位文档列表
     * @return 成功保存的岗位ID列表
     */
    private List<Long> saveMatchingResults(Long userId, List<Document> results) {
        log.info("保存用户与岗位匹配度信息: userId={}, resultCount={}", userId, results.size());
        
        List<UserPostMatching> matchingList = new ArrayList<>();
        List<Long> postIds = new ArrayList<>();
        
        // 计算基准分数，用于归一化匹配度
        double maxScore = results.isEmpty() ? 0 : results.get(0).getScore();
        double minScore = maxScore;
        
        // 找出最小分数，用于计算分数范围
        for (Document doc : results) {
            if (doc.getScore() < minScore) {
                minScore = doc.getScore();
            }
        }
        
        // 分数范围
        double scoreRange = maxScore - minScore;
        
        // 匹配度范围设置
        final int MAX_MATCH_PERCENT = 95; // 最高匹配度
        final int MIN_MATCH_PERCENT = 80; // 最低匹配度，提高到80%
        final int MATCH_RANGE = MAX_MATCH_PERCENT - MIN_MATCH_PERCENT;
        
        for (int i = 0; i < results.size(); i++) {
            Document doc = results.get(i);
            
            // 从文档ID中提取岗位ID（格式为"post:123"）
            String docId = doc.getId();
            Long postId = null;
            
            if (docId.startsWith("post:")) {
                try {
                    postId = Long.parseLong(docId.substring(5));
                } catch (NumberFormatException e) {
                    log.warn("无法从文档ID解析岗位ID: {}", docId);
                    continue;
                }
            } else {
                // 尝试从元数据中获取岗位ID
                Object idObj = doc.getMetadata().get("id");
                if (idObj != null) {
                    if (idObj instanceof Long) {
                        postId = (Long) idObj;
                    } else if (idObj instanceof Number) {
                        postId = ((Number) idObj).longValue();
                    } else if (idObj instanceof String) {
                        try {
                            postId = Long.parseLong((String) idObj);
                        } catch (NumberFormatException e) {
                            log.warn("无法从元数据中解析岗位ID: {}", idObj);
                            continue;
                        }
                    }
                }
            }
            
            if (postId == null) {
                log.warn("无法确定岗位ID，跳过匹配度保存: {}", doc.getId());
                continue;
            }
            
            postIds.add(postId);
            
            // 计算匹配度百分比
            int matchingPercent;
            
            if (i == 0) {
                // 第一个结果给予最高匹配度
                matchingPercent = MAX_MATCH_PERCENT;
            } else if (scoreRange <= 0.0001) {
                // 如果分数范围太小，根据位置递减匹配度
                matchingPercent = MAX_MATCH_PERCENT - (i * 5);
                if (matchingPercent < MIN_MATCH_PERCENT) {
                    matchingPercent = MIN_MATCH_PERCENT;
                }
            } else {
                // 基于分数在范围内的相对位置计算匹配度
                double normalizedScore = (doc.getScore() - minScore) / scoreRange;
                
                // 应用平方根函数使分布更加均匀（较低分数的差异被放大）
                normalizedScore = Math.sqrt(normalizedScore);
                
                // 将归一化分数映射到匹配度范围
                matchingPercent = MIN_MATCH_PERCENT + (int) Math.round(normalizedScore * MATCH_RANGE);
                
                // 为结果添加一些随机性，避免匹配度完全相同
                int randomFactor = new Random().nextInt(5) - 2; // -2到2之间的随机数
                matchingPercent += randomFactor;
            }
            
            // 确保匹配度在合理范围内
            matchingPercent = Math.max(MIN_MATCH_PERCENT, Math.min(MAX_MATCH_PERCENT, matchingPercent));
            
            // 创建匹配记录
            UserPostMatching matching = new UserPostMatching();
            matching.setUserId(userId);
            matching.setPostId(postId);
            matching.setMatchPercent(matchingPercent);
            
            matchingList.add(matching);
            
            log.debug("创建匹配记录: userId={}, postId={}, matchingPercent={}, score={}", 
                userId, postId, matchingPercent, doc.getScore());
        }
        
        // 批量保存匹配记录
        if (!matchingList.isEmpty()) {
            userPostMatchingService.batchSaveMatching(userId, matchingList);
            log.info("成功保存{}条用户与岗位匹配度信息", matchingList.size());
        }
        
        return postIds;
    }
}
