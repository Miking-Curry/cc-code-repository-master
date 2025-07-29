package com.caicai.service;

import com.caicai.entity.pojo.ResumeEvaluation;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author CCN
* @description 针对表【resume_evaluation(简历AI评分汇总表)】的数据库操作Service
* @createDate 2025-06-25 17:58:19
*/
public interface ResumeEvaluationService extends IService<ResumeEvaluation> {
    /**
     * @param resumeEvaluation:
     * @return ResumeEvaluation:
     * @Author: Panda
     * @Description: 更新/保存人才评分总评记录
     * @Date: 2025/6/26 9:32
     */
    ResumeEvaluation saveResumeEvaluation(ResumeEvaluation resumeEvaluation);
}
