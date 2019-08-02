package com.etar.dev;

import entity.adverstising.Advertising;
import entity.advstatic.AdvStatic;
import entity.common.entity.Result;
import entity.dev.Device;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * DevMng服务降级
 *
 * @author Gmq
 * @since 2019-05-07 15:14
 **/
@Component
public class DevMngServiceFallbcak implements IDevMngService {
    @Override
    public Device findByDevCode(String devCode) {
        return null;
    }

    @Override
    public int updateBindStatus(Integer status, String devCode) {
        return 0;
    }

    @Override
    public int hardwareUnbind(Integer status, String devCode) throws MqttException {
        return 0;
    }

    @Override
    public int updateOnline(Integer online, String devCode) throws MqttException {
        return 0;
    }

    @Override
    public void updateFilterLife(Integer filterLife, String devCode) {

    }

    @Override
    public int updateFilterCode(int status, String filterCode) {
        return 0;
    }

    @Override
    public Advertising findById(int id) {
        return null;
    }

    @Override
    public void save(AdvStatic advstatic) {

    }

    @Override
    public boolean existsByDevCode(String devCode) {
        return false;
    }

    @Override
    public void updateFilterLifeAndActive(String devCode, Integer filterLife, int active) {

    }

    @Override
    public Result addDevice(Device byDevCode) {
        return null;
    }

    @Override
    public int updateOnlineAndIpAndTime(Integer online, String ip, String devCode) throws MqttException {
        System.out.println("降级处理 updateOnlineAndIpAndTime");
        return 0;
    }


}
