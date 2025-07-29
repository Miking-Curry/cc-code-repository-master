package com.caicai.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
/**
 * @Description:  职位标签表
 */
@TableName("post_tag")
@Data
public class PostTag {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField(value = "post_id")
    private Integer postId;
    @TableField(value = "name")
    private String name;
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    @TableField(value = "deleted_at")
    private LocalDateTime deletedAt;

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
        PostTag other = (PostTag) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
                && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()))
                && (this.getDeletedAt() == null ? other.getDeletedAt() == null : this.getDeletedAt().equals(other.getDeletedAt()))
                && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
                && (this.getPostId() == null ? other.getPostId() == null : this.getPostId().equals(other.getPostId()));
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
        result = prime * result + ((getPostId() == null) ? 0 : getPostId().hashCode());

        return result;
    }

    @Override
    public String toString() {
       StringBuilder sb=new StringBuilder();
       sb.append(getClass().getSimpleName());
       sb.append(" [");
       sb.append("Hash = ").append(hashCode());
       sb.append(", id=").append(id);
       sb.append(", createdAt=").append(createdAt);
       sb.append(", updatedAt=").append(updatedAt);
       sb.append(", deletedAt=").append(deletedAt);
       sb.append(", name=").append(name);
       sb.append(", postId=").append(postId);
       sb.append("]");
       return sb.toString();
    }
}
