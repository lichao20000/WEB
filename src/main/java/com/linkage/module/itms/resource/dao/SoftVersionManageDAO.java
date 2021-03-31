package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.gw.VendorModelVersionDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

public class SoftVersionManageDAO extends SuperDAO {

	// 记录日志
	private static Logger logger = LoggerFactory
			.getLogger(SoftVersionManageDAO.class);

	private HashMap<String, String> vendorMap = null;
	private HashMap<String, String> deviceModelMap = null;
	private HashMap<String, String> devicetypeMap = null;
	private Map<String, String>  cityMap = null;
	private HashMap<String, String> tempMap = null;

	private Map<String, String> accMap = null;

	/**
	 * 查询列表
	 * 
	 * @param vendor
	 * @param device_model
	 * @param devicetypeId
	 * @return
	 */
	public List<Map> getVersionList(String cityId,String vendor,
			String device_model, String devicetypeId, String tempId,
			String hardwareversion,int curPage_splitPage, int num_splitPage) {

		logger.debug("SoftVersionManageDAO==>getVersionList({},{},{},{},{})",
				new Object[] { vendor, device_model, devicetypeId, tempId,
						hardwareversion });

		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.devicetype_id, a.vendor_id, a.device_model_id, a.softwareversion,a.hardwareversion, b.devicetype_id as devicetype_id_new,  b.devicetype_id_old, b.temp_id");
		if (Global.SDLT.equals(Global.instAreaShortName)||Global.SDDX.equals(Global.instAreaShortName)) {
			psql.append(" ,b.city_id ");
		}
		psql.append("  from tab_devicetype_info a, gw_soft_upgrade_temp_map b ");
		psql.append(" where 1=1 ");
		psql.append("   and a.devicetype_id = b.devicetype_id_old ");

		if (!StringUtil.IsEmpty(vendor) && !"-1".equals(vendor)) {
			psql.append("   and a.vendor_id = '");
			psql.append(vendor);
			psql.append("' ");
		}

		if (!StringUtil.IsEmpty(device_model) && !"-1".equals(device_model)) {
			psql.append("   and a.device_model_id = '");
			psql.append(device_model);
			psql.append("'");
		}

		if (!StringUtil.IsEmpty(devicetypeId) && !"-1".equals(devicetypeId)) {
			psql.append("   and a.devicetype_id = ");
			psql.append(devicetypeId);
		}

		if (!StringUtil.IsEmpty(tempId) && !"-1".equals(tempId)) {
			psql.append("   and b.temp_id = ");
			psql.append(tempId);
		}
		
		if (Global.SDLT.equals(Global.instAreaShortName)||Global.SDDX.equals(Global.instAreaShortName)) {
			if (!StringUtil.IsEmpty(cityId) && !"-1".equals(cityId)) {
				psql.append("   and (b.city_id like '%"+cityId+"%' or b.city_id is null) ");
			}
		}

		if (!StringUtil.IsEmpty(hardwareversion)
				&& !"-1".equals(hardwareversion)) {
			psql.append("   and a.hardwareversion = '");
			psql.append(hardwareversion);
			psql.append("'");
		}
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		devicetypeMap = VendorModelVersionDAO.getDevicetypeMap();
		tempMap = getTempMap();

		@SuppressWarnings("unchecked")
		List<Map> list = querySP(psql.getSQL(),
				(curPage_splitPage - 1) * num_splitPage + 1, num_splitPage,
				new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {

						Map<String, String> map = new HashMap<String, String>();

						/** 原始版本ID */
						map.put("devicetype_id_old", StringUtil
								.getStringValue(rs
										.getString("devicetype_id_old")));
						/** 目标版本ID */
						map.put("devicetype_id_new", StringUtil
								.getStringValue(rs
										.getString("devicetype_id_new")));

						/** 获取设备厂商名称 */
						String vendor_add = StringUtil.getStringValue(vendorMap
								.get(StringUtil.getStringValue(rs
										.getString("vendor_id"))));
						if (false == StringUtil.IsEmpty(vendor_add)) {
							map.put("vendorName", vendor_add);
						} else {
							map.put("vendorName", "");
						}

						/** 获取设备型号 */
						String device_model = StringUtil.getStringValue(deviceModelMap
								.get(StringUtil.getStringValue(rs
										.getString("device_model_id"))));
						if (false == StringUtil.IsEmpty(device_model)) {
							map.put("device_model", device_model);
						} else {
							map.put("device_model", "");
						}
						/** 获取硬件版本 */
						String hardwareversion = StringUtil.getStringValue(rs
								.getString("hardwareversion"));
						if (false == StringUtil.IsEmpty(hardwareversion)) {
							map.put("hardwareversion", hardwareversion);
						} else {
							map.put("hardwareversion", "");
						}
						/** 获取原软件版本 */
						String origSoftWareVersion = StringUtil.getStringValue(devicetypeMap
								.get(StringUtil.getStringValue(rs
										.getString("devicetype_id_old"))));
						if (false == StringUtil.IsEmpty(origSoftWareVersion)) {
							map.put("origSoftWareVersion", origSoftWareVersion);
						} else {
							map.put("origSoftWareVersion", "");
						}

						/** 获取目标软件版本 */
						String targetSoftWareVersion = StringUtil.getStringValue(devicetypeMap
								.get(StringUtil.getStringValue(rs
										.getString("devicetype_id_new"))));
						if (false == StringUtil.IsEmpty(targetSoftWareVersion)) {
							map.put("targetSoftWareVersion",
									targetSoftWareVersion);
						} else {
							map.put("targetSoftWareVersion", "");
						}
						/** 软件升级方式 */
						String tempName = StringUtil.getStringValue(tempMap
								.get(StringUtil.getStringValue(rs
										.getString("temp_id"))));
						if (StringUtil.IsEmpty(tempName)) {
							map.put("tempName", "");
							map.put("tempId", "");
						} else {
							map.put("tempName", tempName);
							map.put("tempId", StringUtil.getStringValue(rs
									.getString("temp_id")));
						}
						// 获取地市信息
						if (Global.SDLT.equals(Global.instAreaShortName)||Global.SDDX.equals(Global.instAreaShortName)) {
							cityMap = CityDAO.getCityIdCityNameMap();
							String city_id = StringUtil.getStringValue(rs.getString("city_id"));
							map.put("city_id", city_id);
							String city_name ="";
							String[] array=city_id.split(",");
							for (int i = 0; i < array.length; i++) {
								if (i==array.length-1) {
									city_name+=StringUtil.getStringValue(cityMap.get(array[i]));
								}else {
									city_name+=StringUtil.getStringValue(cityMap.get(array[i]))+",";
								}
							}
							city_name=city_name.replaceAll("本地网","").replaceAll("分公司", "");
							if(!StringUtil.IsEmpty(city_name))
							{
								map.put("city_name", city_name);
							}
							else
							{
								map.put("city_name", "全省");
							}
						}
						
						return map;

					}
				});

		/** 清楚缓存 */
		vendorMap = null;
		deviceModelMap = null;
		devicetypeMap = null;
		Collections.reverse(list);
		return list;
	}

	public List<Map> parseExcel(String vendor,
			String device_model, String devicetypeId, String tempId,
			String hardwareversion) {
		
		logger.debug("SoftVersionManageDAO==>parseExcel({},{},{},{},{})",
				new Object[] { vendor, device_model, devicetypeId, tempId,
						hardwareversion });

		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.devicetype_id, a.vendor_id, a.device_model_id, a.softwareversion,a.hardwareversion, b.devicetype_id as devicetype_id_new,  b.devicetype_id_old, b.temp_id");
		psql.append("  from tab_devicetype_info a, gw_soft_upgrade_temp_map b ");
		psql.append(" where 1=1 ");
		psql.append("   and a.devicetype_id = b.devicetype_id_old ");

		if (!StringUtil.IsEmpty(vendor) && !"-1".equals(vendor)) {
			psql.append("   and a.vendor_id = '");
			psql.append(vendor);
			psql.append("' ");
		}

		if (!StringUtil.IsEmpty(device_model) && !"-1".equals(device_model)) {
			psql.append("   and a.device_model_id = '");
			psql.append(device_model);
			psql.append("'");
		}

		if (!StringUtil.IsEmpty(devicetypeId) && !"-1".equals(devicetypeId)) {
			psql.append("   and a.devicetype_id = ");
			psql.append(devicetypeId);
		}

		if (!StringUtil.IsEmpty(tempId) && !"-1".equals(tempId)) {
			psql.append("   and b.temp_id = ");
			psql.append(tempId);
		}

		if (!StringUtil.IsEmpty(hardwareversion)
				&& !"-1".equals(hardwareversion)) {
			psql.append("   and a.hardwareversion = '");
			psql.append(hardwareversion);
			psql.append("'");
		}
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		devicetypeMap = VendorModelVersionDAO.getDevicetypeMap();
		tempMap = getTempMap();

		@SuppressWarnings("unchecked")
		List<Map> list = jt.query(psql.getSQL(), new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {

						Map<String, String> map = new HashMap<String, String>();

						/** 原始版本ID */
						map.put("devicetype_id_old", StringUtil
								.getStringValue(rs
										.getString("devicetype_id_old")));
						/** 目标版本ID */
						map.put("devicetype_id_new", StringUtil
								.getStringValue(rs
										.getString("devicetype_id_new")));

						/** 获取设备厂商名称 */
						String vendor_add = StringUtil.getStringValue(vendorMap
								.get(StringUtil.getStringValue(rs
										.getString("vendor_id"))));
						if (false == StringUtil.IsEmpty(vendor_add)) {
							map.put("vendorName", vendor_add);
						} else {
							map.put("vendorName", "");
						}

						/** 获取设备型号 */
						String device_model = StringUtil.getStringValue(deviceModelMap
								.get(StringUtil.getStringValue(rs
										.getString("device_model_id"))));
						if (false == StringUtil.IsEmpty(device_model)) {
							map.put("device_model", device_model);
						} else {
							map.put("device_model", "");
						}
						/** 获取硬件版本 */
						String hardwareversion = StringUtil.getStringValue(rs
								.getString("hardwareversion"));
						if (false == StringUtil.IsEmpty(hardwareversion)) {
							map.put("hardwareversion", hardwareversion);
						} else {
							map.put("hardwareversion", "");
						}
						/** 获取原软件版本 */
						String origSoftWareVersion = StringUtil.getStringValue(devicetypeMap
								.get(StringUtil.getStringValue(rs
										.getString("devicetype_id_old"))));
						if (false == StringUtil.IsEmpty(origSoftWareVersion)) {
							map.put("origSoftWareVersion", origSoftWareVersion);
						} else {
							map.put("origSoftWareVersion", "");
						}

						/** 获取目标软件版本 */
						String targetSoftWareVersion = StringUtil.getStringValue(devicetypeMap
								.get(StringUtil.getStringValue(rs
										.getString("devicetype_id_new"))));
						if (false == StringUtil.IsEmpty(targetSoftWareVersion)) {
							map.put("targetSoftWareVersion",
									targetSoftWareVersion);
						} else {
							map.put("targetSoftWareVersion", "");
						}
						/** 软件升级方式 */
						String tempName = StringUtil.getStringValue(tempMap
								.get(StringUtil.getStringValue(rs
										.getString("temp_id"))));
						if (StringUtil.IsEmpty(tempName)) {
							map.put("tempName", "");
							map.put("tempId", "");
						} else {
							map.put("tempName", tempName);
							map.put("tempId", StringUtil.getStringValue(rs
									.getString("temp_id")));
						}

						return map;

					}
				});

		/** 清楚缓存 */
		vendorMap = null;
		deviceModelMap = null;
		devicetypeMap = null;
		Collections.reverse(list);
		return list;
	}
	
	/**
	 * 查询自动软件升级操作日志列表
	 * 
	 * @param vendor
	 * @param device_model
	 * @param devicetypeId
	 * @return
	 */
	public List<Map> getVersionOperateLogList(String vendor,
			String device_model, String devicetypeId, String tempId,
			String hardwareversion,int curPage_splitPage, int num_splitPage) {

		logger.debug("SoftVersionManageDAO==>getVersionList({},{},{},{},{})",
				new Object[] { vendor, device_model, devicetypeId, tempId,
						hardwareversion });

		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.devicetype_id, a.vendor_id, a.device_model_id, a.softwareversion,a.hardwareversion, b.devicetype_id as devicetype_id_new,  b.devicetype_id_old, b.temp_id");
		psql.append(" , b.acc_oid ,b.operate_time,b.operate_type ");
		psql.append("  from tab_devicetype_info a, gw_soft_upgrade_temp_map_log b ");
		psql.append(" where 1=1 ");
		psql.append("   and a.devicetype_id = b.devicetype_id_old ");

		if (!StringUtil.IsEmpty(vendor) && !"-1".equals(vendor)) {
			psql.append("   and a.vendor_id = '");
			psql.append(vendor);
			psql.append("' ");
		}

		if (!StringUtil.IsEmpty(device_model) && !"-1".equals(device_model)) {
			psql.append("   and a.device_model_id = '");
			psql.append(device_model);
			psql.append("'");
		}

		if (!StringUtil.IsEmpty(devicetypeId) && !"-1".equals(devicetypeId)) {
			psql.append("   and a.devicetype_id = ");
			psql.append(devicetypeId);
		}

		if (!StringUtil.IsEmpty(tempId) && !"-1".equals(tempId)) {
			psql.append("   and b.temp_id = ");
			psql.append(tempId);
		}

		if (!StringUtil.IsEmpty(hardwareversion)
				&& !"-1".equals(hardwareversion)) {
			psql.append("   and a.hardwareversion = '");
			psql.append(hardwareversion);
			psql.append("'");
		}
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		devicetypeMap = VendorModelVersionDAO.getDevicetypeMap();
		tempMap = getTempMap();

		accMap = getAccMap();

		@SuppressWarnings("unchecked")
		List<Map> list = querySP(psql.getSQL(),
				(curPage_splitPage - 1) * num_splitPage + 1, num_splitPage,
				new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {

						Map<String, String> map = new HashMap<String, String>();

						/** 原始版本ID */
						map.put("devicetype_id_old", StringUtil
								.getStringValue(rs
										.getString("devicetype_id_old")));

						/** 获取设备厂商名称 */
						String vendor_add = StringUtil.getStringValue(vendorMap
								.get(StringUtil.getStringValue(rs
										.getString("vendor_id"))));
						if (false == StringUtil.IsEmpty(vendor_add)) {
							map.put("vendorName", vendor_add);
						} else {
							map.put("vendorName", "");
						}

						/** 获取设备型号 */
						String device_model = StringUtil.getStringValue(deviceModelMap
								.get(StringUtil.getStringValue(rs
										.getString("device_model_id"))));
						if (false == StringUtil.IsEmpty(device_model)) {
							map.put("device_model", device_model);
						} else {
							map.put("device_model", "");
						}
						/** 获取硬件版本 */
						String hardwareversion = StringUtil.getStringValue(rs
								.getString("hardwareversion"));
						if (false == StringUtil.IsEmpty(hardwareversion)) {
							map.put("hardwareversion", hardwareversion);
						} else {
							map.put("hardwareversion", "");
						}
						/** 获取原软件版本 */
						String origSoftWareVersion = StringUtil.getStringValue(devicetypeMap
								.get(StringUtil.getStringValue(rs
										.getString("devicetype_id_old"))));
						if (false == StringUtil.IsEmpty(origSoftWareVersion)) {
							map.put("origSoftWareVersion", origSoftWareVersion);
						} else {
							map.put("origSoftWareVersion", "");
						}

						/** 获取目标软件版本 */
						String targetSoftWareVersion = StringUtil.getStringValue(devicetypeMap
								.get(StringUtil.getStringValue(rs
										.getString("devicetype_id_new"))));
						if (false == StringUtil.IsEmpty(targetSoftWareVersion)) {
							map.put("targetSoftWareVersion",
									targetSoftWareVersion);
						} else {
							map.put("targetSoftWareVersion", "");
						}
						/** 软件升级方式 */
						String tempName = StringUtil.getStringValue(tempMap
								.get(StringUtil.getStringValue(rs
										.getString("temp_id"))));
						if (StringUtil.IsEmpty(tempName)) {
							map.put("tempName", "");
							map.put("tempId", "");
						} else {
							map.put("tempName", tempName);
							map.put("tempId", StringUtil.getStringValue(rs
									.getString("temp_id")));
						}

						String operateTime = StringUtil.getStringValue(rs
								.getString("operate_time"));
						try {
							long opertime = StringUtil
									.getLongValue(operateTime) * 1000L;
							DateTimeUtil dt = new DateTimeUtil(opertime);
							map.put("operateTime", dt.getLongDate());
						} catch (NumberFormatException e) {
							map.put("operateTime", "");
						} catch (Exception e) {
							map.put("operateTime", "");
						}

						String acc_oid = StringUtil.getStringValue(rs
								.getString("acc_oid"));
						if (StringUtil.IsEmpty(acc_oid)) {
							map.put("loginname", "");
						} else {
							map.put("loginname", accMap.get(acc_oid));
						}

						String ot = StringUtil.getStringValue(rs
								.getString("operate_type"));

						if ("1".equals(ot)) {
							map.put("operate_type", "增加");
						} else {
							map.put("operate_type", "删除");
						}
						return map;

					}
				});

		/** 清楚缓存 */
		vendorMap = null;
		deviceModelMap = null;
		devicetypeMap = null;

		return list;
	}
	/**
	 * 查询自动软件升级操作日志列表
	 * 
	 * @param vendor
	 * @param device_model
	 * @param devicetypeId
	 * @return
	 */
	public int getVersionOperateLogMaxPage(String vendor,
			String device_model, String devicetypeId, String tempId,
			String hardwareversion,int curPage_splitPage, int num_splitPage) {

		logger.debug("SoftVersionManageDAO==>getVersionOperateLogMaxPage({},{},{},{},{})",
				new Object[] { vendor, device_model, devicetypeId, tempId,
						hardwareversion });

		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(1)");
		psql.append("  from tab_devicetype_info a, gw_soft_upgrade_temp_map_log b ");
		psql.append(" where 1=1 ");
		psql.append("   and a.devicetype_id = b.devicetype_id_old ");

		if (!StringUtil.IsEmpty(vendor) && !"-1".equals(vendor)) {
			psql.append("   and a.vendor_id = '");
			psql.append(vendor);
			psql.append("' ");
		}

		if (!StringUtil.IsEmpty(device_model) && !"-1".equals(device_model)) {
			psql.append("   and a.device_model_id = '");
			psql.append(device_model);
			psql.append("'");
		}

		if (!StringUtil.IsEmpty(devicetypeId) && !"-1".equals(devicetypeId)) {
			psql.append("   and a.devicetype_id = ");
			psql.append(devicetypeId);
		}

		if (!StringUtil.IsEmpty(tempId) && !"-1".equals(tempId)) {
			psql.append("   and b.temp_id = ");
			psql.append(tempId);
		}

		if (!StringUtil.IsEmpty(hardwareversion)
				&& !"-1".equals(hardwareversion)) {
			psql.append("   and a.hardwareversion = '");
			psql.append(hardwareversion);
			psql.append("'");
		}
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	/**
	 * 新增
	 * 
	 * 在新增之前，删除与原软件版本相关的记录
	 * 
	 * @param vendor_add
	 * @param device_model_add
	 * @param devicetypeIdOld
	 * @param devicetypeIdNew
	 * @return
	 */
	public String addInfo(String devicetypeIdOld, String devicetypeIdNew,
			String tempId, long acc_oid,String cityId_add) {

		logger.debug("SoftVersionManageDAO==>addInfo({},{},{})", new Object[] {
				devicetypeIdOld, devicetypeIdNew, tempId });

		if (StringUtil.IsEmpty(devicetypeIdOld)) {
			return "0;新增失败！您未选择原软件版本！";
		}
		String temp_id = "";
//		if ("js_dx".equals(Global.instAreaShortName)) {
			temp_id = tempId;
//		} else {
//			temp_id = "1";
//		}
		long operateTime = new DateTimeUtil().getLongTime();

		String[] deviceTypeOldIdArray = devicetypeIdOld.split(";");
		StringBuffer sqlBuffer = new StringBuffer();

		PrepareSQL deleSql = new PrepareSQL();

		deleSql.append("delete from gw_soft_upgrade_temp_map where temp_id= ");
		deleSql.append(temp_id);
		deleSql.append(" and devicetype_id_old in(");
		deleSql.append(devicetypeIdOld.replaceAll(";", ","));
		deleSql.append(")");

		sqlBuffer.append(deleSql.getSQL()).append(";");

		for (int i = 0; i < deviceTypeOldIdArray.length; i++) {

			PrepareSQL insertSql = new PrepareSQL();

			PrepareSQL logSql = new PrepareSQL();

			insertSql.append("insert into gw_soft_upgrade_temp_map values (");
			insertSql.append(temp_id);
			insertSql.append(",");
			insertSql.append(deviceTypeOldIdArray[i]);
			insertSql.append(",");
			insertSql.append(devicetypeIdNew);
			if (Global.SDLT.equals(Global.instAreaShortName)||Global.SDDX.equals(Global.instAreaShortName)) {
				insertSql.append(",'"+cityId_add+"'");
			}
			insertSql.append(")");

			sqlBuffer.append(insertSql.getSQL()).append(";");

			logSql.append("insert into gw_soft_upgrade_temp_map_log values(");
			logSql.append(temp_id);
			logSql.append(",");
			logSql.append(deviceTypeOldIdArray[i]);
			logSql.append(",");
			logSql.append(devicetypeIdNew);
			logSql.append("," + operateTime);
			logSql.append("," + acc_oid);
			logSql.append("," + 1 + "");
			if (Global.SDLT.equals(Global.instAreaShortName)||Global.SDDX.equals(Global.instAreaShortName)) {
				logSql.append(",'"+cityId_add+"'");
			}
			logSql.append(" )");
			sqlBuffer.append(logSql.getSQL()).append(";");
		}

		/** SQL语句批处理 */
		int[] iCodes = DataSetBean.doBatch(sqlBuffer.toString());

		deleSql = null;
		sqlBuffer = null;

		// 返回操作的记录条数
		if (iCodes != null && iCodes.length > 0) {
			return "1;新增成功！";
		} else {
			return "0;新增失败！";
		}

	}

	/**
	 * 删除
	 * 
	 * @param devicetypeIdOld
	 * @return
	 */
	public String deleInfo(String devicetypeIdOld,String devicetypeIdNew, String tempId, long acc_oid,String cityId) {

		logger.debug("SoftVersionManageDAO==>deleInfo({},{})", new Object[] {
				devicetypeIdOld, tempId });

		StringBuffer sqlBuffer = new StringBuffer();

		PrepareSQL deleSql = new PrepareSQL();
		deleSql.append("delete from gw_soft_upgrade_temp_map where temp_id =");
		deleSql.append(tempId);
		deleSql.append(" and devicetype_id_old = ");
		deleSql.append(devicetypeIdOld);

		sqlBuffer.append(deleSql.getSQL()).append(";");

		PrepareSQL logSql = new PrepareSQL();
		logSql.append("insert into gw_soft_upgrade_temp_map_log values(");
		logSql.append(tempId);
		logSql.append(",");
		logSql.append(devicetypeIdOld);
		logSql.append(",");
		logSql.append(devicetypeIdNew);
		logSql.append("," + new DateTimeUtil().getLongTime());
		logSql.append("," + acc_oid);
		logSql.append("," + 0 + "");
		if (Global.SDLT.equals(Global.instAreaShortName)||Global.SDDX.equals(Global.instAreaShortName)) {
			logSql.append(",'"+cityId+"'");
		}
		logSql.append(" )");

		sqlBuffer.append(logSql.getSQL()).append(";");

		int[] iCodes = DataSetBean.doBatch(sqlBuffer.toString());
		// 返回操作的记录条数
		if (iCodes != null && iCodes.length > 0) {
			return "1;删除成功！";
		} else {
			return "0;删除失败！";
		}
	}

	/**
	 * 获取原软件版本的复选框 和 目标软件版本的下拉框
	 * 
	 * @param vendor_add
	 * @param device_model_add
	 * @return
	 */
	public String getVersionCheckList(String vendor_add, String device_model_add) {

		logger.debug("SoftVersionManageDAO==>getVersionCheckList({},{})",
				new Object[] { vendor_add, device_model_add });

		List<Map> list = getSoftVersion(vendor_add, device_model_add);

		StringBuffer sb = new StringBuffer();

		/** 获取原软件版本的复选框 */
		sb.append(getHTMLCheckList(list, "devicetypeIdOld", "devicetype_id",
				"softwareversion", "hardwareversion"));
		sb.append(";");
		/** 获取目标软件版本的下拉框 */
		sb.append(getHTMLSelectList(list, "devicetype_id", "softwareversion","hardwareversion",""));

		list = null;

		return sb.toString();
	}
	
	public String getVersion(String vendor_add, String device_model_add,String devicetypeIdNew) {

		logger.debug("SoftVersionManageDAO==>getVersion({},{})",
				new Object[] { vendor_add, device_model_add ,devicetypeIdNew});

		List<Map> list = getSoftVersionExTarget(vendor_add, device_model_add,devicetypeIdNew);

		StringBuffer sb = new StringBuffer();

		/** 获取原软件版本的复选框 */
		sb.append(getHTMLCheckList(list, "devicetypeIdOld", "devicetype_id",
				"softwareversion", "hardwareversion"));
		//sb.append(";");
		/** 获取目标软件版本的下拉框 */
		/*sb.append(getHTMLSelectList(list, "devicetype_id", "softwareversion",
				""));*/

		list = null;

		return sb.toString();
	}
	

	/**
	 * 获取原软件版本的复选框
	 * 
	 * @param cursor
	 * @param name
	 * @param value
	 * @param showName
	 *            软件版本
	 * @param showName2
	 *            硬件版本
	 * @return
	 */
	public String getHTMLCheckList(List<Map> list, String name, String value,
			String showName, String showName2) {

		logger.debug("SoftVersionManageDAO==>getHTMLCheckList({},{},{},{})",
				new Object[] { list, name, value, showName });

		StringBuffer bufferHTML = new StringBuffer();

		if (null == list || list.size() <= 0) {
			bufferHTML.append("没有符合条件的记录");
		} else {
			for (int i = 0; i < list.size(); i++) {
				bufferHTML.append("<input type='checkbox' id='" + name
						+ "' name='" + name + "' value='");
				// bufferHTML.append(StringUtil.getStringValue(list.get(i).get(value))
				// + "'>" + StringUtil.getStringValue(list.get(i).get(showName))
				// + "<br>");
				bufferHTML.append(StringUtil.getStringValue(list.get(i).get(
						value))
						+ "'>"
						+ StringUtil.getStringValue(list.get(i).get(showName2))
						+ "("
						+ StringUtil.getStringValue(list.get(i).get(showName))
						+ ")");
			}
		}
		return bufferHTML.toString();
	}

	/**
	 * 获取目标软件版本的下拉框
	 * 
	 * @param cursor
	 * @param name 硬件版本
	 * @param value
	 * @param showName
	 * @return
	 */
	public String getHTMLSelectList(List<Map> list, String value,
			String showName,String name, String compare) {

		logger.debug("SoftVersionManageDAO==>getHTMLSelectList({},{},{},{})",
				new Object[] { list, value, showName, compare });

		StringBuffer bufferHTML = new StringBuffer();

		if (StringUtil.IsEmpty(compare)) {
			compare = "-1";
		}
		if (null == list || list.size() <= 0) {
			bufferHTML.append("<OPTION VALUE = -1>--此项没有记录--</OPTION>");
		} else {
			bufferHTML.append("<OPTION VALUE = -1>--请选择--</OPTION>");
			for (int i = 0; i < list.size(); i++) {

				bufferHTML.append("<OPTION VALUE = '").append(
						StringUtil.getStringValue(list.get(i).get(value)));

				if (compare.equals(StringUtil.getStringValue(list.get(i).get(
						value)))) {
					bufferHTML.append("' selected>--");
				} else {
					bufferHTML.append("'>--");
				}
				bufferHTML.append(StringUtil.getStringValue(list.get(i).get(name))).append("(");
				bufferHTML.append(StringUtil.getStringValue(list.get(i).get(showName))).append(")--</OPTION>");
			}
		}
		return bufferHTML.toString();
	}

	/**
	 * 获取软件版本的List
	 * 
	 * @param vendor_add
	 * @param device_model_add
	 * @return
	 */
	public List<Map> getSoftVersion(String vendor_add, String device_model_add) {

		logger.debug("SoftVersionManageDAO==>getSoftVersionList({},{})",
				new Object[] { vendor_add, device_model_add });

		PrepareSQL psql = new PrepareSQL();
		psql.append("select distinct a.devicetype_id, a.softwareversion,a.hardwareversion");
		psql.append("  from tab_devicetype_info a, gw_device_model b,tab_vendor c ");
		psql.append(" where 1=1 ");
		psql.append("   and a.device_model_id = b.device_model_id ");
		psql.append("   and a.vendor_id = c.vendor_id ");

		if (false == StringUtil.IsEmpty(vendor_add) && !"-1".equals(vendor_add)) {
			psql.append("   and c.vendor_id = '");
			psql.append(vendor_add);
			psql.append("'");
		}

		if (false == StringUtil.IsEmpty(device_model_add)
				&& !"-1".equals(device_model_add)) {
			psql.append("   and b.device_model_id = '");
			psql.append(device_model_add);
			psql.append("'");
		}

		return jt.queryForList(psql.getSQL());
	}
	
	
	public List<Map> getSoftVersionExTarget(String vendor_add, String device_model_add,String devicetypeIdNew) {

		logger.debug("SoftVersionManageDAO==>getSoftVersionExTarget({},{})",
				new Object[] { vendor_add, device_model_add ,devicetypeIdNew});

		PrepareSQL psql = new PrepareSQL();
		psql.append("select distinct a.devicetype_id, a.softwareversion,a.hardwareversion");
		psql.append("  from tab_devicetype_info a, gw_device_model b,tab_vendor c ");
		psql.append(" where 1=1 ");
		psql.append("   and a.device_model_id = b.device_model_id ");
		psql.append("   and a.vendor_id = c.vendor_id ");

		if (false == StringUtil.IsEmpty(vendor_add) && !"-1".equals(vendor_add)) {
			psql.append("   and c.vendor_id = '");
			psql.append(vendor_add);
			psql.append("'");
		}

		if (false == StringUtil.IsEmpty(device_model_add)
				&& !"-1".equals(device_model_add)) {
			psql.append("   and b.device_model_id = '");
			psql.append(device_model_add);
			psql.append("'");
		}
		
		if (false == StringUtil.IsEmpty(devicetypeIdNew) && !"-1".equals(devicetypeIdNew)) {
			psql.append(" and a.devicetype_id not in (select devicetype_id from tab_devicetype_info where softwareversion in (select softwareversion" +
					" from tab_devicetype_info where devicetype_id=");
			psql.append(devicetypeIdNew);
			psql.append("))");
		}
		
		return jt.queryForList(psql.getSQL());
	}
	

	/**
	 * 软件升级类型
	 * 
	 * @return
	 */
	public HashMap<String, String> getTempMap() {
		tempMap = new HashMap<String, String>();
		tempMap.put("1", "普通软件升级");
		tempMap.put("2", "业务相关软件升级");
		tempMap.put("3", "非业务软件升级");
		return tempMap;
	}

	/**
	 * 管理yuan id name 对应
	 * 
	 * @return
	 */
	public Map<String, String> getAccMap() {
		logger.debug("getAccMap()");
		HashMap<String, String> accountMap = new HashMap<String, String>();
		accountMap.clear();
		Cursor cursor = DataSetBean
				.getCursor("select acc_oid,acc_loginname from tab_accounts");
		Map fields = cursor.getNext();
		if (fields == null) {
			logger.debug("管理员表没数据");
		} else {
			while (fields != null) {
				accountMap.put((String) fields.get("acc_oid"),
						(String) fields.get("acc_loginname"));
				fields = cursor.getNext();
			}
		}
		return accountMap;
	}

	public int getMaxPage(String cityId,String vendor, String device_model,
			String devicetypeId, String tempId, String hardwareversion,
			int curPage_splitPage, int num_splitPage) {
		// TODO Auto-generated method stub
		logger.debug("SoftVersionManageDAO==>getVersionList({},{},{},{},{})",
				new Object[] { vendor, device_model, devicetypeId, tempId,
						hardwareversion });

		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(1) ");
		psql.append("  from tab_devicetype_info a, gw_soft_upgrade_temp_map b ");
		psql.append(" where 1=1 ");
		psql.append("   and a.devicetype_id = b.devicetype_id_old ");

		if (!StringUtil.IsEmpty(vendor) && !"-1".equals(vendor)) {
			psql.append("   and a.vendor_id = '");
			psql.append(vendor);
			psql.append("' ");
		}

		if (!StringUtil.IsEmpty(device_model) && !"-1".equals(device_model)) {
			psql.append("   and a.device_model_id = '");
			psql.append(device_model);
			psql.append("'");
		}

		if (!StringUtil.IsEmpty(devicetypeId) && !"-1".equals(devicetypeId)) {
			psql.append("   and a.devicetype_id = ");
			psql.append(devicetypeId);
		}

		if (!StringUtil.IsEmpty(tempId) && !"-1".equals(tempId)) {
			psql.append("   and b.temp_id = ");
			psql.append(tempId);
		}

		if (!StringUtil.IsEmpty(hardwareversion)
				&& !"-1".equals(hardwareversion)) {
			psql.append("   and a.hardwareversion = '");
			psql.append(hardwareversion);
			psql.append("'");
		}
		
		if (Global.SDLT.equals(Global.instAreaShortName)||Global.SDDX.equals(Global.instAreaShortName)) {
			if (!StringUtil.IsEmpty(cityId) && !"-1".equals(cityId)) {
				psql.append("   and (b.city_id like '%"+cityId+"%' or b.city_id is null)");
			}
		}

		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
}
