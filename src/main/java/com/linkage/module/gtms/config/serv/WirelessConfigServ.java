package com.linkage.module.gtms.config.serv;

import java.util.List;

public interface WirelessConfigServ {

	public String getUserInfo(String deviceId);

	public String doConfig(String deviceId, String gw_type);

	public String getServUserInfo(String username, String gwType);

	public String sendSSIDSheet(String username, String cityId, String gwType,
			String netNum, String loid);

	public String sendCloseSSIDSheet(String username, String cityId,
			String gwType);
	
	public String sendCloseSSIDXML(String username, String loidParam);
	
	public String getResultMeg(String resultString );

	public String SendBathOpenSheet(List<String> usernameList,
			List<String> netNumList, String cityId, String gw_type);

	public List getDeviceList(String gw_type, String fileName);

	public String getMsg();

}
