import fly from '@/request/http'

// 洗发激活滤芯命令
export function bindFilter(params) {
    return fly.request({
        url: `/yida/miniProgram/activeFilterLife`,
        params,
        method: 'get'
    })
}

// 验证滤芯编码是否合法
export function validFilterCode({ filterCode, userId, devCode }) {
    return fly.request({
        url: `/yida/miniProgram/validFilterCode`,
        params: { filterCode, userId, devCode },
        method: 'get'
    })
}