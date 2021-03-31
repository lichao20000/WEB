package com.linkage.module.gtms.resource.serv;

import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public interface CountDeviceByServTypeIdServ 
{
	
	
	/**
	 * 统计已开通业务
	 * @param gw_type 设备类型
	 * @param city_id 属地
	 * @return
	 */
	public List<Map> countHaveOpenningService(String gw_type, String city_id);
	
	/**
	 * 统计未开通业务
	 * @param gw_type   设备类型
	 * @param city_id   属地
	 * @return
	 */
	public List<Map> countHaveNotOpenningService(String gw_type, String city_id);
	
	/**
	 * 详细信息展示
	 * @param cityId   属地
	 * @param gw_type  设备类型
	 * @param servTypeId   业务类型
	 * @param isOpen   用于区分 展示已开通业务信息，还是展示未开通业务信息
	 * @param m 
	 * @param l 
	 * @return
	 */
	public List<Map> getDetail(String cityId, String gw_type, String servTypeId, String isOpen, int curPage_splitPage, int num_splitPage, long l, long m);
	
	/**
	 * 用于分页
	 * @param cityId   属地
	 * @param gw_type  设备类型
	 * @param servTypeId   业务类型
	 * @param isOpen   用于区分 展示已开通业务信息，还是展示未开通业务信息
	 * @param m 
	 * @param l 
	 * @return
	 */
	public int getCount(String cityId, String gw_type, String servTypeId, String isOpen, int curPage_splitPage, int num_splitPage, long l, long m);
	
	/**
	 * 详细信息导出
	 * @param cityId
	 * @param gw_type
	 * @param servTypeId
	 * @param isOpen
	 * @param m 
	 * @param l 
	 * @return
	 */
	public List<Map> getDetailExcel(String cityId, String gw_type, String servTypeId, String isOpen, long l, long m);

	/**
	 * 统计家庭网关宽带业务或VOIP业务已开通业务量
	 * 宁夏特制
	 */
	public List<Map> countHaveOpenService(String gw_type, String cityId,
			String servTypeId, long longTime, long longTime2,String type);
	
}
