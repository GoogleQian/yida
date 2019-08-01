// 搜索框项目
export const searchItems = [
    {
        label: "用户名",
        value: "userName",
        type: "input",
        placeholder: "用户名",
    },
    {
        label: "状态",
        value: "action",
        type: "select",
        options: [{label: '上线', value: 1}, {label: '下线', value: 0}],
        placeholder: "请选择",
    },
    {
        label: '开始时间',
        value: 'startTime',
        type: 'datetime'
    },
    {
        label: '结束时间',
        value: 'endTime',
        type: 'datetime'
    },
]

// 功能按钮
export const funcs = [];

// 表格项目
export const tableColumns = [
    {label: "用户名", value: "username"},
    {label: "设备编号", value: "client_id"},
    {
        label: "状态", value: "action", formatter(row) {
            let text = '-';
            if (row.action === "1") {
                text = '<span style="color:#67C23A">上线</span>'
            } else {
                text = '<span style="color:#f54567">下线</span>'
            }
            return text
        }
    },
    {label: "创建时间", value: "createTime"},
]

