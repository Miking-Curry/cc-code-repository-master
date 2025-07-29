package com.caicai.mapper;

import com.caicai.entity.model.resume.UserJobIntentionModel;
import com.caicai.entity.pojo.UserJobIntention;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author CCN
* @description 针对表【user_job_intention】的数据库操作Mapper
* @createDate 2025-06-23 11:17:43
* @Entity com.caicai.entity.pojo.UserJobIntention
*/
public interface UserJobIntentionMapper extends BaseMapper<UserJobIntention> {
    /**
     * @param userId:
     * @return List<UserJobIntentionModel>:
     * @Author: Panda
     * @Description: 根据人才id查询求职意向基本信息
     * @Date: 2025/6/24 17:06
     */
    List<UserJobIntentionModel> selectUserJobIntentionModel(@Param("userId") Long userId);
}




