import http from './http';

// 用户列表
export function devList(data) {
    return http({
        url: '/yida/user/list',
        method: 'get',
        params: data
    })
}

// 删除用户
export function userDel(data) {
    return http({
        url: `/yida/user/users/${data.id}`,
        method: 'delete',
    })
}

// 批量删除
export function userMulDel(data) {
    return http({
        url: `/yida/user/batch`,
        method: 'post',
        data
    })
}