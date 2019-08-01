<template>
  <div class="content">
    <i-input i-class="phone_input" :value="phone" autofocus placeholder="请输入手机号" maxlength="11" @change="inputChange($event, 'phone')" />
    <i-row i-class="code_input">
      <i-col :span="18">
        <i-input :value="code" placeholder="请输入验证码" maxlength="6" @change="inputChange($event, 'code')"></i-input>
      </i-col>
      <i-col :span="6">
        <div class="text" @click="getCode">{{text}}</div>
      </i-col>
    </i-row>
    <i-button @click="postPhone">提交</i-button>
  </div>
</template>

<script>
import store from "@/store";
import { getCode, postPhone } from "./api";
export default {
  data() {
    return {
      phone: null,
      code: null,
      text: "获取验证码",
      open1: true
    };
  },
  onLoad() {},
  methods: {
    inputChange(val, typ) {
      if (typ === "phone") {
        this.phone = val.target.detail.value;
      } else {
        this.code = val.target.detail.value;
      }
    },
    //   获取验证码
    getCode() {
      if (this.open1) {
        if (this.isPhoneNumber(this.phone)) {
          getCode({ phone: this.phone }).then(res => {
            if (res.ret === 0) {
              // 禁用按钮并开始倒计时
              this.startCountDown();
            } else if (res.ret !== 0) {
              wx.showToast({
                title: res.msg,
                icon: "none",
                duration: 2000
              });
            }
          });
        }
      }
    },
    // 提交手机号
    postPhone() {
      if (!this.isPhoneNumber(this.phone)) {
        wx.showToast({
          title: "请正确填写手机号",
          icon: "none",
          duration: 1000
        });
        return;
      }
      if (this.code === null) {
        wx.showToast({
          title: "验证码不能为空",
          icon: "none",
          duration: 1000
        });
        return;
      }
      postPhone({
        phone: this.phone,
        code: this.code,
        userId: store.state.userId
      }).then(res => {
        if (res.ret === 0) {
          // 提交成功
          wx.showToast({
            title: "绑定成功",
            icon: "none",
            duration: 2000
          });
          wx.switchTab({
            url: "/pages/dev/main"
          });
        } else {
          // 提交失败
          wx.showToast({
            title: res.msg,
            icon: "none",
            duration: 2000
          });
        }
      });
    },
    startCountDown() {
      let timer = 30;
      this.text = `${timer}s`;
      this.open1 = false;
      let s = setInterval(() => {
        if (timer > 0) {
          this.text = `${--timer}s`;
        } else {
          clearInterval(s);
          this.text = "重试";
          this.open1 = true;
        }
      }, 1000);
    },
    isPhoneNumber(phone) {
      var myreg = /^[1][3,4,5,6,7,8,9][0-9]{9}$/;
      if (!myreg.test(phone)) {
        wx.showToast({
          title: "手机号格式有误",
          icon: "none",
          duration: 1000
        });
        return false;
      } else {
        return true;
      }
    }
  },
  components: {}
};
</script>

<style>
.content {
  height: 100vh;
  background: #f8f8f9;
  padding: 10rpx 0;
}
.phone_input {
}
.code_input {
  margin: 10rpx 0;
}
.text {
  height: 48.25px;
  line-height: 48px;
  font-size: 14px;
  padding: 10rpx;
  box-sizing: border-box;
  color: #ed3f14;
}
</style>
