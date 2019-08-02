package com.etar.mqtt;


import com.etar.config.ApplicationContextUtils;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * mqtt回调函数
 *
 * @author hzh
 * @version 1.0
 * @date 2019/1/17 13:42
 */
@Service
public class PushCallback implements MqttCallback {
    private static Logger log = LoggerFactory.getLogger(PushCallback.class);

    @Override
    public void connectionLost(Throwable cause) {
        try {
            MqttClient client = MqttPushClient.getClient();
            if (!client.isConnected()) {
                client.reconnect();
                System.out.println("client连接断开，重连成功");
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        try {
            int msgId = token.getMessageId();
            System.out.println("deliveryComplete---------" + msgId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void messageArrived(String topic, MqttMessage message) {
        ApplicationContextUtils.getIMessageArrivedService().startParseMessage(topic, message);
    }


}

