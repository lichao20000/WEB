
package com.linkage.module.gtms.stb.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gtms.stb.utils.DeviceTypeUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * @author yinlei3 (73167)
 * @version v4.0.0.23_20150528
 * @since 2015年5月31日
 * @category com.linkage.module.gtms.stb.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class ShowAdverDAO extends SuperDAO
{

	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(ShowAdverDAO.class);
	/** 配置结果map */
	private HashMap<String, String> faultCodeMap = null;

	/**
	 * @category getVendor 获取所有的厂商
	 */
	@SuppressWarnings("rawtypes")
	public List getVendor()
	{
		logger.debug("getVendor()");
		PrepareSQL pSQL = new PrepareSQL(
				"select vendor_id,vendor_name, vendor_add from stb_tab_vendor");
		return jt.queryForList(pSQL.getSQL());
	}

	/**
	 * @category getVendor 根据厂商获取型号
	 * @param vendorId
	 */
	@SuppressWarnings("rawtypes")
	public List getDeviceModel(String vendorId)
	{
		logger.debug("getDeviceModel(vendorId:{})", vendorId);
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append(" select a.device_model_id,a.device_model from stb_gw_device_model a where 1=1 ");
		if ((null != vendorId) && (!"".equals(vendorId)) && (!"-1".equals(vendorId)))
		{
			pSQL.append(" and a.vendor_id='");
			pSQL.append(vendorId);
			pSQL.append("'");
		}
		return jt.queryForList(pSQL.getSQL());
	}

	/**
	 * @category getVersionList 获取所有的设备版本
	 * @param deviceModelId
	 * @return List
	 */
	@SuppressWarnings("rawtypes")
	public List getVersionList(String deviceModelIds)
	{
		logger.debug("getVersionList(deviceModelId:{})", deviceModelIds);
		String sql = "";
		String[] tempId = deviceModelIds.split(",");
		if (tempId.length > 0)
		{
			sql = "(";
			for (int i = 0; i < tempId.length; i++)
			{
				sql += "'" + tempId[i] + "',";
			}
			sql = sql.substring(0, sql.length() - 1) + ")";
		}
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select a.devicetype_id,a.softwareversion from stb_tab_devicetype_info a where 1=1 ");
		if (!StringUtil.IsEmpty(sql))
		{
			pSQL.append(" and a.device_model_id in");
			pSQL.append(sql);
			pSQL.append("");
		}
		return jt.queryForList(pSQL.toString());
	}

	/**
	 * 获取广告下发结果
	 *
	 * @param curPage_splitPage
	 *            当前页
	 * @param num_splitPage
	 *            每页行数
	 * @param vendorId
	 *            厂商ID
	 * @param deviceModelId
	 *            型号ID
	 * @param deviceTypeId
	 *            版本ID
	 * @param loopbackIp
	 *            最近一次上报IP
	 * @param deviceSerialnumber
	 *            设备序列号
	 * @param cpeMac
	 *            mac地址
	 * @param servAccount
	 *            业务账号
	 * @param faultType
	 *            下发状态
	 * @param tsakName
	 *            策略名称
	 * @return 下发广告查询 结果
	 */
	@SuppressWarnings("rawtypes")
	public List getAdverResultList(int curPage_splitPage, int num_splitPage,
			String vendorId, String deviceModelId, String deviceTypeId,
			String loopbackIp, String deviceSerialnumber, String cpeMac,
			String servAccount, String faultType, String taskName)
	{
		faultCodeMap = getFaultCode();
		StringBuffer sql = new StringBuffer();// TODO wait (more table related)
		sql.append("select b.city_id, b.vendor_id, b.device_model_id, b.devicetype_id, b.device_serialnumber," +
				"b.loopback_ip, b.cpe_mac, b.serv_account,c.status, a.result_id," +
				" a.start_time, a.task_id,c.task_name from stb_tab_gw_device b,stb_logo_record a,stb_logo_task c " +
				"where a.device_id = b.device_id and a.task_id = c.task_id ");
		if ((!StringUtil.IsEmpty(vendorId)) && (!"-1".equals(vendorId)))
		{
			sql.append(" and b.vendor_id='").append(vendorId).append("'");
		}
		if ((!StringUtil.IsEmpty(deviceModelId)) && (!"-1".equals(deviceModelId)))
		{
			sql.append(" and b.device_model_id='").append(deviceModelId).append("'");
		}
		if ((!StringUtil.IsEmpty(deviceTypeId)) && (!"-1".equals(deviceTypeId)))
		{
			sql.append(" and b.devicetype_id=").append(deviceTypeId);
		}
		if (!StringUtil.IsEmpty(loopbackIp))
		{
			sql.append(" and b.loopback_ip='").append(loopbackIp).append("'");
		}
		if (!StringUtil.IsEmpty(deviceSerialnumber))
		{
			sql.append(" and b.device_serialnumber like '%").append(deviceSerialnumber)
					.append("'");
		}
		if (!StringUtil.IsEmpty(cpeMac))
		{
			sql.append(" and b.cpe_mac='").append(cpeMac).append("'");
		}
		if (!StringUtil.IsEmpty(servAccount))
		{
			sql.append(" and b.serv_account='").append(servAccount).append("'");
		}
		if ((!StringUtil.IsEmpty(faultType)) && (!"-1".equals(faultType)))
		{
			// 下发状态为未下发
			if ("notIssued".equalsIgnoreCase(faultType))
			{
				sql.append(" and c.status=").append("0");
				// 下发状态为成功
			}
			else if ("success".equalsIgnoreCase(faultType))
			{
				sql.append(" and a.result_id in").append("(0,1)");
				// 下发状态为失败
			}
			else if ("fail".equalsIgnoreCase(faultType))
			{
				sql.append(" and a.result_id not in").append("(0,1)")
						.append(" and a.result_id is not null");
				;
			}
		}
		if (!StringUtil.IsEmpty(taskName))
		{
			sql.append(" and c.task_name='").append(taskName).append("'");
		}
		sql.append(" order by c.update_time desc");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage,
				num_splitPage, new RowMapper()
				{

					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						map.put("cityName", CityDAO.getCityName(StringUtil
								.getStringValue(rs.getString("city_id"))));
						try
						{
							long update_time = StringUtil.getLongValue(rs
									.getString("start_time"));
							if (update_time == 0)
							{
								map.put("update_time", "");
							}
							else
							{
								DateTimeUtil dt = new DateTimeUtil(update_time * 1000);
								map.put("update_time", dt.getLongDate());
							}
						}
						catch (NumberFormatException e)
						{
							map.put("update_time", "");
						}
						catch (Exception e)
						{
							map.put("update_time", "");
						}
						map.put("vendorName", DeviceTypeUtil.getVendorName(StringUtil
								.getStringValue(rs.getString("vendor_id"))));
						map.put("deviceModel", DeviceTypeUtil.getDeviceModel(StringUtil
								.getStringValue(rs.getString("device_model_id"))));
						map.put("deviceTypeName", DeviceTypeUtil
								.getDeviceSoftVersion(StringUtil.getStringValue(rs
										.getString("devicetype_id"))));
						map.put("deviceSerialNumber", StringUtil.getStringValue(rs
								.getString("device_serialnumber")));
						map.put("servAccount",
								StringUtil.getStringValue(rs.getString("serv_account")));
						map.put("loopback_ip",
								StringUtil.getStringValue(rs.getString("loopback_ip")));
						map.put("cpe_mac",
								StringUtil.getStringValue(rs.getString("cpe_mac")));
						map.put("task_name",
								StringUtil.getStringValue(rs.getString("task_name")));
						map.put("task_id",
								StringUtil.getStringValue(rs.getString("task_id")));
						if (!StringUtil.IsEmpty(StringUtil.getStringValue(rs
								.getString("result_id"))))
						{
							map.put("result", faultCodeMap.get(StringUtil
									.getStringValue(rs.getString("result_id"))));
						}
						else
						{
							map.put("result", "未下发");
						}
						logger.warn("----------Dao---result_id--------");
						logger.warn(faultCodeMap.get(StringUtil
								.getStringValue(rs.getString("result_id"))));
						logger.warn("----------Dao---result_id--------");
						return map;
					}
				});

		return list;
	}

	/**
	 * 获取定制的总页数
	 *
	 * @param num_splitPage
	 *            每页行数
	 * @param vendorId
	 *            厂商ID
	 * @param deviceModelId
	 *            型号ID
	 * @param deviceTypeId
	 *            版本ID
	 * @param loopbackIp
	 *            最近一次上报IP
	 * @param deviceSerialnumber
	 *            设备序列号
	 * @param cpeMac
	 *            mac地址
	 * @param servAccount
	 *            业务账号
	 * @param faultType
	 *            下发状态
	 * @param tsakName
	 *            策略名称
	 * @return 页数
	 */
	public int countAdverResultList(int num_splitPage, String vendorId,
			String deviceModelId, String deviceTypeId, String loopbackIp,
			String deviceSerialnumber, String cpeMac, String servAccount,
			String faultType, String taskName)
	{
		StringBuffer sql = new StringBuffer();// TODO wait (more table related)
		sql.append("select count(*) from stb_tab_gw_device b,stb_logo_record a,stb_logo_task c " +
				"where a.device_id = b.device_id and a.task_id = c.task_id  " );
		if ((!StringUtil.IsEmpty(vendorId)) && (!"-1".equals(vendorId)))
		{
			sql.append(" and b.vendor_id='").append(vendorId).append("'");
		}
		if ((!StringUtil.IsEmpty(deviceModelId)) && (!"-1".equals(deviceModelId)))
		{
			sql.append(" and b.device_model_id='").append(deviceModelId).append("'");
		}
		if ((!StringUtil.IsEmpty(deviceTypeId)) && (!"-1".equals(deviceTypeId)))
		{
			sql.append(" and b.devicetype_id=").append(deviceTypeId);
		}
		if (!StringUtil.IsEmpty(loopbackIp))
		{
			sql.append(" and b.loopback_ip='").append(loopbackIp).append("'");
		}
		if (!StringUtil.IsEmpty(deviceSerialnumber))
		{
			sql.append(" and b.device_serialnumber like '%").append(deviceSerialnumber)
					.append("'");
		}
		if (!StringUtil.IsEmpty(cpeMac))
		{
			sql.append(" and b.cpe_mac='").append(cpeMac).append("'");
		}
		if (!StringUtil.IsEmpty(servAccount))
		{
			sql.append(" and b.serv_account='").append(servAccount).append("'");
		}
		if ((!StringUtil.IsEmpty(faultType)) && (!"-1".equals(faultType)))
		{
			// 下发状态为未下发
			if ("notIssued".equals(faultType))
			{
				sql.append(" and c.status=").append("0");
				// 下发状态为成功
			}
			else if ("success".equals(faultType))
			{
				sql.append(" and a.result_id in").append("(0,1)");
				// 下发状态为失败
			}
			else if ("fail".equals(faultType))
			{
				sql.append(" and a.result_id not in").append("(0,1)")
						.append(" and a.result_id is not null");
				;
			}
		}
		if (!StringUtil.IsEmpty(taskName))
		{
			sql.append(" and c.task_name='").append(taskName).append("'");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0)
		{
			maxPage = total / num_splitPage;
		}
		else
		{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	/**
	 * 获取定制信息的详细情报
	 *
	 * @param taskId
	 *            任务Id
	 * @return 详细情报
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getTaskDetail(String taskId)
	{
		Map<String, String> taskMap = null;
		String sql = "select a.city_id, a.vendor_id, a.add_time, b.acc_loginname from stb_logo_task a,tab_accounts b where task_id=? and a.acc_oid=b.acc_oid";
		PrepareSQL taskSql = new PrepareSQL(sql);
		taskSql.setLong(1, StringUtil.getLongValue(taskId));
		taskMap = jt.queryForMap(taskSql.getSQL());
		if (null != taskMap)
		{
			if (null != taskMap.get("city_id"))
			{
				if (String.valueOf(taskMap.get("city_id")).indexOf(",") > 0)
				{
					String[] cityArr = String.valueOf(taskMap.get("city_id")).split(",");
					String cityName = " ";
					for (String cityid : cityArr)
					{
						cityName += CityDAO.getCityName(cityid) + ",";
					}
					taskMap.put("cityName", cityName.substring(0, cityName.length() - 1)
							.trim());
				}
				else
				{
					taskMap.put("cityName",
							CityDAO.getCityName(String.valueOf(taskMap.get("city_id"))));
				}
			}
			else
			{
				taskMap.put("cityName", "");
			}
			if (null != taskMap.get("vendor_id"))
			{
				taskMap.put("vendorName",
						DeviceTypeUtil.getVendorName(taskMap.get("vendor_id")));
			}
			else
			{
				taskMap.put("vendorName", "");
			}
			try
			{
				long add_time = StringUtil.getLongValue(taskMap.get("add_time"));
				DateTimeUtil dt = new DateTimeUtil(add_time * 1000);
				taskMap.put("add_time", dt.getLongDate());
			}
			catch (NumberFormatException e)
			{
				taskMap.put("add_time", "");
			}
			catch (Exception e)
			{
				taskMap.put("add_time", "");
			}
		}
		return taskMap;
	}

	/**
	 * 获取软件版本
	 *
	 * @param taskId
	 *            任务Id
	 * @return 软件版本
	 */
	@SuppressWarnings("rawtypes")
	public List getVerList(String taskId)
	{
		String sql = "select vendor_id, device_model_id, devicetype_id from stb_logo_version where task_id=?";
		PrepareSQL taskSql = new PrepareSQL(sql);
		taskSql.setLong(1, StringUtil.getLongValue(taskId));
		List taskMap = jt.queryForList(taskSql.getSQL());
		return taskMap;
	}

	/**
	 * 获取任务中定制的ip地址
	 *
	 * @param taskId
	 *            任务Id
	 * @return ip地址
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> getIpList(String taskId)
	{
		String sql = "select start_ip, end_ip from stb_logo_ipcheck  where task_id=?";
		PrepareSQL taskSql = new PrepareSQL(sql);
		taskSql.setLong(1, StringUtil.getLongValue(taskId));
		List taskMap = jt.queryForList(taskSql.getSQL());
		return taskMap;
	}

	/**
	 * 获取任务中定制的mac地址
	 *
	 * @param taskId
	 *            任务Id
	 * @return 任务Id
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> getMacList(String taskId)
	{
		String sql = "select start_mac, end_mac from stb_logo_maccheck  where task_id=?";
		PrepareSQL taskSql = new PrepareSQL(sql);
		taskSql.setLong(1, StringUtil.getLongValue(taskId));
		return jt.queryForList(taskSql.getSQL());
	}

	/**
	 * 获取配置结果map
	 *
	 * @return 配置结果map
	 */
	private HashMap<String, String> getFaultCode()
	{
		logger.debug("getFaultCode()");
		faultCodeMap = new HashMap<String, String>();
		String sql = "select fault_code,fault_desc  from tab_cpe_faultcode";
		PrepareSQL psql = new PrepareSQL(sql);
		List<HashMap<String, String>> faultCodeList = DBOperation.getRecords(psql
				.getSQL());
		if (faultCodeList != null && !faultCodeList.isEmpty())
		{
			for (HashMap<String, String> map : faultCodeList)
			{
				faultCodeMap.put(map.get("fault_code"), map.get("fault_desc"));
			}
		}
		return faultCodeMap;
	}
}
