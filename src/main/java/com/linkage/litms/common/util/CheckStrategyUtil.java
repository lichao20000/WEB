package com.linkage.litms.common.util;

import java.util.Map;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;

/**
 * 
 * @author chenzhangjian (Ailk No.)
 * @version 1.0
 * @since 2015-9-29
 * @category com.linkage.litms.common.util
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class CheckStrategyUtil
{
	public static boolean chechStrategy(String deviceId){
		boolean result = false;
		String tableName = LipossGlobals.getLipossProperty("strategy_tabname.serv.tabname");
		if(StringUtil.IsEmpty(tableName)){
			tableName = LipossGlobals.getLipossProperty("strategy_tabname.strategy.tabname");
		}

		String sql = "select count(1) rcount from " + tableName + " where status not in (0,100) and device_id = ?";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select count(*) rcount from " + tableName + " where status not in (0,100) and device_id = ?";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setString(1, deviceId);
		psql.getSQL();
		Map<String,String> resultMap= DBOperation.getRecord(psql.toString());
		if(null != resultMap){
			int count = StringUtil.getIntegerValue(resultMap.get("rcount"));
			if(count < 1){
				result = true;
			}
		}
		return result;
	}
}
