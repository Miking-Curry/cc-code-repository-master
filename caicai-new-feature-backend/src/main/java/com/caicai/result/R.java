package com.caicai.result;

import cn.hutool.http.HttpStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
@description: 返回结果类
@author: LiWeihang
@create: 2025/5/13 14:48
**/
@Getter
@Setter
@Accessors(chain = true) //允许链式编程
public class R<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int code;

    private String msg;

    private T data;

    public static <T> R<T> success() {
        return restResult(null, HttpStatus.HTTP_OK, null);
    }

    public static <T> R<T> success(T data) {
        return restResult(data, HttpStatus.HTTP_OK, null);
    }

    public static <T> R<T> success(T data, String msg) {
        return restResult(data, HttpStatus.HTTP_OK, msg);
    }

    public static <T> R<T> error() {
        return restResult(null, HttpStatus.HTTP_INTERNAL_ERROR, null);
    }

    public static <T> R<T> error(String msg) {
        return restResult(null, HttpStatus.HTTP_INTERNAL_ERROR, msg);
    }

    public static <T> R<T> error(T data) {
        return restResult(data, HttpStatus.HTTP_INTERNAL_ERROR, null);
    }

    public static <T> R<T> error(T data, String msg) {
        return restResult(data, HttpStatus.HTTP_INTERNAL_ERROR, msg);
    }

    public static <T> R<T> error(int code, String msg) {
        return restResult(null, code, msg);
    }

    private static <T> R<T> restResult(T data, int code, String msg) {
        return new R<T>().setCode(code).setData(data).setMsg(msg);
    }

}