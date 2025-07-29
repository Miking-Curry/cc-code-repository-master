package com.caicai.config;

import com.caicai.filter.CORSFilter;
import com.caicai.filter.JwtFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
@description: 过滤器注册配置
@author: LiWeihang
@create: 2025/5/16 11:20
**/
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilter() {
        FilterRegistrationBean<JwtFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new JwtFilter());
        bean.addUrlPatterns("/test/JWT"); // 只拦截你想保护的接口路径 TODO
        bean.addUrlPatterns("/test/point/token");
        bean.addUrlPatterns("/token/parse-token");
        bean.addUrlPatterns("/user/*"); // user下所有路径
        bean.addUrlPatterns("/credit/*");
        bean.addUrlPatterns("/idcards/*");
        bean.addUrlPatterns("/educations/*");
        bean.addUrlPatterns("/work-experiences/*");
        bean.addUrlPatterns("/certificates/*");
        bean.addUrlPatterns("/resignations/*");
        bean.addUrlPatterns("/task/*"); // task下所有路径
        bean.addUrlPatterns("/point/*"); // point下所有路径
        bean.addUrlPatterns("/exchangeable/*"); // exchangeable下所有路径
        bean.addUrlPatterns("/withdraw/*"); // exchangeable下所有路径
        bean.addUrlPatterns("/track/*"); // track下的一层路径
        bean.addUrlPatterns("/talent/*"); // track下的一层路径
        bean.addUrlPatterns("/resume/*"); // 添加简历相关接口
        bean.addUrlPatterns("/post/*"); //  添加职位相关接口
        bean.addUrlPatterns("/postVector/*");
//        bean.addUrlPatterns("/ai/*");
        bean.addUrlPatterns("/region/*");
        bean.addUrlPatterns("/postTag/*");
        bean.addUrlPatterns("/post-status/*");
        bean.setOrder(1); // 顺序：多个 Filter 时使用
        return bean;
    }

    @Bean
    public FilterRegistrationBean<CORSFilter> corsFilterRegistration() {
        FilterRegistrationBean<CORSFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new CORSFilter());
        registration.addUrlPatterns("/*"); // 应用于所有接口
        registration.setOrder(0); // 设置优先级为最高
        return registration;
    }
}