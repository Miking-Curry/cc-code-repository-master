package com.caicai.mapper;

import com.caicai.entity.dto.ResumeDetailDTO;
import com.caicai.entity.pojo.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author CCN
* @description 针对表【resume(简历表)】的数据库操作Mapper
* @createDate 2025-06-24 11:05:31
* @Entity com.caicai.entity.pojo.Resume
*/
public interface ResumeMapper extends BaseMapper<Resume> {
    /**
     * @param userId:
     * @return Long:
     * @Author: Panda
     * @Description: 根据用户id查询简历id 每个用户仅有一份简历
     * @Date: 2025/6/25 9:32
     */
    Long selectIdByUserId(@Param("userId") Long userId);

    /**
     * 通过用户ID查询简历
     */
    Resume getResumeByUserId(@Param("userId") Long userId);
    
    /**
     * 通过用户ID查询工作经历
     */
    List<UserWorkExperience> getUserWorkExperiencesByUserId(@Param("userId") Long userId);
    
    /**
     * 通过用户ID查询教育经历
     */
    List<UserEducationExperience> getUserEducationExperiencesByUserId(@Param("userId") Long userId);
    
    /**
     * 通过用户ID查询项目经验
     */
    List<UserProjectExperience> getProjectExperiencesByUserId(@Param("userId") Long userId);
    
    /**
     * 通过简历ID查询获奖荣誉
     */
    List<AwardHonor> getAwardHonorsByResumeId(@Param("resumeId") Long resumeId);
    
    /**
     * 通过简历ID查询技能标签
     */
    List<UserSkill> getSkillTagsByUserId(@Param("userId") Long userId);
    
    /**
     * 通过用户ID查询简历详情
     */
    List<ResumeDetailDTO> getResumeDetailByUserId(@Param("userId") Long userId);

    /**
     * 通过用户ID查询用户基本信息
     */
    User getUserById(Long userId);
}
