package com.caicai.mapper;

import com.caicai.entity.model.resume.OrganizationExperienceModel;
import com.caicai.entity.pojo.OrganizationExperience;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author CCN
* @description 针对表【organization_experience(用户社团/组织经历表)】的数据库操作Mapper
* @createDate 2025-06-24 16:12:30
* @Entity com.caicai.entity.pojo.OrganizationExperience
*/
public interface OrganizationExperienceMapper extends BaseMapper<OrganizationExperience> {
    /**
     * @param userId:
     * @return List<OrganizationExperienceModel>:
     * @Author: Panda
     * @Description: 根据人才id获取所有社团/组织经历基本信息
     * @Date: 2025/6/24 16:19
     */
    List<OrganizationExperienceModel> selectOrganizationExperienceModelByUserId(@Param("userId") Long userId);
}




