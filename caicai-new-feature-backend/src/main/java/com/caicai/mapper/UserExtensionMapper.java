package com.caicai.mapper;

import com.caicai.entity.pojo.UserExtension;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author LiWeihang
* @description 针对表【user_extension(用户扩展表)】的数据库操作Mapper
* @createDate 2025-05-20 09:22:29
* @Entity com.caicai.entity.pojo.UserExtension
*/
public interface UserExtensionMapper extends BaseMapper<UserExtension> {

    /**
     * 根据用户ID查询用户扩展信息
     * @param userId 用户ID
     * @return 用户扩展信息
     */
    UserExtension selectByUserId(@Param("userId") Long userId);

}




