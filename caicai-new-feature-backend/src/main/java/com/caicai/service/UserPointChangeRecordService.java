package com.caicai.service;

import com.caicai.entity.pojo.UserPointChangeRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.caicai.entity.vo.UserPointChangeRecordVO;
import com.caicai.entity.vo.UserPointCountVO;

import java.util.List;

/**
* @author CCN
* @description 针对表【user_point_change_record(用户积分变动明细表)】的数据库操作Service
* @createDate 2025-06-03 11:49:15
*/
public interface UserPointChangeRecordService extends IService<UserPointChangeRecord> {

    /**
     * @param userId:
     * @return List<UserPointChangeRecordVO>:
     * @Author: Panda
     * @Description: 获取用户积分变动记录
     * @Date: 2025/6/3 11:54
     */
    List<UserPointChangeRecordVO> getUserPointChangeRecord(Long userId);

    /**
     * @param userId:
     * @return UserPointCountVO:
     * @Author: Panda
     * @Description: 获取用户当日积分总收入
     * @Date: 2025/6/4 10:37
     */
    UserPointCountVO getUserTodayPointCount(Long userId);

    /**
     * @param userId:
     * @return UserPointCountVO:
     * @Author: Panda
     * @Description: 获取用户历史积分总收入
     * @Date: 2025/6/4 11:10
     */
    UserPointCountVO getUserAllPointIncome(Long userId);
}
