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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

public class BatchAddIptvNodeDAO extends SuperDAO {

	private static Logger logger = LoggerFactory.getLogger(BatchAddIptvNodeDAO.class);
	private SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sdfSec = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


	/**
	 * 获取今天总数
	 */
	public long getTodayCount() {
		long startTime = getStartTime();

		PrepareSQL pSQL = new PrepareSQL();

		pSQL.append(" select count(*) allnum from tab_addiptvnode_task_dev where add_time>="
				+ startTime + " and add_time<" + (startTime + 86400));

		return StringUtil.getLongValue(DBOperation.getRecord(pSQL.getSQL()), "allnum", 0);
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


	/**
	 * 查询设备信息
	 * @param sqlSpell
	 */
	public  ArrayList<HashMap<String, String>> getDevIdsHBLT(String sqlSpell){
		return DBOperation.getRecords(sqlSpell);
	}

	/**
	 * 增加任务信息
	 * @param name
	 * @param taskid
	 * @param accoid
	 * @param addtime
	 * @param starttime
	 * @param endtime
	 * @param enddate
	 * @return
	 */
	public int addTaskHBLT(long taskid, long accoid, String param, int type, String serviceId, String strategy_type){

		PrepareSQL psql = new PrepareSQL(
				"insert into tab_addiptvnode_task(task_id,acc_oid,add_time,service_id,param,type,strategy_type) values(?,?,?,?,?, ?,?)");
		psql.setLong(1, taskid);
		psql.setLong(2, accoid);
		psql.setLong(3, taskid);
		psql.setInt(4, StringUtil.getIntegerValue(serviceId));
		psql.setString(5, param);
		psql.setInt(6, type);
		psql.setInt(7, StringUtil.getIntegerValue(strategy_type));

		return jt.update(psql.getSQL());

	}


	/**
	 * 把设备序列号插入临时表
	 * @param fileName
	 * @param dataList
	 */
	public void insertTmp(String fileName, List<String> dataList) {
		ArrayList<String> sqlList = new ArrayList<String>();
		PrepareSQL psql = null;
		for (int i = 0; i < dataList.size(); i++) {
			psql = new PrepareSQL();
			psql.append("insert into tab_temporary_device"
					+ "(filename,device_serialnumber)" + " values ('" + fileName + "','"
					+ dataList.get(i) + "')");
			sqlList.add(psql.getSQL());
		}
		int res = 0;
		if (LipossGlobals.inArea(Global.JSDX)) {
			res = DBOperation.executeUpdate(sqlList, "proxool.xml-report");
		} else {
			ArrayList<String> sqlListTmp = new ArrayList<String>();
			if(sqlList.size()>0){
				for(String sql : sqlList){
					sqlListTmp.add(sql);
					if(sqlListTmp.size()>=200){
						int resTmp = DBOperation.executeUpdate(sqlListTmp);
						if(resTmp>0){
							res += sqlListTmp.size();
						}
						sqlListTmp.clear();
					}
				}
			}
			if(sqlListTmp.size()>0){
				int resTmp = DBOperation.executeUpdate(sqlListTmp);
				if(resTmp>0){
					res += sqlListTmp.size();
				}
				sqlListTmp.clear();
			}
			logger.info("====成功插入tab_temporary_device表"+res+"条数据====");
		}
	}


	/**
	 * 获取设备信息
	 * @param serList
	 * @param filename
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getTaskDevListHBLT(String filename) {

		PrepareSQL psql = new PrepareSQL();// TODO wait (more table related)

		psql.append(" select a.device_serialnumber a_device_id, b.device_id, b.oui, b.device_serialnumber,d.wan_type, d.username serusername, ");
		psql.append(" c.username, b.city_id ,d.user_id from tab_temporary_device a left join tab_gw_device b on ");
		psql.append(" a.device_serialnumber=b.device_id left join tab_hgwcustomer c on b.device_id=c.device_id ");
		psql.append(" left join hgwcust_serv_info d on (c.user_id=d.user_id and d.serv_type_id=11 and d.serv_status=1 ");
		if(LipossGlobals.inArea(Global.HBLT)) {
			psql.append(" and d.wan_type=2 ");
		}
		psql.append(" ) where a.filename='"+filename+"'");


		ArrayList<HashMap<String, String>> list = DBOperation.getRecords(psql.getSQL());
		if(null==list || list.size()==0){
			return null;
		}

		return list;
	}

	public ArrayList<String> sqlList(List<HashMap<String, String>> devList,long taskid, String serviceId){
		ArrayList<String> sqlList = new ArrayList<String>();
		for (int i = 0; i < devList.size(); i++) {
			String device_serialnumber = String.valueOf(devList.get(i).get("device_serialnumber"));
			String a_device_id = String.valueOf(devList.get(i).get("a_device_id"));
			String device_id = String.valueOf(devList.get(i).get("device_id"));
			String oui = String.valueOf(devList.get(i).get("oui"));
			String loid = String.valueOf(devList.get(i).get("username"));

			PrepareSQL pSql = new PrepareSQL();
			pSql.append(" insert into tab_addiptvnode_task_dev (task_id,device_id,oui,device_serialnumber ");
			pSql.append(" ,file_username,loid,add_time,update_time) values(?,?,?,?,?,  ?,?,?)");

			pSql.setLong(1, taskid);
			pSql.setString(2, device_id);
			pSql.setString(3, oui);
			pSql.setString(4, device_serialnumber);
			pSql.setString(5, a_device_id);
			pSql.setString(6, loid);
			pSql.setLong(7, taskid);
			pSql.setLong(8, taskid);

			sqlList.add(pSql.getSQL());
		}
		return sqlList;
	}


	/**
	 * 批量插入设备信息
	 * @param sqlList
	 */
	public int insertTaskDevHBLT(ArrayList<String> sqlList){
		return DBOperation.executeUpdate(sqlList);
	}

}
