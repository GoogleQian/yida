import fly from '@/request/http'

// 获取banner广告
export function getCode(params) {
    return fly.request({
        url: `yida/miniProgram/phone/sendSms`,
        method: 'get',
        params
    })
}

// 提交手机号
export function postPhone(params){
    return fly.request({
        url: `yida/miniProgram/phone/validCode`,
        method: 'get',
        params
    })
}