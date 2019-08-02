package com.etar.purifier.modules.firmware.controller;

import com.etar.purifier.common.annotation.LogOperate;
import com.etar.purifier.common.validation.XException;
import com.etar.purifier.modules.firmwaretask.service.FirmwareTaskService;
import com.etar.purifier.modules.mqtt.MqttService;
import com.etar.purifier.utils.MqttUtil;
import com.etar.purifier.utils.StringUtil;
import entity.firmware.Firmware;
import entity.firmware.FirmwareVo;
import entity.firmware.QueryFirmware;
import com.etar.purifier.modules.firmware.service.FirmwareService;
import entity.common.entity.DataResult;
import entity.common.entity.PageBean;
import entity.common.entity.Result;
import entity.firmwaretask.FirmwareTask;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import utils.ConstantUtil;
import utils.ResultCode;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * FirmwareController层
 *
 * @author hzh
 * @since 2019-07-01
 */
@RestController
@RequestMapping(value = "yida/firmwares")
public class FirmwareController {
    private static Logger log = LoggerFactory.getLogger(FirmwareController.class);
    private final FirmwareService firmwareService;
    private final MqttService mqttService;
    private final FirmwareTaskService firmwareTaskService;

    @Autowired
    public FirmwareController(FirmwareService firmwareService, MqttService mqttService, FirmwareTaskService firmwareTaskService) {
        this.firmwareService = firmwareService;
        this.mqttService = mqttService;
        this.firmwareTaskService = firmwareTaskService;
    }


    /**
     * 保存对象
     *
     * @param firmware 对象
     */
    @PostMapping
    @LogOperate(description = "新增固件")
    public Result save(@Validated @RequestBody Firmware firmware) {
        Result result = new Result();
        try {
            boolean existsByName = firmwareService.existsByName(firmware.getName());
            if (existsByName) {
                return result.error(ResultCode.FIRMWARE_NAME_EXISTS);
            }
            firmware.setUploadTime(new Date());
            firmwareService.save(firmware);
        } catch (Exception e) {
            log.error(e.getMessage());
            return result.error(2001, "新增失败");
        }
        return result.ok();
    }

    /**
     * 更新
     *
     * @param firmware 对象
     * @return 更新结果
     */
    @PutMapping(value = "/{id}")
    @LogOperate(description = "更新固件")
    public Result update(@Validated @RequestBody Firmware firmware, @PathVariable("id") Integer id) {
        Result result = new Result();
        try {
            boolean exists = firmwareService.existsById(id);
            if (!exists) {
                return result.error(2002, "修改失败，未找到");
            }
            firmwareService.save(firmware);
        } catch (Exception e) {
            log.error(e.getMessage());
            return result.error(2002, "修改失败");
        }
        return result.ok();
    }


    /**
     * 通过id查找对象
     *
     * @param id id
     * @return Firmware 对象
     */
    @GetMapping(value = "/{id}")
    @LogOperate(description = "查找固件信息")
    public Result findById(@PathVariable("id") Integer id) {
        DataResult result = new DataResult();
        try {
            result.setDatas(firmwareService.findById(id));
        } catch (Exception e) {
            log.error(e.getMessage());
            return result.error(2004, "不存在");
        }
        return result.ok();
    }

    /**
     * 分页查询
     *
     * @return Page<Firmware> 对象
     */
    @PostMapping(value = "/pages")
    @LogOperate(description = "分页查询固件")
    public Result findByPage(@RequestBody QueryFirmware queryFirmware) {

        DataResult result = new DataResult();
        try {
            int page = queryFirmware.getPage();
            int pageSize = queryFirmware.getPageSize();
            Page<Firmware> all = firmwareService.findAll(page - 1, pageSize, queryFirmware);
            PageBean<Firmware> pageBean = new PageBean<>();
            if (all == null || 0 == all.getTotalElements()) {
                pageBean.setList(new ArrayList<>());
                result.setDatas(pageBean);
                return result.ok();
            }
            pageBean.setCurPage(page);
            pageBean.setItemCounts(all.getTotalElements());
            pageBean.setPageSize(pageSize);
            pageBean.setList(all.getContent());
            result.setDatas(pageBean);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return result.error(2005, "查询出错");
        }
        return result.ok();
    }

    /**
     * 删除banner
     *
     * @param ids ids
     */
    @DeleteMapping(value = "/{ids}")
    @LogOperate(description = "删除固件")
    public Result delFirmware(@PathVariable("ids") String ids) {
        log.info("进入删除固件接口");
        Result result = new Result();
        if (StringUtils.isBlank(ids)) {
            return result.error(ResultCode.FIRMWARE_ID_IS_NULL);
        }
        try {
            List<FirmwareTask> firmwareTasks = firmwareTaskService.findByFmIdIn(StringUtil.stringToIntegerList(ids));
            if (firmwareTasks.size() != 0) {
                return result.error(ResultCode.FIRMWARE_HAS_TASK);
            }
            firmwareService.delBatch(StringUtil.stringToIntegerList(ids));
        } catch (Exception e) {
            log.info(e.getMessage());
            return result.error(ResultCode.DB_ERROR);
        }
        return result.ok();
    }

    /**
     * 下发广告到指定设备
     */
    @PostMapping(value = "/send/dev")
    @LogOperate(description = "下发固件到指定设备")
    public Result sendFirmwareToTheDev(@Valid @RequestBody FirmwareVo firmwareVo) {
        Result result = new Result();
        String msg = MqttUtil.getFirmwareMsg(ConstantUtil.MQTT_FIRMWARE_DEV_PREFIX, firmwareVo.getOssUrl(), 0);
        try {
            List<String> devCodes = firmwareVo.getDevCodes();
            if (!CollectionUtils.isEmpty(devCodes)) {
                for (String devCode : devCodes) {
                    mqttService.publish(2, ConstantUtil.DOWN_TOPIC + devCode, msg);
                }
            }
        } catch (MqttException e) {
            log.info(e.getMessage());
            if (e.getReasonCode() == MqttException.REASON_CODE_CLIENT_DISCONNECTING || e.getReasonCode() == MqttException.REASON_CODE_SERVER_CONNECT_ERROR
                    || e.getReasonCode() == MqttException.REASON_CODE_CLIENT_NOT_CONNECTED || e.getReasonCode() == MqttException.REASON_CODE_CONNECTION_LOST
                    || e.getReasonCode() == MqttException.REASON_CODE_CLIENT_DISCONNECT_PROHIBITED || e.getReasonCode() == MqttException.REASON_CODE_CLIENT_ALREADY_DISCONNECTED) {
                try {
                    mqttService.reconnect();
                } catch (MqttException e1) {
                    e1.printStackTrace();
                    log.info("重连MQTT服务器失败！");
                }
            } else {
                log.info("Mqtt异常");
            }
            throw new XException(e.getMessage());
        }
        return result.ok();
    }

    /**
     * 广播固件到设备
     */
    @GetMapping(value = "/broadcast/{id}")
    @LogOperate(description = "群发广播固件到设备")
    public Result broadcastFirmwareToTheDev(@PathVariable("id") Integer id) {
        Result result = new Result();
        Firmware byId = firmwareService.findById(id);
        if (byId == null) {
            return result.error(ResultCode.FIRMWARE_IS_NULL);
        }
        String msg = MqttUtil.getFirmwareMsg(ConstantUtil.MQTT_FIRMWARE_BROADCAST_PREFIX, byId.getOssUrl(), id);
        try {
            mqttService.publish(2, ConstantUtil.BROADCAST_TOP, msg);
            //保存任务推送情况
            firmwareTaskService.saveByFirmware(byId);
        } catch (MqttException e) {
            log.info(e.getMessage());
            if (e.getReasonCode() == MqttException.REASON_CODE_CLIENT_DISCONNECTING || e.getReasonCode() == MqttException.REASON_CODE_SERVER_CONNECT_ERROR
                    || e.getReasonCode() == MqttException.REASON_CODE_CLIENT_NOT_CONNECTED || e.getReasonCode() == MqttException.REASON_CODE_CONNECTION_LOST
                    || e.getReasonCode() == MqttException.REASON_CODE_CLIENT_DISCONNECT_PROHIBITED || e.getReasonCode() == MqttException.REASON_CODE_CLIENT_ALREADY_DISCONNECTED) {
                try {
                    mqttService.reconnect();
                } catch (MqttException e1) {
                    e1.printStackTrace();
                    log.info("重连MQTT服务器失败！");
                }
            } else {
                log.info("Mqtt异常");
            }
            throw new XException(e.getMessage());
        }
        return result.ok();
    }
}
