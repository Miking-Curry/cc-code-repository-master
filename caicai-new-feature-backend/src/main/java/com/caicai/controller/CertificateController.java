package com.caicai.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caicai.entity.vo.CertificateInfoVO;
import com.caicai.result.R;
import com.caicai.service.CertificateService;
import com.caicai.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/certificates")
public class CertificateController {
    private final CertificateService certificateService;
    private final FileService fileService;
    /**
     * 上传证书信息
     *
     * @param userId
     * @param certificateName
     * @param certificateImage
     * @return
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<Boolean> uploadCertificateInfo(
            @RequestAttribute("userId") Long userId,
            @RequestParam("certificateName") String certificateName,
            @RequestParam("certificateImage") MultipartFile certificateImage) {
        try {
            if (userId == null) {
                log.warn("上传证书信息失败: 用户ID为空");
                return R.error("用户ID不能为空");
            }
            if (!StringUtils.hasText(certificateName)) {
                log.warn("上传证书信息失败: 证书名称为空", userId);
                return R.error("证书名称不能为空");
            }
            if (certificateImage == null || certificateImage.isEmpty()) {
                log.warn("上传证书信息失败: 证书图片为空", userId);
                return R.error("证书图片不能为空");
            }
            //校验文件类型
            String originalFilename = certificateImage.getOriginalFilename();
            log.info("用户{}上传的证书文件名: {}", userId, originalFilename);

            if (originalFilename == null || !(originalFilename.endsWith(".jpg")
                    || originalFilename.endsWith(".jpeg")
                    || originalFilename.endsWith(".png"))) {
                log.warn("用户{}上传证书信息失败: 图片格式不正确", userId);
                return R.error("上传的图片格式不正确，请上传jpg、jpeg或png格式的图片");
            }
            //校验文件大小（最大5MB）
            if (certificateImage.getSize() > 5 * 1024 * 1024) {
                log.warn("用户{}上传证书信息失败: 图片大小超过5MB", userId);
            }
            log.info("用户{}的证书图片校验通过，准备上传", userId);
            String certificateUrl = fileService.uploadImageFile(certificateImage, "certificate");
            log.info("用户{}的证书图片上传成功，URL: {}", userId, certificateUrl);

            //调用服务层保存证书信息
            return R.success(certificateService.saveCertificateInfo(userId, certificateName, certificateUrl));
        } catch (Exception e) {
            log.error("用户{}上传证书信息失败", userId, e);
            return R.error("上传证书信息失败: " + e.getMessage());
        }
    }
    /**
     * 根据ID直接删除证书记录
     *
     * @param certificateId 证书ID
     * @return 操作结果
     */
    @DeleteMapping(value = "/{certificateId}")
    public R<Boolean> deleteCertificate(@PathVariable("certificateId") Long certificateId,
                                        @RequestAttribute("userId") Long userId) {
        try {
            // 校验 certificateId 是否有效
            if (certificateId == null || certificateId <= 0) {
                log.warn("删除证书失败: 无效的证书ID: {}", certificateId);
                return R.error("无效的证书ID");
            }
            log.info("请求删除证书记录，ID: {}", certificateId);
            // 调用服务层直接删除证书记录
            return R.success(certificateService.deleteCertificateById(certificateId, userId));
        } catch (Exception e) {
            log.error("删除证书记录 ID: {} 失败", certificateId, e);
            return R.error("删除证书失败: " + e.getMessage());
        }
    }
    /**
     * 修改证书信息（名称和图片）
     *
     * @param certificateId    证书ID
     * @param certificateName  新的证书名称
     * @param certificateImage 新的证书图片文件
     * @return 操作结果
     */
    @PutMapping(value = "/update/{certificateId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<Boolean> updateCertificateInfo(
            @PathVariable("certificateId") Long certificateId,
            @RequestParam(value = "certificateName") String certificateName,
            @RequestParam(value = "certificateImage") MultipartFile certificateImage) {
        try {
            // 校验 certificateId
            if (certificateId == null || certificateId <= 0) {
                log.warn("修改证书失败: 无效的证书ID: {}", certificateId);
                return R.error("无效的证书ID");
            }

            // 校验证书名称
            if (!StringUtils.hasText(certificateName)) {
                log.warn("修改证书 ID: {} 失败: 证书名称为空", certificateId);
                return R.error("证书名称不能为空");
            }

            String newCertificateImageUrl = null;
            // 如果上传了新的证书图片，则进行处理
            if (certificateImage != null && !certificateImage.isEmpty()) {
                // 校验文件类型
                String originalFilename = certificateImage.getOriginalFilename();
                if (originalFilename == null || !(originalFilename.endsWith(".jpg")
                        || originalFilename.endsWith(".jpeg")
                        || originalFilename.endsWith(".png"))) {
                    log.warn("修改证书 ID: {} 失败: 图片格式不正确, 文件名: {}", certificateId, originalFilename);
                    return R.error("上传的图片格式不正确，请上传jpg、jpeg或png格式的图片");
                }
                // 校验文件大小（最大5MB）
                if (certificateImage.getSize() > 5 * 1024 * 1024) {
                    log.warn("修改证书 ID: {} 失败: 图片大小超过5MB, 大小: {} bytes", certificateId, certificateImage.getSize());
                    return R.error("上传的图片不能超过5MB");
                }
                // 上传新的图片文件
                newCertificateImageUrl = fileService.uploadImageFile(certificateImage, "certificate");
                log.info("证书 ID: {} 的新图片上传成功，URL: {}", certificateId, newCertificateImageUrl);
            } else {
                log.info("用户修改证书 ID: {} 时未提供新的证书图片，将仅更新名称或保留旧图片 (如果服务层逻辑支持)", certificateId);
            }

            log.info("请求修改证书信息，ID: {}, 新名称: {}, 新图片URL (如果提供): {}", certificateId, certificateName, newCertificateImageUrl);

            // 调用服务层更新证书信息

            return R.success(certificateService.updateCertificateInfo(certificateId, certificateName, newCertificateImageUrl));
        } catch (Exception e) {
            log.error("修改证书 ID: {} 信息失败", certificateId, e);
            return R.error("修改证书信息失败: " + e.getMessage());
        }
    }
    /**
     * 修改证书审核状态
     * @param certificateId 证书ID
     * @param checkStatus 审核状态
     */
    @PutMapping(value = "/{certificateId}/{checkStatus}")
    public R<Boolean> updateCertificateCheckStatus(@PathVariable("certificateId") Long certificateId,
                                                   @PathVariable("checkStatus") Integer checkStatus) {
        //校验数据
        if (certificateId == null || certificateId <= 0) {
            log.warn("修改证书失败: 无效的证书ID: {}", certificateId);
            return R.error("无效的证书ID");
        }
        //校验数据
        if (checkStatus == null || checkStatus < 0 || checkStatus > 2) {
            log.warn("修改证书失败: 无效的审核状态: {}", checkStatus);
        }
        return R.success(certificateService.updateCertificateStatus(certificateId, checkStatus));}
    /**
     * 根据用户ID查询该用户的所有技能证书列表（证书名称和图片）。
     * @param userId 用户ID
     * @return 包含证书信息列表的R对象，数据体为List<CertificateInfoVO>。
     */
    @GetMapping
    public R<List<CertificateInfoVO>> getCertificatesByUserId(@RequestAttribute("userId") Long userId) {
        log.info("请求查询用户ID: {} 的所有技能证书列表。", userId);
        if (userId == null) {
            log.warn("查询用户技能证书列表失败：路径中的用户ID为空。");
            return R.error("用户ID不能为空");
        }
        return R.success(certificateService.getCertificateInfoListByUserId(userId));
    }
    /**
     * 管理员分页查询所有证书信息
     * 
     * @param current 当前页码
     * @param size 每页数据量
     * @return 包含证书信息的分页数据
     */
    @GetMapping("/page")
    public R<Page<CertificateInfoVO>> listCertificatesForAdmin(
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("管理员请求分页查询证书信息，页码：{}，每页数量：{}", current, size);
        
        try {
            // 调用服务层获取分页数据
            Page<CertificateInfoVO> page = certificateService.pageCertificatesForAdmin(current, size);
            return R.success(page);
        } catch (Exception e) {
            log.error("管理员分页查询证书信息失败", e);
            return R.error("查询证书信息失败: " + e.getMessage());
        }
    }
}
