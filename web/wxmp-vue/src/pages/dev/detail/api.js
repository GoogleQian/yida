import fly from '@/request/http';

export function buyFilter(){
    return fly.request({
        url: `/yida/miniProgram/filters`,
        method: 'get'
    })
}
// 解绑设备
export function unbindDev({devCode}){
    return fly.request({
        url: `/yida/miniProgram/unbind/${devCode}`,
        method: 'get'
    })
}