package com.etar.mqtt;


import com.etar.utils.MqttUtil;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import utils.ConstantUtil;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

/**
 * MqttPushClient连接配置
 *
 * @author hzh
 * @version 1.0
 * @date 2019/1/17 13:41
 */
@Component
public class MqttPushClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(MqttPushClient.class);
    private static MqttClient client;
    @Resource
    MqttConfiguration mqttConfiguration;

    public static MqttClient getClient() {
        return client;
    }


    public static void setClient(MqttClient client) {
        MqttPushClient.client = client;
    }

    public void connect(String host, String clientID, String username, String password, int timeout, int keepalive) {
        MqttClient client;
        try {
            client = new MqttClient(host, clientID, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(false);
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            options.setConnectionTimeout(timeout);
            options.setKeepAliveInterval(keepalive);
            MqttPushClient.setClient(client);
            try {
                client.setCallback(new PushCallback());
                client.connect(options);
            } catch (MqttException e) {
                e.printStackTrace();
                LOGGER.error(e.getMessage());
            }
        } catch (MqttException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * 发布，默认qos为0，非持久化
     *
     * @param topic       订阅主题
     * @param pushMessage 发送的消息
     */
    public int publish(int qos, String topic, String pushMessage) throws MqttException {
        return publish(qos, true, topic, pushMessage);
    }

    /**
     * 发布
     *
     * @param qos         订阅方式
     * @param retained    retained
     * @param topic       主题
     * @param pushMessage 消息
     */
    public int publish(int qos, boolean retained, String topic, String pushMessage) throws MqttException {
        MqttMessage message = new MqttMessage();
        message.setQos(qos);
        message.setRetained(retained);
        try {
            byte[] bytes;
            //是否是广告下发
            boolean isBroadCastSendAd = ConstantUtil.BROADCAST_TOP.equals(topic) && pushMessage.startsWith(ConstantUtil.MQTT_ADV_PREFIX_BROADCAST);
            //是否是开机下发广告
            boolean isOnlineSendAd = topic.startsWith(ConstantUtil.DOWN_TOPIC) && pushMessage.startsWith(ConstantUtil.MQTT_ADV_PREFIX_ONLINE);
            //是否是单独测试的广告
            boolean isSendAdToDev = topic.startsWith(ConstantUtil.DOWN_TOPIC) && pushMessage.startsWith(ConstantUtil.MQTT_ADV_PREFIX_THE_DEV);
            if (isOnlineSendAd || isBroadCastSendAd || isSendAdToDev) {
                LOGGER.info(topic + "下发广告，对广告进行Unicode编码：" + pushMessage);
                bytes = MqttUtil.adUnicode(pushMessage);
            } else {
                LOGGER.info(topic + "一般指令，进行GB2312编码");
                bytes = pushMessage.getBytes(ConstantUtil.GB2312);
            }
            message.setPayload(bytes);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage());
        }
        MqttTopic mTopic = MqttPushClient.getClient().getTopic(topic);
        if (null == mTopic) {
            LOGGER.error("topic not exist");
        }
        MqttDeliveryToken token;

        token = mTopic.publish(message);
        token.waitForCompletion();
        return token.getMessageId();

    }


    public static void main(String[] args) {
        try {
            byte[] adUnicode = MqttUtil.adUnicode("1,\\u0000\\u0000\\u0000&,12,51,中文标点测试！@#￥%……&*——+“”？《》,。，‘【】、=；~：“”");
            for (byte b : adUnicode) {
                System.out.print(b + "    ");
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 订阅某个主题，qos默认为0
     *
     * @param topic 主题
     */
    public void subscribe(String topic) throws MqttException {
        subscribe(topic, 0);
    }

    /**
     * 订阅某个主题
     *
     * @param topic 主题
     * @param qos   订阅方式
     */

    public void subscribe(String topic, int qos) throws MqttException {
        MqttPushClient.getClient().subscribe(topic, qos);
    }

    /**
     * 重连
     */
    public void reconnect() {
        connect(mqttConfiguration.getHost(), mqttConfiguration.getClientid(), mqttConfiguration.getUsername(), mqttConfiguration.getPassword()
                , mqttConfiguration.getTimeout(), mqttConfiguration.getKeepalive());

    }
}
