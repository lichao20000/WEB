package com.linkage.module.gtms.stb.resource.action;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gtms.stb.resource.serv.ZeroConfigHistoryBIO;
import com.linkage.module.gwms.util.StringUtil;

public class ZeroConfigHistoryACT extends splitPageAction{

	private static final long serialVersionUID = 1L;
	
	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(ZeroConfigHistoryACT.class);
	
	private ZeroConfigHistoryBIO bio;
	
	/** 开始时间*/
	private String starttime;
	/** 结束时间*/
	private String endtime;
	/** 业务账号*/
	private String servAccount;
	/** 设备序列号*/
	private String deviceSerialnumber;
	
	private List<Map> data = null;
	
	/** 导出文件列标题 */
	private String[] title = null;
	
	/** 导出文件列 */
	private String[] column = null;
	
	/** 导出文件名 */
	private String fileName = null;
	
	/** 设备Id*/
	private String deviceId;
	
	/** 失败原因菜单配置信息标识*/
	private String failReasonMark;
	
	private String reasonId;
	
	private String ctiyId;
	
	
	/**
	 * 初始化历史配置
	 */
	public String execute(){
		logger.debug("ZeroConfigHistoryACT ==>execute()");
		logger.warn("Historical inquiry into the user profile page.");
		DateTimeUtil dt = new DateTimeUtil();
		starttime = dt.getFirtDayOfMonth() + " 00:00:00";
		endtime = new DateTimeUtil().getLongDate();
		return "success";
	}
	
	/**
	 * 查询历史配置数据
	 * @return
	 */
	public String doZeroHistoryQuery(){
		logger.debug("ZeroConfigHistoryACT ==>doZeroHistoryQuery()");
		logger.warn("Zero Configuration set-top box configuration historical data query");
		this.setTime();	
		this.data = bio.doZeroHistoryQueryPage(curPage_splitPage,num_splitPage,servAccount,deviceSerialnumber,starttime,endtime);
		int total = bio.doZeroHistoryQueryCount(servAccount,deviceSerialnumber,starttime,endtime);
		if (total % num_splitPage == 0) {
			maxPage_splitPage = total / num_splitPage;
		} else {
			maxPage_splitPage = total / num_splitPage + 1;
		}
		return "zeroHistoryQuery";
	}
	
	/**
	 * 查询设备历史配置详情
	 * @return
	 */
	public String doDeviceZeroHistoryQuery(){
		logger.debug("ZeroConfigHistoryACT ==>doDeviceZeroHistoryQuery()");
		logger.warn("Discover the history of zero configuration device Details");
		this.data = bio.getDeviceZeroConfigDetail(curPage_splitPage, num_splitPage, deviceId,starttime,endtime,failReasonMark,reasonId,ctiyId);
		int total = bio.getDeviceZeroConfigDetailCount(deviceId,starttime,endtime,failReasonMark,reasonId,ctiyId);
		if (total % num_splitPage == 0) {
			maxPage_splitPage = total / num_splitPage;
		} else {
			maxPage_splitPage = total / num_splitPage + 1;
		}
		return "zeroDeviceHistoryQuery";
	}
	
	/**
	 * 导出历史配置
	 * @return
	 */
	public String doZeroHistoryExcel(){
		logger.debug("ZeroConfigHistoryACT==>doZeroHistoryExcel()");
		logger.warn("Export historical data Zero Configuration set-top box");
		this.setTime();
		fileName = "ZeroConfigHistoryQuery";
		
		title = new String[7];
		column = new String[7];
		
		title[0] = "属地";
		title[1] = "业务账号";
		title[2] = "设备序列号";
		title[3] = "绑定时间";
		title[4] = "业务类型";
		title[5] = "当前阶段";
		title[6] = "状态";
		
		column[0] = "city_name";
		column[1] = "serv_account";
		column[2] = "device_serialnumber";
		column[3] = "bind_time";
		column[4] = "config_type";
		column[5] = "bind_way";
		column[6] = "bind_state";
		
		data = bio.doZeroHistoryExcel(servAccount,deviceSerialnumber,starttime,endtime);
		
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

	public ZeroConfigHistoryBIO getBio() {
		return bio;
	}

	public void setBio(ZeroConfigHistoryBIO bio) {
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

	public String getServAccount() {
		return servAccount;
	}

	public void setServAccount(String servAccount) {
		this.servAccount = servAccount;
	}

	public String getDeviceSerialnumber() {
		return deviceSerialnumber;
	}

	public void setDeviceSerialnumber(String deviceSerialnumber) {
		this.deviceSerialnumber = deviceSerialnumber;
	}
	
	public List<Map> getData() {
		return data;
	}

	public void setData(List<Map> data) {
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

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getFailReasonMark() {
		return failReasonMark;
	}

	public void setFailReasonMark(String failReasonMark) {
		this.failReasonMark = failReasonMark;
	}

	public String getReasonId() {
		return reasonId;
	}

	public void setReasonId(String reasonId) {
		this.reasonId = reasonId;
	}

	public String getCtiyId() {
		return ctiyId;
	}

	public void setCtiyId(String ctiyId) {
		this.ctiyId = ctiyId;
	}
}
