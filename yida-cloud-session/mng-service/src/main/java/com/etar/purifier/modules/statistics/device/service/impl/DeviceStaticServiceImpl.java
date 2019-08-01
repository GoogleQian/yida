package com.etar.purifier.modules.statistics.device.service.impl;

import entity.devicestatic.DeviceStatic;
import entity.devicestatic.QueryDeviceStatic;
import com.etar.purifier.modules.statistics.device.jpa.DeviceStaticRepository;
import com.etar.purifier.modules.statistics.device.service.DeviceStaticService;
import entity.wxuserstatic.WxUserStatic;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * DeviceStatic服务类
 * </p>
 *
 * @author gmq
 * @since 2019-05-22
 */

@Service
public class DeviceStaticServiceImpl implements DeviceStaticService {

    @Autowired
    private DeviceStaticRepository deviceStaticRepository;

    /**
     * 保存对象
     *
     * @param deviceStatic 对象
     *                     持久对象，或者对象集合
     */
    @Override
    public DeviceStatic save(DeviceStatic deviceStatic) {
        return deviceStaticRepository.save(deviceStatic);
    }

    /**
     * 删除对象
     *
     * @param deviceStatic 对象
     */
    @Override
    public void delete(DeviceStatic deviceStatic) {
        deviceStaticRepository.delete(deviceStatic);
    }

    @Override
    public void deleteAll(List<DeviceStatic> list) {
        deviceStaticRepository.deleteAll(list);
    }

    @Override
    public void deleteById(Long id) {
        deviceStaticRepository.deleteById(id);
    }

    @Override
    public List<DeviceStatic> findList() {
        return deviceStaticRepository.findAll();
    }

    /**
     * 通过id判断是否存在
     *
     * @param id
     */
    @Override
    public boolean existsById(Long id) {
        return deviceStaticRepository.existsById(id);
    }

    /**
     * 返回可用实体的数量
     */
    @Override
    public long count() {
        return deviceStaticRepository.count();
    }

    /**
     * 通过id查询
     *
     * @param id id
     * @return WxUserStatic对象
     */
    @Override
    public DeviceStatic findById(Long id) {
        Optional<DeviceStatic> optional = deviceStaticRepository.findById(id);
        boolean present = optional.isPresent();
        return present ? optional.get() : null;
    }

    /**
     * 分页查询
     * id处字符串为需要排序的字段，可以传多个，比如 "id","createTime",...
     *
     * @param page     页面
     * @param pageSize 页面大小
     * @return Page<WxUserStatic>对象
     */
    @Override
    public Page<DeviceStatic> findAll(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "id");
        return deviceStaticRepository.findAll(pageable);
    }


    @Override
    public Page<DeviceStatic> findAll(int page, int pageSize, QueryDeviceStatic queryDeviceStatic) {
        //过滤自己的广告
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "id");
        //查询条件构造
        Specification<DeviceStatic> spec = new Specification<DeviceStatic>() {
            @Override
            public Predicate toPredicate(Root<DeviceStatic> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (queryDeviceStatic.getId() != null) {
                    predicate.getExpressions().add(cb.equal(root.get("id").as(Integer.class), queryDeviceStatic.getId()));
                }

                if (queryDeviceStatic.getDailyCount() != null) {
                    predicate.getExpressions().add(cb.equal(root.get("dailyCount").as(Integer.class), queryDeviceStatic.getDailyCount()));
                }

                if (queryDeviceStatic.getTotalCount() != null) {
                    predicate.getExpressions().add(cb.equal(root.get("totalCount").as(Integer.class), queryDeviceStatic.getTotalCount()));
                }

                if (queryDeviceStatic.getOnlineCount() != null) {
                    predicate.getExpressions().add(cb.equal(root.get("onlineCount").as(Integer.class), queryDeviceStatic.getOnlineCount()));
                }

                if (queryDeviceStatic.getStatusCount() != null) {
                    predicate.getExpressions().add(cb.equal(root.get("statusCount").as(Integer.class), queryDeviceStatic.getStatusCount()));
                }

                if (queryDeviceStatic.getActiveCount() != null) {
                    predicate.getExpressions().add(cb.equal(root.get("activeCount").as(Integer.class), queryDeviceStatic.getActiveCount()));
                }
                return predicate;
            }

        };
        return deviceStaticRepository.findAll(spec, pageable);
    }

    @Override
    public DeviceStatic getTotalStatic() {
        DeviceStatic deviceStatic =deviceStaticRepository.getTotalStatic();
        return deviceStatic;
    }
    @Override
    public List<DeviceStatic> findByCountTimeBetween(String startTime, String endTime) {
        return deviceStaticRepository.findByCountTimeBetween(startTime, endTime);
    }


}


