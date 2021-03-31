package com.linkage.module.itms.report.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

public class StatisticalLordMGCAddressDAO extends SuperDAO 
{

	private Map<String, String> cityMap = null;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Map<String, String>> countMoreBroadbandBusiness(String city_id) 
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("select a.city_id,c.prox_serv,count(*) as num ");
		}else{
			sql.append("select a.city_id,c.prox_serv,count(1) as num ");
		}
		sql.append("from tab_hgwcustomer a,tab_voip_serv_param b,tab_sip_info c ");
		sql.append("where a.user_id = b.user_id and b.sip_id = c.sip_id ");
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		sql.append(" group by c.prox_serv,a.city_id ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = jt.queryForList(psql.getSQL());
		Map<String, String> cityMap = null;
		Map<String, Map<String, String>> resultMap = new HashMap<String, Map<String, String>>();
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				String prox_serv = StringUtil.getStringValue(list.get(i).get(
						"prox_serv"));
				String cityId = StringUtil.getStringValue(list.get(i).get(
						"city_id"));
				String num = StringUtil.getStringValue(list.get(i).get("num"));
				if (null == resultMap.get(prox_serv)) 
				{
					cityMap = new HashMap<String, String>();
				}
				else
				{
					cityMap = resultMap.get(prox_serv);
				}
				cityMap.put(cityId, num);
				resultMap.put(prox_serv, cityMap);

			}
		}
		return resultMap;
	}
	
	public Map<String, String> xiaoji(String city_id) 
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("select a.city_id,count(*) as num ");
		}else{
			sql.append("select a.city_id,count(1) as num ");
		}
		sql.append("from tab_hgwcustomer a,tab_voip_serv_param b,tab_sip_info c ");
		sql.append("where a.user_id = b.user_id and b.sip_id = c.sip_id ");
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		sql.append(" group by a.city_id ");
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

	@SuppressWarnings("rawtypes")
	public List<Map> getDeviceListForWBdTerminal(String city_id,
			String prox_serv, int curPage_splitPage, int num_splitPage) 
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id,a.username as loid,c.prox_serv,");
		sql.append("c.prox_port,c.stand_prox_serv,c.stand_prox_port ");
		sql.append("from tab_hgwcustomer a,tab_voip_serv_param b,tab_sip_info c ");
		sql.append("where a.user_id=b.user_id and b.sip_id=c.sip_id ");
		sql.append("and c.prox_serv='"+ prox_serv + "'");
		if (!StringUtil.IsEmpty(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1)
				* num_splitPage + 1, num_splitPage, new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				String city_id = rs.getString("city_id");
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap
						.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)) {
					map.put("city_name", city_name);
				} else {
					map.put("city_name", "");
				}
				map.put("loid", rs.getString("loid"));
				map.put("prox_serv", rs.getString("prox_serv"));
				map.put("prox_port", rs.getString("prox_port"));
				map.put("stand_prox_serv", rs.getString("stand_prox_serv"));
				map.put("stand_prox_port", rs.getString("stand_prox_port"));
				return map;
			}
		});
		cityMap = null;
		return list;
	}

	@SuppressWarnings("rawtypes")
	public int getDeviceListForWBdTerminalCount(String city_id,
			String prox_serv, int curPage_splitPage, int num_splitPage) 
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		sql.append("from tab_hgwcustomer a,tab_voip_serv_param b,tab_sip_info c ");
		sql.append("where a.user_id = b.user_id and b.sip_id = c.sip_id ");
		sql.append("and c.prox_serv='"+ prox_serv + "'");
		if (!StringUtil.IsEmpty(city_id)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (")
					.append(StringUtils.weave(cityIdList)).append(")");
			cityIdList = null;
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

	
}
