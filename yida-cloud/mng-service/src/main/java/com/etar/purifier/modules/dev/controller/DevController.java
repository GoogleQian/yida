package com.etar.purifier.modules.dev.controller;

import com.etar.purifier.common.annotation.LogOperate;
import com.etar.purifier.modules.dev.service.DeviceService;
import com.etar.purifier.modules.emqclient.service.EmqClientService;
import com.etar.purifier.modules.filterinfo.service.FilterInfoService;
import com.etar.purifier.modules.mqtt.MqttService;
import com.etar.purifier.utils.DateUtil;
import com.etar.purifier.utils.DevCodeUtil;
import entity.common.entity.*;
import entity.dev.DevVo;
import entity.dev.Device;
import entity.emqclient.EmqClient;
import entity.emqclient.QueryEmqClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import utils.ConstantUtil;
import utils.ResultCode;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hzh
 * @date 2018/10/8
 */
@RestController
@RequestMapping(value = "/yida/dev")
@Validated
public class DevController {


    @Value("${emq_server}")
    private String emqServer;
    @Value("${connect_url}")
    private String connectUrl;
    @Value("${route_url}")
    private String routeUrl;
    @Value("${appid}")
    private String appid;
    @Value("${appsecret}")
    private String appsecret;


    private static Logger log = LoggerFactory.getLogger(DevController.class);

    private final DeviceService deviceService;
    private final MqttService mqttService;
    private final EmqClientService emqClientService;
    private final FilterInfoService filterInfoService;

    @Autowired
    public DevController(DeviceService deviceService, MqttService mqttService, EmqClientService emqClientService, FilterInfoService filterInfoService) {
        this.deviceService = deviceService;
        this.mqttService = mqttService;
        this.emqClientService = emqClientService;
        this.filterInfoService = filterInfoService;
    }

    /**
     * 解除绑定
     *
     * @param devId 设备id
     */
    @PutMapping(value = "/unbind/{devId}")
    @LogOperate(description = "解除绑定")
//    @RequiresPermissions("dev:unbindDev")
    public Result unbindDev(@NotNull(message = "设备ID不能为空") @PathVariable("devId") Integer devId) {
        Result result = new Result();
        try {
            if (devId == null) {
                result.error(ResultCode.DEV_ID_NULL);
            }
            Device device = deviceService.findById(devId);
            if (device == null) {
                return result.error(ResultCode.DEV_NOT_EXIST);
            }
            //todo 解除绑定
            int active = unbindDev(devId, device.getDevCode());
            if (1 == active) {
                result.ok();
            } else if (2 == active) {
                result.error(ResultCode.DEV_UNBIND_FAIL);
            } else {
                result.error(ResultCode.ERROR);
            }
        } catch (Exception e) {
            result.error(ResultCode.ERROR);
        }
        return result;
    }

    /**
     * 进入解绑
     *
     * @param devId 设备id
     * @param code  code
     */
    private int unbindDev(Integer devId, String code) {
        try {

            mqttService.publish(1, ConstantUtil.DOWN_TOPIC + code, "3,1");
            log.info("进入后台解绑：" + ConstantUtil.DOWN_TOPIC + code);
            //更改数据库状态
            deviceService.unbindDevByDevId(devId);
            return 1;
        } catch (MqttException e) {
            if (e.getReasonCode() == MqttException.REASON_CODE_CLIENT_DISCONNECTING || e.getReasonCode() == MqttException.REASON_CODE_SERVER_CONNECT_ERROR
                    || e.getReasonCode() == MqttException.REASON_CODE_CLIENT_NOT_CONNECTED || e.getReasonCode() == MqttException.REASON_CODE_CONNECTION_LOST
                    || e.getReasonCode() == MqttException.REASON_CODE_CLIENT_DISCONNECT_PROHIBITED || e.getReasonCode() == MqttException.REASON_CODE_CLIENT_ALREADY_DISCONNECTED) {
                try {
                    mqttService.reconnect();
                } catch (MqttException e1) {
                    e1.printStackTrace();
                    log.info("重连MQTT服务器失败！");
                }
                log.info("解绑失败,请稍后重试");
            } else {
                log.info("Mqtt异常");
            }
            return 2;
        }
    }

    /**
     * 批量解除绑定
     *
     * @param batchReqVo 批量绑定id集合
     */
    @PostMapping(value = "/unbind")
    @LogOperate(description = "批量解除绑定")
//    @RequiresPermissions("dev:unbindDevBatch")
    public Result unbindDevBatch(@Validated @RequestBody BatchReqVo batchReqVo) {
        Result result = new Result();
        try {
            List<Integer> idList = batchReqVo.getIdList();
            if (idList.isEmpty()) {
                result.error(ResultCode.DEV_ID_NULL);
            }
            //解绑
            for (Integer devId : idList) {
                unbindDev(devId);
            }
            deviceService.unbindDevBatch(idList);

            result.ok();
        } catch (NullPointerException e) {
            log.info(e.getMessage());
            return result.error(ResultCode.NULL_POINTER_ERROR);
        } catch (Exception e) {
            log.info(e.getMessage());
            result.error(ResultCode.DB_ERROR);
        }
        return result;
    }


    /**
     * 删除设备
     *
     * @param devId 要删除的id
     * @return 结果
     */
    @DeleteMapping(value = "/devices/{devId}")
    @LogOperate(description = "删除设备")
//    @RequiresPermissions("dev:delete")
    public Result delDevice(@PathVariable("devId") Integer devId) {
        Result result = new Result();
        try {
            Device device = deviceService.findById(devId);
            if (device == null) {
                return result.error(ResultCode.DEV_NOT_EXIST);
            }
            //删除设备时，如果是绑定状态，不能删除
            if (device.getStatus() == 1) {
                return result.error(ResultCode.DEV_IS_BIND_CANT_DEL);
            }
            deviceService.deleteById(devId);
            result.ok();
        } catch (Exception e) {
            log.info(e.getMessage());
            result.error(ResultCode.DB_ERROR);
        }
        return result;
    }

    /**
     * 批量删除设备
     *
     * @param batchReqVo 要删除的id集合
     * @return 结果
     */
    @PostMapping(value = "/batch")
    @LogOperate(description = "批量删除设备")
//    @RequiresPermissions("dev:delBatch")
    public Result delBatch(@Validated @RequestBody BatchReqVo batchReqVo) {
        Result result = new Result();
        if (batchReqVo.getIdList().isEmpty()) {
            return result.error(ResultCode.DEV_ID_NULL);
        }
        try {
            deviceService.deleteBatch(batchReqVo.getIdList());
            result.ok();
        } catch (Exception e) {
            log.info(e.getMessage());
            result.error(ResultCode.ERROR);
        }
        return result;
    }


    /**
     * 是否开启重复绑定功能
     *
     * @param devId 设备id
     * @return 状态
     */
    @PutMapping(value = "/repeat/{devId}")
    @LogOperate(description = "是否开启重复绑定功能")
//    @RequiresPermissions("dev:repeat")
    public Result repeat(@PathVariable("devId") Integer devId, @RequestParam(value = "enableMultipleBind") Integer enableMultipleBind) {
        Result result = new Result();
        try {
            if (devId == null) {
                return result.error(ResultCode.DEV_ID_NULL);
            }
            //更新数据
            result = unbindDev(devId);
        } catch (Exception e) {
            log.error(e.getMessage());
            result.error(ResultCode.DB_ERROR);
        }
        return result.ok();
    }

    /**
     * 新增设备
     *
     * @param device 新增对象属性
     * @return 结果
     */
    @PostMapping(value = "/devices")
    @LogOperate(description = "新增设备")
//    @RequiresPermissions("dev:add")
    public Result addDevice(@RequestBody Device device) {
        Result result;
        int code = DevCodeUtil.verifyDevCode(device.getDevCode());
        result = DevCodeUtil.parseCode(code);
        if (!Result.isOk(result)) {
            return result;
        }
        try {
            boolean exists = deviceService.existsByDevCode(device.getDevCode());
            if (exists) {
                return result.error(ResultCode.DEV_CID_EXIST);
            }
            device.setInventoryTime(new Date());
            //默认绑定时间为2019-01-01 00:00:00，方便判断重绑
            device.setBindTime(DateUtil.DEFAULT_TIME);
            deviceService.save(device);
            result.ok();
        } catch (Exception e) {
            log.info(e.getMessage());
            result.error(ResultCode.DB_ERROR);
        }
        return result;
    }

    /**
     * 更新设备
     *
     * @param devId 设备id
     * @return 状态
     */
    @PutMapping(value = "/devices/{devId}")
    @LogOperate(description = "更新设备")
//    @RequiresPermissions("dev:update")
    public Result updateDevice(@PathVariable("devId") Integer devId, @RequestBody Device device) {
        Result result;
        int code = DevCodeUtil.verifyDevCode(device.getDevCode());
        result = DevCodeUtil.parseCode(code);
        if (!Result.isOk(result)) {
            return result;
        }
        try {
            if (devId == null) {
                return result.error(ResultCode.DEV_ID_NULL);
            }
            //更新数据
            Device dbDevice = deviceService.findById(devId);
            if (dbDevice == null) {
                return result.error(ResultCode.DEV_NOT_EXIST);
            }
            //todo 设备暂时不更新数据
            // deviceService.updatedDevice(device);
            result.ok();
        } catch (Exception e) {
            log.info(e.getMessage());
            result.error(ResultCode.DB_ERROR);
        }
        return result;
    }

    /**
     * 获取设备列表
     *
     * @param devCode 设备code
     * @return 查询结果
     */
    @GetMapping(value = "/list")
//    @RequiresPermissions("dev:list")
    public DataResult deviceList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                 @RequestParam(value = "devCode", required = false, defaultValue = "") String devCode,
                                 @RequestParam(value = "online", required = false, defaultValue = "-1") Integer online,
                                 @RequestParam(value = "userId", required = false) Integer userId,
                                 @RequestParam(value = "bindAccount", required = false,defaultValue = "") String bindAccount) {
        DataResult result = new DataResult();
        Page<Device> devicePage = deviceService.findPage(page - 1, pageSize, devCode, online, userId,bindAccount);
        List<Device> deviceList = devicePage.getContent();
        if (!deviceList.isEmpty()) {
            //一般是分页后的数据集合，不会很多
            deviceService.getDevBindAccount(deviceList);
        }
        PageBean<Device> pageBean = new PageBean<>();
        pageBean.setCurPage(page);
        pageBean.setPageSize(pageSize);
        pageBean.setItemCounts(devicePage.getTotalElements());
        //按在线状态排序  在线在前
        List<Device> collect = deviceList.stream().sorted(Comparator.comparingInt(Device::getOnline).reversed()).collect(Collectors.toList());
        pageBean.setList(collect);
        result.ok();
        result.setDatas(pageBean);
        return result;
    }


    /**
     * 获取设备连接历史
     *
     * @param queryEmqClient 设备连接
     * @return 查询结果
     */
    @PostMapping(value = "/clientConnRecord")
//    @RequiresPermissions("dev:connect:records")
    public DataResult getClientConn(@RequestBody QueryEmqClient queryEmqClient) {
        DataResult result = new DataResult();
        try {
            //获取最近100条数据
            Page<EmqClient> devicePage = emqClientService.findAll(queryEmqClient.getPage() - 1, queryEmqClient.getPageSize()
                    , queryEmqClient);
            List<EmqClient> deviceList = devicePage.getContent();
            PageBean<EmqClient> pageBean = new PageBean<>();
            pageBean.setCurPage(queryEmqClient.getPage());
            pageBean.setPageSize(queryEmqClient.getPageSize());
            pageBean.setItemCounts(devicePage.getTotalElements() > 100 ? 100 : devicePage.getTotalElements());
            pageBean.setList(deviceList);
            result.ok();
            result.setDatas(pageBean);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            result.error(2005, "查询出错");
            return result;
        }
        return result;
    }

    /**
     * 通过devCode查询对象
     *
     * @param devCode 设备编码
     * @return 对象
     */
    @GetMapping(value = "/findByDevCode")
    Device findByDevCode(@RequestParam(value = "devCode") String devCode) {
        return deviceService.findByDevCode(devCode);
    }

    /**
     * 更新设备绑定状态
     *
     * @param status  设备状态
     * @param devCode 设备码
     * @return 绑定的结果
     */
    @GetMapping(value = "/updateBindStatus")
    int updateBindStatus(@RequestParam(value = "status") Integer status, @RequestParam(value = "devCode") String devCode) {
        return deviceService.updateBindStatus(status, devCode);
    }

    /**
     * 硬件手动解除绑定
     *
     * @param status  设备状态
     * @param devCode 设备码
     * @return 解除结果
     * @throws MqttException MqttException
     */
    @GetMapping(value = "/hardwareUnbind")
    int hardwareUnbind(@RequestParam(value = "status") Integer status, @RequestParam(value = "devCode") String devCode) throws MqttException {
        int hardwareUnbind = deviceService.hardwareUnbind(status, devCode);
        if (hardwareUnbind == 1) {
            mqttService.publish(1, ConstantUtil.DOWN_TOPIC + devCode, "5,1");
            log.info("硬件发送手动解绑:" + ConstantUtil.DOWN_TOPIC + devCode + "5,1");
        }
        return hardwareUnbind;
    }

    /**
     * 开机修改上线状态
     *
     * @param online  上线状态
     * @param devCode 设备码
     * @return int int
     * @throws MqttException MqttException
     */
    @GetMapping(value = "/updateOnline")
    int updateOnline(@RequestParam(value = "online") Integer online, @RequestParam(value = "devCode") String devCode) throws MqttException {
        return deviceService.updateOnline(online, devCode);
    }

    /**
     * 设备上线更新状态
     *
     * @param online  上线状态
     * @param ip      ip
     * @param devCode 设备码
     * @return int int
     * @throws MqttException MqttException
     */
    @GetMapping(value = "/updateOnlineAndIpAndTime")
    int updateOnlineAndIpAndTime(@RequestParam(value = "online") Integer online,
                                 @RequestParam(value = "ip") String ip,
                                 @RequestParam(value = "devCode") String devCode) throws MqttException {
        return deviceService.updateOnlineAndIpAndTime(online, ip, devCode);
    }

    /**
     * 更新滤芯寿命
     *
     * @param filterLife 滤芯寿命
     * @param devCode    设备码
     */
    @GetMapping(value = "/updateFilterLife")
    void updateFilterLife(@RequestParam(value = "filterLife") Integer filterLife, @RequestParam(value = "devCode") String devCode) {
        //更新滤芯寿命
        Device byDevCode = deviceService.findByDevCode(devCode);
        byDevCode.setFilterLifeTime(filterLife);
        deviceService.save(byDevCode);
        //更新为已使用
        filterInfoService.updateFilterCode(1, byDevCode.getFilterCode());
    }

    /**
     * 硬件解绑 滤芯归零 激活状态改为未激活
     *
     * @param devCode    设备号
     * @param filterLife 滤芯寿命
     * @param active     激活状态
     */
    @GetMapping(value = "/updateFilterLifeAndActive")
    void updateFilterLifeAndActive(@RequestParam(value = "devCode") String devCode, @RequestParam(value = "filterLife") Integer filterLife, @RequestParam(value = "active") int active) {
        deviceService.updateFilterLifeAndActive(devCode, filterLife, active);
    }

    /**
     * 查询设备是否存在
     *
     * @param devCode 设备号
     */
    @GetMapping(value = "/existsByDevCode")
    boolean existsByDevCode(@RequestParam(value = "devCode") String devCode) {
        return deviceService.existsByDevCode(devCode);
    }


    /**
     * 导入设备
     *
     * @param file ex文件
     * @return Result
     */
    @PostMapping("/import")
    @LogOperate(description = "导入设备")
    public Result addDevInfo(@RequestParam("file") MultipartFile file) {
        Result result = new Result();
        try {
            result = deviceService.batchImport(file);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new Result().error(ResultCode.EXCEL_IMPORT_FAIL);
        }
        return result;
    }

    /**
     * 批量导出
     *
     * @param batchReqVo 要导出的id
     * @return result
     */
    @PostMapping("/export")
    @LogOperate(description = "批量导出设备")
    public Result export(@Valid @RequestBody BatchLongReqVo batchReqVo) {
        try {
            deviceService.batchExport(batchReqVo.getIds());
        } catch (Exception e) {
            log.error(e.getMessage());
            return new Result().error(ResultCode.EXCEL_EXPORT_FAIL);
        }
        return new Result().ok();
    }


    /**
     * 下载模板
     */
    @GetMapping("/download")
    @LogOperate(description = "下载设备模板")
    public Result downloadTemplate() {
        try {
            deviceService.downloadTemplate();
        } catch (Exception e) {
            log.error(e.getMessage());
            return new Result().error(ResultCode.EXCEL_EXPORT_FAIL);
        }
        return new Result().ok();
    }

    /**
     * 通过手机号或者设备号查找设备
     */
    @GetMapping(value = "/search/msg")
    public Result searchDev(@RequestParam(value = "page", defaultValue = "1") Integer page,
                            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                            @NotBlank(message = "搜索条件不能为空") @RequestParam(value = "key") String key) {
        DataResult result = new DataResult();
        int length = key.trim().length();
        PageBean<DevVo> devPageBean;
        switch (length) {
            case 0:
                return result.error(ResultCode.PARAM_IS_NULL);
            //手机号查询
            case 11:
                devPageBean = deviceService.findDevMsgByPhone(key, page, pageSize);
                break;
            //设备号查询
            case 15:
                devPageBean = deviceService.findDevMsgByDevCode(key);
                break;
            default:
                return result.error(ResultCode.ERROR);
        }
        result.setDatas(devPageBean);
        return result.ok();
    }

}
