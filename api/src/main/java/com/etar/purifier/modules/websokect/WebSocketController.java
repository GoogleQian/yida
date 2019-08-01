package com.etar.purifier.modules.websokect;

import com.etar.purifier.modules.common.entity.DataResult;
import com.etar.purifier.modules.common.entity.Result;
import com.etar.purifier.utils.ResultCode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hzh
 * @date 2018/10/25
 */
@RestController
public class WebSocketController {

    @RequestMapping(value = "/pushToWeb")
    public Result pushVideoListToWeb(String massage) {
        DataResult result = new DataResult();

        try {
          WebSocketServer.sendInfo(null);
            result.setDatas(massage);
            result.ok();
        } catch (Exception e) {
            result.error(ResultCode.ERROR);
        }
        return result;
    }
}
