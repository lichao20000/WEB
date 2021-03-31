
package com.linkage.module.itms.report.dao;

import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * @author fangchao (Ailk No.)
 * @version 1.0
 * @since 2013-7-19
 * @category com.linkage.module.itms.report.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class UserDeviceReportDAO extends SuperDAO
{

	/**
	 * 获取E8-B用户数
	 * 
	 * @param cityId
	 * @param startTimeInSecond
	 * @param endTimeInSecond
	 * @return
	 */
	public List countE8BUser(String startTimeInSecond, String endTimeInSecond)
	{
		PrepareSQL pSql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			pSql.append("select a.city_id, count(*) as e8b_count ");
		}else{
			pSql.append("select a.city_id, count(1) as e8b_count ");
		}
		pSql.append("from tab_hgwcustomer a, gw_cust_user_dev_type b ");
		pSql.append("where a.user_id = b.user_id and b.type_id = '1'");
		if (!StringUtil.IsEmpty(startTimeInSecond))
		{
			pSql.append(" and a.opendate >= "+startTimeInSecond);
		}
		if (!StringUtil.IsEmpty(endTimeInSecond))
		{
			pSql.append(" and a.opendate < "+endTimeInSecond);
		}
		pSql.append(" group by a.city_id");
		return jt.queryForList(pSql.getSQL());
	}

	/**
	 * 统计E8-C和政企融合网关的用户数
	 * 
	 * @param cityId
	 * @param startTimeInSecond
	 * @param endTimeInSecond
	 * @return
	 */
	public List<Map<String, String>> countE8CUser(String startTimeInSecond,
			String endTimeInSecond)
	{
		PrepareSQL pSql = new PrepareSQL();
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		pSql.append("select a.city_id, count(case c.gw_type when '1' then 1 else null end) e8c_count,"
				+ " count(case c.gw_type when '2' then 1 else null end) all_e8c_count"
				+ " from tab_hgwcustomer a, gw_cust_user_dev_type b, tab_bss_dev_port c"
				+ " where a.user_id = b.user_id and a.spec_id = c.id and b.type_id = '2'");
		if (!StringUtil.IsEmpty(startTimeInSecond))
		{
			pSql.append(" and a.opendate >= "+startTimeInSecond);
		}
		if (!StringUtil.IsEmpty(endTimeInSecond))
		{
			pSql.append(" and a.opendate < "+endTimeInSecond);
		}
		pSql.append(" group by a.city_id");
		return jt.queryForList(pSql.getSQL());
	}

	/**
	 * 统计绑定设备E8-B用户数
	 * 
	 * @param city_id
	 * @param startTimeInSecond
	 * @param endTimeInSecond
	 * @return
	 */
	public List countBindE8BUser(String startTimeInSecond, String endTimeInSecond)
	{
		PrepareSQL pSql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			pSql.append("select a.city_id, count(*) as bind_e8b_count ");
		}else{
			pSql.append("select a.city_id, count(1) as bind_e8b_count ");
		}
		pSql.append("from tab_hgwcustomer a,gw_cust_user_dev_type b ");
		pSql.append("where a.user_id=b.user_id and b.type_id='1' ");
		pSql.append("and a.device_id!=null and a.device_id!='' ");
		if (!StringUtil.IsEmpty(startTimeInSecond))
		{
			pSql.append(" and a.opendate >= "+startTimeInSecond);
		}
		if (!StringUtil.IsEmpty(endTimeInSecond))
		{
			pSql.append(" and a.opendate < "+endTimeInSecond);
		}
		pSql.append(" group by a.city_id");
		return jt.queryForList(pSql.getSQL());
	}

	/**
	 * 统计设备绑定的E8-C和政企融合网关的用户数
	 * 
	 * @param cityId
	 * @param startTimeInSecond
	 * @param endTimeInSecond
	 * @return
	 */
	public List<Map<String, String>> countBindE8CUser(String startTimeInSecond,
			String endTimeInSecond)
	{
		PrepareSQL pSql = new PrepareSQL();
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		pSql.append("select a.city_id, count(case c.gw_type when '1' then 1 else null end) bind_e8c_count,"
				+ " count(case c.gw_type when '2' then 1 else null end) bind_all_e8c_count"
				+ " from tab_hgwcustomer a, gw_cust_user_dev_type b, tab_bss_dev_port c"
				+ " where a.user_id = b.user_id and a.spec_id = c.id and b.type_id = '2'"
				+ " and a.device_id != null and a.device_id != ''");
		if (!StringUtil.IsEmpty(startTimeInSecond))
		{
			pSql.append(" and a.opendate >= "+startTimeInSecond);
		}
		if (!StringUtil.IsEmpty(endTimeInSecond))
		{
			pSql.append(" and a.opendate < "+endTimeInSecond);
		}
		pSql.append(" group by a.city_id");
		return jt.queryForList(pSql.getSQL());
	}
}
