package com.caicai.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 可兑换物品表
 * @TableName exchangeable_item
 */
@TableName(value ="exchangeable_item")
@Data
public class ExchangeableItem {
    /**
     * 可兑换物品ID，主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 可兑换物品名称
     */
    @TableField(value = "title")
    private String title;

    /**
     * 可兑换物品类型ID，外键
     */
    @TableField(value = "item_type_id")
    private Integer itemTypeId;

    /**
     * 兑换所需积分
     */
    @TableField(value = "point_cost")
    private Integer pointCost;

    /**
     * 可用库存数量
     */
    @TableField(value = "available_stock")
    private Integer availableStock;

    /**
     * 提现金额(仅提现)
     */
    @TableField(value = "amount")
    private BigDecimal amount;

    /**
     * 是否可见
     */
    @TableField(value = "is_visible")
    private Integer isVisible;

    /**
     * 是否启用
     */
    @TableField(value = "is_enabled")
    private Integer isEnabled;

    /**
     * 兑换开始时间
     */
    @TableField(value = "start_time")
    private LocalDateTime startTime;

    /**
     * 兑换结束时间
     */
    @TableField(value = "end_time")
    private LocalDateTime endTime;

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
        ExchangeableItem other = (ExchangeableItem) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getItemTypeId() == null ? other.getItemTypeId() == null : this.getItemTypeId().equals(other.getItemTypeId()))
            && (this.getPointCost() == null ? other.getPointCost() == null : this.getPointCost().equals(other.getPointCost()))
            && (this.getAvailableStock() == null ? other.getAvailableStock() == null : this.getAvailableStock().equals(other.getAvailableStock()))
            && (this.getAmount() == null ? other.getAmount() == null : this.getAmount().equals(other.getAmount()))
            && (this.getIsVisible() == null ? other.getIsVisible() == null : this.getIsVisible().equals(other.getIsVisible()))
            && (this.getIsEnabled() == null ? other.getIsEnabled() == null : this.getIsEnabled().equals(other.getIsEnabled()))
            && (this.getStartTime() == null ? other.getStartTime() == null : this.getStartTime().equals(other.getStartTime()))
            && (this.getEndTime() == null ? other.getEndTime() == null : this.getEndTime().equals(other.getEndTime()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getItemTypeId() == null) ? 0 : getItemTypeId().hashCode());
        result = prime * result + ((getPointCost() == null) ? 0 : getPointCost().hashCode());
        result = prime * result + ((getAvailableStock() == null) ? 0 : getAvailableStock().hashCode());
        result = prime * result + ((getAmount() == null) ? 0 : getAmount().hashCode());
        result = prime * result + ((getIsVisible() == null) ? 0 : getIsVisible().hashCode());
        result = prime * result + ((getIsEnabled() == null) ? 0 : getIsEnabled().hashCode());
        result = prime * result + ((getStartTime() == null) ? 0 : getStartTime().hashCode());
        result = prime * result + ((getEndTime() == null) ? 0 : getEndTime().hashCode());
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
        sb.append(", name=").append(title);
        sb.append(", itemTypeId=").append(itemTypeId);
        sb.append(", pointCost=").append(pointCost);
        sb.append(", availableStock=").append(availableStock);
        sb.append(", amount=").append(amount);
        sb.append(", isVisible=").append(isVisible);
        sb.append(", isEnabled=").append(isEnabled);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append("]");
        return sb.toString();
    }
}