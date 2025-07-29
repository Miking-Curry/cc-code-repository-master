package com.caicai.entity.model.resume;

import lombok.Data;

import java.time.LocalDate;

/**
 * @ClassName: CertificateMode
 * @Description: AI评分用资格证书基本信息
 * @Author: PCT
 * @Date: 2025/6/24 15:52
 * @Version: 1.0
 */

@Data
public class CertificateModel {
    private Long id;
    private String certificateName;
    private LocalDate issueDate;
}
