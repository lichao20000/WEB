<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.Cursor"%>
<%@ page import="com.linkage.litms.common.database.DataSetBean"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Date"%>
<%@ page
	import="com.linkage.litms.common.util.*,com.linkage.litms.LipossGlobals"%>
<%--
	zhaixf(3412) 2008-05-08
	req:XJDX_ITMS-BUG-20080506-XXF-001
--%>
<%--
	zhaixf(3412) 2008-04-11
	req:XJDX_ITMS-REQ-20080411-CZM-001
--%>
<jsp:useBean id="HGWUserInfoAct" scope="request"
	class="com.linkage.litms.resource.HGWUserInfoAct" />
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />
<jsp:useBean id="ServiceAct" scope="request"
	class="com.linkage.litms.resource.ServiceAct" />
<SCRIPT LANGUAGE="JavaScript">
var m_bShow_pe; 
var m_bShow_ce;
var selected_value;
var selected_value_ce;
var time = new Date();
</SCRIPT>

<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>

<%
	request.setCharacterEncoding("GBK");

	//用于区分网关类型和操作类型：1为家庭网关，2为企业网关；1为增加(用户增加菜单)，2为编辑(用户列表链接)
	//11,12,21,22 分为四类
	String gwOptType = request.getParameter("gwOptType");

	//*************************当操作类型是“编辑”的时候要取出相应用户信息*******************************************************

	String tabName = "tab_hgwcustomer";
	String username = "";
	String passwd = "";
	String cust_type_id = "";
	String user_type_id = "";
	String linkman = "";
	String linkphone = "";
	String linkaddress = "";
	String access_style_id = "";
	String realname = "";
	String sex = "";
	String cred_type_id = "";
	String credno = "";

	//静态IP方式上网时
	String ipaddress = "";
	String ipmask = "";
	String gateway = "";
	String adsl_ser = "";

	String mobile = "";
	String email = "";
	String vlanid = "";
	String user_state = "";
	String staff_id = "";
	String vpiid = "";
	String vciid = "";
	String opmode = "";
	String phonenumber = "";

	String selectCityId = "";
	String selectOffId = "";
	String selectZoneId = "";
	String selectServTypeId = "";

	String max_user_number = "";
	String wan_type = "1";
	String oui = "";
	String device_serialnumber = "";
	Date curDtae = new Date();

	String hidOpendate = "";
	String hidOnlinedate = "";
	String hidPausedate = "";
	String hidClosedate = "";
	String hidUpdatetime = "";
	String hidDealdate = "";

	String opendate = "";
	String onlinedate = "";
	String pausedate = "";
	String closedate = "";
	String updatetime = "";
	String dealdate = "";

	String openTime = "";
	String onlineTime = "";
	String closeTime = "";
	String pauseTime = "";
	String upTime = "";
	String time = "";

	String user_id = "";
	String gather_id = "";
	String device_id = "";

	//EGW专有字段
	String EGWUsername = "";
	String EGWPasswd = "";
	String EGWid = "";
	//上网方式选择
	String select1 = "";
	String select2 = "";
	String select3 = "";
	String select4 = "";

	Map fields = null;

	if ("12".equals(gwOptType) || "22".equals(gwOptType)) {

		user_id = request.getParameter("user_id");
		gather_id = request.getParameter("gather_id");

		if ("22".equals(gwOptType))
			tabName = "tab_egwcustomer";

		String strSQL = "select * from " + tabName + " where user_id="
		+ user_id + " and gather_id='" + gather_id + "'";


		fields = DataSetBean.getRecord(strSQL);

		if (fields != null) {

			hidOpendate = (String) fields.get("opendate");
			hidOnlinedate = (String) fields.get("onlinedate");
			hidPausedate = (String) fields.get("pausedate");
			hidClosedate = (String) fields.get("closedate");
			//hidUpdatetime = (String) fields.get("updatetime");
			hidDealdate = (String) fields.get("dealdate");

			if ((hidOpendate != null) && (hidOpendate.length() != 0)
			&& (!"0".equals(hidOpendate))) {
		opendate = new DateTimeUtil(
				Long.parseLong(hidOpendate) * 1000).getDate();
		openTime = new DateTimeUtil(
				Long.parseLong(hidOpendate) * 1000).getTime();
			}
			if ((hidOnlinedate != null)
			&& (hidOnlinedate.length() != 0)
			&& (!"0".equals(hidOnlinedate))) {
		onlinedate = new DateTimeUtil(Long
				.parseLong(hidOnlinedate) * 1000).getDate();
		onlineTime = new DateTimeUtil(Long
				.parseLong(hidOnlinedate) * 1000).getTime();
			}
			if ((hidPausedate != null) && (hidPausedate.length() != 0)
			&& (!"0".equals(hidPausedate))) {
		pausedate = new DateTimeUtil(Long
				.parseLong(hidPausedate) * 1000).getDate();
		pauseTime = new DateTimeUtil(Long
				.parseLong(hidPausedate) * 1000).getTime();
			}
			if ((hidClosedate != null) && (hidClosedate.length() != 0)
			&& (!"0".equals(hidClosedate))) {
		closedate = new DateTimeUtil(Long
				.parseLong(hidClosedate) * 1000).getDate();
		closeTime = new DateTimeUtil(Long
				.parseLong(hidClosedate) * 1000).getTime();
			}
			/**		if ((hidUpdatetime != null) && (hidUpdatetime.length() != 0)
			 && (!"0".equals(hidUpdatetime))) {
			 updatetime = new DateTimeUtil(Long
			 .parseLong(hidUpdatetime) * 1000).getDate();
			 upTime = new DateTimeUtil(Long
			 .parseLong(hidUpdatetime) * 1000).getTime();
			 }*/
			if ((hidDealdate != null) && (hidDealdate.length() != 0)
			&& (!"0".equals(hidDealdate))) {
		dealdate = new DateTimeUtil(
				Long.parseLong(hidDealdate) * 1000).getDate();
		time = new DateTimeUtil(
				Long.parseLong(hidDealdate) * 1000).getTime();
			}

			device_id = (String) fields.get("device_id".toLowerCase());
			username = (String) fields.get("username".toLowerCase());
			passwd = (String) fields.get("passwd".toLowerCase());
			cust_type_id = (String) fields.get("cust_type_id"
			.toLowerCase());
			user_type_id = (String) fields.get("user_type_id"
			.toLowerCase());
			access_style_id = (String) fields.get("access_style_id"
			.toLowerCase());
			realname = (String) fields.get("realname".toLowerCase());
			sex = (String) fields.get("sex".toLowerCase());
			cred_type_id = (String) fields.get("cred_type_id"
			.toLowerCase());
			credno = (String) fields.get("credno".toLowerCase());
			mobile = (String) fields.get("mobile".toLowerCase());
			email = (String) fields.get("email".toLowerCase());
			vlanid = (String) fields.get("vlanid".toLowerCase());
			user_state = (String) fields
			.get("user_state".toLowerCase());
			vpiid = (String) fields.get("vpiid".toLowerCase());
			vciid = (String) fields.get("vciid".toLowerCase());

			opmode = (String) fields.get("opmode".toLowerCase());
			max_user_number = (String) fields.get("max_user_number"
			.toLowerCase());
			wan_type = (String) fields.get("wan_type".toLowerCase());
			if (wan_type.equals("1")||wan_type.equals("5")) {
		select1 = "selected";
			} else if (wan_type.equals("2")||wan_type.equals("6")) {
		select2 = "selected";
			} else if (wan_type.equals("3")||wan_type.equals("7")) {
		select3 = "selected";
			} else if (wan_type.equals("4")||wan_type.equals("8")) {
		select4 = "selected";
			}
			oui = (String) fields.get("oui".toLowerCase());
			device_serialnumber = (String) fields
			.get("device_serialnumber".toLowerCase());
			device_serialnumber = device_serialnumber
			.substring(device_serialnumber.indexOf("-") + 1);
			linkman = (String) fields.get("linkman".toLowerCase());
			linkphone = (String) fields.get("linkphone".toLowerCase());
			linkaddress = (String) fields.get("linkaddress"
			.toLowerCase());

			ipaddress = (String) fields.get("ipaddress".toLowerCase());
			ipmask = (String) fields.get("ipmask".toLowerCase());
			gateway = (String) fields.get("gateway".toLowerCase());
			adsl_ser = (String) fields.get("adsl_ser".toLowerCase());//DNS地址

			staff_id = (String) fields.get("staff_id".toLowerCase());
			phonenumber = (String) fields.get("phonenumber");

			selectCityId = (String) fields.get("city_id");
			selectOffId = (String) fields.get("office_id");
			selectZoneId = (String) fields.get("zone_id");
			selectServTypeId = (String) fields.get("serv_type_id");

			//EGW专有字段
			EGWUsername = (String) fields.get("e_id");
			EGWPasswd = (String) fields.get("e_username");
			EGWid = (String) fields.get("e_passwd");
		}
	} else {
		//添加用户时时间的默认值为当前时间
		curDtae = new Date();

		opendate = new DateTimeUtil(curDtae.getTime()).getDate();
		openTime = new DateTimeUtil(curDtae.getTime()).getTime();

		onlinedate = new DateTimeUtil(curDtae.getTime()).getDate();
		onlineTime = new DateTimeUtil(curDtae.getTime()).getTime();

		dealdate = new DateTimeUtil(curDtae.getTime()).getDate();
		time = new DateTimeUtil(curDtae.getTime()).getTime();
		vpiid = "8";
		vciid = "35";
	}
	updatetime = new DateTimeUtil(curDtae.getTime()).getDate();
	upTime = new DateTimeUtil(curDtae.getTime()).getTime();

	//×××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××//
	//员工代码默认为当前用户账号
	if (staff_id == null || "".equals(staff_id)) {
		staff_id = String.valueOf(curUser.getUser().getId());
	}

	//**************下拉列表展示，如果没有值显示空，如果有值则显示该值****************************************
	//获取页面属地的下拉列表，包括本属地及子属地
	String curCity_ID = curUser.getCityId();
	String strCityList = "";
	if (selectCityId == null || "".equals(selectCityId)) {
		selectCityId = curCity_ID;
	}
	if (LipossGlobals.isXJDX()) {
		strCityList = HGWUserInfoAct.getCityListByuser(false,
		selectCityId, "", request);
	} else {
		strCityList = HGWUserInfoAct.getCityListSelf(false,
		selectCityId, "", request);
	}

	//获取业务类型下拉列表
	String servTypeList = "";
	if ("11".equals(gwOptType)) {
		servTypeList = HGWUserInfoAct.getServTypeNameList(1,
		selectServTypeId);
	} else if ("21".equals(gwOptType)) {
		servTypeList = HGWUserInfoAct.getServTypeNameList(5,
		selectServTypeId);
	} else {
		//当以编辑状态进入该页面时，业务类型不可编辑，显示列表唯一
		servTypeList = HGWUserInfoAct
		.getServTypeNameList(selectServTypeId);
	}
	//获取局向下拉列表
	String strOfficeList = "";
	if (selectOffId == null || "".equals(selectOffId)) {
		strOfficeList = HGWUserInfoAct.getOfficeList(false, "", "");
	} else {
		strOfficeList = HGWUserInfoAct.getOfficeList(false,
		selectOffId, "");
	}

	//获取小区下拉列表
	String strZoneList = "";
	if (selectZoneId == null || "".equals(selectZoneId)) {
		strZoneList = HGWUserInfoAct.getZoneList(false, "", "");
	} else {
		strZoneList = HGWUserInfoAct.getZoneList(false, selectZoneId,
		"");
	}

	//************************************************************************************************//
%>

<SCRIPT LANGUAGE="JavaScript">
<!--
	//var usernameflag = 1;

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
        document.all.isOK.innerHTML = request.responseText;
       } else
         alert("status is " + request.status);
     }
   }
//用于设备检查的ajax
   function send_requestD(url) {
     request.open("GET", url, true);
     request.onreadystatechange = updatePageD;
     request.send(null);
   }

	function updatePageD() {
     if (request.readyState == 4) {
       if (request.status == 200) {
        document.all.isOKD.innerHTML = request.responseText;
       } else
         alert("status is " + request.status);
     }
   }


	function CheckForm() {
		var obj = document.frm;
		var gwOptType = obj.gwOptType.value;
		
		//如果用户名已存在则不能添加
		 //if(usernameflag == 0){
		 //	alert("该用户名已存在");
		 //	return false;
		 //}
		if(document.all.isOK.innerHTML != ""){
			alert("该用户名已被注册该业务,不能提交！");
			obj.username.focus();
			return false;
		}
		
		if(!IsNull(obj.username.value,"用户帐户")){
			obj.username.focus();
			obj.username.select();
			return false;
		} else if (obj.some_service.value == "-1") {
			alert("请选择业务类型！");
			return false;
		} else if (obj.wan_type.value == "-1") {
			alert("请选择上网类型！");
			return false;
		} else if((obj.wan_type.value == "3" || obj.wan_type.value == "7" ) && !IsIPAddr(obj.ipaddress.value)
					&& !IsIPAddr(obj.ipmask.value) && !IsIPAddr(obj.adsl_ser.value)){
			//当选择静态IP方式时，检查IP地址
			return false;
		}else if(("1"==obj.wan_type.value || "2"==obj.wan_type.value ||"5"==obj.wan_type.value ||"6"==obj.wan_type.value) && !IsNull(obj.passwd.value,"用户密码")){
			obj.passwd.focus();
			obj.passwd.select();
			return false;
		} else if(("1"==obj.wan_type.value || "2"==obj.wan_type.value ||"5"==obj.wan_type.value ||"6"==obj.wan_type.value) && !IsNull(obj.validate_passwd.value,"密码验证")){
			obj.validate_passwd.focus();
			obj.validate_passwd.select();
			return false;
		} else if(obj.validate_passwd.value != obj.passwd.value){
			alert("密码和验证密码不一致!");
			obj.validate_passwd.focus();
			obj.validate_passwd.select();
			return false;
		}else if(obj.access_style_id.value==0 && obj.vpiid.value == ""){
			 alert("请输入VPI");
		     obj.vpiid.focus();
		     return false;
		}else if(obj.access_style_id.value==0 && obj.vciid.value == ""){
			 alert("请输入VCI");
		     obj.vciid.focus();
		     return false;
		} else if(obj.access_style_id.value ==0 && !IsNumber(obj.vciid.value,"VCI")){
			//alert("VCI号应为数字");
			obj.vciid.focus();
			obj.vciid.select();
			return false;
		} else if(obj.access_style_id.value ==0 && !IsNumber(obj.vpiid.value,"VPI")){
			//alert("VPI号应为数字");
			obj.vpiid.focus();
			obj.vpiid.select();
			return false;
		}else if(obj.vlanid.value!="" && Trim(obj.vlanid.value)==""){
			alert("VlanID号应为数字");
			obj.vlanid.focus();
			obj.vlanid.select();
			return false;
		}else if(obj.vlanid.value!="" && !IsNumber(obj.vlanid.value,"VlanID号")){		
			obj.vlanid.focus();
			obj.vlanid.select();
			return false;
		}else if(obj.max_user_number.value!="" && Trim(obj.max_user_number.value)==""){
			alert("允许用户上网数应为数字");
			obj.max_user_number.focus();
			obj.max_user_number.select();
			return false;
		} else if(gwOptType == "11" || gwOptType == "21"){
			if (obj.device_id == null || obj.device_id.value == ""){
				if (!confirm("网关设备不存在，是否继续保存")){
					//alert("操作取消!");
					obj.oui.focus();
					return false;
				}
			}
		}
			//开户时间
			obj.hidOpenDate.value = DateToDes_long(obj.opendate.value, obj.openTime.value);
			//开通时间
			obj.hidOnlineDate.value = DateToDes_long(obj.onlinedate.value, obj.onlineTime.value);
			//暂停时间
			obj.hidPauseDate.value = DateToDes_long(obj.pausedate.value, obj.pauseTime.value);
			//销户时间
			obj.hidCloseDate.value = DateToDes_long(obj.closedate.value, obj.closeTime.value);
			//updatetime为更新日期
			obj.hidUpdateTime.value = DateToDes_long(obj.updatetime.value, obj.upTime.value);
			//工单受理时间
			obj.hidDealDate.value = DateToDes_long(obj.dealdate.value,obj.time.value);
			obj.some_device.value = obj.oui.value + '/' + obj.vender.value;
			//return true;

		//该页面返回device_id的值
//		document.all("childFrm1").src = "checkDevice.jsp?oui="+obj.oui.value
//								+"&vender="+obj.vender.value
//								+"&gwOptType="+gwOptType
//								+"&username="+obj.username.value;
		
		//javabean中需要此字段来判断操作方式
		if(gwOptType == "12" || gwOptType == "22"){
			obj.action.value = "update";
		}else{
			obj.action.value = "add";
			//默认为“手工添加”
			obj.user_type_id.value = "3";
		}
		obj.submit();
	}
	
	function SearchDevice() {
		var obj = document.frm;
		send_requestD("checkDevice.jsp?oui="+obj.oui.value
							+"&vender="+obj.vender.value
							+"&gwOptType="+obj.gwOptType.value
							+"&username="+obj.username.value
							+"&time="+new Date());
	}
	
	function DateToDes(v){
	if(v != ""){
		pos = v.indexOf("-");
		if(pos != -1 && pos == 4){
			y = parseInt(v.substring(0,pos));
			v = v.substring(pos+1,v.length);
		}
		pos = v.indexOf("-");
		if(pos != -1){
			m = parseInt(v.substring(0,pos));
			v = v.substring(pos+1,v.length);
		}
		if(v.length>0)
			d = parseInt(v);

		dt = new Date(m+"/"+d+"/"+y);
		var s  = dt.getTime();
		return s/1000;
	}
	else
		return 0;
}
//add by benyp
function DateToDes_long(v,t){
   
	if(v != ""){
	  if(t != ""){
	  //yyyy-mm-dd
		pos = v.indexOf("-");
		if(pos != -1 && pos == 4){
			y = parseInt(v.substring(0,pos));
			v = v.substring(pos+1,v.length);
		}
		pos = v.indexOf("-");
		if(pos != -1){
			m = parseInt(v.substring(0,pos));
			v = v.substring(pos+1,v.length);
		}
		if(v.length>0)
			d = parseInt(v);
	//hh:mm:ss	

	    h = parseInt(t.substring(0,2));
	    r = parseInt(t.substring(3,5));
	    s = parseInt(t.substring(6,8));
	
		dt = new Date(y,m-1,d,h,r,s);	
		var q  = dt.getTime()/1000;
		return q;
		}
		
		
		else{
		
		pos = v.indexOf("-");
		if(pos != -1 && pos == 4){
			y = parseInt(v.substring(0,pos));
			v = v.substring(pos+1,v.length);
		}
		pos = v.indexOf("-");
		if(pos != -1){
			m = parseInt(v.substring(0,pos));
			v = v.substring(pos+1,v.length);
		}
		if(v.length>0)
			d = parseInt(v);

		dt = new Date(m+"/"+d+"/"+y);
		var s  = dt.getTime();
		return s/1000;
		}
	}
	else
		return 0;
}

	function SearchUName(time) {
		var f = document.frm;
		var username = f.username.value;
		var gwOptType = f.gwOptType.value;
		var user_id = f.user_id.value;
		var some_service = f.some_service.value;
		if (username == "")	{
			//if(gwOptType=="11" || gwOptType == "21")
			//	usernameflag = 0;
			f.username.focus();
			return false;
		} else {
			send_request('checkUserName.jsp?username='+username
						+'&gwOptType='+gwOptType
						+'&user_id='+user_id
						+'&time='+time
						+'&some_service='+some_service);
		}
	}
	//这里s为业务类型
function showVal(s){
	SearchUName(new Date());

	wanTp(s.value,document.frm.access_style_id.value);
}

function wanTp(v,x){
	var ss = "<SELECT name='wan_type' class=bk onchange = 'ShowIPAddr(this)'>";
	if(x=="0"){
		ss += "<option value='-1'>==请选择==</option>";
		ss += "<option value='1' <%=select1%>>桥接</option>";
		ss += "<option value='2' <%=select2%>>路由</option>";
		if(v == "" || v == "10" || v == "50"){
			//根据业务类型选择上网方式
			ss += "<option value='3' <%=select3%>>静态IP</option>";
		}
		ss += "<option value='4' <%=select4%>>DHCP</option>";
		ss += "</SELECT>&nbsp;<font color='red'>*</font>&nbsp;&nbsp;";
	}else{
		ss += "<option value='-1'>==请选择==</option>";
		ss += "<option value='5' <%=select1%>>桥接</option>";
		ss += "<option value='6' <%=select2%>>路由</option>";
		if(v == "" || v == "10" || v == "50"){
			//根据业务类型选择上网方式
			ss += "<option value='7' <%=select3%>>静态IP</option>";
		}
		ss += "<option value='8' <%=select4%>>DHCP</option>";
		ss += "</SELECT>&nbsp;<font color='red'>*</font>&nbsp;&nbsp;";
	}
	typeSpan.innerHTML = ss;
}

//当选择静态 IP方式时，要求输入IP地址
function ShowIPAddr(ths){
	//alert(ths.value);
	var wan_type = ths.value;
	if(wan_type == "3" || wan_type == "7"){
		ipaddrSpan.style.display = "";
		ipaddrSpan2.style.display = "";
		ipaddrSpan3.style.display = "none";
	}else if(wan_type == "4" || wan_type == "8"){
		ipaddrSpan.style.display = "none";
		ipaddrSpan2.style.display = "none";
		ipaddrSpan3.style.display = "none";
	}else{
		ipaddrSpan.style.display = "none";
		ipaddrSpan2.style.display = "none";
		ipaddrSpan3.style.display = "";
	}
}

function EC(leaf,obj){
	pobj = obj.offsetParent;
	oTRs = pobj.getElementsByTagName("TR");
	var m_bShow; 
	for(var i=0; i<oTRs.length; i++){
		if(oTRs[i].leaf == leaf){
			m_bShow = (oTRs[i].style.display=="none");
			oTRs[i].style.display = m_bShow?"":"none";
		}
	}
	sobj = obj.getElementsByTagName("IMG");
	if(m_bShow) {
		sobj[0].src = "images/up_enabled.gif";
		obj.className="yellow_title";
	}
	else{
		sobj[0].src = "images/down_enabled.gif";
		obj.className="green_title";
	}
}

function selDev(time)
{
	window.open("./selectDevice.jsp?time="+Math.random(),"win","height:480,width:650");
	
	//设备序列号至少为5位
/**
	var gwOptType = document.frm.gwOptType.value;
	var venderLen = document.frm.vender.value;
	
	if(venderLen.length < 5){
		alert("为了保证您查询结果的有效性，请输入至少5位的设备序列号");
		document.frm.vender.focus();
		return false;
	}
	
	if(gwOptType == "11" || gwOptType == "12"){
			var retValue = window.showModalDialog("./selDeviceList.jsp?oui="
	        +document.frm.oui.value+"&vender="+document.frm.vender.value 
	        + "&gw_type=1&time="+time,window,"dialogHeight:600px;dialogWidth:800px");
	}else if(gwOptType == "21" || gwOptType == "22"){
			var retValue = window.showModalDialog("./selDeviceList.jsp?oui="
	        +document.frm.oui.value+"&vender="+document.frm.vender.value 
	        + "&gw_type=2&time="+time,window,"dialogHeight:600px;dialogWidth:800px");
	}

	var retValue = window.showModalDialog("./selectDevice.jsp?time="+Math.random(),window,"dialogHeight:600px;dialogWidth:800px");

	document.frm.vender.focus();
	document.frm.vlanid.focus();
	if (typeof(retValue) != 'undefined'){
		document.frm.some_device.value = retValue;
		var temp = retValue.split('/');
		document.frm.oui.value = temp[0];
		document.frm.vender.value = temp[1];
		document.frm.device_id.value = temp[2];
	}
	**/
}

function clearDev(){
	var frm = document.frm;
	frm.vender.value = "";
	frm.oui.value = "";
}

//-->
</script>

<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" id="myTable">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<FORM NAME="frm" METHOD="post" action="GwUserInfoSave.jsp"><input
			type="hidden" name="gwOptType" value="<%= gwOptType %>"> <input
			type="hidden" name="device_id" value="<%=device_id%>"> <input
			type="hidden" name="user_id" value="<%= user_id%>"> <input
			type="hidden" name="user_type_id" value="<%= user_type_id%>">
		<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
			<tr>
				<td>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">用户资源</td>
						<td><img src="../images/attention_2.gif" width="15"
							height="12"> 带' <font color="#FF0000">*</font>'的表单必须填写或选择</td>
					</tr>
				</table>
				</td>
			</tr>

			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH colspan="4" align="center">
						<%
							//用于显示抬头标题
							String title = "";
							if ("21".equals(gwOptType)) {
								title = "企业网关用户增加";
							} else if ("22".equals(gwOptType)) {
								title = "企业网关用户编辑(" + username + "-" + realname + ")";
							} else if ("12".equals(gwOptType)) {
								title = "用户编辑(" + username + "-" + realname + ")";
							} else {
								title = "用户增加";
							}
						%> <%=title%></TH>
					</TR>
					<TR>
						<TD colspan="4" align="center" class=column>必填区</TD>
					</TR>

					<TR bgcolor="#FFFFFF">

						<TD class=column align="right" nowrap>用户帐户</TD>
						<TD>
						<%
						if ("12".equals(gwOptType) || "22".equals(gwOptType)) {
						%> <INPUT TYPE="text" NAME="username" maxlength=50 class=bk
							value="<%=username%>" readonly='readonly'> <%
 } else {
 %> <INPUT TYPE="text" NAME="username" maxlength=50 class=bk
							value="<%=username%>" onChange="SearchUName(new Date())">
						<%
						}
						%> &nbsp; <font color="#FF0000">*宽带帐号</font> <span id="isOK"></span>
						</TD>
						<TD class=column align="right">接入方式标识</TD>
						<TD><SELECT name="access_style_id" onChange="access_style_id_onchange()" class=bk>
							<option value="0" <% if ("0".equals(access_style_id)) {%>
								selected <%}%>>ADSL接入</option>
							<option value="1" <% if ("1".equals(access_style_id)) {%>
								selected <%}%>>LAN接入</option>
							<option value="2" <% if ("2".equals(access_style_id)) {%>
								selected <%}%>>光纤接入</option>
						</SELECT> &nbsp;</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id=tdiid >
						<TD class=column align="right" nowrap>VPI/VCI</TD>

						<TD colspan="3"><INPUT TYPE="text" NAME="vpiid" value="<%=vpiid%>" size=3
							class=bk>/<INPUT TYPE="text" NAME="vciid"
							value="<%=vciid%>" size=3 class=bk> <font color="#FF0000">*</font>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id=tdvlanid style=display:none>
						<TD class=column align="right" >VlanID号</TD>
						<TD colspan="3"><INPUT TYPE="text" NAME="vlanid" class=bk
							value="<%= vlanid%>"> &nbsp;</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>业务类型</TD>
						<TD><%=servTypeList%> &nbsp; <font color="red">*</font>&nbsp;&nbsp;
						</TD>
						<TD class=column align="right" nowrap>上网类型</TD>
						<TD id='typeSpan'><SELECT name='wan_type' class=bk
							onchange='ShowIPAddr(this)'>
							<option value="-1">==请选择==</option>
							<option value='1' <%=select1%>>桥接</option>
							<option value='2' <%=select2%>>路由</option>
							<option value='3' <%=select3%>>静态IP</option>
							<option value='4' <%=select4%>>DHCP</option>
						</SELECT>&nbsp;<font color='red'>*</font> &nbsp;&nbsp;</TD>
					</TR>
					<TR id="ipaddrSpan" bgcolor="#FFFFFF" style="display:none">
						<TD class=column align='right' nowrap>IP地址</TD>
						<TD><input type='text' name='ipaddress' class=bk
							value=<%= ipaddress%>>&nbsp; <font color='#FF0000'>*</font></TD>
						<TD class=column align='right' nowrap>掩码</TD>
						<TD><INPUT TYPE='text' NAME='ipmask' class=bk
							value='<%=ipmask%>'> &nbsp;<font color='#FF0000'>*</font></TD>
					</TR>
					<TR id="ipaddrSpan2" bgcolor="#FFFFFF" style="display:none">
						<TD class=column align='right' nowrap>网关</TD>
						<TD><input type='text' name='gateway' class=bk
							value=<%= gateway%>>&nbsp; <font color='#FF0000'>*</font></TD>
						<TD class=column align='right' nowrap>DNS地址</TD>
						<TD><INPUT TYPE='text' NAME='adsl_ser' class=bk
							value='<%=adsl_ser%>'> &nbsp;<font color='#FF0000'>*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="ipaddrSpan3">
						<TD class=column align="right" nowrap>用户密码</TD>
						<TD><INPUT TYPE="password" NAME="passwd" maxlength=50
							class=bk value="<%=passwd%>"> &nbsp; <font
							color="#FF0000">*</font></TD>
						<TD class=column align="right" nowrap>密码验证</TD>
						<TD><INPUT TYPE="password" NAME="validate_passwd"
							maxlength=50 class=bk value="<%=passwd%>"> &nbsp; <font
							color="#FF0000">*</font></TD>
					</TR>


					<TR class="green_title" onclick="EC('suggestedContent',this);">
						<TD colspan="4">
						<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
							<TR class=column>
								<TD><font size="2">建议填写区</font></TD>
								<TD align="right"><IMG SRC="images/up_enabled.gif"
									WIDTH="7" HEIGHT="9" BORDER="0" ALT="">&nbsp;</TD>
							</TR>
						</TABLE>
						</TD>
					</TR>

					<TR bgcolor="#FFFFFF" leaf="suggestedContent">
						<TD class=column align="right">网关设备 <input type="hidden"
							name="some_device" value=""></TD>
						<TD colspan=3><input type="text" name="oui" class=bk size="6"
							value="<%= oui%>" readonly> &nbsp; －&nbsp; <input
							type="text" name="vender" class=bk size="35"
							value="<%=device_serialnumber%>" readonly> &nbsp; <IMG
							onClick="selDev(new Date())" SRC="../images/search.gif"
							WIDTH="15" HEIGHT="12" BORDER="0" ALT="选择"> &nbsp;点击选择设备 <span
							id="isOKD"></span>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a
							href="javascript:clearDev()">清空设备</a></TD>
					</TR>
					<TR bgcolor="#FFFFFF" leaf="suggestedContent">

						<TD class=column align="right">属地编码</TD>
						<TD><%=strCityList%> &nbsp;</TD>
						<TD class=column align="right">局向标识</TD>
						<TD><%=strOfficeList%> &nbsp;</TD>
					</TR>

					<TR bgcolor="#FFFFFF" leaf="suggestedContent">
						<TD class=column align="right">小区标识</TD>
						<TD><%=strZoneList%> &nbsp;</TD>
						<TD class=column align="right">绑定电话</TD>
						<TD ><INPUT TYPE="text" NAME="phonenumber"
							maxlength=15 class=bk value="<%=phonenumber %>"></TD>
					</TR>

					<TR class="green_title" onclick="EC('optionalContents',this);">
						<TD colspan="4">
						<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
							<TR>
								<TD><font size="2">选择填写区</font></TD>
								<TD align="right"><IMG SRC="images/up_enabled.gif"
									WIDTH="7" HEIGHT="9" BORDER="0" ALT="">&nbsp;</TD>
							</TR>
						</TABLE>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" leaf="optionalContents">
						<TD class=column align="right">用户实名</TD>
						<TD><INPUT TYPE="text" NAME="realname" maxlength=20 class=bk
							value="<%=realname%>"> &nbsp;</TD>
						<TD class=column align="right">性别</TD>
						<TD><SELECT NAME="sex" class=bk>
							<option value="男" <% if (sex.equals("0")) {%> selected <%}%>>
							男</option>
							<option value="女" <% if (sex.equals("1")) {%> selected <%}%>>
							女</option>
						</SELECT></TD>
					</TR>
					<TR bgcolor="#FFFFFF" leaf="optionalContents">
						<TD class=column align="right">联系人</TD>
						<TD><INPUT TYPE="text" NAME="linkman" class=bk
							value="<%=linkman%>"> &nbsp;</TD>


						<TD class=column align="right">联系电话</TD>
						<TD><INPUT TYPE="text" NAME="linkphone" class=bk
							value="<%=linkphone%>"> &nbsp;</TD>
					</TR>
					<TR bgcolor="#FFFFFF" leaf="optionalContents">
						<TD class=column align="right">联系人手机</TD>
						<TD><INPUT TYPE="text" NAME="mobile" class=bk
							value="<%=mobile%>"> &nbsp;</TD>
						<TD class=column align="right">联系人email</TD>
						<TD><INPUT TYPE="text" NAME="email" class=bk
							value="<%=email%>"> &nbsp;</TD>
					</TR>
					<TR bgcolor="#FFFFFF" leaf="optionalContents">
						<TD class=column align="right">联系人地址</TD>
						<TD colspan="3"><INPUT TYPE="text" NAME="linkaddress"
							class=bk value="<%=linkaddress%>" size=60> &nbsp;</TD>
					</TR>

					<TR bgcolor="#FFFFFF" leaf="optionalContents">
						<TD class=column align="right">证件类型</TD>
						<TD><SELECT name="cred_type_id" onChange="" class=bk>
							<option value="1" <% if (cred_type_id.equals("1")) {%> selected
								<%}%>>身份证</option>
							<option value="2" <% if (cred_type_id.equals("2")) {%> selected
								<%}%>>工作证</option>
							<option value="3" <% if (cred_type_id.equals("3")) {%> selected
								<%}%>>军官证</option>
							<option value="4" <% if (cred_type_id.equals("4")) {%> selected
								<%}%>>其他</option>
						</SELECT> &nbsp;</TD>
						<TD class=column align="right">证件号码</TD>
						<TD><INPUT TYPE="text" NAME="credno" maxlength=30 class=bk
							size=20 value="<%=credno%>"></TD>
					</TR>

					<TR bgcolor="#FFFFFF" leaf="optionalContents">
						<TD class=column align="right">客户类型</TD>
						<TD><SELECT name="cust_type_id" onChange="" class=bk>
							<option value="2" <% if (cust_type_id.equals("2")) {%> selected
								<%}%>>个人客户</option>
							<option value="0" <% if (cust_type_id.equals("0")) {%> selected
								<%}%>>公司客户</option>
							<option value="1" <% if (cust_type_id.equals("1")) {%> selected
								<%}%>>网吧客户</option>
						</SELECT> &nbsp;</TD>
						<TD class=column align="right">允许用户上网数</TD>
						<TD><INPUT TYPE="text" NAME="max_user_number" maxlength=15
							class=bk value="<%=max_user_number%>" size=13></TD>
					</TR>
					<TR bgcolor="#FFFFFF" leaf="optionalContents">
						<TD class=column align="right">用户状态编码</TD>
						<TD>
						<INPUT TYPE="text" NAME="user_state_dis" disabled=true class=bk size=13>
						<INPUT TYPE="hidden" NAME="user_state" readonly=true size=13>
						<!--  <select name="user_state" class=bk >
							<option value="1" <% if ("1".equals(user_state)) {%> selected
								<%}%>>开户</option>
							<option value="2" <% if ("2".equals(user_state)) {%> selected
								<%}%>>暂停</option>
							<option value="3" <% if ("3".equals(user_state)) {%> selected
								<%}%>>销户</option>
							<option value="4" <% if ("4".equals(user_state)) {%> selected
								<%}%>>更换设备</option>
						</select> &nbsp;-->
						</TD>
						<TD class=column align="right">工单受理时间</TD>
						<TD><INPUT TYPE="text" NAME="dealdate" class=bk size=13
							value="<%=dealdate%>" readonly> <INPUT TYPE="button"
							value="▼" class=btn onclick="showCalendar('day',event)"> <input
							type="text" name="time" value="<%=time%>" class=bk size=13>
						<INPUT TYPE="hidden" NAME="hidDealDate" class=bk></TD>
					</TR>
					<TR bgcolor="#FFFFFF" leaf="optionalContents">
						<TD class=column align="right">竣工状态</TD>
						<TD><SELECT name="opmode" onChange="" class=bk>
							<option value="0" <% if (opmode.equals("0")) {%> selected <%}%>>
							未竣工</option>
							<option value="1" <% if (opmode.equals("1")) {%> selected <%}%>>
							已竣工</option>
						</SELECT> &nbsp;</TD>
						<TD class=column align="right">竣工时间</TD>
						<TD><INPUT TYPE="text" NAME="onlinedate" class=bk
							value="<%= onlinedate%>" size=13 readonly> <INPUT
							TYPE="button" value="▼" class=btn onclick="showCalendar('day',event)">
						<input type="text" name="onlineTime" value="<%=onlineTime%>"
							class=bk size=13> <INPUT TYPE="hidden"
							NAME="hidOnlineDate" class=bk></TD>
					</TR>
					<TR bgcolor="#FFFFFF" leaf="optionalContents">
						<TD class=column align="right">开户时间</TD>
						<TD><INPUT TYPE="text" NAME="opendate" class=bk
							value="<%=opendate %>" size=13 readonly> <INPUT
							TYPE="button" value="▼" class=btn onclick="showCalendar('day',event)">
						<input type="text" name="openTime" value="<%=openTime%>" class=bk
							size=13> <INPUT TYPE="hidden" NAME="hidOpenDate" class=bk>
						&nbsp;</TD>
						<TD class=column align="right">暂停时间</TD>
						<TD><INPUT TYPE="text" NAME="pausedate" class=bk
							value="<%=pausedate %>" size=13 readonly> <INPUT
							TYPE="button" value="▼" class=btn onclick="showCalendar('day',event)">
						<input type="text" name="pauseTime" value="<%=pauseTime%>"
							class=bk size=13> <INPUT TYPE="hidden"
							NAME="hidPauseDate" class=bk></TD>
					</TR>
					<TR bgcolor="#FFFFFF" leaf="optionalContents">
						<TD class=column align="right">销户时间</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="closedate" class=bk
							value="<%=closedate %>" size=13 readonly> <INPUT
							TYPE="button" value="▼" class=btn onclick="showCalendar('day',event)">
						<input type="text" name="closeTime" value="<%=closeTime%>"
							class=bk size=13> <INPUT TYPE="hidden"
							NAME="hidCloseDate" class=bk> <INPUT TYPE="hidden"
							NAME="updatetime" class=bk value="<%= updatetime%>" size=13>
						<input type="hidden" name="upTime" value="<%=upTime%>" class=bk
							size=13> <INPUT TYPE="hidden" NAME="hidUpdateTime"
							class=bk> <INPUT type="hidden" name="staff_id"
							value="<%=staff_id%>"></TD>
					</TR>
					<TR>
						<TD colspan="4" align="right" class="green_foot"><INPUT
							TYPE="button" value=" 保 存 " class="btn" onclick="CheckForm()">
						&nbsp;&nbsp; <INPUT TYPE="reset" value=" 重 写 " class="btn">
						<INPUT TYPE="hidden" name="action" value=""></TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</FORM>
		</TD>
	</TR>
</TABLE>
<script LANGUAGE="JavaScript">
	//根据上网类型，是否加载IP地址两行
   	var wtype = "<%= wan_type%>";
   	if(wtype == "3"){
   		
		ipaddrSpan.style.display = "";
		document.getElementById("ipaddrSpan2").style.display = "";
	}
	startHtmlInit();
	function startHtmlInit() {
		access_style_id_onchange();
		setUser_state();
		ShowIPAddr(document.frm.wan_type);
		var gwOptType = <%=gwOptType%>
		if("11"==gwOptType){
			document.frm.wan_type.value = "1";
		}
	}
	
	function  access_style_id_onchange(){
		//根据业务类型，加载上网方式
		var servtype = "<%=selectServTypeId%>";
		var access_style_id = document.frm.access_style_id.value;
		showVal(document.frm.some_service);
		if("0"==access_style_id){
			this.tdiid.style.display="inline";
			this.tdvlanid.style.display="none";
		}else{
			this.tdiid.style.display="none";
			this.tdvlanid.style.display="inline";
		}
	}
	
	function setUser_state(){
		var user_state = "<%=user_state%>";
		var user_state_ch;
		var user_state_sub = user_state;
		if("1"==user_state){
			user_state_ch = "开户";
		}else if("2"==user_state){
			user_state_ch = "暂停";
		}else if("3"==user_state){
			user_state_ch = "销户";
		}else if("4"==user_state){
			user_state_ch = "更换设备	";
		}else{
			user_state_ch = "开户";
			user_state_sub = "1";
		}
		document.frm.user_state_dis.value = user_state_ch;
		document.frm.user_state.value = user_state_sub;
	}
	//onBlur = "SearchDevice()" 
</script>
<iframe id="childFrm" style="display:none"></iframe>
<iframe id="childFrm1" style="display:none"></iframe>

<br>
<%@ include file="../foot.jsp"%>
