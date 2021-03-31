package com.linkage.module.gtms.stb.resource.action;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gtms.stb.resource.serv.ZeroConfigFailReasonBIO;
import com.linkage.module.gwms.util.StringUtil;

public class ZeroConfigFailReasonACT extends splitPageAction{

	private static final long serialVersionUID = 1L;
	
	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(ZeroConfigFailReasonACT.class);
	
	private ZeroConfigFailReasonBIO bio;
	
	/** 开始时间*/
	private String starttime;
	
	/** 结束时间*/
	private String endtime;
	
	private List<List<String>> dataString;
	
	private List<Map> data;
	
	private String reasonId;
	
	private String cityId;
	
	/** 导出文件列标题 */
	private String[] title = null;
	
	/** 导出文件列 */
	private String[] column = null;
	
	/** 导出文件名 */
	private String fileName = null;
	
	/**
	 * 初始化
	 */
	public String execute(){
		logger.debug("ZeroConfigFailReasonACT ==>execute()");
		logger.warn("Into the cause of the failure statistics page Zero Configuration");
		DateTimeUtil dt = new DateTimeUtil();
		starttime = dt.getFirtDayOfMonth() + " 00:00:00";
		endtime = new DateTimeUtil().getLongDate();
		return "init";
	}
	
	/**
	 * 统计失败原因
	 * @return
	 */
	public String queryZeroConfigFailData(){
		logger.debug("ZeroConfigFailReasonACT ==>queryZeroConfigFailData()");
		logger.warn("Zero Configuration reason for the failure statistics");
		setTime();
		List<String> cityNameList = bio.getAllCityName("00");
		Map<String,Map> cityFailMap = bio.queryZeroConfigFailReason(starttime, endtime, "00");
		dataString = bio.getZeroFailData(cityNameList, cityFailMap,"00");
		return "list";
	}
	
	/**
	 * 失败详情
	 * @return
	 */
	public String queryZeroConfigFailReasonDetail(){
		logger.debug("ZeroConfigFailReasonACT ==>queryZeroConfigFailReasonDetail()");
		logger.warn("Query failed Reason Details");
		
		data = bio.queryZeroConfigFailReasonDetail(reasonId, cityId,starttime,endtime,curPage_splitPage,num_splitPage);
		int total = bio.queryZeroConfigFailReasonDetailCount(reasonId, cityId, starttime, endtime);
		if (total % num_splitPage == 0) {
			maxPage_splitPage = total / num_splitPage;
		} else {
			maxPage_splitPage = total / num_splitPage + 1;
		}		
		return "detail";
	}
	
	/**
	 * 导出失败详情
	 * @return
	 */
	public String zeroConfigFaileDeviceExcel(){
		logger.debug("ZeroConfigFailReasonACT==>zeroConfigFaileDeviceExcel()");
		logger.warn("For more details, export the reason for the failure");
		
		fileName = "ZeroConfigFaileDetail";
		
		title = new String[8];
		column = new String[8];
		
		title[0] = "属地";
		title[1] = "厂商";
		title[2] = "设备型号";
		title[3] = "软件版本";
		title[4] = "设备序列号";
		title[5] = "MAC";
		title[6] = "业务账号";
		title[7] = "设备IP";
		
		column[0] = "city_name";
		column[1] = "vendor_name";
		column[2] = "device_model";
		column[3] = "softwareversion";
		column[4] = "device_serialnumber";
		column[5] = "cpe_mac";
		column[6] = "serv_account";
		column[7] = "loopback_ip";
		
		data = bio.zeroConfigFaileDeviceExcel(reasonId, cityId,starttime,endtime);
		return "excel";
	}
	
	/**
	 * 时间转化
	 */
	private void setTime(){
		logger.debug("BusinessOpenCountActionImpl==>setTime()" + starttime);
		//该月第一天
		if(StringUtil.IsEmpty(starttime) && StringUtil.IsEmpty(endtime))
		{
			starttime = String.valueOf(new DateTimeUtil(new DateTimeUtil().getFirtDayOfMonth()).getLongTime());
			endtime   = String.valueOf(new DateTimeUtil().getLongTime());
		}
		else
		{
			starttime = String.valueOf(new DateTimeUtil(starttime).getLongTime());
			endtime   = String.valueOf(new DateTimeUtil(endtime).getLongTime());
		}
		
	}
	
	public ZeroConfigFailReasonBIO getBio() {
		return bio;
	}

	public void setBio(ZeroConfigFailReasonBIO bio) {
		this.bio = bio;
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



	public String getReasonId() {
		return reasonId;
	}

	public void setReasonId(String reasonId) {
		this.reasonId = reasonId;
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

	public List<List<String>> getDataString() {
		return dataString;
	}

	public void setDataString(List<List<String>> dataString) {
		this.dataString = dataString;
	}

	public List<Map> getData() {
		return data;
	}

	public void setData(List<Map> data) {
		this.data = data;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
}
