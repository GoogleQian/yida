package entity.common.entity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 批量操作接受id集合对象
 *
 * @author hzh
 * @date 2018/10/11
 */
public class BatchLongReqVo {
    @NotEmpty(message = "要操作的ID集合不能为空")
    private List<Long> ids;

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
