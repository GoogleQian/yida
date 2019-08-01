// pages/valide_code/validate_code.js
const http1 = require('../../service/validate.js');
var app = getApp()
Page({
  /**
   * 页面的初始数据
   */
  data: {
    phone: null, // 手机号表单
    code: null, // 验证码表单
    text: '获取验证码', // 文本
    disabled1: false, // 获取验证码按钮
    disabled2: true, // 提交验证码按钮
    loading: false,
  },
  getValidateCode() {
    // 获取验证码
    // 先验证手机号
    if (!this.isPhoneNumber(this.data.phone)) {
      wx.showToast({
        title: '手机号格式有误',
        icon: 'none',
        duration: 2000
      })
      return;
    }
    // 开始获取验证码，禁用获取验证码按钮
    this.setData({
      disabled1: true
    })
    this.startCountDown()
    http1.getValidateCode({
      phone: this.data.phone
    }).then(res => {
      console.log(res)
      if (res.data.ret === 0) {
        // 获取验证码成功解禁提交按钮
        this.setData({
          disabled2: false
        })
      }else{
        wx.showToast({
          title: res.data.msg,
          icon: 'none',
          duration: 2000
        })
      }
    })
  },
  postCode() {
    // 提交手机号和验证码
    // 校验手机号是否正确，验证码是否为空
    if (!this.isPhoneNumber(this.data.phone)) {
      wx.showToast({
        title: '请正确填写手机号',
        icon: 'none',
        duration: 1000
      })
      return
    }
    if (this.data.code === null) {
      wx.showToast({
        title: '验证码不能为空',
        icon: 'none',
        duration: 1000
      })
      return
    }
    this.setData({
      loading: true
    })
    http1.postNumber({
      userId: app.globalData.userId,
      code: this.data.code,
      phone: this.data.phone
    }).then(res => {
      this.setData({
        loading: false
      })
      console.log(res)
      if (res.data.ret === 0) {
        // 提交成功
        wx.showToast({
          title: '绑定成功',
          icon: 'none',
          duration: 2000
        })
        wx.switchTab({
          url: '/pages/dev/dev',
        })
      } else {
        // 提交失败
        wx.showToast({
          title: res.data.msg,
          icon: 'none',
          duration: 2000
        })
        this.setData({
          disabled2: true
        })
      }
    })
  },
  startCountDown() {
    let timer = 60;
    this.setData({
      text: '60s后重试'
    })
    let s = setInterval(() => {
      if (timer > 0) {
        this.setData({
          text: `${--timer}s后重试`
        })
      } else {
        clearInterval(s);
        this.setData({
          text: '重试',
          disabled1: false
        })
      }
    }, 1000)
  },
  bindKeyInput(e) {
    this.setData({
      phone: e.detail.value
    })
  },
  bindCodeInput(e) {
    this.setData({
      code: e.detail.value
    })
  },
  isPhoneNumber(phone) {
    var myreg = /^[1][3,4,5,7,8][0-9]{9}$/;
    if (!myreg.test(phone)) {
      return false;
    } else {
      return true;
    }
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {

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