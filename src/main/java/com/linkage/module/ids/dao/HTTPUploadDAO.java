package com.linkage.module.ids.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.db.DBUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DBOperation;
import com.linkage.litms.uss.DateUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
/**
 * @author os_hanzz (Ailk No.)
 * @version 1.0
 * @since 2015/4/16
 * @category com.linkage.module.itms.report.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class HTTPUploadDAO extends SuperDAO {

	private static Logger logger = LoggerFactory.getLogger(HTTPDeviceQueryDAO.class);

	/**
	 * 增加任务信息
	 * @param name
	 * @param taskid
	 * @param accoid
	 * @param addtime
	 * @param starttime
	 * @param endtime
	 * @param enddate
	 * @return
	 */
	public int addTask4NX(String name,String taskid,long accoid,String addtime,String starttime,String endtime,String enddate,
			String url,String testUserName, String testPWD, String param, int type){
		
		PrepareSQL psql = new PrepareSQL();
		StringBuffer sql = new StringBuffer();
		sql.append("insert into tab_ids_task (task_name,task_id,acc_oid,add_time,type_id,start_time,end_time,end_date,url,task_statu,run_time,test_username,test_pwd, param,type)");
		sql.append(" values(?,?,?,?,7,?,?,?,?,0,null,?,?,?,?)");//type_id 区分业务 7代表上行测速
		psql.setSQL(sql.toString());
		psql.setString(1, name);
		psql.setString(2, taskid);
		psql.setLong(3, accoid);
		psql.setLong(4, Long.parseLong(addtime));
		psql.setLong(5, Long.parseLong(starttime));
		psql.setLong(6, Long.parseLong(endtime));
		psql.setLong(7, Long.parseLong(enddate));
		psql.setString(8, url);
//		psql.setString(9, httpType);
		psql.setString(9, testUserName);
		psql.setString(10, testPWD);
		psql.setString(11, param);
		psql.setInt(12, type);
		int num = jt.update(psql.getSQL());
		return num;
	}
	
	/**
	 * 查询设备信息
	 * @param sqlSpell
	 */
	public  ArrayList<HashMap<String, String>> getDevIds4NX(String sqlSpell){
		return DBOperation.getRecords(sqlSpell);
	}
	
	/**
	 * 把设备序列号插入临时表
	 * @param fileName
	 * @param dataList
	 */
	public void insertTmp(String fileName, List<String> dataList) {
		ArrayList<String> sqlList = new ArrayList<String>();
		PrepareSQL psql = null;
		for (int i = 0; i < dataList.size(); i++) {
			psql = new PrepareSQL();
			psql.append("insert into tab_temporary_device"
					+ "(filename,device_serialnumber)" + " values ('" + fileName + "','"
					+ dataList.get(i) + "')");
			sqlList.add(psql.getSQL());
		}
		int res = 0;
		if (LipossGlobals.inArea(Global.JSDX)) {
			res = DBOperation.executeUpdate(sqlList, "proxool.xml-report");
		} else {
			ArrayList<String> sqlListTmp = new ArrayList<String>();
			if(sqlList.size()>0){
				for(String sql : sqlList){
					sqlListTmp.add(sql);
					if(sqlListTmp.size()>=200){
						int resTmp = DBOperation.executeUpdate(sqlListTmp);
						if(resTmp>0){
							res += sqlListTmp.size();
						}
						sqlListTmp.clear();
					}
				}
			}
			if(sqlListTmp.size()>0){
				int resTmp = DBOperation.executeUpdate(sqlListTmp);
				if(resTmp>0){
					res += sqlListTmp.size();
				}
				sqlListTmp.clear();
			}
			logger.info("====成功插入tab_temporary_device表"+res+"条数据====");
		}
	}
	
	
	/**
	 * 获取设备信息
	 * @param serList
	 * @param filename
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getTaskDevList4NX(String filename) {

		PrepareSQL psql = new PrepareSQL();
		//TODO wait
		psql.append(" select a.device_serialnumber a_device_id, b.device_id, b.oui, b.device_serialnumber, d.wan_type, ");
		psql.append(" d.username serusername,d.passwd, c.username from tab_temporary_device a left join tab_gw_device b on ");
		psql.append(" a.device_serialnumber=b.device_id left join tab_hgwcustomer c on b.device_id=c.device_id ");
		psql.append(" left join hgwcust_serv_info d on (c.user_id=d.user_id and d.serv_type_id=10 and d.serv_status=1 and d.wan_type=2) ");
		psql.append("  where a.filename=? order by d.opendate desc ");
		
		psql.setString(1, filename);
		
		ArrayList<HashMap<String, String>> list = DBOperation.getRecords(psql.getSQL());
		if(null==list || list.size()==0){
			return null;
		}
		
		ArrayList<HashMap<String, String>> listNew = new ArrayList<HashMap<String, String>>();
		List<String> listTmp = new ArrayList<String>();
		
		for(HashMap<String, String> map : list){
			String serUsername = map.get("serusername");
			if(!StringUtil.IsEmpty(serUsername)){
				if(listTmp.contains(serUsername)){
					continue;
				}else{
					listTmp.add(serUsername);
					listNew.add(map);
				}
			}else{
				logger.warn("=======[{}]-[{}]没有查到serusername=======",filename, map.get("a_device_id"));
			}
		}
		
		return listNew;
	}
	
	
	public ArrayList<String> sqlList(List<HashMap<String, String>> devList,String name,String taskid){
		ArrayList<String> sqlList = new ArrayList<String>();
		for (int i = 0; i < devList.size(); i++) {
			String oui = String.valueOf(devList.get(i).get("oui"));
			String username = String.valueOf(devList.get(i).get("username"));
			String device_serialnumber = String.valueOf(devList.get(i).get("device_serialnumber"));
			String device_id = String.valueOf(devList.get(i).get("device_id"));
			String testUsername = String.valueOf(devList.get(i).get("serusername"));
			String testPwd = String.valueOf(devList.get(i).get("passwd"));
			PrepareSQL pSql = new PrepareSQL();
			if (LipossGlobals.inArea(Global.NXDX))
			{
				pSql.append("insert into tab_ids_task_dev(task_name,task_id,device_id,oui,device_serialnumber,loid,statu,test_username,test_pwd)");
				pSql.append(" values('"+name+"','"+taskid+"','"+device_id+"','"+oui+"','"+device_serialnumber+"','"+username+"',0,'" + testUsername + "','" + testPwd + "')");
			}
			else
			{
				pSql.append("insert into tab_ids_task_dev(task_name,task_id,device_id,oui,device_serialnumber,loid,statu)");
				pSql.append(" values('"+name+"','"+taskid+"','"+device_id+"','"+oui+"','"+device_serialnumber+"','"+username+"',0)");
			}
			sqlList.add(pSql.getSQL());
		}
		return sqlList;
	}

	/**
	 * 批量插入设备信息
	 * @param sqlList
	 */
	public int insertTaskDev4NX(ArrayList<String> sqlList){
		return DBOperation.executeUpdate(sqlList);
	}
	
	/**
	 * 任务查询
	 * @param name
	 * @param accoid
	 * @param starttime
	 * @param endtime
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> queryTask(String name,String acc_loginname,String starttime,String endtime,int curPage_splitPage, int num_splitPage){
		PrepareSQL psql = new PrepareSQL();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.task_name,b.acc_loginname,a.add_time,a.task_id from tab_ids_task a,tab_accounts b where a.acc_oid=b.acc_oid and a.type_id=7 ");
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
	 * 任务导出
	 * @param name
	 * @param accoid
	 * @param starttime
	 * @param endtime
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> queryTaskExcel(String name,String acc_loginname,String starttime,String endtime){
		PrepareSQL psql = new PrepareSQL();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.task_name,b.acc_loginname,a.add_time,a.task_id from tab_ids_task a,tab_accounts b where a.acc_oid=b.acc_oid and a.type_id=7 ");
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
	
	
	/**
	 * 获取设备列表
	 * @param serList
	 * @param filename
	 * @return
	 */
	public List<Map> getTaskDevList(String taskid,int curPage_splitPage, int num_splitPage) {

		PrepareSQL psql = new PrepareSQL();
		//TODO wait
		StringBuffer sql = new StringBuffer();
		sql.append("select a.task_name,a.task_id,e.acc_loginname,a.device_serialnumber,f.city_name,d.vendor_name,g.device_model,c.hardwareversion,c.softwareversion");
		sql.append(" from tab_ids_task_dev a,tab_gw_device b,tab_devicetype_info c,tab_vendor d,tab_accounts e,tab_city f,gw_device_model g,tab_ids_task h");
		sql.append(" where a.task_id=h.task_id and a.device_id=b.device_id and b.devicetype_id=c.devicetype_id ");
		sql.append(" and b.vendor_id=d.vendor_id and h.acc_oid=e.acc_oid and b.city_id=f.city_id and b.device_model_id=g.device_model_id");
		sql.append(" and h.task_id=?");
		psql.setSQL(sql.toString());
		psql.setString(1, taskid);
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,num_splitPage);
		return list;
	}
	
	/**
	 * 任务查询(分页)
	 * @param name
	 * @param accoid
	 * @param starttime
	 * @param endtime
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int queryTaskCount(String name,String acc_loginname,String starttime,String endtime,int curPage_splitPage, int num_splitPage){
		PrepareSQL psql = new PrepareSQL();
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) from tab_ids_task a,tab_accounts b where a.acc_oid=b.acc_oid and a.type_id=7 ");
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
	 * 获取设备列表
	 * @param serList
	 * @param filename
	 * @return
	 */
	public int getTaskDevCount(String taskid,int curPage_splitPage, int num_splitPage) {

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
	 * 获取设备信息
	 * @param serList
	 * @param filename
	 * @return
	 */
	public List<Map> getTaskDevList(String filename) {

		PrepareSQL psql = new PrepareSQL();
		StringBuffer sql = new StringBuffer();
		sql.append("select d.device_id ,d.oui,d.device_serialnumber , c.username from ");
		sql.append("( select a.device_id,a.oui,a.device_serialnumber from tab_gw_device a,tab_temporary_device b ");
		sql.append(" where a.device_serialnumber = b.device_serialnumber  and b.filename=?) d ");
		sql.append(" left join tab_hgwcustomer c on d.device_id = c.device_id");
		psql.setSQL(sql.toString());
		psql.setString(1, filename);
		List list = jt.queryForList(psql.getSQL());
		return list;
	}
	
	

	/**
	 * 查询任务详细
	 * @param taskid
	 * @return
	 */
	public Map queryTaskInfo(String taskid){
		
		PrepareSQL psql = new PrepareSQL();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.task_name,b.acc_loginname,a.add_time,a.task_id, "
				+ " a.start_time,a.end_time,a.end_date,a.url,a.test_username from tab_ids_task a,tab_accounts b where a.acc_oid=b.acc_oid and task_id = ? ");
		psql.setSQL(sql.toString());
		psql.setString(1, taskid);
		Map map = jt.queryForMap(psql.getSQL());
		if(map!=null){
			map.put("add_time", DateUtil.transTime(Long.parseLong(map.get("add_time").toString()),"yyyy-MM-dd HH:mm:ss" ));
			//开始时间
			int startHour = Integer.valueOf(map.get("start_time").toString())/3600;
			int startMin =  (Integer.valueOf(map.get("start_time").toString()) - Integer.valueOf(map.get("start_time").toString())/3600*3600)/60;
			String startTime="";
			String startHourString =startHour+"";
			String startMinString =startMin+"";
			if(startHour<10)
			{
				startHourString = "0"+startHour;
			}
			if(startMin<10)
			{
				startMinString = "0"+startMin;
			}
			startTime = startHourString+":"+startMinString;
			map.put("start_time", startTime );
			//结束时间
			int startHourEnd = Integer.valueOf(map.get("end_time").toString())/3600;
			int startMinEnd =  (Integer.valueOf(map.get("end_time").toString()) - Integer.valueOf(map.get("end_time").toString())/3600*3600)/60;
			String endTime = "";
			String endHourString =startHourEnd+"";
			String endMinString =startMinEnd+"";
			if(startHourEnd<10)
			{
				endHourString = "0"+startHourEnd;
			}
			if(startMinEnd<10)
			{
				endMinString = "0"+startMinEnd;
			}
			endTime = endHourString+":"+endMinString;
			map.put("end_time", endTime );
			//截止时间
			long endDate = Long.valueOf(map.get("end_date").toString())*1000;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String endDateString = sdf.format(endDate);
			map.put("end_date", endDateString );
		}
		return map;
		
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
			logger.warn("insertTaskDev()");
			res = DBOperation.executeUpdate(sqlList);
		}
	}
	

	public void delTask(String taskid){
		
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
	
	public List<Map> queryDevBySerialnumber(String taskname,String serialnumber,
			String starttime,String endtime,int curPage_splitPage, int num_splitPage)
	{
		PrepareSQL psql = new PrepareSQL();
		//TODO wait
		psql.append("select e.city_name,c.vendor_name,d.device_model,a.tcpopenrequesttime," +
				"a.tcpopenresponsetime,a.totalbytessent,a.totalbytesreceived,a.bom_time," +
				"a.eom_time,a.device_serialnumber,a.upload_pert,a.wan_type,f.task_name,f.task_id " +
				"from tab_http_diag_result_upload a,tab_gw_device b,tab_vendor c," +
				"gw_device_model d,tab_city e,tab_ids_task f ");
		psql.append(" where a.device_serialnumber=b.device_serialnumber and b.vendor_id=c.vendor_id " +
				"and b.device_model_id=d.device_model_id and b.city_id=e.city_id " +
				"and a.task_id=f.task_id and a.http_result is not null ");
		if(!StringUtil.IsEmpty(serialnumber)){
			psql.append(" and a.device_serialnumber='"+serialnumber+"'");
		}
		
		if(!StringUtil.IsEmpty(starttime)){
			psql.append(" and a.test_time>"+starttime);
		}
		
		if(!StringUtil.IsEmpty(endtime)){
			psql.append(" and a.test_time<"+endtime);
		}
		
		if(!StringUtil.IsEmpty(taskname))
		{
			psql.append(" and f.task_name like '%"+taskname+"%'");
		}
		psql.setString(1, serialnumber);
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,num_splitPage);
		
		return list;
		
	}
	
	public List<Map> queryDevBySerialnumberExcel(String taskname,String serialnumber,String starttime,String endtime)
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL)
		{
			psql.append("select t.task_name,t.tcpopenrequesttime,t.tcpopenresponsetime,t.totalbytessent,");
			psql.append("t.totalbytesreceived,t.bom_time,t.eom_time,t.device_serialnumber,t.upload_pert,");
			psql.append("b.vendor_id,b.device_model_id,b.city_id ");
			psql.append("from (select f.task_name,a.tcpopenrequesttime,a.tcpopenresponsetime,a.totalbytessent,");
			psql.append("a.totalbytesreceived,a.bom_time,a.eom_time,a.device_serialnumber,a.upload_pert ");
			psql.append("from tab_http_diag_result_upload a,tab_ids_task f ");
			psql.append("where a.task_id=f.task_id ");
			psql.append(" and a.device_serialnumber='"+serialnumber+"'");
			if(!StringUtil.IsEmpty(starttime)){
				psql.append(" and a.test_time>"+starttime);
			}
			if(!StringUtil.IsEmpty(endtime)){
				psql.append(" and a.test_time<"+endtime);
			}
			if(!StringUtil.IsEmpty(taskname)){
				psql.append(" and f.task_name like '%"+taskname+"%'");
			}
			psql.append(") t,tab_gw_device b ");
			psql.append("where t.device_serialnumber=b.device_serialnumber ");
			
			List<Map> list = jt.queryForList(psql.getSQL());
			List<Map> returnList=new ArrayList<Map>();
			if(list!=null && !list.isEmpty()){
				Map<String,String> vn=getVendorName();
				Map<String,String> dm=getDeviceModel();
				for(Map m:list)
				{
					String city_name=CityDAO.getCityName(StringUtil.getStringValue(m,"city_id"));
					String vendor_name=vn.get(StringUtil.getStringValue(m,"vendor_id"));
					String device_model=dm.get(StringUtil.getStringValue(m,"device_model_id"));
					if(StringUtil.IsEmpty(city_name) 
							|| StringUtil.IsEmpty(vendor_name) 
							|| StringUtil.IsEmpty(device_model)){
						continue;
					}
					
					Map map=new HashMap();
					map.put("city_name",city_name);
					map.put("vendor_name",vendor_name);
					map.put("device_model",device_model);
					map.put("task_name",StringUtil.getStringValue(m,"task_name"));
					map.put("tcpopenrequesttime",StringUtil.getStringValue(m,"tcpopenrequesttime"));
					map.put("tcpopenresponsetime",StringUtil.getStringValue(m,"tcpopenresponsetime"));
					map.put("totalbytessent",StringUtil.getStringValue(m,"totalbytessent"));
					map.put("totalbytesreceived",StringUtil.getStringValue(m,"totalbytesreceived"));
					map.put("bom_time",StringUtil.getStringValue(m,"bom_time"));
					map.put("eom_time",StringUtil.getStringValue(m,"eom_time"));
					map.put("device_serialnumber",StringUtil.getStringValue(m,"device_serialnumber"));
					map.put("upload_pert",StringUtil.getStringValue(m,"upload_pert"));
					
					returnList.add(map);
					map=null;
					m=null;
				}
			}
			list.clear();
			list=null;
			return returnList;
		}
		else
		{
			psql.append("select f.task_name,e.city_name,c.vendor_name,d.device_model,a.tcpopenrequesttime,a.tcpopenresponsetime,a.totalbytessent,a.totalbytesreceived,a.bom_time,a.eom_time,a.device_serialnumber,a.upload_pert from tab_http_diag_result_upload a,tab_gw_device b,tab_vendor c,gw_device_model d,tab_city e ");
			psql.append(" ,tab_ids_task f where a.task_id=f.task_id and a.device_serialnumber=b.device_serialnumber and b.vendor_id=c.vendor_id and b.device_model_id=d.device_model_id and b.city_id=e.city_id");
			psql.append(" and a.device_serialnumber='"+serialnumber+"'");
			
			if(!StringUtil.IsEmpty(starttime)){
				psql.append(" and a.test_time>"+starttime);
			}
			
			if(!StringUtil.IsEmpty(endtime)){
				psql.append(" and a.test_time<"+endtime);
			}
			
			if(!StringUtil.IsEmpty(taskname)){
				psql.append(" and f.task_name like '%"+taskname+"%'");
			}
			psql.setString(1, serialnumber);
			
			return jt.queryForList(psql.getSQL());
		}
	}
	
	public List<Map> getTaskDevResToExcel(String taskId)
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select t.task_name,t.tcpopenrequesttime,t.tcpopenresponsetime,t.totalbytessent,");
			psql.append("t.totalbytesreceived,t.bom_time,t.eom_time,t.device_serialnumber,t.upload_pert,");
			psql.append("b.vendor_id,b.device_model_id,b.city_id ");
			psql.append("from (select f.task_name,a.tcpopenrequesttime,a.tcpopenresponsetime,a.totalbytessent,");
			psql.append("a.totalbytesreceived,a.bom_time,a.eom_time,a.device_serialnumber,a.upload_pert ");
			psql.append("from tab_http_diag_result_upload a,tab_ids_task f ");
			psql.append("where a.task_id=f.task_id and f.task_id='"+taskId+"'");
			psql.append(") t,tab_gw_device b ");
			psql.append("where t.device_serialnumber=b.device_serialnumber ");
			
			List<Map> list = jt.queryForList(psql.getSQL());
			List<Map> returnList=new ArrayList<Map>();
			if(list!=null && !list.isEmpty()){
				Map<String,String> vn=getVendorName();
				Map<String,String> dm=getDeviceModel();
				for(Map m:list)
				{
					String city_name=CityDAO.getCityName(StringUtil.getStringValue(m,"city_id"));
					String vendor_name=vn.get(StringUtil.getStringValue(m,"vendor_id"));
					String device_model=dm.get(StringUtil.getStringValue(m,"device_model_id"));
					if(StringUtil.IsEmpty(city_name) 
							|| StringUtil.IsEmpty(vendor_name) 
							|| StringUtil.IsEmpty(device_model)){
						continue;
					}
					
					Map map=new HashMap();
					map.put("city_name",city_name);
					map.put("vendor_name",vendor_name);
					map.put("device_model",device_model);
					map.put("task_name",StringUtil.getStringValue(m,"task_name"));
					map.put("tcpopenrequesttime",StringUtil.getStringValue(m,"tcpopenrequesttime"));
					map.put("tcpopenresponsetime",StringUtil.getStringValue(m,"tcpopenresponsetime"));
					map.put("totalbytessent",StringUtil.getStringValue(m,"totalbytessent"));
					map.put("totalbytesreceived",StringUtil.getStringValue(m,"totalbytesreceived"));
					map.put("bom_time",StringUtil.getStringValue(m,"bom_time"));
					map.put("eom_time",StringUtil.getStringValue(m,"eom_time"));
					map.put("device_serialnumber",StringUtil.getStringValue(m,"device_serialnumber"));
					map.put("upload_pert",StringUtil.getStringValue(m,"upload_pert"));
					
					returnList.add(map);
					map=null;
					m=null;
				}
			}
			list.clear();
			list=null;
			return returnList;
		}else{
			psql.append("select f.task_name,e.city_name,c.vendor_name,d.device_model,a.tcpopenrequesttime,a.tcpopenresponsetime,a.totalbytessent,a.totalbytesreceived,a.bom_time,a.eom_time,a.device_serialnumber,a.upload_pert from tab_http_diag_result_upload a,tab_gw_device b,tab_vendor c,gw_device_model d,tab_city e ");
			psql.append(" ,tab_ids_task f where a.task_id=f.task_id and a.device_serialnumber=b.device_serialnumber and b.vendor_id=c.vendor_id and b.device_model_id=d.device_model_id and b.city_id=e.city_id");
			psql.append(" and f.task_id='"+taskId+"'");
			return jt.queryForList(psql.getSQL());
		}
	}
	
	
	public int queryDevBySerialnumberCount(String taskname,String serialnumber,String starttime,String endtime,int curPage_splitPage, int num_splitPage)
	{
		int total=0;
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select count(1) from tab_http_diag_result_upload a,tab_gw_device b,tab_vendor c,gw_device_model d,tab_city e,tab_ids_task f ");
			psql.append(" where a.device_serialnumber=b.device_serialnumber and b.vendor_id=c.vendor_id and b.device_model_id=d.device_model_id and b.city_id=e.city_id and a.task_id=f.task_id");
			psql.append(" and a.device_serialnumber='"+serialnumber+"'");
			
			if(!StringUtil.IsEmpty(starttime)){
				psql.append(" and a.test_time>"+starttime);
			}
			
			if(!StringUtil.IsEmpty(endtime)){
				psql.append(" and a.test_time<"+endtime);
			}
			if(!StringUtil.IsEmpty(taskname)){
				psql.append(" and f.task_name like '%"+taskname+"%'");
			}
			//TODO
			//TODO
			//TODO
		}else{
			psql.append("select count(1) from tab_http_diag_result_upload a,tab_gw_device b,tab_vendor c,gw_device_model d,tab_city e,tab_ids_task f ");
			psql.append(" where a.device_serialnumber=b.device_serialnumber and b.vendor_id=c.vendor_id and b.device_model_id=d.device_model_id and b.city_id=e.city_id and a.task_id=f.task_id");
			psql.append(" and a.device_serialnumber='"+serialnumber+"'");
			
			if(!StringUtil.IsEmpty(starttime)){
				psql.append(" and a.test_time>"+starttime);
			}
			
			if(!StringUtil.IsEmpty(endtime)){
				psql.append(" and a.test_time<"+endtime);
			}
			if(!StringUtil.IsEmpty(taskname)){
				psql.append(" and f.task_name like '%"+taskname+"%'");
			}
			
			total = jt.queryForInt(psql.getSQL());
		}
		
		
		
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	
	/**
	 * 获取所有厂商
	 */
	private Map<String,String> getVendorName()
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select vendor_id,vendor_name from tab_vendor order by vendor_id ");
		List<Map> list=jt.queryForList(psql.getSQL());
		Map<String,String> map=new HashMap<String,String>();
		if(list!=null && !list.isEmpty()){
			for(Map m:list){
				map.put(StringUtil.getStringValue(m,"vendor_id"),
						StringUtil.getStringValue(m,"vendor_name"));
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

}
