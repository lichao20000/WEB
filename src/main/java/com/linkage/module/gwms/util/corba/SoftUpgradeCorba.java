/**
 * 
 */
package com.linkage.module.gwms.util.corba;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import SoftUp.SoftUpManager;

import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.init.bio.AppInitBIO;

/**
 * @author zhangsm
 * @date 2013-1-9
 * 
 * 软件升级模块corab工具类
 */
public class SoftUpgradeCorba {
	
	/** log */
	private static Logger logger = LoggerFactory.getLogger(SoftUpgradeCorba.class);
	
	private static SoftUpManager G_SoftUpgrade = null;
	
	private String gw_type;
	
	public SoftUpgradeCorba(){
	}
	
	public SoftUpgradeCorba(String gw_type){
		this.gw_type = gw_type;
		
		if(Global.GW_TYPE_ITMS.equals(gw_type)){
			G_SoftUpgrade = Global.G_Softgrade_itms;
		} 
		else if(Global.GW_TYPE_STB.equals(gw_type)){
			G_SoftUpgrade = Global.G_Softgrade_stb;
		}
		else {
			G_SoftUpgrade = Global.G_Softgrade_bbms;
		}
	}
	
	/**
	 * 更改指引  用于二次获取cobar
	 * @param type
	 */
	private void changeManager(String type)
	{
		if(Global.GW_TYPE_ITMS.equals(type)){
			G_SoftUpgrade = Global.G_Softgrade_itms;
		} 
		else if(Global.GW_TYPE_STB.equals(type)){
			G_SoftUpgrade = Global.G_Softgrade_stb;
		}
		else {
			G_SoftUpgrade = Global.G_Softgrade_bbms;
		}
	}
	
	/**
	 * 
	 * @param processDeviceStrategy
	 * @return
	 */
	public void processDeviceStrategy(String[] deviceIdIdArr,String serviceId,String[] paramArr)
	{
		logger.debug("processDeviceStrategy({},{},{})", new Object[]{deviceIdIdArr, serviceId, paramArr});
		try{
			G_SoftUpgrade.processDeviceStrategy(deviceIdIdArr, serviceId, paramArr);
		}
		catch(Exception e)
		{
			logger.error("SoftUpgrade Error.\n{}, rebind!", e);
			AppInitBIO.initSoftUpgradeCorba(this.gw_type);
			changeManager(this.gw_type);
			try
			{
				G_SoftUpgrade.processDeviceStrategy(deviceIdIdArr, serviceId, paramArr);
			}
			catch(Exception ex)
			{
				logger.error("rebind SoftUpgrade Error.\n{}", ex);
			}
		}
	}
}
