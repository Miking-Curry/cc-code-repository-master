package com.caicai.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.caicai.entity.pojo.UserResign;
import com.caicai.entity.vo.ResignVO;

import java.time.LocalDateTime;

public interface ResignService extends IService<UserResign> {
    /**
     * 保存离职信息
     * @param userId
     * @param resignReason
     * @param resignAt
     * @param joinAt
     * @param witnessName
     * @param witnessPhone
     * @return
     */
    boolean saveResignInfo(Long userId, String resignReason, String resignAt,String joinAt,
                            String witnessName, String witnessPhone);
    /**
     * 修改离职信息
     * @param userId
     * @param resignReason
     * @param resignAt
     * @param joinAt
     * @param witnessName
     * @param witnessPhone
     * @return
     */
    boolean updateResignInfo(Long userId, String resignReason, String resignAt,String joinAt,
                              String witnessName, String witnessPhone);

    /**
     * 删除离职信息
     * @param userId
     * @return
     */
    boolean removeResignInfo(Long userId);

    /**
     * 获取离职信息
     * @param userId
     * @return
     */
    ResignVO getResignInfo(Long userId);

    /**
     * 修改离职信息审核状态
     * @param resignId
     * @param checkStatus
     * @return
     */
    boolean updateResignStatus(Long resignId, Integer checkStatus);

    /**
     * 管理员分页查询所有离职信息
     * 
     * @param current 当前页码
     * @param size 每页数据量
     * @return 包含离职信息的分页数据
     */
    Page<ResignVO> pageResignsForAdmin(Integer current, Integer size);
}
