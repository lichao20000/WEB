package com.linkage.module.gtms.resource.action;


public interface CountDeviceByServTypeIdAction 
{
	
	
	/**
	 * 统计已开通业务
	 * @return
	 */
	public String countHaveOpenningService();
	
	public String QueryCountHaveOpenService();
	
	public String countHaveOpenService();
	
	/**
	 * 统计未开通业务
	 * @return
	 */
	public String countHaveNotOpenningService();
	
	/**
	 * 数据导出到Excel
	 * @return
	 */
	public String getExcel();
	
	/**
	 * 详细信息展示
	 * @return
	 */
	public String getDetail();
	
	/**
	 * 详细信息导出
	 * @return
	 */
	public String getDetailExcel();
	
}
