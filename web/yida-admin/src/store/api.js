import http from '@/service/http';


// 获取菜单项
export function getNav(params) {
    return http({
        url: '/sys/menu/nav',
        method: 'get',
        params
    })
}