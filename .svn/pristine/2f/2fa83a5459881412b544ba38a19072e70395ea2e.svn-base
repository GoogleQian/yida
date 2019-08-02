package com.etar.dev;

import entity.adverstising.Advertising;
import entity.advstatic.AdvStatic;
import entity.common.entity.Result;
import entity.dev.Device;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@FeignClient(value = "mng-service", fallback = DevMngServiceFallbcak.class)
@Component
public interface IDevMngService {


	/**
	 * 通过devCode查询对象
	 *
	 * @param devCode 设备编码
	 * @return 对象
	 */
	@GetMapping(value = "/yida/dev/findByDevCode")
	Device findByDevCode(@RequestParam(value = "devCode") String devCode);

	/**
	 * 更新设备绑定状态
	 *
	 * @param status  设备状态
	 * @param devCode 设备码
	 * @return 绑定的结果
	 */
	@Transactional(rollbackFor = Exception.class)
	@GetMapping(value = "/yida/dev/updateBindStatus")
	int updateBindStatus(@RequestParam(value = "status") Integer status, @RequestParam(value = "devCode") String devCode);

	/**
	 * 硬件手动解除绑定
	 *
	 * @param status  设备状态
	 * @param devCode 设备码
	 * @return 解除结果
	 * @throws MqttException MqttException
	 */
	@Transactional(rollbackFor = Exception.class)
	@GetMapping(value = "/yida/dev/hardwareUnbind")
	int hardwareUnbind(@RequestParam(value = "status") Integer status, @RequestParam(value = "devCode") String devCode) throws MqttException;

	/**
	 * 开机修改上线状态
	 *
	 * @param online  上线状态
	 * @param devCode 设备码
	 * @return int int
	 * @throws MqttException MqttException
	 */
	@Transactional(rollbackFor = Exception.class)
	@GetMapping(value = "/yida/dev/updateOnline")
	int updateOnline(@RequestParam(value = "online") Integer online, @RequestParam(value = "devCode") String devCode) throws MqttException;

	/**
	 * 设备上线更新状态
	 *
	 * @param online      上线状态
	 * @param ip          ip
	 * @param devCode     设备码
	 * @return int int
	 * @throws MqttException MqttException
	 */
	@Transactional(rollbackFor = Exception.class)
	@GetMapping(value = "/yida/dev/updateOnlineAndIpAndTime")
	int updateOnlineAndIpAndTime(@RequestParam(value = "online") Integer online,
	                             @RequestParam(value = "ip") String ip,
	                             @RequestParam(value = "devCode") String devCode) throws MqttException;

	/**
	 * 更新滤芯寿命
	 *
	 * @param filterLife 滤芯寿命
	 * @param devCode    设备码
	 */
	@GetMapping(value = "/yida/dev/updateFilterLife")
	void updateFilterLife(@RequestParam(value = "filterLife") Integer filterLife, @RequestParam(value = "devCode") String devCode);

	/**
	 * 硬件解绑 滤芯归零 激活状态改为未激活
	 *
	 * @param devCode    设备号
	 * @param filterLife 滤芯寿命
	 * @param active     激活状态
	 */
	@GetMapping(value = "/yida/dev/updateFilterLifeAndActive")
	void updateFilterLifeAndActive(@RequestParam(value = "devCode") String devCode, @RequestParam(value = "filterLife") Integer filterLife, @RequestParam(value = "active") int active);

	/**
	 * 更新滤芯使用情况
	 *
	 * @param status     使用状态
	 * @param filterCode 滤芯编码
	 * @return 更新结果 int
	 */
	@GetMapping(value = "/yida/filterInfos/updateFilterCode")
	int updateFilterCode(@RequestParam(value = "status") int status, @RequestParam(value = "filterCode") String filterCode);

	/**
	 * 查询广告
	 *
	 * @param id 使用状态
	 * @return 更新结果 int
	 */
	@GetMapping(value = "/yida/ad/findById")
	Advertising findById(@RequestParam(value = "id") int id);

	/**
	 * 新增设备
	 *
	 * @param byDevCode 设备
	 */
	@PostMapping(value = "/yida/dev/devices")
	Result addDevice(@RequestBody Device byDevCode);

	/**
	 * 保存advstatic对象
	 *
	 * @param advstatic 持久对象，或者对象集合
	 */
	@PostMapping(value = "/advstatic/save")
	void save(@RequestBody AdvStatic advstatic);

	/**
	 * 查询设备是否存在
	 *
	 * @param devCode 设备号
	 */
	@GetMapping(value = "/yida/dev/existsByDevCode")
	boolean existsByDevCode(@RequestParam(value = "devCode") String devCode);

}
