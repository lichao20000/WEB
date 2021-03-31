package com.linkage.module.itms.service.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.itms.service.obj.SheetObj;


public class BssSheetByHand4ynltDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(BssSheetByHand4ynltDAO.class);
	
	
	/**
	 * 查询user_id
	 * @param loid
	 * @param userType 
	 * @return
	 */
	public List getUserIdFromTabHgwcustomer(String loid, String userType)
	{
		logger.debug("BssSheetByHandDAO==>getCountUserNameFromTabHgwcustomer()", loid);
		
		PrepareSQL psql = new PrepareSQL();
		psql.append("select user_id from tab_hgwcustomer where username = '"+loid+"'");
		
		return jt.queryForList(psql.getSQL());
	}
	
	
	
	/**
	 * 根据user_id查询用户表，业务用户表信息
	 * 
	 * @param userId
	 * @return
	 */
	public List<HashMap<String,String>> getBssSheetInfo(String userId)
	{
		logger.debug("BssSheetByHandDAO==>getBssSheetInfo()", userId);
		
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("select a.username as loid,a.passwd as pwd,a.city_id,a.adsl_hl,");
		sqlBuffer.append("a.user_state,a.cust_type_id,a.user_type_id,a.dealdate,a.open_status,");
		sqlBuffer.append("a.linkman,a.linkphone,a.email,a.mobile,a.linkaddress,a.linkman_credno,");
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
			//无引用的方法
		}*/
		sqlBuffer.append("b.*,c.* from tab_hgwcustomer a, hgwcust_serv_info b, tab_bss_dev_port c");
		sqlBuffer.append("where a.user_id = b.user_id ");
		sqlBuffer.append("and a.spec_id = c.id ");
		sqlBuffer.append("and a.user_id = "+userId);
		
		PrepareSQL psql = new PrepareSQL(sqlBuffer.toString());
		return DBOperation.getRecords(psql.getSQL());
	}
	
	
	/**
	 * 根据user_id查询用户表，业务用户表信息
	 * 
	 * @param userId
	 * @return
	 */
	public List<HashMap<String,String>> getUserSheetInfo(String userId)
	{
		logger.debug("BssSheetByHandDAO==>getUserSheetInfo()", userId);
		
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("select a.username as loid,a.passwd as pwd,a.city_id,");
		sqlBuffer.append("a.adsl_hl,a.cust_type_id,a.user_type_id,a.linkman,");
		sqlBuffer.append("a.linkphone,a.email,a.mobile,a.linkaddress,a.linkman_credno,");
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
			//无引用的方法
		}*/
		sqlBuffer.append("c.* from tab_hgwcustomer a, tab_bss_dev_port c");
		sqlBuffer.append(" where a.spec_id = c.id and a.user_id = "+userId);
		
		PrepareSQL psql = new PrepareSQL(sqlBuffer.toString());
		return DBOperation.getRecords(psql.getSQL());
	}
	
	
	/**
	 * city_id 转换
	 * 
	 * @param cityId
	 * @return
	 */
	public List <HashMap<String,String>> getCityIdExFromGwCityMap(String cityId) 
	{
		logger.debug("BssSheetByHandDAO==>getCityIdExFromGwCityMap()", cityId);
		StringBuffer sqlBuffer = new StringBuffer();
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
			//无引用的方法
		}*/
		sqlBuffer.append("select * from gw_city_map ");
		sqlBuffer.append(" where city_id = '"+cityId+"'");
		
		PrepareSQL psql = new PrepareSQL(sqlBuffer.toString());
		return DBOperation.getRecords(psql.getSQL());
	}
	
	
	/**
	 * 根据userId 查询VOIP信息
	 * 
	 * @param userId
	 * @return
	 */
	public List<HashMap<String, String>> getVoipinfo(String userId)
	{
		logger.debug("BssSheetByHandDAO==>getVoipinfo()", userId);
		
		StringBuffer sqlBuffer = new StringBuffer();
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
			//无引用的方法
		}*/
		sqlBuffer.append("select a.*,");
		sqlBuffer.append("b.prox_serv,b.prox_port,b.stand_prox_serv,b.stand_prox_port ");
		sqlBuffer.append("from tab_voip_serv_param a, tab_sip_info b ");
		sqlBuffer.append("where a.sip_id = b.sip_id and a.user_id = " + userId);
		
		PrepareSQL psql = new PrepareSQL(sqlBuffer.toString());
		return DBOperation.getRecords(psql.getSQL());
	}


	public boolean isExistCustomer(String customerId)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select customer_name,city_id,office_id,customer_account ");
		psql.append("from tab_customerinfo where customer_id='"+ customerId + "'");
		Map<String, String> map = DBOperation.getRecord(psql.getSQL());
		if (map != null) {
			return true;
		}
		return false;
	}
	
	public boolean isLoidExists(String loid, String userType)
	{
		logger.debug("isLoidExists({})", new Object[] { loid });
		String tabName = "tab_hgwcustomer";
		if ("2".equals(userType))
		{
			tabName = "tab_egwcustomer";
		}
		/** * 判断账号的存在性 */
		if (StringUtil.IsEmpty(loid, true))
		{
			logger.warn("用户账号为空");
			return false;
		}
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select username from ");
		}else{
			psql.append("select * from ");
		}
		psql.append(tabName);
		psql.append(" where (user_state='1' or user_state='2') and username=? ");
		psql.setString(1, loid);
		List<HashMap<String, String>> map = DBOperation.getRecords(psql.getSQL());
		return !map.isEmpty();
	}
	
	public Map<String, String> getUserInfo(String loid, String userType)
	{
		logger.debug("isLoidExists({})", new Object[] { loid });
		String tabName = "tab_hgwcustomer a,tab_bss_dev_port b";
		if ("2".equals(userType))
		{
			tabName = "tab_egwcustomer a,tab_bss_dev_port b";
		}
		/** * 判断账号的存在性 */
		if (StringUtil.IsEmpty(loid, true))
		{
			logger.warn("用户账号为空");
			return null;
		}
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select a.city_id,a.office_id,a.zone_id,a.access_style_id,");
			psql.append("a.linkman,a.linkphone,a.email,a.mobile,a.linkaddress,a.credno,");
			psql.append("a.spec_name,a.customer_id,a.linkman,a.user_id,");
		}else{
			psql.append("select a.*,");
		}
		
		psql.append("b.spec_name from "+tabName);
		psql.append(" where (user_state='1' or user_state='2') and username=? and a.spec_id = b.id ");
		psql.setString(1, loid);
		List<HashMap<String, String>> map = DBOperation.getRecords(psql.getSQL());
		if (map.isEmpty())
		{
			return null;
		}
		return map.get(0);
	}
	
	public List<HashMap<String, String>> getServInfo(String loid, String userType)
	{
		logger.debug("isLoidExists({})", new Object[] { loid });
		String tabUser = "tab_hgwcustomer a";
		String tabServ = "hgwcust_serv_info b";
		if ("2".equals(userType))
		{
			tabUser = "tab_egwcustomer a";
			tabServ = "egwcust_serv_info b";
		}
		/** * 判断账号的存在性 */
		if (StringUtil.IsEmpty(loid, true))
		{
			logger.warn("用户账号为空");
			return null;
		}
		
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select b.serv_type_id,b.username,b.passwd,b.vlanid,");
			psql.append("b.wan_type,b.ipaddress,b.ipmask,b.gateway,b.adsl_ser,b.ip_type,");
			psql.append("b.user_id,b.device_name,b.dscp_mark,b.bind_port,b.serv_num ");
		}else{
			psql.append("select b.* ");
		}
		psql.append("from "+tabUser+","+tabServ);
		psql.append(" where a.user_id=b.user_id and a.username='"+loid+"'");
		return DBOperation.getRecords(psql.getSQL());
	}
	
	public HashMap<String, String> getVoipParaInfo(String userId, String userType,String servType)
	{
		logger.debug("isLoidExists({})", new Object[] { userId });
		String tabServ = "hgwcust_serv_info b";
		String tabParam = "tab_voip_serv_param a";
		String tabSip = "tab_sip_info c ";
		if ("2".equals(userType))
		{
			tabServ = "egwcust_serv_info b";
			tabParam = "tab_egw_voip_serv_param a";
		}
		/** * 判断账号的存在性 */
		if (StringUtil.IsEmpty(userId, true))
		{
			logger.warn("用户账号为空");
			return null;
		}
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			//TODO wait
			psql.append("select protocol,voip_phone,reg_id,reg_id_type,prox_serv,");
			psql.append("prox_port,stand_prox_serv,stand_prox_port,voip_port,");
			psql.append("voip_username,voip_passwd,regi_serv,regi_port,stand_regi_serv,");
			psql.append("stand_regi_port,out_bound_proxy,out_bound_port,");
			psql.append("stand_out_bound_proxy,stand_out_bound_port,uri,user_agent_domain ");
		}else{
			psql.append("select a.*,c.* ");
		}
		psql.append("from "+tabParam+","+tabServ+","+tabSip);
		psql.append(" where a.user_id=b.user_id and a.sip_id = c.sip_id ");
		psql.append(" and b.user_id ="+userId);
		psql.append(" and b.serv_type_id ="+servType);
		
		List<HashMap<String, String>> map = DBOperation.getRecords(psql.getSQL());
		if (map.isEmpty())
		{
			return null;
		}
		return map.get(0);
	}



	public List<HashMap<String, String>> getServInfoByVoipPort(String userId, 
			String userType, String sipVoipPort,String servType)
	{
		logger.debug("getServInfoByVoipPort({})", new Object[] { userId+ "-"+userType+"-"+sipVoipPort});
		String tabServ = "hgwcust_serv_info b";
		String tabParam = "tab_voip_serv_param a";
		String tabSip = "tab_sip_info c ";
		if ("2".equals(userType))
		{
			tabServ = "egwcust_serv_info b";
			tabParam = "tab_egw_voip_serv_param a";
		}
		/** * 判断账号的存在性 */
		if (StringUtil.IsEmpty(userId, true))
		{
			logger.warn("用户账号为空");
			return null;
		}
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			//TODO wait
			psql.append("select voip_phone,reg_id,reg_id_type,prox_serv,prox_port,");
			psql.append("stand_prox_serv,stand_prox_port,vlanid,wan_type,");
			psql.append("ipaddress,ipmask,gateway,adsl_ser,device_name,dscp_mark,");
			psql.append("voip_username,voip_passwd,regi_serv,regi_port,stand_regi_serv,");
			psql.append("stand_regi_port,out_bound_proxy,out_bound_port,stand_out_bound_proxy,");
			psql.append("stand_out_bound_port,uri,user_agent_domain,protocol ");
		}else{
			psql.append("select a.*,b.*,c.* ");
		}
		psql.append("from "+tabParam+","+tabServ+","+tabSip);
		psql.append(" where a.user_id=b.user_id and a.sip_id = c.sip_id ");
		psql.append(" and b.user_id ="+userId);
		psql.append(" and a.voip_port ='"+sipVoipPort+"' ");
		psql.append(" and b.serv_type_id ="+servType);
		
		return DBOperation.getRecords(psql.getSQL());
	}
	
	public void addHandSheetLog(SheetObj userInfoSheet,UserRes curUser,int servType,
			int operType,int resultId,String resultDesc)
	{
		logger.debug("addHandSheetLog()");
		User user = curUser.getUser();
		PrepareSQL psql = new PrepareSQL();
		psql.append("insert into tab_handsheet_log(id,username,city_id,serv_type_id,");
		psql.append("oper_type,result_id,result_desc,oper_time,occ_id,occ_ip) ");
		psql.append("values(?,?,?,?,?,?,?,?,?,?) ");
		psql.setString(1, user.getId()+StringUtil.getStringValue(Math.round(Math.random() * 1000000000000L)));
		psql.setString(2, userInfoSheet.getLoid());
		psql.setString(3, userInfoSheet.getCityId());
		psql.setInt(4, servType);
		psql.setInt(5, operType);
		psql.setInt(6, resultId);
		psql.setString(7, resultDesc);
		psql.setLong(8, new Date().getTime()/1000);
		psql.setLong(9, user.getId());
		psql.setString(10, "");
		DBOperation.executeUpdate(psql.getSQL());
	}
	
	private int getLineId(String linePort)
	{
		logger.debug("getLineId({})", linePort);
		int lineId = 1;
		if ("V1".equals(linePort) || "A0".equals(linePort) || "USER001".equals(linePort))
		{
			lineId = 1;
		}
		else if ("V2".equals(linePort) || "A1".equals(linePort) || "USER002".equals(linePort))
		{
			lineId = 2;
		}
		else if ("V3".equals(linePort) || "A3".equals(linePort) || "USER003".equals(linePort))
		{
			lineId = 3;
		}
		else if ("V4".equals(linePort) || "A4".equals(linePort) || "USER004".equals(linePort))
		{
			lineId = 4;
		}
		else if ("V5".equals(linePort) || "A5".equals(linePort) || "USER005".equals(linePort))
		{
			lineId = 5;
		}
		else if ("V6".equals(linePort) || "A6".equals(linePort) || "USER006".equals(linePort))
		{
			lineId = 6;
		}
		else if ("V7".equals(linePort) || "A7".equals(linePort) || "USER007".equals(linePort))
		{
			lineId = 7;
		}
		else if ("V8".equals(linePort) || "A8".equals(linePort) || "USER008".equals(linePort))
		{
			lineId = 8;
		}
		return lineId;
	}



	public List<Map<String, String>> getSpec()
	{
		PrepareSQL psql = new PrepareSQL("select id,spec_name from tab_bss_dev_port");
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		list = jt.query(psql.getSQL(), new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("spec_id", rs.getString("spec_name"));
				map.put("spec_name", rs.getString("spec_name"));
				return map;
			}
			
		});
		
		return list;
	}
}
