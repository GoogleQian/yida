import http from './http';
// banner列表
export function bannerList(data) {
    return http({
        url: '/yida/banner/pages',
        method: 'get',
        params: data
    })
}

// 添加banner
export function bannerAdd(data) {
    return http({
        url: '/yida/banner/banners',
        method: 'post',
        data: data
    })
}

// 修改banner
export function bannerEdit(data) {
    return http({
        url: `/yida/banner/banners/${data.id}`,
        method: 'put',
        data: data
    })
}

// 删除banner
export function devBanner(data) {
    return http({
        url: `yida/banner/banners/${data.id}`,
        method: 'delete',
    })
}

// 批量删除
export function devMulBanner(data) {
    return http({
        url: `/yida/banner/batch`,
        method: 'post',
        data
    })
}

// 上架下架
export function onSellStatus(data) {
    return http({
        url: `yida/banner/banners/${data.id}/state/${data.state}`,
        method: 'put'
    })
}