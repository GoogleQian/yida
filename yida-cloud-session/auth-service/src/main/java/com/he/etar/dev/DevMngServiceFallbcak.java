package com.he.etar.dev;

import entity.common.entity.Result;
import entity.dev.Device;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Component;

/**
 * DevMng服务降级
 *
 * @author Gmq
 * @since 2019-05-07 15:14
 **/
@Component
public class DevMngServiceFallbcak implements IDevMngService {

	@Override
	public boolean existsByDevCode(String devCode) {
		System.out.println("降级处理 existsByDevCode ");
		return false;
	}

	@Override
	public Result addDevice(Device byDevCode) {
		System.out.println("降级处理 addDevice ");
		return null;
	}

	@Override
	public Device findByDevCode(String devCode) {
		System.out.println("降级处理 findByDevCode ");
		return null;
	}
	@Override
	public int updateOnlineAndIpAndTime(Integer online, String ip, String devCode) throws MqttException {
		System.out.println("降级处理 updateOnlineAndIpAndTime ");
		return 0;
	}
}
