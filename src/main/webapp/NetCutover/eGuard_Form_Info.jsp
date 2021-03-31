<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.netcutover.*,java.util.Map,com.linkage.litms.common.database.Cursor"%>
<%
String _type = "";
String oper_type_id = request.getParameter("oper_type_id");
String serv_type_id = request.getParameter("serv_type_id");


String username = "";
String passwd = "";
String vpiid="";
String vciid="";
String device_id  = "";
String gather_id = "";
String devicetype_id = "";
String _oui = "";
String _device_serialnumber = "";
String wan_type_ = "";
String def_lanIP = "192.168.1.188";
String def_wanPort1 = "8000";
String def_wanPort2 = "8001";
String def_wanPort3 = "8002";
String def_wanPort4 = "8003";
String def_wanPort5 = "5060";

String def_lanPort1 = "8000";
String def_lanPort2 = "8001";
String def_lanPort3 = "8002";
String def_lanPort4 = "8003";
String def_lanPort5 = "5060";


Map fields = SheetManage.usrMap(request);

int flag = 0;
if(fields != null ){
	flag = 0;
	username = (String)fields.get("username");
	passwd = (String)fields.get("passwd");
	vpiid=  (String)fields.get("vpiid");
	vciid=  (String)fields.get("vciid");
	device_id  = (String)fields.get("device_id");
	gather_id = (String)fields.get("gather_id");
	devicetype_id = (String)fields.get("devicetype_id");
	_oui = (String)fields.get("oui");
	_device_serialnumber = (String)fields.get("device_serialnumber");
	wan_type_ = (String)fields.get("wan_type");
	if ("1".equals(wan_type_)) {
		wan_type_ = "2";
	}
	//电子工单下发后，ACS暂未把wan_type变为2(路由)，所以暂时只根据serv_type_id, oper_type_id进行过滤 
	//_type = SheetManage.getServiceId(serv_type_id, oper_type_id, wan_type_);//"1031";//
	_type = SheetManage.getServiceId(serv_type_id, oper_type_id);
}else{
	flag = -1;
}
%>
<link rel="stylesheet" href="../css/listview.css" type="text/css">
<SCRIPT LANGUAGE="JavaScript">

</SCRIPT>

<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center" id="idTable">
  <TR><TD bgcolor=#999999>
	  <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
			<TR bgcolor="#ffffff">
				<TH colspan="4">
					用户设备信息:<%=username%>
				</TH>
			</TR>
			<TR bgcolor="#ffffff">
				<TD align="right" width="15%">
					绑定设备：
				</TD>
				<TD colspan="3" width="75%">
					<%=_oui%>－<%=_device_serialnumber%>
				</TD>
			</TR>
			<TR bgcolor="#ffffff">
				<TD align="right" width="15%">
					用户密码：
				</TD>
				<TD colspan="" width="30%">
					<input type="text" name="passwd"
						value="<%=passwd%>" class="bk">
					&nbsp;&nbsp;
					<font color=red>*</font>
				</TD>
				<TD align="right" width="15%">
					确认密码：
				</TD>
				<TD colspan="" width="30%">
					<input type="text" name="conf_passwd"
						value="<%=passwd%>" class="bk">
					&nbsp;&nbsp;
					<font color=red>*</font>
				</TD>
			</TR>
			<TR bgcolor="#ffffff">
				<TD align="right" width="15%">
					VPI/VCI：
				</TD>
				<TD colspan="" width="30%">
					<input type="text" name="vpiid" size="2"
						value="<%=vpiid%>" class="bk">&nbsp;/&nbsp;<input type="text" name="vciid" size="3"
						value="<%=vciid%>" class="bk">
					<font color=red>*</font>
				</TD>
				<TD align="right" width="15%">
					上网类型：
				</TD>
				<TD align="left" width="30%">
					路由
				</TD>
			</TR>
		</TABLE>
		
		<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
			<TR bgcolor="#ffffff">
				<TH colspan="4">
					端口映射配置
				</TH>
			</TR>
			<TR bgcolor="#ffffff">
			    <TD align="left" class="green_title2">
					&nbsp;&nbsp;序号
				</TD>
				<TD align="left" class="green_title2">
					<font size="2">外网端口：</font>
					<font color=red>*</font>
				</TD>
				<TD align="left"  class="green_title2">
					<font size="2">内网IP：</font>
					<font color=red>*</font>
				</TD>
				<TD align="left"  class="green_title2">
					<font size="2">内网端口：</font>
					<font color=red>*</font>
				</TD>
			</TR>
			<TR bgcolor="#ffffff">
				<TD align="right">
					&nbsp;&nbsp;1：
				</TD>
				<TD align="left">
					<input type="text" name="wanPort1"
						value="<%= def_wanPort1%>" class="bk">
					&nbsp;&nbsp;
				</TD>
				<TD align="left">
					<input type="text" name="lanIP1"
						value="<%= def_lanIP%>" class="bk">
					&nbsp;&nbsp;
				</TD>
				<TD align="left">
					<input type="text" name="lanPort1"
						value="<%= def_lanPort1%>" class="bk">
					&nbsp;&nbsp;
				</TD>
			</TR>
			<TR bgcolor="#ffffff">
				<TD align="right">
					&nbsp;&nbsp;2：
				</TD>
				<TD align="left">
					<input type="text" name="wanPort2"
						value="<%= def_wanPort2%>" class="bk">
					&nbsp;&nbsp;
				</TD>
				<TD align="left">
					<input type="text" name="lanIP2"
						value="<%= def_lanIP%>" class="bk">
					&nbsp;&nbsp;
				</TD>
				<TD align="left">
					<input type="text" name="lanPort2"
						value="<%= def_lanPort2%>" class="bk">
					&nbsp;&nbsp;
				</TD>
			</TR>
			<TR bgcolor="#ffffff">
				<TD align="right">
					&nbsp;&nbsp;3：
				</TD>
				<TD align="left">
					<input type="text" name="wanPort3"
						value="<%= def_wanPort3%>" class="bk">
					&nbsp;&nbsp;
				</TD>
				<TD align="left">
					<input type="text" name="lanIP3"
						value="<%= def_lanIP%>" class="bk">
					&nbsp;&nbsp;
				</TD>
				<TD align="left">
					<input type="text" name="lanPort3"
						value="<%= def_lanPort3%>" class="bk">
					&nbsp;&nbsp;
				</TD>
			</TR>
			<TR bgcolor="#ffffff">
				<TD align="right">
					&nbsp;&nbsp;4：
				</TD>
				<TD align="left">
					<input type="text" name="wanPort4"
						value="<%= def_wanPort4%>" class="bk">
					&nbsp;&nbsp;
				</TD>
				<TD align="left">
					<input type="text" name="lanIP4"
						value="<%= def_lanIP%>" class="bk">
					&nbsp;&nbsp;
				</TD>
				<TD align="left">
					<input type="text" name="lanPort4"
						value="<%= def_lanPort4%>" class="bk">
					&nbsp;&nbsp;
				</TD>
			</TR>
			<TR bgcolor="#ffffff">
				<TD align="right">
					&nbsp;&nbsp;5：
				</TD>
				<TD align="left">
					<input type="text" name="wanPort5"
						value="<%= def_wanPort5%>" class="bk">
					&nbsp;&nbsp;
				</TD>
				<TD align="left">
					<input type="text" name="lanIP5"
						value="<%= def_lanIP%>" class="bk">
					&nbsp;&nbsp;
				</TD>
				<TD align="left">
					<input type="text" name="lanPort5"
						value="<%= def_lanPort5%>" class="bk">
					&nbsp;&nbsp;
				</TD>
			</TR>
			
			<TR bgcolor="#ffffff"  class="green_foot">										
				<TD align="right" colspan="4">
					<input type="hidden" name="username" value="<%=username%>">
					<input type="hidden" name="oui" value="<%=_oui%>">
					<input type="hidden" name="device_serialnumber" value="<%=_device_serialnumber%>">
					<input type="hidden" name="device_id" value="<%=device_id%>">
					<input type="hidden" name="gather_id" value="<%=gather_id%>">
					<input type="hidden" name="devicetype_id" value="<%=devicetype_id%>">
					<input type="hidden" name="wan_type_" value="<%=wan_type_%>">
					<input type="button" name="button" value=" 配 置 " onclick="send('<%=_type%>')">
				</TD>
			</TR>

	  </TABLE>
  </TD></TR>
</TABLE>

<SCRIPT LANGUAGE="JavaScript">
<!--
var _flag = "<%=flag%>";
if(_flag == -1){
	alert("当前系统中没有此帐号或者设备不存在，请确认后重新输入！");
}else{
	parent.document.all("idBody").innerHTML = idTable.innerHTML;
}

//-->
</SCRIPT>

