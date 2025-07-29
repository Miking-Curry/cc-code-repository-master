package com.caicai.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("user_post_matching")
@Data
public class UserPostMatching {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField(value = "user_id")
    private Long userId;
    @TableField(value = "post_id")
    private Long postId;
    @TableField(value = "match_percent")
    private Integer matchPercent;
    @TableField(value = "created_at", fill = FieldFill.INSERT)
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
        UserPostMatching other = (UserPostMatching) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getPostId() == null ? other.getPostId() == null : this.getPostId().equals(other.getPostId()))
            && (this.getMatchPercent() == null ? other.getMatchPercent() == null : this.getMatchPercent().equals(other.getMatchPercent()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getPostId() == null) ? 0 : getPostId().hashCode());
        result = prime * result + ((getMatchPercent() == null) ? 0 : getMatchPercent().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
       StringBuilder  sb = new StringBuilder();
       sb.append(getClass().getSimpleName());
       sb.append(" [");
       sb.append("Hash = ").append(hashCode());
       sb.append(", id=").append(id);
       sb.append(", userId=").append(userId);
       sb.append(", postId=").append(postId);
       sb.append(", matchingPercent=").append(matchPercent);
       sb.append(", createdAt=").append(createdAt);
       sb.append("]");
       return sb.toString();
    }
}
