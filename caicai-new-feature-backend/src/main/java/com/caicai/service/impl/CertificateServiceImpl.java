package com.caicai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caicai.entity.pojo.Certificate;
import com.caicai.entity.pojo.UserExtension;
import com.caicai.entity.vo.CertificateInfoVO;
import com.caicai.mapper.CertificateMapper;
import com.caicai.mapper.UserExtensionMapper;
import com.caicai.service.CertificateService;
import com.caicai.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
/**
 * @Description: 证书服务实现类
 * @Author: YangFuyi
 * @Date: 2025-05-23 11:17
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CertificateServiceImpl extends ServiceImpl<CertificateMapper, Certificate> implements CertificateService {
    private final UserExtensionMapper userExtensionMapper;
    private final FileService fileService;
    /**
     * 保存证书信息
     * @param userId 用户ID
     * @param certificateName 证书名称
     * @param certificateUrl 证书图片URL
     * @return 保存成功返回true，否则返回false
     */
    @Override
    @Transactional
    public boolean saveCertificateInfo(Long userId, String certificateName, String certificateUrl) {
            UserExtension userExtension = userExtensionMapper.selectById(userId);
            Certificate certificate = new Certificate();
            certificate.setUserId(userId);
            certificate.setCertificateName(certificateName);
            certificate.setCertificatePic(certificateUrl);
            boolean saved = this.save(certificate);
            if (saved) {
                log.info("用户ID: {} 的新证书记录创建成功，ID: {}", userId, certificate.getId());
                return true;
            } else {
                log.error("为用户ID: {} 保存证书信息失败。", userId);
                return false;
            }
    }
    @Override
    @Transactional
    public boolean deleteCertificateById(Long certificateId, Long userId) {
        UserExtension userExtension = userExtensionMapper.selectById(userId);
        Certificate certificate = this.getById(certificateId);

        if (certificate == null) {
            log.warn("尝试删除不存在的证书记录，ID: {}", certificateId);
            return false;
        }
        //先删除文件
        fileService.deleteFile(certificate.getCertificatePic());
        //再删除数据
        boolean deleted = this.removeById(certificateId);

        // 查询这个用户的所有技能证书
        List<Certificate> certificates = this.list(new LambdaQueryWrapper<Certificate>().eq(Certificate::getUserId, userId));

        // 做判断，查看用户是否还有其他技能证书,如果没有技能证书，则减去积分
        if (certificates.isEmpty()) {
            // 使用LambdaUpdateWrapper显式更新积分
            LambdaUpdateWrapper<UserExtension> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(UserExtension::getUserId, userId)
                    .set(UserExtension::getCreditScore, userExtension.getCreditScore() - 2);
            userExtensionMapper.update(null, updateWrapper);
            log.info("用户ID: {} 的所有证书已删除，信用分已减少2分", userId);
        }

        if (deleted) {
            log.info("证书记录 ID: {} 已成功删除", certificateId);
            return true;
        } else {
            log.error("删除证书记录 ID: {} 失败，但记录可能仍存在或已被删除。数据库操作未报告成功。", certificateId);
            return false;
        }
    }
    /**
     * 修改技能证书信息
     * @param certificateId 证书ID
     * @param newCertificateName 新证书名称
     * @param newCertificateImageUrl 新证书图片URL
     * @return 修改成功返回true，否则返回false
     */
    @Override
    @Transactional
    public boolean updateCertificateInfo(Long certificateId, String newCertificateName, String newCertificateImageUrl) {
            Certificate certificate = this.getById(certificateId);
            if (certificate == null) {
                log.warn("尝试修改不存在的证书记录，ID: {}", certificateId);
                return false;
            }
            if (!StringUtils.hasText(newCertificateName)) {
                log.warn("修改证书 ID: {} 失败: 新证书名称为空", certificateId);
                return false;
            }
            certificate.setCertificateName(newCertificateName);
            log.info("证书 ID: {} 名称已更新为: {}", certificateId, newCertificateName);
            if (StringUtils.hasText(newCertificateImageUrl)) {
                // 如果提供了新图片URL，并且与旧图片不同，则删除旧图片
                if (StringUtils.hasText(certificate.getCertificatePic()) && 
                        !certificate.getCertificatePic().equals(newCertificateImageUrl)) {
                    log.info("删除证书 ID: {} 的旧图片: {}", certificateId, certificate.getCertificatePic());
                    fileService.deleteFile(certificate.getCertificatePic());
                }
                certificate.setCertificatePic(newCertificateImageUrl);
                log.info("证书 ID: {} 图片已更新为: {}", certificateId, newCertificateImageUrl);
            } else {
                log.info("证书 ID: {} 修改时未提供新图片URL，图片未更改。", certificateId);
            }
            boolean updated = this.updateById(certificate);
            if (updated) {
                log.info("证书记录 ID: {} 已成功修改", certificateId);
                return true;
            } else {
                log.error("修改证书记录 ID: {} 失败，数据库操作可能未成功（数据可能未变化）。", certificateId);
                return false;
            }
    }
    @Override
    @Transactional
    public List<CertificateInfoVO> getCertificateInfoListByUserId(Long userId) {
        // 先根据用户ID查询证书实体列表
        LambdaQueryWrapper<Certificate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Certificate::getUserId, userId)
                .orderByDesc(Certificate::getCreatedAt);
        List<Certificate> certificates = this.list(queryWrapper);

        if (CollectionUtils.isEmpty(certificates)) {
            log.info("未找到用户ID: {} 的任何技能证书。", userId);
            return Collections.emptyList(); // 返回空列表而不是null
        }

        // 将实体转换为VO对象
        List<CertificateInfoVO> certificateList = certificates.stream()
                .map(cert -> {
                    CertificateInfoVO vo = new CertificateInfoVO();
                    vo.setId(cert.getId());
                    vo.setCertificateName(cert.getCertificateName());
                    vo.setCertificatePic(cert.getCertificatePic());
                    vo.setCheckStatus(cert.getCheckStatus());
                    return vo;
                })
                .collect(java.util.stream.Collectors.toList());

        log.info("成功为用户ID: {} 查询到 {} 条技能证书记录。", userId, certificateList.size());

        return certificateList; // 返回已查询的列表而不是再次查询
    }
    /**
     * 修改证书状态(管理员审核）
     * @param certificateId
     * @param checkStatus
     * @return
     */
    @Override
    @Transactional
    public boolean updateCertificateStatus(Long certificateId, Integer checkStatus) {
            //根据证书id，查询证书
            Certificate certificate = this.getById(certificateId);
            if (certificate == null) {
                log.warn("尝试修改不存在的证书，ID: {}", certificateId);
                return false;
            }
            //修改证书状态
            certificate.setCheckStatus(checkStatus);
            boolean updated = this.updateById(certificate);
            if (updated) {
                log.info("证书状态已修改为: {}", checkStatus);
                return true;
            }
             return false;
    }
    /**
     * 管理员分页查询所有证书信息
     * 
     * @param current 当前页码
     * @param size 每页数据量
     * @return 包含证书信息的分页数据
     */
    @Override
    public Page<CertificateInfoVO> pageCertificatesForAdmin(Integer current, Integer size) {
        log.info("开始分页查询证书信息，页码：{}，每页数量：{}", current, size);
        
        // 创建分页对象
        Page<Certificate> certificatePage = new Page<>(current, size);
        
        // 创建查询条件
        LambdaQueryWrapper<Certificate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Certificate::getCreatedAt);
        
        // 执行分页查询
        this.page(certificatePage, queryWrapper);
        
        // 创建返回结果页对象
        Page<CertificateInfoVO> resultPage = new Page<>(current, size);
        resultPage.setTotal(certificatePage.getTotal());
        resultPage.setCurrent(certificatePage.getCurrent());
        resultPage.setSize(certificatePage.getSize());
        resultPage.setPages(certificatePage.getPages());
        
        // 转换查询结果为VO对象
        if (!CollectionUtils.isEmpty(certificatePage.getRecords())) {
            List<CertificateInfoVO> records = certificatePage.getRecords().stream()
                    .map(cert -> {
                        CertificateInfoVO vo = new CertificateInfoVO();
                        vo.setId(cert.getId());
                        vo.setCertificateName(cert.getCertificateName());
                        vo.setCertificatePic(cert.getCertificatePic());
                        vo.setCheckStatus(cert.getCheckStatus());
                        return vo;
                    })
                    .collect(Collectors.toList());
            
            resultPage.setRecords(records);
            log.info("分页查询证书信息成功，共查询到 {} 条记录", records.size());
        } else {
            resultPage.setRecords(Collections.emptyList());
            log.info("分页查询证书信息成功，但未查询到记录");
        }
        
        return resultPage;
    }
}
