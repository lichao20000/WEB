package com.linkage.module.ids.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DBOperation;
import com.linkage.litms.uss.DateUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

public class HttpQualityTestDAO extends SuperDAO 
{
	private Logger logger = LoggerFactory.getLogger(HttpQualityTestDAO.class);

	/**
	 * 定制任务信息
	 * @param taskname
	 * @param taskid
	 * @param accoid
	 * @param addtime
	 * @param url
	 * @param levelreport
	 * @param filename
	 * @param devs
	 * @return
	 */
	public int addTask(String taskname, String taskid, long accoid,String addtime, 
			String url, String levelreport, String filename,long devs) 
	{
		PrepareSQL psql = new PrepareSQL();
		StringBuffer sql = new StringBuffer();
		sql.append("insert into tab_ids_task (task_name,task_id,acc_oid," +
				"add_time,type_id,run_time,task_statu,url,level_report,file_name,devs) ");
		sql.append(" values(?,?,?,?,3,null,0,?,?,?,?)");
		psql.setSQL(sql.toString());
		psql.setString(1, taskname);
		psql.setString(2, taskid);
		psql.setLong(3, accoid);
		psql.setLong(4, Long.parseLong(addtime));
		psql.setString(5, url);
		psql.setInt(6, Integer.parseInt(levelreport));
		psql.setString(7, filename);
		psql.setLong(8, devs);
		int num = jt.update(psql.getSQL());
		return num;
	}

	/**
	 * 获取当天定制数量
	 * @param date
	 * @return
	 */
	public int queryDevsByDate() 
	{
		PrepareSQL psql = new PrepareSQL();
		String sql = "select sum(devs) num from tab_ids_task " +
				"where add_time>="+getStartDate()+" and add_time<="+getEndDate();
		psql.setSQL(sql);
		List<Map> list = jt.queryForList(psql.getSQL());
		int count = StringUtil.getIntegerValue(list.get(0).get("num"));
		return count;
	}
	
	public void delTask(String taskid)
	{
		PrepareSQL psql = new PrepareSQL();
		String sql = "delete from tab_ids_task where task_id = ?";
		psql.setSQL(sql.toString());
		psql.setString(1, taskid);
		jt.update(psql.getSQL());
		sql = "delete from tab_ids_task_dev where task_id = ?";
		psql.setSQL(sql.toString());
		psql.setString(1, taskid);
		jt.update(psql.getSQL());
		
	}
	
	/**
	 * 查询任务详细
	 * @param taskid
	 * @return
	 */
	public Map queryTaskInfo(String taskid)
	{
		PrepareSQL psql = new PrepareSQL();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.task_name,b.acc_loginname,a.add_time,a.task_id, "
				+ " a.url from tab_ids_task a,tab_accounts b where a.acc_oid=b.acc_oid and task_id = ? ");
		psql.setSQL(sql.toString());
		psql.setString(1, taskid);
		Map map = jt.queryForMap(psql.getSQL());
		if(map!=null){
			map.put("add_time", DateUtil.transTime(Long.parseLong(map.get("add_time").toString()),"yyyy-MM-dd HH:mm:ss" ));
		}
		return map;
		
	}
	
	/**
	 * 获取设备列表
	 * @param serList
	 * @param filename
	 * @return
	 */
	public List<Map> getTaskDevList(String taskid,int curPage_splitPage, int num_splitPage) 
	{
		PrepareSQL psql = new PrepareSQL();
		//TODO wait
		StringBuffer sql = new StringBuffer();
		sql.append("select a.task_name,a.task_id,e.acc_loginname,a.device_serialnumber," +
				"f.city_name,d.vendor_name,g.device_model,c.hardwareversion,c.softwareversion");
		sql.append(" from tab_ids_task_dev a,tab_gw_device b,tab_devicetype_info c," +
				"tab_vendor d,tab_accounts e,tab_city f,gw_device_model g,tab_ids_task h");
		sql.append(" where a.task_id=h.task_id and a.device_id=b.device_id and b.devicetype_id=c.devicetype_id ");
		sql.append(" and b.vendor_id=d.vendor_id and h.acc_oid=e.acc_oid " +
				"and b.city_id=f.city_id and b.device_model_id=g.device_model_id");
		sql.append(" and h.task_id=?");
		psql.setSQL(sql.toString());
		psql.setString(1, taskid);
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,num_splitPage);
		return list;
	}
	
	/**
	 * 获取设备列表
	 * @param serList
	 * @param filename
	 * @return
	 */
	public int getTaskDevCount(String taskid,int curPage_splitPage, int num_splitPage) 
	{
		PrepareSQL psql = new PrepareSQL();
		//TODO wait
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("select count(*)");
		}else{
			sql.append("select count(1)");
		}
		
		sql.append(" from tab_ids_task_dev a,tab_gw_device b,tab_devicetype_info c,tab_vendor d,tab_accounts e,tab_city f,gw_device_model g,tab_ids_task h");
		sql.append(" where a.task_id=h.task_id and a.device_id=b.device_id and b.devicetype_id=c.devicetype_id ");
		sql.append(" and b.vendor_id=d.vendor_id and h.acc_oid=e.acc_oid and b.city_id=f.city_id and b.device_model_id=g.device_model_id");
		sql.append(" and h.task_id=?");
		psql.setSQL(sql.toString());
		psql.setString(1, taskid);
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	/**
	 * 把设备序列号插入临时表
	 * @param fileName
	 * @param dataList
	 */
	public void insertTmp(String fileName,List<String> dataList,String importQueryField)
	{
		ArrayList<String> sqlList = new ArrayList<String>();
		PrepareSQL psql = null;
		if("username".equals(importQueryField)){
			for (int i = 0;i<dataList.size();i++)
			{
				psql = new PrepareSQL();
				psql.append("insert into tab_seniorquery_tmp" + "(filename,username)"+" values ('" + fileName + "','"  + dataList.get(i) +"')");
				sqlList.add(psql.getSQL());
			}
		} else
		{
			for (int i = 0;i<dataList.size();i++)
			{
				psql = new PrepareSQL();
				psql.append("insert into tab_seniorquery_tmp" + "(filename,devicesn)"+" values ('" + fileName + "','"  + dataList.get(i) +"')");
				sqlList.add(psql.getSQL());
			}
		}
		int res ;
		if(LipossGlobals.inArea(Global.JSDX))
		{
			res = DBOperation.executeUpdate(sqlList,"proxool.xml-test");
		}
		else
		{
			res = DBOperation.executeUpdate(sqlList);
		}
	}
	
	/**
	 * 批量插入设备信息
	 * @param sqlList
	 */
	public void insertTaskDev(ArrayList<String> sqlList){
		int res;
		if (LipossGlobals.inArea(Global.JSDX))
		{
			res = DBOperation.executeUpdate(sqlList, "proxool.xml-test");
		}
		else
		{
			res = DBOperation.executeUpdate(sqlList);
		}
	}
	/**
	 * 
	 * 
	 * @param areaId
	 * @param cityId
	 * @param userList
	 * @return
	 */
	public List queryDeviceByImportDevicesn(List<String> devidesnList,String fileName){
//		logger.debug("queryDeviceByImportDevicesn()");
//		//江苏查报表库
//		setDataSourceType(LipossGlobals.inArea("js_dx")?"false":"true", this.getClass().getName());
		PrepareSQL pSQL = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			pSQL.append("select a.device_id,a.oui,a.device_serialnumber,a.device_name,a.city_id,a.office_id,a.complete_time,");
			pSQL.append("a.zone_id,a.buy_time,a.staff_id,a.remark,a.loopback_ip,a.interface_id,a.device_status,a.gather_id,");
			pSQL.append("a.devicetype_id,a.maxenvelopes,a.cr_port,a.cr_path,a.cpe_mac,a.cpe_currentupdatetime,");
			pSQL.append("a.cpe_allocatedstatus,a.cpe_username,a.cpe_passwd,a.acs_username,a.acs_passwd,a.device_type,");
			pSQL.append("a.x_com_username,a.gw_type,a.device_model_id,a.customer_id,a.device_url,a.x_com_passwd_old,a.vendor_id ");
			pSQL.append("from tab_gw_device a,tab_seniorquery_tmp b ");
			pSQL.append("where a.device_serialnumber=b.devicesn and b.filename=? ");
			pSQL.setString(1, fileName);
			List list=jt.queryForList(pSQL.getSQL());
			if(list!=null && !list.isEmpty()){
				list=resultList(list);
			}
			return list;
		}else{
			pSQL.append("select d.*  from ");
			pSQL.append("( select a.*,v.vendor_add,g.device_model,t.softwareversion,tc.city_name from tab_gw_device a,tab_seniorquery_tmp b,tab_vendor v,gw_device_model g,tab_devicetype_info t,tab_city tc ");
			pSQL.append(" where a.device_serialnumber = b.devicesn and a.vendor_id=v.vendor_id and a.device_model_id=g.device_model_id and a.devicetype_id=t.devicetype_id and a.city_id=tc.city_id and b.filename=?) d ");
			pSQL.append(" left join tab_hgwcustomer c on d.device_id = c.device_id");
			pSQL.setString(1, fileName);
			
			return jt.query(pSQL.getSQL(), new RowMapper() {
				public Object mapRow(ResultSet rs, int arg1) throws SQLException {
					Map<String, String> map = new HashMap<String, String>();
					return resultSet2Map(map, rs);
				}
			});
		}
	}
	
	/**
	 * 获取设备信息
	 * @param serList
	 * @param filename
	 * @param importQueryField 
	 * @return
	 */
	public List<Map> getTaskDevListBySN(String filename) {
		PrepareSQL psql = new PrepareSQL();
		//TODO wait
	    StringBuffer sql = new StringBuffer();
	    sql.append("select f.device_id ,f.oui,f.device_serialnumber , e.username,f.city_id,f.username loid from ( ");
	    sql.append("select c.username,d.device_id,d.oui,d.device_serialnumber,c.city_id,");
	    if(DBUtil.GetDB()==Global.DB_MYSQL){
	    	sql.append("ifnull(c.user_id,0) user_id ");
	    }else{
	    	sql.append("isnull(c.user_id,0) user_id ");
	    }
	    
	    sql.append("from ( select a.device_id,a.oui,a.device_serialnumber  from tab_gw_device a,tab_seniorquery_tmp b ");
	    sql.append(" where a.device_serialnumber = b.devicesn  and b.filename=?) d ");
	    sql.append(" left join tab_hgwcustomer c on d.device_id = c.device_id ) f ");
	    sql.append(" left join hgwcust_serv_info e on (f.user_id = e.user_id  and e.serv_type_id=10) ");
	    psql.setSQL(sql.toString());
	    psql.setString(1, filename);
	    List list = this.jt.queryForList(psql.getSQL());
	    return list;
	}
	
	/**
	 * 获取设备信息
	 * @param serList
	 * @param filename
	 * @param importQueryField 
	 * @return
	 */
	public List<Map> getTaskDevListByLOID(String filename) {
		PrepareSQL psql = new PrepareSQL();
		//TODO wait
	    StringBuffer sql = new StringBuffer();
	    sql.append("select f.device_id,f.oui,f.device_serialnumber,e.username,f.city_id,f.username loid from ( ");
	    sql.append("select d.device_id,d.oui,d.device_serialnumber,c.username,c.city_id,");
	    if(DBUtil.GetDB()==Global.DB_MYSQL){
	    	sql.append("ifnull(c.user_id,0) user_id ");
	    }else{
	    	sql.append("isnull(c.user_id,0) user_id ");
	    }
	    
	    sql.append("from( select a.device_id,a.oui,a.device_serialnumber from tab_gw_device a,tab_seniorquery_tmp b,tab_hgwcustomer h ");
	    sql.append(" where a.device_id = h.device_id and h.username=b.username  and b.filename=?) d ");
	    sql.append(" left join tab_hgwcustomer c on d.device_id = c.device_id");
	    sql.append(" ) f left join hgwcust_serv_info e on (f.user_id = e.user_id  and e.serv_type_id=10) ");
	    psql.setSQL(sql.toString());
	    psql.setString(1, filename);
	    List list = this.jt.queryForList(psql.getSQL());
	    return list;
	}
	
	public List<Map<String, String>> resultList(List<Map> list) 
	{
		List<Map<String, String>> returnList=new ArrayList<Map<String, String>>();
		try{
			Map<String,String> vn=getVendorName();
			Map<String,String> dm=getDeviceModel();
			Map<String,String> dti=getDeviceTypeInfo();
			for(Map m:list)
			{
				//v.vendor_add,g.device_model,t.softwareversion,tc.city_name
				//device_model_id,devicetype_id,city_id,vendor_id
				String city_id=StringUtil.getStringValue(m,"city_id");
				String city_name=CityDAO.getCityName(city_id);
				String vendor_id=StringUtil.getStringValue(m,"vendor_id");
				String vendor_add=vn.get(vendor_id);
				String device_model_id=StringUtil.getStringValue(m,"device_model_id");
				String device_model=dm.get(device_model_id);
				String devicetype_id=StringUtil.getStringValue(m,"devicetype_id");
				String softwareversion=dti.get(devicetype_id);
				
				if(StringUtil.IsEmpty(city_name) 
						|| StringUtil.IsEmpty(vendor_add) 
						|| StringUtil.IsEmpty(device_model) 
						|| StringUtil.IsEmpty(softwareversion)){
					continue;
				}
				
				Map<String, String> map=new HashMap<String, String>();
				map.put("device_model_id", device_model_id);
				map.put("device_model", device_model);
				map.put("vendor_id", vendor_id);
				map.put("vendor_add", vendor_add);
				map.put("devicetype_id", devicetype_id);
				map.put("softwareversion", softwareversion);
				map.put("city_id", city_id);
				map.put("city_name", city_name);
				
				map.put("device_id", StringUtil.getStringValue(m,"device_id"));
				map.put("oui", StringUtil.getStringValue(m,"oui"));
				map.put("device_serialnumber", StringUtil.getStringValue(m,"device_serialnumber"));
				map.put("device_name", StringUtil.getStringValue(m,"device_name"));
				map.put("office_id", StringUtil.getStringValue(m,"office_id"));
				map.put("complete_time", new DateTimeUtil(StringUtil.getLongValue(m,"complete_time")*1000).getYYYY_MM_DD_HH_mm_ss());
				map.put("zone_id", StringUtil.getStringValue(m,"zone_id"));
				map.put("buy_time", new DateTimeUtil(StringUtil.getLongValue(m,"buy_time")*1000).getYYYY_MM_DD_HH_mm_ss());
				map.put("staff_id", StringUtil.getStringValue(m,"staff_id"));
				map.put("remark", StringUtil.getStringValue(m,"remark"));
				map.put("loopback_ip", StringUtil.getStringValue(m,"loopback_ip"));
				map.put("interface_id", StringUtil.getStringValue(m,"interface_id"));
				map.put("device_status", StringUtil.getStringValue(m,"device_status"));
				map.put("gather_id", StringUtil.getStringValue(m,"gather_id"));
				map.put("maxenvelopes", StringUtil.getStringValue(m,"maxenvelopes"));
				map.put("cr_port",StringUtil.getStringValue(m,"cr_port"));
				map.put("cr_path", StringUtil.getStringValue(m,"cr_path"));
				map.put("cpe_mac", StringUtil.getStringValue(m,"cpe_mac"));
				map.put("cpe_currentupdatetime", new DateTimeUtil(StringUtil.getLongValue(m,"cpe_currentupdatetime")*1000).getYYYY_MM_DD_HH_mm_ss());
				map.put("cpe_allocatedstatus", StringUtil.getStringValue(m,"cpe_allocatedstatus"));
				map.put("cpe_username", StringUtil.getStringValue(m,"cpe_username"));
				map.put("cpe_passwd", StringUtil.getStringValue(m,"cpe_passwd"));
				map.put("acs_username", StringUtil.getStringValue(m,"acs_username"));
				map.put("acs_passwd", StringUtil.getStringValue(m,"acs_passwd"));
				map.put("device_type", StringUtil.getStringValue(m,"device_type"));
				map.put("x_com_username", StringUtil.getStringValue(m,"x_com_username"));
				map.put("x_com_passwd",StringUtil.getStringValue(m,"x_com_passwd"));
				map.put("gw_type", StringUtil.getStringValue(m,"gw_type"));
				map.put("customer_id", StringUtil.getStringValue(m,"customer_id"));
				map.put("device_url",StringUtil.getStringValue(m,"device_url"));
				map.put("x_com_passwd_old", StringUtil.getStringValue(m,"x_com_passwd_old"));
				
				returnList.add(map);
				map=null;
			}
		}catch(Exception e){
			logger.error(e.getMessage());
			return returnList;
		}
		return returnList;
	}
	
	/**
	 * 数据转换
	 */
	public Map<String, String> resultSet2Map(Map<String, String> map,ResultSet rs) {
		try{
			map.put("device_id", rs.getString("device_id"));
			map.put("oui", rs.getString("oui"));
			map.put("device_serialnumber", rs.getString("device_serialnumber"));
			map.put("device_name", rs.getString("device_name"));
			map.put("city_id", rs.getString("city_id"));
			map.put("city_name", CityDAO.getCityIdCityNameMap().get(rs.getString("city_id")));
			map.put("office_id", rs.getString("office_id"));
			map.put("complete_time", new DateTimeUtil(rs.getLong("complete_time")*1000).getYYYY_MM_DD_HH_mm_ss());
			map.put("zone_id", rs.getString("zone_id"));
			map.put("buy_time", new DateTimeUtil(rs.getLong("buy_time")*1000).getYYYY_MM_DD_HH_mm_ss());
			map.put("staff_id", rs.getString("staff_id"));
			map.put("remark", rs.getString("remark"));
			map.put("loopback_ip", rs.getString("loopback_ip"));
			map.put("interface_id", rs.getString("interface_id"));
			map.put("device_status", rs.getString("device_status"));
			map.put("gather_id", rs.getString("gather_id"));
			map.put("devicetype_id", rs.getString("devicetype_id"));
			map.put("softwareversion", rs.getString("softwareversion"));
			map.put("maxenvelopes", rs.getString("maxenvelopes"));
			map.put("cr_port", rs.getString("cr_port"));
			map.put("cr_path", rs.getString("cr_path"));
			map.put("cpe_mac", rs.getString("cpe_mac"));
			map.put("cpe_currentupdatetime", new DateTimeUtil(rs.getLong("cpe_currentupdatetime")*1000).getYYYY_MM_DD_HH_mm_ss());
			map.put("cpe_allocatedstatus", rs.getString("cpe_allocatedstatus"));
			map.put("cpe_username", rs.getString("cpe_username"));
			map.put("cpe_passwd", rs.getString("cpe_passwd"));
			map.put("acs_username", rs.getString("acs_username"));
			map.put("acs_passwd", rs.getString("acs_passwd"));
			map.put("device_type", rs.getString("device_type"));
			map.put("x_com_username", rs.getString("x_com_username"));
			map.put("x_com_passwd", rs.getString("x_com_passwd"));
			map.put("gw_type", rs.getString("gw_type"));
			map.put("device_model_id", rs.getString("device_model_id"));
			map.put("device_model", rs.getString("device_model"));
			map.put("customer_id", rs.getString("customer_id"));
			map.put("device_url", rs.getString("device_url"));
			map.put("x_com_passwd_old", rs.getString("x_com_passwd_old"));
			map.put("vendor_id", rs.getString("vendor_id"));
			map.put("vendor_add", rs.getString("vendor_add"));
		}catch(SQLException e){
			logger.error(e.getMessage());
		}
		return map;
	}
	
	public int getTaskDevCount(String filename) 
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL)
		{
			StringBuffer sql = new StringBuffer();
			sql.append("select d.vendor_id,d.device_model_id,d.devicetype_id,d.city_id from ");
			sql.append("(select a.device_id,a.vendor_id,a.device_model_id,a.devicetype_id,a.city_id ");
			sql.append("from tab_gw_device a,tab_temporary_device b ");
			sql.append("where a.device_serialnumber=b.device_serialnumber and b.filename=?) d ");
			sql.append(" left join tab_hgwcustomer c on d.device_id=c.device_id");
			psql.setSQL(sql.toString());
			psql.setString(1, filename);
			
			int total=0;
			List<Map> list=jt.queryForList(psql.getSQL());
			if(list!=null && !list.isEmpty())
			{
				total=list.size();
				
				Map<String,String> vn=getVendorName();
				Map<String,String> dm=getDeviceModel();
				Map<String,String> dti=getDeviceTypeInfo();
				for(Map m:list)
				{
					String city_name=CityDAO.getCityName(StringUtil.getStringValue(m,"city_id"));
					String vendor_name=vn.get(StringUtil.getStringValue(m,"vendor_id"));
					String device_model=dm.get(StringUtil.getStringValue(m,"device_model_id"));
					String softwareversion=dti.get(StringUtil.getStringValue(m,"devicetype_id"));
					if(StringUtil.IsEmpty(city_name) 
							|| StringUtil.IsEmpty(vendor_name) 
							|| StringUtil.IsEmpty(device_model) 
							|| StringUtil.IsEmpty(softwareversion)){
						total--;
					}
				}
			}
				
			return total;
		}
		else
		{
			StringBuffer sql = new StringBuffer();
			sql.append("select count(1) from ");
			sql.append("( select a.device_id,a.oui,a.device_serialnumber,a.loopback_ip,v.vendor_name,g.device_model,t.softwareversion,tc.city_name from tab_gw_device a,tab_temporary_device b,tab_vendor v,gw_device_model g,tab_devicetype_info t,tab_city tc ");
			sql.append(" where a.device_serialnumber = b.device_serialnumber and a.vendor_id=v.vendor_id and a.device_model_id=g.device_model_id and a.devicetype_id=t.devicetype_id and a.city_id=tc.city_id and b.filename=?) d ");
			sql.append(" left join tab_hgwcustomer c on d.device_id = c.device_id");
			psql.setSQL(sql.toString());
			psql.setString(1, filename);
			
			return jt.queryForInt(psql.getSQL());
		}
	}
	
	/**
	 * 任务查询
	 */
	public List<Map> queryTask(String name,String acc_loginname,String starttime,String endtime,int curPage_splitPage, int num_splitPage)
	{
		PrepareSQL psql = new PrepareSQL();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.task_name,b.acc_loginname,a.add_time,a.task_id,a.url,a.level_report,a.file_name from tab_ids_task a,tab_accounts b where a.acc_oid=b.acc_oid and a.type_id=3 ");
		if(!StringUtil.IsEmpty(name)){
			sql.append(" and a.task_name like '%"+name+"%'");
		}
		
		if(!StringUtil.IsEmpty(acc_loginname)){
			sql.append(" and b.acc_loginname like '%"+acc_loginname+"%'");
		}
		
		if(!StringUtil.IsEmpty(starttime)){
			sql.append(" and a.add_time >").append(starttime);
		}
		
		if(!StringUtil.IsEmpty(endtime)){
			sql.append(" and a.add_time <").append(endtime);
		}
		sql.append(" order by a.add_time desc");
		psql.setSQL(sql.toString());
		
		List<Map> list =querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,num_splitPage);
		
		if(null!=list && list.size()>0){
			for (int i = 0; i < list.size(); i++)
			{
					String addtime = String.valueOf(list.get(i).get("add_time"));
					list.get(i).put("add_time", DateUtil.transTime(Long.parseLong(addtime),"yyyy-MM-dd HH:mm:ss"));
			}
		}
		
		return list;
	}
	
	/**
	 * 任务查询(分页)
	 */
	public int queryTaskCount(String name,String acc_loginname,String starttime,String endtime,int curPage_splitPage, int num_splitPage)
	{
		PrepareSQL psql = new PrepareSQL();
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		sql.append("from tab_ids_task a,tab_accounts b where a.acc_oid=b.acc_oid and a.type_id=3 ");
		if(!StringUtil.IsEmpty(name)){
			sql.append(" and a.task_name like '%"+name+"%'");
		}
		
		if(!StringUtil.IsEmpty(acc_loginname)){
			sql.append(" and b.acc_loginname like'%"+acc_loginname+"%'");
		}
		
		if(!StringUtil.IsEmpty(starttime)){
			sql.append(" and a.add_time >").append(starttime);
		}
		
		if(!StringUtil.IsEmpty(endtime)){
			sql.append(" and a.add_time <").append(endtime);
		}
		psql.setSQL(sql.toString());
		
		int total = jt.queryForInt(psql.getSQL());
		
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	
	public int checkTaskName(String taskname)
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select count(*) from tab_ids_task where task_name='"+taskname+"'");
		}else{
			psql.append("select count(1) from tab_ids_task where task_name='"+taskname+"'");
		}
		return jt.queryForInt(psql.getSQL());
	}
	
	public int checkFileName(String filename)
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select count(*) from tab_ids_task where file_name='"+filename+"'");
		}else{
			psql.append("select count(1) from tab_ids_task where file_name='"+filename+"'");
		}
		return jt.queryForInt(psql.getSQL());
	}
	
	
	// 当前时间的23:59:59,如 2011-05-11 23:59:59
	private Long getEndDate() {
		DateTimeUtil dt = null;
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.US);
		now.set(Calendar.HOUR_OF_DAY, 23);
		now.set(Calendar.MINUTE, 59);
		now.set(Calendar.SECOND, 59);
		String time = fmtrq.format(now.getTime());
		dt = new DateTimeUtil(time);
		return dt.getLongTime();
	}

	// 当前时间的23:59:59,如 2011-05-11 00:00:00
	private Long getStartDate() {
		DateTimeUtil dt = null;
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.US);
		now.set(Calendar.HOUR_OF_DAY, 00);
		now.set(Calendar.MINUTE, 00);
		now.set(Calendar.SECOND, 00);
		String time = fmtrq.format(now.getTime());
		dt = new DateTimeUtil(time);
		return dt.getLongTime();
	}
	/**
	 * 任务导出
	 */
	public List<Map> queryTaskExcel(String name,String acc_loginname,String starttime,String endtime){
		PrepareSQL psql = new PrepareSQL();
		StringBuffer sql = new StringBuffer();
		sql.append("select  a.task_name,b.acc_loginname,a.add_time,a.task_id,a.url,a.level_report,a.file_name from tab_ids_task a,tab_accounts b where a.acc_oid=b.acc_oid and a.type_id=3 ");
		if(!StringUtil.IsEmpty(name)){
			sql.append(" and a.task_name like '%"+name+"%'");
		}
		
		if(!StringUtil.IsEmpty(acc_loginname)){
			sql.append(" and b.acc_loginname like '%"+acc_loginname+"%'");
		}
		
		if(!StringUtil.IsEmpty(starttime)){
			sql.append(" and a.add_time >").append(starttime);
		}
		
		if(!StringUtil.IsEmpty(endtime)){
			sql.append(" and a.add_time <").append(endtime);
		}
		sql.append(" order by a.add_time desc");
		psql.setSQL(sql.toString());
		
		List<Map> list =jt.queryForList(psql.getSQL());
		
		if(null!=list && list.size()>0){
			for (int i = 0; i < list.size(); i++)
			{
					String addtime = String.valueOf(list.get(i).get("add_time"));
					list.get(i).put("add_time", DateUtil.transTime(Long.parseLong(addtime),"yyyy-MM-dd HH:mm:ss"));
			}
		}
		
		
		return list;
	}

	public List queryDeviceByImportUsername(List<String> dataList, String fileName) 
	{
		PrepareSQL pSQL = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			pSQL.append("select d.device_id,d.oui,d.device_serialnumber,d.device_name,d.city_id,d.office_id,d.complete_time,d.zone_id,");
			pSQL.append("d.buy_time,d.staff_id,d.remark,d.loopback_ip,d.interface_id,d.device_status,d.gather_id,d.devicetype_id,");
			pSQL.append("d.maxenvelopes,d.cr_port,d.cr_path,d.cpe_mac,d.cpe_currentupdatetime,d.cpe_allocatedstatus,");
			pSQL.append("d.cpe_username,d.cpe_passwd,d.acs_username,d.acs_passwd,d.device_type,d.x_com_username,d.x_com_passwd,");
			pSQL.append("d.gw_type,d.device_model_id,d.customer_id,d.device_url,d.x_com_passwd_old,d.vendor_id ");
			pSQL.append("from tab_gw_device a,");
			pSQL.append("(select h.device_id from tab_seniorquery_tmp b,tab_hgwcustomer h ");
			pSQL.append("where b.username=h.username and b.filename=?) d ");
			pSQL.append("where a.device_id=d.device_id ");
			pSQL.setString(1, fileName);
			
			List list=jt.queryForList(pSQL.getSQL());
			if(list!=null && !list.isEmpty()){
				list=resultList(list);
			}
			return list;
		}else{
			pSQL.append("select d.*  from ");
			pSQL.append("( select a.*,v.vendor_add,g.device_model,t.softwareversion,tc.city_name from tab_gw_device a,tab_seniorquery_tmp b,tab_vendor v,gw_device_model g,tab_devicetype_info t,tab_city tc,tab_hgwcustomer h ");
			pSQL.append(" where a.device_id=h.device_id and h.username = b.username and a.vendor_id=v.vendor_id and a.device_model_id=g.device_model_id and a.devicetype_id=t.devicetype_id and a.city_id=tc.city_id and b.filename=?) d ");
			pSQL.append(" left join tab_hgwcustomer c on d.device_id = c.device_id");
			pSQL.setString(1, fileName);
			
			return jt.query(pSQL.toString(), new RowMapper() {
				public Object mapRow(ResultSet rs, int arg1) throws SQLException {
					Map<String, String> map = new HashMap<String, String>();
					return resultSet2Map(map, rs);
				}
			});
		}
	}
	
	/**
	 * 获取所有厂商
	 */
	private Map<String,String> getVendorName()
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select vendor_id,vendor_add from tab_vendor order by vendor_id ");
		List<Map> list=jt.queryForList(psql.getSQL());
		Map<String,String> map=new HashMap<String,String>();
		if(list!=null && !list.isEmpty()){
			for(Map m:list){
				map.put(StringUtil.getStringValue(m,"vendor_id"),
						StringUtil.getStringValue(m,"vendor_add"));
			}
		}
		
		return map;
	}
	
	/**
	 * 获取所有型号
	 */
	private Map<String,String> getDeviceModel()
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select device_model_id,device_model from gw_device_model order by device_model_id ");
		List<Map> list=jt.queryForList(psql.getSQL());
		Map<String,String> map=new HashMap<String,String>();
		if(list!=null && !list.isEmpty()){
			for(Map m:list){
				map.put(StringUtil.getStringValue(m,"device_model_id"),
						StringUtil.getStringValue(m,"device_model"));
			}
		}
		
		return map;
	}
	
	/**
	 * 获取所有版本
	 */
	private Map<String,String> getDeviceTypeInfo()
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select devicetype_id,softwareversion from tab_devicetype_info order by devicetype_id ");
		List<Map> list=jt.queryForList(psql.getSQL());
		Map<String,String> map=new HashMap<String,String>();
		if(list!=null && !list.isEmpty()){
			for(Map m:list){
				map.put(StringUtil.getStringValue(m,"devicetype_id"),
						StringUtil.getStringValue(m,"softwareversion"));
			}
		}
		
		return map;
	}
}
