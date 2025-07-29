package com.caicai.entity.vo;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdCardInfoVO {
    private Long id;
    private String idCardNum;
    private String idCardPic;
    private String name;
    
    /**
     * 审核状态: 0-待审核, 1-审核不通过, 2-审核通过
     */
    private Integer checkStatus;
} 