package com.caicai.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: CORSFilter
 * @Description:
 * @Author: PCT
 * @Date: 2025/7/11 14:01
 * @Version: 1.0
 */


@WebFilter("/*")
public class CORSFilter implements Filter {

    // 允许的 Origin 列表（必须是完整的，包括协议、域名/IP、端口）
    private static final List<String> ALLOWED_ORIGINS = Arrays.asList(
            "http://localhost:5500",
            "http://127.0.0.1:5500",
            "http://192.168.5.49:5777",
            "https://caicainet.com",
            "http://112.74.33.58:48111",
            "http://192.168.5.69:5777",
            "http://192.168.5.69:5173"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        //获取origin头
        String origin = req.getHeader("Origin");

        //若origin头不为空 且origin头在允许列表内
        if (origin != null && ALLOWED_ORIGINS.contains(origin)) {
            res.setHeader("Access-Control-Allow-Origin", origin); // 设置为当前请求的 Origin
            res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            res.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
            res.setHeader("Access-Control-Allow-Credentials", "true");
            res.setHeader("Access-Control-Max-Age", "3600");
        }

        // OPTIONS 请求提前返回 200，避免进入后续业务逻辑
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            res.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(request, response);
    }
}
