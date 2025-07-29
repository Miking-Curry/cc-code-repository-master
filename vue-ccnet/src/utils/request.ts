import axios, { AxiosInstance } from 'axios'
import { useUserStore } from '@/stores/user'
import { showNeedLoginDialog } from './globalDialog';

// error的实例内容
// {
//     message: 'timeout of 5000ms exceeded',
//     code: 'ECONNABORTED',
//     config: { url: '/api/user', method: 'get', ... },
//     request: XMLHttpRequest, // 有请求但没响应
//     response: undefined      // 因为没响应，所以 response 是 undefined
// }

//创建axios对象
const service: AxiosInstance = axios.create({
    baseURL: '/api',
    timeout: 30000, // 请求超时时间（毫秒）
    // withCredentials: true, // 跨域时是否携带 cookie
});

//请求拦截器
service.interceptors.request.use(
    config => {
        //传递token
        const userStore = useUserStore()
        const token = userStore.token
        if (token) {
            config.headers.Authorization = `Bearer ${token}`; //生产环境使用
        }
        return config;
    }, error => {
        //请求失败了
        return Promise.reject(error);
    });

// 请求情况	会进入哪里？
// ✅ HTTP 200 且业务 code 正常	response => 成功分支	正常执行
// ❌ HTTP 200 但业务 code 失败	response =>，你手动 Promise.reject()	接下来会进入.catch()
// ❌ HTTP 非 2xx（如 500、404）	error => 系统级错误
// ❌ 网络错误、断网、超时	error => error.request 存在，但无 response
// ❌ 请求配置错误	error => error.message 为主

// 响应拦截器
// 判断code码
// 自定义业务异常处理交给调用的下一层处理异常信息
service.interceptors.response.use(
    response => {
        const res = response.data;
        if (res.code !== 200) {
            // showError('业务错误')
            console.log('业务错误')
            return Promise.reject(res);
        }
        return res;
    }, error => {
        if (error.response) {
            if (error.response.data.msg === '无效或过期的Token') {
                showNeedLoginDialog()
            }
        } else if (error.request) {
            console.log('无响应，请求不成功');
        } else {
            console.log('请求配置错误');
        }
        return Promise.reject(error);
    }
);

export default service