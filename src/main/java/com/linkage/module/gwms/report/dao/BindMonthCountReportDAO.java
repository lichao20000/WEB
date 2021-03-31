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
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

import dao.util.JdbcTemplateExtend;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-11-13
 * @category com.linkage.module.gwms.report.dao
 * 
 */
@SuppressWarnings("rawtypes")
public class BindMonthCountReportDAO 
{
	Logger logger = LoggerFactory.getLogger(BindMonthCountReportDAO.class);
	
	private JdbcTemplateExtend jt;
	
	/**
	 * DATA SOURCE 注入 
	 * @param dao
	 */
	public void setDao(DataSource dao) {
		jt = new JdbcTemplateExtend(dao);
	}
	
	/**
	 * 根据属地查询该属地所有满足条件的用户
	 * 
	 * @param cityId
	 * @param endDataInt
	 * @return
	 */
	public int getUserCount(String cityId,long endDataInt)
	{
		logger.debug("getUserCount(cityId:{},endDataInt:{})",cityId,endDataInt);
		
		PrepareSQL ppSQL = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			ppSQL.append("select count(*) ");
		}else{
			ppSQL.append("select count(1) ");
		}
		
		if(2==LipossGlobals.SystemType()){
			ppSQL.append("from tab_egwcustomer a,tab_customerinfo b ");
			ppSQL.append("where a.customer_id=b.customer_id ");
			
			if(!"00".equals(cityId)){
				List list = CityDAO.getAllNextCityIdsByCityPid(cityId);
				ppSQL.append(PrepareSQL.AND,"b.city_id",list);
				list = null;
			}
		}else{
			ppSQL.append("from tab_hgwcustomer a where 1=1 ");
			if(!"00".equals(cityId)){
				List list = CityDAO.getAllNextCityIdsByCityPid(cityId);
				ppSQL.append(PrepareSQL.AND,"a.city_id",list);
				list = null;
			}
		}
		ppSQL.append(" and a.user_state in ('1','2') ");
		ppSQL.append(" and a.opendate<"+endDataInt);
		
		return jt.queryForInt(ppSQL.toString());
	}
	
	/**
	 * 根据属地查询该属地满足条件的用户
	 * 
	 * @param cityId
	 * @param userTime
	 * @param deviceTime
	 * @return
	 */
	public int getDeviceCount(String cityId,long userTime,long deviceTime)
	{
		logger.debug("getDeviceCount(cityId:{},endDataInt:{})",cityId,deviceTime);
		PrepareSQL ppSQL = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			ppSQL.append("select count(*) from tab_gw_device a ");
		}else{
			ppSQL.append("select count(1) from tab_gw_device a ");
		}
		
		if(2==LipossGlobals.SystemType()){
			ppSQL.append(",tab_egwcustomer b ");
		}else{
			ppSQL.append(",tab_hgwcustomer b ");
		}
		ppSQL.append(" where a.device_id=b.device_id and b.user_state in ('1','2') ");
		ppSQL.append(" and b.opendate<"+userTime);
		ppSQL.append(" and b.binddate<"+deviceTime);
		if(!"00".equals(cityId)){
			List list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			ppSQL.append(PrepareSQL.AND,"a.city_id",list);
			list = null;
		}
		
		return jt.queryForInt(ppSQL.toString());
	}
}
