/*
 * label：栏目名称
 * key：对应值
 * width：宽度
 * formatter：回调函数，参数为row:该行数据，cellVal：单元格数据，返回格式化值
 */
export const tableColumn = [
    { label: '微信账号', key: "nickName", width: '200' },
    { label: '手机号码', key: "phone", width: '200' },
    { label: '注册时间', key: "regTime" },
    {
        label: '绑定设备', key: "bindDevNum", width: '200', formatter: function (row, cellVal) {
            return `<span>${cellVal} 台</a>`
        }
    }
]

/*
 * 表格功能
 * select：第一列选中框
 * del：操作栏删除功能
 * view：操作栏详情功能
 */
export const tableFunc = ['select', 'del', 'mulDel', 'view']

/*
 * 查询栏数据
 */
export const queryItem = [
    { label: "账号：", type: 'text', key: 'nickName' }
]

/*
 * 存储查询栏数据
  */
export class QueryParams {
    constructor({ nickName = null }) {
        this.nickName = nickName;
    }
} 