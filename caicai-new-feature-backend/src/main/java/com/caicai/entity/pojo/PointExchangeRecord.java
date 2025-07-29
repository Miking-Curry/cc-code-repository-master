package com.caicai.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * 用户积分兑换记录表
 * @TableName point_exchange_record
 */
@TableName(value ="point_exchange_record")
@Data
public class PointExchangeRecord {
    /**
     * 积分兑换记录ID，主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID，外键
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 兑换物品ID，外键
     */
    @TableField(value = "item_title")
    private String itemTitle;

    /**
     * 兑换消耗积分数量
     */
    @TableField(value = "point_cost")
    private Integer pointCost;

    /**
     * 积分兑换状态ID，外键
     */
    @TableField(value = "tx_status")
    private String txStatus;

    /**
     * 兑换处理完成时间
     */
    @TableField(value = "processed_at")
    private LocalDateTime processedAt;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

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
        PointExchangeRecord other = (PointExchangeRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getItemTitle() == null ? other.getItemTitle() == null : this.getItemTitle().equals(other.getItemTitle()))
            && (this.getPointCost() == null ? other.getPointCost() == null : this.getPointCost().equals(other.getPointCost()))
            && (this.getTxStatus() == null ? other.getTxStatus() == null : this.getTxStatus().equals(other.getTxStatus()))
            && (this.getProcessedAt() == null ? other.getProcessedAt() == null : this.getProcessedAt().equals(other.getProcessedAt()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getItemTitle() == null) ? 0 : getItemTitle().hashCode());
        result = prime * result + ((getPointCost() == null) ? 0 : getPointCost().hashCode());
        result = prime * result + ((getTxStatus() == null) ? 0 : getTxStatus().hashCode());
        result = prime * result + ((getProcessedAt() == null) ? 0 : getProcessedAt().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
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
        sb.append(", itemId=").append(itemTitle);
        sb.append(", pointCost=").append(pointCost);
        sb.append(", txStatusId=").append(txStatus);
        sb.append(", processedAt=").append(processedAt);
        sb.append(", createdAt=").append(createdAt);
        sb.append("]");
        return sb.toString();
    }
}