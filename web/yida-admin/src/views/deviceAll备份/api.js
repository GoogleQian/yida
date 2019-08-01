import http from "@/service/http";

//系统总览
export function getTotal() {
  return http({
    url: "statics/device/total",
    method: "post"
  });
}

//微信用户
export function getUser() {
  return http({
    url: "/statics/user/totals",
    method: "get"
  });
}

//绑定设备数
export function getBindDev(params) {
  return http({
    url: "statics/device/days",
    method: "get",
    params
  });
}

// 注册用户数
export function getRegister(params) {
  return http({
    url: "/statics/user/days",
    method: "get",
    params
  });
}
