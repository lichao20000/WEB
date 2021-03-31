package com.linkage.module.gtms.config.action;

public interface WirelessConfigAction {
	/**
	 * 根据设备查询用户信息中的ssid2Status字段是否存在 ；
	 * @return
	 */
	public String  getUserInfo();
	/**
	 * 配置ssid2Status
	 * @return
	 */
	public String doConfig();
}
