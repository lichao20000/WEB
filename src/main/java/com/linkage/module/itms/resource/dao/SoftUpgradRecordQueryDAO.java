
package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.WebUtil;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.itms.resource.util.DeviceTypeUtil;
import com.linkage.system.utils.DateTimeUtil;
import com.linkage.system.utils.database.DBUtil;

/**
 * @author yinlei3 (73167)
 * @version 1.0
 * @since 2015年6月3日
 * @category com.linkage.module.itms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
@SuppressWarnings("deprecation")
public class SoftUpgradRecordQueryDAO extends SuperDAO
{

	/** 日志 */
	private static Logger logger = LoggerFactory
			.getLogger(SoftUpgradRecordQueryDAO.class);

	/**
	 * 获取所有的厂商
	 * 
	 * @return 厂商集合
	 */
	@SuppressWarnings("rawtypes")
	public List getVendors()
	{
		logger.debug("getVendors()");
		PrepareSQL pSQL = new PrepareSQL(
				"select vendor_id,vendor_name, vendor_add from tab_vendor order by vendor_name");
		return jt.queryForList(pSQL.getSQL());
	}

	/**
	 * 获取所有的设备型号
	 * 
	 * @param vendorId
	 *            设备Id
	 * @return 设备型号集合
	 */
	@SuppressWarnings("rawtypes")
	public List getDeviceModels(String vendorId)
	{
		logger.debug("getDeviceModels(vendorId:{})", vendorId);
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append(" select a.device_model_id,a.device_model from gw_device_model a where 1=1 ");
		if (null != vendorId && !"".equals(vendorId) && !"-1".equals(vendorId))
		{
			pSQL.append(" and a.vendor_id='");
			pSQL.append(vendorId);
			pSQL.append("'");
		}
		pSQL.append(" order by device_model");
		return jt.queryForList(pSQL.getSQL());
	}

	/**
	 * 获取所有软件版本
	 * 
	 * @param deviceModelId
	 *            设备型号
	 * @return 软件版本集合
	 */
	@SuppressWarnings("rawtypes")
	public List getVersionList(String deviceModelId)
	{
		logger.debug("getVersionList(deviceModelId:{})", deviceModelId);
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select a.devicetype_id,a.softwareversion from tab_devicetype_info a where 1=1 ");
		if ((!StringUtil.IsEmpty(deviceModelId)) && (!"-1".equals(deviceModelId)))
		{
			pSQL.append(" and a.device_model_id='");
			pSQL.append(deviceModelId);
			pSQL.append("'");
		}
		return jt.queryForList(pSQL.getSQL());
	}

	/**
	 * 软件升级记录查询
	 * 
	 * @param vendor
	 *            终端厂家Id
	 * @param device_model
	 *            终端型号Id
	 * @param starttime
	 *            升级开始起始时间
	 * @param endtime
	 *            升级开始截止时间
	 * @param curPage_splitPage
	 *            当前页
	 * @param num_splitPage
	 *            每页行数
	 * @return 软件升级记录集合
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getSoftUpgradRecordQuery(String vendor, String device_model,
			String starttime, String endtime, int curPage_splitPage, int num_splitPage)
	{
		logger.debug(
				"SoftUpgradRecordQueryDAO==>getSoftUpgradRecordQuery({},{},{},{},{})",
				new Object[] { vendor, device_model, starttime, endtime });

		PrepareSQL psql = new PrepareSQL();
		psql.append("select record_id,vendor_id,device_model_id,current_devicetype_id,target_devicetype,"
				+ "  upgrade_range,device_count,upgrade_reason,upgrade_method,upgrade_start_time,upgrade_end_time,contact_way,upgrade_file_name");
		psql.append(" from tab_soft_upgrade_record where 1=1");
		if ((!StringUtil.IsEmpty(vendor)) && (!"-1".equals(vendor)))
		{
			psql.append("   and vendor_id = '");
			psql.append(vendor);
			psql.append("' ");
		}
		if ((!StringUtil.IsEmpty(device_model)) && (!"-1".equals(device_model)))
		{
			psql.append("   and device_model_id = '");
			psql.append(device_model);
			psql.append("'");
		}
		if (!StringUtil.IsEmpty(starttime))
		{
			psql.append("   and upgrade_start_time >= ");
			psql.append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime))
		{
			psql.append("   and upgrade_start_time <= ");
			psql.append(endtime);
		}
		psql.append(" order by record_id desc");
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				// 终端厂家
				map.put("vendorName", DeviceTypeUtil.vendorMap.get(StringUtil
						.getStringValue(rs.getString("vendor_id"))));
				// 终端型号
				map.put("device_model", DeviceTypeUtil.deviceModelMap.get(StringUtil
						.getStringValue(rs.getString("device_model_id"))));
				// 现有版本
				map.put("currentSoftWareVersion", DeviceTypeUtil.softVersionMap
						.get(StringUtil.getStringValue(rs
								.getString("current_devicetype_id"))));
				// 目标版本
				map.put("targetSoftWareVersion",
						StringUtil.getStringValue(rs.getString("target_devicetype")));
				// 升级范围
				map.put("upgradeRange",
						StringUtil.getStringValue(rs.getString("upgrade_range")));
				// 终端数量
				map.put("deviceCount",
						StringUtil.getStringValue(rs.getString("device_count")));
				// 升级原因
				map.put("upgradeReason",
						StringUtil.getStringValue(rs.getString("upgrade_reason")));
				// 升级方式
				map.put("upgradeMethod",
						StringUtil.getStringValue(rs.getString("upgrade_method")));
				// 升级时间
				map.put("upgradeTime", timeChange(rs.getString("upgrade_start_time"))
						+ " - " + timeChange(rs.getString("upgrade_end_time")));
				// 终端厂家联系方式
				map.put("contactWay",
						StringUtil.getStringValue(rs.getString("contact_way")));
				// 附件
				map.put("file",
						StringUtil.getStringValue(rs.getString("upgrade_file_name")));
				// 软件升级记录Id
				// 附件
				map.put("recordId", StringUtil.getStringValue(rs.getString("record_id")));
				return map;
			}
		});
		return list;
	}

	/**
	 * 时间转换
	 * 
	 * @param time
	 *            时间
	 * @return 转换后的时间(YYYY-MM-DD)
	 */
	@SuppressWarnings("unused")
	private String timeChange(String time)
	{
		try
		{
			long timelong = Long.parseLong(time);
			DateTimeUtil begindateTimeUtil = new DateTimeUtil(timelong * 1000);
			return new DateTimeUtil(timelong * 1000).getDate();
		}
		catch (NumberFormatException e)
		{
			return "";
		}
		catch (Exception e)
		{
			return "";
		}
	}

	/**
	 * 软件升级记录页数查询
	 * 
	 * @param vendor
	 *            终端厂家Id
	 * @param device_model
	 *            终端型号Id
	 * @param starttime
	 *            升级开始起始时间
	 * @param endtime
	 *            升级开始截止时间
	 * @param curPage_splitPage
	 *            当前页
	 * @param num_splitPage
	 *            每页行数
	 * @return 页数
	 */
	public int getCountSoftUpgradRecordQuery(String vendor, String device_model,
			String starttime, String endtime, int curPage_splitPage, int num_splitPage)
	{
		logger.debug(
				"SoftUpgradRecordQueryDAO==>getCountSoftUpgradRecordQuery({},{},{},{},{})",
				new Object[] { vendor, device_model, starttime, endtime });
		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(1) from tab_soft_upgrade_record where 1=1");
		if ((!StringUtil.IsEmpty(vendor)) && (!"-1".equals(vendor)))
		{
			psql.append("   and vendor_id = '");
			psql.append(vendor);
			psql.append("' ");
		}
		if ((!StringUtil.IsEmpty(device_model)) && (!"-1".equals(device_model)))
		{
			psql.append("   and device_model_id = '");
			psql.append(device_model);
			psql.append("'");
		}
		if (!StringUtil.IsEmpty(starttime))
		{
			psql.append("   and upgrade_start_time >= ");
			psql.append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime))
		{
			psql.append("   and upgrade_start_time <= ");
			psql.append(endtime);
		}
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
	 * 删除所选记录
	 * 
	 * @param recordId
	 *            软件升级记录Id
	 * @return 删除结果
	 */
	public int deleRecordByRecordId(String recordId)
	{
		logger.debug("SoftUpgradRecordQueryDAO==>deleRecordByRecordId({})",
				new Object[] { recordId });
		PrepareSQL deleSql = new PrepareSQL();
		deleSql.append("delete from tab_soft_upgrade_record where record_id =");
		deleSql.append(recordId);
		return jt.update(deleSql.getSQL());
	}

	/**
	 * 根据recordId获取所选记录
	 * 
	 * @param recordId
	 *            软件升级记录Id
	 * @return 软件升级记录结果
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map findRecordByRecordId(String recordId)
	{
		logger.debug("SoftUpgradRecordQueryDAO==>findRecordByRecordId({})",
				new Object[] { recordId });

		PrepareSQL sql = new PrepareSQL();
		sql.append("select * from tab_soft_upgrade_record where record_id =");
		sql.append(recordId);
		Map<String, String> map = jt.queryForMap(sql.getSQL());
		if (map != null && !map.isEmpty())
		{
			map.put("vendorName", DeviceTypeUtil.vendorMap.get(StringUtil.getStringValue(
					map, "vendor_id")));
			map.put("device_model", DeviceTypeUtil.deviceModelMap.get(StringUtil
					.getStringValue(map, "device_model_id")));
			map.put("currentSoftWareVersion", DeviceTypeUtil.softVersionMap
					.get(StringUtil.getStringValue(map, "current_devicetype_id")));
			map.put("start_time",
					timeChange(StringUtil.getStringValue(map, "upgrade_start_time")));
			map.put("end_time",
					timeChange(StringUtil.getStringValue(map, "upgrade_end_time")));
		}
		return map;
	}

	/**
	 * 修改所选记录
	 * 
	 * @param recordId
	 *            软件升级记录Id
	 * @param targetVersion_add
	 *            目标版本
	 * @param upgradeRange_add
	 *            升级范围
	 * @param deviceCount_add
	 *            终端数量
	 * @param upgradeReason_add
	 *            升级原因
	 * @param upgradeMethod_add
	 *            升级方式
	 * @param starttime_add
	 *            升级开始时间
	 * @param endtime_add
	 *            升级结束时间
	 * @param contactWay_add
	 *            终端厂家联系方式
	 * @param file_name
	 *            附件
	 * @return 修改结果
	 */
	public String modifyByRecordId(String recordId, String targetVersion_add,
			String upgradeRange_add, String deviceCount_add, String upgradeReason_add,
			String upgradeMethod_add, String starttime_add, String endtime_add,
			String contactWay_add, String file_name)
	{
		logger.debug("SoftUpgradRecordQueryDAO==>modifyByRecordId({})",
				new Object[] { recordId });
		// 更新设备版本表
		String sql = "update tab_soft_upgrade_record set target_devicetype=?,upgrade_range=?, "
				+ "device_count = ?,upgrade_reason=?,upgrade_method=?,upgrade_start_time=?,upgrade_end_time=?,"
				+ "contact_way=?, upgrade_file_name=? where record_id=?";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setString(1, targetVersion_add);
		psql.setString(2, upgradeRange_add);
		psql.setLong(3, new Long(deviceCount_add));
		psql.setString(4, upgradeReason_add);
		psql.setString(5, upgradeMethod_add);
		psql.setLong(6, new Long(starttime_add));
		psql.setLong(7, new Long(endtime_add));
		psql.setString(8, contactWay_add);
		psql.setString(9, file_name);
		psql.setLong(10, new Long(recordId));
		return StringUtil.getStringValue(jt.update(psql.getSQL()));
	}

	/**
	 * 新增软件升级记录
	 * 
	 * @param vendor_add
	 *            终端厂家
	 * @param device_model_add
	 *            终端型号
	 * @param currentVersion_add
	 *            现有版本
	 * @param targetVersion_add
	 *            目标版本
	 * @param upgradeRange_add
	 *            升级范围
	 * @param deviceCount_add
	 *            终端数量
	 * @param upgradeReason_add
	 *            升级原因
	 * @param upgradeMethod_add
	 *            升级方式
	 * @param starttime_add
	 *            升级开始时间
	 * @param endtime_add
	 *            升级结束时间
	 * @param contactWay_add
	 *            终端厂家联系方式
	 * @param file_name
	 *            附件
	 * @return 新增结果
	 */
	public String addSoftUpgradRecord(String vendor_add, String device_model_add,
			String currentVersion_add, String targetVersion_add, String upgradeRange_add,
			String deviceCount_add, String upgradeReason_add, String upgradeMethod_add,
			String starttime_add, String endtime_add, String contactWay_add,
			String file_name)
	{
		logger.debug("SoftUpgradRecordQueryDAO==>addSoftUpgradRecord({},{},{},{},{},{},{},{},{},{}，{},{})",
				new Object[] { vendor_add, device_model_add, currentVersion_add,
						targetVersion_add, upgradeRange_add, deviceCount_add,
						upgradeReason_add, upgradeMethod_add, starttime_add, endtime_add,
						contactWay_add, file_name });
		// 获取最大id
		long max_id = DataSetBean.getMaxId("tab_soft_upgrade_record", "record_id");
		StringBuffer sql = new StringBuffer();
		sql.append("insert into tab_soft_upgrade_record(record_id, vendor_id,device_model_id,current_devicetype_id,"
				+ "target_devicetype,upgrade_range,upgrade_method,upgrade_start_time,upgrade_end_time");
		if (!StringUtil.IsEmpty(deviceCount_add))
		{
			sql.append(" ,device_count");
		}
		if (!StringUtil.IsEmpty(upgradeReason_add))
		{
			sql.append(" ,upgrade_reason");
		}
		if (!StringUtil.IsEmpty(contactWay_add))
		{
			sql.append(" ,contact_way");
		}
		if (!StringUtil.IsEmpty(file_name))
		{
			sql.append(" ,upgrade_file_name");
		}
		sql.append(" ) values(").append(max_id).append(",'").append(vendor_add)
				.append("','").append(device_model_add).append("',")
				.append(currentVersion_add).append(",'").append(targetVersion_add)
				.append("','").append(upgradeRange_add).append("','")
				.append(upgradeMethod_add).append("',").append(starttime_add).append(",")
				.append(endtime_add);
		if (!StringUtil.IsEmpty(deviceCount_add))
		{
			sql.append(" ,").append(deviceCount_add);
		}
		if (!StringUtil.IsEmpty(upgradeReason_add))
		{
			sql.append(" ,'").append(upgradeReason_add).append("' ");
		}
		if (!StringUtil.IsEmpty(contactWay_add))
		{
			sql.append(" ,'").append(contactWay_add).append("' ");
		}
		if (!StringUtil.IsEmpty(file_name))
		{
			sql.append(" ,'").append(file_name).append("' ");
		}
		sql.append(")");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return StringUtil.getStringValue(jt.update(psql.getSQL()));
	}

	/**
	 * 插入公告
	 * @param title  标题
	 * @param content 内容
	 * @return
	 */
	public String insertBroadInfo(String title, String content)
	{
		logger.debug("SoftUpgradRecordQueryDAO==>insertBroadInfo({},{})", new Object[] {
				title, content });
		// 获取最大id
		long max_id = DataSetBean.getMaxId("tab_broad_info", "id");
		// 获取当前时间
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = fmt.format(new Date());
		// 获取属地和操作员
		UserRes curUser = WebUtil.getCurrentUser();
		String city_id = curUser.getCityId();
		long acc_oid = curUser.getUser().getId();
		String sql = "";
		if (1 != DBUtil.getDbType())
		{
			sql = "insert into tab_broad_info(id, title,content,titletype,broad_time,city_id,acc_oid)"
					+ "values("
					+ max_id
					+ ",?,?,1,to_date('"
					+ dateString
					+ "','yyyy-mm-dd')" + ",?,?)";
		}
		else
		{
			sql = "insert into tab_broad_info(id, title,content,titletype,broad_time,city_id,acc_oid)"
					+ "values("
					+ max_id
					+ ",?,?,1,convert(datetime,'"
					+ dateString
					+ "')" + ",?,?)";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setString(1, title);
		psql.setString(2, content);
		psql.setString(3, city_id);
		psql.setString(4, StringUtil.getStringValue(acc_oid));
		return StringUtil.getStringValue(jt.update(psql.getSQL()));
	}
}
