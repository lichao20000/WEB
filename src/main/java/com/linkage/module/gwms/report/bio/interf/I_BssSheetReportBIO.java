/**
 * 工单统计
 */
package com.linkage.module.gwms.report.bio.interf;

import java.util.List;
import java.util.Map;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-6-11
 * @category bio.report
 * 
 */
@SuppressWarnings("unchecked")
public interface I_BssSheetReportBIO {

	/**
	 * @category 查询所有的属地，查询传入参数的子属地
	 * 
	 * @param cityId
	 * @return
	 */
	public List getAllCity(String cityId);
	
	/**
	 * 获取属地父子关系
	 * 
	 * @return
	 */
	public Map getCityPC();
	
	/**
	 * @查询报表数据
	 * 
	 * @param city_id
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List getReportData(String cityId,long startDate,long endDate);
	
	/**
	 * 取得表头
	 */
	public String[] getTbTitle();
	
	/**
	 * 取得列名
	 */
	public String[] getTbName();
}
