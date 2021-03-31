
package com.linkage.module.gtms.stb.resource.serv;

import java.util.List;
import java.util.Map;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gtms.stb.resource.dao.CustomerDAO;
import com.linkage.module.gtms.stb.resource.dto.CustomerDTO;


/**
 * @author fangchao (Ailk No.)
 * @version 1.0
 * @since 2013-8-26
 * @category com.linkage.module.lims.itv.zeroconf.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class CustomerBIOImpl implements CustomerBIO
{

	private CustomerDAO customerDAO;

	/**
	 * 根据页面查询条件，统计满足条件的记录条数，用于分页
	 * 
	 * @param dto
	 * @return 返回满足页面查询条件的总记录条数
	 */
	public int countCustomer(CustomerDTO dto)
	{
		return customerDAO.countCustomer(dto);
	}

	/**
	 * 根据页面查询条件，查询用户业务信息列表
	 * 
	 * @param dto
	 * @return 返回根据列表页面分页后的列表集合
	 */
	public List<Map> queryCustomerList(CustomerDTO dto, int firstRecord, int pageSize)
	{
		return customerDAO.queryCustomerList(dto, firstRecord, pageSize);
	}
	
	/**
	 * 根据页面查询条件，导出所有查询结果
	 */
	public List<Map> exportCustomerList(CustomerDTO dto)
	{
		return customerDAO.exportCustomerList(dto);
	}

	/**
	 * <pre>
	 * 查询用户页面页面详情
	 * 1.根据用户ID查询用户信息
	 * 2.如果用户信息中，没有关联设备，则不查询设备信息
	 * 3.如果关联设备，则根据关联的设备ID，查询设备信息
	 * 4.一个用户可以对应多个设备，查询用户和设备信息，需要根据用户和设备两个条件查询
	 * </pre>
	 * 
	 * @param customerId
	 *            用户ID
	 * @param deviceId
	 *            用户关联的设备ID，有可能为null
	 * @return 返回包含用户信息Map
	 */
	public Map queryCustomerDetail(String customerId, String deviceId, String status)
	{
		Map customerMap = customerDAO.queryCustomerDetail(customerId, deviceId, status);
		if (!StringUtil.IsEmpty(deviceId))
		{
			Map deviceMap = customerDAO.queryDeviceDetail(deviceId);
			customerMap.putAll(deviceMap);
		}
		return customerMap;
	}
	
	public Map querySTBCustomerDetail(String serv_account, String deviceId, String status)
	{
		Map customerMap = customerDAO.querySTBCustomerDetail(serv_account, deviceId, status);
		if (!StringUtil.IsEmpty(deviceId))
		{
			Map deviceMap = customerDAO.queryDeviceDetail(deviceId);
			customerMap.putAll(deviceMap);
		}
		return customerMap;
	}
	
	@Override
	public List<Map> queryZeroDetail(String customerId, String deviceId) {
		return customerDAO.queryZeroDetail(customerId,deviceId);
	}
	
	public List<Map> queryWorkDetail(String customerId) {
		return customerDAO.queryWorkDetail(customerId);
	}

	public void setCustomerDAO(CustomerDAO customerDAO)
	{
		this.customerDAO = customerDAO;
	}

	/** 根据loid得到userId */
	@Override
	public String getUserIdByLoid(String loid, String gw_type) {
		return customerDAO.getUserIdByLoid(loid,gw_type);
	}
}
