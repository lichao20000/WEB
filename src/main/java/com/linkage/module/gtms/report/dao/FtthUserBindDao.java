package com.linkage.module.gtms.report.dao;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author Administrator(工号) Tel:
 * @version 1.0
 * @since Apr 25, 2012 3:03:29 PM
 * @category com.linkage.module.gtms.report.dao
 * @copyright 南京联创科技 网管科技部
 *
 */
public interface FtthUserBindDao {
	
	/**
	 * 所有工单用户数（全量FTTH用户）   
	 * @param cityId
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public Map<String, String> getAllFtthUser(String cityId, String starttime, String endtime,String isFiber);
	
	/**
	 * 绑定用户数
	 * 
	 * @param cityId
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public Map<String, String> getBindFtthUser(String cityId, String starttime, String endtime,String isFiber);
	
	/**
	 * 获取用户列表
	 * 
	 * @param bindFlag  0 表示获取所有用户列表  1 表示获取已绑定用户列表 
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> getUserList(String bindFlag, String cityId, String starttime1,
			String endtime1, int curPage_splitPage, int num_splitPage ,String isFiber);
	
	/**
	 * 获取用户列表
	 * 
	 * @param bindFlag  0 表示获取所有用户列表  1 表示获取已绑定用户列表 
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int getUserCount(String bindFlag, String cityId, String starttime1,
			String endtime1, int curPage_splitPage, int num_splitPage ,String isFiber);
	
	/**
	 * 导出Excel
	 * 
	 * @param bindFlag  0 表示获取所有用户列表  1 表示获取已绑定用户列表
	 * @param cityId
	 * @param starttime1
	 * @param endtime1
	 * @return
	 */
	public List<Map> getUserExcel(String bindFlag, String cityId, String starttime1,
			String endtime1 ,String isFiber);
	/**
	 * 所有工单用户数（全量FTTH用户）
	 * @param cityId
	 * @param starttime
	 * @param endtime
	 * @return
	 */

}
