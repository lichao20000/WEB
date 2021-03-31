package com.linkage.module.itms.report.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.itms.report.bio.BusOnceDownSucBIO;

public class BusOnceDownSucACT extends splitPageAction implements SessionAware {

	private static Logger logger = LoggerFactory
			.getLogger(BusOnceDownSucACT.class);

	private Map session;
	/** 属地 */
	private String cityId;
	/** 开始时间 */
	private String starttime;
	/** 结束时间 */
	private String endtime;
	/** 开始时间转化后 */
	private String starttime1;
	/** 结束时间转化后 */
	private String endtime1;
	/** 业务类型Id */
	private String servTypeId;
	/**业务信息信息*/
	private List<Map> servInfoList;
	/** 导出数据 */
	private List<Map<String, Object>> data;
	/** 导出文件列标题 */
	private String[] title;
	/** 导出文件列 */
	private String[] column;
	/** 导出文件名 */
	private String fileName;
	
	private String gwType;

	private BusOnceDownSucBIO busOnceDownSucBIO;
	

	/**
	 * 查询成功率
	 * 
	 * @return
	 */
	public String queryList() {
		logger.debug("queryList({},{},{})", cityId, starttime,
				endtime);
		logger.warn("查询成功率:starttime={},endtime={},cityId={}",starttime,endtime,cityId);
		this.setTime();
		data = busOnceDownSucBIO.queryDataList(cityId, starttime1, endtime1, gwType);
		return "list";
	}

	/**
	 * 导出查询的数据
	 * @return
	 */
	public String getAllResultExcel() {
		logger.debug("getAllResultExcel({},{},{})", cityId, starttime1, endtime1);
		if (Global.AHDX.equals(Global.instAreaShortName)) {
			title = new String[] { "本地网", "voip一次下发成功率", "总下发成功率" };
			column = new String[] { "cityName", "voipSucRate", "totalSucRate" };
		} else {
			title = new String[] { "本地网", "宽带一次下发成功率", "IPTV一次下发成功率", "voip一次下发成功率", "总下发成功率" };
			column = new String[] { "cityName", "broadbandSucRate", "iptvSucRate", "voipSucRate", "totalSucRate" };
		}
		fileName = "业务一次下发成功率统计";
		data = busOnceDownSucBIO.queryDataList(cityId, starttime1, endtime1,gwType);
		return "excel";
	}
	/**
	 * 获取业务信息的详细信息
	 * @return
	 */
	public String getServInfoDetail() {
		logger.debug("getServInfoDetail({},{},{},{})", cityId, starttime1, endtime1, servTypeId);

		servInfoList = busOnceDownSucBIO.getServInfoDetail(cityId, starttime1,
				endtime1, servTypeId, curPage_splitPage, num_splitPage,gwType);

		totalRowCount_splitPage = busOnceDownSucBIO.getServInfoCount(cityId,
				starttime1, endtime1, servTypeId,gwType);

		maxPage_splitPage = 1;
		if (totalRowCount_splitPage % num_splitPage == 0){
			maxPage_splitPage = totalRowCount_splitPage / num_splitPage;
		}else{
			maxPage_splitPage = totalRowCount_splitPage / num_splitPage + 1;
		}
		logger.warn("返回值totalPageNum:{}",maxPage_splitPage);

		return "serInfoList";
	}


	/**
	 * 导出业务信息详细信息
	 * @return
	 */
	public String getServInfoExcel() {
		logger.debug("getServInfoExcel({},{},{},{})", cityId, starttime1, endtime1, servTypeId );
		title = new String[]{"设备序列号","逻辑SN","受理时间","业务名称","业务账号","业务开通状态"};
		column = new String[]{"deviceSerialnumber","logicSN", "dealdate" ,"servType","username","openStatus"};
		fileName = "busOnceDownSuccessRate";
		data = busOnceDownSucBIO.getServInfoExcel(cityId, starttime1, endtime1,servTypeId,gwType);	
		return "excel";
	}

	/**
	 * 时间转化
	 */
	private void setTime() {
		logger.debug("setTime({})",starttime);
		DateTimeUtil dt = null;
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

	public BusOnceDownSucBIO getBusOnceDownSucBIO() {
		return busOnceDownSucBIO;
	}

	public String getGwType() {
		return gwType;
	}

	public void setGwType(String gwType) {
		this.gwType = gwType;
	}

	public void setBusOnceDownSucBIO(BusOnceDownSucBIO busOnceDownSucBIO) {
		this.busOnceDownSucBIO = busOnceDownSucBIO;
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

   
}
