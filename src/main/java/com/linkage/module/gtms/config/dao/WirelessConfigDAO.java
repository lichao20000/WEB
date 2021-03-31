package com.linkage.module.gtms.config.dao;

import java.util.List;

public interface WirelessConfigDAO {

	public String getUserInfo(String deviceId);

    public 	void doConfig(String deviceId);

	public List isExists(String username,String gwType);
	
	public String getLoid(String username,String gwType);

}
