import ax from '@/service/http'

// 列表
export function getList(data) {
    return ax({
        url: `/yida/dev/clientConnRecord`,
        method: 'post',
        data
    })
}

// 新增
export function add(data) {
    return ax({
        url: `/emqClients/`,
        method: 'post',
        data
    })
}

// 修改
export function edit({ id, data }) {
    return ax({
        url: `/emqClients/${id}`,
        method: 'put',
        data
    })
}

// 删除
export function del({ id }) {
    return ax({
        url: `/emqClients/${id}`,
        method: 'delete'
    })
}

// 详情
export function getDetail({ id }) {
    return ax({
        url: `/emqClients/${id}`,
        method: 'get'
    })
}
// 导出
export function _export({ id }) {
    return ax({
        url: `/emqClients/export/${id}`,
        method: 'get',
        responseType: 'blob',
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
}

