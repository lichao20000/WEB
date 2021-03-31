package com.linkage.module.gtms.stb.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.obj.StrategyOBJ;

/**
 * @author Jason(3412)
 * @date 2009-3-5
 */
public class SqlUtil {

	static Logger logger = LoggerFactory.getLogger(SqlUtil.class);
	/**
	 * 增加策略
	 * @author gongsj
	 * @date 2009-7-16
	 * @param obj
	 * @return Object
	 */
	public Boolean addStrategy(List<StrategyOBJ> obj) {
		logger.debug("addStrategy({})", obj);


		String tablename = LipossGlobals.getLipossProperty("batchImportAdv.tablename");
		String logtablename = LipossGlobals.getLipossProperty("batchImportAdv.logtablename");

		if (obj == null) {
			logger.debug("obj == null");
			return false;
		}

		int result = 0;
		ArrayList<String> list = new ArrayList<String>();

		for (StrategyOBJ sobj : obj) {
			StringBuilder sql3 = new StringBuilder();
			StringBuilder sql1 = new StringBuilder();
			StringBuilder sql2 = new StringBuilder();
			StringBuilder sql = new StringBuilder();

			sql.append("delete from ").append(tablename).append(" where device_id='").append(sobj.getDeviceId()).append("' and temp_id=").append(sobj.getTempId());;

			sql1.append("insert into ").append(tablename).append(" (");
			sql1.append("redo,id,acc_oid,time,type,device_id,oui,device_serialnumber,username,sheet_para,service_id,task_id,order_id,sheet_type, temp_id, is_last_one");
			sql1.append(") values (");
			sql1.append(sobj.getRedo());
			sql1.append(",");
			sql1.append(sobj.getId());
			sql1.append("," + sobj.getAccOid());
			sql1.append("," + sobj.getTime());
			sql1.append("," + sobj.getType());
			sql1.append(",'" + sobj.getDeviceId());
			sql1.append("','" + sobj.getOui());
			sql1.append("','" + sobj.getSn());
			sql1.append("','" + sobj.getUsername());
			sql1.append("','" + sobj.getSheetPara());
			sql1.append("'," + sobj.getServiceId());
			sql1.append(",'" + sobj.getTaskId());
			sql1.append("'," + sobj.getOrderId());
			sql1.append("," + sobj.getSheetType());
			sql1.append("," + sobj.getTempId());
			sql1.append("," + sobj.getIsLastOne());
			sql1.append(")");

			sql2.append("insert into ").append(logtablename).append(" (");
			sql2.append("redo,id,acc_oid,time,type,device_id,oui,device_serialnumber,username,sheet_para,service_id,task_id,order_id,sheet_type, temp_id, is_last_one");
			sql2.append(") values (");
			sql2.append(sobj.getRedo());
			sql2.append(",");
			sql2.append(sobj.getId());
			sql2.append("," + sobj.getAccOid());
			sql2.append("," + sobj.getTime());
			sql2.append("," + sobj.getType());
			sql2.append(",'" + sobj.getDeviceId());
			sql2.append("','" + sobj.getOui());
			sql2.append("','" + sobj.getSn());
			sql2.append("','" + sobj.getUsername());
			sql2.append("','" + sobj.getSheetPara());
			sql2.append("'," + sobj.getServiceId());
			sql2.append(",'" + sobj.getTaskId());
			sql2.append("'," + sobj.getOrderId());
			sql2.append("," + sobj.getSheetType());
			sql2.append("," + sobj.getTempId());
			sql2.append("," + sobj.getIsLastOne());
			sql2.append(")");

			PrepareSQL ppSQL = new PrepareSQL();
			ppSQL.setSQL("insert into stb_logo_record(ucid, strategy_id, device_id, task_id, result_id, start_time) values(?,?,?,?,0,?)");
			ppSQL.setLong(1, sobj.getAccOid());
			ppSQL.setLong(2, sobj.getId());
			ppSQL.setString(3, sobj.getDeviceId());
			ppSQL.setLong(4, StringUtil.getLongValue(sobj.getTaskId()));
			ppSQL.setLong(5, new Date().getTime()/1000);

			PrepareSQL psql = new PrepareSQL();
			psql.setSQL(sql.toString().replaceAll("'null'", "null"));
			list.add(psql.getSQL());

			psql.setSQL(sql1.toString().replaceAll("'null'", "null"));
			list.add(psql.getSQL());

			psql.setSQL(sql2.toString().replaceAll("'null'", "null"));
			list.add(psql.getSQL());

			list.add(ppSQL.getSQL());

			sql1 = null;
			sql2 = null;
			sql = null;
		}

		logger.warn("入策略表和stb_logo_record表.list长度:{}", list.size());

//		result = DBOperation.executeBatch(list);
		if(Global.CQDX.equals(Global.instAreaShortName))
		{
			result= DBOperation.executeUpdate(list, "xml-test");
		}else
		{
			result = DBOperation.executeUpdate(list, "xml-picture");
		}

		list = null;

		if (1 == result) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 增加策略
	 * @author gongsj
	 * @date 2009-7-16
	 * @param obj
	 * @return Object
	 */
	public Boolean delStrategy(String task_id) {
		logger.debug("task_id({})", task_id);
		StringBuilder sql = new StringBuilder();

		String tablename = LipossGlobals.getLipossProperty("batchImportAdv.tablename");

		if (task_id == null) {
			logger.debug("obj == null");
			return false;
		}

		int result = 0;
		ArrayList<String> list = new ArrayList<String>();

		sql.append("delete from ").append(tablename).append(" where status<>100 and task_id=").append(task_id);

		PrepareSQL psql = new PrepareSQL(sql.toString().replaceAll("'null'", "null"));
		list.add(psql.getSQL());

		if(Global.CQDX.equals(Global.instAreaShortName))
		{
			result= DBOperation.executeUpdate(list, "xml-test");
		}else
		{
			result = DBOperation.executeUpdate(list, "xml-picture");
		}
		if(1 != result && 0 != result) {
			logger.warn("删除task{}的iTV业务策略失败", task_id);
		}

		logger.warn("删除策略表...");

		sql = null;
		list = null;

		if (1 == result) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 增加策略
	 * @author gongsj
	 * @date 2009-7-16
	 * @param obj
	 * @return Object
	 */
	public static ArrayList<String> addStrategySql(StrategyOBJ obj) {
		logger.debug("addStrategySql({})", obj);
		ArrayList<String> list = new ArrayList<String>();
		StringBuilder sql = new StringBuilder();
		StringBuilder sql1 = new StringBuilder();
		StringBuilder sql2 = new StringBuilder();

		String tablename = LipossGlobals.getLipossProperty("batchImportAdv.tablename");
		String logtablename = LipossGlobals.getLipossProperty("batchImportAdv.logtablename");

		if (obj == null) {
			logger.debug("obj == null");
			return list;
		}

		sql.append("delete from ").append(tablename).append(" where device_id='").append(obj.getDeviceId()).append("' and service_id=").append(obj.getServiceId());

		PrepareSQL psql = new PrepareSQL(sql.toString());
		list.add(psql.getSQL());

		sql1.append("insert into ").append(tablename).append(" (");
		sql1.append("redo,id,acc_oid,time,type,device_id,oui,device_serialnumber,username,sheet_para,service_id,task_id,order_id,sheet_type, temp_id, is_last_one");
		sql1.append(") values (");
		sql1.append(obj.getRedo());
		sql1.append(",");
		sql1.append(obj.getId());
		sql1.append("," + obj.getAccOid());
		sql1.append("," + obj.getTime());
		sql1.append("," + obj.getType());
		sql1.append(",'" + obj.getDeviceId());
		sql1.append("','" + obj.getOui());
		sql1.append("','" + obj.getSn());
		sql1.append("','" + obj.getUsername());
		sql1.append("','" + obj.getSheetPara());
		sql1.append("'," + obj.getServiceId());
		sql1.append(",'" + obj.getTaskId());
		sql1.append("'," + obj.getOrderId());
		sql1.append("," + obj.getSheetType());
		sql1.append("," + obj.getTempId());
		sql1.append("," + obj.getIsLastOne());
		sql1.append(")");

		sql2.append("insert into ").append(logtablename).append(" (");
		sql2.append("redo,id,acc_oid,time,type,device_id,oui,device_serialnumber,username,sheet_para,service_id,task_id,order_id,sheet_type, temp_id, is_last_one");
		sql2.append(") values (");
		sql2.append(obj.getRedo());
		sql2.append(",");
		sql2.append(obj.getId());
		sql2.append("," + obj.getAccOid());
		sql2.append("," + obj.getTime());
		sql2.append("," + obj.getType());
		sql2.append(",'" + obj.getDeviceId());
		sql2.append("','" + obj.getOui());
		sql2.append("','" + obj.getSn());
		sql2.append("','" + obj.getUsername());
		sql2.append("','" + obj.getSheetPara());
		sql2.append("'," + obj.getServiceId());
		sql2.append(",'" + obj.getTaskId());
		sql2.append("'," + obj.getOrderId());
		sql2.append("," + obj.getSheetType());
		sql2.append("," + obj.getTempId());
		sql2.append("," + obj.getIsLastOne());
		sql2.append(")");

		psql.setSQL(sql1.toString().replaceAll("'null'", "null"));
		list.add(psql.getSQL());

		psql.setSQL(sql2.toString().replaceAll("'null'", "null"));
		list.add(psql.getSQL());

		logger.debug("入策略表:{}", list);

		sql = null;
		sql1 = null;
		sql2 = null;

		return list;
	}

}
