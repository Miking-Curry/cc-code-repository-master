package com.caicai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caicai.entity.dto.PostDTO;
import com.caicai.entity.pojo.Post;
import com.caicai.entity.pojo.PostTag;
import com.caicai.mapper.PostMapper;
import com.caicai.mapper.PostTagMapper;
import com.caicai.service.PostService;
import com.caicai.service.PostTagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostTagServiceImpl extends ServiceImpl<PostTagMapper, PostTag> implements PostTagService {

    private final PostTagMapper postTagMapper;
    private final PostMapper postMapper;
    private final PostService postService;
    private final VectorStore vectorStore;
    
    // 常用技能标签库，按照行业类别划分
    private static final Map<String, List<String>> COMMON_SKILL_TAGS = new HashMap<>();
    // 岗位吸引点标签库
    private static final List<String> ATTRACTION_TAGS = Arrays.asList(
            "五险一金", "绩效奖金", "年终奖", "加班费", "股票期权", 
            "弹性工作", "免费班车", "定期体检", "补充医疗", "带薪年假", 
            "员工旅游", "团队氛围好", "领导好", "成长空间大", "晋升快",
            "技术氛围好", "免费零食", "交通补贴", "餐补", "全勤奖",
            "专业培训", "节日礼物");
    
    static {
        // 初始化常用技能标签
        COMMON_SKILL_TAGS.put("技术", Arrays.asList(
                "Java", "Python", "C++", "Go", "JavaScript",
                "React", "Vue.js", "Angular", "Spring", "MySQL",
                "Redis", "MongoDB", "Docker", "Kubernetes", "微服务",
                "Linux", "Git", "CI/CD", "AWS", "Azure",
                "人工智能", "大数据", "云计算", "DevOps", "敏捷开发"));
        
        COMMON_SKILL_TAGS.put("营销", Arrays.asList(
                "内容营销", "SEO", "SEM", "社交媒体", "市场调研",
                "品牌策划", "用户增长", "活动策划", "数据分析", "CRM",
                "渠道管理", "销售策略", "客户关系", "商务谈判", "市场推广"));
        
        COMMON_SKILL_TAGS.put("设计", Arrays.asList(
                "UI设计", "UX设计", "平面设计", "产品设计", "交互设计",
                "Photoshop", "Sketch", "Figma", "插画", "动画",
                "3D建模", "视觉设计", "创意设计", "用户研究", "原型设计"));
        
        COMMON_SKILL_TAGS.put("财务", Arrays.asList(
                "财务分析", "财务规划", "成本核算", "预算管理", "税务筹划",
                "审计", "风险控制", "资金管理", "会计", "财务报表",
                "ERP系统", "投资分析", "财务建模", "成本控制", "合规管理"));
        
        COMMON_SKILL_TAGS.put("人力资源", Arrays.asList(
                "招聘", "培训发展", "绩效管理", "薪酬福利", "员工关系",
                "人才发展", "组织发展", "企业文化", "员工体验", "劳动法规",
                "HRIS系统", "人才测评", "员工激励", "团队建设", "人力资源规划"));
    }

    @Override
    public boolean savePostTag(PostTag postTag) {
        return this.save(postTag);
    }

    @Override
    public boolean batchSavePostTags(List<PostTag> postTags) {
        return this.saveBatch(postTags);
    }

    @Override
    public List<PostTag> getTagsByPostId(Long postId) {
        LambdaQueryWrapper<PostTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostTag::getPostId, postId)
                .isNull(PostTag::getDeletedAt);
        return this.list(queryWrapper);
    }

    @Override
    public boolean deleteTagsByPostId(Long postId) {
        LambdaQueryWrapper<PostTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostTag::getPostId, postId);
        return this.remove(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int generateTagsForAllPosts() {
        log.info("开始为所有岗位生成标签");
        int count = 0;
        int batchSize = 100; // 每批处理的标签数量
        
        try {
            // 查询所有未删除的岗位
            LambdaQueryWrapper<Post> postQueryWrapper = new LambdaQueryWrapper<>();
            postQueryWrapper.isNull(Post::getDeletedAt);
            List<Post> posts = postMapper.selectList(postQueryWrapper);
            log.info("共找到{}个岗位需要生成标签", posts.size());
            
            // 批量处理，减少数据库操作
            List<PostTag> allTags = new ArrayList<>();
            List<Long> processedPostIds = new ArrayList<>();
            
            for (Post post : posts) {
                try {
                    // 先清除原有标签（不在循环中执行删除，而是收集ID后批量删除）
                    processedPostIds.add(post.getId());
                    
                    // 获取岗位详情，包括描述和公司信息
                    PostDTO postDetail = postService.getPostDetail(post.getId());
                    if (postDetail == null) {
                        log.warn("无法获取岗位详情，跳过标签生成, postId={}", post.getId());
                        continue;
                    }
                    
                    // 从向量数据库中获取岗位内容
                    Document postDocument = findPostDocumentById(post.getId());
                    
                    // 生成标签
                    List<String> tags = generateTagsForPost(postDetail, postDocument);
                    if (tags.isEmpty()) {
                        log.warn("无法为岗位生成标签, postId={}", post.getId());
                        continue;
                    }
                    
                    // 确保最多只有三个标签
                    List<String> finalTags = tags.stream().limit(3).collect(Collectors.toList());
                    
                    // 创建标签对象
                    for (String tag : finalTags) {
                        PostTag postTag = new PostTag();
                        postTag.setPostId(post.getId().intValue());
                        postTag.setName(tag);
                        allTags.add(postTag);
                    }
                    
                    count++;
                    
                    // 当积累了足够的标签或处理完所有岗位时，执行批量操作
                    if (allTags.size() >= batchSize || count == posts.size()) {
                        // 批量删除这些岗位的旧标签
                        if (!processedPostIds.isEmpty()) {
                            // 使用 OR 条件批量删除
                            LambdaQueryWrapper<PostTag> deleteWrapper = new LambdaQueryWrapper<>();
                            deleteWrapper.in(PostTag::getPostId, processedPostIds);
                            this.remove(deleteWrapper);
                            log.info("已批量删除{}个岗位的旧标签", processedPostIds.size());
                            processedPostIds.clear();
                        }
                        
                        // 批量插入新标签
                        if (!allTags.isEmpty()) {
                            batchSavePostTags(allTags);
                            log.info("已批量保存{}个岗位标签", allTags.size());
                            allTags.clear();
                        }
                    }
                    
                } catch (Exception e) {
                    log.error("为岗位生成标签时出错, postId={}, error={}", post.getId(), e.getMessage(), e);
                }
            }
            
            // 处理剩余的数据（此处是保险代码，实际上前面的循环结束条件已经确保了批量操作）
            if (!processedPostIds.isEmpty()) {
                LambdaQueryWrapper<PostTag> deleteWrapper = new LambdaQueryWrapper<>();
                deleteWrapper.in(PostTag::getPostId, processedPostIds);
                this.remove(deleteWrapper);
                log.info("已批量删除剩余{}个岗位的旧标签", processedPostIds.size());
            }
            
            if (!allTags.isEmpty()) {
                batchSavePostTags(allTags);
                log.info("已批量保存剩余{}个岗位标签", allTags.size());
            }
            
            log.info("岗位标签生成完成，共处理{}个岗位", count);
            return count;
        } catch (Exception e) {
            log.error("批量生成岗位标签过程中发生错误: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * 根据岗位ID从向量数据库中查找对应的Document
     * @param postId 岗位ID
     * @return 岗位Document，如果找不到则返回null
     */
    private Document findPostDocumentById(Long postId) {
        String documentId = "post:" + postId;
        
        try {
            // 尝试直接获取所有岗位文档
            SearchRequest request = SearchRequest.builder()
                    .query("") // 空查询会匹配所有文档
                    .topK(1000) // 限制返回的数量
                    .build();
                    
            List<Document> allPosts = vectorStore.similaritySearch(request);
            
            Optional<Document> foundDoc = allPosts.stream()
                    .filter(doc -> doc.getId().equals(documentId))
                    .findFirst();
                    
            if (foundDoc.isPresent()) {
                return foundDoc.get();
            }
            
            // 如果没找到，尝试通过岗位名称搜索
            Post post = postMapper.selectById(postId);
            if (post != null && StringUtils.hasText(post.getName())) {
                SearchRequest searchRequest = SearchRequest.builder()
                        .query(post.getName())
                        .topK(10)
                        .build();
                        
                List<Document> searchResults = vectorStore.similaritySearch(searchRequest);
                
                // 在搜索结果中查找精确匹配的岗位
                Optional<Document> exactMatch = searchResults.stream()
                        .filter(doc -> doc.getId().equals(documentId))
                        .findFirst();
                        
                if (exactMatch.isPresent()) {
                    return exactMatch.get();
                }
            }
            
        } catch (Exception e) {
            log.warn("从向量数据库查找岗位文档失败, postId={}, error={}", postId, e.getMessage());
        }
        
        return null;
    }
    
    /**
     * 为岗位生成标签
     * @param postDetail 岗位详情
     * @param postDocument 岗位向量文档
     * @return 标签列表
     */
    private List<String> generateTagsForPost(PostDTO postDetail, Document postDocument) {
        List<String> tags = new ArrayList<>();
        Post post = postDetail.getPosts();
        
        if (post == null) {
            return tags;
        }
        
        try {
            // 1. 根据薪资范围生成标签
            String salaryTag = generateSalaryTag(post.getMinSalary(), post.getMaxSalary());
            if (StringUtils.hasText(salaryTag)) {
                tags.add(salaryTag);
            }
            
            // 2. 从岗位描述中识别技能标签
            String jobDescription = post.getDesc();
            if (StringUtils.hasText(jobDescription)) {
                String skillTag = extractSkillTag(jobDescription);
                if (StringUtils.hasText(skillTag)) {
                    tags.add(skillTag);
                }
            }
            
            // 3. 从向量文档内容中提取吸引点标签
            if (postDocument != null && StringUtils.hasText(postDocument.getText())) {
                String attractionTag = extractAttractionTag(postDocument.getText());
                if (StringUtils.hasText(attractionTag)) {
                    tags.add(attractionTag);
                }
            }
            
            // 4. 根据公司类型或规模添加标签
            if (postDetail.getCompanies() != null) {
                String companyTag = generateCompanyTag(postDetail.getCompanies().getType(), 
                                                      postDetail.getCompanies().getStaffSize());
                if (StringUtils.hasText(companyTag) && tags.size() < 3) {
                    tags.add(companyTag);
                }
            }
            
        } catch (Exception e) {
            log.error("生成岗位标签时发生错误, postId={}, error={}", post.getId(), e.getMessage());
        }
        
        return tags;
    }
    
    /**
     * 生成薪资范围标签
     */
    private String generateSalaryTag(Integer minSalary, Integer maxSalary) {
        if (minSalary == null && maxSalary == null) {
            return null;
        }
        
        int avgSalary = 0;
        if (minSalary != null && maxSalary != null) {
            avgSalary = (minSalary + maxSalary) / 2;
        } else if (minSalary != null) {
            avgSalary = minSalary;
        } else {
            avgSalary = maxSalary;
        }
        
        // 按照月薪范围生成标签
        if (avgSalary < 5000) {
            return "5K以下";
        } else if (avgSalary < 10000) {
            return "5K-10K";
        } else if (avgSalary < 15000) {
            return "10K-15K";
        } else if (avgSalary < 20000) {
            return "15K-20K";
        } else if (avgSalary < 30000) {
            return "20K-30K";
        } else if (avgSalary < 50000) {
            return "30K-50K";
        } else {
            return "50K以上";
        }
    }
    
    /**
     * 从岗位描述中提取技能标签
     */
    private String extractSkillTag(String jobDescription) {
        if (!StringUtils.hasText(jobDescription)) {
            return null;
        }
        
        // 遍历所有行业的技能标签
        for (Map.Entry<String, List<String>> entry : COMMON_SKILL_TAGS.entrySet()) {
            for (String skill : entry.getValue()) {
                if (jobDescription.contains(skill)) {
                    return skill;
                }
            }
        }
        
        // 如果没有匹配到预定义的技能标签，尝试提取关键词
        List<String> keywords = extractKeywords(jobDescription);
        return keywords.isEmpty() ? null : keywords.get(0);
    }
    
    /**
     * 从文本中提取关键词（简化版，实际应用可能需要更复杂的NLP处理）
     */

    private List<String> extractKeywords(String text) {
        List<String> keywords = new ArrayList<>();
        
        // 定义一些常见的技能关键词模式
        Pattern pattern = Pattern.compile("(?:精通|熟悉|掌握|了解|使用|开发|设计)\\s*([\\u4e00-\\u9fa5a-zA-Z0-9+#]+)");
        Matcher matcher = pattern.matcher(text);
        
        while (matcher.find() && keywords.size() < 3) {
            String keyword = matcher.group(1).trim();
            if (keyword.length() > 1 && keyword.length() < 20) {
                keywords.add(keyword);
            }
        }
        
        return keywords;
    }
    
    /**
     * 从文本中提取吸引点标签
     */
    private String extractAttractionTag(String content) {
        if (!StringUtils.hasText(content)) {
            return null;
        }
        
        for (String tag : ATTRACTION_TAGS) {
            if (content.contains(tag)) {
                return tag;
            }
        }
        
        return null;
    }
    
    /**
     * 根据公司类型和规模生成标签
     */
    private String generateCompanyTag(String companyType, Integer staffSize) {
        // 优先使用公司类型
        if (StringUtils.hasText(companyType)) {
            if (companyType.contains("外企") || companyType.contains("外资")) {
                return "外企";
            } else if (companyType.contains("国企")) {
                return "国企";
            } else if (companyType.contains("上市")) {
                return "上市公司";
            } else if (companyType.contains("创业")) {
                return "创业公司";
            }
        }
        
        // 如果没有合适的公司类型，使用公司规模
        if (staffSize != null) {
            if (staffSize < 50) {
                return "小型公司";
            } else if (staffSize < 200) {
                return "中小型公司";
            } else if (staffSize < 1000) {
                return "中型公司";
            } else if (staffSize < 5000) {
                return "大型公司";
            } else {
                return "大型企业";
            }
        }
        
        return null;
    }
    

} 