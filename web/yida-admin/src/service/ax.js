import axios from 'axios'
import { correct_code_parse } from './code-parse';
import { Message } from 'element-ui'
import router from '@/router/index.js'
import returnCitySN from 'returnCitySN'
// 创建axios实例
const baseURL = process.env.NODE_ENV === 'development' ? '/api' : '/api';
const ax = axios.create({
  // baseURL: '/api',
  baseURL,
  timeout: 100000000
})
// const returnCitySN = returnCitySN;
// 请求拦截
ax.interceptors.request.use(config => {
  config.headers.token = localStorage.getItem('token');
    // config.headers.userName = localStorage.getItem('userName');
  config.headers['x-forwarded-for'] = returnCitySN.cip;
  // config.headers['x-forwarded-for'] = window.returnCitySN.cip;
  return config
}, error => {
  return Promise.reject(error)
})

ax.interceptors.response.use(res => {
  if (res.data.ret === -401) {
    Message({
      message: '权限不足',
      type: 'error'
    })
    router.push({ path: '/' })
  }

  return res
}, error => {
  if (error.response) {
    correct_code_parse(error.response.status);
}

  // Message({ // 其他错误处理
  //   message: '登录认证失败，请重新登录！',
  //   type: 'error',
  //   duration: 5 * 1000
  // })
  // router.replace({ name: 'login' })
  return Promise.reject(error)
})

export default ax
