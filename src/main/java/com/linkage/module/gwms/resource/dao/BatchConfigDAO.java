
package com.linkage.module.gwms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.itms.resource.util.DeviceTypeUtil;

public class BatchConfigDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(BatchConfigDAO.class);

	

	public void deleteTask(String taskId) {
		String sql ="delete from tab_soft_task where task_id ="+taskId;
		PrepareSQL psql = new PrepareSQL(sql);
		jt.update(psql.getSQL());
	}

	public void insertTask(String mode,List<Map<String,String>> devList,String paramValue,
			String paramPath,String paramType,
			long time, String task_id, String task_name, String id) throws Exception
	{
		List<String> sqlL = new ArrayList<String>();
		int index = 1;
		for(int i=0;i<devList.size();i++){
			String sql1 = "delete from tab_batch_result_telnet  where device_id='"+devList.get(i).get("device_id")+"'";
			String sql0 = "insert into tab_batch_result_telnet(device_id,task_id,status,addtime) values ('"
							+devList.get(i).get("device_id")+"','"+task_id+"',"+7+","+time+")";
			sqlL.add(sql1);
			sqlL.add(sql0);
			if(index>=500){
				String[] strings = new String[sqlL.size()];
				sqlL.toArray(strings);
				jt.batchUpdate(strings);
				sqlL = new ArrayList<String>();
				index = 1;
			}
			index = index +2;
		}
		if(sqlL.size()>0){
			String[] strings = new String[sqlL.size()];
			sqlL.toArray(strings);
			jt.batchUpdate(strings);
		}
		logger.warn("插入任务明细tab_batch_result_telnet结束！");
		
		String sql ="insert into tab_batchconfig_task(task_id,task_name,status,addtime,updatetime,operator,"
					+ "ParamPath,ParamValue,ParamType,set_strategy) values (?,?,?,?,?,?,?,?,?,?)";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setString(1, task_id);
		psql.setString(2, task_name);
		psql.setInt(3, 7);
		psql.setLong(4, time);
		psql.setLong(5, time);
		psql.setString(6, id);
		psql.setString(7, paramPath);
		psql.setString(8, paramValue);
		psql.setString(9, paramType);
		psql.setString(10, mode);
		jt.update(psql.getSQL());
		logger.warn("插入任务表tab_batchconfig_task结束！");
	}
	
	
	/**
	 * 根据查询条件得到需要定制的设备
	 */
	public List<Map<String,String>> queryDevice(String devicetypeId,String deviceModelId,
			String vendorId,String cityId, String startOpenDate, String endOpenDate) 
	{
		logger.debug("queryDevice({})", new Object[]{devicetypeId, deviceModelId, 
				vendorId,cityId,startOpenDate,endOpenDate});
		List<Map<String,String>> list;
		StringBuffer sb = new StringBuffer();
		sb.append("select d.device_id from tab_gw_device d " +
				"inner join tab_devicetype_info t on d.devicetype_id=t.devicetype_id " +
				" where 1=1 ");
		if(!StringUtil.IsEmpty(vendorId)&&!"-1".equals(vendorId)){
			sb.append(" and t.vendor_id='").append(vendorId).append("' ");
		}
		
		if(!StringUtil.IsEmpty(deviceModelId)&&!"-1".equals(deviceModelId)){
			sb.append(" and t.device_model_id='").append(deviceModelId).append("' ");
		}
		
		/*if(!StringUtil.IsEmpty(devicetypeId)&&!"-1".equals(devicetypeId)){
			sb.append(" and t.softwareversion='").append(devicetypeId).append("' "); yaoli注释
		}*/
		if(!StringUtil.IsEmpty(devicetypeId)&&!"-1".equals(devicetypeId)){
			sb.append(" and t.devicetype_id in (").append(devicetypeId).append(") "); 
		}
		
		if( null!=cityId &&!"-1".equals(cityId)&&!"00".equals(cityId)) {
			sb.append(" and d.city_id in (" + cityId + ")");
		}
		
		if (!StringUtil.IsEmpty(startOpenDate))
		{
			sb.append(" and d.complete_time>=").append(dealTime(startOpenDate));
		}
		if (false == StringUtil.IsEmpty(endOpenDate))
		{
			sb.append(" and d.complete_time<=").append(dealTime(endOpenDate));
		}

		PrepareSQL psql = new PrepareSQL(sb.toString());
		return jt.queryForList(psql.getSQL());
	}
	
	
	public static void main(String[] args)
	{
		System.out.println("InternetGatewayDevice.Services.X_CT-COM_MWBAND.TotalTerminalNumber,1,1".replace(",", ",\n"));
		
	}
	/**
	 * 管理页面初查任务
	 * @returns
	 */
	public List<Map> queryTaskList4CQ(String task_name_query, int status_query,
			long expire_time_start, long expire_time_end,int curPage_splitPage,
			int num_splitPage) 
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select task_id,task_name,parampath,paramvalue,paramtype,status,addtime ");
		}else{
			psql.append("select * ");
		}
		psql.append("select * from tab_batchconfig_task");
		psql.append(" where 1=1 ");
		if (!StringUtil.IsEmpty(task_name_query.trim())) {
			psql.append(" and task_name ='"+task_name_query+"'");
		}
		if (-1 != status_query) {
			psql.append(" and status = "+status_query);
		}
		if (-1 != expire_time_start) {
			psql.append(" and addtime >= "+expire_time_start);
		}
		if (-1 != expire_time_end) {
			psql.append(" and addtime <= "+expire_time_end);
		}
		psql.append(" order by addtime desc");
		
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
				map.put("parampath", rs.getString("parampath").replace(",", ",\n"));
				map.put("parampaths", rs.getString("parampath").length()>30 ? 
						rs.getString("parampath").substring(0,30)+"...":rs.getString("parampath"));
				map.put("paramvalue", rs.getString("paramvalue").replace(",", ",\n"));
				map.put("paramvalues", rs.getString("paramvalue").length()>10 ? 
						rs.getString("paramvalue").substring(0,10)+"...":rs.getString("paramvalue"));
				String type = rs.getString("paramtype");
				type = type.replaceAll("1", "string");
				type = type.replaceAll("2", "int");
				type = type.replaceAll("3", "unsignedInt");
				type = type.replaceAll("4", "boolean");
				map.put("paramtype", type.replace(",", ",\n"));
				map.put("paramtypes", type.length()>10?type.substring(0,10)+"...":type);
				String taskId = rs.getString("task_id");

				if(!StringUtil.IsEmpty(taskId))
				{
					PrepareSQL psql = new PrepareSQL();
					if(DBUtil.GetDB()==3){
						psql.append("select count(*) ");
					}else{
						psql.append("select count(1) ");
					}
					psql.append("from tab_batch_result_telnet where (status=0 or status=7) and task_id=? ");
					psql.setString(1, taskId);
					unDoneNum = jt.queryForLong(psql.getSQL());
					
					PrepareSQL psqlb = new PrepareSQL();
					if(DBUtil.GetDB()==3){
						psqlb.append("select count(*) ");
					}else{
						psqlb.append("select count(1) ");
					}
					psqlb.append("from tab_batch_result_telnet where status=1 and task_id=? ");
					psqlb.setString(1, taskId);
					succNum = jt.queryForLong(psqlb.getSQL());
					
					PrepareSQL psqla = new PrepareSQL();
					if(DBUtil.GetDB()==3){
						psqla.append("select count(*) ");
					}else{
						psqla.append("select count(1) ");
					}
					psqla.append("from tab_batch_result_telnet where task_id=? ");
					psqla.setString(1, taskId);
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

				String set_time = rs.getString("addtime");
				String date = new DateTimeUtil(StringUtil.getLongValue(set_time)*1000).getLongDate();
				map.put("settime", date);
					
				return map;
			}
		});
		return list;

	}
	
	public int queryTaskCount4CQ(String task_name_query, int status_query,
			long expire_time_start, long expire_time_end, int curPage_splitPage,
			int num_splitPage) 
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(*) from tab_batchconfig_task ");
		
		psql.append("where 1=1 ");
		if (!StringUtil.IsEmpty(task_name_query.trim())) {
			psql.append(" and task_name ='"+task_name_query+"'");
		}
		if (-1 != status_query) {
			psql.append(" and status = "+status_query);
		}
		if (-1 != expire_time_start) {
			psql.append(" and addtime >= "+expire_time_start);
		}
		if (-1 != expire_time_end) {
			psql.append(" and addtime <= "+expire_time_end);
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
	public List<Map> queryTaskGyCityList(String task_id,String task_name,int curPage_splitPage, int num_splitPage) 
	{
		List<Map> tList = new ArrayList<Map>();
		List<Map<String,String>> sList = new ArrayList<Map<String,String>>();
		List<Map<String,String>> uList = new ArrayList<Map<String,String>>();
		long succNum = 0;
		long failNum = 0;
		long totalNum = 0;
		long unDoneNum = 0;

		if(!StringUtil.IsEmpty(task_id))
		{
			PrepareSQL psql = new PrepareSQL();
			if(DBUtil.GetDB()==3){
				psql.append("select count(*) num,d.city_id ");
			}else{
				psql.append("select count(1) num,d.city_id ");
			}
			psql.append("from tab_batch_result_telnet s,tab_gw_device d where d.device_id=s.device_id and (s.status = 0 or s.status = 7) and s.task_id = '");
			psql.append(task_id);
			psql.append("' group by d.city_id");
			uList = jt.queryForList(psql.getSQL());
			
			PrepareSQL psqlb = new PrepareSQL();
			if(DBUtil.GetDB()==3){
				psqlb.append("select count(*) num,d.city_id ");
			}else{
				psqlb.append("select count(1) num,d.city_id ");
			}
			psqlb.append("from tab_batch_result_telnet s,tab_gw_device d where d.device_id=s.device_id and s.status = 1 and s.task_id = '");
			psqlb.append(task_id);
			psqlb.append("' group by d.city_id");
			sList = jt.queryForList(psqlb.getSQL());
			
			PrepareSQL psqla = new PrepareSQL();
			if(DBUtil.GetDB()==3){
				psqla.append("select count(*) num,d.city_id ");
			}else{
				psqla.append("select count(1) num,d.city_id ");
			}
			psqla.append("from tab_batch_result_telnet s,tab_gw_device d where d.device_id=s.device_id and s.task_id = '");
			psqla.append(task_id);
			psqla.append("' group by d.city_id order by d.city_id");
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

	
	public int queryTaskGyCityCount(String task_id) 
	{
		int total = 0;
		if(!StringUtil.IsEmpty(task_id)){
			PrepareSQL psqla = new PrepareSQL();
			if(DBUtil.GetDB()==3){
				psqla.append("select count(x.num) from (select count(*) num,d.city_id ");
			}else{
				psqla.append("select count(x.num) from (select count(1) num,d.city_id ");
			}
			psqla.append("from tab_batch_result_telnet s,tab_gw_device d ");
			psqla.append("where d.device_id=s.device_id and s.task_id='"+task_id);
			psqla.append("' group by d.city_id) x");
			total = jt.queryForInt(psqla.getSQL());
		}
		
		return total;
	}

	
	public List<Map> queryDevList4CQ(String task_id, String city_id, String type,
			int curPage_splitPage, int num_splitPage)
	{
		List<Map> list = new ArrayList<Map>();

		if(!StringUtil.IsEmpty(task_id))
		{
			PrepareSQL psql = new PrepareSQL();
			psql.append("select d.vendor_id,d.city_id,d.device_model_id,");
			psql.append("d.device_serialnumber,d.devicetype_id,s.status,s.settime ");
			psql.append("from tab_batch_result_telnet s,tab_gw_device d ");
			psql.append("where d.device_id=s.device_id and s.task_id='"+task_id+"'");
			if(!StringUtil.IsEmpty(city_id)){
				psql.append(" and d.city_id='"+city_id+"'");
			}
			
			if("unDone".equals(type)){
				psql.append(" and (s.status = 0 or s.status = 7) ");
			}else if("succ".equals(type)){
				psql.append(" and s.status=1 ");
			}else if("fail".equals(type)){
				psql.append(" and (s.status!=1 and s.status != 0 and s.status != 7) ");
			}
			psql.append(" order by s.status desc,s.settime desc");
			
			//不传分页信息查询全部
			if(curPage_splitPage==0 && num_splitPage==0){
				list = jt.queryForList(psql.getSQL());
			}else{
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
				
				long start_time = StringUtil.getLongValue(map.get("settime"));
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
		int res = 1;
		String[] sqls = new String[2];
		//当前状态：1为正常，7为暂停
		if("1".equals(type)){
			sqls[0] = "update tab_batchconfig_task set status=7 where task_id='"+task_id+"'";
			sqls[1] = "update tab_batch_result_telnet set status=7 where status=0 and task_id='"+task_id+"'";//删除未作的策略
		}
		else{
			sqls[0] = "update tab_batchconfig_task set status=1 where task_id='"+task_id+"'";
			sqls[1] = "update tab_batch_result_telnet set status=0 where status=7 and task_id='"+task_id+"'";//删除未作的策略
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
		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(*) from tab_batch_result_telnet ");
		if("1".equals(type)){
			psql.append("where status=0 and task_id='"+task_id+"'");//删除未作的策略
		}else{
			psql.append("where status=7 and task_id='"+task_id+"'");//删除未作的策略
		}
		
		return jt.queryForInt(psql.getSQL());
	}
	
	
	public int del(String task_id)
	{
		int res = 1;
		String[] sqls = new String[2];
		sqls[0] = "delete from tab_batchconfig_task where task_id='"+task_id+"'";
		sqls[1] = "delete from tab_batch_result_telnet where task_id='"+task_id+"'";//删除未作的策略
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
		psql.append("select count(*) from tab_batch_result_telnet where task_id='"+task_id+"'");//删除未作的策略
		
		return jt.queryForInt(psql.getSQL());
	}
	
	
	public long dealTime(String time) {
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date str = new Date();
		try {
			str = date.parse(time);
		}
		catch (ParseException e) {
			logger.warn("选择开始或者结束的时间格式不对:" + time);
		}

		return str.getTime() / 1000L;
	}
}
