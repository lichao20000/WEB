
package com.linkage.module.gtms.stb.config.act;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.stb.config.bio.BootAdvertiseConfigBIO;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.corba.ACSCorba;

/**
 * 机顶盒开机广告及时下发
 * 
 * @author banyr (Ailk No.)
 * @version 1.0
 * @since 2018-6-22
 * @category com.linkage.itms.besttone.main
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class BootAdvertiseConfigACT
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(BootAdvertiseConfigACT.class);
	// 开机图片
	private String bootImage = null;
	// 开机动画
	private String bootAnimation = null;
	// 认证图片
	private String cerPicture = null;
	private BootAdvertiseConfigBIO bootAdvertiseConfigBIO = null;
	private String resultDesc = null;
	private String deviceId = null;

	public String resultReturn()
	{
		logger.warn("start execute BootAdvertiseConfig()" + deviceId);
		ACSCorba acsCorba = new ACSCorba(Global.GW_TYPE_STB);
		int statusResult = acsCorba.testConnection(deviceId, Global.GW_TYPE_STB);
		if (0 == statusResult)
		{
			resultDesc = "设备不在线";
			return "bootAdvertiseResult";
		}
		int result = bootAdvertiseConfigBIO.bootAdvertiseSet(deviceId, bootImage,
				bootAnimation, cerPicture);
		if (1 == result || 0 == result)
		{
			resultDesc = "设置成功";
		}
		else if (-7 == result)
		{
			resultDesc = "系统参数错误";
		}
		else if (-6 == result)
		{
			resultDesc = "设备正被操作";
		}
		else if (-1 == result)
		{
			resultDesc = "设备连接失败";
		}
		else if (-9 == result)
		{
			resultDesc = "系统内部错误";
		}
		else
		{
			resultDesc = "TR069错误";
		}
		logger.warn("execute BootAdvertiseConfig() result" + resultDesc);
		return "bootAdvertiseResult";
	}

	public String getBootImage()
	{
		return bootImage;
	}

	public void setBootImage(String bootImage)
	{
		this.bootImage = bootImage;
	}

	public String getBootAnimation()
	{
		return bootAnimation;
	}

	public void setBootAnimation(String bootAnimation)
	{
		this.bootAnimation = bootAnimation;
	}

	public String getCerPicture()
	{
		return cerPicture;
	}

	public void setCerPicture(String cerPicture)
	{
		this.cerPicture = cerPicture;
	}

	public String getDeviceId()
	{
		return deviceId;
	}

	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	public String getResultDesc()
	{
		return resultDesc;
	}

	public void setResultDesc(String resultDesc)
	{
		this.resultDesc = resultDesc;
	}

	public BootAdvertiseConfigBIO getBootAdvertiseConfigBIO()
	{
		return bootAdvertiseConfigBIO;
	}

	public void setBootAdvertiseConfigBIO(BootAdvertiseConfigBIO bootAdvertiseConfigBIO)
	{
		this.bootAdvertiseConfigBIO = bootAdvertiseConfigBIO;
	}
}
