/**
 * 
 */
package com.linkage.litms.paramConfig;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;

/**
 * @author zhaixf
 * @date 2008-8-6
 */
public class BatchCongigDbAct {

	private static String sqveDeviceTaskSQL = "insert into gw_conf_task_dev(task_id,device_id) values (?,?)";

	private static String saveTaskSQL = "insert into gw_conf_task("
			+ "task_id,conf_type,time,test_dev_perc,expe_succ_perc,task_para,acc_oid,max_redo_num) values(?,?,?,?,?,?,?,?)";

	private static String getAllTaskSQL = "select a.*,b.acc_loginname from gw_conf_task a, tab_accounts b where a.acc_oid=b.acc_oid order by task_id desc";

	private static String deviceListByTaskSQL = "select a.*,b.device_serialnumber from gw_conf_task_dev a, tab_gw_device b where task_id=? and a.device_id=b.device_id";

	private static String delConfigTaskSQL = "delete from gw_conf_task where task_id=?";

	private static String deleteConfigTaskDeviceSQL = "delete from gw_conf_task_dev where task_id=?";

	/**
	 * 插入数据到批量配置策略表
	 * 
	 * @param
	 * @author zhaixf
	 * @date 2008-8-6
	 * @return int
	 */
	public static int saveTask(long taskId, int conf_type, long time,
			int test_dev_perc, int expe_succ_perc, String task_para, long userId, int max_redo_num) {

		PrepareSQL preSQL = new PrepareSQL();
		preSQL.setSQL(saveTaskSQL);
		preSQL.setLong(1, taskId);
		preSQL.setInt(2, conf_type);
		preSQL.setLong(3, time);
		preSQL.setInt(4, test_dev_perc);
		preSQL.setInt(5, expe_succ_perc);
		preSQL.setString(6, task_para);
		preSQL.setLong(7, userId);
		preSQL.setInt(8, max_redo_num);
		return DataSetBean.executeUpdate(preSQL.getSQL());
	}

	/**
	 * 插入数据到批量配置策略设备表
	 * 
	 * @param
	 * @author zhaixf
	 * @date 2008-8-6
	 * @return int
	 */
	public static int saveDeviceTask(long taskId, String[] arrayDeviceId) {

		int len = arrayDeviceId.length;
		String[] arraySQL = new String[len];
		PrepareSQL preSQL = new PrepareSQL();
		for (int i = 0; i < len; i++) {
			preSQL.setSQL(sqveDeviceTaskSQL);
			preSQL.setLong(1, taskId);
			preSQL.setString(2, arrayDeviceId[i]);
			arraySQL[i] = preSQL.getSQL();
		}
		int[] arrayInt = DataSetBean.doBatch(arraySQL);
		if (arrayInt.length > 0) {
			return 1;
		}
		return 0;
	}

	/**
	 * 获取所有的批量配置任务
	 * 
	 * @param
	 * @author zhaixf
	 * @date 2008-8-6
	 * @return Cursor
	 */
	public static Cursor getAllConfigTask() {
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			getAllTaskSQL = "select a.task_id, a.conf_type, a.time, a.test_dev_perc, a.expe_succ_perc, " +
					"a.real_succ_perc, a.audit_type, a.ok_perc, b.acc_loginname " +
					"from gw_conf_task a, tab_accounts b where a.acc_oid=b.acc_oid order by task_id desc";
		}
		PrepareSQL psql = new PrepareSQL(getAllTaskSQL);
		psql.getSQL();
		return DataSetBean.getCursor(getAllTaskSQL);
	}

	/**
	 * 获取批量配置任务设备信息，通过任务ID
	 * 
	 * @param
	 * @author zhaixf
	 * @date 2008-8-6
	 * @return Cursor
	 */
	public static Cursor getDeviceListByTask(long task_id) {
		PrepareSQL preSQL = new PrepareSQL();
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			deviceListByTaskSQL = "select a.is_start, a.start_time, a.end_time, a.is_succ, a.dev_desc, a.redo_num, b.device_serialnumber " +
					"from gw_conf_task_dev a, tab_gw_device b where task_id=? and a.device_id=b.device_id";
		}
		preSQL.setSQL(deviceListByTaskSQL);
		preSQL.setLong(1, task_id);
		return DataSetBean.getCursor(preSQL.getSQL());
	}

	/**
	 * 删除批量配置任务
	 * 
	 * @param
	 * @author zhaixf
	 * @date 2008-8-7
	 * @return void
	 */
	public static int deleteConfigTask(long task_id) {

		PrepareSQL preSQL = new PrepareSQL();
		preSQL.setSQL(deleteConfigTaskDeviceSQL);
		preSQL.setLong(1, task_id);
		DataSetBean.executeUpdate(preSQL.getSQL());

		preSQL.setSQL(delConfigTaskSQL);
		preSQL.setLong(1, task_id);
		return DataSetBean.executeUpdate(preSQL.getSQL());
	}

	/**
	 * 更新测试结果
	 * 
	 * @param
	 * @author zhaixf
	 * @date 2008-8-7
	 * @return void
	 */
	public static void updateExecutState(long task_id, int succ_perc,
			int audit_type, int ok_perc) {

		String updateExcute = "update gw_conf_task set ok_perc=" + ok_perc
				+ ", real_succ_perc=" + succ_perc + ",audit_type=" + audit_type
				+ " where task_id=" + task_id;
		PrepareSQL preSQL = new PrepareSQL(updateExcute);
		DataSetBean.executeUpdate(preSQL.getSQL());
	}

	/**
	 * 更新执行情况
	 * 
	 * @param
	 * @author zhaixf
	 * @date 2008-8-7
	 * @return void
	 */
	public static void updateExecutState(long task_id, int ok_perc) {

		String updateExcute = "update gw_conf_task set ok_perc =" + ok_perc
				+ " where task_id=" + task_id;
		PrepareSQL preSQL = new PrepareSQL(updateExcute);
		DataSetBean.executeUpdate(preSQL.getSQL());

	}

	/**
	 * 更新为执行完成
	 * 
	 * @param
	 * @author zhaixf
	 * @date 2008-8-7
	 * @return void
	 */
	public static void updateExecutState(long task_id) {

		String updateExcute = "update gw_conf_task set ok_perc = 100 where task_id="
				+ task_id;
		PrepareSQL psql = new PrepareSQL(updateExcute);
		psql.getSQL();
		DataSetBean.executeUpdate(updateExcute);

	}

}
