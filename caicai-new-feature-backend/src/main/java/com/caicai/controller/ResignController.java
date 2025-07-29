package com.caicai.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caicai.entity.vo.ResignVO;
import com.caicai.result.R;
import com.caicai.service.ResignService;
import com.caicai.util.RegxUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/resignations")
public class ResignController {
    private final ResignService resignService;
    /**
     * 获取离职信息
     * @param userId 用户ID
     * @return 离职信息
     */
    @GetMapping
    public R<ResignVO> getResignInfo(@RequestAttribute("userId") Long userId) {
        //校验id
        if (userId == null) {
            log.warn("用户 {} 获取离职信息失败: 用户ID为空", userId);
            return R.error("用户ID不能为空");
        }
        return R.success(resignService.getResignInfo(userId));
    }
    /**
     * 修改离职信息
     * @param userId 用户ID
     * @param resignReason 离职原因
     * @param resignAt 离职时间
     * @param joinAt 入职时间
     * @param witnessName  证明人名字
     * @param witnessPhone  证明人电话
     * @return 操作结果
     */
    @PutMapping
    public R<Boolean> updateResignInfo(@RequestAttribute("userId") Long userId,
                                       @RequestParam("resignReason") String resignReason,
                                       @RequestParam("resignAt") String resignAt,
                                       @RequestParam("joinAt") String joinAt,
                                       @RequestParam("witnessName") String witnessName,
                                       @RequestParam("witnessPhone") String witnessPhone)
    {
        //校验id
        if (userId == null) {
            log.warn("用户 {} 修改离职信息失败: 用户ID为空", userId);
            return R.error("用户ID不能为空");
        }
        //校验证明人名字
        if (!StringUtils.hasText(witnessName)) {
            log.warn("用户 {} 添加离职信息失败: 证明人名字为空", userId);
        }
        //校验证明人电话
        if(!StringUtils.hasText(witnessPhone) || RegxUtil.checkPhone(witnessPhone) == false){
            log.warn("用户 {} 添加离职信息失败: 验证手机号失败", userId);
            return R.error("手机号格式错误");
        }
        return R.success(resignService.updateResignInfo(userId, resignReason, resignAt, joinAt, witnessName, witnessPhone));
    }
    /**
     * 保存离职信息
     * @param userId 用户ID
     * @param resignReason 离职原因
     * @param resignAt 离职时间
     * @return 操作结果
     */
    @PostMapping
    public R<Boolean> saveResignInfo(@RequestAttribute("userId") Long userId,
                                     @RequestParam("resignReason") String resignReason,
                                     @RequestParam("resignAt")String resignAt,
                                     @RequestParam("joinAt") String joinAt,
                                     @RequestParam("witnessName") String witnessName,
                                     @RequestParam("witnessPhone") String witnessPhone)
    {
        //校验id
        if (userId == null) {
            log.warn("用户 {} 添加离职信息失败: 用户ID为空", userId);
            return R.error("用户ID不能为空");
        }
        //校验离职原因
        if (!StringUtils.hasText(resignReason)) {
            log.warn("用户 {} 添加离职信息失败: 离职原因为空", userId);
            return R.error("离职原因不能为空");
        }
        //校验离职时间
        if (resignAt == null) {
            log.warn("用户 {} 添加离职信息失败: 离职时间为空", userId);
        }
        //校验工作时间
        if (joinAt == null)
        {
            log.warn("用户 {} 添加离职信息失败: 入职时间为空", userId);
        }
        //校验证明人名字
        if (!StringUtils.hasText(witnessName)) {
            log.warn("用户 {} 添加离职信息失败: 证明人名字为空", userId);
        }
        //校验证明人电话
        if(!StringUtils.hasText(witnessPhone) || RegxUtil.checkPhone(witnessPhone) == false)
        {
            log.warn("用户 {} 添加离职信息失败: 验证手机号失败", userId);
        }
        return R.success(resignService.saveResignInfo(userId, resignReason,resignAt,joinAt,witnessName,witnessPhone));
    }
    /**
     * 删除离职信息
     * @param userId 用户ID
     * @return 操作结果
     */
    @DeleteMapping
    public R<Boolean> removeResignInfo(@RequestAttribute("userId") Long userId) {
        //校验id
        if (userId == null) {
            log.warn("用户 {} 删除离职信息失败: 用户ID为空", userId);
            return R.error("用户ID不能为空");
        }
        return R.success(resignService.removeResignInfo(userId));
    }
    /**
     * 更新离职信息审核状态
     * @param resignId 离职ID
     * @param checkStatus 审核状态
     */
    @PutMapping("/{resignId}/{checkStatus}")
    public R<Boolean> updateResignStatus(@PathVariable("resignId") Long resignId,
                                         @PathVariable("checkStatus") Integer checkStatus)
    {
        //校验id
        if (resignId == null) {
            log.warn("用户 {} 更新离职信息审核状态失败: 离职ID为空", resignId);
            return R.error("离职ID不能为空");
        }
        //校验状态
        if (checkStatus == null) {
            log.warn("用户 {} 添加离职信息失败: 状态为空", resignId);
            return R.error("状态不能为空");
        }
        return R.success(resignService.updateResignStatus(resignId, checkStatus));
    }
    /**
     * 管理员分页查询所有离职信息
     * 
     * @param current 当前页码
     * @param size 每页数据量
     * @return 包含离职信息的分页数据
     */
    @GetMapping("/page")
    public R<Page<ResignVO>> listResignsForAdmin(
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("管理员请求分页查询离职信息，页码：{}，每页数量：{}", current, size);
        
        try {
            // 调用服务层获取分页数据
            Page<ResignVO> page = resignService.pageResignsForAdmin(current, size);
            return R.success(page);
        } catch (Exception e) {
            log.error("管理员分页查询离职信息失败", e);
            return R.error("查询离职信息失败: " + e.getMessage());
        }
    }
}
