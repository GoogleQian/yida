import fly from '@/request/http'
import fly1 from '@/request/http1'

// 获取banner广告
export function getBanner() {
    return fly.request({
        url: `yida/miniProgram/banners`,
        method: 'get'
    })
}

// 获取设备列表
export function getDevList(params) {
    return fly.request({
        url: `/yida/miniProgram/list`,
        method: 'get',
        params: { ...params, page: 1, pageSize: 999 }
    })
}

// 获取用户信息
export function getUserInfo({ code }) {
    return fly.request({
        url: `/yida/miniProgram/info/${code}`,
        method: 'get'
    })
}

// 绑定设备
export function bindDev({ devCode, userId }) {
    return fly.request({
        url: `/yida/miniProgram/bind/${devCode}/${userId}`,
        method: 'get'
    })
}

// 检查是否已绑定手机报
export function isBindPhone(params) {
    return fly.request({
        url: `yida/miniProgram/phone/bindingStatus`,
        method: 'get',
        params
    })
}

// 提交用户信息到后台
export function saveUserinfo(params) {
    return fly1.post(`/yida/miniProgram/nick`, params)
}