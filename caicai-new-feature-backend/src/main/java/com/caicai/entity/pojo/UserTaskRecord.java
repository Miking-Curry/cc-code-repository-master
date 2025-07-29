package com.caicai.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

/**
 * 用户任务完成记录表
 * @TableName user_task_record
 */
@TableName(value ="user_task_record")
@Data
@Builder
public class UserTaskRecord {
    /**
     * 用户任务记录ID，主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID，外键
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 任务定义ID，外键
     */
    @TableField(value = "task_id")
    private Long taskId;

    /**
     * 任务周期开始时间(配合task_type)
     */
    @TableField(value = "period_start")
    private LocalDateTime periodStart;

    /**
     * 任务周期结束时间(配合task_type)
     */
    @TableField(value = "period_end")
    private LocalDateTime periodEnd;

    /**
     * 当前周期内任务完成次数
     */
    @TableField(value = "finish_count")
    private Integer finishCount;

    /**
     * 任务状态(0进行中 1已完成 2已超时)
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 周期内任务最后一次完成时间
     */
    @TableField(value = "last_finished_at")
    private LocalDateTime lastFinishedAt;

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
        UserTaskRecord other = (UserTaskRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getTaskId() == null ? other.getTaskId() == null : this.getTaskId().equals(other.getTaskId()))
            && (this.getPeriodStart() == null ? other.getPeriodStart() == null : this.getPeriodStart().equals(other.getPeriodStart()))
            && (this.getPeriodEnd() == null ? other.getPeriodEnd() == null : this.getPeriodEnd().equals(other.getPeriodEnd()))
            && (this.getFinishCount() == null ? other.getFinishCount() == null : this.getFinishCount().equals(other.getFinishCount()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getLastFinishedAt() == null ? other.getLastFinishedAt() == null : this.getLastFinishedAt().equals(other.getLastFinishedAt()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getTaskId() == null) ? 0 : getTaskId().hashCode());
        result = prime * result + ((getPeriodStart() == null) ? 0 : getPeriodStart().hashCode());
        result = prime * result + ((getPeriodEnd() == null) ? 0 : getPeriodEnd().hashCode());
        result = prime * result + ((getFinishCount() == null) ? 0 : getFinishCount().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getLastFinishedAt() == null) ? 0 : getLastFinishedAt().hashCode());
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
        sb.append(", userId=").append(userId);
        sb.append(", taskId=").append(taskId);
        sb.append(", periodStart=").append(periodStart);
        sb.append(", periodEnd=").append(periodEnd);
        sb.append(", finishCount=").append(finishCount);
        sb.append(", status=").append(status);
        sb.append(", lastFinishedAt=").append(lastFinishedAt);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append("]");
        return sb.toString();
    }
}