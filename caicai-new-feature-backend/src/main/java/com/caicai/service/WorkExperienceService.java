package com.caicai.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.caicai.entity.pojo.UserWorkExperienceExtension;
import com.caicai.entity.vo.UserWorkExperienceVO;
public interface WorkExperienceService extends IService<UserWorkExperienceExtension> {
    /**
     * 保存工作经历信息
     * @param userId
     * @param workYears
     * @param socialSecurityRecordPicUrl
     * @return
     */
    boolean saveWorkExperienceInfo(Long userId, String workYears, String socialSecurityRecordPicUrl);
    /**
     * 获取工作经历信息
     * @param userId
     * @return
     */
    UserWorkExperienceVO getWorkExperienceInfo(Long userId);

    /**
     * 删除工作经历信息
     * @param userId
     * @return
     */
    boolean removeExperienceInfo(Long userId);
    /**
     * 更新工作经历信息
     * @param userId
     * @param workYears
     * @param socialSecurityRecordPicUrl
     * @return
     */
    boolean updateExperienceInfo(Long userId,String workYears, String socialSecurityRecordPicUrl);
    /**
     * 更新工作经历状态
     * @param workExperienceId
     * @param checkStatus
     * @return
     */
    boolean updateExperienceStatus(Long workExperienceId, Integer checkStatus);

    /**
     * 管理员分页查询所有工作经验信息
     * 
     * @param current 当前页码
     * @param size 每页数据量
     * @return 包含工作经验信息的分页数据
     */
    Page<UserWorkExperienceVO> pageWorkExperiencesForAdmin(Integer current, Integer size);
}
