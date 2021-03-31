package com.linkage.liposs.action.securitygw;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.liposs.buss.bio.securitygw.EntSecStatBIO;
import com.linkage.liposs.buss.dao.securitygw.EntSecStatDAO;
import com.linkage.liposs.buss.dao.securitygw.SgwPerformanceDao;
import com.linkage.litms.common.util.DateTimeUtil;

public class EntSecStatAction extends splitPageAction
{
	/** log */
	private static Logger logger = LoggerFactory.getLogger(EntSecStatAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1572040972370052928L;
	private EntSecStatBIO entSecStat;
	private EntSecStatDAO entSecDAO;
	private SgwPerformanceDao sgwPerformanceDao;
	private String deviceName;
	private String type;
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public String getDeviceName()
	{
		return deviceName;
	}
	public void setDeviceName(String deviceName)
	{
		this.deviceName = deviceName;
	}
	public SgwPerformanceDao getSgwPerformanceDao()
	{
		return sgwPerformanceDao;
	}
	public void setSgwPerformanceDao(SgwPerformanceDao sgwPerformanceDao)
	{
		this.sgwPerformanceDao = sgwPerformanceDao;
	}
	public void setEntSecDAO(EntSecStatDAO entSecDAO)
	{
		this.entSecDAO = entSecDAO;
	}
	public void setEntSecStatBIO(EntSecStatBIO entSecStat)
	{
		this.entSecStat = entSecStat;
	}
	private String deviceId;
	private String customerId;
	private String customerName;
	private JFreeChart chart;// chart对象，提供给result
	private String starttime; // 开始时间
	private String endtime; // 结束时间
	private int virusType;
	private String srcIp;
	private String destIp;
	private int opType;
	private int attackType;
	private int protocolType;
	private List<Map> securityDetails;
	public int getProtocolType()
	{
		return protocolType;
	}
	public void setProtocolType(int protocolType)
	{
		this.protocolType = protocolType;
	}
	public int getAttackType()
	{
		return attackType;
	}
	public void setAttackType(int attackType)
	{
		this.attackType = attackType;
	}
	public String getStarttime()
	{
		return starttime;
	}
	public void setStarttime(String starttime)
	{
		this.starttime = starttime;
	}
	public String getEndtime()
	{
		return endtime;
	}
	public void setEndtime(String endtime)
	{
		this.endtime = endtime;
	}
	public int getVirusType()
	{
		return virusType;
	}
	public void setVirusType(int virusType)
	{
		this.virusType = virusType;
	}
	public String getSrcIp()
	{
		return srcIp;
	}
	public void setSrcIp(String srcIp)
	{
		this.srcIp = srcIp;
	}
	public String getDestIp()
	{
		return destIp;
	}
	public void setDestIp(String destIp)
	{
		this.destIp = destIp;
	}
	public int getOpType()
	{
		return opType;
	}
	public void setOpType(int opType)
	{
		this.opType = opType;
	}
	public JFreeChart getChart()
	{
		return chart;
	}
	public void setChart(JFreeChart chart)
	{
		this.chart = chart;
	}
	public String getDeviceId()
	{
		return deviceId;
	}
	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}
	public String getCustomerId()
	{
		return customerId;
	}
	public void setCustomerId(String customerId)
	{
		this.customerId = customerId;
	}
	public String getCustomerName()
	{
		return customerName;
	}
	public void setCustomerName(String customerName)
	{
		this.customerName = customerName;
	}
	/**
	 * 负责页面跳转
	 * 
	 * @return：success
	 */
	public String redirect() throws Exception
	{
		return SUCCESS;
	}
	/**
	 * 负责病毒详情页面跳转
	 * 
	 * @return：virusdetail
	 */
	public String virusDateDetail() throws Exception
	{
		return "virusdetail";
	}
	/**
	 * 负责获取当天的病毒统计信息图
	 * 
	 * @return：virusStatToday
	 */
	public String getVirusStatToday() throws Exception
	{
		chart = entSecStat.getVirusStatTodayByHour(deviceId);
		return "virusStatToday";
	}
	/**
	 * 负责获取当天的垃圾邮件统计信息图
	 * 
	 * @return：spamStatToday
	 */
	public String getSpamStatToday() throws Exception
	{
		chart = entSecStat.getSpamStatTodayByHour(deviceId);
		return "spamStatToday";
	}
	/**
	 * 负责获取当天的攻击统计信息图
	 * 
	 * @return：attackStatToday
	 */
	public String getAttackStatToday() throws Exception
	{
		chart = entSecStat.getAttackStatTodayByHour(deviceId);
		return "attackStatToday";
	}
	/**
	 * 负责获取当天的过滤统计信息图
	 * 
	 * @return：filterStatToday
	 */
	public String getFilterStatToday() throws Exception
	{
		chart = entSecStat.getFilterStatTodayByHour(deviceId);
		return "filterStatToday";
	}
	/**
	 * 负责获取当天的流量统计信息图
	 * 
	 * @return：fluxStatToday
	 */
	public String getFluxStatToday() throws Exception
	{
		chart = entSecStat.getFluxStatTodayByHour(deviceId);
		return "fluxStatToday";
	}
	/**
	 * 负责获取用户上网TOPN统计信息图
	 * 
	 * @return：fluxStatToday
	 */
	public String getTerminalConnStatToday() throws Exception
	{
		chart = entSecStat.getTerminalConnStatTodayByHour(deviceId);
		return "terminConnStatToday";
	}
	/**
	 * 负责获取网站访问TOPN统计信息图
	 * 
	 * @return：fluxStatToday
	 */
	public String getWebVisitTimes() throws Exception
	{
		chart = entSecStat.getWebVistTimesByHour(deviceId);
		return "webVisitTimes";
	}
	/**
	 * 查询病毒事件的原始信息
	 * 
	 * @return：queryViursDetails
	 */
	public String getVirusOrginData() throws Exception
	{
		// 设置每页显示的页数
		String sql = "";
		setNum_splitPage(50);
		maxPage_splitPage = 0; // 总共显示的页数
		securityDetails = null;
		long startTime = 0;
		long endTime = 0;
		if (deviceId == null)
			{
				deviceId = "123456";
			}
		Map map = sgwPerformanceDao.getDevicePerformanceInfo(deviceId, "");
		if (map != null)
			{
				deviceName = (String) map.get("device_name");
			}
		logger.debug("starttime=" + starttime);
//		if (starttime == null || endtime == null)
//			{
//				startTime = new Date().getTime();
//				endTime = startTime + 6 * 24 * 3600000;
//				endtime = new SimpleDateFormat("yyyy-MM-dd").format(new Date(endTime));
//				starttime = new SimpleDateFormat("yyyy-MM-dd")
//						.format(new Date(startTime));
//				endtime += " 00:00:00";
//				starttime += " 00:00:00";
//			}
//		else
//			{
//				startTime = new DateTimeUtil(starttime).getLongTime() * 1000;
//				endTime = new DateTimeUtil(endtime).getLongTime() * 1000;
//			}
		List list = getStartAndEndTime(type,starttime,endtime);
		startTime = (Long)list.get(0);
		endTime = (Long)list.get(1);
		logger.debug("starttime="+startTime+",endtime="+endTime);
		 
		sql = entSecDAO.getVirusOrigDataSQL(deviceId, startTime, endTime, virusType,
				srcIp, opType);
		try
			{
				// sql = entSecDAO.getVirusOrigDataSQL(deviceId, startTime,
				// endTime,
				// 1, "1.1.1.1", 1);
				maxPage_splitPage = entSecDAO.getCountForOrginData(sql);
				// 计算最大页大小
				maxPage_splitPage = ((maxPage_splitPage % num_splitPage == 0) ? (maxPage_splitPage / num_splitPage)
						: (maxPage_splitPage / num_splitPage + 1));
				securityDetails = entSecDAO.getVirusOrigData(sql, curPage_splitPage,
						num_splitPage);
				logger.debug("securityDetails.size=" + securityDetails.size());
			}
		catch (Exception e)
			{
				logger.error(e.toString());
			}
		return "queryViursDetails";
	}
	/**
	 * 查询攻击事件的原始信息
	 * 
	 * @return：queryAttackDetails
	 */
	public String getAttackOrginData() throws Exception
	{
		// 设置每页显示的页数
		String sql = "";
		setNum_splitPage(50);
		maxPage_splitPage = 0; // 总共显示的页数
		securityDetails = null;
		long startTime = 0;
		if (deviceId == null)
			{
				deviceId = "123456";
			}
		Map map = sgwPerformanceDao.getDevicePerformanceInfo(deviceId, "");
		if (map != null)
			{
				deviceName = (String) map.get("device_name");
			}
		long endTime = 0;
//		if (starttime == null || endtime == null)
//			{
//				startTime = new Date().getTime();
//				endTime = startTime + 6 * 24 * 3600000;
//				endtime = new SimpleDateFormat("yyyy-MM-dd").format(new Date(endTime));
//				starttime = new SimpleDateFormat("yyyy-MM-dd")
//						.format(new Date(startTime));
//				endtime += " 00:00:00";
//				starttime += " 00:00:00";
//			}
//		else
//			{
//				startTime = new DateTimeUtil(starttime).getLongTime() * 1000;
//				endTime = new DateTimeUtil(endtime).getLongTime() * 1000;
//			}
		List list = getStartAndEndTime(type,starttime,endtime);
		startTime = (Long)list.get(0);
		endTime = (Long)list.get(1);

		sql = entSecDAO.getAttackOrigDataSQL(deviceId, startTime, endTime, attackType,
				srcIp);
		maxPage_splitPage = entSecDAO.getCountForOrginData(sql);
		// 计算最大页大小
		maxPage_splitPage = ((maxPage_splitPage % num_splitPage == 0) ? (maxPage_splitPage / num_splitPage)
				: (maxPage_splitPage / num_splitPage + 1));
		securityDetails = entSecDAO.getAttackOrigData(sql, curPage_splitPage,
				num_splitPage);
		return "queryAttackDetails";
	}
	/**
	 * 查询垃圾邮件的原始信息
	 * 
	 * @return：queryAttackDetails
	 */
	public String getSpamMailOrginData() throws Exception
	{
		// 设置每页显示的页数
		String sql = "";
		setNum_splitPage(50);
		maxPage_splitPage = 0; // 总共显示的页数
		securityDetails = null;
		long startTime = 0;
		long endTime = 0;
		if (deviceId == null)
			{
				deviceId = "123456";
			}
		Map map = sgwPerformanceDao.getDevicePerformanceInfo(deviceId, "");
		if (map != null)
			{
				deviceName = (String) map.get("device_name");
			}
//		if (starttime == null || endtime == null)
//			{
//				startTime = new Date().getTime();
//				endTime = startTime + 6 * 24 * 3600000;
//				endtime = new SimpleDateFormat("yyyy-MM-dd").format(new Date(endTime));
//				starttime = new SimpleDateFormat("yyyy-MM-dd")
//						.format(new Date(startTime));
//				endtime += " 00:00:00";
//				starttime += " 00:00:00";
//			}
//		else
//			{
//				startTime = new DateTimeUtil(starttime).getLongTime() * 1000;
//				endTime = new DateTimeUtil(endtime).getLongTime() * 1000;
//			}
		List list = getStartAndEndTime(type,starttime,endtime);
		startTime = (Long)list.get(0);
		endTime = (Long)list.get(1);

		sql = entSecDAO.getOrigDataSQL(deviceId, startTime, endTime,"sgw_mail_original");
		maxPage_splitPage = entSecDAO.getCountForOrginData(sql);
		// 计算最大页大小
		maxPage_splitPage = ((maxPage_splitPage % num_splitPage == 0) ? (maxPage_splitPage / num_splitPage)
				: (maxPage_splitPage / num_splitPage + 1));
		securityDetails = entSecDAO.getSpamMailOrigData(sql, curPage_splitPage,
				num_splitPage);
		return "querySpamMailDetails";
	}
	/**
	 * 查询垃圾邮件的原始信息
	 * 
	 * @return：queryAttackDetails
	 */
	public String getFilterOrginData() throws Exception
	{
		// 设置每页显示的页数
		String sql = "";
		setNum_splitPage(50);
		maxPage_splitPage = 0; // 总共显示的页数
		securityDetails = null;
		long startTime = 0;
		long endTime = 0;
		if (deviceId == null)
			{
				deviceId = "123456";
			}
		Map map = sgwPerformanceDao.getDevicePerformanceInfo(deviceId, "");
		if (map != null)
			{
				deviceName = (String) map.get("device_name");
			}
//		if (starttime == null || endtime == null)
//			{
//				startTime = new Date().getTime();
//				endTime = startTime + 6 * 24 * 3600000;
//				endtime = new SimpleDateFormat("yyyy-MM-dd").format(new Date(endTime));
//				starttime = new SimpleDateFormat("yyyy-MM-dd")
//						.format(new Date(startTime));
//				endtime += " 00:00:00";
//				starttime += " 00:00:00";
//			}
//		else
//			{
//				startTime = new DateTimeUtil(starttime).getLongTime() * 1000;
//				endTime = new DateTimeUtil(endtime).getLongTime() * 1000;
//			}
		List list = getStartAndEndTime(type,starttime,endtime);
		startTime = (Long)list.get(0);
		endTime = (Long)list.get(1);
		sql = entSecDAO.getOrigDataSQL(deviceId, startTime, endTime,"sgw_filter_original");
		maxPage_splitPage = entSecDAO.getCountForOrginData(sql);
		// 计算最大页大小
		maxPage_splitPage = ((maxPage_splitPage % num_splitPage == 0) ? (maxPage_splitPage / num_splitPage)
				: (maxPage_splitPage / num_splitPage + 1));
		securityDetails = entSecDAO.getFilterOrigData(sql, curPage_splitPage,
				num_splitPage);
		return "queryFilterDetails";
	}
	public List<Map> getSecurityDetails()
	{
		return securityDetails;
	}
	public void setSecurityDetails(List<Map> securityDetails)
	{
		this.securityDetails = securityDetails;
	}
	/**
	 * 根据开始、结束时间、和时间标识符获取
	 * 
	 * @return：List<starttime,endtime>ms
	 */
	private List getStartAndEndTime(String type, String startTime, String endTime)
	{
		List list = new ArrayList();
		long startTimeL = 0 , endTimeL = 0,tmpL;
		if (type != null)
			{
				endtime = startTime + " 00:00:00";
 
				tmpL = new DateTimeUtil(endtime).getLongTime()+24*3600*1;
				endtime = new SimpleDateFormat("yyyy-MM-dd")
				.format(new Date(tmpL*1000)) + " 00:00:00";
	 
				endTimeL = new DateTimeUtil(endtime).getLongTime();
				 
				if (type.equals("day"))
					{
						startTimeL = endTimeL-24*3600 ;
 
					}
				else
					if (type.equals("week"))
						{
							startTimeL = endTimeL - 7 * 24 * 3600;
						}
					else
						if (type.equals("month"))
							{
								startTimeL = endTimeL - 30 * 24 * 3600;
							}
				starttime = new SimpleDateFormat("yyyy-MM-dd")
						.format(new Date(startTimeL*1000));
//				endtime += " 00:00:00";
				starttime += " 00:00:00";
				logger.debug("starttime="+starttime+",endtime="+endtime);
				list.add(startTimeL*1000);
				list.add(endTimeL*1000-1);
				return list;
			}
		else
			{
				if (starttime == null || endtime == null)
				{
					endTimeL = new Date().getTime();
					
					starttime = new SimpleDateFormat("yyyy-MM-dd").format(new Date(
							endTimeL));
					endTimeL = endTimeL+24 * 3600000;
					endtime = new SimpleDateFormat("yyyy-MM-dd").format(new Date(
							endTimeL));
 
					endtime += " 00:00:00";
					starttime += " 00:00:00";
					startTimeL = new DateTimeUtil(starttime).getLongTime() * 1000;
					endTimeL = new DateTimeUtil(endtime).getLongTime() * 1000;
				}else{
					startTimeL = new DateTimeUtil(starttime).getLongTime() * 1000;
					endTimeL = new DateTimeUtil(endtime).getLongTime() * 1000;
				}
			}
		list.add(startTimeL);
		list.add(endTimeL);
 
		return list;
	}
}
