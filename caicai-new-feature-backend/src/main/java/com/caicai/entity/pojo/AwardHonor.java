package com.caicai.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("award_honor")
public class AwardHonor {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField(value = "award_honor_name")
    private String awardHonorName;
    @TableField(value = "award_honor_pic")
    private String awardHonorPic;
    @TableField(value = "award_honor_description")
    private String awardHonorDescription;
    @TableField(value = "obtain_date")
    private LocalDateTime obtainDate;
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    @TableField(value = "deleted_at")
    private LocalDateTime deletedAt;


    @Override
    public String toString() {
      StringBuilder  sb = new StringBuilder();
      sb.append(getClass().getSimpleName());
      sb.append(" [");
      sb.append("Hash = ").append(hashCode());
      sb.append(", id=").append(id);
      sb.append(", awardHonorName=").append(awardHonorName);
      sb.append(", awardHonorPic=").append(awardHonorPic);
      sb.append(", awardHonorDescription=").append(awardHonorDescription);
      sb.append(", obtainDate=").append(obtainDate);
      sb.append(", createdAt=").append(createdAt);
      sb.append(", updatedAt=").append(updatedAt);
      sb.append(", deletedAt=").append(deletedAt);
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
        AwardHonor other = (AwardHonor) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getAwardHonorName() == null ? other.getAwardHonorName() == null : this.getAwardHonorName().equals(other.getAwardHonorName()))
            && (this.getAwardHonorPic() == null ? other.getAwardHonorPic() == null : this.getAwardHonorPic().equals(other.getAwardHonorPic()))
            && (this.getAwardHonorDescription() == null ? other.getAwardHonorDescription() == null : this.getAwardHonorDescription().equals(other.getAwardHonorDescription()))
            && (this.getObtainDate() == null ? other.getObtainDate() == null : this.getObtainDate().equals(other.getObtainDate()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()))
            && (this.getDeletedAt() == null ? other.getDeletedAt() == null : this.getDeletedAt().equals(other.getDeletedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getAwardHonorName() == null) ? 0 : getAwardHonorName().hashCode());
        result = prime * result + ((getAwardHonorPic() == null) ? 0 : getAwardHonorPic().hashCode());
        result = prime * result + ((getAwardHonorDescription() == null) ? 0 : getAwardHonorDescription().hashCode());
        result = prime * result + ((getObtainDate() == null) ? 0 : getObtainDate().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        result = prime * result + ((getDeletedAt() == null) ? 0 : getDeletedAt().hashCode());
        return result;
    }
}
