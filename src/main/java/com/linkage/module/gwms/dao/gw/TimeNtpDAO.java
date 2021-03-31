package com.linkage.module.gwms.dao.gw;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.obj.gw.TimeNtpOBJ;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author Jason(3412)
 * @date 2009-10-26
 */
public class TimeNtpDAO extends SuperDAO 
{
	private static Logger logger = LoggerFactory.getLogger(TimeNtpDAO.class);

	/**
	 * 根据设备ID获取设备上TIME.结点NTP服务器,时区的信息
	 * 
	 * @param 设备ID
	 * @author Jason(3412)
	 * @date 2009-10-26
	 * @return TimeNtpOBJ 正常返回信息的TimeNtpOBJ对象，否则返回null
	 */
	public TimeNtpOBJ queryTimeNTpObj(String deviceId) 
	{
		logger.debug("queryTimeNTpObj");
		if (StringUtil.IsEmpty(deviceId)) {
			return null;
		}
		TimeNtpOBJ timeNtpObj = null;
		String strSQL = "select * ";
		PrepareSQL psql = new PrepareSQL(strSQL);
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select device_id,ntp_server1,ntp_server2,timezone,");
			psql.append("timezone_name,current_time,enable,apply,gather_time ");
		}else{
			psql.append("select * ");
		}
		psql.append("from gw_time where device_id=? ");
		psql.setString(1, deviceId);
		Map map = queryForMap(psql.getSQL());
		if (null != map && false == map.isEmpty()) {
			timeNtpObj = new TimeNtpOBJ();
			map2obj(map, timeNtpObj);
		}
		return timeNtpObj;
	}

	/**
	 * Map转为TimeNtpOBJ对象，不对map和TimeNtpOBJ做空判断
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-10-26
	 * @return void
	 */
	private void map2obj(Map map, TimeNtpOBJ timeNtpObj) {
		logger.debug("map2obj({}, {})", map, timeNtpObj);

		timeNtpObj.setDeviceId(StringUtil.getStringValue(map.get("device_id")));
		timeNtpObj.setNtpServer1(StringUtil
				.getStringValue(map.get("ntp_server1")));
		timeNtpObj.setNtpServer2(StringUtil
				.getStringValue(map.get("ntp_server2")));
		timeNtpObj.setTimezone(StringUtil.getStringValue(map.get("timezone")));
		timeNtpObj.setTimezoneName(StringUtil.getStringValue(map
				.get("timezone_name")));
		timeNtpObj.setCurrentTime(StringUtil.getStringValue(map
				.get("current_time")));
		timeNtpObj.setEnable(StringUtil.getIntegerValue(map.get("enable")));
		timeNtpObj.setApply(StringUtil.getStringValue(map.get("apply")));
		timeNtpObj.setGatherTime(StringUtil
				.getLongValue(map.get("gather_time")));
	}
}
