package com.caicai.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @TableName user_skill
 */
@TableName(value ="user_skill")
@Data
public class UserSkill {
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
    @TableField(value = "skill_name")
    private String skillName;

    /**
     * 
     */
    @TableField(value = "skill_time")
    private String skillTime;

    /**
     * 
     */
    @TableField(value = "skill_proficiency")
    private String skillProficiency;

    /**
     * 
     */
    @TableField(value = "skill_cert_id")
    private Long skillCertId;

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
        UserSkill other = (UserSkill) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()))
            && (this.getDeletedAt() == null ? other.getDeletedAt() == null : this.getDeletedAt().equals(other.getDeletedAt()))
            && (this.getSkillName() == null ? other.getSkillName() == null : this.getSkillName().equals(other.getSkillName()))
            && (this.getSkillTime() == null ? other.getSkillTime() == null : this.getSkillTime().equals(other.getSkillTime()))
            && (this.getSkillProficiency() == null ? other.getSkillProficiency() == null : this.getSkillProficiency().equals(other.getSkillProficiency()))
            && (this.getSkillCertId() == null ? other.getSkillCertId() == null : this.getSkillCertId().equals(other.getSkillCertId()))
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
        result = prime * result + ((getSkillName() == null) ? 0 : getSkillName().hashCode());
        result = prime * result + ((getSkillTime() == null) ? 0 : getSkillTime().hashCode());
        result = prime * result + ((getSkillProficiency() == null) ? 0 : getSkillProficiency().hashCode());
        result = prime * result + ((getSkillCertId() == null) ? 0 : getSkillCertId().hashCode());
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
        sb.append(", skillName=").append(skillName);
        sb.append(", skillTime=").append(skillTime);
        sb.append(", skillProficiency=").append(skillProficiency);
        sb.append(", skillCertId=").append(skillCertId);
        sb.append(", userId=").append(userId);
        sb.append("]");
        return sb.toString();
    }
}