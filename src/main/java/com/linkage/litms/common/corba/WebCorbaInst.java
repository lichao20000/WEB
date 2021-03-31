/*
 *
 * 创建日期 2006-2-22
 * Administrator suzr
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.linkage.litms.common.corba;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import RemoteDB.DB;

import com.linkage.litms.system.User;

public class WebCorbaInst {
	private static Logger m_logger = LoggerFactory.getLogger(WebCorbaInst.class);

	private static org.omg.CORBA.Object object = null;

	public static String passwordString = null;

	private String name = null;

	public WebCorbaInst(String name) {
		this.name = name;

		if (object == null) {
			object = CorbaInstFactory.factory(name);
		}
	}

	public org.omg.CORBA.Object getCorbaInst(String name) {
		return CorbaInstFactory.factory(name);
	}
    /**
     * 调用MasterControl方法Corba接口获得设备内部编码
     * 默认为取device_id为1个
     * @param user
     * @return
     */
	public String getDeviceSerial(User user){
        return getDeviceSerial(user,1);
    }
	/**
	 * 调用MasterControl方法Corba接口获得设备内部编码
	 * 
	 * @param user
     * @param count 需要获取device_id的个数
	 * @return String
	 */
	public String getDeviceSerial(User user,int count) {
		String m_DeviceSerial = null;
		DB inst = null;
		m_logger.debug("getDeviceSerial(User user)");
		if (object == null) {
			object = CorbaInstFactory.factory(name);
		}

		if (object == null) {
			return null;
		}

		try {
			inst = (DB) object;
			if (passwordString == null) {
				passwordString = inst.ConnectToDb(user.getAccount(), user
						.getPasswd());
			}

			m_DeviceSerial = inst.createControlManager(passwordString)
					.GetUnusedDeviceSerial(count);
		} catch (Exception e) {
			m_logger.debug("get device serial error : " + e);

			try {
				// reset corba
				object = CorbaInstFactory.factory(name);
				inst = (DB) object;

				passwordString = inst.ConnectToDb(user.getAccount(), user
						.getPasswd());
				m_DeviceSerial = inst.createControlManager(passwordString)
						.GetUnusedDeviceSerial(1);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		m_logger.debug(m_DeviceSerial);
		return m_DeviceSerial;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
