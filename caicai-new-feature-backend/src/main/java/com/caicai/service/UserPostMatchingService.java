package com.caicai.service;

import com.caicai.entity.pojo.UserPostMatching;
import java.util.List;

public interface UserPostMatchingService {
    /**
     * 保存用户与岗位的匹配度信息
     * @param userId 用户ID
     * @param postId 岗位ID
     * @param matchingPercent 匹配度百分比
     * @return 保存的匹配信息实体
     */
    UserPostMatching saveMatching(Long userId, Long postId, Integer matchingPercent);
    
    /**
     * 批量保存用户与岗位的匹配度信息
     * @param userId 用户ID
     * @param matchingList 匹配信息列表，包含岗位ID和匹配度
     * @return 保存的匹配信息实体列表
     */
    List<UserPostMatching> batchSaveMatching(Long userId, List<UserPostMatching> matchingList);
    
    /**
     * 获取用户的所有岗位匹配信息
     * @param userId 用户ID
     * @return 匹配信息列表
     */
    List<UserPostMatching> getUserMatchings(Long userId);
} 