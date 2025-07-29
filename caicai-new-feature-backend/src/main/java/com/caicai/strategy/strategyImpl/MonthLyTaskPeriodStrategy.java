package com.caicai.strategy.strategyImpl;

import com.caicai.entity.model.TaskPeriod;
import com.caicai.entity.pojo.TaskDefinition;
import com.caicai.enums.TaskTypeEnums;
import com.caicai.strategy.TaskPeriodStrategy;

import java.time.LocalDateTime;

/**
 * @ClassName: MonthLyTaskPeriodStrategy
 * @Description: 每月任务策略实现类
 * @Author: PCT
 * @Date: 2025/6/11 10:58
 * @Version: 1.0
 */

public class MonthLyTaskPeriodStrategy implements TaskPeriodStrategy {

    @Override
    public TaskTypeEnums getTaskType() {
        return TaskTypeEnums.MONTHLY;
    }

    @Override
    public TaskPeriod getTaskPeriod(LocalDateTime now, TaskDefinition task) {
        int lastDay = now.toLocalDate().lengthOfMonth();
        return new TaskPeriod(
                now.withDayOfMonth(1).toLocalDate().atTime(0, 1, 0),
                now.withDayOfMonth(lastDay).toLocalDate().atTime(23, 59, 59)
        );
    }
}
