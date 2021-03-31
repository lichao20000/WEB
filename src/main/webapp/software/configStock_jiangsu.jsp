<%--
FileName	: configStock.jsp
Author		: lizhaojun
Date		: 2007年5月10日
Desc		: 版本升级.
since       : V1.0, 2007年5月10日

modify record
------------------------------------------------------
desc		: modify URL, add a type para.
author		: Alex.Yan (yanhj@lianchuang.com)
version		: V1.2.0003,2007-8-30
--%>
<%@ include file="../timelater.jsp"%>
<%@ page import="com.linkage.litms.resource.*"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="java.util.Date" %>
<jsp:useBean id="versionManage" scope="request" class="com.linkage.litms.software.VersionManage" />
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct" />
<%
	request.setCharacterEncoding("gb2312");

	DeviceAct deviceAct = new DeviceAct();
	//采集点下拉框
	String gatherList = deviceAct.getGatherList(session, "", "", false);
	
	//设备厂商
	String strVendorList = DeviceAct.getVendorList(true, "", "");
	//String deviceModel = versionManage.getDeviceTypeList("", true);

	String file_path = versionManage.getFilePath_1("file_path_1");
	String file_path2 = versionManage.getVersionFilePathToConfigStock("");
	//String file_path3 = versionManage.getFilePath_2("file_path_3","","");
	
	//操作结果
	String opeResult = request.getParameter("operResult");
	//区分ITMS和BBMS功能菜单
	String gw_type = request.getParameter("gw_type");

%>
<%@ include file="../head.jsp"%>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript">
<%if(null!=opeResult)
    {  
    	if("true".equals(opeResult))
    	{
    		out.println("alert('策略定制成功！');");
    	}
    	else if("false".equals(opeResult))
    	{
    		out.println("alert('策略定制失败！');");
    	}
    	else if("notAllow".equals(opeResult))
    	{
    		out.println("alert('一个设备不能定义两种版本升级策略，请重新选择设备！')");
    	}
    }
%>

	function selectAll(elmID){
		var needFilter=false;
		t_obj = document.all(elmID);
		if(!t_obj) return;
		obj = event.srcElement;
		if(obj.checked){
			if(typeof(t_obj) == "object" ) {
				if(typeof(t_obj.length) != "undefined") {
					for(var i=0; i<t_obj.length; i++){
						t_obj[i].checked = true;
					}
				} else {
					t_obj.checked = true;
					needFilter = true;
				}
			}
		
		}else{
			if(typeof(t_obj) == "object" ) {
				if(typeof(t_obj.length) != "undefined") {
					for(var i=0; i<t_obj.length; i++){
						t_obj[i].checked = false;
					}
				} else {
					t_obj.checked = false;
				}
			}
		}
		
//		page = "showFilePath.jsp?device_id=" + "&devicetype_id=" +document.all.devicetype_id.value;
//		document.all("childFrm1").src = page;
		
	}
	function showChild(param){
		var page ="";
		if(param=="filename_1")
		{
		  document.all("filename_3").value=document.all("filename_1").value;
		}
		if(param=="file_path_1")
		{		  
		  var filePath=document.all("file_path_1").value;
		  var obj=document.all("file_path_1");
		  if(filePath!="-1")
		  {
		     document.all("file_path_3").value=obj.options[obj.selectedIndex].text;	
		     document.all("hidden_url_path").value =filePath.split("|")[0]+"/upload.jsp?dir_id="+filePath.split("|")[1];
		   }
		   else
		   {
		     document.all("file_path_3").value="";
		     document.all("hidden_url_path").value="";
		   }	    
		}
		if(param == "devicetype_id"){
			page = "showDevicetype1.jsp?vendor_id="+document.frm.vendor_id.value + "&devicetype_id="+ encodeURIComponent(document.frm.devicetype_id.value);
			document.all("childFrm2").src = page;	
		}
		if(param == "softwareversion"){
			document.frm.device.checked = false;
			page = "showDeviceArr.jsp?gather_id="+document.frm.gather_id.value + "&devicetype_id=" +document.frm.softwareversion.value+ "&needFilter=false";
			page += "&vendor_id="+document.frm.vendor_id.value;
			page += "&gw_type="+<%=gw_type%> ;     //区分ITMS和BBMS
			document.all("childFrm").src = page;
		}
		if(param == "vendor_id"){
			page = "showDevicetype.jsp?vendor_id="+document.frm.vendor_id.value;
			document.all("childFrm1").src = page;		
			
		}
		if(param == "file_path_2"){
			if(document.frm.file_path_2.value != -1){
				var file = document.frm.file_path_2.value;
				var arrFile = file.split("|");
				document.frm.file_size_2.value=arrFile[2];
				document.frm.filename_2.value=arrFile[1];
//				document.frm.username_2.value=arrFile[3];
//				document.frm.passwd_2.value=arrFile[4];
				
			}else{
				document.frm.file_size_2.value="";
				document.frm.filename_2.value="";	
//				document.frm.username_2.value="";
//				document.frm.passwd_2.value="";
			}
		}		
	}
	function ShowUser(Index){
		switch(Index){
	    case '1':
	    	document.frm.username_3.value= document.frm.username_1.value;  
	    	break;
	    case '2':
	    	document.frm.passwd_3.value= document.frm.passwd_1.value;  
	    	break;
	    case '3':
	    	document.frm.username_1.value= document.frm.username_3.value;  
	    	break;
	    case '4':
	    	document.frm.passwd_1.value= document.frm.passwd_3.value;  
	    	break;	   	
	   }   
	}
	
/**	
	function filterByDevID() {
		var obj = document.frm;
		var dev_id = "";
		for (var i = 0; i < obj.device_id.length; i++) {
			if (obj.device_id[i].checked) {
				dev_id = obj.device_id[i].value;
				for (var j = 0; j < obj.device_id.length; j++) {
					if (i != j && obj.device_id[j].checked) {
						isOnlyOne = true;
						dev_id = "";
						break;
					}
				}
				
			}
			
		}
		page = "showFilePath.jsp?device_id="+dev_id + "&devicetype_id=" +obj.devicetype_id.value+"&needFilter=true&refresh="+Math.random();
		document.all("childFrm").src = page;
		//alert("page:"+page);
	}
*/	
	function CheckForm(){
	var hguser=document.all("hguser").value;
    var telephone= document.all("telephone").value;
    var checkradios = document.all("checkType");
    var checkType = "";
    for(var i=0;i<checkradios.length;i++)
    {
      if(checkradios[i].checked)
	  {
	    checkType = checkradios[i].value;
	    break;
	  }
    }
    var excute_type_radios = document.all("excute_type");
    var excute_type = "";
    for(var i=0;i<excute_type_radios.length;i++)
    {
      if(excute_type_radios[i].checked)
	  {
	    excute_type = excute_type_radios[i].value;
	    break;
	  }
    }
    
    if(excute_type==2&&document.all("task_name").value=="")
    {
       alert("请填写策略名称！");
       document.frm.task_name.focus();
       return false;
    }
    if(excute_type==0&&document.all("task_name").value=="")
    {
       alert("请填写策略名称！");
       document.frm.task_name.focus();
       return false;
    }
	if(checkType==0&&document.frm.gather_id.value == -1){
		alert("请选择采集点！");
		document.frm.gather_id.focus();
		return false;
  	}
  	 if(checkType==0&&document.frm.vendor_id.value == -1){
		alert("请选择厂商！");
		document.frm.vendor_id.focus();
		return false;
 	}
 	if(checkType==0&&document.frm.devicetype_id.value == -1){
		alert("请选择设备型号！");
		document.frm.devicetype_id.focus();
		return false;
 	}
 	if(checkType==0&&document.frm.softwareversion.value == -1){
			alert("请选择设备版本！");
			document.frm.softwareversion.focus();
			return false;
		}
		
	if(checkType==1&&""==hguser&&""==telephone)
	{
	  alert("请填写用户名或电话号码！");
	  document.all("hguser").focus();
	  return false;
	}
				var oselect = document.all("device_id");
		if(oselect == null){
			alert("请选择设备！");
			return false;
		}
		var num = 0;
		if(typeof(oselect.length)=="undefined"){
			if(oselect.checked){
				num = 1;
			}
		}else{
			for(var i=0;i<oselect.length;i++){
				if(oselect[i].checked){
					num++;
				}
			}

		}
		if(num ==0){
			alert("请选择设备！");
			return false;
		}
		var obj = document.frm;
		if(obj.file_path_1.value == -1){
			alert("请选择备份配置文件路径！");
			obj.file_path_1.focus();
			return false;
		}
		if(!IsNull(obj.filename_1.value,'备份配置文件名称')){
		    alert("请填写备份配置文件名称！");
			obj.filename_1.focus();
			return false;
		}
		if(!IsNull(obj.delay_time_1.value,'备份配置时延')){
			obj.delay_time_1.focus();
			obj.delay_time_1.select();
			return false;
		}		
//-------------------------------------------------------------------
		if(obj.file_path_2.value == -1){
			alert("请选择软件升级文件路径！");
			obj.file_path_2.focus();
			return false;
		}
		if(!IsNull(obj.file_size_2.value,'软件升级文件大小')){
			obj.file_size_2.focus();
			obj.file_size_2.select();
			return false;
		}	
		if(!IsNull(obj.filename_2.value,'软件升级文件名')){
			obj.filename_2.focus();
			obj.filename_2.select();
			return false;
		}
		if(!IsNull(obj.delay_time_2.value,'软件升级时延')){
			obj.delay_time_2.focus();
			obj.delay_time_2.select();
			return false;
		}			
//-------------------------------------------------------------------

		if(obj.file_path_3.value == -1){
			alert("请选择配置恢复文件路径！");
			bj.file_path_3.focus();
			return false;
		}
		if(!IsNull(obj.file_size_3.value,'配置恢复文件大小')){
			obj.file_size_3.focus();
			obj.file_size_3.select();
			return false;
		}	
		if(!IsNull(obj.filename_3.value,'配置恢复文件名')){
			obj.filename_3.focus();
			obj.filename_3.select();
			return false;
		}
		
		if(obj.filename_3.value!=obj.filename_1.value)
		{
		    alert("配置恢复那边的配置文件名称，必须与配置备份那边一致！");
		    obj.filename_3.focus();
			obj.filename_3.select();
			return false;
		}
		if(!IsNull(obj.delay_time_3.value,'配置恢复时延')){
			obj.delay_time_3.focus();
			obj.delay_time_3.select();
			return false;
		}			
	}
	
function relateDevice()
{
   var hguser=document.all("hguser").value;
   var telephone= document.all("telephone").value;
   var checkradios = document.all("checkType");
   var checkType = "";
   for(var i=0;i<checkradios.length;i++)
   {
     if(checkradios[i].checked)
	 {
	   checkType = checkradios[i].value;
	   break;
	 }
   }
   //alert("hguser:"+hguser+"   telephone:"+telephone+"   checkType:"+checkType);
   if(1==checkType&&""==hguser&&""==telephone)
   {
      alert("请填写用户名或电话号码！");
      document.all("hguser").focus();
   }
   else if(1==checkType)
   {
      var page="";
      page="showDeviceArr.jsp?hguser="+hguser+"&telephone="+telephone+"&needFilter=false&refresh="+Math.random();
      page += "&gw_type="+<%=gw_type%>;
      document.all("childFrm1").src = page;
   }
   
}


function relateDeviceBySerialno()
{
   var serialnumber=document.all("serialnumber").value;
   var checkradios = document.all("checkType");
   var checkType = "";
   for(var i=0;i<checkradios.length;i++)
   {
     if(checkradios[i].checked)
	 {
	   checkType = checkradios[i].value;
	   break;
	 }
   }
   //alert("hguser:"+hguser+"   telephone:"+telephone+"   checkType:"+checkType);
   if(checkType==2 && serialnumber.length<6)
   {
      alert("请填写至少最后六位设备序列号！");
      document.all("serialnumber").focus();
   }
   else if(checkType==2)
   {
      var page="";
      page="showDeviceArr.jsp?serialnumber="+serialnumber+"&needFilter=false&refresh="+Math.random();
      page += "&gw_type="+<%=gw_type%>;
      document.all("childFrm1").src = page;
   }
   
}

function ShowDialog(param)
{
  //根据用户来查询
  if(param==1)
  {
     tr1.style.display="none";
     tr2.style.display="none";
     tr3.style.display="";
     tr4.style.display="none";
  }
  
  //根据设备版本来查询
  if(param==0)
  {
     tr1.style.display="";
     tr2.style.display="";
     tr3.style.display="none";
     tr4.style.display="none";
  }
  
  //根据设备序列号来查询
  if(param==2)
  {
     tr1.style.display="none";
     tr2.style.display="none";
     tr3.style.display="none";
     tr4.style.display="";
  }
}

function displayType(param)
{
   //如果选择立即执行或计划执行升级
   if(1==param||0==param)
   {
      tr0.style.display="none";
   }
   
   //选择自动执行升级
   if(2==param)
   {
      tr0.style.display="";
   }
}
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="post" ACTION="configStock_send_jiangsu.jsp" onsubmit="return CheckForm()">
				<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162" class="title_bigwhite" align="center" nowrap>
										软件升级管理
									</td>
									<td nowrap>
										<img src="../images/attention_2.gif" width="15" height="12">
										对选择的设备进行软件升级备份配置。
										<input type="radio" value="0" onclick="ShowDialog(this.value)" name="checkType" checked>根据设备版本查询&nbsp;&nbsp;
			                            <input type="radio" value="1" onclick="ShowDialog(this.value)" name="checkType">根据用户查询设备&nbsp;&nbsp;
			                            <input type="radio" value="2" onclick="ShowDialog(this.value)" name="checkType">根据设备序列号查询
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td>
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR>
												<TH colspan="4" align="center" >
													定制备份升级策略
												</TH>										
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%" nowrap>
													业务类型:
												</TD>
												<TD width="35%">
													<select name="service_id" class="bk" readOnly>
														<option value="6">
															软件升级备份配置
														</option>
													</select>
												</TD>
												<TD align="right" width="15%">
													执行方式:
												</TD>
												<TD width="35%">
													<input type="radio" name="excute_type" value="0" onclick="displayType(this.value)">
													立即执行
													<input type="radio" name="excute_type" value="2" onclick="displayType(this.value)" checked>
													定制策略
												</TD>																								
											<tr bgcolor="#FFFFFF">
												<TD align="right" width="15%">
											    策略名称：
											    </TD>
											    <TD width="35%" colspan=3>
											    <input type=text name="task_name" class=bk value="">
											    </TD>
											</tr>
											<TR bgcolor="#FFFFFF" id="tr0" STYLE="display:">
											    <TD align="right" width="15%">
													策略执行时间:
												</TD>
												<TD width="35%" colspan=3>
													<select name="auto_excutetime_type" class="bk">
														<option value="1">
															设备初始安装第一次启动时自动升级
														</option>
														<option value="2">
															设备Periodic Inform自动升级
														</option>
														<option value="3">
															设备重新启动时自动升级
														</option>
														<option value="4" selected>
															设备下次连接到ITMS时自动升级
														</option>
													</select>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr1" STYLE="display:">
											    <TD align="right" width="15%">
													采集点:
												</TD>
												<TD width="35%">
													<%=gatherList%>
												</TD>
												
												<TD align="right" width="15%">
													厂商:
												</TD>
												<TD width="35%">
													<%=strVendorList%>
												</TD>
											</TR>											
											<TR bgcolor="#FFFFFF" id="tr2" STYLE="display:">	
												<TD align="right" width="15%">
													设备型号:
												</TD>
												<TD width="30%">
													<div id="div_devicetype">
														<select name="devicetype_id" class="bk">
															<option value="-1">
																--请先选择厂商--
															</option>
														</select>
													</div>
												</TD>
												<TD align="right" width="20%">
													设备版本:
												</TD>
												<TD width="30%">
													<div id="div_deviceversion">
														<select name="device_version" class="bk">
															<option value="-1">
																--请先选择设备型号--
															</option>
														</select>													
													</div>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr3" STYLE="display:none">
											    <TD align="right" width="15%">
													用户名:
												</TD>
												<TD width="35%">
													<input type="text" name="hguser" class=bk value="">
												</TD>
												<TD align="right" width="15%">
													用户电话号码:
												</TD>
												<TD width="35%">
													<input type="text" name="telephone" class=bk value="">
													<input type="button" class=btn value="查询" onclick="relateDevice()">
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr4" STYLE="display:none">
											    <TD align="right">
													设备序列号:
												</TD>
												<TD>
													<input type="text" name="serialnumber" value="" class=bk><font color="red">至少最后六位</font>
												</TD>
												<TD colspan=2>
													<input type="button" class=btn value="查询" onclick="relateDeviceBySerialno()">
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right">
													设备列表:
													<br>
													<INPUT TYPE="checkbox" onclick="selectAll('device_id')" name="device">
													全选
												</TD>
												<TD colspan="3">
													<div id="div_device" style="width:95%; height:100px; z-index:1; top: 100px; overflow:scroll">
														<span>请选择设备！</span>
													</div>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD colspan="4">
													<BR>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TH colspan="4">
													备份配置
												</TH>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%" style="display:none">
													关键字:
												</TD>
												<TD width="30%" style="display:none">
													<input type="text" name="keyword_1" maxlength=255 class=bk size=20 value="config_upload">
												</TD>
												<TD align="right" width="15%">
													文件类型:
												</TD>
												<TD width="30%" colspan=3>
													<input type="text" name="filetype_1" maxlength=255 class=bk size=30 value="2 Vendor Configuration File" readOnly>
													<!-- <select name="filetype_1" class="bk" readOnly>
														<option value="2 Vendor Configuration File">
															2 Vendor Configuration File
														</option> -->
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													文件路径
												</TD>
												<TD width="" colspan="3">
													<div id="path_1">
													<%=file_path%>
													</div>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
											    <TD align="right" width="15%">
													文件名:
												</TD>
												<TD width="30%">
													设备ID.时间.<input type="text" name="filename_1" maxlength=255 class=bk size=15 onchange="showChild('filename_1')">&nbsp;.bin
												</TD>
												<TD align="right" width="15%">
													时延:
												</TD>
												<TD width="30%">
													<input type="text" name="delay_time_1" maxlength=255 class=bk size=20 value="0">
													&nbsp;&nbsp;(单位:s)
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" style="display:none">
												<TD align="right" width="15%">
													用户名:
												</TD>
												<TD width="30%">
													<input type="text" name="username_1" maxlength=255 class=bk size=20 onkeyup="ShowUser('1')">
												</TD>
												<TD align="right" width="15%">
													密码:
												</TD>
												<TD width="30%">
													<input type="text" name="passwd_1" maxlength=255 class=bk size=20 onkeyup="ShowUser('2')">
												</TD>
											</TR>											
											<TR bgcolor="#FFFFFF">
												<TD colspan="4">
													<BR>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TH colspan="4">
													软件升级
												</TH>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%" style="display:none">
													关键字:
												</TD>
												<TD width="30%" style="display:none">
													<input type="text" name="keyword_2" maxlength=255 class=bk size=20 value="software_download">
												</TD>
												<TD align="right" width="15%">
													文件类型:
												</TD>
												<TD width="30%" colspan=3>
													<input type="text" name="filetype_2" maxlength=255 class=bk size=30 value="1 Firmware Upgrade Image" readOnly>
													<!-- <select name="filetype_2" class="bk" readOnly>
														<option value="1 Firmware Upgrade Image">
															1 Firmware Upgrade Image
														</option> -->
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													文件路径
												</TD>
												<TD width="" colspan="3">
													<div id="path_2">
													<%=file_path2%>
													</div>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" style="display:none">
												<TD align="right" width="15%">
													用户名:
												</TD>
												<TD width="30%">
													<input type="text" name="username_2" maxlength=255 class=bk size=20>
												</TD>
												<TD align="right" width="15%">
													密码:
												</TD>
												<TD width="30%">
													<input type="text" name="passwd_2" maxlength=255 class=bk size=20>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													文件大小:
												</TD>
												<TD width="30%">
													<input type="text" name="file_size_2" maxlength=255 class=bk size=20 readOnly>
													&nbsp;&nbsp;(单位:字节)
												</TD>
												<TD align="right" width="15%">
													文件名:
												</TD>
												<TD width="30%">
													<input type="text" name="filename_2" maxlength=255 class=bk size=20>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													时延:
												</TD>
												<TD colspan="3">
													<input type="text" name="delay_time_2" maxlength=255 class=bk size=20 value="0" >
													&nbsp;&nbsp;(单位:s)
												</TD>

											</TR>
											<TR bgcolor="#FFFFFF" style="display:none">
												<TD align="right" width="15%">
													成功URL:
												</TD>
												<TD colspan="3">
													<input type="text" name="sucess_url_2" class=bk size=80>
												</TD>

											</TR>
											<TR bgcolor="#FFFFFF" style="display:none">
												<TD align="right" width="15%">
													失败URL:
												</TD>
												<TD colspan="3">
													<input type="text" name="fail_url_2" class=bk size=80>
												</TD>

											</TR>
											<TR bgcolor="#FFFFFF">
												<TD colspan="4">
													<BR>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TH colspan="4">
													配置恢复
												</TH>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%" style="display:none">
													关键字:
												</TD>
												<TD width="30%" style="display:none">
													<input type="text" name="keyword_3" maxlength=255 class=bk size=20 value="config_download">
												</TD>
												<TD align="right" width="15%">
													文件类型:
												</TD>
												<TD width="30%" colspan=3>
												    <input type="text" name="filetype_3" maxlength=255 class=bk size=30 value="3 Vendor Configuration File" readOnly>
													<!-- <select name="filetype_3" class="bk" readOnly>
														<option value="3 Vendor Configuration File">
															3 Vendor Configuration File
														</option> -->
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													文件路径
												</TD>
												<TD width="" colspan="3">
												   <input type="text" name="file_path_3" maxlength=255 class=bk size=80 readOnly value="">
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" style="display:none">
												<TD align="right" width="15%">
													用户名:
												</TD>
												<TD width="30%">
												    <input type="text" name="username_3" maxlength=255 class=bk size=20>
													<!--<input type="text" name="username_3" maxlength=255 class=bk size=20 onkeyup="ShowUser('3')">-->
												</TD>
												<TD align="right" width="15%">
													密码:
												</TD>
												<TD width="30%">
												    <input type="text" name="passwd_3" maxlength=255 class=bk size=20>
													<!--<input type="text" name="passwd_3" maxlength=255 class=bk size=20 onkeyup="ShowUser('4')"> -->
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													时延:
												</TD>
												<TD>
													<input type="text" name="delay_time_3" maxlength=255 class=bk size=20 value="0">
													&nbsp;&nbsp;(单位:s)
												</TD>
												<TD align="right" width="15%">
													文件名:
												</TD>
												<TD width="35%">
													<input type="text" name="filename_3" maxlength=255 class=bk size=20 readOnly>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" style="display:none">
												<TD align="right" width="15%">
													文件大小:
												</TD>
												<TD colspan="3">
													<input type="text" name="file_size_3" maxlength=255 class=bk size=20 value="20480">
													&nbsp;&nbsp;(单位:字节)
												</TD>

											</TR>
											<TR bgcolor="#FFFFFF" style="display:none">
												<TD align="right" width="15%">
													成功URL:
												</TD>
												<TD colspan="3">
													<input type="text" name="sucess_url_3" class=bk size=80>
												</TD>

											</TR>
											<TR bgcolor="#FFFFFF" style="display:none">
												<TD align="right" width="15%">
													失败URL:
												</TD>
												<TD colspan="3">
													<input type="text" name="fail_url_3" class=bk size=80>
												</TD>

											</TR>
											<TR>
												<TD colspan="4" align="right" class="green_foot">

													<INPUT TYPE="reset" value=" 重 置 " class=btn>
													&nbsp;
													<INPUT TYPE="submit" value=" 发 送 " class=btn>
													<INPUT NAME="type" TYPE="hidden"  value="1">
													<input type="hidden" name="hidden_url_path" value="">
													<input type ="hidden" name="auto_type" value="3">
												</TD>
											</TR>
										</TABLE>
									</TD>
								</TR>
							</TABLE>
						</td>
					</tr>
				</table>
			</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			&nbsp;
			<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display:none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
