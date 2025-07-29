package com.caicai.controller;

import com.caicai.entity.vo.UserPointChangeRecordVO;
import com.caicai.entity.vo.UserPointCountVO;
import com.caicai.result.R;
import com.caicai.service.UserPointChangeRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName: PointController
 * @Description: 用户积分相关
 * @Author: PCT
 * @Date: 2025/6/3 10:56
 * @Version: 1.0
 */

@RestController
@RequestMapping("/point")
@RequiredArgsConstructor
public class PointController {
    private final UserPointChangeRecordService userPointChangeRecordService;

    /**
     * @param userId:
     * @return R<List<UserPointChangeRecordVO>>:
     * @Author: Panda
     * @Description: 获取用户积分变动记录
     * @Date: 2025/6/3 11:52
     */
    @GetMapping("/record")
    public R<List<UserPointChangeRecordVO>> getUserPointChangeRecord(@RequestAttribute("userId") Long userId) {
        return R.success(userPointChangeRecordService.getUserPointChangeRecord(userId));
    }

    /**
     * @param userId:
     * @return R<UserPointCountVO>:
     * @Author: Panda
     * @Description: 获取用户当日积分总收入
     * @Date: 2025/6/4 10:59
     */
    @GetMapping("/today")
    public R<UserPointCountVO> getUserTodayPointCount(@RequestAttribute("userId") Long userId) {
        return R.success(userPointChangeRecordService.getUserTodayPointCount((userId)));
    }

    /**
     * @param userId:
     * @return R<UserPointCountVO>:
     * @Author: Panda
     * @Description: 获取用户历史积分总收入
     * @Date: 2025/6/4 11:10
     */
    @GetMapping("/all-income")
    public R<UserPointCountVO> getUserAllPointIncome(@RequestAttribute("userId") Long userId) {
        return R.success(userPointChangeRecordService.getUserAllPointIncome(userId));
    }
}
