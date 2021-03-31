package com.linkage.module.gtms.resource.dao;

import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public interface CountDeviceByServTypeIdDAO 
{
	
	/**
	 * 统计已开通业务
	 * 
	 * @param gw_type<br>设备类型
	 * @param city_id<br>当前登录者的属地
	 * @return
	 */
	public List<Map> countHaveOpenningService(String gw_type, String city_id);
	
	/**
	 * 统计未开通业务
	 * @param gw_type<br>设备类型
	 * @param city_id<br>当前登录者的属地
	 * @return
	 */
	public List<Map> countHaveNotOpenningService(String gw_type, String city_id);
	
	/**
	 * 详细信息展示
	 * @param cityId
	 * @param gw_type
	 * @param servTypeId
	 * @param isOpen
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param etime 
	 * @param stime 
	 * @return
	 */
	public List<Map> getDetail(String cityId, String gw_type, String servTypeId,
			String isOpen, int curPage_splitPage, int num_splitPage, long stime, long etime);
	
	/**
	 * 用于分页
	 * @param cityId
	 * @param gw_type
	 * @param servTypeId
	 * @param isOpen
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param etime 
	 * @param stime 
	 * @return
	 */
	public int getCount(String cityId, String gw_type, String servTypeId,
			String isOpen, int curPage_splitPage, int num_splitPage, long stime, long etime);
	
	/**
	 * 详细信息导出
	 * @param cityId
	 * @param gw_type
	 * @param servTypeId
	 * @param isOpen
	 * @param etime 
	 * @param stime 
	 * @return
	 */
	public List<Map> getDetailExcel(String cityId, String gw_type, String servTypeId,
			String isOpen, long stime, long etime);

	/**
	 * 统计家庭网关宽带业务或VOIP业务已开通业务量
	 * 宁夏特制
	 */
	public List<Map> countHaveOpenService(String gw_type, String cityId,
			String servTypeId, long startTime, long endTime,String type);
}
