<%@page import="com.linkage.module.gwms.dao.tabquery.CityDAO"%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@ page
	import="com.linkage.litms.common.database.*,java.util.*,com.linkage.litms.common.util.*"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />
<jsp:useBean id="EGWUserInfoAct" scope="request"
	class="com.linkage.litms.resource.EGWUserInfoAct" />
<jsp:useBean id="FileSevice" scope="request"
	class="com.linkage.litms.resource.FileSevice" />
<jsp:useBean id="HGWUserInfoAct" scope="request"
	class="com.linkage.litms.resource.HGWUserInfoAct" />
	
<%
	Cursor cursor = null;
	Map fields = null;
	request.setCharacterEncoding("GBK");
	String device_id = request.getParameter("device_id");
	String gw_type = request.getParameter("gw_type");
	if (null == gw_type || "".equals(gw_type)) {
		gw_type = "1";
	}
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
	String is_normal = ""; //是否规范
	String is_check = ""; //是否审核
	String spec_version = ""; //硬件版本
	//	2009/03/04 漆学启注释，原因：判断终端在线状态以及设备更新时间变换，现在从设备状态表查询
	//	String cpe_currentstatus = "";//设备当前注册状态
	String online_status = "";
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
	// String gw_type = "";
	String gw_type_name = "";
	//在线状态
	String status = "";
	//厂商ID
	String vendor_id = "";
	//终端规格
	String spec_name = "";
	//是否支持awifi开通
	String is_awifi = "否";
	//河北增加光猫标识
	String gigabit_port="";
	String gigabitport="";
	// 江西新增终端支持速率
	String gbbroadband="";
	// 江西新增接入类型
	String accessStyle="";
	
	//新疆增加上行方式和版本类型,是否电信资产
	String access_type = "";
	String deviceTypeName = "";
	String isTelDev = "";

	String device_version_type="";

	//新疆增加报废终端状态 0 为报废，空为正常
	String scrapStatus="报废";
	
	if (fields != null)
	{
		oui = (String) fields.get("oui");
		vendor_id = (String) fields.get("vendor_id");
		device_serialnumber = (String) fields.get("device_serialnumber");
		cpe_mac = (String) fields.get("cpe_mac");
		
		spec_name = (String)fields.get("spec_name");
		if("hb_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
		gigabit_port=(String)fields.get("gigabit_port");
		if(gigabit_port.equals("1"))
		{
			gigabitport="是";
		}else
		{
			gigabitport="否";
		}
		}
		
		if("gs_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
			device_version_type = (String)fields.get("device_version_type");
			if("1".equals(device_version_type)){
				device_version_type= "E8-C";
			}else if ("2".equals(device_version_type)){
				device_version_type= "PON融合";
			}else if ("3".equals(device_version_type)){
				device_version_type= "10GPON";
			}else if ("4".equals(device_version_type)){
				device_version_type= "政企网关";
			}else if ("5".equals(device_version_type)){
				device_version_type= "天翼网关1.0";
			}else if ("6".equals(device_version_type)){
				device_version_type= "天翼网关2.0";
			}else if ("7".equals(device_version_type)){
				device_version_type= "天翼网关3.0";
			}else{
				device_version_type="";
			}
			
			
		}

        if("ah_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
			device_version_type = (String)fields.get("deviceVersionType");
		}
		
		if("jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
			boolean flg = DeviceAct.IsHtMegaBytes(device_id);
			gbbroadband=(String)fields.get("gbbroadband");
			
			if(gbbroadband.equals("1"))
			{
				gbbroadband="千兆";
			}
			// 20200512 江西电信新增万兆
			else if(gbbroadband.equals("3"))
			{
				gbbroadband="万兆";
			}
			else if(flg == true)
			{
				gbbroadband="千兆";
			}
			else
			{
				gbbroadband="百兆";
			}

			if (null != fields.get("device_version_type")) {
			    if ("5".equals(fields.get("device_version_type").toString())) {
					accessStyle = "XGPON";
				}
				else if ("4".equals(fields.get("device_version_type").toString())) {
					accessStyle = "10GEPON";
				}
			}
			if ("".equals(accessStyle) && null != fields.get("access_style_relay_id")) {
				if ("3".equals(fields.get("access_style_relay_id").toString())) {
					accessStyle = "EPON";
				}
				else if ("4".equals(fields.get("access_style_relay_id").toString())) {
					accessStyle = "GPON";
				}
			}
			if ("".equals(accessStyle) && null != fields.get("access_style_id")) {
				if ("3".equals(fields.get("access_style_id").toString())) {
					accessStyle = "EPON";
				}
				else if ("4".equals(fields.get("access_style_id").toString())) {
					accessStyle = "GPON";
				}
			}

		}
		loopback_ip = (String) fields.get("loopback_ip");
		//		2009/03/04 漆学启注释，原因：判断终端在线状态以及设备更新时间变换，现在从设备状态表查询
		//		cpe_currentupdatetime = (String) fields.get("cpe_currentupdatetime");
		last_time = (String) fields.get("last_time");
		complete_time = (String) fields.get("complete_time");
		//		2009/03/04 漆学启注释，原因：判断终端在线状态以及设备更新时间变换，现在从设备状态表查询
		//		cpe_currentstatus = (String) fields.get("cpe_currentstatus");
		online_status = (String) fields.get("online_status");
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
		if (null != fields.get("is_awifi") && "1".equals((String) fields.get("is_awifi")))
		{
			is_awifi = "是";
		}
		if("xj_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
			String access_type_id = (String)fields.get("device_version_type");
			if("1".equals(access_type_id)){
				deviceTypeName = "";  //应需求当为空或为E8C的时候显示为空
			}else if("2".equals(access_type_id)){
				deviceTypeName = "天翼网关1.0";
			}else if("3".equals(access_type_id)){
				deviceTypeName = "天翼网关2.0";
			}else if("4".equals(access_type_id)){
				deviceTypeName = "融合网关";
			}else if("5".equals(access_type_id)){
				deviceTypeName = "天翼网关3.0";
			}else{
				deviceTypeName = "";
			}
			access_type = (String)fields.get("type_name");
			System.out.print("deviceTypeName:"+deviceTypeName+",access_type:"+access_type);
			
			String telDev = (String)fields.get("is_tel_dev");
			if("1".equals(telDev)){
				isTelDev = "电信"; 
			}else if("2".equals(telDev)){
				isTelDev = "非电信";
			}

			String isScrap  =  (String)fields.get("isscrap") ;
			String devStatus = (String)fields.get("dev_id") ;
			if("".equals(isScrap) && "".equals(devStatus)){
				scrapStatus ="正常";
			}


		}
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
		online_status = "<font color='green'>能正常交互 </font>";
	}
	else
	{
		online_status = "<font color='red'>不能正常交互</font>";
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
	// 山西联通机顶盒
	if("sx_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")) &&  gw_type.equals("4"))
	{
		fields = DeviceAct.getStbDeviceModelVersion(vendor_id, devicetype_id);
	}
	else 
	{
		fields = DeviceAct.getDeviceModelVersion(vendor_id, devicetype_id);
	}
	if (fields != null)
	{
		software_version = (String) fields.get("softwareversion");
		handware_version = (String) fields.get("hardwareversion");
		spec_version = (String) fields.get("specversion");
		manufacturer = (String) fields.get("vendor_name");
		device_model = (String) fields.get("device_model");
		is_normal = (String) fields.get("is_normal");
		is_check = (String) fields.get("is_check");
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
		String sql = "";
		// 山西联通机顶盒
		if("sx_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")) &&  gw_type.equals("4"))
		{
			sql = "select device_serialnumber,x_com_username,x_com_passwd,cpe_username,cpe_passwd,"
				+ "acs_username,acs_passwd from stb_tab_gw_device where device_id ='" + device_id + "'";
		}else
		{
			sql = "select device_serialnumber,x_com_username,x_com_passwd,cpe_username,cpe_passwd,acs_username,acs_passwd from tab_gw_device where device_id ='"
				+ device_id + "'";
		}
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
	Map cityMap = CityDAO.getCityIdCityNameMap();
	String city = "";
	if (null != cityMap.get(city_id))
	{
		city = cityMap.get(city_id).toString();
	}
%>
<%
	/*
	*  added  by zhangsb 2012年6月11日  新疆需求 --设备详细信息展示页面增加软件升级版本信息 start
	*/
	String stragyStatusCode = "";
		String stragyStatusName = "";
		String resultId = "";
		String result = "";
		String sheetParam = "";
		String newDeviceTypeId = "";
		String oldDeviceTypeId = "";
		String newDeviceType = "";
		String oldDeviceType = "";
		Map fields2 = null;
		Map fields3 = null;
	if(LipossGlobals.isXJDX()){
		
		fields2 = DeviceAct.getCurrentSoftUpStrategy(request);
		fields3 = DeviceAct.getCurrentSoftUpStrategyXML(request);
		
		Map<String,String> softMap = DeviceAct.getSoftwareKV();
		
	 	if(null != fields2){
	 		stragyStatusCode = (String)fields2.get("status");
	 		resultId = (String)fields2.get("resultId");
	 		if(null != fields2.get("newDeviceTypeId")){
	 			newDeviceType = softMap.get((String)fields2.get("newDeviceTypeId"));
	 			if(null==newDeviceType)
	 			     newDeviceType="";
	 		}
	 		if (null != fields2.get("oldDeviceTypeId")){
	 			oldDeviceType = softMap.get((String)fields2.get("oldDeviceTypeId"));
	 			if(null==oldDeviceType)
	 				oldDeviceType ="";
	 		}
	 		//策略执行的状态
	 		if(null != stragyStatusCode){
		 		if("0".equals(stragyStatusCode)){
		 			stragyStatusName = "等待执行";
		 		}else if("1".equals(stragyStatusCode)){
		 			stragyStatusName = "预读PVC";
		 		}else if("2".equals(stragyStatusCode)){
		 			stragyStatusName = "预读绑定端口";
		 		}else if("3".equals(stragyStatusCode)){
		 			stragyStatusName = "预读无线";
		 		}else if("4".equals(stragyStatusCode)){
		 			stragyStatusName = "业务下发";
		 		}else if("100".equals(stragyStatusCode)){
		 			stragyStatusName = "执行完成";
		 		}
	 		}else{
	 			stragyStatusName = "";
	 		}
	 		if(null != resultId){
		 		//策略执行的结果
		 		if("1".equals(resultId)){
		 			result = "成功" ;
		 		}else if("0".equals(resultId) || "2".equals(resultId)){
		 			result = "中间状态";
		 		}else if ("3".equals(resultId)){
		 			result = "设备无法连接" ;
		 		}else if ("4".equals(resultId)){
		 			result = "提示：设备未配置iTV有线，故无法继续配置无线" ;
		 		}else {
		 			result = "失败";
		 		}
	 		}else {
	 			result = "";
	 		}
	 	}
	 	if(null != fields3){
	 		if(null != fields3.get("sheet_para")){
	 			sheetParam = (String)fields3.get("sheet_para");
	 		}else{
	 			sheetParam = "未获取到XML信息";
	 		}
	 	}else{
	 		sheetParam = "未获取到XML信息";
	 	}
 	}
 	 /*
	*  added  by zhangsb 2012年6月11日  新疆需求 --设备详细信息展示页面增加软件升级版本信息    end
	*/
 %>
<%@ include file="../head.jsp"%>
<script type="text/javascript" src="../Js/jquery.js"></script>
<script type="text/javascript" src="../Js/commFunction.js"/></script>
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
	
	$(function(){
		<%-- 当页面加载后，如果当前用户有设备详情页面的密码显示权限，则记录超级密码操作日志 --%>
		<ms:hasAuth authCode="ShowDevPwd">
			superAuthLog('ShowDevPwd',
					'查看[<%=device_serialnumber%>]设备的电信维护密码[<%=x_com_passwd%>]');
		</ms:hasAuth>
	});

	function updatePage() {
     	if (request.readyState == 4) {
        if (request.status == 200) {
        	document.all.operation_info.innerHTML = request.responseText;
        } else
            alert("status is " + request.status);
     	}
   }

///////////////////////////////生成密码/////////////////////////////////////////
	//用于用户名检查的ajax
   function send_request_PW(url) {
     request.open("GET", url, true);
     request.onreadystatechange = updatePagePW;
     request.send(null);
   }

	function updatePagePW() {
     	if (request.readyState == 4) {
        if (request.status == 200) {
        	document.all.compw_span_new.innerHTML = "<font color='blue'>"+request.responseText + "</font>";
        	document.all.compw_span_new.value = request.responseText;
        } else
            alert("status is " + request.status);
     	}
   }
    
	function showOperation() {
		<%--
		var obj = document.frm;
		//alert("showOperationInfo.jsp?device_id="+<%=device_id%>);
		//alert("showOperationInfo.jsp?device_id=\""+<%=device_id%>+"\"");--%>
		send_request("showOperationInfo.jsp?device_id="+<%=device_id%>+"&tt="+new Date().getTime());
	}

	function toggleOperation(){
		var btnHTML = $("#his_check").html();
		if(btnHTML == "查看"){
			$("#his_check").html("关闭");
			send_request("showOperationInfo.jsp?device_id="+<%=device_id%>+"&tt="+new Date().getTime());
		}else{
			$("#his_check").html("查看");
			document.all.operation_info.innerHTML = "";
		}
		
	}
	function generateComPw() {

		//$.ajax({
        //    type: "GET", 
        //    url: "GenerateComPw.jsp", 
        //    data: "tt="+new Date().getTime(),
        //    success:
        //            function(data) {
        //                    $("#compw_span_new").html("<font color='blue'>"+data+"</font>");

        //                    alert(data.trim());
        //                    $("input[@name=compw_span_hid]").value = data.trim();
        //                    alert($("input[@name=compw_span_hid]").value);
        //            },
        //    erro:
        //            function(xmlR,msg,other){
        //                   $("#compw_span_new").html("");
        //            }
        //});
	    
		send_request_PW("GenerateComPw.jsp?gw_type="+<%=gw_type%>+"&tt="+new Date().getTime());
	}

///////////////////////////////设置密码/////////////////////////////////////////
	//用于用户名检查的ajax
    function send_request_setPW(url) {
        request.open("GET", url, true);
        request.onreadystatechange = updatePageSetPW;
        request.send(null);
    }

	function updatePageSetPW() {
     	if (request.readyState == 4) {
	        if (request.status == 200) {
	        	closeMsgDlg();
	        	//alert(request.responseText.trim());
				if (request.responseText.trim() == "-2") {
					alert("数据出错，请检查程序！");
					document.all.compw_span_new.innerHTML = "";
				} else if (request.responseText.trim() == "-1") {
					alert("密码设置失败,请确保设备连接正常！");
					document.all.compw_span_new.innerHTML = "";
				} else if(request.responseText.trim() == "0") {
					alert("密码设置成功，但数据库更新失败，请联系管理员！");
				} else if(request.responseText.trim() == "1") {
					alert("密码设置成功！");
					document.all.compw_span.innerHTML = document.all.compw_span_new.value.trim();
		        	document.all.compw_span_new.innerHTML = "";
				} else {
					alert("未知错误！");
					document.all.compw_span_new.innerHTML = "";
				}
				inspireBtns();
	        } else {
	        	closeMsgDlg();
	            alert("出错！(status is " + request.status + ")");
	            inspireBtns();
	        }
     	}
    }

	function setComPw() {
		//alert($("input[@name=compw_span_hid]").val());
		var obj = document.all.compw_span_new;
		if (obj.value == undefined || obj.innerHTML == "") {
			alert(" 请先 “生成” 密码！ ");
		} else {
			showMsgDlg();
			send_request_setPW("SetComPw.jsp?device_id="+<%=device_id%>+"&pwValue="+obj.value.trim()+"&tt="+new Date().getTime());
			disableBtns();
		}
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
		document.all.onlineStatus.innerHTML = "<font color='blue'>正在获取设备与平台交互状态</font>";
		send_request_OS("../paramConfig/testConnectionSubmit.jsp?device_id="+<%=device_id%>+"&tt="+new Date().getTime());
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
	        	document.all.onlineStatus.innerHTML = "<font color='green'>能正常交互(实时)</font>";
	        	document.all.onlineStatus.value = onlineStatus;
        	}else{
        		document.all.onlineStatus.innerHTML = "<font color='red'>不能正常交互(实时)</font>";
	        	document.all.onlineStatus.value = onlineStatus;
        	}
        } else
            alert("status is " + request.status);
     	}
   }
 
   
  //中间件的增加 
function add(){
	$("input[@name='des']").val("add device");
	var url = "<s:url value='/itms/midware/midWare!add.action'/>";
	operate(url);
}

function update(){
	$("input[@name='des']").val("change device info");
	var url = "<s:url value='/itms/midware/midWare!update.action'/>";
	operate(url);
}

function del(){
	$("input[@name='des']").val("delete device");
	var url = "<s:url value='/itms/midware/midWare!delete.action'/>";
	operate(url);
}

function midareOpen(){
	var deviceId = $.trim($("input[@name='deviceId']").val());
	var url = "<s:url value='/itms/midware/businessOpen!midMareDevOpen.action'/>";
	$.post(url,{
		deviceId:deviceId
	},function(ajax){
	    alert(ajax);
	});
}

//中间件操作  
function operate(url){
	var cityId = $.trim($("input[@name='cityId']").val());
	var deviceId = $.trim($("input[@name='deviceId']").val());
	var oui = $.trim($("input[@name='oui']").val());
	var deviceSn = $.trim($("input[@name='deviceSn']").val());
	var deviceModel = $.trim($("input[@name='deviceModel']").val());
	var adNumber = $.trim($("input[@name='adNumber']").val());
	var status = $.trim($("input[@name='status']").val());
	var des = $.trim($("input[@name='des']").val());
	var area = $.trim($("input[@name='area']").val());
	var group = $.trim($("input[@name='group']").val());
	var phone = $.trim($("input[@name='phone']").val());
	$.post(url,{
		cityId:cityId,
		deviceId:deviceId,
		oui:oui,
		deviceSn:deviceSn,
		deviceModel:deviceModel,
		adNumber:adNumber,
		status:status,
		des:des,
		area:area,
		group:group,
		phone:phone
	},function(ajax){	
	    alert(ajax);
	});
}

//中间件业务开通
function business_open(){	
	
	var cityId = $.trim($("input[@name='cityId']").val());
	var deviceId = $.trim($("input[@name='deviceId']").val());
	var oui = $.trim($("input[@name='oui']").val());
	var deviceSn = $.trim($("input[@name='deviceSn']").val());
	var deviceModel = $.trim($("input[@name='deviceModel']").val());
	var adNumber = $.trim($("input[@name='adNumber']").val());
	var status = $.trim($("input[@name='status']").val());
	var des = $.trim($("input[@name='des']").val());
	var area = $.trim($("input[@name='area']").val());
	var group = $.trim($("input[@name='group']").val());
	var phone = $.trim($("input[@name='phone']").val());
	var strpage="<s:url value='/itms/midware/businessOpen.action'/>?"
		+ "&cityId="+cityId
		+ "&deviceModel="+deviceModel
		+ "&deviceId=" + deviceId
		+ "&oui=" + oui
		+ "&deviceSn=" + deviceSn
		+ "&account=" + adNumber
		+ "&username=" + adNumber;
	window.open(strpage,"","left=100,top=150,width=850,height=350,resizable=yes,scrollbars=yes");
}
 
 function GoContent(user_id,gw_type){
 if(gw_type=="2"){
 	var strpage="EGWUserRelatedInfo.jsp?user_id=" + user_id;
 }else{
	var strpage="HGWUserRelatedInfo.jsp?user_id=" + user_id;
 }
	window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}
/**
 *add by zhangsb3 2012年6月11日 start  新疆需求
 */	
function getDeviceStrategyXML(){
	$("#tr_sheet_param").show();
}
function showSoftUpHistoryInfo() {
	var obj = document.frm;
	var url="showSoftUpHistoryInfo.jsp";
	$.post(url,{
		device_id:<%=device_id%>
	},function(ajax){	
	    $("#softUpHistory").html(ajax);
	});
}
/**
 *add by zhangsb3 2012年6月11日 end  新疆需求
 */	
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
					<img src="../images/cursor_hourglas.gif" border="0" WIDTH="30"
						HEIGHT="30">
				</td>
				<td>
					&nbsp;&nbsp;
				</td>
				<td valign="middle">
					<span id=txtLoading style="font-size: 12px; font-family: 宋体">正在设置密码，请稍等…</span>
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
								<TD><%=software_version%>
								<%
								if (null != LipossGlobals.getLipossProperty("InstArea.ShortName")
										&& "jx_dx".equals(LipossGlobals
												.getLipossProperty("InstArea.ShortName")) )
								{
									 if ("1".equals(is_check)){ // 原来是  if ("1".equals(is_normal)){ // chenxj6-20161025
									 %> 
									<font color="green">(正规版本)</font>
								<%} else {%>
									<font color="red">(非正规版本)</font>
								<%}}%>
								</TD>
								<TD class=column align="right">
									设备类型
								</TD>
								<TD><%=device_type%></TD>
							</TR>
						 <%if("xj_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
						 <TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									上行方式
								</TD>
								<TD width=140><%=access_type %></TD>
								<TD class=column align="right">
									版本类型
								</TD>
								<TD><%=deviceTypeName %></TD>
						 </TR>
						 <%} %>
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
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									终端规格
								</TD>
								<TD><%=spec_name%></TD>
								<ms:inArea areaCode="sd_lt" notInMode="false">
								<TD class=column align="right">
								</TD>
								<TD>
								</TD>
								</ms:inArea>
								<ms:inArea areaCode="sd_lt" notInMode="true">
								<TD class=column align="right">
									地址方式
								</TD>
								<TD>
									<%
										String ipType = null;
										//得到用户信息
										cursor = DeviceAct.getCustomerOfDev(device_id, gw_type);
										fields = cursor.getNext();
										if (fields == null){
											ipType = "";
										}else{
											// 只循环一次，就不用while了
											if (fields != null){
												String user_id = (String) fields.get("user_id");
												ipType = HGWUserInfoAct.getTabNetServParamByUserId(user_id);
												
											}
										}
									%>
									<%=ipType %>
								</TD>
								</ms:inArea>
							</TR>
							<%if("gs_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")) || "ah_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
							<TR bgcolor="#FFFFFF" height="20">
							<TD class=column align="right">
									设备版本类型
								</TD>
								<TD><%=device_version_type%></TD>
								<TD class=column align="right"></TD><TD></TD>
							</TR>
							<%} %>
							<%if("xj_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
							<TR bgcolor="#FFFFFF" height="20">
							<TD class=column align="right">
									终端资产
								</TD>
								<TD><%=isTelDev%></TD>
								<TD class=column align="right">状态</TD>
								<TD>
									<%=scrapStatus%>
								</TD>
							</TR>
							<%} %>
							<%if("hb_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
							<TR bgcolor="#FFFFFF" height="20">
							<TD class=column align="right">
									是否支持光猫标识
								</TD>
								<TD><%=gigabitport%></TD>
								<TD class=column align="right"></TD><TD></TD>
							</TR>
							<%} %>
							
							<%if("jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									终端支持速率
								</TD>
								<TD><%=gbbroadband%></TD>

								<TD class=column align="right">
									接入类型
								</TD>
								<TD><%=accessStyle%></TD>
							</TR>
							<%} %>
							
							<%
								if (null != LipossGlobals.getLipossProperty("InstArea.ShortName")
										&& "js_dx".equals(LipossGlobals
												.getLipossProperty("InstArea.ShortName"))
										&& "1".equals(gw_type))
								{
							%>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									是否支持awifi开通
								</TD>
								<TD colspan= 3><%=is_awifi%></TD>
							</TR>
							<%
							}
							%>
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
							<%if(!"3".equals(LipossGlobals.getLipossProperty("ClusterMode.mode"))){ %>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									设备与平台正常交互状态
								</TD>
								<TD colspan="3" width="30%">
									<span id="onlineStatus"><%=online_status%></span>&nbsp;&nbsp;
									<input name="onlineStatusGet" type="button" value="检测交互状态"
										class="btn_g" onclick="getOnlineStatus()">
								</TD>
							</TR>
							<%} %>
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
							<%--				<TR bgcolor="#FFFFFF" height="20">
					<TD class=column align="right" nowrap>软件所支持的业务代码</TD>
					<TD><textarea width=100% cols=40 rows=4 readonly><%=device_support_code%></textarea></TD>
					<TD class=column align="right">当前开通的业务代码</TD>
					<TD><textarea width=100% cols=40 rows=4 readonly><%=device_current_code%></textarea></TD>
				</TR>
		
				<TR bgcolor="#FFFFFF" height="20">
					<TD class=column align="right">设备配置模板</TD>
					<TD ><textarea cols=40 rows=4 readonly><%=deviceModelTemplate%></textarea></TD>
				<TD class=column align="right" nowrap>硬件所支持的业务代码</TD>
					<TD><textarea width=100% cols=40 rows=4 readonly><%=device_hard_code%></textarea></TD>		
				</TR>
--%>
                            <%  if(!LipossGlobals.isXJDX()){%>
                            <!-- 新疆电信不上这个功能 -->
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="center" valign="top" colspan=4>
									<%
										if ("1".equals(gw_type))
										{
									%>
									<%
											if (LipossGlobals.inArea("sd_lt") || LipossGlobals.inArea("sx_lt") || LipossGlobals.inArea("nx_lt"))
											{
									%>
									RMS操作历史信息
									<%
											}
											else
											{
									%>
									ITMS操作历史信息
									<%										
											}
									 %>
									<%
										}
										else
										{
									%>
									BBMS操作历史信息
									<%
									}
									%>
									
									<%if(!LipossGlobals.inArea("nx_lt")){ %>
									〖
									<a href="javascript:showOperation()"
										style="color: 808080; font-size: 9pt;">查看</a>〗
									<%}else{ %>
									〖
									<a href="javascript:toggleOperation()"
										style="color: 808080; font-size: 9pt;" id='his_check'>查看</a>〗
									<%} %>
								</TD>

							</TR>
							<%} %>
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
							<ms:hasAuth authCode="ShowDevPwd">
							<TR bgcolor="#FFFFFF" height="20">
								<%if (LipossGlobals.inArea("sd_lt") || LipossGlobals.inArea("sx_lt") || LipossGlobals.inArea("jl_lt") || LipossGlobals.inArea("ah_lt")) { %>
								<TD class=column align="right">
								联通维护账号
								</TD>
								<%} else if(LipossGlobals.inArea("nx_lt")){ %>
								<TD class=column align="right">
								超级管理员账号
								</TD>
								<%}else{ %>
								<TD class=column align="right">
								电信维护账号
								</TD>
								<%} %>
								<TD><%=x_com_username%></TD>
								<%if (LipossGlobals.inArea("sd_lt") || LipossGlobals.inArea("sx_lt") || LipossGlobals.inArea("jl_lt") || LipossGlobals.inArea("ah_lt")) { %>
								<TD class=column align="right">
								联通维护密码
								</TD>
								<%} else if(LipossGlobals.inArea("nx_lt")){ %>
								<TD class=column align="right">
								超级管理员密码
								</TD>
								<%}else{ %>
								<TD class=column align="right">
								电信维护密码
								</TD>
								<%} %>
								<TD>
									<span id="compw_span"><%=x_com_passwd%></span>&nbsp;
									<span id="compw_span_new"></span>&nbsp;
									<!-- <input name="compw_span_hid" value="" type="hidden"/> -->
									<input name="compwGen" type="button" value="生成" class="btn_g"
										onclick="generateComPw()">
									<input name="compwSet" type="button" value="设置" class="btn_g"
										onclick="setComPw()">
								</TD>
							</TR>
							</ms:hasAuth>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="center" colspan=4>
									用户基本信息
								</TD>
							</TR>
							<%
								//得到用户信息
								cursor = DeviceAct.getCustomerOfDev(device_id, gw_type);
								fields = cursor.getNext();
								if (fields == null)
								{
							%>
							<TR bgcolor="#FFFFFF" height="20">
								<TD align="center" colspan=4>
									设备暂无用户.
								</TD>
							</TR>
							<%}else{
								while (fields != null)
								{
									username = (String) fields.get("username");
									service_name = (String) fields.get("service_name");
									String user_id = (String) fields.get("user_id");
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
							<%}%>
								<%  if(LipossGlobals.getMidWare()){%>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="center" colspan=4>
									中间件设备操作
								</TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="center" colspan="4">
								<input type="hidden" name="status" value="1">
								<input type="hidden" name="area" value="">
								<input type="hidden" name="group" value="">
								<input type="hidden" name="phone" value="">
								<input type="hidden" name="des" value="">
								<input type="button" value="新 增" class="btn_g"
										onclick="add();">
										&nbsp;&nbsp;&nbsp;
								<input type="button" value="更 新" class="btn_g"
										onclick="update();">
										&nbsp;&nbsp;&nbsp;
								<input type="button" value="删 除" class="btn_g"
										onclick="del();">
										&nbsp;&nbsp;&nbsp;
								<input type="button" value="开启中间件" class="btn_g"
										onclick="midareOpen();">
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="button" value="业务操作" class="btn_g"
										onclick="business_open();">
								</TD>
							</TR>
							<% }%>
							
							<%}
								if (!"1".equals(gw_type))
								{
									//得到用户信息
									String customer_name;
									String customer_pwd;
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
										fields = cursor.getNext();
							%>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									客户名称
								</TD>
								<TD><%=customer_name%></TD>
								<TD class=column align="right">
									客户密码
								</TD>
								<TD><%=customer_pwd%></TD>
							</TR>
							
							<%
								}
								}
							%>
							<% if(LipossGlobals.isXJDX()){%>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="center" colspan=4>
									当前设备软件升级策略信息  <input type="button" value="查看策略XML" class="btn_g"
										onclick="getDeviceStrategyXML();">
								</TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									设备老版本
								</TD>
								<TD><%=oldDeviceType%></TD>
								<TD class=column align="right">
									设备目标版本
								</TD>
								<TD><%=newDeviceType%></TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									策略执行状态
								</TD>
								<TD><%=stragyStatusName%></TD>
								<TD class=column align="right">
									执行结果
								</TD>
								<TD><%=result%></TD>
							</TR>
							<TR id ="tr_sheet_param" bgcolor="#FFFFFF" height="20" style="display: none">
								<TD class=column width="15%" align='right'>
									策略参数信息
								</TD>
								<TD width="85%" colspan="3" >
									<%=sheetParam %>
								</TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="center" valign="top" colspan=4>
									设备版本变化历史记录
									〖
									<a href="javascript:showSoftUpHistoryInfo()"
										style="color: 808080; font-size: 9pt;">查看</a>〗
								</TD>
							</TR>
							<TR>
								<TD class=column align="center" colspan=4>
									<div id="softUpHistory" style="width: 100%; height: 100%; z-index: 1; top: 100%;">
									</div>
								</TD>
							</TR>
							<%} %>
							<TR>
								<TD colspan="4" align="center" class=foot>
									<INPUT TYPE="submit" value=" 关 闭 " class=jianbian
										onclick="javascript:window.close();">
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