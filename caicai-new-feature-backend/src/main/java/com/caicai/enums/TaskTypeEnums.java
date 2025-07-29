package com.caicai.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * @ClassName: TaskTypeEnums
 * @Description: 任务类型枚举类
 * @Author: PCT
 * @Date: 2025/5/22 17:04
 * @Version: 1.0
 */

@Getter
public enum TaskTypeEnums {
    DAILY(1, "DAILY", "每日任务")
    ,
    ONCE(2, "ONCE", "一次性任务")
    ,
    WEEKLY(3, "WEEKLY", "每周任务")
    ,
    MONTHLY(4, "MONTHLY", "每月任务")
    ,
    EVENT(5, "EVENT", "活动限时任务")
    ;


    private final Integer taskTypeId;
    private final String taskTypeCode;
    private final String taskTypeName;

    private TaskTypeEnums(Integer taskTypeId, String taskTypeCode, String taskTypeName) {
        this.taskTypeId = taskTypeId;
        this.taskTypeCode = taskTypeCode;
        this.taskTypeName = taskTypeName;
    }

    public static TaskTypeEnums ofId(Integer taskTypeId) {
        return Arrays.stream(values())
                .filter(e -> e.taskTypeId.equals(taskTypeId))
                .findFirst()
                .orElse(null);
    }

    public static TaskTypeEnums ofCode(String taskTypeCode) {
        return Arrays.stream(values())
                .filter(e -> e.taskTypeCode.equals(taskTypeCode))
                .findFirst()
                .orElse(null);
    }
}
