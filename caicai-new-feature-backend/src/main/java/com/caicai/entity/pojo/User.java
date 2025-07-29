package com.caicai.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import lombok.Data;

/**
@description: 用户表
@author: LiWeihang
@create: 2025/5/20 9:23
**/
@TableName(value ="user")
@Data
public class User {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "last_login")
    private LocalDateTime lastLogin;

    @TableField(value = "is_superuser")
    private Integer isSuperuser;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableField(value = "deleted_at")
    private LocalDateTime deletedAt;

    @TableField(value = "is_active")
    private Integer isActive;

    @TableField(value = "is_staff")
    private Integer isStaff;

    @TableField(value = "username")
    private String username;

    @TableField(value = "password")
    private String password;

    @TableField(value = "created_by_id")
    private Long createdById;

    @TableField(value = "company_id")
    private Long companyId;

    @TableField(value = "organization_id")
    private Long organizationId;

    @TableField(value = "avatar_id")
    private Long avatarId;

    @TableField(value = "email")
    private String email;

    @TableField(value = "phone")
    private String phone;

    @TableField(value = "type")
    private String type;

    @TableField(value = "real_password")
    private String realPassword;

    @TableField(value = "source")
    private String source;

    @TableField(value = "is_user")
    private Integer isUser;

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
        User other = (User) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getLastLogin() == null ? other.getLastLogin() == null : this.getLastLogin().equals(other.getLastLogin()))
            && (this.getIsSuperuser() == null ? other.getIsSuperuser() == null : this.getIsSuperuser().equals(other.getIsSuperuser()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()))
            && (this.getDeletedAt() == null ? other.getDeletedAt() == null : this.getDeletedAt().equals(other.getDeletedAt()))
            && (this.getIsActive() == null ? other.getIsActive() == null : this.getIsActive().equals(other.getIsActive()))
            && (this.getIsStaff() == null ? other.getIsStaff() == null : this.getIsStaff().equals(other.getIsStaff()))
            && (this.getUsername() == null ? other.getUsername() == null : this.getUsername().equals(other.getUsername()))
            && (this.getPassword() == null ? other.getPassword() == null : this.getPassword().equals(other.getPassword()))
            && (this.getCreatedById() == null ? other.getCreatedById() == null : this.getCreatedById().equals(other.getCreatedById()))
            && (this.getCompanyId() == null ? other.getCompanyId() == null : this.getCompanyId().equals(other.getCompanyId()))
            && (this.getOrganizationId() == null ? other.getOrganizationId() == null : this.getOrganizationId().equals(other.getOrganizationId()))
            && (this.getAvatarId() == null ? other.getAvatarId() == null : this.getAvatarId().equals(other.getAvatarId()))
            && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
            && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getRealPassword() == null ? other.getRealPassword() == null : this.getRealPassword().equals(other.getRealPassword()))
            && (this.getSource() == null ? other.getSource() == null : this.getSource().equals(other.getSource()))
            && (this.getIsUser() == null ? other.getIsUser() == null : this.getIsUser().equals(other.getIsUser()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getLastLogin() == null) ? 0 : getLastLogin().hashCode());
        result = prime * result + ((getIsSuperuser() == null) ? 0 : getIsSuperuser().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        result = prime * result + ((getDeletedAt() == null) ? 0 : getDeletedAt().hashCode());
        result = prime * result + ((getIsActive() == null) ? 0 : getIsActive().hashCode());
        result = prime * result + ((getIsStaff() == null) ? 0 : getIsStaff().hashCode());
        result = prime * result + ((getUsername() == null) ? 0 : getUsername().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getCreatedById() == null) ? 0 : getCreatedById().hashCode());
        result = prime * result + ((getCompanyId() == null) ? 0 : getCompanyId().hashCode());
        result = prime * result + ((getOrganizationId() == null) ? 0 : getOrganizationId().hashCode());
        result = prime * result + ((getAvatarId() == null) ? 0 : getAvatarId().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getRealPassword() == null) ? 0 : getRealPassword().hashCode());
        result = prime * result + ((getSource() == null) ? 0 : getSource().hashCode());
        result = prime * result + ((getIsUser() == null) ? 0 : getIsUser().hashCode());
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", id=" + id +
                ", lastLogin=" + lastLogin +
                ", isSuperuser=" + isSuperuser +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deletedAt=" + deletedAt +
                ", isActive=" + isActive +
                ", isStaff=" + isStaff +
                ", username=" + username +
                ", password=" + password +
                ", createdById=" + createdById +
                ", companyId=" + companyId +
                ", organizationId=" + organizationId +
                ", avatarId=" + avatarId +
                ", email=" + email +
                ", phone=" + phone +
                ", type=" + type +
                ", realPassword=" + realPassword +
                ", source=" + source +
                ", isUser=" + isUser +
                "]";
    }
}