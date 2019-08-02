package com.etar.firmware.jpa;

import entity.firmwarestatic.FirmwareStatic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * jpa 接口
 *
 * @author hzh
 * @since 2019-07-17
 */

@Transactional(rollbackFor = Exception.class)
public interface FirmwareStaticRepository extends JpaRepository<FirmwareStatic, Integer> {

    /**
     * 按条件查询方案
     *
     * @param spec     spec
     * @param pageable 分页
     * @return page
     */
    Page<FirmwareStatic> findAll(Specification<FirmwareStatic> spec, Pageable pageable);

    /**
     * 通过固件id和设备号查询
     *
     * @param fmId    固件id
     * @param devCode 设备号
     * @return 查询结果
     */
    FirmwareStatic findByFmIdAndDevCode(Integer fmId, String devCode);
}