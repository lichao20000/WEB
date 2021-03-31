/**
 * 
 */
package com.linkage.module.gwms.report.bio.interf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-6-26
 * @category com.linkage.module.gwms.report.bio.interf
 * 
 */
public interface I_OnlineDevStatBIO {

	/**
	 * @author qxq
	 * @category 
	 * 
	 * @param chartType
	 * @param city_id
	 * @param startTime
	 * @param startEnd
	 * @return
	 */
	public String getDataOnLine(String isReport,String reportType,String chartType,String city_id, long startTime, long startEnd);
	
	/**
	 * @author qxq
	 * @category 
	 * 
	 * @param chartType
	 * @param city_id
	 * @param startTime
	 * @param startEnd
	 * @param r_timepoint
	 * @return
	 */
	public String getMonthOnLine(String isReport,String reportType,String chartType,String city_id, long startTime,
			long startEnd, String r_timepoint);
	
	/**
	 * @category 折线图
	 * 
	 * @param title 标题
	 * @param X_coordinate_name 纵坐标名称
	 * @param Y_coordinate_name 横坐标名称
	 * @param X_coordinate 纵坐标
	 * @param Y_coordinate 横坐标
	 * @param data 数据
	 * 
	 * @return
	 */
	public String createLineChartByLinkage(String title,
			String X_coordinate_name, String Y_coordinate_name,
			String X_coordinate[], String Y_coordinate[], double[][] data);

	/**
	 * 获取所有属地
	 * 
	 * @param city_id
	 * @return
	 */
	public List getChildCity(String city_id) ;
	
	/**
	 * @author qxq
	 * @category 
	 * 
	 * @param chartType
	 * @param city_id
	 * @param startTime
	 * @param startEnd
	 * @return
	 */
	public ArrayList<Map> getDataOnLineData(String reportType,String chartType,String city_id, long startTime,
			long startEnd);
	
	/**
	 * @author qxq
	 * @category 
	 * 
	 * @param chartType
	 * @param city_id
	 * @param startTime
	 * @param startEnd
	 * @param r_timepoint
	 * @return
	 */
	public ArrayList<Map> getMonthOnLineData(String reportType,String chartType,String city_id, long startTime,
			long startEnd, String r_timepoint);
}
