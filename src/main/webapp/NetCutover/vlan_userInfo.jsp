<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.netcutover.*,java.util.Map,com.linkage.litms.common.database.Cursor"%>
<%

String _type = request.getParameter("type");


String uname="";
String password = "";
String device_id  = "";
String gather_id = "";
String devicetype_id = "";
String _oui = "";
String _device_serialnumber = "";
String e_id = "";
String e_username = "";
String e_passwd = "";

Map  fields = SheetManage.egUsrMap(request);

int flag = 0;
if(fields != null ){
	flag = 0;
	uname= (String)fields.get("username");
	password = (String)fields.get("passwd");
	device_id  = (String)fields.get("device_id");
	gather_id = (String)fields.get("gather_id");
	devicetype_id = (String)fields.get("devicetype_id");
	_oui = (String)fields.get("oui");
	_device_serialnumber = (String)fields.get("device_serialnumber");
	e_id = (String)fields.get("e_id");
	if(e_id == null){
		e_id = "";
	}
	e_username = (String)fields.get("e_username");
	if(e_username == null){
		e_username = "";
	}
	e_passwd = (String)fields.get("e_passwd");
	if(e_passwd == null){
		e_passwd = "";
	}
}else{
	flag = -1;
}
%>
<link rel="stylesheet" href="../css/listview.css" type="text/css">
<SCRIPT LANGUAGE="JavaScript">

</SCRIPT>
<div id="idBody">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD>
	<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center" >
	  <TR><TD bgcolor=#999999>
		  <TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="idTable">
			<TR bgcolor="#ffffff" >
			  <TH colspan="4">用户设备信息</TH>
			</TR>
			<TR bgcolor="#ffffff">
			  <TD align="right">用户名称</TD>
			  <TD colspan=""><input type="text" name="username" value="<%=uname%>" class="bk">&nbsp;&nbsp;<font color=red>*</font></TD>
			  <TD align="right">用户密码</TD>
			  <TD colspan=""><input type="text" name="pwd" value="<%=password%>" class="bk">&nbsp;&nbsp;<font color=red>*</font></TD>
			</TR>
			<TR bgcolor="#ffffff">
			  <TD align="right">绑定设备</TD>
			  <TD colspan="3">
				<input type="text" size="10" name="old_oui" value="<%=_oui%>" class="bk" readOnly>&nbsp;-&nbsp;<input type="text" size="40" name="old_device_serialnumber" value="<%=_device_serialnumber%>" class="bk" readOnly>
			  </TD>
			</TR>
			<TR bgcolor="#ffffff">
				<TD align="right">
					企业ID：
				</TD>
				<TD colspan="3">
					<input type="text" name="e_id" value="<%=e_id%>" class="bk" readOnly>&nbsp;&nbsp;<font color=red>*</font>
				</TD>
			</TR>
			
			<TR bgcolor="#ffffff">
				<TD align="right">
					企业帐号：
				</TD>
				<TD colspan="">
					<input type="text" name="e_username" value="<%=e_username%>" class="bk" readOnly>&nbsp;&nbsp;<font color=red>*</font>
				</TD>
				<TD align="right">
					企业密码：
				</TD>
				<TD colspan="">
					<input type="text" name="e_passwd" value="<%=e_passwd%>" class="bk" readOnly>&nbsp;&nbsp;<font color=red>*</font>
				</TD>
			</TR>
			<TR bgcolor="#ffffff">
				<TD align="right">
					SSID1用户名:
				</TD>
				<TD colspan="">
					<input type="text" name="ssid1_username" value="bnet" class="bk">&nbsp;&nbsp;<font color=red>*</font>
				</TD>
				<TD align="right">
					SSID1密码：
				</TD>
				<TD colspan="">
					<input type="text" name="ssid1_passwd" value="12345678" class="bk">&nbsp;&nbsp;<font color=red>*</font>
				</TD>
			</TR>
			<TR bgcolor="#ffffff">
				<TD align="right">
					SSID2用户名:
				</TD>
				<TD colspan="">
					<input type="text" name="ssid2_username" value="bnet-guest" class="bk">&nbsp;&nbsp;<font color=red>*</font>
				</TD>
				<TD align="right">
					SSID2密码：
				</TD>
				<TD colspan="">
					<input type="text" name="ssid2_passwd" value="12345678" class="bk">&nbsp;&nbsp;<font color=red>*</font>
				</TD>
			</TR>
			<TR bgcolor="#ffffff">
				<TD align="right">
					VLAN2绑定端口：
				</TD>
				<TD colspan="3">
					<input type="checkbox" id="LanInterface"
						name="LanInterface"
						value="InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.1"
						>
					LAN1 &nbsp;
					<input type="checkbox" id="LanInterface"
						name="LanInterface"
						value="InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.2"
						>
					LAN2 &nbsp;
					<input type="checkbox" id="LanInterface"
						name="LanInterface"
						value="InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.3"
						>
					LAN3 &nbsp;
					<input type="checkbox" id="LanInterface"
						name="LanInterface"
						value="InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.4"
						>
					LAN4 &nbsp;
					<input type="checkbox" id="LanInterface"
						name="LanInterface"
						value="InternetGatewayDevice.LANDevice.1.WLANConfiguration.1"
						>
					WLAN1 &nbsp;
					<input type="checkbox" id="LanInterface"
						name="LanInterface"
						value="InternetGatewayDevice.LANDevice.1.WLANConfiguration.2"
						checked >
					WLAN2 &nbsp;											
				</TD>
			</TR>	
			<TR>
				<TD class=foot colspan=4 align=right>
					<input type="hidden" name="device_id" value="<%=device_id%>">
					<input type="hidden" name="gather_id" value="<%=gather_id%>">
					<input type="hidden" name="devicetype_id" value="<%=devicetype_id%>">
				  <input type="button" name="cmdSend" class=btn  value=" 发送工单 " onclick="CheckForm();">
				</TD>
			</TR>

		  </TABLE>
	  </TD></TR>
	</TABLE>
</TD></TR>
</TABLE>
</div>
<SCRIPT LANGUAGE="JavaScript">
<!--
var _flag = "<%=flag%>";
if(_flag == -1){
	parent.document.all("idBody").style.display = "none";
	alert("当前系统中没有此帐号或者设备不存在，请确认后重新输入！");
	
}else{
	parent.document.all("idBody").style.display = "";
	parent.document.all("idBody").innerHTML = idBody.innerHTML;
}
//-->
</SCRIPT>
