//获取应用实例
const http = require('../../../service/dev.js')
var app = getApp()
Page({
  data: {
    inputValue: null
  },
  onLoad: function() {

  },
  bindKeyInput(e) {
    this.setData({
      inputValue: e.detail.value
    })
  },
  submit() {
    wx.showLoading({})
    this.bindDev()
  },
  bindDev() {
    app.globalData.devCode = this.data.inputValue;
    http.bindDev({
      devCode: this.data.inputValue,
      userId: app.globalData.userId
    }).then(res => {
      wx.hideLoading()
      if (res.data.ret === 0) {
        // 去绑定
        wx.navigateTo({
          url: './devbinding/devbinding'
        })
      } else {
        wx.showToast({
          title: res.data.msg,
          icon: 'none',
          duration: 2000
        })
      }
    })
      // http.activteDev(this.data.inputValue)
  }
})