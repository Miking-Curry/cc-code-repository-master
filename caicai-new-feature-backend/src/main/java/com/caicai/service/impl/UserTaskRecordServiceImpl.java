package com.caicai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caicai.entity.pojo.UserTaskRecord;
import com.caicai.enums.TaskCompletedStatusEnums;
import com.caicai.service.UserTaskRecordService;
import com.caicai.mapper.UserTaskRecordMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
* @author CCN
* @description 针对表【user_task_record(用户任务完成记录表)】的数据库操作Service实现
* @createDate 2025-05-22 17:29:49
*/
@Service
public class UserTaskRecordServiceImpl extends ServiceImpl<UserTaskRecordMapper, UserTaskRecord>
    implements UserTaskRecordService{

    /**
     * @param userId:
     * @return List<UserTaskRecord>:
     * @Author: Panda
     * @Description: 查询当前用户任务完成情况
     * @Date: 2025/5/23 15:34
     */
    public List<UserTaskRecord> getUserTaskRecordListByUserId(Long userId) {
        //查询当前用户任务完成情况
        LambdaQueryWrapper<UserTaskRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserTaskRecord::getUserId, userId);
        List<UserTaskRecord> userTaskRecordList = this.list(wrapper);
        return userTaskRecordList;
    }

    /**
     * @param userTaskRecord:
     * @return: This method has no return value.
     * @Author: Panda
     * @Description: 保存用户已完成的任务记录
     * @Date: 2025/6/10 15:49
     */
    @Override
    public UserTaskRecord saveUserTaskRecord(UserTaskRecord userTaskRecord) {
        //查询当前需添加的任务记录是否存在(用户id 任务id 周期)
        UserTaskRecord userTaskRecordByIdAndUserId = this.getUserTaskRecordByIdAndUserId(
                userTaskRecord.getTaskId(), userTaskRecord.getUserId()
        );

        //存在->更新 累计完成次数
        if (userTaskRecordByIdAndUserId != null) {
            Integer finishCount = userTaskRecordByIdAndUserId.getFinishCount();
            userTaskRecordByIdAndUserId.setFinishCount(finishCount + 1);
            this.updateById(userTaskRecordByIdAndUserId);
            return userTaskRecordByIdAndUserId;
        }

        //不存在->新增
        this.save(userTaskRecord);
        return userTaskRecord;
    }

    /**
     * @param taskId:
     * @param userId:
     * @return UserTaskRecord:
     * @Author: Panda
     * @Description: 根据用户id与任务id查询当前周期内的任务完成记录
     * @Date: 2025/6/10 16:48
     */
    private UserTaskRecord getUserTaskRecordByIdAndUserId(Long taskId, Long userId) {
        LocalDateTime now = LocalDateTime.now();
        return this.lambdaQuery()
                .eq(UserTaskRecord::getUserId, userId)
                .eq(UserTaskRecord::getTaskId, taskId)
                .le(UserTaskRecord::getPeriodStart, now)
                .ge(UserTaskRecord::getPeriodEnd, now)
                .one();
    }
}




