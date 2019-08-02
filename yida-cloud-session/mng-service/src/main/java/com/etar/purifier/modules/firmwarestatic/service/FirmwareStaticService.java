package com.etar.purifier.modules.firmwarestatic.service;

import com.etar.purifier.base.IBaseService;
import entity.common.entity.PageBean;
import entity.dev.Device;
import entity.firmwarestatic.FirmwareStatic;
import entity.firmwarestatic.QueryFirmwareStatic;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * <p>
 * FirmwareStatic接口
 * </p>
 *
 * @author hzh
 * @since 2019-07-17
 */

public interface FirmwareStaticService extends IBaseService<FirmwareStatic, Integer> {

    /**
     * 按条件查询
     *
     * @param page                页数
     * @param pageSize            数量
     * @param queryFirmwareStatic 查询条件对象
     * @return Page
     */
    Page<FirmwareStatic> findAll(int page, int pageSize, QueryFirmwareStatic queryFirmwareStatic);

    /**
     * 根据Id查询list
     *
     * @return 集合对象
     */
    List<FirmwareStatic> findAllById(List<Integer> ids);

    /**
     * 获取设备固件版本信息
     *
     * @param deviceList 设备列表
     */
    void getFirmwareMsg(List<Device> deviceList);

    /**
     * 分页查询
     *
     * @param queryFirmwareStatic 查询条件
     * @return 分页对象
     */
    PageBean<FirmwareStatic> findPages(QueryFirmwareStatic queryFirmwareStatic);

    /**
     * 通过固件id查询
     *
     * @param fmId 固件id
     * @return 结果
     */
    List<FirmwareStatic> findByFmId(Integer fmId);

    /**
     * 通过固件ids查询
     *
     * @param fmIds 固件ids
     * @return 查询list
     */
    List<FirmwareStatic> findByFmIdIn(List<Integer> fmIds);
}


