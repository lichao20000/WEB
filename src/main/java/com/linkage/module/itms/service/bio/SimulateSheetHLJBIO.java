package com.linkage.module.itms.service.bio;

import com.linkage.module.itms.service.dao.SimulateSheetHLJDAO;

public class SimulateSheetHLJBIO {
	private SimulateSheetHLJDAO dao;

	public String sendOpenSheet(String servTypeId, String operateType,
			String dealdate, String userType, String username, String cityId,
			String officeId, String zoneId, int orderType, String linkman,
			String linkphone, String linkEmail, String linkMobile,
			String homeAddr, String credNo, String customerId,
			String customerAccount, String customerPwd) {
		return dao.sendOpenSheet(servTypeId, operateType, dealdate, userType,
				username, cityId, officeId, zoneId, orderType, linkman,
				linkphone, linkEmail, linkMobile, homeAddr, credNo, customerId,
				customerAccount, customerPwd);
	}

	public String sendStopSheet(String servTypeId, String operateType,
			String dealdate, String userType, String username, String cityId) {
		return dao.sendStopSheet(servTypeId, operateType, dealdate, userType,
				username, cityId);
	}

	public String sendNetOpenSheet(String servTypeId, String operateType,
			String dealdate, String userType, String username,
			String netUsername, String netPassword, String cityId,
			String vlanId, String wlantype, String ipaddress, String code,
			String netway, String dns, String useriptype) {
		return dao.sendNetOpenSheet(servTypeId, operateType, dealdate,
				userType, username, netUsername, netPassword, cityId, vlanId,
				wlantype, ipaddress, code, netway, dns, useriptype);
	}

	public String sendH248VoipOpenSheet(String servTypeId, String operateType,
			String dealdate, String userType, String username, String cityId,
			String regId, String regIdType, String sipIp, int sipPort,
			String standSipIp, int standSipPort, String linePort,
			String voipTelepone) {
		return dao.sendH248VoipOpenSheet(servTypeId, operateType, dealdate,
				userType, username, cityId, regId, regIdType, sipIp, sipPort,
				standSipIp, standSipPort, linePort, voipTelepone);
	}

	public String sendH248VoipStopSheet(String servTypeId, String operateType,
			String dealdate, String userType, String username, String cityId,
			String voipTelepone) {
		return dao.sendH248VoipStopSheet(servTypeId, operateType, dealdate,
				userType, username, cityId, voipTelepone);
	}

	public void insertSheet(String id, String username, String serv_account,
			String city_id, int serv_type_id, int oper_type, int result_id,
			String result_desc, long oper_time, long occ_id, String occ_ip) {
		dao.insertSheet(id, username, serv_account, city_id, serv_type_id,
				oper_type, result_id, result_desc, oper_time, occ_id, occ_ip);
	}

	public SimulateSheetHLJDAO getDao() {
		return dao;
	}

	public void setDao(SimulateSheetHLJDAO dao) {
		this.dao = dao;
	}

}
