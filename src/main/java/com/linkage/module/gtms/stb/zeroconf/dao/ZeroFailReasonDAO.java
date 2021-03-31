
package com.linkage.module.gtms.stb.zeroconf.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gtms.stb.zeroconf.dto.ZeroFailReasonDTO;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.system.utils.StringUtils;

public class ZeroFailReasonDAO extends SuperDAO
{

	public static Logger log = Logger.getLogger(ZeroFailReasonDAO.class);

	@SuppressWarnings("rawtypes")
	private void SQLAppend(ZeroFailReasonDTO dto, StringBuffer sql)
	{
		if (null != dto.getBeginTime())
		{
			sql.append(" and b.fail_time>=").append(dto.getBeginTime());
		}
		if (null != dto.getEndTime())
		{
			sql.append(" and b.fail_time<=").append(dto.getEndTime());
		}
		if (false == StringUtil.IsEmpty(dto.getCityId()) && !"00".equals(dto.getCityId()))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(dto.getCityId());
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		sql.append(" group by a.city_id");
	}

	@SuppressWarnings("rawtypes")
	// 成功
	public Map getZeroFailSuccessNum(ZeroFailReasonDTO dto)
	{
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,count(*) cnt from stb_tab_zeroconfig_fail b,stb_tab_gw_device a where ");
		if(DBUtil.GetDB() == Global.DB_MYSQL)
		{// mysql
			sql.append(" a.device_id = cast(b.device_id as char) ");
		}else{
			sql.append(" a.device_id = convert(varchar,b.device_id) ");
		}
		sql.append(" and b.fail_reason_id=0  ");
		SQLAppend(dto, sql);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("cnt")));
			}
		}
		log.warn(map);
		return map;
	}

	@SuppressWarnings("rawtypes")
	// E8-C终端未上报该机顶盒MAC
	public Map getE8CNoUpMACNum(ZeroFailReasonDTO dto)
	{
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,count(*) cnt from stb_tab_zeroconfig_fail b,stb_tab_gw_device a where ");
		if(DBUtil.GetDB() == Global.DB_MYSQL)
		{// mysql
			sql.append(" a.device_id = cast(b.device_id as char) ");
		}else{
			sql.append(" a.device_id = convert(varchar,b.device_id) ");
		}
		sql.append(" and b.fail_reason_id=1 ");
		SQLAppend(dto, sql);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("cnt")));
			}
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	// E8-C上报机顶盒MAC异常（含绑定多个机顶盒MAC）
	public Map getE8CUpMACExceptionNum(ZeroFailReasonDTO dto)
	{
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,count(*) cnt from stb_tab_zeroconfig_fail b,stb_tab_gw_device a where ");
		if(DBUtil.GetDB() == Global.DB_MYSQL)
		{// mysql
			sql.append(" a.device_id = cast(b.device_id as char) ");
		}else{
			sql.append(" a.device_id = convert(varchar,b.device_id) ");
		}
		sql.append(" and b.fail_reason_id=2 ");
		SQLAppend(dto, sql);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("cnt")));
			}
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	// IPTV账号不匹配
	public Map getIPTVAccountNoMatchNum(ZeroFailReasonDTO dto)
	{
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,count(*) cnt from stb_tab_zeroconfig_fail b,stb_tab_gw_device a where ");
		if(DBUtil.GetDB() == Global.DB_MYSQL)
		{// mysql
			sql.append(" a.device_id = cast(b.device_id as char) ");
		}else{
			sql.append(" a.device_id = convert(varchar,b.device_id) ");
		}
		sql.append(" and b.fail_reason_id=3 ");
		SQLAppend(dto, sql);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("cnt")));
			}
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	// AAA查询不到宽带账号拨号信息
	public Map getAAANotFindAccountNum(ZeroFailReasonDTO dto)
	{
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,count(*) cnt from stb_tab_zeroconfig_fail b,stb_tab_gw_device a where ");
		if(DBUtil.GetDB() == Global.DB_MYSQL)
		{// mysql
			sql.append(" a.device_id = cast(b.device_id as char) ");
		}else{
			sql.append(" a.device_id = convert(varchar,b.device_id) ");
		}
		sql.append(" and b.fail_reason_id=4 ");
		SQLAppend(dto, sql);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("cnt")));
			}
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	// AAA反馈宽带账号信息匹配失败
	public Map getAAABackInfoErrorNum(ZeroFailReasonDTO dto)
	{
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,count(*) cnt from stb_tab_zeroconfig_fail b,stb_tab_gw_device a where ");
		if(DBUtil.GetDB() == Global.DB_MYSQL)
		{// mysql
			sql.append(" a.device_id = cast(b.device_id as char) ");
		}else{
			sql.append(" a.device_id = convert(varchar,b.device_id) ");
		}
		sql.append(" and b.fail_reason_id=5 ");
		SQLAppend(dto, sql);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("cnt")));
			}
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	public List getZeroDeviceList(int curPage_splitPage, int num_splitPage,
			ZeroFailReasonDTO dto)
	{
		PrepareSQL pSql = new PrepareSQL();// TODO wait (more table related)
		pSql.setSQL("select a.vendor_id, a.device_model_id, a.devicetype_id, a.city_id, a.device_serialnumber, a.device_id," +
				"a.oui, a.device_name, a.cpe_mac, a.loopback_ip, a.complete_time " +
				"b.addressing_type, a.serv_account, b.cust_account,d.reason_desc " +
				"from stb_tab_gw_device a left join stb_tab_customer b " +
				"on a.customer_id = b.customer_id, stb_tab_zeroconfig_fail c,stb_tab_zeroconfig_reason d where  ");
		if(DBUtil.GetDB() == Global.DB_MYSQL)
		{// mysql
			pSql.append(" a.device_id = cast(c.device_id as char) ");
		}else{
			pSql.append(" a.device_id = convert(varchar, c.device_id) ");
		}
		pSql.append(" and c.fail_reason_id = d.reason_id ");
		SQLAppend(dto, pSql);
		if (-1 != curPage_splitPage)
		{
			return querySP(pSql.toString(), (curPage_splitPage - 1) * num_splitPage,
					num_splitPage, new RowMapper()
					{

						List<Map> vendorList = queryAllVendor();
						List<Map> modelList = queryAllModel();
						List<Map> typeInfoList = queryAllTypeInfo();

						public Object mapRow(ResultSet rs, int arg1) throws SQLException
						{
							Map<String, String> map = new HashMap<String, String>();
							return resultSet2Map(map, rs, vendorList, modelList,
									typeInfoList);
						}
					});
		}
		else
		{
			return jt.query(pSql.toString(), new RowMapper()
			{

				List<Map> vendorList = queryAllVendor();
				List<Map> modelList = queryAllModel();
				List<Map> typeInfoList = queryAllTypeInfo();

				public Object mapRow(ResultSet rs, int arg1) throws SQLException
				{
					Map<String, String> map = new HashMap<String, String>();
					return resultSet2Map(map, rs, vendorList, modelList, typeInfoList);
				}
			});
		}
	}

	@SuppressWarnings("rawtypes")
	private void SQLAppend(ZeroFailReasonDTO dto, PrepareSQL pSql)
	{
		if (null != dto.getBeginTime())
		{
			pSql.append(" and c.fail_time>=" + dto.getBeginTime());
		}
		if (null != dto.getEndTime())
		{
			pSql.append(" and c.fail_time<=" + dto.getEndTime());
		}
		if (null != dto.getCityId() && !"00".equals(dto.getCityId()))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(dto.getCityId());
			pSql.append(" and a.city_id in (" + StringUtils.weave(cityIdList) + ") ");
			cityIdList = null;
		}
		if (null != dto.getFailReasonId())
		{
			pSql.append(" and c.fail_reason_id = " + dto.getFailReasonId());
		}
	}

	public int getZeroDeviceListCount(ZeroFailReasonDTO dto)
	{
		PrepareSQL pSql = new PrepareSQL();// TODO wait (more table related)
		pSql.setSQL("select count(*) from stb_tab_gw_device a left join stb_tab_customer b on a.customer_id = b.customer_id, stb_tab_zeroconfig_fail c,stb_tab_zeroconfig_reason d "
				+ "where a.device_id = convert(varchar,c.device_id) and c.fail_reason_id = d.reason_id  ");
		SQLAppend(dto, pSql);
		return jt.queryForInt(pSql.toString());
	}

	@SuppressWarnings("rawtypes")
	private List<Map> queryAllVendor()
	{
		PrepareSQL pSql = new PrepareSQL();
		pSql.setSQL("select vendor_id, vendor_add from stb_tab_vendor");
		return querySP(pSql.toString(), 0, 1000);
	}

	@SuppressWarnings("rawtypes")
	private List<Map> queryAllModel()
	{
		PrepareSQL pSql = new PrepareSQL();
		pSql.setSQL("select device_model_id, device_model from stb_gw_device_model");
		return querySP(pSql.toString(), 0, 10000);
	}

	@SuppressWarnings("rawtypes")
	private List<Map> queryAllTypeInfo()
	{
		PrepareSQL pSql = new PrepareSQL();
		pSql.setSQL("select devicetype_id, softwareversion from stb_tab_devicetype_info");
		return querySP(pSql.toString(), 0, 10000);
	}

	@SuppressWarnings("rawtypes")
	public Map<String, String> resultSet2Map(Map<String, String> map, ResultSet rs,
			List<Map> vendorList, List<Map> modelList, List<Map> typeInfoList)
	{
		try
		{
			map.put("vendor_add",
					getVendorNameByVendorID(vendorList, rs.getString("vendor_id")));
			map.put("device_model",
					getModelNameByModelID(modelList, rs.getString("device_model_id")));
			map.put("softwareversion",
					getSoftVersionByTypeInfoID(typeInfoList,
							rs.getString("devicetype_id")));
			map.put("city_name",
					CityDAO.getCityIdCityNameMap().get(rs.getString("city_id")));
			map.put("device_serialnumber", rs.getString("device_serialnumber"));
			map.put("device_id", rs.getString("device_id"));
			map.put("oui", rs.getString("oui"));
			map.put("device_name", rs.getString("device_name"));
			map.put("city_id", rs.getString("city_id"));
			map.put("serv_account", rs.getString("serv_account"));
			map.put("cpe_mac", rs.getString("cpe_mac"));
			map.put("loopback_ip", rs.getString("loopback_ip"));
			map.put("addressing_type", rs.getString("addressing_type"));
			map.put("complete_time", new DateTimeUtil(rs.getLong("complete_time") * 1000)
					.getYYYY_MM_DD_HH_mm_ss());
			map.put("reason_desc", rs.getString("reason_desc"));
		}
		catch (SQLException e)
		{
			log.error(e.getMessage(), e);
		}
		return map;
	}

	/**
	 * 转换软件版本
	 *
	 * @param typeInfoList
	 * @param devicetypeID
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String getSoftVersionByTypeInfoID(List<Map> typeInfoList, String devicetypeID)
	{
		if (null == typeInfoList || null == devicetypeID
				|| "".equals(devicetypeID.trim()) || "null".equals(devicetypeID.trim()))
		{
			return devicetypeID;
		}
		for (Map map : typeInfoList)
		{
			if (devicetypeID.equals(StringUtil.getStringValue(map, "devicetype_id")))
			{
				return StringUtil.getStringValue(map, "softwareversion");
			}
		}
		return devicetypeID;
	}

	@SuppressWarnings("rawtypes")
	public String getModelNameByModelID(List<Map> modelList, String modelID)
	{
		if (null == modelList || null == modelID || "".equals(modelID.trim())
				|| "null".equals(modelID.trim()))
		{
			return modelID;
		}
		for (Map map : modelList)
		{
			if (modelID.equals(StringUtil.getStringValue(map, "device_model_id")))
			{
				return StringUtil.getStringValue(map, "device_model");
			}
		}
		return modelID;
	}

	@SuppressWarnings("rawtypes")
	public String getVendorNameByVendorID(List<Map> vendorList, String vendorID)
	{
		if (null == vendorList || null == vendorID || "".equals(vendorID.trim())
				|| "null".equals(vendorID.trim()))
		{
			return vendorID;
		}
		for (Map map : vendorList)
		{
			if (vendorID.equals(StringUtil.getStringValue(map, "vendor_id")))
			{
				return StringUtil.getStringValue(map, "vendor_add");
			}
		}
		return vendorID;
	}

	@SuppressWarnings("rawtypes")
	public Map getSuperNum(ZeroFailReasonDTO dto)
	{
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,count(*) cnt from stb_tab_zeroconfig_fail b,stb_tab_gw_device a "
				+ "where a.device_id = convert(varchar,b.device_id) ");
		SQLAppend(dto, sql);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("cnt")));
			}
		}
		return map;
	}
}
