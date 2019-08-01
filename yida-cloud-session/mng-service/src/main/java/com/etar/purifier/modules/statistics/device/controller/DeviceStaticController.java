package com.etar.purifier.modules.statistics.device.controller;

import entity.devicestatic.DeviceStatic;
import entity.devicestatic.QueryDeviceStatic;
import com.etar.purifier.modules.statistics.device.service.DeviceStaticService;
import entity.common.entity.DataResult;
import entity.common.entity.PageBean;
import entity.common.entity.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DeviceStaticController层
 *
 * @author wzq
 * @since 2019-05-22
 */
@RestController
@RequestMapping(value = "/statics")
public class DeviceStaticController {
    private static Logger log = LoggerFactory.getLogger(DeviceStaticController.class);
    @Autowired
    private DeviceStaticService deviceStaticService;

    @GetMapping(value = "/device/days")
//    @RequiresPermissions("sys:device:static")
    public Result countWxUserDateNum(@RequestParam(value = "startTime", required = false) String startTime, @RequestParam(value = "endTime", required = false) String endTime) {
        DataResult result = new DataResult();
        List<DeviceStatic> userStaticList = deviceStaticService.findByCountTimeBetween(startTime, endTime);
        result.setDatas(userStaticList);
        result.ok();
        return result;
    }

    /**
     * 实时统计
     *
     * @return DeviceStatic 对象
     */
    @PostMapping(value = "/device/total")
//    @RequiresPermissions("sys:device:static")
    public Result getTotalStatic() {
        DataResult result = new DataResult();
        try {
            DeviceStatic deviceStatic = deviceStaticService.getTotalStatic();
            result.setDatas(deviceStatic);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return result.error(2005, "查询出错");
        }
        return result.ok();
    }
}
