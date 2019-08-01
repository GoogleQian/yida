package com.etar.purifier.modules.dev.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.etar.purifier.modules.advertising.service.AdvertisingService;
import entity.common.entity.PageBean;
import entity.dev.DevVo;
import com.etar.purifier.modules.dev.jpa.DeviceRepository;
import com.etar.purifier.modules.dev.service.DeviceService;
import com.etar.purifier.modules.mqtt.MqttService;
import com.etar.purifier.modules.users.service.UserService;
import com.etar.purifier.utils.CompareObject;
import com.etar.purifier.utils.MqttUtil;
import entity.adverstising.Advertising;
import entity.common.entity.Result;
import entity.dev.Device;
import entity.user.User;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.*;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import utils.ConstantUtil;
import utils.ResultCode;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Device服务类
 * </p>
 *
 * @author hzh
 * @since 2018-10-08
 */

@Service
public class DeviceServiceImpl implements DeviceService {
    private static Logger log = LoggerFactory.getLogger(DeviceServiceImpl.class);
    private final DeviceRepository deviceRepository;
    private final UserService userService;
    private final MqttService mqttService;
    private final AdvertisingService advertisingService;
    @Resource
    private HttpServletResponse response;
    @Resource
    private ResourceLoader resourceLoader;

    @Autowired
    public DeviceServiceImpl(DeviceRepository deviceRepository, UserService userService, MqttService mqttService, AdvertisingService advertisingService) {
        this.deviceRepository = deviceRepository;
        this.userService = userService;
        this.mqttService = mqttService;
        this.advertisingService = advertisingService;
    }

    /**
     * 保存对象
     *
     * @param device 对象
     *               持久对象，或者对象集合
     */
    @Override
    public Device save(Device device) {
        return deviceRepository.save(device);
    }

    /**
     * 删除对象
     *
     * @param device 对象
     */
    @Override
    public void delete(Device device) {
        deviceRepository.delete(device);
    }

    @Override
    public void deleteAll(List<Device> list) {
        deviceRepository.deleteAll(list);
    }

    /**
     * 通过id删除对象
     *
     * @param id id
     */
    @Override
    public void deleteById(Integer id) {
        deviceRepository.deleteById(id);
    }

    /**
     * 通过id判断是否存在
     *
     * @param id id
     */
    @Override
    public boolean existsById(Integer id) {
        return deviceRepository.existsById(id);
    }

    /**
     * 返回可用实体的数量
     */
    @Override
    public long count() {
        return deviceRepository.count();
    }

    /**
     * 通过id查询
     *
     * @param id id
     * @return Device对象
     */
    @Override
    public Device findById(Integer id) {
        Optional<Device> optional = deviceRepository.findById(id);
        boolean present = optional.isPresent();
        return present ? optional.get() : null;
    }

    /**
     * 分页查询
     * id处字符串为需要排序的字段，可以传多个，比如 "id","createTime",...
     *
     * @param page     页面
     * @param pageSize 页面大小
     * @return Page<Device>对象
     */
    @Override
    public Page<Device> findAll(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "id");
        return deviceRepository.findAll(pageable);
    }

    @Override
    public List<Device> findByUserId(Integer userId) {
        return deviceRepository.findByUserId(userId);
    }

    @Override
    public List<Device> findList() {
        return deviceRepository.findAll();
    }

    @Override
    public boolean existsByDevCode(String devCode) {
        return deviceRepository.existsByDevCode(devCode);
    }

    @Override
    public void bindDev(String devCode, Integer userId) {
        //先通过devCode查询对象
        Device device = findByDevCode(devCode);
        //设置要绑定的值
        device.setBindTime(new Date());
        device.setUserId(userId);
        //第一次扫码，滤芯寿命设为0并标记为激活
        if (ConstantUtil.DEV_UN_ACTIVE == device.getActive()) {
            //绑定时，滤芯寿命更为0
            updateFilterLife(0, devCode);
            //设置激活
            device.setActive(ConstantUtil.DEV_ACTIVE);
        }
        deviceRepository.save(device);
    }

    @Override
    public void unbindDevBatch(List<Integer> devIds) {
        if (!devIds.isEmpty()) {
            for (Integer id : devIds) {
                unbindDevByDevId(id);
            }
        }

    }

    @Override
    public Device findByDevCode(String devCode) {
        return deviceRepository.findByDevCode(devCode);
    }


    @Override
    public Page<Device> findPage(int page, int pageSize, String devCode, Integer online, Integer userId,String bindAccount) {
        boolean bindAountFlag = StringUtils.isNoneBlank(bindAccount);
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, bindAountFlag?"userId":"id");
        Specification specification = (Specification) (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (StringUtils.isNotBlank(devCode)) {
                predicate.getExpressions().add(cb.like(root.get("devCode").as(String.class), "%" + devCode + "%"));
            }
            //在线筛选
            if (-1 != online) {
                predicate.getExpressions().add(cb.equal(root.get("online").as(Integer.class), online));
            }
            if (userId != null) {
                predicate.getExpressions().add(cb.equal(root.get("userId").as(Integer.class), userId));
                //获取绑定的设备
                predicate.getExpressions().add(cb.equal(root.get("status").as(Integer.class), 1));
            }
            //绑定账号
            if(bindAountFlag){
                CriteriaBuilder.In<Object> in = cb.in(root.get("userId").as(String.class));
                Set<Integer> userIdsByNickName = userService.findUserIdsByNickName(bindAccount);
                for (Integer integer : userIdsByNickName) {
                    in.value(integer);
                }
                predicate.getExpressions().add(cb.and(in));
            }
            return predicate;
        };
        return deviceRepository.findAll(specification, pageable);
    }

    @Override
    public int updatedDevice(Device device) {
        //查找源数据
        Device byId = findById(device.getId());
        Device byDevCode = deviceRepository.findByDevCode(device.getDevCode());
        if (byDevCode != null) {
            //如果两个id相等，则表示banner名称已存在数据库
            if (byId.getId().intValue() != byDevCode.getId().intValue()) {
                return 0;
            }
        }
        //修改数据
        CompareObject.modifyBeanContent(device, byId);

        return deviceRepository.update(byId);
    }

    @Override
    public void getDevBindAccount(List<Device> deviceList) {
        for (Device device : deviceList) {
            Integer userId = device.getUserId();
            if (userId != null) {
                User user = userService.findById(userId);
                if (user != null) {
                    device.setBindAccount(user.getNickName());
                }
            }

        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Integer> devIds) {
        //查找所有设备
        List<Device> deviceList = deviceRepository.findAllById(devIds);
        if (!deviceList.isEmpty()) {
            deviceRepository.deleteInBatch(deviceList);
        }
    }


    @Override
    public int updateBindStatus(Integer status, String devCode) {
        return deviceRepository.updateBindStatus(status, devCode);
    }

    @Override
    public int hardwareUnbind(Integer status, String devCode) throws MqttException {
        //硬件发送手动解绑后,成功返回给硬件
        int bindStatus = updateBindStatus(status, devCode);
        if (bindStatus == 1) {
//            mqttPushClient.publish(1, "activate/" + devCode, "5,1");
            log.info("硬件发送手动解绑:" + "activate/" + devCode + "5,1");
        }

        return bindStatus;
    }

    @Override
    public int updateOnline(Integer online, String devCode) throws MqttException {
        //开机同步时间下发指令
        String timeMsg = MqttUtil.getTimeMsg();
        mqttService.publish(1, ConstantUtil.DOWN_TOPIC + devCode, timeMsg);
        log.info("开机同步时间下发指令:" + ConstantUtil.DOWN_TOPIC + devCode + timeMsg);
        delaySendAd(devCode);
        return deviceRepository.updateOnline(online, devCode);
    }

    @Override
    public int updateOnlineAndIpAndTime(Integer online, String ip, String devCode) throws MqttException {
        Device byDevCode = deviceRepository.findByDevCode(devCode);
        byDevCode.setOnline(online);
        byDevCode.setIp(ip);
        Date connectTime = byDevCode.getConnectTime();
        if (online == 1) {
            byDevCode.setConnectTime(connectTime != null ? connectTime : new Date());
        } else {
            byDevCode.setConnectTime(null);
        }
        deviceRepository.save(byDevCode);
        return 1;
    }

    @Override
    public void updateFilterLife(Integer filterLife, String devCode) {
        deviceRepository.updateFilterLife(filterLife, devCode);
    }

    @Override
    public Integer countByUserId(Integer userId) {
        return deviceRepository.countDevNumByUserId(userId);
    }

    @Override
    public void unbindDevByDevId(Integer devId) {
        Device device = findById(devId);
        if (device != null) {
            //更改设备为未绑定状态
            device.setStatus(0);
            //关闭重复绑定功能
            device.setUserId(null);
            deviceRepository.save(device);
        }
    }

    @Override
    public void changeFilter(Integer filterLifeTime, String devCode) {
        deviceRepository.changeFilter(filterLifeTime, devCode);
    }

    @Override
    public List<Device> findUserBindDevList(Integer status, Integer userId) {
        return deviceRepository.findByStatusAndUserId(status, userId);
    }

    /**
     * 收到开机指令后，延时3秒下发广告
     *
     * @param devCode 设备编码
     */
    private void delaySendAd(String devCode) {
        ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("send-ad-schedule-pool-%d").daemon(true).build());
        executor.schedule((Callable<Void>) () -> {
            //获取上架的广告
            Advertising byState = advertisingService.findByState(ConstantUtil.AD_IS_STAY);
            //下发广告
            String advMsg = MqttUtil.getAdvMsg(byState, ConstantUtil.MQTT_ADV_PREFIX_ONLINE);
            mqttService.publish(2, ConstantUtil.DOWN_TOPIC + devCode, advMsg);
            log.info("开机下发广告:" + ConstantUtil.DOWN_TOPIC + devCode + advMsg);
            return null;
        }, 3, TimeUnit.SECONDS);
    }

    @Override
    public void updateFilterLifeAndActive(String devCode, int filterLife, int active) {
        deviceRepository.updateFilterLifeAndActive(devCode, filterLife, active);
    }

    @Override
    public void downloadTemplate() {
        InputStream inputStream = null;
        ServletOutputStream servletOutputStream = null;
        try {
            String fileName = "设备编码导入模板.xlsx";
            String path = "template/devInfoModel.xlsx";
            org.springframework.core.io.Resource resource = resourceLoader.getResource("classpath:" + path);

            response.setContentType("application/msexcel");
            response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.addHeader("charset", "utf-8");
            response.addHeader("Pragma", "no-cache");
            String encodeName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
            response.setHeader("Content-Disposition", "attachment; fileName=\"" + encodeName);

            inputStream = resource.getInputStream();
            servletOutputStream = response.getOutputStream();
            IOUtils.copy(inputStream, servletOutputStream);
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (servletOutputStream != null) {
                    servletOutputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                // 召唤jvm的垃圾回收器
                System.gc();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result batchImport(MultipartFile file) throws Exception {
        Result result = new Result();
        List<Device> devInfos = new ArrayList<>();

        String fileName = file.getOriginalFilename();
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            return result.error(ResultCode.EXCEL_INVALID_FORMAT);
        }
        boolean isExcel2003 = true;
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }
        InputStream is;
        Workbook wb;

        is = file.getInputStream();
        if (isExcel2003) {
            wb = new HSSFWorkbook(is);
        } else {
            wb = new XSSFWorkbook(is);
        }
        Sheet sheet = wb.getSheetAt(0);
        int rowNum = sheet.getPhysicalNumberOfRows();
        int coloumNum = sheet.getRow(0).getPhysicalNumberOfCells();
        if (rowNum == 1) {
            //只有标题行
            return result.error(ResultCode.EXCEL_NULL_VALUE);
        }
        if (coloumNum != 1) {
            //不是一列
            return result.error(ResultCode.EXCEL_INVALID_FORMAT);
        }
        Device devInfo;
        Date date = new Date();
        //导入失败的记录
        List<String> erroList = new LinkedList<>();
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }

            row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
            String devCode = row.getCell(0).getStringCellValue();
            if (devCode == null || devCode.isEmpty()) {
                log.info("导入失败(第" + (r + 1) + "行,设备ID未填写)");
                continue;
            }

            devInfo = new Device();
            devInfo.setDevCode(devCode);
            devInfo.setInventoryTime(date);
            devInfo.setStatus(0);
            devInfos.add(devInfo);
        }

        for (Device info : devInfos) {
            String filterCode = info.getDevCode();
            boolean exists = existsByDevCode(filterCode);
            if (exists) {
                log.info(filterCode + "已经存在，不能导入");
                erroList.add(filterCode);
                continue;
            }
            save(info);
        }
        result.setRet(erroList.size() > 0 ? -1 : 0);
        result.setMsg("成功导入" + (devInfos.size() - erroList.size()) + "条,导入失败" + erroList.size() + "条");
        return result;
    }

    @Override
    public void batchExport(List<Long> ids) throws Exception {

        //创建HSSFWorkbook对象(excel的文档对象)
        XSSFWorkbook wb = new XSSFWorkbook();
        //建立新的sheet对象（excel的表单）
        XSSFSheet sheet = wb.createSheet("设备信息表");
        setTitle(wb, sheet);

        List<Device> devInfos;
        if (CollectionUtils.isEmpty(ids)) {
            devInfos = findList();
        } else {
            devInfos = findByIdIn(ids);
        }
        //新增数据行，并且设置单元格数据
        int rowNum = 1;
        for (Device devInfo : devInfos) {
            XSSFRow row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(devInfo.getDevCode());
            rowNum++;
        }
        //输出Excel文件
        OutputStream output = response.getOutputStream();
        response.reset();
        response.setHeader("Content-disposition", "attachment; filename=devInfo.xlsx");
        response.setContentType("application/msexcel");
        wb.write(output);
        output.close();

    }

    /***
     * 设置表头
     * @param workbook workbook
     * @param sheet  XSSFSheet
     */
    private void setTitle(XSSFWorkbook workbook, XSSFSheet sheet) {
        XSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0, 20 * 256);

        //设置为居中加粗
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //sheet页对象
        //设置一下上下左右的格式
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);

        style.setFont(font);
        XSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("设备编号");
        cell.setCellStyle(style);

    }

    private List<Device> findByIdIn(List<Long> ids) {
        return deviceRepository.findByIdInOrderByIdDesc(ids);
    }

    @Override
    public long countTodayOnline() {
        return deviceRepository.countByOnline();
    }

    @Override
    public long countTodayBind(Date startTime, Date endTime) {
        return deviceRepository.countByBind(startTime, endTime);
    }

    @Override
    public long countTodayActive(Date startTime, Date endTime) {
        return deviceRepository.countByActive(startTime, endTime);
    }

    @Override
    public long countInventoryc(Date startTime, Date endTime) {
        return deviceRepository.countInventoryc(startTime, endTime);
    }

    @Override
    public PageBean<DevVo> findDevMsgByPhone(String phone, Integer page, Integer pageSize) {
        Integer devTotalSize = countDevMsgByPhone(phone);
        PageBean<DevVo> pageBean = new PageBean<>();
        if (devTotalSize == 0) {
            pageBean.setItemCounts(0L);
            pageBean.setList(new ArrayList<>());
        } else {
            //显示第几页
            Integer currentPage = (page - 1) * pageSize;
            List<Object> byPhone = deviceRepository.findDevMsgByPhone(phone, currentPage, pageSize);
            List<DevVo> devVoList = new ArrayList<>(byPhone.size());
            searchResultParseToDev(byPhone, devVoList);
            pageBean.setItemCounts(Long.valueOf(devTotalSize));
            pageBean.setList(devVoList);
        }
        pageBean.setCurPage(page);
        pageBean.setPageSize(pageSize);
        return pageBean;
    }

    @Override
    public Integer countDevMsgByPhone(String phone) {
        return deviceRepository.countDevMsgByPhone(phone);
    }

    @Override
    public PageBean<DevVo> findDevMsgByDevCode(String devCode) {
        List<Object> devMsgByDevCode = deviceRepository.findDevMsgByDevCode(devCode);
        List<DevVo> devVoList = new ArrayList<>(devMsgByDevCode.size());
        searchResultParseToDev(devMsgByDevCode, devVoList);
        PageBean<DevVo> pageBean = new PageBean<>();
        pageBean.setCurPage(1);
        pageBean.setPageSize(5);
        pageBean.setItemCounts(0L);
        pageBean.setList(devVoList);
        return pageBean;
    }

    /**
     * 查询结果解析到dev实体类
     *
     * @param searchObjectList 查询得到结果集
     * @param devVoList          想要的结果集
     */
    private void searchResultParseToDev(List<Object> searchObjectList, List<DevVo> devVoList) {
        if (!CollectionUtils.isEmpty(searchObjectList)) {
            for (Object searchObject : searchObjectList) {
                JSONArray jsonArray = JSONArray.parseArray(JSONObject.toJSONString(searchObject));
                DevVo devVo = new DevVo();
                devVo.setPhone(jsonArray.getString(0));
                devVo.setDevCode(jsonArray.getString(1));
                devVo.setOnline(jsonArray.getInteger(2));
                devVoList.add(devVo);
            }
        }
    }
}


