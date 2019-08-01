import http from '@/service/http';

// 用户列表
export function devList(data) {
    return http({
        url: '/yida/userInfos/pages',
        method: 'post',
        data: data
    })
}

// 添加用户
export function add(data) {
    return http({
        url: `/yida/userInfos`,
        method: 'post',
        data
    })
}

// 编辑用户
export function edit({id, data}){
    return http({
        url: `yida/userInfos/${id}`,
        method: 'put',
        data
    })
}

// 删除用户
export function devDel(data) {
    return http({
        url: `/yida/userInfos/${data.id}`,
        method: 'delete',
    })
}

// 批量删除用户
export function devMulDel(data) {
    return http({
        url: `/yida/userInfos/batch`,
        method: 'post',
        data
    })
}
