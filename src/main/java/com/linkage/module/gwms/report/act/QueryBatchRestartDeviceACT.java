
package com.linkage.module.gwms.report.act;

import action.splitpage.splitPageAction;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.uss.DateUtil;
import com.linkage.module.gwms.report.bio.QueryBatchRestartDeviceBIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author ou.yuan
 * @date 2020-9-3
 */
public class QueryBatchRestartDeviceACT extends splitPageAction {

    // 日志记录
    private static Logger logger = LoggerFactory.getLogger(QueryBatchRestartDeviceACT.class);
    // 导出数据
    private List<Map> data;
    // 导出文件列标题
    private String[] title;
    // 导出文件列
    private String[] column;
    // 导出文件名
    private String fileName;
    /** 开始时间 */
    private String starttime = "";
    /** 开始时间 */
    private String starttime1 = "";
    /** 结束时间 */
    private String endtime = "";
    /** 结束时间 */
    private String endtime1 = "";


    // 重启数，重启成功数，重启失败数，改善数
    private String numType = "";

    private String cityId = "";

    private List<Map<String, String>> batchRestartDevDetailList = null;

    private QueryBatchRestartDeviceBIO queryBatchRestartDeviceBIO;


    /**
     * 查询设备信息
     *
     * @return String
     */
    public String execute() throws Exception
    {
        return "list";
    }

    /**
     * 页面初始化
     * @return
     */
    public String init() {
        // 获取系统当前时间
        String dateTime = DateUtil.getNowTime("yyyy-MM-dd");
        endtime = dateTime;
        DateTimeUtil befDateTime = new DateTimeUtil(dateTime);
        befDateTime.getNextMonth(-1); // 获取上个月时间
        // 获取一个月之内批量重启设备
        starttime = DateUtil.format(befDateTime.getDateTime(), "yyyy-MM-dd");
        return "init";
    }


    /**
     * 设备批量重启列表查询
     *
     * @author ouyuan
     * @date 9.2, 2020
     * @return String
     */
    public String queryBatchRestartDevList() {
        logger.debug("QueryBatchRestartDeviceACT.deviceOperate() execute()");
        this.setTime();
        data = queryBatchRestartDeviceBIO.getBatchRestartDeviceList(starttime1, endtime1);
        return "deviceBatchRestart";
    }

    /**
     * 设备批量重启列表导出Excel
     * @return
     */
    public String getBatchRestartDevExcel() {
        fileName = "集团光宽批量重启查询单";
        title = new String[] {"属地", "重启用户数", "重启成功数", "重启失败数", "重启改善用户数", "改善率(%)", "重启成功率(%)"};
        column = new String[] {"cityName", "deviceNum", "successNum", "failedNum", "improveNum", "improveRate", "successRate"};
        data = queryBatchRestartDeviceBIO.getBatchRestartDevExcel(starttime1, endtime1);
        return "excel";
    }


    /**
     * ou.yuan
     * 批量重启设备详情查询
     * @return
     */
    public String queryBatchRestartDevDetailList() {
        Map<String, Object> result = queryBatchRestartDeviceBIO.queryBatchRestartDevDetailList(cityId, curPage_splitPage, num_splitPage, starttime1, endtime1, numType);
        totalRowCount_splitPage = (Integer) (result.get("total"));
        maxPage_splitPage = getMaxPage_splitPage();

        if (null == result.get("list")) {
            batchRestartDevDetailList = null;
        }
        else {
            batchRestartDevDetailList = (List<Map<String, String>>) result.get("list");
        }
        return "deviceBatchRestartDetail";
    }


    /**
     * ou.yuan
     * 光宽批量重启设备详情导出Excel
     * @return
     */
    public String getBatchRestartDevDetailExcel() {
        fileName = "集团光宽批量重启详情单";
        title = new String[] {"设备厂商", "设备型号", "软件版本", "属地", "设备重启原因", "设备序列号", "重启前指标", "重启后指标", "设备IP", "LOID"};
        column = new String[] {"vendorName", "deviceModelName", "softwareVersion", "cityName", "rebootReason", "deviceSerialnumber", "beforeReStartNum", "afterReStartNum", "loopbackIp", "loid"};
        data = queryBatchRestartDeviceBIO.getBatchRestartDevDetailExcel(cityId, numType, starttime1, endtime1);
        return "excel";
    }



    /**
     * 时间转化
     */
    private void setTime() {
        logger.debug("setTime()" + starttime);
        DateTimeUtil dt = null; // 定义DateTimeUtil
        if (starttime == null || "".equals(starttime)) {
            starttime1 = null;
        }
        else {
            String start = starttime + " 00:00:00";
            DateTimeUtil st = new DateTimeUtil(start);
            starttime1 = String.valueOf(st.getLongTime());
        }
        if (endtime == null || "".equals(endtime)) {
            endtime1 = null;
        }
        else {
            String end = endtime + " 23:59:59";
            DateTimeUtil et = new DateTimeUtil(end);
            endtime1 = String.valueOf(et.getLongTime());
        }
    }



    public List<Map> getData()
    {
        return data;
    }

    public void setData(List<Map> data)
    {
        this.data = data;
    }

    public String[] getTitle()
    {
        return title;
    }

    public void setTitle(String[] title)
    {
        this.title = title;
    }

    public String[] getColumn()
    {
        return column;
    }

    public void setColumn(String[] column)
    {
        this.column = column;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getStarttime()
    {
        return starttime;
    }

    public void setStarttime(String starttime)
    {
        this.starttime = starttime;
    }

    public String getStarttime1()
    {
        return starttime1;
    }

    public void setStarttime1(String starttime1)
    {
        this.starttime1 = starttime1;
    }

    public String getEndtime()
    {
        return endtime;
    }

    public void setEndtime(String endtime)
    {
        this.endtime = endtime;
    }

    public String getEndtime1()
    {
        return endtime1;
    }

    public void setEndtime1(String endtime1)
    {
        this.endtime1 = endtime1;
    }

    public String getNumType() {
        return numType;
    }

    public void setNumType(String numType) {
        this.numType = numType;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public List<Map<String, String>> getBatchRestartDevDetailList() {
        return batchRestartDevDetailList;
    }

    public void setBatchRestartDevDetailList(List<Map<String, String>> batchRestartDevDetailList) {
        this.batchRestartDevDetailList = batchRestartDevDetailList;
    }

    public QueryBatchRestartDeviceBIO getQueryBatchRestartDeviceBIO() {
        return queryBatchRestartDeviceBIO;
    }

    public void setQueryBatchRestartDeviceBIO(QueryBatchRestartDeviceBIO queryBatchRestartDeviceBIO) {
        this.queryBatchRestartDeviceBIO = queryBatchRestartDeviceBIO;
    }

}
