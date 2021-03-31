package com.linkage.module.itms.service.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;


public class SimulateSheetNxDAO extends SuperDAO 
{
	private static Logger logger = LoggerFactory.getLogger(SimulateSheetNxDAO.class);
	
	public List<Map<String,String>> getUserInfo(String username){
		logger.debug("getUserInfo({})", username);
		StringBuffer sql = new StringBuffer();
		sql.append("select access_style_id from tab_hgwcustomer where (user_state='1' or user_state='2') and username='").append(
				username).append("'");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map<String, String>> list = jt.query(psql.getSQL(), new RowMapper(){
			public Object mapRow(ResultSet rs, int arg1) throws SQLException{
				Map<String, String> map = new HashMap<String, String>();
				map.put("orderType", rs.getString("access_style_id"));
				return map;
			}
		});
		return list;
	}
	
	
	/**
	 * 确认tab_hgwcustomer中是否存在此用户名
	 * 
	 * @param userName
	 * @return
	 */
	public String getUserIdByTabHgwcustomer(String userName) {
		
		logger.debug("getUserIdByHgwcustServInfo({})",new Object[]{userName});
		
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct user_id from tab_hgwcustomer where username='"+userName+"'");
		PrepareSQL psql = new PrepareSQL(sb.toString());
		Map map = jt.queryForMap(psql.getSQL());
		return StringUtil.getStringValue(map.get("user_id"));
	}
	
	
	/**
	 * 通过userId确认hgwcust_serv_info中是否有符合条件的记录
	 * 
	 * @param userId
	 * @return
	 */
	public int checkHgwcustServInfo(String userId) 
	{
		logger.debug("checkHgwcustServInfo({})", userId);
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select count(*) ");
		}else{
			psql.append("select count(1) ");
		}
		psql.append("from hgwcust_serv_info "); 
		psql.append("where user_id="+userId+" and serv_type_id=14"); // 14 表示：VOIP H.248
		
		return jt.queryForInt(psql.getSQL());
	}
	
	/**
	 * 不存在VOIP H.248业务，则新增
	 * @param userId		用户ID
	 * @param servTypeId	业务类型
	 * @param operateType	开户 OR 销户
	 * @param username		用户名
	 * @param wanType		上网方式
	 * @param ipaddress		IP地址
	 * @param ipmask		掩码
	 * @param gateway		网关
	 * @param adslSer		DNS
	 * @param vlanid		vlanid
	 * @param vpiid			vpiid
	 * @param vciid			vciid
	 * @param mgcIp			主MGC服务器地址
	 * @param mgcPort		主MGC服务器地址
	 * @param standMgcIp	备MGC服务器地址
	 * @param standMgcPort	备MGC服务器地址
	 * @param lineId		线路ID
	 * @param regId			终端向软交换注册全局唯一标识
	 * @param regIdType		注册标识类型
	 * @param voipTelepone	业务电话号码
	 * @return
	 */
	public String insertAllTableInfo(String userId, String servTypeId, String operateType,
			String username, String wanType, String ipaddress, 
			String ipmask, String gateway, String adslSer, String vlanid, String vpiid, String vciid, 
			String mgcIp, int mgcPort, String standMgcIp, int standMgcPort, 
			String voipPort, String regId ,String regIdType, String voipTelepone)
	{
		logger.debug("insertHgwcustServInfo({},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{})",
				new Object[] { userId, servTypeId, operateType, username,
						wanType, ipaddress, ipmask, gateway, adslSer, vlanid,
						vpiid, vciid, mgcIp, mgcPort, standMgcIp, standMgcPort,
						voipPort, regId, regIdType, voipTelepone });
		
		StringBuffer sqlStr = new StringBuffer();
		PrepareSQL psql = null;
		String strMsg = "";
		
		long currTime = System.currentTimeMillis()/1000;
		
		StringBuffer sql_1 = new StringBuffer();
		if("3".equals(wanType)){
			sql_1.append("insert into hgwcust_serv_info(user_id, serv_type_id, username, wan_type, vpiid, vciid, vlanid, ipaddress, ipmask, gateway, adsl_ser, open_status, dealdate, updatetime, opendate)  ");
			sql_1.append("values("+userId+",14,'"+username+"',"+wanType+",null,null,'45','"+ipaddress+"','"+ipmask+"','"+gateway+"','"+adslSer+"',0,"+currTime+","+currTime+","+currTime+") ");
		}else{
			sql_1.append("insert into hgwcust_serv_info(user_id, serv_type_id, username, wan_type, vpiid, vciid, vlanid, open_status, dealdate, updatetime, opendate)  ");
			sql_1.append("values("+userId+",14,'"+username+"',"+wanType+",null,null,'45',0,"+currTime+","+currTime+","+currTime+") ");
		}
		psql = new PrepareSQL(sql_1.toString());
		sqlStr.append(psql.getSQL());
		sqlStr.append(";");
		
		long maxSipId;
		StringBuffer sql_2 = new StringBuffer();
		
		/** 判断tab_sip_info表中是否存在对应的记录，如果没有则新增tab_sip_info，否则不对tab_sip_info做操作 */
		List<Map> sipIdList = getSipIdFromTabSipInfo(mgcIp, mgcPort, standMgcIp, standMgcPort);
		if(null != sipIdList && sipIdList.size()>0){  /** tab_sip_info表中存在对应的记录 */
			maxSipId = StringUtil.getLongValue(sipIdList.get(0).get("sip_id"));
		}else{  /** tab_sip_info表中不存在对应的记录 */
			maxSipId = DBOperation.getMaxId("sip_id", "tab_sip_info");
			++maxSipId;
			sql_2.append("insert into tab_sip_info(sip_id,prox_serv,prox_port,stand_prox_serv,stand_prox_port) ");
			sql_2.append("values("+maxSipId+",'"+mgcIp+"',"+mgcPort+",'"+standMgcIp+"',"+standMgcPort+")");
			psql = new PrepareSQL(sql_2.toString());
			psql.getSQL();
			sqlStr.append(sql_2.toString());
			sqlStr.append(";");
		}
		
		StringBuffer sql_3 = new StringBuffer();
		String lineId = "";
		if("A1".equals(voipPort) || "AL1".equals(voipPort) || "AG58900".equals(voipPort)){
			lineId = "1";
		}else if("A2".equals(voipPort) || "AL2".equals(voipPort) || "AG58901".equals(voipPort)){
			lineId = "2";
		}
		sql_3.append("insert into tab_voip_serv_param(user_id, line_id, sip_id, updatetime, voip_phone, parm_stat, protocol, voip_port, reg_id, reg_id_type) ");
		sql_3.append("values("+userId+","+lineId+","+maxSipId+","+currTime+",'"+voipTelepone+"',0,2,'"+voipPort+"','"+regId+"',"+regIdType+")");
		psql = new PrepareSQL(sql_3.toString());
		sqlStr.append(psql.getSQL());
		sqlStr.append(";");
		
		int[] iCodes = DataSetBean.doBatch(sqlStr.toString());
		
		/** 清除缓存 */
		sql_1 = null;
		sql_2 = null;
		sql_3 = null;
		psql = null; 
		sqlStr = null;
		
		//返回结果
		if (iCodes != null && iCodes.length > 0) {
			strMsg = "开户成功！";
		} else {
			strMsg = "开户失败！";
		}
		
		return strMsg;
	}
	
	
	
	/**
	 * 不存在VOIP H.248业务，则新增
	 * @param userId		用户ID
	 * @param servTypeId	业务类型
	 * @param operateType	开户 OR 销户
	 * @param username		用户名
	 * @param wanType		上网方式
	 * @param ipaddress		IP地址
	 * @param ipmask		掩码
	 * @param gateway		网关
	 * @param adslSer		DNS
	 * @param vlanid		vlanid
	 * @param vpiid			vpiid
	 * @param vciid			vciid
	 * @param mgcIp			主MGC服务器地址
	 * @param mgcPort		主MGC服务器地址
	 * @param standMgcIp	备MGC服务器地址
	 * @param standMgcPort	备MGC服务器地址
	 * @param lineId		线路ID
	 * @param regId			终端向软交换注册全局唯一标识
	 * @param regIdType		注册标识类型
	 * @param voipTelepone	业务电话号码
	 * @return
	 */
	public String updateHgwcustServInfo(String userId, String username, String wanType, String ipaddress, 
		String ipmask, String gateway, String adslSer, String vlanid, String vpiid, String vciid) 
	{
		logger.debug("updateHgwcustServInfo({},{},{},{},{},{},{},{},{},{})",
				new Object[] { userId, username, wanType, ipaddress, ipmask, gateway, 
				adslSer, vlanid, vpiid, vciid });
		
		long currTime = System.currentTimeMillis()/1000;
		StringBuffer sql = new StringBuffer();
		
		if("3".equals(wanType)){
			sql.append("update hgwcust_serv_info set wan_type=" + wanType
					+ ", vlanid='45', ipaddress='" + ipaddress + "', ipmask='"
					+ ipmask + "', gateway='" + gateway + "', adsl_ser='" + adslSer
					+ "', open_status=0, updatetime=" + currTime
					+ " where user_id=" + userId + " and serv_type_id=14"); // serv_type_id=14 表示：VOIP H248
		}else{
			sql.append("update hgwcust_serv_info set wan_type=" + wanType
					+ ", vlanid='45', open_status=0, updatetime=" + currTime
					+ " where user_id=" + userId + " and serv_type_id=14"); // serv_type_id=14 表示：VOIP H248
		}
		
		PrepareSQL psql_1 = new PrepareSQL(sql.toString());
		return psql_1.getSQL();
	}
	
	
	/**
	 * 根据一下参数判断参数表tab_voip_serv_param中是否有符合条件的记录
	 * 
	 * @param user_id
	 * @param voipPhone
	 * @return
	 */
	public int checkTabVoipServParam(String user_id, String lineId) 
	{
		logger.debug("checkTabVoipServParam({},{})", new Object[]{user_id, lineId});
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select count(*) ");
		}else{
			psql.append("select count(1) ");
		}
		psql.append("from tab_voip_serv_param where user_id = "+user_id+" and line_id="+lineId);
		
		return jt.queryForInt(psql.getSQL());
	}
	
	
	/**
	 * 根据以下参数查询参数表tab_voip_serv_param，得出SIP服务器ID(sip_id)
	 * 
	 * @param user_id
	 * @param voipPhone
	 * @param lineId
	 * @return
	 */
	public String getTabVoipServParamKey(String userId, String lineId) {
		
		logger.debug("getTabVoipServParamKey({},{})", new Object[]{userId, lineId});
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct sip_id from tab_voip_serv_param where user_id = "+userId+" and line_id="+lineId);
		PrepareSQL psql = new PrepareSQL(sb.toString());
		Map map = jt.queryForMap(psql.getSQL());
		return StringUtil.getStringValue(map.get("sip_id"));
	}
	
	/**
	 * 新增 参数表tab_voip_serv_param   SIP服务器信息表tab_sip_info
	 * 
	 * @param userId
	 * @param mgcIp
	 * @param mgcPort
	 * @param standMgcIp
	 * @param standMgcPort
	 * @param lineId
	 * @param regId
	 * @param regIdType
	 * @param voipTelepone
	 * @return
	 */
	public String insertInfo(String userId, String mgcIp, int mgcPort, String standMgcIp, int standMgcPort, 
			   String voipPort, String regId ,String regIdType, String voipTelepone) {
		
		logger.debug("insertInfo({},{},{},{},{},{},{},{},{})", new Object[] {
				userId, mgcIp, mgcPort, standMgcIp, standMgcPort, voipPort,
				regId, regIdType, voipTelepone });
		
		StringBuffer sBuffer = new StringBuffer();
		PrepareSQL psql = null;
		long maxSipId ;
		StringBuffer sql_1 = new StringBuffer();
		
		long currTime = System.currentTimeMillis()/1000;
		
		/** 判断表tab_sip_info是否存在对应的记录，没有则新增，否则不对tab_sip_info做操作 */
		List<Map> listMap = getSipIdFromTabSipInfo(mgcIp, mgcPort, standMgcIp, standMgcPort);
		if (null != listMap && listMap.size() > 0) {
			
			maxSipId = new Long(listMap.get(0).get("sip_id").toString()).longValue();
			
		} else {
			
			maxSipId = DBOperation.getMaxId("sip_id", "tab_sip_info");
			++maxSipId;
			
			sql_1.append("insert into tab_sip_info(sip_id,prox_serv,prox_port,stand_prox_serv,stand_prox_port) ");
			sql_1.append("values("+maxSipId+",'"+mgcIp+"',"+mgcPort+",'"+standMgcIp+"',"+standMgcPort+")");
			psql = new PrepareSQL(sql_1.toString());
			sBuffer.append(psql.getSQL());
			sBuffer.append(";");
		}
		
		StringBuffer sql_2 = new StringBuffer();
		String lineId = "";
		if("A1".equals(voipPort) || "AL1".equals(voipPort) || "AG58900".equals(voipPort)){
			lineId = "1";
		}else if("A2".equals(voipPort) || "AL2".equals(voipPort) || "AG58901".equals(voipPort)){
			lineId = "2";
		}
		sql_2.append("insert into tab_voip_serv_param(user_id, line_id, sip_id, updatetime, voip_phone, parm_stat, protocol, voip_port, reg_id, reg_id_type) ");
		sql_2.append("values("+userId+","+lineId+","+maxSipId+","+currTime+",'"+voipTelepone+"',0,2,'"+voipPort+"','"+regId+"',"+regIdType+")");
		psql = new PrepareSQL(sql_2.toString());
		sBuffer.append(psql.getSQL());
		sBuffer.append(";");
		
		StringBuffer sql_3 = new StringBuffer();
		sql_3.append("update tab_voip_serv_param set reg_id = '"+regId+"', reg_id_type = "+regIdType+", sip_id =" + maxSipId+" where user_id = "+userId);
		psql = new PrepareSQL(sql_3.toString());
		sBuffer.append(psql.getSQL());
		
		/** 清除缓存 */
		sql_1 = null;
		sql_2 = null;
		sql_3 = null;
		psql = null;
		
		return sBuffer.toString();
	}
	
	
	/**
	 * 更新 资源_SIP服务器信息表(tab_sip_info)    资源_用户VOIP业务参数表(tab_voip_serv_param)
	 * 
	 * @param userId
	 * @param mgcIp
	 * @param mgcPort
	 * @param standMgcIp
	 * @param standMgcPort
	 * @param lineId
	 * @param regId
	 * @param regIdType
	 * @param voipTelepone
	 * @return
	 */
	public String updateInfo(String userId, String mgcIp, int mgcPort, String standMgcIp, int standMgcPort, 
			   String voipPort, String regId ,String regIdType, String voipTelepone) 
	{
		logger.debug("updateInfo({},{},{},{},{},{},{},{},{})", new Object[] {
				userId, mgcIp, mgcPort, standMgcIp, standMgcPort, voipPort,
				regId, regIdType, voipTelepone });
		
		StringBuffer sBuffer = new StringBuffer();
		StringBuffer sql_1 = new StringBuffer();
		PrepareSQL psql =  null;
		long maxSipId ;
		
		long currTime = System.currentTimeMillis()/1000;
		
		String lineId = "";
		if("A1".equals(voipPort) || "AL1".equals(voipPort) || "AG58900".equals(voipPort)){
			lineId = "1";
		}else if("A2".equals(voipPort) || "AL2".equals(voipPort) || "AG58901".equals(voipPort)){
			lineId = "2";
		}
		
		/** 判断表tab_sip_info是否存在对应的记录，没有则新增，否则不对tab_sip_info做操作 */
		List<Map> listMap = getSipIdFromTabSipInfo(mgcIp, mgcPort, standMgcIp, standMgcPort);
		if (null != listMap && listMap.size()>0) {
			maxSipId = StringUtil.getLongValue(listMap.get(0).get("sip_id"));
		} else {
			maxSipId = DBOperation.getMaxId("sip_id", "tab_sip_info");
			++maxSipId;
			
			sql_1.append("insert into tab_sip_info(sip_id,prox_serv,prox_port,stand_prox_serv,stand_prox_port) ");
			sql_1.append("values("+maxSipId+",'"+mgcIp+"',"+mgcPort+",'"+standMgcIp+"',"+standMgcPort+")");
			psql = new PrepareSQL(sql_1.toString());
			sBuffer.append(psql.getSQL());
			sBuffer.append(";");
		}
		
		StringBuffer sql_2 = new StringBuffer();
		sql_2.append("update tab_voip_serv_param set updatetime=" + currTime
				+ ", voip_phone='" + voipTelepone
				+ "', parm_stat=0, protocol=2, voip_port='" + voipPort
				+ "', reg_id='" + regId + "', reg_id_type=" + regIdType
				+ ",sip_id =" + maxSipId+" where user_id=" + userId + " and line_id=" + lineId);
		psql = new PrepareSQL(sql_2.toString());
		sBuffer.append(psql.getSQL());
		sBuffer.append(";");
		
		StringBuffer sql_3 = new StringBuffer();
		sql_3.append("update tab_voip_serv_param set reg_id = '"+regId+"', reg_id_type = "+regIdType+", sip_id =" + maxSipId+" where user_id = "+userId);
		psql = new PrepareSQL(sql_3.toString());
		sBuffer.append(psql.getSQL());
		
		/** 清除缓存 */
		sql_1 = null;
		sql_2 = null;
		sql_3 = null;
		psql = null;
		
		return sBuffer.toString();
	}
	
	
	/**
	 * 获取sip_id
	 * 
	 * @param mgcIp
	 * @param mgcPort
	 * @param standMgcIp
	 * @param standMgcPort
	 * @return
	 */
	public List<Map> getSipIdFromTabSipInfo(String mgcIp, int mgcPort, String standMgcIp, int standMgcPort ) {
		logger.debug("getSipIdFromTabSipInfo({},{},{},{})", new Object[]{mgcIp, mgcPort, standMgcIp, standMgcPort});
		StringBuffer sql = new StringBuffer();
		sql.append("select sip_id from tab_sip_info ");
		sql.append(" where prox_serv = '"+mgcIp+"' ");
		sql.append("   and prox_port = "+mgcPort );
		sql.append("   and stand_prox_serv = '"+standMgcIp+"' ");
		sql.append("   and stand_prox_port = "+standMgcPort );
		sql.append("   and regi_serv = null ");
		sql.append("   and regi_port = null ");
		sql.append("   and stand_regi_serv = null ");
		sql.append("   and stand_regi_port = null ");
		sql.append("   and out_bound_proxy = null ");
		sql.append("   and out_bound_port = null ");
		sql.append("   and stand_out_bound_proxy = null ");
		sql.append("   and stand_out_bound_port = null ");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		
		return jt.queryForList(psql.getSQL());
	}
	
	
	
}
