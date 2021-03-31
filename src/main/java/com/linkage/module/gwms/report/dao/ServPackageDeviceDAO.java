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
import com.linkage.module.gwms.report.dao.interf.I_ServPackageDeviceDAO;

import dao.util.JdbcTemplateExtend;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-6-11
 * @category dao.report
 * 
 */
@SuppressWarnings("unchecked")
public class ServPackageDeviceDAO implements I_ServPackageDeviceDAO{

	private static Logger log = LoggerFactory.getLogger(ServPackageDeviceDAO.class);
	
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
	 * @category 查询所有的套餐类型
	 * 
	 * @return
	 */
	public List getGwServPackage(){
		String sql = "select serv_package_id,serv_package_name from gw_serv_package";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql);
	}
	
	/**
	 * @category 根据属地，时间查询套餐对应的工单
	 * 
	 * @param cityId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List getDevNum(List cityList,long startDate,long endDate){
		
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
			sql.append("select b.serv_package_name,count(*) as num ");
		}else{
			sql.append("select b.serv_package_name,count(1) as num ");
		}
		sql.append("from gw_cust_user_package a,gw_serv_package b,tab_hgwcustomer c ");
		sql.append("where a.user_id=c.user_id and a.serv_package_id=b.serv_package_id ");
		sql.append("and time>"+startDate/1000+" and time<"+endDate/1000);
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
			sql.append(") ");
		}
		sql.append(" group by serv_package_name");
		
		log.debug("getDevNum:"+sql.toString());
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		return jt.queryForList(sql.toString());
		
	}
	
}
