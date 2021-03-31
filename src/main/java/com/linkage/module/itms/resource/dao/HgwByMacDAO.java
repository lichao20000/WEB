
package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.resource.bio.HgwByMacBIO;

public class HgwByMacDAO extends SuperDAO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(HgwByMacBIO.class);
	/**
	 * Map<city_id,city_name>
	 */
	private Map<String, String> cityMap = null;

	public List getHgwByMacList(String starttime1, String endtime1, String username,
			String mac, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("getHgwByMacList({},{},{},{},{})", new Object[] { starttime1,
				endtime1, curPage_splitPage, num_splitPage });
		StringBuffer sql = new StringBuffer();
		// teledb
		if (DBUtil.GetDB() == 3) {
			sql.append("select username, city_id, cpe_mac, dslam_ip, dslam_shelf, dslam_casing, dslam_slot, dslam_port, gather_time from gw_binded_by_mac where 1=1 ");
		}
		else {
			sql.append("select * from gw_binded_by_mac where 1=1 ");
		}
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and gather_time>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and gather_time<").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(username))
		{
			sql.append(" and username='").append(username).append("'");
		}
		if (false == StringUtil.IsEmpty(mac))
		{
			sql.append(" and cpe_mac='").append(mac).append("'");
		}
		sql.append(" order by city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = querySP(sql.toString(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("username", rs.getString("username"));
				String city_id = rs.getString("city_id");
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name))
				{
					map.put("city_name", city_name);
				}
				else
				{
					map.put("city_name", "");
				}
				map.put("cpe_mac", rs.getString("cpe_mac"));
				map.put("dslam_ip", rs.getString("dslam_ip"));
				map.put("dslam_shelf", rs.getString("dslam_shelf"));// DSLAM机架
				map.put("dslam_casing", rs.getString("dslam_casing"));// DSLAM框
				map.put("dslam_slot", rs.getString("dslam_slot"));// DSLAM槽
				map.put("dslam_port", rs.getString("dslam_port"));// DSLAM端口
				if (true == StringUtil.IsEmpty(rs.getString("dslam_shelf"))
						&& true == StringUtil.IsEmpty(rs.getString("dslam_casing"))
						&& true == StringUtil.IsEmpty(rs.getString("dslam_slot"))
						&& true == StringUtil.IsEmpty(rs.getString("dslam_port")))
				{
					map.put("dslam", "-");
				}
				else
				{
					map.put("dslam", rs.getString("dslam_shelf") + "-"
							+ rs.getString("dslam_casing") + "-"
							+ rs.getString("dslam_slot") + "-"
							+ rs.getString("dslam_port"));
				}
				// 将gather_time转换成时间 采集时间
				try
				{
					long gather_time = StringUtil.getLongValue(rs
							.getString("gather_time"));
					DateTimeUtil dt = new DateTimeUtil(gather_time * 1000);
					map.put("gather_time", dt.getLongDate());
				}
				catch (NumberFormatException e)
				{
					map.put("gather_time", "");
				}
				catch (Exception e)
				{
					map.put("gather_time", "");
				}
				return map;
			}
		});
		cityMap = null;
		return list;
	}

	public int getHgwByMacCount(String starttime1, String endtime1, String username,
			String mac, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("getHgwByMacCount({},{},{},{},{})", new Object[] { starttime1,
				endtime1, curPage_splitPage, num_splitPage });
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from gw_binded_by_mac where 1=1 ");
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and gather_time>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and gather_time<").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(username))
		{
			sql.append(" and username='").append(username).append("'");
		}
		if (false == StringUtil.IsEmpty(mac))
		{
			sql.append(" and cpe_mac='").append(mac).append("'");
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
}
