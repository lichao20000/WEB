package com.linkage.module.gtms.config.serv;

public interface ServerConfigServ {
	
	public String doConfigAll(String serverConfig, String isOpenDHCP, String isRelay,
			String minAddress, String maxAddress, String reservedAddresses,
			String subnetMask, String dNSServers,  String dHCPLeaseTime,String deviceId, String gw_type);

	public String maintainAccConifig(String deviceId, String gw_type,
			String adminName, String adminPassword);

	public String igmpConifig(String deviceId, String gw_type,
			String iGMPSnoopingEnable);

	public String algConifig(String deviceId, String gw_type, String ftpEnable,
			String sipEnable);

	public String wlanConfig(String deviceId, String gw_type, String wlanEnable);

}
