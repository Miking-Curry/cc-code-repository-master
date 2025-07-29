package com.caicai.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName: ExchangeableItemVO
 * @Description: 可兑换物品实体VO
 * @Author: PCT
 * @Date: 2025/6/4 14:29
 * @Version: 1.0
 */

@Data
public class ExchangeableItemVO {
    private Long id;
    private String title;
    private Long itemTypeId;
    private Integer pointCost;
    private Integer availableStock;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    //是否可兑换
    private Boolean canExchange = true;
}
