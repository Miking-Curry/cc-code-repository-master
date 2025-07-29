package com.caicai.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description: 用户的搜索关键词的DTO
 * @author: LiWeihang
 * @create: 2025/6/9 9:40
 **/
@Data
public class UserKeywordDTO {

    /**
     * 关键词
     */
    private String keyword;

    /**
     * 搜索时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime searchTime;

}
