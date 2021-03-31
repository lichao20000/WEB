package com.linkage.module.gwms.dao.tabquery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author Jason(3412)
 * @date 2009-9-24
 */
@SuppressWarnings("unchecked")
public class CityOfficeZoneDAO extends SuperDAO 
{
	private static Logger logger = LoggerFactory.getLogger(CityOfficeZoneDAO.class);
	/**
	 * Map<city_id,city_name>
	 */
	private Map<String, String> cityMap = null;
	
	/**
	 * 获取属地,局向,小区的对应关系表
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-9-24
	 * @return List
	 */
	public List getAllCityOfficeZone() 
	{
		logger.debug("getAllCityOfficeZone()");
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select city_id,office_id from tab_city_office_zone");
		}else{
			psql.append("select * from tab_city_office_zone");
		}
		List rList = jt.queryForList(psql.getSQL());
		if (null != rList && false == rList.isEmpty()) {
			int size = rList.size();
			for (int i = 0; i < size; i++) {
				Map tMap = (Map) rList.get(i);
			}
		}
		return rList;
	}
	
	/**
	 * 通过局向ID获得局向name
	 *
	 * @author wangsenbo
	 * @date Jan 20, 2010
	 * @param 
	 * @return List
	 */
	public List getOfficeById(String officeId) {
		logger.debug("getOfficeById({})",officeId);
		String sql = "select office_id,office_name from tab_office where office_id=?";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setString(1, officeId);
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 属地ID下所有的局向ID
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-9-24
	 * @return List
	 */
	public Map<String, List<String>> getCityIdOfficeIds() {
		logger.debug("getOfficeIdByCityId({})");
		Map<String, List<String>> rMap = null;
		List allList = getAllCityOfficeZone();
		if (null != allList && false == allList.isEmpty()) {
			rMap = new HashMap<String, List<String>>();
			int size = allList.size();
			for (int i = 0; i < size; i++) {
				Map tMap = (Map) allList.get(i);
				String cityId = StringUtil.getStringValue(tMap.get("city_id"));
				String officeId = StringUtil.getStringValue(tMap.get("office_id"));
				if(rMap.containsKey(cityId)){
					rMap.get(cityId).add(officeId);
				}else{
					List<String> tList = new ArrayList<String>();
					tList.add(officeId);
					rMap.put(cityId, tList);
				}
			}
		}
		return rMap;
	}

	
	/**
	 * 获取局向ID和属地ID的map
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-9-24
	 * @return List
	 */
	public Map<String, String> getOfficeIdCityId() {
		logger.debug("getOfficeIdByCityId({})");
		Map<String, String> rMap = null;
		List allList = getAllCityOfficeZone();
		if (null != allList && false == allList.isEmpty()) {
			rMap = new HashMap<String, String>();
			int size = allList.size();
			for (int i = 0; i < size; i++) {
				Map tMap = (Map) allList.get(i);
				String cityId = StringUtil.getStringValue(tMap.get("city_id"));
				String officeId = StringUtil.getStringValue(tMap.get("office_id"));
				rMap.put(officeId, cityId);
			}
		}
		return rMap;
	}

	/**
	 * 通过officeName模糊匹配局向数据
	 *
	 * @author wangsenbo
	 * @date Jan 20, 2010
	 * @param 
	 * @return List<Map<String,String>>
	 */
	public List<Map<String, String>> getOfficeListByName(List cityList, String officeName)
	{
		logger.debug("getOfficeListByName({},{})",cityList, officeName);
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.office_id,a.office_name ");
		psql.append("from tab_office a,tab_city_office_zone b ");
		psql.append("where b.city_id in ("+StringUtils.weave(cityList)+") ");
		psql.append("and a.office_name like '%"+officeName+"%' and a.office_id=b.office_id");
		return jt.queryForList(psql.getSQL());
	}


	public List getOfficeVoipList(String cityId, int curPage_splitPage, int num_splitPage)
	{
		List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
		
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
			psql.append("select a.office_id,a.office_name,b.city_id,");
			psql.append("c.proxy_server,c.proxy_port,c.standby_proxy_server,c.standby_proxy_port,");
			psql.append("c.regist_server,c.regist_port,c.standby_regist_server,c.standby_regist_port,");
			psql.append("c.outbound_proxy,c.outbound_port,c.standby_outbound_proxy,c.standby_outbound_port ");
		}else{
			psql.append("select a.office_id,a.office_name,b.city_id,c.* ");
		}
		psql.append("from tab_office a, tab_city_office_zone b, gw_office_voip c ");
		psql.append("where a.office_id=b.office_id and a.office_id=c.office_id ");
		psql.append("and b.city_id in("+StringUtils.weave(cityIdList)+") ");
		psql.append("order by b.city_id");
		cityMap = CityDAO.getCityIdCityNameMap();
		cityIdList = null;
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper()
				{
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> tmap = new HashMap<String, String>();
						tmap.put("proxServ", rs.getString("proxy_server"));
						tmap.put("proxPort", rs.getString("proxy_port"));
						tmap.put("proxServ2", rs.getString("standby_proxy_server"));
						tmap.put("proxPort2", rs.getString("standby_proxy_port"));
						tmap.put("regiServ", rs.getString("regist_server"));
						tmap.put("regiPort", rs.getString("regist_port"));
						tmap.put("standRegiServ", rs.getString("standby_regist_server"));
						tmap.put("standRegiPort", rs.getString("standby_regist_port"));
						tmap.put("outBoundProxy", rs.getString("outbound_proxy"));
						tmap.put("outBoundPort", rs.getString("outbound_port"));
						tmap.put("standOutBoundProxy", rs.getString("standby_outbound_proxy"));
						tmap.put("standOutBoundPort", rs.getString("standby_outbound_port"));
						tmap.put("officeName", rs.getString("office_name"));
						tmap.put("cityId", rs.getString("city_id"));
						tmap.put("officeId", rs.getString("office_id"));
						tmap.put("cityName", cityMap.get(rs.getString("city_id")));
						return tmap;
					}
				});
		cityMap = null;
		cityIdList = null;
		return list;
	}

	public int getOfficeVoipCount(String cityId, int curPage_splitPage, int num_splitPage)
	{
		List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
			psql.append("select count(*) ");
		}else{
			psql.append("select count(1) ");
		}
		psql.append("from tab_office a, tab_city_office_zone b, gw_office_voip c ");
		psql.append("where a.office_id=b.office_id and a.office_id=c.office_id ");
		psql.append("and b.city_id in("+StringUtils.weave(cityIdList)+")");
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		cityIdList = null;
		return maxPage;
	}
}
