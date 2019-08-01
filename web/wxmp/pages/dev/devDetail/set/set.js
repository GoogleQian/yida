// pages/dev/devDetail/set/set.js
const http = require('../../../../service/dev.js')
var app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    loading: false,
    devCode: null
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    this.setData({
      devCode: options.id
    })
  },
  callCamera() {
    const that = this;
    wx.scanCode({
      success(res) {
        // todo
        that.replaceFilter({
          filterCode: res.result,
          devCode: app.globalData.devCode
        });
        // http.activteDev(res.result);
      },
      fail(error) {
        console.log(error)
      }
    })
  },
  devUnbind() {
    const that = this
    wx.showModal({
      title: '确认解绑设备吗？',
      content: '解绑设备后，设备将从列表中移除。',
      success(res) {
        if (res.confirm) {
          http.unbindDev({
            id: that.data.devCode
          }).then(res => {
            if (res.data.ret === 0) {
              wx.showToast({
                title: '解绑成功',
                icon: 'none',
                duration: 1000,
                success: function() {
                  setTimeout(function() {
                    wx.switchTab({
                      url: '/pages/dev/dev'
                    })
                  }, 1000)
                }
              })
            } else {
              wx.showToast({
                title: res.msg,
                icon: 'none',
                duration: 2000
              })
            }
          })
        } else if (res.cancel) {
          console.log('用户点击取消')
        }
      }
    })
    // console.log(this.data.devCode)

  },
  replaceFilter(data) {
    http.replaceFilter(data).then(res => {
      if (res.data.ret === 0) {
        wx.showToast({
          title: '滤芯更换成功',
          icon: 'success',
          duration: 1000,
          success: function () {
            setTimeout(function () {
              wx.switchTab({
                url: '/pages/dev/dev'
              })
            }, 1000)
          }
        })
      } else {
        wx.showToast({
          title: res.data.msg,
          icon: 'none',
          duration: 2000,
          success: function() {}
        })
      }
    })
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function() {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function() {

  }
})