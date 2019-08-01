import ax from '@/request/ax';

// 获取所有部门
export function deptList() {
    return ax({
        url: `/sys/dept/list`,
        method: 'get'
    })
}

// 获取部门详情
export function dept({ deptId }) {
    return ax({
        url: `/sys/dept/info/${deptId}`,
        method: 'get'
    })
}
// 获取部门详情
export function deptSelect() {
    return ax({
        url: `/sys/dept/select`,
        method: 'get'
    })
}

// 新增部门
export function addDept(data) {
    return ax({
        url: `/sys/dept/save`,
        method: 'post',
        data
    })
}

// 修改部门
export function editDept(data) {
    return ax({
        url: `/sys/dept/update`,
        method: 'post',
        data
    })
}

// 删除部门
export function delDept(data) {
    return ax({
        url: `/sys/dept/delete`,
        method: 'post',
        data
    })
}

