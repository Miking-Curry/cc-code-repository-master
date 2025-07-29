import request from '@/utils/request'
import { AiConclusionResponse, ApiResponse, MatchJobItem } from './type'
import { showError } from '../toast';

// 获取简历评分
export function getResumeRating(): Promise<ApiResponse<number>> {
    return request.get('/xxx')
}

// 投递简历
export function sendResume(id: number): Promise<ApiResponse> {
    const formData = new FormData();
    formData.append('id', id.toString());
    return request.post('http://112.74.33.58:48801/api/user/post/send_resume/', formData)
        .then((response) => {
            if (response.data.code === 403) {
                showError(response.data.msg);
            }
            return response.data;
        })
        .catch((error) => {
            // 这里处理 HTTP 错误码的响应体
            if(error.code === 0){
                return error;
            }
            showError(error.msg)
            return error;
        });
}

// 获取简历评分，建议文本，和关键词
export function getAiConclusion(): Promise<AiConclusionResponse> {
    return request.post('/resume/conclusion/generate')
}

// 获取匹配岗位列表
export function getJobList(): Promise<ApiResponse<MatchJobItem[]>> {
    return request.get('/postVector/recommend/save')
}