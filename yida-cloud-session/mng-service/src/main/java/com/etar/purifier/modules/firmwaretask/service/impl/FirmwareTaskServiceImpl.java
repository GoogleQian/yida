package com.etar.purifier.modules.firmwaretask.service.impl;

import com.etar.purifier.modules.firmwarestatic.service.FirmwareStaticService;
import com.etar.purifier.modules.firmwaretask.jpa.FirmwareTaskRepository;
import com.etar.purifier.modules.firmwaretask.service.FirmwareTaskService;
import com.etar.purifier.utils.StringUtil;
import entity.firmware.Firmware;
import entity.firmwarestatic.FirmwareStatic;
import entity.firmwaretask.FirmwareTask;
import entity.firmwaretask.QueryFirmwareTask;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import utils.ResultCode;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * FirmwareTask服务类
 *
 * @author hzh
 * @since 2019-07-17
 */

@Service
public class FirmwareTaskServiceImpl implements FirmwareTaskService {

    private final FirmwareTaskRepository firmwareTaskRepository;
    private final FirmwareStaticService firmwareStaticService;

    @Autowired
    public FirmwareTaskServiceImpl(FirmwareTaskRepository firmwareTaskRepository, FirmwareStaticService firmwareStaticService) {
        this.firmwareTaskRepository = firmwareTaskRepository;
        this.firmwareStaticService = firmwareStaticService;
    }

    /**
     * 保存对象
     *
     * @param firmwareTask 对象
     *                     持久对象，或者对象集合
     */
    @Override
    public FirmwareTask save(FirmwareTask firmwareTask) {
        return firmwareTaskRepository.save(firmwareTask);
    }

    /**
     * 删除对象
     *
     * @param firmwareTask 对象
     */
    @Override
    public void delete(FirmwareTask firmwareTask) {
        firmwareTaskRepository.delete(firmwareTask);
    }

    @Override
    public void deleteById(Integer id) {
        firmwareTaskRepository.deleteById(id);
    }

    @Override
    public void deleteAll(List<FirmwareTask> list) {
        firmwareTaskRepository.deleteAll(list);
    }


    /**
     * 通过id判断是否存在
     *
     * @param id
     */
    @Override
    public boolean existsById(Integer id) {
        return firmwareTaskRepository.existsById(id);
    }

    /**
     * 返回可用实体的数量
     */
    @Override
    public long count() {
        return firmwareTaskRepository.count();
    }

    /**
     * 通过id查询
     *
     * @param id id
     * @return FirmwareTask对象
     */
    @Override
    public FirmwareTask findById(Integer id) {
        Optional<FirmwareTask> optional = firmwareTaskRepository.findById(id);
        boolean present = optional.isPresent();
        return present ? optional.get() : null;
    }

    /**
     * 分页查询
     * id处字符串为需要排序的字段，可以传多个，比如 "id","createTime",...
     *
     * @param page     页面
     * @param pageSize 页面大小
     * @return Page<FirmwareTask>对象
     */
    @Override
    public Page<FirmwareTask> findAll(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "id");
        return firmwareTaskRepository.findAll(pageable);
    }

    @Override
    public List<FirmwareTask> findList() {
        return firmwareTaskRepository.findAll();
    }

    @Override
    public Page<FirmwareTask> findAll(int page, int pageSize, QueryFirmwareTask queryFirmwareTask) {
        //过滤自己的广告
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "id");
        //查询条件构造
        Specification<FirmwareTask> spec = (Specification<FirmwareTask>) (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (StringUtils.isNotBlank(queryFirmwareTask.getFmName())) {
                predicate.getExpressions().add(cb.like(root.get("fmName").as(String.class), "%" + queryFirmwareTask.getFmName() + "%"));
            }
            return predicate;
        };
        return firmwareTaskRepository.findAll(spec, pageable);
    }

    /**
     * 根据Id查询list
     *
     * @param ids id集合
     * @return list
     */
    @Override
    public List<FirmwareTask> findAllById(List<Integer> ids) {
        return firmwareTaskRepository.findAllById(ids);
    }

    @Override
    public int delBatch(List<Integer> ids) {
        List<FirmwareTask> byIds = firmwareTaskRepository.findAllById(ids);
        List<Integer> fmIds = new ArrayList<>();
        for (FirmwareTask byId : byIds) {
            fmIds.add(byId.getFmId());
        }
        List<FirmwareStatic> fsList = firmwareStaticService.findByFmIdIn(fmIds);
        if (fsList.size() != 0) {
            return 2;
        }
        if (!byIds.isEmpty()) {
            firmwareTaskRepository.deleteInBatch(byIds);
        }
        return 1;
    }

    @Override
    public void saveAll(List<FirmwareTask> list) {
        firmwareTaskRepository.saveAll(list);
    }

    @Override
    public void saveByFirmware(Firmware firmware) {
        FirmwareTask firmwareTask = firmwareTaskRepository.findByFmId(firmware.getId());
        if (firmwareTask == null) {
            firmwareTask = new FirmwareTask();
        }
        //保证在固件修改后，能同步
        firmwareTask.setFmId(firmware.getId());
        firmwareTask.setFmName(firmware.getName());
        firmwareTask.setFmVersion(firmware.getVersion());
        firmwareTask.setPushTime(new Date());
        save(firmwareTask);
    }

    @Override
    public List<FirmwareTask> findByFmIdIn(List<Integer> fmIds) {
        return firmwareTaskRepository.findByFmIdIn(fmIds);
    }
}


