<template>
  <div class="box" style="text-align: center;">
    <div class="icon">
      <img :src="icon" alt="">
      <p style="font-size:12px;margin-top: 10px;">{{msg}}</p>
    </div>
    <div class="btn">
      <i-button @click="callCamera(btnText)" shape="circle" type="ghost" size="large">{{btnText}}</i-button>
      <p @click="goHome" style="font-size: 12px;">回到首页</p>
    </div>
  </div>
</template>

<script>
import store from "@/store/index";
import { bindFilter, validFilterCode } from "./api";
const dataArr = [];
export default {
  data() {
    return {
      icon: "/static/img/lvxin.png",
      msg: "扫描滤芯二维码激活",
      btnText: "扫码激活",
      filterCode: ""
    };
  },
  methods: {
    callCamera(btnText) {
      const _this = this;
      if (btnText === "扫码激活" || btnText === "重试") {
        // 打开扫码
        wx.scanCode({
          success(res) {
            console.log(store.state);
            // 判断滤芯编号合法性
            _this.filterCode = res.result;
            _this.validFilterCode({ filterCode: res.result });
            store.commit("setFilterCode", res.result);
          },
          fail(error) {
            console.log(error);
          }
        });
      } else if (btnText === "立即激活") {
        // 点击激活先发送激活命令，然后跳转绑定页轮询绑定结果
        _this.bindFilter({
          devCode: _this.devCode,
          filterCode: _this.filterCode
        });
      }
    },
    validFilterCode({ filterCode }) {
      validFilterCode({
        filterCode,
        userId: store.state.userId,
        devCode: store.state.devInfo.devCode
      }).then(res => {
        console.log(res);
        if (res.ret === 0) {
          // 滤芯编号可用则提示
          this.btnText = "立即激活";
          this.icon = "/static/img/lvxin.png";
          this.msg = "使用新滤芯，激活成功后二维码将失效";
        } else {
          this.btnText = "重试";
          this.icon = "/static/img/cha.png";
          wx.showToast({
            title: res.msg,
            icon: "none",
            duration: 1500
          });
          if (res.ret === 10001) {
            this.msg = "滤芯编号不存在";
          } else if (res.ret === 10005) {
            this.msg = "滤芯已被使用";
          }
        }
      });
    },
    bindFilter(params) {
      // 绑定
      bindFilter(params).then(res => {
        console.log(res);
        const _this = this;
        if (res.ret !== 0) {
          wx.showToast({
            title: res.msg,
            icon: "none",
            duration: 3000,
            success: function() {}
          });
          this.btnText = "重试";
          this.msg = res.msg;
        } else if (res.ret === 0) {
          //
          wx.showToast({
            title: "指令下发成功",
            icon: "none",
            duration: 3000,
            success: function() {
              wx.navigateTo({
                url: `/pages/dev/filterActive/main?filterCode=${
                  _this.filterCode
                }`
              });
            }
          });
        }
      });
    },
    goHome() {
      // 回到首页
      wx.switchTab({
        url: "/pages/dev/main"
      });
    }
  },
  computed: {
    devCode() {
      return store.state.devInfo.devCode;
    }
  },
  onLoad() {
    wx.setNavigationBarTitle({
      title: "绑定设备" //页面标题为路由参数
    });
    console.log("getFilter,page onload");
    this.callCamera("扫码激活");
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
  flex-direction: column;
  /* align-items: center; */
  height: 100vh;
  justify-content: space-between;
}
.btn {
  width: 100%;
  margin-bottom: 80rpx;
}
.icon {
  margin-top: 80px;
}
.icon img {
  width: 200rpx;
  height: 200rpx;
}
.box .btn-default {
  background: #d4d3db !important;
}
</style>
