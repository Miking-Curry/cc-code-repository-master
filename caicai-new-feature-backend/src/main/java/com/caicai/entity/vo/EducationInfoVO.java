package com.caicai.entity.vo;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducationInfoVO {
    /**
     * 教育经历扩展ID
     */
    private Long id;
    private String schoolName;
    private String certificatePic;
    /**
     * 0:未审核 1:审核不通过 2:审核通过
     */
    private Integer  checkStatus;
} 