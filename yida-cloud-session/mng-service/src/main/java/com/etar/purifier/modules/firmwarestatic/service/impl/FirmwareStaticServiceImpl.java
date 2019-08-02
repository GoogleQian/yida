package com.etar.purifier.modules.firmwarestatic.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.etar.purifier.utils.DevCodeUtil;
import entity.common.entity.PageBean;
import entity.dev.Device;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import com.etar.purifier.modules.firmwarestatic.service.FirmwareStaticService;
import entity.firmwarestatic.FirmwareStatic;
import entity.firmwarestatic.QueryFirmwareStatic;
import com.etar.purifier.modules.firmwarestatic.jpa.FirmwareStaticRepository;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * FirmwareStatic服务类
 *
 * @author hzh
 * @since 2019-07-17
 */

@Service
public class FirmwareStaticServiceImpl implements FirmwareStaticService {

    @Autowired
    private FirmwareStaticRepository firmwareStaticRepository;

    /**
     * 保存对象
     *
     * @param firmwareStatic 对象
     *                       持久对象，或者对象集合
     */
    @Override
    public FirmwareStatic save(FirmwareStatic firmwareStatic) {
        return firmwareStaticRepository.save(firmwareStatic);
    }

    /**
     * 删除对象
     *
     * @param firmwareStatic 对象
     */
    @Override
    public void delete(FirmwareStatic firmwareStatic) {
        firmwareStaticRepository.delete(firmwareStatic);
    }

    @Override
    public void deleteAll(List<FirmwareStatic> list) {
        firmwareStaticRepository.deleteAll(list);
    }

    @Override
    public void deleteById(Integer id) {
        firmwareStaticRepository.deleteById(id);
    }

    @Override
    public List<FirmwareStatic> findList() {
        return firmwareStaticRepository.findAll();
    }

    /**
     * 通过id判断是否存在
     *
     * @param id
     */
    @Override
    public boolean existsById(Integer id) {
        return firmwareStaticRepository.existsById(id);
    }

    /**
     * 返回可用实体的数量
     */
    @Override
    public long count() {
        return firmwareStaticRepository.count();
    }

    /**
     * 通过id查询
     *
     * @param id id
     * @return FirmwareStatic对象
     */
    @Override
    public FirmwareStatic findById(Integer id) {
        Optional<FirmwareStatic> optional = firmwareStaticRepository.findById(id);
        boolean present = optional.isPresent();
        return present ? optional.get() : null;
    }

    /**
     * 分页查询
     * id处字符串为需要排序的字段，可以传多个，比如 "id","createTime",...
     *
     * @param page     页面
     * @param pageSize 页面大小
     * @return Page<FirmwareStatic>对象
     */
    @Override
    public Page<FirmwareStatic> findAll(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "id");
        return firmwareStaticRepository.findAll(pageable);
    }

    @Override
    public Page<FirmwareStatic> findAll(int page, int pageSize, QueryFirmwareStatic queryFirmwareStatic) {
        //过滤自己的广告
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "id");
        //查询条件构造
        Specification<FirmwareStatic> spec = new Specification<FirmwareStatic>() {
            @Override
            public Predicate toPredicate(Root<FirmwareStatic> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (queryFirmwareStatic.getFmId() != null) {
                    predicate.getExpressions().add(cb.equal(root.get("fmId").as(Integer.class), queryFirmwareStatic.getFmId()));
                }

                if (StringUtils.isNotBlank(queryFirmwareStatic.getFmName())) {
                    predicate.getExpressions().add(cb.like(root.get("firmwareName").as(String.class), "%" + queryFirmwareStatic.getFmName() + "%"));
                }
                if (StringUtils.isNotBlank(queryFirmwareStatic.getDevCode())) {
                    predicate.getExpressions().add(cb.like(root.get("devCode").as(String.class), "%" + queryFirmwareStatic.getDevCode() + "%"));
                }
                return predicate;
            }

        };
        return firmwareStaticRepository.findAll(spec, pageable);
    }

    /**
     * 根据Id查询list
     *
     * @param ids id集合
     * @return list
     */
    @Override
    public List<FirmwareStatic> findAllById(List<Integer> ids) {
        return firmwareStaticRepository.findAllById(ids);
    }

    @Override
    public void getFirmwareMsg(List<Device> deviceList) {
        List<String> devCodes = DevCodeUtil.getDevCodes(deviceList);
        List<FirmwareStatic> list = firmwareStaticRepository.findTop1ByDevCodeInOrderByReportTimeDesc(devCodes);
        Map<String, FirmwareStatic> map = list.stream()
                .collect(Collectors.toMap(FirmwareStatic::getDevCode, firmwareStatic -> firmwareStatic));
        // 合并
        deviceList.forEach(n -> {
            // 如果devCode一致
            if (map.containsKey(n.getDevCode())) {
                FirmwareStatic fs = map.get(n.getDevCode());
                // 把数量复制过去
                n.setFirmwareName(fs.getFmName());
                n.setFirmwareVersion(fs.getFmVersion());
            }
        });
    }


    @Override
    public PageBean<FirmwareStatic> findPages(QueryFirmwareStatic query) {
        PageBean<FirmwareStatic> pageBean = new PageBean<>();
        List<FirmwareStatic> list;
        Long count;
        final Integer page = query.getPage();
        final Integer pageSize = query.getPageSize();
        int start = (page - 1) * pageSize;
        final String devCode = query.getDevCode();
        if (StringUtils.isNotBlank(devCode)) {
            count = firmwareStaticRepository.countListDataByDevCode(query.getFmId(), "%" + devCode + "%");
            list = firmwareStaticRepository.findListDataByDevCode(start, pageSize, query.getFmId(), "%" + devCode + "%");
        } else {
            count = firmwareStaticRepository.countListData(query.getFmId());
            list = firmwareStaticRepository.findListData(start, pageSize, query.getFmId());
    }
        pageBean.setList(list);
        pageBean.setItemCounts(count);
        pageBean.setCurPage(page);
        pageBean.setPageSize(pageSize);
        return pageBean;
    }

    @Override
    public List<FirmwareStatic> findByFmId(Integer fmId) {
        return firmwareStaticRepository.findByFmId(fmId);
    }

    @Override
    public List<FirmwareStatic> findByFmIdIn(List<Integer> fmIds) {
        return firmwareStaticRepository.findByFmIdIn(fmIds);
    }
}


