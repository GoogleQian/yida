package com.etar.purifier.modules.miniprogram;

import com.alibaba.fastjson.JSONObject;
import com.etar.purifier.common.annotation.LogOperate;
import com.etar.purifier.modules.common.entity.Result;
import com.etar.purifier.modules.users.entity.User;
import com.etar.purifier.modules.users.service.UserService;
import com.etar.purifier.modules.verificationCode.entity.VerificationCode;
import com.etar.purifier.modules.verificationCode.service.VerificationCodeService;
import com.etar.purifier.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 手机注册
 *
 * @author hzh
 * @date 2019/01/11
 */
@RestController
@RequestMapping(value = "yida/miniProgram/phone/")
@Validated
public class PhoneController {
    private static Logger log = LoggerFactory.getLogger(MiniProgramController.class);

    private final UserService userService;
    private final VerificationCodeService verificationCodeService;

    @Autowired
    public PhoneController(UserService userService, VerificationCodeService verificationCodeService) {
        this.userService = userService;
        this.verificationCodeService = verificationCodeService;
    }

    /**
     * 获取是否绑定手机号
     *
     * @param userId 用户手机
     */
    @GetMapping(value = "/bindingStatus")
    public Result phone(@NotNull(message = "用户id不能为空") @RequestParam(value = "userId") String userId) {
        Result result = new Result();
        if (ConstantUtil.NULL_STR.equals(userId)) {
            return result.error(ResultCode.USER_ID_NULL);
        }
        try {
            User user = userService.findById(Integer.valueOf(userId));
            if (user == null) {
                return result.error(ResultCode.USER_NOT_EXIST);
            }
            if (StringUtils.isBlank(user.getPhone())) {
                return result.error(ResultCode.USER_UNBIND_PHONE);
            }
            result.ok();
        } catch (Exception e) {
            log.error(e.getMessage());
            return result.error(ResultCode.ERROR);
        }
        return result;
    }

    /**
     * 发送消息
     *
     * @param phone 用户手机
     */
    @GetMapping(value = "/sendSms")
    @LogOperate(description = "发送验证码")
    public Result sendSms(@NotBlank(message = "手机号码不能为空") @RequestParam(value = "phone") String phone) {
        Result result = new Result();
        try {
            User byPhone = userService.findByPhone(phone);
            if (byPhone != null) {
                return result.error(ResultCode.USER_BIND_PHONE);
            }
            String nonceStr = RandomCode.getNonceStr();
            result = SmsUtil.sendMsg(phone, nonceStr);
            log.info(JSONObject.toJSONString(result));
            if (ConstantUtil.MSG_OK.equals(result.getMsg()) && 0 == result.getRet()) {
                VerificationCode vc = verificationCodeService.findByPhone(phone);
                if (vc == null) {
                    vc = new VerificationCode();
                    vc.setPhone(phone);
                }
                vc.setCreateTime(new Date());
                vc.setCode(nonceStr);
                verificationCodeService.save(vc);
            } else {
                return result;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return result.error(ResultCode.ERROR);
        }
        return result;
    }

    /**
     * 验证注册码
     *
     * @param phone 用户手机
     */
    @GetMapping(value = "/validCode")
    @LogOperate(description = "验证注册码")
    public Result validCode(@NotBlank(message = "手机号不能为空") @RequestParam(value = "phone") String phone,
                            @NotBlank(message = "验证码不能为空") @RequestParam(value = "code") String code,
                            @NotNull(message = "用户id不能为空") @RequestParam(value = "userId") Integer userId) {
        Result result = new Result();
        try {
            VerificationCode verificationCode = verificationCodeService.findByPhone(phone);
            if (verificationCode == null) {
                return result.error(ResultCode.PHONE_CODE_IS_NULL);
            }
            if (verificationCode.getCode().equals(code)) {
                //保存手机号
                Date createTime = verificationCode.getCreateTime();
                //判断是否超时5分钟
                boolean isFiveMin = DateUtil.isInFiveMin(createTime, new Date());
                if (isFiveMin) {
                    User byId = userService.findById(userId);
                    if (byId != null) {
                        byId.setPhone(phone);
                        userService.save(byId);
                    }
                } else {
                    return result.error(ResultCode.PHONE_CODE_OUT_TIME);
                }
                result.ok();
            } else {
                return result.error(ResultCode.PHONE_CODE_FAIL);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return result.error(ResultCode.ERROR);
        }
        return result;
    }


}
