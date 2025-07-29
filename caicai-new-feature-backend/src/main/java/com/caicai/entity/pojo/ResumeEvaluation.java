package com.caicai.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * 简历AI评分汇总表
 * @TableName resume_evaluation
 */
@TableName(value ="resume_evaluation")
@Data
public class ResumeEvaluation {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 简历ID
     */
    @TableField(value = "resume_id")
    private Long resumeId;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 总评分（如 0~10）
     */
    @TableField(value = "overall_score")
    private Integer overallScore;

    /**
     * 总评级（如 优秀/良好/中等）
     */
    @TableField(value = "overall_level")
    private String overallLevel;

    /**
     * 总体评价
     */
    @TableField(value = "overall_evaluate")
    private String overallEvaluate;

    /**
     * 原始评分结果（可选）
     */
    @TableField(value = "raw_json")
    private Object rawJson;

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
        ResumeEvaluation other = (ResumeEvaluation) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getResumeId() == null ? other.getResumeId() == null : this.getResumeId().equals(other.getResumeId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getOverallScore() == null ? other.getOverallScore() == null : this.getOverallScore().equals(other.getOverallScore()))
            && (this.getOverallLevel() == null ? other.getOverallLevel() == null : this.getOverallLevel().equals(other.getOverallLevel()))
            && (this.getOverallEvaluate() == null ? other.getOverallEvaluate() == null : this.getOverallEvaluate().equals(other.getOverallEvaluate()))
            && (this.getRawJson() == null ? other.getRawJson() == null : this.getRawJson().equals(other.getRawJson()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getResumeId() == null) ? 0 : getResumeId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getOverallScore() == null) ? 0 : getOverallScore().hashCode());
        result = prime * result + ((getOverallLevel() == null) ? 0 : getOverallLevel().hashCode());
        result = prime * result + ((getOverallEvaluate() == null) ? 0 : getOverallEvaluate().hashCode());
        result = prime * result + ((getRawJson() == null) ? 0 : getRawJson().hashCode());
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
        sb.append(", resumeId=").append(resumeId);
        sb.append(", userId=").append(userId);
        sb.append(", overallScore=").append(overallScore);
        sb.append(", overallLevel=").append(overallLevel);
        sb.append(", overallEvaluate=").append(overallEvaluate);
        sb.append(", rawJson=").append(rawJson);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append("]");
        return sb.toString();
    }
}