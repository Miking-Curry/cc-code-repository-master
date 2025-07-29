package com.caicai.service;

import com.caicai.entity.pojo.TaskDefinition;
import com.baomidou.mybatisplus.extension.service.IService;
import com.caicai.entity.vo.TaskDefinitionVO;

import java.util.List;

/**
* @author CCN
* @description 针对表【task_definition(任务定义表)】的数据库操作Service
* @createDate 2025-05-21 15:25:52
*/
public interface TaskDefinitionService extends IService<TaskDefinition> {

    /**
     * @param userId:
     * @return List<TaskDefinitionVO>:
     * @Author: Panda
     * @Description: 查询当前用户对应的任务列表 包含任务状态
     * @Date: 2025/6/3 9:08
     */
    List<TaskDefinitionVO> getTaskDefinitionList(Long userId);

    /**
     * @param taskId:
     * @param userId:
     * @return Boolean:
     * @Author: Panda
     * @Description: 根据任务id及用户id判断任务是否可完成
     * @Date: 2025/6/10 15:32
     */
    Boolean getTaskStatusByIdAndUserId(Long taskId, Long userId);

    /**
     * @param taskId:
     * @return Integer:
     * @Author: Panda
     * @Description: 根据任务id获取任务类型id
     * @Date: 2025/6/10 16:50
     */
    Integer getTaskTypeIdByTaskId(Long taskId);
}
