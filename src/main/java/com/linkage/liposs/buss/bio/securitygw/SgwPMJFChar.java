package com.linkage.liposs.buss.bio.securitygw;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateTickUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.liposs.buss.dao.securitygw.SgwPerformanceDao;
import com.linkage.liposs.buss.util.ChartUtil;
import com.linkage.litms.common.util.DateTimeUtil;

/**
 * 安全网关的维护管理的图形处理类
 * @author wangping5221
 * @version 1.0
 * @since 2008-4-1
 * @category com.linkage.liposs.buss.bio.securitygw 
 * 版权：南京联创科技 网管科技部
 * 
 */

public class SgwPMJFChar
{
	private static Logger log = LoggerFactory.getLogger(SgwPMJFChar.class);
	
	// 数据库查询类
	private SgwPerformanceDao sgwPerformanceDao;

	// 图形类
	private ChartUtil cu;

	/**
	 * 获取性能的多实例的图形显示
	 * 
	 * @param device_id
	 * @param class1
	 * @param reportType
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JFreeChart getPerformance(String device_id, String class1,
					String reportType, long beginTime, long endTime,String srcType)
	{
		JFreeChart reJFC;
		// 获取表名(考虑跨表)
		String[] tableNames = getTableName(reportType, beginTime, endTime);

		// 获取性能指标下的各性能实例
		List<Map> pmeeID = null;

		// 获取列名
		String columnName = getColumnName(reportType);

		// 获取日期展示形式
		int timeType = getTimeType(reportType);

		String unit = "";
		String title = "CPU利用率";
		String xAName = "时间";
		String yAName = "性能数值";
		String xName = "gathertime";
		String yName = columnName;
		// 查询数据结果集
		List<Map>[] listData = null;
		// 设置各曲线的说明
		String[] rowKeys;

		//设置图标题
		if ("1".equals(class1))
		{
			title = "CPU利用率";
		}
		else if ("2".equals(class1))
		{
			title = "内存利用率";
		}
		else if ("8".equals(class1))
		{
			title = "设备连接数";
		}
		
        //设置时间显示格式
		int type = Integer.parseInt(reportType);
		switch (type)
		{
			case 1:
				cu.setXAxisUnit(DateTickUnit.HOUR, 4, "HH:mm");
				break;
			case 2:
				cu.setXAxisUnit(DateTickUnit.DAY, 1, "E");
				break;
			case 3:
				cu.setXAxisUnit(DateTickUnit.DAY, 2, "dd");
				break;
			default:
				cu.setXAxisUnit(DateTickUnit.HOUR, 2, "HH:mm");
		}		
		
		//来自具体设备的性能指标的情况下，title要加上时间
		if("device".equals(srcType))
		{
			title+="("+new DateTimeUtil(beginTime*1000).getLongDate()+"到"+new DateTimeUtil(endTime*1000).getLongDate()+")";
			if(1==type)
			{
				cu.setXAxisUnit(DateTickUnit.HOUR, 2, "HH:mm");
			}
		}
		
		//设置起始、结束时间
		cu.setXDate(new Date(beginTime*1000),new Date(endTime*1000));

		try
		{
			pmeeID = getPerformanceID(device_id, class1);

			// 没有配置性能指标
			if (pmeeID.size() == 0)
			{
				listData = new ArrayList[0];
				rowKeys = new String[0];
			}
			else
			{
				unit = (String) pmeeID.get(0).get("unit");
				//title = (String) pmeeID.get(0).get("name");
				xAName = "时间";
				yAName = "值(单位:" + unit + ")";
				xName = "gathertime";
				yName = columnName;

				// 查询数据结果集
				listData = sgwPerformanceDao.getPerformanceData(pmeeID,
								tableNames, columnName, beginTime, endTime);
				log.debug("getPerformance+++++++++size:"+listData[0].size());
				
				

				// 设置各曲线的说明
				rowKeys = new String[pmeeID.size()];
				for (int i = 0; i < pmeeID.size(); i++)
				{
					rowKeys[i] = (String) pmeeID.get(i).get("descr");
					
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			listData = new ArrayList[0];
			rowKeys = new String[0];
			cu.setNoDataMessage("数据库异常!");
		}

		//小图不需要描述信息
		if("device".equals(srcType))
		{
			reJFC = cu.createSTP(title, xAName, yAName, rowKeys, listData, xName,
							yName, timeType);
		}
		else
		{
			reJFC = cu.createSTP(title, xAName, yAName, rowKeys, listData, xName,
							yName, timeType,false);
		}
		
		return reJFC;
	}

	/**
	 * 获取日期显示格式
	 * 
	 * @param reportType
	 * @return
	 */
	private int getTimeType(String reportType)
	{
		int timeType = 1;
		int type = Integer.parseInt(reportType);
		switch (type)
		{
			case 1:
				timeType = 1;
				break;
			case 2:
				timeType = 1;
				break;
			case 3:
				timeType = 2;
				break;
			default:
				timeType = 1;
		}
		return timeType;
	}

	/**
	 * 获取列名
	 * 
	 * @param reportType
	 * @return
	 */
	private String getColumnName(String reportType)
	{
		String columnName = "value";
		int type = Integer.parseInt(reportType);
		switch (type)
		{
			case 1:
				columnName = "value";
				break;
			case 2:				
			case 3:
				columnName = "avgvalue";
				break;
			default:
				columnName = "value";
		}

		return columnName;
	}

	/**
	 * 
	 * @param device_id
	 * @param class1
	 * @return
	 */
	private List<Map> getPerformanceID(String device_id, String class1)
	{
		List<Map> listID = new ArrayList<Map>();
		listID = sgwPerformanceDao.getPerformanceInfo(device_id, class1);
		return listID;
	}

	/**
	 * 获取报表数据的来源表名
	 * 
	 * @param reportType
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private String[] getTableName(String reportType, long beginTime,
					long endTime)
	{
		String[] tableName = null;
		int type = Integer.parseInt(reportType);
		DateTimeUtil beginDate = new DateTimeUtil(beginTime * 1000);
		DateTimeUtil endDate = new DateTimeUtil(endTime * 1000);
		switch (type)
		{
			// 日报表
			case 1:
				tableName = new String[1];
				tableName[0] = "pm_raw_" + beginDate.getYear() + "_"
								+ beginDate.getMonth();
				break;
			// 周报表
			case 2:
				// 确定是否要跨表
//				if ((beginDate.getYear() + "_" + beginDate.getMonth())
//								.equals(endDate.getYear() + "_"
//												+ endDate.getMonth()))
//				{
//					tableName = new String[1];
//					tableName[0] = "pm_raw_" + beginDate.getYear() + "_"
//									+ beginDate.getMonth();
//				}
//				else
//				{
//					tableName = new String[2];
//					tableName[0] = "pm_raw_" + beginDate.getYear() + "_"
//									+ beginDate.getMonth();
//					tableName[1] = "pm_raw_" + endDate.getYear() + "_"
//									+ endDate.getMonth();
//				}
				if ((beginDate.getYear() + "_" + beginDate.getMonth())
								.equals(endDate.getYear() + "_"
												+ endDate.getMonth()))
				{
					tableName = new String[1];
					tableName[0] = "pm_hour_stats_" + beginDate.getYear() + "_"
									+ beginDate.getMonth();
				}
				else
				{
					tableName = new String[2];
					tableName[0] = "pm_hour_stats_" + beginDate.getYear() + "_"
									+ beginDate.getMonth();
					tableName[1] = "pm_hour_stats_" + endDate.getYear() + "_"
									+ endDate.getMonth();
				}
				break;
			// 月报表
			case 3:
				// 判断是否要跨表
				if ((beginDate.getYear() + "_" + beginDate.getMonth())
								.equals(endDate.getYear() + "_"
												+ endDate.getMonth()))
				{
					tableName = new String[1];
					tableName[0] = "pm_hour_stats_" + beginDate.getYear() + "_"
									+ beginDate.getMonth();
				}
				else
				{
					tableName = new String[2];
					tableName[0] = "pm_hour_stats_" + beginDate.getYear() + "_"
									+ beginDate.getMonth();
					tableName[1] = "pm_hour_stats_" + endDate.getYear() + "_"
									+ endDate.getMonth();
				}
				break;
			// 默认按照日报表来显示
			default:
				tableName = new String[1];
				tableName[0] = "pm_raw_" + beginDate.getYear() + "_"
								+ beginDate.getMonth();

		}

		return tableName;
	}

	public void setCu(ChartUtil cu)
	{
		this.cu = cu;
	}

	public void setSgwPerformanceDao(SgwPerformanceDao sgwPerformanceDao)
	{
		this.sgwPerformanceDao = sgwPerformanceDao;
	}

}
