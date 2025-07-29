package com.caicai.mapper;

import com.caicai.entity.model.resume.AwardHonorModel;
import com.caicai.entity.pojo.AwardHonor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author CCN
* @description 针对表【award_honor(用户获奖荣誉表)】的数据库操作Mapper
* @createDate 2025-06-24 11:38:02
* @Entity com.caicai.entity.pojo.AwardHonor
*/
public interface AwardHonorMapper extends BaseMapper<AwardHonor> {

    /**
     * @param resumeId:
     * @return List<AwardHonor>:
     * @Author: Panda
     * @Description: 根据简历id获取对应所有荣誉奖项
     * @Date: 2025/6/24 15:23
     */
    List<AwardHonorModel> getAwardHonorModelByResumeId(@Param("resumeId") Long resumeId);
}




