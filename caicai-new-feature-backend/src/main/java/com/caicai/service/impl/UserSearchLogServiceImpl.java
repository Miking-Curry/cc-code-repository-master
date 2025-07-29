package com.caicai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caicai.entity.dto.UserKeywordDTO;
import com.caicai.entity.pojo.UserSearchLog;
import com.caicai.service.UserSearchLogService;
import com.caicai.mapper.UserSearchLogMapper;
import org.springframework.stereotype.Service;

/**
* @author ccn00
* @description 针对表【user_search_log(用户搜索关键词记录表)】的数据库操作Service实现
* @createDate 2025-06-09 09:33:05
*/
@Service
public class UserSearchLogServiceImpl extends ServiceImpl<UserSearchLogMapper, UserSearchLog>
        implements UserSearchLogService {

    /**
    @description: 用户搜索关键词数据采集业务实现
    @author: LiWeihang
    @create: 2025/6/9 9:51
    **/
    @Override
    public boolean saveKeyword(Long userId, UserKeywordDTO userKeywordDTO) {
        UserSearchLog userSearchLog = new UserSearchLog()
                .setUserId(userId)
                .setKeyword(userKeywordDTO.getKeyword())
                .setSearchTime(userKeywordDTO.getSearchTime());
        return super.save(userSearchLog);
    }
}




