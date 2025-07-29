package com.caicai.entity.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName: UserPointCountVO
 * @Description: 用户积分计数实体VO
 * @Author: PCT
 * @Date: 2025/6/4 9:31
 * @Version: 1.0
 */

@Data
@Builder
public class UserPointCountVO {
    private Long userId;
    private Integer pointCount;
}
