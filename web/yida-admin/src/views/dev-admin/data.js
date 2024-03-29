/*
 * label：栏目名称
 * key：对应值
 * width：宽度
 * formatter：回调函数，参数为row:该行数据，cellVal：单元格数据，返回格式化值
 */
export const tableColumn = [
  { label: "设备号", key: "devCode", width: "130px" },
  {
    label: "绑定账号",
    key: "bindAccount",
    formatter: function(row, cellVal) {
      if (row.bindAccount === null) {
        return "/";
      } else {
        return row.bindAccount;
      }
    }
  },
  // {
  //     label: '激活状态', key: "active", formatter: function (row, cellVal) {
  //         if (cellVal === 0) {
  //             return '<span style="color:#F56C6C">未激活</span>'
  //         } else if (cellVal === 1) {
  //             return '<span style="color:#67C23A">激活</span>'
  //         } else {
  //             return '/'
  //         }
  //     }
  // },
  {
    label: "绑定状态",
    key: "status",
    formatter: function(row, cellVal) {
      if (cellVal === 0) {
        return '<span style="color:#909399">未绑定</span>';
      } else if (cellVal === 1) {
        return '<span style="color:#67C23A">已绑定</span>';
      } else {
        return "/";
      }
    }
  },
  {
    label: "绑定时间",
    key: "bindTime",
    formatter: function(row, cellVal) {
      if (row.status === 0) {
        return "/";
      } else {
        return cellVal;
      }
    }
  },
  { label: "入库时间", key: "inventoryTime" },
  {
    label: "在线状态",
    key: "online",
    formatter: function(row, cellVal) {
      if (cellVal === 0) {
        return '<span style="color:#909399">离线</span>';
      } else if (cellVal === 1) {
        return '<span style="color:#67C23A">在线</span>';
      } else {
        return "/";
      }
    }
  },
  { label: "ip地址", key: "ip", width: "120px" },
  { label: "连接时间", key: "connectTime", width: "140px" },
  {
    label: "滤芯剩余寿命",
    key: "filterLifeTime",
    formatter: function(row, cellVal) {
      if (row.status === 0) {
        return "/";
      } else {
        return cellVal + "%";
      }
    }
  }
];

/*
 * 表格功能
 * select：第一列选中框
 * del：操作栏删除功能
 * view：操作栏详情功能
 */
export const tableFunc = [
  "select",
  "del",
  "qrCode",
  "singleStatus",
  "add",
  "mulDel",
  "connectLog",
  "import",
  "export",
  "screen"
];

/*
 * 查询栏数据
 */
export const queryItem = [
  { label: "设备号：", type: "text", key: "devCode" },
  { label: "在线状态：", type: "select", key: "online",placeholder:"请选择设备状态" }
];

/*
 * 存储查询栏数据
 */
// export class QueryParams {
//     constructor({ id = null }) {
//         this.id = id;
//     }
// }
