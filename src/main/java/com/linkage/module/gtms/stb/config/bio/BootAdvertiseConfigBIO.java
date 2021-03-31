
package com.linkage.module.gtms.stb.config.bio;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.obj.tr069.ParameValueOBJ;
import com.linkage.module.gwms.util.corba.ACSCorba;

/**
 * @author banyr (Ailk No.)
 * @version 1.0
 * @since 2018-6-22
 * @category com.linkage.module.gtms.stb.config.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class BootAdvertiseConfigBIO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(BootAdvertiseConfigBIO.class);
	// 开机图片节点值地址
	private static final String bootImagePath = "Device.UserInterface.Logo.X_CT-COM_BootPicURL";
	// 开机动画节点值地址
	private static final String bootAnimationPath = "Device.UserInterface.Logo.X_CT-COM_StartPicURL";
	// 认证图片节点值地址
	private static final String cerPicturePath = "Device.UserInterface.Logo.X_CT-COM_AuthenticatePicURL";

	/**
	 * 调用corba发送信息
	 * 
	 * @param deviceId
	 * @param bootImage
	 * @param bootAnimation
	 * @param cerPicture
	 * @return
	 */
	public int bootAdvertiseSet(String deviceId, String bootImage, String bootAnimation,
			String cerPicture)
	{
		logger.debug("bootAdvertiseSet({},{})", deviceId);
		ACSCorba acsCorba = new ACSCorba(Global.GW_TYPE_STB);
		ParameValueOBJ bootImageObj = new ParameValueOBJ();
		bootImageObj.setName(bootImagePath);
		bootImageObj.setValue(bootImage);
		bootImageObj.setType("1");
		ParameValueOBJ bootAnimationObj = new ParameValueOBJ();
		bootAnimationObj.setName(bootAnimationPath);
		bootAnimationObj.setValue(bootAnimation);
		bootAnimationObj.setType("1");
		ParameValueOBJ cerPictureObj = new ParameValueOBJ();
		cerPictureObj.setName(cerPicturePath);
		cerPictureObj.setValue(cerPicture);
		cerPictureObj.setType("1");
		ArrayList<ParameValueOBJ> objList = new ArrayList<ParameValueOBJ>();
		objList.add(bootImageObj);
		objList.add(bootAnimationObj);
		objList.add(cerPictureObj);
		return acsCorba.setValue(deviceId, objList);
	}
}
