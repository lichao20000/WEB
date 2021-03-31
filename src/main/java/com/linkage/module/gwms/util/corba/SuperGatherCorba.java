/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package com.linkage.module.gwms.util.corba;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.init.bio.AppInitBIO;

import SuperGather.GatherParam;
import SuperGather.GatherResult;
import SuperGather.SuperGatherManager;



/**
 * CORBA operation for SuperGather.
 * 
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Jun 21, 2009
 * @see
 * @since 1.0
 */
public class SuperGatherCorba {
	
	/** log */
	private static Logger logger = LoggerFactory.getLogger(SuperGatherCorba.class);

	private SuperGatherManager G_SuperGatherManager = null;
	
	private String gw_type;
	
	/**
	 * 无参构造函数
	 */
	public SuperGatherCorba(){
	}
	
	/**
	 * 有参构造函数
	 * @param gw_type
	 */
	public SuperGatherCorba(String gw_type){
		
		this.gw_type = gw_type;
		
		if(Global.GW_TYPE_ITMS.equals(gw_type)){
			G_SuperGatherManager = Global.G_SuperGatherManager_itms;
		} else {
			G_SuperGatherManager = Global.G_SuperGatherManager_bbms;
		}
	}
	/**
	 * 更改指引  用于二次获取cobar
	 * @param type
	 */
	private void changeManager(String type)
	{
		if(Global.GW_TYPE_ITMS.equals(type)){
			G_SuperGatherManager = Global.G_SuperGatherManager_itms;
		} else {
			G_SuperGatherManager = Global.G_SuperGatherManager_bbms;
		}
	}
	
	/**
	 * 设备信息重新采集.
	 * 
	 * @param deviceIdArr
	 * @param type
	 */
	public void gatherCpeParams(String[] deviceIdArr, int type) {
		logger.debug("gatherCpeParams({},{})", deviceIdArr, type);
		//准实时,多次采集直接报成功
		gatherCpeParams(deviceIdArr, type, 0);
	}

	/**
	 * 设备信息重新采集.
	 * 
	 * @param deviceId
	 * @param type
	 */
	public void gatherCpeParams(String deviceId, int type) {
		logger.debug("gatherCpeParams({},{})", deviceId, type);
		//准实时,多次采集直接报成功
		gatherCpeParams(deviceId, type, 0);
	}

	/**
	 * 获取设备采集信息.
	 * 
	 * @param deviceIdArr
	 * @param type
	 */
	public GatherResult[] getCpeParams(String[] deviceIdArr, int type) {
		logger.debug("getCpeParams({},{})", deviceIdArr, type);
		//准实时,多次采集直接报成功
		return getCpeParams(deviceIdArr, type, 0);
	}

	/**
	 * 获取设备采集信息.
	 * 
	 * @param deviceId
	 * @param type
	 */
	public int getCpeParams(String deviceId, int type) {
		logger.debug("getCpeParams({},{})", deviceId, type);
		//准实时,多次采集直接报成功
		return getCpeParams(deviceId, type, 0);
	}
	

	
	/**
	 * 设备信息重新采集.
	 * 
	 * @param deviceIdArr deviceIds数组, type 采集结点, invokeType是否实时采集
	 * @param type
	 */
	public void gatherCpeParams(String[] deviceIdArr, int type, int invokeType) {
		logger.debug("gatherCpeParams({},{})", deviceIdArr, type);

		if (deviceIdArr == null || deviceIdArr.length == 0) {
			logger.error("deviceIdArr == null");

			return;
		}

		try {
			G_SuperGatherManager.getCpeParams(
					createGatherParam(deviceIdArr, type, invokeType));
		} catch (Exception e) {
			logger.warn("CORBA SuperGather Error:{},Rebind.", e.getMessage());

			AppInitBIO.initSuperGather(this.gw_type);
			changeManager(this.gw_type);
			try {
				G_SuperGatherManager.getCpeParams(
						createGatherParam(deviceIdArr, type, invokeType));
			} catch (RuntimeException e1) {
				logger.error("CORBA SuperGather Error:{}", e1.getMessage());
			}
		}
	}

	/**
	 * 设备信息重新采集.
	 * 
	 * @param deviceId deviceId, type 采集结点, invokeType是否实时采集
	 * @param type
	 */
	public void gatherCpeParams(String deviceId, int type, int invokeType) {
		logger.debug("gatherCpeParams({},{})", deviceId, type);

		if (deviceId == null) {
			logger.error("deviceIdArr == null");

			return;
		}

		String[] deviceIdArr = new String[] { deviceId };

		try {
			G_SuperGatherManager.getCpeParams(
					createGatherParam(deviceIdArr, type, invokeType));
		} catch (Exception e) {
			logger.warn("CORBA SuperGather Error:{},Rebind.", e.getMessage());

			AppInitBIO.initSuperGather(this.gw_type);
			changeManager(this.gw_type);
			try {
				G_SuperGatherManager.getCpeParams(
						createGatherParam(deviceIdArr, type, invokeType));
			} catch (RuntimeException e1) {
				logger.error("CORBA SuperGather Error:{}", e1.getMessage());
			}
		}
	}

	/**
	 * 获取设备采集信息.
	 * 
	 * @param deviceIdArr deviceIds数组, type 采集结点, invokeType是否实时采集
	 * @param type
	 */
	public GatherResult[] getCpeParams(String[] deviceIdArr, int type, int invokeType) {
		logger.debug("getCpeParams({},{})", deviceIdArr, type);

		GatherResult[] resultArr = null;

		if (deviceIdArr == null || deviceIdArr.length == 0) {
			logger.error("deviceIdArr == null");

			return resultArr;
		}

		try {
			resultArr = G_SuperGatherManager.getCpeParams(
					createGatherParam(deviceIdArr, type, invokeType));
		} catch (Exception e) {
			logger.warn("CORBA SuperGather Error:{},Rebind.", e.getMessage());

			AppInitBIO.initSuperGather(this.gw_type);
			changeManager(this.gw_type);
			try {
				resultArr = G_SuperGatherManager.getCpeParams(
						createGatherParam(deviceIdArr, type, invokeType));
			} catch (RuntimeException e1) {
				logger.error("CORBA SuperGather Error:{}", e1.getMessage());
			}
		}

		return resultArr;
	}

	/**
	 * 获取设备采集信息.
	 * 
	 * @param deviceId deviceId, type 采集结点, invokeType是否实时采集
	 * @param type
	 */
	public int getCpeParams(String deviceId, int type, int invokeType) {
		logger.debug("getCpeParams({},{})", deviceId, type);
		int result = -9;

		if (deviceId == null) {
			logger.error("deviceId == null");

			return result;
		}

		GatherResult[] resultArr = null;
		String[] deviceIdArr = new String[] { deviceId };

		try {
			resultArr = G_SuperGatherManager.getCpeParams(
					createGatherParam(deviceIdArr, type, invokeType));
		} catch (Exception e) {
			logger.warn("CORBA SuperGather Error:{},Rebind.", e.getMessage());
		
			AppInitBIO.initSuperGather(this.gw_type);
			try {
				//更改变量指引
				changeManager(this.gw_type);
				resultArr = G_SuperGatherManager.getCpeParams(
						createGatherParam(deviceIdArr, type, invokeType));
			} catch (Exception e1) {
				logger.error("CORBA SuperGather Error:{}", e1.getMessage());
			}
		}
		
		if (resultArr != null) {
			result = resultArr[0].result;
			resultArr = null;
		}
		return result;
	}
	
	/**
	 * 生成SuperGather模块的调用对象
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-8-20
	 * @return GatherParam
	 */
	public GatherParam createGatherParam(String[] deviceIdArr, int paramType, int invokeType){
		logger.debug("createGatherParam({},{},{})", new Object[] {deviceIdArr, paramType, invokeType});
		if(null == deviceIdArr){
			logger.warn("deviceIdArr is null");
			return null;
		}
		GatherParam gatherParam = new GatherParam();
		gatherParam.deviceIdArr = deviceIdArr;
		gatherParam.invokeType = invokeType;
		gatherParam.paramType = paramType;
		return gatherParam;
	}
}
