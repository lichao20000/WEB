package com.linkage.module.gtms.stb.diagnostic.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.SuperDAO;

public class StbBatchRestartDAO extends SuperDAO {
	private static Logger logger = LoggerFactory.getLogger(StbBatchRestartDAO.class);
	private SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sdfSec = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * @param fileName
	 * @param dataList
	 * @param importQueryField
	 * @return
	 */
	public void insertTmp(String fileName, List<String> dataList, String importQueryField) {
		ArrayList<String> sqlList = new ArrayList<String>();
		PrepareSQL psql = null;
		if ("username".equals(importQueryField)) {
			for (int i = 0; i < dataList.size(); i++) {
				psql = new PrepareSQL(" insert into stb_tab_seniorquery_tmp(filename,username) ");
				psql.append(" values('" + fileName + "','" + dataList.get(i) + "') ");
				sqlList.add(psql.getSQL());
			}
		}
		DBOperation.executeUpdate(sqlList);
	}

	/**
	 * @param fileName
	 * @return
	 */
	public ArrayList<HashMap<String, String>> queryDeviceByImportUsername(String fileName) {
		logger.warn("StbBatchRestartDao.queryDeviceByImportUsername()");
		PrepareSQL pSQL = new PrepareSQL();// TODO wait (more table related)
		pSQL.append(" select a.username file_username,b.serv_account,b.city_id,c.device_id,c.device_serialnumber ");
		pSQL.append(" from stb_tab_seniorquery_tmp a left join stb_tab_customer b on (a.username=b.serv_account) ");
		pSQL.append(" left join stb_tab_gw_device c on (b.customer_id=c.customer_id) where a.filename =? ");
		pSQL.setString(1, fileName);
		return DBOperation.getRecords(pSQL.getSQL());
	}

	/**
	 * 入stb_tab_restartdev表记录非法信息
	 * @return
	 */
	public String saveResSql(long taskId, String fileUsername, String res) {
		logger.debug("StbBatchRestartDAO.saveResSql()");
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append(" insert into stb_tab_restartdev (task_id,file_username,res,status)");
		pSQL.append(" values (?,?,?,?) ");
		int index = 0;
		pSQL.setLong(++index, taskId);
		pSQL.setString(++index, fileUsername);
		pSQL.setString(++index, res);
		pSQL.setInt(++index, 4);
		return pSQL.getSQL();
	}

	/**
	 * 入stb_tab_restartdev任务清单表记录合法信息
	 * @return
	 */
	public String insertRestartdevSql(long taskId, String fileUsername, String deviceId, String devsn) {
		logger.debug("StbBatchRestartDAO.saveResSql()");
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append(" insert into stb_tab_restartdev (task_id,file_username,device_id,devsn,status)");
		pSQL.append(" values (?,?,?,?,?) ");
		int index = 0;
		pSQL.setLong(++index, taskId);
		pSQL.setString(++index, fileUsername);
		pSQL.setString(++index, deviceId);
		pSQL.setString(++index, devsn);
		pSQL.setInt(++index, 1);
		return pSQL.getSQL();
	}

	/**
	 * 入stb_tab_restartdev_task任务表记录信息
	 * @return
	 */
	public String insertRestartdevTaskSql(long taskId , Long ocid, Long startTime, int intervalTime) {
		logger.debug("StbBatchRestartDAO.saveResSql()");
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.append(" insert into stb_tab_restartdev_task (task_id,ocid,task_status,start_time,task_time,add_time,interval_time)");
		pSQL.append(" values (?,?,?,?,?,?,?) ");
		int index = 0;
		pSQL.setLong(++index, taskId);
		pSQL.setLong(++index, ocid);
		pSQL.setInt(++index, 1);
		pSQL.setLong(++index, startTime);
		pSQL.setLong(++index, new DateTimeUtil().getLongTime());
		pSQL.setLong(++index, new DateTimeUtil().getLongTime());
		pSQL.setInt(++index, intervalTime);
		return pSQL.getSQL();
	}

	/**
	 * 获取今天总数
	 */
	public long getTodayCount() {
		long startTime = getStartTime();

		PrepareSQL pSQL = new PrepareSQL();

		pSQL.append(" select count(*) allnum from tab_sendsheet_res where updatetime>="
				+ startTime + " and updatetime<" + (startTime + 86400));

		return StringUtil.getLongValue(DBOperation.getRecord(pSQL.getSQL()), "allnum", 0);
	}

	/**
	 * @param sql
	 * @return
	 */
	public int excuteSingle(String sql) {

		logger.debug("StbBatchRestartDAO.excuteBatch()");
		return DBOperation.executeUpdate(sql);
	}

	/**
	 * @param sqlList
	 * @return
	 */
	public int excuteBatch(ArrayList<String> sqlList) {

		logger.debug("StbBatchRestartDAO.excuteBatch()");
		return DBOperation.executeUpdate(sqlList);
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
