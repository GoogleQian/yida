package com.etar.purifier.modules.statistics;

import com.etar.purifier.modules.dev.service.DeviceService;
import com.etar.purifier.modules.statistics.device.service.DeviceStaticService;
import com.etar.purifier.modules.statistics.wechat.service.WxUserStaticService;
import com.etar.purifier.modules.users.service.UserService;
import entity.devicestatic.DeviceStatic;
import entity.wxuserstatic.WxUserStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author hzh
 * @version 1.0
 * @date 2019/5/22 15:17
 */
@Component
@EnableScheduling
public class TimerService {
    private final WxUserStaticService wxUserStaticService;
    private final UserService userService;
    private final DeviceStaticService deviceStaticService;
    private final DeviceService deviceService;

    @Autowired
    public TimerService(WxUserStaticService wxUserStaticService, UserService userService, DeviceStaticService deviceStaticService, DeviceService deviceService) {
        this.wxUserStaticService = wxUserStaticService;
        this.userService = userService;
        this.deviceStaticService = deviceStaticService;
        this.deviceService = deviceService;
    }

    /**
     * 统计设备
     */
    @Scheduled(cron = "58 59 23 * * *") //cron = 0/20 * * * * *
    public void contDayRegDevice() {
        Date now = new Date();
        //当天开始时间
        Date startTime = DateUtil.getDayStartTime(now);
        //当天结束时间
        Date endTime = DateUtil.getDayEndTime(now);

        //统计当天在线设备数
        long onlineCount = deviceService.countTodayOnline();
        //统计当天绑定设备数
        long bindCount = deviceService.countTodayBind(startTime, endTime);
        //统计当天激活设备数
        long activeCount = deviceService.countTodayActive(startTime, endTime);
        //统计当天入库数
        long inventoryCount = deviceService.countInventoryc(startTime, endTime);
        //统计当前设备总数
        long totalDeviceCount = deviceService.count();

        DeviceStatic deviceStatic = new DeviceStatic();
        deviceStatic.setOnlineCount(onlineCount);
        deviceStatic.setActiveCount(activeCount);
        deviceStatic.setStatusCount(bindCount);
        deviceStatic.setDailyCount(inventoryCount);
        deviceStatic.setTotalCount(totalDeviceCount);
        deviceStatic.setDate(startTime);
        DeviceStatic deviceStatic1 = deviceStaticService.findByDate(startTime);
        if (deviceStatic1 == null) {
            deviceStaticService.save(deviceStatic);
        }
    }

    /**
     * 统计用户
     */
    @Scheduled(cron = "59 59 23 * * *")
    public void countDayRegUser() {
        Date now = new Date();
        //当天开始时间
        Date startTime = DateUtil.getDayStartTime(now);
        //当天结束时间
        Date endTime = DateUtil.getDayEndTime(now);
        //统计当天注册用户数
        long registerTotal = userService.countTodayRegisterUser(startTime, endTime);
        //统计当前注册用户总数
        long userTotal = userService.count();

        WxUserStatic wxUserStatic = new WxUserStatic();
        //以每天开会时间保存
        wxUserStatic.setCountTime(startTime);
        wxUserStatic.setDailyNum(registerTotal);
        wxUserStatic.setTotalNum(userTotal);
        WxUserStatic wxUserStatic1 = wxUserStaticService.findByCountTime(startTime);
        //如果按时间查不出结果则保存数据
        if (wxUserStatic1 == null) {
            wxUserStaticService.save(wxUserStatic);
        }
    }
}
