package com.caicai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caicai.entity.pojo.*;
import com.caicai.mapper.*;
import com.caicai.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;



import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class CreditServiceImpl extends ServiceImpl<UserExtensionMapper, UserExtension> implements CreditService {

    
    // 添加所需的Mapper依赖
    private final CertificateMapper certificateMapper;
    private final UserEducationExperienceMapper userEducationMapper;
    private final UserEducationExperienceExtensionMapper educationMapper;
    private final UserWorkExperienceExtensionMapper workExperienceMapper;
    private final ResignMapper resignMapper;

    /**
     * 获取信用分
     * @param userId
     * @return
     */
    @Override
    public Integer getCreditScore(Long userId) {
        // 通过userId查询用户扩展信息（userId是外键，不是主键）
        UserExtension userExtension = baseMapper.selectByUserId(userId);
                        
        int credit = 0;

        // 判断此用户是否存在
        if (userExtension == null) {
            log.warn("用户不存在或未创建扩展信息, ID: {}", userId);
            return 0; // 用户不存在直接返回0分
        }

        // 判断此用户的审核状态
        if (userExtension.getCheckStatus() != null) {
            credit += 1;
        }

        // 直接查询证书信息的审核状态
        List<Certificate> certificates = certificateMapper.selectList(
                new LambdaQueryWrapper<Certificate>()
                        .eq(Certificate::getUserId, userId));
        
        if (certificates != null && !certificates.isEmpty()) {
            // 判断证书是否存在一个审核通过的
            boolean hasApprovedCertificate = certificates.stream()
                    .anyMatch(certificate -> certificate.getCheckStatus() != null);

            if (hasApprovedCertificate) {
                credit += 2;
            }
        } else {
            log.warn("用户 {} 的证书信息为空", userId);
        }

        // 直接查询工作经历的审核状态
        UserWorkExperienceExtension workExperienceExt = workExperienceMapper.selectByUserId(userId);
        
        if (workExperienceExt == null) {
            log.warn("用户 {} 的工作经历为空", userId);
        } else {
            // 判断工作经历是否审核通过
            if (workExperienceExt.getCheckStatus() != null) {
                credit += 5;
            }
        }

        // 直接查询离职信息的审核状态
        UserResign resign = resignMapper.selectByUserId(userId);
        
        if (resign == null) {
            log.warn("用户 {} 的离职信息为空", userId);
        } else {
            // 判断离职信息是否审核通过
            if (resign.getCheckStatus() != null) {
                credit += 1;
            }
        }
        
        // 使用自定义的selectByUserId方法查询教育经历
        UserEducationExperience userEducationExperience = userEducationMapper.selectByUserId(userId);
        
        // 判断是否为空
        if (userEducationExperience == null) {
            log.warn("用户 {} 的教育经历为空", userId);
            // 提前返回或跳过后续处理
            return credit;
        }
        
        log.info("成功查询到用户 {} 的教育经历，ID: {}, 学校: {}", userId, 
                userEducationExperience.getId(), userEducationExperience.getSchoolName());

        // 根据教育经历表的主键ID去查询教育经历扩展表的信息，获取checkStatus的值
        UserEducationExperienceExtension educationExperienceExt = educationMapper.selectByExperienceId(userEducationExperience.getId());
        
        // 判断是否为空
        if (educationExperienceExt == null) {
            log.warn("用户 {} 的教育经历扩展信息为空，教育经历ID: {}", userId, userEducationExperience.getId());
            return credit;
        }
        
        log.info("成功查询到用户 {} 的教育经历扩展信息，教育经历ID: {}, 审核状态: {}", 
                userId, userEducationExperience.getId(), educationExperienceExt.getCheckStatus());

        if (educationExperienceExt.getCheckStatus() != null) {
            credit += 1;
            log.info("用户 {} 的教育经历审核已通过，信用分+1", userId);
        }

        log.info("用户 ID: {} 的信用分是: {}", userId, credit);

        // 使用LambdaUpdateWrapper更新积分
        LambdaUpdateWrapper<UserExtension> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserExtension::getUserId, userId)
                .set(UserExtension::getCreditScore, credit);
        baseMapper.update(null, updateWrapper);

        return credit;
    }
}