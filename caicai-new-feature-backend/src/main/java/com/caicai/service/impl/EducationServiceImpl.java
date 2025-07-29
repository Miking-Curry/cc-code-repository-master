package com.caicai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caicai.entity.pojo.UserEducationExperience;
import com.caicai.entity.pojo.UserEducationExperienceExtension;
import com.caicai.entity.pojo.UserExtension;
import com.caicai.entity.vo.EducationInfoVO;
import com.caicai.mapper.UserEducationExperienceExtensionMapper;
import com.caicai.mapper.UserEducationExperienceMapper;
import com.caicai.mapper.UserExtensionMapper;
import com.caicai.service.EducationService;
import com.caicai.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 教育经历服务实现类
 * @author YangFuyi
 * @create 2025/5/22
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class EducationServiceImpl extends ServiceImpl<UserEducationExperienceMapper, UserEducationExperience> implements EducationService {

    private final UserEducationExperienceExtensionMapper educationExtensionMapper;
    private final UserExtensionMapper userExtensionMapper;
    private final FileService  fileService;
    /**
     * 保存/更新教育信息
     * @param userId 用户ID
     * @param schoolName 学校名称
     * @param certificatePicUrl 证书图片URL
     * @return 是否保存/更新成功
     */
    @Override
    @Transactional
    public boolean saveEducationInfo(Long userId, String schoolName, String certificatePicUrl) {
            log.info("开始为用户ID: {} 保存/更新教育经历,学校名称: {}, 证书图片URL: {}", userId, schoolName, certificatePicUrl);
            UserEducationExperience educationExperience;
            boolean isNewExperience = false;

            educationExperience = this.baseMapper.selectByUserId(userId);

        LocalDate DEFAULT_START_DATE = LocalDate.of(1970, 1, 1);

            if (educationExperience == null) {
                isNewExperience = true;
                educationExperience = new UserEducationExperience();
                educationExperience.setUserId(userId);
                educationExperience.setMajor("未填写");
                educationExperience.setDegree(0);
                educationExperience.setIsFullTime(1);
                educationExperience.setStartTime(DEFAULT_START_DATE);
                educationExperience.setEndTime(null);
                educationExperience.setSchoolExperience("");
                log.info("未找到用户ID: {} 的现有教育经历，将创建新的记录。", userId);
            } else {
                log.info("找到用户ID: {} 的现有教育经历，ID: {}，将进行更新。", userId, educationExperience.getId());
            }

            educationExperience.setSchoolName(schoolName);

            if (isNewExperience) {
                boolean saved = this.save(educationExperience);
                if (!saved || educationExperience.getId() == null) {
                    log.error("为用户ID: {} 创建新的教育经历记录失败", userId);
                    return false;
                }
                log.info("为用户ID: {} 成功创建新的教育经历记录，ID: {}", userId, educationExperience.getId());
            } else {
                boolean updated = this.updateById(educationExperience);
                if (!updated) {
                    log.warn("更新用户ID: {} 的教育经历记录 (ID: {})时，数据库没有行受影响", userId, educationExperience.getId());
                }
                log.info("成功更新用户ID: {} 的教育经历记录，ID: {}", userId, educationExperience.getId());
            }

            UserEducationExperienceExtension educationExtension;
            boolean isNewExtension = false;

            if (educationExperience.getId() == null) {
                log.error("无法处理教育经历扩展信息，因为主教育经历记录的ID为空。用户ID: {}", userId);
                return false;
            }

            educationExtension = educationExtensionMapper.selectByExperienceId(educationExperience.getId());

            if (educationExtension == null) {
                isNewExtension = true;
                educationExtension = new UserEducationExperienceExtension();
                educationExtension.setUserEducationExperienceId(educationExperience.getId());
                educationExtension.setIsGraduated(1);
                educationExtension.setGraduationDate(LocalDateTime.now().toLocalDate().toString());
                log.info("未找到教育经历ID: {} 的现有扩展记录，将创建新的记录", educationExperience.getId());
            } else {
                log.info("找到教育经历ID: {} 的现有扩展记录，ID: {}，将进行更新", educationExperience.getId(), educationExtension.getId());
                
                // 检查是否需要删除旧图片
                if (StringUtils.hasText(educationExtension.getCertificatePic()) && 
                        !educationExtension.getCertificatePic().equals(certificatePicUrl)) {
                    log.info("删除教育经历ID: {} 的旧证书图片: {}", educationExperience.getId(), educationExtension.getCertificatePic());
                    fileService.deleteFile(educationExtension.getCertificatePic());
                }
            }

            educationExtension.setCertificatePic(certificatePicUrl);

            if (isNewExtension) {
                int insertedRows = educationExtensionMapper.insert(educationExtension);
                if (insertedRows == 0) {
                    log.error("为教育经历ID: {} 创建新的扩展记录失败", educationExperience.getId());
                    throw new RuntimeException("保存毕业证书信息失败");
                }
                log.info("为教育经历ID: {} 成功创建新的扩展记录，ID: {}", educationExperience.getId(), educationExtension.getId());
            } else {
                int updatedRows = educationExtensionMapper.updateById(educationExtension);
                if (updatedRows == 0) {
                    log.warn("更新教育经历ID: {} 的扩展记录 (ID: {})时，数据库没有行受影响", educationExperience.getId(), educationExtension.getId());
                }
                log.info("成功更新教育经历ID: {} 的扩展记录，ID: {}", educationExperience.getId(), educationExtension.getId());
            }

            log.info("用户ID: {} 的教育经历和毕业证书信息已成功保存/更新", userId);
            return true;


    }
    /**
     * 获取教育信息
     * @param userId 用户ID
     * @return EducationInfoVO
     */
    @Override
    public EducationInfoVO getEducationInfo(Long userId) {
            log.info("开始为用户ID: {} 查询教育信息 (学校名称和证书图片)。", userId);
            if (userId == null) {
                log.warn("查询教育信息失败：用户ID为空。");
                return null;
            }
            EducationInfoVO educationInfo = this.baseMapper.selectEducationInfoByUserId(userId);
            if (educationInfo == null) {
                log.info("未找到用户ID: {} 的教育信息。", userId);
                return null; // 返回null数据体和提示消息
            }
            if (educationInfo.getSchoolName() == null && educationInfo.getCertificatePic() == null) {
                 log.info("用户ID: {} 的教育信息记录存在，但学校名称和证书图片均为空。", userId);
                 return null;
            }
            log.info("成功为用户ID: {} 查询到教育信息。学校: {}, 证书图片: {}", userId, educationInfo.getSchoolName(), educationInfo.getCertificatePic());
            return educationInfo;
    }
    /**
     * 清除教育信息
     * @param userId 用户ID
     * @return boolean
     */
    @Override
    @Transactional
    public boolean clearEducationInfo(Long userId) {
        log.info("开始为用户ID: {} 清除教育经历信息（学校名称和证书图片）", userId);

        if (userId == null) {
            log.warn("清除教育经历信息失败：用户ID为空。");
            return false;
        }

        // 1. 查询用户的教育经历记录
        UserEducationExperience educationExperience = this.baseMapper.selectByUserId(userId);

        if (educationExperience == null) {
            log.info("未找到用户ID: {} 的教育经历记录，无需清除。", userId);
            return false;
        }

        // 2. 使用LambdaUpdateWrapper显式设置学校名称为null
        LambdaUpdateWrapper<UserEducationExperience> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserEducationExperience::getId, educationExperience.getId())
                .set(UserEducationExperience::getSchoolName, null);
        boolean mainRecordUpdated = this.update(null, updateWrapper);

        if (!mainRecordUpdated) {
            log.warn("清除用户ID: {} 的学校名称失败，更新操作未影响任何行。", userId);
        } else {
            log.info("已成功清除用户ID: {} 的学校名称。", userId);
        }
        // 3. 查询并清除证书图片
        UserEducationExperienceExtension educationExtension =
                educationExtensionMapper.selectByExperienceId(educationExperience.getId());

        if (educationExtension != null) {
            //先调用fileService删除图片
            fileService.deleteFile(educationExtension.getCertificatePic());
            // 使用LambdaUpdateWrapper显式设置证书图片为null
            LambdaUpdateWrapper<UserEducationExperienceExtension> extensionUpdateWrapper = new LambdaUpdateWrapper<>();
            extensionUpdateWrapper.eq(UserEducationExperienceExtension::getId, educationExtension.getId())
                    .set(UserEducationExperienceExtension::getCertificatePic, null);
            int extensionUpdated = educationExtensionMapper.update(null, extensionUpdateWrapper);

            if (extensionUpdated == 0) {
                log.warn("清除用户ID: {} 的毕业证书图片失败，更新操作未影响任何行。", userId);
            } else {
                log.info("已成功清除用户ID: {} 的毕业证书图片。", userId);
            }
        } else {
            log.info("未找到用户ID: {} 教育经历ID: {} 的扩展记录，无需清除证书图片。",
                    userId, educationExperience.getId());
        }

        // 4. 更新信用分数
        UserExtension userExtension = userExtensionMapper.selectById(userId);
        LambdaUpdateWrapper<UserExtension> userExtUpdateWrapper = new LambdaUpdateWrapper<>();
        userExtUpdateWrapper.eq(UserExtension::getUserId, userId)
                .set(UserExtension::getCreditScore, userExtension.getCreditScore() - 2);
        userExtensionMapper.update(null, userExtUpdateWrapper);

        log.info("用户ID: {} 的教育经历信息（学校名称和证书图片）已成功清除。", userId);
        return true;
    }

    @Override
    @Transactional
    public boolean updateEducationStatus(Long userEducationExperienceExtensionId, Integer checkStatus) {
        //根据教育经历扩展查询教育经历扩展信息
        UserEducationExperienceExtension educationExtension = educationExtensionMapper.selectById(userEducationExperienceExtensionId);
        if(educationExtension == null){
            log.warn("教育经历扩展信息不存在，无法更新状态");
            return false;
        }
        educationExtension.setCheckStatus(checkStatus);
        int UpdateResult = educationExtensionMapper.updateById(educationExtension);
        if(UpdateResult > 0){
            log.info("更新教育经历扩展信息成功");
            return true;
        }
        log.error("更新教育经历扩展信息失败");
        return false;
    }

    /**
     * 管理员分页查询所有教育信息
     * 
     * @param current 当前页码
     * @param size 每页数据量
     * @return 包含教育信息的分页数据
     */
    @Override
    public Page<EducationInfoVO> pageEducationsForAdmin(Integer current, Integer size) {
        log.info("开始分页查询教育信息，页码：{}，每页数量：{}", current, size);
        
        // 创建分页对象和结果页对象
        Page<EducationInfoVO> resultPage = new Page<>(current, size);
        
        // 调用自定义Mapper方法进行分页查询
        List<EducationInfoVO> records = this.baseMapper.selectEducationInfoForAdmin((current - 1) * size, size);
        long total = this.baseMapper.countEducationInfoForAdmin();
        
        resultPage.setRecords(records);
        resultPage.setTotal(total);
        resultPage.setCurrent(current);
        resultPage.setSize(size);
        resultPage.setPages((total + size - 1) / size); // 计算总页数
        
        if (!CollectionUtils.isEmpty(records)) {
            log.info("分页查询教育信息成功，共查询到 {} 条记录", records.size());
        } else {
            resultPage.setRecords(Collections.emptyList());
            log.info("分页查询教育信息成功，但未查询到记录");
        }
        
        return resultPage;
    }
}
