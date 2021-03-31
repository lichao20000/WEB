package com.linkage.module.gtms.stb.resource.serv;

import java.util.List;
import java.util.Map;

import com.linkage.module.gtms.stb.resource.dto.CustomerDTO;

/**
 * 
 * @author fangchao (Ailk No.)
 * @version 1.0
 * @since 2013-8-26
 * @category com.linkage.module.lims.itv.zeroconf.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public interface CustomerBIO
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
	 * 
	 * @param dto
	 * @return 返回根据列表页面分页后的列表集合
	 */
	List<Map> exportCustomerList(CustomerDTO dto);
	
	/**
	 * <pre>
	 * 查询用户页面页面详情
	 * 一个用户可以对应多个设备，查询用户和设备信息，需要根据用户和设备两个条件查询
	 * </pre>
	 * @param customerId 用户ID
	 * @param deviceId 用户关联的设备ID，有可能为null
	 * @return 返回包含用户信息Map
	 */
	Map queryCustomerDetail(String customerId, String deviceId, String status);
	
	Map querySTBCustomerDetail(String serv_account, String deviceId, String status);
	
	List<Map> queryZeroDetail(String customerId, String deviceId);
	List<Map> queryWorkDetail(String customerId);
	/** 根据loid得到userId */
	String getUserIdByLoid(String loid, String gw_type);
}
