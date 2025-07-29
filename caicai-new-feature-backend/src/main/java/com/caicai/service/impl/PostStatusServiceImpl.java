package com.caicai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caicai.entity.pojo.Post;
import com.caicai.entity.pojo.PostStatus;
import com.caicai.enums.PostStatusType;
import com.caicai.mapper.PostMapper;
import com.caicai.mapper.PostStatusMapper;
import com.caicai.service.PostStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 岗位状态服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PostStatusServiceImpl extends ServiceImpl<PostStatusMapper, PostStatus> implements PostStatusService {

    private final PostMapper postMapper;
    private final Random random = new Random();
    
    /**
     * 为所有岗位设置合理的状态值
     * @return 成功设置状态的岗位数量
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int setStatusForAllPosts() {
        log.info("开始为所有岗位设置状态值");
        
        // 查询所有未删除的岗位
        LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.isNull(Post::getDeletedAt);
        List<Post> posts = postMapper.selectList(queryWrapper);
        log.info("找到{}个未删除的岗位", posts.size());
        
        if (posts.isEmpty()) {
            return 0;
        }
        
        // 先删除所有现有的岗位状态记录
        LambdaQueryWrapper<PostStatus> deleteWrapper = new LambdaQueryWrapper<>();
        baseMapper.delete(deleteWrapper);
        log.info("已删除所有现有的岗位状态记录");
        
        // 批量插入新的岗位状态记录
        List<PostStatus> postStatusList = new ArrayList<>(posts.size());
        LocalDateTime now = LocalDateTime.now();
        
        for (Post post : posts) {
            PostStatus postStatus = new PostStatus();
            postStatus.setPostId(post.getId());
            
            // 根据一些规则为岗位分配合理的状态
            Integer status = determineStatus(post);
            postStatus.setStatus(status);
            
            postStatus.setCreatedAt(now);
            postStatus.setUpdatedAt(now);
            
            postStatusList.add(postStatus);
        }
        
        // 批量保存
        saveBatch(postStatusList);
        log.info("成功为{}个岗位设置状态值", postStatusList.size());
        
        return postStatusList.size();
    }
    
    /**
     * 根据岗位信息确定合理的状态值
     * @param post 岗位信息
     * @return 状态值
     */
    private Integer determineStatus(Post post) {
        // 这里可以根据实际业务逻辑来确定岗位状态
        // 例如：根据创建时间、更新时间、岗位类型等
        
        // 简单示例：如果岗位有状态字段，优先使用它
        if (post.getStatus() != null) {
            return post.getStatus();
        }
        
        // 根据创建时间判断
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime createdAt = post.getCreatedAt();
        
        if (createdAt == null) {
            // 如果没有创建时间，随机分配状态
            int rand = random.nextInt(100);
            if (rand < 10) {
                return PostStatusType.URGENT.getCode();      // 10%的概率为急聘
            } else if (rand < 30) {
                return PostStatusType.NEW.getCode();         // 20%的概率为新岗
            } else if (rand < 50) {
                return PostStatusType.RECOMMENDED.getCode(); // 20%的概率为推荐
            } else if (rand < 70) {
                return PostStatusType.PREMIUM.getCode();     // 20%的概率为优选
            } else {
                return PostStatusType.NORMAL.getCode();      // 30%的概率为普通
            }
        }
        
        // 根据创建时间判断
        long daysBetween = java.time.Duration.between(createdAt, now).toDays();
        
        if (daysBetween <= 7) {
            // 7天内的岗位，50%概率为新岗，20%概率为急聘，30%概率为其他
            int rand = random.nextInt(100);
            if (rand < 50) {
                return PostStatusType.NEW.getCode();         // 新岗
            } else if (rand < 70) {
                return PostStatusType.URGENT.getCode();      // 急聘
            } else if (rand < 85) {
                return PostStatusType.RECOMMENDED.getCode(); // 推荐
            } else {
                return PostStatusType.PREMIUM.getCode();     // 优选
            }
        } else if (daysBetween <= 30) {
            // 7-30天的岗位，30%概率为推荐，20%概率为优选，10%概率为急聘，40%概率为普通
            int rand = random.nextInt(100);
            if (rand < 30) {
                return PostStatusType.RECOMMENDED.getCode(); // 推荐
            } else if (rand < 50) {
                return PostStatusType.PREMIUM.getCode();     // 优选
            } else if (rand < 60) {
                return PostStatusType.URGENT.getCode();      // 急聘
            } else {
                return PostStatusType.NORMAL.getCode();      // 普通
            }
        } else {
            // 30天以上的岗位，60%概率为普通，20%概率为优选，20%概率为推荐
            int rand = random.nextInt(100);
            if (rand < 60) {
                return PostStatusType.NORMAL.getCode();      // 普通
            } else if (rand < 80) {
                return PostStatusType.PREMIUM.getCode();     // 优选
            } else {
                return PostStatusType.RECOMMENDED.getCode(); // 推荐
            }
        }
    }
} 