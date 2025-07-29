package com.caicai.controller;

import com.caicai.entity.dto.TaskDTO;
import com.caicai.entity.vo.TaskDefinitionVO;
import com.caicai.result.R;
import com.caicai.service.TaskDefinitionService;
import com.caicai.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: TaskController
 * @Description: 任务相关Controller
 * @Author: PCT
 * @Date: 2025/5/22 16:25
 * @Version: 1.0
 */

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskDefinitionService taskDefinitionService;
    private final TaskService taskService;

    /**
     * @param userId:
     * @return R<List<TaskDefinitionVO>>:
     * @Author: Panda
     * @Description: 查询当前用户对应的任务列表 包含任务状态
     * @Date: 2025/5/23 17:29
     */
    @GetMapping
    public R<List<TaskDefinitionVO>> getTaskListByUserId(@RequestAttribute("userId") Long userId) {
        List<TaskDefinitionVO> taskVoList = taskDefinitionService.getTaskDefinitionList(userId);
        return R.success(taskVoList);
    }

    @PostMapping("/complete")
    public R<Void> completeTask(@RequestAttribute("userId") Long userId, @RequestBody TaskDTO taskDTO) {
        String result = taskService.handleTask(userId, taskDTO.getTaskId());
        return R.success(null, result);
    }
}
