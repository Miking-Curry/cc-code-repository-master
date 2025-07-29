package com.caicai.handler;

import com.caicai.exception.BusinessException;
import com.caicai.result.R;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
@description: 全局异常处理类
@author: LiWeihang
@create: 2025/5/13 14:53
**/
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义业务异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<Void> handleBusinessException(BusinessException e) {
        return R.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理请求参数校验异常 (Valid 和 Validated 注解)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMsg = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return R.error(HttpStatus.BAD_REQUEST.value(), errorMsg);
    }

    /**
     * 处理单个参数校验异常 (@Valid @Validated)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<Void> handleConstraintViolationException(ConstraintViolationException e) {
        String errorMsg = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)  // 提取每个违反约束的错误信息
                .collect(Collectors.joining(", "));
        return R.error(HttpStatus.BAD_REQUEST.value(), errorMsg);
    }

    /**
     * 处理绑定异常 (如表单提交)
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<Void> handleBindException(BindException e) {
        String errorMsg = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return R.error(HttpStatus.BAD_REQUEST.value(), errorMsg);
    }

    /**
     * 处理 JSON 格式不正确异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return R.error(HttpStatus.BAD_REQUEST.value(), "JSON格式错误"+e.getMessage());
    }

    /**
     * 处理所有未知异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<Void> handleException(Exception e) {
        //打印错误到控制台，后期可以记录错误日志
        e.printStackTrace();
        return R.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器异常");
    }
}