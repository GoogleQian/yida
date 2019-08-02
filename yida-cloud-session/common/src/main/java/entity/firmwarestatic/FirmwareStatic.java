package entity.firmwarestatic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author hzh
 * @since 2019-07-17
 */

@Entity
@Table(name = "firmware_static")
@DynamicInsert
@DynamicUpdate
public class FirmwareStatic implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 固件版本id
     */
    private Integer fmId;
    /**
     * 固件版本名称
     */
    private String fmName;
    /**
     * 固件版本
     */
    private String fmVersion;
    /**
     * 设备编号
     */
    private String devCode;
    /**
     * 响应时间
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date reportTime;
    /**
     * 升级状态
     */
    private Integer upStatus;
    /**
     * 设备在线状态
     */
    private Integer online;

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

    public String getFmName() {
        return fmName;
    }

    public void setFmName(String fmName) {
        this.fmName = fmName;
    }

    public String getDevCode() {
        return devCode;
    }

    public void setDevCode(String devCode) {
        this.devCode = devCode;
    }

    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    public String getFmVersion() {
        return fmVersion;
    }

    public void setFmVersion(String fmVersion) {
        this.fmVersion = fmVersion;
    }

    public Integer getOnline() {
        return online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }

    public Integer getUpStatus() {
        return upStatus;
    }

    public void setUpStatus(Integer upStatus) {
        this.upStatus = upStatus;
    }
}