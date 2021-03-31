package com.linkage.module.gtms.stb.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.resource.util.DeviceTypeUtil;
import com.linkage.system.utils.StringUtils;

public class BatchConfigNodeDAO extends SuperDAO{
	private static Logger logger = LoggerFactory.getLogger(BatchConfigNodeDAO.class);


	/**
	 * 查询任务
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param queryVaild
	 * @param startTime
	 * @param endTime
	 * @param cityId
	 * @return
	 */
	public List getBatchConfigNodeTask(int curPage_splitPage, int num_splitPage,
			String queryVaild,String startTime,String endTime,String cityId,String taskName)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select  a.task_id,a.acc_oid,a.task_name,a.add_time,a.status,a.update_time,c.acc_loginname from stb_batch_con_task a,tab_accounts c where a.acc_oid=c.acc_oid");
		if (null != queryVaild && !"".equals(queryVaild) && !"-1".equals(queryVaild))
		{
			sql.append(" and a.status=").append(queryVaild);
		}
		if(null != taskName && !"".equals(taskName))
		{
			sql.append(" and a.task_name='").append(taskName).append("'");
		}
		if(null != startTime && !"".equals(startTime))
		{
			sql.append(" and a.add_time>=").append(getTime(startTime));
		}
		if(null != endTime && !"".equals(endTime))
		{
			sql.append(" and a.add_time<=").append(getTime(endTime)+86399);
		}
		//属地
		if(!StringUtil.IsEmpty(cityId) && !"-1".equals(cityId) && !"00".equals(cityId)){

			List list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append( " and  a.city_id in ("
					+ StringUtils.weave(list) + ")");
			list = null;
		}
		sql.append(" order by a.add_time desc");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		//vendorMap = GwVendorModelVersionDAO.getVendorMap();
		List list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage,
				num_splitPage, new RowMapper()
				{

					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{
						Map<String, String> map = new HashMap<String, String>();
						//map.put("vendor_add", vendorMap.get(vendor_id));
						map.put("acc_loginname", rs.getString("acc_loginname"));
						map.put("acc_oid", rs.getString("acc_oid"));
						map.put("task_name", StringUtil.getStringValue(rs.getString("task_name")));
						try
						{
							long add_time = StringUtil.getLongValue(rs
									.getString("add_time"));
							DateTimeUtil dt = new DateTimeUtil(add_time * 1000);
							map.put("add_time", dt.getLongDate());
						}
						catch (NumberFormatException e)
						{
							map.put("add_time", "");
						}
						catch (Exception e)
						{
							map.put("add_time", "");
						}
						try
						{
							long update_time = StringUtil.getLongValue(rs
									.getString("update_time"));
							DateTimeUtil dt = new DateTimeUtil(update_time * 1000);
							map.put("update_time", dt.getLongDate());
						}
						catch (NumberFormatException e)
						{
							map.put("update_time", "");
						}
						catch (Exception e)
						{
							map.put("update_time", "");
						}
						map.put("task_id", rs.getString("task_id"));
						map.put("status", rs.getString("status"));
						return map;
					}
				});
		return list;
	}

	public int countBatchConfigNodeTask(int curPage_splitPage, int num_splitPage,String queryVaild,String startTime,String endTime,String cityId,String taskName)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from stb_batch_con_task a,tab_accounts c where a.acc_oid=c.acc_oid");
		if (null != queryVaild && !"".equals(queryVaild) && !"-1".equals(queryVaild))
		{
			sql.append(" and a.status=").append(queryVaild);
		}
		if(null != taskName && !"".equals(taskName))
		{
			sql.append(" and a.task_name='").append(taskName).append("'");
		}
		if(null != startTime && !"".equals(startTime))
		{
			sql.append(" and a.add_time>=").append(getTime(startTime));
		}
		if(null != endTime && !"".equals(endTime))
		{
			sql.append(" and a.add_time<=").append(getTime(endTime)+86399);
		}
		if(!StringUtil.IsEmpty(cityId) && !"-1".equals(cityId) && !"00".equals(cityId)){

			List list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append( " and  a.city_id in ("
					+ StringUtils.weave(list) + ")");
			list = null;
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
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


	public int updateTaskStatus(String taskId,String status)
	{
		String sql = "update stb_batch_con_task set status=?,update_time=? where task_id=?";
		PrepareSQL pSql = new PrepareSQL(sql);
		long currTime = new Date().getTime() / 1000L;
		pSql.setLong(1, StringUtil.getLongValue(status));
		pSql.setLong(2,currTime);
		pSql.setLong(3, StringUtil.getLongValue(taskId));
		return jt.update(pSql.getSQL());
	}

	public void deleteTask(String taskId)
	{
		List<String> sqlList = new ArrayList<String>();
		PrepareSQL psql = new PrepareSQL();
		psql.setSQL("delete from stb_batch_con_task where task_id="+taskId);
		sqlList.add(psql.getSQL());
		psql.setSQL("delete from stb_batch_con_ipcheck where task_id="+taskId);
		sqlList.add(psql.getSQL());
		psql.setSQL("delete from stb_batch_con_version where task_id="+taskId);
		sqlList.add(psql.getSQL());
		psql.setSQL("delete from stb_batch_con_maccheck where task_id="+taskId);
		sqlList.add(psql.getSQL());
		psql.setSQL("delete from stb_batch_con_para_value where task_id="+taskId);
		sqlList.add(psql.getSQL());
		psql.setSQL("delete from stb_batch_con_record where task_id="+taskId);
		sqlList.add(psql.getSQL());

		if(LipossGlobals.inArea(Global.JXDX) || LipossGlobals.inArea(Global.CQDX)){
			psql.setSQL("delete from stb_batch_con_account where task_id="+taskId);
			sqlList.add(psql.getSQL());
			psql.setSQL("delete from stb_batch_con_recent where task_id="+taskId);
			sqlList.add(psql.getSQL());
		}

		int[] ier = doBatch(sqlList);
		if (ier != null && ier.length > 0) {
			logger.debug("批量入库成功");
		} else {
			logger.debug("批量入库失败");
		}
	}

	public List getTaskResult(String taskId)
	{
		String sql = "select b.vendor_id, b.device_model_id, b.devicetype_id, b.city_id, b.device_serialnumber, b.serv_account, " +
				" b.loopback_ip, b.cpe_mac, a.result_id, a.end_time " +
				" from stb_batch_con_record a, stb_tab_gw_device b where a.task_id=? and a.device_id=b.device_id";
		PrepareSQL taskSql = new PrepareSQL(sql);
		taskSql.setLong(1, Long.parseLong(taskId));
		List taskMap = null;
		taskMap = jt.queryForList(taskSql.getSQL());
		return taskMap;
	}

	public Map getTaskDetail(String taskID)
	{
		Map taskMap = null;
		Map VerMap = null;

		String sql = "select a.city_id, a.vendor_id, a.add_time, a.update_time from stb_batch_con_task a,tab_accounts c where task_id=? and a.acc_oid=c.acc_oid";
		PrepareSQL taskSql = new PrepareSQL(sql);
		taskSql.setLong(1, Long.parseLong(taskID));
		taskMap = jt.queryForMap(taskSql.getSQL());
		if(null != taskMap)
		{
			if(null !=taskMap.get("city_id"))
			{
				taskMap.put("cityName", CityDAO.getCityName(String.valueOf(taskMap.get("city_id"))));
			}
			else
			{
				taskMap.put("cityName","");
			}
			if(null !=taskMap.get("vendor_id"))
			{
				taskMap.put("vendorName", DeviceTypeUtil.vendorMap.get(taskMap.get("vendor_id")));
			}
			else
			{
				taskMap.put("vendorName","");
			}
			try
			{
				long add_time = StringUtil.getLongValue(taskMap
						.get("add_time"));
				DateTimeUtil dt = new DateTimeUtil(add_time * 1000);
				taskMap.put("add_time", dt.getLongDate());
			}
			catch (NumberFormatException e)
			{
				taskMap.put("add_time", "");
			}
			catch (Exception e)
			{
				taskMap.put("add_time", "");
			}
			try
			{
				long update_time = StringUtil.getLongValue(taskMap.get("update_time"));
				DateTimeUtil dt = new DateTimeUtil(update_time * 1000);
				taskMap.put("update_time", dt.getLongDate());
			}
			catch (NumberFormatException e)
			{
				taskMap.put("update_time", "");
			}
			catch (Exception e)
			{
				taskMap.put("update_time", "");
			}
		}
		return taskMap;
	}

	/*
	 * 获取软件版本
	 */
	public List getVerList(String taskId)
	{
		String sql = "select vendor_id, device_model_id, devicetype_id from stb_batch_con_version where task_id=?";
		PrepareSQL taskSql = new PrepareSQL(sql);
		taskSql.setLong(1, Long.parseLong(taskId));
		List taskMap = null;
		taskMap = jt.queryForList(taskSql.getSQL());
		return taskMap;
	}

	/*
	 * 获取ip地址
	 */
	public List getIpList(String taskId)
	{
		String sql = "select start_ip, end_ip from stb_batch_con_ipcheck where task_id=?";
		PrepareSQL taskSql = new PrepareSQL(sql);
		taskSql.setLong(1, Long.parseLong(taskId));
		List taskMap = null;
		taskMap = jt.queryForList(taskSql.getSQL());
		return taskMap;
	}

	/*
	 * 获取软件版本
	 */
	public List getMacList(String taskId)
	{
		String sql = "select start_mac, end_mac from stb_batch_con_maccheck where task_id=?";
		PrepareSQL taskSql = new PrepareSQL(sql);
		taskSql.setLong(1, Long.parseLong(taskId));
		List taskMap = null;
		taskMap = jt.queryForList(taskSql.getSQL());
		return taskMap;
	}


	/*
	 * 获取软件版本
	 */
	public List getParaList(String taskId)
	{
		String sql = "select para_path, para_value from stb_batch_con_para_value where task_id=?";
		PrepareSQL taskSql = new PrepareSQL(sql);
		taskSql.setLong(1, Long.parseLong(taskId));
		List taskMap = null;
		taskMap = jt.queryForList(taskSql.getSQL());
		return taskMap;
	}

	private long getTime(String date)
	{
		DateTimeUtil dt = new DateTimeUtil(date);
		return dt.getLongTime();
	}


}
