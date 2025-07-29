package com.caicai.strategy.strategyFactory;

import com.caicai.enums.TaskTypeEnums;
import com.caicai.exception.BusinessException;
import com.caicai.strategy.TaskPeriodStrategy;
import com.caicai.strategy.strategyImpl.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: TaskPeriodStrategyFactory
 * @Description: 任务周期策略工厂类
 * @Author: PCT
 * @Date: 2025/6/11 11:00
 * @Version: 1.0
 */

public class TaskPeriodStrategyFactory {
    private final Map<TaskTypeEnums, TaskPeriodStrategy> strategyMap = new HashMap<>();

    private void register(TaskPeriodStrategy taskPeriodStrategy) {
        strategyMap.put(
                taskPeriodStrategy.getTaskType(),
                taskPeriodStrategy
        );
    }

    public TaskPeriodStrategyFactory() {
        register(new DailyTaskPeriodStrategy());
        register(new MonthLyTaskPeriodStrategy());
        register(new WeeklyTaskPeriodStrategy());
        register(new OnceTaskPeriodStrategy());
        register(new EventTaskPeriodStrategy());
    }

    public TaskPeriodStrategy getTaskPeriodStrategy(TaskTypeEnums taskType) {
        TaskPeriodStrategy taskPeriodStrategy = strategyMap.get(taskType);
        if (taskPeriodStrategy == null) {
            throw new IllegalArgumentException("No Such Strategy Type: " + taskType);
        }
        return taskPeriodStrategy;
    }
}
