import fly from '@/request/http';

export function buyFilter(){
    return fly.request({
        url: `/yida/miniProgram/filters`,
        method: 'get'
    })
}