package com.linkage.module.gtms.stb.config.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.linkage.litms.LipossGlobals;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;

@SuppressWarnings("unchecked")
public class BaseConfigDAO
{

	private JdbcTemplate jt;

	public Map getLAN(String deviceId)
	{
		String sql = "select address_type, ip, mask, gateway, dns, networkAddress, gather_time " +
				"from stb_lan where device_id='" + deviceId + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForMap(psql.getSQL());
	}

	/**
	 * 查询stb_lan最新记录表
	 *
	 * @param deviceId
	 * @return
	 */
	public Map getLANRecent(String deviceId)
	{
		String sql = "select address_type, ip, mask, gateway, dns, networkAddress, gather_time " +
				"from stb_lan_recent where device_id='" + deviceId + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForMap(psql.getSQL());
	}

	public Map getCmpstVideoAndAspRatio(String deviceId)
	{
		String sql = "select composite_video_standard, aspect_ratio from stb_capa where device_id='" + deviceId + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForMap(psql.getSQL());
	}

	public Map getX_CTC_IPTV_ServiceInfo(String deviceId)
	{
		String sql = "select pppoe_id, pppoe_pwd, user_id, user_pwd, auth_url, iptv_dhcp_username, iptv_dhcp_password " +
				"from stb_x_serv_info where device_id='" + deviceId + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForMap(psql.getSQL());
	}

	/**
	 * 查询stb_x_serv_info最新记录表
	 *
	 * @param deviceId
	 * @return
	 */
	public Map getX_CTC_IPTV_ServiceInfoRecent(String deviceId)
	{
		String sql = "select pppoe_id, pppoe_pwd, user_id, user_pwd, auth_url, iptv_dhcp_username, iptv_dhcp_password " +
				"from stb_x_serv_info_recent where device_id='" + deviceId + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForMap(psql.getSQL());
	}

	public Map getUserInterface(String deviceId)
	{
		String sql = "select auto_update_serv from stb_user_itfs where device_id='" + deviceId + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForMap(psql.getSQL());
	}

	/**
	 * 查询stb_user_itfs最新记录表
	 *
	 * @param device_id
	 * @return
	 */
	public Map getUserInterfaceRecent(String deviceId)
	{
		String sql = "select auto_update_serv from stb_user_itfs_recent where device_id='" + deviceId + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForMap(psql.getSQL());
	}

	/**
	 * 查询 stb_capa最新记录表
	 *
	 * @param deviceId
	 * @return map
	 */
	public Map getCmpstVideoAndAspRatioRecent(String deviceId)
	{
		Map<String, String> map = new HashMap<String, String>();
		// 制式与频显 查询最新记录表
		String sql = "select composite_video_standard, aspect_ratio from stb_capa_recent where device_id='" + deviceId + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		List<Map> list = jt.queryForList(psql.getSQL());
		if (null != list && list.size() > 0)
		{
			map.put("composite_video_standard", StringUtil.getStringValue(list.get(0)
					.get("composite_video_standard")));
			map.put("aspect_ratio",
					StringUtil.getStringValue(list.get(0).get("aspect_ratio")));
		}
		return map;
	}

	public void setDao(DataSource dao)
	{
		jt = new JdbcTemplate(dao);
	}
}
