package com.etar.purifier.modules.statistics.wechat.jpa;

import entity.wxuserstatic.WxUserStatic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * jpa 接口
 *
 * @author hzh
 * @since 2019-05-22
 */

@Transactional(rollbackFor = Exception.class)
public interface WxUserStaticRepository extends JpaRepository<WxUserStatic, Long> {

    /**
     * 按条件查询方案
     *
     * @param spec     spec
     * @param pageable 分页
     * @return page
     */
    Page<WxUserStatic> findAll(Specification<WxUserStatic> spec, Pageable pageable);

    /**
     * 通过统计时间查询
     *
     * @param countTime 统计日期
     * @return 返回结果
     */
    WxUserStatic findByCountTime(Date countTime);

    /**
     * 时间段内查询结果
     *
     * @param startTime 选择的开始日期
     * @param endTime   选择的结束日期
     * @return 时间段内查询结果
     */
    List<WxUserStatic> findByCountTimeBetweenOrderByCountTime(Date startTime, Date endTime);
}