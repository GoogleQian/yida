import ax from '@/service/http'

// 列表
export function getList(data) {
    return ax({
        url: `/errorLogs`,
        method: 'post',
        data
    })
}
