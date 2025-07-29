package com.caicai.service;

import com.caicai.entity.vo.TalentRatingVO;
import org.springframework.web.bind.annotation.RequestAttribute;

/**
 * @ClassName: TalentRatingService
 * @Description: 人才评分
 * @Author: PCT
 * @Date: 2025/6/23 11:13
 * @Version: 1.0
 */

public interface TalentRatingService {
    /**
     * @param userId:
     * @return TalentRatingVO:
     * @Author: Panda
     * @Description: 根据人才简历等信息通过大模型计算评分
     * @Date: 2025/6/26 11:33
     */
    TalentRatingVO talentRating(@RequestAttribute Long userId);

    /**
     * @param userId:
     * @return TalentRatingVO:
     * @Author: Panda
     * @Description: 通过userId查询数据库中的人才评分
     * @Date: 2025/6/26 11:34
     */
    TalentRatingVO getRating(Long userId);
}
