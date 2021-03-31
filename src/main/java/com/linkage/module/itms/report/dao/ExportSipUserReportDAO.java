package com.linkage.module.itms.report.dao;

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
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 
 * @author xiangzl (Ailk No.)
 * @version 1.0
 * @since 2014-6-30
 * @category com.linkage.module.itms.report.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
@SuppressWarnings("unchecked")
public class ExportSipUserReportDAO extends SuperDAO
{
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(ExportSipUserReportDAO.class);
	private Map<String, String> cityMap = null;

	public List<Map> getHgwList(String starttime1, String endtime1, String cityId,
			int curPage_splitPage, int num_splitPage, String userType)
	{
		logger.debug("getHgwList({},{},{},{},{},{},{},{},{},{})", new Object[] { starttime1,
				endtime1, cityId, curPage_splitPage, num_splitPage, userType });
		String tabName = "tab_hgwcustomer a,tab_voip_serv_param b";
		if(Global.GW_TYPE_BBMS.equals(userType))
		{
			tabName = "tab_egwcustomer a,tab_egw_voip_serv_param b";
		}
		StringBuffer sql = new StringBuffer();
		
		sql.append("select a.city_id,a.username,a.user_id,b.voip_username,b.voip_phone,b.protocol from " + tabName +
				   " where a.user_id=b.user_id and b.protocol != 2");
		if (!StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and a.dealdate >= ").append(starttime1);   // 竣工时间
		}
		if (!StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and a.dealdate <= ").append(endtime1);     // 竣工时间
		}
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList)).append(
					")");
			cityIdList = null;
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException{
				Map<String, String> map = new HashMap<String, String>();
				map.put("username", rs.getString("username"));
				String city_id = rs.getString("city_id");
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (!StringUtil.IsEmpty(city_name)){
					map.put("city_name", city_name);
				}else{
					map.put("city_name", "");
				}
				map.put("user_id", rs.getString("user_id"));
				map.put("voip_username", rs.getString("voip_username"));
				map.put("voip_phone", rs.getString("voip_phone"));
				if("0".equals(rs.getString("protocol")))
				{
					map.put("protocol", "软交换SIP");
				}
				else
				{
					map.put("protocol", "IMS SIP");
				}
				
				return map;
			}
		});
		cityMap = null;
		return list;
		
	}

	public int getHgwCount(String starttime1, String endtime1, String cityId,
			int curPage_splitPage, int num_splitPage, String userType)
	{
		logger.debug("getHgwCount()");
		String tabName = "tab_hgwcustomer a,tab_voip_serv_param b";
		if(Global.GW_TYPE_BBMS.equals(userType))
		{
			tabName = "tab_egwcustomer a,tab_egw_voip_serv_param b";
		}
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		sql.append("from " + tabName +" where a.user_id=b.user_id and b.protocol != 2");
		if (!StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and a.dealdate >= ").append(starttime1);   // 竣工时间
		}
		if (!StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and a.dealdate <= ").append(endtime1);     // 竣工时间
		}
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList)).append(
					")");
			cityIdList = null;
		}

		PrepareSQL psql = new PrepareSQL(sql.toString());
		
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getHgwExcel(String starttime1, String endtime1, String cityId,
			String userType)
	{
		logger.debug("getHgwList({},{},{})", new Object[] { starttime1,endtime1, cityId });
		String tabName = "tab_hgwcustomer a,tab_voip_serv_param b";
		if(Global.GW_TYPE_BBMS.equals(userType))
		{
			tabName = "tab_egwcustomer a,tab_egw_voip_serv_param b";
		}
		StringBuffer sql = new StringBuffer();
		
		sql.append("select a.city_id,a.username,a.user_id,b.voip_username,b.voip_phone,b.protocol from " + tabName +
				   " where a.user_id=b.user_id and b.protocol != 2");
		if (!StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and a.dealdate >= ").append(starttime1);   // 竣工时间
		}
		if (!StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and a.dealdate <= ").append(endtime1);     // 竣工时间
		}
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList)).append(
					")");
			cityIdList = null;
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = jt.query(psql.getSQL(), new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException{
				Map<String, String> map = new HashMap<String, String>();
				map.put("username", rs.getString("username"));
				String city_id = rs.getString("city_id");
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (!StringUtil.IsEmpty(city_name)){
					map.put("city_name", city_name);
				}else{
					map.put("city_name", "");
				}
				map.put("user_id", rs.getString("user_id"));
				map.put("voip_username", rs.getString("voip_username"));
				map.put("voip_phone", rs.getString("voip_phone"));
				if("0".equals(rs.getString("protocol")))
				{
					map.put("protocol", "软交换SIP");
				}
				else
				{
					map.put("protocol", "IMS SIP");
				}
				map.put("is_config", "否");
				return map;
			}
		});
		cityMap = null;
		return list;
	}
}
