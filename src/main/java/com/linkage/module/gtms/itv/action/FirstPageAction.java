
package com.linkage.module.gtms.itv.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateTickUnit;

import com.linkage.module.gtms.itv.bio.FirstPageBIO;
import com.linkage.module.liposs.system.basesupport.BaseSupportAction;
import com.linkage.system.utils.chart.ChartTools;
import com.linkage.system.utils.chart.ChartUtil;

public class FirstPageAction extends BaseSupportAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7681867078174149043L;

	private static final Logger LOG = Logger.getLogger(FirstPageAction.class);
	
	private static String WEEKLY_INTERVAL="weekly";
	
	private static String DAILY_INTERVAL="daily";
	
	private FirstPageBIO firstPageBIO;
	
	private String ajax;
	
	public String getNFReport() throws ParseException
	{
		List queryList = firstPageBIO.getNFReport(WEEKLY_INTERVAL);
		SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
		for(Object o:queryList)
		{
			Map m = (Map)o;
			String date = m.get("reportdate").toString();
			m.put("reportdate", sdf.parse(date).getTime()/1000);
		}
		ChartUtil chartUtil = new ChartUtil();
		//JFreeChart chart = chartUtil.createPLP("科升VOD片源", "时间(天)", "数目", queryList,"err_count", "reportdate","err_count", false);
		chartUtil.setXAxisUnit(DateTickUnit.DAY, 1, "MM-dd");
		JFreeChart chart = chartUtil.createSTP("VOD错误片源总数", "时间(天)", "",new String[]{""}, new List[]{queryList}, "reportdate","err_count", 3,false);
		ajax = ChartTools.getChartWithContextUrl(chart, 324, 234, request);
		return "ajax";
	}
	public String getNFReportTwo() throws ParseException
	{
		List queryList = firstPageBIO.getNFReport(WEEKLY_INTERVAL);
		SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
		for(Object o:queryList)
		{
			Map m = (Map)o;
			String date = m.get("reportdate").toString();
			m.put("reportdate", sdf.parse(date).getTime()/1000);
		}
		ChartUtil chartUtil = new ChartUtil();
		//JFreeChart chart = chartUtil.createPLP("科升VOD片源", "时间(天)", "数目", queryList,"err_count", "reportdate","err_count", false);
		chartUtil.setXAxisUnit(DateTickUnit.DAY, 1, "MM-dd");
		JFreeChart chart = chartUtil.createSTP("VOD错误片源总数", "时间(天)", "",new String[]{""}, new List[]{queryList}, "reportdate","err_count", 3,false);
		ajax = ChartTools.getChartWithContextUrl(chart, 324, 200, request);
		return "ajax";
	}
	public String getEPGReport() throws ParseException
	{
		String cityId = "";
		try
		{
			cityId = getUser().getCityId();
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			LOG.warn("获取用户属地失败", e);
		}
		List queryList = firstPageBIO.getEPGReport(WEEKLY_INTERVAL,cityId);
//		for(Object o:queryList)
//		{
//			Map m = (Map)o;
//			String date = m.get("reportdate").toString();
//			String[] dateArray = date.split("-");
//			m.put("reportdate", dateArray[1]+"-"+dateArray[2]);
//		}
		Map<String,List> tempMap = new HashMap<String,List>();
		SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
		for(Object o:queryList)
		{
			Map m = (Map)o;
			String date = m.get("reportdate").toString();
			m.put("reportdate", sdf.parse(date).getTime()/1000);
			if(null != tempMap.get(m.get("city").toString()))
			{
				List list = (List) tempMap.get(m.get("city").toString());
				list.add(m);
			}
			else
			{
				List list = new ArrayList();
				list.add(m);
				tempMap.put(m.get("city").toString(), list);
			}
		}
		ChartUtil chartUtil = new ChartUtil();
		String[] cityA = new String[tempMap.keySet().size()];
		List[] valueA = new List[tempMap.keySet().size()];
		int i=0;
		Iterator it = tempMap.keySet().iterator();
		while(it.hasNext())
		{
			String str = (String) it.next();
			cityA[i] = str;
			List tempList = tempMap.get(str);
			valueA[i] = tempList;
			i++;
		}
//		JFreeChart chart = chartUtil.createCategoryBar3DP("EPG拨测错误总数", "时间(天)", "", queryList, "err_count", "reportdate",
//		"city", true);
		chartUtil.setXAxisUnit(DateTickUnit.DAY, 1, "MM-dd");
		JFreeChart chart = chartUtil.createXYCategoryBarP("EPG拨测错误总数", "时间(天)", "",cityA ,valueA, "reportdate","err_count",3, true);
		ajax = ChartTools.getChartWithContextUrl(chart, 324, 221, request);
		return "ajax";
	}
	
	
	/**
	 * 数据设备告警
	 * @return
	 */
	public String getDatainputReport()
	{
		List queryList = firstPageBIO.getDatainputReport();
			for(Object o : queryList)
			{
				Map m = (Map)o;
				int severity = Integer.parseInt(m.get("severity").toString());
				switch(severity)
				{
					case 0:
						m.put("severity", "清除告警");
						break;
					case 1:
						m.put("severity", "正常日志");
						break;
					case 2:
						m.put("severity", "提示告警");
						break;
					case 3:
						m.put("severity", "一般告警");
						break;
					case 4:
						m.put("severity", "严重告警");
						break;
					case 5:
						m.put("severity", "紧急告警");
						break;
				}
			}
			ChartUtil chartUtil = new ChartUtil();
			JFreeChart chart = chartUtil.createPieP("网络层面告警统计",queryList, "severity", "countnum", true, true);
			ajax = ChartTools.getChartWithContextUrl(chart, 160, 230, request);
			System.out.println(ajax);
		return "ajax";
	}
	
	/**
	 * 城域网告警
	 * @return
	 */
	public String getTownReport()
	{
		List queryList = firstPageBIO.getTownReport();
		for(Object o : queryList)
			{
				Map m = (Map)o;
				int severity = Integer.parseInt(m.get("severity").toString());
				switch(severity)
				{
					case 0:
						m.put("severity", "清除告警");
						break;
					case 1:
						m.put("severity", "正常日志");
						break;
					case 2:
						m.put("severity", "提示告警");
						break;
					case 3:
						m.put("severity", "一般告警");
						break;
					case 4:
						m.put("severity", "严重告警");
						break;
					case 5:
						m.put("severity", "紧急告警");
						break;
				}
			}
			ChartUtil chartUtil = new ChartUtil();
			JFreeChart chart = chartUtil.createPieP("网络层面告警统计",queryList, "severity", "countnum", true, true);
			ajax = ChartTools.getChartWithContextUrl(chart, 160, 230, request);
			System.out.println(ajax);
		return "ajax";
	}
	
	public String getWarnReport()
	{
		String cityId = "";
		try
		{
			cityId = getUser().getCityId();
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			LOG.warn("获取用户属地失败", e);
		}
		List queryList = firstPageBIO.getWarnReport(WEEKLY_INTERVAL,cityId);
		for(Object o : queryList)
		{
			Map m = (Map)o;
			int severity = Integer.parseInt(m.get("severity").toString());
			switch(severity)
			{
				case 0:
					m.put("severity", "清除告警");
					break;
				case 1:
					m.put("severity", "正常日志");
					break;
				case 2:
					m.put("severity", "提示告警");
					break;
				case 3:
					m.put("severity", "一般告警");
					break;
				case 4:
					m.put("severity", "严重告警");
					break;
				case 5:
					m.put("severity", "紧急告警");
					break;
			}
		}
		ChartUtil chartUtil = new ChartUtil();
		JFreeChart chart = chartUtil.createPieP("能力平台告警统计",queryList, "severity", "countnum", true, true);
		//ajax = ChartTools.getChartWithContextUrl(chart, 160, 240, request);
		ajax = ChartTools.getChartWithContextUrl(chart, 217, 255, request);
		System.out.println(ajax);
		return "ajax";
	}
	
	public String getServiceUser()
	{
		String cityId = "";
		try
		{
			cityId = getUser().getCityId();
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			LOG.warn("获取用户属地失败", e);
		}
		List queryList = firstPageBIO.getServiceUser(DAILY_INTERVAL,cityId);
		List<Map> list = new ArrayList<Map>();
		Map cityMap = firstPageBIO.getCityMap();
		for(Object o:queryList)
		{
			Map m = (Map)o;
			String city = cityMap.get(m.get("city_id").toString()).toString();
//			Map vgtemp = new HashMap();
//			vgtemp.put("city", city);
//			vgtemp.put("value", m.get("vgoodnum").toString());
//			vgtemp.put("numList", "优");
//			list.add(vgtemp);
//			Map gTemp = new HashMap();
//			gTemp.put("city", city);
//			gTemp.put("value", m.get("goodnum").toString());
//			gTemp.put("numList", "良");
//			list.add(gTemp);
//			Map norTemp = new HashMap();
//			norTemp.put("city", city);
//			norTemp.put("value", m.get("nornum").toString());
//			norTemp.put("numList", "一般");
//			list.add(norTemp);
			Map badTemp = new HashMap();
			badTemp.put("city", city);
			badTemp.put("value", null == m.get("badnum")?"":m.get("badnum").toString());
			badTemp.put("numList", "差");
			list.add(badTemp);
			Map talTemp = new HashMap();
			talTemp.put("city", city);
			talTemp.put("value", null == m.get("onlinenum")?"":m.get("onlinenum").toString());
			talTemp.put("numList", "在线总数");
			list.add(talTemp);
		}
		Util chartUtil = new Util();
		chartUtil.setCLablePosition(1);
		JFreeChart chart = chartUtil.createCategoryBarP("现网用户报表", "", "", list, "value",
				"city","numList", true);
		//ajax = ChartTools.getChartWithContextUrl(chart, 324, 186, request);
		ajax = ChartTools.getChartWithContextUrl(chart, 434, 200, request);
		return "ajax";
	}
	
	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public FirstPageBIO getFirstPageBIO()
	{
		return firstPageBIO;
	}

	public void setFirstPageBIO(FirstPageBIO firstPageBIO)
	{
		this.firstPageBIO = firstPageBIO;
	}
	
	
}
