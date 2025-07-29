package com.caicai.service;

import com.caicai.entity.pojo.UserTaskRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author CCN
* @description 针对表【user_task_record(用户任务完成记录表)】的数据库操作Service
* @createDate 2025-05-22 17:29:49
*/
public interface UserTaskRecordService extends IService<UserTaskRecord> {
    /**
     * @param userId:
     * @return List<UserTaskRecord>:
     * @Author: Panda
     * @Description: 获取用户所有已完成的任务记录
     * @Date: 2025/6/10 15:48
     */
    List<UserTaskRecord> getUserTaskRecordListByUserId(Long userId);

    /**
     * @param userTaskRecord:
     * @return: This method has no return value.
     * @Author: Panda
     * @Description: 保存用户已完成的任务记录
     * @Date: 2025/6/10 15:48
     */
    UserTaskRecord saveUserTaskRecord(UserTaskRecord userTaskRecord);
}
