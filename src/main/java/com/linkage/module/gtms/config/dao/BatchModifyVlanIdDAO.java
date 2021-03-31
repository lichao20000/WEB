package com.linkage.module.gtms.config.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gtms.config.serv.StackRefreshToolsBIO;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.itms.resource.util.DeviceTypeUtil;

public class BatchModifyVlanIdDAO extends SuperDAO {

	private SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sdfSec = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 获取今天总数
	 */
	public long getTodayCount() {
		long startTime = getStartTime();

		PrepareSQL pSQL = new PrepareSQL();

		pSQL.append(" select count(*) num from tab_vlanid_task_dev where add_time>="
				+ startTime + " and add_time<" + (startTime + 86400));

		return StringUtil.getLongValue(DBOperation.getRecord(pSQL.getSQL()), "num", 0);
	}

	public int doConfig4JX (long userId, String cityId, long time, String filePath,String wanBus,String iptvBus,String voipBus)
	{
		PrepareSQL psql = new PrepareSQL("insert into tab_vlanid_task(task_id,acc_oid,add_time," +
										 "file_path,status,city_id,wan_bus,iptv_bus,voip_bus) values(?,?,?,?,?,  ?,?,?,?)");
		psql.setLong(1, time);
		psql.setLong(2, userId);
		psql.setLong(3, time);
		psql.setString(4, filePath);
		psql.setInt(5, 0);  //0：失效 1：任务下发成功
		psql.setString(6, cityId);
		psql.setString(7, wanBus);
		psql.setString(8, iptvBus);
		psql.setString(9, voipBus);

		return DBOperation.executeUpdate(psql.getSQL());
	}

	public long getStartTime() {
		try {
			String day = sdfDay.format(new Date());
			return sdfSec.parse(day + " 00:00:00").getTime() / 1000L;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
}
