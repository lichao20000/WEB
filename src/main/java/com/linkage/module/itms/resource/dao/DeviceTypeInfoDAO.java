/**
 *
 */

package com.linkage.module.itms.resource.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.TimeUtil;
import com.linkage.litms.Global;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.resource.bio.DeviceTypeInfoBIO;
import com.linkage.module.itms.resource.enums.AHLTDevVersionTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenjie
 */
public class DeviceTypeInfoDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(DeviceTypeInfoDAO.class);
	public static final String HBLT="hb_lt";
	public static final String AHLT="ah_lt";
	public static final String SXLT="sx_lt";
	public static final String SDDX="sd_dx";
	public static final String JSDX="js_dx";
	public static final String NXDX="nx_dx";
	public static final String XJDX="xj_dx";
	public static final String JLDX="jl_dx";
	public static final String JXDX="jx_dx";
	public static final String NMGDX="nmg_dx";
	public static final String SXDX="sx_dx";
	public static final String GSDX="gs_dx";
	private static String instArea=com.linkage.module.gwms.Global.instAreaShortName;

	@SuppressWarnings("unchecked")
	public List<Map> queryDeviceList(int vendor, int device_model, String hard_version,
			String soft_version, int is_check, int rela_dev_type, int curPage_splitPage,
			int num_splitPage, String startTime, String endTime,
			int access_style_relay_id, int spec_id, String machineConfig, String ipvsix,
			String startOpenDate, String endOpenDate, String mbBroadband,
			String device_version_type, String wifi, String wifi_frequency,
			String download_max_wifi, String gigabit_port, String gigabit_port_type,
			String download_max_lan, String power, String terminal_access_time,String deviceVersionType,int isSupSpeedTest_Query)
	{
		logger.debug("queryDeviceList({},{},{},{},{},{},{},{})", new Object[] { vendor,
				device_model, hard_version, soft_version, is_check, rela_dev_type,
				curPage_splitPage, num_splitPage });
		StringBuffer sb = new StringBuffer();
		// sql修改采用左链接实现，部分地市如江西电信tab_devicetype_info.spec_id可以为空
		String sql = null;
		// 江苏电信的sql，多一个reason(定版原因)字段
		if (JSDX.equals(instArea))
		{
			sql = "select a.zeroconf,a.versionttime,a.mbbroadband,"
					+ "a.devicetype_id, a.vendor_id, a.device_model_id, a.spec_id,"
					+ " a.access_style_relay_id, c.vendor_add, b.device_model, d.spec_name as specName, "
					+ " a.specversion, a.hardwareversion, a.softwareversion, a.is_check, "
					+ "a.rela_dev_type_id,a.ip_type,a.ip_model_type,is_normal,a.reason,a.is_awifi,e.is_speedTest,a.is_qoe"
					+ "  from tab_devicetype_info a left join tab_bss_dev_port d on a.spec_id = d.id left join tab_device_version_attribute e on a.devicetype_id"
					+ "=e.devicetype_id, "
					+ "gw_device_model b,tab_vendor c"
					+ " where a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id";
		}
		// 其他地区
		else
		{
			sql = "select a.zeroconf,a.versionttime,a.mbbroadband,"
					+ "a.devicetype_id, a.vendor_id, a.device_model_id, a.spec_id,"
					+ " a.access_style_relay_id, c.vendor_add, b.device_model, d.spec_name as specName, "
					+ " a.specversion, a.hardwareversion, a.softwareversion, a.is_check, "
					+ "a.rela_dev_type_id,a.ip_type,a.ip_model_type,is_normal "
					+ " from tab_devicetype_info a left join tab_bss_dev_port d on a.spec_id = d.id, gw_device_model b,tab_vendor c"
					+ " where a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id";
			if (XJDX.equals(instArea))
			{
				sql = "select a.zeroconf,a.versionttime,a.mbbroadband,"
						+ "a.devicetype_id, a.vendor_id, a.device_model_id, a.spec_id,"
						+ " a.access_style_relay_id, c.vendor_add, b.device_model, d.spec_name as specName, "
						+ " a.specversion, a.hardwareversion, a.softwareversion, a.is_check,a.is_multicast, "
						+ " a.rela_dev_type_id,a.ip_type,a.ip_model_type,is_normal,e.device_version_type,e.wifi,e.wifi_frequency,e.wifi_ability,"
						+" e.is_security_plugin,e.security_plugin_type,"
						+ " e.download_max_wifi,e.gigabit_port,e.gigabit_port_type,e.download_max_lan,e.power,e.terminal_access_time, "
						+ " e.iscloudnet, e.is_speedtest "
						+ " from tab_devicetype_info a left join tab_bss_dev_port d on a.spec_id = d.id left join tab_device_version_attribute e"
						+ " on a.devicetype_id=e.devicetype_id , gw_device_model b,tab_vendor c"
						+ " where a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id";
			}
			if(HBLT.equals(instArea)){
				sql = "select a.zeroconf,a.versionttime,a.mbbroadband,"
						+ "a.devicetype_id, a.vendor_id, a.device_model_id, a.spec_id,"
						+ " a.access_style_relay_id, c.vendor_add, b.device_model, d.spec_name as specName, "
						+ " a.specversion, a.hardwareversion, a.softwareversion, a.is_check,a.is_multicast, "
						+ " a.rela_dev_type_id,a.ip_type,a.ip_model_type,is_normal,e.device_version_type,e.wifi,e.wifi_frequency,"
						+ " e.download_max_wifi,e.gigabit_port,e.gigabit_port_type,e.download_max_lan,e.power,e.terminal_access_time,e.version_feature "
						+ " from tab_devicetype_info a left join tab_bss_dev_port d on a.spec_id = d.id left join tab_device_version_attribute e"
						+ " on a.devicetype_id=e.devicetype_id , gw_device_model b,tab_vendor c"
						+ " where a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id";
			}
			if (JLDX.equals(instArea))
			{
				sql = "select a.zeroconf,a.versionttime,a.mbbroadband,"
						+ "a.devicetype_id, a.vendor_id, a.device_model_id, a.spec_id,"
						+ " a.access_style_relay_id, c.vendor_add, b.device_model, d.spec_name as specName,e.device_version_type, "
						+ " a.specversion, a.hardwareversion, a.softwareversion, a.is_check, "
						+ "a.rela_dev_type_id,a.ip_type,a.ip_model_type,is_normal "
						+ " from tab_devicetype_info a left join tab_bss_dev_port d on a.spec_id = d.id left join tab_device_version_attribute e on a.devicetype_id=e.devicetype_id , gw_device_model b,tab_vendor c"
						+ " where a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id";
			}
			if (SDDX.equals(instArea))
			{
				sql = "select a.zeroconf,a.versionttime,a.mbbroadband,"
						+ "a.devicetype_id, a.vendor_id, a.device_model_id, a.spec_id,"
						+ " a.access_style_relay_id, c.vendor_add, b.device_model, d.spec_name as specName,e.device_version_type, "
						+ " a.specversion, a.hardwareversion, a.softwareversion, a.is_check, "
						+ "a.rela_dev_type_id,a.ip_type,a.ip_model_type,is_normal,e.is_speedTest"
						+ " from tab_devicetype_info a left join tab_bss_dev_port d on a.spec_id = d.id left join tab_device_version_attribute e on a.devicetype_id=e.devicetype_id , gw_device_model b,tab_vendor c"
						+ " where a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id";
			}

			if (NXDX.equals(instArea))
			{
				sql = "select a.zeroconf,a.versionttime,a.mbbroadband,"
						+ "a.devicetype_id, a.vendor_id, a.device_model_id, a.spec_id,"
						+ " a.access_style_relay_id, c.vendor_add, b.device_model, d.spec_name as specName, e.device_version_type,"
						+ " a.specversion, a.hardwareversion, a.softwareversion, a.is_check, "
						+ "a.rela_dev_type_id,a.ip_type,a.ip_model_type,is_normal "
						+ " from tab_devicetype_info a left join tab_bss_dev_port d on a.spec_id = d.id left join tab_device_version_attribute e on a.devicetype_id=e.devicetype_id ,gw_device_model b,tab_vendor c"
						+ " where a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id";
			}
			if (JXDX.equals(instArea))
			{
				sql = "select a.zeroconf,a.versionttime,a.mbbroadband,"
						+ "a.devicetype_id, a.vendor_id, a.device_model_id, a.spec_id,"
						+ " a.access_style_relay_id, c.vendor_add, b.device_model, d.spec_name as specName, "
						+ " a.specversion, a.hardwareversion, a.softwareversion, a.is_check, "
						+ "a.rela_dev_type_id,a.ip_type,a.ip_model_type,is_normal,e.is_tyGate,e.gbBroadband, e.is_speedTest, e.wifi_ability,e.device_version_type,a.is_qoe,a.is_highversion  "
						+ " from tab_devicetype_info a left join tab_bss_dev_port d on a.spec_id = d.id left join tab_device_version_attribute e"
						+ " on a.devicetype_id=e.devicetype_id , gw_device_model b,tab_vendor c"
						+ " where a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id ";
			}
			if(NMGDX.equals(instArea)){
				sql = "select a.zeroconf,a.versionttime,a.mbbroadband,"
						+ "a.devicetype_id, a.vendor_id, a.device_model_id, a.spec_id,"
						+ " a.access_style_relay_id, c.vendor_add, b.device_model, d.spec_name as specName, "
						+ " a.specversion, a.hardwareversion, a.softwareversion, a.is_check, "
						+ "a.rela_dev_type_id,a.ip_type,a.ip_model_type,is_normal, e.wifi_ability, "
						+ " e.wifi,e.device_version_type,e.gigabitNum,e.mbitNum,e.voipNum,e.is_wifi_double ,e.fusion_ability,e.terminal_access_method, "
						+ " e.devMaxSpeed "
						+ " from tab_devicetype_info a left join tab_bss_dev_port d on a.spec_id = d.id left join tab_device_version_attribute e"
						+ " on a.devicetype_id=e.devicetype_id , gw_device_model b,tab_vendor c"
						+ " where a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id";
			}
			if (GSDX.equals(instArea))
			{
				sql = "select a.zeroconf,a.versionttime,a.mbbroadband,e.ssid_instancenum,e.hvoip_port,e.device_version_type,"
						+ "a.devicetype_id, a.vendor_id, a.device_model_id, a.spec_id,"
						+ " a.access_style_relay_id, c.vendor_add, b.device_model, d.spec_name as specName, "
						+ " a.specversion, a.hardwareversion, a.softwareversion, a.is_check, "
						+ "a.rela_dev_type_id,a.ip_type,a.ip_model_type,is_normal "
						+ " from tab_devicetype_info a left join tab_bss_dev_port d on a.spec_id = d.id left join tab_device_version_attribute e on e.devicetype_id = a.devicetype_id, gw_device_model b,tab_vendor c"
						+ " where a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id";
			}
			//sx_dx
			if (SXDX.equals(instArea))
			{
				sql = "select a.zeroconf,a.versionttime,a.mbbroadband,e.hvoip_type,e.svoip_type,e.device_version_type,"
						+ "a.devicetype_id, a.vendor_id, a.device_model_id, a.spec_id,"
						+ " a.access_style_relay_id, c.vendor_add, b.device_model, d.spec_name as specName, "
						+ " a.specversion, a.hardwareversion, a.softwareversion, a.is_check, "
						+ "a.rela_dev_type_id,a.ip_type,a.ip_model_type,is_normal ,f.type_name as rela_dev_type_name"
						+ " from tab_devicetype_info a left join tab_bss_dev_port d on a.spec_id = d.id left join tab_device_version_attribute e on e.devicetype_id = a.devicetype_id left join gw_dev_type f on a.rela_dev_type_id = f.type_id , gw_device_model b,tab_vendor c"
						+ " where a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id";
			}
			if(AHLT.equals(instArea)){
				sql = "select a.zeroconf,a.versionttime,a.mbbroadband,"
						+ "a.devicetype_id, a.vendor_id, a.device_model_id, a.spec_id,"
						+ " a.access_style_relay_id, c.vendor_add, b.device_model, d.spec_name as specName, "
						+ " a.specversion, a.hardwareversion, a.softwareversion, a.is_check, "
						+ "a.rela_dev_type_id,a.ip_type,a.ip_model_type,is_normal,a.is_qoe,v.device_version_type"
						+ " from tab_devicetype_info a left join tab_bss_dev_port d on a.spec_id = d.id "
						+ " left join tab_device_version_attribute v on a.devicetype_id = v.devicetype_id "
						+ ", gw_device_model b,tab_vendor c"
						+ " where a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id";
			}
		}
		sb.append(sql);
		if (!"-1".equals(machineConfig))
		{
			sb.append(" and a.zeroconf=" + machineConfig + "");
		}
		if (!"-1".equals(ipvsix))
		{
			if ("1".equals(ipvsix))
			{
				sb.append(" and a.ip_type <> 0");
			}
			if ("2".equals(ipvsix))
			{
				sb.append(" and a.ip_type = 0");
			}
		}
		if (startOpenDate != null && !"".equals(startOpenDate))
		{
			sb.append(" and a.versionttime >= " + startOpenDate);
		}
		if (endOpenDate != null && !"".equals(endOpenDate))
		{
			sb.append(" and a.versionttime <= " + endOpenDate);
		}
		if (mbBroadband != null && !"-1".equals(mbBroadband))
		{
			sb.append(" and a.mbbroadband=" + mbBroadband + "");
		}
		if (vendor != -1)
		{
			sb.append(" and c.vendor_id='" + vendor + "'");
		}
		if (device_model != -1)
		{
			sb.append(" and b.device_model_id='" + device_model + "'");
		}
		if (hard_version != null && !"".equals(hard_version))
		{
			sb.append(" and a.hardwareversion='" + hard_version + "'");
		}
		// 软件版本后模糊匹配
		if (soft_version != null && !"".equals(soft_version))
		{
			sb.append(" and a.softwareversion like '" + soft_version + "%'");
		}
		if (is_check != -2)
		{
			sb.append(" and a.is_check=" + is_check);
		}
		if (rela_dev_type != -1)
		{
			sb.append(" and a.rela_dev_type_id=" + rela_dev_type);
		}
		if (startTime != null && !"".equals(startTime))
		{
			sb.append(" and a.add_time > " + startTime);
		}
		if (endTime != null && !"".equals(endTime))
		{
			sb.append(" and a.add_time < " + endTime);
		}
		if (access_style_relay_id != -1)
		{
			sb.append(" and a.access_style_relay_id=" + access_style_relay_id);
		}
		if (spec_id != -1)
		{
			sb.append(" and spec_id = " + spec_id);
		}
		if(AHLT.equals(instArea)
				&& !StringUtil.IsEmpty(deviceVersionType) && !"-1".equals(deviceVersionType)){
			sb.append(" and v.device_version_type = " + deviceVersionType);
		}

		//山东电信新增 查询条件 是否支持测速
		if(isSupSpeedTest_Query != -1 && SDDX.equals(instArea))
		{
			sb.append(" and e.is_speedTest = " + isSupSpeedTest_Query);
		}
		/*
		 * if(!StringUtil.IsEmpty(device_version_type)&&device_version_type!="-1") {
		 * sb.append(" and e.device_version_type='"+device_version_type+"'"); }
		 * if(!StringUtil.IsEmpty(wifi)&&wifi!="-1") {
		 * sb.append(" and e.wifi='"+wifi+"'"); }
		 * if(!StringUtil.IsEmpty(wifi_frequency)&&wifi_frequency!="-1") {
		 * sb.append(" and e.wifi_frequency='"+wifi_frequency+"'"); }
		 * if(!StringUtil.IsEmpty(download_max_wifi)) {
		 * sb.append(" and e.download_max_wifi='"+download_max_wifi+"'"); }
		 * if(!StringUtil.IsEmpty(gigabit_port)&&gigabit_port!="-1") {
		 * sb.append(" and e.gigabit_port='"+gigabit_port+"'"); }
		 * if(!StringUtil.IsEmpty(gigabit_port_type)&&gigabit_port_type!="-1") {
		 * sb.append(" and e.gigabit_port_type='"+gigabit_port_type+"'"); }
		 * if(!StringUtil.IsEmpty(download_max_lan)) {
		 * sb.append(" and e.download_max_lan='"+download_max_lan+"'"); }
		 * if(!StringUtil.IsEmpty(power)) { sb.append(" and e.power='"+power+"'"); }
		 * if(!StringUtil.IsEmpty(device_version_type)&&device_version_type!="-1") {
		 * sb.append(" and e.device_version_type='"+device_version_type+"'"); }
		 */
		sb.append(" order by a.devicetype_id");
		PrepareSQL psql = new PrepareSQL(sb.toString());
		final Map accessTypeMap = getAccessType();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				String accessStyleRelayId = StringUtil.getStringValue(rs
						.getString("access_style_relay_id"));
				map.put("devicetype_id", rs.getString("devicetype_id"));
				map.put("zeroconf", rs.getString("zeroconf"));
				try
				{
					long versionttime = StringUtil.getLongValue(rs
							.getString("versionttime")) * 1000L;
					DateTimeUtil dt = new DateTimeUtil(versionttime);
					map.put("versionttime", dt.getLongDate());
				}
				catch (NumberFormatException e)
				{
					map.put("versionttime", "");
				}
				catch (Exception e)
				{
					map.put("versionttime", "");
				}
				map.put("mbbroadband", rs.getString("mbbroadband"));
				map.put("vendor_add", rs.getString("vendor_add"));
				map.put("device_model", rs.getString("device_model"));
				map.put("spec_id", rs.getString("spec_id"));
				map.put("specversion", rs.getString("specversion"));
				map.put("hardwareversion", rs.getString("hardwareversion"));
				map.put("softwareversion", rs.getString("softwareversion"));
				map.put("is_check", rs.getString("is_check"));
				map.put("rela_dev_type_id", rs.getString("rela_dev_type_id"));
				map.put("vendor_id", rs.getString("vendor_id"));
				map.put("device_model_id", rs.getString("device_model_id"));
				map.put("ip_type", rs.getString("ip_type"));
				map.put("ip_model_type", rs.getString("ip_model_type"));
				map.put("is_normal", rs.getString("is_normal"));
				map.put("specName", rs.getString("specName"));
				if (SXDX.equals(instArea))
				{
					map.put("rela_dev_type_name", rs.getString("rela_dev_type_name"));
				}else {
					map.put("rela_dev_type_name", Global.G_DeviceTypeID_Name_Map.get(rs
							.getString("rela_dev_type_id")));
				}
				// 定版原因，仅对江苏电信有效
				if (JSDX.equals(instArea))
				{
					map.put("reason", rs.getString("reason"));
					map.put("is_awifi", rs.getString("is_awifi"));
					map.put("is_speedtest", rs.getString("is_speedtest"));
					map.put("is_qoe", rs.getString("is_qoe"));
				}
				if (JLDX.equals(instArea))
				{
					map.put("device_version_type", rs.getString("device_version_type"));
					map.put("wifi", "");
					map.put("wifi_frequency", "");
					map.put("download_max_wifi", "");
					map.put("gigabit_port", "");
					map.put("gigabit_port_type", "");
					map.put("download_max_lan", "");
					map.put("power", "");
					map.put("terminal_access_time", "");
				}
				else if (XJDX.equals(instArea)
						|| HBLT.equals(instArea))
				{ // 是否支持组播，针对新疆电信
					map.put("is_multicast", rs.getString("is_multicast"));
					map.put("device_version_type", rs.getString("device_version_type"));
					map.put("wifi", rs.getString("wifi"));
					map.put("wifi_frequency", rs.getString("wifi_frequency"));
					map.put("download_max_wifi", rs.getString("download_max_wifi"));
					map.put("gigabit_port", StringUtil.IsEmpty(rs.getString("gigabit_port"))?"-1":rs.getString("gigabit_port"));
					map.put("gigabit_port_type", rs.getString("gigabit_port_type"));
					map.put("download_max_lan", rs.getString("download_max_lan"));
					map.put("power", rs.getString("power"));
					try
					{
						long versionttime = StringUtil.getLongValue(rs
								.getString("terminal_access_time")) * 1000L;
						DateTimeUtil dt = new DateTimeUtil(versionttime);
						map.put("terminal_access_time", dt.getLongDate());
					}
					catch (NumberFormatException e)
					{
						map.put("terminal_access_time", "");
					}
					catch (Exception e)
					{
						map.put("terminal_access_time", "");
					}

					if(XJDX.equals(instArea)){
						map.put("is_security_plugin", rs.getString("is_security_plugin"));
						map.put("security_plugin_type", rs.getString("security_plugin_type"));
						map.put("wifi_ability", rs.getString("wifi_ability"));
						map.put("iscloudnet", rs.getString("iscloudnet"));
						map.put("is_speedTest", rs.getString("is_speedtest"));
					}
				}
				else
				{
					if(SDDX.equals(instArea)){
						map.put("device_version_type", rs.getString("device_version_type"));
						map.put("is_speedTest", rs.getString("is_speedTest"));
					}else{
						map.put("device_version_type", "");
					}
					map.put("wifi", "");
					map.put("wifi_frequency", "");
					map.put("download_max_wifi", "");
					map.put("gigabit_port", "");
					map.put("gigabit_port_type", "");
					map.put("download_max_lan", "");
					map.put("power", "");
					map.put("terminal_access_time", "");
				}
				if(HBLT.equals(instArea))
				{
					map.put("version_feature", StringUtil.IsEmpty(rs.getString("version_feature"))?"0":rs.getString("version_feature"));
				}
				if (JXDX.equals(instArea))
				{
					if (StringUtil.IsEmpty(rs.getString("is_tyGate")))
					{
						map.put("is_esurfing", "0");
					}
					else
					{
						map.put("is_esurfing", rs.getString("is_tyGate"));
					}
					map.put("gbbroadband", rs.getString("gbbroadband"));
					map.put("wifi_ability", rs.getString("wifi_ability"));
					map.put("is_speedTest", rs.getString("is_speedTest"));
					map.put("is_qoe", rs.getString("is_qoe"));
					map.put("is_highversion", rs.getString("is_highversion"));
					map.put("device_version_type", rs.getString("device_version_type"));
				}
				if (!"".equals(accessStyleRelayId))
				{
					// String type_name =
					// getTypeNameByTypeId(accessStyleRelayId);
					map.put("type_id", accessStyleRelayId);
					map.put("type_name", (String) accessTypeMap.get(accessStyleRelayId));
				}
				else
				{
					map.put("type_id", "");
					map.put("type_name", "");
				}
				if(NMGDX.equals(instArea)){
					map.put("device_version_type", rs.getString("device_version_type"));
					map.put("wifi_ability", rs.getString("wifi_ability"));
					map.put("gigabitNum", rs.getString("gigabitNum"));
					map.put("mbitNum", rs.getString("mbitNum"));
					map.put("voipNum", rs.getString("voipNum"));
					map.put("is_wifi_double", rs.getString("is_wifi_double"));
					map.put("fusion_ability", rs.getString("fusion_ability"));
					map.put("terminal_access_method", rs.getString("terminal_access_method"));
					map.put("devMaxSpeed", rs.getString("devMaxSpeed"));
					map.put("wifi", rs.getString("wifi"));
				}
				if(GSDX.equals(instArea)){
					map.put("ssid_instancenum", rs.getString("ssid_instancenum"));
					map.put("hvoip_port", rs.getString("hvoip_port"));
					map.put("device_version_type", rs.getString("device_version_type"));
				}
				//sx_dx
				if(SXDX.equals(instArea)){
					map.put("hvoip_type", rs.getString("hvoip_type"));
					map.put("svoip_type", rs.getString("svoip_type"));
					map.put("device_version_type", rs.getString("device_version_type"));
				}
				if(NXDX.equals(instArea)){
					map.put("device_version_type", rs.getString("device_version_type"));
				}
				if(AHLT.equals(instArea))
				{
					String devVersionTypeId = rs.getString("device_version_type");
					map.put("device_version_type", rs.getString("device_version_type"));
					map.put("devVersionType", StringUtil.IsEmpty(devVersionTypeId) ? "" : AHLTDevVersionTypeEnum.getNameByCode(devVersionTypeId));
					map.put("is_qoe", rs.getString("is_qoe"));
				}
				return map;
			}
		});
		return list;
	}

	public List<Map<String, String>> querySpecList()
	{
		String specSql = "select id,spec_name from tab_bss_dev_port";
		if(AHLT.equals(instArea)){
			specSql = specSql + " where status != -1";
		}
		List<Map<String, String>> specList = new ArrayList<Map<String, String>>();
		PrepareSQL specPsql = new PrepareSQL(specSql.toString());
		specList = jt.queryForList(specPsql.getSQL());
		return specList;
	}

	public String getSpecName(String specId)
	{
		String specSql = "select spec_name from tab_bss_dev_port where id = " + specId;
		List<Map> specList = new ArrayList<Map>();
		PrepareSQL specPsql = new PrepareSQL(specSql.toString());
		specList = jt.queryForList(specPsql.getSQL());
		String specName = "";
		if (specList.size() > 0)
		{
			specName = (String) specList.get(0).get("spec_name");
		}
		return specName;
	}

	public String getPortAndType(long deviceTypeId)
	{
		String portSql = "select port_name,port_dir,port_type,port_desc from tab_devicetype_info_port "
				+ "where devicetype_id=" + deviceTypeId;
		List<Map> portList = new ArrayList<Map>();
		PrepareSQL portPsql = new PrepareSQL(portSql.toString());
		portList = jt.queryForList(portPsql.getSQL());
		String name = "";
		String dir = "";
		String type = "";
		String desc = "";
		if (portList.size() > 0)
		{
			for (int i = 0; i < portList.size(); i++)
			{
				name += StringUtil.getStringValue(portList.get(i).get("port_name")) + "#";
				dir += StringUtil.getStringValue(portList.get(i).get("port_dir")) + "#";
				type += StringUtil.getStringValue(portList.get(i).get("port_type")) + "#";
				desc += StringUtil.getStringValue(portList.get(i).get("port_desc")) + "#";
			}
		}
		String typeSql = "select server_type from tab_devicetype_info_servertype "
				+ "where devicetype_id=" + deviceTypeId;
		List<Map> typeList = new ArrayList<Map>();
		PrepareSQL typePsql = new PrepareSQL(typeSql.toString());
		typeList = jt.queryForList(typePsql.getSQL());
		String serverType = "";
		if (typeList.size() > 0)
		{
			for (int i = 0; i < typeList.size(); i++)
			{
				serverType += StringUtil.getStringValue(typeList.get(i)
						.get("server_type")) + ",";
			}
			serverType = serverType.substring(0, serverType.length() - 1);
		}
		String result = "";
		result = name + dir + type + desc + "&" + serverType;
		return result;
	}

	public Map getAccessType()
	{
		String sql = "select * from gw_access_type ";
		// teledb
		if (DBUtil.GetDB() == 3) {
			sql = "select type_id, type_name from gw_access_type ";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> AccessMap = new HashMap<String, String>();
		list = jt.queryForList(sql);
		for (Map accessMap : list)
		{
			AccessMap.put(accessMap.get("type_id").toString(),
					(String) accessMap.get("type_name"));
		}
		return AccessMap;
	}

	/**
	 * @param vendor
	 * @return 查询的特定数据
	 */
	@SuppressWarnings("unchecked")
	public List<Map> queryDeviceDetail(long deviceTypeId, long spec_id)
	{
		String infoSql = "select a.zeroconf,a.versionttime,a.mbbroadband,a.devicetype_id, c.vendor_add, "
				+ "b.device_model, a.access_style_relay_id,a.spec_id,"
				+ " a.specversion, a.hardwareversion, a.softwareversion, a.is_check, a.rela_dev_type_id,"
				+ " a.access_style_relay_id,a.ip_type,a.ip_model_type,is_normal";
		// 江苏电信itms需要查询是否开通了awifi
		if (JSDX.equals(instArea))
		{
			infoSql += ",a.is_awifi";
		}

		if(XJDX.equals(instArea)){
			infoSql += " from tab_devicetype_info a, gw_device_model b,tab_vendor c where a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id and a.devicetype_id="
					+ deviceTypeId;
		}else if(NMGDX.equals(instArea)){
			infoSql += ",d.wifi_ability,d.wifi, d.gigabitNum,d.mbitNum,d.voipNum,d.is_wifi_double ,d.fusion_ability,d.terminal_access_method,"
					+ " d.devMaxSpeed,d.device_version_type  "
					+ " from tab_devicetype_info a left join tab_device_version_attribute d on a.devicetype_id=d.devicetype_id, gw_device_model b,tab_vendor c "
					+ " where a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id and a.devicetype_id="
					+ deviceTypeId;
		}else if(SDDX.equals(instArea)){

			infoSql += ",d.device_version_type,d.is_speedTest  from tab_devicetype_info a left join tab_device_version_attribute d on a.devicetype_id=d.devicetype_id,"
					+  " gw_device_model b,tab_vendor c where a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id and a.devicetype_id="
					+ deviceTypeId;;

		}else{

			infoSql += " from tab_devicetype_info a, gw_device_model b,tab_vendor c where a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id and a.devicetype_id="
					+ deviceTypeId;
		}

		PrepareSQL infoPsql = new PrepareSQL(infoSql.toString());
		List<Map> infoList = new ArrayList<Map>();
		infoList = jt.queryForList(infoPsql.getSQL());
		if (infoList.size() > 0)
		{
			infoList.get(0).put("zeroconf", infoList.get(0).get("zeroconf"));
			try
			{
				String tempversionttime = String.valueOf(infoList.get(0).get(
						"versionttime"));
				if (!StringUtil.IsEmpty(tempversionttime))
				{
					long versionttime = StringUtil.getLongValue(tempversionttime) * 1000L;
					DateTimeUtil dt = new DateTimeUtil(versionttime);
					infoList.get(0).put("versionttime", dt.getLongDate());
				}
				else
				{
					infoList.get(0).put("versionttime", "");
				}
			}
			catch (NumberFormatException e)
			{
				infoList.get(0).put("versionttime", "");
			}
			catch (Exception e)
			{
				infoList.get(0).put("versionttime", "");
			}
			infoList.get(0).put("mbbroadband", infoList.get(0).get("mbbroadband"));
			if (JSDX.equals(instArea))
			{
				infoList.get(0).put("is_awifi", infoList.get(0).get("is_awifi"));
			}
			if(NMGDX.equals(instArea)){
				infoList.get(0).put("wifi_ability", infoList.get(0).get("wifi_ability"));
			}
			if(SDDX.equals(instArea)){

				infoList.get(0).put("device_version_type", getDeviceVersionType(infoList.get(0).get("device_version_type")));
				infoList.get(0).put("is_speedTest", infoList.get(0).get("is_speedTest"));
			}
			String access_style_relay_id = StringUtil.getStringValue(infoList.get(0).get(
					"access_style_relay_id"));
			if (!"".equals(access_style_relay_id))
			{
				String type_name = getTypeNameByTypeId(access_style_relay_id);
				infoList.get(0).put("type_id", access_style_relay_id);
				infoList.get(0).put("type_name", type_name);
			}
			else
			{
				infoList.get(0).put("type_id", "");
				infoList.get(0).put("type_name", "");
			}
			infoList.get(0).put(
					"rela_dev_type_name",
					StringUtil.IsEmpty(StringUtil.getStringValue(infoList.get(0).get(
							"rela_dev_type_id"))) ? "" : Global.G_DeviceTypeID_Name_Map
							.get(StringUtil.getStringValue(infoList.get(0).get(
									"rela_dev_type_id"))));
		}
		String specName = getSpecName(String.valueOf(spec_id));
		infoList.get(0).put("specName", specName);
		String portSql = "select port_name,port_dir,port_type,port_desc from tab_devicetype_info_port "
				+ "where devicetype_id=" + deviceTypeId;
		List<Map> portList = new ArrayList<Map>();
		PrepareSQL portPsql = new PrepareSQL(portSql.toString());
		portList = jt.queryForList(portPsql.getSQL());
		if (portList.size() > 0 && infoList.size() > 0)
		{
			String port = "";
			for (int i = 0; i < portList.size(); i++)
			{
				port += StringUtil.getStringValue(portList.get(i).get("port_name")) + ",";
			}
			port = port.substring(0, port.length() - 1);
			infoList.get(0).put("port_name", port);
		}
		String typeSql = "select server_type from tab_devicetype_info_servertype where devicetype_id="
				+ deviceTypeId;
		List<Map> typeList = new ArrayList<Map>();
		PrepareSQL typePsql = new PrepareSQL(typeSql.toString());
		typeList = jt.queryForList(typePsql.getSQL());
		if (typeList.size() > 0 && infoList.size() > 0)
		{
			String type = "";
			for (int i = 0; i < typeList.size(); i++)
			{
				String tempType = StringUtil.getStringValue(typeList.get(i).get(
						"server_type"));
				if (tempType.equals("0"))
				{
					type = type + "IMS SIP" + ",";
				}
				if (tempType.equals("1"))
				{
					type = type + "软交换SIP" + ",";
				}
				if (tempType.equals("2"))
				{
					type = type + "H248" + ",";
				}
			}
			type = type.substring(0, type.length() - 1);
			infoList.get(0).put("server_type", type);
		}
		return infoList;
	}

	/**
	 * 根据上行方式ID获取上行方式名称 add by zhangchy 2011-10-25
	 *
	 * @param accessStyleRelayId
	 * @return
	 */
	public String getTypeNameByTypeId(String accessStyleRelayId)
	{
		StringBuffer sb = new StringBuffer(
				"select distinct type_name from gw_access_type where type_id = "
						+ accessStyleRelayId);
		PrepareSQL psql = new PrepareSQL(sb.toString());
		List<Map> list = jt.queryForList(psql.getSQL());
		if (list.size() < 1)
		{
			return "";
		}
		if (StringUtil.getStringValue(list.get(0), "type_name") == null)
		{
			return "";
		}
		return StringUtil.getStringValue(list.get(0), "type_name");
	}

	public String getTypeNameList(String typeId)
	{
		StringBuffer sb = new StringBuffer(
				"select distinct type_id, type_name from gw_access_type");
		PrepareSQL psql = new PrepareSQL(sb.toString());
		psql.getSQL();
		return DeviceTypeInfoBIO.createListBox(DataSetBean.getCursor(sb.toString()),
				"type_id", "type_name", typeId, true);
	}

	public int getDeviceListCount(int vendor, int device_model, String hard_version,
			String soft_version, int is_check, int rela_dev_type, int curPage_splitPage,
			int num_splitPage, String startTime, String endTime,
			int access_style_relay_id, int spec_id, String machineConfig, String ipvsix,
			String startOpenDate, String endOpenDate, String mbBroadband,
			String device_version_type, String wifi, String wifi_frequency,
			String download_max_wifi, String gigabit_port, String gigabit_port_type,
			String download_max_lan, String power, String terminal_access_time,int isSupSpeedTest_Query,String deviceVersionType)
	{
		logger.debug("getDeviceListCount({},{},{},{},{},{},{},{})", new Object[] {
				vendor, device_model, hard_version, soft_version, is_check,
				rela_dev_type, curPage_splitPage, num_splitPage });
		StringBuffer sb = new StringBuffer();
		String sql = "select count(1) from tab_devicetype_info a, gw_device_model b,tab_vendor c"
				+ " where a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id ";

		if(SDDX.equals(instArea))
		{
			sql = " select count(1) from tab_devicetype_info a left join tab_device_version_attribute d on  a.devicetype_id=d.devicetype_id ,"+
					" gw_device_model b,tab_vendor c where a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id  ";
		}
		if(AHLT.equals(instArea)){
			sql = "select count(1) from tab_devicetype_info a,gw_device_model b,tab_vendor c,tab_device_version_attribute v"
					+ " where a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id and a.devicetype_id = v.devicetype_id";
		}

		sb.append(sql);
		if (!"-1".equals(machineConfig))
		{
			sb.append(" and a.zeroconf=" + machineConfig + "");
		}
		if (!"-1".equals(ipvsix))
		{
			if ("1".equals(ipvsix))
			{
				sb.append(" and a.ip_type <> 0");
			}
			if ("2".equals(ipvsix))
			{
				sb.append(" and a.ip_type = 0");
			}
		}
		if (startOpenDate != null && !"".equals(startOpenDate))
		{
			sb.append(" and a.versionttime >= " + startOpenDate);
		}
		if (endOpenDate != null && !"".equals(endOpenDate))
		{
			sb.append(" and a.versionttime <= " + endOpenDate);
		}
		if (null != mbBroadband && !"-1".equals(mbBroadband))
		{
			sb.append(" and a.mbbroadband=" + mbBroadband + "");
		}
		if (vendor != -1)
		{
			sb.append(" and c.vendor_id='" + vendor + "'");
		}
		if (device_model != -1)
		{
			sb.append(" and b.device_model_id='" + device_model + "'");
		}
		if (hard_version != null && !"".equals(hard_version))
		{
			sb.append(" and a.hardwareversion='" + hard_version + "'");
		}
		// 软件版本后模糊匹配
		if (soft_version != null && !"".equals(soft_version))
		{
			sb.append(" and a.softwareversion like '" + soft_version + "%'");
		}
		if (is_check != -2)
		{
			sb.append(" and a.is_check=" + is_check);
		}
		if (rela_dev_type != -1)
		{
			sb.append(" and a.rela_dev_type_id=" + rela_dev_type);
		}
		if (startTime != null && !"".equals(startTime))
		{
			sb.append(" and a.add_time > " + startTime);
		}
		if (endTime != null && !"".equals(endTime))
		{
			sb.append(" and a.add_time < " + endTime);
		}
		if (access_style_relay_id != -1)
		{
			sb.append(" and a.access_style_relay_id=" + access_style_relay_id);
		}
		if (spec_id != -1)
		{
			sb.append(" and spec_id = " + spec_id);
		}
		//山东电信新增 查询条件 是否支持测速
		if(isSupSpeedTest_Query != -1 && SDDX.equals(instArea))
		{
			sb.append(" and d.is_speedTest = " + isSupSpeedTest_Query);
		}
		if(AHLT.equals(instArea)
				&& !StringUtil.IsEmpty(deviceVersionType) && !"-1".equals(deviceVersionType)){
			sb.append(" and v.device_version_type = " + deviceVersionType);
		}
		PrepareSQL psql = new PrepareSQL(sb.toString());
		int total = jt.queryForInt(psql.getSQL());
		logger.warn("total" + total);
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

	public void deleteDevice(int id)
	{
		logger.debug("deleteDevice({})", new Object[] { id });
		String[] sql = new String[4];
		sql[0] = "delete from tab_devicetype_info where devicetype_id=" + id;
		sql[1] = "delete from tab_devicetype_info_servertype where devicetype_id=" + id;
		sql[2] = "delete from tab_devicetype_info_port where devicetype_id=" + id;
		sql[3] = "delete from tab_device_version_attribute where devicetype_id=" + id;
		for (int i = 0; i < 4; i++)
		{
			PrepareSQL psql = new PrepareSQL(sql[i]);
			psql.setLong(1, id);
			sql[i] = psql.getSQL();
		}
		jt.batchUpdate(sql);
	}

	/**
	 * 根据devicetype_id获取该版本编号下有多少设备
	 *
	 * @param id
	 *            devicetype_id
	 * @return
	 */
	public int getDeviceListCount(int id)
	{
		String sql = "select count(1) from tab_gw_device where devicetype_id=" + id;
		// teledb
		if (DBUtil.GetDB() == 3) {
			sql = "select count(*) from tab_gw_device where devicetype_id=" + id;
		}
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForInt(psql.getSQL());
	}

	/**
	 * 增加设备版本
	 *
	 * @param vendor
	 *            厂商
	 * @param device_model
	 *            设备型号
	 * @param specversion
	 *            特定版本
	 * @param hard_version
	 *            硬件型号
	 * @param soft_version
	 *            软件型号
	 * @param is_check
	 *            是否审核
	 * @param rela_dev_type
	 *            设备类型
	 * @param gbBroadband
	 * @param is_newVersion
	 * @param remark
	 * @param res_type
	 * @param res_vendor
	 * @param res_type_id
	 * @param terminal_access_method
	 * @param fusion_ability
	 * @param is_wifi_double
	 * @param devMaxSpeed
	 * @param voipNum
	 * @param mbitNum
	 * @param gigabitNum
	 * @param remark
	 * @param res_type
	 * @param res_vendor
	 * @param res_type_id
	 * @param terminal_access_method
	 * @param fusion_ability
	 * @param is_wifi_double
	 * @param devMaxSpeed
	 * @param voipNum
	 * @param mbitNum
	 * @param gigabitNum
	 * @return
	 */
	public Object[] addDevTypeInfo(int vendor, int device_model, String specversion,
			String hard_version, String soft_version, int is_check, int rela_dev_type,
			String typeId, String ipType, long spec_id, String mbBroadband,
			String startOpenDate, String machineConfig, String is_awifi, String reason,
			int is_multicast, String is_qoe, String is_esurfing,
			String device_version_type, String wifi, String wifi_frequency,int wifi_ability,
			String download_max_wifi, String gigabit_port, String gigabit_port_type,
			String download_max_lan, String power, String terminal_access_time,
			String gbBroadband,String version_feature,int isSecurityPlugin,int securityPlugin,
			String is_newVersion,int ssid_instancenum,String hvoip_port,String hvoip_type,
			String svoip_type, int gigabitNum, int mbitNum, int voipNum, String devMaxSpeed,
			String is_wifi_double, String fusion_ability, String terminal_access_method, int iscloudnet

			)
	{
		String sql = null;
		String sql1 = null;
		String sql2 = null;
		if (JSDX.equals(instArea))
		{
			// 江苏电信
			sql = "insert into tab_devicetype_info(devicetype_id,vendor_id,device_model_id,specversion,"
					+ "hardwareversion,softwareversion,add_time,is_check,rela_dev_type_id,access_style_relay_id,"
					+ "ip_type,spec_id,zeroconf,versionttime,mbbroadband,ip_model_type,is_awifi,reason,is_qoe) "
					+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		}
		else
		{
			// 其他地区
			sql = "insert into tab_devicetype_info(devicetype_id,vendor_id,device_model_id,specversion,"
					+ "hardwareversion,softwareversion,add_time,is_check,rela_dev_type_id,access_style_relay_id,"
					+ "ip_type,spec_id,zeroconf,versionttime,mbbroadband,ip_model_type) "
					+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			if (XJDX.equals(instArea))
			{ 
				// 新疆
				sql = "insert into tab_devicetype_info(devicetype_id,vendor_id,device_model_id,specversion,"
						+ "hardwareversion,softwareversion,add_time,is_check,rela_dev_type_id,access_style_relay_id,"
						+ "ip_type,spec_id,zeroconf,versionttime,mbbroadband,ip_model_type,is_multicast) "
						+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				sql1 = "insert into tab_device_version_attribute (devicetype_id,device_version_type,wifi,"
						+"wifi_frequency,download_max_wifi,gigabit_port,gigabit_port_type,download_max_lan,"
						+"power,terminal_access_time,is_tygate,is_security_plugin,security_plugin_type,wifi_ability,iscloudnet)"
						+" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			}
			if (HBLT.equals(instArea))
			{ 
				// 河北
				sql = "insert into tab_devicetype_info(devicetype_id,vendor_id,device_model_id,specversion,"
						+ "hardwareversion,softwareversion,add_time,is_check,rela_dev_type_id,access_style_relay_id,"
						+ "ip_type,spec_id,zeroconf,versionttime,mbbroadband,ip_model_type,is_multicast) "
						+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				sql1 = "insert into tab_device_version_attribute (devicetype_id,device_version_type,wifi,wifi_frequency,download_max_wifi,gigabit_port,gigabit_port_type,download_max_lan,power,terminal_access_time,is_tygate,version_feature) values(?,?,?,?,?,?,?,?,?,?,?,?)";
			}
			if (JLDX.equals(instArea) ||
					SDDX.equals(instArea)
					||NXDX.equals(instArea)
					|| AHLT.equals(instArea))
				// 吉林
			{
				sql1 = "insert into tab_device_version_attribute (devicetype_id,device_version_type) values(?,?)";
			}
			if (JXDX.equals(instArea))
				// 江西
			{
				if ("1".equals(is_newVersion)){
					sql2 = "update tab_devicetype_info set is_highversion = 0 where vendor_id ='"+vendor+"' and device_model_id='"+device_model+"' and hardwareversion ='"+hard_version+"'";
					PrepareSQL psql2 = new PrepareSQL(sql2);
					jt.queryForList(psql2.getSQL());
				}
				sql = "insert into tab_devicetype_info(devicetype_id,vendor_id,device_model_id,specversion,"
						+ "hardwareversion,softwareversion,add_time,is_check,rela_dev_type_id,access_style_relay_id,"
						+ "ip_type,spec_id,zeroconf,versionttime,mbbroadband,ip_model_type,is_qoe,is_highversion) "
						+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				sql1 = "insert into tab_device_version_attribute (devicetype_id, is_tyGate, gbBroadband,device_version_type) values(?,?,?,?)";
			}
			if(GSDX.equals(instArea)) { 
				//甘肃
				// 其他地区
				sql = "insert into tab_devicetype_info(devicetype_id,vendor_id,device_model_id,specversion,"
						+ "hardwareversion,softwareversion,add_time,is_check,rela_dev_type_id,access_style_relay_id,"
						+ "ip_type,spec_id,zeroconf,versionttime,mbbroadband,ip_model_type) "
						+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				sql1 = "insert into tab_device_version_attribute(devicetype_id,ssid_instancenum,hvoip_port,device_version_type)values(?,?,?,?)";
			}
			//sx_dx
			if(SXDX.equals(instArea)) { 
				//陕西
				// 其他地区
				sql = "insert into tab_devicetype_info(devicetype_id,vendor_id,device_model_id,specversion,"
						+ "hardwareversion,softwareversion,add_time,is_check,rela_dev_type_id,access_style_relay_id,"
						+ "ip_type,spec_id,zeroconf,versionttime,mbbroadband,ip_model_type) "
						+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				sql1 = "insert into tab_device_version_attribute(devicetype_id,ssid_instancenum,hvoip_port,hvoip_type,svoip_type,device_version_type)values(?,?,?,?,?,?)";
			}
			if(NMGDX.equals(instArea))
			{
				sql1 = "insert into tab_device_version_attribute "
				+ " (devicetype_id,device_version_type,wifi,gigabitNum,mbitNum,voipNum,is_wifi_double,fusion_ability,"
				+ " terminal_access_method,devMaxSpeed) "
				+ "values "
				+ "(?,?,?,?,?,?,?,?,?,?)";
			}
		}
		long max_id = DataSetBean.getMaxId("tab_devicetype_info", "devicetype_id");
		PrepareSQL psql = new PrepareSQL(sql);
		PrepareSQL psql1 = new PrepareSQL(sql1);
		if (is_esurfing != null)
		{
			psql1.setLong(1, max_id);
			psql1.setInt(2, StringUtil.getIntegerValue(is_esurfing));
			psql1.setInt(3, StringUtil.getIntegerValue(gbBroadband));
		}

		if (JXDX.equals(instArea))
		{
			psql1.setLong(1, max_id);
			psql1.setInt(2, StringUtil.getIntegerValue(is_esurfing));
			psql1.setInt(3, StringUtil.getIntegerValue(gbBroadband));
			psql1.setInt(4, StringUtil.getIntegerValue(device_version_type));
		}

		if (JLDX.equals(instArea) ||
				SDDX.equals(instArea)||
				NXDX.equals(instArea)
				|| AHLT.equals(instArea))
		{
			psql1.setLong(1, max_id);
			psql1.setInt(2, StringUtil.getIntegerValue(device_version_type));
		}
		if (XJDX.equals(instArea))
		{
			psql1.setLong(1, max_id);
			psql1.setInt(2, StringUtil.getIntegerValue(device_version_type));
			psql1.setString(3, wifi);
			psql1.setInt(4, StringUtil.getIntegerValue(wifi_frequency));
			psql1.setInt(5, StringUtil.getIntegerValue(download_max_wifi));
			psql1.setInt(6, StringUtil.getIntegerValue(gigabit_port));
			psql1.setInt(7, StringUtil.getIntegerValue(gigabit_port_type));
			psql1.setInt(8, StringUtil.getIntegerValue(download_max_lan));
			psql1.setString(9, power);
			psql1.setString(10, terminal_access_time);
			psql1.setInt(11, 0);
			psql1.setInt(12, isSecurityPlugin);
			psql1.setInt(13, securityPlugin);
			psql1.setInt(14, StringUtil.getIntegerValue(wifi_ability));
			psql1.setInt(15, iscloudnet);
		}
		if (HBLT.equals(instArea))
		{
			psql1.setLong(1, max_id);
			psql1.setInt(2, StringUtil.getIntegerValue(device_version_type));
			psql1.setString(3, wifi);
			psql1.setInt(4, StringUtil.getIntegerValue(wifi_frequency));
			psql1.setInt(5, StringUtil.getIntegerValue(download_max_wifi));
			psql1.setInt(6, StringUtil.getIntegerValue(gigabit_port));
			psql1.setInt(7, StringUtil.getIntegerValue(gigabit_port_type));
			psql1.setInt(8, StringUtil.getIntegerValue(download_max_lan));
			psql1.setString(9, power);
			psql1.setString(10, terminal_access_time);
			psql1.setInt(11, 0);
			psql1.setString(12, version_feature);
		}
		if(NMGDX.equals(instArea))
		{
			psql1.setLong(1, max_id);
			psql1.setInt(2, StringUtil.getIntegerValue(device_version_type));
			psql1.setString(3, wifi);
			psql1.setInt(4, gigabitNum);
			psql1.setInt(5, mbitNum);
			psql1.setInt(6, voipNum);
			psql1.setInt(7, StringUtil.getIntegerValue(is_wifi_double));
			psql1.setString(8, fusion_ability);
			psql1.setString(9, terminal_access_method);
			psql1.setString(10, devMaxSpeed);

		}
		psql.setLong(1, max_id);
		psql.setString(2, String.valueOf(vendor));
		psql.setString(3, String.valueOf(device_model));
		psql.setString(4, specversion);
		psql.setString(5, hard_version);
		psql.setString(6, soft_version);
		psql.setLong(7, TimeUtil.getCurrentTime());
		psql.setInt(8, is_check);
		psql.setInt(9, rela_dev_type);
		psql.setLong(10, new Long(typeId));
		String tempIpType = ipType;
		if (!"0".equals(tempIpType) && !"1".equals(tempIpType))
		{
			tempIpType = "1";
		}
		psql.setInt(11, StringUtil.getIntegerValue(tempIpType));
		psql.setLong(12, spec_id);
		psql.setInt(13, StringUtil.getIntegerValue(machineConfig));
		psql.setInt(14, StringUtil.getIntegerValue(startOpenDate));
		psql.setInt(15, StringUtil.getIntegerValue(mbBroadband));
		psql.setInt(16, StringUtil.getIntegerValue(ipType));
		// 仅新疆电信有效
		if (XJDX.equals(instArea)
				|| HBLT.equals(instArea))
		{
			psql.setInt(17, is_multicast);
		}
		// 定版原因，仅对江苏电信有效
		if (JSDX.equals(instArea))
		{
			psql.setInt(17, StringUtil.getIntegerValue(is_awifi));
			psql.setString(18, reason);
			psql.setInt(19, Integer.parseInt(is_qoe));
		}
		// JXDX-ITMS-REQ-20181119-WUWF-001(ITMS平台家庭网关设备维护界面Qoe维护需求
		if (JXDX.equals(instArea))
		{
			psql.setInt(17, Integer.parseInt(is_qoe));
			psql.setInt(18, Integer.parseInt(is_newVersion));
		}
		// JXDX-ITMS-REQ-20181119-WUWF-001(ITMS平台家庭网关设备维护界面Qoe维护需求
		if (GSDX.equals(instArea))
		{
			psql1.setLong(1, max_id);
			psql1.setString(2, String.valueOf(ssid_instancenum));
			psql1.setString(3, String.valueOf(hvoip_port));
			psql1.setInt(4, StringUtil.getIntegerValue(device_version_type));
			return new Object[] {
					jt.batchUpdate(new String[] { psql.getSQL(), psql1.getSQL() })[0],
					max_id };
		}
		//sx_dx
		if (SXDX.equals(instArea))
		{
			psql1.setLong(1, max_id);
			psql1.setString(2, String.valueOf(ssid_instancenum));
			psql1.setString(3, String.valueOf(hvoip_port));
			psql1.setString(4, String.valueOf(hvoip_type));
			psql1.setString(5, String.valueOf(svoip_type));
			psql1.setInt(6, StringUtil.getIntegerValue(device_version_type));
			return new Object[] {
					jt.batchUpdate(new String[] { psql.getSQL(), psql1.getSQL() })[0],
					max_id };
		}

		if (JXDX.equals(instArea)
				|| XJDX.equals(instArea)
				|| HBLT.equals(instArea)
				|| JLDX.equals(instArea)
				|| SDDX.equals(instArea)
				|| NMGDX.equals(instArea))
		{
			return new Object[] {
					jt.batchUpdate(new String[] { psql.getSQL(), psql1.getSQL() })[0],
					max_id };
		}
		else
		{
			return new Object[] { jt.update(psql.getSQL()), max_id };
		}
	}

	/**
	 * 修改设备版本
	 *
	 * @param deviceTypeId
	 *            ID
	 * @param vendor
	 *            厂商
	 * @param device_model
	 *            设备型号
	 * @param specversion
	 *            特定版本
	 * @param hard_version
	 *            硬件型号
	 * @param soft_version
	 *            软件型号
	 * @param is_check
	 *            是否审核
	 * @param rela_dev_type
	 *            设备类型
	 * @param gbBroadband
	 * @param is_newVersion
	 * @param remark
	 * @param res_type
	 * @param res_vendor
	 * @param res_type_id
	 * @param terminal_access_method
	 * @param fusion_ability
	 * @param is_wifi_double
	 * @param devMaxSpeed
	 * @param voipNum
	 * @param mbitNum
	 * @param gigabitNum
	 * @return 操作结果
	 */
	public int updateDevTypeInfo(long deviceTypeId, int vendor, int device_model,
			String specversion, String hard_version, String soft_version, int is_check,
			int rela_dev_type, String typeId, String ipType, String isNormal,
			long spec_id, String mbBroadband, String startOpenDate, String machineConfig,
			String is_awifi, String reason, String editDeviceType, int is_multicast,
			String is_qoe, String is_esurfing, String device_version_type, String wifi,
			String wifi_frequency, int wifi_ability, String download_max_wifi, String gigabit_port,
			String gigabit_port_type, String download_max_lan, String power,
			String terminal_access_time, String gbBroadband,String version_feature,int isSecurityPlugin,
			int securityPlugin, String is_newVersion,int ssid_instancenum,String hvoip_port,
			String hvoip_type,String svoip_type, int gigabitNum, int mbitNum, int voipNum, String devMaxSpeed, 
			String is_wifi_double, String fusion_ability, String terminal_access_method, int iscloudnet)
	{
		logger.warn("入参为{},{},{},{},{},{},{},{},{},{},{}", new Object[] { device_version_type,
				wifi, wifi_frequency, download_max_wifi, gigabit_port, gigabit_port_type,
				download_max_lan, power, terminal_access_time, isSecurityPlugin,securityPlugin});
		if ("0".equals(editDeviceType))
		{
			// 新功能，只更新设备版本表
			// 更新设备版本表
			String sql = null;
			String sql1 = null;
			String sql2 = null;
			if (JSDX.equals(instArea))
			{
				sql = "update tab_devicetype_info set is_check=?,rela_dev_type_id=?, "
						+ "access_style_relay_id = ?,specversion=?,ip_type=?,is_normal=?,spec_id=?,"
						+ "zeroconf=?,versionttime=?,mbbroadband=?,ip_model_type=?,reason=?,is_awifi=?,is_qoe=? where devicetype_id=? ";
			}
			else
			{
				sql = "update tab_devicetype_info set is_check=?,rela_dev_type_id=?, "
						+ "access_style_relay_id = ?,specversion=?,ip_type=?,is_normal=?,spec_id=?,"
						+ "zeroconf=?,versionttime=?,mbbroadband=?,ip_model_type=? where devicetype_id=? ";
				if (XJDX.equals(instArea))
				{
					sql = "update tab_devicetype_info set is_check=?,rela_dev_type_id=?, "
							+ "access_style_relay_id = ?,specversion=?,ip_type=?,is_normal=?,spec_id=?,"
							+ "zeroconf=?,versionttime=?,mbbroadband=?,ip_model_type=?,is_multicast=? where devicetype_id=? ";
					StringBuffer tempSQL = new StringBuffer();
					tempSQL.append("select * from tab_device_version_attribute where devicetype_id="
							+ deviceTypeId + "");
					PrepareSQL psql = new PrepareSQL(tempSQL.toString());
					List<Map> list = new ArrayList<Map>();
					list = jt.queryForList(psql.getSQL());
					if (list != null && list.size() > 0)
					{
						sql1 = "update  tab_device_version_attribute  set  device_version_type=? ,wifi=?,wifi_frequency=?,download_max_wifi=?,gigabit_port=?,gigabit_port_type=?,"
								+"download_max_lan=?,power=?,terminal_access_time=? ,is_tyGate =? ,is_security_plugin=?,security_plugin_type=?,wifi_ability=? where devicetype_id=?";
					}
					else
					{
						sql1 = "insert into tab_device_version_attribute (device_version_type,wifi,wifi_frequency,download_max_wifi,gigabit_port,"
								+ "gigabit_port_type,download_max_lan,power,terminal_access_time,is_tyGate,is_security_plugin,security_plugin_type,"
								+ "wifi_ability,devicetype_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					}
				}
				if (HBLT.equals(instArea))
				{
					sql = "update tab_devicetype_info set is_check=?,rela_dev_type_id=?, "
							+ "access_style_relay_id = ?,specversion=?,ip_type=?,is_normal=?,spec_id=?,"
							+ "zeroconf=?,versionttime=?,mbbroadband=?,ip_model_type=?,is_multicast=? where devicetype_id=? ";
					StringBuffer tempSQL = new StringBuffer();
					tempSQL.append("select * from tab_device_version_attribute where devicetype_id="
							+ deviceTypeId + "");
					PrepareSQL psql = new PrepareSQL(tempSQL.toString());
					List<Map> list = new ArrayList<Map>();
					list = jt.queryForList(psql.getSQL());
					if (list != null && list.size() > 0)
					{
						sql1 = "update  tab_device_version_attribute  set  device_version_type=? ,wifi=?,wifi_frequency=?,download_max_wifi=?,gigabit_port=?,gigabit_port_type=?,download_max_lan=?,power=?,terminal_access_time=? ,is_tyGate =? ,version_feature=? where devicetype_id=?";
					}
					else
					{
						sql1 = "insert into tab_device_version_attribute (device_version_type,wifi,wifi_frequency,download_max_wifi,gigabit_port,gigabit_port_type,download_max_lan,power,terminal_access_time,is_tyGate,version_feature,devicetype_id) values(?,?,?,?,?,?,?,?,?,?,?,?)";
					}
				}



				if (JLDX.equals(instArea) ||
						SDDX.equals(instArea))
				{
					sql = "update tab_devicetype_info set is_check=?,rela_dev_type_id=?, "
							+ "access_style_relay_id = ?,specversion=?,ip_type=?,is_normal=?,spec_id=?,"
							+ "zeroconf=?,versionttime=?,mbbroadband=?,ip_model_type=? where devicetype_id=? ";
					StringBuffer tempSQL = new StringBuffer();
					tempSQL.append("select * from tab_device_version_attribute where devicetype_id="
							+ deviceTypeId + "");
					PrepareSQL psql = new PrepareSQL(tempSQL.toString());
					List<Map> list = new ArrayList<Map>();
					list = jt.queryForList(psql.getSQL());
					if (list != null && list.size() > 0)
					{
						sql1 = "update tab_device_version_attribute  set  device_version_type=?  where devicetype_id=?";
					}
					else
					{
						sql1 = "insert into tab_device_version_attribute (device_version_type,devicetype_id) values(?,?)";
					}
				}
				if (NXDX.equals(instArea))
				{
					sql = "update tab_devicetype_info set is_check=?,rela_dev_type_id=?, "
							+ "access_style_relay_id = ?,specversion=?,ip_type=?,is_normal=?,spec_id=?,"
							+ "zeroconf=?,versionttime=?,mbbroadband=?,ip_model_type=? where devicetype_id=? ";
					StringBuffer tempSQL = new StringBuffer();
					tempSQL.append("select * from tab_device_version_attribute where devicetype_id="
							+ deviceTypeId + "");
					PrepareSQL psql = new PrepareSQL(tempSQL.toString());
					List<Map> list = new ArrayList<Map>();
					list = jt.queryForList(psql.getSQL());
					if (list != null && list.size() > 0)
					{
						sql1 = "update tab_device_version_attribute  set  device_version_type=?  where devicetype_id=?";
					}
					else
					{
						sql1 = "insert into tab_device_version_attribute (device_version_type,devicetype_id) values(?,?)";
					}
				}
				if (JXDX.equals(instArea))
				{
					StringBuffer tempSQL = new StringBuffer();
					tempSQL.append("select * from tab_device_version_attribute where devicetype_id="
							+ deviceTypeId + "");
					PrepareSQL psql = new PrepareSQL(tempSQL.toString());
					List<Map> list = new ArrayList<Map>();
					list = jt.queryForList(psql.getSQL());
					if ("1".equals(is_newVersion)){
						sql2 = "update tab_devicetype_info set is_highversion = 0 where vendor_id ='"+vendor+"' and device_model_id='"+device_model+"' and hardwareversion ='"+hard_version+"'";
					}
					if (list != null && list.size() > 0)
					{
						sql = "update tab_devicetype_info set is_check=?,rela_dev_type_id=?, "
								+ "access_style_relay_id = ?,specversion=?,ip_type=?,is_normal=?,spec_id=?,"
								+ "zeroconf=?,versionttime=?,mbbroadband=?,ip_model_type=?,is_qoe=?,is_highversion=? where devicetype_id=? ";
						sql1 = "update  tab_device_version_attribute  set  is_tyGate=?,gbBroadband=?,device_version_type=? where devicetype_id=?";
					}
					else
					{
						sql1 = "insert into tab_device_version_attribute (is_tyGate,gbBroadband,device_version_type,devicetype_id) values(?,?,?,?)";
					}
				}
				if(NMGDX.equals(instArea))
				{
					StringBuffer tempSQL = new StringBuffer();
					tempSQL.append("select * from tab_device_version_attribute where devicetype_id="
							+ deviceTypeId + "");
					PrepareSQL psql = new PrepareSQL(tempSQL.toString());
					List<Map> list = new ArrayList<Map>();
					list = jt.queryForList(psql.getSQL());
					if (list != null && list.size() > 0)
					{
						sql1 = "update tab_device_version_attribute  set  device_version_type=?,wifi=?,gigabitNum=?,"
								+ " mbitNum = ?,voipNum=?,is_wifi_double = ?,fusion_ability = ?,terminal_access_method = ?,"
								+ " devMaxSpeed = ? where devicetype_id=?";
					}
					else
					{
						sql1 = "insert into tab_device_version_attribute "
								+ " (device_version_type,wifi,gigabitNum,mbitNum,voipNum,is_wifi_double,fusion_ability,"
								+ " terminal_access_method,devMaxSpeed,devicetype_id) "
								+ "values "
								+ "(?,?,?,?,?,?,?,?,?,?)";
					}


				}
				if (GSDX.equals(instArea))
				{

					sql = "update tab_devicetype_info set is_check=?,rela_dev_type_id=?, "
							+ "access_style_relay_id = ?,specversion=?,ip_type=?,is_normal=?,spec_id=?,"
							+ "zeroconf=?,versionttime=?,mbbroadband=?,ip_model_type=? where devicetype_id=? ";
					sql1 = "update tab_device_version_attribute set ssid_instancenum=?,hvoip_port=? ,device_version_type=? where devicetype_id=?";
				}
				//sx_dx
				if (SXDX.equals(instArea))
				{

					sql = "update tab_devicetype_info set is_check=?,rela_dev_type_id=?, "
							+ "access_style_relay_id = ?,specversion=?,ip_type=?,is_normal=?,spec_id=?,"
							+ "zeroconf=?,versionttime=?,mbbroadband=?,ip_model_type=? where devicetype_id=? ";
					sql1 = "update tab_device_version_attribute set ssid_instancenum=?,hvoip_port=? ,hvoip_type=?,svoip_type=? ,device_version_type=? where devicetype_id=?";
				}
				if(AHLT.equals(instArea)){
					sql = "update tab_devicetype_info set is_check=?,rela_dev_type_id=?, "
							+ "access_style_relay_id = ?,specversion=?,ip_type=?,is_normal=?,spec_id=?,"
							+ "zeroconf=?,versionttime=?,mbbroadband=?,ip_model_type=?,is_qoe=? where devicetype_id=? ";
					StringBuffer tempSQL = new StringBuffer();
					tempSQL.append("select * from tab_device_version_attribute where devicetype_id="
							+ deviceTypeId + "");
					PrepareSQL psql = new PrepareSQL(tempSQL.toString());
					List<Map> list = new ArrayList<Map>();
					list = jt.queryForList(psql.getSQL());
					if (list != null && list.size() > 0)
					{
						sql1 = "update tab_device_version_attribute  set  device_version_type=?  where devicetype_id=?";
					}
					else
					{
						sql1 = "insert into tab_device_version_attribute (device_version_type,devicetype_id,is_tygate) values(?,?,0)";
					}
				}
			}
			PrepareSQL psql = new PrepareSQL(sql);
			PrepareSQL psql1 = new PrepareSQL(sql1);
			PrepareSQL psql2 = new PrepareSQL(sql2);
			if (JXDX.equals(instArea))
			{
				psql1.setInt(1, StringUtil.getIntegerValue(is_esurfing));
				psql1.setInt(2, StringUtil.getIntegerValue(gbBroadband));
				psql1.setInt(3, StringUtil.getIntegerValue(device_version_type));
				psql1.setLong(4, deviceTypeId);
			}
			if (JLDX.equals(instArea) ||
					SDDX.equals(instArea)
					|| AHLT.equals(instArea))
			{
				psql1.setInt(1, StringUtil.getIntegerValue(device_version_type));
				psql1.setLong(2, deviceTypeId);
			}
			if (NXDX.equals(instArea))
			{
				psql1.setInt(1, StringUtil.getIntegerValue(device_version_type));
				psql1.setLong(2, deviceTypeId);
			}
			if (XJDX.equals(instArea))
			{
				psql1.setInt(1, StringUtil.getIntegerValue(device_version_type));
				psql1.setInt(2, StringUtil.getIntegerValue(wifi));
				psql1.setInt(3, StringUtil.getIntegerValue(wifi_frequency));
				psql1.setInt(4, StringUtil.getIntegerValue(download_max_wifi));
				psql1.setInt(5, StringUtil.getIntegerValue(gigabit_port));
				psql1.setInt(6, StringUtil.getIntegerValue(gigabit_port_type));
				psql1.setInt(7, StringUtil.getIntegerValue(download_max_lan));
				psql1.setString(8, power);
				psql1.setString(9, terminal_access_time);
				psql1.setInt(10, 0);
				psql1.setLong(11, isSecurityPlugin);
				psql1.setLong(12, securityPlugin);
				psql1.setInt(13, wifi_ability);
				psql1.setLong(15, deviceTypeId);
			}
			if (HBLT.equals(instArea))
			{
				psql1.setInt(1, StringUtil.getIntegerValue(device_version_type));
				psql1.setInt(2, StringUtil.getIntegerValue(wifi));
				psql1.setInt(3, StringUtil.getIntegerValue(wifi_frequency));
				psql1.setInt(4, StringUtil.getIntegerValue(download_max_wifi));
				psql1.setInt(5, StringUtil.getIntegerValue(gigabit_port));
				psql1.setInt(6, StringUtil.getIntegerValue(gigabit_port_type));
				psql1.setInt(7, StringUtil.getIntegerValue(download_max_lan));
				psql1.setString(8, power);
				psql1.setString(9, terminal_access_time);
				psql1.setInt(10, 0);
				psql1.setString(11, version_feature);
				psql1.setLong(12, deviceTypeId);
			}
			if (NMGDX.equals(instArea))
			{
				psql1.setInt(1, StringUtil.getIntegerValue(device_version_type));
				psql1.setString(2, wifi);
				psql1.setInt(3, gigabitNum);
				psql1.setInt(4, StringUtil.getIntegerValue(mbitNum));
				psql1.setInt(5, StringUtil.getIntegerValue(voipNum));
				psql1.setInt(6,StringUtil.getIntegerValue(is_wifi_double));
				psql1.setString(7, fusion_ability);
				psql1.setString(8, terminal_access_method);
				psql1.setString(9, devMaxSpeed);
				psql1.setLong(10, deviceTypeId);
			}
			psql.setInt(1, is_check);
			psql.setInt(2, rela_dev_type);
			psql.setLong(3, new Long(typeId));
			psql.setString(4, specversion);
			String tempIpType = ipType;
			if (!"0".equals(tempIpType) && !"1".equals(tempIpType))
			{
				tempIpType = "1";
			}
			psql.setInt(5, StringUtil.getIntegerValue(tempIpType));
			psql.setInt(6, StringUtil.getIntegerValue(isNormal));
			psql.setLong(7, spec_id);
			psql.setInt(8, Integer.parseInt(machineConfig));
			psql.setInt(9, Integer.parseInt(startOpenDate));
			psql.setInt(10, Integer.parseInt(mbBroadband));
			psql.setInt(11, StringUtil.getIntegerValue(ipType));
			if (JSDX.equals(instArea))
			{
				psql.setString(12, reason);
				psql.setInt(13, Integer.parseInt(is_awifi));
				psql.setLong(14, Integer.parseInt(is_qoe));
				psql.setLong(15, deviceTypeId);
			}
			else if (XJDX.equals(instArea))
			{
				psql.setInt(12, is_multicast);
				psql.setLong(13, deviceTypeId);
			}
			else if (HBLT.equals(instArea))
			{
				psql.setInt(12, is_multicast);
				psql.setLong(13, deviceTypeId);
			}
			else if (JXDX.equals(instArea))
			{
				psql.setLong(12, Integer.parseInt(is_qoe));
				psql.setInt(13, StringUtil.getIntegerValue(is_newVersion));
				psql.setLong(14, deviceTypeId);
			}else if (SXDX.equals(instArea))
			{

				psql.setLong(12, deviceTypeId);

				String selectSql="select count(1) from tab_device_version_attribute where deviceType_id ='"+deviceTypeId+"'";

				int count = jt.queryForInt(selectSql) ;
				if(count<=0) {

					String  insertSql = "insert into tab_device_version_attribute(devicetype_id,ssid_instancenum,hvoip_port,hvoip_type,svoip_type,device_version_type)values('"+deviceTypeId+"','"+ssid_instancenum+"','"+hvoip_port+"','"+hvoip_type+"','"+svoip_type+"',"+StringUtil.getIntegerValue(device_version_type)+")";
					jt.execute(insertSql);

				}
				psql1.setInt(1, ssid_instancenum);
				psql1.setString(2, hvoip_port);
				psql1.setString(3, hvoip_type);
				psql1.setString(4, svoip_type);
				psql1.setInt(5, StringUtil.getIntegerValue(device_version_type));
				psql1.setLong(6, deviceTypeId);


			}
			else if (GSDX.equals(instArea))
			{

				psql.setLong(12, deviceTypeId);

				String selectSql="select count(1) from tab_device_version_attribute where deviceType_id ='"+deviceTypeId+"'";

				int count = jt.queryForInt(selectSql) ;
				if(count<=0) {

					String  insertSql = "insert into tab_device_version_attribute(devicetype_id,ssid_instancenum,hvoip_port)values('"+deviceTypeId+"','"+ssid_instancenum+"','"+hvoip_port+"')";
					jt.execute(insertSql);

				}
				psql1.setInt(1, ssid_instancenum);
				psql1.setString(2, hvoip_port);
				psql1.setInt(3, StringUtil.getIntegerValue(device_version_type));
				psql1.setLong(4, deviceTypeId);

			}else if(AHLT.equals(instArea)){
				psql.setString(12, is_qoe);
				psql.setLong(13, deviceTypeId);
			} else
			{
				psql.setLong(12, deviceTypeId);
			}
			/*
			 * if("jx_dx".equals(instArea))
			 * { return jt.batchUpdate(new String[] { psql.getSQL(), psql1.getSQL() })[0];
			 * } else { return jt.batchUpdate(new String[] { psql.getSQL()})[0]; }
			 */
			List sqllist = new ArrayList();
			if (!StringUtil.IsEmpty(sql2) && "1".equals(is_newVersion)
					&& JXDX.equals(instArea))
			{
				sqllist.add(psql2.getSQL());
			}
			if (!StringUtil.IsEmpty(sql))
			{
				sqllist.add(psql.getSQL());
			}
			if (!StringUtil.IsEmpty(sql1))
			{
				sqllist.add(psql1.getSQL());
			}
			String[] strings = new String[sqllist.size()];
			sqllist.toArray(strings);
			return jt.batchUpdate(strings)[0];
		}
		else if ("1".equals(editDeviceType))
		{
			// 保持原有功能，更新设备版本表，更新设备表
			// 更新设备版本表
			String sql = null;
			String sql1 = null;
			String sql3 = null;
			if (JSDX.equals(instArea))
			{
				sql = "update tab_devicetype_info set is_check=?,"
						+ "access_style_relay_id = ?,specversion=?,ip_type=?,is_normal=?,spec_id=?,"
						+ "zeroconf=?,versionttime=?,mbbroadband=?,ip_model_type=?,rela_dev_type_id=?,reason=?,is_awifi=?,is_qoe=? where devicetype_id=? ";
			}
			else
			{
				sql = "update tab_devicetype_info set is_check=?,"
						+ "access_style_relay_id = ?,specversion=?,ip_type=?,is_normal=?,spec_id=?,"
						+ "zeroconf=?,versionttime=?,mbbroadband=?,ip_model_type=?,rela_dev_type_id=? where devicetype_id=? ";
				if (XJDX.equals(instArea))
				{
					sql = "update tab_devicetype_info set is_check=?,"
							+ "access_style_relay_id = ?,specversion=?,ip_type=?,is_normal=?,spec_id=?,"
							+ "zeroconf=?,versionttime=?,mbbroadband=?,ip_model_type=?,rela_dev_type_id=?,is_multicast=? where devicetype_id=? ";
					StringBuffer tempSQL = new StringBuffer();
					tempSQL.append("select * from tab_device_version_attribute where devicetype_id="
							+ deviceTypeId + "");
					PrepareSQL psql = new PrepareSQL(tempSQL.toString());
					List<Map> list = new ArrayList<Map>();
					list = jt.queryForList(psql.getSQL());
					logger.warn("list====" + list);
					if (list != null && list.size() > 0)
					{
						sql1 = "update  tab_device_version_attribute  set  device_version_type=? ,wifi=?,wifi_frequency=?,download_max_wifi=?,gigabit_port=?,gigabit_port_type=?,download_max_lan=?,power=?,terminal_access_time=?,is_tyGate=?,is_security_plugin=?,security_plugin_type=?,wifi_ability=?,iscloudnet=? where devicetype_id=?";
					}
					else
					{
						sql1 = "insert into tab_device_version_attribute (device_version_type,wifi,wifi_frequency,download_max_wifi,gigabit_port,gigabit_port_type,download_max_lan,power,terminal_access_time,is_tyGate,is_security_plugin,security_plugin_type,wifi_ability,iscloudnet,devicetype_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					}
				}

				if (HBLT.equals(instArea))
				{
					sql = "update tab_devicetype_info set is_check=?,"
							+ "access_style_relay_id = ?,specversion=?,ip_type=?,is_normal=?,spec_id=?,"
							+ "zeroconf=?,versionttime=?,mbbroadband=?,ip_model_type=?,rela_dev_type_id=?,is_multicast=? where devicetype_id=? ";
					StringBuffer tempSQL = new StringBuffer();
					tempSQL.append("select * from tab_device_version_attribute where devicetype_id="
							+ deviceTypeId + "");
					PrepareSQL psql = new PrepareSQL(tempSQL.toString());
					List<Map> list = new ArrayList<Map>();
					list = jt.queryForList(psql.getSQL());
					logger.warn("list====" + list);
					if (list != null && list.size() > 0)
					{
						sql1 = "update  tab_device_version_attribute  set  device_version_type=? ,wifi=?,wifi_frequency=?,download_max_wifi=?,gigabit_port=?,gigabit_port_type=?,download_max_lan=?,power=?,terminal_access_time=?,is_tyGate=? ,version_feature=? where devicetype_id=?";
					}
					else
					{
						sql1 = "insert into tab_device_version_attribute (device_version_type,wifi,wifi_frequency,download_max_wifi,gigabit_port,gigabit_port_type,download_max_lan,power,terminal_access_time,is_tyGate, version_feature,devicetype_id) values(?,?,?,?,?,?,?,?,?,?,?,?)";
					}
				}

				if (JLDX.equals(instArea) ||
						SDDX.equals(instArea)
						|| AHLT.equals(instArea))
				{
					sql = "update tab_devicetype_info set is_check=?,"
							+ "access_style_relay_id = ?,specversion=?,ip_type=?,is_normal=?,spec_id=?,"
							+ "zeroconf=?,versionttime=?,mbbroadband=?,ip_model_type=?,rela_dev_type_id=? where devicetype_id=? ";
					StringBuffer tempSQL = new StringBuffer();
					tempSQL.append("select * from tab_device_version_attribute where devicetype_id="
							+ deviceTypeId + "");
					PrepareSQL psql = new PrepareSQL(tempSQL.toString());
					List<Map> list = new ArrayList<Map>();
					list = jt.queryForList(psql.getSQL());
					logger.warn("list====" + list);
					if (list != null && list.size() > 0)
					{
						sql1 = "update tab_device_version_attribute set device_version_type=? where devicetype_id=?";
					}
					else
					{
						sql1 = "insert into tab_device_version_attribute (device_version_type, devicetype_id) values(?,?)";
					}
				}
				if (NXDX.equals(instArea))
				{
					sql = "update tab_devicetype_info set is_check=?,"
							+ "access_style_relay_id = ?,specversion=?,ip_type=?,is_normal=?,spec_id=?,"
							+ "zeroconf=?,versionttime=?,mbbroadband=?,ip_model_type=?,rela_dev_type_id=? where devicetype_id=? ";
					StringBuffer tempSQL = new StringBuffer();
					tempSQL.append("select * from tab_device_version_attribute where devicetype_id="
							+ deviceTypeId + "");
					PrepareSQL psql = new PrepareSQL(tempSQL.toString());
					List<Map> list = new ArrayList<Map>();
					list = jt.queryForList(psql.getSQL());
					logger.warn("list====" + list);
					if (list != null && list.size() > 0)
					{
						sql1 = "update tab_device_version_attribute set device_version_type=? where devicetype_id=?";
					}
					else
					{
						sql1 = "insert into tab_device_version_attribute (device_version_type, devicetype_id) values(?,?)";
					}
				}
				if (JXDX.equals(instArea))
				{
					StringBuffer tempSQL = new StringBuffer();
					tempSQL.append("select * from tab_device_version_attribute where devicetype_id="
							+ deviceTypeId + "");
					PrepareSQL psql = new PrepareSQL(tempSQL.toString());
					List<Map> list = new ArrayList<Map>();
					list = jt.queryForList(psql.getSQL());
					if ("1".equals(is_newVersion)){
						sql3 = "update tab_devicetype_info set is_highversion = 0 where vendor_id ='"+vendor+"' and device_model_id='"+device_model+"' and hardwareversion ='"+hard_version+"'";
					}
					if (list != null && list.size() > 0)
					{
						sql = "update tab_devicetype_info set is_check=?,"
								+ "access_style_relay_id = ?,specversion=?,ip_type=?,is_normal=?,spec_id=?,"
								+ "zeroconf=?,versionttime=?,mbbroadband=?,ip_model_type=?,rela_dev_type_id=?,is_qoe=?,is_highversion=?  where devicetype_id=? ";
						sql1 = "update  tab_device_version_attribute  set  is_tyGate=?,gbBroadband=?,device_version_type=? where devicetype_id=?";
					}
					else
					{
						sql1 = "insert into tab_device_version_attribute (is_tyGate,gbBroadband,device_version_type,devicetype_id) values(?,?,?,?)";
					}
				}
				if(NMGDX.equals(instArea))
				{
					StringBuffer tempSQL = new StringBuffer();
					// teledb
					if (DBUtil.GetDB() == 3) {
						tempSQL.append("select devicetype_id from tab_device_version_attribute where devicetype_id="
								+ deviceTypeId + "");
					}
					else {
						tempSQL.append("select * from tab_device_version_attribute where devicetype_id="
								+ deviceTypeId + "");
					}
					PrepareSQL psql = new PrepareSQL(tempSQL.toString());
					List<Map> list = new ArrayList<Map>();
					list = jt.queryForList(psql.getSQL());
					if (list != null && list.size() > 0)
					{
						sql1 = "update tab_device_version_attribute  set  device_version_type=?,wifi=?,gigabitNum=?,"
								+ " mbitNum = ?,voipNum=?,is_wifi_double = ?,fusion_ability = ?,terminal_access_method = ?,"
								+ " devMaxSpeed = ? where devicetype_id=?";
					}
					else
					{
						sql1 = "insert into tab_device_version_attribute "
								+ " (device_version_type,wifi,gigabitNum,mbitNum,voipNum,is_wifi_double,fusion_ability,"
								+ " terminal_access_method,devMaxSpeed,devicetype_id) "
								+ "values "
								+ "(?,?,?,?,?,?,?,?,?,?)";
					}
				}
				if (GSDX.equals(instArea))
				{
					sql = "update tab_devicetype_info set is_check=?,"
						+ "access_style_relay_id = ?,specversion=?,ip_type=?,is_normal=?,spec_id=?,"
						+ "zeroconf=?,versionttime=?,mbbroadband=?,ip_model_type=?,rela_dev_type_id=?  where devicetype_id=? ";
					sql1 = "update tab_device_version_attribute set ssid_instancenum=?,hvoip_port=?,device_version_type=? where devicetype_id=?";
				}
				//sx_dx
				if (SXDX.equals(instArea))
				{
					sql = "update tab_devicetype_info set is_check=?,"
						+ "access_style_relay_id = ?,specversion=?,ip_type=?,is_normal=?,spec_id=?,"
						+ "zeroconf=?,versionttime=?,mbbroadband=?,ip_model_type=?,rela_dev_type_id=?  where devicetype_id=? ";
					sql1 = "update tab_device_version_attribute set ssid_instancenum=?,hvoip_port=?,hvoip_type=?,svoip_type=?,device_version_type=? where devicetype_id=?";
				}
			}
			PrepareSQL psql = new PrepareSQL(sql);
			PrepareSQL psql1 = new PrepareSQL(sql1);
			PrepareSQL psql3 = new PrepareSQL(sql3);
			if (JXDX.equals(instArea))
			{
				psql1.setInt(1, StringUtil.getIntegerValue(is_esurfing));
				psql1.setInt(2, StringUtil.getIntegerValue(gbBroadband));
				psql1.setInt(3, StringUtil.getIntegerValue(device_version_type));
				psql1.setLong(4, deviceTypeId);
			}
			if (JLDX.equals(instArea) ||
					SDDX.equals(instArea)
					|| AHLT.equals(instArea))
			{
				psql1.setInt(1, StringUtil.getIntegerValue(device_version_type));
				psql1.setLong(2, deviceTypeId);
			}
			if (NXDX.equals(instArea))
			{
				psql1.setInt(1, StringUtil.getIntegerValue(device_version_type));
				psql1.setLong(2, deviceTypeId);
			}
			if (XJDX.equals(instArea))
			{
				psql1.setInt(1, StringUtil.getIntegerValue(device_version_type));
				psql1.setInt(2, StringUtil.getIntegerValue(wifi));
				psql1.setInt(3, StringUtil.getIntegerValue(wifi_frequency));
				psql1.setInt(4, StringUtil.getIntegerValue(download_max_wifi));
				psql1.setInt(5, StringUtil.getIntegerValue(gigabit_port));
				psql1.setInt(6, StringUtil.getIntegerValue(gigabit_port_type));
				psql1.setInt(7, StringUtil.getIntegerValue(download_max_lan));
				psql1.setString(8, power);
				psql1.setString(9, terminal_access_time);
				psql1.setInt(10, 0);
				psql1.setLong(11, isSecurityPlugin);
				psql1.setLong(12, securityPlugin);
				psql1.setInt(13, wifi_ability);
				psql1.setInt(14, iscloudnet);
				psql1.setLong(15, deviceTypeId);
			}
			if (HBLT.equals(instArea))
			{
				psql1.setInt(1, StringUtil.getIntegerValue(device_version_type));
				psql1.setInt(2, StringUtil.getIntegerValue(wifi));
				psql1.setInt(3, StringUtil.getIntegerValue(wifi_frequency));
				psql1.setInt(4, StringUtil.getIntegerValue(download_max_wifi));
				psql1.setInt(5, StringUtil.getIntegerValue(gigabit_port));
				psql1.setInt(6, StringUtil.getIntegerValue(gigabit_port_type));
				psql1.setInt(7, StringUtil.getIntegerValue(download_max_lan));
				psql1.setString(8, power);
				psql1.setString(9, terminal_access_time);
				psql1.setInt(10, 0);
				psql1.setString(11, version_feature);
				psql1.setLong(12, deviceTypeId);
			}
			if (NMGDX.equals(instArea))
			{
				psql1.setInt(1, StringUtil.getIntegerValue(device_version_type));
				psql1.setString(2, wifi);
				psql1.setInt(3, gigabitNum);
				psql1.setInt(4, StringUtil.getIntegerValue(mbitNum));
				psql1.setInt(5, StringUtil.getIntegerValue(voipNum));
				psql1.setInt(6,StringUtil.getIntegerValue(is_wifi_double));
				psql1.setString(7, fusion_ability);
				psql1.setString(8, terminal_access_method);
				psql1.setString(9, devMaxSpeed);
				psql1.setLong(10, deviceTypeId);
			}
			psql.setInt(1, is_check);
			psql.setLong(2, new Long(typeId));
			psql.setString(3, specversion);
			String tempIpType = ipType;
			if (!"0".equals(tempIpType) && !"1".equals(tempIpType))
			{
				tempIpType = "1";
			}
			psql.setInt(4, StringUtil.getIntegerValue(tempIpType));
			psql.setInt(5, StringUtil.getIntegerValue(isNormal));
			psql.setLong(6, spec_id);
			psql.setInt(7, Integer.parseInt(machineConfig));
			psql.setInt(8, Integer.parseInt(startOpenDate));
			psql.setInt(9, Integer.parseInt(mbBroadband));
			psql.setInt(10, StringUtil.getIntegerValue(ipType));
			psql.setInt(11, rela_dev_type);
			if (JSDX.equals(instArea))
			{
				psql.setString(12, reason);
				psql.setInt(13, Integer.parseInt(is_awifi));
				psql.setLong(14, Integer.parseInt(is_qoe));
				psql.setLong(15, deviceTypeId);
			}
			else if (XJDX.equals(instArea)
					|| HBLT.equals(instArea))
			{
				psql.setInt(12, is_multicast);
				psql.setLong(13, deviceTypeId);
			}
			else if (JXDX.equals(instArea))
			{
				psql.setLong(12, Integer.parseInt(is_qoe));
				psql.setInt(13, StringUtil.getIntegerValue(is_newVersion));
				psql.setLong(14, deviceTypeId);
			}
			//sx_dx
			else if (SXDX.equals(instArea))
			{
				psql.setLong(12, deviceTypeId);
				String selectSql = "select count(1) from tab_device_version_attribute where deviceType_id ='"
						+ deviceTypeId + "'";
				int count = jt.queryForInt(selectSql);
				if (count <= 0)
				{
					String insertSql = "insert into tab_device_version_attribute(devicetype_id,ssid_instancenum,hvoip_port,hvoip_type,svoip_type,device_version_type)values('"
							+ deviceTypeId + "','" + ssid_instancenum + "','" + hvoip_port + "','" + hvoip_type + "','" + svoip_type + "','" + StringUtil.getIntegerValue(device_version_type)
							+ "')";
					jt.execute(insertSql);
				}
				psql1.setInt(1, ssid_instancenum);
				psql1.setString(2, hvoip_port);
				psql1.setString(3, hvoip_type);
				psql1.setString(4, svoip_type);
				psql1.setInt(5, StringUtil.getIntegerValue(device_version_type));
				psql1.setLong(6, deviceTypeId);
			}else if (GSDX.equals(instArea))
			{
				psql.setLong(12, deviceTypeId);
				String selectSql = "select count(1) from tab_device_version_attribute where deviceType_id ='"
						+ deviceTypeId + "'";
				int count = jt.queryForInt(selectSql);
				if (count <= 0)
				{
					String insertSql = "insert into tab_device_version_attribute(devicetype_id,ssid_instancenum,hvoip_port,device_version_type)values('"
							+ deviceTypeId + "','" + ssid_instancenum + "','" + hvoip_port + "','" + StringUtil.getIntegerValue(device_version_type)
							+ "')";
					jt.execute(insertSql);
				}
				psql1.setInt(1, ssid_instancenum);
				psql1.setString(2, hvoip_port);
				psql1.setInt(3, StringUtil.getIntegerValue(device_version_type));
				psql1.setLong(4, deviceTypeId);
			}
			else
			{
				psql.setLong(12, deviceTypeId);
			}
			String tempSQL = "select type_name from gw_dev_type where type_id='"
					+ rela_dev_type + "'";
			PrepareSQL temppsql = new PrepareSQL(tempSQL);
			temppsql.getSQL();
			List list = jt.queryForList(tempSQL);
			Map map = (Map) list.get(0);
			String tempTypeName = (String) map.get("type_name");
			String sql2 = "update tab_gw_device set device_type='" + tempTypeName
					+ "' where devicetype_id=?";
			PrepareSQL psql2 = new PrepareSQL(sql2);
			psql2.setLong(1, deviceTypeId);
			/*
			 * if("jx_dx".equals(instArea))
			 * { return jt.batchUpdate(new String[] { psql.getSQL(),
			 * psql2.getSQL(),psql1.getSQL() })[0]; } else { return jt.batchUpdate(new
			 * String[] { psql.getSQL(), psql2.getSQL()})[0]; }
			 */
			List sqllist = new ArrayList();
			if (!StringUtil.IsEmpty(sql3) && "1".equals(is_newVersion)
					&& JXDX.equals(instArea))
			{
				sqllist.add(psql3.getSQL());
			}
			if (!StringUtil.IsEmpty(sql))
			{
				sqllist.add(psql.getSQL());
			}
			if (!StringUtil.IsEmpty(sql1))
			{
				sqllist.add(psql1.getSQL());
			}
			if (!StringUtil.IsEmpty(sql2))
			{
				sqllist.add(psql2.getSQL());
			}
			String[] strings = new String[sqllist.size()];
			sqllist.toArray(strings);
			return jt.batchUpdate(strings)[0];
		}
		return 0;
	}

	/**
	 * 保存设备类型
	 *
	 * @return
	 */
	public int updateDeviceType(long deviceTypeId, int rela_dev_type_id)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("update tab_devicetype_info set rela_dev_type_id=? ");
		psql.append(" where devicetype_id=? ");
		psql.setInt(1, rela_dev_type_id);
		psql.setLong(2, deviceTypeId);
		PrepareSQL psql1 = new PrepareSQL();
		psql1.append("select type_name from gw_dev_type where type_id=? ");
		psql1.setString(1, rela_dev_type_id + "");
		List list = jt.queryForList(psql1.getSQL());
		Map map = (Map) list.get(0);
		String tempTypeName = (String) map.get("type_name");
		PrepareSQL psql2 = new PrepareSQL();
		psql2.append("update tab_gw_device set device_type=? where devicetype_id=? ");
		psql2.setString(1, tempTypeName);
		psql2.setLong(2, deviceTypeId);
		return jt.batchUpdate(new String[] { psql.getSQL(), psql2.getSQL() })[0];
	}

	/**
	 * 更新设备版本类型
	 * @param deviceTypeId
	 * @param devVersionTypeId
	 * @return
	 */
	public int updateDevVersionType(long deviceTypeId, int devVersionTypeId){
		PrepareSQL psql = new PrepareSQL();
		psql.append("update tab_device_version_attribute set device_version_type=? ");
		psql.append(" where devicetype_id=? ");
		psql.setInt(1, devVersionTypeId);
		psql.setLong(2, deviceTypeId);
		return jt.update(psql.getSQL());
	}

	public void updateIsCheck(long id)
	{
		logger.debug("updateIsCheck({})", new Object[] { id });
		String sql = "update tab_devicetype_info set is_check=1 where devicetype_id=?";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setLong(1, id);
		jt.update(psql.getSQL());
	}

	/**
	 * 添加端口和设备协议
	 *
	 * @param
	 * @return 操作结果
	 */
	public int addPortAddServertype(String portInfo, long acc_oid, String servertype)
	{
		long max_id = DataSetBean.getMaxId("tab_devicetype_info", "devicetype_id");
		if (!servertype.equals(""))
		{
			String[] allServertype = servertype.split("@");
			String[] sql = new String[allServertype.length];
			for (int i = 0; i < allServertype.length; i++)
			{
				sql[i] = "insert into tab_devicetype_info_servertype (devicetype_id,server_type,time,acc_oid) values(?,?,?,?)";
				PrepareSQL psql = new PrepareSQL(sql[i]);
				psql.setLong(1, max_id - 1);
				psql.setInt(2, Integer.parseInt(allServertype[i]));
				psql.setLong(3, System.currentTimeMillis() / 1000);
				psql.setLong(4, acc_oid);
				sql[i] = psql.getSQL();
			}
			jt.batchUpdate(sql);
		}
		String[] port = new String[4];
		if (!"".equals(portInfo))
		{
			String[] AllPort = portInfo.split("#");
			String[] sql = new String[AllPort.length];
			for (int i = 0; i < AllPort.length; i++)
			{
				port = AllPort[i].split("@");
				sql[i] = "insert into tab_devicetype_info_port(devicetype_id,port_name,port_dir,"
						+ "port_type,port_desc,add_time,acc_oid) values(?,?,?,?,?,?,?)";
				PrepareSQL psq = new PrepareSQL(sql[i]);
				psq.setLong(1, max_id - 1);
				psq.setString(2, port[0]);
				psq.setString(3, port[1]);
				psq.setInt(4, Integer.parseInt(port[2]));
				if (port.length > 3)
				{
					psq.setString(5, port[3]);
				}
				else
				{
					psq.setString(5, "");
				}
				psq.setLong(6, System.currentTimeMillis() / 1000);
				psq.setLong(7, acc_oid);
				sql[i] = psq.getSQL();
			}
			return jt.batchUpdate(sql)[0];
		}
		else
		{
			return 0;
		}
	}

	/**
	 * 修改端口和设备协议
	 *
	 * @param
	 * @return 操作结果
	 */
	public int updatePortAddServertype(long deviceTypeId, String portInfo, long acc_oid,
			String servertype)
	{
		// long max_id = DataSetBean.getMaxId("tab_devicetype_info",
		// "devicetype_id") - 1;
		String delSqlForPort = "delete  from tab_devicetype_info_port  where devicetype_id="
				+ deviceTypeId;
		String delSqlForType = "delete  from tab_devicetype_info_servertype   where devicetype_id="
				+ deviceTypeId;
		String[] del = new String[2];
		PrepareSQL psql1 = new PrepareSQL(delSqlForPort);
		PrepareSQL psql2 = new PrepareSQL(delSqlForType);
		del[0] = psql1.getSQL();
		del[1] = psql2.getSQL();
		jt.batchUpdate(del);
		;
		if (!servertype.equals(""))
		{
			String[] allServertype = servertype.split("@");
			String[] sql = new String[allServertype.length];
			for (int i = 0; i < allServertype.length; i++)
			{
				sql[i] = "insert into tab_devicetype_info_servertype (devicetype_id,server_type,time,acc_oid) values(?,?,?,?)";
				PrepareSQL psql = new PrepareSQL(sql[i]);
				psql.setLong(1, deviceTypeId);
				psql.setInt(2, Integer.parseInt(allServertype[i]));
				psql.setLong(3, System.currentTimeMillis() / 1000);
				psql.setLong(4, acc_oid);
				sql[i] = psql.getSQL();
			}
			jt.batchUpdate(sql);
		}
		if (!"".equals(portInfo))
		{
			String[] AllPort = portInfo.split("#");
			String[] port = new String[4];
			String[] sql = new String[AllPort.length];
			for (int i = 0; i < AllPort.length; i++)
			{
				port = AllPort[i].split("@");
				sql[i] = "insert into tab_devicetype_info_port(devicetype_id,port_name,port_dir,port_type,port_desc,add_time,acc_oid) values(?,?,?,?,?,?,?)";
				PrepareSQL psql = new PrepareSQL(sql[i]);
				psql.setLong(1, deviceTypeId);
				psql.setString(2, port[0]);
				psql.setString(3, port[1]);
				psql.setInt(4, Integer.parseInt(port[2]));
				if (port.length > 3)
				{
					psql.setString(5, port[3]);
				}
				else
				{
					psql.setString(5, "");
				}
				psql.setLong(6, System.currentTimeMillis() / 1000);
				psql.setLong(7, acc_oid);
				sql[i] = psql.getSQL();
			}
			return jt.batchUpdate(sql)[0];
		}
		else
		{
			return 0;
		}
	}

	/**
	 * @param deviceModel
	 * @return
	 */
	public int isNormalVersion(int deviceModel)
	{
		StringBuffer sb = new StringBuffer("select count(1)  from tab_devicetype_info a "
				+ " where a.is_normal = 1 and a.device_model_id='" + deviceModel + "'");
		PrepareSQL psql = new PrepareSQL(sb.toString());
		return jt.queryForInt(psql.getSQL());
	}

	/**
	 * 查询设备类型
	 */
	public List<Map<String, String>> getGwDevType()
	{
		PrepareSQL psql = new PrepareSQL("select type_id,type_name from gw_dev_type");
		if(SXLT.equals(instArea)){
			psql = new PrepareSQL("select type_id, DECODE(type_name,'e8-b','adsl(e8-b)','e8-c','光纤(e8-c)',type_name) as type_name from gw_dev_type");
		}
		return jt.queryForList(psql.getSQL());
	}

	public List<Map> queryByVendorIdAndDeviceModelId(int vendor, int device_model)
	{
		String sql = "select devicetype_id,softwareversion,hardwareversion from tab_devicetype_info where vendor_id = '"
				+ vendor
				+ "' and device_model_id = '"
				+ device_model
				+ "' order by softwareversion asc";
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForList(psql.getSQL());
	}

	public int insertGwSoftUpgradeTempMap(Integer relationType,
			long oldVersionDeviceTypeId, Long deviceTypeId)
	{
		String sql = "insert into gw_soft_upgrade_temp_map(temp_id,devicetype_id_old,devicetype_id) values(?,?,?)";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setLong(1, relationType);
		psql.setLong(2, oldVersionDeviceTypeId);
		psql.setLong(3, deviceTypeId);
		sql = psql.getSQL();
		return jt.update(sql);
	}

	/**
	 * 通过devicetype_id， 查询该型号支持的协议
	 *
	 * @param deviceTypeId
	 * @return
	 */
	public List<Map> queryDevicetypeInfoServertypeByDeviceTypeId(Long deviceTypeId)
	{
		String sql = "select server_type from tab_devicetype_info_servertype where devicetype_id = "
				+ deviceTypeId;
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 通过devicetype_id 单条查询tab_devicetype_info
	 *
	 * @param deviceTypeId
	 * @return
	 */
	public Map queryByDeviceTypeId(long deviceTypeId)
	{
		String sql = "select c.vendor_add, b.device_model, a.specversion, a.hardwareversion, a.softwareversion, "
				+ "a.reason, a.versionttime, a.ip_type, a.mbbroadband,a.zeroconf "
				+ "from tab_devicetype_info a left join gw_device_model b on a.device_model_id=b.device_model_id "
				+ "left join tab_vendor c on a.vendor_id=c.vendor_id where devicetype_id = "
				+ deviceTypeId;
		PrepareSQL psql = new PrepareSQL(sql);
		List list = jt.queryForList(psql.getSQL());
		if (list != null && !list.isEmpty())
		{
			return (Map) list.get(0);
		}
		return null;
	}

	public void deleteGwSoftUpgradeTempMapByTempIdAndOldDeviceTypeId(
			Integer relationType, long id)
	{
		String sql = "delete from gw_soft_upgrade_temp_map where temp_id = ? and devicetype_id_old = ?";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setInt(1, relationType);
		psql.setLong(2, id);
		jt.update(psql.getSQL());
	}

	// 向设备参数表里面添加或修改仿真测速参数
	public int addOrUpdateDevVerAtt(long id, int is_speedtest)
	{
		String sql = "select devicetype_id from tab_device_version_attribute where devicetype_id="
				+ id;
		String excSql = "";
		if (null == jt.queryForList(sql) || 0 == jt.queryForList(sql).size())
		{
			excSql = "insert into tab_device_version_attribute(is_speedTest,devicetype_id) values(?,?)";
		}
		else
		{
			excSql = "update tab_device_version_attribute set is_speedTest = ? where devicetype_id= ?";
		}
		PrepareSQL psql = new PrepareSQL(excSql);
		psql.setLong(2, id);
		psql.setInt(1, is_speedtest);
		return jt.update(psql.getSQL());
	}

	// 向设备参数表里面添加或修改仿真测速参数
	public int addOrUpdateDevVerAtt(long id, int is_speedtest, int wifi_ability) {
		String sql = "select devicetype_id from tab_device_version_attribute where devicetype_id=" + id;
		String excSql = "";
		if (null == jt.queryForList(sql) || 0 == jt.queryForList(sql).size())
		{
			excSql = "insert into tab_device_version_attribute(wifi_ability, is_speedTest, devicetype_id) values(?, ?, ?)";
		}
		else
		{
			excSql = "update tab_device_version_attribute set wifi_ability = ?, is_speedTest = ? where devicetype_id= ?";
		}
		PrepareSQL psql = new PrepareSQL(excSql);
		psql.setLong(3, id);
		psql.setInt(1, wifi_ability);
		psql.setInt(2, is_speedtest);
		return jt.update(psql.getSQL());
	}

	public String getDeviceVersionType(Object type){

		String resType = "未定义";
		if(null == type){
			return resType;
		}

		type = StringUtil.getStringValue(type);
		logger.warn("type:"+type);
		if("1".equals(type)){
			resType = "天翼网关1.0";
		}else if("2".equals(type)){
			resType = "天翼网关2.0";
		}else if("3".equals(type)){
			resType = "天翼网关3.0";
		}else if("4".equals(type)){
			resType = "天翼网关4.0";
		}else if("5".equals(type)){
			resType = "天翼网关5.0";
		}else if("6".equals(type)){
			resType = "E8C";
		}else if("7".equals(type)){
			resType = "融合网关";
		}else if("8".equals(type)){
			resType = "政企网关";
		}

		return resType;
	}

	/**
	 * 安徽联通 版本管理条件查询
	 * @param vendor
	 * @param device_model
	 * @param hard_version
	 * @param soft_version
	 * @param is_check
	 * @param startTime
	 * @param endTime
	 * @param access_style_relay_id
	 * @param spec_id
	 * @param machineConfig
	 * @param ipvsix
	 * @param startOpenDate
	 * @param endOpenDate
	 * @param mbBroadband
	 * @param deviceVersionType
	 * @return
	 */
	public Map<String,JSONObject> queryDevVersionListAhlt(int vendor, int device_model, String hard_version,
									 String soft_version, int is_check, String startTime, String endTime,
									 int access_style_relay_id, int spec_id, String machineConfig, String ipvsix,
									 String startOpenDate, String endOpenDate, String mbBroadband, String deviceVersionType,int startIndex,int endIndex)
	{
		logger.debug("queryDeviceList({},{},{},{},{},{},{})", new Object[] { vendor,
				device_model, hard_version, soft_version, is_check});
		//获取条件查询sql
		StringBuilder sb = getSqlBuild(vendor, device_model, hard_version, soft_version, is_check, startTime, endTime,
				access_style_relay_id, spec_id, machineConfig, ipvsix, startOpenDate, endOpenDate, mbBroadband, deviceVersionType,startIndex,endIndex);
		PrepareSQL psql = new PrepareSQL(sb.toString());
		final Map accessTypeMap = getAccessType();
		List<Map> resultList = jt.queryForList(psql.getSQL());
		//转为map: key: vendorId  增加已审查和未审查分类 update on 2020/8/10
		Map<String,JSONObject> vendorListMap = new HashMap<String, JSONObject>();
		for(Map temp : resultList){
			Map<String, String> map = getMap(accessTypeMap, temp);
			String vendorId = StringUtil.getStringValue(temp.get("vendor_id"));
			String vendorName = StringUtil.getStringValue(temp.get("vendor_add"));
			String isCheck = map.get("is_check");
			if(vendorListMap.get(vendorId) == null){
				JSONObject valueObject = new JSONObject();
				valueObject.put("vendorName",vendorName);
				//定义 已审核和未审核分类数组
				JSONArray checkArray = new JSONArray();
				JSONObject isCheckObject = new JSONObject();
				isCheckObject.put("id","1");
				isCheckObject.put("name","已审核");
				isCheckObject.put("children",new JSONArray());
				checkArray.add(isCheckObject);

				JSONObject isNoCheckObject = new JSONObject();
				isNoCheckObject.put("id","-1");
				isNoCheckObject.put("name","未审核");
				isNoCheckObject.put("children",new JSONArray());
				checkArray.add(isNoCheckObject);

				if(isCheck.equals("1")){
					//已审核
					//已审核设备类型列表
					isCheckObject.put("children",getModelDataArray(map));
				}else {
					//未审核
					//未审核设备类型列表
					isNoCheckObject.put("children",getModelDataArray(map));
				}
				valueObject.put("children",checkArray);
				vendorListMap.put(vendorId, valueObject);
			}else {
				JSONObject checkObject = vendorListMap.get(vendorId);
				JSONArray checkArray = checkObject.getJSONArray("children");
				if(isCheck.equals("1")){
					//已审核
					for(int i = 0; i < checkArray.size(); i++){
						JSONObject object = checkArray.getJSONObject(i);
						if(!object.getString("id").equals("1")){
							continue;
						}
						if(object.get("children") == null){
							object.put("children",getModelDataArray(map));
						}else {
							JSONArray dataArray = object.getJSONArray("children");
							analysisModelDataArray(map,dataArray);
						}
					}

				}else {
					//未审核  数据处理部分同 已审核的相同
					for(int i = 0; i < checkArray.size(); i++){
						JSONObject object = checkArray.getJSONObject(i);
						if(!object.getString("id").equals("-1")){
							continue;
						}
						if(object.get("children") == null){
							object.put("children",getModelDataArray(map));
						}else {
							JSONArray dataArray = object.getJSONArray("children");
							analysisModelDataArray(map,dataArray);
						}
					}
				}
			}
		}
		return vendorListMap;
	}

	private JSONArray getModelDataArray(Map<String, String> map){
		JSONArray dataArray = new JSONArray();
		JSONObject tempObject = new JSONObject();
		tempObject.put("id",map.get("device_model_id"));
		tempObject.put("name", map.get("device_model"));
		List<Map<String,String>> devTypeList = new ArrayList<Map<String,String>>();
		devTypeList.add(map);
		//存对应的已审核/未审核版本list
		tempObject.put("data", devTypeList);
		dataArray.add(tempObject);
		return dataArray;
	}

	private void analysisModelDataArray(Map<String, String> map, JSONArray dataArray){
		boolean isDevModelHave = false;
		//若该modelId 已存在 则将该设备map追加进去 不存在 则新增
		for(int j = 0; j < dataArray.size(); j++){
			JSONObject dataObject = dataArray.getJSONObject(j);
			String deviceModelId = dataObject.getString("id");
			if(deviceModelId.equals(map.get("device_model_id"))){
				isDevModelHave = true;
				List<Map<String,String>> devTypeList = (List<Map<String, String>>) dataObject.get("data");
				devTypeList.add(map);
				break;
			}
		}
		if(!isDevModelHave){
			JSONObject tempObject = new JSONObject();
			tempObject.put("id",map.get("device_model_id"));
			tempObject.put("name", map.get("device_model"));
			List<Map<String,String>> devTypeList = new ArrayList<Map<String,String>>();
			devTypeList.add(map);
			tempObject.put("data", devTypeList);
			dataArray.add(tempObject);
		}
	}


	public List<Map<String,String>> getDevVersionList(int vendor, int device_model, String hard_version,
													  String soft_version, int is_check, String startTime, String endTime,
													  int access_style_relay_id, int spec_id, String machineConfig, String ipvsix,
													  String startOpenDate, String endOpenDate, String mbBroadband, String deviceVersionType,int startIndex,int endIndex){
		//获取条件查询sql
		StringBuilder sb = getSqlBuild(vendor, device_model, hard_version, soft_version, is_check, startTime, endTime, access_style_relay_id,
				spec_id, machineConfig, ipvsix, startOpenDate, endOpenDate, mbBroadband, deviceVersionType,startIndex,endIndex);
		PrepareSQL psql = new PrepareSQL(sb.toString());
		final Map accessTypeMap = getAccessType();
		List<Map> resultList = jt.queryForList(psql.getSQL());
		List<Map<String,String>> resultMapList = new ArrayList<Map<String, String>>();
		for(Map temp : resultList) {
			resultMapList.add(getMap(accessTypeMap, temp));
		}
		return resultMapList;
	}

	private Map<String, String> getMap(Map accessTypeMap, Map temp) {
		Map<String, String> map = new HashMap<String, String>();
		String accessStyleRelayId = StringUtil.getStringValue(temp
				.get("access_style_relay_id"));
		map.put("devicetype_id", StringUtil.getStringValue(temp.get("devicetype_id")));
		map.put("zeroconf", StringUtil.getStringValue(temp.get("zeroconf")));
		try
		{
			long versionttime = StringUtil.getLongValue(temp
					.get("versionttime")) * 1000L;
			DateTimeUtil dt = new DateTimeUtil(versionttime);
			map.put("versionttime", dt.getLongDate());
		}
		catch (NumberFormatException e)
		{
			map.put("versionttime", "");
		}
		catch (Exception e)
		{
			map.put("versionttime", "");
		}
		map.put("mbbroadband", StringUtil.getStringValue(temp.get("mbbroadband")));
		map.put("vendor_add", StringUtil.getStringValue(temp.get("vendor_add")));
		map.put("device_model", StringUtil.getStringValue(temp.get("device_model")));
		map.put("spec_id", StringUtil.getStringValue(temp.get("spec_id")));
		map.put("specversion", StringUtil.getStringValue(temp.get("specversion")));
		map.put("hardwareversion", StringUtil.getStringValue(temp.get("hardwareversion")));
		map.put("softwareversion", StringUtil.getStringValue(temp.get("softwareversion")));
		map.put("is_check", StringUtil.getStringValue(temp.get("is_check")));
		map.put("rela_dev_type_id", StringUtil.getStringValue(temp.get("rela_dev_type_id")));
		map.put("vendor_id", StringUtil.getStringValue(temp.get("vendor_id")));
		map.put("device_model_id", StringUtil.getStringValue(temp.get("device_model_id")));
		map.put("ip_type", StringUtil.getStringValue(temp.get("ip_type")));
		map.put("ip_model_type", StringUtil.getStringValue(temp.get("ip_model_type")));
		map.put("is_normal",StringUtil.getStringValue(temp.get("is_normal")));
		map.put("specName", StringUtil.getStringValue(temp.get("specname")));
		map.put("rela_dev_type_name", Global.G_DeviceTypeID_Name_Map.get(StringUtil.getStringValue((temp.get("rela_dev_type_id")))));
		map.put("wifi", "");
		map.put("wifi_frequency", "");
		map.put("download_max_wifi", "");
		map.put("gigabit_port", "");
		map.put("gigabit_port_type", "");
		map.put("download_max_lan", "");
		map.put("power", "");
		map.put("terminal_access_time", "");
		if (!"".equals(accessStyleRelayId))
		{
			map.put("type_id", accessStyleRelayId);
			map.put("type_name", (String) accessTypeMap.get(accessStyleRelayId));
		}
		else
		{
			map.put("type_id", "");
			map.put("type_name", "");
		}
		String devVersionTypeId = StringUtil.getStringValue(temp.get("device_version_type"));
		map.put("devVersionType", StringUtil.IsEmpty(devVersionTypeId) ? "" : AHLTDevVersionTypeEnum.getNameByCode(devVersionTypeId));
		return map;
	}

	private StringBuilder getSqlBuild(int vendor, int device_model, String hard_version, String soft_version, int is_check, String startTime, String endTime, int access_style_relay_id,
									  int spec_id, String machineConfig, String ipvsix, String startOpenDate, String endOpenDate, String mbBroadband,
									  String deviceVersionType,int startIndex,int endIndex) {
		StringBuilder sb = new StringBuilder();
		String sql = "select * from (select rownum as rn,a.zeroconf,a.versionttime,a.mbbroadband,"
				+ "a.devicetype_id, a.vendor_id, a.device_model_id, a.spec_id,"
				+ " a.access_style_relay_id, c.vendor_add, b.device_model, d.spec_name as specName, "
				+ " a.specversion, a.hardwareversion, a.softwareversion, a.is_check, "
				+ "a.rela_dev_type_id,a.ip_type,a.ip_model_type,is_normal,v.device_version_type"
				+ " from tab_devicetype_info a left join tab_bss_dev_port d on a.spec_id = d.id "
				+ " left join tab_device_version_attribute v on a.devicetype_id = v.devicetype_id "
				+ ", gw_device_model b,tab_vendor c"
				+ " where a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id";

		sb.append(sql);
		if (!"-1".equals(machineConfig))
		{
			sb.append(" and a.zeroconf=").append(machineConfig);
		}
		if (!"-1".equals(ipvsix))
		{
			if ("1".equals(ipvsix))
			{
				sb.append(" and a.ip_type <> 0");
			}
			if ("2".equals(ipvsix))
			{
				sb.append(" and a.ip_type = 0");
			}
		}
		if (startOpenDate != null && !"".equals(startOpenDate))
		{
			sb.append(" and a.versionttime >= ").append(startOpenDate);
		}
		if (endOpenDate != null && !"".equals(endOpenDate))
		{
			sb.append(" and a.versionttime <= ").append(endOpenDate);
		}
		if (mbBroadband != null && !"-1".equals(mbBroadband))
		{
			sb.append(" and a.mbbroadband=").append(mbBroadband);
		}
		if (vendor != -1)
		{
			sb.append(" and c.vendor_id='").append(vendor).append("'");
		}
		if (device_model != -1)
		{
			sb.append(" and b.device_model_id='").append(device_model).append("'");
		}
		if (hard_version != null && !"".equals(hard_version))
		{
			sb.append(" and a.hardwareversion='").append(hard_version).append("'");
		}
		// 软件版本后模糊匹配
		if (soft_version != null && !"".equals(soft_version))
		{
			sb.append(" and a.softwareversion like '").append(soft_version).append("%'");
		}
		if (is_check != -2)
		{
			sb.append(" and a.is_check=").append(is_check);
		}

		if (startTime != null && !"".equals(startTime))
		{
			sb.append(" and a.add_time > ").append(startTime);
		}
		if (endTime != null && !"".equals(endTime))
		{
			sb.append(" and a.add_time < ").append(endTime);
		}
		if (access_style_relay_id != -1)
		{
			sb.append(" and a.access_style_relay_id=").append(access_style_relay_id);
		}
		if (spec_id != -1)
		{
			sb.append(" and spec_id = ").append(spec_id);
		}
		if (!deviceVersionType.equals("-1"))
		{
			sb.append(" and v.device_version_type = ").append(deviceVersionType);
		}
		if(endIndex != -1){
			sb.append(" and rownum <= ").append(endIndex);
		}
		sb.append(" order by a.devicetype_id) re where 1=1");
		if(startIndex != -1){
			sb.append(" and re.rn > ").append(startIndex);
		}
		return sb;
	}


	public Map getDetailByDevTypeId(int devTypeId){
		String sql = "select a.zeroconf,a.versionttime,a.mbbroadband,"
				+ "a.devicetype_id, a.vendor_id, a.device_model_id, a.spec_id,"
				+ " a.access_style_relay_id, c.vendor_add, b.device_model, d.spec_name as specName, "
				+ " a.specversion, a.hardwareversion, a.softwareversion, a.is_check, "
				+ "a.rela_dev_type_id,a.ip_type,a.ip_model_type,is_normal,a.is_qoe,v.device_version_type"
				+ " from tab_devicetype_info a left join tab_bss_dev_port d on a.spec_id = d.id "
				+ " left join tab_device_version_attribute v on a.devicetype_id = v.devicetype_id "
				+ ", gw_device_model b,tab_vendor c"
				+ " where a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id and a.devicetype_id=" + devTypeId;
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForMap(psql.getSQL());
	}


	public List<Map> queryDetailForExcel(int vendor, int device_model, String hard_version, String soft_version, int is_check, int rela_dev_type, int curPage_splitPage,
						 int num_splitPage, String startTime, String endTime, int access_style_relay_id, int spec_id,
						 String machineConfig, String ipvsix, String startOpenDate, String endOpenDate, String mbBroadband,
						 String device_version_type, String wifi, String wifi_frequency, String download_max_wifi,
						 String gigabit_port, String gigabit_port_type, String download_max_lan, String power,
						 String terminal_access_time, int isSupSpeedTest_query) {
		StringBuffer sb = new StringBuffer();
		String sql = "";
		sql = "select  c.vendor_add, b.device_model,a.hardwareversion, a.softwareversion,  a.is_check," +
				" a.access_style_relay_id ,a.ip_type,a.ip_model_type,a.mbbroadband,e.device_version_type,e.is_speedTest " +
				" from tab_devicetype_info a left join tab_device_version_attribute e on a.devicetype_id=e.devicetype_id , " +
				" gw_device_model b,tab_vendor c"
				+ " where a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id";
		// a.access_style_relay_id 上行方式  (String) accessTypeMap.get(accessStyleRelayId)
		sb.append(sql);
		if (!"-1".equals(machineConfig))
		{
			sb.append(" and a.zeroconf=" + machineConfig + "");
		}
		if (!"-1".equals(ipvsix))
		{
			if ("1".equals(ipvsix))
			{
				sb.append(" and a.ip_type <> 0");
			}
			if ("2".equals(ipvsix))
			{
				sb.append(" and a.ip_type = 0");
			}
		}
		if (startOpenDate != null && !"".equals(startOpenDate))
		{
			sb.append(" and a.versionttime >= " + startOpenDate);
		}
		if (endOpenDate != null && !"".equals(endOpenDate))
		{
			sb.append(" and a.versionttime <= " + endOpenDate);
		}
		if (mbBroadband != null && !"-1".equals(mbBroadband))
		{
			sb.append(" and a.mbbroadband=" + mbBroadband + "");
		}
		if (vendor != -1)
		{
			sb.append(" and c.vendor_id='" + vendor + "'");
		}
		if (device_model != -1)
		{
			sb.append(" and b.device_model_id='" + device_model + "'");
		}
		if (hard_version != null && !"".equals(hard_version))
		{
			sb.append(" and a.hardwareversion='" + hard_version + "'");
		}
		// 软件版本后模糊匹配
		if (soft_version != null && !"".equals(soft_version))
		{
			sb.append(" and a.softwareversion like '" + soft_version + "%'");
		}
		if (is_check != -2)
		{
			sb.append(" and a.is_check=" + is_check);
		}
		if (rela_dev_type != -1)
		{
			sb.append(" and a.rela_dev_type_id=" + rela_dev_type);
		}
		if (startTime != null && !"".equals(startTime))
		{
			sb.append(" and a.add_time > " + startTime);
		}
		if (endTime != null && !"".equals(endTime))
		{
			sb.append(" and a.add_time < " + endTime);
		}
		if (access_style_relay_id != -1)
		{
			sb.append(" and a.access_style_relay_id=" + access_style_relay_id);
		}
		if (spec_id != -1)
		{
			sb.append(" and spec_id = " + spec_id);
		}
		//山东电信新增 查询条件 是否支持测速
		if(isSupSpeedTest_query != -1 && SDDX.equals(instArea))
		{
			sb.append(" and e.is_speedTest = " + isSupSpeedTest_query);
		}

		PrepareSQL psql = new PrepareSQL(sb.toString());
		List<Map> list = jt.queryForList(psql.getSQL());
		return list;
	}
}
