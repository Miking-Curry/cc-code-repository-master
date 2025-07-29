package com.caicai.entity.dto;

import lombok.Data;

/**
 * @description: 用户浏览岗位DTO
 * @author: LiWeihang
 * @create: 2025/5/21 17:29
 **/
@Data
public class JobViewDTO {

    private Long postId;

    private Integer viewTime;

    private Integer minSalary;

    private Integer maxSalary;

    private String industryCategory;

}
