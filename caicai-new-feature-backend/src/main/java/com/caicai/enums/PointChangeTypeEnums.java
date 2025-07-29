package com.caicai.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * @ClassName: PointChangeType
 * @Description: 积分变动类型枚举类
 * @Author: PCT
 * @Date: 2025/6/5 15:13
 * @Version: 1.0
 */

@Getter
public enum PointChangeTypeEnums {
    INCOME("1", "积分收入"),
    OUTCOME("-1", "积分支出");

    private final String code;
    private final String desc;

    PointChangeTypeEnums(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static PointChangeTypeEnums ofCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.code.equals(code))
                .findFirst()
                .orElse(null);
    }
}
