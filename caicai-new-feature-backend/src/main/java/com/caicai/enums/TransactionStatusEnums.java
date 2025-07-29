package com.caicai.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * @ClassName: TransactionStatus
 * @Description: 兑换/提现状态枚举类
 * @Author: PCT
 * @Date: 2025/6/5 11:54
 * @Version: 1.0
 */

@Getter
public enum TransactionStatusEnums {
    PROCESSING("PROCESSING", "处理中"),
    SUCCESS("SUCCESS", "处理完成"),
    FAILED("FAILED", "处理失败");

    private final String code;
    private final String desc;

    private TransactionStatusEnums(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static TransactionStatusEnums ofCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.code.equals(code))
                .findFirst()
                .orElse(null);
    }
}
