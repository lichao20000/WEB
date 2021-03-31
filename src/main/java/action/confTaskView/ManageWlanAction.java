package action.confTaskView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.paramConfig.ParamInfoAct;
import com.linkage.litms.paramConfig.WlanConfigDAO;
import com.linkage.litms.paramConfig.WlanObj;
import com.linkage.module.gwms.Global;

/**
 * @author OneLineSky
 * @date 2008-12-26
 */
public class ManageWlanAction 
{
	private static Logger logger = LoggerFactory.getLogger(ManageWlanAction.class);
	private String device_id = "";
	private String lan_id = "";
	private String lan_wlan_id = "";
	private String gather_time = "";
	private String ap_enable = "";
	private String powerlevel = "";
	private String enable = "";
	private String ssid = "";
	private String hide = "";
	private String beacontype = "";
	private String encr_mode="";
	private String wepEncrLevel="";
	private String wep_key = "";
	private String wpa_key = "";
	private String ssid_old = "";
	private String hide_old = "";
	private String beacontype_old = "";
	private String wep_key_old = "";
	private String wpa_key_old = "";
	private String gwType="";
	private String ajax;
	/**华为厂商*/
	public static final String VENDORID_HW="2";
	/**中兴厂商*/
	public static final String VENDORID_ZX="10";
	
	/**
	 * wlan节点删除
	 */
	public String delWlan() 
	{
		logger.warn("[{}] ManageWlanAction.delWlan(lan_id:{},lan_wlan_id:{})",device_id,lan_id,lan_wlan_id);
		ajax = "结点删除失败";
		if ((new ParamInfoAct(true)).deleteWlanConnection(device_id, lan_id, lan_wlan_id,gwType)) {
			ajax = "结点删除成功";
		}

		logger.warn("[{}] ManageWlanAction.delWlan(lan_id:{},lan_wlan_id:{},result:{})",
						new Object[]{device_id,lan_id,lan_wlan_id,ajax});
		return "ajax";
	}

	/**
	 *  wlan节点修改
	 */
	public String edit() 
	{
		logger.warn("[{}] ManageWlanAction.edit(lan_id:{},lan_wlan_id:{},encr_mode:{},gather_time:{})",
				new Object[]{device_id,lan_id,lan_wlan_id,encr_mode,gather_time});
		logger.warn("[{}] ManageWlanAction.edit(ap_enable:{},powerlevel:{},enable:{})",
				new Object[]{device_id,ap_enable,powerlevel,enable});
		logger.warn("[{}] ManageWlanAction.edit(ssid:{}->{},beacontype:{}->{},"
						+"wep_key:{}->{},wpa_key:{}->{},hide:{}->{})",
				new Object[]{device_id,ssid_old,ssid,beacontype_old,beacontype,wep_key_old,wep_key,
								wpa_key_old,wpa_key,hide_old,hide});

		WlanObj wlanObj = new WlanObj();
		wlanObj.setDeviceId(device_id);
		wlanObj.setLanId(lan_id);
		wlanObj.setLanWlanId(lan_wlan_id);
		wlanObj.setGatherTime(gather_time);
		wlanObj.setApEnable(ap_enable);
		wlanObj.setPowerLevel(powerlevel);
		wlanObj.setEnable(enable);
		wlanObj.setWepKeyId("1");
		wlanObj.setRadioEnable("0");
		wlanObj.setSsid(ssid);
		wlanObj.setHide(hide);
		
		boolean flag=false;
		if(Global.XJDX.equals(Global.instAreaShortName))
		{
			String vendor_id=new WlanConfigDAO().getDevVendorId(device_id);
			if("WEP".equals(beacontype)){
				beacontype="Basic";
			}else if("WPA-PSK".equals(beacontype)){
				beacontype="WPA";
			}else if("WPA2-PSK".equals(beacontype)){
				beacontype="WPA2";
				if(!VENDORID_HW.equals(vendor_id) && !VENDORID_ZX.equals(vendor_id)){
					beacontype="11i";
				}
			}else if("WPA-PSK/WPA2-PSK".equals(beacontype)){
				beacontype="WPA/WPA2";
				if(!VENDORID_HW.equals(vendor_id) && !VENDORID_ZX.equals(vendor_id)){
					beacontype="WPAand11i";
				}
			}
			wlanObj.setBeacontType(beacontype);
			if("Basic".equals(beacontype)){
				wlanObj.setBasicAuthMode(encr_mode);
			}else if("WPA".equals(beacontype) 
					||"WPA2".equals(beacontype) 
					||"WPA/WPA2".equals(beacontype)
					||"11i".equals(beacontype) 
					||"WPAand11i".equals(beacontype)){
				wlanObj.setWpaEncrMode(encr_mode);
			}
			
			wlanObj.setWepKey(wep_key);
			wlanObj.setWpaKey(wpa_key);
			
			flag=new ParamInfoAct(vendor_id).updateNode(wlanObj);
		}else{
			flag=new ParamInfoAct(true).updateWlanConnection(wlanObj);
		}
		
		ajax = flag?"结点修改成功":"结点修改失败";
		
		logger.warn("[{}] ManageWlanAction.edit(lan_id:{},lan_wlan_id:{},result:{})",
				new Object[]{device_id,lan_id,lan_wlan_id,ajax});
		return "ajax";
	}

	/**
	 * wlan节点增加
	 */
	public String add() 
	{
		logger.warn("[{}] ManageWlanAction.add(ssid:{},hide:{},beacontype:{},encr_mode:{}，wep_key:{},wpa_key:{})",
				new Object[]{device_id,ssid,hide,beacontype,encr_mode,wep_key,wpa_key});
		
		WlanObj wlanObj = new WlanObj();
		
		wlanObj.setDeviceId(device_id);
		wlanObj.setSsid(ssid);
		wlanObj.setHide(hide);
		wlanObj.setWepKey(wep_key);
		wlanObj.setWpaKey(wpa_key);
		
		boolean flag=false;
		if(Global.XJDX.equals(Global.instAreaShortName))
		{
			String vendor_id=new WlanConfigDAO().getDevVendorId(device_id);
			if("WEP".equals(beacontype)){
				beacontype="Basic";
			}else if("WPA-PSK".equals(beacontype)){
				beacontype="WPA";
			}else if("WPA2-PSK".equals(beacontype)){
				beacontype="WPA2";
				if(!VENDORID_HW.equals(vendor_id) && !VENDORID_ZX.equals(vendor_id)){
					beacontype="11i";
				}
			}else if("WPA-PSK/WPA2-PSK".equals(beacontype)){
				beacontype="WPA/WPA2";
				if(!VENDORID_HW.equals(vendor_id) && !VENDORID_ZX.equals(vendor_id)){
					beacontype="WPAand11i";
				}
			}
			wlanObj.setBeacontType(beacontype);
			if("Basic".equals(beacontype)){
				wlanObj.setBasicAuthMode(encr_mode);
			}else if("WPA".equals(beacontype) 
					||"WPA2".equals(beacontype) 
					||"WPA/WPA2".equals(beacontype) 
					||"11i".equals(beacontype) 
					||"WPAand11i".equals(beacontype)){
				wlanObj.setWpaEncrMode(encr_mode);
			}
			
			flag=new ParamInfoAct(vendor_id).addNode(wlanObj);
		}else{
			flag=new ParamInfoAct(true).AddWlan(wlanObj);
		}
		
		ajax=flag?"结点新增成功":"结点新增失败";
		logger.warn("[{}] ManageWlanAction.add(ssid:{},result:{})",
				new Object[]{device_id,ssid,ajax});
		
		return "ajax";
	}
	
	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getBeacontype() {
		return beacontype;
	}

	public void setBeacontype(String beacontype) {
		this.beacontype = beacontype;
	}

	public String getBeacontype_old() {
		return beacontype_old;
	}

	public void setBeacontype_old(String beacontype_old) {
		this.beacontype_old = beacontype_old;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getHide() {
		return hide;
	}

	public void setHide(String hide) {
		this.hide = hide;
	}

	public String getHide_old() {
		return hide_old;
	}

	public void setHide_old(String hide_old) {
		this.hide_old = hide_old;
	}

	public String getLan_wlan_id() {
		return lan_wlan_id;
	}

	public void setLan_wlan_id(String lan_wlan_id) {
		this.lan_wlan_id = lan_wlan_id;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public String getSsid_old() {
		return ssid_old;
	}

	public void setSsid_old(String ssid_old) {
		this.ssid_old = ssid_old;
	}

	public String getLan_id() {
		return lan_id;
	}

	public void setLan_id(String lan_id) {
		this.lan_id = lan_id;
	}

	public String getWep_key() {
		return wep_key;
	}

	public void setWep_key(String wep_key) {
		this.wep_key = wep_key;
	}

	public String getWep_key_old() {
		return wep_key_old;
	}

	public void setWep_key_old(String wep_key_old) {
		this.wep_key_old = wep_key_old;
	}

	public String getWpa_key() {
		return wpa_key;
	}

	public void setWpa_key(String wpa_key) {
		this.wpa_key = wpa_key;
	}

	public String getWpa_key_old() {
		return wpa_key_old;
	}

	public void setWpa_key_old(String wpa_key_old) {
		this.wpa_key_old = wpa_key_old;
	}

	public String getAp_enable() {
		return ap_enable;
	}

	public void setAp_enable(String ap_enable) {
		this.ap_enable = ap_enable;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getGather_time() {
		return gather_time;
	}

	public void setGather_time(String gather_time) {
		this.gather_time = gather_time;
	}

	public String getPowerlevel() {
		return powerlevel;
	}

	public void setPowerlevel(String powerlevel) {
		this.powerlevel = powerlevel;
	}

	public String getGwType() {
		return gwType;
	}

	public void setGwType(String gwType) {
		this.gwType = gwType;
	}

	public String getEncr_mode() {
		return encr_mode;
	}

	public void setEncr_mode(String encr_mode) {
		this.encr_mode = encr_mode;
	}

	public String getWepEncrLevel() {
		return wepEncrLevel;
	}

	public void setWepEncrLevel(String wepEncrLevel) {
		this.wepEncrLevel = wepEncrLevel;
	}
	
	
}
