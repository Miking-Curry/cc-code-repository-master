package com.caicai.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.caicai.entity.model.TalentRatingResult;
import com.caicai.entity.model.TalentResumeModel;
import com.caicai.entity.pojo.ResumeEvaluation;
import com.caicai.entity.pojo.ResumeEvaluationDimension;
import com.caicai.entity.vo.TalentRatingVO;
import com.caicai.exception.BusinessException;
import com.caicai.mapper.*;
import com.caicai.service.ResumeEvaluationDimensionService;
import com.caicai.service.ResumeEvaluationService;
import com.caicai.service.ResumeService;
import com.caicai.service.TalentRatingService;
import com.caicai.util.TalentResume2StringUtil;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: TalentRatingServiceImpl
 * @Description: 人才评分
 * @Author: PCT
 * @Date: 2025/6/23 11:14
 * @Version: 1.0
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class TalentRatingServiceImpl implements TalentRatingService {
    @Resource(name = "talentRatingClient")
    private final ChatClient talentRatingClient;
    private final ResumeService resumeService;
    private final TalentResume2StringUtil talentResume2StringUtil;
    private final ResumeEvaluationService resumeEvaluationService;
    private final ResumeEvaluationDimensionService resumeEvaluationDimensionService;

    /**
     * @param userId:
     * @return TalentRatingVO:
     * @Author: Panda
     * @Description: 根据人才简历等信息通过大模型计算评分
     * @Date: 2025/6/26 11:36
     */
    @Override
    public TalentRatingVO talentRating(Long userId) {
        log.info("========开始获取简历字符串========");

        TalentResumeModel talentResumeDetail = resumeService.getTalentResumeDetail(userId);
        String talentResumeString = talentResume2StringUtil.getTalentResumeModel2String(talentResumeDetail);
        System.out.println("简历字符串 -> " + talentResumeString);

        log.info("========简历字符串获取完成========");

        log.info("========开始人才评分========");
        String content = null;
        try {
            content = talentRatingClient.prompt()
                    .user(talentResumeString)
                    .call()
                    .content();
        } catch (Exception e) {
            log.error("》》》》》获取人才评分出现异常《《《《《 异常信息 -> [{}]", e.getMessage());
        }

        if(content == null || content.isEmpty()) {
            log.error("》》》》》获取人才评分出现异常《《《《《");
            throw new BusinessException(HttpStatus.BAD_REQUEST.value(), "获取人才评分出现异常，请稍后重试");
        }

        content = content.replaceAll("```", "").replaceAll("json", "");
        System.out.println(content);

        JSONObject entries = JSONUtil.parseObj(content);
        Integer overallScore = entries.getInt("overallScore");
        String overallLevel = entries.getStr("overallLevel");
        String overallEvaluate = entries.getStr("overallEvaluate");
        JSONArray dimensions = entries.getJSONArray("dimensions");

        List<TalentRatingResult> dimensionResults = dimensions.stream()
                .map(JSONObject::new)
                .map(dimension -> {
                    TalentRatingResult result = new TalentRatingResult();
                    result.setDimensionName(dimension.getStr("dimension"));
                    result.setScore(dimension.getInt("score"));
                    result.setLevel(dimension.getStr("level"));
                    result.setReason(dimension.getStr("reason"));
                    return result;
                })
                .toList();

        //持久化存储评分结果
        ResumeEvaluation resumeEvaluation = new ResumeEvaluation();
        resumeEvaluation.setUserId(userId);
        resumeEvaluation.setResumeId(talentResumeDetail.getResumeId());
        resumeEvaluation.setOverallScore(overallScore);
        resumeEvaluation.setOverallLevel(overallLevel);
        resumeEvaluation.setOverallEvaluate(overallEvaluate);
        resumeEvaluation.setRawJson(content);
        resumeEvaluation = this.resumeEvaluationService.saveResumeEvaluation(resumeEvaluation);

        //持久化存储各维度评分结果
        //获取总评记录id
        Long resumeEvaluationId = resumeEvaluation.getId();
        List<ResumeEvaluationDimension> resumeEvaluationDimensionList = dimensionResults.stream()
                .map(dimensionResult -> {
                    ResumeEvaluationDimension resumeEvaluationDimension = new ResumeEvaluationDimension();
                    resumeEvaluationDimension.setEvaluationId(resumeEvaluationId);
                    resumeEvaluationDimension.setDimension(dimensionResult.getDimensionName());
                    resumeEvaluationDimension.setScore(dimensionResult.getScore());
                    resumeEvaluationDimension.setLevel(dimensionResult.getLevel());
                    resumeEvaluationDimension.setReason(dimensionResult.getReason());
                    return resumeEvaluationDimension;
                })
                .toList();
        this.resumeEvaluationDimensionService.saveResumeEvaluationDimension(resumeEvaluationDimensionList);

        TalentRatingVO talentRatingVO =
                buildTalentRatingVO(userId, overallScore, overallLevel, overallEvaluate, dimensionResults);

        log.info("========人才评分完成========");
        return talentRatingVO;
    }

    /**
     * @param userId:
     * @return TalentRatingVO:
     * @Author: Panda
     * @Description: 通过userId查询数据库中的人才评分
     * @Date: 2025/6/26 11:52
     */
    @Override
    public TalentRatingVO getRating(Long userId) {
        //查询人才评分总评记录
        ResumeEvaluation evaluationResult = this.resumeEvaluationService.lambdaQuery()
                .eq(ResumeEvaluation::getUserId, userId)
                .one();

        //评分记录不存在
        if (evaluationResult == null) {
            log.error("当前用户不存在已完成的评分记录，userId -> [{}]", userId);
            throw new BusinessException(HttpStatus.BAD_REQUEST.value(), "当前用户不存在已完成的评分记录，请先开始评分");
        }

        //评分记录存在->查询各维度评分记录
        Long evaluationId = evaluationResult.getId();
        List<ResumeEvaluationDimension> dimensionEvaluationList = this.resumeEvaluationDimensionService.lambdaQuery()
                .eq(ResumeEvaluationDimension::getEvaluationId, evaluationId)
                .list();

        List<TalentRatingResult> dimensionResultList = dimensionEvaluationList.stream()
                .map(dimensionEvaluation -> {
                    TalentRatingResult result = new TalentRatingResult();
                    result.setDimensionName(dimensionEvaluation.getDimension());
                    result.setScore(dimensionEvaluation.getScore());
                    result.setLevel(dimensionEvaluation.getLevel());
                    result.setReason(dimensionEvaluation.getReason());
                    return result;
                })
                .toList();

        return buildTalentRatingVO(
                userId,
                evaluationResult.getOverallScore(),
                evaluationResult.getOverallLevel(),
                evaluationResult.getOverallEvaluate(),
                dimensionResultList
        );
    }


    private TalentRatingVO buildTalentRatingVO(
            Long userId, Integer overallScore, String overallLevel,
            String overallEvaluate, List<TalentRatingResult> dimensionResults ) {
        TalentRatingVO talentRatingVO = new TalentRatingVO();
        talentRatingVO.setUserId(userId);
        talentRatingVO.setOverallScore(overallScore);
        talentRatingVO.setOverallLevel(overallLevel);
        talentRatingVO.setOverallEvaluate(overallEvaluate);
        talentRatingVO.setDimensionResults(dimensionResults);
        return talentRatingVO;
    }
}
