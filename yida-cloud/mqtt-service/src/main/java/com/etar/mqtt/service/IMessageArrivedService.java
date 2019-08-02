package com.etar.mqtt.service;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public interface IMessageArrivedService {
    /**
     * 开始解析
     *
     * @param topic   主题
     * @param message 消息
     */
    void startParseMessage(String topic, MqttMessage message);


    /**
     * 广告应答
     *
     * @param message 消息
     */
    void broadcastResponse(String message);

    /**
     * 更换滤芯
     *
     * @param code    设备号
     * @param timeTag 同步时间成功标志
     */
    void changeFilter(String timeTag, String code);

    /**
     * 硬件手动解绑
     *
     * @param devCode 设备号
     */
    void hardwareManualUnbunding(String devCode);

    /**
     * 硬件上报滤芯寿命
     *
     * @param filterLife 滤芯寿命
     * @param devCode    设备号
     */
    void changeFilterLife(Integer filterLife, String devCode);

    /**
     * 设备上线开机
     *
     * @param devCode 设备号
     */
    void devOnline(String devCode);

    /**
     * 解除绑定响应
     *
     * @param devCode 设备号
     */
    void hardwareUnbunding(String devCode);

    /**
     * 激活设备
     *
     * @param devCode 设备号
     */
    void activateDev(String devCode);
}
