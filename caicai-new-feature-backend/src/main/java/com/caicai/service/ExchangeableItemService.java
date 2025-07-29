package com.caicai.service;

import com.caicai.entity.pojo.ExchangeableItem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.caicai.entity.vo.ExchangeableItemVO;

import java.util.List;

/**
* @author CCN
* @description 针对表【exchangeable_item(可兑换物品表)】的数据库操作Service
* @createDate 2025-06-04 15:16:16
*/
public interface ExchangeableItemService extends IService<ExchangeableItem> {

    /**
     * @param userId:
     * @return List<ExchangeableItemVO>:
     * @Author: Panda
     * @Description: 获取当前用户可兑换物品
     * @Date: 2025/6/4 15:17
     */
    List<ExchangeableItemVO> getAllExchangeableItems(Long userId);
}
