import request from '@/utils/request'
import { TalentRatingResponse } from './type'

// 获取获取当前用户的人才评分（机构端）
export function getTalentRating(userId: number):Promise<TalentRatingResponse> {
    const formData = new FormData();
    formData.append('userId', userId.toString());
    return request.post('/talent/rating',formData)
}