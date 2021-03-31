<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.webtopo.common.DeviceCommonOperation,com.linkage.litms.webtopo.DeviceResourceInfo" %>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<%
request.setCharacterEncoding("GBK");
String oIndex = request.getParameter("oIndex");
String device_id = request.getParameter("device_id");
String type = request.getParameter("type");
String id = request.getParameter("id");
String vendorid = "-1";
//通过设备的device_id得到设备的名称和IP
DeviceResourceInfo deviceInfo = new DeviceResourceInfo();
Cursor cursor = deviceInfo.getDeviceResource(device_id);
Map myMap = cursor.getNext();
String deviceName= (String)myMap.get("device_name");
String loopbackip= (String)myMap.get("loopback_ip");
//得到厂商的select控件
String strVendorList = DeviceAct.getVendorList(true,"","");

DeviceCommonOperation dco=new DeviceCommonOperation(session);
if (Integer.parseInt(type) >= 0)
{
	vendorid = dco.getVendor_idBySerial(device_id);
}
%>
<script language="JavaScript">
<!--
var isCall = 0;
var isContinue = 0;
var iTimerID;
var iTimerContinue;
var oIndex = "<%=oIndex%>";
var vendorid = "<%=vendorid%>";
var type = "<%=type%>";
iTimerContinue = window.setInterval("CallContinue()", 100);
function CallPro()
{
	if (parseInt(isCall, 10) == 1)
	{
		opener.arrDev[oIndex]._type = frm.device_model.value;
		window.alert("修改对象类型成功!");
		window.clearInterval(iTimerID);
		window.close();
	}
}

function CallContinue()
{
	if (parseInt(isContinue, 10) == 1)
	{
		if (parseInt(type, 10) > 0)
		{
			frm.device_model.value = type;
		}
		window.clearInterval(iTimerContinue);
	}
}

function showChild(parname)
{
	if(parname=="vendor_id")
	{
		var o = document.all("vendor_id");
		var id = o.options[o.selectedIndex].value;
		document.all("childFrm").src = "getVendorDeviceModel.jsp?pause=true&vendor_id="+ id;
	}
}

function CheckForm()
{	
	if(frm.vendor_id.selectedIndex==0)
	{
		window.alert("请选择厂商");
		return ;
	}
	if(typeof(frm.device_model)!="object" || (typeof(frm.device_model)=="object" && frm.device_model.selectedIndex==0))
	{
		window.alert("请选择设备型号");
		return ;
	}
	var id = "<%=id%>";
	var type = "<%=type%>";
	var page = "webtopo_SaveDeviceType.jsp?id=" + id + "&type=" + frm.device_model.value;
	document.all("childFrm").src=page;
	iTimerID = window.setInterval("CallPro()", 1000);
}
//-->
</script>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD>
    <FORM NAME="frm" METHOD="post">
	<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH colspan="2" align="center">修改设备类型</TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="30%">设备名称</TD>
					<TD><%=deviceName%></TD>					
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="30%">设备IP</TD>
					<TD><%=loopbackip%></TD>					
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="30%">请选择厂商</TD>
					<TD><span id="span2"><%=strVendorList%></span></TD>					
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="30%">请选择设备型号</TD>
					<TD><span id=strModelList></span></TD>					
				</TR>
				<TR>
					<TD colspan="2" align="center" class=foot>
						<INPUT TYPE="button" value=" 保 存 " onclick="javascript:CheckForm();" class=btn>&nbsp;&nbsp;
						<INPUT TYPE="button" value=" 关 闭 " class=btn onclick="javascript:window.close();">						
					</TD>
				</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
    </FORM>
<TR><TD>&nbsp;</TD></TR>
<TR><TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>
&nbsp;</TD></TR>
</TABLE>
<script language="JavaScript">
<!--
frm.vendor_id.value = vendorid;
showChild("vendor_id");
//-->
</script>
<%@ include file="../foot.jsp"%>