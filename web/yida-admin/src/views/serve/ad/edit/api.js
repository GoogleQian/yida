import http from "@/service/http";
// 根据特定条件查询
export function getDev(params) {
  return http({
    url: `/yida/dev/search/msg`,
    method: "get",
    params
  });
}

//推送
export function pushAd(data) {
  return http({
    url: `/yida/ad/send/dev`,
    method: "post",
    data
  });
}
