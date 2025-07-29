package com.caicai.mapper;

import com.caicai.entity.model.resume.UserProjectExperienceModel;
import com.caicai.entity.pojo.UserProjectExperience;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author CCN
* @description 针对表【user_project_experience】的数据库操作Mapper
* @createDate 2025-06-27 14:54:09
* @Entity com.caicai.entity.pojo.UserProjectExperience
*/
public interface UserProjectExperienceMapper extends BaseMapper<UserProjectExperience> {

    /**
     * @param userId:
     * @return List<UserProjectExperienceModel>:
     * @Author: Panda
     * @Description: 根据简历id查询所有项目经验基本信息
     * @Date: 2025/6/25 15:17
     */
    List<UserProjectExperienceModel> selectProjectExperienceModelByUserId(@Param("userId") Long userId);
}




