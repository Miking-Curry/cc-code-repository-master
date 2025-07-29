package com.caicai.service;

import com.caicai.enums.TaskActionTypeEnums;

/**
 * @ClassName: TaskStrategy
 * @Description: 任务策略接口
 * @Author: PCT
 * @Date: 2025/6/9 9:54
 * @Version: 1.0
 */

public interface TaskService {
    
    /**
     * @param: This method has no parameters.
     * @return TaskActionTypeEnums:
     * @Author: Panda
     * @Description: 当前任务支持的任务类型
     * @Date: 2025/6/9 10:41
     */
    TaskActionTypeEnums getTaskActionType();
    
    
    /**
     * @param userId:
     * @param taskId:
     * @return: String
     * @Author: Panda
     * @Description: 此时taskId为可完成 直接实现完成任务业务
     * @Date: 2025/6/9 10:46
     */
    String handleTask(Long userId, Long taskId);
}
