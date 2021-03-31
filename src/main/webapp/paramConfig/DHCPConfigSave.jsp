<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ page import="com.linkage.litms.paramConfig.ConfigDevice" %>
<%@ page import="com.linkage.litms.paramConfig.DHCPConfig" %>

<%
String device_id = request.getParameter("device_id");
String type= request.getParameter("type");
String html = "";

String[] DHCPOID = new String[9];
DHCPOID[0] = "377";
DHCPOID[1] = "382";
DHCPOID[2] = "385";
DHCPOID[3] = "386";
DHCPOID[4] = "378";
DHCPOID[5] = "379";
DHCPOID[6] = "380";
DHCPOID[7] = "381";
DHCPOID[8] = "383";

String[] DHCPName = new String[9];
for (int i=0;i<DHCPOID.length;i++){
	DHCPName[i] = ConfigDevice.getParaArr(DHCPOID[i], device_id);
}

String[] value = new String[9];
value[0] = request.getParameter("DHCPServerEnable");
value[1] = request.getParameter("DHCPAddressInfo");
value[2] = request.getParameter("statusArr");
value[3] = request.getParameter("DHCPListArr");
value[4] = request.getParameter("IpListArr");
value[5] = request.getParameter("SubnetMaskArr");
value[6] = request.getParameter("MinAddressArr");
value[7] = request.getParameter("MaxAddressArr");
value[8] = request.getParameter("LeaseTimeArr");


List<String[]> valueList = new ArrayList<String[]>();
valueList.add(0,value[0].split(";"));
valueList.add(1,value[1].split(";"));
valueList.add(2,value[2].split(";"));
valueList.add(3,value[3].split(";"));
valueList.add(4,value[4].split(";"));
valueList.add(5,value[5].split(";"));
valueList.add(6,value[6].split(";"));
valueList.add(7,value[7].split(";"));
valueList.add(8,value[8].split(";"));

String msg = "设置DHCP相关信息：";

int[] ret = null;


if ("2".equals(type)){
	DHCPConfig dhcp = new DHCPConfig();
	ret = dhcp.setDHCPInfo(device_id, valueList);
}
else{
	ret = ConfigDevice.setDevInfo_tr069_all(device_id, user, DHCPName, valueList);
}

if (ret != null && ret.length == DHCPName.length){
	if (!"".equals(value[0])){
		if (ret[0] == 1){
			html += "配置DHCP启用状态成功...<br>";
			msg += "设置DHCP启用状态成功，值为" + value[0] + "；";
		}
		else if (ret[0] == 0){
			html += "配置DHCP启用状态失败...<br>";
			msg += "设置DHCP启用状态失败，值为" + value[0] + "；";
		}
	}
	if (!"".equals(value[1])){
		if (ret[1] == 1){
			html += "配置DHCP池预留地址成功...<br>";
			msg += "设置池预留地址成功，值为" + value[1] + "；";
		}
		else if (ret[1] == 0){
			html += "配置DHCP池预留地址失败...<br>";
			msg += "设置池预留地址失败，值为" + value[1] + "；";
		}
	}
	if (!"".equals(value[2])){
		if (ret[2] == 1){
			html += "配置DHCP地址池启用状态成功...<br>";
			msg += "设置DHCP地址池启用状态成功，值为" + value[2] + "；";
		}
		else if (ret[2] == 0){
			html += "配置DHCP地址池启用状态失败...<br>";
			msg += "设置DHCP地址池启用状态失败，值为" + value[2] + "；";
		}
	}
	if (!"".equals(value[3])){
		if (ret[3] == 1){
			html += "配置DHCP地址池列表信息成功...<br>";
			msg += "设置DHCP地址池列表信息成功，值为" + value[3] + "；";
		}
		else if (ret[3] == 0){
			html += "配置DHCP地址池列表信息失败...<br>";
			msg += "设置DHCP地址池列表信息失败，值为" + value[3] + "；";
		}
	}
	if (!"".equals(value[4])){
		if (ret[4] == 1){
			html += "配置DHCP地址池网段成功...<br>";
			msg += "设置地址池网段成功，值为" + value[4] + "；";
		}
		else if (ret[4] == 0){
			html += "配置DHCP地址池网段失败...<br>";
			msg += "设置地址池网段失败，值为" + value[4] + "；";
		}
	}
	if (!"".equals(value[5])){
		if (ret[5] == 1){
			html += "配置DHCP掩码成功...<br>";
			msg += "设置掩码成功，值为" + value[5] + "；";
		}
		else if (ret[5] == 0){
			html += "配置DHCP掩码失败...<br>";
			msg += "设置掩码失败，值为" + value[5] + "；";
		}
	}
	if (!"".equals(value[6])){
		if (ret[6] == 1){
			html += "配置DHCP起始地址成功...<br>";
			msg += "设置起始地址成功，值为" + value[6] + "；";
		}
		else if (ret[6] == 0){
			html += "配置DHCP起始地址失败...<br>";
			msg += "设置起始地址失败，值为" + value[6] + "；";
		}
	}
	if (!"".equals(value[7])){
		if (ret[7] == 1){
			html += "配置DHCP结束地址成功...<br>";
			msg += "设置结束地址成功，值为" + value[7] + "；";
		}
		else if (ret[7] == 0){
			html += "配置DHCP结束地址失败...<br>";
			msg += "设置结束地址失败，值为" + value[7] + "；";
		}
	}
	if (!"".equals(value[8])){
		if (ret[8] == 1){
			html += "配置DHCP租期成功...<br>";
			msg += "设置租期成功，值为" + value[8] + "。";
		}
		else if (ret[8] == 0){
			html += "配置DHCP租期失败...<br>";
			msg += "设置租期失败，值为" + value[8] + "。";
		}
	}
}
else{
	html += "配置DHCP信息失败...<br>";
}

msg = Encoder.toISO(msg);
//记日志
LogItem.getInstance().writeItemLog_other(request, 1, device_id, msg, Encoder.toISO("成功"), "DHCP配置");

%>
<html>
<head>
<script type="text/javascript">
parent.document.all("div_ping").innerHTML = "<%=html%>";
</script>
</head>
<body></body>
</html>
