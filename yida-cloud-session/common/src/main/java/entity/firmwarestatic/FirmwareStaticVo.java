package entity.firmwarestatic;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * @author hzh
 * @version 1.0
 * @date 2019/7/18 15:31
 */
public class FirmwareStaticVo  implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 固件版本id
     */
    private Integer fmId;
    /**
     * 固件版本id
     */
    private Integer online;
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
}
