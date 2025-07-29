package com.caicai.entity.vo;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateInfoVO {
    /**
     * 证书ID
     */
    private Long id;
    /**
     * 证书名称
     */
    private String certificateName;

    /**
     * 证书图片URL
     */
    private String certificatePic;
    
    /**
     * 审核状态: 0-待审核, 1-审核不通过, 2-审核通过
     */
    private Integer checkStatus;
} 