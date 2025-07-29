package com.caicai.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.caicai.entity.pojo.UserExtension;
import com.caicai.entity.vo.IdCardInfoVO;


public interface IdCardService extends IService<UserExtension> {
    /**
     * 保存用户上传的身份信息
     * @param userId 用户ID
     * @param idCardNum 身份证号码
     * @param idCardPic 身份证图片URL
     * @param name 姓名
     * @return 操作结果
     */
    boolean uploadIdCardInfo(Long userId, String idCardNum, String idCardPic,String name);


    /**
     * 清除用户的身份证信息（将身份证号码和图片URL置空）
     * @param userId 用户ID
     * @return 操作结果
     */
    boolean clearIdCardInfo(Long userId);

    /**
     * 根据用户ID查询用户的身份证信息（号码和图片URL）。
     * @param userId 用户ID
     * @return 返回包含身份证信息的VO对象。
     */
    IdCardInfoVO getIdCardInfo(Long userId);

    /**
     *  更新用户的身份证信息审核状态
     *  @param userExtensionId 用户扩展ID
     *  @param checkStatus 审核状态
     *  @return 操作结果
     */
    boolean updateIdCardStatus(Long userExtensionId, Integer checkStatus);

    /**
     * 管理员分页查询所有身份证信息
     * 
     * @param current 当前页码
     * @param size 每页数据量
     * @return 包含身份证信息的分页数据
     */
    Page<IdCardInfoVO> pageIdCardsForAdmin(Integer current, Integer size);
}
