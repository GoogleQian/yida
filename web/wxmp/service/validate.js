const http = require('./http.js');
// const baseURL = 'https://10.0.1.231:443/';
//  const baseURL = 'http://10.0.1.223:7001/';
const baseURL = 'https://iotsvr.zsxiaohe.com/';

// 提交code
function getBindStatus(data) {
  const result = http.promiseRequest({
    url: `${baseURL}yida/applet/phone/bindingStatus`,
    method: 'GET',
    data: data
  })
  return result
}

// 获取验证码
function getValidateCode(data) {
  const result = http.promiseRequest({
    url: `${baseURL}yida/applet/phone/sendSms`,
    method: 'GET',
    data: data
  })
  return result
}

// 提交手机号
function postNumber(data) {
  const result = http.promiseRequest({
    url: `${baseURL}yida/applet/phone/validCode`,
    method: 'GET',
    data: data
  })
  return result
}
module.exports = {
  getValidateCode,
  postNumber,
  getBindStatus
}