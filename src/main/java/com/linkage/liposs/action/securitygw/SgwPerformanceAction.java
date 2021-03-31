package com.linkage.liposs.action.securitygw;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.jfree.chart.JFreeChart;

import com.linkage.liposs.action.cst;
import com.linkage.liposs.buss.bio.securitygw.SgwPMJFChar;
import com.linkage.liposs.buss.common.NetMaintenanceSub;
import com.linkage.liposs.buss.dao.securitygw.SgwPerformanceDao;
import com.linkage.litms.common.util.DateTimeUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 安全网关的维护管理action
 * @author wangping5221
 * @version 1.0
 * @since 2008-4-1
 * @category com.linkage.liposs.action.securitygw 
 * 版权：南京联创科技 网管科技部
 * 
 */
public class SgwPerformanceAction extends ActionSupport
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4858220905950539461L;

	private SgwPerformanceDao sgwPerformanceDao;

	// 设备ID
	private String device_id;

	// 描述
	private String desc;

	// 性能指标
	private String class1;

	// 设备信息
	private Map deviceInfoMap;

	// 画图
	private SgwPMJFChar sgwPMJFChar;

	private JFreeChart chart;

	// 报表类型(1:日报表;2:周报表;3:月报表)
	private String reportType;	

//	// 日报表开始时间
//	private String beginDayTime;
//
//	// 日报表结束时间
//	private String endDayTime;
//
//	// 周报表开始时间
//	private String beginWeekTime;
//
//	// 周报表结束时间
//	private String endWeekTime;
//
//	// 月报表开始时间
//	private String beginMonthTime;
//
//	// 月报表结束时间
//	private String endMonthTime;
	
	//来源方向,默认来自设备具体性能指标
	private String srcType="device";
	
	
	//网络时延报表
	private ArrayList netDelayList = null;
	
	private NetMaintenanceSub netMaintenanceSub;
	
	//为了防止浏览器的缓存
	private long time;

	/**
	 * 查询设备的详细信息
	 */
	public String execute() throws Exception
	{		
		try
		{
			deviceInfoMap = sgwPerformanceDao.getDevicePerformanceInfo(
							device_id, desc);
			String loopback_ip=(String)deviceInfoMap.get("loopback_ip");
			//网络时延的情况下
			if(null==class1)
			{
				netDelayList =netMaintenanceSub.getNetDelay(loopback_ip);
			}
			
			time =new Date().getTime();
		}
		catch (Exception e)
		{			
			e.printStackTrace();
		}		
		return SUCCESS;
	}

	/**
	 * 画各种性能报表图形
	 * 
	 * @return
	 */
	public String getPerformanceChart()
	{
		//当天零点时间
		DateTimeUtil now = new DateTimeUtil(new DateTimeUtil().getDate());
		now.getNextDate(1);
		//设置结束时间
		long endTime=now.getLongTime();
		//初始化开始时间
		long beginTime = endTime;
		int type = Integer.parseInt(reportType);
		
		switch (type)
		{
			// 日报表
			case 1:
				now.getNextDate(-1);
				beginTime =now.getLongTime();
				break;
			// 周报表
			case 2:				
				now.getNextDate(-7);
				beginTime = now.getLongTime();
				break;
			// 月报表
			case 3:				
				now.getNextMonth(-1);
				beginTime = now.getLongTime();
				break;
			// 默认日报表
			default:
				now.getNextDate(-1);
			    beginTime =now.getLongTime();
		}
		chart = sgwPMJFChar.getPerformance(device_id, class1, reportType,
						beginTime, endTime,srcType);
		return cst.OK;
	}

	public String getClass1()
	{
		return class1;
	}

	public void setClass1(String class1)
	{
		this.class1 = class1;
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		
		this.desc =desc;
	}

	public String getDevice_id()
	{
		return device_id;
	}

	public void setDevice_id(String device_id)
	{
		this.device_id = device_id;
	}

	public SgwPerformanceDao getSgwPerformanceDao()
	{
		return sgwPerformanceDao;
	}

	public void setSgwPerformanceDao(SgwPerformanceDao sgwPerformanceDao)
	{
		this.sgwPerformanceDao = sgwPerformanceDao;
	}

	public Map getDeviceInfoMap()
	{
		return deviceInfoMap;
	}

	public void setSgwPMJFChar(SgwPMJFChar sgwPMJFChar)
	{
		this.sgwPMJFChar = sgwPMJFChar;
	}

	public String getReportType()
	{
		return reportType;
	}

	public void setReportType(String reportType)
	{
		this.reportType = reportType;
	}

	public JFreeChart getChart()
	{
		return chart;
	}

//	public String getBeginDayTime()
//	{
//		return beginDayTime;
//	}
//
//	public String getBeginMonthTime()
//	{
//		return beginMonthTime;
//	}
//
//	public String getBeginWeekTime()
//	{
//		return beginWeekTime;
//	}
//
//	public String getEndDayTime()
//	{
//		return endDayTime;
//	}
//
//	public String getEndMonthTime()
//	{
//		return endMonthTime;
//	}
//
//	public String getEndWeekTime()
//	{
//		return endWeekTime;
//	}

	public String getSrcType()
	{
		return srcType;
	}

	public void setSrcType(String srcType)
	{
		this.srcType = srcType;
	}

	public ArrayList getNetDelayList()
	{
		return netDelayList;
	}

	public void setNetMaintenanceSub(NetMaintenanceSub netMaintenanceSub)
	{
		this.netMaintenanceSub = netMaintenanceSub;
	}

	public long getTime()
	{
		return time;
	}

}
