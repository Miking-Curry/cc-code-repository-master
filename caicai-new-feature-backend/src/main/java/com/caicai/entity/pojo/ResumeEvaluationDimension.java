package com.caicai.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * 人才评分各维度记录表
 * @TableName resume_evaluation_dimension
 */
@TableName(value ="resume_evaluation_dimension")
@Data
public class ResumeEvaluationDimension {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联评分记录ID
     */
    @TableField(value = "evaluation_id")
    private Long evaluationId;

    /**
     * 评分维度名称（如 技能/经验）
     */
    @TableField(value = "dimension")
    private String dimension;

    /**
     * 维度分数
     */
    @TableField(value = "score")
    private Integer score;

    /**
     * 等级（优秀/良好/中等）
     */
    @TableField(value = "level")
    private String level;

    /**
     * 评分理由
     */
    @TableField(value = "reason")
    private String reason;

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
        ResumeEvaluationDimension other = (ResumeEvaluationDimension) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getEvaluationId() == null ? other.getEvaluationId() == null : this.getEvaluationId().equals(other.getEvaluationId()))
            && (this.getDimension() == null ? other.getDimension() == null : this.getDimension().equals(other.getDimension()))
            && (this.getScore() == null ? other.getScore() == null : this.getScore().equals(other.getScore()))
            && (this.getLevel() == null ? other.getLevel() == null : this.getLevel().equals(other.getLevel()))
            && (this.getReason() == null ? other.getReason() == null : this.getReason().equals(other.getReason()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getEvaluationId() == null) ? 0 : getEvaluationId().hashCode());
        result = prime * result + ((getDimension() == null) ? 0 : getDimension().hashCode());
        result = prime * result + ((getScore() == null) ? 0 : getScore().hashCode());
        result = prime * result + ((getLevel() == null) ? 0 : getLevel().hashCode());
        result = prime * result + ((getReason() == null) ? 0 : getReason().hashCode());
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
        sb.append(", evaluationId=").append(evaluationId);
        sb.append(", dimension=").append(dimension);
        sb.append(", score=").append(score);
        sb.append(", level=").append(level);
        sb.append(", reason=").append(reason);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append("]");
        return sb.toString();
    }
}