package com.caicai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caicai.entity.pojo.UserWorkExperienceExtension;
import com.caicai.entity.vo.UserWorkExperienceVO;
import com.caicai.mapper.UserWorkExperienceExtensionMapper;
import com.caicai.service.FileService;
import com.caicai.service.WorkExperienceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class WorkExperienceServiceImpl extends ServiceImpl<UserWorkExperienceExtensionMapper, UserWorkExperienceExtension> implements WorkExperienceService {
    private final FileService fileService;
    /**
     * 保存工作经历信息
     * @param userId
     * @param socialSecurityRecordPicUrl
     * @return
     */
    @Override
    @Transactional
    public boolean saveWorkExperienceInfo(Long userId,String workYears,String socialSecurityRecordPicUrl) {
        // 创建工作经历扩展信息对象
        UserWorkExperienceExtension workExperienceExtension = null;
            workExperienceExtension = new UserWorkExperienceExtension();
            // 保存用户工作经历信息
            workExperienceExtension.setUserId(userId);
            workExperienceExtension.setWorkYears(workYears);
            workExperienceExtension.setSocialSecurityRecordPicUrl(socialSecurityRecordPicUrl);
            boolean saved = this.save(workExperienceExtension);
            if (saved) {
                log.info("用户ID: {} 的工作经历记录创建成功，ID: {}", userId, workExperienceExtension.getId());
                return true;
            } else {
                log.error("为用户ID: {} 保存工作经历信息失败。", userId);
                return false;
            }
    }
    /**
     * 根据用户ID获取工作经历信息
     * @param userId
     * @return UserWorkExperienceVO
     */
    @Override
    public UserWorkExperienceVO getWorkExperienceInfo(Long userId) {
        log.info("开始获取用户ID: {} 的工作经历信息", userId);

        // 使用LambdaQueryWrapper根据用户ID查询工作经历信息
        LambdaQueryWrapper<UserWorkExperienceExtension> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserWorkExperienceExtension::getUserId, userId);

        // 获取用户的工作经历信息
        UserWorkExperienceExtension workExperienceExtension = this.getOne(queryWrapper);

        // 为空则返回null
        if (workExperienceExtension == null) {
            log.info("未找到用户ID: {} 的工作经历信息", userId);
            return null;
        }

        // 转换为VO
        UserWorkExperienceVO workExperienceVO = new UserWorkExperienceVO();
        BeanUtils.copyProperties(workExperienceExtension, workExperienceVO);

        log.info("成功获取用户ID: {} 的工作经历信息", userId);
        return workExperienceVO;
    }
    /**
     * 删除工作经历信息
     * @param userId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeExperienceInfo(Long userId) {
        // 使用LambdaQueryWrapper根据用户ID查询工作经历
        LambdaQueryWrapper<UserWorkExperienceExtension> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserWorkExperienceExtension::getUserId, userId);
        // 先删除文件图片
        UserWorkExperienceExtension workExperienceExtension = this.getOne(queryWrapper);
        if(workExperienceExtension != null) {
            fileService.deleteFile(workExperienceExtension.getSocialSecurityRecordPicUrl());
        }
        // 删除该用户的所有工作经历
        boolean removed = this.remove(queryWrapper);

        if (removed) {
            log.info("用户ID: {} 的工作经历信息删除成功", userId);
            return true;
        } else {
            log.error("用户ID: {} 的工作经历信息删除失败", userId);
            return false;
        }
    }
    /**
     * 根据用户ID修改工作经历信息
     * @param userId 用户ID

     * @param workYears 工作年限
     * @param socialSecurityRecordPicUrl 社保记录图片URL
     * @return 操作结果
     */
    @Override
    @Transactional
    public boolean updateExperienceInfo(Long userId,  String workYears, String socialSecurityRecordPicUrl) {
        // 先根据用户ID查询工作经历记录
        LambdaQueryWrapper<UserWorkExperienceExtension> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserWorkExperienceExtension::getUserId, userId);
        UserWorkExperienceExtension existingRecord = this.getOne(queryWrapper);

        if (existingRecord == null) {
            log.error("用户ID: {} 的工作经历记录不存在，无法更新", userId);
            return false;
        }

        // 检查是否需要删除旧图片
        if (StringUtils.hasText(existingRecord.getSocialSecurityRecordPicUrl()) && 
                !existingRecord.getSocialSecurityRecordPicUrl().equals(socialSecurityRecordPicUrl)) {
            log.info("删除用户ID: {} 的旧社保图片: {}", userId, existingRecord.getSocialSecurityRecordPicUrl());
            fileService.deleteFile(existingRecord.getSocialSecurityRecordPicUrl());
        }

        // 更新工作经历信息
        UserWorkExperienceExtension workExperienceExtension = new UserWorkExperienceExtension();
        workExperienceExtension.setId(existingRecord.getId());
        workExperienceExtension.setWorkYears(workYears);
        workExperienceExtension.setSocialSecurityRecordPicUrl(socialSecurityRecordPicUrl);

        boolean updated = this.updateById(workExperienceExtension);
        if (updated) {
            log.info("用户ID: {} 的工作经历信息更新成功", userId);
            return true;
        }

        log.error("用户ID: {} 的工作经历信息更新失败", userId);
        return false;
    }

    @Override
    @Transactional
    public boolean updateExperienceStatus(Long workExperienceId, Integer checkStatus) {
        // 创建工作经历扩展信息对象
        UserWorkExperienceExtension workExperienceExtension =new UserWorkExperienceExtension();
        // 根据Id获取工作经历信息
        workExperienceExtension = this.getById(workExperienceId);
        // 判断是否为空
        if(workExperienceExtension == null)
        {
            log.error("用户ID: {} 的工作经历信息不存在", workExperienceId);
            return false;
        }
        workExperienceExtension.setCheckStatus(checkStatus);
        boolean updated = this.updateById(workExperienceExtension);
        if (updated) {
            log.info("用户ID: {} 的工作经历状态已修改为: {}", workExperienceId, checkStatus);
            return true;
        }
        log.error("用户ID: {} 的工作经历状态修改失败", workExperienceId);
        return false;
    }

    /**
     * 管理员分页查询所有工作经验信息
     * 
     * @param current 当前页码
     * @param size 每页数据量
     * @return 包含工作经验信息的分页数据
     */
    @Override
    public Page<UserWorkExperienceVO> pageWorkExperiencesForAdmin(Integer current, Integer size) {
        log.info("开始分页查询工作经验信息，页码：{}，每页数量：{}", current, size);
        
        // 创建分页对象
        Page<UserWorkExperienceExtension> experiencePage = new Page<>(current, size);
        
        // 创建查询条件
        LambdaQueryWrapper<UserWorkExperienceExtension> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(UserWorkExperienceExtension::getId);
        
        // 执行分页查询
        this.page(experiencePage, queryWrapper);
        
        // 创建返回结果页对象
        Page<UserWorkExperienceVO> resultPage = new Page<>(current, size);
        resultPage.setTotal(experiencePage.getTotal());
        resultPage.setCurrent(experiencePage.getCurrent());
        resultPage.setSize(experiencePage.getSize());
        resultPage.setPages(experiencePage.getPages());
        
        // 转换查询结果为VO对象
        if (!CollectionUtils.isEmpty(experiencePage.getRecords())) {
            List<UserWorkExperienceVO> records = experiencePage.getRecords().stream()
                    .map(exp -> {
                        UserWorkExperienceVO vo = new UserWorkExperienceVO();
                        BeanUtils.copyProperties(exp, vo);
                        return vo;
                    })
                    .collect(Collectors.toList());
            
            resultPage.setRecords(records);
            log.info("分页查询工作经验信息成功，共查询到 {} 条记录", records.size());
        } else {
            resultPage.setRecords(Collections.emptyList());
            log.info("分页查询工作经验信息成功，但未查询到记录");
        }
        
        return resultPage;
    }

}
