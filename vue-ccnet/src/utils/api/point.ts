
import request from '@/utils/request'
import type { ApiResponse, RecordListResponse, Task, TaskListResponse, TodayPointResponse, TotalPointResponse } from './type'

// GET		axios.get(url, config)
// POST		axios.post(url, data, config)
// DELETE   axios.delete(url, config)
// PUT		axios.put(url, data, config)

export function getPoints() {
    return request.get('/api/xxx')
}

// 获取任务列表
export function getTaskList(): Promise<TaskListResponse> {
    return request.get('/task')
}

// 查询用户总积分
export function getUserPoint(): Promise<ApiResponse<number>> {
    return request.get('/user/point')
}

// 查询积分获取记录
export function getUserPointRecord(): Promise<RecordListResponse> {
    return request.get('/point/record')
}

// 获取用户今日获得积分
export function getUserTodayPoint(): Promise<TodayPointResponse> {
    return request.get('/point/today')
}

// 获取用户累计获得积分
export function getUserTotalPoint(): Promise<TotalPointResponse> {
    return request.get('/point/all-income')
}

export function withdraw(amount: number, phone: string): Promise<ApiResponse> {
    const formData = new FormData();
    formData.append("amount", amount.toString());
    formData.append("phone", phone);
    return request.post('/withdraw/apply', formData);
}

// 查询用户电话号码
export function getUserPhoneNumber(): Promise<ApiResponse> {
    return request.get('/user/phone')
}
