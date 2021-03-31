
package com.linkage.module.gwms.report.act;

import action.splitpage.splitPageAction;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.report.bio.QueryBatchSpeedMeasureBIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

/**
 * JXDX-REQ-ITMS-20201013-WWF-003(江西电信ITMS+家庭网关新增批量定制测速结果查询页面需求)
 * @author ou.yuan
 * @date 2020-10-15
 */
public class QueryBatchSpeedMeasureACT extends splitPageAction {

    // 日志记录
    private static Logger logger = LoggerFactory.getLogger(QueryBatchSpeedMeasureACT.class);
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
    /** 任务名称 */
    private String taskName = "";

    private String taskNameEncode = "";



    private String cityId = "";

    private String downlink = "";

    private String numType = "";


    private List<Map<String, String>> batchSpeedMeasureDetailList = null;

    private QueryBatchSpeedMeasureBIO queryBatchSpeedMeasureBIO;


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
        logger.warn("QueryBatchSpeedMeasureACT.init()");
        taskName = "";
        starttime = "";
        endtime = "";
        // 获取系统当前时间
        /*String dateTime = DateUtil.getNowTime("yyyy-MM-dd");
        endtime = dateTime;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        // 将date - 7
        c.add(Calendar.DATE, -7);
        Date startDate = c.getTime();

        // 获取一周之内批量重启设备
        starttime = format.format(startDate);*/
        return "init";
    }


    /**
     * 批量测速结果查询
     *
     * @author ouyuan
     * @date 9.2, 2020
     * @return String
     */
    public String queryBatchSpeedMeasureList() {
        logger.warn("QueryBatchSpeedMeasureACT.queryBatchSpeedMeasureList()");
        this.setTime();
        data = queryBatchSpeedMeasureBIO.getBatchSpeedMeasureList(starttime1, endtime1, taskName);
        return "deviceBatchSpeedMeasure";
    }

    /**
     * 批量测速结果查询列表导出Excel
     * @return
     */
    public String getBatchSpeedMeasureExcel() {
        fileName = "批量测速结果查询单";
        title = new String[] {"地市", "测速总数", "达标总数", "总体达标率(%)", "测速100M总数", "测速100M达标总数", "测速100M达标率(%)",
                "测速200M总数", "测速200M达标总数", "测速200M达标率(%)", "测速300M总数", "测速300M达标总数", "测速300M达标率(%)",
                "测速500M总数", "测速500M达标总数", "测速500M达标率(%)", "测速1000M总数", "测速1000M达标总数", "测速1000M达标率(%)"};
        column = new String[] {"cityName", "measureAllNum", "accStandAllNum", "accStandAllRate", "measureAllNum_100", "accStandAllNum_100", "accStandAllRate_100",
                "measureAllNum_200", "accStandAllNum_200", "accStandAllRate_200", "measureAllNum_300", "accStandAllNum_300", "accStandAllRate_300",
                "measureAllNum_500", "accStandAllNum_500", "accStandAllRate_500", "measureAllNum_1000", "accStandAllNum_1000", "accStandAllRate_1000"};
        this.decodeTaskName();
        data = queryBatchSpeedMeasureBIO.getBatchSpeedMeasureExcel(starttime1, endtime1, taskName);
        return "excel";
    }


    /**
     * ou.yuan
     * 批量测速结果详情查询
     * @return
     */
    public String queryBatchSpeedMeasureDetailList() {
        this.decodeTaskName();
        Map<String, Object> result = queryBatchSpeedMeasureBIO.queryBatchSpeedMeasureDetailList(cityId, curPage_splitPage, num_splitPage, starttime1, endtime1, downlink, taskName, numType);
        totalRowCount_splitPage = (Integer) (result.get("total"));
        maxPage_splitPage = getMaxPage_splitPage();

        if (null == result.get("list")) {
            batchSpeedMeasureDetailList = null;
        }
        else {
            batchSpeedMeasureDetailList = (List<Map<String, String>>) result.get("list");
        }
        return "deviceBatchSpeedMeasureDetail";
    }


    /**
     * ou.yuan
     * 批量测速结果详情导出Excel
     * @return
     */
    public String getBatchSpeedMeasureDetailExcel() {
        this.decodeTaskName();
        fileName = "批量测速结果详情单";
        title = new String[] {"CITY_NAME", "LOID", "KD", "上网方式", "DEVICE_NAME", "下行签约速率", "PON类型", "厂家", "型号",
                "软件版本", "硬件版本", "天翼网关类型", "是否千兆猫", "是否支持测速", "测速时间", "测试结果",
                "测速结果", "测速质量", "达标情况"};
        column = new String[] {"city_name", "loid", "KD", "onlineType", "device_name", "downlink", "PON_TYPE", "vendor_add", "device_model",
                "softwareversion", "hardwareversion", "device_version_type", "gbbroadband", "is_speedtest", "test_time", "http_result_one",
                "total_down_pert_deal", "speedQuality", "accessStandard"};
        data = queryBatchSpeedMeasureBIO.getBatchSpeedMeasureDetailExcel(cityId, downlink, starttime1, endtime1, taskName, numType);
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

    private void decodeTaskName() {
        try {
            taskName =  URLDecoder.decode(taskNameEncode, "UTF-8");
        }
        catch (Exception e) {
            logger.warn("QueryBatchSpeedMeasureACT.decodeTaskName, taskNameEncode:{}", taskNameEncode);
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

    public String getDownlink() {
        return downlink;
    }

    public void setDownlink(String downlink) {
        this.downlink = downlink;
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

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public List<Map<String, String>> getBatchSpeedMeasureDetailList() {
        return batchSpeedMeasureDetailList;
    }

    public void setBatchSpeedMeasureDetailList(List<Map<String, String>> batchSpeedMeasureDetailList) {
        this.batchSpeedMeasureDetailList = batchSpeedMeasureDetailList;
    }

    public QueryBatchSpeedMeasureBIO getQueryBatchSpeedMeasureBIO() {
        return queryBatchSpeedMeasureBIO;
    }

    public void setQueryBatchSpeedMeasureBIO(QueryBatchSpeedMeasureBIO queryBatchSpeedMeasureBIO) {
        this.queryBatchSpeedMeasureBIO = queryBatchSpeedMeasureBIO;
    }

    public String getTaskNameEncode() {
        return taskNameEncode;
    }

    public void setTaskNameEncode(String taskNameEncode) {
        this.taskNameEncode = taskNameEncode;
    }
}
