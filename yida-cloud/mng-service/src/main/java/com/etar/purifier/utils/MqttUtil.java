package com.etar.purifier.utils;

import entity.adverstising.Advertising;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ConstantUtil;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @author hzh
 * @date 2018/11/28
 */
public class MqttUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(MqttUtil.class);


    /**
     * 数字小于10返回01,02，03，否则返回本身字符串
     *
     * @param s 要操作的字符串
     */
    public static String strUnicodeLen(String s) {
        if (s == null || "".equals(s)) {
            return "00";
        }
        int length = 0;
        try {
            byte[] unicodeBytes = s.getBytes("Unicode");
            length = unicodeBytes.length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String change;
        //去掉unicode编码后的前两个字符长度
        length = length - 2;
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
        byte[] targets = new byte[4];
        targets[3] = (byte) (id);
        targets[2] = (byte) ((id >> 8) & 0xff);
        targets[1] = (byte) ((id >> 16) & 0xff);
        targets[0] = (byte) ((id >> 24) & 0xff);

        String res = new String(targets);

        String sendTime = DateUtil.format(new Date());
        String updateTime = DateUtil.format(ad.getUpdateTime());
        return prefix + sendTime + ConstantUtil.MQTT_COMMA + updateTime + ConstantUtil.MQTT_COMMA + res + ConstantUtil.MQTT_COMMA + MqttUtil.strUnicodeLen(ad.getTitle()) + ConstantUtil.MQTT_COMMA

                + MqttUtil.strUnicodeLen(ad.getSolgan()) + ConstantUtil.MQTT_COMMA + "@" + ad.getTitle() + ad.getSolgan();
    }


    /**
     * 组合下发时间
     *
     * @return 组合结果
     */
    public static String getTimeMsg() {
        return ConstantUtil.MQTT_TIME_PREFIX + DateUtil.format(new Date());
    }


    public static String getFirmwareMsg(String prefix, String ossUrl, int id) {
        int length = ossUrl.length();
        byte[] targets = new byte[4];
        targets[3] = (byte) (id);
        targets[2] = (byte) ((id >> 8) & 0xff);
        targets[1] = (byte) ((id >> 16) & 0xff);
        targets[0] = (byte) ((id >> 24) & 0xff);

        String res = new String(targets);
        return prefix + ConstantUtil.MQTT_VERSION + ConstantUtil.MQTT_COMMA + res + ConstantUtil.MQTT_COMMA + length + ConstantUtil.MQTT_COMMA + ossUrl;
    }

    public static void main(String[] args) {
        System.out.println(getFirmwareMsg(ConstantUtil.MQTT_FIRMWARE_BROADCAST_PREFIX, "http://localhost:7777/firmware/xxx.bin", 24));
    }
}
