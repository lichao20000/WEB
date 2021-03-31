package com.linkage.module.gwms.util;

import PreProcess.OneToMany;
import PreProcess.UserInfo;

import com.linkage.module.gwms.util.corba.obj.PreServInfoOBJ;

/**
 * 调用配置模块公用接口
 * @author Administrator (Ailk NO.)
 * @version 1.0
 * @since 2016-11-3
 * @category com.linkage.module.gwms.util
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public interface PreProcessInterface {

	/**
	 * 生成新的策略.
	 * 
	 * @param idArr
	 *            the id of strategy.
	 */
	public boolean processOOBatch(String[] idArr);

	/**
	 * 生成新的策略.
	 * 
	 * @param idArr
	 *            the id of strategy.
	 */
	public boolean processOOBatch(String id);

	/**
	 * 生成新的策略.
	 * 
	 * @param idArr
	 *            the id of strategy.
	 */
	public boolean processOOBatch_stb(String[] idArr, String serviceId);
	
	
	/**
	 * 默认业务生成新的策略.
	 * 
	 * @param idArr
	 *            the id of strategy.
	 */
	public boolean processOMBatch4DefaultService(OneToMany[] objArr);

	/**
	 * 默认业务生成新的策略.
	 * 
	 * @param idArr
	 * the id of strategy.
	 */
	public boolean processOMBatch4DefaultService(OneToMany obj);

	
	/**
	 * 绑定设备后通知PP.
	 * 
	 * @param userInfoArr
	 * @return <li>1:成功</li> <li>-1:参数为空</li> <li>-2:绑定失败</li>
	 */
	public int processServiceInterface(UserInfo userInfo);

	/**
	 * 绑定设备后通知PP.(STB专用)
	 * 
	 * @param userInfoArr
	 * @return <li>1:成功</li> <li>-1:参数为空</li> <li>-2:绑定失败</li>
	 */
	public int processServiceInterface_STB(String xmlString);
	
	
	/**
	 * 绑定设备后通知PP.
	 * 
	 * @param userInfoArr
	 * @return <li>1:成功</li> <li>-1:参数为空</li> <li>-2:绑定失败</li>
	 */
	public int processServiceInterface(UserInfo[] userInfoArr);
	
	/**
	 * get userInfo from params
	 * 
	 * @param length
	 * @return
	 */
	public UserInfo GetPPBindUserList(PreServInfoOBJ preInfoObj);
	
	/**
	 * 长短定时器配置
	 * 
	 * @param userInfoArr
	 * @return <li>1:成功</li> <li>-1:参数为空</li> <li>-2:失败</li>
	 */
	public int processDeviceStrategy(String[] deviceIds, String serviceId,String[] paramArr);
	
	/**
	 * 批量软件升级，需要将task——id传到配置模块
	 * @param deviceIds
	 * @param serviceId
	 * @param paramArr
	 * @param taskId
	 * @return
	 */
	public int processDeviceStrategy(String[] deviceIds, String serviceId, String[] paramArr,
			String taskId);
	
	/**
	 * 更新配置模块缓存策略信息
	 * @param deviceIdArr
	 * @param alias
	 * @return
	 */
	public int processSetStrategyMem(String[] deviceIdArr,String serviceId);

}
