/*
 * label：栏目名称
 * key：对应值
 * width：宽度
 * formatter：回调函数，参数为row:该行数据，cellVal：单元格数据，返回格式化值
 */
export const tableColumn = [
  { label: "滤芯编码", key: "filterCode", width: "200" },
  { label: "生成时间", key: "inventoryTime" },
  {
    label: "导出时间",
    key: "exportTime",
    formatter(row) {
      let text = "";
      if (row.exportTime) {
        text = `<span>${row.exportTime}</span>`;
      } else {
        text = "<span>/</span>";
      }
      return text;
    }
  },
  {
    label: "滤芯状态",
    key: "status",
    width: "200",
    formatter: function(row, cellVal) {
      let text = "-";
      if (cellVal === 0) {
        text = `<span style="color: #19be6b"> 未使用 </span>`;
      } else if (cellVal === 1) {
        text = `<span style="color: #ed3f14">使用中</span>`;
      }
      return text;
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
  "add",
  "import",
  "export",
  "qrCode",
  "generate"
];

/*
 * 查询栏数据
 */
export const queryItem = [
  { label: "滤芯编码：", type: "text", key: "filterCode" },
  {
    label: "滤芯状态：",
    type: "select",
    key: "status",
    placeholder: "请选择滤芯状态"
  }
];
