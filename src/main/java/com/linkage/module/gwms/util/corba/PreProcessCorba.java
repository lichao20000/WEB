/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package com.linkage.module.gwms.util.corba;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import PreProcess.OneToMany;
import PreProcess.PPManager;
import PreProcess.UserInfo;

import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.init.bio.AppInitBIO;
import com.linkage.module.gwms.util.PreProcessInterface;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.gwms.util.corba.obj.PreServInfoOBJ;

/**
 * <pre>
 * CORBA operation for PreProcess.
 * history：fangchao 2013-11-28
 * ITMS融合机顶盒，封装该类，支持机顶盒CORBA调用。
 * 由于机顶盒CORBA的特殊性，故封装PreProcessCorba4Stb类，但对外仍保留该方法调用
 * <pre>
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Jun 21, 2009
 * @see PreProcessCorba4Stb
 * @since 1.0
 */
public class PreProcessCorba implements PreProcessInterface{
	/** log */
	private static Logger logger = LoggerFactory.getLogger(PreProcessCorba.class);

	private PPManager G_PPManager = null;
	
	private PreProcessCorba4Stb stbCorba = null;
	
	private String gw_type;
	
	public PreProcessCorba()
	{
	}
	
	public PreProcessCorba(String gw_type){
		this.gw_type = gw_type;
		
		if (Global.GW_TYPE_STB.equals(gw_type))
		{
			stbCorba = new PreProcessCorba4Stb();
		}
		else if(Global.GW_TYPE_ITMS.equals(gw_type))
		{
			G_PPManager = Global.G_PPManager_itms;
		}
		else
		{
			G_PPManager = Global.G_PPManager_bbms;
		}
	}
	
	/**
	 * 更改指引  用于二次获取cobar
	 * @param type
	 */
	private void changeManager(String type)
	{
		
		logger.warn("更改指引！！！用于二次获取cobar");
		if (Global.GW_TYPE_STB.equals(gw_type))
		{
			stbCorba = new PreProcessCorba4Stb();
		}
		else if(Global.GW_TYPE_ITMS.equals(gw_type))
		{
			G_PPManager = Global.G_PPManager_itms;
		}
		else
		{
			G_PPManager = Global.G_PPManager_bbms;
		}
	}
	/**
	 * 生成新的策略.
	 * 
	 * @param idArr
	 *            the id of strategy.
	 */
	public boolean processOOBatch(String[] idArr) {
		if (Global.GW_TYPE_STB.equals(gw_type))
		{
			return stbCorba.processOOBatch(idArr);
		}
		
		boolean flag = false;

		if (idArr == null || idArr.length == 0) {
			logger.error("idArr == null");

			return flag;
		}

		try {
			G_PPManager.processOOBatch(idArr);
			flag = true;
		} catch (Exception e) {
			logger.warn("CORBA PreProcess Error:{},Rebind.", e.getMessage());

			AppInitBIO.initPreProcess(this.gw_type);
			changeManager(this.gw_type);
			try {
				G_PPManager.processOOBatch(idArr);
				flag = true;
			} catch (RuntimeException e1) {
				logger.error("CORBA SuperGather Error:{}", e1.getMessage());
			}
		}

		return flag;
	}
	
	/**
	 * 生成新的策略.
	 * 
	 * @param idArr
	 *            the id of strategy.
	 */
	public boolean processOOBatch_stb(String[] idArr, String serviceId) {
		if (Global.GW_TYPE_STB.equals(gw_type))
		{
			return stbCorba.processOOBatch(idArr);
		}
		
		boolean flag = false;

		if (idArr == null || idArr.length == 0) {
			logger.error("idArr == null");

			return flag;
		}

		try {
			G_PPManager.processOOBatch(idArr);
			flag = true;
		} catch (Exception e) {
			logger.warn("CORBA PreProcess Error:{},Rebind.", e.getMessage());

			AppInitBIO.initPreProcess(this.gw_type);
			changeManager(this.gw_type);
			try {
				G_PPManager.processOOBatch(idArr);
				flag = true;
			} catch (RuntimeException e1) {
				logger.error("CORBA SuperGather Error:{}", e1.getMessage());
			}
		}

		return flag;
	}

	/**
	 * 生成新的策略.
	 * 
	 * @param idArr
	 *            the id of strategy.
	 */
	public boolean processOOBatch(String id) {
		if (Global.GW_TYPE_STB.equals(gw_type))
		{
			return stbCorba.processOOBatch(id);
		}

		boolean flag = false;

		if (id == null) {
			logger.error("id == null");

			return flag;
		}

		String[] idArr = new String[] { id };

		try {
			G_PPManager.processOOBatch(idArr);
			flag = true;
		} catch (Exception e) {
			logger.warn("CORBA PreProcess Error:{},Rebind.", e.getMessage());

			AppInitBIO.initPreProcess(this.gw_type);
			changeManager(this.gw_type);
			try {
				G_PPManager.processOOBatch(idArr);
				flag = true;
			} catch (RuntimeException e1) {
				logger.error("CORBA SuperGather Error:{}", e1.getMessage());
			}
		}

		return flag;
	}

	/**
	 * 默认业务生成新的策略.
	 * 
	 * @param idArr
	 *            the id of strategy.
	 */
	public boolean processOMBatch4DefaultService(OneToMany[] objArr) {
		if (Global.GW_TYPE_STB.equals(gw_type))
		{
			throw new UnsupportedOperationException(
					"gw_type=4 is not supported processOMBatch4DefaultService method.");
		}

		boolean flag = false;

		if (objArr == null || objArr.length == 0) {
			logger.error("objArr == null");

			return flag;
		}

		try {
			G_PPManager.processOMBatch4DefaultService(objArr);
			flag = true;
		} catch (Exception e) {
			logger.warn("CORBA PreProcess Error:{},Rebind.", e.getMessage());

			AppInitBIO.initPreProcess(this.gw_type);
			changeManager(this.gw_type);
			try {
				G_PPManager.processOMBatch4DefaultService(objArr);
				flag = true;
			} catch (RuntimeException e1) {
				logger.error("CORBA SuperGather Error:{}", e1.getMessage());
			}
		}

		return flag;
	}

	/**
	 * 默认业务生成新的策略.
	 * 
	 * @param idArr
	 * the id of strategy.
	 */
	public boolean processOMBatch4DefaultService(OneToMany obj) {
		if (Global.GW_TYPE_STB.equals(gw_type))
		{
			throw new UnsupportedOperationException(
					"gw_type=4 is not supported processOMBatch4DefaultService method.");
		}

		boolean flag = false;

		if (obj == null) {
			logger.error("objArr == null");

			return flag;
		}

		OneToMany[] objArr = new OneToMany[1];
		objArr[0] = obj;

		try {
			G_PPManager.processOMBatch4DefaultService(objArr);
			flag = true;
		} catch (Exception e) {
			logger.warn("CORBA PreProcess Error:{},Rebind.", e.getMessage());

			AppInitBIO.initPreProcess(this.gw_type);
			changeManager(this.gw_type);
			try {
				G_PPManager.processOMBatch4DefaultService(objArr);
				flag = true;
			} catch (RuntimeException e1) {
				logger.error("CORBA SuperGather Error:{}", e1.getMessage());
			}
		}
		return flag;
	}

	
	/**
	 * 绑定设备后通知PP.
	 * 
	 * @param userInfoArr
	 * @return <li>1:成功</li> <li>-1:参数为空</li> <li>-2:绑定失败</li>
	 */
	public int processServiceInterface(UserInfo userInfo) {
		if (Global.GW_TYPE_STB.equals(gw_type))
		{
			throw new UnsupportedOperationException(
					"gw_type=4 is not supported processServiceInterface method.");
		}
		if (userInfo == null) {
			logger.error("userInfo == null");
			return -1;
		}
		UserInfo[] userInfoArr = new UserInfo[]{userInfo};
		
		return processServiceInterface(userInfoArr);
	}

	/**
	 * 绑定设备后通知PP.(STB专用)
	 * 
	 * @param userInfoArr
	 * @return <li>1:成功</li> <li>-1:参数为空</li> <li>-2:绑定失败</li>
	 */
	public int processServiceInterface_STB(String xmlString) {
		if (!Global.GW_TYPE_STB.equals(gw_type))
		{
			throw new UnsupportedOperationException(
					"processServiceInterface(String xmlString) is not support when gw_type is not 4");
		}
		if (StringUtil.IsEmpty(xmlString)) {
			logger.error("processServiceInterface---->调用Stb配置模块的参数为空");
			return -2;
		}
		
		return stbCorba.processServiceInterface(xmlString);
	}
	
	
	/**
	 * 绑定设备后通知PP.
	 * 
	 * @param userInfoArr
	 * @return <li>1:成功</li> <li>-1:参数为空</li> <li>-2:绑定失败</li>
	 */
	public int processServiceInterface(UserInfo[] userInfoArr) {
		if (Global.GW_TYPE_STB.equals(gw_type))
		{
			throw new UnsupportedOperationException(
					"gw_type=4 is not supported processServiceInterface method.");
		}
		if (userInfoArr == null) {
			logger.error("userInfoArr == null");
			return -1;
		}

		try {
			G_PPManager.processServiceInterface(userInfoArr);
		} catch (Exception e) {
			logger.warn("CORBA PreProcess Error:{},Rebind.", e.getMessage());

			AppInitBIO.initPreProcess(this.gw_type);
			changeManager(this.gw_type);
			try {
				G_PPManager.processServiceInterface(userInfoArr);
			} catch (RuntimeException e1) {
				logger.error("rebind PreProcess Error.\n{}", e1);
				return -2;
			}
		}

		return 1;
	}
	
	/**
	 * get userInfo from params
	 * 
	 * @param length
	 * @return
	 */
	public UserInfo GetPPBindUserList(PreServInfoOBJ preInfoObj) {
		logger.debug("GetScheduleSQLList({})", preInfoObj);

		UserInfo uinfo = new UserInfo();
		uinfo.userId = StringUtil.getStringValue(preInfoObj.getUserId());
		uinfo.deviceId = StringUtil.getStringValue(preInfoObj.getDeviceId());
		uinfo.oui = StringUtil.getStringValue(preInfoObj.getOui());
		uinfo.deviceSn = StringUtil.getStringValue(preInfoObj.getDeviceSn());
		uinfo.gatherId = StringUtil.getStringValue(preInfoObj.getGatherId());
		uinfo.servTypeId = StringUtil.getStringValue(preInfoObj.getServTypeId());
		uinfo.operTypeId = StringUtil.getStringValue(preInfoObj.getOperTypeId());
		return uinfo;
	}
	/**
	 * 长短定时器配置
	 * 
	 * @param userInfoArr
	 * @return <li>1:成功</li> <li>-1:参数为空</li> <li>-2:失败</li>
	 */
	public int processDeviceStrategy(String[] deviceIds, String serviceId,
			String[] paramArr) {
		if (Global.GW_TYPE_STB.equals(gw_type))
		{
			return stbCorba.processDeviceStrategy(deviceIds, serviceId, paramArr);
		}
		if (deviceIds == null) {
			logger.error("userInfoArr == null");
			return -1;
		}

		try {
			G_PPManager.processDeviceStrategy(deviceIds, serviceId, paramArr);
		} catch (Exception e) {
			logger.warn("CORBA PreProcess Error:{},Rebind.", e.getMessage());

			AppInitBIO.initPreProcess(this.gw_type);
			changeManager(this.gw_type);
			try {
				G_PPManager.processDeviceStrategy(deviceIds, serviceId, paramArr);
			} catch (RuntimeException e1) {
				logger.error(" PreProcess Error.\n{}", e1);
				return -2;
			}
		}

		return 1;
	}

	@Override
	public int processSetStrategyMem(String[] deviceIdArr, String alias) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int processDeviceStrategy(String[] deviceIds, String serviceId,
			String[] paramArr, String taskId)
	{
		if (Global.GW_TYPE_STB.equals(gw_type))
		{
			return stbCorba.processDeviceStrategy(deviceIds, serviceId, paramArr);
		}
		if (deviceIds == null) {
			logger.error("userInfoArr == null");
			return -1;
		}

		int len = deviceIds.length;
		String[] newdeviceIds = new String[len+1];
		newdeviceIds[0] = taskId;
		System.arraycopy(deviceIds, 0, newdeviceIds, 1, len); 
		try {
			G_PPManager.processDeviceStrategy(newdeviceIds, serviceId, paramArr);
		} catch (Exception e) {
			logger.warn("CORBA PreProcess Error:{},Rebind.", e.getMessage());

			AppInitBIO.initPreProcess(this.gw_type);
			changeManager(this.gw_type);
			try {
				G_PPManager.processDeviceStrategy(newdeviceIds, serviceId, paramArr);
			} catch (RuntimeException e1) {
				logger.error(" PreProcess Error.\n{}", e1);
				return -2;
			}
		}

		return 1;
	}
	
}
