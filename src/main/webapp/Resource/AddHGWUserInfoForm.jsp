<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Date"%>
<%@page import="com.linkage.litms.common.util.*,com.linkage.litms.LipossGlobals"%>
<jsp:useBean id="HGWUserInfoAct" scope="request"
	class="com.linkage.litms.resource.HGWUserInfoAct" />
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />


<SCRIPT LANGUAGE="JavaScript" id="idParentFun">
var m_bShow_pe; 
var m_bShow_ce;
var selected_value;
var selected_value_ce;
</SCRIPT>


<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>

<%
	String curCity_ID = curUser.getCityId();
/**
	String curGather_ID = "-1";
	Cursor cursorTmp = DataSetBean
			.getCursor("select gather_id,descr from tab_process_desc where city_id = '"
			+ curCity_ID + "'");
	Map fieldTmp = cursorTmp.getNext();
	if (fieldTmp != null) {
		curGather_ID = (String) fieldTmp.get("gather_id");
	}
*/
	request.setCharacterEncoding("GBK");
	//获取程序安装属地ShortName
	String shortName = LipossGlobals.getLipossProperty("InstArea.ShortName");
	if (shortName == null) shortName = "";

	String strCityList = HGWUserInfoAct.getCityListSelf(false,curCity_ID, "",
			request);
	String servTypeList = HGWUserInfoAct.getServTypeList(1);
	String strOfficeList = HGWUserInfoAct.getOfficeList(false, "", "");
	String strZoneList = HGWUserInfoAct.getZoneList(false, "", "");

	String strGatherList = DeviceAct.getGatherList(session, "", "",
			false);
	//设备列表
	//String strDeviceList = HGWUserInfoAct.getDeviceByArea(false,"","",request);
	Cursor cursor_device = HGWUserInfoAct.getDeviceByArea(request);
	Map fields_device = cursor_device.getNext();

	//厂商列表
	//String strVendorList = DeviceAct.getVendorList(true, "", "");
	//业务列表
	//获得所有的服务信息
	Cursor cursor_service = HGWUserInfoAct.getServiceInfo();
	Map fields_service = cursor_service.getNext();

	Date curDtae = new Date();
	//String dealdate = StringUtils.formatDate("yyyy-MM-dd HH:mm:ss",curDtae.getTime()/1000);
	//String longtime = new DateTimeUtil(curDtae.getTime()).getLongDate();
	String dealdate = new DateTimeUtil(curDtae.getTime()).getDate();
	String time=new DateTimeUtil(curDtae.getTime()).getTime();
	
	//String curTime = StringUtils.formatDate("yyyy-MM-dd",new Date().getTime()/1000);	
	
%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	function check(){
		document.all("childframe").src = "checkDevice.jsp?oui="+document.frm.oui.value+"&vender="+document.frm.vender.value;
		
		if (document.frm.device_id.value == ""){
			if (!confirm("该家庭网关设备不存在，是否继续保存")){
				alert("操作取消!");
				obj.oui.focus();
				return false;
			}
			else{
				CheckForm();
			}
		}
	}

	function CheckForm() {
		var obj = document.frm;
		
		//document.all("childFrm").src = "checkUserName.jsp?username="+document.frm.username.value;
	    
		//var retValue   = window.showModalDialog("./checkDevice.jsp?oui="+document.frm.oui.value+"&vender="+document.frm.vender.value,window,"dialogLeft:1px;dialogTop:1px;dialogHeight:0px;dialogWidth:0px");
 		//if (typeof(retValue) != 'undefined'){
		//	document.frm.device_id.value = retValue;
		//}
		//if (document.frm.device_id.value == ""){
		//	if (!confirm("该家庭网关设备不存在，是否继续保存")){
		//		alert("操作取消!");
		//		obj.oui.focus();
		//		return false;
		//	}
		//}

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
		} else if(!IsNull(obj.passwd.value,"用户密码")){
			obj.passwd.focus();
			obj.passwd.select();
			return false;
		} else if(!IsNull(obj.validate_passwd.value,"密码验证")){
			obj.validate_passwd.focus();
			obj.validate_passwd.select();
			return false;
		} else if(obj.validate_passwd.value != obj.passwd.value){
			alert("密码和验证密码不一致!");
			obj.validate_passwd.focus();
			obj.validate_passwd.select();
			return false;
		}else if(obj.vpiid.value == ""){
			 alert("请输入VPI");
		     obj.vpiid.focus();
		     return false;
		}else if(obj.vciid.value == ""){
			 alert("请输入VCI");
		     obj.vciid.focus();
		     return false;
		} else if(obj.vciid.value!="" && !IsNumber(obj.vciid.value,"VCI")){
			obj.vciid.focus();
			obj.vciid.select();
			return false;
		} else if(obj.vpiid.value!="" && !IsNumber(obj.vpiid.value,"VPI")){
			obj.vpiid.focus();
			obj.vpiid.select();
			return false;
		} else if(obj.virtualnum.value!="" && Trim(obj.virtualnum.value)==""){
			alert("虚拟号码应为数字");
			obj.virtualnum.focus();
			obj.virtualnum.select();
			return false;
		} else if(obj.virtualnum.value!="" && !IsNumber(obj.virtualnum.value,"虚拟号码")){		
			obj.virtualnum.focus();
			obj.virtualnum.select();
			return false;
		} else if(obj.bandwidth.value!="" && Trim(obj.bandwidth.value)==""){
			alert("带宽应为数字");
			obj.bandwidth.focus();
			obj.bandwidth.select();
			return false;
		} else if(obj.bandwidth.value!="" && !IsNumber(obj.bandwidth.value,"带宽")){		
			obj.bandwidth.focus();
			obj.bandwidth.select();
			return false;
		} else if(obj.overipnum.value!="" && Trim(obj.overipnum.value)==""){
			alert("额外IP地址需求数应为数字");
			obj.overipnum.focus();
			obj.overipnum.select();
			return false;
		} else if(obj.overipnum.value!="" && !IsNumber(obj.overipnum.value,"额外IP地址需求数")){		
			obj.overipnum.focus();
			obj.overipnum.select();
			return false;
		} else if(obj.device_shelf.value!="" && Trim(obj.device_shelf.value)==""){
			alert("接入设备机架号应为数字");
			obj.device_shelf.focus();
			obj.device_shelf.select();
			return false;
		} else if(obj.device_shelf.value!="" && !IsNumber(obj.device_shelf.value,"接入设备机架号")){		
			obj.device_shelf.focus();
			obj.device_shelf.select();
			return false;
		} else if(obj.device_frame.value!="" && Trim(obj.device_frame.value)==""){
			alert("接入设备框号应为数字");
			obj.device_frame.focus();
			obj.device_frame.select();
			return false;
		} else if(obj.device_frame.value!="" && !IsNumber(obj.device_frame.value,"接入设备框号")){		
			obj.device_frame.focus();
			obj.device_frame.select();
			return false;
		} else if(obj.device_slot.value!="" && Trim(obj.device_slot.value)==""){
			alert("接入设备槽位号应为数字");
			obj.device_slot.focus();
			obj.device_slot.select();
			return false;
		} else if(obj.device_slot.value!="" && !IsNumber(obj.device_slot.value,"接入设备槽位号")){		
			obj.device_slot.focus();
			obj.device_slot.select();
			return false;
		} else if(obj.device_port.value!="" && Trim(obj.device_port.value)==""){
			alert("接入设备端口应为数字");
			obj.device_port.focus();
			obj.device_port.select();
			return false;
		} else if(obj.device_port.value!="" && !IsNumber(obj.device_port.value,"接入设备端口")){		
			obj.device_port.focus();
			obj.device_port.select();
			return false;
		} else if(obj.basdevice_shelf.value!="" && Trim(obj.basdevice_shelf.value)==""){
			alert("BAS接入设备机架号应为数字");
			obj.basdevice_shelf.focus();
			obj.basdevice_shelf.select();
			return false;
		} else if(obj.basdevice_shelf.value!="" && !IsNumber(obj.basdevice_shelf.value,"BAS接入设备机架号")){		
			obj.basdevice_shelf.focus();
			obj.basdevice_shelf.select();
			return false;
		} else if(obj.basdevice_frame.value!="" && Trim(obj.basdevice_frame.value)==""){
			alert("BAS接入设备框号应为数字");
			obj.basdevice_frame.focus();
			obj.basdevice_frame.select();
			return false;
		} else if(obj.basdevice_frame.value!="" && !IsNumber(obj.basdevice_frame.value,"BAS接入设备框号")){		
			obj.basdevice_frame.focus();
			obj.basdevice_frame.select();
			return false;
		} else if(obj.basdevice_slot.value!="" && Trim(obj.basdevice_slot.value)==""){
			alert("BAS接入设备槽位号应为数字");
			obj.basdevice_slot.focus();
			obj.basdevice_slot.select();
			return false;
		} else if(obj.basdevice_slot.value!="" && !IsNumber(obj.basdevice_slot.value,"BAS接入设备槽位号")){		
			obj.basdevice_slot.focus();
			obj.basdevice_slot.select();
			return false;
		} else if(obj.basdevice_port.value!="" && Trim(obj.basdevice_port.value)==""){
			alert("BAS接入设备端口应为数字");
			obj.basdevice_port.focus();
			obj.basdevice_port.select();
			return false;
		} else if(obj.basdevice_port.value!="" && !IsNumber(obj.basdevice_port.value,"BAS接入设备端口")){		
			obj.basdevice_port.focus();
			obj.basdevice_port.select();
			return false;
		} else if(obj.vlanid.value!="" && Trim(obj.vlanid.value)==""){
			alert("VlanID号应为数字");
			obj.vlanid.focus();
			obj.vlanid.select();
			return false;
		} else if(obj.vlanid.value!="" && !IsNumber(obj.vlanid.value,"VlanID号")){		
			obj.vlanid.focus();
			obj.vlanid.select();
			return false;
		} else if(obj.userline.value!="" && Trim(obj.userline.value)==""){
			alert("用户线路应为数字");
			obj.userline.focus();
			obj.userline.select();
			return false;
		} else if(obj.userline.value!="" && !IsNumber(obj.userline.value,"用户线路")){		
			obj.userline.focus();
			obj.userline.select();
			return false;
		} else if(obj.maxattdnrate.value!="" && Trim(obj.maxattdnrate.value)==""){
			alert("最大下行可达速率应为数字");
			obj.maxattdnrate.focus();
			obj.maxattdnrate.select();
			return false;
		} else if(obj.maxattdnrate.value!="" && !IsNumber(obj.maxattdnrate.value,"最大下行可达速率")){		
			obj.maxattdnrate.focus();
			obj.maxattdnrate.select();
			return false;
		} else if(obj.upwidth.value!="" && Trim(obj.upwidth.value)==""){
			alert("上行承诺速率应为数字");
			obj.upwidth.focus();
			obj.upwidth.select();
			return false;
		} else if(obj.upwidth.value!="" && !IsNumber(obj.upwidth.value,"上行承诺速率")){		
			obj.upwidth.focus();
			obj.upwidth.select();
			return false;
		} else if(obj.max_user_number.value!="" && Trim(obj.max_user_number.value)==""){
			alert("允许用户上网数应为数字");
			obj.max_user_number.focus();
			obj.max_user_number.select();
			return false;
		} else if(obj.max_user_number.value!="" && !IsNumber(obj.max_user_number.value,"允许用户上网数")){		
			obj.upwidth.focus();
			obj.upwidth.select();
			return false;
		} else {
			obj.hidOpenDate.value = DateToDes(obj.opendate.value);
			obj.hidOnlineDate.value = DateToDes(obj.onlinedate.value);
			obj.hidPauseDate.value = DateToDes(obj.pausedate.value);
			obj.hidCloseDate.value = DateToDes(obj.closedate.value);
			obj.hidUpdateTime.value = DateToDes(obj.updatetime.value);
			
			obj.hidMoveDate.value = DateToDes(obj.movedate.value);
			obj.hidDealDate.value = DateToDes_long(obj.dealdate.value,obj.time.value);	
			obj.some_device.value = obj.oui.value + '/' + obj.vender.value;
			return true;
		}
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

	function SearchUName() {
		var f = document.frm;
		var username = f.username.value;
		if (username == "")	{
			f.username.focus();
			return false;
		} else {
			send_request('checkUserName.jsp?username='+username);
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
//-->
</SCRIPT>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="post" ACTION="HGWUserInfoSave.jsp"
				onsubmit="return CheckForm()">
				<input type="hidden" name="device_id" value="">
				<TABLE width="95%" border=0 cellspacing=0 cellpadding=0
					align="center">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162" align="center" class="title_bigwhite">
										用户资源
									</td>
									<td>
										<img src="../images/attention_2.gif" width="15" height="12">
										带'
										<font color="#FF0000">*</font>'的表单必须填写或选择
									</td>
								</tr>
							</table>
						</td>
					</tr>

					<TR>
						<TD bgcolor=#999999>

							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<TR>
									<TH colspan="4" align="center">
										家庭网关用户添加
									</TH>
								</TR>
								<TR>
									<TD colspan="4" align="center" class=column>
										必填区
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									
									<TD class=column align="left">
										用户帐户
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="username" maxlength=100 class=bk
											value="" onChange="SearchUName()">
										&nbsp;
										<font color="#FF0000">*</font>
										<span id="isOK"></span>
									</TD>
									<TD class=column align="left">
										业务类型
									</TD>
									<TD>
										<%=servTypeList%>
										&nbsp;
										<font color="red">*</font>&nbsp;&nbsp;

									</TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD class=column align="left">
										上网类型
									</TD>
									<TD colspan="3">
										<SELECT name="wan_type" class=bk>
											<option value="-1">
												==请选择==
											</option>
											<option value="1" selected>
												桥接
											</option>
											<option value="2">
												路由
											</option>
										</SELECT>
										&nbsp;
										<font color="red">*</font>&nbsp;&nbsp;
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD class=column align="left">
										用户密码
									</TD>
									<TD>
										<INPUT TYPE="password" NAME="passwd" maxlength=100 class=bk
											value="">
										&nbsp;
										<font color="#FF0000">*</font>
									</TD>
									<TD class=column align="left">
										密码验证
									</TD>
									<TD>
										<INPUT TYPE="password" NAME="validate_passwd" maxlength=100
											class=bk value="">
										&nbsp;
										<font color="#FF0000">*</font>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD class=column align="left">
										VPI
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="vpiid" maxlength=15 class=bk
											value="8">
										&nbsp;
										<font color="#FF0000">*</font>
									</TD>
									<TD class=column align="left">
										VCI
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="vciid" maxlength=15 class=bk
											value="35">
										&nbsp;
										<font color="#FF0000">*</font>
									</TD>

								</TR>


								<TR class="green_title" onclick="EC('suggestedContent',this);">
									<TD colspan="4" >
										<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
										<TR class=column>
											<TD>
												<font size="2">建议填写区</font>
											</TD>
											<TD align="right">
												<IMG SRC="images/up_enabled.gif" WIDTH="7" HEIGHT="9" BORDER="0" ALT="" >&nbsp;
											</TD>
										</TR>
										</TABLE>
									</TD>
								</TR>
								
								<TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none">
									<TD class=column align="left">
										家庭网关设备(OUI)
										<input type="hidden" name="some_device" value="">
									</TD>
									<TD colspan=3>
										<input type="text" name="oui" class=bk size="10">
										&nbsp; 序列号:&nbsp;
										<input type="text" name="vender" class=bk size="35">
										&nbsp;
										<IMG onClick="selDev()" SRC="../images/search.gif" WIDTH="15"
											HEIGHT="12" BORDER="0" ALT="选择">
										&nbsp;
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none">
								    <TD class=column align="left">
										采集点
									</TD>
									<TD>
										<%=strGatherList%>
										&nbsp;
										
									</TD>
									<TD class=column align="left">
										属地编码
									</TD>
									<TD>
										<%=strCityList%>
										&nbsp;
									</TD>
									
								</TR>
								<TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none">
									<TD class=column align="left">
										局向标识
									</TD>
									<TD>
										<%=strOfficeList%>
										&nbsp;
									</TD>
									<TD class=column align="left">
										小区标识
									</TD>
									<TD>
										<%=strZoneList%>
										&nbsp;
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none">
									<TD class=column align="left">
										用户实名
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="realname" maxlength=20 class=bk>
										&nbsp;
									</TD>
									<TD class=column align="left">
										性别
									</TD>
									<TD>
										<SELECT NAME="sex" class=bk>
											<option value="男">
												男
											</option>
											<option value="女">
												女
											</option>
										</SELECT>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none">
									<TD class=column align="left">
										联系人
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="linkman" class=bk>
										&nbsp;
									</TD>


									<TD class=column align="left">
										联系电话
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="linkphone" class=bk>
										&nbsp;
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none">
									<TD class=column align="left">
										联系人地址
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="linkaddress" class=bk>
										&nbsp;
									</TD>

									<TD class=column align="left">
										联系人手机
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="mobile" class=bk>
										&nbsp;
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none">
									<TD class=column align="left">
										联系人email
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="email" class=bk>
										&nbsp;
									</TD>
									<TD class=column align="left">
										带宽
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="bandwidth" maxlength=15 class=bk
											value="2048">
										(bps)
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none">
									<TD class=column align="left">
										Adsl绑定电话
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="phonenumber" maxlength=15 class=bk>
										&nbsp;
									</TD>
									<TD class=column align="left">
										工单受理时间
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="dealdate" class=bk
											value="<%=dealdate%>">
										<INPUT TYPE="button" value="▼" class=btn
											onclick="showCalendar('day',event)">
										<input type="text" name="time" value="<%=time%>" class=bk>	
										<INPUT TYPE="hidden" NAME="hidDealDate" class=bk>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="suggestedContent" style="display:none">
									<TD class=column align="left">
										最大下行可达速率
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="maxattdnrate" maxlength=15 class=bk
											value="2048">
										(bps)
									</TD>
									<TD class=column align="left">
										上行承诺速率
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="upwidth" maxlength=15 class=bk
											value="512">
										(bps)
									</TD>

								</TR>


								<TR class="green_title" onclick="EC('optionalContents',this);">
									<TD colspan="4">
										<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
										<TR>
										<TD>
											<font size="2">选择填写区</font> 
										</TD>
										<TD align="right">
											<IMG SRC="images/up_enabled.gif" WIDTH="7" HEIGHT="9" BORDER="0" ALT="" >&nbsp;
										</TD>
										</TR>
										</TABLE>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										合同号
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="cotno" maxlength=100 class=bk>
										&nbsp;
									</TD>
									<TD class=column align="left">
										所在电缆id
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="cableid" maxlength=15 class=bk>
									</TD>

								</TR>




								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										计费类别
									</TD>
									<TD>
										<SELECT name="bill_type_id" onChange="" class=bk>
											<option value="0">
												包时长
											</option>
											<option value="1">
												包月
											</option>
											<option value="2">
												包年
											</option>
											<option value="3">
												记流量
											</option>
										</SELECT>
										&nbsp;
									</TD>
									<TD class=column align="left">
										下月计费类别
									</TD>
									<TD>
										<SELECT name="next_bill_type_id" onChange="" class=bk>
											<option value="0">
												包时长
											</option>
											<option value="1">
												包月
											</option>
											<option value="2">
												包年
											</option>
											<option value="3">
												记流量
											</option>
										</SELECT>
										&nbsp;
									</TD>

								</TR>


								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										用户状态编码
									</TD>
									<TD>
										<select name="user_state" class=bk>
											<option value="-1">
												===请选择=
											</option>
											<option value="1" selected>
												开户
											</option>
											<option value="2">
												暂停
											</option>
											<option value="3">
												销户
											</option>
										</select>
										&nbsp;
									<TD class=column align="left">
										开户时间
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="opendate" class=bk>
										<INPUT TYPE="button" value="▼" class=btn
											onclick="showCalendar('day',event)">
										<INPUT TYPE="hidden" NAME="hidOpenDate" class=bk>
										&nbsp;

									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										客户类型
									</TD>
									<TD>
										<SELECT name="cust_type_id" onChange="" class=bk>
											<option value="0">
												公司客户
											</option>
											<option value="1">
												网吧客户
											</option>
											<option value="2" selected>
												个人客户
											</option>
										</SELECT>
										&nbsp;
									</TD>
									<TD class=column align="left">
										用户类型
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="user_type_id" maxlength=20 class=bk>
										&nbsp;
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										证件类型
									</TD>
									<TD>
										<SELECT name="cred_type_id" onChange="" class=bk>
											<option value="0">
												==请选择==
											</option>
											<option value="1">
												其他
											</option>
											<option value="2" selected>
												身份证
											</option>
											<option value="3">
												军官证
											</option>
											<option value="4">
												工作证
											</option>
										</SELECT>
										&nbsp;
									</TD>
									<TD class=column align="left">
										证件号码
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="credno" maxlength=30 class=bk>
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										安装住址
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="address" maxlength=20 class=bk>
									</TD>
									<TD class=column align="left">
										接入性质标识
									</TD>
									<TD>
										<SELECT name="access_kind_id" onChange="" class=bk>
											<option value="0">
												企业
											</option>
											<option value="1">
												网吧
											</option>
											<option value="2" selected>
												个人
											</option>
										</SELECT>
										&nbsp;
									</TD>
								</TR>



								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										绑定类型
									</TD>
									<TD>
										<SELECT name="bindtype" onChange="" class=bk>
											<option value="0">
												ADSL绑定
											</option>
											<option value="1">
												IPTV绑定
											</option>
											<option value="2">
												VOIP绑定
											</option>
										</SELECT>
										&nbsp;
									</TD>
									<TD class=column align="left">
										虚拟号码
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="virtualnum" maxlength=20 class=bk>
										&nbsp;
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										号码特性
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="numcharacter" maxlength=20 class=bk>
									</TD>
									<TD class=column align="left">
										接入方式标识
									</TD>
									<TD>
										<SELECT name="access_style_id" onChange="" class=bk>
											<option value="0">
												ADSL接入
											</option>
											<option value="1">
												LAN接入
											</option>
											<option value="2">
												光纤接入
											</option>
										</SELECT>
										&nbsp;
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										认证标志
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="aut_flag" maxlength=20 class=bk>
									</TD>
									<TD class=column align="left">
										享受服务类型编码集
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="service_set" maxlength=30 class=bk>
									</TD>

								</TR>

								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										单位客户行业类别
									</TD>
									<TD>
										<SELECT name="trade_id" onChange="" class=bk>
											<option value="0">
												生产行业
											</option>
											<option value="1">
												销售行业
											</option>
											<option value="2">
												金融行业
											</option>
											<option value="3">
												服务行业
											</option>
										</SELECT>
										&nbsp;
									</TD>
									<TD class=column align="left">
										法人营业执照注册号
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="licenceregno" class=bk>
										&nbsp;
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										个人职业代码
									</TD>
									<TD>
										<SELECT name="occupation_id" onChange="" class=bk>
											<option value="0">
												技术人员
											</option>
											<option value="1">
												公务员
											</option>
											<option value="2">
												管理者
											</option>
											<option value="3">
												老板
											</option>
											<option value="4">
												自由职业者
											</option>
											<option value="5">
												其他职业者
											</option>
										</SELECT>
									</TD>
									<TD class=column align="left">
										教育程度代码
									</TD>
									<TD>
										<SELECT name="education_id" onChange="" class=bk>
											<option value="0">
												高中
											</option>
											<option value="1">
												大专
											</option>
											<option value="2">
												大学本科
											</option>
											<option value="3">
												硕士
											</option>
											<option value="4">
												博士
											</option>
											<option value="5">
												其他
											</option>
										</SELECT>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										VIP卡号
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="vipcardno" class=bk>
										&nbsp;
									</TD>
									<TD class=column align="left">
										联系人身份证号
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="linkman_credno" maxlength=20 class=bk>
										&nbsp;
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										代办人
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="agent" class=bk>
										&nbsp;
									</TD>
									<TD class=column align="left">
										代办人身份证号
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="agent_credno" class=bk>
										&nbsp;
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										代办人联系电话
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="agentphone" class=bk>
										&nbsp;
									</TD>
									<TD class=column align="left">
										ADSL设备来源
									</TD>
									<TD>
										<SELECT name="adsl_res" onChange="" class=bk>
											<option value="0">
												运营商提供
											</option>
											<option value="1">
												个人购买
											</option>
										</SELECT>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										ADSL入网证号
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="adsl_card" class=bk>
										&nbsp;
									</TD>
									<TD class=column align="left">
										ADSL设备型号
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="adsl_dev" class=bk>
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										ADSL设备序号
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="adsl_ser" maxlength=15 class=bk>
									</TD>
									<TD class=column align="left">
										是否代维
									</TD>
									<TD>
										<INPUT TYPE="radio" NAME="isrepair" value="0" checked>
										否&nbsp;&nbsp;
										<INPUT TYPE="radio" NAME="isrepair" value="1">
										是
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										协议编号
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="contractno" class=bk>
										&nbsp;
									</TD>
									<TD class=column align="left">
										用户IP地址
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="ipaddress" maxlength=15 class=bk>
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										额外IP地址需求数
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="overipnum" maxlength=15 class=bk>
									</TD>
									<TD class=column align="left">
										掩码
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="ipmask" maxlength=15 class=bk>
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										网关
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="gateway" maxlength=15 class=bk>
										&nbsp;
									</TD>
									<TD class=column align="left">
										MAC地址
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="macaddress" maxlength=15 class=bk>
										&nbsp;
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										允许用户上网数
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="max_user_number" maxlength=15
											class=bk>
									</TD>
									<TD class=column align="left">
										接入设备ip
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="device_ip" maxlength=15 class=bk>
										&nbsp;
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										接入设备机架号
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="device_shelf" maxlength=15 class=bk>
										&nbsp;
									</TD>

									<TD class=column align="left">
										接入设备框号
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="device_frame" maxlength=15 class=bk>
										&nbsp;
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										接入设备槽位号
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="device_slot" maxlength=15 class=bk>
										&nbsp;
									</TD>
									<TD class=column align="left">
										接入设备端口
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="device_port" maxlength=15 class=bk>
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										BAS接入设备编号
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="basdevice_id" maxlength=15 class=bk>
									</TD>
									<TD class=column align="left">
										BAS接入设备ip
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="basdevice_ip" maxlength=15 class=bk>
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										BAS接入设备机架号
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="basdevice_shelf" maxlength=15
											class=bk>
									</TD>
									<TD class=column align="left">
										BAS接入设备框号
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="basdevice_frame" maxlength=15
											class=bk>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										BAS接入设备槽位号
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="basdevice_slot" maxlength=15 class=bk>
										&nbsp;
									</TD>
									<TD class=column align="left">
										BAS接入设备端口
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="basdevice_port" maxlength=15 class=bk>
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										VlanID号
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="vlanid" maxlength=15 class=bk>
										&nbsp;
									</TD>
									<TD class=column align="left">
										工单号
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="workid" maxlength=15 class=bk>
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										开通时间
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="onlinedate" class=bk value="<%= dealdate%>">
										<INPUT TYPE="button" value="▼" class=btn
											onclick="showCalendar('day',event)">
										<INPUT TYPE="hidden" NAME="hidOnlineDate" class=bk>
									</TD>

									<TD class=column align="left">
										暂停时间
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="pausedate" class=bk>
										<INPUT TYPE="button" value="▼" class=btn
											onclick="showCalendar('day',event)">
										<INPUT TYPE="hidden" NAME="hidPauseDate" class=bk>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										销户时间
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="closedate" class=bk>
										<INPUT TYPE="button" value="▼" class=btn
											onclick="showCalendar('day',event)">
										<INPUT TYPE="hidden" NAME="hidCloseDate" class=bk>
									</TD>
									<TD class=column align="left">
										更新时间
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="updatetime" class=bk value="<%= dealdate%>">
										<INPUT TYPE="button" value="▼" class=btn
											onclick="showCalendar('day',event)">
										<INPUT TYPE="hidden" NAME="hidUpdateTime" class=bk>
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										员工代码
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="staff_id" maxlength=15 class=bk>
										&nbsp;
									</TD>
									<TD class=column align="left">
										备注
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="remark" maxlength=15 class=bk>
									</TD>

								</TR>

								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										ADSL横列
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="adsl_hl" maxlength=15 class=bk>
									</TD>
									<TD class=column align="left">
										握手协议
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="opmode" maxlength=15 class=bk>
									</TD>

								</TR>

								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										用户线路
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="userline" maxlength=15 class=bk>
									</TD>
									<TD class=column align="left">
										Dslam设备序号
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="dslamserialno" maxlength=15 class=bk>
									</TD>

								</TR>
								<TR bgcolor="#FFFFFF" leaf="optionalContents" style="display:none">
									<TD class=column align="left">
										移动日期
									</TD>
									<TD colspan="3">
										<INPUT TYPE="text" NAME="movedate" class=bk>
										<INPUT TYPE="button" value="▼" class=btn
											onclick="showCalendar('day',event)">
										<INPUT TYPE="hidden" NAME="hidMoveDate" class=bk>
									</TD>


								</TR>


								<TR>
									<TD colspan="4" align="right" class=foot>
										<INPUT TYPE="submit" value=" 保 存 " class=btn>
										&nbsp;&nbsp;
										<INPUT TYPE="reset" value=" 重 写 " class=btn>
										<INPUT TYPE="hidden" name="action" value="add">

									</TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
				</TABLE>
			</FORM>
		</TD>
	</TR>
</TABLE>
<iframe id="childFrm" style="display:none"></iframe>
<iframe id="childFrm1" style="display:none"></iframe>
<script language="javascript">
/**
show();
function show() {
	var shortName = '';
	var tr = document.getElementById("wanType");
	if (tr != null) {
		if (tr.isShow == shortName) {
			tr.style.display = "";
		}
	}
}

*/

function showVal(obj) {
	//alert(obj.value);
}
var vNow = new Date();
var y0  = vNow.getYear();
var m0  = vNow.getMonth()+1;
var d0  = vNow.getDate();


document.frm.opendate.value = y0 + "-" + m0 + "-" + d0;
//document.frm.hidOpenDate.value = "<%=curDtae.getTime() / 1000%>";

function selDev()
{
	var retValue = window.showModalDialog("./selDeviceList.jsp?oui="+document.frm.oui.value+"&vender="+document.frm.vender.value + "&gw_type=1",window,"dialogHeight:600px;dialogWidth:800px");
	
	if (typeof(retValue) != 'undefined'){
		document.frm.some_device.value = retValue;
		var temp = retValue.split('/');
		document.frm.oui.value = temp[0];
		document.frm.vender.value = temp[1];
	}
	
	//window.open("./selDeviceList.jsp?queryStr="+document.frm.some_device.value);
}
</script>
<br>
<%@ include file="../foot.jsp"%>