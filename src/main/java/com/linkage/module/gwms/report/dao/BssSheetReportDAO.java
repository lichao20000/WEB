/**
 * 
 */
package com.linkage.module.gwms.report.dao;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.report.dao.interf.I_BssSheetReportDAO;

import dao.util.JdbcTemplateExtend;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-6-11
 * @category dao.report
 * 
 */
@SuppressWarnings("unchecked")
public class BssSheetReportDAO implements I_BssSheetReportDAO{

	private static Logger log = LoggerFactory.getLogger(BssSheetReportDAO.class);
	
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
	 * @category 查询所有的操作类型
	 * 
	 * @return
	 */
	public List getGwOperType(){
		String sql = "select oper_type_id,oper_type_name from tab_gw_oper_type where type=1";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql);
	}
	
	/**
	 * @category 根据属地，时间查询北向发送过来的工单
	 * 
	 * @param cityId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List getBssSheet(List cityList,long startDate,long endDate){
		
		StringBuffer sql = new StringBuffer();
		sql.append("select city_id,type,result_spec_state from tab_bss_sheet");
		sql.append(" where receive_date>");
		sql.append(startDate);
		sql.append(" and receive_date<");
		sql.append(endDate);
		if(cityList.size()>0){
			sql.append(" and city_id in (");
			
			for(int i=0;i<cityList.size();i++){
				if(i!=0){
					sql.append(",");
				}
				sql.append("'");
				sql.append(cityList.get(i));
				sql.append("'");
			}
			sql.append(")");
		}
		
		log.debug("getBssSheet:"+sql.toString());
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		return jt.queryForList(sql.toString());
		
	}
	
}
