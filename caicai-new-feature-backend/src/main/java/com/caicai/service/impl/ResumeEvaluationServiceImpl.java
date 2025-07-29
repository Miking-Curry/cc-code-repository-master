package com.caicai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caicai.entity.pojo.ResumeEvaluation;
import com.caicai.service.ResumeEvaluationService;
import com.caicai.mapper.ResumeEvaluationMapper;
import org.springframework.stereotype.Service;

/**
* @author CCN
* @description 针对表【resume_evaluation(简历AI评分汇总表)】的数据库操作Service实现
* @createDate 2025-06-25 17:58:19
*/
@Service
public class ResumeEvaluationServiceImpl extends ServiceImpl<ResumeEvaluationMapper, ResumeEvaluation>
    implements ResumeEvaluationService{

    /**
     * @param newResumeEvaluation:
     * @return ResumeEvaluation:
     * @Author: Panda
     * @Description: 更新/保存人才评分总评记录
     * @Date: 2025/6/26 9:37
     */
    @Override
    public ResumeEvaluation saveResumeEvaluation(ResumeEvaluation newResumeEvaluation) {
        ResumeEvaluation resumeEvaluation = this.lambdaQuery()
                .eq(ResumeEvaluation::getUserId, newResumeEvaluation.getUserId())
                .or()
                .eq(ResumeEvaluation::getResumeId, newResumeEvaluation.getResumeId())
                .one();

        if (resumeEvaluation != null) {
            newResumeEvaluation.setId(resumeEvaluation.getId());
            this.updateById(newResumeEvaluation);
            return newResumeEvaluation;
        }

        this.save(newResumeEvaluation);
        return newResumeEvaluation;
    }
}




