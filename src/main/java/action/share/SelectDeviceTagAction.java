package action.share;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bio.share.SelectDeviceTagBIO;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * @author ASUS E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-2-2
 * @category action.share
 * 
 */
public class SelectDeviceTagAction implements ServletRequestAware{
	/** log */
	private static Logger logger = LoggerFactory.getLogger(SelectDeviceTagAction.class);
	/**
	 * 所有的属地
	 */
	private List cityList = null;
	/**
	 * 查询类型
	 */
	private String select_type = null;
	/**
	 * 属地
	 */
	private String city_id = null;
	/**
	 * 局向
	 */
	private String office_id = null;
	/**
	 * 在线状态
	 */
	private String online_status = null;
	/**
	 * 厂商Id
	 */
	private String vendor_id = null;
	/**
	 * 型号Id
	 */
	private String device_model_id = null;
	/**
	 * 版本Id
	 */
	private String devicetype_id = null;
	/**
	 * 用户账号
	 */
	private String hguser = null;
	
	/**
	 * VOIP 电话号码
	 */
	private String voipPara = null;
	
	// 按iTV业务帐号查询
	private String itvUserName = null;
	
	
	// 按宽带主帐号
	private String wideNetPara = null;
	/**
	 * 设备序列号
	 */
	private String device_serialnumber = null;
	/**
	 * IP
	 */
	private String loopback_ip = null;
	/**
	 * 所有的厂商
	 */
	private List vendorList = null;
	/**
	 * 局向
	 */
	private List officeList = null;
	/**
	 * request取登陆帐号使用
	 */
	private HttpServletRequest request;
	/**
	 * 作为ajax调用的返回
	 */
	private String ajax = null;
	/**
	 * 生成查询结果显示的类型
	 */
	private String selectType = null;
	/**
	 * 控制显示设备列表时显示条数
	 */
	private String listControl = "20";
	/**
	 * 生成查询结果显示的点击操作方法名
	 */
	private String jsFunctionName = null;
	/**
	 * 生成查询结果显示的点击操作方法中，是否传递设备序列号
	 */
	private String jsFunctionNameBySn = null;
	/**
	 * 文件解析的最多行数
	 */
	private String maxFileNum = null;
	/**
	 * 导入文件,根据此文件解析用户账号
	 */
	private File file;
	/**
	 * 文件导入时返回值
	 */
	private String byImportList = null;
	/**
	 * 按设备查询显示情况
	 */
	private String byDeviceno = "2";
	/**
	 * 按设备查询显示情况
	 */
	private String byDevicenoState = "";
	/**
	 * 按设备查询显示情况
	 */
	private String byDevicenoChecked = "checked";
	/**
	 * 按设备查询显示情况
	 */
	private String byDevicenoParam = "";
	/**
	 * 按用户查询显示情况
	 */
	private String byUsername = "1";
	/**
	 * 按用户查询显示情况
	 */
	private String byUsernameState = "";
	/**
	 * 按用户查询显示情况
	 */
	private String byUsernameChecked = "";
	/**
	 * 按用户查询显示情况
	 */
	private String byUsernameParam = "none";
	
	
	
	// 按VOIP电话号码 add by zhangchy 2012-02-21  begin
	private String byVoipTelNum = "3";
	private String byVOIPState = "";
	private String byVOIPChecked = "";
	private String byVOIPParam = "none";
	//  按VOIP电话号码 add by zhangchy 2012-02-21  end
	
	
	
	// 需求单 JSDX_ITMS-REQ-20120222-LUHJ-001  add by zhangchy 2012-02-29 begin 
	// 按iTV 
	private String byItv = "5";
	private String byItvState = "";
	private String byItvChecked = "";
	private String byItvParam = "none";
	
	// 按宽带主帐号
	private String byWideNet = "6";
	private String byNetAccountState = "";
	private String byNetAccountChecked = "";
	private String byWideNetParam = "none";
	//  需求单 JSDX_ITMS-REQ-20120222-LUHJ-001  add by zhangchy 2012-02-29 end
	
	
	/**
	 * 按高级查询显示情况
	 */
	private String byCity = "1";
	/**
	 * 按高级查询显示情况
	 */
	private String byCityState = "";
	/**
	 * 按高级查询显示情况
	 */
	private String byCityChecked = "";
	/**
	 * 按高级查询显示情况
	 */
	private String byCityParam = "none";
	/**
	 * 按文件导入查询显示情况
	 */
	private String byImport = "0";
	/**
	 * 按文件导入查询显示情况
	 */
	private String byImportState = "none";
	/**
	 * 按文件导入查询显示情况
	 */
	private String byImportChecked = "";
	/**
	 * 按文件导入查询显示情况
	 */
	private String byImportParam = "none";
	
	private SelectDeviceTagBIO selectDeviceTagBIO;
	
	private  int  gw_type;
	
	public int getGw_type()
	{
		return gw_type;
	}

	
	public void setGw_type(int gwType)
	{
		gw_type = gwType;
	}

	/**
	 * @category 默认的方法，待写
	 * 
	 * @return
	 * @throws Exception
	 */
	public String execute() throws Exception {
		return null;
	}
	
	/**
	 * @category 默认的方法，待写
	 * 
	 * @return
	 * @throws Exception
	 */
	public String initImport() throws Exception {
		
		return "initImport";
	}
	
	/**
	 * @category reload 初始化属地以及商场
	 * 
	 * @return “INIT”
	 * 
	 * @throws Exception
	 */
	public String reload() throws Exception {
		
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String curUserCityId = curUser.getCityId();
		if("null".equals(maxFileNum) || null==maxFileNum || "".equals(maxFileNum)){
			maxFileNum = "100";
		}
		
		// 增加了 && !"2".equals(this.byVoipTelNum)  用于宁夏 按VOIP电话号码查询 add by zhangchy 2012-02-21
		if (!"2".equals(this.byDeviceno) && !"2".equals(this.byCity)
				&& !"2".equals(this.byUsername) && !"2".equals(this.byImport)
				&& !"2".equals(this.byVoipTelNum) && !"2".equals(byItv) && !"2".equals(byWideNet)) {
			this.byDeviceno = "2";
		}
		
		//需要显示的选项
		if("0".equals(this.byDeviceno)){
			this.byDevicenoState="none";
		}else{
			this.byDevicenoState="";
		}
		if("0".equals(this.byUsername)){
			this.byUsernameState="none";
		}else{
			this.byUsernameState="";
		}
		if("0".equals(this.byCity)){
			this.byCityState="none";
		}else{
			this.byCityState="";
			this.cityList = CityDAO.getAllNextCityListByCityPid(curUserCityId);
			this.vendorList = selectDeviceTagBIO.getVendor();
			this.officeList = selectDeviceTagBIO.getOfficeList(this.city_id);
		}
		if("0".equals(this.byImport)){
			this.byImportState="none";
		}else{
			this.byImportState="";
		}
		
		/**
		 * 用于宁夏 如果byVoipTelNum==0，则在界面上不显示
		 * add by zhangchy 2012-02-21
		 */
		if("0".equals(this.byVoipTelNum)){
			this.byVOIPState="none";
		}else{
			this.byVOIPState="";
		}
		
		// 需求单:JSDX_ITMS-REQ-20120222-LUHJ-001 add by zhangchy 2012-02-29
		// iTV  如果byItv==0，则在界面上不显示
		if("0".equals(this.byItv)){
			this.byItvState = "none";
		}else{
			this.byItvState = "";
		}
		
		// 需求单：JSDX_ITMS-REQ-20120222-LUHJ-001 add by zhangchy 2012-02-29
		// 宽带主帐号  如果byWideNet==0，则在界面上不显示
		if("0".equals(this.byWideNet)){
			this.byNetAccountState = "none";
		}else{
			this.byNetAccountState = "";
		}
		
		//当前选中的状态
		if("2".equals(this.byDeviceno)){
			this.byDevicenoChecked="checked";
			this.byDevicenoParam = "";
		}else{
			this.byDevicenoChecked="";
			this.byDevicenoParam = "none";
		}
		if("2".equals(this.byUsername)){
			this.byUsernameChecked="checked";
			this.byUsernameParam = "";
		}else{
			this.byUsernameChecked="";
			this.byUsernameParam = "none";
		}
		if("2".equals(this.byCity)){
			this.byCityChecked="checked";
			this.byCityParam = "";
		}else{
			this.byCityChecked="";
			this.byCityParam = "none";
		}
		if("2".equals(this.byImport)){
			this.byImportChecked="checked";
			this.byImportParam = "";
		}else{
			this.byImportChecked="";
			this.byImportParam = "none";
		}
		if("2".equals(this.byVoipTelNum)){
			this.byVOIPChecked = "checked";
			this.byVOIPParam = "";
		}else {
			this.byVOIPChecked = "";
			this.byVOIPParam = "none";
		}
		
		if("2".equals(this.byItv)){
			this.byItvChecked = "checked";
			this.byItvParam = "";
		}else {
			this.byItvChecked = "";
			this.byItvParam = "none";
		}
		
		if("2".equals(this.byWideNet)){
			this.byNetAccountChecked = "checked";
			this.byWideNetParam = "";
		}else {
			this.byNetAccountChecked = "";
			this.byWideNetParam = "none";
		}
		logger.debug("byDeviceno:"+byDeviceno);
		logger.debug("byUsername:"+byUsername);
		logger.debug("byCity:"+byCity);
		logger.debug("byImport:"+byImport);
		logger.debug("byVOIPTelNum:"+ byVoipTelNum);
		logger.debug("byItv:"+ byItv);
		logger.debug("byWideNet:"+ byWideNet);
		
		return "INIT";
	}
	
	/**
	 * @category getDevicetype 根据厂商查询设备型号
	 * 
	 * @return “ajax”
	 * 
	 * @throws Exception
	 */
	public String getDeviceModel() throws Exception {
		
		this.ajax = selectDeviceTagBIO.getDeviceModel(this.vendor_id);
		
		return "ajax";
	}
	
	/**
	 * @category getVersion 获取所有的设备版本
	 * 
	 * @return “ajax”
	 * 
	 * @throws Exception
	 */
	public String getVersion() throws Exception {
		
		this.ajax = selectDeviceTagBIO.getVersionList(this.device_model_id);
		
		return "ajax";
	}
	
	/**
	 * @category getDeviceByCity 获取所有的终端设备
	 * 
	 * @return “ajax”
	 * 
	 * @throws Exception
	 */
	public String getDeviceByCity() throws Exception {
		
		logger.debug("this.selectType"+this.selectType);
		logger.debug("this.jsFunctionName"+this.jsFunctionName);
		logger.warn("getDeviceByCity():"+ gw_type);
		this.ajax = selectDeviceTagBIO.getDeviceCheckboxBySenior(gw_type,city_id,office_id,vendor_id,device_model_id ,devicetype_id,loopback_ip,online_status,selectType,jsFunctionName,jsFunctionNameBySn,listControl);
		
		return "ajax";
	}
	
	/**
	 * @category getDeviceByUsername 获取所有的终端设备
	 * 
	 * @return “ajax”
	 * 
	 * @throws Exception
	 */
	public String getDeviceByUsername() throws Exception {
		
		logger.debug("this.selectType"+this.selectType);
		logger.debug("this.jsFunctionName"+this.jsFunctionName);
		
		//int gw_type = LipossGlobals.SystemType();
		
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String curUserCityId = curUser.getCityId();
		logger.warn("getDeviceByUsername():"+ gw_type);
		this.ajax = selectDeviceTagBIO.getDeviceCheckbox(this.selectType,gw_type,this.jsFunctionName,jsFunctionNameBySn,curUserCityId,this.select_type,this.hguser,null,listControl);
		
		return "ajax";
	}
	
	/**
	 * @category getDeviceByUsername 获取所有的终端设备
	 * 
	 * @return “ajax”
	 * 
	 * @throws Exception
	 */
	public String getDeviceBySerialno() throws Exception {
		
		//int gw_type = LipossGlobals.SystemType();
		
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String curUserCityId = curUser.getCityId();
		logger.warn("getDeviceBySerialno():"+ gw_type);
		this.ajax = selectDeviceTagBIO.getDeviceCheckbox(this.selectType,gw_type,this.jsFunctionName,jsFunctionNameBySn,curUserCityId,this.select_type,this.device_serialnumber,this.loopback_ip ,listControl);
		
		return "ajax";
	}
	
	/**
	 * @category 批量导入用户账号查询设备,查询的设备主要分为两种，
	 * 
	 * @author qxq(4174)
	 * @date 2009-04-13
	 * @return String
	 */
	public String getDeviceByImportUsername(){
		
		String queryCity = null;
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		queryCity = curUser.getCityId();
		//int gw_type = LipossGlobals.SystemType();
		
		//导入的文件中，前maxFileNum条数据
		List<String> username50List = selectDeviceTagBIO.getUsernameByIportFile(this.file,this.maxFileNum);
		logger.warn("getDeviceByImportUsername():"+ gw_type);
		this.byImportList = selectDeviceTagBIO.getDeviceCheckboxByImport(gw_type,queryCity,username50List,this.selectType,this.jsFunctionName,jsFunctionNameBySn,this.maxFileNum);
		
		return "importList";
	}
	
	
	
	/**
	 * @category getDeviceByVoipTelNum 获取所有的终端设备
	 * 
	 * @return “ajax”
	 * 
	 * @throws Exception
	 */
	public String getDeviceByVoipTelNum() throws Exception {
		logger.debug("getDeviceByVoipTelNum()");
		logger.debug("this.selectType"+this.selectType);
		logger.debug("this.jsFunctionName"+this.jsFunctionName);
		
		//int gw_type = LipossGlobals.SystemType();
		logger.warn("getDeviceByVoipTelNum():"+ gw_type);
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String curUserCityId = curUser.getCityId();
		
		this.ajax = selectDeviceTagBIO.getDeviceCheckbox(this.selectType,gw_type,this.jsFunctionName,jsFunctionNameBySn,curUserCityId,this.select_type,this.voipPara,null,listControl);
		
		return "ajax";
	}
	
	
	
	/**
	 * add by zhangchy 2012-03-01 
	 * 
	 * 需求单：JSDX_ITMS-REQ-20120222-LUHJ-001 
	 * 
	 * 按iTV业务帐号获取终端设备
	 * 
	 * @return "ajax"
	 * 
	 * @throws Exception
	 */
	public String getDeviceByITVUserName() throws Exception {
		logger.debug("getDeviceByITVUserName()");
		logger.debug("this.selectType"+this.selectType);
		logger.debug("this.jsFunctionName"+this.jsFunctionName);
		
		//int gw_type = LipossGlobals.SystemType();
		logger.warn("getDeviceByITVUserName():"+ gw_type);
		logger.warn("getDeviceByITVUserName:" + this.itvUserName);
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String curUserCityId = curUser.getCityId();
		
		this.ajax = selectDeviceTagBIO.getDeviceCheckbox(this.selectType,gw_type,this.jsFunctionName,jsFunctionNameBySn,curUserCityId,this.select_type,this.itvUserName,null,listControl);
		
		return "ajax";
	}
	
	
	
	/**
	 * add by zhangchy 2012-03-01 
	 * 
	 * 需求单：JSDX_ITMS-REQ-20120222-LUHJ-001 
	 * 
	 * 按宽带主帐号获取终端设备
	 * 
	 * @return "ajax"
	 * 
	 * @throws Exception
	 */
	public String getDeviceByWideAccount() throws Exception {
		logger.debug("getDeviceByWideAccount()");
		logger.debug("this.selectType"+this.selectType);
		logger.debug("this.jsFunctionName"+this.jsFunctionName);
		
		//int gw_type = LipossGlobals.SystemType();
		logger.warn("getDeviceByWideAccount():"+ gw_type);
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String curUserCityId = curUser.getCityId();
		
		this.ajax = selectDeviceTagBIO.getDeviceCheckbox(this.selectType,gw_type,this.jsFunctionName,jsFunctionNameBySn,curUserCityId,this.select_type,this.wideNetPara,null,listControl);
		
		return "ajax";
	}
	
	
	/**
	 * @category setServletRequest
	 * 
	 */
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}
	
	/**
	 * @return the cityList
	 */
	public List getCityList() {
		return cityList;
	}

	/**
	 * @param cityList the cityList to set
	 */
	public void setCityList(List cityList) {
		this.cityList = cityList;
	}

	/**
	 * @return the officeList
	 */
	public List getOfficeList() {
		return officeList;
	}

	/**
	 * @param officeList the officeList to set
	 */
	public void setOfficeList(List officeList) {
		this.officeList = officeList;
	}

	/**
	 * @return the vendorList
	 */
	public List getVendorList() {
		return vendorList;
	}

	/**
	 * @param vendorList the vendorList to set
	 */
	public void setVendorList(List vendorList) {
		this.vendorList = vendorList;
	}

	/**
	 * @return the selectDeviceTagBIO
	 */
	public SelectDeviceTagBIO getSelectDeviceTagBIO() {
		return selectDeviceTagBIO;
	}
	/**
	 * @param selectDeviceTagBIO the selectDeviceTagBIO to set
	 */
	public void setSelectDeviceTagBIO(SelectDeviceTagBIO selectDeviceTagBIO) {
		this.selectDeviceTagBIO = selectDeviceTagBIO;
	}

	public String getAjax() {
		return ajax;
	}
	public void setAjax(String ajax) {
		this.ajax = ajax;
	}
	/**
	 * @return the vendor_id
	 */
	public String getVendor_id() {
		return vendor_id;
	}
	/**
	 * @param vendor_id the vendor_id to set
	 */
	public void setVendor_id(String vendor_id) {
		this.vendor_id = vendor_id;
	}

	/**
	 * @return the device_model_id
	 */
	public String getDevice_model_id() {
		return device_model_id;
	}

	/**
	 * @param device_model_id the device_model_id to set
	 */
	public void setDevice_model_id(String device_model_id) {
		this.device_model_id = device_model_id;
	}

	/**
	 * @return the devicetype_id
	 */
	public String getDevicetype_id() {
		return devicetype_id;
	}

	/**
	 * @param devicetype_id the devicetype_id to set
	 */
	public void setDevicetype_id(String devicetype_id) {
		this.devicetype_id = devicetype_id;
	}

	/**
	 * @return the device_serialnumber
	 */
	public String getDevice_serialnumber() {
		return device_serialnumber;
	}

	/**
	 * @param device_serialnumber the device_serialnumber to set
	 */
	public void setDevice_serialnumber(String device_serialnumber) {
		this.device_serialnumber = device_serialnumber;
	}

	/**
	 * @return the hguser
	 */
	public String getHguser() {
		return hguser;
	}

	/**
	 * @param hguser the hguser to set
	 */
	public void setHguser(String hguser) {
		this.hguser = hguser;
	}

	/**
	 * @return the loopback_ip
	 */
	public String getLoopback_ip() {
		return loopback_ip;
	}

	/**
	 * @param loopback_ip the loopback_ip to set
	 */
	public void setLoopback_ip(String loopback_ip) {
		this.loopback_ip = loopback_ip;
	}

	/**
	 * @return the city_id
	 */
	public String getCity_id() {
		return city_id;
	}

	/**
	 * @param city_id the city_id to set
	 */
	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	/**
	 * @return the select_type
	 */
	public String getSelect_type() {
		return select_type;
	}

	/**
	 * @param select_type the select_type to set
	 */
	public void setSelect_type(String select_type) {
		this.select_type = select_type;
	}

	/**
	 * @return the jsFunctionName
	 */
	public String getJsFunctionName() {
		return jsFunctionName;
	}

	/**
	 * @param jsFunctionName the jsFunctionName to set
	 */
	public void setJsFunctionName(String jsFunctionName) {
		this.jsFunctionName = jsFunctionName;
	}

	/**
	 * @return the selectType
	 */
	public String getSelectType() {
		return selectType;
	}

	/**
	 * @param selectType the selectType to set
	 */
	public void setSelectType(String selectType) {
		this.selectType = selectType;
	}

	/**
	 * @return the cpe_currentstatus
	 */
	public String getOnline_status() {
		return online_status;
	}

	/**
	 * @param cpe_currentstatus the cpe_currentstatus to set
	 */
	public void setOnline_status(String online_status) {
		this.online_status = online_status;
	}

	/**
	 * @return the office_id
	 */
	public String getOffice_id() {
		return office_id;
	}

	/**
	 * @param office_id the office_id to set
	 */
	public void setOffice_id(String office_id) {
		this.office_id = office_id;
	}

	/**
	 * @return the maxFileNum
	 */
	public String getMaxFileNum() {
		return maxFileNum;
	}

	/**
	 * @param maxFileNum the maxFileNum to set
	 */
	public void setMaxFileNum(String maxFileNum) {
		this.maxFileNum = maxFileNum;
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * @return the byImportList
	 */
	public String getByImportList() {
		return byImportList;
	}

	/**
	 * @param byImportList the byImportList to set
	 */
	public void setByImportList(String byImportList) {
		this.byImportList = byImportList;
	}

	/**
	 * @return the byCityChecked
	 */
	public String getByCityChecked() {
		return byCityChecked;
	}

	/**
	 * @param byCityChecked the byCityChecked to set
	 */
	public void setByCityChecked(String byCityChecked) {
		this.byCityChecked = byCityChecked;
	}

	/**
	 * @return the byCityState
	 */
	public String getByCityState() {
		return byCityState;
	}

	/**
	 * @param byCityState the byCityState to set
	 */
	public void setByCityState(String byCityState) {
		this.byCityState = byCityState;
	}

	/**
	 * @return the byDevicenoChecked
	 */
	public String getByDevicenoChecked() {
		return byDevicenoChecked;
	}

	/**
	 * @param byDevicenoChecked the byDevicenoChecked to set
	 */
	public void setByDevicenoChecked(String byDevicenoChecked) {
		this.byDevicenoChecked = byDevicenoChecked;
	}

	/**
	 * @return the byDevicenoState
	 */
	public String getByDevicenoState() {
		return byDevicenoState;
	}

	/**
	 * @param byDevicenoState the byDevicenoState to set
	 */
	public void setByDevicenoState(String byDevicenoState) {
		this.byDevicenoState = byDevicenoState;
	}

	/**
	 * @return the byImportChecked
	 */
	public String getByImportChecked() {
		return byImportChecked;
	}

	/**
	 * @param byImportChecked the byImportChecked to set
	 */
	public void setByImportChecked(String byImportChecked) {
		this.byImportChecked = byImportChecked;
	}

	/**
	 * @return the byImportState
	 */
	public String getByImportState() {
		return byImportState;
	}

	/**
	 * @param byImportState the byImportState to set
	 */
	public void setByImportState(String byImportState) {
		this.byImportState = byImportState;
	}

	/**
	 * @return the byUsernameChecked
	 */
	public String getByUsernameChecked() {
		return byUsernameChecked;
	}

	/**
	 * @param byUsernameChecked the byUsernameChecked to set
	 */
	public void setByUsernameChecked(String byUsernameChecked) {
		this.byUsernameChecked = byUsernameChecked;
	}

	/**
	 * @return the byUsernameState
	 */
	public String getByUsernameState() {
		return byUsernameState;
	}

	/**
	 * @param byUsernameState the byUsernameState to set
	 */
	public void setByUsernameState(String byUsernameState) {
		this.byUsernameState = byUsernameState;
	}

	/**
	 * @return the byCityParam
	 */
	public String getByCityParam() {
		return byCityParam;
	}

	/**
	 * @param byCityParam the byCityParam to set
	 */
	public void setByCityParam(String byCityParam) {
		this.byCityParam = byCityParam;
	}

	/**
	 * @return the byDevicenoParam
	 */
	public String getByDevicenoParam() {
		return byDevicenoParam;
	}

	/**
	 * @param byDevicenoParam the byDevicenoParam to set
	 */
	public void setByDevicenoParam(String byDevicenoParam) {
		this.byDevicenoParam = byDevicenoParam;
	}

	/**
	 * @return the byImportParam
	 */
	public String getByImportParam() {
		return byImportParam;
	}

	/**
	 * @param byImportParam the byImportParam to set
	 */
	public void setByImportParam(String byImportParam) {
		this.byImportParam = byImportParam;
	}

	/**
	 * @return the byUsernameParam
	 */
	public String getByUsernameParam() {
		return byUsernameParam;
	}

	/**
	 * @param byUsernameParam the byUsernameParam to set
	 */
	public void setByUsernameParam(String byUsernameParam) {
		this.byUsernameParam = byUsernameParam;
	}

	/**
	 * @return the byCity
	 */
	public String getByCity() {
		return byCity;
	}

	/**
	 * @param byCity the byCity to set
	 */
	public void setByCity(String byCity) {
		this.byCity = byCity;
	}

	/**
	 * @return the byDeviceno
	 */
	public String getByDeviceno() {
		return byDeviceno;
	}

	/**
	 * @param byDeviceno the byDeviceno to set
	 */
	public void setByDeviceno(String byDeviceno) {
		this.byDeviceno = byDeviceno;
	}

	/**
	 * @return the byImport
	 */
	public String getByImport() {
		return byImport;
	}

	/**
	 * @param byImport the byImport to set
	 */
	public void setByImport(String byImport) {
		this.byImport = byImport;
	}

	/**
	 * @return the byUsername
	 */
	public String getByUsername() {
		return byUsername;
	}

	/**
	 * @param byUsername the byUsername to set
	 */
	public void setByUsername(String byUsername) {
		this.byUsername = byUsername;
	}

	/**
	 * @return the jsFunctionNameBySn
	 */
	public String getJsFunctionNameBySn() {
		return jsFunctionNameBySn;
	}

	/**
	 * @param jsFunctionNameBySn the jsFunctionNameBySn to set
	 */
	public void setJsFunctionNameBySn(String jsFunctionNameBySn) {
		this.jsFunctionNameBySn = jsFunctionNameBySn;
	}
	
	/**
	 * @return the listControl
	 */
	public String getListControl()
	{
		return listControl;
	}
	
	/**
	 * @param listControl the listControl to set
	 */
	public void setListControl(String listControl)
	{
		this.listControl = listControl;
	}

	public String getByVoipTelNum() {
		return byVoipTelNum;
	}

	public void setByVoipTelNum(String byVoipTelNum) {
		this.byVoipTelNum = byVoipTelNum;
	}

	public String getByVOIPState() {
		return byVOIPState;
	}

	public void setByVOIPState(String byVOIPState) {
		this.byVOIPState = byVOIPState;
	}

	public String getByVOIPChecked() {
		return byVOIPChecked;
	}

	public void setByVOIPChecked(String byVOIPChecked) {
		this.byVOIPChecked = byVOIPChecked;
	}

	public String getByVOIPParam() {
		return byVOIPParam;
	}

	public void setByVOIPParam(String byVOIPParam) {
		this.byVOIPParam = byVOIPParam;
	}

	public String getVoipPara() {
		return voipPara;
	}

	public void setVoipPara(String voipPara) {
		this.voipPara = voipPara;
	}	
	
	public String getItvUserName()
	{
		return itvUserName;
	}


	
	public void setItvUserName(String itvUserName)
	{
		this.itvUserName = itvUserName;
	}


	public String getWideNetPara() {
		return wideNetPara;
	}

	public void setWideNetPara(String wideNetPara) {
		this.wideNetPara = wideNetPara;
	}

	public String getByItv() {
		return byItv;
	}

	public void setByItv(String byItv) {
		this.byItv = byItv;
	}

	public String getByItvState() {
		return byItvState;
	}

	public void setByItvState(String byItvState) {
		this.byItvState = byItvState;
	}

	public String getByItvChecked() {
		return byItvChecked;
	}

	public void setByItvChecked(String byItvChecked) {
		this.byItvChecked = byItvChecked;
	}

	public String getByItvParam() {
		return byItvParam;
	}

	public void setByItvParam(String byItvParam) {
		this.byItvParam = byItvParam;
	}

	public String getByWideNet() {
		return byWideNet;
	}

	public void setByWideNet(String byWideNet) {
		this.byWideNet = byWideNet;
	}

	public String getByNetAccountState() {
		return byNetAccountState;
	}

	public void setByNetAccountState(String byNetAccountState) {
		this.byNetAccountState = byNetAccountState;
	}

	public String getByNetAccountChecked() {
		return byNetAccountChecked;
	}

	public void setByNetAccountChecked(String byNetAccountChecked) {
		this.byNetAccountChecked = byNetAccountChecked;
	}

	public String getByWideNetParam() {
		return byWideNetParam;
	}

	public void setByWideNetParam(String byWideNetParam) {
		this.byWideNetParam = byWideNetParam;
	}		
}
