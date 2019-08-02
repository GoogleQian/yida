package com.etar.purifier.modules.firmwarestatic.controller;

import com.etar.purifier.modules.dev.service.DeviceService;
import com.etar.purifier.modules.firmwarestatic.service.FirmwareStaticService;
import com.etar.purifier.modules.firmwaretask.service.FirmwareTaskService;
import com.etar.purifier.utils.NumUtil;
import entity.common.entity.DataResult;
import entity.common.entity.PageBean;
import entity.common.entity.Result;
import entity.firmwarestatic.FirmwareStatic;
import entity.firmwarestatic.QueryFirmwareStatic;
import entity.firmwaretask.FirmwareTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * FirmwareStaticController层
 *
 * @author hzh
 * @since 2019-07-17
 */
@RestController
@RequestMapping(value = "yida/firmwareStatics")
public class FirmwareStaticController {
    private static Logger log = LoggerFactory.getLogger(FirmwareStaticController.class);
    private final FirmwareStaticService firmwareStaticService;
    private final DeviceService deviceService;
    private final FirmwareTaskService firmwareTaskService;

    @Autowired
    public FirmwareStaticController(FirmwareStaticService firmwareStaticService, DeviceService deviceService, FirmwareTaskService firmwareTaskService) {
        this.firmwareStaticService = firmwareStaticService;
        this.deviceService = deviceService;
        this.firmwareTaskService = firmwareTaskService;
    }

    /**
     * 分页查询
     *
     * @return Page<FirmwareStatic> 对象
     */
    @GetMapping(value = "/rate")
    public Result pushRate() {

        Result result = new Result();
        try {
            List<FirmwareTask> list = firmwareTaskService.findList();
            Integer devTolNum = deviceService.countByVersionNum(2);
            for (FirmwareTask firmwareTask : list) {
                List<FirmwareStatic> firmwareStatics = firmwareStaticService.findByFmId(firmwareTask.getFmId());
                firmwareTask.setPushRate(NumUtil.get2Double(firmwareStatics.size(), devTolNum));
            }
            firmwareTaskService.saveAll(list);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return result.error(2005, "统计推送率出错");
        }
        return result.ok();
    }

    /**
     * 分页查询
     *
     * @return Page<FirmwareStatic> 对象
     */
    @PostMapping(value = "/pages")
    public Result findByPage(@RequestBody QueryFirmwareStatic queryFirmwareStatic) {

        DataResult result = new DataResult();
        try {
            PageBean<FirmwareStatic> pageBean = firmwareStaticService.findPages(queryFirmwareStatic);
            result.setDatas(pageBean);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return result.error(2005, "查询出错");
        }
        return result.ok();
    }

}
