
package com.linkage.module.gwms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.itms.resource.util.DeviceTypeUtil;

/**
 * @author zhaixx (Ailk No.)
 * @version 1.0
 * @since 2018年11月9日
 * @category com.linkage.module.gwms.config.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class SetMulticastBatchCountDAO extends SuperDAO {

	private static Logger logger = LoggerFactory.getLogger(SetMulticastBatchCountDAO.class);
	

	public List getOrderTaskListNew(int curPage_splitPage, int num_splitPage, String startTime, String endTime,
			String taskname) {
		PrepareSQL psql = new PrepareSQL(" select a.task_id,a.task_name,a.acc_oid,a.add_time,a.service_id from ");
		psql.append(" tab_setmulticast_task a where 1=1 ");
		if (!StringUtil.IsEmpty(taskname)) {
			psql.append(" and a.task_name like '%" + taskname + "%'");
		}
		if (!StringUtil.IsEmpty(startTime)) {
			psql.append(" and a.add_time>=" + startTime);
		}
		if (!StringUtil.IsEmpty(endTime)) {
			psql.append(" and a.add_time<=" + endTime);
		}
		psql.append(" order by a.add_time desc ");
		
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
					if(DBUtil.GetDB()==3){
						psql.append("select count(*) ");
					}else{
						psql.append("select count(1) ");
					}
					psql.append("from gw_serv_strategy_batch where (status != 100) and ids_task_id = ");
					psql.append(taskId);
					psql.append(" ");
					unDoneNum = jt.queryForLong(psql.getSQL());
					
					PrepareSQL psqlb = new PrepareSQL();
					if(DBUtil.GetDB()==3){
						psqlb.append("select count(*) ");
					}else{
						psqlb.append("select count(1) ");
					}
					psqlb.append("from gw_serv_strategy_batch where result_id = 1 and ids_task_id = ");
					psqlb.append(taskId);
					psqlb.append(" "); 
					succNum = jt.queryForLong(psqlb.getSQL());
					
					PrepareSQL psqla = new PrepareSQL();
					if(DBUtil.GetDB()==3){
						psqla.append("select count(*) ");
					}else{
						psqla.append("select count(1) ");
					}
					psqla.append("from gw_serv_strategy_batch where ids_task_id = ");
					psqla.append(taskId);
					psqla.append(" ");
					totalNum = jt.queryForLong(psqla.getSQL());

					failNum = totalNum-unDoneNum-succNum;

				}
				
				map.put("task_id", taskId);
				map.put("totalNum", totalNum+"");
				map.put("succNum", succNum+"");
				map.put("failNum", failNum+"");
				map.put("unDoneNum", unDoneNum+"");
				
				String set_time = rs.getString("add_time");
				String date = new DateTimeUtil(StringUtil.getLongValue(set_time)*1000).getLongDate();
				map.put("add_time", date);
					
				return map;
			}
		});
		
		return list;
	}

	public int countOrderTaskNew(int curPage_splitPage, int num_splitPage, String startTime, String endTime,
			String taskname) 
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select count(*) allnum ");
		}else{
			psql.append("select count(1) allnum ");
		}
		psql.append("from tab_setmulticast_task a where 1=1 ");
		
		if (!StringUtil.IsEmpty(taskname)) {
			psql.append(" and a.task_name like '%" + taskname + "%'");
		}
		if (!StringUtil.IsEmpty(startTime)) {
			psql.append(" and a.add_time>=" + startTime);
		}
		if (!StringUtil.IsEmpty(endTime)) {
			psql.append(" and a.add_time<=" + endTime);
		}
		int total = StringUtil.getIntValue(DBOperation.getRecord(psql.getSQL()), "allnum", 0);
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	public List getOrderTaskDetailNew(String taskname, int curPage_splitPage, int num_splitPage, String task_id) {

		List<Map> tList = new ArrayList<Map>();
		List<Map<String,String>> sList = new ArrayList<Map<String,String>>();
		List<Map<String,String>> uList = new ArrayList<Map<String,String>>();
		long succNum = 0;
		long failNum = 0;
		long totalNum = 0;
		long unDoneNum = 0;

		if(null != task_id || "".equals(task_id)){
			PrepareSQL psql = new PrepareSQL();
			if(DBUtil.GetDB()==3){
				psql.append("select count(*) num,d.city_id ");
			}else{
				psql.append("select count(1) num,d.city_id ");
			}
			psql.append("from gw_serv_strategy_batch s,tab_gw_device d where d.device_id=s.device_id and (s.status != 100) and s.ids_task_id = ");
			psql.append(task_id);
			psql.append(" group by d.city_id");
			uList = jt.queryForList(psql.getSQL());
			
			PrepareSQL psqlb = new PrepareSQL();
			if(DBUtil.GetDB()==3){
				psqlb.append("select count(*) num,d.city_id ");
			}else{
				psqlb.append("select count(1) num,d.city_id ");
			}
			psqlb.append("from gw_serv_strategy_batch s,tab_gw_device d where d.device_id=s.device_id and s.result_id = 1 and s.ids_task_id = ");
			psqlb.append(task_id);
			psqlb.append(" group by d.city_id");
			sList = jt.queryForList(psqlb.getSQL());
			
			PrepareSQL psqla = new PrepareSQL();
			if(DBUtil.GetDB()==3){
				psqla.append("select count(*) num,d.city_id ");
			}else{
				psqla.append("select count(1) num,d.city_id ");
			}
			psqla.append("from gw_serv_strategy_batch s,tab_gw_device d where d.device_id=s.device_id and s.ids_task_id = ");
			psqla.append(task_id);
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
				map.put("city_id",cityid);
				map.put("task_name",taskname);
				map.put("task_id", task_id);
				map.put("city_name", Global.G_CityId_CityName_Map.get(cityid));
			}
		}
		
		return tList;

	}
	public List getOrderTaskDetailNewExcel(String task_id, String task_name) {
		List<Map> tList = new ArrayList<Map>();
		List<Map<String,String>> sList = new ArrayList<Map<String,String>>();
		List<Map<String,String>> uList = new ArrayList<Map<String,String>>();
		long succNum = 0;
		long failNum = 0;
		long totalNum = 0;
		long unDoneNum = 0;

		if(null != task_id || "".equals(task_id)){
			PrepareSQL psql = new PrepareSQL();
			if(DBUtil.GetDB()==3){
				psql.append("select count(*) num,d.city_id ");
			}else{
				psql.append("select count(1) num,d.city_id ");
			}
			psql.append("from gw_serv_strategy_batch s,tab_gw_device d where d.device_id=s.device_id and (s.status != 100) and s.ids_task_id = ");
			psql.append(task_id);
			psql.append(" group by d.city_id");
			uList = jt.queryForList(psql.getSQL());
			
			PrepareSQL psqlb = new PrepareSQL();
			if(DBUtil.GetDB()==3){
				psqlb.append("select count(*) num,d.city_id ");
			}else{
				psqlb.append("select count(1) num,d.city_id ");
			}
			psqlb.append("from gw_serv_strategy_batch s,tab_gw_device d where d.device_id=s.device_id and s.result_id = 1 and s.ids_task_id = ");
			psqlb.append(task_id);
			psqlb.append(" group by d.city_id");
			sList = jt.queryForList(psqlb.getSQL());
			
			PrepareSQL psqla = new PrepareSQL();
			if(DBUtil.GetDB()==3){
				psqla.append("select count(*) num,d.city_id ");
			}else{
				psqla.append("select count(1) num,d.city_id ");
			}
			psqla.append("from gw_serv_strategy_batch s,tab_gw_device d where d.device_id=s.device_id and s.ids_task_id = ");
			psqla.append(task_id);
			psqla.append(" group by d.city_id order by d.city_id");
			tList = jt.queryForList(psqla.getSQL());

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
				map.put("city_id",cityid);
				map.put("task_name",task_name);
				map.put("city_name", Global.G_CityId_CityName_Map.get(cityid));
			}
		}
		return tList;
	}
	public int countOrderTaskDetailNew(String taskname, int curPage_splitPage, int num_splitPage, String taskId) {
		int total = 0;
		if(null != taskId || "".equals(taskId)){
			PrepareSQL psqla = new PrepareSQL();
			if(DBUtil.GetDB()==3){
				psqla.append("select count(x.num) from (select count(*) num,d.city_id ");
			}else{
				psqla.append("select count(x.num) from (select count(1) num,d.city_id ");
			}
			psqla.append("from gw_serv_strategy_batch s,tab_gw_device d where d.device_id=s.device_id and s.ids_task_id = ");
			psqla.append(taskId);
			psqla.append(" group by d.city_id) x");
			total = jt.queryForInt(psqla.getSQL());
		}
		
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
		
	}

	public List showDetailListByType(int curPage_splitPage, int num_splitPage, String task_id, String city_id, String type) {
		List<Map> list = new ArrayList<Map>();

		if(null != task_id || "".equals(task_id)){
			PrepareSQL psql = new PrepareSQL();
			psql.append("select d.vendor_id,d.city_id,d.device_model_id,d.device_serialnumber,d.devicetype_id,s.result_id,s.status,s.start_time,s.end_time from gw_serv_strategy_batch s,tab_gw_device d where d.device_id=s.device_id and s.ids_task_id = ");
			psql.append(task_id);
			psql.append(" ");
			if(!StringUtil.IsEmpty(city_id)){
				psql.append(" and d.city_id='"+city_id+"'");
			}
			if("unDone".equals(type)){
				psql.append(" and (s.status != 100) ");
			}
			else if("succ".equals(type)){
				psql.append(" and s.result_id=1 ");
			}
			else if("fail".equals(type)){
				psql.append(" and (s.result_id!=1 and s.status = 100) ");
			}
			
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
				
				
				long start_time = StringUtil.getLongValue(map.get("start_time"));
				if(start_time!=0){
					map.put("start_time", new DateTimeUtil(start_time*1000).getLongDate());
				}
				else{
					map.put("start_time", "");
				}
				
				long end_time = StringUtil.getLongValue(map.get("end_time"));
				if(start_time!=0){
					map.put("end_time", new DateTimeUtil(end_time*1000).getLongDate());
				}
				else{
					map.put("end_time", "");
				}
				
				if(null!=Global.G_CityId_CityName_Map){
					map.put("city_name", Global.G_CityId_CityName_Map.get(city_id));
				}
				
				int result_id = StringUtil.getIntegerValue(map.get("result_id"));
				int status = StringUtil.getIntegerValue(map.get("status"));
				if(null!=Global.G_Fault_Map && null!=Global.G_Fault_Map.get(result_id) && !StringUtil.IsEmpty(Global.G_Fault_Map.get(result_id).getFaultDesc())){
					map.put("result_desc", Global.G_Fault_Map.get(result_id).getFaultDesc());
				}
				else{
					map.put("result_desc", "其他");
				}
				
				if(status!=100){
					map.put("result_id", "未做");
					map.put("result_desc", "/");
				}
				else if(result_id==1){
					map.put("result_id", "成功");
				}
				else{
					map.put("result_id", "失败");
				}
			}
		}
		return list;
	}

	public int showDetailListCountByType(int curPage_splitPage, int num_splitPage, String task_id, String city_id,
			String type) {
		
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select count(*) allnum ");
		}else{
			psql.append("select count(1) allnum ");
		}
		psql.append("from gw_serv_strategy_batch s,tab_gw_device d where d.device_id=s.device_id and s.ids_task_id = ");
		psql.append(task_id);
		psql.append(" ");
		if(!StringUtil.IsEmpty(city_id)){
			psql.append(" and d.city_id='"+city_id+"'");
		}
		if("unDone".equals(type)){
			psql.append(" and (s.status != 100) ");
		}
		else if("succ".equals(type)){
			psql.append(" and s.result_id=1 ");
		}
		else if("fail".equals(type)){
			psql.append(" and (s.result_id!=1 and s.status = 100) ");
		}
		
		int total = StringUtil.getIntValue(DBOperation.getRecord(psql.getSQL()), "allnum", 0);
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
}
