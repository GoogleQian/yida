<template>
  <div>
    <div class="hasuserinfo" v-if="hasUserInfo">
      <image class="userinfo-avatar" :src="userInfo.avatarUrl" style="width: 64px;height: 64px;border-radius: 50%; margin: 40rpx;"></image>
      <span class="userinfo-nickname">{{userInfo.nickName}}</span>
    </div>
    <div v-if="!hasUserInfo">
      <div size="large" style="width: 200rpx;height: 200rpx; border-radius: 50%;background:#b2b2b2;margin: 60rpx auto;"></div>
      <div style='padding: 40rpx 0; margin: 40rpx; border-top: 1px solid #d3d4d5;'>
        <div class='text'>
          <div style='font-size: 17px;color: #555555;'>申请获取以下权限</div>
          <div style='font-size: 14px;color: #888888;margin-top: 10px;'>获取您的公开信息(昵称、头像等）</div>
        </div>
        <button style="margin-top: 40rpx;border-radius: 30px;" class='btn' type='primary' open-type="getUserInfo" @getuserinfo="bindGetUserInfo" @click="getUserInfo1">授权登录 </button>
        <!-- <i-button type="success" open-type="getUserInfo" @getuserinfo="bindGetUserInfo" @click="getUserInfo1">获取权限</i-button> -->
      </div>
    </div>
  </div>
</template>

<script>
import store from "@/store/index";
const dataArr = [];
export default {
  data() {
    return {
      logs: []
    };
  },
  methods: {
    bindGetUserInfo(e) {
      // console.log(e.mp.detail.rawData)
      if (e.mp.detail.rawData) {
        //用户按了允许授权按钮
        console.log("用户按了允许授权按钮");
        store.dispatch("getSetting");
      } else {
        //用户按了拒绝按钮
        console.log("用户按了拒绝按钮");
      }
    }
  },
  created() {
    console.log(store);
    // wx.getUserInfo({
    //   success: function(res){
    //     console.log(res)
    //   },
    //   fail:function(err){
    //     console.log(err)
    //   }
    // })
  },
  computed: {
    userInfo() {
      console.log(store.state.userInfo);
      return store.state.userInfo;
    },
    hasUserInfo() {
      return store.state.hasUserInfo;
    }
  },
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
.hasuserinfo {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  height: 104px;
}
</style>
