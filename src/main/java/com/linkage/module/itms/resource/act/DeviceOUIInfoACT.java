package com.linkage.module.itms.resource.act;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.itms.resource.bio.DeviceOUIInfoBIO;

public class DeviceOUIInfoACT extends splitPageAction implements
		ServletRequestAware, SessionAware {
	
	private static Logger logger = LoggerFactory.getLogger(DeviceOUIInfoACT.class);
	private HttpServletRequest request;
	private Map session;
	
	private DeviceOUIInfoBIO bio;
	
	private List<Map<String,String>> vendorMap;
	private List<Map<String,String>> vendorMapStb;
	private List<Map<String,String>> ouiMap;
	
	private String oui;
	private String vendor_name;
	private String vendor_name_stb;
	
	private String ajax;
 	private String id;
 	private String ouiId;
 	private String ouiDesc;
 	private String vendorName; 
 	private String remark;
 	private String deviceModel;
 	private String deviceType;
	private String deviceModelId;
	private String vendorId;
	private int isLink;
	private String device_type_qry;

	public int getIsLink() {
		return isLink;
	}

	public void setIsLink(int isLink) {
		this.isLink = isLink;
	}

	public String getDeviceModelId() {
		return deviceModelId;
	}

	public void setDeviceModelId(String deviceModelId) {
		this.deviceModelId = deviceModelId;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	
	

	//登录的账户
 	private String userName;
	
	private List<Map> dataList;
	
	public String init(){
		logger.debug("DeviceOUIInfoACT->init");
		//ouiMap = bio.getOUIMap();
		vendorMap = bio.getVendorMap();
		UserRes curUser = (UserRes) session.get("curUser");
		if(null != curUser){
			userName = curUser.getUser().getAccount();
		}
		if(LipossGlobals.inArea(Global.CQDX) ){
			return "modelinit";
		}
		if(LipossGlobals.inArea(Global.SXLT)){
			vendorMapStb = bio.getVendorMapStb();
			return "modelInitSxlt";
		}
		return "init";
	}
	
	public String getDeviceOuiInfo(){
		logger.debug("DeviceOUIInfoACT->getDeviceOuiInfo");
		dataList = bio.getDeviceOUIinfo(oui, vendor_name, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countDeviceOUIinfo(oui, vendor_name, curPage_splitPage, num_splitPage);
		UserRes curUser = (UserRes) session.get("curUser");
		if(null != curUser){
			userName = curUser.getUser().getAccount();
		}
		if(LipossGlobals.inArea(Global.CQDX)){
			return "modellist";
		}
		
		return "list";
	}
	
	
	public String getDeviceOuiInfoSxlt(){
		logger.debug("DeviceOUIInfoACT->getDeviceOuiInfo");
		dataList = bio.getDeviceOUIinfo(device_type_qry, oui, vendor_name, vendor_name_stb, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countDeviceOUIinfo(device_type_qry, oui, vendor_name, vendor_name_stb, curPage_splitPage, num_splitPage);
		UserRes curUser = (UserRes) session.get("curUser");
		if(null != curUser){
			userName = curUser.getUser().getAccount();
		}
		return "modelListSxlt";
	}

	public String addSXLTOUI(){
		logger.debug("DeviceOUIInfoACT->addSXLTOUI");
		String add_date = getNowDate();
		int result = bio.addSXLTOUI( ouiId,ouiDesc, vendorName, remark, add_date, deviceModel,deviceType,deviceModelId,vendorId);
		if(result==1){
			ajax = "新增成功!";
		}else if(result==2) {
			ajax = "已存在!";
		}else{
			ajax = "新增失败!";
		}
		return "ajax";
	}

	public String deleteSXLTOUI(){
		logger.debug("DeviceOUIInfoACT->deleteSXLTOUI");
		int result = bio.deleteSXLTOUI(id);
		if(result==1){
			ajax = "1";
		}else{
			ajax = "0";
		}
		return "ajax";
	}

	public String deleteOUI(){
		logger.debug("DeviceOUIInfoACT->deleteOUI");
		int result = bio.deleteOUI(id,deviceType);
		if(result==1){
			ajax = "1";
		}else{
			ajax = "0";
		}
		return "ajax";
	}
	
	public String editOUI(){
		logger.debug("DeviceOUIInfoACT->editOUI");
		String add_date = getNowDate();
		int result = bio.editOUI(id, ouiId, ouiDesc, vendorName, remark, add_date, deviceModel, deviceType);
		if(result==1){
			ajax = "编辑成功!";
		}else{
			ajax = "编辑失败!";
		}
		return "ajax";
	}
	
	public String addOUI(){
		logger.debug("DeviceOUIInfoACT->addOUI");
		String add_date = getNowDate();
		int result = bio.addOUI( ouiId, ouiDesc, vendorName, remark, add_date, deviceModel);
		if(result==1){
			ajax = "新增成功!";
		}else{
			ajax = "新增失败!";
		}
		return "ajax";
	}
	
	private String getNowDate()
	{
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		String time = fmtrq.format(now.getTime());
		DateTimeUtil dt = new DateTimeUtil(time);
		return String.valueOf(dt.getLongTime());
	}
	
	
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public void setServletRequest(HttpServletRequest req) {
		
		this.request = req;
	}

	public DeviceOUIInfoBIO getBio() {
		return bio;
	}

	public void setBio(DeviceOUIInfoBIO bio) {
		this.bio = bio;
	}


	public List<Map<String, String>> getVendorMap() {
		return vendorMap;
	}

	public void setVendorMap(List<Map<String, String>> vendorMap) {
		this.vendorMap = vendorMap;
	}

	public List<Map<String, String>> getOuiMap() {
		return ouiMap;
	}

	public void setOuiMap(List<Map<String, String>> ouiMap) {
		this.ouiMap = ouiMap;
	}

	public String getOui() {
		return oui;
	}

	public void setOui(String oui) {
		this.oui = oui;
	}

	public String getVendor_name() {
		return vendor_name;
	}

	public void setVendor_name(String vendor_name) {
		this.vendor_name = vendor_name;
	}

	public List<Map> getDataList() {
		return dataList;
	}

	public void setDataList(List<Map> dataList) {
		this.dataList = dataList;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOuiId() {
		return ouiId;
	}

	public void setOuiId(String ouiId) {
		this.ouiId = ouiId;
	}

	public String getOuiDesc() {
		return ouiDesc;
	}

	public void setOuiDesc(String ouiDesc) {
		this.ouiDesc = ouiDesc;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	
	public String getDeviceModel()
	{
		return deviceModel;
	}

	
	public void setDeviceModel(String deviceModel)
	{
		this.deviceModel = deviceModel;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	
	public String getDevice_type_qry()
	{
		return device_type_qry;
	}

	
	public void setDevice_type_qry(String device_type_qry)
	{
		this.device_type_qry = device_type_qry;
	}

	
	public String getVendor_name_stb()
	{
		return vendor_name_stb;
	}

	
	public void setVendor_name_stb(String vendor_name_stb)
	{
		this.vendor_name_stb = vendor_name_stb;
	}

	
	public List<Map<String, String>> getVendorMapStb()
	{
		return vendorMapStb;
	}

	
	public void setVendorMapStb(List<Map<String, String>> vendorMapStb)
	{
		this.vendorMapStb = vendorMapStb;
	}

}
