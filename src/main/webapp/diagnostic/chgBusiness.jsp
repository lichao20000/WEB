<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.resource.DeviceAct"%>

<%
	DeviceAct deviceAct = new DeviceAct();
	String 	strCityList = deviceAct.getCityListSelf(true, "", "", request);
	
	String strVendorList = deviceAct.getVendorList(true, "", "");
%>
<%@ include file="../head.jsp"%>
<%@page import="com.linkage.litms.resource.DeviceAct"%>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript">
	var _device_id = null;
	var devicetype = null;
	function showChild(param)
	{
		if(param == "city_id"){
			document.frm.vendor_id.value = "-1";
		}
		if(param == "devicetype_id"){
				page = "getdevice_version.jsp?vendor_id="+document.frm.vendor_id.value + "&devicetype_id="+ encodeURIComponent(document.frm.devicetype_id.value);
				document.all("childFrm2").src = page;
			}
		if(param == "softwareversion")
		{	
			page = "getdevice_model.jsp?city_id="+document.frm.city_id.value+"&vendor_id="+document.frm.vendor_id.value+"&devicetype_id="+document.frm.devicetype_id.value+ "&softwareversion=" + document.frm.softwareversion.value + "&needFilter=true&refresh="+Math.random();	
			document.all("childFrm").src = page;
		}
		if(param == "vendor_id")
		{
			page = "getdevice_model_from.jsp?vendor_id="+document.frm.vendor_id.value;
			document.all("childFrm1").src = page;	
		}
	}
	
	
  	function ExecMod(){
  		obj = document.all;
  		vpi = obj.vpiid.value;
  		vci = obj.vciid.value;
  		if(CheckForm()){
  		  //var dev_id = obj.device_id.value.trim();
  		  //var connType = obj.connType.value.trim();
  		  //var username = obj.RouteAccount.value.trim();
  		  //var passwd = obj.RoutePasswd.value.trim();
  		  //if (connType == "PPPoE_Bridged") {
  		  //	page = "chgBusiness_exec.jsp?device_id="+dev_id+"&connType="+connType+"&refresh="+(new Date()).getTime();
  		  //} else {
  		  //	page = "chgBusiness_exec.jsp?device_id="+dev_id+"&connType="+connType+"&username="+username+"&passwd="+passwd+"&refresh="+(new Date()).getTime();
  		  //}
  		  
  		 page = "chgBusiness_exec.jsp";
  		 obj.frm.action = page;
  		 obj.frm.submit();
  		 showMsgDlg("����Ѱ������PVC(" + vpi + "/" +vci + ")...");
  		 
  		 //runningMsg.innerHTML = "����Ѱ������PVC��������ɾ�����ؽ�...";
  		 
  			/*t_obj = document.all('device_id');
          	var checkvalue = "";
          	if(typeof(t_obj.length)== "undefined"){
          		checkvalue = t_obj.value;
          	}	
          	for(var i=0; i<t_obj.length; i++){
       			if(t_obj[i].checked == true){
					checkvalue = t_obj[i].value;
				}
			}
			var param = "";
			if(devicetype =="snmp"){
				param = "&test_ip=" + document.frm.test_ip.value + "&time_out=" + document.frm.time_out.value;
			}else{
				param = "&Interface=" +document.frm.Interface.value+ "&Host=" +document.frm.Host.value+ "&NumberOfRepetitions=" +document.frm.NumberOfRepetitions.value+ "&Timeout=" +document.frm.Timeout.value+ "&DataBlockSize=" +document.frm.DataBlockSize.value;
			}				
       		page = "jt_device_zendan_from1_save.jsp?device_id=" + checkvalue+ "&devicetype=" + devicetype + param;
			
			document.all("childFrm").src = page;*/
		}else{
			return false;
		}	
    }

	/*function selectAll(elmID){
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
				}
			}
	
		} else {
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
	}
	*/
	function CheckChkBox() {
		var oSelect = document.all("device_id");
		if(typeof(oSelect) == "object" ) {
			if(typeof(oSelect.length) != "undefined") {
				for(var i=0; i<oSelect.length; i++) {
					if(oSelect[i].checked) {
						return true;
					}
				}
				return false;
			}
			else {
		    //  alert(oSelect.checked);
				if(oSelect.checked) {
					return true;
				}
				return false;
			}
		} 
	}
	
	function CheckForm(){
	
		var rule = /^[0-9]*[1-9][0-9]*$/;//������ʽ��/��/֮��    	
		var obj = document.frm;
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
		if(checkType==0 && document.frm.city_id.value == -1){
			alert("��ѡ�����أ�");
			document.frm.gather_id.focus();
			return false;
	  	}
	  	if(checkType==0 && document.frm.vendor_id.value == -1){
			alert("��ѡ���̣�");
			document.frm.vendor_id.focus();
			return false;
	 	}
 		if(checkType==0 && document.frm.devicetype_id.value == -1){
			alert("��ѡ���豸�ͺţ�");
			document.frm.devicetype_id.focus();
			return false;
	 	}
 		if(checkType==0 && document.frm.softwareversion.value == -1){
			alert("��ѡ���豸�汾��");
			document.frm.softwareversion.focus();
			return false;
		}
		
		if(checkType==1 && ""==hguser && ""==telephone){
		  alert("����д�û�����绰���룡");
		  document.all("hguser").focus();
		  return false;
		} else if(!CheckChkBox()){
		  alert("��ѡ������豸��");
		  return false;
	    }
	    if (("" == obj.vpiid.value)) {
		  alert("������VPI��");
		  document.all("vpiid").focus();
		  return false;
		}
		if (("" == obj.vciid.value)) {
		  alert("������VCI��");
		  document.all("vciid").focus();
		  return false;
		}
		if (("" == obj.RouteAccount.value) && (obj.connType.value == "1021")) {
		  alert("������豸�Ƿ��Ӧ���û���");
		  document.all("RouteAccount").focus();
		  return false;
		}
		if (("" == obj.RoutePasswd.value) && (obj.connType.value == "1021")) {
		  alert("����д��д���");
		  document.all("RoutePasswd").focus();
		  return false;
		}
		return true;					
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
	      page="getdevice_model.jsp?hguser="+hguser+"&telephone="+telephone+"&needFilter=true&refresh="+Math.random();
	      document.all("childFrm1").src = page;
	   }   
	}


function relateDeviceBySerialno()
{
   var serialnumber=document.all("serialnumber").value;
   var loopback_ip=document.all("loopback_ip").value;
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
   if(checkType==2 && serialnumber.length < 6 && loopback_ip == "")
   {
	  alert('�������������6λ���кŽ��в�ѯ!');
      document.all("serialnumber").focus();
      return false;
   }
   else if(checkType==2)
   {
      var page="";
      page="getdevice_model.jsp?serialnumber="+serialnumber+"&loopback_ip=" + loopback_ip + "&needFilter=true&refresh="+Math.random();
      //alert(page);
      document.all("childFrm1").src = page;
   }
   
}

function ShowDialog(param)
{
  //�����û�����ѯ
  if(param==1)
  {
     //tr1.style.display="none";
     //tr2.style.display="none";
     tr3.style.display="";
     tr4.style.display="none";
  }
  
  //�����豸�汾����ѯ
  if(param==0)
  {
     //tr1.style.display="";
     //tr2.style.display="";
     tr3.style.display="none";
     tr4.style.display="none";
  }
  
  //�����豸���к�����ѯ
  if(param==2)
  {
     //tr1.style.display="none";
     //tr2.style.display="none";
     tr3.style.display="none";
     tr4.style.display="";
  }
}
function filterByDevIDAndDevTypeID(obj){
	document.all.RouteAccount.value = obj.username;
	document.all.RoutePasswd.value = obj.passwd;
	//if(obj.devicetype == "snmp"){
	//	line4.style.display = "";
	//	line1.style.display = "none";
	//	line2.style.display = "none";
	//	line3.style.display = "none";
	//	devicetype = "snmp";
	//} else {
	//	line4.style.display = "none";
	//	line1.style.display = "";
	//	line2.style.display = "";
	//	line3.style.display = "";
	//	devicetype = "tr069";
	//}
}

function chgConnType() {
	obj = document.all;
	if (obj.connType.value == "1022") {
		line1.style.display = "none";
	} else {
		line1.style.display = "";
	}
	
}

function showMsgDlg(strMsg){
	w = document.body.clientWidth;
	h = document.body.clientHeight;

	l = (w-250)/2;
	t = (h-60)/2;
	PendingMessage.style.left = l;
	PendingMessage.style.top  = t;
	PendingMessage.style.display="";
	document.all.txtLoading.innerText=strMsg;
}
function closeMsgDlg(){
	PendingMessage.style.display="none";
}

</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<div id="PendingMessage"
	style="position:absolute;z-index:3;top:240px;left:250px;width:250;height:60;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;display:none">
<center>
<table border="0">
	<tr>
		<td valign="middle"><img src="../images/cursor_hourglas.gif"
			border="0" WIDTH="30" HEIGHT="30"></td>
		<td>&nbsp;&nbsp;</td>
		<td valign="middle">
			<span id=txtLoading style="font-size:14px;font-family: ����"></span>
		</td>
	</tr>
</table>
</center>
</div>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="POST" ACTION="" onSubmit="return CheckForm();">
				<table width="95%" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162">
										<div align="center" class="title_bigwhite">
											��������
										</div>
									</td>
									<td><img src="../images/attention_2.gif" width="15"
										height="12">&nbsp;��ѯ��ʽ�� 
										<!-- <input
										type="radio" value="0" onclick="ShowDialog(this.value)"
										name="checkType">�����غ��ͺ�&nbsp;&nbsp;  -->
										
										<input type="radio" value="1" onclick="ShowDialog(this.value)"
										name="checkType" checked>���û�&nbsp;&nbsp; 
										<input type="radio" value="2" onclick="ShowDialog(this.value)" 
										name="checkType">���豸
									</td>

								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td bgcolor="#FFFFFF">
							<table width="100%" border=0 align="center" cellpadding="1"
								cellspacing="1" bgcolor="#999999" class="text">
								<TR bgcolor="#FFFFFF">
									<TH colspan="4">
										����������ʽ
									</TH>
								</TR>
								<!-- 
								<TR bgcolor="#FFFFFF" id="tr1" STYLE="display:none">
									<TD align="right" width="10%">
										����:
									</TD>
									<TD align="left" width="30%">
										<%/*=strCityList*/%>
									</TD>
									<TD align="right" width="10%">
										����:
									</TD>
									<TD align="left" width="30%">
										<%/*=strVendorList*/%>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" id="tr2" STYLE="display:none">
									<TD align="right" width="10%">
										�豸�ͺ�:
									</TD>
									<TD width="30%">
										<div id="div_devicetype">
											<select name=devicetype_id class="bk">
												<option value="-1">
													--����ѡ����--
												</option>
											</select>
										</div>
									</TD>
									<TD align="right" width="10%">
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
								 -->
								 
								<TR bgcolor="#FFFFFF" id="tr3" STYLE="display:">
									<TD align="right" width="10%">
										�û���:
									</TD>
									<TD width="30%">
										<input type="text" name="hguser" class="bk" value="">
									</TD>
									<TD align="right" width="10%">
										�û��绰����:
									</TD>
									<TD width="30%">
										<input type="text" name="telephone" class="bk" value="">
										<input type="button" class=btn value=" �� ѯ "
											onclick="relateDevice()">
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" id="tr4" STYLE="display:none">
									<TD align="right" width="10%">
										�豸���к�:
									</TD>
									<TD width="30%">
										<input type="text" name="serialnumber" value="" class=bk><font color="red">���������λ</font>
									</TD>
									<TD align="right" width="10%">�豸������IP:</TD>
									<TD width="30%"><input type="text" name="loopback_ip" value=""
										class=bk> <input type="button" class=btn value=" �� ѯ "
										onclick="relateDeviceBySerialno()">
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD align="right" width="10%">
										�豸�б�:
										<br>
									</TD>
									<TD colspan="3">
										<div id="div_device"
											style="width:95%; height:100px; z-index:1; top: 100px; overflow:scroll">
											<span>��ѡ���豸��</span>
										</div>
									</TD>
								</TR>
								<tr bgcolor="#FFFFFF" id="line2">
									
									<TD align="right" width="15%">
										VPI/VCI��
									</TD>
									<TD colspan="" width="30%">
										<input type="text" name="vpiid" size="2"
											value="" class="bk">&nbsp;/&nbsp;<input type="text" name="vciid" size="3"
											value="" class="bk">
										<font color=red>*</font>&nbsp;&nbsp;����Ҫ���ĵ�PVC��
									</TD>
				
									<td nowrap class=column align="right">
										������ʽ��
									</td>
									<td class=column nowrap>
										<select name="connType" class="bk" onChange="chgConnType()">
											<option value="1021" selected>·��</option>
											<option value="1022">�Ž�</option>
										</select>	
									</td>
								</tr>
								
								<tr bgcolor="#FFFFFF" id="line1" style="display:">
									<td nowrap class=column align="right">
										�û�����
									</td>
									<td class=column nowrap>
										<input type="text" name="RouteAccount" class="bk" value="" readonly>
									</td>
									<td nowrap class=column align="right">
										���
									</td>
									<td class=column>
										<input type="text" name="RoutePasswd" class="bk" value="">&nbsp;&nbsp;<font color=red>*</font>
									</td>
								</tr>
								
								<tr bgcolor="#FFFFFF">
									<td  colspan="4">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="0">
											<tr align="right" CLASS="green_foot">
												<td>
													<INPUT TYPE="button" value=" ��ʼ���� " class=btn onclick="ExecMod()">
													&nbsp;&nbsp;
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td height="10">
						</td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td colspan="4" align="center" id="runningMsg">
						</td>
					</tr>
				</table>
			</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display:none"></IFRAME>
			&nbsp;
		</TD>
	</TR>
</TABLE>
<table width="100%" border="0" cellspacing="0" cellpadding="0">

</table>											
<%@ include file="../foot.jsp"%>
