package com.caicai.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName: TaskDefinitionVO
 * @Description: 任务定义实体VO
 * @Author: PCT
 * @Date: 2025/5/22 15:45
 * @Version: 1.0
 */

@Data
public class TaskDefinitionVO {
    private Long id;
    private String title;
    private Long rewardPoint;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;
    private Integer sortOrder;
    //是否可继续完成
    private Boolean canDo = true;
    private String taskActionName;
    private String taskActionCode;
}
