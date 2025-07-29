package com.caicai.service;

import com.caicai.entity.dto.ApplyWithdrawFormDTO;
import com.caicai.result.R;

/**
 * @ClassName: WithdrawService
 * @Description: 提现相关Service
 * @Author: PCT
 * @Date: 2025/6/4 15:41
 * @Version: 1.0
 */

public interface WithdrawService {
    /**
     * @param userId:
     * @param formDTO:
     * @return: This method has no return value.
     * @Author: Panda
     * @Description: 用户提现申请
     * @Date: 2025/6/4 16:30
     */
    void applyWithdraw(Long userId, ApplyWithdrawFormDTO formDTO);
}
