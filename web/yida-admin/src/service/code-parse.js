import { Message } from 'element-ui';
import router from '@/router/index.js'

export function correct_code_parse(errMsg) {
    let code = errMsg.status;
    let text = errMsg.data;
    switch (code) {
        case 401:
            // Message({
            //     message: '未登录或登录失效',
            //     type: 'error'
            // })
            router.push({ path: '/login' });
            break;
        case 400:
            Message({
                message: text || '参数格式有误',
                type: 'error'
            })
            break;
    }
}

export function normal_code_parse(errMsg) {
    let code = errMsg.code;
    switch (code) {
        case -1:
            Message({
                message: '参数格式有误',
                type: 'error'
            });
            break;
        case -101:
            Message({
                message: '系统异常',
                type: 'error'
            });
            break;
    }
}