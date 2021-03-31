package com.linkage.module.itms.report.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

public class MothTerminalOrderDAO extends SuperDAO {

	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(MothTerminalOrderDAO.class);
	/**
	 * Map<city_id,city_name>
	 */
	private Map<String, String> cityMap = null;
	private Map<String, String> userTypeMap = null;

	/**
	 * 开户未绑定终端明细
	 * 
	 * @param city_id
	 * @param starttime
	 * @param endtime
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> getDeviceListForWBdTerminal(String city_id,
			String starttime, String endtime,String flag, int curPage_splitPage,
			int num_splitPage) {
		StringBuffer sql = new StringBuffer();
		sql.append("select a.device_id, a.city_id, a.username, a.user_id, a.user_type_id, a.oui, a.device_serialnumber, a.opendate,a.userline ");
		if ("1".equals(flag)) {
			sql.append(" from tab_hgwcustomer a where 1=1 ");
		}
		if ("2".equals(flag)) {
			sql.append(" from tab_hgwcustomer a,tab_voip_serv_param c where  a.user_id=c.user_id ");
		}
		if ("3".equals(flag)) {
			sql.append(" from tab_hgwcustomer a,gw_cust_user_dev_type b where 1=1 and a.user_id = b.user_id  and  a.device_id is null ");
		}
		if ("4".equals(flag)) {
			sql.append(" from tab_hgwcustomer a,tab_gw_device b  where a.device_id=b.device_id and b.device_status = 1 and b.cpe_allocatedstatus=1 ");
		}
		if ("5".equals(flag)) {
			/**	if(DBUtil.GetDB()==3){
				//TODO wait
			}else{
				
			} */
			sql.append(" from tab_hgwcustomer a,tab_gw_device b ,tab_voip_serv_param d where a.device_id=b.device_id  and a.user_id=d.user_id and b.device_status = 1 and b.cpe_allocatedstatus=1 ");
		}
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(starttime)) {
			sql.append(" and a.dealdate>=").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime)) {
			sql.append(" and a.dealdate<=").append(endtime);
		}
		sql.append(" order by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		userTypeMap = getUserType();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1)
				* num_splitPage + 1, num_splitPage, new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				// 用户账号
				map.put("username",
						StringUtil.getStringValue(rs.getString("username")));
				// 属地
				String city_id = rs.getString("city_id");
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap
						.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)) {
					map.put("city_name", city_name);
				} else {
					map.put("city_name", "");
				}
				String user_type_id = rs.getString("user_type_id");
				String tmp = "手工添加";
				if (false == StringUtil.IsEmpty(user_type_id)) {
					tmp = userTypeMap.get(user_type_id);
					if (true == StringUtil.IsEmpty(tmp)) {
						tmp = "其他";
					}
				}
				map.put("user_type", tmp);
				// 将opendate转换成时间
				try {
					long opendate = StringUtil.getLongValue(rs
							.getString("opendate"));
					DateTimeUtil dt = new DateTimeUtil(opendate * 1000);
					map.put("opendate", dt.getLongDate());
				} catch (NumberFormatException e) {
					map.put("opendate", "");
				} catch (Exception e) {
					map.put("opendate", "");
				}
				map.put("device_id", rs.getString("device_id"));
				map.put("user_id",
						StringUtil.getStringValue(rs.getString("user_id")));
				return map;
			}
		});
		cityMap = null;
		userTypeMap = null;
		return list;
	}

	public int getDeviceListForWBdTerminalCount(String city_id,
			String starttime, String endtime,String flag, int curPage_splitPage,
			int num_splitPage) {
		StringBuffer sql = new StringBuffer();
//		sql.append("select count(1) from tab_hgwcustomer a,gw_cust_user_dev_type b where 1=1   and a.user_id = b.user_id and  a.device_id is null ");
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		
		if ("1".equals(flag)) {
			sql.append(" from tab_hgwcustomer a where 1=1 ");
		}else if ("2".equals(flag)) {
			sql.append(" from tab_hgwcustomer a,tab_voip_serv_param c where  a.user_id=c.user_id ");
		}else if ("3".equals(flag)) {
			sql.append(" from tab_hgwcustomer a,gw_cust_user_dev_type b where 1=1 and a.user_id = b.user_id  and  a.device_id is null ");
		}else if ("4".equals(flag)) {
			sql.append(" from tab_hgwcustomer a,tab_gw_device b  where a.device_id=b.device_id and b.device_status = 1 and b.cpe_allocatedstatus=1 ");
		}else if ("5".equals(flag)) {
			sql.append(" from tab_hgwcustomer a,tab_gw_device b ,tab_voip_serv_param d where a.device_id=b.device_id  and a.user_id=d.user_id and b.device_status = 1 and b.cpe_allocatedstatus=1 ");
		}
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(starttime)) {
			sql.append(" and a.dealdate>=").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime)) {
			sql.append(" and a.dealdate<=").append(endtime);
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	public List<Map> getDeviceListForTerminalExcel(String city_id,String flag,
			String starttime, String endtime) {
		StringBuffer sql = new StringBuffer();
		sql.append("select a.device_id, a.city_id, a.username, a.user_id,  a.user_type_id, a.oui, a.device_serialnumber, a.opendate, a.userline ");
		if ("1".equals(flag)) {
			sql.append(" from tab_hgwcustomer a where 1=1 ");
		}else if ("2".equals(flag)) {
			sql.append(" from tab_hgwcustomer a,tab_voip_serv_param c where  a.user_id=c.user_id ");
		}else if ("3".equals(flag)) {
			sql.append(" from tab_hgwcustomer a,gw_cust_user_dev_type b where 1=1   and a.user_id = b.user_id and  a.device_id is null ");
		}else if ("4".equals(flag)) {
			sql.append(" from tab_hgwcustomer a,tab_gw_device b  where a.device_id=b.device_id and b.device_status = 1 and b.cpe_allocatedstatus=1 ");
		}else if ("5".equals(flag)) {
			/**	if(DBUtil.GetDB()==3){
				//TODO wait
			}else{
				
			} */
			sql.append(" from tab_hgwcustomer a,tab_gw_device b ,tab_voip_serv_param d where a.device_id=b.device_id  and a.user_id=d.user_id and b.device_status = 1 and b.cpe_allocatedstatus=1 ");
		}
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(starttime)) {
			sql.append(" and a.dealdate>=").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime)) {
			sql.append(" and a.dealdate<=").append(endtime);
		}
		sql.append(" order by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		userTypeMap = getUserType();
		List<Map> list = jt.query(psql.getSQL(), new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				// 用户账号
				map.put("username",
						StringUtil.getStringValue(rs.getString("username")));
				// 属地
				String city_id = rs.getString("city_id");
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap
						.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)) {
					map.put("city_name", city_name);
				} else {
					map.put("city_name", "");
				}
				String user_type_id = rs.getString("user_type_id");
				String tmp = "手工添加";
				if (false == StringUtil.IsEmpty(user_type_id)) {
					tmp = userTypeMap.get(user_type_id);
					if (true == StringUtil.IsEmpty(tmp)) {
						tmp = "其他";
					}
				}
				map.put("user_type", tmp);
				// 将opendate转换成时间
				try {
					long opendate = StringUtil.getLongValue(rs
							.getString("opendate"));
					DateTimeUtil dt = new DateTimeUtil(opendate * 1000);
					map.put("opendate", dt.getLongDate());
				} catch (NumberFormatException e) {
					map.put("opendate", "");
				} catch (Exception e) {
					map.put("opendate", "");
				}
				map.put("device_id", rs.getString("device_id"));
				map.put("user_id",
						StringUtil.getStringValue(rs.getString("user_id")));
				return map;
			}
		});
		cityMap = null;
		userTypeMap = null;
		return list;
	}

	/**
	 * 当月开户总数
	 * 
	 * @param starttime
	 * @param endtime
	 * @param city_id
	 * @return
	 */
	public Map<String, String> countMothTotalOrder(String starttime,
			String endtime, String city_id) 
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select a.city_id, count(*) num from tab_hgwcustomer a where 1=1 ");
		}else{
			sql.append("select a.city_id, count(1) num from tab_hgwcustomer a where 1=1 ");
		}

		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}

		if (!StringUtil.IsEmpty(starttime)) {
			sql.append(" and a.dealdate>=").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime)) {
			sql.append(" and a.dealdate<=").append(endtime);
		}

		sql.append(" group by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		Map<String, String> map = new HashMap<String, String>();
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				map.put(StringUtil.getStringValue(list.get(i).get("city_id")),
						StringUtil.getStringValue(list.get(i).get("num")));
			}
		}
		return map;
	}

	/**
	 * 当月开户工单绑定终端数
	 * 
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public Map<String, String> countBdTerminalOrder(String starttime,
			String endtime, String bindTime, String city_id, String isMonth) 
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select a.city_id,count(*) num ");
		}else{
			sql.append("select a.city_id,count(1) num ");
		}
		sql.append("from tab_hgwcustomer a,tab_gw_device b  where a.device_id=b.device_id ");
		sql.append("and b.device_status = 1 and b.cpe_allocatedstatus=1 ");

		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}

		if (!StringUtil.IsEmpty(starttime)) {
			sql.append(" and a.dealdate>=").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime)) {
			sql.append(" and a.dealdate<=").append(endtime);
		}
		if ("false".equals(isMonth)) {
			if (!StringUtil.IsEmpty(starttime)) {
				sql.append(" and a.binddate>=").append(starttime);
			}
			if (!StringUtil.IsEmpty(endtime)) {
				sql.append(" and a.binddate<=").append(endtime);
			}
		} else {
			if (!StringUtil.IsEmpty(bindTime)) {
				sql.append(" and a.binddate<").append(bindTime);
			}
		}
		sql.append(" group by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		Map<String, String> map = new HashMap<String, String>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				map.put(StringUtil.getStringValue(list.get(i).get("city_id")),
						StringUtil.getStringValue(list.get(i).get("num")));
			}
		}
		return map;
	}

	/**
	 * 当月语音业务工单数
	 * 
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public Map<String, String> countYyTotalOrder(String starttime,
			String endtime, String city_id) 
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select a.city_id, count(*) num ");
		}else{
			sql.append("select a.city_id, count(1) num ");
		}
		sql.append("from tab_hgwcustomer a,tab_voip_serv_param c where  a.user_id=c.user_id ");

		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}

		if (!StringUtil.IsEmpty(starttime)) {
			sql.append(" and a.dealdate>=").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime)) {
			sql.append(" and a.dealdate<=").append(endtime);
		}
		sql.append(" group by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		Map<String, String> map = new HashMap<String, String>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				map.put(StringUtil.getStringValue(list.get(i).get("city_id")),
						StringUtil.getStringValue(list.get(i).get("num")));
			}
		}
		return map;
	}

	/**
	 * 当月语音业务工单绑定终端数
	 * 
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public Map<String, String> countYyBdTerminalOrder(String starttime,
			String endtime, String bindTime, String city_id, String isMonth) 
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("select a.city_id, count(*) num ");
		}else{
			sql.append("select a.city_id, count(1) num ");
		}
		sql.append("from tab_hgwcustomer a,tab_gw_device b ,tab_voip_serv_param d ");
		sql.append("where a.device_id=b.device_id  and a.user_id=d.user_id and b.device_status = 1 and b.cpe_allocatedstatus=1 ");
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(starttime)) {
			sql.append(" and a.dealdate>=").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime)) {
			sql.append(" and a.dealdate<=").append(endtime);

		}
		if ("false".equals(isMonth)) {
			if (!StringUtil.IsEmpty(starttime)) {
				sql.append(" and a.binddate>=").append(starttime);
			}
			if (!StringUtil.IsEmpty(endtime)) {
				sql.append(" and a.binddate<=").append(endtime);

			}
		} else {
			if (!StringUtil.IsEmpty(bindTime)) {
				sql.append(" and a.binddate<").append(bindTime);
			}
		}
		sql.append(" group by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		Map<String, String> map = new HashMap<String, String>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				map.put(StringUtil.getStringValue(list.get(i).get("city_id")),
						StringUtil.getStringValue(list.get(i).get("num")));
			}
		}
		return map;
	}

	/**
	 * 当月未绑定终端的开户工单总数
	 * 
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public Map<String, String> countWYyBdTerminalOrder(String starttime,
			String endtime, String city_id) 
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select a.city_id, count(*) num ");
		}else{
			sql.append("select a.city_id, count(1) num ");
		}
		sql.append("from tab_hgwcustomer a  where a.device_id is null   ");

		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(starttime)) {
			sql.append(" and a.dealdate>=").append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime)) {
			sql.append(" and a.dealdate<=").append(endtime);
		}
		sql.append(" group by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		Map<String, String> map = new HashMap<String, String>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				map.put(StringUtil.getStringValue(list.get(i).get("city_id")),
						StringUtil.getStringValue(list.get(i).get("num")));
			}
		}
		return map;
	}

	/**
	 * 获取 用户类型ID 与用户类型名称 对应的Map
	 * 
	 * @return
	 */
	private Map<String, String> getUserType() 
	{
		logger.debug("FtthUserBindDaoImp==>getUserType()");
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select user_type_id,type_name from user_type ");
		}else{
			psql.append("select * from user_type");
		}
		List<Map> list = jt.queryForList(psql.getSQL());
		Map<String, String> userTypeMap = new HashMap<String, String>();
		for (Map map : list) {
			userTypeMap.put(StringUtil.getStringValue(map.get("user_type_id")),
					StringUtil.getStringValue(map.get("type_name")));
		}
		return userTypeMap;
	}

	private String getDecimal(String total, String ttotal) {
		if ("".equals(total) || "0".equals(total) || "".equals(ttotal)
				|| "0".equals(ttotal)) {
			return "N/A";
		}
		float t1 = Float.parseFloat(total);
		float t2 = Float.parseFloat(ttotal);
		float f = t1 / t2;
		DecimalFormat df = new DecimalFormat();
		String style = "0.00%";
		df.applyPattern(style);
		return df.format(f);
	}
}
