package com.caicai.strategy.strategyImpl;

import com.caicai.entity.model.TaskPeriod;
import com.caicai.entity.pojo.TaskDefinition;
import com.caicai.enums.TaskTypeEnums;
import com.caicai.strategy.TaskPeriodStrategy;

import java.time.LocalDateTime;

/**
 * @ClassName: EventTaskPeriodStrategy
 * @Description: 活动限时任务策略实现类
 * @Author: PCT
 * @Date: 2025/6/11 15:55
 * @Version: 1.0
 */

public class EventTaskPeriodStrategy implements TaskPeriodStrategy {
    @Override
    public TaskTypeEnums getTaskType() {
        return TaskTypeEnums.EVENT;
    }

    @Override
    public TaskPeriod getTaskPeriod(LocalDateTime now, TaskDefinition task) {
        return new TaskPeriod(
                task.getStartTime(),
                task.getEndTime()
        );
    }
}
