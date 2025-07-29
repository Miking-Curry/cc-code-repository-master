package com.caicai.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.caicai.entity.pojo.Certificate;
import com.caicai.entity.vo.CertificateInfoVO;
import java.util.List;

public interface CertificateService extends IService<Certificate> {
    boolean saveCertificateInfo(Long userId, String certificateName, String certificateUrl);

    /**
     * 根据ID直接删除证书记录
     * @param certificateId 证书ID
     * @return 操作结果
     */
    boolean deleteCertificateById(Long certificateId,Long userId);

    /**
     * 修改证书信息（名称和图片）
     * @param certificateId 证书ID
     * @param newCertificateName 新的证书名称
     * @param newCertificateImageUrl 新的证书图片URL (如果为null，则不更新图片)
     * @return 操作结果
     */
    boolean updateCertificateInfo(Long certificateId, String newCertificateName, String newCertificateImageUrl);

    /**
     * 根据用户ID查询该用户的所有技能证书列表（仅包含证书名称和图片URL）。
     * 
     * @param userId 用户ID
     * @return 返回一个列表，其中包含该用户所有技能证书的证书名称和图片URL。
     */
    List<CertificateInfoVO> getCertificateInfoListByUserId(Long userId);
    /**
     * 修改证书审核状态
     * @param certificateId 证书ID
     * @param checkStatus 审核状态
     * @return 操作结果
     */
    boolean updateCertificateStatus(Long certificateId, Integer checkStatus);

    /**
     * 管理员分页查询所有证书信息
     * 
     * @param current 当前页码
     * @param size 每页数据量
     * @return 包含证书信息的分页数据
     */
    Page<CertificateInfoVO> pageCertificatesForAdmin(Integer current, Integer size);
}
