package com.caicai.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户查看岗位详情表
 * @TableName user_view_job_detail
 */
@TableName(value ="user_view_job_detail")
@Data
@Accessors(chain = true)
public class UserViewJobDetail {
    /**
     * 用户查看岗位详情记录ID，主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID，外键
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 职位id，外键
     */
    @TableField(value = "post_id")
    private Long postId;

    /**
     * 浏览时长
     */
    @TableField(value = "view_time")
    private Integer viewTime;

    /**
     * 薪资范围-最低薪资
     */
    @TableField(value = "min_salary")
    private Integer minSalary;

    /**
     * 薪资范围-最低薪资
     */
    @TableField(value = "max_salary")
    private Integer maxSalary;

    /**
     * 行业大类
     */
    @TableField(value = "industry_category")
    private String industryCategory;

    /**
     * 用户查看岗位详情记录创建时间
     */
    @TableField(value = "created_at",fill = FieldFill.INSERT)
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
        UserViewJobDetail other = (UserViewJobDetail) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getPostId() == null ? other.getPostId() == null : this.getPostId().equals(other.getPostId()))
            && (this.getViewTime() == null ? other.getViewTime() == null : this.getViewTime().equals(other.getViewTime()))
            && (this.getMinSalary() == null ? other.getMinSalary() == null : this.getMinSalary().equals(other.getMinSalary()))
            && (this.getMaxSalary() == null ? other.getMaxSalary() == null : this.getMaxSalary().equals(other.getMaxSalary()))
            && (this.getIndustryCategory() == null ? other.getIndustryCategory() == null : this.getIndustryCategory().equals(other.getIndustryCategory()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getPostId() == null) ? 0 : getPostId().hashCode());
        result = prime * result + ((getViewTime() == null) ? 0 : getViewTime().hashCode());
        result = prime * result + ((getMinSalary() == null) ? 0 : getMinSalary().hashCode());
        result = prime * result + ((getMaxSalary() == null) ? 0 : getMaxSalary().hashCode());
        result = prime * result + ((getIndustryCategory() == null) ? 0 : getIndustryCategory().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", id=" + id +
                ", userId=" + userId +
                ", postId=" + postId +
                ", viewTime=" + viewTime +
                ", minSalary=" + minSalary +
                ", maxSalary=" + maxSalary +
                ", industryCategory=" + industryCategory +
                ", createdAt=" + createdAt +
                "]";
    }
}