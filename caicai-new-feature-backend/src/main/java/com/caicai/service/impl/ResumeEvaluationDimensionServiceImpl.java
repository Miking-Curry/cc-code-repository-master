package com.caicai.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caicai.entity.pojo.ResumeEvaluationDimension;
import com.caicai.service.ResumeEvaluationDimensionService;
import com.caicai.mapper.ResumeEvaluationDimensionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author CCN
* @description 针对表【resume_evaluation_dimension(人才评分各维度记录表)】的数据库操作Service实现
* @createDate 2025-06-25 17:58:26
*/
@Service
public class ResumeEvaluationDimensionServiceImpl extends ServiceImpl<ResumeEvaluationDimensionMapper, ResumeEvaluationDimension>
    implements ResumeEvaluationDimensionService{
    /**
     * @param newResumeEvaluationDimensionList:
     * @return: This method has no return value.
     * @Author: Panda
     * @Description: 更新/保存人才评分各维度记录
     * @Date: 2025/6/26 9:39
     */
    @Transactional
    @Override
    public void saveResumeEvaluationDimension(List<ResumeEvaluationDimension> newResumeEvaluationDimensionList) {
        if (CollUtil.isEmpty(newResumeEvaluationDimensionList)) {
            log.error("保存失败，各维度记录为空");
            return;
        }
        List<Long> evaluationIdList = newResumeEvaluationDimensionList.stream()
                .map(ResumeEvaluationDimension::getEvaluationId)
                .toList();

        //查询是否已存在记录
        List<ResumeEvaluationDimension> resumeEvaluationDimensionList = this.lambdaQuery()
                .in(ResumeEvaluationDimension::getEvaluationId, evaluationIdList)
                .list();

        if (CollUtil.isNotEmpty(resumeEvaluationDimensionList)) {
            //存在->删除原有记录
            //获取原有记录的id
            List<Long> originIds = resumeEvaluationDimensionList.stream()
                    .map(ResumeEvaluationDimension::getId)
                    .toList();
            this.removeBatchByIds(originIds);
        }

        //批量保存
        this.saveBatch(newResumeEvaluationDimensionList);
    }

}




