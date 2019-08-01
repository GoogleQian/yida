package com.etar.mqtt.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.etar.config.ApplicationContextUtils;
import com.etar.dev.IDevMngService;
import com.etar.mqtt.MqttPushClient;
import com.etar.mqtt.service.IMessageArrivedService;
import com.etar.utils.MqttUtil;
import entity.adverstising.Advertising;
import entity.advstatic.AdvStatic;
import entity.dev.Device;
import entity.emqclient.EmqClient;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import utils.ConstantUtil;

import java.util.Date;

/**
 * @author Gmq
 * @since 2019-05-24 10:55
 **/
@Service
public class MessageArrivedServiceImpl implements IMessageArrivedService {
    private static Logger log = LoggerFactory.getLogger(MessageArrivedServiceImpl.class);

    @Override
    public void startParseMessage(String topic, MqttMessage message) {
        // subscribe后得到的消息会执行到这里面
        System.out.println("接收消息主题:" + topic);
        String[] infos = topic.split("/");

        System.out.println("接收消息Qos:" + message.getQos());
        String ret = new String(message.getPayload());
        System.out.println("接收消息内容:" + ret);
        if (topic.contains(ConstantUtil.MQTT_RET_TOPIC)) {
            ret = StringUtils.trim(ret);
            //硬件响应指令操作
            hardwareResponseCommand(infos[1], ret);
        } else if (topic.contains(ConstantUtil.MQTT_HARDWARE_ADVRET)) {
            System.out.println("群发广告应答");
            adResponse(ret);
        }
    }

    /**
     * 硬件响应指令
     *
     * @param info 设备号
     * @param ret  消息
     */
    private void hardwareResponseCommand(String info, String ret) {
        String devCode = info.trim();
        if (ConstantUtil.MQTT_ACTIVE_SUCCESS.equals(ret)) {
            System.out.println("激活:" + ret);
            activateDev(devCode);
            return;
        }
        //解除绑定
        if (ConstantUtil.MQTT_UNBIND_SUCCESS.equals(ret)) {
            System.out.println("解除绑定 : " + ret);
            hardwareUnbunding(devCode);
            return;
        }
        //上线
        if (ConstantUtil.MQTT_OPEN_SUCCESS.equals(ret)) {
            System.out.println("开机 : " + ret);
            devOnline(devCode);
            return;
        }
        //滤芯寿命
        if (ret.contains(ConstantUtil.MQTT_FILTER_LIFE)) {
            String[] split = ret.split(",");
            System.out.println("滤芯寿命 : " + ret);
            Integer filterLife = Integer.valueOf(split[1].trim());
            changeFilterLife(filterLife, devCode);
            return;
        }
        //硬件恢复出厂设置，发送解绑
        if (ret.equals(ConstantUtil.MQTT_HARDWARE_UNBIND)) {
            System.out.println("硬件手动解绑 : " + ret);
            hardwareManualUnbunding(devCode);
            return;
        }
        //更换滤芯
        if (ret.startsWith(ConstantUtil.MQTT_HARDWARE_CHANGE_FILTER)) {
            String[] split = ret.split(",");
            String timeTag = split[1].trim();
            System.out.println("更换滤芯 : " + timeTag);
            changeFilter(timeTag, devCode);
        }
    }

    @Override
    public void activateDev(String devCode) {
        try {
            ApplicationContextUtils.getIDevMngService().updateBindStatus(ConstantUtil.ACTIVE, devCode);
        } catch (Exception e) {
            parseException(e);
        }
    }

    @Override
    public void hardwareUnbunding(String devCode) {
        try {
            ApplicationContextUtils.getIDevMngService().updateBindStatus(ConstantUtil.UN_ACTIVE, devCode);
        } catch (Exception e) {
            parseException(e);
        }
    }

    @Override
    public void devOnline(String devCode) {
        try {
            ApplicationContextUtils.getIDevMngService().updateOnline(ConstantUtil.ONLINE, devCode);
        } catch (Exception e) {
            parseException(e);
        }
    }

    @Override
    public void changeFilterLife(Integer filterLife, String devCode) {
        try {
            ApplicationContextUtils.getIDevMngService().updateFilterLife(filterLife, devCode);
        } catch (Exception e) {
            parseException(e);
        }
    }


    @Override
    public void hardwareManualUnbunding(String devCode) {
        try {
            ApplicationContextUtils.getIDevMngService().hardwareUnbind(ConstantUtil.UN_ACTIVE, devCode);
            ApplicationContextUtils.getIDevMngService().updateFilterLifeAndActive(devCode, 0, ConstantUtil.UN_ACTIVE);
        } catch (Exception e) {
            parseException(e);
        }
    }


    @Override
    public void changeFilter(String timeTag, String devCode) {
        try {
            if (timeTag.equals(ConstantUtil.HARD_GET_TIME_SUCCESS)) {

                Device byDevCode = ApplicationContextUtils.getIDevMngService().findByDevCode(devCode);
                if (byDevCode != null) {
                    //滤芯更新为已使用
                    ApplicationContextUtils.getIDevMngService().updateFilterCode(1, byDevCode.getFilterCode());
                    //绑定滤芯
                    ApplicationContextUtils.getIDevMngService().updateFilterLife(ConstantUtil.ACTIVE_FILTER, devCode);
                }
            } else if (timeTag.equals(ConstantUtil.HARD_GET_TIME_FAIL)) {
                log.info("设备同步时间失败");
            }
        } catch (Exception e) {
            parseException(e);
        }
    }

    @Override
    public void adResponse(String message) {
        try {
            String[] advRets = message.split(",");
            if ("1".equals(advRets[0].trim())) {

                int bytesToInt = MqttUtil.bytesToInt(advRets[2].getBytes(), 0);
                System.out.println("广告id:" + bytesToInt);
                Advertising advertising = ApplicationContextUtils.getIDevMngService().findById(bytesToInt);
                if (advertising != null) {
                    AdvStatic advStatic = new AdvStatic();
                    advStatic.setDevId(advRets[1].trim());
                    advStatic.setAdvName(advertising.getName());
                    advStatic.setAdvId(bytesToInt);
                    advStatic.setReportTime(new Date());

                    ApplicationContextUtils.getIDevMngService().save(advStatic);
                }
            }
        } catch (Exception e) {
            parseException(e);
        }
    }

    private void parseException(Exception e) {
        if (e instanceof MqttException) {
            try {
                MqttPushClient.getClient().reconnect();
            } catch (MqttException e1) {
                e1.printStackTrace();
            }
        } else {
            log.info(e.getMessage());
            e.printStackTrace();
        }
    }
}
