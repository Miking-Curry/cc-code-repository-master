package com.caicai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caicai.entity.pojo.UserPostMatching;
import com.caicai.mapper.UserPostMatchingMapper;
import com.caicai.service.UserPostMatchingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserPostMatchingServiceImpl extends ServiceImpl<UserPostMatchingMapper, UserPostMatching> implements UserPostMatchingService {

    @Override
    public UserPostMatching saveMatching(Long userId, Long postId, Integer matchingPercent) {
        log.info("保存用户与岗位匹配度信息: userId={}, postId={}, matchingPercent={}", userId, postId, matchingPercent);
        
        // 检查是否已存在匹配记录
        LambdaQueryWrapper<UserPostMatching> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPostMatching::getUserId, userId)
                .eq(UserPostMatching::getPostId, postId);
        
        UserPostMatching matching = this.getOne(queryWrapper);
        
        if (matching == null) {
            // 创建新记录
            matching = new UserPostMatching();
            matching.setUserId(userId);
            matching.setPostId(postId);
            matching.setMatchPercent(matchingPercent);
            this.save(matching);
        } else {
            // 更新已有记录
            matching.setMatchPercent(matchingPercent);
            this.updateById(matching);
        }
        
        return matching;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<UserPostMatching> batchSaveMatching(Long userId, List<UserPostMatching> matchingList) {
        log.info("批量保存用户与岗位匹配度信息: userId={}, matchingCount={}", userId, matchingList.size());
        
        List<UserPostMatching> result = new ArrayList<>();
        
        for (UserPostMatching matching : matchingList) {
            // 确保userId一致
            matching.setUserId(userId);
            result.add(saveMatching(userId, matching.getPostId(), matching.getMatchPercent()));
        }
        
        return result;
    }

    @Override
    public List<UserPostMatching> getUserMatchings(Long userId) {
        log.info("获取用户的所有岗位匹配信息: userId={}", userId);
        
        LambdaQueryWrapper<UserPostMatching> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPostMatching::getUserId, userId)
                .orderByDesc(UserPostMatching::getMatchPercent);
        
        return this.list(queryWrapper);
    }
} 