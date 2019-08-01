package com.etar.purifier.modules.verificationCode.controller;

import com.etar.purifier.common.annotation.LogOperate;
import com.etar.purifier.modules.common.entity.DataResult;
import com.etar.purifier.modules.common.entity.PageBean;
import com.etar.purifier.modules.common.entity.Result;
import com.etar.purifier.modules.verificationCode.entity.QueryVerificationCode;
import com.etar.purifier.modules.verificationCode.entity.VerificationCode;
import com.etar.purifier.modules.verificationCode.service.VerificationCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * 手机验证码 VerificationCodeController层
 *
 * @author gmq
 * @since 2019-01-11
 */
@RestController
@RequestMapping(value = "/verificationCodes")
public class VerificationCodeController {
    private static Logger log = LoggerFactory.getLogger(VerificationCodeController.class);
    private final VerificationCodeService verificationCodeService;

    @Autowired
    public VerificationCodeController(VerificationCodeService verificationCodeService) {
        this.verificationCodeService = verificationCodeService;
    }


    /**
     * 保存对象<br/>
     *
     * @param verificationCode 对象
     */
    @PostMapping
    @LogOperate(description = "保存验证码")
    public Result save(@Validated @RequestBody VerificationCode verificationCode) {
        Result result = new Result();
        try {
            verificationCodeService.save(verificationCode);
        } catch (Exception e) {
            log.error(e.getMessage());
            return result.error(2001, "新增失败");
        }
        return result.ok();
    }

    /**
     * 更新
     *
     * @param verificationCode 验证码
     * @return 123
     */
    @PutMapping(value = "/{id}")
    @LogOperate(description = "重发验证码")
    public Result updateBanner(@Validated @RequestBody VerificationCode verificationCode, @PathVariable("id") Integer id) {
        Result result = new Result();
        try {
            boolean exists = verificationCodeService.existsById(id);
            if (!exists) {
                return result.error(2002, "修改失败，未找到");
            }
            verificationCodeService.save(verificationCode);
        } catch (Exception e) {
            log.error(e.getMessage());
            return result.error(2002, "修改失败");
        }
        return result.ok();
    }


    /**
     * 通过id查找对象
     *
     * @param id id
     * @return VerificationCode 对象
     */
    @GetMapping(value = "/{id}")
    public Result findById(@PathVariable("id") Integer id) {
        DataResult result = new DataResult();
        try {
            result.setDatas(verificationCodeService.findById(id));
        } catch (Exception e) {
            log.error(e.getMessage());
            return result.error(2004, "不存在");
        }
        return result.ok();
    }

    /**
     * 分页查询
     *
     * @return Page<VerificationCode> 对象
     */
    @PostMapping(value = "/pages")
    public Result findByPage(@RequestBody QueryVerificationCode queryVerificationCode) {

        DataResult result = new DataResult();
        try {
            int page = queryVerificationCode.getPage();
            int pageSize = queryVerificationCode.getPageSize();
            Page<VerificationCode> all = verificationCodeService.findAll(page - 1, pageSize, queryVerificationCode);
            PageBean<VerificationCode> pageBean = new PageBean<>();
            if (all == null) {
                pageBean.setList(new ArrayList<>());
                result.setDatas(pageBean);
                return result.ok();
            }
            pageBean.setCurPage(page);
            pageBean.setItemCounts(all.getTotalElements());
            pageBean.setPageSize(pageSize);
            pageBean.setList(all.getContent());
            result.setDatas(pageBean);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return result.error(2005, "查询出错");
        }
        return result.ok();
    }
}