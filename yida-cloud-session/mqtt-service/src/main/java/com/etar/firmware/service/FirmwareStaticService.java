package com.etar.firmware.service;

import com.etar.base.IBaseService;
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
     * @param page     页数
     * @param pageSize 数量
     * @param
     * @return Page
     */
    Page<FirmwareStatic> findAll(int page, int pageSize, QueryFirmwareStatic queryFirmwareStatic);

    /**
     * 根据Id查询list
     *
     * @return
     */
    List<FirmwareStatic> findAllById(List<Integer> ids);

    void saveOnRespone(FirmwareStatic firmwareStatic);
}


