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

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DBOperation;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.resource.obj.BatchSoftUpBean;
import com.linkage.module.itms.resource.util.DeviceTypeUtil;

/**
 * 
 * @author zzd (Ailk No.)
 * @version 1.0
 * @since 2016-10-13
 * @category com.linkage.module.gwms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class BatchSoftwareUpDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(BatchSoftwareUpDAO.class);





	public List<Map> queryTaskList(String task_name_query, int status_query,
			long expire_time_start, long expire_time_end,int curPage_splitPage,
			int num_splitPage) {

		PrepareSQL psql = new PrepareSQL();
		psql.append("select task_id,task_name,status");
		psql.append(" from tab_time_softup_task");
		psql.append(" where 1=1 ");
		if (null != task_name_query && !"".equals(task_name_query.trim())) {
			psql.append(" and task_name ='"+task_name_query+"'");
		}
		if (-1 != status_query) {
			psql.append(" and status = '"+status_query+"'");
		}
		if (-1 != expire_time_start) {
			psql.append(" and add_time >= "+expire_time_start);
		}
		if (-1 != expire_time_end) {
			psql.append(" and add_time <= "+expire_time_end);
		}

		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				long totalNum = 0;
				long unDoneNum = 0;
				long doneNum = 0;
				Map<String, String> map = new HashMap<String, String>();
				map.put("task_name", rs.getString("task_name"));
				String taskId = rs.getString("task_id");

				if(null != taskId || "".equals(taskId)){
					PrepareSQL psql = new PrepareSQL();
					if(DBUtil.GetDB()==3){
						psql.append("select count(*) ");
					}else{
						psql.append("select count(1) ");
					}
					psql.append("from tab_time_softup_task_dev where status = 0 and task_id = ");
					psql.append(taskId);
					unDoneNum = jt.queryForLong(psql.getSQL());
					PrepareSQL psqlb = new PrepareSQL();
					if(DBUtil.GetDB()==3){
						psqlb.append("select count(*) ");
					}else{
						psqlb.append("select count(1) ");
					}
					psqlb.append("from tab_time_softup_task_dev where task_id = ");
					psqlb.append(taskId);
					totalNum = jt.queryForLong(psqlb.getSQL());

					doneNum = totalNum-unDoneNum;

				}
				map.put("totalNum", totalNum+"");
				map.put("doneNum", doneNum+"");
				map.put("unDoneNum", unDoneNum+"");
				String status = rs.getString("status");
				if("1".equals(status)){
					map.put("status", "是");
				}else{
					map.put("status", "否");
				}

				return map;
			}
		});
		return list;

	}
	
	
	
	public List<Map> queryTaskList4CQ(String task_name_query, int status_query,
			long expire_time_start, long expire_time_end,int curPage_splitPage,
			int num_splitPage,String gw_type,String mode) {

		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select task_name,task_id,status,task_model,set_time ");
		}else{
			psql.append("select * ");
		}
		psql.append("from tab_softup_task where 1=1 ");
		if("2".equals(gw_type)){
			psql.append(" and gw_type =2");
		}
		else{
			psql.append(" and gw_type !=2");
		}
		//安徽联通 选择条件增加 主被动模式选择
		if(!StringUtil.IsEmpty(mode)){
			psql.append(" and task_model = "+mode);
		}

		if (null != task_name_query && !"".equals(task_name_query.trim())) {
			psql.append(" and task_name like '%"+task_name_query+"%'");
		}
		if (-1 != status_query) {
			psql.append(" and status = "+status_query);
		}
		if (-1 != expire_time_start) {
			psql.append(" and set_time >= "+expire_time_start);
		}
		if (-1 != expire_time_end) {
			psql.append(" and set_time <= "+expire_time_end);
		}
		psql.append(" order by set_time desc");
		
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
					
					String strategyTab = "gw_serv_strategy_soft";
					if(LipossGlobals.inArea(Global.YNLT)){
						strategyTab = "gw_serv_strategy";
					} 
					
					PrepareSQL psql = new PrepareSQL();
					if(DBUtil.GetDB()==3){
						psql.append("select count(*) from ");
					}else{
						psql.append("select count(1) from ");
					}
					psql.append(strategyTab + " where (status!=100) and task_id='"+taskId+"'");
					if(LipossGlobals.inArea(Global.YNLT)){
						psql.append(" and service_id=5");
					}
					unDoneNum = jt.queryForLong(psql.getSQL());
					
					PrepareSQL psqlb = new PrepareSQL();
					if(DBUtil.GetDB()==3){
						psqlb.append("select count(*) from ");
					}else{
						psqlb.append("select count(1) from ");
					}
					psqlb.append(strategyTab + " where result_id=1 and task_id='"+taskId+"'"); 
					if(LipossGlobals.inArea(Global.YNLT)){
						psqlb.append(" and service_id=5");
					}
					succNum = jt.queryForLong(psqlb.getSQL());
					
					PrepareSQL psqla = new PrepareSQL();
					if(DBUtil.GetDB()==3){
						psqla.append("select count(*) from ");
					}else{
						psqla.append("select count(1) from ");
					}
					psqla.append(strategyTab + "  where task_id='"+taskId+"'");
					if(LipossGlobals.inArea(Global.YNLT)){
						psqla.append(" and service_id=5");
					}
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
				
				String TASK_MODEL = rs.getString("task_model");
				if("1".equals(TASK_MODEL)){
					map.put("task_model", "被动模式");
				}else{
					map.put("task_model", "主动模式");
				}

				String set_time = rs.getString("set_time");
				String date = new DateTimeUtil(StringUtil.getLongValue(set_time)*1000).getLongDate();
				map.put("set_time", date);
					
				return map;
			}
		});
		return list;

	}

	/**
	 * 根据task_id从策略表、设备表根据属地分组统计任务信息(成功、失败等)
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

		String strategyTab = "gw_serv_strategy_soft";
		if(LipossGlobals.inArea(Global.YNLT)){
			strategyTab = "gw_serv_strategy";
		}
		
		if(null != task_id || "".equals(task_id)){
			PrepareSQL psql = new PrepareSQL();
			if(DBUtil.GetDB()==3){
				psql.append("select count(*) num,d.city_id from ");
			}else{
				psql.append("select count(1) num,d.city_id from ");
			}
			psql.append(strategyTab+" s,tab_gw_device d where d.device_id=s.device_id and (s.status != 100) and s.task_id = '");
			psql.append(task_id + "'");
			if(LipossGlobals.inArea(Global.YNLT)){
				psql.append(" and s.service_id=5");
			}
			psql.append(" group by d.city_id");
			uList = jt.queryForList(psql.getSQL());
			
			PrepareSQL psqlb = new PrepareSQL();
			if(DBUtil.GetDB()==3){
				psql.append("select count(*) num,d.city_id from ");
			}else{
				psql.append("select count(1) num,d.city_id from ");
			}
			psqlb.append(strategyTab+" s,tab_gw_device d where d.device_id=s.device_id and s.result_id = 1 and s.task_id = '");
			psqlb.append(task_id + "'");
			if(LipossGlobals.inArea(Global.YNLT)){
				psqlb.append(" and s.service_id=5");
			}
			psqlb.append(" group by d.city_id");
			sList = jt.queryForList(psqlb.getSQL());
			
			PrepareSQL psqla = new PrepareSQL();
			if(DBUtil.GetDB()==3){
				psql.append("select count(*) num,d.city_id from ");
			}else{
				psql.append("select count(1) num,d.city_id from ");
			}
			psqla.append(strategyTab+" s,tab_gw_device d where d.device_id=s.device_id and s.task_id = '");
			psqla.append(task_id + "'");
			if(LipossGlobals.inArea(Global.YNLT)){
				psqla.append(" and s.service_id=5");
			}
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

	
	public int queryTaskGyCityCount(String task_id) {

		String strategyTab = "gw_serv_strategy_soft";
		if(LipossGlobals.inArea(Global.YNLT)){
			strategyTab = "gw_serv_strategy";
		}
		int total = 0;
		if(null != task_id || "".equals(task_id)){
			PrepareSQL psqla = new PrepareSQL();
			if(DBUtil.GetDB()==3){
				psqla.append("select count(x.num) from (select count(*) num,d.city_id from ");
			}else{
				psqla.append("select count(x.num) from (select count(1) num,d.city_id from ");
			}
			psqla.append(strategyTab+" s,tab_gw_device d where d.device_id=s.device_id and s.task_id = '");
			psqla.append(task_id + "'");
			if(LipossGlobals.inArea(Global.YNLT)){
				psqla.append(" and s.service_id=5");
			}
			psqla.append(" group by d.city_id) x");
			total = jt.queryForInt(psqla.getSQL());
		}
		
		return total;
	}
	
	
	public int queryTaskCount(String task_name_query, int status_query,
			long expire_time_start, long expire_time_end, int curPage_splitPage,
			int num_splitPage) 
	{
		PrepareSQL psql = new PrepareSQL();
		String instArea = Global.instAreaShortName;
		psql.append("select count(*)  from tab_time_softup_task");
		psql.append(" where 1=1 ");
		if (null != task_name_query && !"".equals(task_name_query.trim())) {
			psql.append(" and task_name ='"+task_name_query+"'");
		}
		if (-1 != status_query) {
			psql.append(" and status = '"+status_query+"'");
		}
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
	
	public int queryTaskCount4CQ(String task_name_query, int status_query,
			long expire_time_start, long expire_time_end, int curPage_splitPage,
			int num_splitPage,String gw_type,String mode) {
		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(*)  from tab_softup_task");
		psql.append(" where 1=1 ");
		if("2".equals(gw_type)){
			psql.append(" and gw_type =2");
		}
		else{
			psql.append(" and gw_type !=2");
		}

		//安徽联通 选择条件增加 主被动模式选择
		if(!StringUtil.IsEmpty(mode)){
			psql.append(" and task_model = "+mode);
		}

		if (null != task_name_query && !"".equals(task_name_query.trim())) {
			psql.append(" and task_name like '%"+task_name_query+"%'");
		}
		if (-1 != status_query) {
			psql.append(" and status = "+status_query);
		}
		if (-1 != expire_time_start) {
			psql.append(" and set_time >= "+expire_time_start);
		}
		if (-1 != expire_time_end) {
			psql.append(" and set_time <= "+expire_time_end);
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
	 * 设备列表
	 * @param task_id
	 * @param city_id
	 * @param type
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> queryDevList4CQ(String task_id, String city_id, String type,
			int curPage_splitPage, int num_splitPage)
	{
		List<Map> list = new ArrayList<Map>();

		String strategyTab = "gw_serv_strategy_soft" ;
		if(LipossGlobals.inArea(Global.YNLT)){
			strategyTab = "gw_serv_strategy";
		}
		if(null != task_id || "".equals(task_id)){
			PrepareSQL psql = new PrepareSQL();
			psql.append("select d.vendor_id,d.city_id,d.device_model_id,d.device_serialnumber,");
			psql.append("d.devicetype_id,s.result_id,s.status,s.start_time,s.end_time ");
			psql.append("from "+strategyTab+" s,tab_gw_device d where d.device_id=s.device_id and s.task_id = '");
			psql.append(task_id+"'");
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
			
			if(LipossGlobals.inArea(Global.YNLT)){
				psql.append(" and s.service_id=5");
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
					//修复问题：新增的软件信息在软件升级管理界面不展示问题。未从softVersionMap中获取到对应的version时，更新下内存
					if(StringUtil.IsEmpty(version))
					{
						DeviceTypeUtil.init();  //更新下内存
						version = DeviceTypeUtil.softVersionMap.get(devicetype_id); //再取一次
					}
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
	
	
	public static void main(String[] args)
	{
		System.out.println(StringUtil.getIntegerValue(null));
	}
	/**
	 * 删除任务表和策略表
	 * @param task_id
	 * @return
	 */
	public int del(String task_id)
	{
		String strategyTab = "gw_serv_strategy_soft" ;
		if(LipossGlobals.inArea(Global.YNLT)){
			strategyTab = "gw_serv_strategy";
		}
		int res = 1;
		String[] sqls = new String[2];
		sqls[0] = "delete from tab_softup_task where task_id='"+task_id+"'";
		sqls[1] = "delete from " + strategyTab + " where task_id='"+task_id+"'";//删除未作的策略
		
		if(LipossGlobals.inArea(Global.YNLT)){
			sqls[1] = sqls[1] + " and service_id=5";
		}
		try{
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
		String strategyTab = "gw_serv_strategy_soft" ;
		if(LipossGlobals.inArea(Global.YNLT)){
			strategyTab = "gw_serv_strategy";
		}
		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(*) from " + strategyTab + " where task_id='"+task_id+"'");//删除未作的策略
		
		if(LipossGlobals.inArea(Global.YNLT)){
			psql.append(" and service_id=5");
		}
		int total = jt.queryForInt(psql.getSQL());
		return total;
	}
	
	/**
	 * 恢复/暂停
	 * @param task_id
	 * @return
	 */
	public int update(String task_id,String type)
	{
		int res = 1;
		String[] sqls = new String[2];
		
		String strategyTab = "gw_serv_strategy_soft" ;
		if(LipossGlobals.inArea(Global.YNLT)){
			strategyTab = "gw_serv_strategy";
		}
		//当前状态：1为正常，2为暂停
		if("1".equals(type)){
			sqls[0] = "update tab_softup_task set status=2 where task_id='"+task_id+"'";
			sqls[1] = "update " + strategyTab + " set status=7 where status=0 and task_id='"+task_id+"'";//删除未作的策略
			if(LipossGlobals.inArea(Global.YNLT)){
				sqls[1] = sqls[1] + " and service_id=5";
			}
		}
		else{
			sqls[0] = "update tab_softup_task set status=1 where task_id='"+task_id+"'";
			sqls[1] = "update " + strategyTab + " set status=0 where status=7 and task_id='"+task_id+"'";//删除未作的策略
			if(LipossGlobals.inArea(Global.YNLT)){
				sqls[1] = sqls[1] + " and service_id=5";
			}
		}
		
		try{
			jt.batchUpdate(sqls);
		}
		catch (Exception e) {
			e.printStackTrace();
			res = 0;
		}
		return res;
	}
	
	public int updateCount(String task_id,String type)
	{
		String strategyTab = "gw_serv_strategy_soft";
		if(LipossGlobals.inArea(Global.YNLT)){
			strategyTab = "gw_serv_strategy";
		}
		PrepareSQL psql = new PrepareSQL();
		if("1".equals(type)){
			psql.append("select count(*) from " + strategyTab + " where status=0 and task_id='"+task_id+"'");//删除未作的策略
		}
		else{
			psql.append("select count(*) from " + strategyTab + " where status=7 and task_id='"+task_id+"'");//删除未作的策略
		}
		
		if(LipossGlobals.inArea(Global.YNLT)){
			psql.append(" and service_id=5");
		}
		int total = jt.queryForInt(psql.getSQL());
		return total;
	}
	
	public Map continueIds(String task_id)
	{
		PrepareSQL psql = new PrepareSQL();
		
		if(LipossGlobals.inArea(Global.YNLT)){
			psql.append("select device_id from gw_serv_strategy where status=7 and service_id=5 and task_id='"+task_id+"'");//删除未作的策略
		}else{
			psql.append("select device_id from gw_serv_strategy_soft where task_id='"+task_id+"'");//删除未作的策略
		}
		
		Map<String,String> map = DBOperation.getRecord(psql.getSQL());
		return map;
	}
	
	
	public int updateStatus(int status,String taskName)
	{
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("update tab_time_softup_task set status = ");
		pSQL.append(status +" where task_name = '"+taskName+"'");
		int total = jt.update(pSQL.getSQL());
		return total;
	}
	
	

	public int repeatTaskName(String taskName)
	{
		PrepareSQL pSQL = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			pSQL.append("select count(*) ");
		}else{
			pSQL.append("select count(1) ");
		}
		pSQL.append("from tab_time_softup_task where task_name='"+taskName+"'");
		return jt.queryForInt(pSQL.getSQL());
	}

	public int addSoftwareUp(BatchSoftUpBean bsuBean)
	{
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("insert into tab_time_softup_task(task_id,task_name,acc_oid,");
		pSQL.append("add_time,service_id,up_begin,up_end,expire_time,status) values (?,?,?,?,?,?,?,?,?) ");
		pSQL.setLong(1, Long.parseLong(bsuBean.getTask_id()));
		pSQL.setString(2, bsuBean.getTask_name());
		pSQL.setLong(3, Long.parseLong(bsuBean.getAcc_oid()));
		pSQL.setLong(4, Long.parseLong(bsuBean.getAdd_time()));
		pSQL.setInt(5, Integer.parseInt(bsuBean.getService_id()));
		pSQL.setInt(6, Integer.parseInt(bsuBean.getUp_begin()));
		pSQL.setInt(7, Integer.parseInt(bsuBean.getUp_end()));
		pSQL.setLong(8, Long.parseLong(bsuBean.getExpire_t()));
		pSQL.setInt(9, Integer.valueOf(bsuBean.getStatus()));

		int num = this.jt.update(pSQL.getSQL());
		return num;
	}
	
	public String addSoftwareUp(long task_id,String device_id,int devicetype_id_old,int status)
	{
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("insert into tab_time_softup_task_dev(task_id,device_id,devicetype_id_old,status) values (?,?,?,?)");
		pSQL.setLong(1, task_id);
		pSQL.setString(2, device_id);
		pSQL.setInt(3, devicetype_id_old);
		pSQL.setInt(4, status);
		return pSQL.getSQL();
	}
	
	public int recordDev(ArrayList<String> devSqlList) {
		return DBOperation.executeUpdate(devSqlList);
	}
}
