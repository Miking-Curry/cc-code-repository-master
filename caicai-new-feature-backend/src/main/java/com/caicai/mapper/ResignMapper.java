package com.caicai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.caicai.entity.pojo.UserResign;
import org.apache.ibatis.annotations.Param;

public interface ResignMapper extends BaseMapper<UserResign> {
    
    /**
     * 根据用户ID查询离职信息
     * @param userId 用户ID
     * @return 离职信息，如果不存在则返回null
     */
    UserResign selectByUserId(@Param("userId") Long userId);
}
