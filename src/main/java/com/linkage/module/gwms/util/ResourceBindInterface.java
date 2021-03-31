package com.linkage.module.gwms.util;

import ResourceBind.BindInfo;
import ResourceBind.ResultInfo;
import ResourceBind.UnBindInfo;

/**
 * 调用绑定模块接口
 * @author jiafh (Ailk NO.)
 * @version 1.0
 * @since 2016-11-3
 * @category com.linkage.module.gwms.util
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public interface ResourceBindInterface {
	
	/**
	 * 绑定
	 * @param bindInfo
	 * @return
	 */
	public ResultInfo bind(BindInfo[] bindInfo);
	
	/**
	 * 解绑
	 * @param bindInfo
	 * @return
	 */
	public ResultInfo release(UnBindInfo[] unBindInfo);
	
	/**
	 * 删除用户
	 * <br>
	 * 资源绑定模块无返回结果，所以只要不报错，则认为成功(1)
	 * 
	 * @param userName 用户帐号
	 * @return 删除结果
	 */
	public String delUser(String userName);
	
	/**
	 * 更新内存中的用户信息
	 * 
	 * @param userName 用户帐号
	 * @return 更新结果
	 */
	public String updateUser(String userName);
	
	/**
	 * 更新内存中的设备信息
	 * 
	 * @param userName 设备ID
	 * @return 更新结果
	 */
	public String updateDevice(String deviceid);
	
	/**
	 * 更新内存中的设备信息
	 * 
	 * @param device_id 设备ID
	 * @return 更新结果
	 */
	public String delDevice(String device_id);
	

	/**
	 * 獲取内存中的用户信息
	 * 
	 * @param userName 用户帐号
	 * @return 更新结果
	 */
	public String getUser(String username);
	
	/**
	 * 獲取内存中的设备信息
	 * 
	 * @param userName 设备ID
	 * @return 更新结果
	 */
	public String getDevice(String deviceid);
	
	/**
	 * 调用资源绑定模块
	 * 
	 * @param type user\device   user：用户数据，device：设备数据
	 * @param operate set\get\add\del  set：更新，get：获取，add：增加，del：删除
	 * @param parameter 待操作的具体参数  username或者device_id
	 * @return 操作结果
	 */
	public String doTest(String type,String operate,String parameter);
}
