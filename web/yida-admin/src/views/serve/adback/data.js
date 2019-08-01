/*
 * label：栏目名称
 * key：对应值
 * width：宽度
 * formatter：回调函数，参数为row:该行数据，cellVal：单元格数据，返回格式化值
 */
export const tableColumn = [
    { label: '名称', key: "name" },
    { label: '标题', key: "title" },
    { label: '内容', key: "solgan" },
    {
        label: '上架状态', key: "state", formatter: function (row, cellVal) {
            if (cellVal === 0) {
                return '<span style="color:#909399">未上架</span>'
            } else if (cellVal === 1) {
                return '<span style="color:#67C23A">已上架</span>'
            }else if (cellVal === 2) {
                return '<span style="color:#E6A23C">待审核</span>'
            }else if (cellVal === 3) {
                return '<span style="color:#F56C6C">未通过</span>'
            }else{
                return '/'
            }
        }
    },
    { label: '创建时间', key: "createTime" },
    { label: '更新时间', key: "updateTime" }

]

/*
 * 表格功能
 * select：第一列选中框
 * del：操作栏删除功能
 * view：操作栏详情功能
 * add: 添加
 * onSell: 上架下架
 */
export const tableFunc = ['add', 'select', 'del', 'view', 'onSell', 'mulDel']

/*
 * 查询栏数据
 */
export const queryItem = [
    { label: "名称：", type: 'text', key: 'name' }
]

/*
 * 存储查询栏数据
  */
// export class QueryParams {
//     constructor({ id = null }) {
//         this.id = id;
//     }
// } 