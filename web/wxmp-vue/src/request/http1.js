import Fly from 'flyio/dist/npm/wx';

const fly = new Fly();

// const baseURL = "http://iotsvr.he-live.com:8787/";  // 测试
const baseURL = "https://iotsvr.he-live.com/";  // 测试2

// const baseURL = "https://iotsvr.zsxiaohe.com/"; // 生产环境
// const baseURL = "https://10.0.1.223";

fly.interceptors.request.use(request => {
    request.headers = {
        'Content-Type': 'application/json'
    }
    return request;
})

fly.interceptors.response.use(response => {
    return response.data
}, err => {
    if (err) {
        return '请求失败'
    }
})

fly.config.baseURL = baseURL;

export default fly;