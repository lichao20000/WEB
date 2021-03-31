<%--
Author		: Alex.Yan (yanhj@lianchuang.com)
Date		: 2007-11-28
Desc		: TR069: devicetype_list, add;
			  SNMP:	 Ddevicetype list.
--%>

<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import ="com.linkage.litms.resource.DeviceType,com.linkage.litms.resource.DeviceAct"%>
<%@ page import ="com.linkage.litms.resource.DeviceActUtil"%>
<%@ page import ="com.linkage.litms.LipossGlobals"%>
<%

request.setCharacterEncoding("GBK");

String instArea = LipossGlobals.getLipossProperty("InstArea.ShortName");

//�����ļ���snmp��tr069�豸������
int GwProtocol = LipossGlobals.getGwProtocol();	

String strData = "";
int offset;
String strClr = "";
DeviceAct act = new DeviceAct();
DeviceActUtil actUtil = new DeviceActUtil();
ArrayList list = actUtil.getDeviceTypeList(request);
Map areaMap = act.getAreaIdMapName();

String stroffset = request.getParameter("offset");
int pagelen = 15;
if(stroffset == null) offset = 1;
else offset = Integer.parseInt(stroffset);

String strBar = String.valueOf(list.get(0));
Cursor cursor = (Cursor)list.get(1);
Map fields = cursor.getNext();

strData = "<TABLE border=0 cellspacing=1 cellpadding=2 width='100%'> <TR> <TH colspan=8 >�豸�汾</TH></TR>";
strData += "<TR CLASS=green_title2><TD nowrap>�豸��Ӧ��</TD><TD nowrap>�豸�ͺ�</TD><TD nowrap>�ض��汾</TD><TD nowrap>Ӳ���汾</TD>";
strData += "<TD nowrap>����汾</TD><TD nowrap>������</TD><TD nowrap>����</TD></TR>";

if(fields == null){
	strData += "<TR ><TD class=column COLSPAN=8 HEIGHT=30>ϵͳ��û���豸������Ϣ</TD></TR>";
}
else{	
	int i=1;	
	while(fields != null){
		if((i%2)==0) strClr="#e7e7e7";
		else strClr = "#FFFFFF";
		
		String area_desc = "";
		if (areaMap.get(fields.get("area_id")) != null){
			area_desc = (String)areaMap.get(fields.get("area_id"));
		}

		strData += "<TR align= 'center'>";		
		//strData += "<TD  class=column1>"+ fields.get("devicetype_id") + "&nbsp;</TD>";
		strData += "<TD  class=column1 nowrap>"+ fields.get("vendor_add") + "&nbsp;</TD>";
        //strData += "<TD  class=column1>"+ fields.get("oui") + "&nbsp;</TD>";
		strData += "<TD  class=column1 nowrap>"+ fields.get("device_model") +"&nbsp;</TD>";
		strData += "<TD  class=column1 nowrap>"+ fields.get("specversion") +"&nbsp;</TD>";
        strData += "<TD  class=column1 nowrap>"+ fields.get("hardwareversion") + "&nbsp;</TD>";
        strData += "<TD  class=column1 nowrap>"+ fields.get("softwareversion") + "&nbsp;</TD>";
        strData += "<TD  class=column1 nowrap>"+ area_desc + "&nbsp;</TD>";
        strData += "<TD  class=column1 nowrap><A HREF='#' onclick=doSomething('" + fields.get("devicetype_id") + "')>�༭</A> | <A HREF=deviceTypeSave.jsp?action=delete&devicetype_id="+fields.get("devicetype_id")+" onclick='return delWarn();'>ɾ��</A></TD>";
		strData += "</TR>";
        i++;        
		fields = cursor.getNext();
	  }
	strData += "<TR><TD class=column COLSPAN=8 align=right>" + strBar + "</TD></TR>";
}

strData += "</TABLE>";


//OUI�б�
String strVendorList = act.getVendorList(true,"","vendor_id");
//�豸�ͺ��б�
String strDeviceModelList = actUtil.getDeviceModelList(false,"","device_model",null);

//�ɼ���
//String gatherList = act.getGatherList(session, "", "", true);


//snmp
String strSnmpData = "";
cursor = DeviceType.getDeviceTypeList(2);
fields = cursor.getNext();
strSnmpData = "<TABLE border=0 cellspacing=1 cellpadding=2 width='100%'><TR><TH  bgcolor='#ffffff' colspan=8>�豸�ͺ�</TH></TR>";
strSnmpData += "<TR CLASS=green_title2><TD width='10%'>���к�</TD><TD width='10%'>������ID</TD><TD width='10%'>��˾����</TD><TD width='20%'>�豸����</TD><TD width='40%'>�豸sysObjectID</TD><TD width='10%'>ϵͳ�汾</TD></TR>";
if(fields == null){
	strSnmpData += "<TR ><TD class=column COLSPAN=8 HEIGHT=30>ϵͳ��û���豸������Ϣ</TD></TR>";
}
else{
	if (fields != null){
	while(fields != null){
		strSnmpData += "<TR>";		
		strSnmpData += "<TD width=10% class=column1>"+ fields.get("serial") + "&nbsp;</TD>";
		strSnmpData += "<TD width=10% class=column1>"+ fields.get("vendor_id") + "&nbsp;</TD>";
        strSnmpData += "<TD width=10% class=column1>"+ fields.get("company") + "&nbsp;</TD>";
		strSnmpData += "<TD width=20% class=column1>"+ fields.get("device_name") +"&nbsp;</TD>";
		strSnmpData += "<TD width=40% class=column1>"+ fields.get("sys_id") +"&nbsp;</TD>";
        strSnmpData += "<TD width=10% class=column1>"+ fields.get("os_version") + "&nbsp;</TD>";
		strSnmpData += "</TR>";

		fields = cursor.getNext();
	  }
	}	
}

strSnmpData += "</TABLE>";

%>
<%@ include file="../head.jsp"%>

<SCRIPT LANGUAGE="JavaScript">

var instArea = '<%=instArea%>';

<!--

//-----------------ajax----------------------------------------
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
   
   //ajaxһ��ͨ�÷���
	function sendRequest(method,url,object){
		request.open(method, url, true);
		request.onreadystatechange = function(){refreshPage(object);};
		request.send(null);
	}
	function refreshPage(object){
		if (request.readyState == 4) {
    		if (request.status == 200) {
        		object.innerHTML = request.responseText;
			} else{
				alert("status is " + request.status);
			}
		}
	}
/**
	function sendRequest2(method, url, object,sparam){
		request.open(method, url, true);
		request.onreadystatechange = function(){
			refreshPage(object);
			doSomething(sparam);
		};
		request.send(null);
	}
	**/
//---------------------------------------------------------------

function Init(){

	var gwProtocol = "<%=GwProtocol%>";
	if(gwProtocol == 2){
		document.all.idAdd.style.display = "none";
		document.all.idTable.style.display = "none";
		document.all.idData.innerHTML="<%=strSnmpData%>";
	} 

}

//�����豸����
function changeDev(param){	
	if(param.value == 2){
		document.all.idAdd.style.display = "none";
		document.all.idTable.style.display = "none";
		document.all.idData.innerHTML="<%=strSnmpData%>";
	} else {
		document.all.idAdd.style.display = "";
		document.all.idTable.style.display = "";
		document.all.idData.innerHTML="<%=strData%>";
	}
}


//���
function Add() {
	document.all("DeviceTypeLabel").innerHTML = "";
	document.all("actLabel").innerHTML = "���";
	document.all("device_model").value = "-1";
	document.frm.reset();
	showChild("vendor_id");
}

function showChild(vendor_id,device_mode_id){
	if(instArea == 'gs_dx'){
		var type = typeof device_mode_id;
		if(type != 'string' && type != 'number'){
			device_mode_id = '';
		}
	}
//	var oui = document.all(vendor_id).value;
//	var url = "getDeviceModel.jsp?oui=" + oui + "&device_mode=" + device_mode;
	var vendorId = document.all(vendor_id).value;
	var url = "getDeviceModel.jsp?vendor_id=" + vendorId + "&device_mode_id=" + device_mode_id;
	var divObj = document.getElementById("deviceModel");
	sendRequest("post",url,divObj);
}

//-->
</SCRIPT>


<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<FORM NAME="frm" METHOD="post" ACTION="deviceTypeSave.jsp" onsubmit="return CheckForm()">
	<table width="98%" height="30" border="0" align="center"
			cellpadding="0" cellspacing="0" class="green_gargtd">
			<tr>
				<td width="162">
				<div align="center" class="title_bigwhite">������Դ</div>
				</td>
				<td>
					
					<%
						if(GwProtocol == 0){
					 %>
					<!-- ��'<font color="#FF0000">*</font>'�ı�������д��ѡ�� -->
					<img src="../images/attention_2.gif" width="15" height="12">
					<input type="radio" name="dev_type" value="1" onclick="changeDev(this);" checked>TR069�豸
					<input type="radio" name="dev_type" value="2" onclick="changeDev(this);">SNMP�豸
					<%
					
						}
					 %>
					 
					 
				</td>
				<td align="right">
					<A HREF='javascript:Add();'><div id="idAdd">����&nbsp;&nbsp;</div></A>
				</td>
			</tr>
		</table>
	<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999 id="idData">
              <%=strData%> 
			</TD>
		</TR>
	</TABLE>
	<br>
	<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center" id="idTable">
	  <TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH colspan="4" align="center"><SPAN id="actLabel">���</SPAN><SPAN id="DeviceTypeLabel"></SPAN>�豸����</TH>
					</TR>
					<TR bgcolor="#FFFFFF" id="vendor_idID">
						<TD class=column align="right" >�豸��Ӧ��</TD>
						<TD colspan=3><%=strVendorList %>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="device_ModelID">
						<TD class=column align="right" >�豸�ͺ�</TD>
						<TD colspan=3>
							<span id="deviceModel"><%=strDeviceModelList %></span>&nbsp;<font color="#FF0000">*</font>
						</TD>
					</TR>
<!-- 
					<TR bgcolor="#FFFFFF" style="display:none" id="device_modelID">
						<TD class=column align="right" >�豸�ͺ�</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="device_model" value="" readonly">&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
 -->
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" >�ض��汾</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="SpecVersion" maxlength=30 class=bk size=40>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" >Ӳ���汾</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="HardwareVersion" maxlength=30 class=bk size=40>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" >����汾</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="SoftwareVersion" maxlength=30 class=bk size=40>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">��������</TD>
						<TD>
						<INPUT TYPE="text" NAME="area_name" value="" readOnly class="bk"  onclick=areaSelect()>&nbsp;
						<INPUT TYPE="hidden" NAME="area_id" value="-1"><nobr class="BT" onmouseover="this.className='BTOver'" onmouseout="this.className='BT'" onclick="areaSelect();"><IMG SRC="../system/images/search.gif" WIDTH="15" HEIGHT="12" BORDER="0" ALT="��������" valign="middle">&nbsp;ѡ��</nobr>
						</TD>
					</TR>
					<TR >
						<TD colspan="4" align="right" CLASS=green_foot>
							<INPUT TYPE="submit" value=" �� �� " class=btn>&nbsp;&nbsp;
							<INPUT TYPE="hidden" name="action" value="add">							
							<input type="hidden" name="devicetype_id" value="">							
							<INPUT TYPE="reset" value=" �� д " class=btn>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
	</FORM>
</TD></TR>
<TR><TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>&nbsp;</TD></TR>
</TABLE>

<%@ include file="../foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">

Init();
//--------//
function doSomething(devicetype_id){
	var url = "updateDeviceType.jsp?devicetype_id=";
	document.all("childFrm").src = url + devicetype_id;
}
/**
function Edit(page){
	var url = "getDeviceModel.jsp?oui=";
	var divObj = document.getElementById("deviceModel");
	sendRequest2("post",url,divObj,page);
	return false;
}
**/
//--------//
function delWarn(){
	if(confirm("���Ҫɾ�����豸������\n��������ɾ���Ĳ��ָܻ�������")){
		return true;
	}
	else{
		return false;
	}
}

function CheckForm(){   
   temp =document.all("vendor_id").value;
   if(temp=="-1" || temp=="")
   {
     alert("��ѡ���̣�");
     return false;
   }
   temp =document.all("device_model").value;
   if(temp=="-1" || temp=="")
   {
     alert("��ѡ���豸�ͺţ�");
     return false;
   }
   temp =document.all("SpecVersion").value;
   if(temp=="")
   {
     alert("����д�ض��汾��");
     return false;
   }
   temp =document.all("HardwareVersion").value;
   if(temp=="")
   {
     alert("����дӲ���汾��");
     return false;
   }
   temp =document.all("SoftwareVersion").value;
   if(temp=="")
   {
     alert("����д����汾��");
     return false;
   }
   return true;
}


function areaSelect(){
	//var area_pid = document.all("area_id").value;
	var width = 360;
	var page = "../system/AreaSelect.jsp?area_pid=<%=user.getAreaId()%>&width="+ width;
	window.open(page,"","left=20,top=20,width="+ width +",height=450,resizable=no,scrollbars=no");	
	
}
</SCRIPT>

