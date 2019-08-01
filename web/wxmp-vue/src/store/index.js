// https://vuex.vuejs.org/zh-cn/intro.html
// make sure to call Vue.use(Vuex) if using a module system
import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = new Vuex.Store({
  state: {
    count: 0,
    userInfo: {}, // 微信账号信息
    hasUserInfo: false,
    url: '',  // webview地址
    userId: '', // 用户id
    openId: '', // openId
    devInfo: {},  // 设备信息
    devCode: null,  // 全局存储devCode
    filterCode: null,  // 全局存储filterCode
  },
  mutations: {
    increment: (state) => {
      const obj = state
      obj.count += 1
    },
    decrement: (state) => {
      const obj = state
      obj.count -= 1
    },
    setUrl: (state, url) => {
      state.url = url
    },
    setUserId: (state, {userId, openId}) => {
      state.userId = userId;
      state.openId = openId;
    },
    setDevInfo: (state, devInfo) => {
      console.log('setDevInfo')
      state.devInfo = devInfo
    },
    setDevCode: (state, devCode) => {
      state.devCode = devCode
    },
    setFilterCode: (state, filterCode) => {
      state.filterCode = filterCode
    }
  },
  actions: {
    getSetting({ commit, state }) {
      wx.getSetting({
        success: function (res) {
          if (res.authSetting['scope.userInfo']) {
            wx.getUserInfo({
              success: function (res) {
                //用户已经授权过
                console.log('用户已经授权过')
                state.userInfo = res.userInfo;
                console.log('userinfo', res.userInfo)
                state.hasUserInfo = true;
                // 将userinfo发送给后台更新  TODO
                const url = "/pages/dev/main";
                wx.switchTab({ url });
              }
            })
          } else {
            console.log('用户还未授权过')
            const url = "/pages/center/main";
            wx.switchTab({ url });
          }
        }
      })
    }
  }
})

export default store
