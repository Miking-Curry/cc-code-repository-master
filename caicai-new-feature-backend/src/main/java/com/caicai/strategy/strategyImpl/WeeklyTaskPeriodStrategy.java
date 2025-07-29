package com.caicai.strategy.strategyImpl;

import com.caicai.entity.model.TaskPeriod;
import com.caicai.entity.pojo.TaskDefinition;
import com.caicai.enums.TaskTypeEnums;
import com.caicai.strategy.TaskPeriodStrategy;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

/**
 * @ClassName: WeeklyTaskPeriodStrategy
 * @Description: 每周任务策略实现类
 * @Author: PCT
 * @Date: 2025/6/11 10:56
 * @Version: 1.0
 */

public class WeeklyTaskPeriodStrategy implements TaskPeriodStrategy {

    @Override
    public TaskTypeEnums getTaskType() {
        return TaskTypeEnums.WEEKLY;
    }

    @Override
    public TaskPeriod getTaskPeriod(LocalDateTime now, TaskDefinition task) {
        return new TaskPeriod(
                now.with(DayOfWeek.MONDAY).toLocalDate().atTime(0, 1, 0),
                now.with(DayOfWeek.SUNDAY).toLocalDate().atTime(23, 59, 59)
        );
    }
}
