package com.etar.purifier.modules.statistics.device.service;


import com.etar.purifier.base.IBaseService;
import entity.devicestatic.DeviceStatic;
import entity.devicestatic.QueryDeviceStatic;
import entity.wxuserstatic.WxUserStatic;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * DeviceStatic接口
 * </p>
 *
 * @author gmq
 * @since 2019-05-22
 */

public interface DeviceStaticService extends IBaseService<DeviceStatic, Long> {

    /**
     * 按条件查询
     *
     * @param page     页数
     * @param pageSize 数量
     * @param
     * @return Page
     */
    Page<DeviceStatic> findAll(int page, int pageSize, QueryDeviceStatic queryDeviceStatic);


    DeviceStatic getTotalStatic();

    /**
     * 时间段内查询
     *
     * @param startTime 选择的开始日期
     * @param endTime   选择的结束日期
     * @return 时间段内查询结果
     */
    List<DeviceStatic> findByCountTimeBetween(String startTime, String endTime);

    /**
     * 通过统计时间查询
     *
     * @param date 统计日期
     * @return 返回结果
     */
    DeviceStatic findByDate(Date date );
}


