<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="/timelater.jsp"%>
<%@ include file="../head.jsp"%>

<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO"%>
<%@ page import="com.linkage.litms.common.database.*"%>
<%@ page import="com.linkage.litms.common.util.*"%>
<%@ page import="com.linkage.module.gwms.blocTest.act.QueryEgwcustServInfoACT"%>

<%@ page import="java.util.*"%>

<%@ taglib prefix="s" uri="/struts-tags"%>

<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct" />
<jsp:useBean id="EGWUserInfoAct" scope="request" class="com.linkage.litms.resource.EGWUserInfoAct" />
<jsp:useBean id="FileSevice" scope="request" class="com.linkage.litms.resource.FileSevice" />
<%
	Cursor cursor = null;
	Map fields = null;
	request.setCharacterEncoding("GBK");
	String device_id = request.getParameter("device_id");
	
	// 获取设上行方式
	QueryEgwcustServInfoACT qesiACT2 = new QueryEgwcustServInfoACT();
	Cursor cursor2 = qesiACT2.getAccessType(device_id);  
	Map fields2 = cursor2.getNext();
	String access_type = fields2.get("access_type")==null?"":(String)fields2.get("access_type");
	if("1".equals(access_type)){
		access_type = "DSL";
	}else if("2".equals(access_type)){
		access_type = "Ethernet";
	}else if("3".equals(access_type)){
		access_type = "EPON";
	}else if("4".equals(access_type)){
		access_type = "GPON";
	}
	
	//String gw_type = request.getParameter("gw_type");
	//if (null == gw_type || "".equals(gw_type)) {
	//	gw_type = "1";
	//}
	//用户信息
	String username = "";
	String service_name = "";
	// 获取基本设备信息
	fields = DeviceAct.getSingleDeviceInfo(request);
	String device_serialnumber = "";//设备序列号
	String device_name = "";//设备名称
	String maxenvelopes = "";//最大包数Envelopes
	//String port = "";//端口
	//String path = "";//地址
	//String retrycount = "";//重试次数
	String device_model = "";//设备型号
	String devicetype_id = "";
	String cpe_mac = "";//设备MAC地址
	String loopback_ip = "";
	//	2009/03/04 漆学启注释，原因：判断终端在线状态以及设备更新时间变换，现在从设备状态表查询
	//	String cpe_currentupdatetime = "";//设备最近更新时间
	String last_time = "";
	String software_version = ""; //软件版本
	String handware_version = ""; //硬件版本
	String spec_version = ""; //硬件版本
	//	2009/03/04 漆学启注释，原因：判断终端在线状态以及设备更新时间变换，现在从设备状态表查询
	//	String cpe_currentstatus = "";//设备当前注册状态
	String online_status = "";
	String online_status_1 = "";
	String cpe_operationinfo = "";//设备操作的历史信息
	String oui = "";//厂商oui
	String manufacturer = "";
	//String oui_name = "";
	String device_area_id = "";
	String device_status = "";
	String city_id = "";
	String complete_time = "";
	String device_type = "";
	//客户id
	String customer_id = "";
	//管理地址
	String device_url = "";
	String gw_type = "";
	String gw_type_name = "";
	//在线状态
	String status = "";
	//厂商ID
	String vendor_id = "";
	if (fields != null)
	{
		oui = (String) fields.get("oui");
		vendor_id = (String) fields.get("vendor_id");
		device_serialnumber = (String) fields.get("device_serialnumber");
		cpe_mac = (String) fields.get("cpe_mac");
		loopback_ip = (String) fields.get("loopback_ip");
		//		2009/03/04 漆学启注释，原因：判断终端在线状态以及设备更新时间变换，现在从设备状态表查询
		//		cpe_currentupdatetime = (String) fields.get("cpe_currentupdatetime");
		last_time = (String) fields.get("last_time");
		complete_time = (String) fields.get("complete_time");
		//		2009/03/04 漆学启注释，原因：判断终端在线状态以及设备更新时间变换，现在从设备状态表查询
		//		cpe_currentstatus = (String) fields.get("cpe_currentstatus");
		online_status = (String) fields.get("online_status");
		online_status_1 = online_status;
		//	cpe_operationinfo = (String)fields.get("cpe_operationinfo");
		device_name = (String) fields.get("device_name");
		devicetype_id = (String) fields.get("devicetype_id");
		//oui = oui_name = (String)fields.get("oui");
		oui = (String) fields.get("oui");
		device_area_id = (String) fields.get("area_id");
		device_status = (String) fields.get("device_status");
		maxenvelopes = (String) fields.get("maxenvelopes");
		gw_type = (String) fields.get("gw_type");
		if (gw_type.equals("0"))
		{
			gw_type_name = "普通网络设备";
		}
		else if (gw_type.equals("1"))
		{
			gw_type_name = "家庭网关设备";
		}
		else if (gw_type.equals("4"))
		{
			gw_type_name = "安全网关设备";
		}
		else if (gw_type.equals("5"))
		{
			gw_type_name = "混合型网关";
		}
		else
		{
			gw_type_name = "企业网关设备";
		}
		city_id = (String) fields.get("city_id");
		device_type = (String) fields.get("device_type");
		customer_id = (String) fields.get("customer_id");
		device_url = (String) fields.get("device_url");
		//2009/03/04 漆学启注释，原因：判断终端在线状态以及设备更新时间变换，现在从设备状态表查询
		//判断在线状态
		//		String max_time = (String) fields.get("max_time");
		//		if (max_time != null) {
		//	Date date = new Date();
		//	long nowtime = date.getTime();
		//	if ((nowtime - Long.parseLong(max_time) * 1000) > 5 * 60 * 1000) {
		//		status = "<font color='red'>设备下线</font>";
		//	} else {
		//		status = "<font color='green'>设备在线</font>";
		//	}
		//		} else {
		//	status = "<font color='red'>设备下线</font>";
		//		}
		//		if (cpe_currentstatus != null && "1".equals(cpe_currentstatus)) {
		//	cpe_currentstatus = "<font color='green'>设备在线</font>";
		//		} else {
		//	cpe_currentstatus = "<font color='red'>设备下线</font>";
		//		}
		//		if (!LipossGlobals.getLipossProperty("InstArea.ShortName").equals("gd_dx"))
		//	status = cpe_currentstatus;
		//	}
		//将cpe_currentupdatetime转换成时间
		//	if (cpe_currentupdatetime != null && !cpe_currentupdatetime.equals("")) {
		//		DateTimeUtil dateTimeUtil = new DateTimeUtil(
		//		Long.parseLong(cpe_currentupdatetime) * 1000);
		//		cpe_currentupdatetime = dateTimeUtil.getLongDate();
		//		dateTimeUtil = null;
	}
	//online_status将在线状态转换
	if (null != online_status && "1".equals(online_status))
	{
		online_status = "<font color='green'>设备在线</font>";
	}
	else
	{
		online_status = "<font color='red'>设备下线</font>";
	}
	//将last_time转换成时间
	if (null != last_time && !("").equals(last_time))
	{
		DateTimeUtil dateTimeUtil = new DateTimeUtil(
		Long.parseLong(last_time) * 1000);
		last_time = dateTimeUtil.getLongDate();
		dateTimeUtil = null;
	}
	//将complete_time转换成时间
	if (complete_time != null && !complete_time.equals(""))
	{
		DateTimeUtil dateTimeUtil = new DateTimeUtil(Long
		.parseLong(complete_time) * 1000);
		complete_time = dateTimeUtil.getLongDate();
		dateTimeUtil = null;
	}
	//获取设备的相应的硬件版本/软件版本
	fields = DeviceAct.getDeviceModelVersion(vendor_id, devicetype_id);
	if (fields != null)
	{
		software_version = (String) fields.get("softwareversion");
		handware_version = (String) fields.get("hardwareversion");
		spec_version = (String) fields.get("specversion");
		manufacturer = (String) fields.get("vendor_name");
		device_model = (String) fields.get("device_model");
	}
	cpe_operationinfo = cpe_operationinfo == null ? "无" : cpe_operationinfo;
	//设备其他信息
	String device_area_name = "";//设备管理区域
	String device_support_code = "";//设备所支持的业务代码
	String device_current_code = "";//设备当前开通的业务代码
	String device_hard_code = "";//设备硬件所支持的业务代码
	device_support_code = device_hard_code = DeviceAct
			.getSupportService(devicetype_id);
	//device_current_code = DeviceAct.getSupportServiceByDevice(device_id);//设备当前开通的业务代码
	Map area_Map = DeviceAct.getAreaIdMapName();
	device_area_name = (String) area_Map.get(device_area_id);
	cursor = DeviceAct.getDeviceModelTemplate(oui, devicetype_id);
	fields = cursor.getNext();
	String deviceModelTemplate = "";
	StringBuffer sb = new StringBuffer();
	//换行字符串形式
	sb.append("");
	while (fields != null)
	{
		sb.append("" + fields.get("template_name") + "\n");
		fields = cursor.getNext();
	}
	deviceModelTemplate = sb.toString();
	sb = null;
	//获取ITMS操作历史信息
	//cpe_operationinfo = DeviceAct.getHistoryOperation(device_id);
%>
<%
	 /*   
	 *    add by benyp
	 */
	Cursor cursor1 = new Cursor();
	Map fields1 = null;
	if (device_id != null)
	{
		String sql = "select device_serialnumber,x_com_username,x_com_passwd,cpe_username,cpe_passwd,acs_username,acs_passwd from tab_gw_device where device_id ='"
		+ device_id + "'";
		cursor1 = DataSetBean.getCursor(sql);
	}
	String x_com_username = "";
	String x_com_passwd = "";
	String cpe_username = "";
	String cpe_passwd = "";
	String acs_username = "";
	String acs_passwd = "";
	fields1 = cursor1.getNext();
	while (fields1 != null)
	{
		x_com_username = (String) fields1.get("x_com_username");
		x_com_passwd = (String) fields1.get("x_com_passwd");
		cpe_username = (String) fields1.get("cpe_username");
		cpe_passwd = (String) fields1.get("cpe_passwd");
		acs_username = (String) fields1.get("acs_username");
		acs_passwd = (String) fields1.get("acs_passwd");
		fields1 = cursor1.getNext();
	}
	//获取cityMap
	//String city_id_ = "";
	//String city_name = "";
	//String city_sql = "select city_id, city_name from tab_city";
	//Cursor city_cursor = new Cursor();
	//city_cursor = DataSetBean.getCursor(city_sql);
	//Map cityFiled = city_cursor.getNext();
	//HashMap cityMap = new HashMap();
	//while (cityFiled != null)
	//{
	//	city_id_ = cityFiled.get("city_id").toString();
	//	city_name = cityFiled.get("city_name").toString();
	//	cityMap.put(city_id_, city_name);
	//	cityFiled = city_cursor.getNext();
	//}
	Map cityMap = CityDAO.getCityIdCityNameMap();
	String city = "";
	if (null != cityMap.get(city_id))
	{
		city = cityMap.get(city_id).toString();
	}
%>

<script type="text/javascript" src="../../Js/jquery.js"></script>
<script>
<!-- 
var request = false;
   try {
     request = new XMLHttpRequest();
   } catch (trymicrosoft) {
     try {
       request = new ActiveXObject("Msxml2.XMLHTTP");
       
     } catch (othermicrosoft) {
       try {
         request = new ActiveXObject("Microsoft.XMLHTTP");
       } catch (failed) {
         request = false;
       }  
     }
   }
   if (!request)
     alert("Error initializing XMLHttpRequest!");
     
	//用于用户名检查的ajax
   function send_request(url) {
     request.open("GET", url, true);
     request.onreadystatechange = updatePage;
     request.send(null);
   }

	function updatePage() {
     	if (request.readyState == 4) {
        if (request.status == 200) {
        	document.all.operation_info.innerHTML = request.responseText;
        } else
            alert("status is " + request.status);
     	}
   }

   
   function openCust(param){
		window.open("<s:url value='/bbms/CustomerInfo!detailInfo.action'/>?customer_id="+param,"","left=20,top=20,height=450,width=700,resizable=yes scrollbars=yes,status=yes,toolbar=no,menubar=no,location=no");
	}
    

	function showMsgDlg(){
		w = document.body.clientWidth;
		h = document.body.clientHeight;

		l = (w-250)/2;
		t = (h-60)/2;
		PendingMessage.style.left = l;
		PendingMessage.style.top  = t;
		PendingMessage.style.display="";
	}
	function closeMsgDlg(){
		PendingMessage.style.display="none";
	}

	function disableBtns() {
		document.all.compwGen.disabled = true;
		document.all.compwSet.disabled = true;
	}

	function inspireBtns() {
		document.all.compwGen.disabled = false;
		document.all.compwSet.disabled = false;
	}
	
	function getOnlineStatus() {
		document.all.onlineStatus.innerHTML = "<font color='blue'>正在获取设备在线状态</font>";
		send_request_OS("../../paramConfig/testConnectionSubmit.jsp?device_id="+<%=device_id%>+"&tt="+new Date().getTime());
	}
	
	function send_request_OS(url) {
     request.open("GET", url, true);
     request.onreadystatechange = updatePageOS;
     request.send(null);
   }

	function updatePageOS() {
     	if (request.readyState == 4) {
        if (request.status == 200) {
        	var onlineStatus = request.responseText;
        	onlineStatus = onlineStatus.replace(/(^\s*)|(\s*$)/g, ""); 
        	//alert(onlineStatus);
        	if(onlineStatus=="此设备连接成功！"){
	        	document.all.onlineStatus.innerHTML = "<font color='green'>设备在线(实时)</font>";
	        	document.all.onlineStatus.value = onlineStatus;
        	}else{
        		document.all.onlineStatus.innerHTML = "<font color='red'>设备下线(实时)</font>";
	        	document.all.onlineStatus.value = onlineStatus;
        	}
        } else
            alert("status is " + request.status);
     	}
   }
 
   





 function GoContent(user_id,gw_type){
 	var strpage="EGWUserRelatedInfo.jsp?user_id=" + user_id;
	window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}
//-->
</script>
<style type="text/css">
<!--
.btn_g {
	border: 1px solid #999999
}
//
-->
</style>

<div id="PendingMessage"
	style="position: absolute; z-index: 3; top: 240px; left: 250px; width: 250; height: 60; border-width: 1; border-style: ridge; background-color: #eeeeee; padding-top: 10px; display: none">
	<center>
		<table border="0">
			<tr>
				<td valign="middle">
					<img src="../../images/cursor_hourglas.gif" border="0" WIDTH="30"
						HEIGHT="30">
				</td>
				<td>
					&nbsp;&nbsp;
				</td>
				<td valign="middle">
					<span id="txtLoading" style="font-size: 12px; font-family: 宋体">正在设置密码，请稍等…</span>
				</td>
			</tr>
		</table>
	</center>
</div>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD>
			<TABLE width="99%" border=0 cellspacing=0 cellpadding=0
				align="center">
				<TR>
					<TD bgcolor=#999999>
						<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
							<TR>
								<TH colspan="4" align="center">
									设备〖
									<%=device_serialnumber%>
									〗详细信息
								</TH>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="center" colspan=4>
									设备基本信息
								</TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right" width="15%">
									设备ID
								</TD>
								<TD width="25%"><%=device_id%>
								<input type="hidden" name="deviceId" value="<%=device_id%>">
								</TD>
								<TD class=column align="right">
									设备型号
								</TD>
								<TD width="40%"><%=device_model%>
								<input type="hidden" name="deviceModel" value="<%=device_model%>">
								</TD>
							</TR>

							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									设备厂商(OUI)
								</TD>
								<TD><%=manufacturer%>(<%=oui%>)
								<input type="hidden" name="oui" value="<%=oui%>">
								</TD>
								<TD class=column align="right">
									序列号
								</TD>
								<TD><%=device_serialnumber%>
								<input type="hidden" name="deviceSn" value="<%=device_serialnumber%>">
								</TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									硬件版本
								</TD>
								<TD><%=handware_version%></TD>
								<TD class=column align="right">
									特别版本
								</TD>
								<TD><%=spec_version%></TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									软件版本
								</TD>
								<TD><%=software_version%></TD>
								<TD class=column align="right">
									设备类型
								</TD>
								<TD><%=device_type%></TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									最大包数
								</TD>
								<TD width=140><%=maxenvelopes%></TD>
								<TD class=column align="right">
									MAC 地址
								</TD>
								<TD><%=cpe_mac%></TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									设备编号
								</TD>
								<TD width=140><%=device_id%></TD>
								<TD class=column align="right">
									管理域
								</TD>
								<TD><%=device_area_name%></TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									设备组ID
								</TD>
								<TD><%=gw_type%></TD>
								<TD class=column align="right">
									设备组名称
								</TD>
								<TD><%=gw_type_name%></TD>
							</TR>
							<%
								if ("2".equals(gw_type))
								{
							%>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									企业名称
								</TD>
								<TD><%=EGWUserInfoAct.getCustomerName(customer_id)%></TD>
								<TD class=column align="right">
									管理地址
								</TD>
								<TD>
									<a href="#" onclick="window.open('<%=device_url%>')"><%=device_url%></a>
								</TD>
							</TR>
							<%
							}
							%>
							<%
								List devServiceStatus = DeviceAct.getServiceStatByDevice(device_id);
								//List bindCustomerStatus = DeviceAct.getBindCustomerStatusByDeviceId(oui,device_serialnumber);
							%>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="center" colspan=4>
									设备业务信息
								</TD>
							</TR>

							<%
							/**
								String outStr = null;
								if (bindCustomerStatus != null && bindCustomerStatus.size() > 0)
								{
									int len = bindCustomerStatus.size();
									int pause = 0;
									int stop = 0;
									for (int i = 0; i < len; i++)
									{
										String user_state = (String) bindCustomerStatus.get(i);
										if ("2".equals(user_state))
										{
									pause++;
										}
										if ("3".equals(user_state))
										{
									stop++;
										}
									}
									if (pause == len)
									{
										outStr = "暂停";
									}
									if (stop == len)
									{
										outStr = "停机";
									}
								}
								
								if (outStr == null) {
									if (cpe_currentstatus.equals("1")) {
										outStr = "在线";
									} else if (cpe_currentstatus.equals("0")) {
										outStr = "不在线";
									} else {
										outStr = "未知";
									}
								}*/
							%>


							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									当前开通业务
								</TD>
								<TD><%=devServiceStatus == null || devServiceStatus.size() == 0 ? "<无>"
							: "" + devServiceStatus.size() + "个业务"%>
								</TD>
								<TD class=column align="right">
									是否激活
								</TD>
								<TD><%=devServiceStatus == null || devServiceStatus.size() == 0 ? "<无>"
							: ""%>
								</TD>
							</TR>
							<%
								if (devServiceStatus != null)
								{
									if (devServiceStatus.size() > 0)
									{
										int len = devServiceStatus.size();
										for (int i = 0; i < len; i++)
										{
									String[] devStatusData = (String[]) devServiceStatus.get(i);
									String t = devStatusData[1];
									if (t != null && !"".equals(t.trim()))
									{
									}
							%>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right"></TD>
								<TD><%=devStatusData[2]%></TD>
								<TD class=column align="right"></TD>
								<TD><%="1".equals(devStatusData[1]) ? "已激活" : "0".equals(devStatusData[1])?"未激活":"激活失败"%></TD>
							</TR>
							<%
									}
									}
								}
							%>

							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="center" colspan=4>
									设备动态信息
								</TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									设备在线状态
								</TD>
								<TD colspan="3" width="30%">
									<span id="onlineStatus"><%=online_status%></span>&nbsp;&nbsp;
									<input name="onlineStatusGet" type="button" value="检测在线状态"
										class="btn_g" onclick="getOnlineStatus()">
								</TD>
							</TR>

							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									设备属地
									<input type="hidden" name="cityId" value="<%=city_id%>">
								</TD>
								<TD><%=city%></TD>
								<TD class=column align="right">
									注册系统时间
								</TD>
								<TD><%=complete_time%></TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									IP地址
								</TD>
								<TD><%=loopback_ip%></TD>
								<TD class=column align="right">
									最近更新时间
								</TD>
								<TD><%=last_time%></TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">

								<TD class=column align="right">
									设备当前注册状态
								</TD>
								<TD>
									<%
											if (device_status.equals("1"))
											out.println("已确认");
										else if (device_status.equals("0"))
											out.println("未确认");
										else
											out.println("已删除");
									%>
								</TD>
								<TD class=column align="right">
									设备配置模板
								</TD>
								<TD>
									<textarea cols=40 rows=4 readonly><%=deviceModelTemplate%></textarea>
								</TD>
							</TR>

							<TR>
								<TD class=column align="center" colspan=4>

									<div id="div_device"
										style="width: 100%; height: 100%; z-index: 1; top: 100%;">
										<span id="operation_info"></span>
									</div>
								</TD>

							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="center" colspan="4">
									当前配置参数
								</TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									CPE用户名
								</TD>
								<TD><%=cpe_username%></TD>
								<TD class=column align="right">
									CPE密码
								</TD>
								<TD><%=cpe_passwd%></TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									ACS用户名
								</TD>
								<TD><%=acs_username%></TD>
								<TD class=column align="right">
									ACS密码
								</TD>
								<TD><%=acs_passwd%></TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									电信维护账号
								</TD>
								<TD><%=x_com_username%></TD>
								<TD class=column align="right">
									电信维护密码
								</TD>
								<TD>
									<span id="compw_span"><%=x_com_passwd%></span>&nbsp;
									<span id="compw_span_new"></span>&nbsp;
									<!-- <input name="compw_span_hid" value="" type="hidden"/> -->
								</TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="center" colspan=4>
									用户基本信息
								</TD>
							</TR>
							<%
								//得到用户信息
								cursor = DeviceAct.getCustomerOfDev(device_id, gw_type);
								fields = cursor.getNext();
								if (fields == null){
							%>
							<TR bgcolor="#FFFFFF" height="20">
								<TD align="center" colspan=4>
									设备暂无用户.
								</TD>
							</TR>
							<%
								}else{
								String user_id = "";
								while (fields != null){
									username = (String) fields.get("username");
									service_name = (String) fields.get("serv_type_name");
									user_id = (String) fields.get("user_id");
									fields = cursor.getNext();
							%>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									用户帐号
								</TD>
								<TD>
								<a href="javascript:" onclick="GoContent('<%=user_id%>','<%=gw_type%>')"><%=username%></a>
								<input type="hidden" name="adNumber" value="<%=username%>">
								</TD>
								<TD class=column align="right">
									业务类型
								</TD>
								<TD><%=service_name%></TD>
							</TR>
							<%
							 	}
							 	QueryEgwcustServInfoACT qesiACT = new QueryEgwcustServInfoACT();
							 	cursor = qesiACT.getEgwcustServInfo(user_id);  //获取 PPPoE账号、PVC号、VLAN号、接口状态
								fields = cursor.getNext();
								if(fields != null){
									String PPPoENo = (String) fields.get("username");   //PPPoE账号
									
									
									String vpiid = fields.get("vpiid")==null?"":(String)fields.get("vpiid");
									String vciid = fields.get("vciid")==null?"":(String)fields.get("vciid");
									String PVC = vpiid+"/"+vciid; // PVC号
									
									String PVC_VLAN = fields.get("vlanid")==null? "":(String)fields.get("vlanid");  // VLAN号
									
									//路由类型则用PVC，桥接用VLANID
									if("DSL".equals(access_type))
									{
										PVC_VLAN = PVC;
									}
									
									String satusStr = "";
									if (null != online_status_1 && "1".equals(online_status_1)){
										satusStr = "UP";
									}else{
										satusStr = "DOWN";
									}
							%>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">PPPoE账号</TD>
								<TD><%=PPPoENo%></TD>
								<TD class=column align="right">接口状态</TD>
								<TD><%=satusStr%></TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">PVC/VLAN</TD>
								<TD><%=PVC_VLAN%></TD>
								<TD class=column align="right"></TD>
								<TD></TD>
							</TR>
							<%
								}
							}
								if (!"1".equals(gw_type))
								{
									//得到用户信息
									String customer_name;
									String customer_pwd;
									String customer_address;
									String mobile;
									String linkphone;
									String phoneStr;
									cursor = DeviceAct.getCustomerOfDevBBMS(device_id);
									fields = cursor.getNext();
									
									if (fields == null)
									{
							%>
							<TR bgcolor="#FFFFFF" height="20">
								<TD align="center" colspan=4>
									设备暂无客户信息.
								</TD>
							</TR>
							<%
									}
									while (fields != null)
									{
										customer_name = (String) fields.get("customer_name");
										customer_pwd = (String) fields.get("customer_pwd");
										customer_address = fields.get("customer_address") ==null?"":(String)fields.get("customer_address");
										mobile = fields.get("mobile") == null?"":(String)fields.get("mobile");
										linkphone = fields.get("linkphone")==null?"":(String)fields.get("linkphone");
										if(!"".equals(mobile)){
											phoneStr = mobile;
										}else{
											phoneStr = linkphone;
										}
										fields = cursor.getNext();
							%>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									客户名称
								</TD>
								<TD>
									<a href="javascript:openCust('<%=customer_id%>')">
									<%=customer_name%>
									</a>
								</TD>
								<TD class=column align="right">
									客户密码
								</TD>
								<TD><%=customer_pwd%></TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">联系电话</TD>
								<TD><%=phoneStr%></TD>
								<TD class=column align="right">装机地址</TD>
								<TD><%=customer_address%></TD>
							</TR>
							
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">接入方式</TD>
								<TD><%=access_type%></TD>
								<TD class=column align="right"></TD>
								<TD></TD>
							</TR>
							<%
								}
								}
							%>
							<TR>
								<TD colspan="4" align="center" class=foot>
									<!--<INPUT TYPE="submit" value=" 关 闭 " class=jianbian
										onclick="javascript:window.close();">-->
								</TD>
							</TR>

						</TABLE>


					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			&nbsp;
			<div id="setPWDIV"></div>
		</TD>

	</TR>
</TABLE>
<%
	cursor = null;
	if (fields != null)
	{
		fields.clear();
	}
	fields = null;
	DeviceAct = null;
%>
<%@ include file="../foot.jsp"%>