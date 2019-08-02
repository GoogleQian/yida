package com.etar.firmware.service;

import com.etar.base.IBaseService;
import entity.firmware.Firmware;
import entity.firmware.QueryFirmware;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Firmware接口
 *
 * @author hzh
 * @since 2019-07-01
 */

public interface FirmwareService extends IBaseService<Firmware, Integer> {

    /**
     * 按条件查询
     *
     * @param page          页数
     * @param pageSize      数量
     * @param queryFirmware 查询参数
     * @return Page
     */
    Page<Firmware> findAll(int page, int pageSize, QueryFirmware queryFirmware);

    /**
     * 根据Id查询list
     *
     * @param ids 要查询的ids
     * @return list
     */
    List<Firmware> findAllById(List<Integer> ids);

    /**
     * 通过名称查询
     *
     * @param name 名称
     * @return 存在结果
     */
    boolean existsByName(String name);

    /**
     * 批量删除
     *
     * @param ids
     */
    void delBatch(List<Integer> ids);
}


