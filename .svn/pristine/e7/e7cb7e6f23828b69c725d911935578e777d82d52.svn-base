package entity.wxuserstatic;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 微信用户统计
 * </p>
 *
 * @author hzh
 * @since 2019-05-22
 */

@Entity
@Table(name = "wx_user_static")
@DynamicInsert
@DynamicUpdate
public class WxUserStatic implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 每日新增微信用户
     */
    private Long dailyNum;
    /**
     * 每日总用户数
     */
    private Long totalNum;


    /**
     * 统计时间
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date countTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDailyNum() {
        return dailyNum;
    }

    public void setDailyNum(Long dailyNum) {
        this.dailyNum = dailyNum;
    }

    public Long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
    }

    public Date getCountTime() {
        return countTime;
    }

    public void setCountTime(Date countTime) {
        this.countTime = countTime;
    }


}