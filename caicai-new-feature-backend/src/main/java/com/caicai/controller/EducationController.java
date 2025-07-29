package com.caicai.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caicai.entity.vo.EducationInfoVO;
import com.caicai.result.R;
import com.caicai.service.EducationService;
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
@RequestMapping("/educations")
public class EducationController {
    private final EducationService educationService;
    private final FileService fileService;
    /**
     * 上传/更新教育经历和毕业证书
     * @param userId 用户ID
     * @param schoolName 毕业学校名称
     * @param certificateImage 毕业证书图片
     * @return 上传结果
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<Boolean> saveEducationInfo(
            @RequestAttribute("userId") Long userId,
            @RequestParam("schoolName") String schoolName,
            @RequestParam("certificateImage") MultipartFile certificateImage) {

        try {
            // 校验参数
            if (userId == null) {
                log.warn("保存教育经历失败: 用户ID为空");
                return R.error("用户ID不能为空");
            }

            if (!StringUtils.hasText(schoolName)) {
                log.warn("用户 {} 保存教育经历失败: 学校名称为空", userId);
                return R.error("学校名称不能为空");
            }

            if (certificateImage == null || certificateImage.isEmpty()) {
                log.warn("用户 {} 保存教育经历失败: 毕业证书图片为空", userId);
                return R.error("毕业证书图片不能为空");
            }

            // 校验文件类型
            String originalFilename = certificateImage.getOriginalFilename();
            log.info("用户 {} 上传的毕业证书文件名: {}", userId, originalFilename);

            if (originalFilename == null || !(originalFilename.endsWith(".jpg")
                    || originalFilename.endsWith(".jpeg")
                    || originalFilename.endsWith(".png"))) {
                log.warn("用户 {} 保存教育经历失败: 图片格式不正确 ({})", userId, originalFilename);
                return R.error("上传的图片格式不正确，请上传jpg、jpeg或png格式的图片");
            }

            // 校验文件大小（最大5MB）
            if (certificateImage.getSize() > 5 * 1024 * 1024) {
                log.warn("用户 {} 保存教育经历失败: 图片大小超过5MB ({} bytes)", userId, certificateImage.getSize());
                return R.error("上传的图片不能超过5MB");
            }

            log.info("用户 {} 的毕业证书图片校验通过，准备上传", userId);
            // 上传图片文件到文件服务器
            String certificateUrl = fileService.uploadImageFile(certificateImage, "certificate");
            log.info("用户 {} 的毕业证书图片上传成功，URL: {}", userId, certificateUrl);

            // 调用服务层保存教育经历和毕业证书信息


            return R.success(educationService.saveEducationInfo(userId, schoolName, certificateUrl));
        } catch (Exception e) {
            log.error("用户 {} 保存教育经历和毕业证书失败", userId, e);
            return R.error("保存教育经历和毕业证书失败: " + e.getMessage());
        }
    }
    /**
     * 根据用户ID查询教育信息（学校名称和毕业证书图片）。
     * @param userId 用户ID
     * @return 包含教育信息的R对象，数据体为EducationInfoVO。
     */
    @GetMapping
    public R<EducationInfoVO> getEducationInfoByUserId(@RequestAttribute("userId") Long userId) {
        log.info("请求查询用户ID: {} 的教育信息。", userId);
        if (userId == null) {
            log.warn("查询教育信息请求失败：路径中的用户ID为空。");
            return R.error("用户ID不能为空");
        }
        return R.success(educationService.getEducationInfo(userId));
    }
    /**
     * 删除教育经历信息（将学校名称和毕业证书图片置为空）
     * @param userId 用户ID
     * @return 操作结果
     */
    @DeleteMapping
    public R<Boolean> deleteEducationInfo(@RequestAttribute("userId") Long userId) {
        try {
            // 校验用户ID
            if (userId == null) {
                log.warn("删除教育经历信息失败: 用户ID为空");
                return R.error("用户ID不能为空");
            }

            log.info("用户 {} 请求删除教育经历信息（置空处理）", userId);

            // 调用服务层将学校名称和毕业证书图片URL置为空


            return R.success(educationService.clearEducationInfo(userId));
        } catch (Exception e) {
            log.error("用户 {} 删除教育经历信息（置空处理）失败", userId, e);
            return R.error("删除教育经历信息失败: " + e.getMessage());
        }
    }
    /**
     * 修改教育经历审核状态
     * @param userEducationExperienceExtensionId
     * @param checkStatus
     * @return 操作结果
     */
    @PutMapping("/{userEducationExperienceExtensionId}/{checkStatus}")
    public R<Boolean> updateEducationStatus(@PathVariable("userEducationExperienceExtensionId") Long userEducationExperienceExtensionId,
                                            @PathVariable("checkStatus") Integer checkStatus)
    {
        //  校验参数
        if (userEducationExperienceExtensionId == null || checkStatus == null) {
            log.warn("修改教育经历审核状态失败: 参数错误");
            return R.error("参数错误");
        }
        return R.success(educationService.updateEducationStatus(userEducationExperienceExtensionId, checkStatus));
    }
    /**
     * 管理员分页查询所有教育信息
     * 
     * @param current 当前页码
     * @param size 每页数据量
     * @return 包含教育信息的分页数据
     */
    @GetMapping("/page")
    public R<Page<EducationInfoVO>> listEducationsForAdmin(
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("管理员请求分页查询教育信息，页码：{}，每页数量：{}", current, size);
        
        try {
            // 调用服务层获取分页数据
            Page<EducationInfoVO> page = educationService.pageEducationsForAdmin(current, size);
            return R.success(page);
        } catch (Exception e) {
            log.error("管理员分页查询教育信息失败", e);
            return R.error("查询教育信息失败: " + e.getMessage());
        }
    }
}
