
package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.Global;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.SuperDAO;

public class E8cVersionQueryDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(VersionDAO.class);
	public static final String JSDX="js_dx";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getVendorList()
	{
		PrepareSQL psql = new PrepareSQL(
				"select vendor_id,vendor_name, vendor_add from tab_vendor order by vendor_name");
		List<Map<String, String>> queryList = jt.queryForList(psql.getSQL());
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		if (queryList != null && queryList.size() > 0)
		{
			for (int i = 0; i < queryList.size(); i++)
			{
				map = new HashMap<String, Object>();
				Map<String, String> queryMap = queryList.get(i);
				map.put("vendor_id", queryMap.get("vendor_id"));
				map.put("vendorName", queryMap.get("vendor_add"));
				list.add(map);
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getGwDevType()
	{
		PrepareSQL psql = new PrepareSQL("select type_id,type_name from gw_dev_type");
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings({ "rawtypes" })
	public List getDeviceModel(String vendor_id)
	{
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select device_model_id,device_model from gw_device_model where vendor_id='");
		strSQL.append(vendor_id);
		strSQL.append("' order by device_model");
		PrepareSQL psql = new PrepareSQL(strSQL.toString());
		psql.getSQL();
		return jt.queryForList(strSQL.toString());
	}

	@SuppressWarnings("rawtypes")
	public String getSelectOptiones(List mapList, String value, String textarea)
	{
		if (mapList == null || mapList.size() <= 0)
		{
			return "";
		}
		else
		{
			StringBuffer sb = new StringBuffer();
			int size = mapList.size();
			Map map = null;
			for (int i = 0; i < size; i++)
			{
				map = (Map) mapList.get(i);
				if (map != null && !map.isEmpty())
				{
					sb.append("<option value='");
					sb.append(map.get(value));
					sb.append("'>");
					sb.append(map.get(textarea));
					sb.append("</option>");
				}
			}
			return sb.toString();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> querySpecList()
	{
		PrepareSQL psql = new PrepareSQL(
				"select id,spec_name from tab_bss_dev_port order by spec_name");
		List<Map<String, String>> queryList = jt.queryForList(psql.getSQL());
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		if (queryList != null && queryList.size() > 0)
		{
			for (int i = 0; i < queryList.size(); i++)
			{
				map = new HashMap<String, Object>();
				Map<String, String> queryMap = queryList.get(i);
				map.put("id", queryMap.get("id"));
				map.put("spec_name", queryMap.get("spec_name"));
				list.add(map);
			}
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getE8cVersionList(int curPage_splitPage, int num_splitPage,
			String vendor_id, String device_model, String device_type, String spec_id,
			String starttime, String endtime)
	{
		final Map<String, String> vendorMap = getVendor();
		final Map<String, String> deviceModelMap = getDeviceModel();
		final Map accessTypeMap = getAccessType();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.zeroconf,a.versionttime,a.mbbroadband,"
				+ "a.devicetype_id, a.vendor_id, a.device_model_id, a.spec_id,"
				+ "a.access_style_relay_id, c.vendor_add, b.device_model, d.spec_name as specName,"
				+ "a.specversion, a.hardwareversion, a.softwareversion, a.is_check,a.is_recent_version,"
				+ "a.rela_dev_type_id,a.ip_type,a.ip_model_type,a.is_normal,a.reason,a.is_awifi "
				+ "from tab_devicetype_info a left join tab_bss_dev_port d on a.spec_id = d.id,"
				+ "gw_device_model b,tab_vendor c "
				+ "where a.device_model_id=b.device_model_id and a.vendor_id = c.vendor_id ");
		if (!StringUtil.IsEmpty(vendor_id) && !"-1".equals(vendor_id))
		{
			sql.append(" and a.vendor_id = '" + vendor_id + "'");
		}
		if (!StringUtil.IsEmpty(device_model) && !"-1".equals(device_model))
		{
			sql.append(" and a.device_model_id = '" + device_model + "'");
		}
		if (!StringUtil.IsEmpty(device_type) && !"-1".equals(device_type))
		{
			sql.append(" and a.rela_dev_type_id = " + device_type);
		}
		if (!StringUtil.IsEmpty(spec_id) && !"-1".equals(spec_id))
		{
			sql.append(" and a.spec_id = " + spec_id);
		}
		if (!StringUtil.IsEmpty(starttime) && !"".equals(starttime))
		{
			sql.append(" and a.versionttime >= "
					+ new DateTimeUtil(starttime).getLongTime());
		}
		if (!StringUtil.IsEmpty(endtime) && !"".equals(endtime))
		{
			sql.append(" and a.versionttime <= "
					+ new DateTimeUtil(endtime).getLongTime());
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		return querySP(sql.toString(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper()
				{

					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						map.put("vendor_id", rs.getString("vendor_id"));
						map.put("vendor_name", vendorMap.get(rs.getString("vendor_id")));
						map.put("device_model_id", rs.getString("device_model_id"));
						map.put("device_model",
								deviceModelMap.get(rs.getString("device_model_id")));
						map.put("hardware_version", rs.getString("hardwareversion"));
						map.put("software_version", rs.getString("softwareversion"));
						map.put("spec_id", rs.getString("spec_id"));
						map.put("devicetype_id", rs.getString("devicetype_id"));
						map.put("rela_dev_type_name",
								StringUtil.IsEmpty(StringUtil.getStringValue(rs
										.getString("rela_dev_type_id"))) ? ""
										: Global.G_DeviceTypeID_Name_Map.get(StringUtil
												.getStringValue(rs
														.getString("rela_dev_type_id"))));
						map.put("access_type", StringUtil.getStringValue(accessTypeMap
								.get(rs.getString("access_style_relay_id"))));
						map.put("is_check", rs.getString("is_check"));
						map.put("is_recent_version", rs.getString("is_recent_version"));
						map.put("is_normal", rs.getString("is_normal"));
						return map;
					}
				});
	}

	public int getE8cVersionCount(int curPage_splitPage, int num_splitPage,
			String vendor_id, String device_model, String device_type, String spec_id,
			String starttime, String endtime)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from tab_devicetype_info a left join tab_bss_dev_port d "
				+ "on a.spec_id = d.id,gw_device_model b,tab_vendor c "
				+ "where a.device_model_id = b.device_model_id and a.vendor_id = c.vendor_id ");
		if (!StringUtil.IsEmpty(vendor_id) && !"-1".equals(vendor_id))
		{
			sql.append(" and a.vendor_id = '" + vendor_id + "'");
		}
		if (!StringUtil.IsEmpty(device_model) && !"-1".equals(device_model))
		{
			sql.append(" and a.device_model_id = '" + device_model + "'");
		}
		if (!StringUtil.IsEmpty(device_type) && !"-1".equals(device_type))
		{
			sql.append(" and a.rela_dev_type_id = " + device_type);
		}
		if (!StringUtil.IsEmpty(spec_id) && !"-1".equals(spec_id))
		{
			sql.append(" and a.spec_id = " + spec_id);
		}
		if (!StringUtil.IsEmpty(starttime) && !"".equals(starttime))
		{
			sql.append(" and a.versionttime >= "
					+ new DateTimeUtil(starttime).getLongTime());
		}
		if (!StringUtil.IsEmpty(endtime) && !"".equals(endtime))
		{
			sql.append(" and a.versionttime <= "
					+ new DateTimeUtil(endtime).getLongTime());
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		int total = jt.queryForInt(sql.toString());
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

	@SuppressWarnings("rawtypes")
	public List<Map> getE8cVersionOperList(int curPage_splitPage, int num_splitPage,
			String vendor_id, String device_model, String device_type, String spec_id,
			String starttime, String endtime)
	{
		final Map<String, String> vendorMap = getVendor();
		final Map<String, String> deviceModelMap = getDeviceModel();
		final Map accessTypeMap = getAccessType();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.zeroconf,a.versionttime,a.mbbroadband,"
				+ "a.devicetype_id, a.vendor_id, a.device_model_id, a.spec_id,"
				+ "a.access_style_relay_id, c.vendor_add, b.device_model, d.spec_name as specName,"
				+ "a.specversion, a.hardwareversion, a.softwareversion, a.is_check,a.is_recent_version,"
				+ "a.rela_dev_type_id,a.ip_type,a.ip_model_type,a.is_normal,a.reason,a.is_awifi,e.versionfile_name,e.versionfile_path  "
				+ "from tab_devicetype_info a left join tab_bss_dev_port d on a.spec_id = d.id,"
				+ "gw_device_model b,tab_vendor c,tab_version_file e "
				+ "where a.device_model_id=b.device_model_id and a.vendor_id = c.vendor_id and a.devicetype_id = e.devicetype_id");
		if (!StringUtil.IsEmpty(vendor_id) && !"-1".equals(vendor_id))
		{
			sql.append(" and a.vendor_id = '" + vendor_id + "'");
		}
		if (!StringUtil.IsEmpty(device_model) && !"-1".equals(device_model))
		{
			sql.append(" and a.device_model_id = '" + device_model + "'");
		}
		if (!StringUtil.IsEmpty(device_type) && !"-1".equals(device_type))
		{
			sql.append(" and a.rela_dev_type_id = " + device_type);
		}
		if (!StringUtil.IsEmpty(spec_id) && !"-1".equals(spec_id))
		{
			sql.append(" and a.spec_id = " + spec_id);
		}
		if (!StringUtil.IsEmpty(starttime) && !"".equals(starttime))
		{
			sql.append(" and e.versionttime >= "
					+ new DateTimeUtil(starttime).getLongTime());
		}
		if (!StringUtil.IsEmpty(endtime) && !"".equals(endtime))
		{
			sql.append(" and e.versionttime <= "
					+ new DateTimeUtil(endtime).getLongTime());
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		return querySP(sql.toString(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper()
				{

					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						map.put("vendor_id", rs.getString("vendor_id"));
						map.put("vendor_name", vendorMap.get(rs.getString("vendor_id")));
						map.put("device_model_id", rs.getString("device_model_id"));
						map.put("device_model",
								deviceModelMap.get(rs.getString("device_model_id")));
						map.put("hardware_version", rs.getString("hardwareversion"));
						map.put("software_version", rs.getString("softwareversion"));
						map.put("spec_id", rs.getString("spec_id"));
						map.put("devicetype_id", rs.getString("devicetype_id"));
						map.put("rela_dev_type_name",
								StringUtil.IsEmpty(StringUtil.getStringValue(rs
										.getString("rela_dev_type_id"))) ? ""
										: Global.G_DeviceTypeID_Name_Map.get(StringUtil
												.getStringValue(rs
														.getString("rela_dev_type_id"))));
						map.put("access_type", StringUtil.getStringValue(accessTypeMap
								.get(rs.getString("access_style_relay_id"))));
						map.put("is_check", rs.getString("is_check"));
						map.put("is_recent_version", rs.getString("is_recent_version"));
						map.put("is_normal", rs.getString("is_normal"));
						map.put("versionfile_name", rs.getString("versionfile_name"));
						map.put("versionfile_path", rs.getString("versionfile_path"));
						return map;
					}
				});
	}

	public int getE8cVersionOperCount(int curPage_splitPage, int num_splitPage,
			String vendor_id, String device_model, String device_type, String spec_id,
			String starttime, String endtime)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from tab_devicetype_info a left join tab_bss_dev_port d "
				+ "on a.spec_id = d.id,gw_device_model b,tab_vendor c,tab_version_file e "
				+ "where a.device_model_id = b.device_model_id and a.vendor_id = c.vendor_id and a.devicetype_id = e.devicetype_id ");
		if (!StringUtil.IsEmpty(vendor_id) && !"-1".equals(vendor_id))
		{
			sql.append(" and a.vendor_id = '" + vendor_id + "'");
		}
		if (!StringUtil.IsEmpty(device_model) && !"-1".equals(device_model))
		{
			sql.append(" and a.device_model_id = '" + device_model + "'");
		}
		if (!StringUtil.IsEmpty(device_type) && !"-1".equals(device_type))
		{
			sql.append(" and a.rela_dev_type_id = " + device_type);
		}
		if (!StringUtil.IsEmpty(spec_id) && !"-1".equals(spec_id))
		{
			sql.append(" and a.spec_id = " + spec_id);
		}
		if (!StringUtil.IsEmpty(starttime) && !"".equals(starttime))
		{
			sql.append(" and e.versionttime >= "
					+ new DateTimeUtil(starttime).getLongTime());
		}
		if (!StringUtil.IsEmpty(endtime) && !"".equals(endtime))
		{
			sql.append(" and e.versionttime <= "
					+ new DateTimeUtil(endtime).getLongTime());
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		int total = jt.queryForInt(sql.toString());
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> getDetailInfo(long deviceTypeId, long detailSpecId)
	{
		String infoSql = "select a.zeroconf,a.versionttime,a.mbbroadband,a.devicetype_id, c.vendor_add, "
				+ "b.device_model, a.access_style_relay_id,a.spec_id,"
				+ " a.specversion, a.hardwareversion, a.softwareversion, a.is_check, a.rela_dev_type_id,"
				+ " a.access_style_relay_id,a.ip_type,a.ip_model_type,a.is_ott,is_normal";
		String instArea=com.linkage.module.gwms.Global.instAreaShortName;
		// 江苏电信itms需要查询是否开通了awifi
		if (JSDX.equals(instArea))
		{
			infoSql += ",a.is_awifi";
		}
		infoSql += " from tab_devicetype_info a, gw_device_model b,tab_vendor c where a.device_model_id=b.device_model_id and a.vendor_id=c.vendor_id  and a.devicetype_id = "
				+ deviceTypeId;
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
		String specName = getSpecName(String.valueOf(detailSpecId));
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

	public void updateIsCheck(long deviceTypeId)
	{
		PrepareSQL psql = new PrepareSQL(
				"update tab_devicetype_info set is_recent_version = 1 where devicetype_id = ?");
		psql.setLong(1, deviceTypeId);
		jt.update(psql.getSQL());
	}

	public int saveDownE8cVersionRecord(String devicetype_id, String operType,
			long acc_id, String downTime, String fullFileName)
	{
		PrepareSQL psql = new PrepareSQL(
				"insert into tab_version_record(devicetype_id,operType,acc_id,oper_time,file_name) values(?,?,?,?,?) ");
		psql.setInt(1, StringUtil.getIntegerValue(devicetype_id));
		psql.setString(2, operType);
		psql.setLong(3, acc_id);
		psql.setLong(4, new DateTimeUtil(downTime).getLongTime());
		psql.setString(5, fullFileName);
		return jt.update(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getsoftVersion(String device_model)
	{
		PrepareSQL psql = new PrepareSQL(
				"select distinct a.devicetype_id,a.softwareversion from tab_devicetype_info a "
						+ "where a.device_model_id = ? "
						+ " order by a.softwareversion");
		psql.setString(1, device_model);
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getResourcePath()
	{
		String sql = "select * from tab_file_server where file_type = 4";
		// teledb
		if (DBUtil.GetDB() == 3) {
			sql = "select dir_id, inner_url, server_dir from tab_file_server where file_type = 4";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		List<Map<String, String>> queryList = jt.queryForList(psql.getSQL());
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		if (queryList != null && queryList.size() > 0)
		{
			for (int i = 0; i < queryList.size(); i++)
			{
				map = new HashMap<String, Object>();
				Map<String, String> queryMap = queryList.get(i);
				map.put("dir_id", queryMap.get("dir_id"));
				map.put("dir_url", queryMap.get("inner_url") + queryMap.get("server_dir"));
				list.add(map);
			}
		}
		return list;
	}

	public void saveUploadRecord(String devicetype_id, String operType, long acc_id,
			String uploadTime, String fullFileName)
	{
		PrepareSQL psql = new PrepareSQL(
				"insert into tab_version_record(devicetype_id,operType,acc_id,oper_time,file_name) values(?,?,?,?,?)");
		psql.setInt(1, StringUtil.getIntegerValue(devicetype_id));
		psql.setString(2, operType);
		psql.setLong(3, acc_id);
		psql.setLong(4, new DateTimeUtil(uploadTime).getLongTime());
		psql.setString(5, fullFileName);
		jt.update(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public void saveUploadFile(String versionfile_name, String devicetype_id,
			String versionfile_path)
	{
		PrepareSQL psql = new PrepareSQL(
				"select max(versionfile_id) as num from tab_version_file");
		List<Map<String, String>> queryList = jt.queryForList(psql.getSQL());
		int versionfile_id = StringUtil.getIntegerValue(queryList.get(0).get("num")) + 1;
		psql = new PrepareSQL(
				"insert into tab_version_file(versionfile_id,versionfile_name,versionfile_size,versionfile_status,versionfile_isexist,devicetype_id,versionfile_path,versionttime) values(?,?,?,?,?,?,?,?)");
		psql.setInt(1, versionfile_id);
		psql.setString(2, versionfile_name);
		psql.setInt(3, 0);
		psql.setInt(4, 1);
		psql.setInt(5, 1);
		psql.setInt(6, StringUtil.getIntegerValue(devicetype_id));
		psql.setString(7, versionfile_path);
		DateTimeUtil dt = new DateTimeUtil();
		String versionttime = dt.getLongDate();
		psql.setLong(8, new DateTimeUtil(versionttime).getLongTime());
		jt.update(psql.getSQL());
	}

	@SuppressWarnings({ "rawtypes" })
	public List<Map> getFileOperRecordList(int curPage_splitPage, int num_splitPage,
			String devicetype_id, String operType, String starttime, String endtime)
	{
		final Map<String, String> vendorMap = getVendor();
		final Map<String, String> deviceModelMap = getDeviceModel();
		final Map<String, String> softwareversionMap = getSoftVersion();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.devicetype_id,a.operType,a.acc_id,a.oper_time,a.file_name,b.vendor_id,b.device_model_id,c.acc_loginname from tab_version_record a,tab_devicetype_info b,tab_accounts c where a.devicetype_id = b.devicetype_id and a.acc_id = c.acc_oid");
		if (!StringUtil.IsEmpty(devicetype_id) && !"-1".equals(devicetype_id))
		{
			sql.append(" and a.devicetype_id = " + devicetype_id);
		}
		if (!StringUtil.IsEmpty(operType) && !"-1".equals(operType))
		{
			if ("1".equals(operType))
			{
				sql.append(" and a.operType = '上传'");
			}
			else if ("2".equals(operType))
			{
				sql.append(" and a.operType = '下载'");
			}
		}
		if (!StringUtil.IsEmpty(starttime) && !"".equals(starttime))
		{
			sql.append(" and a.oper_time >= " + new DateTimeUtil(starttime).getLongTime());
		}
		if (!StringUtil.IsEmpty(endtime) && !"".equals(endtime))
		{
			sql.append(" and a.oper_time <= " + new DateTimeUtil(endtime).getLongTime());
		}
		sql.append(" order by a.oper_time desc");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		return querySP(sql.toString(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper()
				{

					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						map.put("vendor_name", vendorMap.get(rs.getString("vendor_id")));
						map.put("device_model",
								deviceModelMap.get(rs.getString("device_model_id")));
						map.put("software_version",
								softwareversionMap.get(rs.getString("devicetype_id")));
						map.put("operType", rs.getString("operType"));
						map.put("operUsername", rs.getString("acc_loginname"));
						long oper_time = StringUtil.getLongValue(rs
								.getString("oper_time"));
						DateTimeUtil dt = new DateTimeUtil(oper_time * 1000);
						map.put("time", dt.getLongDate());
						map.put("fileName", rs.getString("file_name"));
						return map;
					}
				});
	}

	public int getFileOperRecordCount(int curPage_splitPage, int num_splitPage,
			String devicetype_id, String operType, String starttime, String endtime)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from tab_devicetype_info b,tab_version_record a where a.devicetype_id = b.devicetype_id");
		if (!StringUtil.IsEmpty(devicetype_id) && !"-1".equals(devicetype_id))
		{
			sql.append(" and a.devicetype_id = " + devicetype_id);
		}
		if (!StringUtil.IsEmpty(operType) && !"-1".equals(operType))
		{
			if ("1".equals(operType))
			{
				sql.append(" and a.operType = '上传'");
			}
			else if ("2".equals(operType))
			{
				sql.append(" and a.operType = '下载'");
			}
		}
		if (!StringUtil.IsEmpty(starttime) && !"".equals(starttime))
		{
			sql.append(" and a.oper_time >= " + new DateTimeUtil(starttime).getLongTime());
		}
		if (!StringUtil.IsEmpty(endtime) && !"".equals(endtime))
		{
			sql.append(" and a.oper_time <= " + new DateTimeUtil(endtime).getLongTime());
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		int total = jt.queryForInt(sql.toString());
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

	@SuppressWarnings("unchecked")
	public String checkFillName(String file_name)
	{
		PrepareSQL psql = new PrepareSQL("select file_name from tab_version_record");
		List<Map<String, String>> list = jt.queryForList(psql.getSQL());
		if (null != list && list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				if (file_name.equals(StringUtil.getStringValue(list.get(i), "file_name")))
				{
					return "-1";
				}
			}
		}
		return "1";
	}

	@SuppressWarnings("unchecked")
	public String checkSoftVersion(String devicetype_id)
	{
		PrepareSQL psql = new PrepareSQL("select devicetype_id from tab_version_record");
		List<Map<String, String>> list = jt.queryForList(psql.getSQL());
		if (null != list && list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				if (devicetype_id.equals(StringUtil.getStringValue(list.get(i),
						"devicetype_id")))
				{
					return "-1";
				}
			}
		}
		return "1";
	}

	/**
	 * 获取厂商名称
	 * 
	 * @return map
	 */
	@SuppressWarnings({ "rawtypes" })
	public static HashMap<String, String> getVendor()
	{
		Cursor cursor = DataSetBean
				.getCursor("select vendor_id,vendor_name, vendor_add from tab_vendor where vendor_name is not null");
		HashMap<String, String> vendorMap = new HashMap<String, String>();
		Map fields = cursor.getNext();
		while (fields != null)
		{
			String vendor_add = (String) fields.get("vendor_add");
			if (false == StringUtil.IsEmpty(vendor_add))
			{
				vendorMap.put((String) fields.get("vendor_id"), vendor_add);
			}
			else
			{
				vendorMap.put((String) fields.get("vendor_id"),
						(String) fields.get("vendor_name"));
			}
			fields = cursor.getNext();
		}
		return vendorMap;
	}

	/**
	 * 获取设备型号
	 * 
	 * @return map
	 */
	@SuppressWarnings({ "rawtypes" })
	public static HashMap<String, String> getDeviceModel()
	{
		Cursor cursor = DataSetBean
				.getCursor("select device_model_id,device_model from gw_device_model where device_model is not null");
		HashMap<String, String> deviceModelMap = new HashMap<String, String>();
		Map fields = cursor.getNext();
		while (fields != null)
		{
			String device_model = (String) fields.get("device_model");
			if (false == StringUtil.IsEmpty(device_model))
			{
				deviceModelMap.put((String) fields.get("device_model_id"), device_model);
			}
			else
			{
				deviceModelMap.put((String) fields.get("device_model_id"),
						(String) fields.get("device_model"));
			}
			fields = cursor.getNext();
		}
		return deviceModelMap;
	}

	/**
	 * 获取软件版本
	 * 
	 * @return map
	 */
	@SuppressWarnings({ "rawtypes" })
	public static HashMap<String, String> getSoftVersion()
	{
		Cursor cursor = DataSetBean
				.getCursor("select distinct a.devicetype_id,a.softwareversion from tab_devicetype_info a "
						+ "where a.softwareversion is not null");
		HashMap<String, String> softwareversionMap = new HashMap<String, String>();
		Map fields = cursor.getNext();
		while (fields != null)
		{
			String softwareversion = (String) fields.get("softwareversion");
			if (false == StringUtil.IsEmpty(softwareversion))
			{
				softwareversionMap.put((String) fields.get("devicetype_id"),
						softwareversion);
			}
			else
			{
				softwareversionMap.put((String) fields.get("devicetype_id"),
						(String) fields.get("softwareversion"));
			}
			fields = cursor.getNext();
		}
		return softwareversionMap;
	}

	/**
	 * 获取上行方式
	 * 
	 * @return map
	 */
	@SuppressWarnings({ "rawtypes" })
	public Map getAccessType()
	{
		Cursor cursor = DataSetBean
				.getCursor("select type_id,type_name from gw_access_type where type_name is not null");
		HashMap<String, String> AccessMap = new HashMap<String, String>();
		Map fields = cursor.getNext();
		while (fields != null)
		{
			String type_name = (String) fields.get("type_name");
			if (false == StringUtil.IsEmpty(type_name))
			{
				AccessMap.put((String) fields.get("type_id"), type_name);
			}
			else
			{
				AccessMap.put((String) fields.get("type_id"),
						(String) fields.get("type_name"));
			}
			fields = cursor.getNext();
		}
		return AccessMap;
	}

	/**
	 * 根据上行方式ID获取上行方式名称
	 * 
	 * @param accessStyleRelayId
	 * @return String
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
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

	public static Logger getLogger()
	{
		return logger;
	}
}
