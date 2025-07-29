package com.caicai.mapper;

import com.caicai.entity.model.resume.UserSkillModel;
import com.caicai.entity.pojo.UserSkill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author CCN
* @description 针对表【user_skill】的数据库操作Mapper
* @createDate 2025-06-27 14:20:02
* @Entity com.caicai.entity.pojo.UserSkill
*/
public interface UserSkillMapper extends BaseMapper<UserSkill> {

    /**
     * @param userId:
     * @return List<SkillTagModel>:
     * @Author: Panda
     * @Description: 根据用户id获取所有技能标签基本信息
     * @Date: 2025/6/24 16:24
     */
    List<UserSkillModel> selectSkillTagModelByUserId(@Param("userId") Long userId);

}




