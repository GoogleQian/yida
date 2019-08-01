package com.etar.purifier.modules.statistics.device.jpa;


import entity.devicestatic.DeviceStatic;
import entity.wxuserstatic.WxUserStatic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;
import java.util.List;

/**
 * 
 *  jpa 接口
 *
 * @author gmq
 * @since 2019-05-22
 */

@Transactional(rollbackFor = Exception.class)
public interface DeviceStaticRepository extends JpaRepository<DeviceStatic, Long> {

    /**
     * 按条件查询方案
     * @param spec spec
     * @param pageable 分页
     * @return page
     */
    Page<DeviceStatic> findAll(Specification<DeviceStatic> spec, Pageable pageable);


    /**
     * id集合查询列表
     * @return 结果
     */
    @Query(value = "SELECT '1' as id ,COUNT( CASE WHEN inventory_time > DATE_FORMAT(NOW(),'%Y-%m-%d') THEN 'inventory_time' END ) daily_count ,now() AS date,COUNT(id) AS total_count,COUNT( CASE WHEN online IN ('0') THEN 'online' END ) online_count, COUNT( CASE WHEN active IN ('1') THEN 'active' END ) active_count, COUNT( CASE WHEN status = '1' THEN 'status' END ) status_count FROM device", nativeQuery = true)
    DeviceStatic getTotalStatic();

    @Query(value = "select * from device_static where date between ?1 and ?2 order by date", nativeQuery = true)
    List<DeviceStatic> findByCountTimeBetween(String startTime, String endTime);
}