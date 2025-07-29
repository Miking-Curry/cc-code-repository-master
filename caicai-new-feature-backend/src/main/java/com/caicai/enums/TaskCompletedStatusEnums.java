package com.caicai.enums;

import lombok.Getter;

/**
 * @ClassName: TaskCompletedStatusEnums
 * @Description: 任务完成状态枚举类
 * @Author: PCT
 * @Date: 2025/6/10 16:15
 * @Version: 1.0
 */

@Getter
public enum TaskCompletedStatusEnums {
    IN_PROGRESS(0, "进行中"),
    COMPLETED(1, "已完成"),
    UNFINISHED(2, "未完成");

    private Integer mark;
    private String desc;

    TaskCompletedStatusEnums(Integer mark, String desc) {
        this.mark = mark;
        this.desc = desc;
    }
}
