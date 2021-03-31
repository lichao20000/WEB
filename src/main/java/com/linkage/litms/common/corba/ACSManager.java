/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package com.linkage.litms.common.corba;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import RemoteDB.ACS_MC_Manager;
import RemoteDB.DB;
import RemoteDB.DeviceStatusStruct;

/**
 * 通知MC
 * 
 * @author alex(yanhj@)
 * @version
 */
public class ACSManager {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(ACSManager.class);

	/**
	 * DB的实例对象
	 */
	private static DB dbInstance = null;

	/**
	 * ACS_MC_Manager的实例对象
	 */
	private static ACS_MC_Manager acsManager = null;

	/**
	 * 单态实例
	 */
	private static ACSManager instance = new ACSManager();

	/**
	 * 单态实例函数
	 */
	public static ACSManager getInstance(String account, String passwd) {
		logger.debug("getInstance({},{})", account, passwd);

		if (dbInstance == null)
			dbInstance = (DB) CorbaInstFactory.factory("db");
		if (dbInstance == null)
			rebind();
		if (null != dbInstance) {
			getACSManager(account, passwd);
		}
		return instance;
	}

	private ACSManager() {

	}

	/**
	 * 重绑
	 * 
	 * @return
	 */
	private static boolean rebind() {
		logger.debug("rebind()");

		dbInstance = null;
		try {
			dbInstance = (DB) CorbaInstFactory.factory("db");

			if (dbInstance == null) {
				dbInstance = (DB) CorbaInstFactory.factory("db");
			}
			return dbInstance != null;
		} catch (Exception e) {
			logger.error("CORBA rebind Exception:{}", e.getMessage());

			return false;
		}
	}

	/**
	 * 从DB中获取ACS_MC_Manager
	 * 
	 * @param account
	 * @param passwd
	 * @return
	 */
	private static void getACSManager(String account, String passwd) {
		logger.debug("getACSManager({},{})", account, passwd);

		try {
			if (WebCorbaInst.passwordString == null) {
				WebCorbaInst.passwordString = dbInstance.ConnectToDb(account,
						passwd);
			}
			acsManager = dbInstance
					.createACSManager(WebCorbaInst.passwordString);
		} catch (Exception e) {
			rebind();
			try {
				if (WebCorbaInst.passwordString == null) {
					WebCorbaInst.passwordString = dbInstance.ConnectToDb(
							account, passwd);
				}
				acsManager = dbInstance
						.createACSManager(WebCorbaInst.passwordString);
			} catch (Exception e1) {
				logger.error("CORBA Exception:{}", e1.getMessage());
			}
		}

	}

	/**
	 * 确认设备成功
	 * 
	 * @return true：成功，false：失败
	 */
	public boolean confirmDevice(String[] deviceIDs) {
		logger.debug("confirmDevice({})", deviceIDs);

		boolean result = true;
		try {
			acsManager.ConfirmeCPE(deviceIDs);
		} catch (Exception e) {
			logger.error("CORBA Exception:{}", e.getMessage());
			rebind();

			try {
				acsManager.ConfirmeCPE(deviceIDs);
			} catch (Exception e1) {
				logger.error("CORBA Exception:{}", e1.getMessage());

				result = false;
			}

		} finally {
			acsManager = null;
			dbInstance = null;
		}

		return result;
	}

	/**
	 * 更新设备
	 * 
	 * @param deviceId
	 *            设备ID
	 * @param status
	 *            <UL>
	 *            <li>1:管理，数据更新</li>
	 *            <li>0:不管理</li>
	 *            <li>-1:删除</li>
	 *            </UL>
	 * @return boolean
	 *         <UL>
	 *         <li>true:成功</li>
	 *         <li>false:失败</li>
	 *         </UL>
	 */
	public boolean manageCPE(String deviceId, String status) {
		logger.debug("manageCPE({},{})", deviceId, status);

		boolean result = true;
		try {
			acsManager.manageCPE(deviceId, status);
		} catch (Exception e) {
			logger.error("CORBA Exception:{}", e.getMessage());

			rebind();

			try {
				acsManager.manageCPE(deviceId, status);
			} catch (Exception e1) {
				logger.error("CORBA Exception:{}", e1.getMessage());

				result = false;
			}
		} finally {
			acsManager = null;
			dbInstance = null;
		}

		return result;
	}

	/**
	 * 更新设备在线状态,#功能取消，在ACS中实现
	 * 
	 * @return true：成功，false：失败
	 */
	public boolean ChangeDeviceStatus(DeviceStatusStruct[] deviceStatusStructArr) {
		logger.debug("ChangeDeviceStatus({})", deviceStatusStructArr);

		boolean result = true;
		try {
			acsManager.ChangeDeviceStatus(deviceStatusStructArr);
		} catch (Exception e) {
			logger.error("CORBA Exception:{}", e.getMessage());

			result = false;
			acsManager = null;
			dbInstance = null;
		}

		return result;
	}

}
