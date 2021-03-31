package com.linkage.module.ids.dao;

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
import com.linkage.commons.db.DBUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

public class DegradationPathsDAO extends SuperDAO 
{
	private static Logger logger = LoggerFactory.getLogger(DegradationPathsDAO.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void SQLAppend(String starttime1, String endtime1, String cityId,
			String oltip, PrepareSQL pSql) 
	{
		if (false == StringUtil.IsEmpty(starttime1)) {
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				//MySQL里date_format(date,'%Y-%m-%d') 对应 oracle中的to_char(date,'yyyy-MM-dd');
				//MySQL里str_to_date(date,'%Y-%m-%d') 对应oracle中的to_date(date,'yyyy-MM-dd');
				pSql.append(" and date_format(a.time,'%Y%m%d')>=date_format(str_to_date('"
						+ starttime1 + "','%Y%m%d'),'%Y%m%d')");
			}else{
				pSql.append(" and to_char(a.time,'yyyymmdd')>=to_char(to_date('"
						+ starttime1 + "','yyyy-mm-dd'),'yyyymmdd')");
			}
		}
		
		if (false == StringUtil.IsEmpty(endtime1)) {
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				pSql.append(" and date_format(a.time,'%Y%m%d')<=date_format(str_to_date('"
						+ endtime1 + "','%Y%m%d'),'%Y%m%d')");
			}else{
				pSql.append(" and to_char(a.time,'yyyymmdd')<=to_char(to_date('"
						+ endtime1 + "','yyyy-mm-dd'),'yyyymmdd')");
			}
		}
		
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			List cityNameList = new ArrayList();
			for (int i = 0; i < cityIdList.size(); i++) {
				cityNameList.add(CityDAO.getCityName(cityIdList.get(i).toString()));
			}
			pSql.append(" and a.area_name in ("+StringUtils.weave(cityNameList)+")");
			cityIdList = null;
			cityNameList = null;

		}
		if (false == StringUtil.IsEmpty(oltip)) {
			pSql.append(" and a.olt_ip ='" + oltip + "'");
		}
	}

	private void SQLAppend1(String oltip, String ponid, String starttime1,
			String endtime1, PrepareSQL pSql) 
	{
		if (false == StringUtil.IsEmpty(oltip)) {
			pSql.append(" and a.olt_ip ='" + oltip + "'");
		}
		if (false == StringUtil.IsEmpty(ponid)) {
			pSql.append(" and a.pon_id ='" + ponid + "'");
		}
		if (false == StringUtil.IsEmpty(starttime1)) {
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				pSql.append(" and date_format(a.time,'%Y%m%d')>=date_format(str_to_date('"
						+ starttime1 + "','%Y%m%d'),'%Y%m%d')");
			}else{
				pSql.append(" and to_char(a.time,'yyyymmdd')>=to_char(to_date('"
						+ starttime1 + "','yyyy-mm-dd'),'yyyymmdd')");
			}
		}
		if (false == StringUtil.IsEmpty(endtime1)) {
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				pSql.append(" and date_format(a.time,'%Y%m%d')<=date_format(str_to_date('"
						+ endtime1 + "','%Y%m%d'),'%Y%m%d')");
			}else{
				pSql.append(" and to_char(a.time,'yyyymmdd')<=to_char(to_date('"
						+ endtime1 + "','yyyy-mm-dd'),'yyyymmdd')");
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public List getDegradationPaths(int curPage_splitPage, int num_splitPage,
			String starttime1, String endtime1, String cityId, String oltip) 
	{
		logger.debug("IdsStatusQueryDAO->getQueryStatusList()");
		PrepareSQL pSql = new PrepareSQL();
		pSql.append("select a.area_name,a.subarea_name,a.site_name,a.olt_name,a.olt_ip,a.pon_id,");
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			pSql.append("count(*) cnt ");
		}else{
			pSql.append("count(1) cnt ");
		}
		pSql.append("from tab_pon_netWork a,tab_hgwcustomer b where 1=1 and a.loid=b.username ");
		SQLAppend(starttime1, endtime1, cityId, oltip, pSql);
		pSql.append(" group by area_name,subarea_name,site_name,olt_name,olt_ip,pon_id having count(1)>5");
		List<Map> list = querySP(pSql.getSQL(), (curPage_splitPage - 1)
				* num_splitPage + 1, num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("area_name", rs.getString("area_name"));
				map.put("subarea_name", rs.getString("subarea_name"));
				map.put("site_name", rs.getString("site_name"));
				map.put("olt_name", rs.getString("olt_name"));
				map.put("olt_ip", rs.getString("olt_ip"));
				map.put("pon_id", rs.getString("pon_id"));
				map.put("cnt", rs.getString("cnt"));
				return map;
			}
		});
		return list;
	}

	public int getDegradationPathsCount(int num_splitPage, String starttime1,
			String endtime1, String cityId, String oltip) 
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select count(*) from tab_pon_netWork a where a.count_num>5 ");
		}else{
			psql.append("select count(1) from tab_pon_netWork a where a.count_num>5 ");
		}
		
		SQLAppend(starttime1, endtime1, cityId, oltip, psql);
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	@SuppressWarnings("rawtypes")
	public List getDegradationPathsInfoList(int curPage_splitPage,
			int num_splitPage, String oltip, String ponid, String starttime1,
			String endtime1) 
	{
		logger.debug("IdsStatusQueryDAO->getQueryStatusList()");
		PrepareSQL pSql = new PrepareSQL();
		pSql.append("select a.area_name,a.subarea_name,a.loid,b.device_serialnumber," +
				"a.tx_power,a.rx_power,b.linkaddress,a.olt_name,"
				+ "a.olt_ip,a.pon_id,a.ont_id,a.count_num " +
				"from tab_pon_netWork  a,tab_hgwcustomer b where a.loid=b.username ");
		SQLAppend1(oltip, ponid, starttime1, endtime1, pSql);
		List<Map> list = querySP(pSql.getSQL(), (curPage_splitPage - 1)
				* num_splitPage + 1, num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("area_name", rs.getString("area_name"));
				map.put("subarea_name", rs.getString("subarea_name"));
				map.put("loid", rs.getString("loid"));
				map.put("device_serialnumber",
						rs.getString("device_serialnumber"));
				map.put("tx_power", rs.getString("tx_power"));
				map.put("rx_power", rs.getString("rx_power"));
				map.put("linkaddress", rs.getString("linkaddress"));
				map.put("olt_name", rs.getString("olt_name"));
				map.put("olt_ip", rs.getString("olt_ip"));
				map.put("pon_id", rs.getString("pon_id"));
				map.put("count_num", rs.getString("count_num"));
				return map;
			}
		});
		return list;
	}

	public int getDegradationPathsInfoCount(int num_splitPage, String oltip,
			String ponid, String starttime1, String endtime1) 
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select count(*) ");
		}else{
			psql.append("select count(1) ");
		}
		psql.append("from tab_pon_netWork a,tab_hgwcustomer b where a.loid=b.username ");
		SQLAppend1(oltip, ponid, starttime1, endtime1, psql);
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
