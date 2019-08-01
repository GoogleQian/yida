<template>
  <div id="box">
    <div id="total" style="width:800px;height:400px">
      <div class="left">
        <span>设备总数：{{totalData.totalCount}}台</span>
        <span>在线设备数：{{totalData.onlineCount}}台</span>
        <span>离线设备数：{{totalData.outlineCount}}台</span>
        <span>已绑定设备数：{{totalData.statusCount}}台</span>
      </div>
      <div id="totalNum" style="width:600px;height:400px"></div>
    </div>
    <div id="wechat" style="height:400px">
      <div class="left">
        <img :src="wechat" alt>
      </div>
      <div class="right">
        <span>微信用户总数：{{wxData.userTotal}}人</span>
        <span>今日新增用户总数：{{wxData.regTotal}}人</span>
      </div>
    </div>
    <div id="bindDev" style="width:100%;height:400px">
      <div class="block">
        <span class="demonstration">每日绑定设备数统计</span>
        <el-date-picker
          v-model="value1"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :picker-options="pickerOptions"
          value-format="yyyy-MM-dd"
          @change="changeBindDate"
        ></el-date-picker>
      </div>
      <v-echart :options="bindOption" style="width:100%;height:300px"></v-echart>
    </div>
    <div id="register" style="width:100%;height:400px">
      <div class="block">
        <span class="demonstration">微信注册用户数统计</span>
        <el-date-picker
          v-model="value2"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :picker-options="pickerOptions"
          value-format="yyyy-MM-dd"
          @change="changeRegDate"
        ></el-date-picker>
      </div>
      <v-echart :options="reOption" style="width:100%;height:300px"></v-echart>
    </div>
  </div>
</template>

<script>
import echarts from "echarts";
import Echarts from "vue-echarts/components/ECharts";
import { geneOptions } from "./data";
import { getUser, getTotal, getRegister, getBindDev } from "./api";
import wechat from "@/assets/wechat.png";
export default {
  data() {
    return {
      wechat,
      totalOption: {
        title: {
          x: "center",
          text: "设备总览"
        },
        tooltip: {
          trigger: "item"
        },
        calculable: true,
        grid: {
          borderWidth: 0,
          y: 80,
          y2: 60
        },
        xAxis: [
          {
            type: "category",
            show: false,
            data: ["设备总数", "在线设备数", "离线设备数", "已绑定设备数"]
          }
        ],
        yAxis: [
          {
            type: "value",
            show: false
          }
        ],
        series: [
          {
            name: "设备总览",
            type: "bar",
            itemStyle: {
              normal: {
                color: function(params) {
                  // build a color map as your need.
                  var colorList = [
                    "#C1232B",
                    "#27727B",
                    "#FE8463",
                    "#9BCA63",
                    "#FAD860",
                    "#F3A43B",
                    "#60C0DD",
                    "#D7504B",
                    "#C6E579",
                    "#F4E001",
                    "#F0805A",
                    "#26C0C0"
                  ];
                  return colorList[params.dataIndex];
                },
                label: {
                  show: true,
                  position: "top",
                  formatter: "{b}\n{c}"
                }
              }
            },
            data: []
          }
        ]
      },
      totalY: [],
      totalData: {
        totalCount: "",
        onlineCount: "",
        outlineCount: "",
        statusCount: ""
      },
      wxData: {
        userTotal: "",
        regTotal: ""
      },
      value1: null,
      value2: null,
      pickerOptions: {
        disabledDate(time) {
          let curDate = new Date().getTime();
          let three = 90 * 24 * 3600 * 1000;
          let threeMonths = curDate - three;
          return time.getTime() > Date.now() || time.getTime() < threeMonths;
        }
      },
      bindParams: {},
      bindX: [],
      bindY: [],
      bindOption: {},
      bindData: {
        statusNum: [],
        bindDate: []
      },
      reParams: {},
      reX: [],
      reY: [],
      reOption: {}
    };
  },
  mounted() {
    let totalNum = echarts.init(document.getElementById("totalNum"));
    this.getDevNum();
    setTimeout(() => {
      for (const key in this.totalData) {
        this.totalOption.series[0].data.push(this.totalData[key]);
      }
      totalNum.setOption(this.totalOption);
    }, 1000);
    this.getUserNum();
    this.bindOption = geneOptions(this.bindX, this.bindY);
    this.reOption = geneOptions(this.reX, this.reY);
  },
  methods: {
    getDevNum() {
      //获取设备总览数据
      getTotal().then(res => {
        if (res.ret === 0) {
          for (const key in this.totalData) {
            this.totalData[key] = res.datas[key];
          }
          let outlineNum = res.datas.totalCount - res.datas.onlineCount;
          this.totalData.outlineCount = outlineNum;
        }
      });
    },
    getUserNum() {
      //获得微信用户数据
      getUser().then(res => {
        if (res.ret === 0) {
          this.wxData.userTotal = res.datas.userTotal;
          this.wxData.regTotal = res.datas.regTotal;
        }
      });
    },
    changeBindDate() {
      let start = this.value1[0];
      let end = this.value1[1];
      this.bindParams.startTime = start;
      this.bindParams.endTime = end;
      //获取绑定设备数据
      getBindDev(this.bindParams).then(res => {
        console.log("bind", res);
        if (res.ret === 0) {
          this.analyzeData(res.datas);
        }
      });
    },
    changeRegDate() {
      let start = this.value2[0];
      let end = this.value2[1];
      this.reParams.startTime = start;
      this.reParams.endTime = end;
      //获取微信注册数据
      getRegister(this.reParams).then(res => {
        console.log("reg", res);
        if (res.ret === 0) {
          this.analyzeData2(res.datas);
        }
      });
    },
    analyzeData(bindData) {
      this.bindX = [];
      this.bindY = [];
      bindData.forEach(v => {
        this.bindX.push(v.date);
        this.bindY.push(v.statusCount);
      });
      this.bindOption = geneOptions(this.bindX, this.bindY);
    },
    analyzeData2(regData) {
      this.reX = [];
      this.reY = [];
      regData.forEach(v => {
        this.reX.push(v.countTime);
        this.reY.push(v.dailyNum);
      });
      this.reOption = geneOptions(this.reX, this.reY);
    }
  },
  components: {
    "v-echart": Echarts
  }
};
</script>

<style lang="less" scoped>
#box {
  display: flex;
  flex-wrap: wrap;
  #total {
    padding: 30px;
    margin-right: 80px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    display: flex;
    justify-content: center;
    .left {
      display: flex;
      flex-direction: column;
      margin-top: 100px;
      span {
        margin-bottom: 20px;
        color: #333;
        font-weight: 600;
      }
    }
  }
  #wechat {
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    flex: 1;
    padding: 30px;
    display: flex;
    align-items: center;
    justify-content: space-around;
    .right {
      display: flex;
      flex-direction: column;
      span {
        margin: 20px 0;
        color: #333;
        font-weight: 600;
      }
    }
  }
  #bindDev {
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    padding: 30px;
    margin-top: 30px;
    .block {
      display: flex;
      flex-direction: column;
      span {
        margin-bottom: 20px;
      }
    }
  }
  #register {
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    padding: 30px;
    margin-top: 30px;
    .block {
      display: flex;
      flex-direction: column;
      span {
        margin-bottom: 20px;
      }
    }
  }
}
</style>
