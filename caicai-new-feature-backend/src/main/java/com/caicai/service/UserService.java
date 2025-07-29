package com.caicai.service;

import com.caicai.entity.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.caicai.entity.vo.UserVO;

/**
* @author LiWeihang
* @description 针对表【user】的数据库操作Service
* @createDate 2025-05-20 09:22:29
*/
public interface UserService extends IService<User> {

    /**
     * @param userId:
     * @return UserVO:
     * @Author: Panda
     * @Description: 获取用户手机号
     * @Date: 2025/6/3 9:07
     */
    UserVO getUserPhone(Long userId);
}
