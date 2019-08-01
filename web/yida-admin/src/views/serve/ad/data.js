// 搜索框项目
export const searchItems = [
      { label: "名称", type: 'input', value: 'name' }
]

// 功能按钮
export const funcs = [];

// 行内功能按钮
export const lineFuncs = ['广告审核'];

// 表格项目
export const tableColumns = [
      { label: '名称', value: "name" },
      { label: '标题', value: "title" },
      { label: '内容', value: "solgan" },
      {
            label: '上架状态', value: "state", formatter: function (row) {
                  if (row.state === 0) {
                        return '<span style="color:#909399">未上架</span>'
                  } else if (row.state === 1) {
                        return '<span style="color:#67C23A">已上架</span>'
                  } else if (row.state === 2) {
                        return '<span style="color:#E6A23C">待审核</span>'
                  } else if (row.state === 3) {
                        return '<span style="color:#F56C6C">审核未通过</span>'
                  } else {
                        return '/'
                  }
            }
      },
      { label: '创建时间', value: "createTime" },
      { label: '更新时间', value: "updateTime" }
]