package com.caicai.util;

import com.alibaba.fastjson2.JSON;
import com.caicai.result.R;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
@description: 过滤器层验证错误返回结果工具类，结果会封装成一个基于R类的Json
@author: LiWeihang
@create: 2025/5/16 11:20
**/
public class ResponseUtil {

    /**
     * 发送统一的错误响应 JSON
     * @param response HttpServletResponse 对象
     * @param code HTTP 状态码，如 401、403、500
     * @param msg  错误提示消息
     */
    public static void writeError(HttpServletResponse response, int code, String msg) throws IOException {
        response.setStatus(code);
        response.setContentType("application/json;charset=UTF-8");

        // 使用R类生成错误结果对象
        R<Object> errorResult = R.error(code, msg);

        // 转成JSON字符串
        String json = JSON.toJSONString(errorResult);

        // 写入响应体
        response.getWriter().write(json);
    }
}