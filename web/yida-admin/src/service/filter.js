import http from './http';

// 滤芯列表
export function filterList(data) {
    return http({
        url: 'yida/filterInfos',
        method: 'get',
        params: data
    })
}

// 删除
export function filterDel({id}) {
    return http({
        url: `/yida/filterInfos/${id}`,
        method: 'delete',
    })
}

// 新增
export function filterAdd(data){
    return http({
        url: `/yida/filterInfos`,
        method: 'post',
        data
    })
}

// 修改
export function filterEdit({id, data}){
    return http({
        url: `/yida/filterInfos/${id}`,
        method: 'put',
        data
    })
}

// 详情
export function filterView({id}){
    return http({
        url: `/yida/filterInfos/${id}`,
        method: 'get'
    })
}

// 导入
export function filterImport(){
    return http({
        url: `/yida/filterInfos/import`,
        method: 'post'
    })
}

//批量生成
export function filterGen(){
    return http({
        url: '/yida/filterInfos/random_export',
        method:"post",
        responseType: 'blob',
        // 'Content-Type': 'multipart/form-data'
    })
}
