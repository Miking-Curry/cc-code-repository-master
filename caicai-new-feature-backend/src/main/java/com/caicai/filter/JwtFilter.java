package com.caicai.filter;

import com.caicai.util.JwtUtil;
import com.caicai.util.ResponseUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
@description: Jwt校验过滤器
@author: LiWeihang
@create: 2025/5/16 11:08
**/
@Slf4j
public class JwtFilter implements Filter {

    /**
     * @description: 过滤器中校验JWT时，一般不推荐抛出异常，而是直接构造响应返回
     * @author: LiWeihang
     * @create: 2025/5/16 11:50
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest sr = (HttpServletRequest)request;
        log.info("request ip -> {}", sr.getRemoteAddr());
        log.info("request path -> [{}]", sr.getServletPath());

        // 将Servlet请求和响应对象强制转换为HTTP类型
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // 从请求头中获取Authorization字段
        String authHeader = req.getHeader("Authorization");

        // 校验Authorization头部是否存在，并且以"Bearer "开头
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // 如果没有 token 或格式不对，则返回 401 未授权
            ResponseUtil.writeError(res, HttpServletResponse.SC_UNAUTHORIZED, "缺少或非法的Authorization头");
            return;
        }

        // 截取真正的JWT字符串，去掉前缀"Bearer "
        String token = authHeader.substring(7);

        try {
            // 使用JwtUtil工具类解析token，获取声明体（Claims）
            Claims claims = JwtUtil.parseToken(token);

            // 将解析出的用户信息保存到请求属性中，供后续控制器或拦截器使用
            req.setAttribute("userId", claims.get("user_id")); // 用户ID或用户名
            req.setAttribute("claims", claims);              // 整个claims对象，可包含角色、权限等

        } catch (JwtException e) {
            // 如果解析失败（token 过期、被篡改、签名不对等），返回 401
            ResponseUtil.writeError(res, HttpServletResponse.SC_UNAUTHORIZED, "无效或过期的Token");
            return;
        }

        // 如果一切正常，继续执行后续过滤器链或请求处理
        chain.doFilter(request, response);
    }
}