package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.database.PrepareSQL;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * 
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2017-3-30
 * @category com.linkage.module.itms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class QueryBatchModifyTerminalDAO extends SuperDAO
{
	//日志
	private static Logger logger = LoggerFactory.getLogger(QueryBatchModifyTerminalDAO.class);
	//属地
	private Map<String,String> cityMap;
	/*select c.city_id,b.update_time,d.specversion,d.hardwareversion,d.softwareversion,g.vendor_name,b.fail_desc,e.device_model 
   from tab_batch_task a,tab_batch_account b 
left join tab_hgwcustomer f on f.username = b.serv_account 
left join tab_gw_device c on f.device_id = c.device_id
left join tab_vendor g on g.vendor_id=c.vendor_id
left join tab_devicetype_info d on c.devicetype_id = d.devicetype_id
left join gw_device_model e on  d.device_model_id = e.device_model_id
where
a.task_id = b.task_id
and a.add_time=1469423433 
and b.status=-1 
and b.account_type=1*/
	/**
	 * 批量重启终端查询
	 * @param starttime
	 * @param endtime
	 * @param taskName
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> Query(String starttime,String endtime,String taskName,int curPage_splitPage, int num_splitPage,long acc_oid)
	{
		logger.debug("Query({},{},{})", new Object[] { taskName, starttime, endtime });
		logger.warn("1122333444");
		StringBuffer sql = new StringBuffer();
		//sql.append("select * from tab_accounts where acc_oid=1 or parentid=1");
		logger.warn("acc_oid========"+acc_oid);
		if(acc_oid==1)
		{
			logger.warn("1111111111111");
		sql.append("select a.add_time,a.task_name,b.status,count(1) num from tab_batch_task a,tab_batch_account b where a.task_id=b.task_id and b.status!=3");
 	    if(!StringUtil.IsEmpty(starttime))
 	    {
 	    	sql.append(" and a.add_time>="+starttime);
 	    }
 	    if(!StringUtil.IsEmpty(endtime))
 	    {
 	    	sql.append(" and a.add_time<="+endtime);
 	    }
 	    if(!StringUtil.IsEmpty(taskName))
 	    {
 	    	sql.append(" and a.task_name="+"'"+taskName+"'");
 	    }
 	    sql.append(" group by a.add_time,b.status,a.task_name");
 	    
		}
		else
		{
			logger.warn("2222222222222");
			sql.append("select a.add_time,a.task_name,b.status,count(1) num from tab_batch_task a,tab_batch_account b where a.task_id=b.task_id and b.status!=3");
			if(!StringUtil.IsEmpty(starttime))
	 	    {
	 	    	sql.append(" and a.add_time>="+starttime);
	 	    }
	 	    if(!StringUtil.IsEmpty(endtime))
	 	    {
	 	    	sql.append(" and a.add_time<="+endtime);
	 	    }
	 	    if(!StringUtil.IsEmpty(taskName))
	 	    {
	 	    	sql.append(" and a.task_name="+"'"+taskName+"'");
	 	    }
	 	    sql.append(" and a.acc_oid="+acc_oid);
	 	    sql.append(" group by a.add_time,b.status,a.task_name");
			}
		logger.warn("Query方法====》sql====>"+sql.toString());
 	    PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		List<Map> list = querySP(sql.toString(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("add_time", transDate(rs.getString("add_time")));
				map.put("status", rs.getString("status"));
				map.put("num", rs.getString("num"));
				map.put("task_name", rs.getString("task_name"));
				return map;
			}
		});
		return list;
	}
	/**
	 * 分页
	 * @param seconds
	 * @return
	 */
	public int getquerypaging(String starttime,String endtime,String taskName,int curPage_splitPage, int num_splitPage,long acc_oid)
	{
		StringBuffer sql = new StringBuffer();
		if(acc_oid==1)
		{
			logger.warn("1111111111111");
		sql.append("select a.add_time,a.task_name,b.status,count(1) num from tab_batch_task a,tab_batch_account b where a.task_id=b.task_id and b.status!=3");
 	    if(!StringUtil.IsEmpty(starttime))
 	    {
 	    	sql.append(" and a.add_time>="+starttime);
 	    }
 	    if(!StringUtil.IsEmpty(endtime))
 	    {
 	    	sql.append(" and a.add_time<="+endtime);
 	    }
 	    if(!StringUtil.IsEmpty(taskName))
 	    {
 	    	sql.append(" and a.task_name="+"'"+taskName+"'");
 	    }
 	    sql.append(" group by a.add_time,b.status,a.task_name");
 	    
		}
		else
		{
			logger.warn("2222222222222");
			sql.append("select a.add_time,a.task_name,b.status,count(1) num from tab_batch_task a,tab_batch_account b where a.task_id=b.task_id and b.status!=3");
			if(!StringUtil.IsEmpty(starttime))
	 	    {
	 	    	sql.append(" and a.add_time>="+starttime);
	 	    }
	 	    if(!StringUtil.IsEmpty(endtime))
	 	    {
	 	    	sql.append(" and a.add_time<="+endtime);
	 	    }
	 	    if(!StringUtil.IsEmpty(taskName))
	 	    {
	 	    	sql.append(" and a.task_name="+"'"+taskName+"'");
	 	    }
	 	    sql.append(" and a.acc_oid="+acc_oid);
	 	    sql.append(" group by a.add_time,b.status,a.task_name");
			}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
 	    logger.warn("===========================================");
 	    logger.warn("sql-------------------"+sql.toString());
 	    int total = list.size();
		int maxPage = 1;
		if (total % num_splitPage == 0)
		{
			maxPage = total / num_splitPage;
		}
		else
		{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	/**
	 * 点击跳转查询
	 * 成功、未执行、失败公用
	 * @param account_type
	 * @param serv_account
	 * @return
	 */
	public List<Map> communalquery(String type,String add_time,int curPage_splitPage, int num_splitPage,long acc_oid,String task_name)
	{
		logger.warn("communalquery=====>方法入口");
		logger.warn("communalquery========方法时间===> "+add_time);
	 StringBuffer	sb=new StringBuffer();
	 StringBuffer sql = new StringBuffer();
	 sql.append("select b.account_type,b.serv_account from tab_batch_task a,tab_batch_account b where a.task_id=b.task_id and rownum=1");
		sql.append(" and a.add_time=" + add_time);
		sql.append(" and a.task_name= '" + task_name + "'");
		sql.append(" and b.status=" + type);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		logger.warn("点击跳转查询-------------->"+sql.toString());
		list = jt.queryForList(psql.getSQL());
	if(list != null && list.size() > 0)
	{
		String account_type = StringUtil.getStringValue(list.get(0).get(
				"account_type"));
		String serv_account = StringUtil.getStringValue(list.get(0).get(
				"serv_account"));
	 if(acc_oid==1)
	 {
		 logger.warn(" acc_oid=1===========>");
	 if("1".equals(account_type))
	 {
		 sb.append("select c.city_id,b.update_time,d.specversion,d.hardwareversion,d.softwareversion,g.vendor_name,b.fail_desc,e.device_model "
				 + " from tab_batch_task a,tab_batch_account b "
				 + " left join tab_hgwcustomer f on f.username = b.serv_account" 
				 +" left join tab_gw_device c on f.device_id = c.device_id"
				 +" left join tab_vendor g on g.vendor_id=c.vendor_id"
				 +" left join tab_devicetype_info d on c.devicetype_id = d.devicetype_id"
				 +" left join gw_device_model e on  d.device_model_id = e.device_model_id   where a.task_id = b.task_id");
			sb.append(" and a.add_time=" + add_time);
			sb.append(" and b.status=" + type);
			sb.append(" and b.account_type=" + 1);
	 }
	 else if("2".equals(account_type))
	 {
		 sb.append("select c.city_id,b.update_time,d.specversion,d.hardwareversion,d.softwareversion,g.vendor_name,b.fail_desc,e.device_model "
			 + " from tab_batch_task a,tab_batch_account b "
			 + " left join tab_hgwcustomer f on f.username = b.serv_account" 
			 +" left join tab_gw_device c on f.device_id = c.device_id"
			 +" left join tab_vendor g on g.vendor_id=c.vendor_id"
			 +" left join tab_devicetype_info d on c.devicetype_id = d.devicetype_id"
			 +" left join gw_device_model e on  d.device_model_id = e.device_model_id   where a.task_id = b.task_id");
		sb.append(" and a.add_time=" + add_time);
		sb.append(" and b.status=" + type);
		sb.append(" and b.account_type=" + 2);
	 }
	 else
	 {
		 List<Map> errList = new ArrayList<Map>();
		return errList;
	 }
	 }
	 else
	 {
		 logger.warn(" acc_oid不等于1===========>");
		 if("1".equals(account_type))
		 {
			 sb.append("select c.city_id,b.update_time,d.specversion,d.hardwareversion,d.softwareversion,g.vendor_name,b.fail_desc,e.device_model "
					 + " from tab_batch_task a,tab_batch_account b "
					 + " left join tab_hgwcustomer f on f.username = b.serv_account" 
					 +" left join tab_gw_device c on f.device_id = c.device_id"
					 +" left join tab_vendor g on g.vendor_id=c.vendor_id"
					 +" left join tab_devicetype_info d on c.devicetype_id = d.devicetype_id"
					 +" left join gw_device_model e on  d.device_model_id = e.device_model_id   where a.task_id = b.task_id");
				sb.append(" and a.add_time=" + add_time);
				sb.append(" and b.status=" + type);
				sb.append(" and b.account_type=" + 1);
				sb.append(" and a.acc_oid="+acc_oid);
		 }
		 else if("2".equals(account_type))
		 {
			 sb.append("select c.city_id,b.update_time,d.specversion,d.hardwareversion,d.softwareversion,g.vendor_name,b.fail_desc,e.device_model "
					 + " from tab_batch_task a,tab_batch_account b "
					 + " left join tab_hgwcustomer f on f.username = b.serv_account" 
					 +" left join tab_gw_device c on f.device_id = c.device_id"
					 +" left join tab_vendor g on g.vendor_id=c.vendor_id"
					 +" left join tab_devicetype_info d on c.devicetype_id = d.devicetype_id"
					 +" left join gw_device_model e on  d.device_model_id = e.device_model_id   where a.task_id = b.task_id");
				sb.append(" and a.add_time=" + add_time);
				sb.append(" and b.status=" + type);
				sb.append(" and b.account_type=" + 2);
				sb.append(" and a.acc_oid="+acc_oid);
		 } 
		 else
		 {
			 List<Map> errList = new ArrayList<Map>();
				return errList;
		 }
	 }
	 	cityMap = CityDAO.getCityIdCityNameMap();
	 	logger.warn("点击跳转查询sql=======>"+sb.toString());
	 List<Map> showlist = querySP(sb.toString(), (curPage_splitPage - 1)
				* num_splitPage + 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				 String city_name=null;
				Map<String, String> map = new HashMap<String, String>();
				logger.warn("city_id====> "+rs.getString("city_id"));
				map.put("city_id", rs.getString("city_id"));
				String cityid=StringUtil.getStringValue(rs.getString("city_id"));
				if(!StringUtil.IsEmpty(cityid))
				{
				 city_name = StringUtil.getStringValue(cityMap.get(cityid));
				}
				else
				{
				 city_name="101001";
				}
				logger.warn("city_name====> "+city_name);
				map.put("city_name", city_name);
				map.put("update_time", transDate(rs.getString("update_time")));
				map.put("specversion", rs.getString("specversion"));
				map.put("hardwareversion", rs.getString("hardwareversion"));
				map.put("softwareversion", rs.getString("softwareversion"));
				map.put("vendor_name", rs.getString("vendor_name"));
				map.put("fail_desc", rs.getString("fail_desc"));
				map.put("device_model", rs.getString("device_model"));
				return map;
			}
		});
	 return showlist;
	}
	List<Map> errList = new ArrayList<Map>();
	return errList;
	}
	/**
	 * 点击跳转分页
	 * 成功、未执行、失败公用
	 * @return
	 */
	public int communalPaging(String type,String add_time,int curPage_splitPage, int num_splitPage,long acc_oid,String task_name)
	{
		logger.warn("communalPaging=====>方法入口");
	 StringBuffer	sb=new StringBuffer();
	 StringBuffer sql = new StringBuffer();
	 sql.append("select b.account_type,a.add_time,b.serv_account from tab_batch_task a,tab_batch_account b where a.task_id=b.task_id and rownum=1");
		sql.append(" and a.add_time=" + add_time);
		sql.append(" and a.task_name= '" + task_name + "'");
		sql.append(" and b.status=" + type);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
	if(list != null && list.size() > 0)
	{
		String account_type = StringUtil.getStringValue(list.get(0).get(
				"account_type"));
		String serv_account = StringUtil.getStringValue(list.get(0).get(
				"serv_account"));
	 if(acc_oid==1)
	 {
	 if("1".equals(account_type))
	 {
		 sb.append("select count(*)"
				 + " from tab_batch_task a,tab_batch_account b "
				 + " left join tab_hgwcustomer f on f.username = b.serv_account" 
				 +" left join tab_gw_device c on f.device_id = c.device_id"
				 +" left join tab_vendor g on g.vendor_id=c.vendor_id"
				 +" left join tab_devicetype_info d on c.devicetype_id = d.devicetype_id"
				 +" left join gw_device_model e on  d.device_model_id = e.device_model_id   where a.task_id = b.task_id");
			sb.append(" and a.add_time=" + add_time);
			sb.append(" and b.status=" + type);
			sb.append(" and b.account_type=" + 1);
	 }
	 else if("2".equals(account_type))
	 {
		 sb.append("select count(*)"
				 + " from tab_batch_task a,tab_batch_account b "
				 + " left join tab_hgwcustomer f on f.username = b.serv_account" 
				 +" left join tab_gw_device c on f.device_id = c.device_id"
				 +" left join tab_vendor g on g.vendor_id=c.vendor_id"
				 +" left join tab_devicetype_info d on c.devicetype_id = d.devicetype_id"
				 +" left join gw_device_model e on  d.device_model_id = e.device_model_id   where a.task_id = b.task_id");
				sb.append(" and a.add_time=" + add_time);
				sb.append(" and b.status=" + type);
				sb.append(" and b.account_type=" + 2);
	 }
	 else
	 {
		 return 0;
	 }
	 }
	 else
	 {
		 if("1".equals(account_type))
		 {
			 sb.append("select count(*)"
					 + " from tab_batch_task a,tab_batch_account b "
					 + " left join tab_hgwcustomer f on f.username = b.serv_account" 
					 +" left join tab_gw_device c on f.device_id = c.device_id"
					 +" left join tab_vendor g on g.vendor_id=c.vendor_id"
					 +" left join tab_devicetype_info d on c.devicetype_id = d.devicetype_id"
					 +" left join gw_device_model e on  d.device_model_id = e.device_model_id   where a.task_id = b.task_id");
				sb.append(" and a.add_time=" + add_time);
				sb.append(" and b.status=" + type);
				sb.append(" and b.account_type=" + 1);
				sb.append(" and a.acc_oid="+acc_oid);
		 }
		 else if("2".equals(account_type))
		 {
			 sb.append("select count(*)"
					 + " from tab_batch_task a,tab_batch_account b "
					 + " left join tab_hgwcustomer f on f.username = b.serv_account" 
					 +" left join tab_gw_device c on f.device_id = c.device_id"
					 +" left join tab_vendor g on g.vendor_id=c.vendor_id"
					 +" left join tab_devicetype_info d on c.devicetype_id = d.devicetype_id"
					 +" left join gw_device_model e on  d.device_model_id = e.device_model_id   where a.task_id = b.task_id");
					sb.append(" and a.add_time=" + add_time);
					sb.append(" and b.status=" + type);
					sb.append(" and b.account_type=" + 2);
					sb.append(" and a.acc_oid="+acc_oid);
		 }
		 else
		 {
			 return 0;
		 }
	 }
	
	 	cityMap = CityDAO.getCityIdCityNameMap();
	 	logger.warn(" 点击跳转分页sql=====>"+sb.toString());
		PrepareSQL ps = new PrepareSQL(sb.toString());
		int total = jt.queryForInt(ps.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0)
		{
			maxPage = total / num_splitPage;
		}
		else
		{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	return 0;
	}
	/**
	 * 点击跳转导出功能
	 * 成功、未执行、失败公用
	 * @param seconds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> communalDerive(String type,String add_time,long acc_oid,String task_name)
	{
	 StringBuffer	sb=new StringBuffer();
	 StringBuffer sql = new StringBuffer();
	 logger.warn("communalDerive==========>开始");
	 logger.warn("communalDerive========方法时间===> "+add_time);
	 sql.append("select b.account_type,b.serv_account from tab_batch_task a,tab_batch_account b where a.task_id=b.task_id and rownum=1");
		sql.append(" and a.task_name= '" + task_name + "'");
		sql.append(" and b.status=" + type);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		logger.warn("communalDerive=== list集合===> "+sql.toString());
	if(list != null && list.size() > 0)
	{	logger.warn("11111111111");
		String account_type = StringUtil.getStringValue(list.get(0).get(
				"account_type"));
		String serv_account = StringUtil.getStringValue(list.get(0).get(
				"serv_account"));
	 if(acc_oid==1)
	 {
	 if("1".equals(account_type))
	 {logger.warn("2222222222");
		 sb.append("select c.city_id,b.update_time,d.specversion,d.hardwareversion,d.softwareversion,g.vendor_name,b.fail_desc,e.device_model "
				 + " from tab_batch_task a,tab_batch_account b "
				 + " left join tab_hgwcustomer f on f.username = b.serv_account" 
				 +" left join tab_gw_device c on f.device_id = c.device_id"
				 +" left join tab_vendor g on g.vendor_id=c.vendor_id"
				 +" left join tab_devicetype_info d on c.devicetype_id = d.devicetype_id"
				 +" left join gw_device_model e on  d.device_model_id = e.device_model_id   where a.task_id = b.task_id");
			sb.append(" and a.add_time=" + add_time);
			sb.append(" and b.status=" + type);
			sb.append(" and b.account_type=" + 1);
	 }
	 else if("2".equals(account_type))
	 {
		 sb.append("select c.city_id,b.update_time,d.specversion,d.hardwareversion,d.softwareversion,g.vendor_name,b.fail_desc,e.device_model "
				 + " from tab_batch_task a,tab_batch_account b "
				 + " left join tab_hgwcustomer f on f.username = b.serv_account" 
				 +" left join tab_gw_device c on f.device_id = c.device_id"
				 +" left join tab_vendor g on g.vendor_id=c.vendor_id"
				 +" left join tab_devicetype_info d on c.devicetype_id = d.devicetype_id"
				 +" left join gw_device_model e on  d.device_model_id = e.device_model_id   where a.task_id = b.task_id");
				sb.append(" and a.add_time=" + add_time);
				sb.append(" and b.status=" + type);
				sb.append(" and b.account_type=" + 2);
	 }
	 else
	 {
		 List<Map> errList = new ArrayList<Map>();
			return errList;
	 }
	 }
	 else
	 {
		 if("1".equals(account_type))
		 {
			 sb.append("select c.city_id,b.update_time,d.specversion,d.hardwareversion,d.softwareversion,g.vendor_name,b.fail_desc,e.device_model "
					 + " from tab_batch_task a,tab_batch_account b "
					 + " left join tab_hgwcustomer f on f.username = b.serv_account" 
					 +" left join tab_gw_device c on f.device_id = c.device_id"
					 +" left join tab_vendor g on g.vendor_id=c.vendor_id"
					 +" left join tab_devicetype_info d on c.devicetype_id = d.devicetype_id"
					 +" left join gw_device_model e on  d.device_model_id = e.device_model_id   where a.task_id = b.task_id");
				sb.append(" and a.add_time=" + add_time);
				sb.append(" and b.status=" + type);
				sb.append(" and b.account_type=" + 1);
				sb.append(" and a.acc_oid="+acc_oid);
		 }
		 else if("2".equals(account_type))
		 {
			 sb.append("select c.city_id,b.update_time,d.specversion,d.hardwareversion,d.softwareversion,g.vendor_name,b.fail_desc,e.device_model "
					 + " from tab_batch_task a,tab_batch_account b "
					 + " left join tab_hgwcustomer f on f.username = b.serv_account" 
					 +" left join tab_gw_device c on f.device_id = c.device_id"
					 +" left join tab_vendor g on g.vendor_id=c.vendor_id"
					 +" left join tab_devicetype_info d on c.devicetype_id = d.devicetype_id"
					 +" left join gw_device_model e on  d.device_model_id = e.device_model_id   where a.task_id = b.task_id");
					sb.append(" and a.add_time=" + add_time);
					sb.append(" and b.status=" + type);
					sb.append(" and b.account_type=" + 2);
					sb.append(" and a.acc_oid="+acc_oid);
		 }
		 else
		 {
			 List<Map> errList = new ArrayList<Map>();
				return errList;
		 }
	 }
	 	cityMap = CityDAO.getCityIdCityNameMap();
	 	PrepareSQL ps = new PrepareSQL(sb.toString());
	 	logger.warn(" 点击跳转导出功能sql=========>"+sb.toString());
	 	List<Map> showlist = jt.query(ps.getSQL(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int rowNum) throws SQLException
			{   String city_name=null;
				Map<String, String> map = new HashMap<String, String>();
				map.put("city_id", rs.getString("city_id"));
				String cityid=StringUtil.getStringValue(rs.getString("city_id"));
				logger.warn("city_name========"+rs.getString("city_id"));
				if(!StringUtil.IsEmpty(cityid))
				{
					city_name = StringUtil.getStringValue(cityMap.get(rs
							.getString("city_id")));
				}
				else{
					city_name="101001";
				}
				logger.warn("city_name========"+city_name);
				map.put("city_name", city_name);
				map.put("update_time", rs.getString("update_time"));
				map.put("specversion", rs.getString("specversion"));
				map.put("hardwareversion", rs.getString("hardwareversion"));
				map.put("softwareversion", rs.getString("softwareversion"));
				map.put("vendor_name", rs.getString("vendor_name"));
				map.put("fail_desc", rs.getString("fail_desc"));
				map.put("device_model", rs.getString("device_model"));
				return map;
			}
		});
	 return showlist;
	}
	List<Map> errList = new ArrayList<Map>();
	return errList;
	}
	/**
	 * 总数查询
	 * @param add_time
	 * @param task_name
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> sunQuery(String add_time, String task_name, int curPage_splitPage,
			int num_splitPage,long acc_oid)
	{
		StringBuffer sql = new StringBuffer();
		if(acc_oid==1)
		{
		/*sql.append("select b.update_time,b.status,count(1) num from tab_batch_task a,tab_batch_account b "
				+ "where a.task_id = b.task_id");*/
		sql.append("select c.city_id,b.status,count(1) num,f.username,c.device_serialnumber,b.update_time,d.specversion,d.hardwareversion,d.softwareversion,g.vendor_name,b.fail_desc,e.device_model "
				 + " from tab_batch_task a,tab_batch_account b "
				 + " left join tab_hgwcustomer f on f.username = b.serv_account" 
				 +" left join tab_gw_device c on f.device_id = c.device_id"
				 +" left join tab_vendor g on g.vendor_id=c.vendor_id"
				 +" left join tab_devicetype_info d on c.devicetype_id = d.devicetype_id"
				 +" left join gw_device_model e on  d.device_model_id = e.device_model_id   where a.task_id = b.task_id");
		sql.append(" and a.add_time=" + add_time);
		sql.append(" and a.task_name= '" + task_name + "'");
		sql.append("  group by c.city_id,b.update_time,b.status,f.username,c.device_serialnumber, d.specversion,d.hardwareversion,d.softwareversion,g.vendor_name,b.fail_desc,e.device_model");
		}
		else
		{
			sql.append("select c.city_id,b.status,count(1) num,f.username,c.device_serialnumber,b.update_time,d.specversion,d.hardwareversion,d.softwareversion,g.vendor_name,b.fail_desc,e.device_model "
					 + " from tab_batch_task a,tab_batch_account b "
					 + " left join tab_hgwcustomer f on f.username = b.serv_account" 
					 +" left join tab_gw_device c on f.device_id = c.device_id"
					 +" left join tab_vendor g on g.vendor_id=c.vendor_id"
					 +" left join tab_devicetype_info d on c.devicetype_id = d.devicetype_id"
					 +" left join gw_device_model e on  d.device_model_id = e.device_model_id   where a.task_id = b.task_id");
			sql.append(" and a.add_time=" + add_time);
			sql.append(" and a.task_name= '" + task_name + "'");
			sql.append(" and a.acc_oid="+acc_oid);
			sql.append("  group by c.city_id,b.update_time,b.status,f.username,c.device_serialnumber, d.specversion,d.hardwareversion,d.softwareversion,g.vendor_name,b.fail_desc,e.device_model");
		}
		logger.warn("总数查询sql======>"+sql.toString());
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = querySP(sql.toString(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				String city_name=null;
				Map<String, String> map = new HashMap<String, String>();
				map.put("update_time", rs.getString("update_time"));
				map.put("status", rs.getString("status"));
				map.put("num", rs.getString("num"));
				map.put("city_id", rs.getString("city_id"));
				String cityid=StringUtil.getStringValue(rs.getString("city_id"));
				if(!StringUtil.IsEmpty(cityid))
				{
					city_name = StringUtil.getStringValue(cityMap.get(rs
							.getString("city_id")));
				}
				else
				{
					city_name="101001";
				}
				map.put("city_name", city_name);
				map.put("username", rs.getString("username"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("specversion", rs.getString("specversion"));
				map.put("hardwareversion", rs.getString("hardwareversion"));
				map.put("softwareversion", rs.getString("softwareversion"));
				map.put("vendor_name", rs.getString("vendor_name"));
				map.put("fail_desc", rs.getString("fail_desc"));
				map.put("device_model", rs.getString("device_model"));
				return map;
			}
		});
		return list;
	}
	/**
	 * 总数导出
	 * @param seconds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> sunQueryDrive(String add_time, String task_name,long acc_oid)
	{
		StringBuffer sql = new StringBuffer();
		if(acc_oid==1)
		{
			sql.append("select c.city_id,b.status,count(1) num,f.username,c.device_serialnumber,b.update_time,d.specversion,d.hardwareversion,d.softwareversion,g.vendor_name,b.fail_desc,e.device_model "
					 + " from tab_batch_task a,tab_batch_account b "
					 + " left join tab_hgwcustomer f on f.username = b.serv_account" 
					 +" left join tab_gw_device c on f.device_id = c.device_id"
					 +" left join tab_vendor g on g.vendor_id=c.vendor_id"
					 +" left join tab_devicetype_info d on c.devicetype_id = d.devicetype_id"
					 +" left join gw_device_model e on  d.device_model_id = e.device_model_id   where a.task_id = b.task_id");
			sql.append(" and a.add_time=" + add_time);
			sql.append(" and a.task_name= '" + task_name + "'");
			sql.append("  group by c.city_id,b.update_time,b.status,f.username,c.device_serialnumber, d.specversion,d.hardwareversion,d.softwareversion,g.vendor_name,b.fail_desc,e.device_model");
		}
		else
		{
			sql.append("select c.city_id,b.status,count(1) num,f.username,c.device_serialnumber,b.update_time,d.specversion,d.hardwareversion,d.softwareversion,g.vendor_name,b.fail_desc,e.device_model "
					 + " from tab_batch_task a,tab_batch_account b "
					 + " left join tab_hgwcustomer f on f.username = b.serv_account" 
					 +" left join tab_gw_device c on f.device_id = c.device_id"
					 +" left join tab_vendor g on g.vendor_id=c.vendor_id"
					 +" left join tab_devicetype_info d on c.devicetype_id = d.devicetype_id"
					 +" left join gw_device_model e on  d.device_model_id = e.device_model_id   where a.task_id = b.task_id");
			sql.append(" and a.add_time=" + add_time);
			sql.append(" and a.task_name= '" + task_name + "'");
			sql.append(" and a.acc_oid="+acc_oid);
			sql.append("  group by c.city_id,b.update_time,f.username,c.device_serialnumber,b.status, d.specversion,d.hardwareversion,d.softwareversion,g.vendor_name,b.fail_desc,e.device_model");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		logger.warn("总数导出sql======》"+sql.toString());
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException
			{
				String city_name=null;
				Map<String, String> map = new HashMap<String, String>();
				map.put("update_time", rs.getString("update_time"));
				map.put("status", rs.getString("status"));
				map.put("num", rs.getString("num"));
				map.put("city_id", rs.getString("city_id"));
				String cityid=StringUtil.getStringValue(rs.getString("city_id"));
				if(!StringUtil.IsEmpty(cityid))
				{
				 city_name = StringUtil.getStringValue(cityMap.get(rs
						.getString("city_id")));
				}
				else
				{
					city_name="101001";
				}
				map.put("city_name", city_name);
				map.put("username", rs.getString("username"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("specversion", rs.getString("specversion"));
				map.put("hardwareversion", rs.getString("hardwareversion"));
				map.put("softwareversion", rs.getString("softwareversion"));
				map.put("vendor_name", rs.getString("vendor_name"));
				map.put("fail_desc", rs.getString("fail_desc"));
				map.put("device_model", rs.getString("device_model"));
				return map;
			}
		});
		return list;
	}
	/**
	 * 总数分页
	 * @param seconds
	 * @return
	 */
	public int sunQuerypaging(String add_time, String task_name, int curPage_splitPage,
			int num_splitPage,long acc_oid)
	{
		StringBuffer sql = new StringBuffer();
		if(acc_oid==1)
		{
			sql.append("select count(1)"
					 + " from tab_batch_task a,tab_batch_account b "
					 + " left join tab_hgwcustomer f on f.username = b.serv_account" 
					 +" left join tab_gw_device c on f.device_id = c.device_id"
					 +" left join tab_vendor g on g.vendor_id=c.vendor_id"
					 +" left join tab_devicetype_info d on c.devicetype_id = d.devicetype_id"
					 +" left join gw_device_model e on  d.device_model_id = e.device_model_id   where a.task_id = b.task_id");
		sql.append(" and a.add_time=" + add_time);
		sql.append(" and a.task_name= '" + task_name + "'");
		}
		else
		{
			sql.append("select count(1)"
					 + " from tab_batch_task a,tab_batch_account b "
					 + " left join tab_hgwcustomer f on f.username = b.serv_account" 
					 +" left join tab_gw_device c on f.device_id = c.device_id"
					 +" left join tab_vendor g on g.vendor_id=c.vendor_id"
					 +" left join tab_devicetype_info d on c.devicetype_id = d.devicetype_id"
					 +" left join gw_device_model e on  d.device_model_id = e.device_model_id   where a.task_id = b.task_id");
			sql.append(" and a.add_time=" + add_time);
			sql.append(" and a.task_name= '" + task_name + "'");
			sql.append(" and a.acc_oid="+acc_oid);
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		PrepareSQL ps = new PrepareSQL(psql.toString());
		logger.warn("总数分页sql======>"+sql.toString());
		int total = jt.queryForInt(ps.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0)
		{
			maxPage = total / num_splitPage;
		}
		else
		{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	private static final String transDate(Object seconds)
	{
		if (seconds != null)
		{
			try
			{
				DateTimeUtil dt = new DateTimeUtil(
						Long.parseLong(seconds.toString()) * 1000);
				return dt.getLongDate();
			}
			catch (NumberFormatException e)
			{
				logger.error(e.getMessage(), e);
			}
			catch (Exception e)
			{
				logger.error(e.getMessage(), e);
			}
		}
		return "";
	}
	
}
