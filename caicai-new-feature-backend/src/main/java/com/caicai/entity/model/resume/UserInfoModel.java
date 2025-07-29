package com.caicai.entity.model.resume;

import lombok.Data;

import java.time.LocalDate;

/**
 * @ClassName: UserInfoModel
 * @Description: AI评分用用户基本信息
 * @Author: PCT
 * @Date: 2025/6/24 17:40
 * @Version: 1.0
 */

@Data
public class UserInfoModel {
    private Long id;
    private String gender;
    private LocalDate birthday;
}
