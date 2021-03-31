/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */

package com.linkage.module.gwms.util.corba;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.init.bio.AppInitBIO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * <pre>
 * web融合机顶盒，由于机顶盒调用corba不同于家庭网关，故封装该类。
 * 1.由于机顶盒与其他家庭网关corba接口不同，该类针对gw_type=4(机顶盒)类型进行封装。
 * 2.该类在同包内访问权限，对于client调用仍使用PreProcessCorba调用,
 *     该类和PreProcessCorba具有相同的方法接口(但没有定义接口)。
 * 3.该类是线程安全的。
 * </pre>
 * 
 * @see PreProcessCorba
 * @author fangchao (Ailk No.)
 * @version 1.0
 * @since 2013-11-28
 * @category com.linkage.module.gwms.util.corba
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class PreProcessCorba4Stb
{

	private static Logger logger = LoggerFactory
			.getLogger(PreProcessCorba4Stb.class);

	public PreProcessCorba4Stb()
	{
	}

	public boolean processOOBatch(String StrategyIdArr)
	{
		if (StringUtil.IsEmpty(StrategyIdArr))
		{
			return false;
		}
		return processOOBatch(new String[] { StrategyIdArr });
	}

	/**
	 * 生成新的策略.
	 * 
	 * @param idArr
	 *            the id of strategy.
	 */
	public boolean processOOBatch(String[] idArr)
	{
		logger.warn("CORBA PreProcess");
		if (idArr == null || idArr.length == 0)
		{
			return false;
		}
		
		try
		{
			Global.G_CMManager_stb.processOOBatch(idArr);
			return true;
		}
		catch (Exception e)
		{
			logger.warn("CORBA PreProcess Error, Rebinding.", e);
		}
		
		try
		{
			AppInitBIO.initPreProcess(Global.GW_TYPE_STB);
			Global.G_CMManager_stb.processOOBatch(idArr);
			return true;
		}
		catch (Exception e1)
		{
			logger.error("CORBA SuperGather Error", e1);
		}
		
		return false;
	}

	/**
	 * 长短定时器配置
	 * 
	 * @param userInfoArr
	 * @return <li>1:成功</li> <li>-1:参数为空</li> <li>-2:失败</li>
	 */
	public int processDeviceStrategy(String[] deviceIds, String serviceId,
			String[] paramArr)
	{
		if (deviceIds == null || deviceIds.length == 0)
		{
			return -1;
		}
		try
		{
			Global.G_CMManager_stb.processDeviceStrategy(deviceIds, serviceId, paramArr);
		}
		catch (Exception e)
		{
			logger.warn("CORBA PreProcess Error, bind.", e);
			try
			{
				AppInitBIO.initPreProcess(Global.GW_TYPE_STB);
				Global.G_CMManager_stb.processDeviceStrategy(deviceIds, serviceId, paramArr);
			}
			catch (Exception e1)
			{
				logger.error("CORBA PreProcess Error, Rebind." + e1.getMessage(), e1);
				return -2;
			}
		}
		
		return 1;
	}

	public int processServiceInterface(String xmlstr) {
		if (StringUtil.IsEmpty(xmlstr)) {
			logger.error("xmlstr == null");
			return -2;
		}
		try {
			Global.G_CMManager_stb.processServiceInterface(xmlstr);
		} catch (Exception e) {
			logger.warn("CORBA PreProcess Error, bind.", e);
			try
			{
				AppInitBIO.initPreProcess(Global.GW_TYPE_STB);
				Global.G_CMManager_stb.processServiceInterface(xmlstr);
			}
			catch (Exception e1)
			{
				logger.error("CORBA PreProcess Error, Rebind." + e1.getMessage(), e1);
				return -2;
			}
		}
		return 1;
	}
}
