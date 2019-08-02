package entity.adverstising;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @author hzh
 * @version 1.0
 * @date 2019/6/18 15:45
 */
public class AdvertVo implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotBlank(message = "广告标题不能为空")
    private String title;
    @NotBlank(message = "广告内容不能为空")
    private String solgan;
    @NotEmpty(message = "要推送的设备号不能为空")
    private List<String> devCodes;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSolgan() {
        return solgan;
    }

    public void setSolgan(String solgan) {
        this.solgan = solgan;
    }

    public List<String> getDevCodes() {
        return devCodes;
    }

    public void setDevCodes(List<String> devCodes) {
        this.devCodes = devCodes;
    }
}
