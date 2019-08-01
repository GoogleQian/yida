const util = require('../utils/util.js');

const promiseRequest = util.wxPromise(wx.request)

module.exports = {
  promiseRequest: promiseRequest
}