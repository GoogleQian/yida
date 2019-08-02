package com.etar.purifier.modules.statistics.device.controller;

import com.etar.purifier.modules.statistics.wechat.service.WxUserStaticService;
import entity.devicestatic.DeviceStatic;
import com.etar.purifier.modules.statistics.device.service.DeviceStaticService;
import entity.common.entity.DataResult;
import entity.common.entity.Result;
import entity.wxuserstatic.WxUserStatic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    @Autowired
    private WxUserStaticService wxUserStaticService;


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
            Map<String, Object> map = new HashMap<>(3);
            map.put("devOverview", deviceStatic);
            Date date = new Date();
            Date dateBefore = DateUtil.getDateBefore(date, 6);
            String end = DateUtil.format(date, DateUtil.DEFAULT_DAY_FORMAT);
            String start = DateUtil.format(dateBefore, DateUtil.DEFAULT_DAY_FORMAT);
            //用户七天新增记录
            List<WxUserStatic> userStaticList = wxUserStaticService.findByCountTimeBetween(dateBefore, date);
            //设备七天新增记录
            List<DeviceStatic> devStaticList = deviceStaticService.findByCountTimeBetween(start, end);
            map.put("devDays", devStaticList);
            map.put("userDays", userStaticList);
            result.setDatas(map);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return result.error(2005, "查询出错");
        }
        return result.ok();
    }
}


