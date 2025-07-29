package com.caicai.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value ="user_resign")
public class UserResign {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "user_id")
    private Long userId;
    @TableField(value = "resign_reason")
    private String resignReason;
    @TableField(value = "resign_at")
    private String resignAt;
    @TableField(value = "check_status")
    private Integer checkStatus;
    @TableField(value = "join_at")
    private String joinAt;
    @TableField(value = "witness_name")
    private String witnessName;
    @TableField(value = "witness_phone")
    private String witnessPhone;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", resignReason=").append(resignReason);
        sb.append(", resignTime=").append(resignAt);
        sb.append(", status=").append(checkStatus);
        sb.append(", workAt=").append(joinAt);
        sb.append(", witnessName=").append(witnessName);
        sb.append(", witnessPhone=").append(witnessPhone);
        sb.append("]");
        return sb.toString();
    }
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
        UserResign other = (UserResign) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getResignReason() == null ? other.getResignReason() == null : this.getResignReason().equals(other.getResignReason()))
            && (this.getResignAt() == null ? other.getResignAt() == null : this.getResignAt().equals(other.getResignAt()))
            && (this.getCheckStatus() == null ? other.getCheckStatus() == null : this.getCheckStatus().equals(other.getCheckStatus()))
            &&  (this.getJoinAt() == null ? other.getJoinAt() == null : this.getJoinAt().equals(other.getJoinAt()))
            &&  (this.getWitnessName() == null ? other.getWitnessName() == null : this.getWitnessName().equals(other.getWitnessName()))
            &&  (this.getWitnessPhone() == null ? other.getWitnessPhone() == null : this.getWitnessPhone().equals(other.getWitnessPhone()))    ;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getResignReason() == null) ? 0 : getResignReason().hashCode());
        result = prime * result + ((getResignAt() == null) ? 0 : getResignAt().hashCode());
        result = prime * result + ((getCheckStatus() == null) ? 0 : getCheckStatus().hashCode());
        result = prime * result + ((getJoinAt() == null) ? 0 : getJoinAt().hashCode());
        result = prime * result + ((getWitnessName() == null) ? 0 : getWitnessName().hashCode());
        result = prime * result + ((getWitnessPhone() == null) ? 0 : getWitnessPhone().hashCode());
        return result;
    }
}
