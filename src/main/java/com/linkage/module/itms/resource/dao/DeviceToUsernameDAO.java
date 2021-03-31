
package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.resource.bio.HgwByMacBIO;

public class DeviceToUsernameDAO extends SuperDAO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(HgwByMacBIO.class);
	/**
	 * Map<bind_type_id,type_name>
	 */
	private Map<String, String> bindTypeMap = null;
	private String device_serialnumber;
	private String oui;
	private Map<String, String> isBindMap = new HashMap<String, String>();

	/**
	 * 通过设备查询账号
	 * 
	 * @author wangsenbo
	 * @param tabName 
	 * @date May 6, 2010
	 * @param
	 * @return List<Map>
	 */
	public List<Map> getHgwByDeviceList(
			String deviceId, String deviceSn, String oui1, int curPage_splitPage,
			int num_splitPage, String tabName)
	{
		logger.debug("getHgwByDeviceList({},{},{},{})", new Object[] { 
				 deviceId, deviceSn, curPage_splitPage, num_splitPage });
		StringBuffer sql = new StringBuffer();

		// teledb
		if (DBUtil.GetDB() == 3) {
			sql.append("select username, serv_type_id, inform_time, bind_type_id, is_bind from ").append(tabName).append(" where device_id='")
					.append(deviceId).append("' ");
		}
		else {
			sql.append("select * from ").append(tabName).append(" where device_id='")
					.append(deviceId).append("' ");
		}

//		if (false == StringUtil.IsEmpty(starttime1))
//		{
//			sql.append(" and inform_time>=").append(starttime1);
//		}
//		if (false == StringUtil.IsEmpty(endtime1))
//		{
//			sql.append(" and inform_time<").append(endtime1);
//		}
		sql.append(" order by inform_time");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		// 获取绑定方式列表
		getBindType();
		device_serialnumber = deviceSn;
		oui = oui1;
		isBindMap.put("-1", "非ADSL账号");
		isBindMap.put("0", "用户设备不匹配");
		isBindMap.put("1", "绑定成功");
		isBindMap.put("2", "无此用户");
		isBindMap.put("3", "用户谁失败");
		isBindMap.put("5", "已经绑定");
		List<Map> list = querySP(sql.toString(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				String device = oui + "-" + device_serialnumber;
				map.put("device", device);
				map.put("username", rs.getString("username"));
				String serv_type_id = rs.getString("serv_type_id");
				if ("10".equals(serv_type_id))
				{
					map.put("serv_type", "ADSL");
				}
				else if ("11".equals(serv_type_id))
				{
					map.put("serv_type", "IPTV");
				}
				else
				{
					map.put("serv_type", "");
				}
				// 将gather_time转换成时间 采集时间
				try
				{
					long inform_time = StringUtil.getLongValue(rs
							.getString("inform_time"));
					DateTimeUtil dt = new DateTimeUtil(inform_time * 1000);
					map.put("inform_time", dt.getLongDate());
				}
				catch (NumberFormatException e)
				{
					map.put("inform_time", "");
				}
				catch (Exception e)
				{
					map.put("inform_time", "");
				}
				String bind_type_id = rs.getString("bind_type_id");
				String bind_type = StringUtil.getStringValue(bindTypeMap
						.get(bind_type_id));
				if (false == StringUtil.IsEmpty(bind_type))
				{
					map.put("bind_type", bind_type);
				}
				else
				{
					map.put("bind_type", "");
				}
				String is_bind = rs.getString("is_bind");
				String isBind = StringUtil.getStringValue(isBindMap.get(is_bind));
				if (false == StringUtil.IsEmpty(isBind))
				{
					map.put("is_bind", isBind);
				}
				else
				{
					map.put("is_bind", "");
				}
				return map;
			}
		});
		bindTypeMap = null;
		isBindMap = null;
		return list;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getBindType()
	{
		String sql = "select * from bind_type";
		// teledb
		if (DBUtil.GetDB() == 3) {
			sql = "select bind_type_id, type_name from bind_type";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		List<Map> list = jt.queryForList(sql);
		bindTypeMap = new HashMap<String, String>();
		for (Map map : list)
		{
			bindTypeMap.put(StringUtil.getStringValue(map.get("bind_type_id")),
					StringUtil.getStringValue(map.get("type_name")));
		}
		bindTypeMap.put("-1", "未做绑定");
		bindTypeMap.put("-2", "无此用户");
		bindTypeMap.put("-3", "用户已经绑定");
		return bindTypeMap;
	}

	/**
	 * 通过设备查询账号统计条数
	 * 
	 * @author wangsenbo
	 * @param tabName 
	 * @date May 6, 2010
	 * @param
	 * @return List<Map>
	 */
	public int getHgwByDeviceCount(String deviceId,
			int curPage_splitPage, int num_splitPage, String tabName)
	{
		logger.debug("getHgwByDeviceCount({},{},{},{})", new Object[] {  deviceId, curPage_splitPage, num_splitPage });
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) from ").append(tabName).append(" where device_id='").append(
				deviceId).append("' ");
//		if (false == StringUtil.IsEmpty(starttime1))
//		{
//			sql.append(" and inform_time>=").append(starttime1);
//		}
//		if (false == StringUtil.IsEmpty(endtime1))
//		{
//			sql.append(" and inform_time<").append(endtime1);
//		}
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
	
	/**
	 * 判断报表是否存在
	 * 
	 * @author wangsenbo
	 * @date Mar 18, 2010
	 * @param
	 * @return int
	 */
	public int isHaveTable(String tableName)
	{
		String sql = "select name from sysobjects where name ='" + tableName + "'";
		if (DBUtil.GetDB() == 1)
		{// oracle
			sql = "select table_name as name from user_tables where table_name='"
					+ StringUtil.getUpperCase(tableName) + "'";
		}
		else if (DBUtil.GetDB() == 2)
		{// sybase
			sql = "select name from sysobjects where name ='" + tableName + "'";
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
    	psql.getSQL();
		Map map = queryForMap(sql);
		if (map == null||map.isEmpty()==true)
		{
			return 0;
		}
		else
		{
			return 1;
		}
	}
}
