package com.linkage.module.gwms.cao.gw;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.cao.gw.interf.I_CAO;
import com.linkage.module.gwms.obj.gw.WlanOBJ;
import com.linkage.module.gwms.obj.tr069.AddOBJ;
import com.linkage.module.gwms.obj.tr069.ParameValueOBJ;
import com.linkage.module.gwms.util.PreProcessInterface;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.gwms.util.corba.ACSCorba;
import com.linkage.module.gwms.util.corba.SuperGatherCorba;

/**
 * CAO:WLAN.
 */
public class WlanCAO implements I_CAO 
{
	private static Logger logger = LoggerFactory.getLogger(WlanCAO.class);

	/** ACS CORBA */
	private ACSCorba acsCorba;
	/** PreProcess CORBA */
	private PreProcessInterface ppCorba;
	/** SuperGather CORBA */
	private SuperGatherCorba sgCorba;
	/**华为厂商*/
	public static final String VENDORID_HW="2";
	/**中兴厂商*/
	public static final String VENDORID_ZX="10";
	/**运营商*/
	public static String instArea=Global.instAreaShortName;

	/**
	 * config wlan.
	 */
	public int configWlan(WlanOBJ obj) 
	{
		logger.warn("[{}] configWlan({},{},{},{})", obj.getDeviceId(),obj.getApEnable(),
								obj.getPowerlevel(),obj.getHide(),obj.getWpsKeyWord());

		String wlan = getURL(obj);
		
		ArrayList<ParameValueOBJ> objList = new ArrayList<ParameValueOBJ>();
		objList.add(new ParameValueOBJ(wlan + "X_CT-COM_APModuleEnable", obj.getApEnable(), "4"));
		logger.warn("[{}] {}:{}",obj.getDeviceId(),wlan + "X_CT-COM_APModuleEnable", obj.getApEnable());
		if (!StringUtil.IsEmpty(obj.getPowerlevel())) {
			objList.add(new ParameValueOBJ(wlan + "X_CT-COM_Powerlevel", obj.getPowerlevel(), "3"));
			logger.warn("[{}] {}:{}",obj.getDeviceId(),wlan + "X_CT-COM_Powerlevel", obj.getPowerlevel());
		}
		if (!StringUtil.IsEmpty(obj.getHide())) {
			objList.add(new ParameValueOBJ(wlan + "X_CT-COM_SSIDHide", obj.getHide(), "4"));
			logger.warn("[{}] {}:{}",obj.getDeviceId(),wlan + "X_CT-COM_SSIDHide", obj.getHide());
		}
		if (!StringUtil.IsEmpty(obj.getWpsKeyWord())) {
			objList.add(new ParameValueOBJ(wlan + "X_CT-COM_WPSKeyWord", obj.getWpsKeyWord(), "3"));
			logger.warn("[{}] {}:{}",obj.getDeviceId(),wlan + "X_CT-COM_WPSKeyWord", obj.getWpsKeyWord());
		}
		
		acsCorba = new ACSCorba(String.valueOf(LipossGlobals.getGw_Type(obj.getDeviceId())));
		return acsCorba.setValue(obj.getDeviceId(), objList);
	}

	/**
	 * add SSID
	 */
	public int addSsid(WlanOBJ wlanOBJ) 
	{
		logger.warn("addSsid({})", wlanOBJ);

		acsCorba = new ACSCorba(String.valueOf(LipossGlobals.getGw_Type(wlanOBJ.getDeviceId())));
		AddOBJ obj = acsCorba.add(wlanOBJ.getDeviceId(),getURL(wlanOBJ));

		if (obj.getStatus() == 0 || obj.getStatus() == 1) {
			wlanOBJ.setLanWlanId(obj.getInstance()+"");
			ArrayList<ParameValueOBJ> objList=getObjList(wlanOBJ,getURL(wlanOBJ));
			
			return acsCorba.setValue(wlanOBJ.getDeviceId(), objList);
		} else {
			return obj.getStatus();
		}
	}
	
	/**
	 * edit SSID.
	 */
	public int editSsid(WlanOBJ wlanOBJ)
	{
		logger.debug("editSsid({})", wlanOBJ);
		ArrayList<ParameValueOBJ> objList = getObjList(wlanOBJ,getURL(wlanOBJ));

		acsCorba = new ACSCorba(String.valueOf(LipossGlobals.getGw_Type(wlanOBJ.getDeviceId())));
		return acsCorba.setValue(wlanOBJ.getDeviceId(), objList);
	}
	
	/**
	 * delee SSID
	 */
	public int delSsid(WlanOBJ wlanOBJ) 
	{
		acsCorba = new ACSCorba(String.valueOf(LipossGlobals.getGw_Type(wlanOBJ.getDeviceId())));
		return acsCorba.del(wlanOBJ.getDeviceId(), getURL(wlanOBJ));
	}
	
	/**
	 * 组装下发节点数据
	 */
	private ArrayList<ParameValueOBJ> getObjList(WlanOBJ wlanOBJ,String wlan)
	{
		ArrayList<ParameValueOBJ> objList = new ArrayList<ParameValueOBJ>();
		objList.add(new ParameValueOBJ(wlan + "SSID", wlanOBJ.getSsid(),"1"));
		objList.add(new ParameValueOBJ(wlan + "Channel", wlanOBJ.getChannel(), "3"));
		objList.add(new ParameValueOBJ(wlan + "Enable",wlanOBJ.getEnable(), "4"));
		objList.add(new ParameValueOBJ(wlan + "BeaconType", wlanOBJ.getBeacontype(), "1"));
		
		logger.warn("[{}] {}:{}",wlanOBJ.getDeviceId(),wlan+"SSID",wlanOBJ.getSsid());
		logger.warn("[{}] {}:{}",wlanOBJ.getDeviceId(),wlan+"Channel",wlanOBJ.getChannel());
		logger.warn("[{}] {}:{}",wlanOBJ.getDeviceId(),wlan+"Enable",wlanOBJ.getEnable());
		logger.warn("[{}] {}:{}",wlanOBJ.getDeviceId(),wlan+"BeaconType",wlanOBJ.getBeacontype());
		
		if(Global.XJDX.equals(instArea))
		{
			if ("Basic".equals(wlanOBJ.getBeacontype())) {
				objList.add(new ParameValueOBJ(wlan + "BasicAuthenticationMode", wlanOBJ.getBasicAuthMode(), "1"));
				objList.add(new ParameValueOBJ(wlan + "WEPKey.1.WEPKey",wlanOBJ.getWepKey(), "1"));
				logger.warn("[{}] {}:{}",wlanOBJ.getDeviceId(),wlan+"BasicAuthenticationMode",wlanOBJ.getBasicAuthMode());
				logger.warn("[{}] {}:{}",wlanOBJ.getDeviceId(),wlan+"WEPKey.1.WEPKey",wlanOBJ.getWepKey());
			} else if ("WPA".equals(wlanOBJ.getBeacontype()) 
						|| ((VENDORID_HW.equals(wlanOBJ.getVendorId()) || VENDORID_ZX.equals(wlanOBJ.getVendorId())) 
								&& ("WPA2".equals(wlanOBJ.getBeacontype()) 
										|| "WPA/WPA2".equals(wlanOBJ.getBeacontype()))) 
						|| ((!VENDORID_HW.equals(wlanOBJ.getVendorId()) && !VENDORID_ZX.equals(wlanOBJ.getVendorId())) 
								&& ("11i".equals(wlanOBJ.getBeacontype()) 
										|| "WPAand11i".equals(wlanOBJ.getBeacontype())))) {
				objList.add(new ParameValueOBJ(wlan + "IEEE11iAuthenticationMode", wlanOBJ.getIeeeAuthMode(), "1"));
				objList.add(new ParameValueOBJ(wlan + "WPAEncryptionModes",wlanOBJ.getWpaEncrMode(), "1"));
				objList.add(new ParameValueOBJ(wlan + "PreSharedKey.1.PreSharedKey", wlanOBJ.getWpaKey(),"1"));
				logger.warn("[{}] {}:{}",wlanOBJ.getDeviceId(),wlan+"IEEE11iAuthenticationMode",wlanOBJ.getIeeeAuthMode());
				logger.warn("[{}] {}:{}",wlanOBJ.getDeviceId(),wlan+"WPAEncryptionModes",wlanOBJ.getWpaEncrMode());
				logger.warn("[{}] {}:{}",wlanOBJ.getDeviceId(),wlan+"PreSharedKey.1.PreSharedKey",wlanOBJ.getWpaKey());
			}else{
				objList.add(new ParameValueOBJ(wlan + "BasicAuthenticationMode", wlanOBJ.getBasicAuthMode(), "1"));
				logger.warn("[{}] {}:{}",wlanOBJ.getDeviceId(),wlan+"BasicAuthenticationMode",wlanOBJ.getBasicAuthMode());
			}
		}
		else
		{
			if ("Basic".equals(wlanOBJ.getBeacontype())) {
				objList.add(new ParameValueOBJ(wlan + "BasicAuthenticationMode", wlanOBJ.getBasicAuthMode(), "1"));
				objList.add(new ParameValueOBJ(wlan + "WEPEncryptionLevel",wlanOBJ.getWepEncrLevel(), "1"));
				objList.add(new ParameValueOBJ(wlan + "WEPKey.1.WEPKey",wlanOBJ.getWepKey(), "1"));
			} else if ("WPA".equals(wlanOBJ.getBeacontype())) {
				objList.add(new ParameValueOBJ(wlan + "WPAAuthenticationMode",wlanOBJ.getWpaAuthMode(), "1"));
				objList.add(new ParameValueOBJ(wlan + "WPAEncryptionModes",wlanOBJ.getWpaEncrMode(), "1"));
				objList.add(new ParameValueOBJ(wlan + "PreSharedKey.1.PreSharedKey", wlanOBJ.getWpaKey(),"1"));
			} else if ("11i".equals(wlanOBJ.getBeacontype())) {
				objList.add(new ParameValueOBJ(wlan + "IEEE11iAuthenticationMode", wlanOBJ.getIeeeAuthMode(), "1"));
				objList.add(new ParameValueOBJ(wlan + "IEEE11iEncryptionModes",wlanOBJ.getIeeeEncrMode(), "1"));
				objList.add(new ParameValueOBJ(wlan + "PreSharedKey.1.PreSharedKey", wlanOBJ.getWpaKey(),"1"));
			} else if("WPA/WPA2".equals(wlanOBJ.getBeacontype())){
				objList.add(new ParameValueOBJ(wlan+ "WPAEncryptionModes", wlanOBJ.getWpaWpa2EncrMode(),"1"));
				objList.add(new ParameValueOBJ(wlan + "PreSharedKey.1.PreSharedKey", wlanOBJ.getWpaKey(),"1"));
			}else{
				objList.add(new ParameValueOBJ(wlan + "BasicAuthenticationMode", wlanOBJ.getBasicAuthMode(), "1"));
			}
		}
		
		return objList;
	}

	/**
	 * 获取路径
	 */
	private String getURL(WlanOBJ obj)
	{
		String url="InternetGatewayDevice.LANDevice."
				+obj.getLanId()+".WLANConfiguration.";
		if(!StringUtil.IsEmpty(obj.getLanWlanId())){
			url+=obj.getLanWlanId()+".";
		}
		return url;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.linkage.module.gwms.diagnostics.cao.interf.I_AdvanceSearchCAO#addStrategyToPP(java.lang.String)
	 */
	@Override
	public boolean addStrategyToPP(String id) 
	{
		logger.debug("addStrategyToPP({})", id);
		return ppCorba.processOOBatch(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.linkage.module.gwms.diagnostics.cao.interf.I_AdvanceSearchCAO#getDataFromSG(java.lang.String,
	 *      int)
	 */
	@Override
	public int getDataFromSG(String deviceId, int type) 
	{
		logger.debug("getDataFromSG({},{})", deviceId, type);
		sgCorba = new SuperGatherCorba(String.valueOf(LipossGlobals.getGw_Type(deviceId)));
		return sgCorba.getCpeParams(deviceId, type, 1);
	}

	
	public void setPpCorba(PreProcessInterface ppCorba) {
		this.ppCorba = ppCorba;
	}

	public void setSgCorba(SuperGatherCorba sgCorba) {
		this.sgCorba = sgCorba;
	}
	
	public void setAcsCorba(ACSCorba acsCorba) {
		this.acsCorba = acsCorba;
	}

}
