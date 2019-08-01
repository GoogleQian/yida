package com.etar.purifier.modules.file;

import com.etar.purifier.common.annotation.LogOperate;
import com.etar.purifier.modules.common.entity.DataResult;
import com.etar.purifier.modules.common.entity.Result;
import com.etar.purifier.utils.ResultCode;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Arrays;

/**
 * @author hzh
 * @date 2018/10/17
 */
@RestController
@RequestMapping(value = "yida/file")
public class FileController {
    private static Logger log = LoggerFactory.getLogger(FileController.class);

    /**
     * 支持图片的类型
     **/
    private String[] types = {".jpg", ".bmp", ".jpeg", ".png", ".gif"};
    /**
     * 图片保存路径
     */
    @Value("${img.uploadPath}")
    private String uploadPath;
    /**
     * t图片重命名前缀
     */
    @Value("${img.prefix}")
    private String imgPrefix;
    /**
     * 返回给前端的访问路径
     */
    @Value("${img.visitPath}")
    private String visitPath;
    /**
     * 返回给前端的访问域名
     */
    @Value("${img.imgUrl}")
    private String imgUrl;
    /**
     * 返回给前端的访问域名
     */
    @Value("${http.port}")
    private String port;
    @Resource
    private HttpServletRequest request;


    /**
     * 图片上传
     *
     * @param file 图片
     * @return 上传结果
     */
    @PostMapping(value = "/image")
    @LogOperate(description = "图片上传")
    public Result upload(@RequestParam(value = "file", required = false) MultipartFile file) {
        DataResult result = new DataResult();
        String fileName;
        if (file == null || file.isEmpty()) {
            return result.error(ResultCode.IMG_IS_EMPTY);
        }
        fileName = file.getOriginalFilename();
        //比较图片格式
        String type = fileName.substring(fileName.lastIndexOf("."));
        if (!Arrays.asList(types).contains(type)) {
            return result.error(ResultCode.IMG_FORMAT_ERROR);
        }
        BufferedOutputStream out;
        File fileSourcePath = new File(uploadPath);
        if (!fileSourcePath.exists()) {
            fileSourcePath.mkdirs();
        }
        //重命名，防重复
        fileName = imgPrefix + System.currentTimeMillis() + type;
        log.info("上传的文件名为：" + fileName);
        try {
            out = new BufferedOutputStream(
                    new FileOutputStream(new File(fileSourcePath, fileName)));
            out.write(file.getBytes());
            out.flush();
        } catch (Exception e) {
            log.info(e.getMessage());
            return result.error(ResultCode.IMG_SAVE_ERROR);
        }
        result.ok();
        result.setDatas(getImgUrl(fileName));
        return result;
    }

    /**
     * 获取图片全路径
     *
     * @param fileName 文件名
     * @return 相对路径
     */
    private String getImgUrl(String fileName) {
        String getContextPath = request.getContextPath();
        String basePath = imgUrl + ":" + port + getContextPath;
        log.info("当前路径:{}", basePath);
        return basePath + visitPath + fileName;
    }


}
