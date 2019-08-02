package entity.firmwaretask;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import javax.persistence.*;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <p>
 *
 * </p>
 *
 * @author hzh
 * @since 2019-07-17
 */

@Entity
@Table(name = "firmware_task")
@DynamicInsert
@DynamicUpdate
public class FirmwareTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Excel(name = "编号")
    private Integer id;
    /**
     * 固件id
     */
    @Excel(name = "固件id")
    private Integer fmId;
    /**
     * 固件名称
     */
    @Excel(name = "固件名称")
    private String fmName;
    /**
     * 版本号
     */
    @Excel(name = "版本号")
    private String fmVersion;
    /**
     * 推送时间
     */
    @Excel(name = "推送时间", format = "yyyy-MM-dd HH:mm:ss")
    @Temporal(value = TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date pushTime;
    /**
     * 推送成功率
     */
    @Excel(name = "推送成功率")
    private Double pushRate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFmId() {
        return fmId;
    }

    public void setFmId(Integer fmId) {
        this.fmId = fmId;
    }

    public Date getPushTime() {
        return pushTime;
    }

    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
    }

    public Double getPushRate() {
        return pushRate;
    }

    public void setPushRate(Double pushRate) {
        this.pushRate = pushRate;
    }

    public String getFmName() {
        return fmName;
    }

    public void setFmName(String fmName) {
        this.fmName = fmName;
    }

    public String getFmVersion() {
        return fmVersion;
    }

    public void setFmVersion(String fmVersion) {
        this.fmVersion = fmVersion;
    }
}