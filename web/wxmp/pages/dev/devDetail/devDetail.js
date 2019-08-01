// pages/dev/devDetail/devDetail.js
const http = require('../../../service/dev.js')
const App = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    devCode: null,
    filterLifeTime: '',
    filterUrl: null,
    filterCode: null
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    wx.setNavigationBarTitle({
      title: options.devCode  //页面标题为路由参数
    });
    console.log(options)
    this.setData({
      devCode: options.devCode,
      filterLifeTime: options.filterLifeTime,
      filterCode: options.filterCode || ''
    })
    this.getFilterUrl();
  },
  getFilterUrl(){
    http.getFilterList().then(res => {
      if(res.data.ret === 0 ){
        this.setData({
          filterUrl: res.data.datas.url
        })
      }
    })
  },
  gotoAd(event) {
    // 去看广告
    wx.navigateTo({
      url: `../../web/web?url=${event.target.dataset.url}`,
    })
  },
  gotoSet(){
    wx.navigateTo({
      url: `./set/set?id=${this.data.devCode}`
    })
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})