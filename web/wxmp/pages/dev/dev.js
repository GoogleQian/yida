const http = require('../../service/dev.js');
const http1 = require('../../service/validate.js');
//获取应用实例
var app = getApp()
Page({
  data: {
    devList: [],
    imgUrls: [
      'http://img02.tooopen.com/images/20150928/tooopen_sy_143912755726.jpg',
      'http://img06.tooopen.com/images/20160818/tooopen_sy_175866434296.jpg',
      'http://img06.tooopen.com/images/20160818/tooopen_sy_175833047715.jpg'
    ],
    bannerInfo: {
      imageUrl: '../../common/img/banner.png'
    },
    bindStatus: false
  },
  onShow() {
    // 获取用户信息
    // 检查是否已授权，若未授权则跳转至授权页，若已授权则请求list
    if (app.globalData.userInfo) {
      this.getList()
      this.getBanner()
    } else {
      wx.switchTab({
        url: '../index/index'
      })
    }
  },
  callCamera() {
    // this.getBindStatus();
    // return;
    const that = this;
    wx.scanCode({
      success(res) {
        console.log(res)
        app.globalData.devCode = res.result;
        that.bindDev({
          devCode: res.result,
          userId: app.globalData.userId
        });
        // http.activteDev(res.result);
      },
      fail(error) {
        console.log(error)
      }
    })
  },
  bindDev(data) {
    // wx.showLoading({})
    http.bindDev(data).then(res => {
      console.log(res)
      // wx.hideLoading()
      if (res.data.ret === 0) {
        wx.navigateTo({
          url: './iptCid/devbinding/devbinding'
        })
      } else {
        console.log('errorrrrrrrrrrrr')
        wx.showToast({
          title: res.data.msg,
          icon: 'none',
          duration: 2000
        })
      }
    });
  },
  getBanner(data) {
    http.getBannerList().then(res => {
      if (res.data.ret === 0) {
        this.setData({
          bannerInfo: res.data.datas || '../../common/img/banner.png'
        })
      }
    })
  },
  iptCid() {
    // 点击前往CID手动添加页面
    wx.navigateTo({
      url: '/pages/dev/iptCid/iptCid'
    })
  },
  getList() {
    if (app.globalData.userId) {
      // 已写入userId则直接获取
      // this.getList(app.globalData.userId)
      // wx.showLoading({})
      http.getDevList({
        userId: app.globalData.userId
      }).then(res => {
        // wx.hideLoading()
        this.setData({
          devList: res.data.datas.list
        })
      })
    } else {
      // 未写入userId则设置回调函数以在ajax回调函数里调用
      app.getUserIdCallback = (userId) => {
        // this.getList(userId)
        // wx.showLoading({})
        http.getDevList({
          userId: app.globalData.userId
        }).then(res => {
          // wx.hideLoading()
          this.setData({
            devList: res.data.datas.list
          })
        })
      }
    }
  },
  goDevDetail(event) {
    // 跳往详情页面
    app.globalData.devCode = event.currentTarget.dataset.devcode;
    wx.navigateTo({
      url: `./devDetail/devDetail?devCode=${event.currentTarget.dataset.devcode}&filterLifeTime=${event.currentTarget.dataset.filterlifetime}&filterCode=${event.currentTarget.dataset.filtercode}`,
    })
  },
  gotoAd(event) {
    // 去看广告
    wx.navigateTo({
      url: `../web/web?url=${event.target.dataset.url}`,
    })
  },
  // 不同类型输入执行不同逻辑
  getBindStatus0(){
    this.getBindStatus(0)
  },
  getBindStatus1(){
    this.getBindStatus(1)
  },
  getBindStatus(typ) {
    // 获取绑定状态
    http1.getBindStatus({
      userId: app.globalData.userId
    }).then(res => {
      console.log(res)
      // 已绑定，若typ === 0 执行扫码输入，typ === 1执行ipt输入
      if (res.data.ret === 0) {
        // 已绑定
        if(typ === 0){
          this.callCamera();
        }else{
          this.iptCid()
        }
      } else {
        // 未绑定
        wx.showToast({
          title: res.data.msg,
          icon: 'none',
          duration: 2000,
        })
        // 跳转去绑定手机号
        setTimeout(() => {
          wx.navigateTo({
            url: `/pages/valide_code/validate_code`
          })
        }, 1000)
        this.setData({
          bindStatus: false
        })
      }
    })
  }

})