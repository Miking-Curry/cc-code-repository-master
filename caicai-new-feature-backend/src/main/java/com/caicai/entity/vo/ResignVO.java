package com.caicai.entity.vo;


import lombok.Data;


@Data
public class ResignVO {
    private Long id;
    private String resignReason;
    private String resignAt;

    private String joinAt;

    private String witnessName;
    private String witnessPhone;
    
    /**
     * 审核状态: 0-待审核, 1-审核不通过, 2-审核通过
     */
    private Integer checkStatus;
}
