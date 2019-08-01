import router from '@/router';
import { Message } from 'element-ui'
export function successCode(res) {
    const code = res.data.ret // 后台约定的返回码
    if (code === 0) {
        return res.data
    } else if (code === 1001) {
        Message({
            message: '参数错误',
            type: 'warning'
        })
    } else if (code === 1002) {
        Message({
            message: '业务异常'
        })
    } else if (code === 1003) {
        Message({
            message: '操作失败',
            type: 'error'
        })
    } else if (code === 1004) {
        Message({
            message: '未知异常',
            type: 'error'
        })
    } else if (code === 1005) {
        Message({
            message: '超时',
            type: 'error'
        })
    } else if (code === 1006) {
        Message({
            message: '非法参数',
            type: 'error'
        })
    } else if (code === 1007 || code === 1008) {
        Message({
            message: '登录失效',
            type: 'error'
        })
        router.push({ path: '/login' })
    } else if (code === 1009) {
        Message({
            message: '未登录',
            type: 'error'
        })
        router.push({ path: '/login' })
    } else if (code === 1) {
        Message({
            message: '终端编号已存在',
            type: 'error'
        })
    } else if (code === 30001) {
        Message({
            message: "数据库操作异常，删除失败",
            type: 'error'
        })
    }
}

export function errorCode(code) {
    switch (code) {
        case 401:
            Message({
                message: '登录失效',
                type: 'error'
            });
            router.push({ path: '/login' })
            break;
        case 413:
            Message({
                message: '图片尺寸过大',
                type: 'error'
            });
            break;
        default:
            Message({
                message: '请求失败',
                type: 'error'
            })
    }
}