<template>
  <div class="panel" style="height: 100%;">
    <div class="left">
      <p class="total"><span>微信用户总数：</span><span class="value">{{datas.userTotal}}</span></p>
      <p class="online"><span>今日新增用户数：</span><span class="value">{{datas.regTotal}}</span></p>
    </div>
    <div class="right">
      <v-chart :options="options" :autoresize="true"/>
    </div>
  </div>
</template>

<script>
import Echarts from "vue-echarts";
import { geneOptions } from "./data";
export default {
  props: {
    api: {
      type: Function
    }
  },
  created() {
    this.api().then(res => {
      if (res.ret === 0) {
        console.log(res);
        this.datas = res.datas;
        this.analyzeData(res.datas);
      }
    });
  },
  methods: {
    analyzeData(data) {
      let _data = [];
      _data.push({ name: "活跃设备数", value: data.online });
      _data.push({ name: "离线设备数", value: data.offline });
      this.options = geneOptions(_data);
    }
  },
  data() {
    return {
      x: [1],
      y: [3],
      options: {},
      datas: []
    };
  },
  components: {
    "v-chart": Echarts
  }
};
</script>

<style scoped lang="less">
.panel {
  padding: 10px;
  display: flex;
  position: relative;
  justify-content: space-between;
  box-sizing: border-box;
  .left {
    position: absolute;
    .total,.online,.offline{
      .value{
        font-weight: 900;
      }
      font-size: 18px;
    }
  }
  .right {
    flex: 1;
  }
}
</style>
