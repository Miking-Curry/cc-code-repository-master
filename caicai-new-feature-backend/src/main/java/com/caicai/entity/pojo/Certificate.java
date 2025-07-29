package com.caicai.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;


@TableName(value ="certificate")
@Data
public class Certificate {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "certificate_name")
    private String certificateName;

    @TableField(value = "certificate_pic")
    private String certificatePic;

    @TableField(value = "issue_date")
    private LocalDateTime issueDate;

    @TableField(value = "created_at",fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableField(value = "deleted_at")
    private LocalDateTime deletedAt;

    @TableField(value = "user_id")
    private Long userId;

    @TableField(value = "check_status")
    private Integer checkStatus;
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
        Certificate  other = (Certificate) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
                && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()))
                && (this.getDeletedAt() == null ? other.getDeletedAt() == null : this.getDeletedAt().equals(other.getDeletedAt()))
                && (this.getCertificateName() == null ? other.getCertificateName() == null : this.getCertificateName().equals(other.getCertificateName()))
                && (this.getCertificatePic() == null ? other.getCertificatePic() == null : this.getCertificatePic().equals(other.getCertificatePic()))
                && (this.getIssueDate() == null ? other.getIssueDate() == null : this.getIssueDate().equals(other.getIssueDate()))
                && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
                && (this.getCheckStatus() == null ? other.getCheckStatus() == null : this.getCheckStatus().equals(other.getCheckStatus()));

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        result = prime * result + ((getDeletedAt() == null) ? 0 : getDeletedAt().hashCode());
        result = prime * result + ((getCertificateName() == null) ? 0 : getCertificateName().hashCode());
        result = prime * result + ((getCertificatePic() == null) ? 0 : getCertificatePic().hashCode());
        result = prime * result + ((getIssueDate() == null) ? 0 : getIssueDate().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getCheckStatus() == null) ? 0 : getCheckStatus().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", deletedAt=").append(deletedAt);
        sb.append(", certificateName=").append(certificateName);
        sb.append(", certificatePic=").append(certificatePic);
        sb.append(", issueDate=").append(issueDate);
        sb.append(", userId=").append(userId);
        sb.append(", status=").append(checkStatus);
        sb.append("]");
        return sb.toString();
    }
}
