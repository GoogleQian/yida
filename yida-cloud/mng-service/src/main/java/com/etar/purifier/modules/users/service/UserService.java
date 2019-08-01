package com.etar.purifier.modules.users.service;

import com.etar.purifier.base.IBaseService;
import entity.user.User;
import entity.user.WxUser;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * User接口
 *
 * @author hzh
 * @since 2018-10-08
 */

public interface UserService extends IBaseService<User, Integer> {
    /**
     * 保存用户信息
     *
     * @param openId 用户信息
     */
    @Transactional(rollbackFor = Exception.class)
    User saveByUserInfo(String openId);

    /**
     * 通过openId查询对象
     *
     * @param openId 微信openId
     * @return 对象
     */
    User findByOpenId(String openId);

    /**
     * 保存昵称
     *
     * @param wxUser 微信用户
     */
    @Transactional(rollbackFor = Exception.class)
    void saveNickName(WxUser wxUser);

    /**
     * 批量删除用户
     *
     * @param userIds 用户id
     */
    @Transactional(rollbackFor = Exception.class)
    void delBatchAndUnboundDev(List<Integer> userIds);

    /**
     * 获取用户绑定设备的个数
     *
     * @param users 用户集合
     */
    void countBindDevByUserId(List<User> users);

    /**
     * 分页查询，可以带昵称搜索
     *
     * @param page     第几页
     * @param pageSize 页面个数
     * @param nickName 搜索条件
     * @return 查询结果
     */
    Page<User> findPage(Integer page, Integer pageSize, String nickName);

    /**
     * 昵称搜索
     * @param nickName 昵称
     * @return 查询结果
     */
    Set<Integer> findUserIdsByNickName(String nickName);

    /**
     * 删除用户并解除该用户下设备的绑定
     *
     * @param userId 用户id
     */
    @Transactional(rollbackFor = Exception.class)
    void delUserAndUnboundDev(Integer userId);

    /**
     * 手机号模糊查询
     *
     * @param phone 手机号
     * @return 结果
     */
    List<User> findByNickNameLike(String phone);

    /**
     * 通过手机号查询
     *
     * @param phone 手机号
     * @return 用户
     */
    User findByPhone(String phone);

    /**
     * 统计今天新注册人数
     *
     * @param endTime   结束时间
     * @param startTime 起始时间
     * @return 人数
     */
    long countTodayRegisterUser(Date startTime, Date endTime);
}


