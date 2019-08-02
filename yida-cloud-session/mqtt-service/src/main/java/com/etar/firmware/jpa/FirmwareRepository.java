package com.etar.firmware.jpa;

import entity.firmware.Firmware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * jpa 接口
 *
 * @author hzh
 * @since 2019-07-01
 */

@Transactional(rollbackFor = Exception.class)
public interface FirmwareRepository extends JpaRepository<Firmware, Integer> {

    /**
     * 按条件查询方案
     *
     * @param spec     查询条件
     * @param pageable 分页对象
     * @return page
     */
    Page<Firmware> findAll(Specification<Firmware> spec, Pageable pageable);

    /**
     * 通过名称查询
     *
     * @param name 名称
     * @return 存在结果
     */
    boolean existsByName(String name);
}