package com.caicai.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: BaseController
 * @Description:
 * @Author: PCT
 * @Date: 2025/6/13 17:29
 * @Version: 1.0
 */

@RestController
public class IndexController {
    @RequestMapping("/")
    public String index() {
        return "<h2>Welcome to the CCN 后端服务</h2><p>这是 API 接口服务，请通过前端或 API 客户端访问。</p>";
    }
}
