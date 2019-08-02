package com.etar.purifier.modules.firmwaretask.jpa;

import entity.firmwaretask.FirmwareTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * jpa 接口
 *
 * @author hzh
 * @since 2019-07-17
 */

@Transactional(rollbackFor = Exception.class)
public interface FirmwareTaskRepository extends JpaRepository<FirmwareTask, Integer> {

    /**
     * 按条件查询方案
     *
     * @param spec     spec
     * @param pageable 分页
     * @return page
     */
    Page<FirmwareTask> findAll(Specification<FirmwareTask> spec, Pageable pageable);

    /**
     * 通过固件id查询
     *
     * @param fmId 固件id
     * @return 查询结果
     */
    FirmwareTask findByFmId(Integer fmId);

    /**
     * 通过固件ids查询
     *
     * @param fmIds 固件ids
     * @return 查询list
     */
    List<FirmwareTask> findByFmIdIn(List<Integer> fmIds);
}