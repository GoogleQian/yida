const http = require('./http.js');
// const baseURL = 'https://10.0.1.231:443/';
//  const baseURL = 'http://10.0.1.223:7001/';
const baseURL = 'https://iotsvr.zsxiaohe.com/';

// 提交code
function getUserId(data) {
  const result = http.promiseRequest({
    url: `${baseURL}yida/applet/info/${data}`
  })
  return result
}

// 提交用户信息
function postUserInfo(data) {
  const result = http.promiseRequest({
    url: `${baseURL}yida/applet/nick`,
    method: 'POST',
    data: data
  })
  return result
}

// 获取设备列表数据
function getDevList(data) {
  const result = http.promiseRequest({
    url: `${baseURL}yida/applet/list`,
    data: { ...data,
      page: 1,
      pageSize: 999
    }
  })
  return result;
}

// 绑定新设备
function bindDev(data) {
  const result = http.promiseRequest({
    url: `${baseURL}yida/applet/bind/${data.devCode}/${data.userId}`
  })
  return result;
}

// 解绑设备
function unbindDev({id}) {
  const result = http.promiseRequest({
    url: `${baseURL}yida/applet/unbind/${id}`
  })
  return result;
}

// 获取设备绑定状态
function getBindStatus(data){
  const result = http.promiseRequest({
    url: `${baseURL}yida/applet/status/${data}`
  })
  return result
}

// 激活设备
function activteDev(data) {
  console.log('激活设备');
  const result = http.promiseRequest({
    url: `${baseURL}yida/applet/activate/${data}`
  })
  return result
}
// 获取上架的banner
function getBannerList(data) {
  const result = http.promiseRequest({
    url: `${baseURL}yida/applet/banners`
     })
  return result;
}

// 获取上架的滤芯广告
function getFilterList(data) {
  const result = http.promiseRequest({
    url: `${baseURL}yida/applet/filters` 
  })
  return result;
}

// 更换滤芯
function replaceFilter(data){
  const result = http.promiseRequest({
    url: `${baseURL}yida/applet/modify`,
    data
  })
  return result
}
module.exports = {
  getUserId,
  getDevList,
  bindDev,
  postUserInfo,
  getBindStatus,
  getBannerList,
  getFilterList,
  activteDev,
  unbindDev,
  replaceFilter
}