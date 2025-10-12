package com.caicai.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
@description: JWT工具类，支持生成和校验JWT
@author: LiWeihang
@create: 2025/5/16 11:08
**/
@Component
public class JwtUtil {

    private static String SECRET; // 秘钥
    
    private static String TOKEN_PREFIX;
    
    private static long EXPIRATION_TIME;

    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        JwtUtil.SECRET = secret;
    }

    @Value("${jwt.token-prefix}")
    public void setHeader(String tokenPrefix) {
        JwtUtil.TOKEN_PREFIX = tokenPrefix;
    }

    @Value("${jwt.expiration}")
    public void setExpiration(long expiration) {
        JwtUtil.EXPIRATION_TIME = expiration;
    }

    // 将一个字符串形式的密钥（String SECRET）转换为一个可以用于HMAC-SHA算法的密钥对象（Key 类型），供JWT使用
    private static Key getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    /**
     * 生成JWT令牌 - String类型userId (向后兼容)
     * @param userId 用户ID
     * @return JWT令牌字符串
     */
    public static String generateToken(String userId) {
        return generateToken(userId, new HashMap<>());
    }

    /**
     * 生成JWT令牌 - Long类型userId
     * @param userId 用户ID
     * @return JWT令牌字符串
     */
    public static String generateToken(Long userId) {
        return generateToken(userId, new HashMap<>());
    }

    /**
     * 生成带有额外信息的JWT令牌 - String类型userId (向后兼容)
     * @param userId 用户ID
     * @param extraClaims 额外的声明信息
     * @return JWT令牌字符串
     */
    public static String generateToken(String userId, Map<String, Object> extraClaims) {
        // 当前时间
        Date now = new Date();
        // 过期时间
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        // 创建声明
        Map<String, Object> claims = new HashMap<>(extraClaims);
        claims.put("user_id", userId);
        
        // 构建并签名JWT
        return TOKEN_PREFIX + Jwts.builder()
                .setClaims(claims)                 // 设置声明
                .setIssuedAt(now)                  // 设置签发时间
                .setExpiration(expiryDate)         // 设置过期时间
                .signWith(getKey(), SignatureAlgorithm.HS256)  // 使用密钥签名
                .compact();                        // 生成JWT字符串
    }
    
    /**
     * 生成带有额外信息的JWT令牌 - Long类型userId
     * @param userId 用户ID
     * @param extraClaims 额外的声明信息
     * @return JWT令牌字符串
     */
    public static String generateToken(Long userId, Map<String, Object> extraClaims) {
        // 当前时间
        Date now = new Date();
        // 过期时间
        //Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        //1年token-测试用
        Date expiryDate = new Date(now.getTime() + 31536000000L);

        // 创建声明
        Map<String, Object> claims = new HashMap<>(extraClaims);
        claims.put("user_id", userId);

        // 构建并签名JWT
        return TOKEN_PREFIX + Jwts.builder()
                .setClaims(claims)                 // 设置声明
                .setIssuedAt(now)                  // 设置签发时间
                .setExpiration(expiryDate)         // 设置过期时间
                .signWith(getKey(), SignatureAlgorithm.HS256)  // 使用密钥签名
                .compact();                        // 生成JWT字符串
    }

    // 解析并验证 JWT
    public static Claims parseToken(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 验证令牌是否有效
     * @param token JWT令牌
     * @return 如果有效返回true，否则返回false
     */
    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}