package com.caicai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caicai.entity.pojo.UserExtension;
import com.caicai.entity.vo.IdCardInfoVO;
import com.caicai.mapper.UserExtensionMapper;
import com.caicai.service.FileService;
import com.caicai.service.IdCardService;
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
public class IdCardServiceImpl extends ServiceImpl<UserExtensionMapper, UserExtension> implements IdCardService {
    private final FileService fileService;
    /**
     * 上传用户身份证信息
     * @param userId
     * @param idCardNum
     * @param idCardPic
     * @param name
     * @return
     */
    @Override
    @Transactional
    public boolean uploadIdCardInfo(Long userId, String idCardNum, String idCardPic, String name) {
        LambdaQueryWrapper<UserExtension> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserExtension::getUserId, userId);
        UserExtension userExtension = this.getOne(queryWrapper);

        // 检查是否需要删除旧图片
        if(userExtension != null && StringUtils.hasText(userExtension.getIdCardPic()) 
                && !userExtension.getIdCardPic().equals(idCardPic)) {
            log.info("用户ID: {} 更新身份证图片，删除旧图片: {}", userId, userExtension.getIdCardPic());
            fileService.deleteFile(userExtension.getIdCardPic());
        }

        if(userExtension == null) {
            log.info("未找到用户ID: {} 的扩展信息，将创建新记录", userId);
            userExtension = new UserExtension();
            userExtension.setUserId(userId);
            userExtension.setPoint(0L);
            userExtension.setCreditScore(0);
            userExtension.setIsVipUser(0);

        }

        userExtension.setIdCardNum(idCardNum);
        userExtension.setIdCardPic(idCardPic);
        userExtension.setName(name);

        boolean result = this.saveOrUpdate(userExtension);
        if(result) {
            log.info("用户ID: {} 的身份证信息上传成功", userId);
        } else {
            log.error("用户ID: {} 的身份证信息上传失败", userId);
        }
        return result;
    }

    @Override
    @Transactional
/**
 * 清除用户ID对应的身份证信息
 * @param userId
 * @return
 */
    public boolean clearIdCardInfo(Long userId) {
        // 先查询用户扩展信息是否存在
        LambdaQueryWrapper<UserExtension> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserExtension::getUserId, userId);
        UserExtension userExtension = this.getOne(queryWrapper);

        if (userExtension == null) {
            log.warn("尝试清除不存在的用户ID {} 的身份证信息", userId);
            return false;
        }

        log.info("准备清除用户ID {} 的身份证信息。当前身份证号: {}, 当前图片URL: {}",
                userId, userExtension.getIdCardNum(), userExtension.getIdCardPic());
        // 使用LambdaUpdateWrapper明确指定要将这些字段设置为null
        // 调用fileService删除图片
        fileService.deleteFile(userExtension.getIdCardPic());
        LambdaUpdateWrapper<UserExtension> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserExtension::getUserId, userId)
                .set(UserExtension::getIdCardNum, null)
                .set(UserExtension::getIdCardPic, null)
                .set(UserExtension::getName, null);

        boolean updateResult = this.update(updateWrapper);

        if (updateResult) {
            log.info("用户ID {} 的身份证信息清除成功", userId);
            return true;
        } else {
            log.error("用户ID {} 的身份证信息清除失败，数据库操作未成功", userId);
            return false;
        }
    }
    /**
     * 获取用户ID对应的身份证信息
     * @param userId
     * @return
     */
    @Override
    public IdCardInfoVO getIdCardInfo(Long userId) {
        log.info("开始为用户ID: {} 查询身份证信息 (号码和图片URL)。", userId);

            //  查询用户ID对应的身份证信息
            UserExtension userExtension = getOne(new LambdaQueryWrapper<UserExtension>().eq(UserExtension::getUserId, userId));

            if (userExtension == null) {
                log.info("未找到用户ID: {} 的身份证信息。", userId);
                return null;
            }
            //转换为VO
            IdCardInfoVO idCardInfo=new IdCardInfoVO();
            BeanUtils.copyProperties(userExtension, idCardInfo);
            //返回VO
            return idCardInfo;
    }
    /**
     * 更新用户身份证信息状态
     * @param userExtensionId
     * @param checkStatus
     * @return
     */
    @Override
    @Transactional
    public boolean updateIdCardStatus(Long userExtensionId, Integer checkStatus) {
        //根据用户扩展id查询信息
        UserExtension userExtension = this.getById(userExtensionId);
        //判断是否为空,为空则返回false
        if(userExtension == null){
            log.warn("用户扩展信息不存在，无法更新状态");
            return false;
        }
        //设置状态
        userExtension.setCheckStatus(checkStatus);
        boolean updateResult = this.updateById(userExtension);
        if(updateResult){
            log.info("用户扩展信息更新成功");
            return true;
        }
        log.error("用户扩展信息更新失败");
        return false;
    }

    /**
     * 管理员分页查询所有身份证信息
     * 
     * @param current 当前页码
     * @param size 每页数据量
     * @return 包含身份证信息的分页数据
     */
    @Override
    public Page<IdCardInfoVO> pageIdCardsForAdmin(Integer current, Integer size) {
        log.info("开始分页查询身份证信息，页码：{}，每页数量：{}", current, size);
        
        // 创建分页对象
        Page<UserExtension> userExtensionPage = new Page<>(current, size);
        
        // 创建查询条件 - 只查询有身份证信息的用户
        LambdaQueryWrapper<UserExtension> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.isNotNull(UserExtension::getIdCardNum)
                .isNotNull(UserExtension::getIdCardPic)
                .orderByDesc(UserExtension::getId);
        
        // 执行分页查询
        this.page(userExtensionPage, queryWrapper);
        
        // 创建返回结果页对象
        Page<IdCardInfoVO> resultPage = new Page<>(current, size);
        resultPage.setTotal(userExtensionPage.getTotal());
        resultPage.setCurrent(userExtensionPage.getCurrent());
        resultPage.setSize(userExtensionPage.getSize());
        resultPage.setPages(userExtensionPage.getPages());
        
        // 转换查询结果为VO对象
        if (!CollectionUtils.isEmpty(userExtensionPage.getRecords())) {
            List<IdCardInfoVO> records = userExtensionPage.getRecords().stream()
                    .filter(ext -> StringUtils.hasText(ext.getIdCardNum()) && StringUtils.hasText(ext.getIdCardPic()))
                    .map(ext -> {
                        IdCardInfoVO vo = new IdCardInfoVO();
                        BeanUtils.copyProperties(ext, vo);
                        return vo;
                    })
                    .collect(Collectors.toList());
            
            resultPage.setRecords(records);
            log.info("分页查询身份证信息成功，共查询到 {} 条记录", records.size());
        } else {
            resultPage.setRecords(Collections.emptyList());
            log.info("分页查询身份证信息成功，但未查询到记录");
        }
        
        return resultPage;
    }
}
