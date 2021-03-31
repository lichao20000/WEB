package com.linkage.module.itms.report.act;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.WebUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.bio.HttpTestReportBIO;

import action.splitpage.splitPageAction;

/**
 * 
 * @author yaoli (Ailk No.)
 * @version 1.0
 * @since 2019年8月9日
 * @category com.linkage.module.itms.report.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class HttpTestReportACT extends splitPageAction
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5311051406702778413L;
	private static Logger logger = LoggerFactory.getLogger(HttpTestReportACT.class);

	private HttpTestReportBIO bio;
	
	private List<HashMap<String, String>> cityList = null;
	private String cityId = "";
	private int statCaliber = 0; //统计口径 :0 .全部 , 1.网厅 ，6.预处理
	private String startTime = "";
	private String endTime = "";
	private List<HashMap<String,String>> httpTestList = null;
	private List httpTestDetailList = null;
	/** 导出数据 */
	private List<HashMap<String,String>> data;
	/** 导出文件列标题 */
	private String[] title;
	/** 导出文件列 */
	private String[] column;
	/** 导出文件名 */
	private String fileName;
	 
	private String type;
	
	public String init(){
		DateTimeUtil dt = new DateTimeUtil();
		endTime = dt.getDate();
		startTime = dt.getFirtDayOfMonth();
		dt = new DateTimeUtil(endTime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000);
		endTime = dt.getLongDate();
		dt = new DateTimeUtil(startTime);
		startTime = dt.getLongDate();
		setCityList(bio.qryCity(WebUtil.getCurrentUser().getCityId()));
		return "init";
	}
	
	/**
	 * 查询
	 * @return
	 */
	public String qryHttpTestList(){
		if(StringUtil.IsEmpty(cityId) || "-1".equals(cityId)){
			cityId = WebUtil.getCurrentUser().getCityId();
		}
		setTime();
		httpTestList = bio.qryHttpTestList(cityId,statCaliber,startTime,endTime);
		return "list";
	}
	
	/**
	 * 查询导出
	 * @return
	 */
	public String qryExcel(){
		if(StringUtil.IsEmpty(cityId) || "-1".equals(cityId)){
			cityId = WebUtil.getCurrentUser().getCityId();
		}
		fileName = "普通用户测速报表";
		title = new String[] { "地市", "不达标", "达标", "总计", "达标率" };
		column = new String[] { "cityName", "noReached", "reached", "total", "reachedRate" };
		setTime();
		setData(bio.qryHttpTestList(cityId,statCaliber,startTime,endTime));
		return "excel";
	}
	
	/**
	 * 详细信息
	 * @return
	 */
	public String qryDetail(){
		setTime();
		httpTestDetailList = bio.qryDetail(cityId, type, statCaliber,startTime,endTime,curPage_splitPage,num_splitPage);
		int total = bio.qryDetailCount(cityId, type);

		if (total % num_splitPage == 0) {
			maxPage_splitPage = total / num_splitPage;
		} else {
			maxPage_splitPage = total / num_splitPage + 1;
		}
		return "detail";
	}
	
	public String qryDetailNext(){
		setTime();
		httpTestDetailList = bio.qryDetailNext(cityId, type, statCaliber,startTime,endTime,curPage_splitPage,num_splitPage);
		int total = bio.qryDetailCount(cityId, type);
		if (total % num_splitPage == 0) {
			maxPage_splitPage = total / num_splitPage;
		} else {
			maxPage_splitPage = total / num_splitPage + 1;
		}
		return "detail";
	}
	
	/**
	 * 详细导出
	 * @return
	 */
	public String getDetailExcel(){
		fileName = "普通用户详细测速报表";
		title = new String[] { "LOID", "厂家", "属地","型号", "硬件版本", "软件版本","签约速率","测速速率","上网方式","测试账户","支持GE","测速时间" };
		column = new String[] { "loid", "vendor_add", "city_name", "device_model", "hardwareversion","softwareversion",
				"speed","test_rate","wan_type","testusername","gbbroadband","test_time" };
		setData(bio.qryDetailExcel());
		return "excel";
	}
	
	private void setTime(){
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (startTime == null || "".equals(startTime)){
			startTime = null;
		}else{
			dt = new DateTimeUtil(startTime);
			startTime = String.valueOf(dt.getLongTime());
		}
		if (endTime == null || "".equals(endTime)){
			endTime = null;
		}else{
			dt = new DateTimeUtil(endTime);
			endTime = String.valueOf(dt.getLongTime());
		}
	}
	 
	public String getCityId()
	{
		return cityId;
	}
 
	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}
 
	public int getStatCaliber()
	{
		return statCaliber;
	}
 	
	public void setStatCaliber(int statCaliber)
	{
		this.statCaliber = statCaliber;
	}
 	
	public String getEndTime()
	{
		return endTime;
	}
 
	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}

	public HttpTestReportBIO getBio()
	{
		return bio;
	}
	public void setBio(HttpTestReportBIO bio)
	{
		this.bio = bio;
	}

	public String getStartTime()
	{
		return startTime;
	}

	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
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

	public List<HashMap<String, String>> getCityList()
	{
		return cityList;
	}

	public void setCityList(List<HashMap<String, String>> cityList)
	{
		this.cityList = cityList;
	}
	
	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public List<HashMap<String,String>> getData()
	{
		return data;
	}

	public void setData(List<HashMap<String,String>> data)
	{
		this.data = data;
	}

	public List<HashMap<String,String>> getHttpTestList()
	{
		return httpTestList;
	}

	public void setHttpTestList(List<HashMap<String,String>> httpTestList)
	{
		this.httpTestList = httpTestList;
	}

	public List getHttpTestDetailList()
	{
		return httpTestDetailList;
	}

	
	public void setHttpTestDetailList(List httpTestDetailList)
	{
		this.httpTestDetailList = httpTestDetailList;
	}
}
