package com.linkage.module.gtms.resource.serv;



public interface ConfigVoiceProcessGainNodeServ {
	
	/**
	 * 根据deviceId，调用ACS获取语音的呼入、呼出增益节点
	 * 
	 * @param deviceId
	 * @param gw_type
	 * @param node_1
	 * @param node_2
	 * @param node_3
	 * @return
	 */
	public String getGainNode(String deviceId, String gw_type, String node_1, String node_2, String node_3);
	
	
	/**
	 * 下发增益节点值
	 * @param deviceId
	 * @param gw_type
	 * @param gainValue
	 * @return
	 */
	public String doConfigGainNode(String deviceId, String gw_type, String gainValue);
	
}
