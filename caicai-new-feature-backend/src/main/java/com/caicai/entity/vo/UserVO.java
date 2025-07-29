package com.caicai.entity.vo;

import lombok.Data;

/**
 * @ClassName: UserVO
 * @Description: 用户实体VO
 * @Author: PCT
 * @Date: 2025/6/3 10:37
 * @Version: 1.0
 */

@Data
public class UserVO {
    private Long id;
    private String username;
    private String email;
    private String phone;
}
