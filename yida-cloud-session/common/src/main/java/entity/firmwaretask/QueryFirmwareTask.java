package entity.firmwaretask;

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


public class QueryFirmwareTask implements Serializable {


    private Integer id;
    /**
     * 固件id
     */
    private Integer fmId;
    /**
     * 固件名称
     */
    private String fmName;
    /**
     * 版本号
     */
    private String fmVersion;
    /**
     * 推送时间
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date pushTime;
    /**
     * 推送成功率
     */
    private Integer pushRate;

    private Integer page = 1;

    private Integer pageSize = 10;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

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

    public String getFmVersion() {
        return fmVersion;
    }

    public void setFmVersion(String fmVersion) {
        this.fmVersion = fmVersion;
    }

    public Date getPushTime() {
        return pushTime;
    }

    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
    }

    public Integer getPushRate() {
        return pushRate;
    }

    public void setPushRate(Integer pushRate) {
        this.pushRate = pushRate;
    }


}