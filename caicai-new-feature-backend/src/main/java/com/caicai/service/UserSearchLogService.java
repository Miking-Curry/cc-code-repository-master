package com.caicai.service;

import com.caicai.entity.dto.UserKeywordDTO;
import com.caicai.entity.pojo.UserSearchLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author ccn00
* @description 针对表【user_search_log(用户搜索关键词记录表)】的数据库操作Service
* @createDate 2025-06-09 09:33:05
*/
public interface UserSearchLogService extends IService<UserSearchLog> {

    boolean saveKeyword(Long userId, UserKeywordDTO userKeywordDTO);
}
