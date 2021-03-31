package com.linkage.module.gtms.report.serv;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author Administrator(工号) Tel:
 * @version 1.0
 * @since Apr 25, 2012 3:44:02 PM
 * @category com.linkage.module.gtms.report.serv
 * @copyright 南京联创科技 网管科技部
 *
 */
public interface FtthUserBindServ {
	
	/**
	 * 统计首页
	 * 
	 * @param cityId
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public List<Map> countAll(String cityId, String starttime, String endtime,String isFiber);
	
	/**
	 * 用户列表
	 * 
	 * @param bindFlag
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> getUserList(String bindFlag, String cityId, String starttime1, String endtime1, int curPage_splitPage, int num_splitPage,String isFiber);
	
	/**
	 * 用于分页
	 * 
	 * @param bindFlag
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int getUserCount(String bindFlag, String cityId, String starttime1, String endtime1, int curPage_splitPage, int num_splitPage,String isFiber);
	
	/**
	 * 导出Excel
	 * 
	 * @param bindFlag
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @return
	 */
	public List<Map> getUserExcel(String bindFlag, String cityId, String starttime1, String endtime1,String isFiber);

}
