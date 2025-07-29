package com.caicai.strategy;

import com.caicai.entity.model.TaskPeriod;
import com.caicai.entity.pojo.TaskDefinition;
import com.caicai.enums.TaskTypeEnums;

import java.time.LocalDateTime;

/**
 * @ClassName: TaskPeriodStrategy
 * @Description: 任务周期策略接口
 * @Author: PCT
 * @Date: 2025/6/11 10:53
 * @Version: 1.0
 */

public interface TaskPeriodStrategy {
    TaskTypeEnums getTaskType();

    TaskPeriod getTaskPeriod(LocalDateTime now, TaskDefinition task);
}
