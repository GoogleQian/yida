// 时间格式转换,接收时间戳
export function formatterTime(val) {
    function add0(m) {
        return m < 10 ? '0' + m : m
    }
    let y = val.getFullYear();
    let m = val.getMonth() + 1;
    let d = val.getDate();
    let h = val.getHours();
    var mm = val.getMinutes();
    var s = val.getSeconds();
    return y + add0(m) + add0(d) + add0(h) + add0(mm) + add0(s)
}