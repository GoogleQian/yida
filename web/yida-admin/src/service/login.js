import http from './http';
import qs from 'qs';
// goods列表
export function login(data) {
    return http({
        url: '/sys/login',
        method: 'post',
        data: qs.stringify(data),

    })
}