package com.caicai.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @TableName user_job_intention
 */
@TableName(value ="user_job_intention")
@Data
public class UserJobIntention {
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
    @TableField(value = "min_salary")
    private Integer minSalary;

    /**
     * 
     */
    @TableField(value = "max_salary")
    private Integer maxSalary;

    /**
     * 
     */
    @TableField(value = "city")
    private String city;

    /**
     * 
     */
    @TableField(value = "work_type")
    private String workType;

    /**
     * 
     */
    @TableField(value = "job")
    private String job;

    /**
     * 
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 
     */
    @TableField(value = "province")
    private String province;

    /**
     * 
     */
    @TableField(value = "state")
    private String state;

    /**
     * 
     */
    @TableField(value = "area")
    private String area;

    /**
     * 
     */
    @TableField(value = "province_area")
    private String provinceArea;

    /**
     * 
     */
    @TableField(value = "state_area")
    private String stateArea;

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
        UserJobIntention other = (UserJobIntention) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()))
            && (this.getDeletedAt() == null ? other.getDeletedAt() == null : this.getDeletedAt().equals(other.getDeletedAt()))
            && (this.getMinSalary() == null ? other.getMinSalary() == null : this.getMinSalary().equals(other.getMinSalary()))
            && (this.getMaxSalary() == null ? other.getMaxSalary() == null : this.getMaxSalary().equals(other.getMaxSalary()))
            && (this.getCity() == null ? other.getCity() == null : this.getCity().equals(other.getCity()))
            && (this.getWorkType() == null ? other.getWorkType() == null : this.getWorkType().equals(other.getWorkType()))
            && (this.getJob() == null ? other.getJob() == null : this.getJob().equals(other.getJob()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getProvince() == null ? other.getProvince() == null : this.getProvince().equals(other.getProvince()))
            && (this.getState() == null ? other.getState() == null : this.getState().equals(other.getState()))
            && (this.getArea() == null ? other.getArea() == null : this.getArea().equals(other.getArea()))
            && (this.getProvinceArea() == null ? other.getProvinceArea() == null : this.getProvinceArea().equals(other.getProvinceArea()))
            && (this.getStateArea() == null ? other.getStateArea() == null : this.getStateArea().equals(other.getStateArea()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        result = prime * result + ((getDeletedAt() == null) ? 0 : getDeletedAt().hashCode());
        result = prime * result + ((getMinSalary() == null) ? 0 : getMinSalary().hashCode());
        result = prime * result + ((getMaxSalary() == null) ? 0 : getMaxSalary().hashCode());
        result = prime * result + ((getCity() == null) ? 0 : getCity().hashCode());
        result = prime * result + ((getWorkType() == null) ? 0 : getWorkType().hashCode());
        result = prime * result + ((getJob() == null) ? 0 : getJob().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getProvince() == null) ? 0 : getProvince().hashCode());
        result = prime * result + ((getState() == null) ? 0 : getState().hashCode());
        result = prime * result + ((getArea() == null) ? 0 : getArea().hashCode());
        result = prime * result + ((getProvinceArea() == null) ? 0 : getProvinceArea().hashCode());
        result = prime * result + ((getStateArea() == null) ? 0 : getStateArea().hashCode());
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
        sb.append(", minSalary=").append(minSalary);
        sb.append(", maxSalary=").append(maxSalary);
        sb.append(", city=").append(city);
        sb.append(", workType=").append(workType);
        sb.append(", job=").append(job);
        sb.append(", userId=").append(userId);
        sb.append(", province=").append(province);
        sb.append(", state=").append(state);
        sb.append(", area=").append(area);
        sb.append(", provinceArea=").append(provinceArea);
        sb.append(", stateArea=").append(stateArea);
        sb.append("]");
        return sb.toString();
    }
}