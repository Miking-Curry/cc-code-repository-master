package com.caicai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caicai.entity.pojo.UserInfo;
import com.caicai.service.UserInfoService;
import com.caicai.mapper.UserInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author CCN
* @description 针对表【user_info】的数据库操作Service实现
* @createDate 2025-06-23 11:16:28
*/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService{

}




