package com.caicai.controller;

import com.caicai.result.R;
import com.caicai.util.JwtUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.jsonwebtoken.Claims;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: TokenController
 * @Description: 用户Token令牌相关
 * @Author: PCT
 * @Date: 2025/6/3 9:33
 * @Version: 1.0
 */
@RestController
@RequestMapping("/token")
public class TokenController {

    // 开发环境启用，生产环境请注释 TODO
     @GetMapping("/get-token")
     public R<Map<String, Object>> getToken(@RequestParam(required = false, defaultValue = "1") Long userId) {
         //生成token令牌
         //String token = JwtUtil.getToken(userId);
         String token = JwtUtil.generateToken(userId);
         //封装响应
         Map<String, Object> tokenData = new HashMap<>();
         tokenData.put("token", token);
         tokenData.put("expiresIn", "3600000");
         tokenData.put("userId", userId);

         return R.success(tokenData);
     }

    /**
     * @param userId:
     * @param claims:
     * @return R<String>:
     * @Author: Panda
     * @Description: 因为已经在过滤器里把userId存到请求属性了，可以用可以用@RequestAttribute("userId")获取userId
     * @Date: 2025/6/3 10:26
     */
    @GetMapping("/parse-token")
    public R<Map<String, Object>> parseToken(@RequestAttribute("userId") Long userId, @RequestAttribute("claims") Claims claims) {
        Map<String, Object> result = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        result.put("user", userId);
        result.put("issueAt", sdf.format(claims.getIssuedAt()));
        result.put("expiresAt", sdf.format(claims.getExpiration()));
        return R.success(result);
    }
}
