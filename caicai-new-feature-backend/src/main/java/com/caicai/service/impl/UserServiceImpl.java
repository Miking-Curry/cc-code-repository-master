package com.caicai.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caicai.entity.pojo.User;
import com.caicai.entity.vo.UserVO;
import com.caicai.service.UserService;
import com.caicai.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author LiWeihang
* @description 针对表【user】的数据库操作Service实现
* @createDate 2025-05-20 09:22:29
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    /**
     * @param userId:
     * @return UserVO:
     * @Author: Panda
     * @Description: 获取用户手机号
     * @Date: 2025/6/3 9:20
     */
    @Override
    public UserVO getUserPhone(Long userId) {
        User userById = this.getById(userId);
        return BeanUtil.toBean(userById, UserVO.class);
    }
}




