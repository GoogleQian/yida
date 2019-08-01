
export function geneOptions(x, y) {
  return {
    tooltip: {
      trigger: "axis"
    },
    calculable: true,
    xAxis: [
      {
        type: "category",
        boundaryGap: false,
        data: x
      }
    ],
    yAxis: [
      {
        type: "value"
      }
    ],
    series: [
      {
        name: "绑定设备数",
        type: "line",
        stack: "总量",
        data: y
      }
    ]
  };
}
