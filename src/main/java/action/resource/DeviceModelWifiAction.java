package action.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.PageQueryActionSupport;
import com.linkage.litms.resource.DeviceAct;

import dao.resource.DeviceModelDao;
import dao.resource.DeviceModelWifiDao;

/**
 * @author Jason(3412)
 * @date 2008-11-10
 */
public class DeviceModelWifiAction extends PageQueryActionSupport{
	private static Logger logger = LoggerFactory.getLogger(DeviceModelWifiAction.class);
	private String actionType;
	private String strVendorList;
	private String deviceModelName;
	private String vendorId;
	private String oui;
	private String deviceModelId;
	private List deviceModelList;
	private String vendorAlias = "vendor_id";
	private String ethernum;
	private String etherrate;
	private String strBack; 
	private int wifi_ability; 
	// 导出数据
	private List<Map> data;
	// 导出文件列标题
	private String[] title;
	// 导出文件列
	private String[] column;
	// 导出文件名
	private String fileName;
	
	private DeviceModelWifiDao modelDao;
	
	public String execute(){
		//每页显示15条
		numperpage = 15;
		//操作结果反馈，提示用户
		strBack = "";
		if("edit".equals(actionType)){
			modelDao.updateDeviceModel(oui, deviceModelName, deviceModelId,wifi_ability);
			strBack = "编辑成功";
		}
		//获取型号列表
		List list= QueryPage(modelDao.getAllModelWifiList());
		this.setDeviceModelList(list);
		
		//初始化厂商OUI列表
		this.setStrVendorList(new DeviceAct().getVendorList(true,"",vendorAlias));
		//执行完之后，置空
		actionType = "";
		return "modelWifiInfo";
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public String getExcel()
	{
		logger.debug("getExcel()");
		fileName = "MODELWIFIINFO";
		title = new String[] { "型号ID", "厂商名称", "型号名称", "WIFI能力等级" ,"WIFI能力描述"};
		column = new String[] { "modelid", "vendorname", "modelname", "wifiability","wifiabilitydesc" };
		
		data = modelDao.getAllModelWifiList();
		return "excel";
	}

	
	public String getDeviceModelName() {
		return deviceModelName;
	}

	public void setDeviceModelName(String deviceModelName) {
		this.deviceModelName = deviceModelName;
	}

	public void setStrVendorList(String strVendorList) {
		this.strVendorList = strVendorList;
	}

	public String getStrVendorList() {
		return strVendorList;
	}


	public String getActionType() {
		return actionType;
	}


	public String getOui() {
		return oui;
	}


	public void setOui(String oui) {
		this.oui = oui;
	}


	public List getDeviceModelList() {
		return deviceModelList;
	}


	public void setDeviceModelList(List deviceModelList) {
		this.deviceModelList = deviceModelList;
	}


	public void setModelDao(DeviceModelWifiDao modelDao) {
		this.modelDao = modelDao;
	}


	public String getVendorAlias() {
		return vendorAlias;
	}


	public void setDeviceModelId(String deviceModelId) {
		this.deviceModelId = deviceModelId;
	}


	public void setActionType(String actionType) {
		this.actionType = actionType;
	}


	public String getStrBack() {
		return strBack;
	}

	
	public String getEthernum()
	{
		return ethernum;
	}

	
	public void setEthernum(String ethernum)
	{
		this.ethernum = ethernum;
	}

	
	public String getEtherrate()
	{
		return etherrate;
	}

	
	public void setEtherrate(String etherrate)
	{
		this.etherrate = etherrate;
	}	
	
	/**
	 * @return the data
	 */
	public List<Map> getData()
	{
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<Map> data)
	{
		this.data = data;
	}

	/**
	 * @return the title
	 */
	public String[] getTitle()
	{
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String[] title)
	{
		this.title = title;
	}

	/**
	 * @return the column
	 */
	public String[] getColumn()
	{
		return column;
	}

	/**
	 * @param column
	 *            the column to set
	 */
	public void setColumn(String[] column)
	{
		this.column = column;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	
	public void setVendorAlias(String vendorAlias)
	{
		this.vendorAlias = vendorAlias;
	}

	
	public String getVendorId()
	{
		return vendorId;
	}

	
	public void setVendorId(String vendorId)
	{
		this.vendorId = vendorId;
	}

	
	public int getWifi_ability()
	{
		return wifi_ability;
	}

	
	public void setWifi_ability(int wifi_ability)
	{
		this.wifi_ability = wifi_ability;
	}
	
	
}
