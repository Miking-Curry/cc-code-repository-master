package com.caicai.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName: UserPointChangeRecordVO
 * @Description: 用户积分变动记录实体VO
 * @Author: PCT
 * @Date: 2025/6/3 11:41
 * @Version: 1.0
 */

@Data
@Builder
public class UserPointChangeRecordVO {
    private Long userId;
    private String changeType;
    private Integer changePoint;
    private String eventDescription;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime changeTime;
}
