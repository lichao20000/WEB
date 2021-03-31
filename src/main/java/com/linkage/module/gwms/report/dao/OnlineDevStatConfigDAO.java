/**
 * 
 */
package com.linkage.module.gwms.report.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.report.dao.interf.I_OnlineDevStatConfigDAO;

import dao.util.JdbcTemplateExtend;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-6-3
 * @category dao.report
 * 
 */
public class OnlineDevStatConfigDAO implements I_OnlineDevStatConfigDAO{
	
	private static Logger log = LoggerFactory.getLogger(OnlineDevStatConfigDAO.class);
	
	private JdbcTemplateExtend jt;
	
	public void setDao(DataSource dao) {
		this.jt = new JdbcTemplateExtend(dao);
	}

	/**
	 * 取属地
	 * 
	 * @return
	 */
	public List getCity(){
		
		String sqlcity = "select a.city_id,a.city_name,case c.checked when NULL then '' when 'checked' then 'checked' end as  checked from tab_city a left join (select distinct b.city_id,'checked' as checked from gw_online_config b ) c on a.city_id=c.city_id where a.parent_id='00' or a.city_id='00'";
		PrepareSQL psql = new PrepareSQL(sqlcity);
		psql.getSQL();
		return jt.queryForList(sqlcity);
		
	}
	
	/**
	 * @category 取时间
	 * 
	 * @return
	 */
	public List getTimePoint(){
		
		String sqlTimePoint = "select distinct time_point from gw_online_config";
		PrepareSQL psql = new PrepareSQL(sqlTimePoint);
		psql.getSQL();
		return jt.queryForList(sqlTimePoint);
		
	}
	
	/**
	 * 配置入库
	 * 
	 * @param time_point
	 * @param city_id
	 * @return
	 */
	public int batchConfig(String time_point[],String city_id[]){
		
		ArrayList<String> batchSql = new ArrayList<String>();
		batchSql.add("delete from gw_online_config");
		PrepareSQL psql = new PrepareSQL("delete from gw_online_config");
		psql.getSQL();
		if(null!=city_id && null !=time_point ){
			
			for(String temp:city_id){
				
				if("00".equals(temp)){
					
					//batchSql.add(sqlTimeAppend(time_point,temp));
					
					for(String temp_time:time_point){
						
						StringBuffer batchSql_ = new StringBuffer();
						
						batchSql_.append("insert into gw_online_config (time_point,city_id,config_time) values (");
						batchSql_.append(temp_time);
						batchSql_.append(",'");
						batchSql_.append(temp);
						batchSql_.append("',");
						batchSql_.append(date2numeric(new Date()));
						batchSql_.append(") ");
						psql = new PrepareSQL(batchSql_.toString());
						batchSql.add(psql.getSQL());
					}
					
				}else{
					
					String sqlchildcity = " select city_id from tab_city where parent_id='"+temp+"' or city_id='"+temp+"'";
					psql = new PrepareSQL(sqlchildcity);
					psql.getSQL();
					List childlist = jt.queryForList(sqlchildcity);
					
					for(int i=0;i<childlist.size();i++){
						
						Map childMap = (Map) childlist.get(i);
						
						String childCity = String.valueOf(childMap.get("city_id")).toString();
						
						//batchSql.add(sqlTimeAppend(time_point,childCity));
						
						for(String temp_time:time_point){
							
							StringBuffer batchSql_ = new StringBuffer();
							
							batchSql_.append("insert into gw_online_config (time_point,city_id,config_time) values (");
							batchSql_.append(temp_time);
							batchSql_.append(",'");
							batchSql_.append(childCity);
							batchSql_.append("',");
							batchSql_.append(date2numeric(new Date()));
							batchSql_.append(") ");
							psql = new PrepareSQL(batchSql_.toString());
							batchSql.add(psql.getSQL());
						}
					}
				}
			}
		}
		
		//String[] tempSql = batchSql.toString().split(";");
		
		int iCode[] = DataSetBean.doBatch(batchSql);
		if (iCode != null && iCode.length > 0) {
			log.debug("批量执行IPTV策略入库：  成功");
			return 1;
			
		} else {
			log.debug("批量执行IPTV策略入库：  失败");
			return 0;
		}
		
	}
	
	/**
	 * @ 配置时间，sql的拼接
	 * 
	 * @param time_point
	 * @param city_id
	 * @return
	 */
	public String sqlTimeAppend(String time_point[],String city_id){
		
		StringBuffer batchSql = new StringBuffer();
		
		for(String temp:time_point){
			batchSql.append("insert into gw_online_config (time_point,city_id,config_time) values (");
			batchSql.append(temp);
			batchSql.append(",'");
			batchSql.append(city_id);
			batchSql.append("',");
			batchSql.append(date2numeric(new Date()));
			batchSql.append(") ;");
		}
		PrepareSQL psql = new PrepareSQL(batchSql.toString());
		psql.getSQL();
		return batchSql.toString();
		
	}
	
	public static String date2numeric(Date dateTime){;
		long datetime_float = dateTime.getTime()/1000;
		return String.valueOf(datetime_float).toString();
	}
}
