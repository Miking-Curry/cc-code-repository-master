package com.caicai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caicai.entity.pojo.UserLoginLog;
import com.caicai.service.UserLoginLogService;
import com.caicai.mapper.UserLoginLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
* @author ccn00
* @description 针对表【user_login_log(用户登录日志表)】的数据库操作Service实现
* @createDate 2025-05-21 15:16:32
*/
@Service
@RequiredArgsConstructor
public class UserLoginLogServiceImpl extends ServiceImpl<UserLoginLogMapper, UserLoginLog>
    implements UserLoginLogService{

    /**
     * 继承自RedisTemplate<String,String>
     * Key Serializer是StringRedisSerializer
     * Value Serializer也是StringRedisSerializer
     */
    private final StringRedisTemplate stringRedisTemplate;

    /**
     @description: 登录时间数据采集业务,添加反刷机制
     @author: LiWeihang
     @create: 2025/5/21 16:06
     **/
    @Override
    public Boolean saveLoginLog(Long userId, LocalDateTime loginTime) {
        String redisKey = "login:log:lock:" + userId;
        // 尝试设置锁，有效期5分钟，表示5分钟内不重复记录
        Boolean loginLock = stringRedisTemplate.opsForValue().setIfAbsent(redisKey, "1", 5, TimeUnit.MINUTES);
        if (Boolean.TRUE.equals(loginLock)) {
            return this.save(new UserLoginLog()
                    .setUserId(userId)
                    .setLoginTime(loginTime));
        } else {
            // 5分钟内重复登录不记录
            return false;
        }
    }

}




