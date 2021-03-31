package com.linkage.module.gtms.blocTest.serv;


public interface ApDeviceSoftUpGradeServ {
	
	/**
	 * Ap软件升级
	 * 
	 * @param deviceId
	 * @param templatePara
	 * @param gw_type
	 * @param filePath
	 * @return
	 */
	public String doConfig(String deviceId, String templatePara, String gw_type, String filePath);
	/**
	 * 获取目标版本文件下拉框
	 * 
	 * @return
	 */
	public String getSelectListBox();
}
