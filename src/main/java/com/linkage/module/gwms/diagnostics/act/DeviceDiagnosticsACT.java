package com.linkage.module.gwms.diagnostics.act;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.ailk.tr069.devrpc.obj.rpc.DevRpcCmdOBJ;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.corba.DevRPCManager;
import com.linkage.litms.system.UserRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.diagnostics.bio.DeviceDiagnosticBIO;
import com.linkage.module.gwms.diagnostics.bio.interf.I_DeviceInfoBIO;
import com.linkage.module.gwms.diagnostics.obj.DiagResult;
import com.linkage.module.gwms.diagnostics.obj.PONInfoOBJ;
import com.linkage.module.gwms.obj.gw.PingObject;
import com.linkage.module.gwms.util.StringUtil;

import ACS.DevRpc;
import ACS.Rpc;
import action.splitpage.splitPageAction;

/**
 * @author Jason(3412)
 * @date 2009-6-15
 */
public class DeviceDiagnosticsACT extends splitPageAction implements SessionAware,ServletRequestAware,ServletResponseAware{

	private static Logger logger = LoggerFactory
	.getLogger(DeviceDiagnosticsACT.class);
	
	private static final long serialVersionUID = 2425363349057904963L;
	
	private Map session;
	/**
	 * 设备ID
	 */
	private String deviceId;
	
	/** 终端类型 */
	private String gw_type = null;
	/**
	 * 用户ID
	 */
	private String userId;
	/**
	 * 诊断类型:有线无法上网，无线无法上网，上网速度慢，异常掉线多，IPTV无法使用
	 */
	private String diagType;
	/**
	 * 订单类型
	 */
	private String orderType;
	
	//ping地址类型：dnsAddress, special Address, specialDomain  
	private String pingAddrType;
	
	/**域名的ping操作*/
	private String pingAddr;
	private String packageSize;
	private String pingTimes;
	private String timeOut;
	
	/**DNS地址的ping操作*/
	private String pingAddrDns;
	private String packageSizeDns;
	private String pingTimesDns;
	private String timeOutDns;
	
	/**IP地址的ping操作*/
	private String pingAddrSpecial;
	private String packageSizeSpecial;
	private String pingTimesSpecial;
	private String timeOutSpecial;
	
	/**IP地址的ping操作*/
	private String pingAddrLan;
	private String packageSizeLan;
	private String pingTimesLan;
	private String timeOutLan;
	private String pingInterLan;
	
	//业务类型
	private String servTypeId;
	//返回字符串值
	private String ajax;
	//采集类型
	private String gatherType = "1";	
//	//1:诊断通过,继续诊断; -1:诊断到异常; -2诊断失败
//	private String pass;
//	//故障描述
//	private String faultDesc;
//	//诊断建议
//	private String suggest;
//	//诊断失败描述
//	private String failture;
	//返回结果
	private List<Map<String, String>> resultList;
	//action执行结果
	public static String AJAX = "ajax";
	//BIO
	private DeviceDiagnosticBIO diagBio;
	//1表示有线, 2表示无线 
	private int wireType;
	
	//上行方式：1:ADSL 2:Ethernet(LAN) 3:EPON 4:POTS -1:未知
	private String accessType;
	
	// pon上行类别，分为Epon和Gpon
	private String pon_type;
	
	//pon信息
	private PONInfoOBJ[] ponInfoOBJArr = null;
	
	/**
	 * 注入
	 */
	I_DeviceInfoBIO deviceInfoBIO;
	
	private DiagResult diagResult;

	private HttpServletRequest request;
	
	private List<Map> checkDetailList = null;
	
	private HttpServletResponse response;	
	
	/**检测查询*/
	private String  account;
	private String  device_serialnumber;
	private String  vendor;
	private String  device_model;
	private String  hardwareversion;
	private String  softwareversion;
	private String  startTime;
	private String  endTime;
	
	
	/** action method fields */

	/**
	 * 初始化页面
	 */
	public String execute(){
		logger.debug("execute()");
		userId = diagBio.getUserId(deviceId);
		//初始化ping操作各个参数
		logger.warn("======gw_type====="+gw_type+"============");
		Map pingMap = diagBio.initPingParam();
		pingAddrDns = StringUtil.getStringValue(pingMap.get("dnsAddress"));
		pingAddrSpecial = StringUtil.getStringValue(pingMap.get("specialAddress"));
		pingAddr = StringUtil.getStringValue(pingMap.get("specialDomain"));
		
		packageSizeSpecial = StringUtil.getStringValue(pingMap.get("packgeSize"));
		timeOutSpecial = StringUtil.getStringValue(pingMap.get("timeOut"));
		pingTimesSpecial = StringUtil.getStringValue(pingMap.get("pingTimes"));
		
		packageSize = StringUtil.getStringValue(pingMap.get("packgeSize"));
		timeOut = StringUtil.getStringValue(pingMap.get("timeOut"));
		pingTimes = StringUtil.getStringValue(pingMap.get("pingTimes"));
		
		packageSizeDns = StringUtil.getStringValue(pingMap.get("packgeSize"));
		timeOutDns = StringUtil.getStringValue(pingMap.get("timeOut"));
		pingTimesDns = StringUtil.getStringValue(pingMap.get("pingTimes"));
		
		packageSizeLan = StringUtil.getStringValue(pingMap.get("packgeSize"));
		timeOutLan = StringUtil.getStringValue(pingMap.get("timeOut"));
		pingTimesLan = StringUtil.getStringValue(pingMap.get("pingTimes"));
		
		//默认有线无法上网
		String act = "wiredNetDiag";
		if("wiredNetDiag".equals(diagType)){
			//有线无法上网
		}else if("wirelessNetDiag".equals(diagType)){
			//无线无法上网
			act = "wirelessNetDiag";
		}else if("netSlowDiag".equals(diagType)){
			//上网速度慢
			act = "netSlowDiag";
		}else if("netOfflineDiag".equals(diagType)){
			//异常掉线多
			act = "netOfflineDiag";
		}else if("diagTools".equals(diagType)){
			//诊断工具
			act = "diagTools";
		}else if("iptvDiag".equals(diagType)){
			//iptv无法上网
			act = "iptvDiag";
		}
		return act;
	}
	
	
	/**
	 * 链路信息检查
	 * 
	 * modify by zhangchy 2011-11-02 
	 * 
	 * 原来没有区分不同的上行方式展示结果 
	 * 现在要求区分不同上行方式采集数据的显示结果
	 * 
	 */
	public String wireInfoCheck() {
		logger.debug("wireInfoCheck()");
//------ add by zhangchy 2011-11-02 --------begin-------
		accessType = String.valueOf(diagBio.getAccessType(deviceId));
		
		if("1".equals(accessType)){//ADSL
			//链路
			diagResult = diagBio.getWireInfo(deviceId, gw_type);
		}else if("2".equals(accessType)){//LAN
			diagResult = diagBio.getWireInfo(deviceId, gw_type);
		}else if("3".equals(accessType)){//EPON
			ponInfoOBJArr = diagBio.queryPONInfo(deviceId,userId);
			pon_type = "EPON";
		}else if("4".equals(accessType)){//GPON
			ponInfoOBJArr = diagBio.queryPONInfo(deviceId,userId);
			pon_type = "GPON";
		}else{
			//链路
			diagResult = diagBio.getWireInfo(deviceId, gw_type);
		}
//------ add by zhangchy 2011-11-02 --------end-------

		// 注释 by zhangchy 2011-11-02 应要求  展示根据不同上行方式采集数据的结果 --begin----
//		diagResult = diagBio.getWireInfo(deviceId);  
		// 注释 by zhangchy 2011-11-02 应要求  展示根据不同上行方式采集数据的结果 --end------
		
//		pass = String.valueOf(diagResult.getPass());
//		if(diagResult.isSuccess()){
//			//诊断成功
//			resultList = diagResult.getRList();
//			if(-1 == diagResult.getPass()){
//				faultDesc = diagResult.getFaultDesc();
//				suggest = diagResult.getSuggest();
//			}
//		}else{
//			failture = diagResult.getSuggest();
//		}
		return "wireInfo";
	}

	/**
	 * 业务信息检查
	 */
	public String servParamCheck() {

		if("11".equals(servTypeId)){
			diagResult = diagBio.iptvServParamCheck(deviceId, userId, servTypeId, gw_type);
//			pass = String.valueOf(diagResult.getPass());
//			if(diagResult.isSuccess()){
//				//诊断成功
//				resultList = diagResult.getRList();
//			}
//			suggest = diagResult.getSuggest();
			return "iptvServInfo";
		}else{
			diagResult = diagBio.servParamCheck(deviceId, userId, servTypeId, gw_type);
//			pass = String.valueOf(diagResult.getPass());
//			if(diagResult.isSuccess()){
//				//诊断成功
//				resultList = diagResult.getRList();
//			}
//			suggest = diagResult.getSuggest();
			return "servInfo";
		}
	}
	
	/**
	 * 拨号错误码检查 
	 */
	public String connErrorCheck(){
		diagResult = diagBio.connErrorCheck(deviceId, userId, servTypeId, gw_type);
//		pass = String.valueOf(diagResult.getPass());
//		if(diagResult.isSuccess()){
//			//诊断成功
//			resultList = diagResult.getRList();
//		}
//		suggest = diagResult.getSuggest();
		return "connErr";
	}
	
	/**
	 * ping检查
	 */
	public String pingCheck(){
		if("lanPing".equals(pingAddrType)){
			//diagResult = diagBio.lanPing(deviceId);
			PingObject pingObj = new PingObject();
			pingObj.setPingAddress(pingAddrLan);
			pingObj.setNumOfRepetitions(StringUtil.getIntegerValue(pingTimesLan));
			pingObj.setPackageSize(StringUtil.getIntegerValue(packageSizeLan));
			pingObj.setTimeOut(StringUtil.getIntegerValue(timeOutLan));
			diagResult = diagBio.lanPing(deviceId, pingObj, gw_type);
		}else{
			//diagBio.getWanDevice(deviceId);
			PingObject pingObj = new PingObject();
			if("dnsAddress".equals(pingAddrType)){
				pingObj.setPingAddress(pingAddrDns);
				pingObj.setNumOfRepetitions(StringUtil.getIntegerValue(pingTimesDns));
				pingObj.setPackageSize(StringUtil.getIntegerValue(packageSizeDns));
				pingObj.setTimeOut(StringUtil.getIntegerValue(timeOutDns));
				diagResult = diagBio.pingCheck(deviceId,userId,servTypeId, pingAddrType, pingObj, gw_type);
			}else if("specialDomain".equals(pingAddrType)){
				pingObj.setPingAddress(pingAddr);
				pingObj.setNumOfRepetitions(StringUtil.getIntegerValue(pingTimes));
				pingObj.setPackageSize(StringUtil.getIntegerValue(packageSize));
				pingObj.setTimeOut(StringUtil.getIntegerValue(timeOut));
				diagResult = diagBio.pingCheck(deviceId,userId,servTypeId, pingAddrType, pingObj, gw_type);
			}else{
				//"specialDomain".equals(pingAddrType)
				pingObj.setPingAddress(pingAddr);
				pingObj.setNumOfRepetitions(StringUtil.getIntegerValue(pingTimesSpecial));
				pingObj.setPackageSize(StringUtil.getIntegerValue(packageSizeSpecial));
				pingObj.setTimeOut(StringUtil.getIntegerValue(timeOutSpecial));
				diagResult = diagBio.pingCheck(deviceId,userId,servTypeId, pingAddrType, pingObj, gw_type);
			}
		}
//		pass = String.valueOf(diagResult.getPass());
//		if(diagResult.isSuccess()){
//			//诊断成功
//			resultList = diagResult.getRList();
//		}
//		suggest = diagResult.getSuggest();
		return "pingInfo";
	}

	/**
	 * PC连接检查
	 */
	public String lanHostCheck(){
		if(2 == wireType){
			diagResult = diagBio.wlanHostCheck(deviceId,gw_type);
//			pass = String.valueOf(diagResult.getPass());
//			if(diagResult.isSuccess()){
//				//诊断成功
//				resultList = diagResult.getRList();
//			}
//			suggest = diagResult.getSuggest();
			return "wlanInfo";
		}else{
			diagResult = diagBio.lanHostCheck(deviceId, gw_type);
//			pass = String.valueOf(diagResult.getPass());
//			if(diagResult.isSuccess()){
//				//诊断成功
//				resultList = diagResult.getRList();
//			}
//			suggest = diagResult.getSuggest();
			return "lanInfo";
		}		
		//	return "hostInfo";
	}
	
	/**
	 * DHCP检查
	 */
	public String dhcpCheck(){
		diagResult = diagBio.dhcpCheck(deviceId, wireType, gw_type);
//		pass = String.valueOf(diagResult.getPass());
//		if(diagResult.isSuccess()){
//			//诊断成功
//			resultList = diagResult.getRList();
//		}
//		suggest = diagResult.getSuggest();
		return "dhcpInfo";
	}
	
	/**
	 * DNS检查
	 */
	public String dnsCheck(){
		diagResult = diagBio.pcDNSCheck(deviceId, userId, servTypeId,gw_type);
//		pass = String.valueOf(diagResult.getPass());
//		if(diagResult.isSuccess()){
//			//诊断成功
//			resultList = diagResult.getRList();
//		}
//		suggest = diagResult.getSuggest();
		return "dnsInfo";
	}

	public String iptvConnCheck(){
		diagResult = diagBio.iptvConnCheck(deviceId,gw_type);
		return "lanInfo";
		//return AJAX;
	}
	
	
	public String autoCheck() {
		
		UserRes curUser = (UserRes) session.get("curUser");
		String account = curUser.getUser().getAccount();
		String ids= request.getParameter("ids");
		String device_serialnumber = request.getParameter("device_serialnumber");
		String oui = request.getParameter("oui");
		String deviceId = request.getParameter("deviceId");
		String loid = request.getParameter("loid");
		String user_id = request.getParameter("user_id");//当前绑定设备的用户id
		String gw_type = request.getParameter("gw_type");
		String city_id = request.getParameter("city_id");
		
		List checkIsBandList = diagBio.checkIsBand(deviceId);
		if (checkIsBandList==null ||checkIsBandList.size()<=0) {
			this.ajax="设备未绑定用户,请绑定后检测";
			return "ajax";
		}
		
		int int_flag = getConnectionFlag(deviceId);
		if(int_flag != 1){
			this.ajax="设备不在线,请接入设备";
			return "ajax";
		}
		
		checkThread thread=new checkThread(gw_type,account, ids, device_serialnumber, deviceId, loid, user_id,oui,city_id);
		thread.start();
		this.ajax="正在检测中，请稍后在查询菜单查询结果";
		
		return "ajax";
	}
	
	public String getCheckList() {
		
		if (!StringUtil.IsEmpty(startTime)) {
			startTime=startTime+" 00:00:00";
		}
		if (!StringUtil.IsEmpty(endTime)) {
			endTime=endTime+" 23:59:59";
		}
		
		checkDetailList = diagBio.getCheckList(account,
				device_serialnumber, vendor, device_model, hardwareversion,softwareversion,startTime,endTime,
				curPage_splitPage, num_splitPage);

		maxPage_splitPage = diagBio.getCheckListCount(account,
				device_serialnumber, vendor, device_model, hardwareversion,softwareversion,startTime,endTime,
				curPage_splitPage, num_splitPage);
		return "queryCheckList";
	}
	
	public void downLoad(){
		
		String filePath =request.getParameter("filePath");
		String device_serialnumber =request.getParameter("device_serialnumber");
		Date dNow = new Date( );
	    SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddhhmmss");
	    String format = ft.format(dNow);
		String name =device_serialnumber+"_"+format+".xlsx";
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
		try {
			File f = new File(filePath);
			response.setContentType("application/x-excel");
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ name);
			response.setHeader("Content-Length", String.valueOf(f.length()));
			in = new BufferedInputStream(new FileInputStream(f));
			out = new BufferedOutputStream(response.getOutputStream());
			byte[] data = new byte[1024];
			int len = 0;
			while (-1 != (len = in.read(data, 0, data.length))) {
				out.write(data, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public int getConnectionFlag(String device_id){
		logger.debug("getConnectionFlag({})", device_id);
		
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(device_id);
		
		DevRpc[] devRPCArr = new DevRpc[1];
		devRPCArr[0] = new DevRpc();
		devRPCArr[0].devId = device_id;
		Rpc[] rpcArr = new Rpc[1];
		rpcArr[0] = new Rpc();
		rpcArr[0].rpcId = "1";
		rpcArr[0].rpcName = "";
		rpcArr[0].rpcValue = "";
		devRPCArr[0].rpcArr = rpcArr;
		// corba
		List<DevRpcCmdOBJ> devRPCRep = null;
		DevRPCManager devRPCManager = new DevRPCManager(gw_type);
		devRPCRep = devRPCManager.execRPC(devRPCArr, Global.RpcTest_Type);
		int flag = 0;
		if (devRPCRep == null || devRPCRep.size() == 0)
		{
			logger.warn("[{}]List<DevRpcCmdOBJ>返回为空！", device_id);
		}
		else if (devRPCRep.get(0) == null)
		{
			logger.warn("[{}]DevRpcCmdOBJ返回为空！", device_id);
		}
		else
		{
			flag = devRPCRep.get(0).getStat();
		}
		return flag;
	}
	
	/**
	 * getter(), setter() method fields
	 */

	public String getDiagType() {
		return diagType;
	}

	public void setDiagType(String diagType) {
		this.diagType = diagType;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public void setDiagBio(DeviceDiagnosticBIO diagBio) {
		this.diagBio = diagBio;
	}

	public String getOrderType() {
		return orderType;
	}


	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getServTypeId() {
		return servTypeId;
	}

	public void setServTypeId(String servTypeId) {
		this.servTypeId = servTypeId;
	}

	public String getPingAddrType() {
		return pingAddrType;
	}

	public void setPingAddrType(String pingAddrType) {
		this.pingAddrType = pingAddrType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGatherType() {
		return gatherType;
	}

	public void setGatherType(String gatherType) {
		this.gatherType = gatherType;
	}

	public List<Map<String, String>> getResultList() {
		return resultList;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}


	public String getPingAddr() {
		return pingAddr;
	}


	public void setPingAddr(String pingAddr) {
		this.pingAddr = pingAddr;
	}


	public String getPackageSize() {
		return packageSize;
	}


	public void setPackageSize(String packageSize) {
		this.packageSize = packageSize;
	}


	public String getPingTimes() {
		return pingTimes;
	}


	public void setPingTimes(String pingTimes) {
		this.pingTimes = pingTimes;
	}


	public String getTimeOut() {
		return timeOut;
	}


	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}


	public String getPingAddrDns() {
		return pingAddrDns;
	}


	public void setPingAddrDns(String pingAddrDns) {
		this.pingAddrDns = pingAddrDns;
	}


	public String getPackageSizeDns() {
		return packageSizeDns;
	}


	public void setPackageSizeDns(String packageSizeDns) {
		this.packageSizeDns = packageSizeDns;
	}


	public String getPingTimesDns() {
		return pingTimesDns;
	}


	public void setPingTimesDns(String pingTimesDns) {
		this.pingTimesDns = pingTimesDns;
	}


	public String getTimeOutDns() {
		return timeOutDns;
	}


	public void setTimeOutDns(String timeOutDns) {
		this.timeOutDns = timeOutDns;
	}


	public String getPingAddrSpecial() {
		return pingAddrSpecial;
	}


	public void setPingAddrSpecial(String pingAddrSpecial) {
		this.pingAddrSpecial = pingAddrSpecial;
	}


	public String getPackageSizeSpecial() {
		return packageSizeSpecial;
	}


	public void setPackageSizeSpecial(String packageSizeSpecial) {
		this.packageSizeSpecial = packageSizeSpecial;
	}


	public String getPingTimesSpecial() {
		return pingTimesSpecial;
	}


	public void setPingTimesSpecial(String pingTimesSpecial) {
		this.pingTimesSpecial = pingTimesSpecial;
	}


	public String getTimeOutSpecial() {
		return timeOutSpecial;
	}


	public void setTimeOutSpecial(String timeOutSpecial) {
		this.timeOutSpecial = timeOutSpecial;
	}

	public void setWireType(String wireType) {
		this.wireType = Integer.valueOf(wireType);
	}


	public String getPingAddrLan() {
		return pingAddrLan;
	}


	public void setPingAddrLan(String pingAddrLan) {
		this.pingAddrLan = pingAddrLan;
	}


	public String getPackageSizeLan() {
		return packageSizeLan;
	}

	public void setPackageSizeLan(String packageSizeLan) {
		this.packageSizeLan = packageSizeLan;
	}

	public String getPingTimesLan() {
		return pingTimesLan;
	}

	public void setPingTimesLan(String pingTimesLan) {
		this.pingTimesLan = pingTimesLan;
	}

	public String getTimeOutLan() {
		return timeOutLan;
	}

	public void setTimeOutLan(String timeOutLan) {
		this.timeOutLan = timeOutLan;
	}

	public String getPingInterLan() {
		return pingInterLan;
	}

	public void setPingInterLan(String pingInterLan) {
		this.pingInterLan = pingInterLan;
	}

	public DiagResult getDiagResult() {
		return diagResult;
	}


	public String getAccessType() {
		return accessType;
	}


	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}


	public String getPon_type() {
		return pon_type;
	}


	public void setPon_type(String pon_type) {
		this.pon_type = pon_type;
	}


	public PONInfoOBJ[] getPonInfoOBJArr() {
		return ponInfoOBJArr;
	}


	public void setPonInfoOBJArr(PONInfoOBJ[] ponInfoOBJArr) {
		this.ponInfoOBJArr = ponInfoOBJArr;
	}


	public String getGw_type() {
		return gw_type;
	}


	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}
	
	public List<Map> getCheckDetailList() {
		return checkDetailList;
	}

	public void setCheckDetailList(List<Map> checkDetailList) {
		this.checkDetailList = checkDetailList;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}

	@Override
	public void setServletResponse(HttpServletResponse response){
		this.response = response;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getDevice_serialnumber() {
		return device_serialnumber;
	}

	public void setDevice_serialnumber(String device_serialnumber) {
		this.device_serialnumber = device_serialnumber;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getDevice_model() {
		return device_model;
	}

	public void setDevice_model(String device_model) {
		this.device_model = device_model;
	}

	public String getHardwareversion() {
		return hardwareversion;
	}

	public void setHardwareversion(String hardwareversion) {
		this.hardwareversion = hardwareversion;
	}

	public String getSoftwareversion() {
		return softwareversion;
	}

	public void setSoftwareversion(String softwareversion) {
		this.softwareversion = softwareversion;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setSession(Map session) {
		this.session = session;
	}

	public Map getSession()
	{
		return session;
	}
	
}