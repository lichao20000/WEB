
package com.linkage.module.gtms.stb.resource.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.WebUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.resource.serv.MacQueryBIO;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-7-19
 * @category com.linkage.module.lims.stb.resource.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class MacQueryACT extends splitPageAction
{

	private static final long serialVersionUID = 5630316259451535268L;
	private static Logger logger = LoggerFactory.getLogger(MacQueryACT.class);
	private String cpe_mac;
	private String vendor_name;
	private String device_model;
	private String supply_mode;
	private String deviceId;
	private String orderId;
	private String packageNo;
	private String vendorName;
	private String supplyMode;
	private String deviceModel;
	private String mac;
	private String deviceSn;
	private String type;
	private String isMAC;
	// 是否登录
	private String isFlag;
	private String ajax;
	private List<Map> macInfoList = new ArrayList<Map>();
	private List<Map> vendorList = new ArrayList<Map>();
	private List<Map> deviceModelList = new ArrayList<Map>();
	private String vendorId;
	private MacQueryBIO bio;

	public String execute()
	{
		logger.debug("execute");
		UserRes curUser = WebUtil.getCurrentUser();
		if (curUser != null)
		{
			isFlag = "1";
		}
		else
		{
			isFlag = "0";
		}
		return "init";
	}

	public String query()
	{
		UserRes curUser = WebUtil.getCurrentUser();
		if (curUser != null)
		{
			isFlag = "1";
		}
		else
		{
			isFlag = "0";
		}
		// MAC查询条件采用后模糊查询，并且自动转化为大写，去掉冒号
		if (!StringUtil.IsEmpty(cpe_mac))
		{
			cpe_mac = cpe_mac.replace(":", "").replace("：", "").toUpperCase();
		}
		totalRowCount_splitPage = bio.getCount(cpe_mac, vendor_name, device_model,
				supply_mode, curPage_splitPage, num_splitPage);
		macInfoList = bio.getMacInfo(cpe_mac, vendor_name, device_model, supply_mode,
				curPage_splitPage, num_splitPage);
		return "list";
	}

	public String addedit()
	{
		UserRes curUser = WebUtil.getCurrentUser();
		String staffId = "";
		String cityId = "";
		if (null != curUser)
		{
			staffId = curUser.getUser().getAccount();
			cityId = curUser.getUser().getCityId();
		}
		if (!StringUtil.IsEmpty(mac))
		{
			mac = mac.toUpperCase();
		}
		if ("1".equals(type))
		{
			ajax = bio.addMac(orderId, packageNo, vendorName, supplyMode, deviceModel,
					mac, deviceSn, cityId, staffId);
		}
		else
		{
			ajax = bio.editMac(orderId, packageNo, vendorName, supplyMode, deviceModel,
					mac, deviceSn, deviceId, cityId, staffId);
		}
		return "ajax";
	}
	
	public String getVendorList(){
		this.ajax=bio.getVendorList();
		return "ajax";
	}
	
	public String getDeviceModelList(){
		this.ajax=bio.getDeviceModelList(vendorId);
		return "ajax";
	}
	public String validateMAC(){
		ajax=bio.validateMAC(vendorId, cpe_mac) ;
		return "ajax";
	}
	public String getCpe_mac()
	{
		return cpe_mac;
	}

	public void setCpe_mac(String cpe_mac)
	{
		this.cpe_mac = cpe_mac;
	}

	public String getVendor_name()
	{
		return vendor_name;
	}

	public void setVendor_name(String vendor_name)
	{
		this.vendor_name = vendor_name;
	}

	public String getDevice_model()
	{
		return device_model;
	}

	public void setDevice_model(String device_model)
	{
		this.device_model = device_model;
	}

	public String getSupply_mode()
	{
		return supply_mode;
	}

	public void setSupply_mode(String supply_mode)
	{
		this.supply_mode = supply_mode;
	}

	public List<Map> getMacInfoList()
	{
		return macInfoList;
	}

	public void setMacInfoList(List<Map> macInfoList)
	{
		this.macInfoList = macInfoList;
	}

	public MacQueryBIO getBio()
	{
		return bio;
	}

	public void setBio(MacQueryBIO bio)
	{
		this.bio = bio;
	}

	public String getOrderId()
	{
		return orderId;
	}

	public void setOrderId(String orderId)
	{
		this.orderId = orderId;
	}

	public String getPackageNo()
	{
		return packageNo;
	}

	public void setPackageNo(String packageNo)
	{
		this.packageNo = packageNo;
	}

	public String getVendorName()
	{
		return vendorName;
	}

	public void setVendorName(String vendorName)
	{
		this.vendorName = vendorName;
	}

	public String getIsFlag()
	{
		return isFlag;
	}

	public void setIsFlag(String isFlag)
	{
		this.isFlag = isFlag;
	}

	public String getSupplyMode()
	{
		return supplyMode;
	}

	public void setSupplyMode(String supplyMode)
	{
		this.supplyMode = supplyMode;
	}

	public String getDeviceModel()
	{
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel)
	{
		this.deviceModel = deviceModel;
	}

	public String getMac()
	{
		return mac;
	}

	public void setMac(String mac)
	{
		this.mac = mac;
	}

	public String getDeviceSn()
	{
		return deviceSn;
	}

	public void setDeviceSn(String deviceSn)
	{
		this.deviceSn = deviceSn;
	}

	public String getDeviceId()
	{
		return deviceId;
	}

	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public void setVendorList(List<Map> vendorList) {
		this.vendorList = vendorList;
	}

	public void setDeviceModelList(List<Map> deviceModelList) {
		this.deviceModelList = deviceModelList;
	}

	public String getIsMAC() {
		return isMAC;
	}

	public void setIsMAC(String isMAC) {
		this.isMAC = isMAC;
	}
	
}
