package com.caicai.exception;

import lombok.Getter;

/**
@description: 自定义业务异常
@author: LiWeihang
@create: 2025/5/13 14:42
**/
@Getter
public class BusinessException extends RuntimeException {
    private final int code;

    /**
    @description: code是业务状态码，不是HTTP状态码
    @author: LiWeihang
    @create: 2025/5/13 15:03
    **/
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

}