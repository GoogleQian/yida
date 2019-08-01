import ax from 'axios';
import { Message } from 'element-ui';
import { correct_code_parse } from './code-parse';
import router from '@/router/'
import returnCitySN from 'returnCitySN'
const baseURL = process.env.NODE_ENV === 'development' ? '/api' : '/mng';

const http = ax.create({
    baseURL,
    timeout: 20000,
    withCredentials: true
})
// 请求拦截
http.interceptors.request.use(config => {
    // ...
    config.headers.token = localStorage.getItem('token');
    // config.headers.userName = localStorage.getItem('userName');
    config.headers['x-forwarded-for'] = returnCitySN.cip;
    // config.headers['x-forwarded-for'] = window.returnCitySN.cip;
    return config
})

// 响应拦截
http.interceptors.response.use(res => {
    // successCode(res);
    if (res.data.ret === 1002) {
        Message({
            message: '登录失效',
            type: 'error'
        })
        setTimeout(() => {
            router.push({ path: '/login' })
        }, 1000);
    }
    return res.data
}, error => {
    if (error.response) {
        // console.log(error.response);
        correct_code_parse(error.response);
        // correct_code_parse(error.response.status);
    }
    return Promise.reject(error)
})

export default http