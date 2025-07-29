package com.caicai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caicai.entity.pojo.UserResign;
import com.caicai.entity.vo.ResignVO;
import com.caicai.mapper.ResignMapper;
import com.caicai.service.ResignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ResignServiceImpl extends ServiceImpl<ResignMapper, UserResign> implements ResignService {
    /**
     * 保存离职信息
     * @param userId
     * @param resignReason
     * @param resignAt
     * @return
     */
    @Override
    @Transactional
    public boolean saveResignInfo(Long userId, String resignReason, String resignAt,String joinAt,
                                  String witnessName, String witnessPhone) {
        UserResign resign = new UserResign();
        resign.setUserId(userId);
        resign.setResignReason(resignReason);
        resign.setResignAt(resignAt);
        resign.setJoinAt(joinAt);
        resign.setWitnessName(witnessName);
        resign.setWitnessPhone(witnessPhone);
        boolean saved = save(resign);
        if (saved) {
            log.info("用户ID: {} 的离职信息保存成功", userId);
        } else {
            log.error("用户ID: {} 的离职信息保存失败", userId);
        }
        return saved;
    }

    /**
     * 更新离职信息
     * @param userId
     * @param resignReason
     * @param resignAt
     * @return
     */
    @Override
    @Transactional
    public boolean updateResignInfo(Long userId, String resignReason,String resignAt,String joinAt,
                                    String witnessName, String witnessPhone) {
        // 使用LambdaUpdateWrapper根据userId更新
        LambdaUpdateWrapper<UserResign> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserResign::getUserId, userId)
                .set(UserResign::getResignReason, resignReason)
                .set(UserResign::getResignAt, resignAt)
                .set(UserResign::getJoinAt, joinAt)
                .set(UserResign::getWitnessName, witnessName)
                .set(UserResign::getWitnessPhone, witnessPhone);

        // 这里是关键，第一个参数为null，表示不使用实体更新
        boolean updated = update(null, updateWrapper);
        if (updated) {
            log.info("用户ID: {} 的离职信息更新成功", userId);
        } else {
            log.error("用户ID: {} 的离职信息更新失败，数据库中没有匹配记录", userId);
        }
        return updated;
    }

    /**
     * 删除离职信息
     * @param userId
     * @return
     */
    @Override
    @Transactional
    public boolean removeResignInfo(Long userId) {
        boolean removed = remove(new LambdaQueryWrapper<UserResign>().eq(UserResign::getUserId, userId));
        if (removed) {
            log.info("用户ID: {} 的离职信息删除成功", userId);
        } else {
            log.info("用户ID: {} 的离职信息删除失败或不存在", userId);
        }
        return removed;
    }
    /**
     * 获取离职信息
     * @param userId
     * @return
     */
    @Override
    public ResignVO getResignInfo(Long userId) {
        // 根据userId找Resign
        UserResign resign = getOne(new LambdaQueryWrapper<UserResign>().eq(UserResign::getUserId, userId));
        if (resign == null) {
            log.warn("未找到用户ID: {} 的离职信息。", userId);
            return null; // 返回null而不是空对象，明确表示没有找到记录
        }

        ResignVO resignVO = new ResignVO();
        // 把resign转换成为ResignVO
        BeanUtils.copyProperties(resign, resignVO);
        log.info("成功获取用户ID: {} 的离职信息。", userId);
        return resignVO;
    }
    /**
     * 更新离职信息状态
     * @param resignId
     * @param checkStatus
     * @return
     */
    @Override
    @Transactional
    public boolean updateResignStatus(Long resignId, Integer checkStatus) {
        //根据ResignId找Resign
        UserResign resign = getById(resignId);
        //判断是否为空
        if (resign == null) {
            log.warn("该信息不存在，更新失败");
            return false;
        }
        resign.setCheckStatus(checkStatus);
        boolean updateResult = updateById(resign);
        if (updateResult) {
            log.info("更新成功");
            return true;
        }
        log.error("更新失败");
        return false;
    }

    /**
     * 管理员分页查询所有离职信息
     * 
     * @param current 当前页码
     * @param size 每页数据量
     * @return 包含离职信息的分页数据
     */
    @Override
    public Page<ResignVO> pageResignsForAdmin(Integer current, Integer size) {
        log.info("开始分页查询离职信息，页码：{}，每页数量：{}", current, size);
        
        // 创建分页对象
        Page<UserResign> resignPage = new Page<>(current, size);
        
        // 创建查询条件
        LambdaQueryWrapper<UserResign> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(UserResign::getId);
        
        // 执行分页查询
        this.page(resignPage, queryWrapper);
        
        // 创建返回结果页对象
        Page<ResignVO> resultPage = new Page<>(current, size);
        resultPage.setTotal(resignPage.getTotal());
        resultPage.setCurrent(resignPage.getCurrent());
        resultPage.setSize(resignPage.getSize());
        resultPage.setPages(resignPage.getPages());
        
        // 转换查询结果为VO对象
        if (!CollectionUtils.isEmpty(resignPage.getRecords())) {
            List<ResignVO> records = resignPage.getRecords().stream()
                    .map(resign -> {
                        ResignVO vo = new ResignVO();
                        BeanUtils.copyProperties(resign, vo);
                        return vo;
                    })
                    .collect(Collectors.toList());
            
            resultPage.setRecords(records);
            log.info("分页查询离职信息成功，共查询到 {} 条记录", records.size());
        } else {
            resultPage.setRecords(Collections.emptyList());
            log.info("分页查询离职信息成功，但未查询到记录");
        }
        
        return resultPage;
    }
}
