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
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gtms.stb.utils.DeviceTypeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * 
 * @author zzd (Ailk No.)
 * @version 1.0
 * @since 2016-10-13
 * @category com.linkage.module.gwms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class BatchSetTempManaDAO extends SuperDAO
{
	private static final Logger logger = LoggerFactory.getLogger(BatchSetTempManaDAO.class);

	public List<Map> queryTaskList(String task_name_query, int status_query,
			long expire_time_start, long expire_time_end,int curPage_splitPage,
			int num_splitPage,String gw_type) {

		PrepareSQL psql = new PrepareSQL();
		psql.append("select t.*,temp.name template_name from stb_tab_batchSetTemplate_task t,tab_serv_template temp");
		psql.append(" where temp.id=t.template_id ");
		if (null != task_name_query && !"".equals(task_name_query.trim())) {
			psql.append(" and t.task_name like '%"+task_name_query+"%'");
		}
		if (-1 != status_query) {
			psql.append(" and t.TASK_STATUS = "+status_query);
		}
		if (-1 != expire_time_start) {
			psql.append(" and t.ADD_TIME >= "+expire_time_start);
		}
		if (-1 != expire_time_end) {
			psql.append(" and t.ADD_TIME <= "+expire_time_end);
		}
		psql.append(" order by t.ADD_TIME desc");
		
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

				if(!StringUtil.IsEmpty(taskId)){
					
					String strategyTab = "stb_tab_batchSetTemplate_dev";
					
					PrepareSQL psql = new PrepareSQL();
					psql.append("select count(1) from " + strategyTab + " where status = 0 and task_id = '");
					psql.append(taskId);
					psql.append("'");
					unDoneNum = jt.queryForLong(psql.getSQL());
					
					PrepareSQL psqlb = new PrepareSQL();
					psqlb.append("select count(1) from  " + strategyTab + "  where status = 1 and task_id = '");
					psqlb.append(taskId);
					psqlb.append("'"); 
					succNum = jt.queryForLong(psqlb.getSQL());
					
					PrepareSQL psqla = new PrepareSQL();
					psqla.append("select count(1) from  " + strategyTab + "  where task_id = '");
					psqla.append(taskId);
					psqla.append("'");
					totalNum = jt.queryForLong(psqla.getSQL());

					failNum = totalNum-unDoneNum-succNum;

				}
				
				map.put("task_id", taskId);
				map.put("totalNum", totalNum+"");
				map.put("succNum", succNum+"");
				map.put("failNum", failNum+"");
				map.put("unDoneNum", unDoneNum+"");
				String status = rs.getString("task_status");
				if("2".equals(status)){
					map.put("status", "暂停");
				}else{
					map.put("status", "正常");
				}
				map.put("status", status);
				
				String template_name = rs.getString("template_name");
				map.put("template_name", template_name);

				String add_time = rs.getString("add_time");
				String date = new DateTimeUtil(StringUtil.getLongValue(add_time)*1000).getLongDate();
				map.put("add_time", date);
					
				return map;
			}
		});
		return list;

	}
	
	
	public Map queryTaskById(String taskId) 
	{
		Map<String,String> map=new HashMap<String,String>();
		String table="stb_tab_batchSetTemplate_task";
		
		PrepareSQL sql = new PrepareSQL();
		sql.append("select t.DONOW,t.TASK_NAME,t.ADD_TIME,t.QUERYTYPE,t.CITY_ID,t.VENDOR_ID,t.DEVICE_MODEL_ID,t.DEVICETYPE_ID,t.ISBIND,t.FILE_NAME,t.TYPE,t.START_TIME,t.END_TIME,a.acc_loginname,temp.NAME,temp.id from tab_serv_template temp,stb_tab_batchSetTemplate_task t left join tab_accounts a on a.acc_oid=t.acc_oid where t.TEMPLATE_ID=temp.id and t.task_id=? ");
		
		sql.setLong(1, Long.parseLong(taskId));
		List list = jt.queryForList(sql.getSQL());
		if (list!=null &&list.size()>0 ) {
			 map=(Map) list.get(0);
		}
		
		String vendor_id = map.get("vendor_id");
		if(null!=DeviceTypeUtil.vendorMap && !StringUtil.IsEmpty(vendor_id) && !StringUtil.IsEmpty(DeviceTypeUtil.vendorMap.get(vendor_id))){
			String vendor = DeviceTypeUtil.vendorMap.get(vendor_id);
			map.put("vendor_id", vendor);
		}
		else{
			DeviceTypeUtil.getVendorName();
			String vendor = DeviceTypeUtil.vendorMap.get(vendor_id);
			map.put("vendor_id", vendor);
		}
		
		String device_model_id = map.get("device_model_id");
		if(null!=DeviceTypeUtil.deviceModelMap && !StringUtil.IsEmpty(device_model_id) && !StringUtil.IsEmpty(DeviceTypeUtil.deviceModelMap.get(device_model_id))){
			String model = DeviceTypeUtil.deviceModelMap.get(device_model_id);
			map.put("device_model_id", model);
		}
		else{
			DeviceTypeUtil.getDeviceModel();
			String model = DeviceTypeUtil.deviceModelMap.get(device_model_id);
			map.put("device_model_id", model);
		}
		
		String devicetype_id = StringUtil.getStringValue(map.get("devicetype_id"));
		if(null!=DeviceTypeUtil.devTypeMap && !StringUtil.IsEmpty(devicetype_id) && !StringUtil.IsEmpty(DeviceTypeUtil.devTypeMap.get(devicetype_id))){
			String version = DeviceTypeUtil.devTypeMap.get(devicetype_id);
			map.put("version", version);
		}
		else{
			DeviceTypeUtil.getDeviceSoftVersion();
			String version = DeviceTypeUtil.devTypeMap.get(devicetype_id);
			map.put("version", version);
		}
		
		String city_id = map.get("city_id");
		logger.warn("city_id="+city_id);
		
		if(null!=Global.G_CityId_CityName_Map && !StringUtil.IsEmpty(city_id)){
			map.put("city_name", Global.G_CityId_CityName_Map.get(city_id));
		}
		else{
			map.put("city_name","");
		}
		
		String isbind = map.get("isbind");
		if("0".equals(isbind)){
			map.put("isbind", "否");
		}
		else if("1".equals(isbind)){
			map.put("isbind", "是");
		}
		else{
			map.put("isbind", "");
		}
		
		Object objects = map.get("start_time");
		Object objecte = map.get("end_time");
		if(!StringUtil.IsEmpty(StringUtil.getStringValue(objects))){
			long start = StringUtil.getLongValue(objects);
			//map.put("start_time", new DateTimeUtil(start*1000-3600000*8).getTime());
			map.put("start_time", new DateTimeUtil(start*1000).getLongDate().substring(11));
			map.put("start_date", new DateTimeUtil(start*1000).getLongDate().substring(0, 10));
		}
		if(!StringUtil.IsEmpty(StringUtil.getStringValue(objecte))){
			long end = StringUtil.getLongValue(objecte);
			//map.put("end_time", new DateTimeUtil(end*1000-3600000*8).getTime());
			map.put("end_time", new DateTimeUtil(end*1000).getLongDate().substring(11));
			map.put("end_date", new DateTimeUtil(end*1000).getLongDate().substring(0, 10));
		}
		
		Object object = map.get("type");
		String informNum  = StringUtil.getStringValue(object);
		StringBuffer type = new StringBuffer();
		for(int i=0;i<informNum.length();i++){
			if(!StringUtil.IsEmpty(type.toString()) && !",".equals(type.toString().substring(type.toString().length()-1))){
				type.append(",");
			}
			type.append(getInformDesc(informNum.substring(i, i+1)));
		}

		if(StringUtil.getStringValue(map, "donow", "").equals("1")){
			type.append(",").append("主动触发");
		}
		map.put("type", type.toString().replace(",", " "));
		
		map.put("add_time", new DateTimeUtil(StringUtil.getLongValue(map.get("add_time"))*1000).getLongDate());
		
		return map;
	}
	
	
	String getInformDesc(String num){
		Map<String,String> map = new HashMap<String,String>();
		map.put("2", "周期上报");
		map.put("5", "重新启动");
		map.put("6", "参数改变");
		map.put("4", "下次连接到系统");
		if(null == map.get(num)) return "";
		return map.get(num);
	}
	
	public static void main(String[] args)
	{
		String informNum = "253";
		StringBuffer type = new StringBuffer();
		for(int i=0;i<informNum.length();i++){
			if(!StringUtil.IsEmpty(type.toString()) && !",".equals(type.toString().substring(type.toString().length()-1))){
				type.append(",");
			}
			type.append(new BatchSetTempManaDAO().getInformDesc(informNum.substring(i, i+1)));
		}
		System.out.println(type.toString().replace(",", " "));
	}
	
	public int queryTaskCount(String task_name_query, int status_query,
			long expire_time_start, long expire_time_end, int curPage_splitPage,
			int num_splitPage,String gw_type) {
		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(*) from stb_tab_batchSetTemplate_task t,tab_serv_template temp");
		psql.append(" where temp.id=t.template_id ");
		
		if (null != task_name_query && !"".equals(task_name_query.trim())) {
			psql.append(" and t.task_name like '%"+task_name_query+"%'");
		}
		if (-1 != status_query) {
			psql.append(" and t.TASK_STATUS = "+status_query);
		}
		if (-1 != expire_time_start) {
			psql.append(" and t.ADD_TIME >= "+expire_time_start);
		}
		if (-1 != expire_time_end) {
			psql.append(" and t.ADD_TIME <= "+expire_time_end);
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
	public List<Map> queryDevList(String task_id, String type, int curPage_splitPage, int num_splitPage)
	{
		List<Map> list = new ArrayList<Map>();

		String strategyTab = "stb_tab_batchSetTemplate_dev" ;
		if(null != task_id || "".equals(task_id)){
			PrepareSQL psql = new PrepareSQL();
			psql.append("select d.vendor_id,d.city_id,d.device_model_id,d.oui,d.loopback_ip,d.device_serialnumber,d.devicetype_id,s.status from "+strategyTab+" s,stb_tab_gw_device d where d.device_id=s.device_id and s.task_id = '");
			psql.append(task_id);
			psql.append("'");
			if("unDone".equals(type)){
				psql.append(" and (s.status =0) ");
			}
			else if("succ".equals(type)){
				psql.append(" and s.status=1 ");
			}
			else if("fail".equals(type)){
				psql.append(" and (s.status!=1 and s.status != 0) ");
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
				if(null!=DeviceTypeUtil.vendorMap && !StringUtil.IsEmpty(vendor_id) && !StringUtil.IsEmpty(DeviceTypeUtil.vendorMap.get(vendor_id))){
					String vendor = DeviceTypeUtil.vendorMap.get(vendor_id);
					map.put("vendor_id", vendor);
				}
				else{
					DeviceTypeUtil.getVendorName();
					String vendor = DeviceTypeUtil.vendorMap.get(vendor_id);
					map.put("vendor_id", vendor);
				}
				
				String device_model_id = map.get("device_model_id");
				if(null!=DeviceTypeUtil.deviceModelMap && !StringUtil.IsEmpty(device_model_id) && !StringUtil.IsEmpty(DeviceTypeUtil.deviceModelMap.get(device_model_id))){
					String model = DeviceTypeUtil.deviceModelMap.get(device_model_id);
					map.put("device_model_id", model);
				}
				else{
					DeviceTypeUtil.getDeviceModel();
					String model = DeviceTypeUtil.deviceModelMap.get(device_model_id);
					map.put("device_model_id", model);
				}
				
				String devicetype_id = StringUtil.getStringValue(map.get("devicetype_id"));
				if(null!=DeviceTypeUtil.devTypeMap && !StringUtil.IsEmpty(devicetype_id) && !StringUtil.IsEmpty(DeviceTypeUtil.devTypeMap.get(devicetype_id))){
					String version = DeviceTypeUtil.devTypeMap.get(devicetype_id);
					map.put("version", version);
				}
				else{
					DeviceTypeUtil.getDeviceSoftVersion();
					String version = DeviceTypeUtil.devTypeMap.get(devicetype_id);
					map.put("version", version);
				}
				
				String city_id = map.get("city_id");
				if(null!=Global.G_CityId_CityName_Map){
					map.put("city_name", Global.G_CityId_CityName_Map.get(city_id));
				}
				
				int status = StringUtil.getIntegerValue(map.get("status"));
				if(status==0){
					map.put("status", "未做");
				}
				else if(status==1){
					map.put("status", "成功");
				}
				else if(null!=Global.G_Fault_Map && null!=Global.G_Fault_Map.get(status) && !StringUtil.IsEmpty(Global.G_Fault_Map.get(status).getFaultDesc())){
					map.put("status", Global.G_Fault_Map.get(status).getFaultDesc());
				}
			}
		}
		return list;
	}
	
	
	/**
	 * 删除任务表和策略表
	 * @param task_id
	 * @return
	 */
	public int del(String task_id)
	{
		int res = 1;
		String[] sqls = new String[3];
		sqls[0] = "delete from stb_tab_batchSetTemplate_task where task_id="+task_id;
		sqls[1] = "delete from stb_tab_batchSetTemplate_dev where task_id="+task_id;
		sqls[2] = "delete from stb_gw_strategy_batch where task_id='"+task_id+"'";
		
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
		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(*) from stb_tab_batchSetTemplate_dev where task_id='"+task_id+"'");//删除未作的策略
		
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
		
		String strategyTab = "stb_gw_strategy_batch" ;
		
		//当前状态：1为正常，2为暂停
		if("1".equals(type)){
			sqls[0] = "update stb_tab_batchSetTemplate_task set task_status=2 where task_id="+task_id;
			sqls[1] = "update " + strategyTab + " set status=7 where status=0 and task_id='"+task_id+"'";//删除未作的策略
			if(LipossGlobals.inArea("yn_lt")){
				sqls[1] = sqls[1] + " and service_id=5";
			}
		}
		else{
			sqls[0] = "update stb_tab_batchSetTemplate_task set task_status=1 where task_id="+task_id;
			sqls[1] = "update " + strategyTab + " set status=0 where status=7 and task_id='"+task_id+"'";//删除未作的策略
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
		String strategyTab = "stb_gw_strategy_batch";
		PrepareSQL psql = new PrepareSQL();
		if("1".equals(type)){
			psql.append("select count(*) from " + strategyTab + " where status=0 and task_id='"+task_id+"'");
		}
		else{
			psql.append("select count(*) from " + strategyTab + " where status=7 and task_id='"+task_id+"'");
		}
		
		if(LipossGlobals.inArea("yn_lt")){
			psql.append(" and service_id=5");
		}
		int total = jt.queryForInt(psql.getSQL());
		return total;
	}
	
	public ArrayList<HashMap<String, String>> continueIds(String task_id)
	{
		PrepareSQL psql = new PrepareSQL();
		
		psql.append("select * from stb_gw_strategy_batch where status=0 and task_id='"+task_id+"'");//删除未作的策略
		
		ArrayList<HashMap<String, String>> list = DBOperation.getRecords(psql.getSQL());
		return list;
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
		pSQL.append("select count(1) from tab_time_softup_task where task_name = '");
		pSQL.append(taskName+"'");
		int total = jt.queryForInt(pSQL.getSQL());
		return total;
	}
	
	public int recordDev(ArrayList<String> devSqlList) {
		return DBOperation.executeUpdate(devSqlList);
	}
}
