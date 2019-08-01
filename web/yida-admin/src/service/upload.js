import http from './http';

// 滤芯
// 导入
export function upload(data) {
    return http({
        url: '/yida/filterInfos/import',
        method: 'post',
        data,
        'Content-Type': 'multipart/form-data'
    })
}

// 导出
export function _export(params) {
    return http({
        url: '/yida/filterInfos/export',
        method: 'post',
        data: params,
        responseType: 'blob',
        // 'Content-Type': 'multipart/form-data'
    })
}

// 下载模板
export function download_ter_template(params) {
    return http({
        url: '/yida/filterInfos/download',
        method: 'get',
        params,
        responseType: 'blob',
        'Content-Type': 'multipart/form-data'
    })
}

// 设备
// 导入
export function uploadDev(data) {
    return http({
        url: 'yida/dev/import',
        method: 'post',
        data,
        'Content-Type': 'multipart/form-data'
    })
}

// 导出
export function _exportDev(params) {
    return http({
        url: '/yida/dev/export',
        method: 'post',
        data: params,
        responseType: 'blob',
        // 'Content-Type': 'multipart/form-data'
    })
}

// 下载模板
export function download_ter_template_dev(params) {
    return http({
        url: '/yida/dev/download',
        method: 'get',
        params,
        responseType: 'blob',
        'Content-Type': 'multipart/form-data'
    })
}