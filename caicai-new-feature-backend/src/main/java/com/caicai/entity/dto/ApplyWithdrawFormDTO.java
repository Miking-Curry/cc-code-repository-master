package com.caicai.entity.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName: ApplyWithdrawFormDTO
 * @Description: 提现申请实体DTO
 * @Author: PCT
 * @Date: 2025/6/4 11:56
 * @Version: 1.0
 */

@Data
@Builder
public class ApplyWithdrawFormDTO {
    private BigDecimal amount;
    private String phone;
}
