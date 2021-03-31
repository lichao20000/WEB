/**
 * 
 */

package com.linkage.module.gtms.stb.diagnostic.serv;

import com.linkage.module.gwms.util.corba.DevReboot;


/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-12-25
 * @category com.linkage.module.stb.resource.act
 */
public class StbSystemCmdBIO
{

	public String reboot(String deviceId, String gw_type)
	{
		return String.valueOf(DevReboot.reboot(deviceId, gw_type));
	}

	/**
	 * 恢复出厂设置
	 * 
	 * @param deviceId
	 *            ，设备ID
	 * @return 恢复出厂设置结果，never null
	 */
	public String restore(String deviceId, String gw_type)
	{
		return String.valueOf(DevReboot.reset(deviceId, gw_type));
	}
}
