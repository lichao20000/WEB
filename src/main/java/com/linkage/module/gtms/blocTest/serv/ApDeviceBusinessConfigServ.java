package com.linkage.module.gtms.blocTest.serv;


public interface ApDeviceBusinessConfigServ {
	
	public String doBusiness(String deviceId, String servTypeId, String gw_type,
			String ssid, String templatePara);
}
