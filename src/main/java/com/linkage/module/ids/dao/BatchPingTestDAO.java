package com.linkage.module.ids.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DBOperation;
import com.linkage.litms.uss.DateUtil;
import com.linkage.commons.db.DBUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class BatchPingTestDAO extends SuperDAO 
{
	private Logger logger = LoggerFactory.getLogger(BatchPingTestDAO.class);

	/**
	 * 增加任务信息
	 */
	public int addTaskInfo(String taskname, String taskid, long accoid,
			String addtime, String starttime, String endtime, String enddate,
			String pingtype, String packetsize, String packetnum,String timeout, String url) 
	{
		PrepareSQL psql = new PrepareSQL();
		StringBuffer sql = new StringBuffer();
		sql.append("insert into tab_ids_task (task_name,task_id,acc_oid,add_time," +
				"type_id,start_time,end_time,end_date,run_time,task_statu,url," +
				"packet_size,packet_num,time_out,ping_type) " +
				"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		psql.setSQL(sql.toString());
		psql.setString(1, taskname);
		psql.setString(2, taskid);
		psql.setLong(3, accoid);
		psql.setLong(4, Long.parseLong(addtime));
		psql.setInt(5, 1);
		psql.setLong(6, Long.parseLong(starttime));
		psql.setLong(7, Long.parseLong(endtime));
		psql.setLong(8, Long.parseLong(enddate));
		psql.setString(9, null);
		psql.setInt(10, 0);
		psql.setString(11, url);
		psql.setString(12, packetsize);
		psql.setString(13, packetnum);
		psql.setString(14, timeout);
		psql.setString(15, pingtype);
		int num = jt.update(psql.getSQL());
		return num;
	}

	/**
	 * 任务查询
	 */
	public List<Map> queryTask(String name, String acc_loginname,
			String starttime, String endtime, int curPage_splitPage,int num_splitPage) 
	{
		PrepareSQL psql = new PrepareSQL();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.task_name,b.acc_loginname,a.add_time,a.task_id " +
				"from tab_ids_task a,tab_accounts b " +
				"where a.acc_oid=b.acc_oid  and a.type_id=1 ");
		if (!StringUtil.IsEmpty(name)) {
			sql.append(" and a.task_name like '%" + name + "%'");
		}

		if (!StringUtil.IsEmpty(acc_loginname)) {
			sql.append(" and b.acc_loginname like '%" + acc_loginname + "%'");
		}

		if (!StringUtil.IsEmpty(starttime)) {
			sql.append(" and a.add_time >").append(starttime);
		}

		if (!StringUtil.IsEmpty(endtime)) {
			sql.append(" and a.add_time <").append(endtime);
		}
		sql.append(" order by a.add_time desc");
		psql.setSQL(sql.toString());

		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1)
				* num_splitPage + 1, num_splitPage);

		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				String addtime = String.valueOf(list.get(i).get("add_time"));
				list.get(i).put(
						"add_time",
						DateUtil.transTime(Long.parseLong(addtime),
								"yyyy-MM-dd HH:mm:ss"));
			}
		}

		return list;
	}

	/**
	 * 任务导出
	 */
	public List<Map> queryTaskExcel(String name, String acc_loginname,String starttime, String endtime)
	{
		PrepareSQL psql = new PrepareSQL();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.task_name,b.acc_loginname,a.add_time,a.task_id " +
				"from tab_ids_task a,tab_accounts b " +
				"where a.acc_oid=b.acc_oid  and type_id=1 ");
		if (!StringUtil.IsEmpty(name)) {
			sql.append(" and a.task_name like '%" + name + "%'");
		}

		if (!StringUtil.IsEmpty(acc_loginname)) {
			sql.append(" and b.acc_loginname like '%" + acc_loginname + "%'");
		}

		if (!StringUtil.IsEmpty(starttime)) {
			sql.append(" and a.add_time >").append(starttime);
		}

		if (!StringUtil.IsEmpty(endtime)) {
			sql.append(" and a.add_time <").append(endtime);
		}
		sql.append(" order by a.add_time desc");
		psql.setSQL(sql.toString());

		List<Map> list = jt.queryForList(psql.getSQL());
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				String addtime = String.valueOf(list.get(i).get("add_time"));
				list.get(i).put(
						"add_time",
						DateUtil.transTime(Long.parseLong(addtime),
								"yyyy-MM-dd HH:mm:ss"));
			}
		}

		return list;
	}

	/**
	 * 任务查询(分页)
	 */
	public int queryTaskCount(String name, String acc_loginname,
			String starttime, String endtime, int curPage_splitPage,int num_splitPage)
	{
		PrepareSQL psql = new PrepareSQL();
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		sql.append("from tab_ids_task a,tab_accounts b ");
		sql.append("where a.acc_oid=b.acc_oid and type_id=1 ");
		if (!StringUtil.IsEmpty(name)) {
			sql.append(" and a.task_name like '%" + name + "%'");
		}

		if (!StringUtil.IsEmpty(acc_loginname)) {
			sql.append(" and b.acc_loginname like'%" + acc_loginname + "%'");
		}

		if (!StringUtil.IsEmpty(starttime)) {
			sql.append(" and a.add_time >").append(starttime);
		}

		if (!StringUtil.IsEmpty(endtime)) {
			sql.append(" and a.add_time <").append(endtime);
		}
		psql.setSQL(sql.toString());

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
	 * 获取设备列表
	 */
	public List<Map> getTaskDevList(String taskid, int curPage_splitPage,int num_splitPage) 
	{
		//TODO wait
		PrepareSQL psql = new PrepareSQL();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.task_name,a.task_id,e.acc_loginname,a.device_serialnumber,");
		sql.append("f.city_name,d.vendor_name,g.device_model,c.hardwareversion,c.softwareversion ");
		sql.append("from tab_ids_task_dev a,tab_gw_device b,tab_devicetype_info c,");
		sql.append("tab_vendor d,tab_accounts e,tab_city f,gw_device_model g,tab_ids_task h ");
		sql.append("where a.task_id=h.task_id and a.device_id=b.device_id and b.devicetype_id=c.devicetype_id ");
		sql.append("and b.vendor_id=d.vendor_id and h.acc_oid=e.acc_oid and b.city_id=f.city_id ");
		sql.append("and b.device_model_id=g.device_model_id and h.task_id=? ");
		psql.setSQL(sql.toString());
		psql.setString(1, taskid);
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1)
				* num_splitPage + 1, num_splitPage);
		return list;
	}

	/**
	 * 分页
	 */
	public int getTaskDevCount(String taskid, int curPage_splitPage,int num_splitPage) 
	{
		PrepareSQL psql = new PrepareSQL();
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		
		sql.append("from tab_ids_task_dev a,tab_gw_device b,tab_devicetype_info c,");
		sql.append("tab_vendor d,tab_accounts e,tab_city f,gw_device_model g,tab_ids_task h ");
		sql.append("where a.task_id=h.task_id and a.device_id=b.device_id and b.devicetype_id=c.devicetype_id ");
		sql.append("and b.vendor_id=d.vendor_id and h.acc_oid=e.acc_oid and b.city_id=f.city_id ");
		sql.append("and b.device_model_id=g.device_model_id and h.task_id=? ");
		psql.setSQL(sql.toString());
		psql.setString(1, taskid);
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
	 * 查询任务详细
	 */
	public Map queryTaskInfo(String taskid)
	{
		PrepareSQL psql = new PrepareSQL();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.task_name,b.acc_loginname,a.add_time,a.task_id," +
				"a.start_time,a.end_time,a.end_date,a.url " +
				"from tab_ids_task a,tab_accounts b " +
				"where a.acc_oid=b.acc_oid and task_id = ? ");
		psql.setSQL(sql.toString());
		psql.setString(1, taskid);
		Map map = jt.queryForMap(psql.getSQL());
		if (map != null) {
			map.put("add_time", DateUtil.transTime(
					Long.parseLong(map.get("add_time").toString()),
					"yyyy-MM-dd HH:mm:ss"));
			
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
	 * 根据文件名获取设备信息
	 */
	public List<Map> getTaskDevList(String filename) 
	{
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
	 * 批量插入设备信息
	 */
	public void insertTaskDev(ArrayList<String> sqlList) 
	{
		int res;
		if (LipossGlobals.inArea(Global.JSDX)) {
			res = DBOperation.executeUpdate(sqlList, "proxool.xml-test");
		} else {
			logger.warn("insertTaskDev()");
			res = DBOperation.executeUpdate(sqlList);
		}
	}

	/**
	 * 把设备序列号插入临时表
	 */
	public void insertTmp(String fileName, List<String> dataList) 
	{
		ArrayList<String> sqlList = new ArrayList<String>();
		PrepareSQL psql = null;
		for (int i = 0; i < dataList.size(); i++) {
			psql = new PrepareSQL();
			psql.append("insert into tab_temporary_device"
					+ "(filename,device_serialnumber)" + " values ('"
					+ fileName + "','" + dataList.get(i) + "')");
			sqlList.add(psql.getSQL());
		}
		int res;
		if (LipossGlobals.inArea(Global.JSDX)) {
			res = DBOperation.executeUpdate(sqlList, "proxool.xml-report");
		} else {
			res = DBOperation.executeUpdate(sqlList);
		}
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
	 * 获取Ping结果
	 */
	public List<Map> queryPingResult(String serialnumber, String starttime,
			String endtime, int curPage_splitPage, int num_splitPage)
	{
		PrepareSQL psql = new PrepareSQL();
		StringBuffer sql = new StringBuffer();
		//TODO wait
		sql.append("select a.device_serialnumber,a.test_time,a.success_count," +
				"a.failure_count,a.avg_resp_time,a.min_resp_time," +
				"a.max_resp_time,a.packet_loss_rate,a.TEST_IP,a.WAN_INTERFACE," +
				"c.vendor_name ,e.city_name ,d.device_model " +
				"from tab_ping_diag_result a,tab_gw_device b,tab_vendor c,gw_device_model d,tab_city e " +
				"where a.ping_result is not null and a.device_serialnumber=b.device_serialnumber " +
				"and b.vendor_id=c.vendor_id and b.city_id=e.city_id and b.device_model_id=d.device_model_id");
		if (!StringUtil.IsEmpty(serialnumber)) {
			sql.append(" and a.device_serialnumber='").append(
					serialnumber + "'");
		}

		if (!StringUtil.IsEmpty(starttime)) {
			sql.append(" and a.test_time>").append(starttime);
		}

		if (!StringUtil.IsEmpty(endtime)) {
			sql.append(" and a.test_time<").append(endtime);
		}
		psql.setSQL(sql.toString());
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1)
				* num_splitPage + 1, num_splitPage, new RowMapper() {
					
					@Override
					public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
						Map<String,String> map = new HashMap<String,String>();
						map.put("city_name", rs.getString("city_name"));
						map.put("vendor_name", rs.getString("vendor_name"));
						map.put("device_model", rs.getString("device_model"));
						map.put("device_serialnumber", rs.getString("device_serialnumber"));
						long test_time = rs.getLong("test_time");
						map.put("test_time", DateUtil.transTime(test_time, "yyyy-MM-dd HH:mm:ss"));
						String ping_type = rs.getString("WAN_INTERFACE");
						if(ping_type.equals("1")){
							ping_type="TR069";
						}else if(ping_type.equals("2")){
							ping_type="宽带上网";
						}
						map.put("ping_type", ping_type);
						map.put("success_count", String.valueOf(rs.getLong("success_count")));
						map.put("failure_count", String.valueOf(rs.getLong("failure_count")));
						map.put("avg_resp_time", String.valueOf(rs.getLong("avg_resp_time")));
						map.put("min_resp_time", String.valueOf(rs.getLong("min_resp_time")));
						map.put("max_resp_time", String.valueOf(rs.getLong("max_resp_time")));
						map.put("packet_loss_rate", String.valueOf(rs.getLong("packet_loss_rate")));
						map.put("url", String.valueOf(rs.getString("TEST_IP")));
						return map;
					}
				});

		return list;
	}

	public int queryPingResultCount(String serialnumber, String starttime,
			String endtime, int curPage_splitPage, int num_splitPage) 
	{
		PrepareSQL psql = new PrepareSQL();
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		sql.append("from tab_ping_diag_result a,tab_gw_device b,tab_vendor c,gw_device_model d,tab_city e " +
				"where a.ping_result is not null and a.device_serialnumber=b.device_serialnumber " +
				"and b.vendor_id=c.vendor_id and b.city_id=e.city_id and b.device_model_id=d.device_model_id ");
		if (!StringUtil.IsEmpty(serialnumber)) {
			sql.append(" and a.device_serialnumber='").append(
					serialnumber + "'");
		}

		if (!StringUtil.IsEmpty(starttime)) {
			sql.append(" and a.test_time>").append(starttime);
		}

		if (!StringUtil.IsEmpty(endtime)) {
			sql.append(" and a.test_time<").append(endtime);
		}
		psql.setSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	
	public List<Map> queryPingResultExcel(String serialnumber, String starttime,String endtime) 
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.device_serialnumber,a.test_time,a.success_count,");
		psql.append("a.failure_count,a.avg_resp_time,a.min_resp_time,");
		psql.append("a.max_resp_time,a.packet_loss_rate,a.test_ip,a.wan_interface,");
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("b.vendor_id,b.city_id,b.device_model_id ");
			psql.append("from tab_ping_diag_result a,tab_gw_device b ");
			psql.append("where a.ping_result is not null and a.device_serialnumber=b.device_serialnumber ");
		}else{
			psql.append("c.vendor_name,e.city_name,d.device_model ");
			psql.append("from tab_ping_diag_result a,tab_gw_device b,tab_vendor c,gw_device_model d,tab_city e ");
			psql.append("where a.ping_result is not null and a.device_serialnumber=b.device_serialnumber ");
			psql.append("and b.vendor_id=c.vendor_id and b.city_id=e.city_id ");
			psql.append("and b.device_model_id=d.device_model_id ");
		}
		
		if (!StringUtil.IsEmpty(serialnumber)) {
			psql.append(" and a.device_serialnumber='"+serialnumber + "'");
		}

		if (!StringUtil.IsEmpty(starttime)) {
			psql.append(" and a.test_time>"+starttime);
		}

		if (!StringUtil.IsEmpty(endtime)) {
			psql.append(" and a.test_time<"+endtime);
		}
		
		return jt.queryForList(psql.getSQL());
	}
	
	/**
	 * 获取所有厂商
	 */
	public Map<String,String> getVendorName()
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
	public Map<String,String> getDeviceModel()
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