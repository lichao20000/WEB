package com.linkage.module.ids.act;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.ids.bio.HTTPDeviceQueryBIO;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;


/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-10-18
 * @category com.linkage.module.ids.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class HTTPDeviceQueryACT extends ActionSupport implements ServletRequestAware
{

	// 日志记录
		private static Logger logger = LoggerFactory
				.getLogger(HTTPDeviceQueryACT.class);
	
	private HttpServletRequest request;
	HTTPDeviceQueryBIO bio = null;
	private String column1;
	private String column2;
	private String city_id;
	private String device_id;
	private String oui;
	private String wanType;
	private String wan_interface;
	private String pppoeUserName;
	private String userName;
	private String password;
	private String connType="IP_Routed";
	private String testType;
	//测速类型 上行下行
	private String type;
	private String code;
	/**江西针对分公司的测速判断**/
	private String idsShare_queryType;
	private String downlink;
	private String loid;
	private String netUsername;
	
	
	private String ajax;
	
	private Map<String,String> httpMap;
	
	private List<Map<String,String>> testUserList;
	/** 带宽 */
	private String speed = "";
	
	@Override
	public String execute() throws Exception
	{
		// TODO Auto-generated method stub
		return "init";
	}
	/**
	 *      安徽电信Http拨测
			* @return
	 */
	public String executeForAH(){
		testUserList=bio.getTestUserList();
		return "initForAH";
	}
	
	/**
	 * 河北联通测测方法（上行，下行）
	 */
	public String executeForHBLT(){
		return "initForHBLT";
	}
	
	/**
	 * 吉林联通测测方法（上行，下行）
	 */
	public String executeForJLLT(){
		return "initForJLLT";
	}
	public String executeForAHLT(){
		return "initForAHLT";
	}
	
	public String executeForNXLT(){
		return "initForNXLT";
	}

	public String getDefaultdiag(){
		ajax = bio.getDefaultdiag();
		return "ajax";
	}
	
	/**
	 * 测速方法入口 （包括河北联通）
	 * @return
	 */
	public String queryHTTPService(){
		logger.warn("connType is:"+connType);
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String acc_loginname = curUser.getUser().getAccount();
		if(LipossGlobals.inArea(Global.SDLT)){
			httpMap = bio.queryHTTPService4SD(device_id,testType);
			code = httpMap.get("code");
			return "listForSDLT";
		}
		logger.warn("LipossGlobals.inArea('jl_lt')="+LipossGlobals.inArea(Global.JLLT));
		if(LipossGlobals.inArea(Global.AHLT)){
			httpMap = bio.queryHTTPServiceAHLT(device_id,wanType, column1, column2,pppoeUserName, userName,password,code);
		}		
		if(LipossGlobals.inArea(Global.JLLT) || LipossGlobals.inArea(Global.JXLT)
				|| LipossGlobals.inArea(Global.ZJLT) ){
			httpMap = bio.queryHTTPServiceJLLT(device_id,wanType, column1, column2,pppoeUserName, userName,password,code);
		}
		else if(LipossGlobals.inArea(Global.SXLT)){
			httpMap = bio.queryHTTPServiceSXLT(device_id,wanType, column1, column2,pppoeUserName, userName,password,code);
		}
		// 山东电信
		else if (LipossGlobals.inArea(Global.SDDX)) {
			httpMap = bio.queryHTTPServiceSDDX(idsShare_queryType, netUsername, column1);
			code = httpMap.get("code");
			return "listForSDDX";
		}
		else if(LipossGlobals.inArea(Global.NXLT)){
			httpMap = bio.queryHTTPServiceNXLT(device_id,wanType, column1, column2,pppoeUserName, userName,password,code);
		}
		else{
			httpMap = bio.queryHTTPService(city_id, oui, device_id,wanType,wan_interface, column1, column2,userName,password,
					connType,speed,pppoeUserName,idsShare_queryType,downlink,loid,netUsername,acc_loginname);
		}
		
		code = httpMap.get("code");
		if(LipossGlobals.inArea(Global.AHDX)){
			return "listForAH";
		}
		if(LipossGlobals.inArea(Global.JXDX)){
			return "listForJX";
		}
		if(LipossGlobals.inArea(Global.HBLT)){
			return "listForHBLT";
		}
		if(LipossGlobals.inArea(Global.AHLT)){
			return "listForAHLT";
		}
		if(LipossGlobals.inArea(Global.JLLT) || LipossGlobals.inArea(Global.SXLT)
				|| LipossGlobals.inArea(Global.JXLT) || LipossGlobals.inArea(Global.ZJLT) ){
			return "listForJLLT";
		}
		if(LipossGlobals.inArea(Global.NXLT)){
			return "listForNXLT";
		}
		return "list";
	}
	
	/**
	 * 河北联通 新测速方法
	 */
	public String queryHTTPServiceHBLT(){
		logger.warn("connType is:"+connType);
		httpMap = bio.queryHTTPServiceHBLT(city_id, oui, device_id,wanType,wan_interface, column1, column2,userName,password,connType,speed,pppoeUserName,type);
		code = httpMap.get("code");
		return "listForHBLT";
	}
	
	
	
	/**
	 * HTTP上行测速
	 * @Description TODO
	 * @author guxl3
	 * @date 2019年6月6日
	 * @return  
	 * @throws
	 */
	public String UploadSpeedByHTTP(){
		return "uploadSpeedByHTTP";
	}
	
	/**
	 * 上行测速（目前宁夏在用）
	 * @Description TODO
	 * @author guxl3
	 * @date 2019年6月10日
	 * @return  
	 * @throws
	 */
	public String queryUploadSpeedService(){
		httpMap = bio.queryUploadSpeedService(city_id, oui, device_id,wanType,wan_interface, column1, column2,userName,password,connType,speed,pppoeUserName);
		code = httpMap.get("code");
		return "uploadHTTPQueryInfoList";
	}
	
	@Override
	public void setServletRequest(HttpServletRequest arg0)
	{
		this.request = arg0;
	}

	
	public HTTPDeviceQueryBIO getBio()
	{
		return bio;
	}

	
	public void setBio(HTTPDeviceQueryBIO bio)
	{
		this.bio = bio;
	}

	
	public String getColumn1()
	{
		return column1;
	}

	
	public void setColumn1(String column1)
	{
		this.column1 = column1;
	}

	
	public String getColumn2()
	{
		return column2;
	}

	
	public void setColumn2(String column2)
	{
		this.column2 = column2;
	}

	
	public String getCity_id()
	{
		return city_id;
	}

	
	public void setCity_id(String city_id)
	{
		this.city_id = city_id;
	}

	
	public String getDevice_id()
	{
		return device_id;
	}

	
	public void setDevice_id(String device_id)
	{
		this.device_id = device_id;
	}

	
	public String getOui()
	{
		return oui;
	}

	
	public void setOui(String oui)
	{
		this.oui = oui;
	}

	
	public String getWanType()
	{
		return wanType;
	}

	
	public void setWanType(String wanType)
	{
		this.wanType = wanType;
	}

	
	public String getWan_interface()
	{
		return wan_interface;
	}

	
	public void setWan_interface(String wan_interface)
	{
		this.wan_interface = wan_interface;
	}

	
	public String getAjax()
	{
		return ajax;
	}

	
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	
	public Map<String, String> getHttpMap()
	{
		return httpMap;
	}

	
	public void setHttpMap(Map<String, String> httpMap)
	{
		this.httpMap = httpMap;
	}

	
	public String getCode()
	{
		return code;
	}

	
	public void setCode(String code)
	{
		this.code = code;
	}

	
	public String getUserName()
	{
		return userName;
	}

	
	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	
	public String getPassword()
	{
		return password;
	}

	
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	public List<Map<String, String>> getTestUserList()
	{
		return testUserList;
	}
	
	public void setTestUserList(List<Map<String, String>> testUserList)
	{
		this.testUserList = testUserList;
	}
	public String getConnType()
	{
		return
				connType;
	}
	public void setConnType(String connType)
	{
		this.connType =
				connType;
	}
	
	public String getSpeed()
	{
		return speed;
	}
	
	public void setSpeed(String speed)
	{
		this.speed = speed;
	}
	
	public String getPppoeUserName()
	{
		return pppoeUserName;
	}
	
	public void setPppoeUserName(String pppoeUserName)
	{
		this.pppoeUserName = pppoeUserName;
	}
	
	public String getTestType()
	{
		return testType;
	}
	
	public void setTestType(String testType)
	{
		this.testType = testType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIdsShare_queryType()
	{
		return idsShare_queryType;
	}
	public void setIdsShare_queryType(String idsShare_queryType)
	{
		this.idsShare_queryType = idsShare_queryType;
	}
	public String getDownlink()
	{
		return downlink;
	}
	public void setDownlink(String downlink)
	{
		this.downlink = downlink;
	}
	

	public String getLoid()
	{
		return loid;
	}
	
	public void setLoid(String loid)
	{
		this.loid = loid;
	}
	
	public String getNetUsername()
	{
		return netUsername;
	}
	
	public void setNetUsername(String netUsername)
	{
		this.netUsername = netUsername;
	}
	
}
