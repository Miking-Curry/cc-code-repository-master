package com.caicai.mapper;

import com.caicai.entity.model.resume.UserEducationExperienceModel;
import com.caicai.entity.pojo.UserEducationExperience;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.caicai.entity.vo.EducationInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author CCN
* @description 针对表【user_education_experience】的数据库操作Mapper
* @createDate 2025-06-23 11:15:32
* @Entity com.caicai.entity.pojo.UserEducationExperience
*/
public interface UserEducationExperienceMapper extends BaseMapper<UserEducationExperience> {

    UserEducationExperience selectByUserId(Long userId);

    EducationInfoVO selectEducationInfoByUserId(Long userId);

    List<EducationInfoVO> selectEducationInfoForAdmin(int i, Integer size);

    long countEducationInfoForAdmin();
    
    /**
     * @param userId:
     * @return List<UserEducationModel>:
     * @Author: Panda
     * @Description: 根据人才id获取教育经历基本信息
     * @Date: 2025/6/24 16:55
     */
    List<UserEducationExperienceModel> selectUserEducationModelByUserId(@Param("userId") Long userId);
}




