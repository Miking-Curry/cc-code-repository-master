package com.caicai.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caicai.entity.pojo.PointExchangeRecord;
import com.caicai.entity.pojo.TaskDefinition;
import com.caicai.entity.pojo.UserPointChangeRecord;
import com.caicai.entity.vo.UserPointChangeRecordVO;
import com.caicai.entity.vo.UserPointCountVO;
import com.caicai.enums.PointChangeTypeEnums;
import com.caicai.mapper.PointExchangeRecordMapper;
import com.caicai.mapper.TaskDefinitionMapper;
import com.caicai.service.UserPointChangeRecordService;
import com.caicai.mapper.UserPointChangeRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author CCN
 * @description 针对表【user_point_change_record(用户积分变动明细表)】的数据库操作Service实现
 * @createDate 2025-06-03 11:49:15
 */
@Service
@RequiredArgsConstructor
public class UserPointChangeRecordServiceImpl extends ServiceImpl<UserPointChangeRecordMapper, UserPointChangeRecord>
        implements UserPointChangeRecordService {

    private final TaskDefinitionMapper taskDefinitionMapper;
    private final PointExchangeRecordMapper pointExchangeRecordMapper;

    /**
     * @param userId:
     * @return List<UserPointChangeRecordVO>:
     * @Author: Panda
     * @Description: 根据用户id查询对应积分变动记录
     * @Date: 2025/6/3 15:39
     */
    @Override
    public List<UserPointChangeRecordVO> getUserPointChangeRecord(Long userId) {
        //根据用户id查询所有积分记录
        List<UserPointChangeRecord> userPointChangeRecords
                = this.lambdaQuery().eq(UserPointChangeRecord::getUserId, userId).list();

        //不存在积分记录->返回空集合
        if(CollUtil.isEmpty(userPointChangeRecords)){
            return Collections.emptyList();
        }

        //获取所有任务
        List<TaskDefinition> taskList = taskDefinitionMapper.selectList(null);

        //获取当前用户所有兑换记录
        List<PointExchangeRecord> pointExchangeRecords = pointExchangeRecordMapper
                .selectList(
                        new LambdaQueryWrapper<PointExchangeRecord>()
                                .eq(PointExchangeRecord::getUserId, userId)
                );

        //将pojo转为VO
        return userPointChangeRecords.stream()
                .map(userPointChangeRecord -> UserPointChangeRecordVO.builder()
                        .userId(userPointChangeRecord.getUserId())
                        .changeType(Optional.ofNullable(PointChangeTypeEnums.ofCode(userPointChangeRecord.getChangeType()))
                                .map(PointChangeTypeEnums::getDesc)
                                .orElse("未知类型")
                        )
                        .changePoint(userPointChangeRecord.getChangePoint())
                        .eventDescription(userPointChangeRecord.getChangeType()
                                .equals(PointChangeTypeEnums.INCOME.getCode()) ?
                                //收入-任务标题
                                taskList.stream()
                                        .filter(task ->
                                                task.getId().equals(userPointChangeRecord.getEventId())
                                        )
                                        .findFirst()
                                        .orElse(new TaskDefinition())
                                        .getTitle()
                                :
                                //支出-兑换记录标题
                                pointExchangeRecords.stream()
                                        .filter(pointExchangeRecord ->
                                                pointExchangeRecord.getId().equals(userPointChangeRecord.getRelatedId())
                                        )
                                        .findFirst()
                                        .orElse(new PointExchangeRecord())
                                        .getItemTitle()
                        )
                        .changeTime(userPointChangeRecord.getCreatedAt())
                        .build()
                )
                .toList();
    }

    /**
     * @param userId:
     * @return UserPointCountVO:
     * @Author: Panda
     * @Description: 获取当日积分总收入
     * @Date: 2025/6/4 10:58
     */
    @Override
    public UserPointCountVO getUserTodayPointCount(Long userId) {
        List<UserPointChangeRecord> allIncomeRecord = this.lambdaQuery()
                .eq(UserPointChangeRecord::getUserId, userId)
                .eq(UserPointChangeRecord::getChangeType, 1) //仅统计收入
                .apply("DATE(created_at) = CURDATE()")
                .list();

        if(CollUtil.isEmpty(allIncomeRecord)){
            return UserPointCountVO.builder()
                    .userId(userId)
                    .pointCount(0)
                    .build();
        }

        int todayPointCount = allIncomeRecord.stream()
                .mapToInt(UserPointChangeRecord::getChangePoint)
                .sum();

        return UserPointCountVO.builder()
                .userId(userId)
                .pointCount(todayPointCount)
                .build();
    }

    /**
     * @param userId:
     * @return UserPointCountVO:
     * @Author: Panda
     * @Description: 获取用户历史积分总收入
     * @Date: 2025/6/4 11:11
     */
    @Override
    public UserPointCountVO getUserAllPointIncome(Long userId) {

        List<UserPointChangeRecord> allIncomeRecord = this.lambdaQuery()
                .eq(UserPointChangeRecord::getUserId, userId)
                .eq(UserPointChangeRecord::getChangeType, 1) //仅统计收入
                .list();

        if(CollUtil.isEmpty(allIncomeRecord)){
            return UserPointCountVO.builder()
                    .userId(userId)
                    .pointCount(0)
                    .build();
        }

        int todayPointCount = allIncomeRecord.stream()
                .mapToInt(UserPointChangeRecord::getChangePoint)
                .sum();

        return UserPointCountVO.builder()
                .userId(userId)
                .pointCount(todayPointCount)
                .build();
    }
}




