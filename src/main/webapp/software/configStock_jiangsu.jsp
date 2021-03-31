<%--
FileName	: configStock.jsp
Author		: lizhaojun
Date		: 2007��5��10��
Desc		: �汾����.
since       : V1.0, 2007��5��10��

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
	//�ɼ���������
	String gatherList = deviceAct.getGatherList(session, "", "", false);
	
	//�豸����
	String strVendorList = DeviceAct.getVendorList(true, "", "");
	//String deviceModel = versionManage.getDeviceTypeList("", true);

	String file_path = versionManage.getFilePath_1("file_path_1");
	String file_path2 = versionManage.getVersionFilePathToConfigStock("");
	//String file_path3 = versionManage.getFilePath_2("file_path_3","","");
	
	//�������
	String opeResult = request.getParameter("operResult");
	//����ITMS��BBMS���ܲ˵�
	String gw_type = request.getParameter("gw_type");

%>
<%@ include file="../head.jsp"%>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript">
<%if(null!=opeResult)
    {  
    	if("true".equals(opeResult))
    	{
    		out.println("alert('���Զ��Ƴɹ���');");
    	}
    	else if("false".equals(opeResult))
    	{
    		out.println("alert('���Զ���ʧ�ܣ�');");
    	}
    	else if("notAllow".equals(opeResult))
    	{
    		out.println("alert('һ���豸���ܶ������ְ汾�������ԣ�������ѡ���豸��')");
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
			page += "&gw_type="+<%=gw_type%> ;     //����ITMS��BBMS
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
       alert("����д�������ƣ�");
       document.frm.task_name.focus();
       return false;
    }
    if(excute_type==0&&document.all("task_name").value=="")
    {
       alert("����д�������ƣ�");
       document.frm.task_name.focus();
       return false;
    }
	if(checkType==0&&document.frm.gather_id.value == -1){
		alert("��ѡ��ɼ��㣡");
		document.frm.gather_id.focus();
		return false;
  	}
  	 if(checkType==0&&document.frm.vendor_id.value == -1){
		alert("��ѡ���̣�");
		document.frm.vendor_id.focus();
		return false;
 	}
 	if(checkType==0&&document.frm.devicetype_id.value == -1){
		alert("��ѡ���豸�ͺţ�");
		document.frm.devicetype_id.focus();
		return false;
 	}
 	if(checkType==0&&document.frm.softwareversion.value == -1){
			alert("��ѡ���豸�汾��");
			document.frm.softwareversion.focus();
			return false;
		}
		
	if(checkType==1&&""==hguser&&""==telephone)
	{
	  alert("����д�û�����绰���룡");
	  document.all("hguser").focus();
	  return false;
	}
				var oselect = document.all("device_id");
		if(oselect == null){
			alert("��ѡ���豸��");
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
			alert("��ѡ���豸��");
			return false;
		}
		var obj = document.frm;
		if(obj.file_path_1.value == -1){
			alert("��ѡ�񱸷������ļ�·����");
			obj.file_path_1.focus();
			return false;
		}
		if(!IsNull(obj.filename_1.value,'���������ļ�����')){
		    alert("����д���������ļ����ƣ�");
			obj.filename_1.focus();
			return false;
		}
		if(!IsNull(obj.delay_time_1.value,'��������ʱ��')){
			obj.delay_time_1.focus();
			obj.delay_time_1.select();
			return false;
		}		
//-------------------------------------------------------------------
		if(obj.file_path_2.value == -1){
			alert("��ѡ����������ļ�·����");
			obj.file_path_2.focus();
			return false;
		}
		if(!IsNull(obj.file_size_2.value,'��������ļ���С')){
			obj.file_size_2.focus();
			obj.file_size_2.select();
			return false;
		}	
		if(!IsNull(obj.filename_2.value,'��������ļ���')){
			obj.filename_2.focus();
			obj.filename_2.select();
			return false;
		}
		if(!IsNull(obj.delay_time_2.value,'�������ʱ��')){
			obj.delay_time_2.focus();
			obj.delay_time_2.select();
			return false;
		}			
//-------------------------------------------------------------------

		if(obj.file_path_3.value == -1){
			alert("��ѡ�����ûָ��ļ�·����");
			bj.file_path_3.focus();
			return false;
		}
		if(!IsNull(obj.file_size_3.value,'���ûָ��ļ���С')){
			obj.file_size_3.focus();
			obj.file_size_3.select();
			return false;
		}	
		if(!IsNull(obj.filename_3.value,'���ûָ��ļ���')){
			obj.filename_3.focus();
			obj.filename_3.select();
			return false;
		}
		
		if(obj.filename_3.value!=obj.filename_1.value)
		{
		    alert("���ûָ��Ǳߵ������ļ����ƣ����������ñ����Ǳ�һ�£�");
		    obj.filename_3.focus();
			obj.filename_3.select();
			return false;
		}
		if(!IsNull(obj.delay_time_3.value,'���ûָ�ʱ��')){
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
      alert("����д�û�����绰���룡");
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
      alert("����д���������λ�豸���кţ�");
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
  //�����û�����ѯ
  if(param==1)
  {
     tr1.style.display="none";
     tr2.style.display="none";
     tr3.style.display="";
     tr4.style.display="none";
  }
  
  //�����豸�汾����ѯ
  if(param==0)
  {
     tr1.style.display="";
     tr2.style.display="";
     tr3.style.display="none";
     tr4.style.display="none";
  }
  
  //�����豸���к�����ѯ
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
   //���ѡ������ִ�л�ƻ�ִ������
   if(1==param||0==param)
   {
      tr0.style.display="none";
   }
   
   //ѡ���Զ�ִ������
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
										�����������
									</td>
									<td nowrap>
										<img src="../images/attention_2.gif" width="15" height="12">
										��ѡ����豸������������������á�
										<input type="radio" value="0" onclick="ShowDialog(this.value)" name="checkType" checked>�����豸�汾��ѯ&nbsp;&nbsp;
			                            <input type="radio" value="1" onclick="ShowDialog(this.value)" name="checkType">�����û���ѯ�豸&nbsp;&nbsp;
			                            <input type="radio" value="2" onclick="ShowDialog(this.value)" name="checkType">�����豸���кŲ�ѯ
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
													���Ʊ�����������
												</TH>										
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%" nowrap>
													ҵ������:
												</TD>
												<TD width="35%">
													<select name="service_id" class="bk" readOnly>
														<option value="6">
															���������������
														</option>
													</select>
												</TD>
												<TD align="right" width="15%">
													ִ�з�ʽ:
												</TD>
												<TD width="35%">
													<input type="radio" name="excute_type" value="0" onclick="displayType(this.value)">
													����ִ��
													<input type="radio" name="excute_type" value="2" onclick="displayType(this.value)" checked>
													���Ʋ���
												</TD>																								
											<tr bgcolor="#FFFFFF">
												<TD align="right" width="15%">
											    �������ƣ�
											    </TD>
											    <TD width="35%" colspan=3>
											    <input type=text name="task_name" class=bk value="">
											    </TD>
											</tr>
											<TR bgcolor="#FFFFFF" id="tr0" STYLE="display:">
											    <TD align="right" width="15%">
													����ִ��ʱ��:
												</TD>
												<TD width="35%" colspan=3>
													<select name="auto_excutetime_type" class="bk">
														<option value="1">
															�豸��ʼ��װ��һ������ʱ�Զ�����
														</option>
														<option value="2">
															�豸Periodic Inform�Զ�����
														</option>
														<option value="3">
															�豸��������ʱ�Զ�����
														</option>
														<option value="4" selected>
															�豸�´����ӵ�ITMSʱ�Զ�����
														</option>
													</select>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr1" STYLE="display:">
											    <TD align="right" width="15%">
													�ɼ���:
												</TD>
												<TD width="35%">
													<%=gatherList%>
												</TD>
												
												<TD align="right" width="15%">
													����:
												</TD>
												<TD width="35%">
													<%=strVendorList%>
												</TD>
											</TR>											
											<TR bgcolor="#FFFFFF" id="tr2" STYLE="display:">	
												<TD align="right" width="15%">
													�豸�ͺ�:
												</TD>
												<TD width="30%">
													<div id="div_devicetype">
														<select name="devicetype_id" class="bk">
															<option value="-1">
																--����ѡ����--
															</option>
														</select>
													</div>
												</TD>
												<TD align="right" width="20%">
													�豸�汾:
												</TD>
												<TD width="30%">
													<div id="div_deviceversion">
														<select name="device_version" class="bk">
															<option value="-1">
																--����ѡ���豸�ͺ�--
															</option>
														</select>													
													</div>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr3" STYLE="display:none">
											    <TD align="right" width="15%">
													�û���:
												</TD>
												<TD width="35%">
													<input type="text" name="hguser" class=bk value="">
												</TD>
												<TD align="right" width="15%">
													�û��绰����:
												</TD>
												<TD width="35%">
													<input type="text" name="telephone" class=bk value="">
													<input type="button" class=btn value="��ѯ" onclick="relateDevice()">
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr4" STYLE="display:none">
											    <TD align="right">
													�豸���к�:
												</TD>
												<TD>
													<input type="text" name="serialnumber" value="" class=bk><font color="red">���������λ</font>
												</TD>
												<TD colspan=2>
													<input type="button" class=btn value="��ѯ" onclick="relateDeviceBySerialno()">
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right">
													�豸�б�:
													<br>
													<INPUT TYPE="checkbox" onclick="selectAll('device_id')" name="device">
													ȫѡ
												</TD>
												<TD colspan="3">
													<div id="div_device" style="width:95%; height:100px; z-index:1; top: 100px; overflow:scroll">
														<span>��ѡ���豸��</span>
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
													��������
												</TH>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%" style="display:none">
													�ؼ���:
												</TD>
												<TD width="30%" style="display:none">
													<input type="text" name="keyword_1" maxlength=255 class=bk size=20 value="config_upload">
												</TD>
												<TD align="right" width="15%">
													�ļ�����:
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
													�ļ�·��
												</TD>
												<TD width="" colspan="3">
													<div id="path_1">
													<%=file_path%>
													</div>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
											    <TD align="right" width="15%">
													�ļ���:
												</TD>
												<TD width="30%">
													�豸ID.ʱ��.<input type="text" name="filename_1" maxlength=255 class=bk size=15 onchange="showChild('filename_1')">&nbsp;.bin
												</TD>
												<TD align="right" width="15%">
													ʱ��:
												</TD>
												<TD width="30%">
													<input type="text" name="delay_time_1" maxlength=255 class=bk size=20 value="0">
													&nbsp;&nbsp;(��λ:s)
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" style="display:none">
												<TD align="right" width="15%">
													�û���:
												</TD>
												<TD width="30%">
													<input type="text" name="username_1" maxlength=255 class=bk size=20 onkeyup="ShowUser('1')">
												</TD>
												<TD align="right" width="15%">
													����:
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
													�������
												</TH>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%" style="display:none">
													�ؼ���:
												</TD>
												<TD width="30%" style="display:none">
													<input type="text" name="keyword_2" maxlength=255 class=bk size=20 value="software_download">
												</TD>
												<TD align="right" width="15%">
													�ļ�����:
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
													�ļ�·��
												</TD>
												<TD width="" colspan="3">
													<div id="path_2">
													<%=file_path2%>
													</div>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" style="display:none">
												<TD align="right" width="15%">
													�û���:
												</TD>
												<TD width="30%">
													<input type="text" name="username_2" maxlength=255 class=bk size=20>
												</TD>
												<TD align="right" width="15%">
													����:
												</TD>
												<TD width="30%">
													<input type="text" name="passwd_2" maxlength=255 class=bk size=20>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													�ļ���С:
												</TD>
												<TD width="30%">
													<input type="text" name="file_size_2" maxlength=255 class=bk size=20 readOnly>
													&nbsp;&nbsp;(��λ:�ֽ�)
												</TD>
												<TD align="right" width="15%">
													�ļ���:
												</TD>
												<TD width="30%">
													<input type="text" name="filename_2" maxlength=255 class=bk size=20>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													ʱ��:
												</TD>
												<TD colspan="3">
													<input type="text" name="delay_time_2" maxlength=255 class=bk size=20 value="0" >
													&nbsp;&nbsp;(��λ:s)
												</TD>

											</TR>
											<TR bgcolor="#FFFFFF" style="display:none">
												<TD align="right" width="15%">
													�ɹ�URL:
												</TD>
												<TD colspan="3">
													<input type="text" name="sucess_url_2" class=bk size=80>
												</TD>

											</TR>
											<TR bgcolor="#FFFFFF" style="display:none">
												<TD align="right" width="15%">
													ʧ��URL:
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
													���ûָ�
												</TH>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%" style="display:none">
													�ؼ���:
												</TD>
												<TD width="30%" style="display:none">
													<input type="text" name="keyword_3" maxlength=255 class=bk size=20 value="config_download">
												</TD>
												<TD align="right" width="15%">
													�ļ�����:
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
													�ļ�·��
												</TD>
												<TD width="" colspan="3">
												   <input type="text" name="file_path_3" maxlength=255 class=bk size=80 readOnly value="">
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" style="display:none">
												<TD align="right" width="15%">
													�û���:
												</TD>
												<TD width="30%">
												    <input type="text" name="username_3" maxlength=255 class=bk size=20>
													<!--<input type="text" name="username_3" maxlength=255 class=bk size=20 onkeyup="ShowUser('3')">-->
												</TD>
												<TD align="right" width="15%">
													����:
												</TD>
												<TD width="30%">
												    <input type="text" name="passwd_3" maxlength=255 class=bk size=20>
													<!--<input type="text" name="passwd_3" maxlength=255 class=bk size=20 onkeyup="ShowUser('4')"> -->
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													ʱ��:
												</TD>
												<TD>
													<input type="text" name="delay_time_3" maxlength=255 class=bk size=20 value="0">
													&nbsp;&nbsp;(��λ:s)
												</TD>
												<TD align="right" width="15%">
													�ļ���:
												</TD>
												<TD width="35%">
													<input type="text" name="filename_3" maxlength=255 class=bk size=20 readOnly>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" style="display:none">
												<TD align="right" width="15%">
													�ļ���С:
												</TD>
												<TD colspan="3">
													<input type="text" name="file_size_3" maxlength=255 class=bk size=20 value="20480">
													&nbsp;&nbsp;(��λ:�ֽ�)
												</TD>

											</TR>
											<TR bgcolor="#FFFFFF" style="display:none">
												<TD align="right" width="15%">
													�ɹ�URL:
												</TD>
												<TD colspan="3">
													<input type="text" name="sucess_url_3" class=bk size=80>
												</TD>

											</TR>
											<TR bgcolor="#FFFFFF" style="display:none">
												<TD align="right" width="15%">
													ʧ��URL:
												</TD>
												<TD colspan="3">
													<input type="text" name="fail_url_3" class=bk size=80>
												</TD>

											</TR>
											<TR>
												<TD colspan="4" align="right" class="green_foot">

													<INPUT TYPE="reset" value=" �� �� " class=btn>
													&nbsp;
													<INPUT TYPE="submit" value=" �� �� " class=btn>
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
