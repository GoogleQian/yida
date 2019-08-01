<template>
  <div class="box" style="position: relative">
    <input v-model="devCode" style="margin: 20rpx;padding: 20rpx;border-bottom: 1px solid #eee;font-size: 14px;" type="text" placeholder="请输入设备CID">
    <i-button type="primary" shape="circle" size="large" @click="bindDev">确定</i-button>
  </div>
</template>

<script>
import { bindDev } from "../api";
import store from "@/store/index";
const dataArr = [];
export default {
  data() {
    return {
      devCode: null
    };
  },
  onLoad() {
    wx.setNavigationBarTitle({
      title: "CID手动添加"
    });
  },
  methods: {
    bindDev() {
      bindDev({ devCode: this.devCode, userId: store.state.userId }).then(
        res => {
          store.commit("setDevCode", this.devCode);
          if (res.ret === 0) {
            // =0表明绑定指令下发成功，跳转至绑定loading页
            wx.navigateTo({
              url: `/pages/dev/binding/main?devCode=${this.devCode}`
            });
          } else {
            // 绑定指令下发失败，一般为校验到devCode不可用
            wx.showToast({
              title: res.msg,
              icon: "none",
              duration: 2000
            });
          }
        }
      );
    }
  },
  onHide() {},
  onLoad() {
    Object.assign(this.$data, this.$options.data());
    // fetch some data
    dataArr.push({ ...this.$data });
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
  justify-content: flex-start;
  flex-direction: column;
  height: 100vh;
}
.top {
  margin-top: 80px;
}
.bottom {
  margin-bottom: 50px;
}
.box .btn-default {
  background: #d4d3db !important;
}
</style>
