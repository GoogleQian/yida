package com.etar.purifier.modules.firmwarestatic.jpa;

import entity.firmwarestatic.FirmwareStatic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
     * 通过设备号查询最新版本号
     *
     * @param devCode 设备号
     * @return 查询结果
     */
    FirmwareStatic findTop1ByDevCodeOrderByReportTimeDesc(String devCode);

    /**
     * 通过设备号集合查询最新版本号
     *
     * @param devCodes 设备号集合
     * @return 查询结果
     */
    @Query(value = "SELECT a.* FROM(SELECT* FROM firmware_static  WHERE dev_code IN (?1)) as  a " +
            "INNER JOIN( SELECT dev_code, MAX( report_time ) report_time FROM firmware_static WHERE dev_code IN ( ?1 ) GROUP BY dev_code ) b ON a.dev_code = b.dev_code AND a.report_time = b.report_time", nativeQuery = true)
    List<FirmwareStatic> findTop1ByDevCodeInOrderByReportTimeDesc(List<String> devCodes);

    /**
     * 分页查询，带devCode
     *
     * @param page     分页起始位置
     * @param pageSize 分页结束位置
     * @param fmId     固件id
     * @param devCode  设备id
     * @return 集合
     */
    @Query(value = "SELECT d.dev_code,d.`online`,d.id,f.fm_name,f.fm_version,f.fm_id,f.report_time,f.up_status  as  up_status  from (SELECT id, dev_code,`online` FROM device  WHERE version_num = 2 and dev_code like ?4) as d LEFT JOIN ( SELECT * FROM firmware_static WHERE fm_id = ?3 ) AS f ON d.dev_code = f.dev_code  LIMIT ?1,?2", nativeQuery = true)
    List<FirmwareStatic> findListDataByDevCode(Integer page, Integer pageSize, Integer fmId, String devCode);

    /**
     * 统计总数 带devCode
     *
     * @param fmId    固件id
     * @param devCode 设备号
     * @return 统计数
     */
    @Query(value = "SELECT count(*)  from (SELECT id,dev_code,`online` FROM device  WHERE version_num = 2 and dev_code like ?2) as d LEFT JOIN ( SELECT * FROM firmware_static WHERE fm_id = ?1 ) AS f ON d.dev_code = f.dev_code ", nativeQuery = true)
    Long countListDataByDevCode(Integer fmId, String devCode);

    /**
     * 分页查询，带devCode
     *
     * @param page     分页起始位置
     * @param pageSize 分页结束位置
     * @param fmId     固件id
     * @return 集合
     */
    @Query(value = "SELECT d.dev_code,d.`online`,d.id,f.fm_name,f.fm_version,f.fm_id,f.up_status ,f.report_time from (SELECT id,dev_code,`online` FROM device  WHERE version_num = 2 ) as d LEFT JOIN ( SELECT * FROM firmware_static WHERE fm_id = ?3 ) AS f ON d.dev_code = f.dev_code  LIMIT ?1,?2", nativeQuery = true)
    List<FirmwareStatic> findListData(Integer page, Integer pageSize, Integer fmId);

    /**
     * 统计总数
     *
     * @param fmId 固件id
     * @return 统计数
     */
    @Query(value = "SELECT count(*)  from (SELECT id,dev_code,`online` FROM device  WHERE version_num = 2) as d LEFT JOIN ( SELECT * FROM firmware_static WHERE fm_id = ?1 ) AS f ON d.dev_code = f.dev_code ", nativeQuery = true)
    Long countListData(Integer fmId);


    /**
     * 通过固件id查询
     *
     * @param fmId 固件id
     * @return 结果
     */
    List<FirmwareStatic> findByFmId(Integer fmId);

    /**
     * 通过固件ids查询
     *
     * @param fmIds 固件ids
     * @return 查询list
     */
    List<FirmwareStatic> findByFmIdIn(List<Integer> fmIds);
}