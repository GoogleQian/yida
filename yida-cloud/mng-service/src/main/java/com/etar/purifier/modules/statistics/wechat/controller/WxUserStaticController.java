package com.etar.purifier.modules.statistics.wechat.controller;

import com.etar.purifier.modules.statistics.wechat.service.WxUserStaticService;
import com.etar.purifier.modules.users.service.UserService;
import entity.common.entity.DataResult;
import entity.common.entity.Result;
import entity.wxuserstatic.WxUserStatic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WxUserStaticController层
 *
 * @author hzh
 * @since 2019-05-22
 */
@RestController
@RequestMapping(value = "/statics")
public class WxUserStaticController {
    private static Logger log = LoggerFactory.getLogger(WxUserStaticController.class);
    private final WxUserStaticService wxUserStaticService;
    private final UserService userService;

    @Autowired
    public WxUserStaticController(WxUserStaticService wxUserStaticService, UserService userService) {
        this.wxUserStaticService = wxUserStaticService;
        this.userService = userService;
    }

    @GetMapping(value = "/user/totals")
//    @RequiresPermissions("sys:wechat:static")
    public Result countWxUserTotalNum() {

        DataResult result = new DataResult();
        long userTotal = userService.count();
        Date endTime = new Date();
        Date startTime = DateUtil.getDayStartTime(endTime);
        long registerTotal = userService.countTodayRegisterUser(startTime, endTime);
        Map<String, Long> map = new HashMap<>(2);
        map.put("userTotal", userTotal);
        map.put("regTotal", registerTotal);
        result.setDatas(map);
        result.ok();
        log.info("获取微信用户总数：{}和新增注册用户数：{}", userTotal, registerTotal);
        return result;
    }

    @GetMapping(value = "/user/days")
//    @RequiresPermissions("sys:wechat:static")
    public Result countWxUserDateNum(@RequestParam(value = "startTime", required = false) String startTime,
                                     @RequestParam(value = "endTime", required = false) String endTime) {
        DataResult result = new DataResult();
        Date start = DateUtil.getDayEndTime(DateUtil.strToDate(startTime, DateUtil.DEFAULT_DAY_FORMAT));
        Date end = DateUtil.getDayEndTime(DateUtil.strToDate(endTime, DateUtil.DEFAULT_DAY_FORMAT));
        List<WxUserStatic> userStaticList = wxUserStaticService.findByCountTimeBetween(start, end);
        result.setDatas(userStaticList);
        result.ok();
        return result;
    }


}
