package com.etar.purifier.modules.firmwaretask.service;

import com.etar.purifier.base.IBaseService;
import entity.firmware.Firmware;
import entity.firmwaretask.FirmwareTask;
import entity.firmwaretask.QueryFirmwareTask;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * <p>
 * FirmwareTask接口
 * </p>
 *
 * @author hzh
 * @since 2019-07-17
 */

public interface FirmwareTaskService extends IBaseService<FirmwareTask, Integer> {

    /**
     * 按条件查询
     *
     * @param page              页数
     * @param pageSize          数量
     * @param queryFirmwareTask 查询条件
     * @return Page
     */
    Page<FirmwareTask> findAll(int page, int pageSize, QueryFirmwareTask queryFirmwareTask);

    /**
     * 根据Id查询list
     *
     * @return 查询list
     */
    List<FirmwareTask> findAllById(List<Integer> ids);

    /**
     * 批量删除
     *
     * @param ids id集合
     */
    int delBatch(List<Integer> ids);

    /**
     * 批量保存
     *
     * @param list 保存结果
     */
    void saveAll(List<FirmwareTask> list);

    /**
     * 通过固件保存固件任务
     *
     * @param firmware 固件
     */
    void saveByFirmware(Firmware firmware);

    /**
     * 通过固件ids查询
     *
     * @param fmIds 固件ids
     * @return 查询list
     */
    List<FirmwareTask> findByFmIdIn(List<Integer> fmIds);

}


