package com.caicai.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caicai.entity.pojo.TaskDefinition;
import com.caicai.entity.pojo.UserTaskRecord;
import com.caicai.entity.vo.TaskDefinitionVO;
import com.caicai.enums.TaskActionTypeEnums;
import com.caicai.enums.TaskTypeEnums;
import com.caicai.service.TaskDefinitionService;
import com.caicai.mapper.TaskDefinitionMapper;
import com.caicai.service.UserTaskRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @author CCN
 * @description 针对表【task_definition(任务定义表)】的数据库操作Service实现
 * @createDate 2025-05-21 15:25:52
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TaskDefinitionServiceImpl extends ServiceImpl<TaskDefinitionMapper, TaskDefinition>
        implements TaskDefinitionService {

    private final UserTaskRecordService userTaskRecordService;

    /**
     * @Author PCT
     * @Description: 获取当前可用的任务列表 并根据用户任务完成记录判断任务是否可继续完成
     * @Date 2025/5/22 16:05
     * @Param: This method has no parameter.
     * @Return java.util.List<com.caicai.entity.vo.TaskDefinitionVO>:
     */
    public List<TaskDefinitionVO> getTaskDefinitionList(Long userId) {
        //获取当前时间
        LocalDateTime now = LocalDateTime.now();

        //查询当前可用任务
        List<TaskDefinition> taskList = this.getAvailableTaskList(now);
        if (taskList == null) {
            return Collections.emptyList();
        }

        //查询一次性任务
        List<Long> onceTaskIdList = this.getOnceTaskIdList(taskList);

        //查询当前用户任务完成情况
        List<UserTaskRecord> userTaskRecordList = userTaskRecordService.getUserTaskRecordListByUserId(userId);

        //获取一次性任务中用户已完成的任务id
        List<Long> finishedOnceTaskIdList = this.getFinishedOnceTaskIdList(userTaskRecordList, onceTaskIdList);

        //获取获取任务最大完成次数
        Map<Long, Integer> taskMaxCount = this.getTaskMaxCount(taskList);

        //获取周期内已完成的任务id
        List<Long> finishedTaskIdList = this.getFinishedTaskIdList(taskList, taskMaxCount, userTaskRecordList, now);

        //合并已完成的任务id
        List<Long> allFinishedTaskIdList = Stream.of(finishedTaskIdList, finishedOnceTaskIdList)
                .flatMap(List::stream)
                .distinct()
                .toList();

        //获取并返回TaskVOList
        return this.getTaskDefinitionVOList(taskList, allFinishedTaskIdList);
    }

    /**
     * @param taskId:
     * @param userId:
     * @return Boolean:
     * @Author: Panda
     * @Description: 根据用户id与任务id判断任务是否可完成
     * @Date: 2025/6/10 15:38
     */
    @Override
    public Boolean getTaskStatusByIdAndUserId(Long taskId, Long userId) {
        //获取当前用户所有可完成的任务
        List<TaskDefinitionVO> taskVOList = this.getTaskDefinitionList(userId);
        //判断任务是否可继续完成
        List<Long> allCandoTaskIdList = taskVOList.stream()
                .filter(taskVO -> taskVO.getCanDo().equals(true))
                .map(TaskDefinitionVO::getId)
                .toList();
        return CollUtil.isNotEmpty(allCandoTaskIdList) && allCandoTaskIdList.contains(taskId);
    }

    /**
     * @param taskId:
     * @return Integer:
     * @Author: Panda
     * @Description: 根据任务id获取任务类型id
     * @Date: 2025/6/10 16:51
     */
    @Override
    public Integer getTaskTypeIdByTaskId(Long taskId) {
        return this.getById(taskId).getTaskTypeId();
    }

    /**
     * @param taskList:
     * @param allFinishedTaskIdList:
     * @return List<TaskDefinitionVO>:
     * @Author: Panda
     * @Description: 将TaskDefinition转为TaskDefinitionVO 并将已完成的任务id标记canDo为false
     * @Date: 2025/6/10 12:03
     */
    private List<TaskDefinitionVO> getTaskDefinitionVOList(List<TaskDefinition> taskList, List<Long> allFinishedTaskIdList) {
        return taskList.stream()
                .map(task -> {
                    TaskDefinitionVO taskVO = BeanUtil.toBean(task, TaskDefinitionVO.class);
                    TaskActionTypeEnums taskActionType = TaskActionTypeEnums.ofId(task.getTaskActionTypeId());
                    if (taskActionType != null) {
                        taskVO.setTaskActionCode(taskActionType.getCode());
                        taskVO.setTaskActionName(taskActionType.getName());
                    }
                    return taskVO;
                })
                .peek(taskVO -> {
                    if (allFinishedTaskIdList.contains(taskVO.getId())) {
                        taskVO.setCanDo(false);
                    }
                })
                .toList();
    }

    /**
     * @param now:
     * @return List<TaskDefinition>:
     * @Author: Panda
     * @Description: 获取当前所有可用任务
     * @Date: 2025/6/10 11:57
     */
    private List<TaskDefinition> getAvailableTaskList(LocalDateTime now) {
        return this.lambdaQuery()
                .eq(TaskDefinition::getIsEnable, true)
                .and(lqw -> lqw
                        .isNull(TaskDefinition::getStartTime)
                        .or()
                        .le(TaskDefinition::getStartTime, now)
                )
                .and(lqw -> lqw
                        .isNull(TaskDefinition::getEndTime)
                        .or()
                        .ge(TaskDefinition::getEndTime, now)
                )
                .list();
    }

    /**
     * @param userTaskRecordList:
     * @param taskId:
     * @param now:
     * @return int:
     * @Author: Panda
     * @Description: 获取指定任务在当前周期的完成次数
     * @Date: 2025/5/23 11:37
     */
    private int getFinishCountInCurrentCycle(List<UserTaskRecord> userTaskRecordList, Long taskId, LocalDateTime now) {
        return userTaskRecordList.stream()
                .filter(userTaskRecord -> userTaskRecord.getTaskId().equals(taskId))
                .filter(userTaskRecord -> !now.isBefore(userTaskRecord.getPeriodStart()) && !now.isAfter(userTaskRecord.getPeriodEnd()))
                .mapToInt(UserTaskRecord::getFinishCount)
                .findFirst()
                .orElse(0);
    }

    /**
     * @param taskList:
     * @return Map<Integer>:
     * @Author: Panda
     * @Description: 获取任务最大完成次数
     * @Date: 2025/5/23 16:03
     */
    private Map<Long, Integer> getTaskMaxCount(List<TaskDefinition> taskList) {
        return taskList.stream()
                .filter(task -> task.getMaxCountPerCycle() != null && task.getMaxCountPerCycle() > 0)
                .collect(Collectors.toMap(TaskDefinition::getId, TaskDefinition::getMaxCountPerCycle));
    }

    /**
     * @param taskList:
     * @return List<Long>:
     * @Author: Panda
     * @Description: 查询一次性任务id
     * @Date: 2025/5/23 16:12
     */
    private List<Long> getOnceTaskIdList(List<TaskDefinition> taskList) {
        log.info("查询一次性任务id");
        return taskList.stream()
                .filter(task -> task.getTaskTypeId().equals(TaskTypeEnums.ONCE.getTaskTypeId()))
                .map(TaskDefinition::getId)
                .toList();
    }

    /**
     * @param userTaskRecordList:
     * @return List<Long>:
     * @Author: Panda
     * @Description: 获取当前用户已完成的一次性任务id
     * @Date: 2025/5/23 16:11
     */
    private List<Long> getFinishedOnceTaskIdList(List<UserTaskRecord> userTaskRecordList, List<Long> onceTaskIdList) {
        log.info("获取当前用户已完成的一次性任务id");
        //获取一次性任务中用户已完成的任务id
        return userTaskRecordList.stream()
                .map(UserTaskRecord::getTaskId)
                .filter(onceTaskIdList::contains)
                .toList();
    }

    /**
     * @param taskList:
     * @param taskMaxCount: 一次性任务无最大完成次数 因此不含一次性任务
     * @param userTaskRecordList:
     * @param now:
     * @return List<Long>:
     * @Author: Panda
     * @Description: 获取除一次性任务外 已完成的任务id
     * @Date: 2025/5/23 16:18
     */
    private List<Long> getFinishedTaskIdList(List<TaskDefinition> taskList,
                                             Map<Long, Integer> taskMaxCount,
                                             List<UserTaskRecord> userTaskRecordList,
                                             LocalDateTime now) {
        log.info("获取除一次性任务外 已完成的任务id");
        return taskList.stream()
                .map(TaskDefinition::getId)
                .filter(id ->
                        taskMaxCount.containsKey(id)
                        &&
                        this.getFinishCountInCurrentCycle(userTaskRecordList, id, now) >= taskMaxCount.get(id)
                )
                .toList();
    }
}
