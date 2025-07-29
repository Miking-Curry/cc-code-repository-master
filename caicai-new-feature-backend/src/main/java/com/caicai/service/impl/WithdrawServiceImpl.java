package com.caicai.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.caicai.entity.dto.ApplyWithdrawFormDTO;
import com.caicai.entity.pojo.PointExchangeRecord;
import com.caicai.entity.pojo.UserExtension;
import com.caicai.entity.pojo.UserPointChangeRecord;
import com.caicai.entity.pojo.UserWithdrawRecord;
import com.caicai.enums.PointChangeTypeEnums;
import com.caicai.enums.TransactionStatusEnums;
import com.caicai.exception.BusinessException;
import com.caicai.service.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName: WithdrawServiceImpl
 * @Description: 提现相关Service实现
 * @Author: PCT
 * @Date: 2025/6/4 15:41
 * @Version: 1.0
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class WithdrawServiceImpl implements WithdrawService {
    private final UserExtensionService userExtensionService;
    private final PointExchangeRecordService pointExchangeRecordService;
    private final UserPointChangeRecordService userPointChangeRecordService;
    private final UserWithdrawRecordService userWithdrawRecordService;

    /**
     * @param userId:
     * @param formDTO:
     * @return R<String>:
     * @Author: Panda
     * @Description: 用户提现申请
     * @Date: 2025/6/4 15:43
     */
    @Override
    @Transactional
    public void applyWithdraw(Long userId, ApplyWithdrawFormDTO formDTO) {
        //获取当前时间
        LocalDateTime now = LocalDateTime.now();

        log.info("amount -> [{}]", formDTO.getAmount());
        log.info("phone -> [{}]", formDTO.getPhone());

        //获取本次申请提现金额
        BigDecimal applyAmount = formDTO.getAmount();
        if (applyAmount == null) {
            throw new BusinessException(HttpStatus.BAD_REQUEST.value(), "内部出现错误");
        }

        //最低金额
        BigDecimal minAmount = BigDecimal.valueOf(100);
        if (applyAmount.compareTo(minAmount) < 0) {
            throw new BusinessException(HttpStatus.BAD_REQUEST.value(), "最低提现金额为100元");
        }

        //获取用户当前总积分
        Long pointByUserId = userExtensionService.getPointByUserId(userId);

        //现金:积分比例
        BigDecimal rate = BigDecimal.valueOf(100);

        //计算本次提现金额所需积分
        BigDecimal needPoint = applyAmount.multiply(rate);

        //判断用户积分是否足够
        if (BigDecimal.valueOf(pointByUserId).compareTo(needPoint) < 0) {
            throw new BusinessException(HttpStatus.BAD_REQUEST.value(), "提现失败, 积分不足!");
        }

        Integer pointCost = Integer.valueOf(needPoint.toPlainString());

        //准备积分兑换记录对象
        PointExchangeRecord pointExchangeRecord = new PointExchangeRecord();
        pointExchangeRecord.setUserId(userId);
        pointExchangeRecord.setItemTitle("现金" + applyAmount.toPlainString() + "元");
        pointExchangeRecord.setPointCost(pointCost);
        pointExchangeRecord.setTxStatus(TransactionStatusEnums.PROCESSING.getCode());
        //保存积分兑换记录
        this.pointExchangeRecordService.save(pointExchangeRecord);

        //准备积分变动记录
        UserPointChangeRecord userPointChangeRecord = new UserPointChangeRecord();
        userPointChangeRecord.setUserId(userId);
        userPointChangeRecord.setChangeType(PointChangeTypeEnums.OUTCOME.getCode());
        userPointChangeRecord.setChangePoint(pointCost);
        userPointChangeRecord.setRelatedId(pointExchangeRecord.getId());
        //保存积分变动记录
        this.userPointChangeRecordService.save(userPointChangeRecord);

        //扣减用户积分
        boolean success = deductPoint(userId, pointCost);
        if (!success) {
            throw new BusinessException(HttpStatus.BAD_REQUEST.value(), "积分扣减失败");
        }

        //准备提现记录
        UserWithdrawRecord userWithdrawRecord = new UserWithdrawRecord();
        userWithdrawRecord.setUserId(userId);
        userWithdrawRecord.setWithdrawAmount(applyAmount);
        userWithdrawRecord.setExchangeRecordId(pointExchangeRecord.getId());
        userWithdrawRecord.setRequestedAt(now);
        //保存提现记录
        this.userWithdrawRecordService.save(userWithdrawRecord);
    }

    /**
     * @param userId:
     * @param pointCost:
     * @return boolean:
     * @Author: Panda
     * @Description: 扣减用户积分
     * @Date: 2025/6/4 17:40
     */
    private boolean deductPoint(Long userId, Integer pointCost) {
        return this.userExtensionService.update(new LambdaUpdateWrapper<UserExtension>()
                .eq(UserExtension::getUserId, userId)
                .ge(UserExtension::getPoint, pointCost)
                .setSql("point = point - " + pointCost)
        );
    }
}
