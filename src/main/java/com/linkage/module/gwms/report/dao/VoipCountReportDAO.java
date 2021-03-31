package com.linkage.module.gwms.report.dao;

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
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
/**
 * 江西电信天翼网关版本一致率
 * @author wangyan10(Ailk NO.76091)
 * @version 1.0
 * @since 2019-4-15
 */
@SuppressWarnings("unchecked")
public class VoipCountReportDAO extends SuperDAO
{
	Logger logger = LoggerFactory.getLogger(VoipCountReportDAO.class);
	private Map<String, String> cityMap = null;
	
	
	public List<Map<String,String>> getCityList(String cityId)
	{
		PrepareSQL ppSQL = new PrepareSQL();
		ppSQL.append("select city_id,city_name from tab_city ");
		ppSQL.append("where (city_id='"+cityId+"' or parent_id='"+cityId+"') ");
		ppSQL.append("order by city_id asc ");
		return jt.queryForList(ppSQL.getSQL());
	}
	
	public List<Map<String,String>> queryDataList(String cityId,String gw_type)
	{
		String tableUser="tab_hgwcustomer";
		String tableServ="hgwcust_serv_info";
		String tableparam="tab_voip_serv_param";
		if (gw_type.equals("2")) {
			tableUser="tab_egwcustomer";
			tableServ="egwcust_serv_info";
			tableparam="tab_egw_voip_serv_param";
		}
		
		PrepareSQL ppSQL = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
			ppSQL.setSQL("select count(*) total,");
		}else{
			ppSQL.setSQL("select count(1) total,");
		}
		ppSQL.append("sum(case when c.protocol != 2 then 1 else 0 end) sip,");
		ppSQL.append("sum(case when c.protocol = 2 then 1  else 0 end) h248,");
		ppSQL.append("a.city_id  city_id_data ");
		ppSQL.append("from "+tableUser+" a,"+tableServ+" b,"+tableparam+" c ");
		ppSQL.append("where a.user_id=b.user_id and a.user_id=c.user_id ");
		ppSQL.append("and b.serv_type_id=14 and c.protocol is not null ");
		if (!"00".equals(cityId)) {
			ppSQL.append(" and a.city_id in ");
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				ppSQL.append("(select city_id from tab_city where city_id='"+cityId+"' ");
				ppSQL.append("or parent_id='"+cityId+"') ");
			}else{
				ppSQL.append("(select city_id from tab_city t start with city_id='"+cityId+"' ");
				ppSQL.append("connect by parent_id = prior city_id) ");
			}
		}
		
		ppSQL.append("group by a.city_id ");
		return jt.queryForList(ppSQL.getSQL());
	}

	
	public List<Map> querydetailList(String gw_type, String city_id, String protocol,String dataCityId,
			int curPage_splitPage, int num_splitPage) 
	{
		String tableUser="tab_hgwcustomer";
		String tableServ="hgwcust_serv_info";
		String tableparam="tab_voip_serv_param";
		if (gw_type.equals("2")) {
			tableUser="tab_egwcustomer";
			tableServ="egwcust_serv_info";
			tableparam="tab_egw_voip_serv_param";
		}
		
		StringBuffer sql = new StringBuffer();
	/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
		}else{
			
		} */
		sql.append("select a.city_id,a.username,c.protocol,c.voip_phone,c.voip_port,");
		sql.append("b.vlanid,b.ipaddress,b.ipmask,b.gateway,d.prox_serv,d.stand_prox_serv ");
		sql.append("from "+tableUser+" a,"+tableServ+" b,"+tableparam+" c,tab_sip_info d ");
		sql.append("where a.user_id=b.user_id and a.user_id=c.user_id ");
		sql.append("and c.sip_id=d.sip_id and b.serv_type_id=14 ");
		if ("sip".equals(protocol)) {
			sql.append(" and c.protocol !=2 ");
		}else {
			sql.append(" and c.protocol =2 ");
		}
		
		if ("-1".equals(dataCityId)) {
			if (!"00".equals(city_id)) {
				sql.append(" and city_id in ");
				if(DBUtil.GetDB()==Global.DB_MYSQL){
					sql.append("(select city_id from tab_city where city_id='"+city_id+"' ");
					sql.append("or parent_id='"+city_id+"') ");
				}else{
					sql.append("(select city_id from tab_city t start with city_id='"+city_id+"' ");
					sql.append("connect by parent_id = prior city_id) ");
				}
			}
		}else {
			if (dataCityId.equals(city_id)) {
				sql.append(" and city_id ='"+dataCityId+"'");
			}else {
				sql.append(" and city_id in ");
				if(DBUtil.GetDB()==Global.DB_MYSQL){
					sql.append("(select city_id from tab_city where city_id='"+city_id+"' ");
					sql.append("or parent_id='"+city_id+"') ");
				}else{
					sql.append("(select city_id from tab_city t start with city_id='"+city_id+"' ");
					sql.append("connect by parent_id = prior city_id) ");
				}
			}
		}

		cityMap = CityDAO.getCityIdCityNameMap();
		PrepareSQL psql = new PrepareSQL();
		psql.setSQL(sql.toString());
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1, num_splitPage,
				new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1) throws SQLException {
						Map<String, String> map = new HashMap<String, String>();
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
						String sCityId = CityDAO.getLocationCityIdByCityId(city_id);
						String sCityName = CityDAO.getCityName(sCityId);
						map.put("scity_name", sCityName);
						map.put("loid", rs.getString("username"));
						String protocol = rs.getString("protocol");
						if ("2".equals(protocol)) {
							map.put("protocol", "H248");
						}else {
							map.put("protocol", "SIP");
						}
						map.put("voip_phone", rs.getString("voip_phone"));
						map.put("voip_port", rs.getString("voip_port"));
						map.put("vlanid", rs.getString("vlanid"));
						map.put("ipaddress", rs.getString("ipaddress"));
						map.put("ipmask", rs.getString("ipmask"));
						map.put("gateway", rs.getString("gateway"));
						map.put("prox_serv", rs.getString("prox_serv"));
						map.put("stand_prox_serv", rs.getString("stand_prox_serv"));
						return map;
					}
				});
		cityMap = null;
		return list;
	}
	
	public List<Map> queryDetailForExcel(String gw_type, String city_id, String protocol,String dataCityId) {
		
		String tableUser="tab_hgwcustomer";
		String tableServ="hgwcust_serv_info";
		String tableparam="tab_voip_serv_param";
		if (gw_type.equals("2")) {
			tableUser="tab_egwcustomer";
			tableServ="egwcust_serv_info";
			tableparam="tab_egw_voip_serv_param";
		}
		
		StringBuffer sql = new StringBuffer();
	/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
		}else{
			
		} */
		sql.append("select a.city_id,a.username,c.protocol,c.voip_phone,c.voip_port,");
		sql.append("b.vlanid,b.ipaddress,b.ipmask,b.gateway,d.prox_serv,d.stand_prox_serv ");
		sql.append("from "+tableUser+" a,"+tableServ+" b,"+tableparam+" c,tab_sip_info d ");
		sql.append("where a.user_id=b.user_id and a.user_id=c.user_id ");
		sql.append("and c.sip_id=d.sip_id and b.serv_type_id=14 ");
		if ("sip".equals(protocol)) {
			sql.append(" and c.protocol !=2 ");
		}else {
			sql.append(" and c.protocol =2 ");
		}
		
		if ("-1".equals(dataCityId)) {
			if (!"00".equals(city_id)) {
				sql.append(" and city_id in ");
				if(DBUtil.GetDB()==Global.DB_MYSQL){
					sql.append("(select city_id from tab_city where city_id='"+city_id+"' ");
					sql.append("or parent_id='"+city_id+"') ");
				}else{
					sql.append("(select city_id from tab_city t start with city_id='"+city_id+"' ");
					sql.append("connect by parent_id = prior city_id) ");
				}
			}
		}else {
			if (dataCityId.equals(city_id)) {
				sql.append(" and city_id ='"+dataCityId+"'");
			}else {
				sql.append(" and city_id in ");
				if(DBUtil.GetDB()==Global.DB_MYSQL){
					sql.append("(select city_id from tab_city where city_id='"+city_id+"' ");
					sql.append("or parent_id='"+city_id+"') ");
				}else{
					sql.append("(select city_id from tab_city t start with city_id='"+city_id+"' ");
					sql.append("connect by parent_id = prior city_id) ");
				}
			}
		}
		
		cityMap = CityDAO.getCityIdCityNameMap();
		PrepareSQL psql = new PrepareSQL();
		psql.setSQL(sql.toString());
		List<Map> list =  jt.query(psql.getSQL(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
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
				String sCityId = CityDAO.getLocationCityIdByCityId(city_id);
				String sCityName = CityDAO.getCityName(sCityId);
				map.put("scity_name", sCityName);
				map.put("loid", rs.getString("username"));
				String protocol = rs.getString("protocol");
				if ("2".equals(protocol)) {
					map.put("protocol", "H248");
				}else {
					map.put("protocol", "SIP");
				}
				map.put("voip_phone", rs.getString("voip_phone"));
				map.put("voip_port", rs.getString("voip_port"));
				map.put("vlanid", rs.getString("vlanid"));
				map.put("ipaddress", rs.getString("ipaddress"));
				map.put("ipmask", rs.getString("ipmask"));
				map.put("gateway", rs.getString("gateway"));
				map.put("prox_serv", rs.getString("prox_serv"));
				map.put("stand_prox_serv", rs.getString("stand_prox_serv"));
				return map;
			}
		});
		cityMap = null;
		return list;
	}
	
	public int querydetailListCount(String gw_type, String city_id, String protocol,String dataCityId,
			int curPage_splitPage, int num_splitPage) {

		PrepareSQL psql = new PrepareSQL();
		String tableUser="tab_hgwcustomer";
		String tableServ="hgwcust_serv_info";
		String tableparam="tab_voip_serv_param";
		if (gw_type.equals("2")) {
			tableUser="tab_egwcustomer";
			tableServ="egwcust_serv_info";
			tableparam="tab_egw_voip_serv_param";
		}
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
			psql.setSQL("select count(*) ");
		}else{
			psql.setSQL("select count(1) ");
		}
		psql.append("from "+tableUser+" a,"+tableServ+" b,"+tableparam+" c,tab_sip_info d");
		psql.append("where a.user_id=b.user_id and a.user_id=c.user_id ");
		psql.append("and c.sip_id=d.sip_id and b.serv_type_id=14 ");
		if ("sip".equals(protocol)) {
			psql.append(" and  c.protocol !=2 ");
		}else {
			psql.append("and  c.protocol =2 ");
		}
		
		if ("-1".equals(dataCityId)) {
			if (!"00".equals(city_id)) {
				psql.append(" and city_id in ");
				if(DBUtil.GetDB()==Global.DB_MYSQL){
					psql.append("(select city_id from tab_city where city_id='"+city_id+"' ");
					psql.append("or parent_id='"+city_id+"') ");
				}else{
					psql.append("(select city_id from tab_city t start with city_id='"+city_id+"' ");
					psql.append("connect by parent_id = prior city_id) ");
				}
			}
		}else {
			
			if (dataCityId.equals(city_id)) {
				psql.append(" and city_id ='"+dataCityId+"'");
			}else {
				psql.append(" and city_id in ");
				if(DBUtil.GetDB()==Global.DB_MYSQL){
					psql.append("(select city_id from tab_city where city_id='"+city_id+"' ");
					psql.append("or parent_id='"+city_id+"') ");
				}else{
					psql.append("(select city_id from tab_city t start with city_id='"+city_id+"' ");
					psql.append("connect by parent_id = prior city_id) ");
				}
			}
		}

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
