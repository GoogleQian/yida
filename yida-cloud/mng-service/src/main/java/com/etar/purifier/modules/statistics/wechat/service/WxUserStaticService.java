package com.etar.purifier.modules.statistics.wechat.service;

import com.etar.purifier.base.IBaseService;
import entity.wxuserstatic.QueryWxUserStatic;
import entity.wxuserstatic.WxUserStatic;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * WxUserStatic接口
 * </p>
 *
 * @author hzh
 * @since 2019-05-22
 */

public interface WxUserStaticService extends IBaseService<WxUserStatic, Long> {

    /**
     * 按条件查询
     *
     * @param page     页数
     * @param pageSize 数量
     * @return Page
     */
    Page<WxUserStatic> findAll(int page, int pageSize, QueryWxUserStatic queryWxUserStatic);

    /**
     * 根据Id查询list
     */
    List<WxUserStatic> findAllById(List<Long> ids);

    /**
     * 通过统计时间查询
     *
     * @param countTime 统计日期
     * @return 返回结果
     */
    WxUserStatic findByCountTime(Date countTime);

    /**
     * 时间段内查询
     *
     * @param startTime 选择的开始日期
     * @param endTime   选择的结束日期
     * @return 时间段内查询结果
     */
    List<WxUserStatic> findByCountTimeBetween(Date startTime, Date endTime);
}


