<template>
  <div class="box" style="position: relative">
    <!-- <i-icon type="activity" size="28" color="#80848f" /> -->
    <span style="position: absolute;top: 10px;right:10px;padding: 0 10rpx;">
      <i-icon type="setup_fill" size="28" @click="goToSet" />
    </span>
    <div class="top">
      <p style="text-align:center; border-radius: 50%; border: 8px solid #f1f1f1;width: 300rpx;height: 300rpx;line-height: 300rpx;">
        <span style="font-size:70px;">{{filterLifeTime}}</span>%
      </p>
      <p style="text-align:center; color: #aaa;font-size: 12px; margin-top: 20rpx;">
        <span v-if="filterLifeTime > 10">滤芯不需要更换</span>
        <span v-if="filterLifeTime <= 10 && filterLifeTime > 0">滤芯寿命不足10%</span>
        <span v-if="filterLifeTime === 0">滤芯已损耗</span>
      </p>
    </div>
    <div class="bottom">
      <i-button type="primary" @click="goToBuyFilter()">立即购买</i-button>
      <p style="font-size: 12px; color: #aaa; text-align:center;" @click="goToActiveFilter()">激活滤芯</p>
    </div>
  </div>
</template>

<script>
import { buyFilter } from "./api";
import store from "@/store/index";
const dataArr = []
export default {
  data() {
    return {
      filterInfo: {}
    };
  },
  onLoad() {
    Object.assign(this.$data, this.$options.data());
    // fetch some data
    dataArr.push({ ...this.$data });
    wx.setNavigationBarTitle({
      title: store.state.devInfo.devCode //页面标题为路由参数
    });
    this.buyFilter(); // 获取购买滤芯页面地址
  },
  methods: {
    goToSet(){
      wx.navigateTo({
        url: `/pages/dev/detail/set/main`
      })
    },
    buyFilter() {
      // 获取够买滤芯页面地址
      buyFilter().then(res => {
        this.filterInfo = res.datas;
      });
    },
    goToBuyFilter() {
      //跳往购买滤芯页面
      store.commit("setUrl", this.filterInfo.url);
      wx.navigateTo({
        url: `/pages/web/main`
      });
    },
    goToActiveFilter() {
      console.log('hi')
      // 跳往激活滤芯页面
      if(this.filterLifeTime < 50){
        wx.navigateTo({
          //   url: `"/pages/dev/detail/main?devCode=" + item.devCode + "&filterLifeTime=" + item.filterLifeTime`
          url: `/pages/dev/getFilterCode/main?devCode=${
            store.state.devInfo.devCode
          }`
        });
      }else{
         wx.showToast({
            title: '滤芯寿命超过50%，无需更换',
            icon: "none",
            duration: 3000
          });
      }
    }
  },
  computed: {
    filterLifeTime() {
      return store.state.devInfo.filterLifeTime;
    }
  },
  onUnload() {
    dataArr.pop();
    const dataNum = dataArr.length;
    if (!dataNum) return;
    Object.assign(this.$data, dataArr[dataNum - 1]);
  }
};
</script>

<style>
.box {
  display: flex;
  justify-content: space-between;
  flex-direction: column;
  height: 100vh;
  background: #eee;
}
.top {
  background: #fff;
  padding: 100px 0;
  display: flex;
  align-items: center;
  justify-content: space-around;
  flex-direction: column;
}
.bottom {
  padding: 50px 0;
  padding-top: 0;
}
</style>
