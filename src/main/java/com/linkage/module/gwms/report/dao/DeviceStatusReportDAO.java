/**
 * 
 */
package com.linkage.module.gwms.report.dao;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.report.dao.interf.I_DeviceStatusReportDAO;

import dao.util.JdbcTemplateExtend;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-8-4
 * @category com.linkage.module.gwms.report.dao
 * 
 */
public class DeviceStatusReportDAO implements I_DeviceStatusReportDAO{
	
	private static Logger log = LoggerFactory.getLogger(DeviceStatusReportDAO.class);
	
	private JdbcTemplateExtend jt;
	
	public void setDao(DataSource dao) {
		jt = new JdbcTemplateExtend(dao);
	}
	
	/**
	 * @category 查询所有的子属地，返回
	 * 
	 * @param cityId
	 * @return
	 */
	public List getChildCity(String cityId){
		
		StringBuffer sql = new StringBuffer();
		sql.append(" select city_id,city_name,parent_id from tab_city ");
		
		if(null!=cityId && !"".equals(cityId)){
			sql.append(" where city_id='");
			sql.append(cityId);
			sql.append("' or parent_id='");
			sql.append(cityId);
			sql.append("' ");
		}
		
		sql.append(" order by city_id ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		return jt.queryForList(sql.toString());
	}
	
	/**
	 * @category 查询所有属地
	 * 
	 * @return
	 */
	public List getAllCity(){
		
		String sql = "select city_id,city_name,parent_id from tab_city ";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql.toString());
	}
	
	/**
	 * @category 根据属地，时间查询套餐对应的工单
	 * 
	 * @param cityId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public int getDevNum(List cityList,long startDate,long endDate,int cpe_allocatedstatus)
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select count(*) ");
		}else{
			psql.append("select count(1) ");
		}
		psql.append("from tab_gw_device where complete_time>"+startDate/1000);
		psql.append(" and complete_time<"+endDate/1000);
		psql.append(" and cpe_allocatedstatus="+cpe_allocatedstatus);
		if(cityList.size()>0){
			psql.append(" and city_id in (");
			
			for(int i=0;i<cityList.size();i++){
				if(i!=0){
					psql.append(",");
				}
				psql.append("'"+cityList.get(i)+"'");
			}
			psql.append(") ");
		}
		
		return jt.queryForInt(psql.getSQL());
	}
	
}
