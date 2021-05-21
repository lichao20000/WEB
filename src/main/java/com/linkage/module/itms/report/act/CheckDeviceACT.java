package com.linkage.module.itms.report.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.itms.report.bio.CheckDeviceBIO;

public class CheckDeviceACT extends splitPageAction implements SessionAware {

    private static Logger logger = LoggerFactory
            .getLogger(CheckDeviceACT.class);

    private Map session;
    /**
     * 属地
     */
    private String cityId;
    /**
     * 开始时间
     */
    private String starttime;
    /**
     * 结束时间
     */
    private String endtime;
    /**
     * 开始时间转化后
     */
    private String starttime1;
    /**
     * 结束时间转化后
     */
    private String endtime1;
    /**
     * 业务类型Id
     */
    private String servTypeId;
    /**
     * 业务信息信息
     */
    private List<Map> servInfoList;
    /**
     * 导出数据
     */
    private List<Map<String, Object>> data;
    /**
     * 导出文件列标题
     */
    private String[] title;
    /**
     * 导出文件列
     */
    private String[] column;
    /**
     * 导出文件名
     */
    private String fileName;

    private String gwType;

    private String open_status;
    
    private String checkState;

    private CheckDeviceBIO checkDeviceBIO;


    /**
     * 查询成功率
     *
     * @return
     */
    public String queryList() {
        logger.debug("queryList({},{},{})", new Object[]{cityId, starttime,
                endtime});
        logger.warn("queryList稽核结果:checkState=" + checkState
                + "  cityId=" + cityId);
        this.setTime();
        data = checkDeviceBIO.queryDataList(cityId, checkState, gwType);
        return "list";
    }



    /**
     * 导出查询的数据
     *
     * @return
     */
    public String getAllResultExcel() {
        logger.debug("getAllResultExcel({},{},{})", new Object[]{cityId,
        		checkState});

        title = new String[]{"属地", "未稽核", "稽核成功数", "稽核失败数", "稽核成功率"};
        column = new String[]{"cityname", "undo", "succ", "fail", "rate"};
        fileName = "光猫稽核结果";
        data = checkDeviceBIO.queryDataList(cityId, checkState, gwType);
        return "excel";
    }


    /**
     * 获取业务信息的详细信息
     *
     * @return
     */
    public String getServInfoDetail() {
        logger.debug("getServInfoDetail({},{},{},{})", new Object[]{cityId,checkState});
        servInfoList = checkDeviceBIO.getServInfoDetail(cityId, checkState, curPage_splitPage, num_splitPage);
        maxPage_splitPage = checkDeviceBIO.getServInfoCount(cityId, checkState, curPage_splitPage, num_splitPage);
        return "serInfoList";
    }



    /**
     * 导出业务信息详细信息
     *
     * @return
     */
    public String getServInfoExcel() {
        logger.debug("getServInfoExcel({},{},{},{})", new Object[]{cityId, checkState});
        title = new String[]{"厂商", "型号", "软件版本", "属地", "设备序列号", "IP"};
        column = new String[]{"vendor_name", "device_model", "softwareversion", "city_name", "device_serialnumber", "loopback_ip"};
        fileName = "光猫稽核详细";
        data = checkDeviceBIO.getServInfoExcel(cityId, checkState);
        return "excel";
    }


    /**
     * 时间转化
     */
    private void setTime() {
        logger.debug("setTime()" + starttime);
        DateTimeUtil dt = null;// 定义DateTimeUtil
        if (starttime == null || "".equals(starttime)) {
            starttime1 = null;
        } else {
            dt = new DateTimeUtil(starttime);
            starttime1 = String.valueOf(dt.getLongTime());
        }
        if (endtime == null || "".equals(endtime)) {
            endtime1 = null;
        } else {
            dt = new DateTimeUtil(endtime);
            endtime1 = String.valueOf(dt.getLongTime());
        }
    }

    public Map getSession() {
        return session;
    }

    @Override
    public void setSession(Map session) {
        this.session = session;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getStarttime1() {
        return starttime1;
    }

    public void setStarttime1(String starttime1) {
        this.starttime1 = starttime1;
    }

    public String getEndtime1() {
        return endtime1;
    }

    public void setEndtime1(String endtime1) {
        this.endtime1 = endtime1;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    public String[] getTitle() {
        return title;
    }

    public void setTitle(String[] title) {
        this.title = title;
    }

    public String[] getColumn() {
        return column;
    }

    public void setColumn(String[] column) {
        this.column = column;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public String getGwType() {
        return gwType;
    }

    public void setGwType(String gwType) {
        this.gwType = gwType;
    }


    public String getServTypeId() {
        return servTypeId;
    }

    public void setServTypeId(String servTypeId) {
        this.servTypeId = servTypeId;
    }

    public List<Map> getServInfoList() {
        return servInfoList;
    }

    public void setServInfoList(List<Map> servInfoList) {
        this.servInfoList = servInfoList;
    }

    public String getOpen_status() {
        return open_status;
    }

    public void setOpen_status(String open_status) {
        this.open_status = open_status;
    }

	
	public String getCheckState()
	{
		return checkState;
	}

	
	public void setCheckState(String checkState)
	{
		this.checkState = checkState;
	}

	
	public CheckDeviceBIO getCheckDeviceBIO()
	{
		return checkDeviceBIO;
	}

	
	public void setCheckDeviceBIO(CheckDeviceBIO checkDeviceBIO)
	{
		this.checkDeviceBIO = checkDeviceBIO;
	}
}
