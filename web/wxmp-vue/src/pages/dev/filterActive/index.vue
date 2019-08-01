<template>
  <div class="box" style="position: relative">
    <!-- 联网绑定，固定的倒计时60s然后设置type = 1 -->
    <div class="top" v-if="type === 0">
      <p style="color: #2b85e4;" v-if="timer!==0 && status===0"><span style=" font-weight: 400;font-size: 70px;">{{timer}}</span>s</p>
      <p v-if="status === 1">
        <img src="/static/img/dui.png" alt="" style="width: 120rpx;height:120rpx;">
      </p>
      <p style="color: #80848f;" v-if="status === 1">滤芯激活成功</p>
    </div>
    <!-- 蓝牙绑定 -->
    <div class="top" v-if="type === 1">
      <p class="ble_status" v-if="ble_status === 'seeking'">
        <img class="Rotation" style="width: 80rpx;height:80rpx;" src="/static/img/loading.png" alt="">
        <span>正在配对蓝牙，激活滤芯...</span>
        <span>请将手机靠近净水器</span>
      </p>
      <p class="ble_status" v-if="ble_status === 'ble_close'">
        <img src="/static/img/lanya.png" alt="">
        <span>请打开手机蓝牙后重试</span>
      </p>
      <p class="ble_status" v-if="ble_status === 'dev_close'">
        <img src="/static/img/cha.png" alt="">
        <span>未搜索到设备将手机靠近净水器</span>
      </p>
      <p class="ble_status" v-if="ble_status===1">
        <img src="/static/img/dui.png" alt="">
        <span>滤芯激活成功</span>
      </p>
    </div>
    <div class="bottom">
      <div>
        <!-- 蓝牙、失败 -->
        <i-button v-if="status === 2 && type === 1" shape="circle" @click="retry">重试</i-button>
        <i-button v-if="status === 1" @click="goToDev" shape="circle" style="width: 80%;">完成</i-button>
        <p v-if="status===2" @click="goToDev" style="font-size: 12px;">回到首页</p>
      </div>
    </div>
  </div>
</template>

<script>
// 该页面轮询绑定状态，超过60s或者接收到绑定失败时标记为绑定失败，然后开始蓝牙连接流程
import { formatterTime } from "@/utils/formatterTime";
import store from "@/store/index";
import { getBindStatus, getCommandContent, postBindSuccess } from "./api";
const dataArr = [];
export default {
  data() {
    return {
      status: 0, // 绑定状态，1成功，2失败，0绑定中
      timer: 30, // 倒计时
      type: 0, // 绑定方式 0为扫码绑定和cid绑定，1为蓝牙绑定
      s: null, // 定时器
      ble_status: 0, // 蓝牙状态，0：绑定失败，seeking：配对中，ble_close：蓝牙未开启，dev_close:未搜索到设备，1：绑定成功
      devices: [], // 用来存储YD设备
      device: {}, // 选中要连接的设备
      serviceId: "F000AA00-0451-4000-B000-000000000000", // 服务uuid
      characteristicId: "F000AA03-0451-4000-B000-000000000000", // 特征uuid
      sWaiting: null
    };
  },
  onLoad() {
    wx.setNavigationBarTitle({
      title: "滤芯激活" //页面标题为路由参数
    });
    // 轮询获取绑定状态，同时启动倒计时定时器
    Object.assign(this.$data, this.$options.data());
    // fetch some data
    dataArr.push({ ...this.$data });
    this.getBindStatus({ devCode: this.devCode });
    const _this = this;
    this.s = setInterval(function() {
      _this.timer > 0 ? _this.timer-- : 0;
      if (_this.timer === 0) {
        // 倒计时为0时，清除定时器并开始尝试蓝牙绑定
        console.log("开始尝试蓝牙绑定");
        _this.type = 1;
        clearInterval(_this.s);
      }
    }, 1000);
  },
  methods: {
    // 获取绑定状态
    getBindStatus({ devCode }) {
      getBindStatus({ devCode }).then(res => {
        if (res.ret !== 0) {
          // 接口出错，终止轮询
          wx.hideLoading();
          wx.showToast({
            title: res.msg,
            icon: "none",
            duration: 2000
          });
          this.status = 2;
        } else if (res.ret === 0) {
          wx.hideLoading();
          if (res.datas === 1) {
            // 绑定成功，终止轮询,并清除定时器
            clearInterval(this.s);
            this.status = 1;
          } else {
            // 绑定中
            // 暂停一秒后如果时间不为0则继续轮询，如果时间为0则停止轮询
            setTimeout(() => {
              if (this.timer === 0) {
                wx.hideLoading();
                // 开始尝试蓝牙绑定
                this.initBle();
              } else if (this.timer !== 0) {
                this.getBindStatus({ devCode });
              }
            }, 2000);
          }
        }
      });
    },
    // 向后台传递成功信号并修改页面状态
    postBindSuccess() {
      postBindSuccess({
        filterCode: this.filterCode,
        devCode: this.devCode // TODO
      }).then(res => {
        // 绑定成功，修改相关状态
        this.ble_status = 1;
        this.status = 1;
      });
    },
    // 初始化蓝牙,选中对应device
    async initBle() {
      console.log("开始初始化蓝牙");
      const _this = this;
      // 打开蓝牙适配器
      wx.openBluetoothAdapter({
        success(res) {
          console.log("蓝牙初始化完成，开始搜索周边设备");
          // 蓝牙打开，更改状态
          _this.ble_status = "seeking";
          // 开启成功，监听扫描
          _this.onFoundDevice();
          // 开始扫描
          _this.startSeekingDev();
          // 30s后关闭扫描，处理devices数据，若length为0，则未找到设备，将状态设置为dev_close,若length不为0，则与信号强度最高的设备建立连接
          setTimeout(async () => {
            _this.stopSeeking();
            // 判断是否有搜索到可用设备
            if (_this.devices.length === 0) {
              _this.ble_status = "dev_close";
              _this.status = 2;
            } else {
              let RSSI = -100;
              _this.devices.map(item => {
                RSSI = item.RSSI > RSSI ? item.RSSI : RSSI;
              });
              let [device] = _this.devices.filter(item => {
                return item.RSSI === RSSI;
              });
              console.log(device);
              _this.device = device;
              // 连接设备
              // 监听设备连接
              _this.onConnectDev();

              // 创建连接
              _this.createConnect();
            }
          }, 30000);
        },
        fail(err) {
          _this.status = 2;
          console.log("err", err);
          // 连接失败，提示"请打开蓝牙"
          _this.ble_status = "ble_close";
        }
      });
    },
    // 监听蓝牙设备特征值变化，当接收到成功标识时向后台发送成功信息(需要先启用notifyBLECharacteristicValueChange才能接收到设备推送的notification)
    // 过程分下发设备号+版本号、同步时间两步。监听到下发设备号+版本号成功后，开始下发同步时间命令  TODO
    onCharacterChange() {
      console.log("开始监听特征值变化");
      const _this = this;
      wx.onBLECharacteristicValueChange(function(res) {
        console.log("value change接收值转hex", _this.buf2hex(res.value));
        let msg = _this.buf2hex(res.value);
        let type = msg.split(",")[0]; // 信号类型(1: 下发设备号的响应，2: 设备绑定时时间同步的响应, 3: 激活滤芯时时间同步的响应)
        let status = msg.split(",")[1]; // 成功标识(1: 通讯成功，0:通讯失败)
        if (type == "1" && status == "1") {
          // 下发设备号成功，开始下发同步时间命令
          let time = formatterTime(new Date()); // 格式化时间
          _this.sendCommand("3," + time);
          console.log("同步时间成功222");
        } else if (type == "3") {
          // 监听到同步时间成功
          _this.postBindSuccess();
          console.log("同步时间成功222");
          if(status == "1"){
          _this.closeConnect();
        }
        }
         else {
          _this.status = 2;
          _this.ble_status = "dev_close";
        }

      });

    },
    // 创建连接
    createConnect() {
      const _this = this;
      console.log("选中设备", _this.device);
      wx.createBLEConnection({
        deviceId: _this.device.deviceId,
        success: function(res) {
          console.log("连接成功：", _this.device);
          // 获取所有服务
          _this.getServices();
        },
        fail: function(res) {
          console.log("connect error：" + res);
        }
      });
    },
    // 获取所有服务
    getServices() {
      const _this = this;
      wx.getBLEDeviceServices({
        deviceId: _this.device.deviceId,
        success: function(res) {
          console.log("所有服务", res);
          const services = res.services; // 服务
          // 根据服务id获取服务特征值
          _this.getCharacter(_this.serviceId);
        }
      });
    },
    // 获取某个服务的特征值
    getCharacter(uuid) {
      const _this = this;
      wx.getBLEDeviceCharacteristics({
        deviceId: _this.device.deviceId,
        serviceId: uuid,
        success: function(res) {
          console.log("获取到服务的特征值", res);
          const characters = res.characteristics;
          // 监听蓝牙设备特征值变化
          // 启用低功耗蓝牙设备特征值变化时的 notify 功能，订阅特征值.
          _this.notifyChangeOn();
          _this.onCharacterChange();
          // 向后台获取下发指令的参数并下发命令
          _this.getCommandContent();
        },
        fail: function(err) {
          console.log("扫描服务特征值失败", err);
        }
      });
    },
    // 下发命令
    sendCommand(val) {
      const _this = this;
      console.log("开始下发命令");
      setTimeout(() => {
        wx.writeBLECharacteristicValue({
          deviceId: _this.device.deviceId,
          serviceId: _this.serviceId,
          characteristicId: _this.characteristicId,
          value: _this.stringToHexBuffer(val),
          success(res) {
            console.log("下发成功", res);
          },
          fail(err) {
            console.log("下发失败", err);
          }
        });
      }, 2000);
      // 下发命令后如果10s内没有成功，则提示失败
      this.sWaiting = setTimeout(() => {
        if (this.status !== 1) {
          this.ble_status = "dev_close";
          this.status = 2;
        }
      }, 20000);
    },
    // 重试蓝牙绑定
    retry() {
      this.status = 0;
      this.ble_status = "seeking";
      this.initBle();
      clearTimeout(this.sWaiting);
    },
    // 监听发现设备
    onFoundDevice() {
      const _this = this;
      console.log("listen found device");
      wx.onBluetoothDeviceFound(res => {
        // 将name==='YD WaterPurifier'的设备推入devices
        if (res.devices[0].name === "YD WaterPurifier") {
          console.log("devices_RSSI", res.devices[0].RSSI);
          _this.devices.push(res.devices[0]);
        }
      });
    },
    // 监听连接设备
    onConnectDev() {
      wx.onBLEConnectionStateChanged(function(res) {
        console.log("connect state changed ", res);
        if (!res.connected) {
          console.log("disconnect");
        } else {
          console.log("connect");
        }
      });
    },
    // 开始扫描设备
    startSeekingDev() {
      console.log("start seeking");
      wx.startBluetoothDevicesDiscovery({
        allowDuplicatesKey: true,
        success: function(res) {}
      });
    },
    // 结束扫描
    stopSeeking() {
      wx.stopBluetoothDevicesDiscovery({
        success: function(res) {
          // success
        }
      });
    },
    // 启用低功耗蓝牙设备特征值变化的notify功能，订阅特征值
    notifyChangeOn() {
      const _this = this;
      // 启用低功耗蓝牙设备特征值变化时的 notify 功能，订阅特征值.
      wx.notifyBLECharacteristicValueChanged({
        deviceId: _this.device.deviceId,
        serviceId: _this.serviceId,
        characteristicId: _this.characteristicId,
        state: true, // 启用notify
        success: function(res1) {
          console.log("启动监听特征值功能", res1);
        },
        fail: function(err1) {
          console.log("启用特征值变化时订阅特征值失败", err1);
        }
      });
    },
    // 获取下发指令的内容
    async getCommandContent() {
      const _this = this;
      console.log("开始向后台获取指令内容,devcode:", this.devCode);
      await getCommandContent({ devCode: this.devCode }).then(res => {
        if (res.ret === 0) {
          console.log("指令参数", res);
          this.commandContent = res.datas;
          _this.sendCommand(_this.commandContent);
        }
      });
    },
    // 断开连接
    closeConnect() {
      wx.closeBluetoothAdapter({
        success:(res) =>{
          console.log("close success", res);
        },
        fail:(err)=> {
          console.log("断开失败", err);
        }
      });
    },
    goToDev() {
      wx.switchTab({
        url: "/pages/dev/main"
      });
    },
    // buffer转16进制字符串
    buf2hex: function(buffer) {
      // buffer is an ArrayBuffer
      let hex = Array.prototype.map
        .call(new Uint8Array(buffer), x => ("00" + x.toString(16)).slice(-2))
        .join("");
      return this.hexCharCodeToStr(hex);
    },
    // 16进制转字符串
    hexCharCodeToStr: function(hexCharCodeStr) {
      var trimedStr = hexCharCodeStr.trim();
      var rawStr =
        trimedStr.substr(0, 2).toLowerCase() === "0x"
          ? trimedStr.substr(2)
          : trimedStr;
      var len = rawStr.length;
      if (len % 2 !== 0) {
        alert("Illegal Format ASCII Code!");
        return "";
      }
      var curCharCode;
      var resultStr = [];
      for (var i = 0; i < len; i = i + 2) {
        curCharCode = parseInt(rawStr.substr(i, 2), 16); // ASCII Code Value
        resultStr.push(String.fromCharCode(curCharCode));
      }
      return resultStr.join("");
    },
    // 字符串转buffer
    stringToHexBuffer: function(data) {
      const hex = this.strToHexCharCode(data);
      var typedArray = new Uint8Array(
        hex.match(/[\da-f]{2}/gi).map(function(h) {
          return parseInt(h, 16);
        })
      );
      return typedArray.buffer;
    },
    // 字符串转16进制
    strToHexCharCode: function(str) {
      if (str === "") return "";
      var hexCharCode = [];
      hexCharCode.push("0x");
      for (var i = 0; i < str.length; i++) {
        hexCharCode.push(str.charCodeAt(i).toString(16));
      }
      return hexCharCode.join("");
    }
  },
  computed: {
    filterCode() {
      console.log(store.state.filterCode);
      return store.state.filterCode;
    },
    devCode() {
      return store.state.devInfo.devCode;
    }
  },
  onUnload() {
    clearInterval(this.s);
    clearInterval(this.sWaiting);
    this.s = null;
    this.timer = 0;
    this.sWaiting = null;
    dataArr.pop();
    const dataNum = dataArr.length;
    if (!dataNum) return;
    this.getBindStatus = null;
    Object.assign(this.$data, dataArr[dataNum - 1]);
  }
};
</script>

<style>
.box {
  display: flex;
  justify-content: space-between;
  flex-direction: column;
  height: 100vh;
  text-align: center;
}
.top {
  margin-top: 80px;
}
.ble_status {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.ble_status img {
  width: 120rpx;
  height: 120rpx;
  margin-bottom: 30rpx;
}
.bottom {
  margin-bottom: 50px;
}
@-webkit-keyframes rotation {
  from {
    -webkit-transform: rotate(0deg);
  }
  to {
    -webkit-transform: rotate(360deg);
  }
}

.Rotation {
  border-radius: 50%;
  -webkit-transform: rotate(360deg);
  animation: rotation 3s linear infinite;
  -moz-animation: rotation 3s linear infinite;
  -webkit-animation: rotation 3s linear infinite;
  -o-animation: rotation 3s linear infinite;
}
</style>
