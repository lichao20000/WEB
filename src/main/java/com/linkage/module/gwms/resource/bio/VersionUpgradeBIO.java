
package com.linkage.module.gwms.resource.bio;

import java.util.List;
import java.util.Map;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.resource.dao.VersionUpgradeDAO;

/**
 * @author Reno (Ailk NO.)
 * @version 1.0
 * @since 2014年12月18日
 * @category com.linkage.module.gwms.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class VersionUpgradeBIO
{

	private VersionUpgradeDAO dao;

	/**
	 * 通过任务号，查询任务状态.
	 * @param taskid 任务号
	 * @return 任务状态
	 */
	public List<Map> queryTaskStatusByTaskId(Long taskid)
	{
		return dao.queryTaskStatusByTaskId(taskid);
	}
	
	
	public List<Map> queryMapByTaskId(Long taskid)
	{
		return dao.queryMapByTaskId(taskid);
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
		dao.insert(task_id, acc_oid, add_time, task_status, strategy_type, operator,
				calldate);
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
		dao.insertVersionUpgradeDev(taskid, device_id, oui, device_serialnumber, city_id);
	}

	/**
	 * 通过deviceIds字符串，查询在其对应的设备.
	 * 
	 * @param deviceIds
	 *            设备id字符串，多个设备id，用,分割
	 * @return 设备列表
	 */
	public List queryByDeviceIds(String deviceIds)
	{
		return dao.queryByDeviceIds(deviceIds);
	}

	public VersionUpgradeDAO getDao()
	{
		return dao;
	}

	public void setDao(VersionUpgradeDAO dao)
	{
		this.dao = dao;
	}
}
