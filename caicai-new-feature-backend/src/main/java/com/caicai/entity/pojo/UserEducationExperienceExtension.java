package com.caicai.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;



/**
 * @description: 用户教育经历扩展表
 * @author: YangFuyi
 * @create: 2025/5/22 17:22
 **/
@TableName(value ="user_education_experience_extension")
@Data
public class UserEducationExperienceExtension {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "user_education_experience_id")
    private Long userEducationExperienceId;
    
    @TableField(value = "is_graduated")
    private Integer isGraduated;
    
    @TableField(value = "graduation_date")
    private String graduationDate;
    
    @TableField(value = "certificate_pic")
    private String certificatePic;

    @TableField(value = "check_status")
    private Integer checkStatus;

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
        UserEducationExperienceExtension other = (UserEducationExperienceExtension) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserEducationExperienceId() == null ? other.getUserEducationExperienceId() == null : this.getUserEducationExperienceId().equals(other.getUserEducationExperienceId()))
            && (this.getIsGraduated() == null ? other.getIsGraduated() == null : this.getIsGraduated().equals(other.getIsGraduated()))
            && (this.getGraduationDate() == null ? other.getGraduationDate() == null : this.getGraduationDate().equals(other.getGraduationDate()))
            && (this.getCertificatePic() == null ? other.getCertificatePic() == null : this.getCertificatePic().equals(other.getCertificatePic()))
            && (this.getCheckStatus() == null ? other.getCheckStatus() == null : this.getCheckStatus().equals(other.getCheckStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserEducationExperienceId() == null) ? 0 : getUserEducationExperienceId().hashCode());
        result = prime * result + ((getIsGraduated() == null) ? 0 : getIsGraduated().hashCode());
        result = prime * result + ((getGraduationDate() == null) ? 0 : getGraduationDate().hashCode());
        result = prime * result + ((getCertificatePic() == null) ? 0 : getCertificatePic().hashCode());
        result = prime * result + ((getCheckStatus() == null) ? 0 : getCheckStatus().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userEducationExperienceId=").append(userEducationExperienceId);
        sb.append(", isGraduated=").append(isGraduated);
        sb.append(", graduationDate=").append(graduationDate);
        sb.append(", certificatePic=").append(certificatePic);
        sb.append(", status=").append(checkStatus);
        sb.append("]");
        return sb.toString();
    }
}
