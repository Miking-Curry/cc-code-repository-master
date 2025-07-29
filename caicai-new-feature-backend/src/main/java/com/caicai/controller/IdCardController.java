package com.caicai.controller;
import  com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caicai.entity.vo.IdCardInfoVO;
import com.caicai.result.R;
import com.caicai.service.IdCardService;
import com.caicai.service.FileService;
import com.caicai.service.TaskService;
import com.caicai.util.IdCardValidator;
import com.caicai.util.RegxUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/idcards")
public class IdCardController {
    private final IdCardService idCardService;
    private final FileService fileService;
    private final TaskService taskService;
    /**
     * 上传身份证信息
     *
     * @param userId      用户ID
     * @param idCardNum   身份证号码
     * @param idCardImage 身份证图片
     * @return 上传结果
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<Boolean> uploadIdCardInfo(
            @RequestAttribute("userId") Long userId,
            @RequestParam("idCardNum") String idCardNum,
            @RequestParam("idCardImage") MultipartFile idCardImage,
            @RequestParam("name") String name) {

        try {
            // 校验身份证号码
            IdCardValidator.ValidationResult validationResult = IdCardValidator.validate(idCardNum);
            if (!validationResult.isValid()) {
                return R.error("身份证号码无效: " + validationResult.getMessage());
            }

            // 校验文件类型
            String originalFilename = idCardImage.getOriginalFilename();
            if (originalFilename == null || !(originalFilename.endsWith(".jpg")
                    || originalFilename.endsWith(".jpeg")
                    || originalFilename.endsWith(".png"))) {
                return R.error("上传的图片格式不正确，请上传jpg、jpeg或png格式的图片");
            }
            //校验名字
            if (!RegxUtil.checkName(name)) {
                return R.error("名字格式不正确");
            }
            // 校验文件大小（最大5MB）
            if (idCardImage.getSize() > 5 * 1024 * 1024) {
                return R.error("上传的图片不能超过5MB");
            }

            // 上传图片文件到文件服务器
            String idCardUrl = fileService.uploadImageFile(idCardImage, "idcard");
            log.info("用户ID: {} 的身份证图片上传成功，URL: {}", userId, idCardUrl);

            // 调用任务接口
            taskService.handleTask(userId, 2L);

            // 调用服务层保存身份证信息
            return R.success(idCardService.uploadIdCardInfo(userId, idCardNum, idCardUrl, name));
        } catch (Exception e) {
            log.error("上传身份证信息失败", e);
            return R.error("上传身份证信息失败: " + e.getMessage());
        }
    }
    /**
     * 删除身份证信息（将身份证号码和图片,姓名置为空）
     *
     * @param userId 用户ID
     * @return 操作结果
     */
    @DeleteMapping
    public R<Boolean> deleteIdCardInfo(@RequestAttribute("userId") Long userId) {
        try {
            // 校验用户ID
            if (userId == null) {
                log.warn("删除身份证信息失败: 用户ID为空");
                return R.error("用户ID不能为空");
            }
            log.info("用户 {} 请求删除身份证信息（置空处理）", userId);
            // 调用服务层将身份证号码和图片URL置为空
            return R.success(idCardService.clearIdCardInfo(userId));
        } catch (Exception e) {
            log.error("用户 {} 删除身份证信息（置空处理）失败", userId, e);
            return R.error("删除身份证信息失败: " + e.getMessage());
        }
    }
    /**
     * 修改身份证审核状态
     *
     * @param userExtensionId 用户扩展信息ID
     * @param checkStatus     审核状态
     */
    @PutMapping(value = "/{userExtensionId}/{checkStatus}")
    public R<Boolean> updateIdCardCheckStatus(@PathVariable("userExtensionId") Long userExtensionId,
                                              @PathVariable("checkStatus") Integer checkStatus)
    {
        //校验数据
        if (userExtensionId == null || userExtensionId <= 0) {
            log.warn("修改身份证信息失败: 无效的userExtensionId: {}", userExtensionId);
        }
        if (checkStatus == null || checkStatus < 0 || checkStatus > 2) {
            log.warn("修改身份证信息失败: 无效的审核状态: {}", checkStatus);
        }
        return R.success(idCardService.updateIdCardStatus(userExtensionId, checkStatus));
    }
    /**
     * 根据用户ID查询身份证信息（身份证号码和图片URL,身份证名字）。
     * @param userId 用户ID
     * @return 包含身份证信息的R对象，数据体为IdCardInfoVO。
     */
    @GetMapping
    public R<IdCardInfoVO> getIdCardInfoByUserId(@RequestAttribute("userId") Long userId) {
        log.info("请求查询用户ID: {} 的身份证信息。", userId);
        if (userId == null) {
            log.warn("查询身份证信息请求失败：路径中的用户ID为空。");
            return R.error("用户ID不能为空");
        }
        return R.success(idCardService.getIdCardInfo(userId));
    }
    /**
     * 管理员分页查询所有身份证信息
     * 
     * @param current 当前页码
     * @param size 每页数据量
     * @return 包含身份证信息的分页数据
     */
    @GetMapping("/page")
    public R<Page<IdCardInfoVO>> listIdCardsForAdmin(
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("管理员请求分页查询身份证信息，页码：{}，每页数量：{}", current, size);
        
        try {
            // 调用服务层获取分页数据
            Page<IdCardInfoVO> page = idCardService.pageIdCardsForAdmin(current, size);
            return R.success(page);
        } catch (Exception e) {
            log.error("管理员分页查询身份证信息失败", e);
            return R.error("查询身份证信息失败: " + e.getMessage());
        }
    }
}
