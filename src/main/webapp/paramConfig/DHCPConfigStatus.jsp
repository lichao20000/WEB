<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ page import="com.linkage.litms.paramConfig.ConfigDevice" %>
<%@ page import="com.linkage.litms.paramConfig.DHCPConfig" %>

<%
//DHCP服务状态
String DHCPServerEnable = "";
//DHCP地址池预留地址
String DHCPAddressInfo = "";
//DHCP地址池预留地址起始地址
String DHCPMinAddress = "";
//DHCP地址池预留地址结束地址
String DHCPMaxAddress = "";
//地址池是否启用
String status = "";
//DHCP地址池列表信息
String DHCPList = "";
//DHCP地址池网段信息
String IPRouters = "";
//DHCP地址池掩码信息
String SubnetMask = "";
//DHCP地址池起始地址
String MinAddress = "";
//DHCP地址池结束地址
String MaxAddress = "";
//租期
String DHCPLeaseTime = "";

String device_id = request.getParameter("device_id");
String type= request.getParameter("type");
String html = "";
String dataHtml = "";

if ("2".equals(type)){
	html += "该设备不支持以SNMP协议配置DHCP信息<br>";
	DHCPConfig dhcp = new DHCPConfig();
	String[] dhcpInfo = dhcp.getDHCPInfo(device_id);
	
	if (dhcpInfo != null && dhcpInfo.length == 10){
		DHCPServerEnable = dhcpInfo[0];
		DHCPAddressInfo = dhcpInfo[1];
		String[] DHCPListArr = dhcpInfo[3].split("<br>");
		String[] IpListArr = dhcpInfo[4].split("<br>");
		String[] SubnetMaskArr = dhcpInfo[5].split("<br>");
		String[] MinAddressArr = dhcpInfo[6].split("<br>");
		String[] MaxAddressArr = dhcpInfo[7].split("<br>");
		String[] statusArr = dhcpInfo[8].split("<br>");
		
		dataHtml += "<TR bgcolor='#FFFFFF'><th>地址池是否启用</th><th>地址池列表信息</th><th>网段</th><th>掩码</th><th>起始地址</th><th>结束地址</th><th>租期</th></TR>";
		for (int i=0;i<DHCPListArr.length;i++){
			//显示数据html
			dataHtml += "<tr bgcolor='#FFFFFF'>";
			if (statusArr.length > i){
				dataHtml += "<td><input type='text' name='statusArr' value='" + statusArr[i] + "'/></td>";
			}
			else{
				dataHtml += "<td><input type='text' name='statusArr' value=''/></td>";
			}
			dataHtml += "<td><input type='text' name='DHCPListArr' value='" + DHCPListArr[i] + "'/></td>";
			if (IpListArr.length > i){
				dataHtml += "<td><input type='text' name='IpListArr' value='" + IpListArr[i] + "'/></td>";
			}
			else{
				dataHtml += "<td><input type='text' name='IpListArr' value=''/></td>";
			}
			if (SubnetMaskArr.length > i){
				dataHtml += "<td><input type='text' name='SubnetMaskArr' value='" + SubnetMaskArr[i] + "'/></td>";
			}
			else{
				dataHtml += "<td><input type='text' name='SubnetMaskArr' value=''/></td>";
			}
			if (MinAddressArr.length > i){
				dataHtml += "<td><input type='text' name='MinAddressArr' value='" + MinAddressArr[i] + "'/></td>";
			}
			else{
				dataHtml += "<td><input type='text' name='MinAddressArr' value=''/></td>";
			}
			if (MaxAddressArr.length > i){
				dataHtml += "<td><input type='text' name='MaxAddressArr' value='" + MaxAddressArr[i] + "'/></td>";
			}
			else{
				dataHtml += "<td><input type='text' name='MaxAddressArr' value=''/></td>";
			}
			dataHtml += "<td><input type='text' name='LeaseTimeArr' value=''/></td>";
			dataHtml += "</tr>";
		}
		
		html = dhcpInfo[9];
	}
}
else{
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
	
	String vlanPara = "";
	for (int i=0;i<DHCPOID.length;i++){
		vlanPara += ConfigDevice.getParaArr(DHCPOID[i], device_id) + "#";
	}
	String[] DHCPName = vlanPara.split("#");
	
	//调后台获取参数值
	Map paraMap = ConfigDevice.getDevInfo_tr069_all(device_id, curUser.getUser(), DHCPName);
	if (paraMap == null){
		html += "获取参数值失败，请检查ACS配置是否正确<br>";
	}
	else{
		DHCPServerEnable = (String)paraMap.get("0");
		DHCPAddressInfo = (String)paraMap.get("1");
		status = (String)paraMap.get("2");
		DHCPList = (String)paraMap.get("3");
		IPRouters = (String)paraMap.get("4");
		SubnetMask = (String)paraMap.get("5");
		MinAddress = (String)paraMap.get("6");
		MaxAddress = (String)paraMap.get("7");
		DHCPLeaseTime = (String)paraMap.get("8");
		//显示数据html
		if (DHCPServerEnable != null && !"null".equals(DHCPServerEnable)){
			html += "获取DHCP启用状态成功...<br>";
		}
		else{
			html += "获取DHCP启用状态失败...<br>";
			DHCPServerEnable = "";
		}
		if (DHCPAddressInfo != null && !"null".equals(DHCPAddressInfo)){
			html += "获取DHCP池预留地址成功...<br>";
		}
		else{
			html += "获取DHCP池预留地址失败...<br>";
			DHCPAddressInfo = "";
		}
		if (status != null && !"null".equals(status)){
			html += "获取DHCP启用状态成功...<br>";
		}
		else{
			html += "获取DHCP启用状态失败...<br>";
			status = "";
		}
		if (DHCPList != null && !"null".equals(DHCPList)){
			html += "获取DHCP地址池列表信息成功...<br>";
		}
		else{
			html += "获取DHCP地址池列表信息失败...<br>";
			DHCPList = "";
		}
		if (IPRouters != null && !"null".equals(IPRouters)){
			html += "获取DHCP地址池网段成功...<br>";
		}
		else{
			html += "获取DHCP地址池网段失败...<br>";
			IPRouters = "";
		}
		if (SubnetMask != null && !"null".equals(SubnetMask)){
			html += "获取DHCP掩码成功...<br>";
		}
		else{
			html += "获取DHCP掩码失败...<br>";
			SubnetMask = "";
		}
		if (MinAddress != null && !"null".equals(MinAddress)){
			html += "获取DHCP起始地址成功...<br>";
		}
		else{
			html += "获取DHCP起始地址失败...<br>";
			MinAddress = "";
		}
		if (MaxAddress != null && !"null".equals(MaxAddress)){
			html += "获取DHCP结束地址成功...<br>";
		}
		else{
			html += "获取DHCP结束地址失败...<br>";
			MaxAddress = "";
		}
		if (DHCPLeaseTime != null && !"null".equals(DHCPLeaseTime)){
			html += "获取DHCP租期成功...<br>";
		}
		else{
			html += "获取DHCP租期失败...<br>";
			DHCPLeaseTime = "";
		}
		
		String[] statusArr = status.split("<br>");
		String[] DHCPListArr = DHCPList.split("<br>");
		String[] IpListArr = IPRouters.split("<br>");
		String[] SubnetMaskArr = SubnetMask.split("<br>");
		String[] MinAddressArr = MinAddress.split("<br>");
		String[] MaxAddressArr = MaxAddress.split("<br>");
		String[] LeaseTimeArr = DHCPLeaseTime.split("<br>");
		
		dataHtml += "<TR bgcolor='#FFFFFF'><th>地址池是否启用</th><th>地址池列表信息</th><th>网段</th><th>掩码</th><th>起始地址</th><th>结束地址</th><th>租期</th></TR>";
		for (int i=0;i<IpListArr.length;i++){
			//显示数据html
			dataHtml += "<tr bgcolor='#FFFFFF'>";
			if (statusArr.length > i){
				dataHtml += "<td><input type='text' name='statusArr' value='" + statusArr[i] + "'/></td>";
			}
			else{
				dataHtml += "<td><input type='text' name='statusArr' value=''/></td>";
			}
			if (DHCPListArr.length > i){
				dataHtml += "<td><input type='text' name='DHCPListArr' value='" + DHCPListArr[i] + "'/></td>";
			}
			else{
				dataHtml += "<td><input type='text' name='DHCPListArr' value=''/></td>";
			}
			dataHtml += "<td><input type='text' name='IpListArr' value='" + IpListArr[i] + "'/></td>";
			if (SubnetMaskArr.length > i){
				dataHtml += "<td><input type='text' name='SubnetMaskArr' value='" + SubnetMaskArr[i] + "'/></td>";
			}
			else{
				dataHtml += "<td><input type='text' name='SubnetMaskArr' value=''/></td>";
			}
			if (MinAddressArr.length > i){
				dataHtml += "<td><input type='text' name='MinAddressArr' value='" + MinAddressArr[i] + "'/></td>";
			}
			else{
				dataHtml += "<td><input type='text' name='MinAddressArr' value=''/></td>";
			}
			if (MaxAddressArr.length > i){
				dataHtml += "<td><input type='text' name='MaxAddressArr' value='" + MaxAddressArr[i] + "'/></td>";
			}
			else{
				dataHtml += "<td><input type='text' name='MaxAddressArr' value=''/></td>";
			}
			if (LeaseTimeArr.length > i){
				dataHtml += "<td><input type='text' name='LeaseTimeArr' value='" + LeaseTimeArr[i] + "'/></td>";
			}
			else{
				dataHtml += "<td><input type='text' name='LeaseTimeArr' value=''/></td>";
			}
			dataHtml += "</tr>";
		}
		
		
		
	}
	
}

%>
<html>
<head>
<script type="text/javascript">
parent.document.all("bt_set").style.display = "";
parent.document.all("div_ping").innerHTML = "<%=html%>";

parent.document.frm.DHCPServerEnable.value = "<%=DHCPServerEnable%>";
parent.document.frm.DHCPAddressInfo.value = "<%=DHCPAddressInfo%>";
parent.document.all("para1").style.display = "";
parent.document.all("para2").style.display = "";

var dataHtml = "<%=dataHtml%>";
parent.setParaHtml(dataHtml);
</script>
</head>
<body></body>
</html>
