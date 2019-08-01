import Fly from 'flyio/dist/npm/wx';
import store from '../store';

const fly = new Fly();

// const baseURL = "http://iotsvr.he-live.com:8787/";  // 测试
const baseURL = "https://iotsvr.he-live.com/";  // 测试2
// const baseURL = "https://iotsvr.zsxiaohe.com/"; // 生产环境
// const baseURL = "https://10.0.1.223";

fly.interceptors.request.use(request => {
    wx.showLoading({
        title: '加载中',
        mask: true
    })
    request.headers = {
        'Content-Type': 'application/json'
    }
    // 处理请求前逻辑
    // if (window.sessionStorage.getItem('token')) {
    //     config.headers.Authorization = window.sessionStorage.getItem('token')
    // }
    return request;
})

fly.interceptors.response.use(response => {
    if (store.state.canCloseLoading) {
        wx.hideLoading();
    } else {
        setTimeout(() => {
            wx.hideLoading();
        }, 2000);
    }
    return response.data
}, err => {
    console.log(err);
    // wx.showToast({
    //     title: `${JSON.stringify(err.request)}`,
    //     icon: "none",
    //     duration: 5000
    // });
    console.log('err')
    wx.hideLoading();
    if (err) {
        return '请求失败'
    }
})

fly.config.baseURL = baseURL;

export default fly;