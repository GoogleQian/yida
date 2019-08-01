<template style="height: 100%;">
  <div class="container">
    <div class="banner" style="max-height: 40%; overflow: hidden;">
      <image mode="widthFix" :src="bannerInfo.imageUrl" @click="goToAd(bannerInfo.adUrl)"></image>
    </div>
    <!-- 设备列表 -->
    <div class="devList">
      <i-row>
        <i-col
          i-class="item"
          :span="12"
          v-for="item in devList"
          :key="item.id"
          @click="goDetail(item)"
        >
          <div class="box" style="padding: 10px;background: #fff;">
            <img
              class="icon"
              src="/static/img/720/wuxian-1.png"
              style="width: 10px;height: 10px;"
              v-if="item.online === 1"
            >
            <img
              class="icon"
              src="/static/img/720/wuxian-2.png"
              style="width: 10px;height: 10px;"
              v-if="item.online === 0"
            >
            <div class="lifeTime">
              <div>
                <span style="font-size: 40px;">{{item.filterLifeTime}}</span>%
              </div>
              <div class="text" v-if="item.filterLifeTime <= 10">换芯></div>
              <div class="text" v-if="item.filterLifeTime > 10">查看></div>
            </div>
            <div style="font-size: 14px;">{{item.devCode}}</div>
          </div>
        </i-col>
      </i-row>
    </div>
    <div class="bottom">
      <i-button type="primary" @click="scanAddDev">添加设备</i-button>
      <p style="text-align:center; font-size: 14px;" @click="cidAddDev">cid手动添加</p>
    </div>
  </div>
</template>

<script>
// 在当前页发起绑定设备命令，如果成功，则跳转至绑定状态页轮询绑定状态
import store from "@/store/index";
import { formatterTime } from "@/utils/formatterTime";
import {
  getBanner,
  getDevList,
  getUserInfo,
  bindDev,
  isBindPhone,
  saveUserinfo
} from "./api";
const dataArr = [];
export default {
  data() {
    return {
      bannerInfo: {},
      devList: [],
      bindPhoneStatus: false,
      wxUserInfo: {}
    };
  },
  onLoad() {
    Object.assign(this.$data, this.$options.data());
    // fetch some data
    dataArr.push({ ...this.$data });
    wx.login({
      // 登录，然后根据返回的code从后台获取userId
      success: async res => {
        await this.getUserInfo({ code: res.code }); // 获取用户id
        this.getWxUserInfo(); // 向微信获取用户信息提交至后台
        this.getDevList({ userId: store.state.userId }); // 获取设备列表
        // 回调向后台传递用户信息 TODO
      },
      fail(err) {
        wx.showToast({
          title: "有点小异常，请重新打开小程序",
          icon: "none",
          duration: 2000
        });
      }
    });
  },
  onShow() {
    store.dispatch("getSetting");
    // this.saveUserinfo(); // 向微信获取用户信息提交至后台
    this.getWxUserInfo();
    this.getDevList({ userId: store.state.userId }); // 获取设备列表
    this.getBanner(); // 每次进入重新获取banner图
  },
  methods: {
    getBanner() {
      // 获取banner图信息
      getBanner().then(res => {
        if (res.ret === 0) {
          this.bannerInfo = res.datas;
        }
      });
    },
    async getUserInfo({ code }) {
      // 获取用户信息
      await getUserInfo({ code }).then(res => {
        store.commit("setUserId", {
          userId: res.datas.userId,
          openId: res.datas.openid
        });
      });
    },
    getDevList(params) {
      // 若没有用户id，则不继续执行
      if (!params.userId) {
        return;
      }
      // 获取设备列表
      getDevList(params).then(res => {
        this.devList = res.datas.list;
      });
    },
    goToAd(adUrl) {
      // 跳转到广告页面
      store.commit("setUrl", adUrl);
      wx.navigateTo({
        url: `/pages/web/main`
      });
    },
    goDetail(item) {
      // 跳转到详情页面
      store.commit("setDevInfo", item);
      wx.navigateTo({
        url: `/pages/dev/detail/main?devCode=${item.devCode}&filterLifeTime=${
          item.filterLifeTime
        }`
      });
    },
    async scanAddDev() {
      await this.isBindPhone();
      if (this.bindPhoneStatus) {
        // 扫码添加新设备
        let reg = /^\d{15}$/;
        const _this = this;
        wx.scanCode({
          success(res) {
            setTimeout(() => {
              console.log('掉借口')
              _this.bindDev({
                devCode: res.result,
                userId: store.state.userId
              });
            }, 1000);
          },
          fail(error) {
            console.log(error);
            wx.showToast({
              title: '扫码失败',
              icon: 'none',
              duration: 2000
            })
          }
        });
      }
    },
    async cidAddDev() {
      // 跳往cid输入添加设备页
      await this.isBindPhone();
      if (this.bindPhoneStatus) {
        wx.navigateTo({
          url: `/pages/dev/cidipt/main`
        });
      }
    },
    // 是否已绑手机号
    async isBindPhone() {
      await isBindPhone({ userId: store.state.userId }).then(res => {
        if (res.ret === 0) {
          this.bindPhoneStatus = true;
        } else {
          this.bindPhoneStatus = false;
          // 未绑定
          wx.showToast({
            title: res.msg,
            icon: "none",
            duration: 2000
          });
          // 跳转去绑定手机号
          setTimeout(() => {
            wx.navigateTo({
              url: `/pages/dev/bind_phone/main`
            });
          }, 1000);
        }
      });
    },
    bindDev({ devCode, userId }) {
      // 全局存储devCode
      store.commit("setDevCode", devCode);
      bindDev({ devCode, userId }).then(
        res => {
          if (res.ret === 0) {
            // =0表明绑定指令下发成功，跳转至绑定loading页
            wx.navigateTo({
              url: `/pages/dev/binding/main?devCode=${devCode}`
            });
          } else {
            // 绑定指令下发失败，一般为校验到devCode不可用
            wx.showToast({
              title: res.msg,
              icon: "none",
              duration: 2000
            });
          }
        },
        () => {
          setTimeout(() => {
            wx.showToast({
              title: "设备编码有误！",
              icon: "none",
              duration: 2000
            });
          }, 1000);
        }
      );
    },
    // 从微信获取到用户信息
    getWxUserInfo() {
      const _this = this;
      wx.getUserInfo({
        success: function(res) {
          _this.wxUserInfo = res.userInfo;
          _this.saveUserinfo();
        }
      });
    },
    // 将用户信息提交到后台
    saveUserinfo() {
      const _this = this;
      let nickName = _this.wxUserInfo.nickName;
      let userId = store.state.userId;
      let openId = store.state.openId;
      if (!nickName || !openId || !userId) {
        return;
      }
      saveUserinfo({ nickName, userId, openId }).then(res => {
        console.log("提交微信用户信息成功", res);
      });
    }
  },
  computed: {
    userInfo() {
      return store.state.userInfo;
    }
  },
  onUnload() {
    dataArr.pop();
    const dataNum = dataArr.length;
    if (!dataNum) return;
    Object.assign(this.$data, dataArr[dataNum - 1]);
  },
  components: {}
};
</script>

<style>
.container {
  background: #eee;
  padding: 0 10rpx;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 100vh;
  width: 100%;
  overflow: hidden;
}
.banner {
  width: 100%;
  padding: 5px 20px;
  background: #fff;
}
.banner image {
  width: 100%;
}
.devList {
  width: 100%;
  height: 400rpx;
  margin-top: 20rpx;
  flex: 1 0 auto;
  overflow: auto;
}
.icon {
  position: absolute;
  top: 20rpx;
  right: 20rpx;
}
.bottom {
  width: 100%;
}
.item {
  border-radius: 6rpx;
  background: #eee;
  padding: 10rpx;
}
.item .box {
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
  background: #f0f0f7;
  border-radius: 20rpx;
  height: 350rpx;
  position: relative;
}
.lifeTime {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border: 5px solid #eee;
  border-radius: 50%;
  width: 270rpx;
  height: 270rpx;
  padding: 20rpx;
  box-sizing: border-box;
}
.lifeTime .text {
  font-size: 12px;
  color: #80848f;
  display: inline-block;
  padding: 10rpx;
  border: 1px solid #f1f1f1;
  border-radius: 6rpx;
}
.righttopimg {
  position: absolute;
  right: 20rpx;
  top: 20rpx;
}
</style>
