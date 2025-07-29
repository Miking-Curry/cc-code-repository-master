package com.caicai.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caicai.entity.pojo.ExchangeableItem;
import com.caicai.entity.vo.ExchangeableItemVO;
import com.caicai.service.ExchangeableItemService;
import com.caicai.mapper.ExchangeableItemMapper;
import com.caicai.service.UserExtensionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
* @author CCN
* @description 针对表【exchangeable_item(可兑换物品表)】的数据库操作Service实现
* @createDate 2025-06-04 15:16:16
*/
@Service
@RequiredArgsConstructor
public class ExchangeableItemServiceImpl extends ServiceImpl<ExchangeableItemMapper, ExchangeableItem>
    implements ExchangeableItemService{
    private final UserExtensionService userExtensionService;

    /**
     * @param userId:
     * @return List<ExchangeableItemVO>:
     * @Author: Panda
     * @Description: 获取当前用户可兑换物品
     * @Date: 2025/6/4 15:08
     */
    @Override
    public List<ExchangeableItemVO> getAllExchangeableItems(Long userId) {
        //获取当前时间
        LocalDateTime now = LocalDateTime.now();

        //查询所有可兑换物品
        List<ExchangeableItem> allExchangeableItem = this.lambdaQuery()
                .eq(ExchangeableItem::getIsVisible, true)
                .eq(ExchangeableItem::getIsEnabled, true)
                .and(lqw -> lqw
                        .isNull(ExchangeableItem::getAvailableStock)
                        .or()
                        .gt(ExchangeableItem::getAvailableStock, 0)
                )
                .and(lqw -> lqw
                        .isNull(ExchangeableItem::getStartTime)
                        .or()
                        .ge(ExchangeableItem::getStartTime, now)
                )
                .and(lqw -> lqw
                        .isNull(ExchangeableItem::getEndTime)
                        .or()
                        .le(ExchangeableItem::getEndTime, now)
                )
                .list();

        //查询当前用户积分
        Long pointByUserId = this.userExtensionService.getPointByUserId(userId);

        //根据当前用户积分设置物品可兑换状态
        return allExchangeableItem.stream()
                //转为ExchangeableItemVO
                .map(exchangeableItem -> BeanUtil.toBean(exchangeableItem, ExchangeableItemVO.class))
                .peek(exchangeableItemVO -> {
                    if (pointByUserId < exchangeableItemVO.getPointCost()) {
                        exchangeableItemVO.setCanExchange(false);
                    }
                })
                .toList();
    }
}




