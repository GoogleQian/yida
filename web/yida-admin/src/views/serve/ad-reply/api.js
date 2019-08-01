import ax from '@/service/http'

// 列表
export function getList(params) {
    return ax({
        url: `/advstatic/findAll`,
        method: 'get',
        params
    })
}

// 新增
export function add(data) {
    return ax({
        url: `/loginLogs/`,
        method: 'post',
        data
    })
}

// 修改
export function edit({ id, data }) {
    return ax({
        url: `/loginLogs/${id}`,
        method: 'put',
        data
    })
}

// 删除
export function del({ id }) {
    return ax({
        url: `/loginLogs/${id}`,
        method: 'delete'
    })
}

// 详情
export function getDetail({ id }) {
    return ax({
        url: `/loginLogs/${id}`,
        method: 'get'
    })
}
