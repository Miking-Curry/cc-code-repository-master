package com.caicai.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("company")
@Data
public class Company {
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
    @TableField(value = "organization_id")
    private Long organizationId;
    @TableField(value = "logo_id")
    private Long logoId;
    @TableField("address")
    private String address;
    @TableField("contact")
    private String contact;
    @TableField("email")
    private String email;
    @TableField("establish_date")
    private LocalDateTime establishDate;
    @TableField("industry")
    private String industry;
    @TableField("introduction")
    private String introduction;
    @TableField("phone")
    private String phone;
    @TableField("staff_size")
    private Integer staffSize;
    @TableField("type")
    private String type;
    @TableField("city")
    private String city;
    @TableField("province")
    private String province;
    @TableField("state")
    private String state;
    @TableField("website")
    private String website;
    @TableField("area")
    private String area;
    @TableField("province_area")
    private String provinceArea;
    @TableField("state_area")
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
        Company other = (Company) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
                && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()))
                && (this.getDeletedAt() == null ? other.getDeletedAt() == null : this.getDeletedAt().equals(other.getDeletedAt()))
                && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
                && (this.getOrganizationId() == null ? other.getOrganizationId() == null : this.getOrganizationId().equals(other.getOrganizationId()))
                && (this.getLogoId() == null ? other.getLogoId() == null : this.getLogoId().equals(other.getLogoId()))
                && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()))
                && (this.getContact() == null ? other.getContact() == null : this.getContact().equals(other.getContact()))
                && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
                && (this.getEstablishDate() == null ? other.getEstablishDate() == null : this.getEstablishDate().equals(other.getEstablishDate()))
                && (this.getIndustry() == null ? other.getIndustry() == null : this.getIndustry().equals(other.getIndustry()))
                && (this.getIntroduction() == null ? other.getIntroduction() == null : this.getIntroduction().equals(other.getIntroduction()))
                && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
                && (this.getStaffSize() == null ? other.getStaffSize() == null : this.getStaffSize().equals(other.getStaffSize()))
                && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
                && (this.getCity() == null ? other.getCity() == null : this.getCity().equals(other.getCity()))
                && (this.getProvince() == null ? other.getProvince() == null : this.getProvince().equals(other.getProvince()))
                && (this.getState() == null ? other.getState() == null : this.getState().equals(other.getState()))
                && (this.getWebsite() == null ? other.getWebsite() == null : this.getWebsite().equals(other.getWebsite()))
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
        result = prime * result + ((getOrganizationId() == null) ? 0 : getOrganizationId().hashCode());
        result = prime * result + ((getLogoId() == null) ? 0 : getLogoId().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getContact() == null) ? 0 : getContact().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getEstablishDate() == null) ? 0 : getEstablishDate().hashCode());
        result = prime * result + ((getIndustry() == null) ? 0 : getIndustry().hashCode());
        result = prime * result + ((getIntroduction() == null) ? 0 : getIntroduction().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getStaffSize() == null) ? 0 : getStaffSize().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getCity() == null) ? 0 : getCity().hashCode());
        result = prime * result + ((getProvince() == null) ? 0 : getProvince().hashCode());
        result = prime * result + ((getState() == null) ? 0 : getState().hashCode());
        result = prime * result + ((getWebsite() == null) ? 0 : getWebsite().hashCode());
        result = prime * result + ((getArea() == null) ? 0 : getArea().hashCode());
        result = prime * result + ((getProvinceArea() == null) ? 0 : getProvinceArea().hashCode());
        result = prime * result + ((getStateArea() == null) ? 0 : getStateArea().hashCode());
        return result;
    }
    @Override
    public String toString() {
        StringBuilder  sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", deletedAt=").append(deletedAt);
        sb.append(", name=").append(name);
        sb.append(", organizationId=").append(organizationId);
        sb.append(", logoId=").append(logoId);
        sb.append(", address=").append(address);
        sb.append(", contact=").append(contact);
        sb.append(", email=").append(email);
        sb.append(", establishDate=").append(establishDate);
        sb.append(", industry=").append(industry);
        sb.append(", introduction=").append(introduction);
        sb.append(", phone=").append(phone);
        sb.append(", staffSize=").append(staffSize);
        sb.append(", type=").append(type);
        sb.append(", city=").append(city);
        sb.append(", province=").append(province);
        sb.append(", state=").append(state);
        sb.append(", website=").append(website);
        sb.append(", area=").append(area);
        sb.append(", provinceArea=").append(provinceArea);
        sb.append(", stateArea=").append(stateArea);
        sb.append("]");
        return sb.toString();
    }
}
