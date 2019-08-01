import http from './http';
// goods列表
export function goodsList(data) {
    return http({
        url: '/yida/filter/pages',
        method: 'get',
        params: data
    })
}

// 添加goods
export function goodsAdd(data) {
    return http({
        url: '/yida/filter/filters',
        method: 'post',
        data: data
    })
}

// 修改goods
export function goodsEdit(data) {
    return http({
        url: `/yida/filter/filters/${data.id}`,
        method: 'put',
        data: data
    })
}

// 删除goods
export function devgoods(data) {
    return http({
        url: `yida/filter/filters/${data.id}`,
        method: 'delete',
    })
}

// 批量删除
export function devMulgoods(data) {
    return http({
        url: `/yida/filter/batch`,
        method: 'post',
        data
    })
}

// 上架下架
export function onSellStatus(data) {
    return http({
        url: `yida/filter/filters/${data.id}/state/${data.state}`,
        method: 'put'
    })
}