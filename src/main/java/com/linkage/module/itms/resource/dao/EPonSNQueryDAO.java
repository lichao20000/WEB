
package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

public class EPonSNQueryDAO extends SuperDAO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(EPonSNQueryDAO.class);

	/**
	 * 该方法只查询用户表
	 * 
	 * @param username
	 * @return
	 */
	public List getHgwInfoByUsername(String cityId,String username, String gw_type){
		logger.debug("getHgwInfoByUsername({},{})", cityId,username);
		
		if(null==username || "".equals(username)){
			return new ArrayList<HashMap<String, String>>();
		}
		
		StringBuffer sql = new StringBuffer();

		if(gw_type.equals(Global.GW_TYPE_ITMS))
        {
			sql.append(" select a.username,a.city_id,a.linkman,a.linkaddress from " +
					" tab_hgwcustomer a,gw_cust_user_dev_type b where " +
					" a.user_id=b.user_id and a.access_style_id in (3,6) and " +
					" b.type_id='2' and a.username='");
        }
		else{
			sql.append(" select a.username,a.city_id,a.linkman,a.linkaddress from " +
					" tab_egwcustomer a,gw_cust_user_dev_type b where " +
					" a.user_id=b.user_id and a.access_style_id in (3,6) and " +
					" b.type_id='2' and a.username='");
			
		}
		
		sql.append(username);
		sql.append("'");
		if(null!=cityId && !"".equals(cityId) && !"00".equals(cityId)){
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in ('" + StringUtils.weave(cityArray,"','") + "')");
			cityArray = null;
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
    	psql.getSQL();
		
		return jt.query(sql.toString(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map(map, rs);
			}
		});
	}
	
	/**
	 * 该方法查询的条件只涉及到VOIP的参数表
	 * 
	 * @param voipUsername
	 * @param voipPhone
	 * @return
	 */
	public List getHgwInfoByVoipParam(String cityId,String voipUsername,String voipPhone,String gw_type){
		logger.debug("getHgwInfoByVoipParam({},{},{})", new Object[]{cityId,voipUsername,voipPhone});
		
		if((null==voipUsername || "".equals(voipUsername)) &&
				(null==voipPhone || "".equals(voipPhone))){
			return new ArrayList<HashMap<String, String>>();
		}
		
		StringBuffer sql = new StringBuffer();
		if(gw_type.equals(Global.GW_TYPE_ITMS))
		{
		sql.append(" select a.username,a.city_id,a.linkman,a.linkaddress from " +
				" tab_hgwcustomer a,gw_cust_user_dev_type b,tab_voip_serv_param " +
				" c where a.user_id=b.user_id and a.user_id=c.user_id and " +
				" a.access_style_id in (3,6) and b.type_id='2' ");
		}
		else
		{
			sql.append(" select a.username,a.city_id,a.linkman,a.linkaddress from " +
					" tab_egwcustomer a,gw_cust_user_dev_type b,tab_voip_serv_param " +
					" c where a.user_id=b.user_id and a.user_id=c.user_id and " +
					" a.access_style_id in (3,6) and b.type_id='2' ");
		}
		if(null!=voipUsername && !"".equals(voipUsername)){
			sql.append(" and c.voip_username='");
			sql.append(voipUsername);
			sql.append("' ");
		}
		if(null!=voipPhone && !"".equals(voipPhone)){
			sql.append(" and c.voip_phone='");
			sql.append(voipPhone);
			sql.append("' ");
		}
		if(null!=cityId && !"".equals(cityId) && !"00".equals(cityId)){
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in ('" + StringUtils.weave(cityArray,"','") + "')");
			cityArray = null;
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
    	psql.getSQL();
    	
		return jt.query(sql.toString(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map(map, rs);
			}
		});
	}
	
	/**
	 * 该方法为VOIP的参数表和用户表混合查询，前提是参数username不能为null或者“”
	 * 
	 * @param username
	 * @param voipUsername
	 * @param voipPhone
	 * @return
	 */
	public List getHgwInfoByAll(String cityId,String username,String voipUsername,String voipPhone, String gw_type){
		logger.debug("getHgwInfoByAll({},{},{})", new Object[]{cityId,username,voipUsername,voipPhone});
		
		StringBuffer sql = new StringBuffer();
		if(gw_type.equals(Global.GW_TYPE_ITMS))
		{
		sql.append(" select a.username,a.city_id,a.linkman,a.linkaddress from " +
				" tab_hgwcustomer a,gw_cust_user_dev_type b,tab_voip_serv_param " +
				" c where a.user_id=b.user_id and a.user_id=c.user_id and " +
				" a.access_style_id in (3,6) and b.type_id='2' and a.username='");
		}
		else
		{
			sql.append(" select a.username,a.city_id,a.linkman,a.linkaddress from " +
					" tab_egwcustomer a,gw_cust_user_dev_type b,tab_voip_serv_param " +
					" c where a.user_id=b.user_id and a.user_id=c.user_id and " +
					" a.access_style_id in (3,6) and b.type_id='2' and a.username='");
		}
		sql.append(username);
		sql.append("' ");
		
		if(null!=voipUsername && !"".equals(voipUsername)){
			sql.append(" and c.voip_username='");
			sql.append(voipUsername);
			sql.append("' ");
		}
		if(null!=voipPhone && !"".equals(voipPhone)){
			sql.append(" and c.voip_phone='");
			sql.append(voipPhone);
			sql.append("' ");
		}
		if(null!=cityId && !"".equals(cityId) && !"00".equals(cityId)){
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in ('" + StringUtils.weave(cityArray,"','") + "')");
			cityArray = null;
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
    	psql.getSQL();
		
		return jt.query(sql.toString(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				return resultSet2Map(map, rs);
			}
		});
	}
	
	/**
	 * 数据转换
	 * @param map
	 * @param rs
	 * @return
	 * @throws SQLException 
	 */
	public Map<String, String> resultSet2Map(Map<String, String> map,ResultSet rs) {
		try{
			map.put("username", rs.getString("username"));
			map.put("cityId", rs.getString("city_id"));
			map.put("linkman", rs.getString("linkman"));
			map.put("linkaddress", rs.getString("linkaddress"));
			map.put("cityName", CityDAO.getCityIdCityNameMap().get(rs.getString("city_id")));
		}catch(SQLException e){
			logger.error(e.getMessage());
		}
		return map;
	}
}
