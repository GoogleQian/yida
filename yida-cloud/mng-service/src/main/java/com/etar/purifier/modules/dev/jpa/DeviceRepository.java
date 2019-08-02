package com.etar.purifier.modules.dev.jpa;

import entity.dev.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * jpa 接口
 *
 * @author hzh
 * @since 2018-10-08
 */

@Transactional(rollbackFor = Exception.class)
public interface DeviceRepository extends JpaRepository<Device, Integer> {

    /**
     * 复杂JPA操作  使用@Query()自定义sql语句  根据业务id UId去更新整个实体
     * 删除和更新操作，需要@Modifying和@Transactional注解的支持
     * <p>
     * 更新操作中 如果某个字段为null则不更新，否则更新【注意符号和空格位置】
     *
     * @param device 传入实体，分别取实体字段进行set
     * @return 更新操作返回sql作用条数
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update Device b set " +
            "b.active = CASE WHEN :#{#device.active} IS NULL THEN b.active ELSE :#{#device.active} END ," +
            "b.bindTime = CASE WHEN :#{#device.bindTime} IS NULL THEN b.bindTime ELSE :#{#device.bindTime} END ," +
            "b.devCode = CASE WHEN :#{#device.devCode} IS NULL THEN b.devCode ELSE :#{#device.devCode} END ," +
            "b.inventoryTime = CASE WHEN :#{#device.inventoryTime} IS NULL THEN b.inventoryTime ELSE :#{#device.inventoryTime} END ," +
            "b.online = CASE WHEN :#{#device.online} IS NULL THEN b.online ELSE :#{#device.online} END ," +
            "b.status = CASE WHEN :#{#device.status} IS NULL THEN b.status ELSE :#{#device.status} END ," +
            "b.userId = CASE WHEN :#{#device.userId} IS NULL THEN b.userId ELSE :#{#device.userId} END ," +
            "b.filterLifeTime =  CASE WHEN :#{#device.filterLifeTime} IS NULL THEN b.filterLifeTime ELSE :#{#device.filterLifeTime} END " +
            "where b.id = :#{#device.id}")
    int update(@Param("device") Device device);

    /**
     * 通过devCode查询设备是否在线
     *
     * @param devCode 设备编码
     * @return 存在结果
     */
    boolean existsByDevCode(String devCode);

    /**
     * 通过设备编码查询对象
     *
     * @param devCode 设备编码
     * @return 查询对象
     */
    Device findByDevCode(String devCode);

    /**
     * 通过用户id获取设备绑定的列表
     *
     * @param userId 用户id
     * @return 集合
     */
    List<Device> findByUserId(Integer userId);

    /**
     * 通过条件查询
     *
     * @param specification 过滤条件
     * @param pageable      分页对象
     * @return 插件page对象
     */
    Page<Device> findAll(Specification specification, Pageable pageable);


    /**
     * 更新绑定（激活）状态
     *
     * @param status  激活状态
     * @param devCode 设备编码
     */
    @Modifying
    @Query("update Device u set u.status = ?1 where u.devCode= ?2")
    int updateBindStatus(Integer status, String devCode);


    /**
     * 更上线状态状态
     *
     * @param online  在线状态
     * @param devCode 设备编码
     */
    @Modifying
    @Query("update Device u set u.online = ?1 where u.devCode= ?2")
    int updateOnline(Integer online, String devCode);

    /**
     * 更改滤芯寿命
     *
     * @param filterLife 滤芯寿命
     * @param devCode    设备码
     */
    @Modifying
    @Query("update Device u set u.filterLifeTime = ?1 where u.devCode= ?2")
    void updateFilterLife(Integer filterLife, String devCode);

    /***
     *修改滤芯
     * @param filterLifeTime 滤芯
     * @param devCode  设备编码
     */
    @Modifying
    @Query("update Device u set u.filterLifeTime = ?1 where u.devCode= ?2")
    void changeFilter(Integer filterLifeTime, String devCode);

    /**
     * 统计用户设备数
     *
     * @param userId 用户id
     * @return 设备数
     */
    @Query(value = "select count(id) from device where user_id=?1 and status=1 ", nativeQuery = true)
    Integer countDevNumByUserId(Integer userId);

    /**
     * 获取用户绑定的设备列表（小程序）
     *
     * @param status 状态
     * @param userId 用户id
     */
    List<Device> findByStatusAndUserId(Integer status, Integer userId);

    /**
     * 硬件解绑 滤芯归零 激活状态改为未激活
     *
     * @param devCode    设备号
     * @param filterLife 滤芯寿命
     * @param active     激活状态
     */
    @Modifying
    @Query("update Device u set u.filterLifeTime = ?2,u.active=?3 where u.devCode= ?1")
    void updateFilterLifeAndActive(String devCode, int filterLife, int active);

    /**
     * id集合查询列表
     *
     * @param ids id集合
     * @return 结果
     */
    @Query(value = "select * from device where id in (?1)", nativeQuery = true)
    List<Device> findByIdInOrderByIdDesc(List<Long> ids);


    /**
     * 统计今天在线设备数
     *
     * @return 设备数
     */
    @Query(value = "select count(id) from device where online = 1 ", nativeQuery = true)
    long countByOnline();

    /**
     * 统计今天绑定设备数
     *
     * @return 设备数
     */
    @Query(value = "select count(id) from device where status = 1 and bind_time BETWEEN ?1 and ?2", nativeQuery = true)
    long countByBind(Date startTime, Date endTime);

    /**
     * 统计今天激活设备数
     *
     * @return 设备数
     */
    @Query(value = "select count(id) from device where active = 1 and bind_time BETWEEN ?1 and ?2", nativeQuery = true)
    long countByActive(Date startTime, Date endTime);

    /**
     * 统计今天入库设备数
     *
     * @return 设备数
     */
    @Query(value = "select count(id) from device where inventory_time BETWEEN ?1 and ?2", nativeQuery = true)
    long countInventoryc(Date startTime, Date endTime);

    /**
     * 通过手机号关联查询设备号、手机号、设备在线状态
     *
     * @param page     显示第几页
     * @param pageSize 每页显示个数
     * @param phone    手机号
     * @return 查询结果
     */
    @Query(value = "SELECT phone,dev_code,online FROM wx_user u LEFT JOIN device d ON  u.id=d.user_id  WHERE phone =?1 LIMIT ?2,?3", nativeQuery = true)
    List<Object> findDevMsgByPhone(String phone, Integer page, Integer pageSize);

    /**
     * 通过设备号查询设备号、手机号、设备在线状态
     *
     * @param devCode 设备号
     * @return 查询结果
     */
    @Query(value = "SELECT phone,dev_code,online FROM  device d LEFT JOIN wx_user u  ON d.user_id = u.id where dev_code =?1", nativeQuery = true)
    List<Object> findDevMsgByDevCode(String devCode);

    /**
     * 通过手机号关联查询设备号的总数
     *
     * @param phone 手机号
     * @return 总数
     */
    @Query(value = "SELECT count(dev_code) FROM wx_user u LEFT JOIN device d ON  u.id=d.user_id  WHERE phone =?1", nativeQuery = true)
    Integer countDevMsgByPhone(String phone);

    /**
     * 通过版本号统计的设备数
     *
     * @param verNum 设备版本号
     * @return 设备数
     */
    Integer countByVersionNum(int verNum);
}