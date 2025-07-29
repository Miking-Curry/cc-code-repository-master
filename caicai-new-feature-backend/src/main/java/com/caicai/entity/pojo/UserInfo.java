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
 * @TableName user_info
 */
@TableName(value ="user_info")
@Data
public class UserInfo {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    @TableField(value = "name")
    private String name;

    /**
     * 
     */
    @TableField(value = "gender")
    private String gender;

    /**
     * 
     */
    @TableField(value = "birthday")
    private LocalDate birthday;

    /**
     * 
     */
    @TableField(value = "address")
    private String address;

    /**
     * 
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 
     */
    @TableField(value = "address_detail")
    private String addressDetail;

    /**
     * 
     */
    @TableField(value = "census_register")
    private String censusRegister;

    /**
     * 
     */
    @TableField(value = "height")
    private Integer height;

    /**
     * 
     */
    @TableField(value = "marriage")
    private String marriage;

    /**
     * 
     */
    @TableField(value = "nation")
    private String nation;

    /**
     * 
     */
    @TableField(value = "wechat")
    private String wechat;

    /**
     * 
     */
    @TableField(value = "weight")
    private Integer weight;

    /**
     * 
     */
    @TableField(value = "advantage")
    private String advantage;

    /**
     * 
     */
    @TableField(value = "created_at")
    private LocalDateTime createdAt;

    /**
     * 
     */
    @TableField(value = "deleted_at")
    private LocalDateTime deletedAt;

    /**
     * 
     */
    @TableField(value = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * 
     */
    @TableField(value = "job_status")
    private String jobStatus;

    /**
     * 
     */
    @TableField(value = "edu")
    private Integer edu;

    /**
     * 
     */
    @TableField(value = "work_exp")
    private Integer workExp;

    /**
     * 
     */
    @TableField(value = "major")
    private String major;

    /**
     * 
     */
    @TableField(value = "feature")
    private String feature;

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
        UserInfo other = (UserInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getGender() == null ? other.getGender() == null : this.getGender().equals(other.getGender()))
            && (this.getBirthday() == null ? other.getBirthday() == null : this.getBirthday().equals(other.getBirthday()))
            && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getAddressDetail() == null ? other.getAddressDetail() == null : this.getAddressDetail().equals(other.getAddressDetail()))
            && (this.getCensusRegister() == null ? other.getCensusRegister() == null : this.getCensusRegister().equals(other.getCensusRegister()))
            && (this.getHeight() == null ? other.getHeight() == null : this.getHeight().equals(other.getHeight()))
            && (this.getMarriage() == null ? other.getMarriage() == null : this.getMarriage().equals(other.getMarriage()))
            && (this.getNation() == null ? other.getNation() == null : this.getNation().equals(other.getNation()))
            && (this.getWechat() == null ? other.getWechat() == null : this.getWechat().equals(other.getWechat()))
            && (this.getWeight() == null ? other.getWeight() == null : this.getWeight().equals(other.getWeight()))
            && (this.getAdvantage() == null ? other.getAdvantage() == null : this.getAdvantage().equals(other.getAdvantage()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getDeletedAt() == null ? other.getDeletedAt() == null : this.getDeletedAt().equals(other.getDeletedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()))
            && (this.getJobStatus() == null ? other.getJobStatus() == null : this.getJobStatus().equals(other.getJobStatus()))
            && (this.getEdu() == null ? other.getEdu() == null : this.getEdu().equals(other.getEdu()))
            && (this.getWorkExp() == null ? other.getWorkExp() == null : this.getWorkExp().equals(other.getWorkExp()))
            && (this.getMajor() == null ? other.getMajor() == null : this.getMajor().equals(other.getMajor()))
            && (this.getFeature() == null ? other.getFeature() == null : this.getFeature().equals(other.getFeature()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getGender() == null) ? 0 : getGender().hashCode());
        result = prime * result + ((getBirthday() == null) ? 0 : getBirthday().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getAddressDetail() == null) ? 0 : getAddressDetail().hashCode());
        result = prime * result + ((getCensusRegister() == null) ? 0 : getCensusRegister().hashCode());
        result = prime * result + ((getHeight() == null) ? 0 : getHeight().hashCode());
        result = prime * result + ((getMarriage() == null) ? 0 : getMarriage().hashCode());
        result = prime * result + ((getNation() == null) ? 0 : getNation().hashCode());
        result = prime * result + ((getWechat() == null) ? 0 : getWechat().hashCode());
        result = prime * result + ((getWeight() == null) ? 0 : getWeight().hashCode());
        result = prime * result + ((getAdvantage() == null) ? 0 : getAdvantage().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getDeletedAt() == null) ? 0 : getDeletedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        result = prime * result + ((getJobStatus() == null) ? 0 : getJobStatus().hashCode());
        result = prime * result + ((getEdu() == null) ? 0 : getEdu().hashCode());
        result = prime * result + ((getWorkExp() == null) ? 0 : getWorkExp().hashCode());
        result = prime * result + ((getMajor() == null) ? 0 : getMajor().hashCode());
        result = prime * result + ((getFeature() == null) ? 0 : getFeature().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", gender=").append(gender);
        sb.append(", birthday=").append(birthday);
        sb.append(", address=").append(address);
        sb.append(", userId=").append(userId);
        sb.append(", addressDetail=").append(addressDetail);
        sb.append(", censusRegister=").append(censusRegister);
        sb.append(", height=").append(height);
        sb.append(", marriage=").append(marriage);
        sb.append(", nation=").append(nation);
        sb.append(", wechat=").append(wechat);
        sb.append(", weight=").append(weight);
        sb.append(", advantage=").append(advantage);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", deletedAt=").append(deletedAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", jobStatus=").append(jobStatus);
        sb.append(", edu=").append(edu);
        sb.append(", workExp=").append(workExp);
        sb.append(", major=").append(major);
        sb.append(", feature=").append(feature);
        sb.append("]");
        return sb.toString();
    }
}