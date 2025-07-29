package com.caicai.service;

import com.caicai.entity.dto.ResumeAiConclusionDTO;
import com.caicai.entity.dto.ResumeDetailDTO;
import com.caicai.entity.model.TalentResumeModel;
import com.caicai.entity.pojo.Resume;
import com.baomidou.mybatisplus.extension.service.IService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
* @author CCN
* @description 针对表【resume(简历表)】的数据库操作Service
* @createDate 2025-06-24 11:05:31
*/
public interface ResumeService extends IService<Resume> {
    /**
     * @param talentId:
     * @return TalentResumeModel:
     * @Author: Panda
     * @Description: 根据人才id获取简历详情
     * @Date: 2025/6/24 14:47
     */
    TalentResumeModel getTalentResumeDetail(Long talentId);

    /**
     * 获取AI评分
     */
    Integer getAiScore(Long userId);
    
    /**
     * 获取简历详情
     */
    ResumeDetailDTO getResumeDetail(Long userId);
    
    /**
     * 使用AI对简历进行评分并保存
     * @param userId 用户ID
     * @return 评分结果
     */
    Integer scoreAndSaveResume(Long userId);
    
    /**
     * 使用流式AI对简历进行评分并保存
     * @param userId 用户ID
     * @return 流式输出内容
     */
    Flux<String> streamScoreAndSaveResume(Long userId);
    
    /**
     * 保存简历评分
     * @param userId 用户ID
     * @param score 评分
     * @return 是否保存成功
     */
    Mono<Boolean> saveResumeScore(Long userId, Integer score);
    
    /**
     * 获取用户简历的AI摘要结论
     * @param userId 用户ID
     * @return AI生成的简历摘要，如果不存在则返回null
     */
    String getResumeAiConclusion(Long userId);
    
    /**
     * 获取用户简历的AI摘要
     * @param userId 用户ID
     * @return AI生成的简历摘要，如果不存在则返回null
     */
    String getResumeAiSummary(Long userId);
    
    /**
     * 使用AI对简历进行评价并保存（不生成分数）
     * @param userId 用户ID
     * @return 生成的AI评价内容和关键词
     */
    ResumeAiConclusionDTO generateAndSaveResumeConclusion(Long userId);
    
    /**
     * 生成并保存简历AI摘要
     * @param userId 用户ID
     * @return AI摘要内容
     */
    String generateAndSaveResumeSummary(Long userId);
    
    /**
     * 流式生成简历AI摘要
     * @param userId 用户ID
     * @return AI摘要流
     */
    Flux<String> streamResumeSummary(Long userId);
    
    /**
     * 流式生成并保存简历AI评价
     * @param userId 用户ID
     * @return AI评价流
     */
    Flux<ResumeAiConclusionDTO> streamGenerateAndSaveResumeConclusion(Long userId);
}
