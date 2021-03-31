package com.linkage.module.ids.act;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.ids.bio.NetWorkQualityAnalyseBIO;


/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2014-2-27
 * @category com.linkage.module.ids.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class NetWorkQualityAnalyseACT extends splitPageAction implements SessionAware,ServletRequestAware
{
	
	private static Logger logger = LoggerFactory.getLogger(NetWorkQualityAnalyseACT.class);
	
	private NetWorkQualityAnalyseBIO bio;
	
	private HttpServletRequest request;
	
	private Map session;
	
	// 开始时间
	private String startOpenDate = "";
	// 开始时间
	private String startOpenDate1 = "";
	// 结束时间
	private String endOpenDate = "";
	// 结束时间
	private String endOpenDate1 = "";
	
	private String loid;
	
	private String device_serialnumber;
	
	private List<Map> netWorkList;
	
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	
	
	@Override
	public String execute() throws Exception
	{
		logger.warn("init()");
		startOpenDate = this.getStartDate();
		endOpenDate = this.getEndDate();
		return "success";
	}
	
	public String netWorkQualityAnalyseInfo(){
		//this.setTime();
		netWorkList = bio.netWorkQualityAnalyseInfo(startOpenDate, endOpenDate, device_serialnumber, loid, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countNetWorkQualityAnalyseInfo(startOpenDate, endOpenDate, device_serialnumber, loid, curPage_splitPage, num_splitPage);
		return "list";
	}
	public String netWorkQualityAnalyseInfoExcel(){
		//this.setTime();
		netWorkList = bio.netWorkQualityAnalyseInfoExcel(startOpenDate, endOpenDate, device_serialnumber, loid);
		String excelCol = "area_name#subarea_name#loid#device_serialnumber#tx_power#rx_power#linkaddress#olt_name#olt_ip#pon_id#ont_id#count_num#avg_delay#loss_pp#time";
		String excelTitle = "区域#子区域#LOID#设备序列号#发送光功率#接收光功率#接收地址#OLT名称#OLTIP#PON端口#ONTID#出现次数#平均延迟#时延出现次数#日期";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "netWorkList";
		data = netWorkList;
		return "excel";
	}
	

	/**
	 * 时间转化
	 */
	private void setTime()
	{
		logger.debug("setTime()" + startOpenDate);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (startOpenDate == null || "".equals(startOpenDate))
		{
			startOpenDate1 = null;
		}
		else
		{
			dt = new DateTimeUtil(startOpenDate);
			startOpenDate1 = String.valueOf(dt.getLongTime());
		}
		if (endOpenDate == null || "".equals(endOpenDate))
		{
			endOpenDate1 = null;
		}
		else
		{
			dt = new DateTimeUtil(endOpenDate);
			endOpenDate1 = String.valueOf(dt.getLongTime());
		}
	}

	// 当前年的1月1号
	private String getStartDate()
	{
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		now.set(GregorianCalendar.DATE, 1);
		now.set(GregorianCalendar.MONTH, 0);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	// 当前时间的23:59:59,如 2011-05-11 23:59:59
	private String getEndDate()
	{
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		now.set(Calendar.HOUR_OF_DAY, 23);
		now.set(Calendar.MINUTE, 59);
		now.set(Calendar.SECOND, 59);
		String time = fmtrq.format(now.getTime());
		return time;
	}
	

	@Override
	public void setServletRequest(HttpServletRequest request)
	{
		// TODO Auto-generated method stub
		this.request = request;
	}

	@Override
	public void setSession(Map<String, Object> session)
	{
		// TODO Auto-generated method stub
	}

	
	public NetWorkQualityAnalyseBIO getBio()
	{
		return bio;
	}

	
	public void setBio(NetWorkQualityAnalyseBIO bio)
	{
		this.bio = bio;
	}

	
	public String getStartOpenDate()
	{
		return startOpenDate;
	}

	
	public void setStartOpenDate(String startOpenDate)
	{
		this.startOpenDate = startOpenDate;
	}

	
	public String getEndOpenDate()
	{
		return endOpenDate;
	}

	
	public void setEndOpenDate(String endOpenDate)
	{
		this.endOpenDate = endOpenDate;
	}

	
	public String getLoid()
	{
		return loid;
	}

	
	public void setLoid(String loid)
	{
		this.loid = loid;
	}

	
	public String getDevice_serialnumber()
	{
		return device_serialnumber;
	}

	
	public void setDevice_serialnumber(String device_serialnumber)
	{
		this.device_serialnumber = device_serialnumber;
	}

	
	public List<Map> getNetWorkList()
	{
		return netWorkList;
	}

	
	public void setNetWorkList(List<Map> netWorkList)
	{
		this.netWorkList = netWorkList;
	}

	
	public List<Map> getData()
	{
		return data;
	}

	
	public void setData(List<Map> data)
	{
		this.data = data;
	}

	
	public String[] getColumn()
	{
		return column;
	}

	
	public void setColumn(String[] column)
	{
		this.column = column;
	}

	
	public String[] getTitle()
	{
		return title;
	}

	
	public void setTitle(String[] title)
	{
		this.title = title;
	}

	
	public String getFileName()
	{
		return fileName;
	}

	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
}
