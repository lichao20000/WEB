package com.linkage.module.ids.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DBOperation;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

public class AddSimSpeedAccDAO extends SuperDAO {
	private static Logger logger = LoggerFactory
			.getLogger(AddSimSpeedAccDAO.class);
//	public int addAccount(String cityId, int netRate, String Account, String password) {
//		logger.debug("addAccount()");
//		PrepareSQL psql = new PrepareSQL();
//		StringBuffer sql = new StringBuffer();
//		sql.append("insert into tab_speed_net (test_rate,city_id,net_account,net_password) values(?,?,?,?)");
//		psql.setSQL(sql.toString());
//		psql.setInt(1, netRate);
//		psql.setString(2, cityId);
//		psql.setString(3, Account);
//		psql.setString(4, password);
//		int num = jt.update(psql.getSQL());
//		return num;
//	}
//	public int updateAccount(String cityId, int netRate, String Account, String password) {
//		logger.debug("updateAccount()");
//		PrepareSQL psql = new PrepareSQL();
//		StringBuffer sql = new StringBuffer();
//		sql.append("update tab_speed_net set net_account=?,net_password=? where test_rate=? and city_id=?");
//		psql.setSQL(sql.toString());
//		psql.setString(1, Account);
//		psql.setString(2, password);
//		psql.setInt(3, netRate);
//		psql.setString(4, cityId);
//		int num = jt.update(psql.getSQL());
//		return num;
//	}
	public List queryAccount(String cityId, int netRate) {
		logger.debug("queryAccount()");
		PrepareSQL psql = new PrepareSQL();
		StringBuffer sql = new StringBuffer();
		sql.append("select net_account account,net_password password from tab_speed_net where test_rate=? and city_id=?");
		psql.setSQL(sql.toString());
		psql.setInt(1, netRate);
		psql.setString(2, cityId);
		List list = jt.queryForList(psql.getSQL());
		return list;
	}
	
	
	//chenxj begin
	public String searchAcc(String devSn) {
		logger.debug("searchAcc()");
		if(StringUtil.IsEmpty(devSn) || devSn.length()<6){
			return "";
		}
		String dev_sub_sn = devSn.substring(devSn.length()-6, devSn.length());
		
		PrepareSQL psql = new PrepareSQL("select distinct a.device_serialnumber,a.rate from tab_speed_dev_rate a, tab_gw_device b where a.device_serialnumber=b.device_serialnumber ");
		psql.append(" and b.device_serialnumber like '%"+devSn+"' and b.dev_sub_sn='"+dev_sub_sn+"'");
		
		ArrayList<HashMap<String,String>> list =  DBOperation.getRecords(psql.getSQL());
		if(null==list || list.isEmpty()){
			return "";
		}
		if(list.size()>1){
			return "toManyResults";
		}
		return StringUtil.getStringValue(list.get(0), "rate", "") + "##" + StringUtil.getStringValue(list.get(0), "device_serialnumber", "");
	}
	
	public int updateRate(String devSn, long rate){
		logger.debug("updateRate()");
		PrepareSQL psql = new PrepareSQL();
		StringBuffer sql = new StringBuffer();
		sql.append("update tab_speed_dev_rate set rate=? where device_serialnumber=?");
		psql.setSQL(sql.toString());
		psql.setLong(1, rate);
		psql.setString(2, devSn);
		return DBOperation.executeUpdate(psql.getSQL());
	}
	
	public List queryAccount(String cityId, int netRate, String netAccount) {
		logger.debug("queryAccount()");
		PrepareSQL psql = new PrepareSQL();
		StringBuffer sql = new StringBuffer();
		sql.append("select net_account account,net_password password from tab_speed_net where test_rate=? and city_id=? and net_account=? ");
		psql.setSQL(sql.toString());
		psql.setInt(1, netRate);
		psql.setString(2, cityId);
		psql.setString(3, netAccount);
		return DBOperation.getRecords(psql.getSQL());
	}
	
	public String addAccount(String cityId, int netRate, String Account, String password) {
		logger.debug("addAccount()");
		PrepareSQL psql = new PrepareSQL("insert into tab_speed_net (test_rate,city_id,net_account,net_password) values(?,?,?,?)");
		psql.setInt(1, netRate);
		psql.setString(2, cityId);
		psql.setString(3, Account);
		psql.setString(4, password);
		return psql.getSQL();
	}
	public String updateAccount(String cityId, int netRate, String Account, String password) {
		logger.debug("updateAccount()");
		PrepareSQL psql = new PrepareSQL("update tab_speed_net set net_password=? where test_rate=? and city_id=? and net_account=?");
		psql.setString(1, password);
		psql.setInt(2, netRate);
		psql.setString(3, cityId);
		psql.setString(4, Account);
		return psql.getSQL();
	}
	
	public String updateAccountList(ArrayList<String> sqlList){
		logger.debug("updateAccountList()");
		if(DBOperation.executeUpdate(sqlList) > 0){
			return "1";
		}	
		return "-1";
	}
	
	public String delAccount(String cityId, int netRate) {
		logger.debug("delAccount()");
		PrepareSQL psql = new PrepareSQL(" delete from tab_speed_net where test_rate=? and city_id=? ");
		psql.setInt(1, netRate);
		psql.setString(2, cityId);
		return psql.getSQL();
	}
	
}
