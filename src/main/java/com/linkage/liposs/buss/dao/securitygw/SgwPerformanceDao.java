package com.linkage.liposs.buss.dao.securitygw;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.DateTimeUtil;

/**
 * 安全网关的维护管理的数据库查询类
 * 
 * @author wangping5221
 * @version 1.0
 * @since 2008-4-1
 * @category com.linkage.liposs.buss.dao.securitygw 版权：南京联创科技 网管科技部
 * 
 */
public class SgwPerformanceDao
{
	private static Logger log = LoggerFactory.getLogger(SgwPerformanceDao.class);

	private JdbcTemplate jt;

	private PrepareSQL ppSQL;

	// 设备信息查询sql
	private static String deviceInfoSQL = "select device_name,loopback_ip,device_model_id as device_model from tab_gw_device where device_id=?";

	// 设备查询状态
	private static String deviceStateSQL = "select severity,cpu_util,mem_util,connection_util,ping_value from tab_taskplan_data where device_id=? and table_id=?";

	// 设备端口数
	// private static String portNumSQL = "select count(1) port_num from
	// flux_interfacedeviceport where device_id=?";

	// 性能实例ID查询sql
	private static String pmeeIDSQL = "select c.id ,a.unit,a.name,c.descr,c.indexid from pm_expression a,pm_map b,pm_map_instance c where a.expressionid=b.expressionid "
					+ " and a.expressionid=c.expressionid and b.device_id=c.device_id and b.isok=1 and a.class1=? and b.device_id=?";

	/**
	 * 查询具体的性能实例数据,一个性能实例对应一个List<Map>
	 * 
	 * @param pmeeID
	 * @param tableNames
	 * @param columnName
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map>[] getPerformanceData(List<Map> pmeeID,
					String[] tableNames, String columnName, long beginTime,
					long endTime)
	{
		String temppSQL = "select ?,gathertime from ? a where a.gathertime>=? and  a.gathertime<=? and a.id =? ";
		List<Map>[] list = new ArrayList[pmeeID.size()];
		String id = "";
		String sql = "";
		for (int i = 0; i < pmeeID.size(); i++)
		{
			id = (String) pmeeID.get(i).get("id");

			// 形成sql
			for (int j = 0; j < tableNames.length; j++)
			{
				if (j == 0)
				{
					sql = temppSQL;
				}
				else
				{
					sql += "union " + temppSQL;
				}
			}
			ppSQL.setSQL(sql);

			// 设置sql参数
			for (int j = 0; j < tableNames.length; j++)
			{
				ppSQL.setStringExt(5 * j + 1, columnName, false);
				ppSQL.setStringExt(5 * j + 2, tableNames[j], false);
				ppSQL.setLong(5 * j + 3, beginTime);
				ppSQL.setLong(5 * j + 4, endTime);
				ppSQL.setString(5 * j + 5, id);
			}

			log.debug("getPerformanceData_SQL(" + id + "):" + ppSQL.getSQL());			
			list[i] = jt.queryForList(ppSQL.getSQL());			
//			log.debug("getPerformanceData"+i+"_size:" + list[i].size());
		}

		return list;
	}

	/**
	 * 查询性能ID的list
	 * 
	 * @param device_id
	 * @param class1
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getPerformanceInfo(String device_id, String class1)
	{
		List<Map> list = jt.queryForList(pmeeIDSQL, new Object[] {
										new Integer(class1), device_id });
		return list;
	}

	/**
	 * 获取设备性能详细信息
	 * 
	 * @param device_id
	 * @param desc
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map getDevicePerformanceInfo(String device_id, String desc)
	{
		Map devicePerformanceInfo = new HashMap();
		log.debug("device_id:" + device_id);

		// 查询设备信息
		List<Map> list = jt.queryForList(deviceInfoSQL,
						new String[] { device_id });
		if (list.size() != 0)
		{
			devicePerformanceInfo = list.get(0);
		}
		else
		{
			devicePerformanceInfo.put("device_name", "");
			devicePerformanceInfo.put("device_model", "");
			devicePerformanceInfo.put("loopback_ip", "");
		}

		// 查询设备状态,cpu利用率
		DateTimeUtil now = new DateTimeUtil();
		String table_id = device_id + "_" + now.getShortDate();
//		log.debug("getDevicePerformanceInfo_device_id:" + device_id
//						+ "    table_id:" + table_id);
		// cpu利用率(现在只有查询当天的,没有查询周或月的概念)
		list = jt.queryForList(deviceStateSQL, new String[] { device_id,
										table_id });
		Map tempMap = null;
		if (list.size() != 0)
		{
			tempMap = list.get(0);

			devicePerformanceInfo.put("severity", ((BigDecimal) tempMap
							.get("severity")).toString());
			// cpu
			if (null == tempMap.get("cpu_util"))
			{
				devicePerformanceInfo.put("cpu_util", "");
			}
			else
			{
				devicePerformanceInfo.put("cpu_util", ((BigDecimal) tempMap
								.get("cpu_util")).toString());
			}
			// 内存
			if (null == tempMap.get("mem_util"))
			{
				devicePerformanceInfo.put("mem_util", "");
			}
			else
			{
				devicePerformanceInfo.put("mem_util", ((BigDecimal) tempMap
								.get("mem_util")).toString());
			}
			// 设备连接数
			if (null == tempMap.get("connection_util"))
			{
				devicePerformanceInfo.put("connection_util", "");
			}
			else
			{
				devicePerformanceInfo.put("connection_util",
								((BigDecimal) tempMap.get("connection_util"))
												.toString());
			}
			// 网络时延
			if (null == tempMap.get("ping_value"))
			{
				devicePerformanceInfo.put("ping_value", "");
			}
			else
			{
				devicePerformanceInfo.put("ping_value", ((BigDecimal) tempMap
								.get("ping_value")).toString());
			}
		}
		else
		{
			// 默认普通状态
			devicePerformanceInfo.put("severity", "1");
			devicePerformanceInfo.put("cpu_util", "");
			devicePerformanceInfo.put("mem_util", "");
			devicePerformanceInfo.put("connection_util", "");
			devicePerformanceInfo.put("ping_value", "");
		}

//		log
//						.debug("cpu_util:"
//										+ (String) devicePerformanceInfo
//														.get("cpu_util")
//										+ "     mem_util:"
//										+ (String) devicePerformanceInfo
//														.get("cpu_util")
//										+ "   connection_util:"
//										+ (String) devicePerformanceInfo
//														.get("connection_util")
//										+ "     ping_value:"
//										+ (String) devicePerformanceInfo
//														.get("connection_util"));

		// 描述
		devicePerformanceInfo.put("desc", desc);

		return devicePerformanceInfo;
	}

	public void setDao(DataSource dao)
	{
		jt = new JdbcTemplate(dao);
	}

	public void setPpSQL(PrepareSQL ppSQL)
	{
		this.ppSQL = ppSQL;
	}

}
