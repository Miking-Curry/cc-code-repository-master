package com.caicai.entity.vo;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWorkExperienceVO {
    private Long id;
    private String workYears;
    private String socialSecurityRecordPicUrl;
    /**
     * 审核状态: 0-待审核, 1-审核不通过, 2-审核通过
     */
    private Integer checkStatus;
}
