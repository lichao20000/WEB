/**
 *
 */
package com.linkage.module.bbms.report.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.gw.VendorModelVersionDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * 需求挖掘和预警DAO
 * @author chenjie
 * @date 2012-12-04
 */
public class WarnReportDAO extends SuperDAO {

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(WarnReportDAO.class);

	private Map<String, String>  cityMap = null;
	private Map<String, String> servTypeMap = null;
	private Map<String, String> accessTypeMap = null;

	/**
	 * 查询需求挖掘
	 * @param cityId
	 * @param industry
	 * @param servTypeId
	 * @param deviceType
	 * @param adsl_hl
	 * @param flowMin
	 * @param flowMax
	 * @param startOpenDate
	 * @param endOpenDate
	 * @param onlinedateMin
	 * @param onlinedateMax
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> queryWarnRequireReport(String cityId, String industry, String servTypeId, String deviceType, String adsl_hl,
			String flowMin, String flowMax, String startOpenDate, String endOpenDate, String onlinedateMin, String onlinedateMax,
			int curPage_splitPage, int num_splitPage) {

		logger.debug("queryWarnRequireReport({})", new Object[]{cityId, industry, servTypeId, deviceType, adsl_hl, flowMin, flowMax, startOpenDate, endOpenDate, onlinedateMin, onlinedateMax, curPage_splitPage, num_splitPage});

		StringBuffer sb = new StringBuffer();
		sb.append("select user_id, username, industry, device_type, flow, onlinedate, city_id," +
				"serv_type_id, adsl_hl, flow_date from tab_egw_warn where adsl_hl=?");

		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sb.append(" and city_id in (").append(StringUtils.weave(cityIdList)).append(")");
		}
		if(!StringUtil.IsEmpty(industry))
		{
			sb.append(" and industry='" + industry + "'");
		}
		if(!StringUtil.IsEmpty(deviceType))
		{
			sb.append(" and device_type='" + deviceType + "'");
		}
		if(!StringUtil.IsEmpty(servTypeId))
		{
			sb.append(" and serv_type_id=" + servTypeId);
		}
		if(!StringUtil.IsEmpty(flowMin))
		{
			sb.append(" and flow>=" + flowMin);
		}
		if(!StringUtil.IsEmpty(flowMax))
		{
			sb.append(" and flow<=" + flowMax);
		}
		if(!StringUtil.IsEmpty(startOpenDate))
		{
			sb.append(" and flow_date>=" + startOpenDate);
		}
		if(!StringUtil.IsEmpty(endOpenDate))
		{
			sb.append(" and flow_date<=" + endOpenDate);
		}
		if(!StringUtil.IsEmpty(onlinedateMin))
		{
			sb.append(" and onlinedate>=" + onlinedateMin);
		}
		if(!StringUtil.IsEmpty(onlinedateMax))
		{
			sb.append(" and onlinedate<=" + onlinedateMax);
		}

		PrepareSQL psql = new PrepareSQL(sb.toString());
		psql.setInt(1, Integer.parseInt(adsl_hl));

		// 获取地市信息
		cityMap = CityDAO.getCityIdCityNameMap();
		// servType信息
		servTypeMap = getServType();
		accessTypeMap = getAccessType();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("user_id", StringUtil.getStringValue(rs.getLong("user_id")));
				map.put("username", rs.getString("username"));
				map.put("industry", rs.getString("industry"));
				map.put("device_type", rs.getString("device_type"));
				map.put("flow", StringUtil.getStringValue(rs.getDouble("flow")));
				map.put("onlinedate", StringUtil.getStringValue(rs.getLong("onlinedate")));

				// 转换city名称
				String city_id = rs.getString("city_id");
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if(!StringUtil.IsEmpty(city_name))
				{
					map.put("city_name", city_name);
				}
				else
				{
					map.put("city_name", "");
				}

				// 转换servType
				long serv_type_id = rs.getLong("serv_type_id");
				map.put("serv_type_id", StringUtil.getStringValue(serv_type_id));
				String tmp = "-";
				if(!StringUtil.IsEmpty(StringUtil.getStringValue(serv_type_id)))
				{
					tmp = servTypeMap.get(StringUtil.getStringValue(serv_type_id));
				}
				map.put("serv_type", tmp);

				// 转化accessType
				long adsl_hl_value = rs.getLong("adsl_hl");
				map.put("adsl_hl", StringUtil.getStringValue(adsl_hl_value));
				tmp = "-";
				if(!StringUtil.IsEmpty(StringUtil.getStringValue(adsl_hl_value)))
				{
					tmp = accessTypeMap.get(StringUtil.getStringValue(adsl_hl_value));
				}
				map.put("adsl_hl_str", tmp);

				// 将flow_date转换成时间
				try
				{
					long flow_date = rs.getLong("flow_date");
					DateTimeUtil dt = new DateTimeUtil(flow_date * 1000);
					map.put("flow_date", dt.getLongDate());
				}
				catch (NumberFormatException e)
				{
					map.put("flow_date", "");
				}
				catch (Exception e)
				{
					map.put("flow_date", "");
				}
				return map;
			}
		});

		return list;
	}

	/**
	 * 需求挖掘查询结果count
	 * @param cityId
	 * @param industry
	 * @param servTypeId
	 * @param deviceType
	 * @param adsl_hl
	 * @param flowMin
	 * @param flowMax
	 * @param startOpenDate
	 * @param endOpenDate
	 * @param onlinedateMin
	 * @param onlinedateMax
	 * @return
	 */
	public int countWarnRequireReport(String cityId, String industry, String servTypeId, String deviceType, String adsl_hl,
			String flowMin, String flowMax, String startOpenDate, String endOpenDate, String onlinedateMin, String onlinedateMax,
			int curPage_splitPage, int num_splitPage)
	{
		logger.debug("countWarnRequireReport({})", new Object[]{cityId, industry, servTypeId, deviceType, adsl_hl, flowMin, flowMax, startOpenDate, endOpenDate, onlinedateMin, onlinedateMax, curPage_splitPage, num_splitPage});

		StringBuffer sb = new StringBuffer();
		sb.append("select count(*) from tab_egw_warn where adsl_hl=?");

		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sb.append(" and city_id in (").append(StringUtils.weave(cityIdList)).append(")");
		}
		if(!StringUtil.IsEmpty(industry))
		{
			sb.append(" and industry='" + industry + "'");
		}
		if(!StringUtil.IsEmpty(deviceType))
		{
			sb.append(" and device_type='" + deviceType + "'");
		}
		if(!StringUtil.IsEmpty(servTypeId))
		{
			sb.append(" and serv_type_id=" + servTypeId);
		}
		if(!StringUtil.IsEmpty(flowMin))
		{
			sb.append(" and flow>=" + flowMin);
		}
		if(!StringUtil.IsEmpty(flowMax))
		{
			sb.append(" and flow<=" + flowMax);
		}
		if(!StringUtil.IsEmpty(startOpenDate))
		{
			sb.append(" and flow_date>=" + startOpenDate);
		}
		if(!StringUtil.IsEmpty(endOpenDate))
		{
			sb.append(" and flow_date<=" + endOpenDate);
		}
		if(!StringUtil.IsEmpty(onlinedateMin))
		{
			sb.append(" and onlinedate>=" + onlinedateMin);
		}
		if(!StringUtil.IsEmpty(onlinedateMax))
		{
			sb.append(" and onlinedate<=" + onlinedateMax);
		}

		PrepareSQL psql = new PrepareSQL(sb.toString());
		psql.setInt(1, Integer.parseInt(adsl_hl));

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

	@SuppressWarnings("unchecked")
	public Map<String, String> getServType()
	{
		logger.debug("getServType()");
		String sql = "select serv_type_id,serv_type_name from tab_gw_serv_type";
		PrepareSQL psql = new PrepareSQL(sql);
		List<Map> list = jt.queryForList(psql.getSQL());
		Map servTypeMap = new HashMap<String, String>();
		for (Map map : list)
		{
			servTypeMap.put(StringUtil.getStringValue(map.get("serv_type_id")),
					StringUtil.getStringValue(map.get("serv_type_name")).toUpperCase());
		}
		return servTypeMap;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getAccessType()
	{
		logger.debug("getAccessType();");
		String sql = "select type_id, type_name from gw_access_type";
		PrepareSQL psql = new PrepareSQL(sql);
		List<Map> list = jt.queryForList(psql.getSQL());
		Map accessType = new HashMap<String, String>();
		for(Map map : list)
		{
			accessType.put(StringUtil.getStringValue(map.get("type_id")),
					StringUtil.getStringValue(map.get("type_name")).toUpperCase());
		}
		return accessType;
	}

	/**
	 * 查询维挽预警
	 * @param cityId
	 * @param linkphone
	 * @param customerAddress
	 * @param productType
	 * @param vendorId
	 * @param adsl_hl
	 * @param industry
	 * @param deviceType
	 * @param warningReason
	 * @param startOpenDate1
	 * @param endOpenDate1
	 * @param startWarningDate1
	 * @param endWarningDate1
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> queryWarnReport(String cityId, String linkphone, String customerAddress, String productType, String vendorId, String adsl_hl, String industry, String deviceType, String warningReason, String startOpenDate1, String endOpenDate1, String startWarningDate1, String endWarningDate1, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("queryWarnReport({})", new Object[]{cityId, linkphone, customerAddress, productType, vendorId, adsl_hl, industry, deviceType, warningReason, startOpenDate1, endOpenDate1, startWarningDate1, endWarningDate1, curPage_splitPage, num_splitPage});

		StringBuffer sb = new StringBuffer();
		sb.append("select user_id, username, linkphone, industry, device_type, product_type, customer_address, " +
				"warning_reason, city_id, vendor_id, adsl_hl, warning_date, opendate from tab_egw_warn where adsl_hl=? and vendor_id=?");

		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sb.append(" and city_id in (").append(StringUtils.weave(cityIdList)).append(")");
		}
		if(!StringUtil.IsEmpty(linkphone))
		{
			sb.append(" and linkphone='" + linkphone + "'");
		}
		if(!StringUtil.IsEmpty(customerAddress))
		{
			sb.append(" and customer_address='" + customerAddress + "'");
		}
		if(!StringUtil.IsEmpty(productType))
		{
			sb.append(" and product_type='" + productType + "'");
		}
		if(!StringUtil.IsEmpty(industry))
		{
			sb.append(" and industry='" + industry + "'");
		}
		if(!StringUtil.IsEmpty(deviceType))
		{
			sb.append(" and device_type='" + deviceType + "'");
		}
		if(!StringUtil.IsEmpty(warningReason))
		{
			sb.append(" and warning_reason='" + warningReason + "'");
		}
		if(!StringUtil.IsEmpty(startOpenDate1))
		{
			sb.append(" and opendate>=" + startOpenDate1);
		}
		if(!StringUtil.IsEmpty(endOpenDate1))
		{
			sb.append(" and opendate<=" + endOpenDate1);
		}
		if(!StringUtil.IsEmpty(startWarningDate1))
		{
			sb.append(" and warning_date>=" + startWarningDate1);
		}
		if(!StringUtil.IsEmpty(endWarningDate1))
		{
			sb.append(" and warning_date<=" + endWarningDate1);
		}

		PrepareSQL psql = new PrepareSQL(sb.toString());
		psql.setInt(1, Integer.parseInt(adsl_hl));
		psql.setString(2, vendorId);

		// 获取地市信息
		cityMap = CityDAO.getCityIdCityNameMap();
		// servType信息
		//servTypeMap = getServType();
		accessTypeMap = getAccessType();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("user_id", StringUtil.getStringValue(rs.getLong("user_id")));
				map.put("username", rs.getString("username"));
				map.put("linkphone", rs.getString("linkphone"));
				map.put("industry", rs.getString("industry"));
				map.put("device_type", rs.getString("device_type"));
				map.put("product_type", rs.getString("product_type"));
				map.put("customer_address", rs.getString("customer_address"));
				map.put("warning_reason", rs.getString("warning_reason"));

				// 转换city名称
				String city_id = rs.getString("city_id");
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if(!StringUtil.IsEmpty(city_name))
				{
					map.put("city_name", city_name);
				}
				else
				{
					map.put("city_name", "");
				}

				// 转换vendor名称
				String vendor_id = rs.getString("vendor_id");
				map.put("vendor_id", vendor_id);
				String vendor_name =  VendorModelVersionDAO.getVendorMap().get(vendor_id);
				map.put("vendor_name", vendor_name);

				// 转化accessType
				long adsl_hl_value = rs.getLong("adsl_hl");
				map.put("adsl_hl", StringUtil.getStringValue(adsl_hl_value));
				String tmp = "-";
				if(!StringUtil.IsEmpty(StringUtil.getStringValue(adsl_hl_value)))
				{
					tmp = accessTypeMap.get(StringUtil.getStringValue(adsl_hl_value));
				}
				map.put("adsl_hl_str", tmp);

				// 将warning_date转换成时间
				try
				{
					long warning_date = rs.getLong("warning_date");
					DateTimeUtil dt = new DateTimeUtil(warning_date * 1000);
					map.put("warning_date", dt.getLongDate());
				}
				catch (NumberFormatException e)
				{
					map.put("warning_date", "");
				}
				catch (Exception e)
				{
					map.put("warning_date", "");
				}

				// 将opendate转换成时间
				try
				{
					long opendate = rs.getLong("opendate");
					DateTimeUtil dt = new DateTimeUtil(opendate * 1000);
					map.put("opendate", dt.getLongDate());
				}
				catch (NumberFormatException e)
				{
					map.put("opendate", "");
				}
				catch (Exception e)
				{
					map.put("opendate", "");
				}

				return map;
			}
		});
		return list;
	}

	/**
	 * 查询维挽预警count
	 * @param cityId
	 * @param linkphone
	 * @param customerAddress
	 * @param productType
	 * @param vendorId
	 * @param adsl_hl
	 * @param industry
	 * @param deviceType
	 * @param warningReason
	 * @param startOpenDate1
	 * @param endOpenDate1
	 * @param startWarningDate1
	 * @param endWarningDate1
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int countWarnReport(String cityId, String linkphone, String customerAddress, String productType, String vendorId, String adsl_hl, String industry, String deviceType, String warningReason, String startOpenDate1, String endOpenDate1, String startWarningDate1, String endWarningDate1, int curPage_splitPage, int num_splitPage) {
		logger.debug("countWarnReport({})", new Object[]{cityId, linkphone, customerAddress, productType, vendorId, adsl_hl, industry, deviceType, warningReason, startOpenDate1, endOpenDate1, startWarningDate1, endWarningDate1, curPage_splitPage, num_splitPage});

		StringBuffer sb = new StringBuffer();
		sb.append("select count(*) from tab_egw_warn where adsl_hl=? and vendor_id=?");

		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sb.append(" and city_id in (").append(StringUtils.weave(cityIdList)).append(")");
		}
		if(!StringUtil.IsEmpty(linkphone))
		{
			sb.append(" and linkphone='" + linkphone + "'");
		}
		if(!StringUtil.IsEmpty(customerAddress))
		{
			sb.append(" and customer_address='" + customerAddress + "'");
		}
		if(!StringUtil.IsEmpty(productType))
		{
			sb.append(" and product_type='" + productType + "'");
		}
		if(!StringUtil.IsEmpty(industry))
		{
			sb.append(" and industry='" + industry + "'");
		}
		if(!StringUtil.IsEmpty(deviceType))
		{
			sb.append(" and device_type='" + deviceType + "'");
		}
		if(!StringUtil.IsEmpty(warningReason))
		{
			sb.append(" and warning_reason='" + warningReason + "'");
		}
		if(!StringUtil.IsEmpty(startOpenDate1))
		{
			sb.append(" and opendate>=" + startOpenDate1);
		}
		if(!StringUtil.IsEmpty(endOpenDate1))
		{
			sb.append(" and opendate<=" + endOpenDate1);
		}
		if(!StringUtil.IsEmpty(startWarningDate1))
		{
			sb.append(" and warning_date>=" + startWarningDate1);
		}
		if(!StringUtil.IsEmpty(endWarningDate1))
		{
			sb.append(" and warning_date<=" + endWarningDate1);
		}

		PrepareSQL psql = new PrepareSQL(sb.toString());
		psql.setInt(1, Integer.parseInt(adsl_hl));
		psql.setString(2, vendorId);

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
}
