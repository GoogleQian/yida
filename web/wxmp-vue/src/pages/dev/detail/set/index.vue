<template>
  <div class="box" style="position: relative">
    <i-cell title="解除绑定" is-link @click="unbindDev"></i-cell>
  </div>
</template>

<script>
import { unbindDev } from "./api";
import store from "@/store/index";
const dataArr = [];
export default {
  data() {
    return {
      modalShow: false
    };
  },
  methods: {
    unbindDev() {
      wx.showModal({
        title: "确认解绑设备吗",
        content: "解绑设备后，设备将从设备列表移除",
        success(res) {
          if (res.confirm) {
            unbindDev({ devCode: store.state.devInfo.devCode }).then(res => {
              console.log(res);
              if (res.ret === 0) {
                wx.showToast({
                  title: "解绑成功",
                  icon: "none",
                  duration: 2000
                });
                setTimeout(() => {
                  wx.switchTab({
                    url: `/pages/dev/main`
                  });
                }, 2000);
              } else {
                wx.showToast({
                  title: res.msg,
                  icon: "none",
                  duration: 2000
                });
              }
            });
          } else if (res.cancel) {
            wx.showToast({
              title: "取消解绑",
              icon: "none",
              duration: 2000
            });
          }
        }
      });
    }
  },
  onLoad() {
    wx.setNavigationBarTitle({
      title: "设置" //页面标题为路由参数
    });
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
  height: 100vh;
  background: #eee;
}
</style>
