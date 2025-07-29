package com.caicai.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caicai.entity.vo.UserWorkExperienceVO;
import com.caicai.result.R;
import com.caicai.service.WorkExperienceService;
import com.caicai.service.FileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/work-experiences")
public class WorkExperienceController {
    private final WorkExperienceService workExperienceService;
    private final FileService fileService;
    /**
     * 保存工作经验
     * @return 操作结果
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<Boolean> uploadWorkExperienceInfo(@RequestAttribute("userId") Long userId,
                                               @RequestParam("workYears") String workYears,
                                               @RequestParam("socialSecurityImage") MultipartFile SocialSecurityImage)
    {
        try {
            //校验参数
            //校验用户id
            if (userId == null) {
                log.warn("用户 {} 保存工作经验失败: 用户ID为空", userId);
                return R.error("用户ID不能为空");
            }
            //校验工作年限
            if (!StringUtils.hasText(workYears)) {
                log.warn("用户 {} 添加工作经验失败: 工作年限为空", userId);
            }
            // 校验文件类型
            String originalFilename = SocialSecurityImage.getOriginalFilename();
            log.info("用户 {} 上传的社保证件文件名: {}", userId, originalFilename);

            if (originalFilename == null || !(originalFilename.endsWith(".jpg")
                    || originalFilename.endsWith(".jpeg")
                    || originalFilename.endsWith(".png"))) {
                log.warn("用户 {} 保存工作经历失败: 图片格式不正确 ({})", userId, originalFilename);
                return R.error("上传的图片格式不正确，请上传jpg、jpeg或png格式的图片");
            }

            // 校验文件大小（最大5MB）
            if (SocialSecurityImage.getSize() > 5 * 1024 * 1024) {
                log.warn("用户 {} 保存工作经历失败: 图片大小超过5MB ({} bytes)", userId, SocialSecurityImage.getSize());
                return R.error("上传的图片不能超过5MB");
            }

            log.info("用户 {} 的社保图片校验通过，准备上传", userId);
            // 上传图片文件到文件服务器
            String socialSecurityUrl = fileService.uploadImageFile(SocialSecurityImage, "SocialSecurity");
            log.info("用户 {} 的社保图片上传成功，URL: {}", userId, socialSecurityUrl);

            // 调用服务层保存工作经历

            return R.success(workExperienceService.saveWorkExperienceInfo(userId, workYears ,socialSecurityUrl));
        }
        catch (Exception e) {
            log.error("用户 {} 保存工作经验失败: {}", userId, e.getMessage(), e);
            return R.error();
        }
    }
    /**
     * 获取工作经验信息
     * @param userId 用户ID
     * @return 工作经验信息
     */
    @GetMapping
    public R<UserWorkExperienceVO> getWorkExperienceInfo(@RequestAttribute("userId") Long userId) {
        // 校验用户ID
        if (userId == null) {
            log.warn("获取用户 {} 的工作经验信息失败: 用户ID为空", userId);
            return R.error("用户ID不能为空");
        }
        log.info("用户 {} 请求获取工作经历信息", userId);
        return R.success(workExperienceService.getWorkExperienceInfo(userId));
    }
    /**
     * 删除工作经验信息
     * @param userId 用户id
     * @return 操作结果
     */
    @DeleteMapping
    public R<Boolean> removeExperienceInfo(@RequestAttribute("userId") Long userId) {
        //校验工作id
        if (userId == null) {
            log.warn("用户 {} 删除工作经历信息失败: 工作经历ID为空", userId);
            return R.error("工作经历ID不能为空");
        }
        //调用服务层完成逻辑
        return R.success(workExperienceService.removeExperienceInfo(userId));
    }

    /**
     * 修改工作经验信息
     * @param userId 用户ID
     * @param workYears 工作年限
     * @param socialSecurityImage 社保图片
     * @return 操作结果
     */
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<Boolean> updateExperienceInfo(
            @RequestAttribute("userId") Long userId,
            @RequestParam(value = "workYears") String workYears,
            @RequestParam(value = "socialSecurityImage") MultipartFile socialSecurityImage)
    {
        try {
            //校验用户id
            if (userId == null) {
                log.warn("修改工作经历信息失败: 用户ID为空");
                return R.error("用户ID不能为空");
            }
            // 校验文件类型
            String originalFilename = socialSecurityImage.getOriginalFilename();
            log.info("用户ID {} 上传的社保证件文件名: {}", userId, originalFilename);
            if (originalFilename == null || !(originalFilename.endsWith(".jpg")
                    || originalFilename.endsWith(".jpeg")
                    || originalFilename.endsWith(".png"))) {
                log.warn("用户ID {} 修改工作经验失败: 图片格式不正确 ({})", userId, originalFilename);
                return R.error("上传的图片格式不正确，请上传jpg、jpeg或png格式的图片");
            }
            // 校验文件大小（最大5MB）
            if (socialSecurityImage.getSize() > 5 * 1024 * 1024) {
                log.warn("用户ID {} 修改工作经验失败: 图片大小超过5MB ({} bytes)", userId, socialSecurityImage.getSize());
                return R.error("上传的图片不能超过5MB");
            }
            log.info("用户ID {} 的社保图片校验通过，准备上传", userId);
            // 假设workExperienceService.updateExperienceInfoByUserId方法已经存在或将被创建
            return R.success(workExperienceService.updateExperienceInfo(userId, workYears,
                    fileService.uploadImageFile(socialSecurityImage, "SocialSecurity")));
        }
        catch (Exception e) {
            log.error("用户ID {} 修改工作经历信息失败: {}", userId, e.getMessage(), e);
            return R.error();
        }
    }
    /**
     * 修改工作经验审核状态
     * @param workExperienceId 工作经历ID
     * @param checkStatus 状态
     * @return 操作结果
     */
    @PutMapping("/{workExperienceId}/{checkStatus}")
    public R<Boolean> updateExperienceStatus(@PathVariable("workExperienceId") Long workExperienceId,
                                             @PathVariable("checkStatus") Integer checkStatus) {
        //校验id
        if (workExperienceId == null) {
            log.warn("用户 {} 修改工作经历状态失败: 工作经历ID为空", workExperienceId);
            return R.error("工作经历ID不能为空");
        }
        //校验状态
        if (checkStatus == null) {
            log.warn("用户 {} 修改工作经历状态失败: 状态为空", workExperienceId);
            return R.error("状态不能为空");
        }
        //调用服务层
        return R.success(workExperienceService.updateExperienceStatus(workExperienceId, checkStatus));
    }

    /**
     * 管理员分页查询所有工作经验信息
     * 
     * @param current 当前页码
     * @param size 每页数据量
     * @return 包含工作经验信息的分页数据
     */
    @GetMapping("/page")
    public R<Page<UserWorkExperienceVO>> listWorkExperiencesForAdmin(
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("管理员请求分页查询工作经验信息，页码：{}，每页数量：{}", current, size);
        
        try {
            // 调用服务层获取分页数据
            Page<UserWorkExperienceVO> page = workExperienceService.pageWorkExperiencesForAdmin(current, size);
            return R.success(page);
        } catch (Exception e) {
            log.error("管理员分页查询工作经验信息失败", e);
            return R.error("查询工作经验信息失败: " + e.getMessage());
        }
    }
}
