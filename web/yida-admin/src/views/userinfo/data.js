/*
 * label：栏目名称
 * key：对应值
 * width：宽度
 * formatter：回调函数，参数为row:该行数据，cellVal：单元格数据，返回格式化值
 */
export const tableColumn = [
    { label: '姓名', key: "realName" },
    { label: '微信账号', key: "nickName" },
    { label: '手机号', key: "phone" },
    { label: '生日', key: "birthday" },
    {
        label: '性别', key: "sex", formatter: function (row) {
            if (row.sex === 0) {
                return '男'
            } else if (row.sex === 1) {
                return '女'
            } else {
                return '/'
            }
        }

    },
    { label: '注册时间', key: "regTime" },
    {
        label: '绑定设备', key: "bindNum", formatter: function (row) {
            return row.bindNum ? row.bindNum + '台' : '/'
        }
    },
    { label: '地址', key: "address" },
    { label: '职业', key: "profession" },
]

/*
 * 表格功能
 * select：第一列选中框
 * del：操作栏删除功能
 * view：操作栏详情功能
 */
export const tableFunc = ['select', 'del', 'add', 'edit', 'mulDel']

/*
 * 查询栏数据
 */
export const queryItem = [
    // { label: "账号：", type: 'text', key: 'nickName' },
    { label: "姓名：", type: 'text', key: 'realName' },
    { label: "手机：", type: 'text', key: 'phone' }
]

/*
 * 存储查询栏数据
  */
// export class QueryParams {
//     constructor({ id = null }) {
//         this.id = id;
//     }
// }

export const pickerOptions = {
    disabledDate(time) {
        return time.getTime() > Date.now();
    },
}