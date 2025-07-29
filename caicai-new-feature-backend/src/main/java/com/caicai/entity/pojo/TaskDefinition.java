package com.caicai.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 任务定义表
 *
 * @TableName task_definition
 */
@TableName(value = "task_definition")
@Data
public class TaskDefinition {
    /**
     * 任务定义ID，主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 任务标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 每次完成任务奖励积分
     */
    @TableField(value = "reward_point")
    private Integer rewardPoint;

    /**
     * 任务类型ID，外键
     */
    @TableField(value = "task_type_id")
    private Integer taskTypeId;

    /**
     * 任务行为类型ID，外键
     */
    @TableField(value = "task_action_type_id")
    private Integer taskActionTypeId;

    /**
     * 任务描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 每个周期最大完成次数
     */
    @TableField(value = "max_count_per_cycle")
    private Integer maxCountPerCycle;

    /**
     * 刷新间隔(小时)
     */
    @TableField(value = "refresh_interval_hours")
    private Integer refreshIntervalHours;

    /**
     * 任务开始时间(限时任务)
     */
    @TableField(value = "start_time")
    private LocalDateTime startTime;

    /**
     * 任务结束时间(限时任务)
     */
    @TableField(value = "end_time")
    private LocalDateTime endTime;

    /**
     * 排序顺序
     */
    @TableField(value = "sort_order")
    private Integer sortOrder;

    /**
     * 是否启用
     */
    @TableField(value = "is_enable")
    private Integer isEnable;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TaskDefinition other = (TaskDefinition) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
                && (this.getRewardPoint() == null ? other.getRewardPoint() == null : this.getRewardPoint().equals(other.getRewardPoint()))
                && (this.getTaskTypeId() == null ? other.getTaskTypeId() == null : this.getTaskTypeId().equals(other.getTaskTypeId()))
                && (this.getTaskActionTypeId() == null ? other.getTaskActionTypeId() == null : this.getTaskActionTypeId().equals(other.getTaskActionTypeId()))
                && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
                && (this.getMaxCountPerCycle() == null ? other.getMaxCountPerCycle() == null : this.getMaxCountPerCycle().equals(other.getMaxCountPerCycle()))
                && (this.getRefreshIntervalHours() == null ? other.getRefreshIntervalHours() == null : this.getRefreshIntervalHours().equals(other.getRefreshIntervalHours()))
                && (this.getStartTime() == null ? other.getStartTime() == null : this.getStartTime().equals(other.getStartTime()))
                && (this.getEndTime() == null ? other.getEndTime() == null : this.getEndTime().equals(other.getEndTime()))
                && (this.getSortOrder() == null ? other.getSortOrder() == null : this.getSortOrder().equals(other.getSortOrder()))
                && (this.getIsEnable() == null ? other.getIsEnable() == null : this.getIsEnable().equals(other.getIsEnable()))
                && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
                && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getRewardPoint() == null) ? 0 : getRewardPoint().hashCode());
        result = prime * result + ((getTaskTypeId() == null) ? 0 : getTaskTypeId().hashCode());
        result = prime * result + ((getTaskActionTypeId() == null) ? 0 : getTaskActionTypeId().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getMaxCountPerCycle() == null) ? 0 : getMaxCountPerCycle().hashCode());
        result = prime * result + ((getRefreshIntervalHours() == null) ? 0 : getRefreshIntervalHours().hashCode());
        result = prime * result + ((getStartTime() == null) ? 0 : getStartTime().hashCode());
        result = prime * result + ((getEndTime() == null) ? 0 : getEndTime().hashCode());
        result = prime * result + ((getSortOrder() == null) ? 0 : getSortOrder().hashCode());
        result = prime * result + ((getIsEnable() == null) ? 0 : getIsEnable().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", title=").append(title);
        sb.append(", rewardPoint=").append(rewardPoint);
        sb.append(", taskTypeId=").append(taskTypeId);
        sb.append(", taskActionTypeId=").append(taskActionTypeId);
        sb.append(", description=").append(description);
        sb.append(", maxCountPerCycle=").append(maxCountPerCycle);
        sb.append(", refreshIntervalHours=").append(refreshIntervalHours);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", sortOrder=").append(sortOrder);
        sb.append(", isEnable=").append(isEnable);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append("]");
        return sb.toString();
    }
}