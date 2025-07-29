package com.caicai.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @TableName user_project_experience
 */
@TableName(value ="user_project_experience")
@Data
public class UserProjectExperience {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    @TableField(value = "created_at")
    private LocalDateTime createdAt;

    /**
     * 
     */
    @TableField(value = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * 
     */
    @TableField(value = "deleted_at")
    private LocalDateTime deletedAt;

    /**
     * 
     */
    @TableField(value = "project_name")
    private String projectName;

    /**
     * 
     */
    @TableField(value = "project_role")
    private String projectRole;

    /**
     * 
     */
    @TableField(value = "project_content")
    private String projectContent;

    /**
     * 
     */
    @TableField(value = "start_time")
    private LocalDate startTime;

    /**
     * 
     */
    @TableField(value = "end_time")
    private LocalDate endTime;

    /**
     * 
     */
    @TableField(value = "project_result")
    private String projectResult;

    /**
     * 
     */
    @TableField(value = "project_technology")
    private String projectTechnology;

    /**
     * 
     */
    @TableField(value = "project_description")
    private String projectDescription;

    /**
     * 
     */
    @TableField(value = "project_url")
    private String projectUrl;

    /**
     * 
     */
    @TableField(value = "user_id")
    private Long userId;

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
        UserProjectExperience other = (UserProjectExperience) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()))
            && (this.getDeletedAt() == null ? other.getDeletedAt() == null : this.getDeletedAt().equals(other.getDeletedAt()))
            && (this.getProjectName() == null ? other.getProjectName() == null : this.getProjectName().equals(other.getProjectName()))
            && (this.getProjectRole() == null ? other.getProjectRole() == null : this.getProjectRole().equals(other.getProjectRole()))
            && (this.getProjectContent() == null ? other.getProjectContent() == null : this.getProjectContent().equals(other.getProjectContent()))
            && (this.getStartTime() == null ? other.getStartTime() == null : this.getStartTime().equals(other.getStartTime()))
            && (this.getEndTime() == null ? other.getEndTime() == null : this.getEndTime().equals(other.getEndTime()))
            && (this.getProjectResult() == null ? other.getProjectResult() == null : this.getProjectResult().equals(other.getProjectResult()))
            && (this.getProjectTechnology() == null ? other.getProjectTechnology() == null : this.getProjectTechnology().equals(other.getProjectTechnology()))
            && (this.getProjectDescription() == null ? other.getProjectDescription() == null : this.getProjectDescription().equals(other.getProjectDescription()))
            && (this.getProjectUrl() == null ? other.getProjectUrl() == null : this.getProjectUrl().equals(other.getProjectUrl()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        result = prime * result + ((getDeletedAt() == null) ? 0 : getDeletedAt().hashCode());
        result = prime * result + ((getProjectName() == null) ? 0 : getProjectName().hashCode());
        result = prime * result + ((getProjectRole() == null) ? 0 : getProjectRole().hashCode());
        result = prime * result + ((getProjectContent() == null) ? 0 : getProjectContent().hashCode());
        result = prime * result + ((getStartTime() == null) ? 0 : getStartTime().hashCode());
        result = prime * result + ((getEndTime() == null) ? 0 : getEndTime().hashCode());
        result = prime * result + ((getProjectResult() == null) ? 0 : getProjectResult().hashCode());
        result = prime * result + ((getProjectTechnology() == null) ? 0 : getProjectTechnology().hashCode());
        result = prime * result + ((getProjectDescription() == null) ? 0 : getProjectDescription().hashCode());
        result = prime * result + ((getProjectUrl() == null) ? 0 : getProjectUrl().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
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
        sb.append(", projectName=").append(projectName);
        sb.append(", projectRole=").append(projectRole);
        sb.append(", projectContent=").append(projectContent);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", projectResult=").append(projectResult);
        sb.append(", projectTechnology=").append(projectTechnology);
        sb.append(", projectDescription=").append(projectDescription);
        sb.append(", projectUrl=").append(projectUrl);
        sb.append(", userId=").append(userId);
        sb.append("]");
        return sb.toString();
    }
}