package com.caicai.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("resume_skill_tag")
@Data
public class ResumeSkillTag {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField(value = "user_id")
    private Long userId;
    @TableField(value = "resume_id")
    private Long resumeId;
    @TableField(value = "skill_tag_id")
    private Long skillTagId;
    @TableField("level")
    private Integer level;
    @TableField(value = "years_of_experience")
    private Integer yearsOfExperience;
    @TableField(value = "description")
    private String description;
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
        ResumeSkillTag other = (ResumeSkillTag) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
                && (this.getResumeId() == null ? other.getResumeId() == null : this.getResumeId().equals(other.getResumeId()))
                && (this.getSkillTagId() == null ? other.getSkillTagId() == null : this.getSkillTagId().equals(other.getSkillTagId()))
                && (this.getLevel() == null ? other.getLevel() == null : this.getLevel().equals(other.getLevel()))
                && (this.getYearsOfExperience() == null ? other.getYearsOfExperience() == null : this.getYearsOfExperience().equals(other.getYearsOfExperience()))
                && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
                && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getResumeId() == null) ? 0 : getResumeId().hashCode());
        result = prime * result + ((getSkillTagId() == null) ? 0 : getSkillTagId().hashCode());
        result = prime * result + ((getLevel() == null) ? 0 : getLevel().hashCode());
        result = prime * result + ((getYearsOfExperience() == null) ? 0 : getYearsOfExperience().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder  sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", resumeId=").append(resumeId);
        sb.append(", skillTagId=").append(skillTagId);
        sb.append(", level=").append(level);
        sb.append(", yearsOfExperience=").append(yearsOfExperience);
        sb.append(", description=").append(description);
        sb.append(", createdAt=").append(createdAt);
        sb.append("]");
        return sb.toString();
    }
}
