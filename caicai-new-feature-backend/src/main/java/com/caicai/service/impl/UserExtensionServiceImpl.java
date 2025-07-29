package com.caicai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caicai.entity.pojo.UserExtension;
import com.caicai.entity.pojo.UserInfo;
import com.caicai.exception.BusinessException;
import com.caicai.mapper.UserInfoMapper;
import com.caicai.service.UserExtensionService;
import com.caicai.mapper.UserExtensionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
* @author LiWeihang
* @description 针对表【user_extension(用户扩展表)】的数据库操作Service实现
* @createDate 2025-05-20 09:22:29
*/
@Service
public class UserExtensionServiceImpl extends ServiceImpl<UserExtensionMapper, UserExtension>
    implements UserExtensionService{

    private final UserInfoMapper userInfoMapper;

    @Autowired
    public UserExtensionServiceImpl(UserInfoMapper userInfoMapper) {
        this.userInfoMapper = userInfoMapper;
    }

    /**
     @description: 获取用户总积分
     @author: LiWeihang
     @create: 2025/5/20 10:44
     **/
    @Override
    public Long getPointByUserId(Long userId) {
        //判断user_extension中是否存在该用户
        UserExtension userExtension = this.lambdaQuery().eq(UserExtension::getUserId, userId).one();
        if (userExtension == null) {
            return 0L;
        }
        return userExtension.getPoint();
    }

    /**
     @description: 获取用户信用分
     @author: LiWeihang
     @create: 2025/5/20 14:34
     **/
    @Override
    public Integer getCreditScoreByUserId(Long userId) {
        return this.getByUserId(userId).getCreditScore();
    }

    @Override
    public String getUserNameByUserId(Long userId) {
        LambdaQueryWrapper<UserInfo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserInfo::getUserId, userId);
        return this.userInfoMapper.selectOne(lqw).getName();
    }
    
    /**
    @description: 重写了查找具体实体类找不到时扔出警告
    @author: LiWeihang
    @create: 2025/5/22 16:51
    **/
    @Override
    public UserExtension getById(Serializable id) {
        if(id == null){
            throw new BusinessException(HttpStatus.NOT_FOUND.value(),"ID为空");
        }
        UserExtension userExtension = super.getById(id);
        if(userExtension==null){
            throw new BusinessException(HttpStatus.NOT_FOUND.value(),"找不到具体用户");
        }
        return userExtension;
    }

    /**
    @description: 获取用户使用用户id
    @author: LiWeihang
    @create: 2025/6/6 8:54
    **/
    public UserExtension getByUserId(Serializable userId) {
        if(userId == null){
            throw new BusinessException(HttpStatus.NOT_FOUND.value(),"userId为空");
        }
        UserExtension userExtension = super.getOne(new LambdaQueryWrapper<UserExtension>().eq(UserExtension::getUserId, userId));
        if(userExtension==null){
            throw new BusinessException(HttpStatus.NOT_FOUND.value(),"找不到具体用户");
        }
        return userExtension;
    }

}




