
package com.linkage.module.gtms.stb.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * @author wuchao(工号) Tel:1
 * @version 1.0
 * @since 2012-2-28 上午09:49:54
 * @category com.linkage.module.lims.stb.resource.dao
 * @copyright 南京联创科技 网管科技部
 */
public class ConfigInfoDAO extends SuperDAO
{

	public List<Map> deviceIds(String servAccount, String serialnumber)
	{
		StringBuffer sb = new StringBuffer();
		String sql = "select a.device_id,b.customer_id from stb_tab_gw_device a, stb_tab_customer b where a.customer_id=b.customer_id ";
		sb.append(sql);
		if (servAccount != null && !"".equals(servAccount))
		{
			sb.append(" and b.serv_account='" + servAccount + "'");
		}
		if (serialnumber != null && !"".equals(serialnumber))
		{
			sb.append(" and a.device_serialnumber= '" + serialnumber + "'");
		}
		PrepareSQL psql = new PrepareSQL(sb.toString());
		return jt.queryForList(psql.getSQL());
	}

	public List<Map> query(String servAccount, String serialnumber,
			List<Map> deviceIdsList, long startTime, long endTime, int curPage_splitPage,
			int num_splitPage)
	{
		StringBuffer ids = new StringBuffer();
		ids.append("");
		for (int i = 0; i < deviceIdsList.size(); i++)
		{
			if (i + 1 == deviceIdsList.size())
			{
				ids.append("'"
						+ StringUtil
								.getStringValue(deviceIdsList.get(i).get("device_id"))
						+ "'");
			}
			else
			{
				ids.append("'"
						+ StringUtil
								.getStringValue(deviceIdsList.get(i).get("device_id"))
						+ "',");
			}
		}// TODO wait (more table related)
		String sql = "select a.device_serialnumber, f.* from (select d.device_id, d.gather_time, c.pppoe_id, c.pppoe_pwd, c.iptv_dhcp_username,"
				+ " c.iptv_dhcp_password,c.auth_url,c.user_id,c.user_pwd,d.ip from stb_lan_recent d left join  stb_x_serv_info_recent c on"
				+ " (d.device_id=c.device_id and d.gather_time=c.gather_time and  d.gather_time >=? and d.gather_time <=? and "
				+ " c.gather_time >=? and c.gather_time <=?))f, stb_tab_gw_device a where a.device_id = f.device_id and a.device_id in(?) and f.gather_time >=? and f.gather_time <= "
				+ endTime;
		PrepareSQL psql = new PrepareSQL();
		psql.append(sql);
		psql.setLong(1, startTime);
		psql.setLong(2, endTime);
		psql.setLong(3, startTime);
		psql.setLong(4, endTime);
		psql.setStringExt(5, ids.toString(), false);
		psql.setLong(6, startTime);
		// psql.setLong(7, endTime);
		return querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage,
				num_splitPage, new RowMapper()
				{

					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						map.put("gather_time", formatTime(StringUtil.getLongValue(rs
								.getString("gather_time"))));
						map.put("gather_time_bak", rs.getString("gather_time"));
						map.put("device_serialnumber",
								rs.getString("device_serialnumber"));
						map.put("user_id", rs.getString("user_id"));
						map.put("user_pwd", rs.getString("user_pwd"));
						map.put("pppoe_id", rs.getString("pppoe_id"));
						map.put("pppoe_pwd", rs.getString("pppoe_pwd"));
						if (null == rs.getString("iptv_dhcp_username")
								|| "not_set".equals(rs.getString("iptv_dhcp_username")))
						{
							map.put("iptv_dhcp_username", "");
						}
						else
						{
							map.put("iptv_dhcp_username",
									rs.getString("iptv_dhcp_username"));
						}
						if (null == rs.getString("iptv_dhcp_password")
								|| "not_set".equals(rs.getString("iptv_dhcp_password")))
						{
							map.put("iptv_dhcp_password", "");
						}
						else
						{
							map.put("iptv_dhcp_password",
									rs.getString("iptv_dhcp_password"));
						}
						map.put("auth_url", rs.getString("auth_url"));
						map.put("ip_address", rs.getString("ip"));
						map.put("device_id", rs.getString("device_id"));
						return map;
					}
				});
	}

	public int getCount(String servAccount, String serialnumber, List<Map> deviceIdsList,
			long startTime, long endTime, int curPage_splitPage, int num_splitPage)
	{
		StringBuffer ids = new StringBuffer();
		ids.append("");
		for (int i = 0; i < deviceIdsList.size(); i++)
		{
			if (i + 1 == deviceIdsList.size())
			{
				ids.append("'"
						+ StringUtil
								.getStringValue(deviceIdsList.get(i).get("device_id"))
						+ "'");
			}
			else
			{
				ids.append("'"
						+ StringUtil
								.getStringValue(deviceIdsList.get(i).get("device_id"))
						+ "',");
			}
		}// TODO wait (more table related)
		String sql = "select count(*) from (select d.device_id, d.gather_time, c.pppoe_id, c.pppoe_pwd, c.iptv_dhcp_username,"
				+ " c.iptv_dhcp_password,c.auth_url,c.user_id,c.user_pwd,d.ip from stb_lan_recent d left join  stb_x_serv_info_recent c on"
				+ " (d.device_id=c.device_id and d.gather_time=c.gather_time and  d.gather_time >=? and d.gather_time <=? and "
				+ " c.gather_time >=? and c.gather_time <=?))f, stb_tab_gw_device a where a.device_id = f.device_id and a.device_id in(?) and f.gather_time >=? and f.gather_time <="
				+ endTime;
		PrepareSQL psql = new PrepareSQL();
		psql.append(sql);
		psql.setLong(1, startTime);
		psql.setLong(2, endTime);
		psql.setLong(3, startTime);
		psql.setLong(4, endTime);
		psql.setStringExt(5, ids.toString(), false);
		psql.setLong(6, startTime);
		// psql.setLong(7, endTime);
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

	public String formatTime(long time)
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(time * 1000);
	}

	public Map queryDetail(String device_id, long startTime, long endTime)
	{
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
		String sql1 = "select d.device_serialnumber,d.city_id ,g.device_model  from stb_tab_gw_device d, stb_gw_device_model g where  d.device_model_id = g.device_model_id and d.device_id=?";
		PrepareSQL psql1 = new PrepareSQL(sql1);
		psql1.setString(1, device_id);
		List<Map> list1 = jt.queryForList(psql1.getSQL());
		if (null != list1)
		{
			map.put("device_serialnumber",
					StringUtil.getStringValue(list1.get(0).get("device_serialnumber")));
			map.put("city_name", cityMap.get(list1.get(0).get("city_id")));
			map.put("device_model",
					StringUtil.getStringValue(list1.get(0).get("device_model")));
		}
		String sql2 = "select l.ip,l.mask,l.gateway,l.dns,l.mac,l.address_type from stb_lan_recent l where l.device_id=? and l.gather_time>"
				+ startTime + " and l.gather_time<" + endTime;
		PrepareSQL psql2 = new PrepareSQL(sql2);
		psql2.setString(1, device_id);
		List<Map> list2 = jt.queryForList(psql2.getSQL());
		if (null != list2 && list2.size() > 0)
		{
			map.put("ip", StringUtil.getStringValue(list2.get(0).get("ip")));
			map.put("mask", StringUtil.getStringValue(list2.get(0).get("mask")));
			map.put("gateway", StringUtil.getStringValue(list2.get(0).get("gateway")));
			map.put("dns", StringUtil.getStringValue(list2.get(0).get("dns")));
			map.put("mac", StringUtil.getStringValue(list2.get(0).get("mac")));
			map.put("address_type",
					StringUtil.getStringValue(list2.get(0).get("address_type")));
		}
		String sql3 = "select x.pppoe_id,x.pppoe_pwd,x.iptv_dhcp_username,x.iptv_dhcp_password,x.user_id,x.user_pwd,x.auth_url from stb_x_serv_info_recent x where x.device_id=? and x.gather_time>"
				+ startTime + " and x.gather_time<" + endTime;
		PrepareSQL psql3 = new PrepareSQL(sql3);
		psql3.setString(1, device_id);
		List<Map> list3 = jt.queryForList(psql3.getSQL());
		if (null != list3 && list3.size() > 0)
		{
			String pppoe_id = StringUtil.getStringValue(list3.get(0).get("pppoe_id"));
			map.put("pppoe_id", pppoe_id);
			map.put("pppoe_pwd", StringUtil.getStringValue(list3.get(0).get("pppoe_pwd")));
			map.put("iptv_dhcp_username",
					StringUtil.getStringValue(list3.get(0).get("iptv_dhcp_username")));
			map.put("iptv_dhcp_password",
					StringUtil.getStringValue(list3.get(0).get("iptv_dhcp_password")));
			String user_id = StringUtil.getStringValue(list3.get(0).get("user_id"));
			String user_pwd = StringUtil.getStringValue(list3.get(0).get("user_id"));
			if ("".equals(user_id) || "not_set".equals(user_id))
			{
				user_id = "";
			}
			if ("".equals(user_pwd) || "not_set".equals(user_pwd))
			{
				user_pwd = "";
			}
			map.put("user_id", user_id);
			map.put("user_pwd", user_pwd);
			map.put("auth_url", StringUtil.getStringValue(list3.get(0).get("auth_url")));
		}
		String sql4 = "select i.auto_update_serv from stb_user_itfs_recent i where i.device_id=? and i.gather_time>"
				+ startTime + " and i.gather_time<" + endTime;
		PrepareSQL psql4 = new PrepareSQL(sql4);
		psql4.setString(1, device_id);
		List<Map> list4 = jt.queryForList(psql4.getSQL());
		if (null != list4 && list4.size() > 0)
		{
			map.put("auto_update_serv",
					StringUtil.getStringValue(list4.get(0).get("auto_update_serv")));
		}
		String sql5 = "select f.software_version from stb_device_info_recent f where f.device_id=? and f.gather_time>"
				+ startTime + " and f.gather_time<" + endTime;
		PrepareSQL psql5 = new PrepareSQL(sql5);
		psql5.setString(1, device_id);
		List<Map> list5 = jt.queryForList(psql5.getSQL());
		if (null != list5 && list5.size() > 0)
		{
			map.put("software_version",
					StringUtil.getStringValue(list5.get(0).get("software_version")));
		}
		map.put("networkAddress", LipossGlobals.getLipossProperty("networkAddress"));
		// 制式与频显 查询最新记录表
		String sql6 = "select composite_video_standard, aspect_ratio from stb_capa_recent s where device_id= ? and s.gather_time>"
				+ startTime + " and s.gather_time<" + endTime;
		PrepareSQL psql6 = new PrepareSQL(sql6);
		psql6.setString(1, device_id);
		List<Map> list6 = jt.queryForList(psql6.getSQL());
		if (null != list6 && list6.size() > 0)
		{
			map.put("composite_video_standard", StringUtil.getStringValue(list6.get(0)
					.get("composite_video_standard")));
			map.put("aspect_ratio",
					StringUtil.getStringValue(list6.get(0).get("aspect_ratio")));
		}
		return map;
	}
}
