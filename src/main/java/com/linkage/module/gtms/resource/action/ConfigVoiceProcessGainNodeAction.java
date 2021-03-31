package com.linkage.module.gtms.resource.action;


public interface ConfigVoiceProcessGainNodeAction {
	
	/**
	 * 初始化界面
	 * @return
	 */
	public String init();
	
	/**
	 * 调ACS实时获取设备上的语音增益节点
	 * 
	 * @return
	 */
	public String getGainNode();
	
	/**
	 * 调ACS给设备下发增益节点
	 * 
	 * @return
	 */
	public String doConfigGainNode();
	
}
