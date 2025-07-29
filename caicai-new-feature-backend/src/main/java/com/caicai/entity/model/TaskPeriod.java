package com.caicai.entity.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName: TaskPeriod
 * @Description: 任务周期模型
 * @Author: PCT
 * @Date: 2025/6/11 15:13
 * @Version: 1.0
 */

@Data
public class TaskPeriod {
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;

    public TaskPeriod(LocalDateTime periodStart, LocalDateTime periodEnd) {
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
    }

    public static TaskPeriod none() {
        return new TaskPeriod(null, null);
    }

    public boolean isEmpty() {
        return periodStart == null && periodEnd == null;
    }
}
