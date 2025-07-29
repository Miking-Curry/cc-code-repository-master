package com.caicai.strategy.strategyImpl;

import com.caicai.entity.model.TaskPeriod;
import com.caicai.entity.pojo.TaskDefinition;
import com.caicai.enums.TaskTypeEnums;
import com.caicai.strategy.TaskPeriodStrategy;

import java.time.LocalDateTime;

/**
 * @ClassName: OnceTaskPeriodStrategy
 * @Description: 一次性任务周期策略实现类
 * @Author: PCT
 * @Date: 2025/6/11 15:46
 * @Version: 1.0
 */

public class OnceTaskPeriodStrategy implements TaskPeriodStrategy {

    @Override
    public TaskTypeEnums getTaskType() {
        return TaskTypeEnums.ONCE;
    }

    @Override
    public TaskPeriod getTaskPeriod(LocalDateTime now, TaskDefinition task) {
        return TaskPeriod.none();
    }
}
