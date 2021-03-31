package com.linkage.module.gtms.service.action;


public interface SendRouterOpenSheetAction {
	
	/**
	 * 发送模拟工单
	 * 
	 * 1：开户    3：销户
	 * 
	 * @return
	 */
	public String sendSheet();
	
	public String init();
}
