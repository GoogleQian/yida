package com.he.etar.dev;

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

@FeignClient(value = "mng-service", fallback = DevMngServiceFallbcak.class)
@Component
public interface IDevMngService {


    /**
     * 查询设备是否存在
     *
     * @param devCode 设备号
     */
    @GetMapping(value = "/yida/dev/existsByDevCode")
    boolean existsByDevCode(@RequestParam(value = "devCode") String devCode);

    /**
     * 新增设备
     *
     * @param byDevCode 设备
     */
    @PostMapping(value = "/yida/dev/devices")
    Result addDevice(@RequestBody Device byDevCode);

    /**
     * 通过devCode查询对象
     *
     * @param devCode 设备编码
     * @return 对象
     */
    @GetMapping(value = "/yida/dev/findByDevCode")
    Device findByDevCode(@RequestParam(value = "devCode") String devCode);

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

}
