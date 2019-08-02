package com.etar.purifier.modules.statistics.wechat.service.impl;

import com.etar.purifier.modules.statistics.wechat.jpa.WxUserStaticRepository;
import com.etar.purifier.modules.statistics.wechat.service.WxUserStaticService;
import entity.wxuserstatic.QueryWxUserStatic;
import entity.wxuserstatic.WxUserStatic;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * WxUserStatic服务类
 *
 * @author hzh
 * @since 2019-05-22
 */

@Service
public class WxUserStaticServiceImpl implements WxUserStaticService {

    @Autowired
    private WxUserStaticRepository wxUserStaticRepository;

    /**
     * 保存对象
     *
     * @param wxUserStatic 对象
     *                     持久对象，或者对象集合
     */
    @Override
    public WxUserStatic save(WxUserStatic wxUserStatic) {
        return wxUserStaticRepository.save(wxUserStatic);
    }

    /**
     * 删除对象
     *
     * @param wxUserStatic 对象
     */
    @Override
    public void delete(WxUserStatic wxUserStatic) {
        wxUserStaticRepository.delete(wxUserStatic);
    }

    @Override
    public void deleteAll(List<WxUserStatic> list) {
        wxUserStaticRepository.deleteAll(list);
    }

    @Override
    public void deleteById(Long id) {
        wxUserStaticRepository.deleteById(id);
    }

    @Override
    public List<WxUserStatic> findList() {
        return wxUserStaticRepository.findAll();
    }

    /**
     * 通过id判断是否存在
     *
     * @param id
     */
    @Override
    public boolean existsById(Long id) {
        return wxUserStaticRepository.existsById(id);
    }

    /**
     * 返回可用实体的数量
     */
    @Override
    public long count() {
        return wxUserStaticRepository.count();
    }

    /**
     * 通过id查询
     *
     * @param id id
     * @return WxUserStatic对象
     */
    @Override
    public WxUserStatic findById(Long id) {
        Optional<WxUserStatic> optional = wxUserStaticRepository.findById(id);
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
    public Page<WxUserStatic> findAll(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "id");
        return wxUserStaticRepository.findAll(pageable);
    }

    @Override
    public Page<WxUserStatic> findAll(int page, int pageSize, QueryWxUserStatic queryWxUserStatic) {
        //过滤自己的广告
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "id");
        //查询条件构造
        Specification<WxUserStatic> spec = new Specification<WxUserStatic>() {
            @Override
            public Predicate toPredicate(Root<WxUserStatic> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (queryWxUserStatic.getId() != null) {
                    predicate.getExpressions().add(cb.equal(root.get("id").as(Long.class), queryWxUserStatic.getId()));
                }

                if (queryWxUserStatic.getDaliyNum() != null) {
                    predicate.getExpressions().add(cb.equal(root.get("daliyNum").as(Integer.class), queryWxUserStatic.getDaliyNum()));
                }

                if (queryWxUserStatic.getTotalNum() != null) {
                    predicate.getExpressions().add(cb.equal(root.get("totalNum").as(Integer.class), queryWxUserStatic.getTotalNum()));
                }

                if (StringUtils.isNotBlank(queryWxUserStatic.getStartTime())) {
                    predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("createTime").as(String.class), queryWxUserStatic.getStartTime()));
                }
                if (StringUtils.isNotBlank(queryWxUserStatic.getEndTime())) {
                    predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("createTime").as(String.class), queryWxUserStatic.getEndTime()));
                }
                return predicate;
            }

        };
        return wxUserStaticRepository.findAll(spec, pageable);
    }

    /**
     * 根据Id查询list
     *
     * @param ids id集合
     * @return list
     */
    @Override
    public List<WxUserStatic> findAllById(List<Long> ids) {
        return wxUserStaticRepository.findAllById(ids);
    }

    @Override
    public WxUserStatic findByCountTime(Date countTime) {
        return wxUserStaticRepository.findByCountTime(countTime);
    }

    @Override
    public List<WxUserStatic> findByCountTimeBetween(Date startTime, Date endTime) {
        return wxUserStaticRepository.findByCountTimeBetweenOrderByCountTime(startTime, endTime);
    }
}


