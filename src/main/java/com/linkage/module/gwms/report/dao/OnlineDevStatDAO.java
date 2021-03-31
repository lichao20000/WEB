package com.linkage.module.gwms.report.dao;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.report.dao.interf.I_OnlineDevStatDAO;

/**
 * @author Jason(3412)
 * @date 2009-4-28
 */
public class OnlineDevStatDAO implements I_OnlineDevStatDAO
{
	private static Logger log = LoggerFactory.getLogger(OnlineDevStatDAO.class);

	private JdbcTemplate jt;

	/**
	 * @category
	 * 
	 * @param city_id
	 * @param startTime
	 * @param startEnd
	 * @param chartType
	 * @return
	 */
	public List getDataOnLineDev(String city_id, long startTime,
			long startEnd, String chartType)
	{
		StringBuffer sql = new StringBuffer();

		int dbtype = DBUtil.GetDB();
		log.warn("OnlineDevStatDAO dbtype:"+dbtype);
		//sybase数据库
		if(dbtype == 2)
		{
			log.debug("sybase database");
			sql.append("select distinct r_timepoint,sum(r_count) as r_count from ");
			sql.append(" gw_online_report where city_id  in (select city_id from ");
			sql.append(" tab_city ");

			if ("1".equals(chartType)) {
				sql.append(" where city_id='"+city_id+"' ");
				if(!"00".equals(city_id)){
					sql.append(" or parent_id='"+city_id+"' ");
				}
			}else{
				if(!"00".equals(city_id)){
					sql.append(" where city_id='"+city_id+"' or parent_id='"+city_id+"' ");
				}
			}
			
			sql.append(" ) and r_time>="+startTime);
			sql.append(" and r_time<"+startEnd);
			sql.append(" group by r_time order by r_time");
			
		}
		//oracle数据库
		else if(dbtype == 1 || dbtype == 3)
		{
			log.debug("oracle database");
			sql.append("select r_timepoint,sum(r_count) as r_count from ");
			sql.append(" gw_online_report where city_id  in (select city_id from ");
			sql.append(" tab_city ");

			if ("1".equals(chartType)) {
				
				if("00".equals(city_id)){
					sql.append(" where city_id='");
					sql.append(city_id);
					sql.append("' ");
				}else{
					sql.append(" where ");
					sql.append(" city_id='");
					sql.append(city_id);
					sql.append("' or parent_id='");
					sql.append(city_id);
					sql.append("' ");
				}
			}else{
				
				if("00".equals(city_id)){
					
					//统计全部，不需要处理！
					
				}else{

					sql.append(" where ");
					sql.append(" city_id='");
					sql.append(city_id);
					sql.append("' or parent_id='");
					sql.append(city_id);
					sql.append("' ");
					
				}
				
			}
			
			sql.append(" ) and ");
			sql.append(" r_time>=");
			sql.append(startTime);
			sql.append(" and r_time<");
			sql.append(startEnd);
			sql.append(" group by r_time,r_timepoint order by max(r_timepoint)");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		return jt.queryForList(sql.toString());
		
	}
	
	/**
	 * @category 查询概属地下所有的数据
	 * 
	 * @param city_id
	 * @param startTime
	 * @param startEnd
	 * @param r_timepoint
	 * @return
	 */
	public List getMonthOnLineDevAll(String city_id, long startTime,
			long startEnd, String r_timepoint, String chartType) {

		StringBuffer sql = new StringBuffer();

		sql.append("select distinct r_time,sum(r_count) as r_count from ");
		sql.append(" gw_online_report where city_id  in (select city_id from ");
		sql.append(" tab_city ");

		if ("1".equals(chartType)) {
			
			if("00".equals(city_id)){
				
				sql.append(" where ");
				sql.append(" city_id='");
				sql.append(city_id);
				sql.append("' ");
				
			}else{

				sql.append(" where ");
				sql.append(" city_id='");
				sql.append(city_id);
				sql.append("' or parent_id='");
				sql.append(city_id);
				sql.append("' ");
				
			}
		}else{
			
			if("00".equals(city_id)){
				
				//统计全部，不需要处理！
				
			}else{

				sql.append(" where ");
				sql.append(" city_id='");
				sql.append(city_id);
				sql.append("' or parent_id='");
				sql.append(city_id);
				sql.append("' ");
				
			}
			
		}
		
		sql.append(" ) and ");
		sql.append(" r_timepoint=");
		sql.append(r_timepoint);
		sql.append(" and r_time>=");
		sql.append(startTime);
		sql.append(" and r_time<");
		sql.append(startEnd);
		sql.append(" group by r_time order by r_time");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		return jt.queryForList(sql.toString());
	}

	public List getChildCity(String city_id) {

		String sql = "select city_id,city_name from tab_city where city_id='"
				+ city_id + "' or parent_id='" + city_id + "' order by city_id";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql);
	}
	
	public List getCityBySelf(String city_id) {

		String sql = "select city_id,city_name from tab_city where city_id='"
				+ city_id + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql);
	}
	
	public void setDao(DataSource dao) {
		jt = new JdbcTemplate(dao);
	}
}
