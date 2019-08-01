import http from '@/service/http'

// 列表
export function getList(params) {
    return http({
        url: `/yida/ad/pages`,
        method: 'get',
        params
    })
}

// 新增
export function add(data) {
    return http({
        url: `/orderss/`,
        method: 'post',
        data
    })
}

// 修改
export function edit({ id, data }) {
    return http({
        url: `/orderss/${id}`,
        method: 'put',
        data
    })
}

// 删除
export function del({ id }) {
    return http({
        url: `/yida/ad/ads/${id}`,
        method: 'delete'
    })
}

// 批量删除
export function mulDel(data) {
    return http({
        url: `/yida/ad/batch`,
        method: 'post',
        data
    })
}

// 详情
export function getDetail({ id }) {
    return http({
        url: `/orderss/${id}`,
        method: 'get'
    })
}

// 上架、下架
export function setAdState({state,adId}){
    return http({
        url: `/yida/ad/ads/${adId}/state/${state}`,
        method: 'put'
    })
}

// 审核广告
export function verify({state, adId}){
    return http({
        url: `/yida/ad/ads/${adId}/audit/${state}`,
        method: 'put'
    })
}
