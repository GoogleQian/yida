import fly from '@/request/http';
import fly1 from '@/request/http1';

// 获取绑定状态
export function getBindStatus({ devCode }) {
    return fly1.request({
        url: `/yida/miniProgram/status/${devCode}`,
        method: 'get'
    })
}

// 获取下发指令内容（用于下发命令给设备）
export function getCommandContent({devCode, code=1}){
    return fly1.request({
        url: `/yida/miniProgram/btCommand`,
        params: {devCode, code}
    })
}

// 向后台发送绑定成功信号
// active = 1为假成功
export function postBindSuccess({devCode, userId, active=1}){
    return fly1.request({
        url: `/yida/miniProgram/btBindDev`,
        params: {devCode, userId, active}
    })
}