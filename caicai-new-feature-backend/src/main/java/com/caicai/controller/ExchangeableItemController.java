package com.caicai.controller;

import com.caicai.entity.vo.ExchangeableItemVO;
import com.caicai.result.R;
import com.caicai.service.ExchangeableItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName: ExchangeableItemController
 * @Description: 可兑换物品Controller
 * @Author: PCT
 * @Date: 2025/6/4 14:28
 * @Version: 1.0
 */

@RestController
@RequestMapping("/exchangeable")
@RequiredArgsConstructor
public class ExchangeableItemController {
    private final ExchangeableItemService exchangeableItemService;

    /**
     * @param userId:
     * @return R<List<ExchangeableItemVO>>:
     * @Author: Panda
     * @Description: 获取当前用户可兑换的所有物品
     * @Date: 2025/6/4 14:51
     */
    @GetMapping
    public R<List<ExchangeableItemVO>> getAllExchangeableItems(@RequestAttribute Long userId) {
        return R.success(exchangeableItemService.getAllExchangeableItems(userId));
    }
}
