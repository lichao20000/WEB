package com.linkage.liposs.buss.util;

import org.omg.CORBA.StringHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import RemoteDB.DB;
import RemoteDB.DataManager;
import RemoteDB.FluxData;
import RemoteDB.SQLException;

import com.linkage.litms.common.corba.CorbaInstFactory;
import com.linkage.litms.common.corba.WebCorbaInst;

/**
 * 
 * @author liuw
 * @version 1.0
 * @since 2007-8-15
 * @category 接口速率/丢包/错包处理实体类, 调用CORBA接口获取数据
 */
public class FluxDataUtil {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(FluxDataUtil.class);
	protected static DB dbInstance = null;

	/**
	 * Corba接口调用失败的时候重新绑定
	 * 
	 * @return
	 */
	public boolean rebind() {
		dbInstance = null;
		try {
			dbInstance = (DB) CorbaInstFactory.factory("db");

			if (dbInstance == null) {
				dbInstance = (DB) CorbaInstFactory.factory("db");
			}
			return dbInstance != null;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 调用CORBA接口的方法
	 * @param device_id 设备ID
	 * @param account  登陆用户的帐号
	 * @param passwd   登陆用户的密码
	 * @return FluxData[]对象数组,该对象封装在RemoteDB里面
	 */
	public FluxData[] getDeviceFlux(String device_id, String account,
			String passwd) {
		DataManager manager = null;
		if (dbInstance == null)
			dbInstance = (DB) CorbaInstFactory.factory("db");
		if (dbInstance == null)
			rebind();
		else
			logger.warn(dbInstance.getClass().toString());

		try {
			if (WebCorbaInst.passwordString == null) {
				WebCorbaInst.passwordString = dbInstance.ConnectToDb(account,
						passwd);
			}
			// logger.debug(passwordString);
			manager = dbInstance.createDataManager(WebCorbaInst.passwordString);
		} catch (Exception e) {
			rebind();
			try {
				if (WebCorbaInst.passwordString == null) {
					WebCorbaInst.passwordString = dbInstance.ConnectToDb(
							account, passwd);
				}
				// logger.debug(passwordString);
				manager = dbInstance
						.createDataManager(WebCorbaInst.passwordString);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			e.printStackTrace();
		}
		if (manager == null) {
			logger.warn("无法获取设备实例");
			return null;
		}
		StringHolder collecttime = new StringHolder();
		logger.debug("开始获取设备" + device_id + "的流量");

		FluxData datas[] = manager.getPortData(device_id, collecttime);
		logger.debug("获得采集时间为" + collecttime.value);
		if (datas != null) {
			logger.debug(""+datas.length);
		}
		return datas;
	}
}
