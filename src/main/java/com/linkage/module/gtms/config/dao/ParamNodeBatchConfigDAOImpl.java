/**
 *
 */
package com.linkage.module.gtms.config.dao;

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
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author chenjie
 *
 */
public class ParamNodeBatchConfigDAOImpl  extends SuperDAO  implements ParamNodeBatchConfigDAO{

	private static Logger logger = LoggerFactory.getLogger(ParamNodeBatchConfigDAOImpl.class);

	//版本型号集
	private static List<HashMap<String, String>> listDeviceModel;
	//软件、硬件型号集
	private static List<HashMap<String, String>> listDeviceType;

	/**
	 * 获取所有的设备版本型号、软件型号、硬件型号
	 */
	public void init1()
	{
		if(null!=listDeviceModel){
			listDeviceModel.clear();
		}

		if(null!=listDeviceType){
			listDeviceType.clear();
		}

		listDeviceModel=getDeviceModel();
		listDeviceType=getDeviceType();
	}

	/**
	 * 获取所有的device_model_id,device_model
	 */
	public List<HashMap<String, String>> getDeviceModel()
	{
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select device_model_id,device_model from gw_device_model ");
		return DBOperation.getRecords(pSQL.getSQL());
	}

	/**
	 * 获取所有的softwareversion,hardwareversion,devicetype_id
	 */
	public List<HashMap<String, String>> getDeviceType()
	{
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append("select softwareversion,hardwareversion,devicetype_id from tab_devicetype_info ");
		return DBOperation.getRecords(pSQL.getSQL());
	}

	/**
	 * 修改任务状态为已完成
	 * @param task_id
	 */
	public void updateStatus(long task_id)
	{
		PrepareSQL pSql = new PrepareSQL();
		pSql.append("update stb_tab_batchconfig_task set task_status=1 ");
		pSql.append("where task_id=? ");
		pSql.setLong(1,task_id);

		DBOperation.executeUpdate(pSql.getSQL());
	}

	public List<HashMap<String, String>> queryUnDoneTask()
	{
		PrepareSQL pSql = new PrepareSQL();
		pSql.append("select task_id, service_id, query_sql, param, gw_type, add_time, task_status from stb_tab_batchconfig_task where task_status=0 ");

		return DBOperation.getRecords(pSql.getSQL());
	}

	/**
	 * 执行sql查询
	 * @param query_sql
	 * @return
	 */
	public List<HashMap<String, String>> queryDevices(String query_sql)
	{
		PrepareSQL pSql = new PrepareSQL(query_sql);
		return DBOperation.getRecords(pSql.getSQL());
	}


	/**
	 * 入设备明细表stb_tab_batchconfig_dev，状态默认为-1，未操作
	 * @param deviceIds
	 * @param task_id
	 */
	public void insertDev(List<HashMap<String, String>> deviceIds,long task_id)
	{
		ArrayList<String> sqlList=new ArrayList<String>();
		long time=System.currentTimeMillis()/1000L;

		for(int i=0;i<deviceIds.size();i++)
		{
			HashMap<String, String> mapDevice=deviceIds.get(i);

			StringBuffer sb=new StringBuffer();
			sb.append("insert into stb_tab_batchconfig_dev(task_id,device_id,");
			sb.append("user_name,device_model,softwareversion,hardwareversion,do_time,status) ");
			sb.append("values ("+task_id+",");
			sb.append("'"+StringUtil.getStringValue(mapDevice,"device_id","")+"',");
			sb.append("'"+StringUtil.getStringValue(mapDevice,"username","")+"',");

			String device_model="";
			for(HashMap<String, String> map:listDeviceModel){
				if(StringUtil.IsEmpty(StringUtil.getStringValue(mapDevice,"device_model_id"))){
					break;
				}

				if(StringUtil.getStringValue(mapDevice,"device_model_id","")
						.equals(StringUtil.getStringValue(map,"device_model_id"))){
					device_model=StringUtil.getStringValue(map,"device_model","");
					break;
				}
			}

			String softwareversion="";
			String hardwareversion="";
			for(HashMap<String, String> map:listDeviceType){
				if(StringUtil.IsEmpty(StringUtil.getStringValue(mapDevice,"devicetype_id"))){
					break;
				}

				if(StringUtil.getStringValue(mapDevice,"devicetype_id")
						.equals(StringUtil.getStringValue(map,"devicetype_id"))){
					softwareversion=StringUtil.getStringValue(map,"softwareversion","");
					hardwareversion=StringUtil.getStringValue(map,"hardwareversion","");
					break;
				}
			}

			sb.append("'"+device_model+"',");
			sb.append("'"+softwareversion+"',");
			sb.append("'"+hardwareversion+"',");
			sb.append(time+",-1) ");

			sqlList.add(sb.toString());
			if(sqlList.size()%200==0 && sqlList.size()>0)
			{
				DBOperation.executeUpdate(sqlList);
				logger.warn("[{}] insert stb_tab_batchconfig_dev size:{}",task_id,sqlList.size());
				sqlList.clear();
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		if(sqlList.size()>0)
		{
			DBOperation.executeUpdate(sqlList);
			logger.warn("[{}] insert stb_tab_batchconfig_dev size:{}",task_id,sqlList.size());
			sqlList.clear();
		}
		logger.warn("[{}] insert stb_tab_batchconfig_dev,合计入库:{}",task_id,deviceIds.size());
	}



	public int queryUndoNum() {

		logger.debug("queryUndoNum()");

		long now = System.currentTimeMillis()/1000L;

		PrepareSQL psql = new PrepareSQL("select count(*) from gw_serv_strategy where status=0 and type>0 and exec_count<=3 and service_id!=5");
		psql.append(" and time>" + (now-86400) + " and time<=" + now);

		int num = jt.queryForInt(psql.getSQL());

		return num;
	}

	public List getFileMsg(String task_id){
		PrepareSQL sql = new PrepareSQL();
		sql.append("select b.city_id,b.username,a.pathvalue,b.paramvalue,a.add_time from batch_operation_task a,batch_operation_dev b where a.task_id=b.task_id");
		sql.append(" and a.task_id="+task_id);
		List<Map> list = jt.queryForList(sql.getSQL());
		for(Map map:list){
			map.put("city_id", (String)CityDAO.getCityIdCityNameMap().get(map.get("city_id")));
			List<Map> ll = new ArrayList<Map>();
			long oper_time = StringUtil.getLongValue(map.get("add_time"));
			DateTimeUtil dt = new DateTimeUtil(oper_time * 1000);
			map.put("add_time", dt.getLongDate());
		}
		return list;
	}
	public int queryRepeatName(String checkRepeatname) {

		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(*) from batch_operation_task where filename = '");
		psql.append(checkRepeatname+"'");
		int num = jt.queryForInt(psql.getSQL());
		return num;
	}
	public List<Map> getParNodeList(int curPage_splitPage, int num_splitPage,
			String customId, String fileName, String starttime, String endtime)
			{
		final Map<String, String> loginNameMap = getLoginName();
		final Map<String, String> loginOidMap = getLoginOid();
		String acc_oid = loginOidMap.get(customId);
		if((!StringUtil.IsEmpty(customId)&&null!=customId)&&(acc_oid==null||StringUtil.IsEmpty(acc_oid))){
			List<Map> list = new ArrayList<Map>();
			return list;
		}
		StringBuffer sql = new StringBuffer();
		sql.append("select task_id,acc_oid,filename,add_time from batch_operation_task where 1=1");
		if (!StringUtil.IsEmpty(customId) && !"".equals(customId))
		{
			sql.append(" and acc_oid = " + acc_oid +"");
		}
		if (!StringUtil.IsEmpty(fileName) && !"".equals(fileName))
		{
			sql.append(" and filename like '%"+fileName+"%'");
		}
		if (!StringUtil.IsEmpty(starttime) && !"".equals(starttime))
		{
			sql.append(" and add_time >= " + new DateTimeUtil(starttime).getLongTime());
		}
		if (!StringUtil.IsEmpty(endtime) && !"".equals(endtime))
		{
			sql.append(" and add_time <= " + new DateTimeUtil(endtime).getLongTime());
		}
		sql.append(" order by task_id desc");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		return querySP(sql.toString(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("task_id", rs.getString("task_id"));
				map.put("loginname", loginNameMap.get(rs.getString("acc_oid")));
				map.put("filename", rs.getString("filename"));
				long oper_time = StringUtil.getLongValue(rs
						.getString("add_time"));
				DateTimeUtil dt = new DateTimeUtil(oper_time * 1000);
				map.put("customtime", dt.getLongDate());
				return map;
			}
		});
			}

	public int queryCustomNum(long todayTimeMillis) {
		logger.debug("queryCustomNum()");
		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(*) from gw_serv_strategy_batch  where service_id = 116 and status  in(0,100) and time >= ");
		psql.append(""+todayTimeMillis);
		int num = jt.queryForInt(psql.getSQL());
		return num;
	}

	public int getParNodeCount(int curPage_splitPage, int num_splitPage,
			String customId, String fileName, String starttime, String endtime)
	{
		final Map<String, String> loginOidMap = getLoginOid();
		String acc_oid = loginOidMap.get(customId);
		if((!StringUtil.IsEmpty(customId)&&null!=customId)&&(acc_oid==null||StringUtil.IsEmpty(acc_oid))){
			return 0;
		}
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from batch_operation_task where 1=1");
		if (!StringUtil.IsEmpty(customId) && !"".equals(customId))
		{
			sql.append(" and acc_oid = " + acc_oid +"");
		}
		if (!StringUtil.IsEmpty(fileName) && !"".equals(fileName))
		{
			sql.append(" and filename like '%"+fileName+"%'");
		}
		if (!StringUtil.IsEmpty(starttime) && !"".equals(starttime))
		{
			sql.append(" and add_time >= " + new DateTimeUtil(starttime).getLongTime());
		}
		if (!StringUtil.IsEmpty(endtime) && !"".equals(endtime))
		{
			sql.append(" and add_time <= " + new DateTimeUtil(endtime).getLongTime());
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		int total = jt.queryForInt(psql.toString());
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

	public int addBatParamTask(long taskId, long accOid, int operType, int servType,
			int operStatus, int gwType,  long addTime,String pathvalue,String filename){
		String strSQL = "insert into batch_operation_task ("
				+"task_id,acc_oid,oper_type,serv_type,oper_status,gw_type," +
				"add_time,pathvalue,filename) values (?,?,?,?,?,?,?,?,?)";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setLong(1, taskId);
		psql.setLong(2,accOid);
		psql.setInt(3, operType);
		psql.setInt(4, servType);
		psql.setInt(5, operStatus);
		psql.setInt(6, gwType);
		psql.setLong(7, addTime);
		psql.setString(8,pathvalue);
		psql.setString(9,filename);
		int res=jt.update(psql.getSQL());
		return res;
	}



	public int[] addBatParamDev(List<Map> list){
		String strSQL = "insert into batch_operation_dev("
				+"task_id,device_id,service_id,strategy_type,add_time,do_status,paramvalue,city_id,username" +
				") values (?,?,?,?,?,?,?,?,?)";
		String[] sqlArr=new String[list.size()];
		PrepareSQL psql = new PrepareSQL(strSQL);

		for(int i=0;i<list.size();i++){
			Map map = list.get(i);
			psql.setLong(1, StringUtil.getLongValue(map, "task_id"));
			psql.setString(2,StringUtil.getStringValue(map, "device_id"));
			psql.setInt(3, StringUtil.getIntValue(map, "service_id"));
			psql.setString(4, StringUtil.getStringValue(map, "strategy_type"));
			psql.setLong(5, StringUtil.getLongValue(map, "add_time"));
			psql.setInt(6,StringUtil.getIntValue(map, "do_status"));
			psql.setString(7,StringUtil.getStringValue(map, "paramvalue"));
			psql.setString(8,StringUtil.getStringValue(map, "city_id"));
			psql.setString(9,StringUtil.getStringValue(map, "username"));
			sqlArr[i]=psql.getSQL();
		}
		int res[]=jt.batchUpdate(sqlArr);
		return res;
	}

	public Map<String, String> getLoginName(){
		PrepareSQL psql = new PrepareSQL("select acc_oid,acc_loginname from tab_accounts where acc_oid is not null");
		Cursor cursor = DataSetBean
				.getCursor(psql.getSQL());
		HashMap<String, String> loginNameMap = new HashMap<String, String>();
		Map fields = cursor.getNext();
		while (fields != null)
		{
			String acc_loginname = (String) fields.get("acc_loginname");
			if (false == StringUtil.IsEmpty(acc_loginname))
			{
				loginNameMap.put((String) fields.get("acc_oid"), acc_loginname);
			}
			else
			{
				loginNameMap.put((String) fields.get("acc_oid"),
						(String) fields.get("acc_loginname"));
			}
			fields = cursor.getNext();
		}
		return loginNameMap;
	}
	public Map<String, String> getLoginOid(){
		PrepareSQL psql = new PrepareSQL("select acc_oid,acc_loginname from tab_accounts where acc_oid is not null");
		Cursor cursor = DataSetBean
				.getCursor(psql.getSQL());
		HashMap<String, String> loginOidMap = new HashMap<String, String>();
		Map fields = cursor.getNext();
		while (fields != null)
		{
			String acc_oid = (String) fields.get("acc_oid");
			if (false == StringUtil.IsEmpty(acc_oid))
			{
				loginOidMap.put((String) fields.get("acc_loginname"), acc_oid);
			}
			else
			{
				loginOidMap.put((String) fields.get("acc_loginname"),
						(String) fields.get("acc_oid"));
			}
			fields = cursor.getNext();
		}
		return loginOidMap;
	}
}
