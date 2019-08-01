package com.etar.emq;

import com.alibaba.fastjson.JSONObject;
import com.etar.dev.IDevMngService;
import com.etar.emqclient.service.EmqClientService;
import entity.dev.Device;
import entity.emqclient.EmqClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.ConstantUtil;

import java.util.Date;

/**
 * emq回调接口
 *
 * @author Gmq
 * @date 2019-01-25 10:13
 **/
@RestController
@RequestMapping("/emq")
public class EmqController {
	private static Logger log = LoggerFactory.getLogger(EmqController.class);

	private final EmqClientService emqClientService;
	private final IDevMngService devMngService;

	@Autowired
	public EmqController(EmqClientService emqClientService, IDevMngService devMngService) {
		this.emqClientService = emqClientService;
		this.devMngService = devMngService;
	}

	@RequestMapping("/on_client_connected")
	@Transactional(rollbackFor = Exception.class)
	public void callback(@RequestBody EmqClient emqClient) {
		log.info("接受到消息·" + emqClient.toString());
		String action = emqClient.getAction();
		if (action != null) {
			String clientId = emqClient.getClientId();
			int connAck = emqClient.getConnAck() != null ? emqClient.getConnAck() : -1;
			//检测上下线 connAck=0 保证连接成功
			if (ConstantUtil.CLIENT_CONN.equals(action) && 0 == connAck) {
				//clientId 即 devCode
				Date date = new Date();
				emqClient.setAction("1");
				emqClient.setCreateTime(date);
				emqClientService.save(emqClient);
			} else if (ConstantUtil.CLIENT_DISCONN.equals(action)) {
				emqClient.setAction("0");
				emqClient.setCreateTime(new Date());
				emqClientService.save(emqClient);
//				修改设备在线状态
				try {
					devMngService.updateOnlineAndIpAndTime(0, "", clientId);
				} catch (MqttException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
