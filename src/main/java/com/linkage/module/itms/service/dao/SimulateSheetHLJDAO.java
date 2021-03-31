package com.linkage.module.itms.service.dao;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.SocketUtil;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.service.act.SimulateSheetACT;

public class SimulateSheetHLJDAO extends SuperDAO 
{
	private static Logger logger = LoggerFactory.getLogger(SimulateSheetACT.class);

	public static Logger getLogger() {
		return logger;
	}

	public String sendOpenSheet(String servTypeId, String operateType,
			String dealdate, String userType, String username, String cityId,
			String officeId, String zoneId, int orderType, String linkman,
			String linkphone, String linkEmail, String linkMobile,
			String homeAddr, String credNo, String customerId,
			String customerAccount, String customerPwd) 
	{
		logger.debug("sendOpenSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(userType).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(officeId).append("|||");
		sbuffer.append(zoneId).append("|||");
		sbuffer.append(orderType).append("|||");
		sbuffer.append(linkman).append("|||");
		sbuffer.append(linkphone).append("|||");
		sbuffer.append(linkEmail).append("|||");
		sbuffer.append(linkMobile).append("|||");
		sbuffer.append(homeAddr).append("|||");
		sbuffer.append(credNo).append("|||");
		sbuffer.append(customerId).append("|||");
		sbuffer.append(customerAccount).append("|||");
		sbuffer.append(customerPwd);
		sbuffer.append("LINKAGE");
		logger.warn("开户工单：" + sbuffer.toString());
		if (StringUtil.IsEmpty(sbuffer.toString())) {
			logger.warn("sendSheet is null");
			return null;
		}
		String server = Global.G_ITMS_Sheet_Server;
		int port = Global.G_ITMS_Sheet_Port;
		String retResult = SocketUtil.sendStrMesg(server, port,
				sbuffer.toString() + "\n");
		return retResult;
	}

	public String sendStopSheet(String servTypeId, String operateType,
			String dealdate, String userType, String username, String cityId) 
	{
		logger.debug("sendStopSheet()");
		// TODO 需要调整工单
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(userType).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(cityId).append("LINKAGE");
		logger.warn("sendStopSheetNew: " + sbuffer.toString());
		if (StringUtil.IsEmpty(sbuffer.toString())) {
			logger.warn("sendSheet is null");
			return null;
		}
		String server = Global.G_ITMS_Sheet_Server;
		int port = Global.G_ITMS_Sheet_Port;
		String retResult = SocketUtil.sendStrMesg(server, port,
				sbuffer.toString() + "\n");
		return retResult;
	}

	public String sendNetOpenSheet(String servTypeId, String operateType,
			String dealdate, String userType, String username,
			String netUsername, String netPassword, String cityId,
			String vlanId, String wlantype, String ipaddress, String code,
			String netway, String dns, String useriptype) 
	{
		logger.debug("sendNetOpenSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(userType).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(netUsername).append("|||");
		sbuffer.append(netPassword).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(vlanId).append("|||");
		sbuffer.append(wlantype).append("|||");
		sbuffer.append(ipaddress).append("|||");
		sbuffer.append(code).append("|||");
		sbuffer.append(netway).append("|||");
		sbuffer.append(dns).append("|||");
		sbuffer.append("LINKAGE");
		logger.warn("上网:" + sbuffer.toString());
		if (StringUtil.IsEmpty(sbuffer.toString())) {
			logger.warn("sendSheet is null");
			return null;
		}
		String server = Global.G_ITMS_Sheet_Server;
		int port = Global.G_ITMS_Sheet_Port;
		String retResult = SocketUtil.sendStrMesg(server, port,
				sbuffer.toString() + "\n");
		return retResult;
	}

	public String sendH248VoipOpenSheet(String servTypeId, String operateType,
			String dealdate, String userType, String username, String cityId,
			String regId, String regIdType, String sipIp, int sipPort,
			String standSipIp, int standSipPort, String linePort,
			String voipTelepone) 
	{
		logger.debug("sendH248VoipOpenSheetNew()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(userType).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(voipTelepone).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(regId).append("|||");
		sbuffer.append(regIdType).append("|||");
		sbuffer.append(sipIp).append("|||");
		sbuffer.append(sipPort).append("|||");
		sbuffer.append(standSipIp).append("|||");
		sbuffer.append(standSipPort).append("|||");
		sbuffer.append(linePort).append("|||45|||4|||||||||||||||2LINKAGE");
		logger.warn("sendH248VoipOpenSheetNew: " + sbuffer.toString());
		if (StringUtil.IsEmpty(sbuffer.toString())) {
			logger.warn("sendSheet is null");
			return null;
		}
		String server = Global.G_ITMS_Sheet_Server;
		int port = Global.G_ITMS_Sheet_Port;
		String retResult = SocketUtil.sendStrMesg(server, port,
				sbuffer.toString() + "\n");
		return retResult;
	}

	public String sendH248VoipStopSheet(String servTypeId, String operateType,
			String dealdate, String userType, String username, String cityId,
			String voipTelepone) 
	{
		logger.debug("sendStopSheet()");
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(servTypeId).append("|||");
		sbuffer.append(operateType).append("|||");
		sbuffer.append(transferDateFormate(dealdate)).append("|||");
		sbuffer.append(userType).append("|||");
		sbuffer.append(username).append("|||");
		sbuffer.append(cityId).append("|||");
		sbuffer.append(voipTelepone).append("LINKAGE");
		logger.warn("sendVoipStopSheetNew: " + sbuffer.toString());
		if (StringUtil.IsEmpty(sbuffer.toString())) {
			logger.warn("sendSheet is null");
			return null;
		}
		String server = Global.G_ITMS_Sheet_Server;
		int port = Global.G_ITMS_Sheet_Port;
		String retResult = SocketUtil.sendStrMesg(server, port,
				sbuffer.toString() + "\n");
		return retResult;
	}

	public void insertSheet(String id, String username, String serv_account,
			String city_id, int serv_type_id, int oper_type, int result_id,
			String result_desc, long oper_time, long occ_id, String occ_ip) 
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("insert into tab_handsheet_log (id,username,serv_account,city_id,serv_type_id,"
				+ "oper_type,result_id,result_desc,oper_time,occ_id,occ_ip) values(?,?,?,?,?,?,?,?,?,?,?) ");
		psql.setString(1, id);
		psql.setString(2, username);
		psql.setString(3, serv_account);
		psql.setString(4, city_id);
		psql.setInt(5, serv_type_id);
		psql.setInt(6, oper_type);
		psql.setInt(7, result_id);
		psql.setString(8, result_desc);
		psql.setLong(9, oper_time);
		psql.setLong(10, occ_id);
		psql.setString(11, occ_ip);
		jt.update(psql.getSQL());
	}

	/**
	 * 时间格式转换
	 * 
	 * @param dealdate
	 *            yyyy-MM-dd HH:mm:ss
	 * @return yyyyMMddHHmmss
	 */
	private String transferDateFormate(String dealdate) 
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = dateFormat.parse(dealdate);
		} catch (ParseException e) {
			e.printStackTrace();
			logger.warn("工单受理时间转换异常");
		}
		dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return dateFormat.format(date);
	}

}
