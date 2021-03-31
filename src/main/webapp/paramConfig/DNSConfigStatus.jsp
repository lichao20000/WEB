<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ page import="com.linkage.litms.paramConfig.ConfigDevice" %>

<%
String DNSServers = "";
String strDevice = request.getParameter("device_id");
String[] device_list = strDevice.split(",");
String device_id = "";
String type= request.getParameter("type");
String html = "";

if ("2".equals(type)){
	html += "该设备不支持以SNMP协议配置DNS信息";
}
else{
	String[] DHCPName = new String[1];
	
	for (int i=0;i<device_list.length;i++){
		
		device_id = device_list[i];
		Cursor cursor = DataSetBean.getCursor("select device_name from tab_gw_device where device_id='" + device_id + "'");
		Map fields = cursor.getNext();
		if (fields != null){
			DHCPName[0] = ConfigDevice.getParaArr("384", device_id);
			//DHCPName[0] = "InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.DNSServers";
			Map paraMap = ConfigDevice.getDevInfo_tr069(device_id, curUser.getUser(), DHCPName);
			if (paraMap == null){
				html += "获取参数值失败，请检查ACS配置是否正确<br>";
			}
			else{
				DNSServers = (String)paraMap.get("0");
				if (DNSServers != null){
					html += "获取DNS配置成功...<br>";
					html += fields.get("device_name") + "的DNS为：" + DNSServers;
				}
				else{
					html += "获取DNS配置失败...<br>";
					DNSServers = "";
				}
			}
		}
	}
}

%>
<html>
<head>
<script type="text/javascript">
parent.document.all("div_ping").innerHTML = "<%=html%>";
</script>
</head>
<body></body>
</html>
