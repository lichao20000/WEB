
package com.linkage.module.gwms.resource.dao;

import java.util.List;
import java.util.Map;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * @author Reno (Ailk NO.)
 * @version 1.0
 * @since 2014年12月18日
 * @category com.linkage.module.gwms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class VersionUpgradeDAO extends SuperDAO
{

	public List<Map> queryTaskStatusByTaskId(Long taskid)
	{
		String sql = "select a.task_status from tab_version_upgrade a where task_id=" + taskid;
		PrepareSQL ppSQL = new PrepareSQL(sql);
		return jt.queryForList(ppSQL.getSQL());
	}
	
	/**
	 * 通过任务号查询一条 tab_version_upgrade
	 * @param taskid 任务号
	 * @return tab_version_upgrade
	 */
	public List<Map> queryMapByTaskId(Long taskid)
	{
		String sql = "select a.task_status,acc_oid,add_time,task_status,strategy_type from tab_version_upgrade a where task_id=" + taskid;
		PrepareSQL ppSQL = new PrepareSQL(sql);
		return jt.queryForList(ppSQL.getSQL());
	}

	/**
	 * 入库表tab_version_upgrade
	 * 
	 * @param task_id
	 *            任务号
	 * @param acc_oid
	 *            定制人
	 * @param add_time
	 *            添加时间
	 * @param task_status
	 *            状态
	 * @param strategy_type
	 *            策略执行时机
	 * @param operator
	 *            升级操作人
	 * @param calldate
	 *            启动升级时间
	 */
	public void insert(Long task_id, Long acc_oid, Long add_time, Long task_status,
			String strategy_type, String operator, Long calldate)
	{
		String sql = "insert into tab_version_upgrade(task_id, acc_oid,add_time,task_status,strategy_type) values(?,?,?,?,?)";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setLong(1, task_id);
		psql.setLong(2, acc_oid);
		psql.setLong(3, add_time);
		psql.setLong(4, task_status);
		psql.setString(5, strategy_type);
		jt.execute(psql.getSQL());
	}

	/**
	 * 通过设备id字符串，查询设备
	 * 
	 * @param deviceIds
	 *            设备id字符串
	 * @return
	 */
	public List queryByDeviceIds(String deviceIds)
	{
		String sql = "select device_id,oui,device_serialnumber,city_id from tab_gw_device where device_id in('"
				+ deviceIds + "')";
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 入库表tab_version_upgrade_dev
	 * 
	 * @param taskid
	 *            任务号
	 * @param device_id
	 *            设备id
	 * @param oui
	 *            oui
	 * @param device_serialnumber
	 *            设备序列号
	 * @param city_id
	 *            属地
	 */
	public void insertVersionUpgradeDev(Long taskid, Long device_id, String oui,
			String device_serialnumber, String city_id)
	{
		String sql = "insert into tab_version_upgrade_dev(task_id, device_id, oui, device_serialnumber, city_id)values(?,?,?,?,?)";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setLong(1, taskid);
		psql.setLong(2, device_id);
		psql.setString(3, oui);
		psql.setString(4, device_serialnumber);
		psql.setString(5, city_id);
		jt.execute(psql.getSQL());
	}
}
