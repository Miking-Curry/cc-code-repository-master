package com.caicai.service;

import com.caicai.entity.pojo.ResumeEvaluationDimension;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author CCN
* @description 针对表【resume_evaluation_dimension(人才评分各维度记录表)】的数据库操作Service
* @createDate 2025-06-25 17:58:26
*/
public interface ResumeEvaluationDimensionService extends IService<ResumeEvaluationDimension> {
    /**
     * @param newResumeEvaluationDimensionList:
     * @return: This method has no return value.
     * @Author: Panda
     * @Description: 更新/保存人才评分各维度记录
     * @Date: 2025/6/26 9:39
     */
    void saveResumeEvaluationDimension(List<ResumeEvaluationDimension> newResumeEvaluationDimensionList);
}
