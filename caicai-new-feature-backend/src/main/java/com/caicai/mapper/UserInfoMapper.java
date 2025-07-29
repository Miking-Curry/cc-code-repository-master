package com.caicai.mapper;

import com.caicai.entity.model.resume.UserInfoModel;
import com.caicai.entity.pojo.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author CCN
* @description 针对表【user_info】的数据库操作Mapper
* @createDate 2025-06-23 11:16:28
* @Entity com.caicai.entity.pojo.UserInfo
*/
public interface UserInfoMapper extends BaseMapper<UserInfo> {
    /**
     * @param userId:
     * @return UserInfoModel:
     * @Author: Panda
     * @Description: 根据用户id查询用户基本信息
     * @Date: 2025/6/24 17:51
     */
    UserInfoModel selectUserInfoModelByUserId(@Param("userId") Long userId);
}




