package com.caicai.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
@TableName("resume_award_honor")
@Data
public class ResumeAwardHonor {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField(value= "user_id")
    private Long userId;
    @TableField(value = "resume_id")
    private Long resumeId;
    @TableField(value = "award_honor_id")
    private Long awardHonorId;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getResumeId() == null) ? 0 : getResumeId().hashCode());
        result = prime * result + ((getAwardHonorId() == null) ? 0 : getAwardHonorId().hashCode());
        return result;
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
        ResumeAwardHonor other = (ResumeAwardHonor) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
                && (this.getResumeId() == null ? other.getResumeId() == null : this.getResumeId().equals(other.getResumeId()))
                && (this.getAwardHonorId() == null ? other.getAwardHonorId() == null : this.getAwardHonorId().equals(other.getAwardHonorId()));
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", resumeId=").append(resumeId);
        sb.append(", awardHonorId=").append(awardHonorId);
        sb.append("]");
        return sb.toString();
    }
}
