package com.linkage.module.gtms.resource.action;



public interface QueryVoipPhoneByLoidAction {
	
	/**
	 * 初始化界面
	 * @return
	 */
	public String execute();
	
	/**
	 * 根据LOID查询VOIP语音电话号码
	 * @return
	 */
	public String queryVoipPhoneByLoid();
	
	/**
	 * 下载Excel模板
	 * @return
	 */
	public String downloadTemplate();
	
	
}
