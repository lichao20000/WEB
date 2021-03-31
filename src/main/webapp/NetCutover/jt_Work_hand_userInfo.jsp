<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.netcutover.*,java.util.Map,com.linkage.litms.common.database.Cursor"%>

<%--
	zhaixf(3412) 2008-04-11
	req:XJDX_ITMS-REQ-20080411-CZM-001
--%>

<%

String _type = "";
String oper_type_id = request.getParameter("oper_type_id");
String serv_type_id = request.getParameter("serv_type_id");


String uname="";
String password = "";
String vpiid="";
String vciid="";
String vlanid="";
String device_id  = "";
String gather_id = "";
String devicetype_id = "";
String _oui = "";
String _device_serialnumber = "";
String wan_value_1 = "";
String wan_value_2 = "";
String wan_type = "";
String wan_type_name = "桥接";
//静态IP上网方式
String ipaddress = "";
String ipmask = "";
String gateway = "";
String adsl_ser = "";

Map fields = SheetManage.usrMap(request);

int flag = 0;
if(fields != null ){
	flag = 0;
	uname= (String)fields.get("username");
	password = (String)fields.get("passwd");
	vpiid=  (String)fields.get("vpiid");
	vciid=  (String)fields.get("vciid");
	vlanid= (String)fields.get("vlanid");
	device_id  = (String)fields.get("device_id");
	gather_id = (String)fields.get("gather_id");
	devicetype_id = (String)fields.get("devicetype_id");
	_oui = (String)fields.get("oui");
	_device_serialnumber = (String)fields.get("device_serialnumber");
	wan_value_1 = (String)fields.get("wan_value_1");
	wan_value_2 = (String)fields.get("wan_value_2");
	
	ipaddress = (String)fields.get("ipaddress");
	ipmask = (String)fields.get("ipmask");
	gateway = (String)fields.get("gateway");
	adsl_ser = (String)fields.get("adsl_ser");
	
	
	wan_type = (String)fields.get("wan_type");

	String wan_type_ = (String)fields.get("wan_type");
	if (wan_type.equals("1")||wan_type.equals("5")) {
		wan_type_name = "桥接";
	} else if (wan_type.equals("2")||wan_type.equals("6")) {
		wan_type_name = "路由";
	} else if (wan_type.equals("3")||wan_type.equals("7")) {
		wan_type_name = "静态IP";
	} else if (wan_type.equals("4")||wan_type.equals("8")) {
		wan_type_name = "DHCP";
	}
	_type = SheetManage.getServiceIdBy(serv_type_id, oper_type_id, wan_type_);
	
	_type = _type == null?"":_type;
}else{
	flag = -1;
}
%>
<link rel="stylesheet" href="../css/listview.css" type="text/css">
<SCRIPT LANGUAGE="JavaScript">
<!--

//-->
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
			  <TD align="right" width="20%">用户名称</TD>
			  <TD ><input type="text" name="username" value="<%=uname%>" class="bk">&nbsp;&nbsp;<font color=red>*</font></TD>
			  <TD align="right">用户密码</TD>
			  <TD ><input type="text" name="pwd" value="<%=password%>" class="bk">&nbsp;&nbsp;<font color=red>*</font></TD>
			</TR>
			<TR bgcolor="#ffffff">
			<%if(wan_type != null && ("1".equals(wan_type)||"2".equals(wan_type)||"3".equals(wan_type)||"4".equals(wan_type))){ %>
			  <TD align="right">VPI / VCI</TD>
			  <TD>
			  <input type="text" name="vpiid" value="<%=vpiid%>" class="bk" size='2'>&nbsp;/&nbsp;<input type="text" name="vciid" value="<%=vciid%>" class="bk" size='2'>&nbsp;&nbsp;<font color=red>*</font></TD>
			<%}else{ %>
			  <TD align="right">VlanID</TD>
			  <TD>
			  <input type="text" name="vlanid" value="<%=vlanid%>" class="bk" size='20'/>&nbsp;&nbsp;<font color=red>*</font></TD>
			<%} %>
				<TD align="right">上网类型</TD>
				<TD >
				<input type="text" name="wan_type_name" value="<%=wan_type_name%>" readOnly="readonly" class="bk">&nbsp;&nbsp;<font color=red>*</font>
				<input type="hidden" name="wan_type" value="<%=wan_type%>">
			  </TD>
			</TR>
			<%if(wan_type != null && "3".equals(wan_type)){ %>
			<TR bgcolor="#ffffff"  id = 'trip'>
			  <TD align="right">IP地址</TD>
			  <TD colspan="3">
				<input type="text" name="ipaddress" value="<%=ipaddress%>" readOnly="readonly" class="bk">&nbsp;&nbsp;<font color=red>*</font>
			  </TD>
			</TR>
			<TR bgcolor="#ffffff" id = 'trmk'>
			  <TD align="right">掩码</TD>
			  <TD colspan="3">
				<input type="text" name="ipmask" value="<%=ipmask%>" readOnly="readonly" class="bk">&nbsp;&nbsp;<font color=red>*</font>
			  </TD>
			</TR>
			<TR bgcolor="#ffffff" id = 'trgw'>
			  <TD align="right">网关</TD>
			  <TD colspan="3">
				<input type="text" name="gateway" value="<%=gateway%>" readOnly="readonly" class="bk">&nbsp;&nbsp;<font color=red>*</font>
			  </TD>
			</TR>
			<TR bgcolor="#ffffff" id = 'trdns'>
			  <TD align="right">DNS地址</TD>
			  <TD colspan="3">
				<input type="text" name="adsl_ser" value="<%=adsl_ser%>" readOnly="readonly" class="bk">&nbsp;&nbsp;<font color=red>*</font>
			  </TD>
			</TR>
			<%} %>
			<TR bgcolor="#ffffff">
			  <TD align="right">绑定接口</TD>
			  <TD colspan="3">
			  		<input type="checkbox" id="LanInterface" name="LanInterface" class="bk" 
			  	value="InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.1">LAN1 &nbsp;
			  		<input type="checkbox" id="LanInterface" name="LanInterface" class="bk" 
			  	value="InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.2">LAN2 &nbsp;
			  		<input type="checkbox" id="LanInterface" name="LanInterface" class="bk" 
			  	value="InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.3">LAN3 &nbsp;
			  		<input type="checkbox" id="LanInterface" name="LanInterface" class="bk" 
			  	value="InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.4">LAN4 &nbsp;
			  		<input type="checkbox" id="LanInterface" name="LanInterface" class="bk" 
			  	value="InternetGatewayDevice.LANDevice.1.WLANConfiguration.1" >WLAN1 &nbsp;
				<input type="checkbox" id="LanInterface" name="LanInterface" class="bk" 
			  	value="InternetGatewayDevice.LANDevice.1.WLANConfiguration.2" >WLAN2 &nbsp;
				<input type="checkbox" id="LanInterface" name="LanInterface" class="bk" 
			  	value="InternetGatewayDevice.LANDevice.1.WLANConfiguration.3" >WLAN3 &nbsp;
				<input type="checkbox" id="LanInterface" name="LanInterface" class="bk" 
			  	value="InternetGatewayDevice.LANDevice.1.WLANConfiguration.4" >WLAN4 &nbsp;
			  </TD>
			</TR>
			<TR bgcolor="#ffffff">
			  <TD align="right">绑定设备</TD>
			  <TD colspan="3">
				<input type="text" size="6" name="old_oui" value="<%=_oui%>" class="bk" readOnly>&nbsp;-&nbsp;<input type="text" size="32" name="old_device_serialnumber" value="<%=_device_serialnumber%>" class="bk" readOnly>
			  </TD>
			</TR>
			<%
				if(_type.equals("107")){			
			%>
			<TR bgcolor="#ffffff" >
			  <TD align="right">更换设备</TD>
			  <TD colspan="3">
				<input type="text" size="10" name="new_oui" value="" class="bk">&nbsp;-&nbsp;<input type="text" size="40" name="new_device_serialnumber" value="" class="bk">&nbsp;<font color=red>*</font>
			  </TD>
			</TR>
			<TR>
				<TD class=green_foot colspan=4 align=right>
					<input type="hidden" name="device_id" value="<%=device_id%>">
					<input type="hidden" name="gather_id" value="<%=gather_id%>">
					<input type="hidden" name="devicetype_id" value="<%=devicetype_id%>">
				  <input type="button" name="cmdSend" class=btn  value="下一步" onclick="CheckForm();">
				</TD>
			</TR>
			<%
				} else {
			%>
			<TR>
				<TD class=green_foot colspan=4 align=right>					
					<input type="hidden" name="ConnectionType" value='<%=wan_type.equals("1") ? "PPPoE_Bridged" : "IP_Routed"%>'/>
					<input type="hidden" name="device_id" value="<%=device_id%>">
					<input type="hidden" name="gather_id" value="<%=gather_id%>">
					<input type="hidden" name="devicetype_id" value="<%=devicetype_id%>">
					<input type="hidden" name="wan_value_1" value="<%=wan_value_1%>">
					<input type="hidden" name="wan_value_2" value="<%=wan_value_2%>">
				  <input type="button" name="cmdSend" class=btn  value=" 发送工单 " onclick="CheckForm();">
				</TD>
			</TR>
			<%
				}
			%>
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
	alert("当前系统中没有此帐号或者设备不存在，请确认后重新输入！");
}else{
	parent.document.all("idBody").innerHTML = idBody.innerHTML;
}

//-->
</SCRIPT>
