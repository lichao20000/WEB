/**
 * 
 */
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
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * @author chenjie(67371)
 * @date 2011-5-7
 */
public class CardInfoDAO extends SuperDAO{
	
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(CardInfoDAO.class);
	
	public List<Map> queryCard(String username, String card_serialnumber, String online_status, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("queryCard({},{},{})", new Object[]{username, card_serialnumber, online_status});

		String sql = "select * from tab_gw_card a, tab_hgwcustomer b where a.user_id=b.user_id";
		// teledb
		if (DBUtil.GetDB() == 3) {
			sql = "select a.card_id, a.card_serialnumber, a.user_id, a.online_status, b.username, b.device_id from tab_gw_card a, tab_hgwcustomer b where a.user_id=b.user_id";
		}

		if(!StringUtil.IsEmpty(username))
		{
			sql += " and b.username='" + username + "'";
 		}
		if(!StringUtil.IsEmpty(card_serialnumber))
		{
			sql += " and a.card_serialnumber='" + card_serialnumber + "'";
		}
		if(!online_status.equals("-1"))
		{
			sql += " and online_status=" + online_status;
		}
		sql += " order by b.user_id";
		
		PrepareSQL psql = new PrepareSQL(sql);
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException{
				Map<String, String> map = new HashMap<String, String>();
				map.put("card_id", rs.getString("card_id"));
				map.put("card_serialnumber", rs.getString("card_serialnumber"));
				map.put("username", rs.getString("username"));
				map.put("user_id", rs.getString("user_id"));
				map.put("online_status", rs.getString("online_status"));
				String device_id = rs.getString("device_id");
				map.put("device_id", device_id == null ? "" : device_id);
				return map;
			}
		});
		return list;
	}

	public int getCardCount(String username, String card_serialnumber, String online_status, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("getCardCount({},{},{})", new Object[]{username, card_serialnumber, online_status});
		String sql = "select count(*) from tab_gw_card a, tab_hgwcustomer b where a.user_id=b.user_id";
		if(!StringUtil.IsEmpty(username))
		{
			sql += " and b.username='" + username + "'";
 		}
		if(!StringUtil.IsEmpty(card_serialnumber))
		{
			sql += " and a.card_serialnumber='" + card_serialnumber + "'";
		}
		if(!online_status.equals("-1"))
		{
			sql += " and online_status=" + online_status;
		}
		PrepareSQL psql = new PrepareSQL(sql);
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
	public String getDeviceIdByUserId(String user_id) 
	{
		logger.debug("getDeviceIdByUserId({})", new Object[]{user_id});
		String sql = "select a.device_id from tab_gw_device a, tab_hgwcustomer b where a.device_id = b.device_id and b.user_id=?";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setInt(1, Integer.parseInt(user_id));
		Map map = jt.queryForMap(psql.getSQL());
		String device_id = (String)map.get("device_id");
		return device_id;
	}
	**/

	public Map queryCardStatus(String device_id) {
		logger.debug("queryCardStatus({})", new Object[]{device_id});
		String sql = "select * from gw_card_manage where device_id=?";
		// teledb
		if (DBUtil.GetDB() == 3) {
			sql = "select card_no, status, card_status from gw_card_manage where device_id=?";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setString(1, device_id);
		Map map = queryForMap(psql.getSQL()); 
		return map;
	}
}