package com.caicai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.caicai.entity.pojo.UserWorkExperienceExtension;
import org.apache.ibatis.annotations.Param;

public interface UserWorkExperienceExtensionMapper extends BaseMapper<UserWorkExperienceExtension> {
    
    /**
     * 根据用户ID查询工作经历扩展信息
     * @param userId 用户ID
     * @return 工作经历扩展信息，如果不存在则返回null
     */
    UserWorkExperienceExtension selectByUserId(@Param("userId") Long userId);
}
