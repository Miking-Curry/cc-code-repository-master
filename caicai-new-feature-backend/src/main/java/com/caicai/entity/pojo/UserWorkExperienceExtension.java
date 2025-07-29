package com.caicai.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value ="user_work_experience_extension")
public class UserWorkExperienceExtension {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "social_security_record_pic_url")
    private String socialSecurityRecordPicUrl;
    @TableField(value="Work_years")
    private String workYears;
  /*@TableField(value = "user_work_experience_id")
    private Long userWorkExperienceId;*/
    @TableField(value = "user_id")
    private Long userId;
    @TableField(value = "check_status")
    private Integer checkStatus;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", socialSecurityUrl=").append(socialSecurityRecordPicUrl);
        sb.append(", workTime=").append(workYears);
        sb.append(", userId=").append(userId);
        sb.append(", status=").append(checkStatus);
        sb.append("]");
        return sb.toString();
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
        UserWorkExperienceExtension other = (UserWorkExperienceExtension) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getSocialSecurityRecordPicUrl() == null ? other.getSocialSecurityRecordPicUrl() == null : this.getSocialSecurityRecordPicUrl().equals(other.getSocialSecurityRecordPicUrl()))
                && (this.getWorkYears() == null ? other.getWorkYears() == null : this.getWorkYears().equals(other.getWorkYears()))
                && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
                && (this.getCheckStatus() == null ? other.getCheckStatus() == null : this.getCheckStatus().equals(other.getCheckStatus()));
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode()); 
        result = prime * result + ((getSocialSecurityRecordPicUrl() == null) ? 0 : getSocialSecurityRecordPicUrl().hashCode());
        result = prime * result + ((getWorkYears() == null) ? 0 : getWorkYears().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getCheckStatus() == null) ? 0 : getCheckStatus().hashCode());
        return result;
    }
}
