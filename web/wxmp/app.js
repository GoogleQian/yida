//app.js
const http = require('./service/dev.js')
App({
  onLaunch: function() {
    const that = this
    // 展示本地存储能力
    var logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)

    // 登录
    wx.login({
      success: res => {
        console.log(res)
        // 发送 res.code 到后台换取 openId, sessionKey, unionId
        http.getUserId(res.code).then(res => {
          console.log(res)
          if (res.data.ret === 0) {
            this.globalData.userId = res.data.datas.userId
            //由于这里是网络请求，可能会在 Page.onLoad 之后才返回
            // 所以此处加入 callback 以防止这种情况
            if (this.getUserIdCallback) {
              this.getUserIdCallback(this.globalData.userId);
            }
          } else {
            wx.showToast({
              title: '获取用户信息失败，请稍后重试，错误码：' + res.data.ret,
              icon: 'none',
              duration: 2000
            })
          }
        }, err => {
          wx.showToast({
            title: '接口请求出错，请稍后重试，错误码：' + res.data.ret,
            icon: 'none',
            duration: 2000
          })
        })
      }
    })
    // 获取用户信息
    console.log('获取用户信息')
    wx.getSetting({
      success: res => {
        if (res.authSetting['scope.userInfo']) {
          // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
          wx.getUserInfo({
            success: res => {
              // 可以将 res 发送给后台解码出 unionId
              this.globalData.userInfo = res.userInfo
              // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
              // 所以此处加入 callback 以防止这种情况
              if (this.userInfoReadyCallback) {
                this.userInfoReadyCallback(res)
              }
            }
          })
          console.log('已授权')
          // 已经授权直接跳至列表页面
          wx.switchTab({
            url: '/pages/dev/dev',
          })
        } else {
          console.log('未授权')
          this.globalData.authUserInfo = false;
          wx.switchTab({
            url: '/pages/index/index'
          })
        }
      }
    })
  },
  globalData: {
    userInfo: null,
    userId: null,
    authUserInfo: false
  }
})