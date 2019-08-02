package com.he.etar.auth;

import com.he.etar.dev.IDevMngService;
import entity.common.entity.Result;
import entity.dev.Device;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import utils.ConstantUtil;
import utils.HMacMD5Util;
import utils.MqttUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

	@Value("${emq_server}")
	private String emqServer;
	@Value("${route_url}")
	private String routeUrl;
	@Value("${appid}")
	private String appid;
	@Value("${appsecret}")
	private String appsecret;
	@Value("${img.imgUrl}")
	private String imgUrl;
	@Value("${mqtt.password}")
	private String mqttPassword;
	/**
	 * 区分生产环境
	 */
	@Value("${spring.profiles.active}")
	String active;
	@Autowired
	private IDevMngService iDevMngService;

	/**
	 * 设备登录认证
	 *
	 * @param username clientID
	 * @param password 老设备没有加密为IMEI 新设备加密
	 * @param clientid 客户端ID imei
	 * @param response res
	 * @throws IOException IO
	 */
	@PostMapping(value = "/loginAuth")
	public void authDev(@RequestParam(value = "username", defaultValue = "") String username,
	                    @RequestParam(value = "password", defaultValue = "") String password,
	                    @RequestParam(value = "clientid", defaultValue = "") String clientid,
	                    HttpServletResponse response) throws IOException {
		log.info(clientid + "--开始登录认证......{}，{}，{}", username, password, clientid);
		try {
			Device byDevCode = iDevMngService.findByDevCode(clientid.trim());
			boolean verDevAuth = false;
			boolean backendAuth = false;
			response.setContentType("application/json;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			String md5String = HMacMD5Util.bytesToHexString(HMacMD5Util.getHmacMd5Bytes(ConstantUtil.MD5_KEY.getBytes(), clientid.getBytes()));
			String md5Pwd = ConstantUtil.PRE_MD5_PWD + md5String;
			boolean v1Auth = username.equals(password) && username.equals(clientid);
			boolean v2Auth = username.equals(clientid) && md5Pwd.equals(password);
			//对应版本设备验证
			if (byDevCode != null) {
				log.info("硬件版本：" + byDevCode.getVersionNum());
				switch (byDevCode.getVersionNum()) {
					case 1:
						verDevAuth = v1Auth;
						break;
					case 2:
						verDevAuth = v2Auth;
						break;
					default:
				}

			} else {
				//后台客户端验证
				backendAuth = clientid.contains("backend") && mqttPassword.equals(password);
			}
			//如果数据库不存在或者不是后台客户端 即新增设备 对新增设备做V1,V2验证
			if (byDevCode == null && !backendAuth && (v1Auth || v2Auth)) {
				Device device = new Device();
				device.setInventoryTime(new Date());
				device.setDevCode(clientid);
				device.setVersionNum(2);
				device.setOnline(1);
				Result result = iDevMngService.addDevice(device);
				if (result.getRet() == 0) {
					log.info("新增设备: " + clientid);
				} else {
					log.info(clientid + "--设备非法，添加失败！");
				}
				response.setStatus(200);
			} else {
				if (verDevAuth || backendAuth) {
					response.setStatus(200);
					log.info(clientid + "--认证成功 ！");
				} else {
					response.setStatus(401);
					log.info(clientid + "--认证失败 ！");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 客户端发布订阅认证 同一行为（发布订阅）下的同一主题只回调一次
	 *
	 * @param access   订阅1 发布2
	 * @param username 用户名
	 * @param clientid 客户端ID
	 * @param ipaddr   连接ip地址
	 * @param topic    主题
	 * @param response res
	 * @throws IOException IO
	 */
	@GetMapping("/aclAuth")
	public void aclAuth(@RequestParam(value = "access", defaultValue = "") String access,
	                    @RequestParam(value = "username", defaultValue = "") String username,
	                    @RequestParam(value = "clientid", defaultValue = "") String clientid,
	                    @RequestParam(value = "ipaddr", defaultValue = "") String ipaddr,
	                    @RequestParam(value = "topic", defaultValue = "") String topic
			, HttpServletResponse response) throws IOException {
		log.info(clientid + "--开始Acl认证......{}，{}，{},{},{}", access, username, clientid, ipaddr, topic);
		try {
			//限制是否入库
			boolean existsByDevCode = iDevMngService.existsByDevCode(clientid);
			response.setContentType("application/json;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			boolean flag = "1".equals(access);
			response.setStatus(200);
			//同一行为、同一主题只回调一次
			String ip = imgUrl == null ? "" : imgUrl.replace("http://", "");
			if (existsByDevCode) {
				//如果是群发广告 限制发送Ip 客户端clientid
				if (!flag && ConstantUtil.BROADCAST_TOP.equals(topic)) {
					response.setStatus(401);
					log.info("无权下发广告！");
				} else if (topic.contains("activate")) {
					//增加对  activate/{devCode} 主题的限制
					//判断是否已经有此主题 调用官方接口
					boolean devSubTopicStatus = MqttUtil.getDevSubTopicStatus(topic, emqServer + routeUrl, appid, appsecret);

					if (devSubTopicStatus) {
						response.setStatus(401);
						log.info("订阅主题认证失败 ！");
					} else if (topic.contains(clientid)) {
						//判断是否是本设备的订阅
						log.info("订阅主题认证成功 ！");
					} else {
						response.setStatus(401);
						log.info("订阅主题认证失败 ！");
					}
				} else {
					log.info("订阅主题认证成功 ！");
				}
				//更新设备在线IP
				try {
					int i = iDevMngService.updateOnlineAndIpAndTime(1, ipaddr, clientid);
					if (1 == i) {
						log.info("设备状态更新成功！");
					} else {
						log.info("设备状态更新失败！");
					}
				} catch (MqttException e) {
					e.printStackTrace();
				}
			} else if (clientid.contains("backend")) {
				//如果是发布群发广告、客户端激活、订阅广告应答 限制发送Ip 客户端clientid
				if (ip.equals(ipaddr)) {
					if (!flag && ConstantUtil.BROADCAST_TOP.equals(topic)) {
						log.info("发布广告主题认证成功 ！");
					} else if (flag && ConstantUtil.CLIENTS_CONN.equals(topic)) {
						log.info("订阅设备上线主题成功 ！");
					} else if (flag && ConstantUtil.CLIENTS_DISCONN.equals(topic)) {
						log.info("订阅设备下线主题成功 ！");
					}
				} else {
					response.setStatus(401);
					log.info("主题认证失败 ！");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
