package com.caicai.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("resume_project_experience")
public class ResumeProjectExperience {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField(value = "resume_id")
    private Long resumeId;
    @TableField(value = "project_experience_id")
    private Long projectExperienceId;


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getResumeId() == null) ? 0 : getResumeId().hashCode());
        result = prime * result + ((getProjectExperienceId() == null) ? 0 : getProjectExperienceId().hashCode());
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
        ResumeProjectExperience other = (ResumeProjectExperience) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getResumeId() == null ? other.getResumeId() == null : this.getResumeId().equals(other.getResumeId()))
                && (this.getProjectExperienceId() == null ? other.getProjectExperienceId() == null : this.getProjectExperienceId().equals(other.getProjectExperienceId()));
    }

    @Override
    public String toString() {
        StringBuilder  sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", resumeId=").append(resumeId);
        sb.append(", projectExperienceId=").append(projectExperienceId);
        sb.append("]");
        return sb.toString();
    }
}
