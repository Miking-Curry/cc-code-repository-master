package com.caicai.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户扩展表
 * @TableName user_extension
 */
@TableName(value ="user_extension")
@Data
@Accessors(chain = true)
public class UserExtension {
    /**
     * 用户扩展信息ID，主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID，外键
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 身份证号码
     */
    @TableField(value = "id_card_num")
    private String idCardNum;

    /**
     * 身份证图片URL
     */
    @TableField(value = "id_card_pic")
    private String idCardPic;

    /**
     * 积分
     */
    @TableField(value = "point")
    private Long point;

    /**
     * 微信openid
     */
    @TableField(value = "wx_open_id")
    private String wxOpenId;

    /**
     * 信用评分
     */
    @TableField(value = "credit_score")
    private Integer creditScore;

    /**
     * 是否为VIP用户
     */
    @TableField(value = "is_vip_user")
    private Integer isVipUser;

   @TableField(value = "check_status")
    private Integer checkStatus;
   @TableField(value = "name")
   private String name;
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
        UserExtension other = (UserExtension) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getIdCardNum() == null ? other.getIdCardNum() == null : this.getIdCardNum().equals(other.getIdCardNum()))
            && (this.getIdCardPic() == null ? other.getIdCardPic() == null : this.getIdCardPic().equals(other.getIdCardPic()))
            && (this.getPoint() == null ? other.getPoint() == null : this.getPoint().equals(other.getPoint()))
            && (this.getWxOpenId() == null ? other.getWxOpenId() == null : this.getWxOpenId().equals(other.getWxOpenId()))
            && (this.getCreditScore() == null ? other.getCreditScore() == null : this.getCreditScore().equals(other.getCreditScore()))
            && (this.getIsVipUser() == null ? other.getIsVipUser() == null : this.getIsVipUser().equals(other.getIsVipUser()))
            && (this.getCheckStatus() == null ? other.getCheckStatus() == null : this.getCheckStatus().equals(other.getCheckStatus()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName())) ;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getIdCardNum() == null) ? 0 : getIdCardNum().hashCode());
        result = prime * result + ((getIdCardPic() == null) ? 0 : getIdCardPic().hashCode());
        result = prime * result + ((getPoint() == null) ? 0 : getPoint().hashCode());
        result = prime * result + ((getWxOpenId() == null) ? 0 : getWxOpenId().hashCode());
        result = prime * result + ((getCreditScore() == null) ? 0 : getCreditScore().hashCode());
        result = prime * result + ((getIsVipUser() == null) ? 0 : getIsVipUser().hashCode());
        result = prime * result + ((getCheckStatus() == null) ? 0 : getCheckStatus().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", id=" + id +
                ", userId=" + userId +
                ", idCardNum=" + idCardNum +
                ", idCardPic=" + idCardPic +
                ", point=" + point +
                ", wxOpenId=" + wxOpenId +
                ", creditScore=" + creditScore +
                ", isVipUser=" + isVipUser +
                "]";
    }
}