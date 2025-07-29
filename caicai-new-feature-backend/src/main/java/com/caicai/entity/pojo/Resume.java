package com.caicai.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("resume")
@Data
public class Resume {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField(value = "user_id")
    private Long userId;
    @TableField(value = "ai_conclusion")
    private String aiConclusion;
    @TableField(value = "created_at",  fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(value = "updated_at",  fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    @TableField(value = "deleted_at")
    private LocalDateTime deletedAt;
    @TableField(value = "ai_score")
    private Integer aiScore;
    @TableField(value = "ai_summary")
    private String aiSummary;
    @TableField(value = "ai_keywords")
    private String aiKeywords;

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
        Resume other = (Resume) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
                && (this.getAiConclusion() == null ? other.getAiConclusion() == null : this.getAiConclusion().equals(other.getAiConclusion()))
                && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
                && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()))
                && (this.getDeletedAt() == null ? other.getDeletedAt() == null : this.getDeletedAt().equals(other.getDeletedAt()))
                && (this.getAiScore() == null ? other.getAiScore() == null : this.getAiScore().equals(other.getAiScore()))
                && (this.getAiSummary() == null ? other.getAiSummary() == null : this.getAiSummary().equals(other.getAiSummary()))
                && (this.getAiKeywords() == null ? other.getAiKeywords() == null : this.getAiKeywords().equals(other.getAiKeywords()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getAiConclusion() == null) ? 0 : getAiConclusion().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 :getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        result = prime * result + ((getDeletedAt() == null) ? 0 : getDeletedAt().hashCode());
        result = prime * result + ((getAiScore() == null) ? 0 : getAiScore().hashCode());
        result = prime * result + ((getAiSummary() == null) ? 0 : getAiSummary().hashCode());
        result = prime * result + ((getAiKeywords() == null) ? 0 : getAiKeywords().hashCode());
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
       sb.append(", aiConclusion=").append(aiConclusion);
       sb.append(", createAt=").append(createdAt);
       sb.append(", updateAt=").append(updatedAt);
       sb.append(", deletedAt=").append(deletedAt);
       sb.append(", aiScore=").append(aiScore);
       sb.append(", aiSummary=").append(aiSummary);
       sb.append(", aiKeywords=").append(aiKeywords);
       sb.append("]");
       return sb.toString();
    }
}
