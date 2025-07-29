package com.caicai.mapper;

import com.caicai.entity.model.resume.UserWorkExperienceModel;
import com.caicai.entity.pojo.UserWorkExperience;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author CCN
* @description 针对表【user_work_experience】的数据库操作Mapper
* @createDate 2025-06-23 11:18:49
* @Entity com.caicai.entity.pojo.UserWorkExperience
*/
public interface UserWorkExperienceMapper extends BaseMapper<UserWorkExperience> {
    /**
     * @param userId:
     * @return List<UserWorkExperienceModel>:
     * @Author: Panda
     * @Description: 根据人才id查询所有工作经历基本信息
     * @Date: 2025/6/24 17:31
     */
    List<UserWorkExperienceModel> selectUserWorkExperienceModel(@Param("userId") Long userId);
}




