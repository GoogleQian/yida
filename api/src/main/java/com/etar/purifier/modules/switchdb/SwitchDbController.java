package com.etar.purifier.modules.switchdb;

import com.etar.purifier.modules.common.entity.Result;
import com.etar.purifier.utils.SwitchDbUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/switchDb")
public class SwitchDbController {

    @GetMapping("/")
    public Result switchDb(@RequestParam(value = "dbsource") int dbsource,
                           @RequestParam(value = "username") String userName,
                           @RequestParam(value = "pwd") String pwd) {
        Result result=new Result();
        if (SwitchDbUtil.userName.equals(userName) && SwitchDbUtil.passWord.equals(pwd)) {
            SwitchDbUtil.switchDb(dbsource);
             result.ok().setMsg("切换成功----当前路由状态:" + (1 == dbsource ? "强制路由主库" : "查询路由所有从库"));
        }else{
            result.error(2002, "账号或密码错误");
        }
        return result;
    }


}
