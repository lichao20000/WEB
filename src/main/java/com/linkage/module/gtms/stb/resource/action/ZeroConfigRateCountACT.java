package com.linkage.module.gtms.stb.resource.action;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gtms.stb.resource.serv.ZeroConfigRateCountBIO;
import com.linkage.module.gwms.util.StringUtil;

public class ZeroConfigRateCountACT extends splitPageAction{

	private static final long serialVersionUID = 1L;

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(ZeroConfigRateCountACT.class);
	
	private ZeroConfigRateCountBIO bio;
	
	/** 开始时间*/
	private String starttime;
	/** 结束时间*/
	private String endtime;
	/** 控件对应的开始时间、结束时间*/
	private String start ;
	private String end;
	
	private String cityId ;
	private String bindState ;
	private String bindWay ;
	
	@SuppressWarnings("rawtypes")
	private List<Map> data = null;
	
	/** 导出文件列标题 */
	private String[] title = null;
	
	/** 导出文件列 */
	private String[] column = null;
	
	/** 导出文件名 */
	private String fileName = null;
	
	/**
	 * 初始化开通统计
	 */
	public String execute(){
		logger.debug("ZeroConfigRateCountACT==>execute()");
		logger.warn("Enter zero configuration page opening success rate statistics.");
		DateTimeUtil dt = new DateTimeUtil();
		start = dt.getFirtDayOfMonth() + " 00:00:00";
		end = new DateTimeUtil().getLongDate();
		return "success";
	}
	
	/**
	 * 统计开通情况
	 * @return
	 */
	public String countAll(){
        logger.debug("ZeroConfigRateCountACT==>countAll()");
        logger.warn("Statistical success rate of zero configuration opening.");
		this.setTime();		
		data = bio.countAll(starttime,endtime,"00");
		
		return "countList";
	}

	/**
	 * 设备详情界面
	 * @author 岩 
	 * @date 2016-6-7
	 * @return
	 */
	public String queryZeroConfigDetail(){
		logger.debug("ZeroConfigFailReasonACT ==>queryZeroConfigFailReasonDetail()");
		logger.warn("Query failed Reason Details");
		
		data = bio.queryZeroConfigDetail(cityId,bindState,bindWay,starttime,endtime,curPage_splitPage,num_splitPage);
		int total = bio.queryZeroConfigDetailCount(cityId, bindState, bindWay, starttime, endtime);
		if (total % num_splitPage == 0) {
			maxPage_splitPage = total / num_splitPage;
		} else {
			maxPage_splitPage = total / num_splitPage + 1;
		}		
		return "detail";
	}
	
	/**
	 * 下载各地区的统计情况
	 * 导出excel
	 */
	public String getZeroConfigCountExcel() {
		
		logger.debug("ZeroConfigRateCountACT==>getZeroConfigCountExcel()");
		logger.warn("Export success rate of zero configuration opened EXCEl.");
		this.setTime();
		fileName = "ZeroConfigSuccessRate";
		
		title = new String[15];
		column = new String[15];
		
		title[0] = "属地";
		title[1] = "机顶盒安装总数";
		title[2] = "零配置总数";
		title[3] = "零配置安装成功数";
		title[4] = "零配置安装失败数";
		title[5] = "其他绑定数";
		title[6] = "零配置率";
		title[7] = "零配置成功率";
		title[8] = "爱运维总数";
		title[9] = "E8-C查询MAC：匹配成功数";
		title[10] = "E8-C查询MAC：匹配成功率";
		title[11] = "通过IP查询AAA宽带账号：匹配成功数";
		title[12] = "通过IP查询AAA宽带账号：匹配成功率";
		title[13] = "输入宽带账号自助安装：匹配成功数";
		title[14] = "输入宽带账号自助安装：匹配成功率";
		
		column[0] = "cityName";
		column[1] = "bindNumTal";
		column[2] = "zeroBindNumTal";
		column[3] = "successBindNumTal";
		column[4] = "failBindNumTal";
		column[5] = "zeroFailOtherTal";
		column[6] = "zeroBindRate";
		column[7] = "successRate";
		column[8] = "imiNumTal";
		column[9] = "macSuccTal";
		column[10] = "macRate";
		column[11] = "ipSuccTal";
		column[12] = "ipRate";
		column[13] = "itvSuccTal";
		column[14] = "itvRate";
		
		data = bio.countAll(starttime,endtime,"00");
		
		return "excel";
	}
	
	/**
	 * 时间转化
	 */
	private void setTime(){
		logger.debug("BusinessOpenCountActionImpl==>setTime()" + starttime);
		//该月第一天
		if(StringUtil.IsEmpty(start) && StringUtil.IsEmpty(end))
		{
			starttime = String.valueOf(new DateTimeUtil(new DateTimeUtil().getFirtDayOfMonth()).getLongTime());
			endtime   = String.valueOf(new DateTimeUtil().getLongTime());
		}
		else
		{
			starttime = String.valueOf(new DateTimeUtil(start).getLongTime());
			endtime   = String.valueOf(new DateTimeUtil(end).getLongTime());
		}
		
	}
	public ZeroConfigRateCountBIO getBio() {
		return bio;
	}

	public void setBio(ZeroConfigRateCountBIO bio) {
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

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getData() {
		return data;
	}

	@SuppressWarnings("rawtypes")
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

	
	public String getCityId()
	{
		return cityId;
	}

	
	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	
	public String getBindState()
	{
		return bindState;
	}

	
	public void setBindState(String bindState)
	{
		this.bindState = bindState;
	}

	
	public String getBindWay()
	{
		return bindWay;
	}

	
	public void setBindWay(String bindWay)
	{
		this.bindWay = bindWay;
	}
	
	
}
