package com.linkage.module.gtms.config.action;

public interface VOIPDigitMapBatchAction {
	/**
	 * 查询语音数图模版
	 * @return  语音数图模版集合
	 */
	public String init();
	/**
	 * 下发配置是否成功
	 * @return 
	 */
	public String doConfigAll();
	public String download();
	public String getCountAll();
	public String execute();
}
