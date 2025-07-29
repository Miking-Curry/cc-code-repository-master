package com.caicai.controller;

import com.caicai.entity.vo.TalentRatingVO;
import com.caicai.result.R;
import com.caicai.service.TalentRatingService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: TalentRatingController
 * @Description: 人才评分Controller
 * @Author: PCT
 * @Date: 2025/6/23 11:31
 * @Version: 1.0
 */

@RestController
@RequestMapping("/talent")
@RequiredArgsConstructor
public class TalentRatingController {
    private final TalentRatingService talentRatingService;

    /**
     * @param userId: 需评分的用户(求职者)id 非当前登录用户id
     * @return R<TalentRatingVO>:
     * @Author: Panda
     * @Description: 根据人才id进行人才评分
     * @Date: 2025/6/25 14:47
     */
    @PostMapping("/rating")
    public R<TalentRatingVO> talentRating(@RequestParam("userId") Long userId) {
        return R.success(talentRatingService.talentRating(userId));
    }

    @PostMapping("/get-rating")
    public R<TalentRatingVO> getRating(@RequestParam("userId") Long userId) {
        return R.success(talentRatingService.getRating(userId));
    }
}
