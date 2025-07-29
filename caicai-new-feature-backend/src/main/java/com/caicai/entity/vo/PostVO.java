package com.caicai.entity.vo;

import com.caicai.enums.PostStatusType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

@Data
public class PostVO {
    // 岗位id
    private Long postId;
    // 岗位名称
    private String postName;
    // 岗位标签
    private List<String> postTag = new ArrayList<>();
    // 岗位创建时间
    private LocalDateTime createdAt;
    // 岗位最低工资
    private Integer minSalary;
    // 岗位最高工资
    private Integer maxSalary;
    // 岗位状态枚举列表 - 在JSON中不显示
    @JsonIgnore
    private List<PostStatusType> postStatuses = new ArrayList<>();
    // 岗位状态描述列表 - 用于前端显示
    private List<String> postStatusDescs = new ArrayList<>();
    // 城市
    private String city;
    // 省份
    private String province;
    // 地区
    private String state;
    // 公司类型
    private String companyType;
    // 公司规模
    private Integer staffSize;
    // 公司名称
    private String companyName;
    // 公司logo URL
    private String companyLogoUrl;
    // 岗位匹配度
    private Double matchPercent;
    
    /**
     * 设置状态字符串，并转换为状态枚举列表和描述列表
     * 该方法仅用于MyBatis映射，不会暴露给前端
     * @param statusStr 逗号分隔的状态字符串
     */
    public void setPostStatusStr(String statusStr) {
        // 如果有状态，将其分割并转换为枚举
        if (StringUtils.hasText(statusStr)) {
            this.postStatuses = Arrays.stream(statusStr.split(","))
                .map(String::trim)
                .filter(status -> !status.isEmpty())
                .map(Integer::parseInt)
                .map(PostStatusType::getByCode)
                .filter(status -> status != null)
                .collect(Collectors.toList());
            
            // 同时设置状态描述列表
            this.postStatusDescs = this.postStatuses.stream()
                .map(PostStatusType::getDesc)
                .collect(Collectors.toList());
        } else {
            this.postStatuses = new ArrayList<>();
            this.postStatusDescs = new ArrayList<>();
        }
    }
}
