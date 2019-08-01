package com.etar.purifier.modules.common.service;

import com.etar.purifier.modules.advertising.entity.Advertising;
import com.etar.purifier.modules.mqtt.MqttPushClient;
import com.etar.purifier.utils.ConstantUtil;
import com.etar.purifier.utils.MqttUtil;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;


/**
 * @author hzh
 * @date 2018/10/25
 */
@Component
@EnableScheduling
public class TimingService {

    private static Logger log = LoggerFactory.getLogger(TimingService.class);

    @Autowired
    private MqttPushClient mqttPushClient;

//    @Scheduled(cron = "*/10 * * * * ?")
    public void mtqqAd() {
        Advertising ad = new Advertising();
        ad.setSolgan(AdWord.getSolgan());
        ad.setTitle(AdWord.getTitle());
        int id = new Random().nextInt(50000);
        ad.setId(id);
        String advMsg = MqttUtil.getAdvMsg(ad, ConstantUtil.MQTT_ADV_PREFIX_BROADCAST);
        try {
            log.info("广告id:{};广告下发的广告详情：{}", id, advMsg);
            mqttPushClient.publish(1, "broadcast", advMsg);
        } catch (MqttException e) {
            if (e.getReasonCode() == MqttException.REASON_CODE_CLIENT_DISCONNECTING || e.getReasonCode() == MqttException.REASON_CODE_SERVER_CONNECT_ERROR
                    || e.getReasonCode() == MqttException.REASON_CODE_CLIENT_NOT_CONNECTED || e.getReasonCode() == MqttException.REASON_CODE_CONNECTION_LOST
                    || e.getReasonCode() == MqttException.REASON_CODE_CLIENT_DISCONNECT_PROHIBITED || e.getReasonCode() == MqttException.REASON_CODE_CLIENT_ALREADY_DISCONNECTED) {
                mqttPushClient.reconnect();
                log.info("下发时间时客户机未连接");
            } else {
                log.info("Mqtt异常");
            }
        }
//                mqttPushClient.unSubscribe("toptest/" + i);

    }

    //    @Scheduled(cron = "0 0/50  * * * ?")
    public void mqttSendDate() {

        try {
            String timeMsg = MqttUtil.getTimeMsg();
            mqttPushClient.publish(1, "broadcast", timeMsg);
            log.info("下发时间broadcast：" + timeMsg);
        } catch (MqttException e) {
            if (e.getReasonCode() == MqttException.REASON_CODE_CLIENT_DISCONNECTING || e.getReasonCode() == MqttException.REASON_CODE_SERVER_CONNECT_ERROR
                    || e.getReasonCode() == MqttException.REASON_CODE_CLIENT_NOT_CONNECTED || e.getReasonCode() == MqttException.REASON_CODE_CONNECTION_LOST
                    || e.getReasonCode() == MqttException.REASON_CODE_CLIENT_DISCONNECT_PROHIBITED || e.getReasonCode() == MqttException.REASON_CODE_CLIENT_ALREADY_DISCONNECTED) {
                mqttPushClient.reconnect();
                log.info("下发时间时客户机未连接");
            } else {
                log.info("Mqtt异常");
            }

        }

    }

}
