package com.linkage.module.gwms.diagnostics.act;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.paramConfig.WlanConfigDAO;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.diagnostics.act.interf.I_AdvanceSearchACT;
import com.linkage.module.gwms.diagnostics.bio.WlanBIO;
import com.linkage.module.gwms.obj.gw.WlanOBJ;

/**
 * action:advance search.WLAN.
 * 
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Jun 17, 2009
 * @see
 * @since 1.0
 */
public class WlanACT implements ServletRequestAware, I_AdvanceSearchACT 
{
	private static Logger logger = LoggerFactory.getLogger(WlanACT.class);

	private WlanBIO bio;
	private final int strategyType = 0;
	private final int serviceId = 103;
	private HttpServletRequest request;
	private HttpSession session;
	private String deviceId;
	private List<WlanOBJ> list;
	private String ajax = "";
	private String result = "";
	private String lanId;
	private String lanWlanId;
	/** 总开关 */
	private String apEnable;
	/** 是否隐藏 */
	private String hide;
	/** 功率级别 */
	private String powerlevel;
	/** 当前功率 */
	private String powervalue;
	/** WPS */
	private String wpsKeyWord;
	/** ssid */
	private String ssid;
	/** 认证模式 beacontype */
	private String beacontype;
	/**加密模式*/
	private String encr_mode;
	/** WEP密钥长度 */
	private String wepEncrLevel;
	/** key */
	private String key;
	/** 终端类型 */
	private String gw_type = null;
	/** 信道*/
	private String channel;

	/**华为厂商*/
	public static final String VENDORID_HW="2";
	/**中兴厂商*/
	public static final String VENDORID_ZX="10";
	private String vendorId="";
	/**运营商*/
	public static String instArea=Global.instAreaShortName;
	/**db为直接查表，否则直接采集*/
	private String queryFrom="sg";
	
	/**
	 * get data.
	 */
	public String execute() 
	{
		logger.warn("[{}] execute({})",deviceId,gw_type);
		
		bio.setQueryFrom(queryFrom);
		list = bio.getData(deviceId);
		
		if (list != null && list.size() > 0) {
			apEnable = list.get(0).getApEnable();
			lanId = list.get(0).getLanId();
			lanWlanId = list.get(0).getLanWlanId();
			hide = list.get(0).getHide();
			powerlevel = list.get(0).getPowerlevel();
			powervalue = list.get(0).getPowervalue();
			wpsKeyWord = list.get(0).getWpsKeyWord();
			wepEncrLevel=list.get(0).getWepEncrLevel();
			channel = list.get(0).getChannel();
		} else {
			logger.warn("[{}] get data null",deviceId);
		}
		result = bio.getResult();
		
		logger.warn("[{}] execute() result:{}",deviceId,result);
		if(Global.XJDX.equals(Global.instAreaShortName)){
			return "success_xjdx";
		}
		
		return "success";
	}

	/**
	 * config dev.
	 */
	public String config() 
	{
		session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");

		WlanOBJ obj = new WlanOBJ();
		obj.setDeviceId(deviceId);
		obj.setLanId(lanId);
		obj.setLanWlanId(lanWlanId);
		obj.setApEnable(apEnable);
		obj.setPowerlevel(powerlevel);
		if(Global.GW_TYPE_ITMS.equals(gw_type)){
			obj.setWpsKeyWord(wpsKeyWord);
			obj.setHide(hide);
		}

		ajax = bio.configDev(obj,curUser.getUser().getId(),strategyType,serviceId,gw_type);
		result = bio.getResult();
		
		ajax = ajax + "|" + result;
		logger.warn("[{}] config() ajax:{}",deviceId,ajax);
		return "ajax";
	}

	/**
	 * add ssid.
	 */
	public String add() 
	{
		logger.warn("[{}] add({},{},{},{},{})",deviceId,lanId,ssid,beacontype,encr_mode,key);
		
		WlanOBJ wlanOBJ = new WlanOBJ();
		wlanOBJ.setDeviceId(deviceId);
		wlanOBJ.setLanId(lanId);
		wlanOBJ.setSsid(ssid);
		wlanOBJ.setWepKey(key);
		wlanOBJ.setWpaKey(key);
		//WPA,11i
		wlanOBJ.setPreSharedKey(key);
		wlanOBJ.setEnable("1");
		wlanOBJ.setRadioEnable("1");
		//wlanOBJ.setChannel("0");
		wlanOBJ.setChannel(channel);
		//BBMS
		wlanOBJ.setKeyPassphrase(key);
		
		if(Global.XJDX.equals(instArea))
		{
			wlanOBJ.setHide("0");
			wlanOBJ.setChannel("0");
			wlanOBJ.setChannelInUse("0");
			
			vendorId=new WlanConfigDAO().getDevVendorId(deviceId);
			wlanOBJ.setVendorId(vendorId);
			
			if("None".equals(beacontype)){
				wlanOBJ.setBeacontype(beacontype);
				wlanOBJ.setBasicAuthMode("OpenSystem");
				wlanOBJ.setIeeeAuthMode(null);
				wlanOBJ.setWpaEncrMode(null);
			}else if("WEP".equals(beacontype)){
				wlanOBJ.setBeacontype("Basic");
			}else if("WPA-PSK".equals(beacontype)){
				wlanOBJ.setBeacontype("WPA");
			}else{
				if(VENDORID_HW.equals(vendorId) || VENDORID_ZX.equals(vendorId)){
					if("WPA2-PSK".equals(beacontype)){
						wlanOBJ.setBeacontype("WPA2");
					}else if("WPA-PSK/WPA2-PSK".equals(beacontype)){
						wlanOBJ.setBeacontype("WPA/WPA2");
					}
				}else{
					if("WPA2-PSK".equals(beacontype)){
						wlanOBJ.setBeacontype("11i");
					}else if("WPA-PSK/WPA2-PSK".equals(beacontype)){
						wlanOBJ.setBeacontype("WPAand11i");
					}
				}
			}
			
			if(StringUtil.IsEmpty(wlanOBJ.getBeacontype())){
				ajax="true|认证模式无法识别！新增失败";
				logger.warn("[{}] add() ajax:{}",deviceId,ajax);
				return "ajax";
			}
			
			if ("WEP".equals(beacontype)) {
				wlanOBJ.setBasicAuthMode(encr_mode);
			} else if ("WPA-PSK".equals(beacontype) 
						|| "WPA2-PSK".equals(beacontype) 
						|| "WPA-PSK/WPA2-PSK".equals(beacontype)) {
				wlanOBJ.setIeeeAuthMode("PSKAuthentication");
				wlanOBJ.setWpaEncrMode(encr_mode);
			}
		}
		else
		{
			wlanOBJ.setBeacontype(beacontype);
			if ("None".equals(wlanOBJ.getBeacontype())) {
				wlanOBJ.setBasicAuthMode("OpenSystem");
				wlanOBJ.setEncryptionType(1);
			} else if ("Basic".equals(wlanOBJ.getBeacontype())) {
				wlanOBJ.setBasicAuthMode("Both");
				wlanOBJ.setWepEncrLevel("40-bit");
				wlanOBJ.setEncryptionType(2);
			} else if ("WPA".equals(wlanOBJ.getBeacontype())) {
				wlanOBJ.setWpaAuthMode("PSKAuthentication");
				wlanOBJ.setWpaEncrMode("TKIPEncryption");
				wlanOBJ.setEncryptionType(3);
			} else if ("11i".equals(wlanOBJ.getBeacontype())) {
				wlanOBJ.setIeeeAuthMode("PSKAuthentication");
				wlanOBJ.setIeeeEncrMode("TKIPEncryption");
				wlanOBJ.setEncryptionType(4);
			} else if("WPA/WPA2".equals(wlanOBJ.getBeacontype())){
				wlanOBJ.setWpaWpa2EncrMode("AESEncryption");
				wlanOBJ.setEncryptionType(5);
			}
		}
		
		if(Global.GW_TYPE_ITMS.equals(gw_type)){
			ajax = bio.add(wlanOBJ);
			result = bio.getResult();

			ajax = ajax + "|" + result;
		}else{
			session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			ajax = bio.addWlan(curUser.getUser().getId(), wlanOBJ, gw_type);
		}
		
		logger.warn("[{}] add() ajax:{}",deviceId,ajax);
		return "ajax";
	}

	/**
	 * edit ssid.
	 */
	public String edit() 
	{
		session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long accOId = curUser.getUser().getId();
		
		WlanOBJ wlanOBJ = new WlanOBJ();
		wlanOBJ.setDeviceId(deviceId);
		wlanOBJ.setLanId(lanId);
		wlanOBJ.setLanWlanId(lanWlanId);
		wlanOBJ.setSsid(ssid);
		wlanOBJ.setWepKey(key);
		wlanOBJ.setWpaKey(key);
		//WPA,11i
		wlanOBJ.setPreSharedKey(key);
		
		wlanOBJ.setEnable("1");
		wlanOBJ.setRadioEnable("1");
		wlanOBJ.setChannel(channel);
		//BBMS
		wlanOBJ.setKeyPassphrase(key);
		
		if(Global.XJDX.equals(instArea))
		{
			wlanOBJ.setHide("0");
			wlanOBJ.setChannel("0");
			wlanOBJ.setChannelInUse("0");
			
			vendorId=new WlanConfigDAO().getDevVendorId(deviceId);
			wlanOBJ.setVendorId(vendorId);
			
			if("None".equals(beacontype)){
				wlanOBJ.setBasicAuthMode("OpenSystem");
				wlanOBJ.setBeacontype(beacontype);
			}else if("WEP".equals(beacontype)){
				wlanOBJ.setBeacontype("Basic");
			}else if("WPA-PSK".equals(beacontype)){
				wlanOBJ.setBeacontype("WPA");
			}else{
				if(VENDORID_HW.equals(vendorId) || VENDORID_ZX.equals(vendorId)){
					if("WPA2-PSK".equals(beacontype)){
						wlanOBJ.setBeacontype("WPA2");
					}else if("WPA-PSK/WPA2-PSK".equals(beacontype)){
						wlanOBJ.setBeacontype("WPA/WPA2");
					}
				}else{
					if("WPA2-PSK".equals(beacontype)){
						wlanOBJ.setBeacontype("11i");
					}else if("WPA-PSK/WPA2-PSK".equals(beacontype)){
						wlanOBJ.setBeacontype("WPAand11i");
					}
				}
			}
			
			if(StringUtil.IsEmpty(wlanOBJ.getBeacontype())){
				ajax="true|认证模式无法识别！编辑失败";
				logger.warn("[{}] edit() ajax:{}",deviceId,ajax);
				return "ajax";
			}
			
			if ("WEP".equals(beacontype)) {
				wlanOBJ.setBasicAuthMode(encr_mode);
			} else if ("WPA-PSK".equals(beacontype) 
						|| "WPA2-PSK".equals(beacontype) 
						|| "WPA-PSK/WPA2-PSK".equals(beacontype)) {
				wlanOBJ.setIeeeAuthMode("PSKAuthentication");
				wlanOBJ.setWpaEncrMode(encr_mode);
			}
		}
		else
		{
			wlanOBJ.setBeacontype(beacontype);
			if ("None".equals(wlanOBJ.getBeacontype())) {
				wlanOBJ.setBasicAuthMode("OpenSystem");
				wlanOBJ.setEncryptionType(1);
			} else if ("Basic".equals(wlanOBJ.getBeacontype())) {
				wlanOBJ.setBasicAuthMode("Both");
				wlanOBJ.setWepEncrLevel("40-bit");
				wlanOBJ.setEncryptionType(2);
			} else if ("WPA".equals(wlanOBJ.getBeacontype())) {
				wlanOBJ.setWpaAuthMode("PSKAuthentication");
				wlanOBJ.setWpaEncrMode("TKIPEncryption");
				wlanOBJ.setEncryptionType(3);
			} else if ("11i".equals(wlanOBJ.getBeacontype())) {
				wlanOBJ.setIeeeAuthMode("PSKAuthentication");
				wlanOBJ.setIeeeEncrMode("TKIPEncryption");
				wlanOBJ.setEncryptionType(4);
			}else if("WPA/WPA2".equals(wlanOBJ.getBeacontype())){
				wlanOBJ.setWpaWpa2EncrMode("AESEncryption");
				wlanOBJ.setEncryptionType(5);
			}
		}
		
				
		if(Global.GW_TYPE_ITMS.equals(gw_type)){
			ajax = bio.edit(wlanOBJ);
			result = bio.getResult();
			ajax = ajax + "|" + result;
		}else{
			ajax = bio.editWlan(accOId, wlanOBJ, gw_type);
		}

		logger.warn("[{}] edit() ajax:{}",deviceId,ajax);
		return "ajax";
	}

	/**
	 * edit ssid.
	 */
	public String del() 
	{
		WlanOBJ obj = new WlanOBJ();
		obj.setDeviceId(deviceId);
		obj.setLanId(lanId);
		obj.setLanWlanId(lanWlanId);

		ajax = bio.del(obj);
		result = bio.getResult();
		ajax = ajax + "|" + result;
		
		logger.warn("[{}] del() ajax:{}",deviceId,ajax);
		return "ajax";
	}

	
	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}
	
	public void setBio(WlanBIO bio) {
		this.bio = bio;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public List<WlanOBJ> getList() {
		return list;
	}

	public void setList(List<WlanOBJ> list) {
		this.list = list;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getAjax() {
		return ajax;
	}

	public String getResult() {
		return result;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getApEnable() {
		return apEnable;
	}

	public String getHide() {
		return hide;
	}

	public String getPowerlevel() {
		return powerlevel;
	}

	public String getPowervalue() {
		return powervalue;
	}

	public String getWpsKeyWord() {
		return wpsKeyWord;
	}

	public void setApEnable(String apEnable) {
		this.apEnable = apEnable;
	}

	public void setHide(String hide) {
		this.hide = hide;
	}

	public void setPowerlevel(String powerlevel) {
		this.powerlevel = powerlevel;
	}

	public void setPowervalue(String powervalue) {
		this.powervalue = powervalue;
	}

	public void setWpsKeyWord(String wpsKeyWord) {
		this.wpsKeyWord = wpsKeyWord;
	}

	public String getLanId() {
		return lanId;
	}

	public String getLanWlanId() {
		return lanWlanId;
	}

	public void setLanId(String lanId) {
		this.lanId = lanId;
	}

	public void setLanWlanId(String lanWlanId) {
		this.lanWlanId = lanWlanId;
	}

	public String getSsid() {
		return ssid;
	}

	public String getBeacontype() {
		return beacontype;
	}

	public String getKey() {
		return key;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public void setBeacontype(String beacontype) {
		this.beacontype = beacontype;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public String getWepEncrLevel() {
		return wepEncrLevel;
	}

	public void setWepEncrLevel(String wepEncrLevel) {
		this.wepEncrLevel = wepEncrLevel;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public static String getInstArea() {
		return instArea;
	}

	public static void setInstArea(String instArea) {
		WlanACT.instArea = instArea;
	}

	public WlanBIO getBio() {
		return bio;
	}

	public int getStrategyType() {
		return strategyType;
	}

	public int getServiceId() {
		return serviceId;
	}

	public static String getVendoridHw() {
		return VENDORID_HW;
	}

	public static String getVendoridZx() {
		return VENDORID_ZX;
	}

	public String getQueryFrom() {
		return queryFrom;
	}

	public void setQueryFrom(String queryFrom) {
		this.queryFrom = queryFrom;
	}

	public String getEncr_mode() {
		return encr_mode;
	}

	public void setEncr_mode(String encr_mode) {
		this.encr_mode = encr_mode;
	}
	
	
}
