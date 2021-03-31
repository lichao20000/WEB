/**
 *
 */
package com.linkage.module.gtms.stb.diagnostic.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gtms.stb.dao.GwStbVendorModelVersionDAO;
import com.linkage.module.gtms.stb.obj.tr069.PingOBJ;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.litms.common.util.JdbcTemplateExtend;
import com.linkage.system.utils.DateTimeUtil;

/**
 * @author chenjie(67371)
 * ping诊断相关的dao操作
 */
public class StbDeviceBatchRebootDAO {

	 //日志记录
	private static Logger logger = LoggerFactory
				.getLogger(StbDeviceBatchRebootDAO.class);

	private JdbcTemplateExtend jt;
	private Map<String, String> cityMap = null;

	/**
	 * @param dao
	 */
	public void setDao(DataSource dao) {
		jt = new JdbcTemplateExtend(dao);
	}

	/**
	 * 查询TraceRoute相关表数据
	 *
	 * @param pingObj
	 */
	@SuppressWarnings("unchecked")
	public PingOBJ queryTraceRouteInfo(PingOBJ pingObj)
	{
		String deviceId = pingObj.getDeviceId();
		// diag表信息
		PrepareSQL psql = new PrepareSQL("select response_time, number_of_route_hops from stb_lan_tracert_diag where device_id=?");
		psql.setString(1,deviceId);
		Map map = jt.queryForMap(psql.getSQL());
		pingObj.setResponseTime((String)map.get("response_time"));
		pingObj.setNumberOfRouteHops((String)map.get("number_of_route_hops"));

		// hops表信息
		PrepareSQL psql2 = new PrepareSQL("select hop_host, max_response_time, min_response_time, avg_response_time from stb_lan_tracert_hops where device_id=?");
		psql2.setString(1,deviceId);
		List<Map> hopsList = jt.queryForList(psql2.getSQL());
		pingObj.setHopHostI(hopsList);
		return null;
	}

	/**
	 * 根据city_id 查询IP类别，IP名称
	 * @param cityId
	 * @return
	 */
	public List getIpTypeByCityId(String cityId) {

		PrepareSQL sql = null;

		// 00 表示    省中心
		if ("00".equals(cityId)) {
			sql = new PrepareSQL(
					"select distinct ip_type,ip_name from tab_gw_diagnosticIP");
		}else {
			sql = new PrepareSQL(
					"select distinct ip_type,ip_name from tab_gw_diagnosticIP where city_id=?");
			sql.setString(1, cityId);
		}

		return jt.queryForList(sql.getSQL());
	}


	/**
	 * 根据city_id,ipType   查询IP
	 *
	 * @param cityId
	 * @param ipType
	 * @return
	 */
	public List getIpByIpType(String cityId, String ipType) {

		PrepareSQL sql = null;

		// 00 表示 省中心
		if ("00".equals(cityId)) {
			sql = new PrepareSQL(
					"select distinct ip from tab_gw_diagnosticIP where ip_type=? ");
			sql.setString(1, ipType);
		}else {
			sql = new PrepareSQL(
					"select distinct ip from tab_gw_diagnosticIP where ip_type=? and city_id=?");
			sql.setString(1, ipType);
			sql.setString(2, cityId);
		}

		return jt.queryForList(sql.getSQL());
	}

	public int importConfig(long taskId, String cityId, String taskName, long acc_oid, long taskTime,
			String filePath, String taskType, long startTime) {
		List<String> sqllist = new ArrayList<String>();
		// 任务
		PrepareSQL sql1 = new PrepareSQL(" insert into stb_tab_restart_task(task_id, city_id, task_status, task_name, ");
		sql1.append(" task_type, start_time, task_time) values(?,?,?,?,?, ?,?)");

		sql1.setLong(1, taskId);
		sql1.setString(2, cityId);
		sql1.setInt(3, 1); // 任务状态（1：未执行，2：执行中，3：完成）
		sql1.setString(4, taskName);
		sql1.setLong(5, StringUtil.getIntegerValue(taskType));
		sql1.setLong(6, startTime);
		sql1.setLong(7, taskTime);

		sqllist.add(sql1.getSQL());

		int[] ier = doBatch(sqllist);

		if (ier != null && ier.length > 0) {
			logger.warn("任务定制：  成功");
			return 1;
		} else {
			logger.warn("任务定制：  失败");
			return 0;
		}
	}

	/**
	 * 执行批量SQL.
	 *
	 * @param arrsql
	 *            SQL语句数组
	 * @return 返回操作的记录条数
	 */
	public int[] doBatch(List<String> sqlList) {
		String[] arrsql = new String[sqlList.size()];
		for (int i = 0; i < sqlList.size(); i++) {
			arrsql[i] = String.valueOf(sqlList.get(i));
		}
		int[] result = jt.batchUpdate(arrsql);
		arrsql = null;
		return result;
	}

	public int addAccTempRes(List<String> accountList, long taskId) {
		try {
			List<String> sqlList = new ArrayList<String>();
			PrepareSQL psql = new PrepareSQL();

			for(String devSn : accountList){
				if (!StringUtil.IsEmpty(devSn)) {
					if (sqlList.size() < 200) {
						psql.setSQL(" insert into stb_devsn_Temp(task_id, devSn) values("+ taskId + ",'" + devSn.trim() + "') ");
						sqlList.add(psql.getSQL());
					} else {
						int[] ier = doBatch(sqlList);
						sqlList.clear();
						if (null == ier || ier.length <= 0) {
							return -1;
						}
					}
				}
			}
			if (sqlList.size() > 0) {
				int[] ier = doBatch(sqlList);
				sqlList.clear();
				if (null == ier || ier.length <= 0) {
					return -1;
				}
			}
		} catch (Exception e) {
			logger.error("taskId[{}]-入临时表发生异常，e[{}]", taskId, e);
			e.printStackTrace();
			return -1;
		}
		return 1;
	}


	public int addRestatDevices(long taskId) {
		try {
			PrepareSQL sql1 = new PrepareSQL(" insert into stb_tab_restart_device(devSn,device_id,task_id,status) ");
			sql1.append(" select a.devSn,b.device_id,"+taskId+",1 from stb_devsn_Temp a left join stb_tab_gw_device b on ");
			sql1.append(" a.devSn=b.device_serialnumber where a.task_id="+taskId);
			int res1 = jt.update(sql1.getSQL());
			if(res1>0){
				return 1;
			}
		} catch (Exception e) {
			logger.error("taskId[{}]-入stb_tab_restat_device表发生异常，e[{}]", taskId, e);
			e.printStackTrace();
			return -1;
		}
		return -1;
	}

	public int deleteStbDevSnTemp(long taskId) {
		try {
			PrepareSQL sql1 = new PrepareSQL(" delete from stb_devsn_Temp where task_id="+taskId);
			int res1 = jt.update(sql1.getSQL());
			if(res1>0){
				return 1;
			}
		} catch (Exception e) {
			logger.error("taskId[{}]-删除stb_devsn_Temp表发生异常，e[{}]", taskId, e);
			e.printStackTrace();
			return -1;
		}
		return -1;
	}


	/**
	 * @param num_splitPage
	 * @param curPage_splitPage
	 */
	public List getStbBatchRebootTask(int curPage_splitPage, int num_splitPage, String taskNameQ) {
		PrepareSQL sql = new PrepareSQL(
				"select task_id, task_name, task_type, task_status, city_id, start_time, task_time from stb_tab_restart_task where 1=1 ");
		if (!StringUtil.IsEmpty(taskNameQ)) {
			sql.append(" and task_name like '%" + taskNameQ+"%'");
		}
		sql.append(" order by task_time desc ");
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = jt.querySP(sql.getSQL(), (curPage_splitPage - 1) * num_splitPage, num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("taskIdR", rs.getString("task_id"));
				map.put("taskNameR", rs.getString("task_name"));
				map.put("cityIdR", rs.getString("city_id"));

				String city_id = rs.getString("city_id");
				if ("-1".equals(city_id) || StringUtil.IsEmpty(city_id)) {
					map.put("cityNameR", "-");
				} else {
					String[] cityArr = city_id.split(",");
					String citysStr = "";
					if(null!=cityArr){
						for(int i=0;i<cityArr.length;i++){
							citysStr += cityMap.get(cityArr[i])+",";
						}
					}
					if(!StringUtil.IsEmpty(citysStr) && citysStr.endsWith(",")){
						citysStr = citysStr.substring(0,citysStr.length()-1);
					}
					map.put("cityNameR", citysStr);
				}

				if("1".equals(rs.getString("task_type"))){//任务类型（1：属地，2：导入）
					map.put("taskTypeR", "属地定制");
				}else if("2".equals(rs.getString("task_type"))){
					map.put("taskTypeR", "导入定制");
				}else{
					map.put("taskTypeR", "-");
				}

				if("1".equals(rs.getString("task_status"))){//任务状态（1：未执行，2：执行中，3：完成）
					map.put("taskStatusR", "未执行");
				}else if("2".equals(rs.getString("task_status"))){
					map.put("taskStatusR", "执行中");
				}else if("3".equals(rs.getString("task_status"))){
					map.put("taskStatusR", "完成");
				}else{
					map.put("taskStatusR", "-");
				}

				try {
					long task_time = StringUtil.getLongValue(rs.getString("task_time"));
					DateTimeUtil dt = new DateTimeUtil(task_time * 1000);
					map.put("taskTimeR", dt.getLongDate());
				} catch (Exception e) {
					map.put("taskTimeR", "");
				}

				try {
					long start_time = StringUtil.getLongValue(rs.getString("start_time"));
					DateTimeUtil dt = new DateTimeUtil(start_time * 1000);
					map.put("startTimeR", dt.getLongDate());
				} catch (Exception e) {
					map.put("startTimeR", "");
				}
				return map;
			}
		});
		return list;
	}

	public int countStbBatchRebootTask(int curPage_splitPage, int num_splitPage, String taskNameQ) {
		PrepareSQL sql = new PrepareSQL(
				"select count(*) from stb_tab_restart_task where 1=1 ");
		if (!StringUtil.IsEmpty(taskNameQ)) {
			sql.append(" and task_name like '%" + taskNameQ+"%'");
		}
		int total = jt.queryForInt(sql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}



	/**
	 * @param num_splitPage
	 * @param curPage_splitPage
	 */
	public List getStbBatchRestartDev(int curPage_splitPage, int num_splitPage, String taskIdQ) {
		PrepareSQL sql = new PrepareSQL("select a.devSn, a.status, a.time, b.city_id, b.serv_account ");
		sql.append(" from stb_tab_restart_device a left join stb_tab_gw_device b on a.device_id=b.device_id ");
		sql.append(" where a.task_id=? order by a.time desc ");
		sql.setLong(1, StringUtil.getLongValue(taskIdQ));
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = jt.querySP(sql.getSQL(), (curPage_splitPage - 1) * num_splitPage, num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("devSn", rs.getString("devSn"));
				map.put("servAcc", rs.getString("serv_account"));

				String city_id = rs.getString("city_id");
				if ("-1".equals(city_id) || StringUtil.IsEmpty(city_id)) {
					map.put("cityName", "-");
				} else {
					map.put("cityName", cityMap.get(city_id));
				}

				if("1".equals(rs.getString("status"))){//重启结果（1：未执行，2：成功，3：失败, 4:没有设备）
					map.put("status", "未执行");
				}else if("2".equals(rs.getString("status"))){
					map.put("status", "成功");
				}else if("3".equals(rs.getString("status"))){
					map.put("status", "失败");
				}else if("4".equals(rs.getString("status"))){
					map.put("status", "没有设备");
				}else{
					map.put("status", "-");
				}

				try {
					long time = StringUtil.getLongValue(rs.getString("time"));
					if (time==0) {
						map.put("time", "");
					}else {
						DateTimeUtil dt = new DateTimeUtil(time * 1000);
						map.put("time", dt.getLongDate());
					}

				} catch (Exception e) {
					map.put("time", "");
				}

				return map;
			}
		});
		return list;
	}

	public int countStbBatchRestartDev(int curPage_splitPage, int num_splitPage, String taskIdQ) {
		PrepareSQL sql = new PrepareSQL("select count(*) from stb_tab_restart_device a left join stb_tab_gw_device b ");
		sql.append(" on a.device_id=b.device_id where a.task_id=? order by a.time desc ");
		sql.setLong(1, StringUtil.getLongValue(taskIdQ));
		int total = jt.queryForInt(sql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	public int[] deleteTask(String taskIdD) {
		int[] res = null;
		try {
			List<String> sqlList = new ArrayList<String>();

			PrepareSQL sql1 = new PrepareSQL(" delete from stb_tab_restart_task where task_id="+taskIdD);
			PrepareSQL sql2 = new PrepareSQL(" delete from stb_tab_restart_device where task_id="+taskIdD);
			PrepareSQL sql3 = new PrepareSQL(" delete from stb_devsn_Temp where task_id="+taskIdD);
			sqlList.add(sql1.getSQL());
			sqlList.add(sql2.getSQL());
			sqlList.add(sql3.getSQL());

			res = doBatch(sqlList);
			sqlList=null;
		} catch (Exception e) {
			logger.error("taskId[{}]-删除批量重启任务发生异常，e[{}]", new Object[]{ taskIdD, e });
			e.printStackTrace();
		}
		return res;
	}
}


