package com.linkage.module.gtms.stb.resource.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.resource.serv.UserInstReleaseBIO;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.init.obj.CpeFaultcodeOBJ;
import com.opensymphony.xwork2.ActionSupport;

public class StbInstACT extends ActionSupport implements SessionAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(StbInstACT.class);
	private List userList;
	private List<HashMap<String,String>> configList;
	private String message;
	private String ajax;
	private Map session;
	private String servAccount;
	private String customer_id;
	private String userCityId;
	private String deviceId;
	private String deviceSN;
	private String oui;
	private String nameListHtml;
	private String instAreaName;
	private String id = "";
	
	private ArrayList<HashMap<String, String>> doServStatusList;
	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	private UserInstReleaseBIO bio;
	public UserInstReleaseBIO getBio() {
		return bio;
	}

	public void setBio(UserInstReleaseBIO bio) {
		this.bio = bio;
	}

	/**
	 * 现场安装页面初始化
	 * 
	 * @author wangsenbo
	 * @date Jul 22, 2010
	 * @param
	 * @return String
	 */
	public String init() {
		this.setNameListHtml();
		return "init";
	}
	
	/**
	 * 查询用户信息 绑定
	 * 
	 * @author wangsenbo
	 * @date Nov 13, 2009
	 * @return String
	 */
	public String getInstUserInfo()
	{
		UserRes curUser = (UserRes) session.get("curUser");
		String cityId = curUser.getCityId();
		userList = bio.queryUser(cityId, servAccount);
		return "userInst";
	}
	
	/**
	 * 设备绑定
	 * 
	 * @author wangsenbo
	 * @date Nov 13, 2009
	 * @return String
	 */
	public String execute()
	{
		logger.debug("execute()");
		UserRes curUser = (UserRes) session.get("curUser");
		if (true == StringUtil.IsEmpty(userCityId))
		{
			userCityId = curUser.getCityId();
		}
		message = bio.stbInst(customer_id, servAccount, deviceId, curUser
				.getUser().getAccount(), 1);
		logger.info(message);
		ajax = message;
		return "ajax";
	}
	
	
	/**
	 * 解绑
	 * 
	 * @author wangsenbo
	 * @date Nov 13, 2009
	 * @return String
	 */
	public String release()
	{
		logger.debug("release()");
		UserRes curUser = (UserRes) session.get("curUser");
		if (true == StringUtil.IsEmpty(userCityId))
		{
			userCityId = curUser.getCityId();
		}
		message = bio.stbRelease(customer_id, deviceId,curUser.getUser().getAccount(), 1);
		logger.info(message);
		ajax = message;
		return "ajax";
	}
	
	/**
	 * 手工业务下发
	 * 
	 * @author liyl10
	 * @date Mar 23, 2016
	 * @return String
	 */
	public String callPreProcess(){
		logger.debug("callPreProcess()");
		ajax = bio.callPreProcess(customer_id, deviceId, oui, deviceSN);
		return "ajax";
	}
	
	/**
	 * 业务下发后参数展示
	 * 
	 * @author liyl10
	 * @date Mar 23, 2016
	 * @return String
	 */
	public String serviceDoneList(){
		logger.warn("serviceDone[{}]", deviceId);
		userList = bio.serviceDoneList(deviceId);
		return "serviceDoneList";
	}

	/**
	 * 配置信息
	 * 
	 * @author liyl10
	 * @date Mar 23, 2016
	 * @return String
	 */
	public String getConfigInfo(){
		logger.warn("getConfigInfo[{},{}]", deviceId, deviceSN);
		configList = bio.getConfigInfo(deviceId);
		return "configList";
	}
	
	/**
	 * bss业务配置信息
	 * 
	 */
	public String getConfigInfoStb(){
		logger.warn("getConfigInfoStb[{},{}]", deviceId, deviceSN);
		configList = bio.getConfigInfo(deviceId);
		return "configListStb";
	}
	
	public String getConfigDetail(){
		logger.warn("getConfigInfo[{},{}]", deviceId, deviceSN);
		configList = bio.getConfigInfo(deviceId);
		
		logger.warn("configList.size="+configList.size());
		//处理下发节点明细
		String fault_path = configList.get(0).get("fault_path");
		logger.warn("fault_path="+fault_path);
		doServStatusList = new ArrayList<HashMap<String, String>>();
		
		if(!StringUtil.IsEmpty(fault_path)){
			String[] values = fault_path.split("\\|",-1)[0].split(",",-1);
			String stat = fault_path.split("\\|",-1)[1];
			String faultPath = fault_path.split("\\|",-1)[2];
			for(int i=0;i<values.length;i++){
				HashMap<String, String> doServStatus = new HashMap<String, String>();
				String name = !StringUtil.IsEmpty(values[i])&&values[i].contains("=")?values[i].split("=")[0]:"";
				String value =  !StringUtil.IsEmpty(values[i])&&values[i].contains("=")?values[i].split("=")[1]:"";
				int result = 0;//1是成功
				doServStatus.put("name", name);
				doServStatus.put("value", value);
				doServStatus.put("type", "设置参数");
				if(!StringUtil.IsEmpty(faultPath) && faultPath.equals(name)) result = StringUtil.getIntegerValue(stat);
				else result = 1;
				//if(!StringUtil.IsEmpty(stat)) result = StringUtil.getIntegerValue(stat);
				doServStatus.put("result", Global.G_Fault_Map.get(result).getFaultDesc());
				if("等待执行".equals(configList.get(0).get("status"))){
					doServStatus.put("result", "等待执行");
				}
				doServStatusList.add(doServStatus);
				
				for(Map.Entry<String, String> map : doServStatus.entrySet()){
					logger.warn(map.getKey()+"="+map.getValue());
				}
			}
		}
		
		
		
		return "configDetailStb";
	}
	
	
	public String getConfigLogDetail(){
		logger.warn("getConfigInfo[{},{}]", deviceId, deviceSN);
		configList = bio.getConfigInfoLogSXLT(deviceId,id);
		
		logger.warn("configList.size="+configList.size());
		//处理下发节点明细
		String fault_path = configList.get(0).get("fault_path");
		logger.warn("fault_path="+fault_path);
		doServStatusList = new ArrayList<HashMap<String, String>>();
		
		if(!StringUtil.IsEmpty(fault_path)){
			String[] values = fault_path.split("\\|",-1)[0].split(",",-1);
			String stat = fault_path.split("\\|",-1)[1];
			String faultPath = fault_path.split("\\|",-1)[2];
			for(int i=0;i<values.length;i++){
				HashMap<String, String> doServStatus = new HashMap<String, String>();
				String name = !StringUtil.IsEmpty(values[i])&&values[i].contains("=")?values[i].split("=")[0]:"";
				String value =  !StringUtil.IsEmpty(values[i])&&values[i].contains("=")?values[i].split("=")[1]:"";
				int result = 0;//1是成功
				doServStatus.put("name", name);
				doServStatus.put("value", value);
				doServStatus.put("type", "设置参数");
				if(!StringUtil.IsEmpty(faultPath) && faultPath.equals(name)) result = StringUtil.getIntegerValue(stat);
				else result = 1;
				//if(!StringUtil.IsEmpty(stat)) result = StringUtil.getIntegerValue(stat);
				doServStatus.put("result", Global.G_Fault_Map.get(result).getFaultDesc());
				if("等待执行".equals(configList.get(0).get("status"))){
					doServStatus.put("result", "等待执行");
				}
				doServStatusList.add(doServStatus);
				
				for(Map.Entry<String, String> map : doServStatus.entrySet()){
					logger.warn(map.getKey()+"="+map.getValue());
				}
			}
		}
		
		
		
		return "configDetailStb";
	}
	
	public static void main(String[] args)
	{
		String a = "Device.X_CU_STB.AuthServiceInfo.UserID=sdasdas,Device.X_CU_STB.AuthServiceInfo.Activate=1,Device.X_CU_STB.AuthServiceInfo.UserIDPassword=&amp;*(^&amp;*^|-1|";
				System.out.println(a.split("\\|", -1)[2]);
	}

	String resultId;
	String solutionData;
	public String getResultId() {
		return resultId;
	}

	public void setResultId(String resultId) {
		this.resultId = resultId;
	}

	public String getSolutionData() {
		return solutionData;
	}

	public void setSolutionData(String solutionData) {
		this.solutionData = solutionData;
	}

	public String getSolution() {
		
		CpeFaultcodeOBJ obj = Global.G_Fault_Map.get(resultId);
		if (null != obj) {
			String solution = obj.getSolutions();
			if (StringUtil.IsEmpty(solution)) {
				solutionData = "无处理意见";
			}
			else {
				solutionData = solution;
			}
		}
		else
		{
			solutionData = "无处理意见";
		}
		return "solutioninfo";
	}
	List<HashMap<String, String>> configLogInfoList = new ArrayList<HashMap<String,String>>();
	
	public List<HashMap<String, String>> getConfigLogInfoList() {
		return configLogInfoList;
	}

	public void setConfigLogInfoList(List<HashMap<String, String>> configLogInfoList) {
		this.configLogInfoList = configLogInfoList;
	}

	public String getConfigLogInfo() {
		instAreaName = Global.instAreaShortName;
		
		logger.warn("getConfigInfo[{},{}]", deviceId, deviceSN);
		if(Global.SXLT.equals(Global.instAreaShortName)){
			configLogInfoList = bio.getConfigInfoLogSXLT(deviceId, null);
		}
		else{
			configLogInfoList = bio.getConfigInfo(deviceId);
		}
		
		
		return "configLogInfo";
	}

	ArrayList<HashMap<String, String>> bssSheetList = new ArrayList<HashMap<String,String>>();
	
	public ArrayList<HashMap<String, String>> getBssSheetList() {
		return bssSheetList;
	}

	public void setBssSheetList(ArrayList<HashMap<String, String>> bssSheetList) {
		this.bssSheetList = bssSheetList;
	}

	/**
	 * bss工单
	 * 
	 */
	public String getBssSheet(){
		logger.warn("getBssSheet[{}]", customer_id);
		bssSheetList = bio.getBssSheet(customer_id);
		return "bssSheetStb";
	}

	/**
	 * @return the userList
	 */
	public List getUserList() {
		return userList;
	}

	/**
	 * @param userList
	 *            the userList to set
	 */
	public void setUserList(List userList) {
		this.userList = userList;
	}

	/**
	 * @return the bio
	 */
	public UserInstReleaseBIO getUserInstReleaseBio() {
		return bio;
	}

	/**
	 * @param bio
	 *            the bio to set
	 */
	public void setUserInstReleaseBio(UserInstReleaseBIO bio) {
		this.bio = bio;
	}

	/**
	 * @return the session
	 */
	public Map getSession() {
		return session;
	}

	/**
	 * @param session
	 *            the session to set
	 */
	public void setSession(Map session) {
		this.session = session;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the username
	 */
	public String getServAccount() {
		return servAccount;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setServAccount(String servAccount) {
		this.servAccount = servAccount;
	}


	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	/**
	 * @return the userCityId
	 */
	public String getUserCityId() {
		return userCityId;
	}

	/**
	 * @param userCityId
	 *            the userCityId to set
	 */
	public void setUserCityId(String userCityId) {
		this.userCityId = userCityId;
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * @param deviceId
	 *            the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * @param nameList
	 *            the nameList to set
	 */
	public void setNameListHtml() {
		StringBuffer sb = new StringBuffer();
		sb.append("<SELECT name='nameType' class='bk' style='width:260px'>");
		sb.append("<option value='1' selected>业务帐号</option>");
		sb.append("</SELECT>");
		nameListHtml = sb.toString();
	}

	/**
	 * @return the nameListHtml
	 */
	public String getNameListHtml() {
		return nameListHtml;
	}

	/**
	 * @param nameListHtml
	 *            the nameListHtml to set
	 */
	public void setNameListHtml(String nameListHtml) {
		this.nameListHtml = nameListHtml;
	}
	

	public List<HashMap<String, String>> getConfigList()
	{
		return configList;
	}
	
	public void setConfigList(List<HashMap<String, String>> configList)
	{
		this.configList = configList;
	}

	public String getDeviceSN()
	{
		return deviceSN;
	}

	public void setDeviceSN(String deviceSN)
	{
		this.deviceSN = deviceSN;
	}

	public String getOui()
	{
		return oui;
	}
	
	public void setOui(String oui)
	{
		this.oui = oui;
	}

	
	public ArrayList<HashMap<String, String>> getDoServStatusList()
	{
		return doServStatusList;
	}

	
	public void setDoServStatusList(ArrayList<HashMap<String, String>> doServStatusList)
	{
		this.doServStatusList = doServStatusList;
	}

	
	public String getInstAreaName()
	{
		return instAreaName;
	}

	
	public void setInstAreaName(String instAreaName)
	{
		this.instAreaName = instAreaName;
	}

	
	public String getId()
	{
		return id;
	}

	
	public void setId(String id)
	{
		this.id = id;
	}

	
}
