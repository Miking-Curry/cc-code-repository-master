package com.caicai.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.caicai.entity.model.TaskPeriod;
import com.caicai.entity.pojo.TaskDefinition;
import com.caicai.entity.pojo.UserExtension;
import com.caicai.entity.pojo.UserPointChangeRecord;
import com.caicai.entity.pojo.UserTaskRecord;
import com.caicai.enums.PointChangeTypeEnums;
import com.caicai.enums.TaskActionTypeEnums;
import com.caicai.enums.TaskCompletedStatusEnums;
import com.caicai.enums.TaskTypeEnums;
import com.caicai.exception.BusinessException;
import com.caicai.service.TaskDefinitionService;
import com.caicai.service.UserExtensionService;
import com.caicai.service.UserPointChangeRecordService;
import com.caicai.service.UserTaskRecordService;
import com.caicai.strategy.TaskPeriodStrategy;
import com.caicai.service.TaskService;
import com.caicai.strategy.strategyFactory.TaskPeriodStrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @ClassName: SignInTaskStrategy
 * @Description: 登录任务策略实现类
 * @Author: PCT
 * @Date: 2025/6/9 11:08
 * @Version: 1.0
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskDefinitionService taskDefinitionService;
    private final UserTaskRecordService userTaskRecordService;
    private final UserPointChangeRecordService userPointChangeRecordService;
    private final UserExtensionService userExtensionService;

    @Override
    public TaskActionTypeEnums getTaskActionType() {
        return TaskActionTypeEnums.SIGN_IN;
    }

    @Transactional
    @Override
    public String handleTask(Long userId, Long taskId) {
        log.info("开始执行完成任务逻辑 userId: {}, taskId: {}", userId, taskId);

        //获取任务对象
        TaskDefinition currentTask = taskDefinitionService.getById(taskId);
        if (ObjectUtil.isEmpty(currentTask)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST.value(), "任务不存在");
        }

        Boolean taskStatus = taskDefinitionService.getTaskStatusByIdAndUserId(taskId, userId);
        if (!taskStatus) {
            //任务不可完成
            log.error("当前任务不可继续完成");
            return "任务完成失败 当前任务已完成";
        }

        //1.保存任务记录
        //获取任务周期时间
        LocalDateTime now = LocalDateTime.now();
        Integer taskTypeId = currentTask.getTaskTypeId();
        TaskTypeEnums taskType = TaskTypeEnums.ofId(taskTypeId);
        TaskPeriodStrategyFactory taskPeriodStrategyFactory = new TaskPeriodStrategyFactory();
        TaskPeriodStrategy taskPeriodStrategy = taskPeriodStrategyFactory.getTaskPeriodStrategy(taskType);
        TaskPeriod taskPeriod = taskPeriodStrategy.getTaskPeriod(now, currentTask);

        //构建任务记录
        UserTaskRecord userTaskRecord = UserTaskRecord.builder()
                .taskId(taskId)
                .userId(userId)
                .finishCount(1)
                .lastFinishedAt(now)
                .status(TaskCompletedStatusEnums.IN_PROGRESS.getMark())
                .periodStart(taskPeriod.getPeriodStart())
                .periodEnd(taskPeriod.getPeriodEnd())
                .build();

        UserTaskRecord savedUserTaskRecord = userTaskRecordService.saveUserTaskRecord(userTaskRecord);

        //获取该任务可得积分
        Integer point = currentTask.getRewardPoint();
        log.info("任务对应积分 -> {}", point);

        //保存积分记录
        UserPointChangeRecord userPointChangeRecord = new UserPointChangeRecord()
                .setUserId(userId)
                .setChangePoint(point)
                .setChangeType(PointChangeTypeEnums.INCOME.getCode())
                .setRelatedId(savedUserTaskRecord.getId())
                .setEventId(taskId);
        userPointChangeRecordService.save(userPointChangeRecord);


        //计入用户总积分
        UserExtension userExtension = userExtensionService.lambdaQuery()
                .eq(UserExtension::getUserId, userId)
                .one();

        if (userExtension == null) {
            //不存在用户扩展信息->新增
            userExtension = new UserExtension()
                    .setUserId(userId)
                    .setPoint(point.longValue());
            userExtensionService.save(userExtension);
        }

        Long currentPoint = userExtension.getPoint();
        currentPoint += point;
        userExtension.setPoint(currentPoint);
        userExtensionService.updateById(userExtension);

        return "任务完成成功";
    }
}
