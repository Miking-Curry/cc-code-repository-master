package com.caicai.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 用户提现记录表
 * @TableName user_withdraw_record
 */
@TableName(value ="user_withdraw_record")
@Data
public class UserWithdrawRecord {
    /**
     * 用户提现记录ID，主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID，外键
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 兑换记录ID，外键
     */
    @TableField(value = "exchange_record_id")
    private Long exchangeRecordId;

    /**
     * 微信开放平台ID
     */
    @TableField(value = "wx_open_id")
    private String wxOpenId;

    /**
     * 提现金额
     */
    @TableField(value = "withdraw_amount")
    private BigDecimal withdrawAmount;

    /**
     * 提现状态ID，外键
     */
    @TableField(value = "tx_status_id")
    private Integer txStatusId;

    /**
     * 提现失败原因
     */
    @TableField(value = "failure_reason")
    private String failureReason;

    /**
     * 提现请求时间
     */
    @TableField(value = "requested_at")
    private LocalDateTime requestedAt;

    /**
     * 提现处理完成时间
     */
    @TableField(value = "processed_at")
    private LocalDateTime processedAt;

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
        UserWithdrawRecord other = (UserWithdrawRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getExchangeRecordId() == null ? other.getExchangeRecordId() == null : this.getExchangeRecordId().equals(other.getExchangeRecordId()))
            && (this.getWxOpenId() == null ? other.getWxOpenId() == null : this.getWxOpenId().equals(other.getWxOpenId()))
            && (this.getWithdrawAmount() == null ? other.getWithdrawAmount() == null : this.getWithdrawAmount().equals(other.getWithdrawAmount()))
            && (this.getTxStatusId() == null ? other.getTxStatusId() == null : this.getTxStatusId().equals(other.getTxStatusId()))
            && (this.getFailureReason() == null ? other.getFailureReason() == null : this.getFailureReason().equals(other.getFailureReason()))
            && (this.getRequestedAt() == null ? other.getRequestedAt() == null : this.getRequestedAt().equals(other.getRequestedAt()))
            && (this.getProcessedAt() == null ? other.getProcessedAt() == null : this.getProcessedAt().equals(other.getProcessedAt()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getExchangeRecordId() == null) ? 0 : getExchangeRecordId().hashCode());
        result = prime * result + ((getWxOpenId() == null) ? 0 : getWxOpenId().hashCode());
        result = prime * result + ((getWithdrawAmount() == null) ? 0 : getWithdrawAmount().hashCode());
        result = prime * result + ((getTxStatusId() == null) ? 0 : getTxStatusId().hashCode());
        result = prime * result + ((getFailureReason() == null) ? 0 : getFailureReason().hashCode());
        result = prime * result + ((getRequestedAt() == null) ? 0 : getRequestedAt().hashCode());
        result = prime * result + ((getProcessedAt() == null) ? 0 : getProcessedAt().hashCode());
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
        sb.append(", exchangeRecordId=").append(exchangeRecordId);
        sb.append(", wxOpenId=").append(wxOpenId);
        sb.append(", withdrawAmount=").append(withdrawAmount);
        sb.append(", txStatusId=").append(txStatusId);
        sb.append(", failureReason=").append(failureReason);
        sb.append(", requestedAt=").append(requestedAt);
        sb.append(", processedAt=").append(processedAt);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append("]");
        return sb.toString();
    }
}