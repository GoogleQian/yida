package entity.firmware;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author hzh
 * @version 1.0
 * @date 2019/7/2 15:53
 */
public class FirmwareVo implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message = "固件下载地址不能为空")
    private String ossUrl;
    @NotEmpty(message = "要推送的设备号不能为空")
    private List<String> devCodes;
    @NotNull(message = "固件字节数不能为空")
    private Integer size;

    public String getOssUrl() {
        return ossUrl;
    }

    public void setOssUrl(String ossUrl) {
        this.ossUrl = ossUrl;
    }

    public List<String> getDevCodes() {
        return devCodes;
    }

    public void setDevCodes(List<String> devCodes) {
        this.devCodes = devCodes;
    }


    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
