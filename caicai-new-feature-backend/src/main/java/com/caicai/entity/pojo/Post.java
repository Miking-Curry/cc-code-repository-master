package com.caicai.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("post")
@Data
public class Post {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    @TableField(value = "deleted_at")
    private LocalDateTime deletedAt;
    @TableField(value = "name")
    private String name;
    @TableField(value = "`desc`")
    private String desc;
    @TableField(value = "address")
    private String address;
    @TableField(value = "min_salary")
    private Integer minSalary;
    @TableField(value = "max_salary")
    private Integer maxSalary;
    @TableField(value = "need_num")
    private Integer needNum;
    @TableField(value = "post_type")
    private String postType;
    @TableField(value = "department")
    private String department;
    @TableField(value = "status")
    private Integer status;
    @TableField(value = "company_id")
    private Long companyId;
    @TableField(value = "education")
    private Integer education;
    @TableField(value = "max_experience")
    private Integer maxExperience;
    @TableField(value = "min_experience")
    private Integer minExperience;
    @TableField(value = "city")
    private String city;
    @TableField(value = "province")
    private String province;
    @TableField(value = "state")
    private String state;
    @TableField(value = "view")
    private Integer view;
    @TableField(value = "area")
    private String area;
    @TableField(value = "province_area")
    private String provinceArea;
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
        Post other = (Post) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
                && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()))
                && (this.getDeletedAt() ==  null ? other.getDeletedAt() == null : this.getDeletedAt().equals(other.getDeletedAt()))
                && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
                && (this.getDesc() == null ? other.getDesc() == null : this.getDesc().equals(other.getDesc()))
                && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()))
                && (this.getMinSalary() == null ? other.getMinSalary() == null : this.getMinSalary().equals(other.getMinSalary()))
                && (this.getMaxSalary() == null ? other.getMaxSalary() == null : this.getMaxSalary().equals(other.getMaxSalary()))
                && (this.getNeedNum() == null ? other.getNeedNum() == null : this.getNeedNum().equals(other.getNeedNum()))
                && (this.getPostType() == null ? other.getPostType() == null : this.getPostType().equals(other.getPostType()))
                && (this.getDepartment() == null ? other.getDepartment() == null : this.getDepartment().equals(other.getDepartment()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getCompanyId() == null ? other.getCompanyId() == null : this.getCompanyId().equals(other.getCompanyId()))
                && (this.getEducation() == null ? other.getEducation() == null : this.getEducation().equals(other.getEducation()))
                && (this.getMaxExperience() == null ? other.getMaxExperience() == null : this.getMaxExperience().equals(other.getMaxExperience()))
                && (this.getMinExperience() == null ? other.getMinExperience() == null : this.getMinExperience().equals(other.getMinExperience()))
                && (this.getCity() == null ? other.getCity() == null : this.getCity().equals(other.getCity()))
                && (this.getProvince() == null ? other.getProvince() == null : this.getProvince().equals(other.getProvince()))
                && (this.getState() == null ? other.getState() == null : this.getState().equals(other.getState()))
                && (this.getView() == null ? other.getView() == null : this.getView().equals(other.getView()))
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
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getDesc() == null) ? 0 : getDesc().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getMinSalary() == null) ? 0 : getMinSalary().hashCode());
        result = prime * result + ((getMaxSalary() == null) ? 0 : getMaxSalary().hashCode());
        result = prime * result + ((getNeedNum() == null) ? 0 : getNeedNum().hashCode());
        result = prime * result + ((getPostType() == null) ? 0 : getPostType().hashCode());
        result = prime * result + ((getDepartment() == null) ? 0 : getDepartment().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCompanyId() == null) ? 0 : getCompanyId().hashCode());
        result = prime * result + ((getEducation() == null) ? 0 : getEducation().hashCode());
        result = prime * result + ((getMaxExperience() == null) ? 0 : getMaxExperience().hashCode());
        result = prime * result + ((getMinExperience() == null) ? 0 : getMinExperience().hashCode());
        result = prime * result + ((this.getCity() == null) ? 0 : this.getCity().hashCode());
        result = prime * result + ((this.getProvince() == null) ? 0 : this.getProvince().hashCode());
        result = prime * result + ((this.getState() == null) ? 0 : this.getState().hashCode());
        result = prime * result + ((this.getView() == null) ? 0 : this.getView().hashCode());
        result = prime * result + ((this.getArea() == null) ? 0 : this.getArea().hashCode());
        result = prime * result + ((this.getProvinceArea() == null) ? 0 : this.getProvinceArea().hashCode());
        result = prime * result + ((this.getStateArea() == null) ? 0 : this.getStateArea().hashCode());
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
        sb.append(", name=").append(name);
        sb.append(", desc=").append(desc);
        sb.append(", address=").append(address);
        sb.append(", minSalary=").append(minSalary);
        sb.append(", maxSalary=").append(maxSalary);
        sb.append(", needNum=").append(needNum);
        sb.append(", postType=").append(postType);
        sb.append(", department=").append(department);
        sb.append(", status=").append(status);
        sb.append(", companyId=").append(companyId);
        sb.append(", education=").append(education);
        sb.append(", maxExperience=").append(maxExperience);
        sb.append(", minExperience=").append(minExperience);
        sb.append(", city=").append(city);
        sb.append(", province=").append(province);
        sb.append(", state=").append(state);
        sb.append(", view=").append(view);
        sb.append(", area=").append(area);
        sb.append(", provinceArea=").append(provinceArea);
        sb.append(", stateArea=").append(stateArea);
        sb.append("]");
        return sb.toString();
    }
}
