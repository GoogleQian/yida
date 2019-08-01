package com.etar.purifier.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.etar.purifier.modules.advertising.entity.Advertising;
import com.etar.purifier.modules.dev.entiy.DevConnInfo;
import com.etar.purifier.modules.dev.entiy.Device;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @author hzh
 * @date 2018/11/28
 */
public class MqttUtil {
    /**
     * 获取字符传长度
     *
     * @param str 要操作的字符串
     */
    private static int getLength(String str) {
        int m = 0;
        char arr[] = str.toCharArray();
        for (char c : arr) {
            // 中文字符(根据Unicode范围判断),中文字符长度为2
            if ((c >= 0x0391 && c <= 0xFFE5)) {
                m = m + 2;
                // 英文字符
            } else if (c <= 0x00FF) {
                m = m + 1;
            }
        }
        return m;
    }

    /**
     * 数字小于10返回01,02，03，否则返回本身字符串
     *
     * @param s 要操作的字符串
     */
    private static String strLen(String s) {
        int length = getLength(s);
        String change;
        if (length < ConstantUtil.STRING_LENGTH) {
            change = "0" + length;
        } else {
            change = String.valueOf(length);
        }
        return change;
    }

    /**
     * 组合下发广告类容
     *
     * @param ad 广告实体类
     * @return 组合结果
     */
    public static String getAdvMsg(Advertising ad, String prefix) {
        if (ad == null) {
            return null;
        }

        int id = ad.getId();

        String idStr = String.format("%0" + 4 + "d", id);

        byte[] targets = new byte[4];
        targets[3] = (byte) (id);
        targets[2] = (byte) ((id >> 8) & 0xff);
        targets[1] = (byte) ((id >> 16) & 0xff);
        targets[0] = (byte) ((id >> 24) & 0xff);

        String res = new String(targets);

        return prefix + res + ConstantUtil.MQTT_COMMA + MqttUtil.strLen(ad.getTitle()) + ConstantUtil.MQTT_COMMA

                + MqttUtil.strLen(ad.getSolgan()) + ConstantUtil.MQTT_COMMA + ad.getTitle() + ad.getSolgan();
    }


    /**
     * 组合下发时间
     *
     * @return 组合结果
     */
    public static String getTimeMsg() {
        return ConstantUtil.MQTT_TIME_PREFIX + DateUtil.format(new Date());
    }

    /**
     * 组合时间
     *
     * @return 组合结果
     */
    public static String getActiveTime() {
        return ConstantUtil.MQTT_ACTIVE_PREFIX + DateUtil.format(new Date());
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在后，高位在前)的顺序。
     */
    public static int bytesToInt(byte[] src, int offset) {
        byte[] targetByte = new byte[4];
        System.arraycopy(src, 0, targetByte, 0, 4);
        int value;
        value = (((targetByte[offset] & 0xFF) << 24)
                | ((targetByte[offset + 1] & 0xFF) << 16)
                | ((targetByte[offset + 2] & 0xFF) << 8)
                | (targetByte[offset + 3] & 0xFF));
        return value;
    }


    public static void main(String[] args) {
        Advertising ad = new Advertising();
        ad.setTitle("aaa");
        ad.setName("asdad");
        ad.setSolgan("ssdfsf");
        String advMsg = ConstantUtil.MQTT_ADV_PREFIX_BROADCAST + MqttUtil.strLen(ad.getTitle()) + ConstantUtil.MQTT_COMMA + MqttUtil.strLen(ad.getSolgan()) + ConstantUtil.MQTT_COMMA + ad.getName() + ad.getSolgan();

        System.out.println(advMsg);
        System.out.println(getTimeMsg());
        System.out.println(getActiveTime());
    }

    /**
     * 获取设备在线状态
     *
     * @param device 设备信息
     */
    public static void getDevOnlineStatus(Device device, String connectUrl, String appid, String appsecret) {
        JSONObject jsonObject = HttpClientUtil.doGet(connectUrl + device.getDevCode(), null, appid, appsecret);
        if (jsonObject != null) {
            JSONArray jsonObject1 = jsonObject.getJSONArray("data");
            if (jsonObject1 != null && jsonObject1.size() != 0) {
                DevConnInfo devConnInfo = JSONObject.parseObject(jsonObject1.get(0).toString(), DevConnInfo.class);
                device.setOnline(device.getDevCode().equals(devConnInfo.getClientId()) ? 1 : 0);
                device.setIp(devConnInfo.getIpaddress());
                device.setConnectTime(devConnInfo.getConnectedAt());
            } else {
                device.setOnline(0);
                device.setIp(null);
                device.setConnectTime(null);
            }
        }
    }
}
