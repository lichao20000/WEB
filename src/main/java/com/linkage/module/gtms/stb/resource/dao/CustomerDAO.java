
package com.linkage.module.gtms.stb.resource.dao;

import java.util.List;
import java.util.Map;

import com.linkage.module.gtms.stb.resource.dto.CustomerDTO;

/**
 * @author fangchao (Ailk No.)
 * @version 1.0
 * @since 2013-8-26
 * @category com.linkage.module.lims.itv.zeroconf.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public interface CustomerDAO
{

	/**
	 * 根据页面查询条件，统计满足条件的记录条数，用于分页
	 * 
	 * @param dto
	 * @return 返回满足页面查询条件的总记录条数
	 */
	int countCustomer(CustomerDTO dto);

	/**
	 * 根据页面查询条件，查询用户业务信息列表
	 * 
	 * @param dto
	 * @return 返回根据列表页面分页后的列表集合
	 */
	List<Map> queryCustomerList(CustomerDTO dto, int firstRecord, int pageSize);
	
	/**
	 * 根据页面查询条件，导出所有查询结果
	 * @param dto
	 * @return
	 */
	List<Map> exportCustomerList(CustomerDTO dto);
	/**
	 * <pre>
	 * 根据用户ID查询用户信息
	 * 由于一个用户可以关联多个设备，所以需要根据设备ID查询唯一信息
	 * </pre>
	 * @param customerId 用户ID，never null
	 * @param deviceId 设备ID，如果用户未关联设备，则为null
	 * @return 返回用户信息Map
	 */
	Map queryCustomerDetail(String customerId, String deviceId, String status);
	
	Map querySTBCustomerDetail(String serv_account, String deviceId, String status);
	/**
	 * 根据用户关联的设备ID查询设备信息
	 * @param deviceId 设备ID，never null
	 * @return 返回包含设备信息Map
	 */
	Map queryDeviceDetail(String deviceId);
	
	List<Map> queryZeroDetail(String customerId, String deviceId);
	List<Map> queryWorkDetail(String customerId);
	/** 根据loid得到userId */
	String getUserIdByLoid(String loid, String gw_type);
}
