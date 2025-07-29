package com.caicai.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.caicai.entity.pojo.UserEducationExperience;
import com.caicai.entity.vo.EducationInfoVO;

/**
 * 教育经历服务接口
 * @author YangFuyi
 * @create 2025/5/22
 */
public interface EducationService extends IService<UserEducationExperience> {

    /**
     * 保存或更新用户的教育经历（学校名称）和毕业证书图片。
     * 
     * @param userId 用户的唯一标识ID。
     * @param schoolName 学校的名称。
     * @param certificatePicUrl 毕业证书图片的URL。
     * @return 返回ture或false。
     */
    boolean saveEducationInfo(Long userId, String schoolName, String certificatePicUrl);

    /**
     * 根据用户ID查询用户的教育信息（学校名称和毕业证书图片）。
     * 
     * @param userId 用户的唯一标识ID。
     * @return 返回包含教育信息的VO对象，如果找不到返回null。
     */
    EducationInfoVO getEducationInfo(Long userId);
    
    /**
     * 清除用户的教育经历信息（将学校名称和毕业证书图片置为空）。
     * 
     * @param userId 用户的唯一标识ID。
     * @return 返回true或false。
     */
    boolean clearEducationInfo(Long userId);
    /**
     * 修改教育信息的审核状态
     * @param userEducationExperienceExtensionId
     * @param checkStatus
     * @return true或者false
     */
    boolean updateEducationStatus(Long userEducationExperienceExtensionId, Integer checkStatus);

    /**
     * 管理员分页查询所有教育信息
     * 
     * @param current 当前页码
     * @param size 每页数据量
     * @return 包含教育信息的分页数据
     */
    Page<EducationInfoVO> pageEducationsForAdmin(Integer current, Integer size);
}
