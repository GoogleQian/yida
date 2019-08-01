import http from './http';

// 用户列表
export function devList(data) {
    return http({
        url: '/yida/dev/list',
        method: 'get',
        params: data
    })
}

// 添加
export function add(data) {
    return http({
        url: `yida/dev/devices`,
        method: 'post',
        data
    })
}

// 编辑
export function edit({devId, data}){
    return http({
        url: `yida/dev/devices/${devId}`,
        method: 'put',
        data
    })
}

// 删除设备
export function devDel(data) {
    return http({
        url: `/yida/dev/devices/${data.id}`,
        method: 'delete',
    })
}

// 解绑
export function devUnbind({id}){
    return http({
        url: `yida/dev/unbind/${id}`,
        method: 'put'
    })
}

// 批量删除
export function devMulDel(data) {
    return http({
        url: `/yida/dev/batch`,
        method: 'post',
        data
    })
}

// 修改重绑状态
export function mulBindStatus(data) {
    return http({
        url: `yida/dev/repeat/${data.id}`,
        method: 'put',
        params: {
            enableMultipleBind: data.enableMultipleBind
        }
    })
}

//导入
export function devImport() {
  return http({
    url: `/yida/filterInfos/import`,
    method: "post"
  });
}