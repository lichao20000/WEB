/**
 * 
 */
package com.linkage.module.gwms.report.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.report.dao.interf.I_DeviceOnlineReportDAO;

import dao.util.JdbcTemplateExtend;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-8-12
 * @category com.linkage.module.gwms.report.dao
 * 
 */
@SuppressWarnings("rawtypes")
public class DeviceOnlineReportDAO implements I_DeviceOnlineReportDAO
{
	private JdbcTemplateExtend jt;
	
	public void setDao(DataSource dao) {
		jt = new JdbcTemplateExtend(dao);
	}
	
	/**
	 * @category 查询所有的属地
	 * 
	 * @param cityId
	 * @return
	 */
	public List getAllCity(String cityId)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append(" select city_id,city_name,parent_id from tab_city ");
		if(!StringUtil.IsEmpty(cityId)){
			if(!"00".equals(cityId)){
				psql.append(" where city_id='"+cityId);
				psql.append("' or parent_id='"+cityId+"' ");
			}
		}
		psql.append(" order by city_id ");
		
		return jt.queryForList(psql.getSQL());
	}
	
	/**
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param cityId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List getReportData(int curPage_splitPage, int num_splitPage,
			String cityId, long startDate, long endDate)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.device_serialnumber,a.city_id,a.oui,a.device_type,b.last_time from ");
		sql.append(" tab_gw_device a,gw_devicestatus b where a.device_id = b.device_id and b.online_status = 1 ");
		sql.append(" and b.last_time > " + startDate/1000);
		sql.append(" and b.last_time <= " + endDate/1000);
		
		if (null != cityId && !"00".equals(cityId)){
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			String cityStr = cityId + "','" + StringUtils.weave(cityArray,"','");
			sql.append(" and a.city_id in ('" + cityStr + "')");
			cityArray = null;
		}
		
		sql.append(" order by b.last_time desc");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		return jt.querySP(sql.toString(), (curPage_splitPage - 1) * num_splitPage + 1,
			num_splitPage, new RowMapper() {
				public Object mapRow(ResultSet rs, int arg1)
						throws SQLException {
					Map<String, String> map = new HashMap<String, String>();
					Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
					map.put("device_serialnumber", rs.getString("device_serialnumber"));
					map.put("city_name", cityMap.get(rs.getString("city_id")));
					map.put("oui", rs.getString("oui"));
					map.put("device_type", rs.getString("device_type"));
					map.put("last_time", new DateTimeUtil(Long.valueOf(rs
							.getString("last_time")+"000")).getDate());
					cityMap = null;
					return map;
				}
		});
	}
	
	/**
	 * @查询报表数据
	 * 
	 * @param city_id
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List getReportData(String cityId, long startDate, long endDate)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.device_serialnumber,a.city_id,a.oui,a.device_type,b.last_time from ");
		sql.append(" tab_gw_device a,gw_devicestatus b where a.device_id = b.device_id and b.online_status = 1 ");
		sql.append(" and b.last_time > " + startDate/1000);
		sql.append(" and b.last_time <= " + endDate/1000);
		
		if (null != cityId && !"00".equals(cityId)){
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			String cityStr = cityId + "','" + StringUtils.weave(cityArray,"','");
			sql.append(" and a.city_id in ('" + cityStr + "')");
			cityArray = null;
		}

		sql.append(" order by b.last_time desc");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		List temp = jt.queryForList(sql.toString());
		List rs = new ArrayList();
		Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
		for(int i=0;i<temp.size();i++){
			Map one = (Map)temp.get(i);
			one.put("city_name", cityMap.get(String.valueOf(one.get("city_id"))));
			one.put("last_time", new DateTimeUtil(Long.valueOf(String.valueOf(one.get("last_time"))+"000")).getDate());
			rs.add(one);
		}
		cityMap = null;
		return rs;
	}
	
	/**
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param cityId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public int getServStrategyCount(int curPage_splitPage, int num_splitPage,
			String cityId, long startDate, long endDate)
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("select count(*) from ");
		}else{
			sql.append("select count(1) from ");
		}
		
		sql.append("tab_gw_device a,gw_devicestatus b where a.device_id = b.device_id and b.online_status = 1 ");
		sql.append(" and b.last_time > " + startDate/1000);
		sql.append(" and b.last_time <= " + endDate/1000);
		
		if (null != cityId && !"00".equals(cityId)){
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			String cityStr = cityId + "','" + StringUtils.weave(cityArray,"','");
			sql.append(" and a.city_id in ('" + cityStr + "')");
			cityArray = null;
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForInt(psql.getSQL());
	}
}
