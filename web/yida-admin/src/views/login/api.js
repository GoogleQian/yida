import http from '@/service/http';

// 登录
export function login(params){
    return http({
        url: `/access/login`,
        method: 'get',
        params
    })
}

// 图形验证码
export function captcha(params){
    return http({
        url: `/access/captcha`,
        method: 'get',
        params,
    })
}