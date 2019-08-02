package com.etar.purifier.modules.firmwaretask.controller;

import com.etar.purifier.common.annotation.LogOperate;
import com.etar.purifier.modules.firmwarestatic.service.FirmwareStaticService;
import com.etar.purifier.modules.firmwaretask.service.FirmwareTaskService;
import com.etar.purifier.utils.StringUtil;
import entity.common.entity.DataResult;
import entity.common.entity.PageBean;
import entity.common.entity.Result;
import entity.firmwarestatic.FirmwareStatic;
import entity.firmwaretask.FirmwareTask;
import entity.firmwaretask.QueryFirmwareTask;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import utils.ResultCode;

import java.util.ArrayList;
import java.util.List;

/**
 * FirmwareTaskController层
 *
 * @author hzh
 * @since 2019-07-17
 */
@RestController
@RequestMapping(value = "yida/firmwareTasks")
public class FirmwareTaskController {
    private static Logger log = LoggerFactory.getLogger(FirmwareTaskController.class);
    private final FirmwareTaskService firmwareTaskService;
    private final FirmwareStaticService firmwareStaticService;

    @Autowired
    public FirmwareTaskController(FirmwareTaskService firmwareTaskService, FirmwareStaticService firmwareStaticService) {
        this.firmwareTaskService = firmwareTaskService;
        this.firmwareStaticService = firmwareStaticService;
    }

    /**
     * 删除固件
     *
     * @param ids ids
     */
    @DeleteMapping(value = "/{ids}")
    @LogOperate(description = "删除固件任务")
    public Result delFirmware(@PathVariable("ids") String ids) {
        log.info("进入删除固件任务接口");
        Result result = new Result();
        if (StringUtils.isBlank(ids)) {
            return result.error(ResultCode.FIRMWARE_ID_IS_NULL);
        }
        try {
            int ret = firmwareTaskService.delBatch(StringUtil.stringToIntegerList(ids));
            if (ret == 2) {
                return result.error(ResultCode.FIRMWARE_IS_UP);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return result.error(ResultCode.DB_ERROR);
        }
        return result.ok();
    }


    /**
     * 通过id查找对象
     *
     * @param id id
     * @return FirmwareTask 对象
     */
    @GetMapping(value = "/{id}")
    public Result findById(@PathVariable("id") Integer id) {
        DataResult result = new DataResult();
        try {
            result.setDatas(firmwareTaskService.findById(id));
        } catch (Exception e) {
            log.error(e.getMessage());
            return result.error(2004, "不存在");
        }
        return result.ok();
    }

    /**
     * 分页查询
     *
     * @return Page<FirmwareTask> 对象
     */
    @PostMapping(value = "/pages")
    public Result findByPage(@RequestBody QueryFirmwareTask queryFirmwareTask) {

        DataResult result = new DataResult();
        try {
            int page = queryFirmwareTask.getPage();
            int pageSize = queryFirmwareTask.getPageSize();
            Page<FirmwareTask> all = firmwareTaskService.findAll(page - 1, pageSize, queryFirmwareTask);
            PageBean<FirmwareTask> pageBean = new PageBean<>();
            if (all == null || 0 == all.getTotalElements()) {
                pageBean.setList(new ArrayList<>());
                result.setDatas(pageBean);
                return result.ok();
            }
            pageBean.setCurPage(page);
            pageBean.setItemCounts(all.getTotalElements());
            pageBean.setPageSize(pageSize);
            pageBean.setList(all.getContent());
            result.setDatas(pageBean);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return result.error(2005, "查询出错");
        }
        return result.ok();
    }

}
