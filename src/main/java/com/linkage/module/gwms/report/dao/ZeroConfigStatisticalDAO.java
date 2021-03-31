/**
 * 
 */
package com.linkage.module.gwms.report.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.report.dao.interf.I_ZeroConfigStatisticalDAO;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-8-6
 * @category com.linkage.module.gwms.report.dao
 * 
 */
public class ZeroConfigStatisticalDAO extends SuperDAO implements I_ZeroConfigStatisticalDAO
{
	private static Logger logger = LoggerFactory.getLogger(ZeroConfigStatisticalDAO.class);
	
	/**
	 * 查询零配置数据
	 * 
	 * @param cityList
	 * @param startTime
	 * @param endTime
	 * @param bindFlag   “1”成功的   “0”失败的
	 * @return
	 */
	public int getData(List cityList,String startTime,String endTime,String bindFlag){
		
		logger.debug("getData(startTime:{},bindFlag:{})",startTime,bindFlag);
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		sql.append("from tab_hgwcustomer where userline=6 and binddate>");
		sql.append(startTime);
		sql.append(" and binddate<");
		sql.append(endTime);

		if("0".equals(bindFlag)){
			sql.append(" and device_id=null");
		}else{
			if (LipossGlobals.isOracle()){
				sql.append(" and device_id is not null");
			}else if(DBUtil.GetDB()==Global.DB_MYSQL){
				sql.append(" and device_id<>null");
			}else{
				sql.append(" and device_id != null");
			}
		}
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
		logger.debug("ZeroConfigStatisticalDAO=>getData=>sql:{}",sql.toString());
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForInt(psql.getSQL());
	}
	
	/**
	 * 查询成功数据
	 * 
	 * @param cityId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List getBindData(List cityList,String startTime,String endTime){
		
		logger.debug("getBindData(startTime:{},endTime:{})",startTime,endTime);
		StringBuffer sql = new StringBuffer();
		sql.append("select username,device_serialnumber,binddate from tab_hgwcustomer where userline=6 and binddate>");
		sql.append(startTime);
		sql.append(" and binddate<");
		sql.append(endTime);
		sql.append(" and device_id!=null");

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
		
		sql.append(" order by binddate desc");
		logger.debug("ZeroConfigStatisticalDAO=>getData=>sql:{}",sql.toString());
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}
	
	/**
	 * 查询失败数据
	 * 
	 * @param cityId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List getNoBindData(List cityList,String startTime,String endTime)
	{
		logger.debug("getNoBindData(startTime:{},endTime:{})",startTime,endTime);
		StringBuffer sql = new StringBuffer();
	/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
		}else{
			
		} */
		sql.append("select c.device_serialnumber,b.binddate,b.bind_desc ");
		sql.append("from tab_hgwcustomer a,bind_log b,tab_gw_device c ");
		sql.append("where a.bind_flag=b.bind_id and b.device_id=c.device_id and a.userline=6 ");
		sql.append("and a.binddate>"+startTime+" and a.binddate<"+endTime);
		sql.append(" and a.device_id=null");

		if(cityList.size()>0){
			sql.append(" and a.city_id in (");
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
		
		sql.append(" order by a.binddate desc");
		logger.debug("ZeroConfigStatisticalDAO=>getData=>sql:{}",sql.toString());
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}
}
