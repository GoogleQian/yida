import fly from '@/request/http';

// 解绑设备
export function unbindDev({devCode}){
    return fly.request({
        url: `/yida/miniProgram/unbind/${devCode}`,
        method: 'get'
    })
}