package com.linkage.module.gtms.blocTest.action;


public interface ApDeviceSoftUpGradeAction {
	/**
	 * 首页初始化
	 * @return
	 */
	public String init();
	/**
	 * AP应用终端软件升级
	 * 
	 * @return
	 */
	public String doConfig();
	/**
	 * 获取目标版本的下拉框列表
	 * @return
	 */
	public String getSelectListBox();
}
