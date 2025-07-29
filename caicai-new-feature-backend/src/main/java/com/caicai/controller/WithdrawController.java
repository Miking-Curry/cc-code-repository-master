package com.caicai.controller;

import com.caicai.entity.dto.ApplyWithdrawFormDTO;
import com.caicai.result.R;
import com.caicai.service.WithdrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: WithdrawController
 * @Description: 用户提现接口
 * @Author: PCT
 * @Date: 2025/6/4 11:25
 * @Version: 1.0
 */

@RestController
@RequestMapping("/withdraw")
@RequiredArgsConstructor
public class WithdrawController {

    private final WithdrawService withdrawService;

    /**
     * @param userId:
     * @param formDTO:
     * @return R<String>:
     * @Author: Panda
     * @Description: 用户提现申请
     * @Date: 2025/6/4 15:43
     */
    @PostMapping("/apply")
    public R<Void> applyWithdraw(@RequestAttribute("userId") Long userId, @ModelAttribute ApplyWithdrawFormDTO formDTO) {
        withdrawService.applyWithdraw(userId, formDTO);
        return R.success();
    }
}
