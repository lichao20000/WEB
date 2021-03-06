package com.linkage.module.gtms.config.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.itms.resource.util.DeviceTypeUtil;

public class RestartDeviceBatchDAO extends SuperDAO {

	private static Logger logger = LoggerFactory.getLogger(RestartDeviceBatchDAO.class);
	private SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sdfSec = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


	/**
	 * 获取今天总数
	 */
	public long getTodayCount() {
		long startTime = getStartTime();

		PrepareSQL pSQL = new PrepareSQL();

		pSQL.append(" select count(*) allnum from tab_restart_task_dev where add_time>="
				+ startTime + " and add_time<" + (startTime + 86400));

		return StringUtil.getLongValue(DBOperation.getRecord(pSQL.getSQL()), "allnum", 0);
	}


	public long getStartTime() {
		try {
			String day = sdfDay.format(new Date());
			return sdfSec.parse(day + " 00:00:00").getTime() / 1000L;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}


	/**
	 * 查询设备信息
	 * @param sqlSpell
	 */
	public  ArrayList<HashMap<String, String>> getDevIds4NX(String sqlSpell){
		return DBOperation.getRecords(sqlSpell);
	}


	/**
	 * 增加任务信息
	 * @param taskid
	 * @param accoid
	 * @param param
	 * @param type
	 * @param serviceId
	 * @param strategy_type
	 * @param taskName
	 * @param task_wan_type
	 * @param paramPath
	 * @param paramValue
	 * @param paramType
	 * @return
	 */
	public int addTask4CQ(long taskid, String taskName, long accoid, String param, int type, String restartNum, String restartInterval){

		PrepareSQL psql = new PrepareSQL();
		psql.append(" insert into tab_restart_task(task_id, task_name, acc_oid, add_time, ");
		psql.append(" param, type, restart_num, restart_interval, status) values(?,?,?,?,?, ?,?,?,?)");

		psql.setLong(1, taskid);
		psql.setString(2, taskName);
		psql.setLong(3, accoid);
		psql.setLong(4, taskid);
		psql.setString(5, param);
		psql.setInt(6, type);
		psql.setInt(7, StringUtil.getIntegerValue(restartNum));
		psql.setInt(8, StringUtil.getIntegerValue(restartInterval));
		psql.setInt(9, 7);

		return jt.update(psql.getSQL());

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
	public ArrayList<HashMap<String, String>> getTaskDevList4CQ(String filename) {

		PrepareSQL psql = new PrepareSQL();// TODO wait (more table related)

		psql.append(" select a.device_serialnumber a_device_id, b.device_id, b.oui, b.device_serialnumber, d.wan_type, ");
		psql.append(" d.username serusername, c.username, b.city_id, d.user_id from tab_temporary_device a left join tab_gw_device b on ");
		psql.append(" a.device_serialnumber=b.device_id left join tab_hgwcustomer c on b.device_id=c.device_id ");
		psql.append(" left join hgwcust_serv_info d on (c.user_id=d.user_id and d.serv_type_id=10 and d.serv_status=1 ");
		if(LipossGlobals.inArea(Global.HBLT)) {
			psql.append(" and d.wan_type=2 ");
		}
		psql.append(" ) where a.filename=? order by d.opendate desc ");

		psql.setString(1, filename);

		ArrayList<HashMap<String, String>> list = DBOperation.getRecords(psql.getSQL());
		if(null==list || list.size()==0){
			return null;
		}

		if(LipossGlobals.inArea(Global.HBLT)) {
			return list;
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


	public ArrayList<String> sqlList4CQ(List<HashMap<String, String>> devList,long taskid){
		ArrayList<String> sqlList = new ArrayList<String>();
		for (int i = 0; i < devList.size(); i++) {
			String device_serialnumber = String.valueOf(devList.get(i).get("device_serialnumber"));
			String a_device_id = String.valueOf(devList.get(i).get("a_device_id"));
			String device_id = String.valueOf(devList.get(i).get("device_id"));
			String oui = String.valueOf(devList.get(i).get("oui"));
			String netusername = String.valueOf(devList.get(i).get("serusername"));
			String loid = String.valueOf(devList.get(i).get("username"));
			String user_id = String.valueOf(devList.get(i).get("user_id"));

			PrepareSQL pSql = new PrepareSQL();
			pSql.append(" insert into tab_restart_task_dev (task_id,device_id,oui,device_serialnumber ");
			pSql.append(" ,file_username,loid,netusername,add_time,status) values(?,?,?,?,?, ?,?,?,?)");

			pSql.setLong(1, taskid);
			pSql.setString(2, device_id);
			pSql.setString(3, oui);
			pSql.setString(4, device_serialnumber);
			pSql.setString(5, a_device_id);
			pSql.setString(6, loid);
			pSql.setString(7, netusername);
			pSql.setLong(8, taskid);
			pSql.setLong(9, 7);

			sqlList.add(pSql.getSQL());


		}
		return sqlList;
	}

	/**
	 * 批量插入设备信息
	 * @param sqlList
	 */
	public int insertTaskDev4CQ(ArrayList<String> sqlList){
		return DBOperation.executeUpdate(sqlList);
	}

	/**
	 * 管理页面初查任务
	 * @returns
	 */
	public List<Map> queryTaskList4CQ(String task_name_query, int status_query,
			long expire_time_start, long expire_time_end,int curPage_splitPage,
			int num_splitPage) {

		PrepareSQL psql = new PrepareSQL();
		psql.append("select task_name, settime, totalNum, succNum, failNum, unDoneNum, task_id, t_status, task_name from tab_restart_task ");
		psql.append(" where 1=1 ");
		if (null != task_name_query && !"".equals(task_name_query.trim())) {
			psql.append(" and task_name ='"+task_name_query+"'");
		}
		if(1==status_query){
			psql.append(" and status!=7 ");
		}else if(7==status_query){
			psql.append(" and status=7 ");
		}
//		if (-1 != status_query) {
//			psql.append(" and status = "+status_query);
//		}
		if (-1 != expire_time_start) {
			psql.append(" and add_time >= "+expire_time_start);
		}
		if (-1 != expire_time_end) {
			psql.append(" and add_time <= "+expire_time_end);
		}
		psql.append(" order by add_time desc");

		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				long succNum = 0;
				long failNum = 0;
				long totalNum = 0;
				long unDoneNum = 0;
				Map<String, String> map = new HashMap<String, String>();
				map.put("task_name", rs.getString("task_name"));
				String taskId = rs.getString("task_id");

				if(null != taskId && !"".equals(taskId)){
					PrepareSQL psql = new PrepareSQL();
					psql.append("select count(*) from tab_restart_task_dev where (status = 0 or status = 7) and task_id="+taskId);
					unDoneNum = jt.queryForLong(psql.getSQL());

					PrepareSQL psqlb = new PrepareSQL();
					psqlb.append("select count(*) from tab_restart_task_dev where status=1 and task_id="+taskId);
					succNum = jt.queryForLong(psqlb.getSQL());

					PrepareSQL psqla = new PrepareSQL();
					psqla.append("select count(*) from tab_restart_task_dev where task_id="+taskId);
					totalNum = jt.queryForLong(psqla.getSQL());

					failNum = totalNum-unDoneNum-succNum;

				}

				map.put("task_id", taskId);
				map.put("totalNum", totalNum+"");
				map.put("succNum", succNum+"");
				map.put("failNum", failNum+"");
				map.put("unDoneNum", unDoneNum+"");
				String status = rs.getString("status");
				/*if("1".equals(status)){
					map.put("status", "正常");
				}else{
					map.put("status", "暂停");
				}*/
				map.put("status", status);

				String set_time = rs.getString("add_time");
				String date = new DateTimeUtil(StringUtil.getLongValue(set_time)*1000).getLongDate();
				map.put("settime", date);

				int t_status = StringUtil.getIntegerValue(rs.getString("status"));
				int t_type = StringUtil.getIntegerValue(rs.getString("type"));
				if(2==t_type && 0==t_status){
					map.put("t_status", "doing");
				}

				return map;
			}
		});
		return list;

	}


	public int queryTaskCount4CQ(String task_name_query, int status_query,
			long expire_time_start, long expire_time_end, int curPage_splitPage,
			int num_splitPage) {
		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(*)  from tab_restart_task");

		psql.append(" where 1=1 ");
		if (null != task_name_query && !"".equals(task_name_query.trim())) {
			psql.append(" and task_name ='"+task_name_query+"'");
		}

		if(1==status_query){
			psql.append(" and status!=7 ");
		}else if(7==status_query){
			psql.append(" and status=7 ");
		}

//		if (-1 != status_query) {
//			psql.append(" and status = "+status_query);
//		}
		if (-1 != expire_time_start) {
			psql.append(" and add_time >= "+expire_time_start);
		}
		if (-1 != expire_time_end) {
			psql.append(" and add_time <= "+expire_time_end);
		}
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	/**
	 * 根据task_id从明细表、设备表根据属地分组统计任务信息(成功、失败等)
	 * @param task_id
	 * @param task_name
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> queryTaskGyCityList(String task_id,String task_name,int curPage_splitPage, int num_splitPage) {

		List<Map> tList = new ArrayList<Map>();
		List<Map<String,String>> sList = new ArrayList<Map<String,String>>();
		List<Map<String,String>> uList = new ArrayList<Map<String,String>>();
		long succNum = 0;
		long failNum = 0;
		long totalNum = 0;
		long unDoneNum = 0;

		if(null != task_id || "".equals(task_id)){
			PrepareSQL psql = new PrepareSQL();
			psql.append("select count(*) num,d.city_id from tab_restart_task_dev s,tab_gw_device d where d.device_id=s.device_id and (s.status = 0 or s.status = 7) and s.task_id="+task_id);
			psql.append(" group by d.city_id");
			uList = jt.queryForList(psql.getSQL());

			PrepareSQL psqlb = new PrepareSQL();
			psqlb.append("select count(*) num,d.city_id from tab_restart_task_dev s,tab_gw_device d where d.device_id=s.device_id and s.status=1 and s.task_id="+task_id);
			psqlb.append(" group by d.city_id");
			sList = jt.queryForList(psqlb.getSQL());

			PrepareSQL psqla = new PrepareSQL();
			psqla.append("select count(*) num,d.city_id from tab_restart_task_dev s,tab_gw_device d where d.device_id=s.device_id and s.task_id="+task_id);
			psqla.append(" group by d.city_id order by d.city_id");
			tList = querySP(psqla.getSQL(), (curPage_splitPage - 1) * num_splitPage
					+ 1, num_splitPage);
			//tList = jt.queryForList(psqla.getSQL());

			logger.warn("......tList="+tList.size()+"\n"+tList.toString());
			logger.warn("......sList="+sList.size()+"\n"+sList.toString());
			logger.warn("......uList="+uList.size()+"\n"+uList.toString());
			for(int i=0;i<tList.size();i++){
				Map<String,String> map  = tList.get(i);
				succNum = 0;
				failNum = 0;
				unDoneNum = 0;
				String cityid = map.get("city_id");
				totalNum = StringUtil.getLongValue(map.get("num"));
				for(Map<String,String> maps:sList){
					if(cityid.equals(maps.get("city_id"))){
						succNum = StringUtil.getLongValue(maps.get("num"));
						break;
					}
				}
				for(Map<String,String> mapu:uList){
					if(cityid.equals(mapu.get("city_id"))){
						unDoneNum = StringUtil.getLongValue(mapu.get("num"));
						break;
					}
				}
				failNum = totalNum-unDoneNum-succNum;
				map.put("totalNum", totalNum+"");
				map.put("succNum", succNum+"");
				map.put("failNum", failNum+"");
				map.put("unDoneNum", unDoneNum+"");
				map.put("task_name", task_name);
				map.put("city_id",cityid);
				map.put("city_name", Global.G_CityId_CityName_Map.get(cityid));
			}
		}

		return tList;

	}

	public List<Map> queryTaskGyCityList(String task_id,String task_name) {

		List<Map> tList = new ArrayList<Map>();
		List<Map<String,String>> sList = new ArrayList<Map<String,String>>();
		List<Map<String,String>> uList = new ArrayList<Map<String,String>>();
		long succNum = 0;
		long failNum = 0;
		long totalNum = 0;
		long unDoneNum = 0;

		if(null != task_id || "".equals(task_id)){
			PrepareSQL psql = new PrepareSQL();
			psql.append("select count(*) num,d.city_id from tab_restart_task_dev s,tab_gw_device d where d.device_id=s.device_id and (s.status = 0 or s.status = 7) and s.task_id="+task_id);
			psql.append(" group by d.city_id");
			uList = jt.queryForList(psql.getSQL());

			PrepareSQL psqlb = new PrepareSQL();
			psqlb.append("select count(*) num,d.city_id from tab_restart_task_dev s,tab_gw_device d where d.device_id=s.device_id and s.status=1 and s.task_id="+task_id);
			psqlb.append(" group by d.city_id");
			sList = jt.queryForList(psqlb.getSQL());

			PrepareSQL psqla = new PrepareSQL();
			psqla.append("select count(*) num,d.city_id from tab_restart_task_dev s,tab_gw_device d where d.device_id=s.device_id and s.task_id="+task_id);
			psqla.append(" group by d.city_id order by d.city_id");
			tList = jt.queryForList(psqla.getSQL());
			//tList = jt.queryForList(psqla.getSQL());

			logger.warn("......tList="+tList.size()+"\n"+tList.toString());
			logger.warn("......sList="+sList.size()+"\n"+sList.toString());
			logger.warn("......uList="+uList.size()+"\n"+uList.toString());
			for(int i=0;i<tList.size();i++){
				Map<String,String> map  = tList.get(i);
				succNum = 0;
				failNum = 0;
				unDoneNum = 0;
				String cityid = map.get("city_id");
				totalNum = StringUtil.getLongValue(map.get("num"));
				for(Map<String,String> maps:sList){
					if(cityid.equals(maps.get("city_id"))){
						succNum = StringUtil.getLongValue(maps.get("num"));
						break;
					}
				}
				for(Map<String,String> mapu:uList){
					if(cityid.equals(mapu.get("city_id"))){
						unDoneNum = StringUtil.getLongValue(mapu.get("num"));
						break;
					}
				}

				PrepareSQL psqlTaskName = new PrepareSQL("select task_name from tab_restart_task where task_id="+task_id);
				List<Map<String,String>> taskNameList = jt.queryForList(psqlTaskName.getSQL());

				failNum = totalNum-unDoneNum-succNum;
				map.put("totalNum", totalNum+"");
				map.put("succNum", succNum+"");
				map.put("failNum", failNum+"");
				map.put("unDoneNum", unDoneNum+"");
				map.put("task_name", taskNameList.get(0).get("task_name"));
				map.put("city_id",cityid);
				map.put("city_name", Global.G_CityId_CityName_Map.get(cityid));
			}
		}

		return tList;

	}


	public int queryTaskGyCityCount(String task_id) {

		int total = 0;
		if(null != task_id || "".equals(task_id)){
			PrepareSQL psqla = new PrepareSQL();
			psqla.append("select count(x.num) from (select count(*) num,d.city_id from tab_restart_task_dev s,tab_gw_device d where d.device_id=s.device_id and s.task_id="+task_id);
			psqla.append(" group by d.city_id) x");
			total = jt.queryForInt(psqla.getSQL());
		}

		return total;
	}

	public List<Map> queryDevList4CQ(String task_id, String city_id, String type,
			int curPage_splitPage, int num_splitPage)
	{
		List<Map> list = new ArrayList<Map>();

		if(null != task_id || "".equals(task_id)){
			PrepareSQL psql = new PrepareSQL();
			psql.append("select d.vendor_id,d.city_id,d.device_model_id,d.device_serialnumber,d.devicetype_id,s.status," +
					"s.restart_time from tab_restart_task_dev s,tab_gw_device d where d.device_id=s.device_id and s.task_id="+task_id);
			if(!StringUtil.IsEmpty(city_id)){
				psql.append(" and d.city_id='"+city_id+"'");
			}
			if("unDone".equals(type)){
				psql.append(" and (s.status = 0 or s.status = 7) ");
			}
			else if("succ".equals(type)){
				psql.append(" and s.status=1 ");
			}
			else if("fail".equals(type)){
				psql.append(" and s.status != 0 and s.status != 7 and (s.status!=1) ");
			}
			psql.append(" order by s.status desc,s.restart_time desc");

			//不传分页信息查询全部
			if(curPage_splitPage==0 && num_splitPage==0){
				list = jt.queryForList(psql.getSQL());
			}
			else{
				list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
						+ 1, num_splitPage);
			}

			for(int i=0;i<list.size();i++){
				Map<String,String> map  = list.get(i);
				String vendor_id = map.get("vendor_id");
				if(null!=DeviceTypeUtil.vendorMap && !StringUtil.IsEmpty(vendor_id)){
					String vendor = DeviceTypeUtil.vendorMap.get(vendor_id);
					map.put("vendor_id", vendor);
				}

				String device_model_id = map.get("device_model_id");
				if(null!=DeviceTypeUtil.deviceModelMap && !StringUtil.IsEmpty(device_model_id)){
					String model = DeviceTypeUtil.deviceModelMap.get(device_model_id);
					map.put("device_model_id", model);
				}

				String devicetype_id = StringUtil.getStringValue(map.get("devicetype_id"));
				if(null!=DeviceTypeUtil.softVersionMap && !StringUtil.IsEmpty(devicetype_id)){
					String version = DeviceTypeUtil.softVersionMap.get(devicetype_id);
					map.put("version", version);
				}


				long start_time = StringUtil.getLongValue(map.get("restart_time"));
				if(start_time!=0){
					map.put("settime", new DateTimeUtil(start_time*1000).getLongDate());
				}
				else{
					map.put("settime", "");
				}

				if(null!=Global.G_CityId_CityName_Map){
					map.put("city_name", Global.G_CityId_CityName_Map.get(city_id));
				}

				int status = StringUtil.getIntegerValue(map.get("status"));
				logger.warn(map.get("device_serialnumber")+"...................status="+status);
				if(null!=Global.G_Fault_Map && null!=Global.G_Fault_Map.get(status) && !StringUtil.IsEmpty(Global.G_Fault_Map.get(status).getFaultDesc())){
					map.put("result_desc", Global.G_Fault_Map.get(status).getFaultDesc());
				}
				else{
					map.put("result_desc", "其他");
				}

				if(status==0||status==7){
					map.put("result_id", "未做");
					map.put("result_desc", "/");
				}
				else if(status==1){
					map.put("result_id", "成功");
				}
				else{
					map.put("result_id", "失败");
				}
			}
		}
		return list;
	}

	public int update(String task_id,String type)
	{
		List<String> sqlList = new ArrayList<String>();
		int res = 1;
		String[] sqls = new String[2];
		//当前状态：1为正常，7为暂停
		if("7".equals(type)){
			sqls[0] = "update tab_restart_task set status=7 where task_id="+task_id;
			sqls[1] = "update tab_restart_task_dev set status=7 where status=0 and task_id="+task_id;//删除未作的策略
		}
		else{
			PrepareSQL pSql = new PrepareSQL("select type from tab_restart_task where task_id="+task_id);
			Map<String,String> resMap = DBOperation.getRecord(pSql.getSQL());
			if(null==resMap){
				return 0;
			}

			String taskType = StringUtil.getStringValue(resMap, "type", "");

			if("1".equals(taskType))
			{
				sqls[0] = "update tab_restart_task set status=0 where task_id="+task_id;
				sqls[1] = "update tab_restart_task_dev set status=0 where status=7 and task_id="+task_id;//删除未作的策略

				pSql = new PrepareSQL(" select b.user_id, a.netusername from tab_restart_task_dev a,tab_hgwcustomer b where a.loid=b.username ");
				pSql.append(" and a.task_id=" + task_id);
				List<HashMap<String, String>> list = DBOperation.getRecords(pSql.getSQL());
				if(null!=list){
					for(HashMap<String, String> map : list)
					{
						String userId = map.get("user_id");
						String netusername = map.get("netusername");

						if(!StringUtil.IsEmpty(userId) && !StringUtil.IsEmpty(netusername))
						{
							pSql = new PrepareSQL(" update tab_net_serv_param set ip_type=3,untreated_ip_type=1 ");
							pSql.append(" where user_id=" + userId + " and serv_type_id=10 and username='"+netusername+"'");
							sqlList.add(pSql.getSQL());

							pSql = new PrepareSQL(" update hgwcust_serv_info set ip_type=3 ");
							pSql.append(" where user_id=" + userId + " and serv_type_id=10 and username='"+netusername+"'");
							sqlList.add(pSql.getSQL());
						}
					}
				}
			}
			else if("2".equals(taskType))
			{
				sqls[0] = "update tab_restart_task set status=0 where task_id="+task_id;
				sqls[1] = "update tab_restart_task_dev set status=0 where status=7 and task_id="+task_id;//删除未作的策略
			}
		}

		try{
			logger.info("sqls[0][{}]", sqls[0]);
			logger.info("sqls[1][{}]", sqls[1]);
			jt.batchUpdate(sqls);

			ArrayList<String> sqlListTmp = new ArrayList<String>();
			int resSuccCount = 0;
			if(sqlList.size()>0){
				for(String sql : sqlList){
					sqlListTmp.add(sql);
					if(sqlListTmp.size()>=200){
						int resTmp = DBOperation.executeUpdate(sqlListTmp);
						if(resTmp>0){
							resSuccCount += sqlListTmp.size();
						}
						sqlListTmp.clear();
					}
				}
			}
			if(sqlListTmp.size()>0){
				int resTmp = DBOperation.executeUpdate(sqlListTmp);
				if(resTmp>0){
					resSuccCount += sqlListTmp.size();
				}
				sqlListTmp.clear();
			}
			logger.info("====成功更新 hgwcust_serv_info，tab_net_serv_param 表"+resSuccCount/2+"条数据====");

		}
		catch (Exception e) {
			e.printStackTrace();
			res = 0;
		}
		return res;
	}

	public int updateCount(String task_id,String type)
	{
		PrepareSQL psql = new PrepareSQL();
		if("1".equals(type)){
			psql.append("select count(*) from tab_restart_task_dev where status!=7 and task_id='"+task_id+"'");//删除未作的策略
		}
		else{
			psql.append("select count(*) from tab_restart_task_dev where status=7 and task_id='"+task_id+"'");//删除未作的策略
		}

		int total = jt.queryForInt(psql.getSQL());
		return total;
	}

	public int del(String task_id)
	{
		int res = 1;
		String[] sqls = new String[2];
		sqls[0] = "delete from tab_restart_task where task_id="+task_id;
		sqls[1] = "delete from tab_restart_task_dev where task_id="+task_id;//删除未作的策略
		try{
			logger.info("sqls[0][{}]", sqls[0]);
			logger.info("sqls[1][{}]", sqls[1]);
			jt.batchUpdate(sqls);
		}
		catch (Exception e) {
			e.printStackTrace();
			res = 0;
		}
		return res;
	}


	public int delCount(String task_id)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(*) from tab_restart_task_dev where task_id="+task_id);//删除未作的策略

		int total = jt.queryForInt(psql.getSQL());
		return total;
	}

}
