//获取应用实例
const http = require('../../../../service/dev.js')
var app = getApp()
Page({
  data: {
    Countdown: 10,
    s1: null,
    s2: null,
    bindStatus: false,
    bindText: '设备绑定成功',
    result: 'duihao.png'
  },
  onLoad: function() {
    this.bindDev()
  },
  bindDev(){
    this.setData({
      Countdown: 10,
      s1: null,
      bindStatus: false,
      bindText: '设备绑定成功',
      result: 'duihao.png',
      s2: setInterval(() => {
        this.setData({
          Countdown: this.data.Countdown - 1
        })
        console.log(this.data.Countdown)
        if (this.data.Countdown === 0) {
          // 倒计时结束，显示设备绑定失败
          this.setData({
            bindStatus: true,
            bindText: '设备绑定失败',
            result: 'ku.png'
          })
          clearInterval(this.data.s2);
          clearInterval(this.data.s1);
        }
      }, 1000)
    })

    // 轮询获取最新绑定状态，已返回则标记为绑定成功
    this.getStatus()
    this.setData({
      s1: setInterval(() => {
        this.getStatus();
      }, 3000)
    })
    // this.data.s1 = setInterval(() => {
    //   this.getStatus();
    // }, 3000)
  },
  goDevList() {
    wx.switchTab({
      url: '/pages/dev/dev'
    })
  },
  getStatus() {
    http.getBindStatus(app.globalData.devCode).then(res => {
      console.log(res.data.datas)
      if (res.data.datas === 1) {
        clearInterval(this.data.s1)
        clearInterval(this.data.s2)
        this.setData({
          Countdown: 0,
          s1: null,
          s2: null,
          bindStatus: true
        })
      }
    })
  }
})