package com.etar.firmware.service.impl;

import com.etar.firmware.jpa.FirmwareRepository;
import com.etar.firmware.service.FirmwareService;
import entity.firmware.Firmware;
import entity.firmware.QueryFirmware;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Optional;

/**
 * Firmware服务类
 *
 * @author hzh
 * @since 2019-07-01
 */

@Service
public class FirmwareServiceImpl implements FirmwareService {

    @Autowired
    private FirmwareRepository firmwareRepository;

    /**
     * 保存对象
     *
     * @param firmware 对象
     *                 持久对象，或者对象集合
     */
    @Override
    public Firmware save(Firmware firmware) {
        return firmwareRepository.save(firmware);
    }

    /**
     * 删除对象
     *
     * @param firmware 对象
     */
    @Override
    public void delete(Firmware firmware) {
        firmwareRepository.delete(firmware);
    }

    @Override
    public void deleteById(Integer id) {
        firmwareRepository.deleteById(id);
    }

    @Override
    public void deleteAll(List<Firmware> list) {
        firmwareRepository.deleteAll(list);
    }


    /**
     * 通过id判断是否存在
     *
     * @param id 主键
     */
    @Override
    public boolean existsById(Integer id) {
        return firmwareRepository.existsById(id);
    }

    /**
     * 返回可用实体的数量
     */
    @Override
    public long count() {
        return firmwareRepository.count();
    }

    /**
     * 通过id查询
     *
     * @param id id
     * @return Firmware对象
     */
    @Override
    public Firmware findById(Integer id) {
        Optional<Firmware> optional = firmwareRepository.findById(id);
        boolean present = optional.isPresent();
        return present ? optional.get() : null;
    }

    /**
     * 分页查询
     * id处字符串为需要排序的字段，可以传多个，比如 "id","createTime",...
     *
     * @param page     页面
     * @param pageSize 页面大小
     * @return Page<Firmware>对象
     */
    @Override
    public Page<Firmware> findAll(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "id");
        return firmwareRepository.findAll(pageable);
    }

    @Override
    public List<Firmware> findList() {
        return firmwareRepository.findAll();
    }

    @Override
    public Page<Firmware> findAll(int page, int pageSize, QueryFirmware queryFirmware) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "id");
        //查询条件构造
        Specification<Firmware> spec = (Specification<Firmware>) (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (StringUtils.isNotBlank(queryFirmware.getName())) {
                predicate.getExpressions().add(cb.like(root.get("name").as(String.class), "%" + queryFirmware.getName() + "%"));
            }
            if (queryFirmware.getVersion() != null) {
                predicate.getExpressions().add(cb.equal(root.get("version").as(Integer.class), queryFirmware.getVersion()));
            }
            if (StringUtils.isNotBlank(queryFirmware.getOssUrl())) {
                predicate.getExpressions().add(cb.like(root.get("ossUrl").as(String.class), "%" + queryFirmware.getOssUrl() + "%"));
            }
            if (StringUtils.isNotBlank(queryFirmware.getRemark())) {
                predicate.getExpressions().add(cb.like(root.get("remark").as(String.class), "%" + queryFirmware.getRemark() + "%"));
            }
            return predicate;
        };
        return firmwareRepository.findAll(spec, pageable);
    }

    /**
     * 根据Id查询list
     *
     * @param ids id集合
     * @return list
     */
    @Override
    public List<Firmware> findAllById(List<Integer> ids) {
        return firmwareRepository.findAllById(ids);
    }

    @Override
    public boolean existsByName(String name) {
        return firmwareRepository.existsByName(name);
    }

    @Override
    public void delBatch(List<Integer> ids) {
        List<Firmware> byIds = firmwareRepository.findAllById(ids);
        if (!byIds.isEmpty()) {
            firmwareRepository.deleteInBatch(byIds);
        }
    }
}


