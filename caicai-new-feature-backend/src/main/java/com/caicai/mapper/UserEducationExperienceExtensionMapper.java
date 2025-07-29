package com.caicai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.caicai.entity.pojo.UserEducationExperienceExtension;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户教育经历扩展Mapper接口
 * @author YangFuyi
 * @create 2025/5/22
 */

public interface UserEducationExperienceExtensionMapper extends BaseMapper<UserEducationExperienceExtension> {

    /**
     * 根据用户教育经历主记录ID查询扩展信息。
     * @param experienceId 用户教育经历主记录的ID (user_education_experience.id)
     * @return 用户教育经历扩展对象，如果不存在则返回null。
     */
    UserEducationExperienceExtension selectByExperienceId(@Param("experienceId") Long experienceId);

}
