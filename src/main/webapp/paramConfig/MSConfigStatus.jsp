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
	String[] DHCPName = new String[5];
	Cursor cursor;
	Map paraMap;
	for (int i=0;i<device_list.length;i++){
		
		device_id = device_list[i];
		cursor = DataSetBean.getCursor("select device_name from tab_gw_device where device_id='" + device_id + "'");
		Map fields = cursor.getNext();
		if (fields != null){
			
			DHCPName[0] = ConfigDevice.getParaArr("400", device_id);
			DHCPName[1] = ConfigDevice.getParaArr("401", device_id);
			DHCPName[2] = ConfigDevice.getParaArr("402", device_id);
			DHCPName[3] = ConfigDevice.getParaArr("403", device_id);
			DHCPName[4] = ConfigDevice.getParaArr("404", device_id);
			paraMap = ConfigDevice.getDevInfo_tr069(device_id, curUser.getUser(), DHCPName);
			if (paraMap == null){
				html += "获取参数值失败，请检查ACS配置是否正确<br>";
			}
			else{
				//InternetGatewayDevice.ManagementServer.URL(URL)
				DNSServers = (String)paraMap.get("0");
				if (DNSServers != null){
					html += "获取URL配置成功...<br>";
					html += fields.get("device_name") + "的URL为：" + DNSServers+"<br>";
				}
				else{
					html += "获取URL配置失败...<br>";
					DNSServers = "";
				}
				//ManagementServer.Username(Username)
				DNSServers = (String)paraMap.get("1");
				if (DNSServers != null){
					html += "获取Username配置成功...<br>";
					html += fields.get("device_name") + "的Username为：" + DNSServers+"<br>";
				}
				else{
					html += "获取Username配置失败...<br>";
					DNSServers = "";
				}
				//InternetGatewayDevice.ManagementServer.Password(Password)
				DNSServers = (String)paraMap.get("2");
				if (DNSServers != null){
					html += "获取Password配置成功...<br>";
					html += fields.get("device_name") + "的Password为：" + DNSServers+"<br>";
				}
				else{
					html += "获取Password配置失败...<br>";
					DNSServers = "";
				}
				//InternetGatewayDevice.ManagementServer.PeriodicInformEnable(上报周期开关)
				DNSServers = (String)paraMap.get("3");
				if (DNSServers != null){
					html += "获取上报周期开关配置成功...<br>";
					html += fields.get("device_name") + "的上报周期开关为：" + DNSServers+"<br>";
				}
				else{
					html += "获取上报周期开关配置失败...<br>";
					DNSServers = "";
				}
				//InternetGatewayDevice.ManagementServer.PeriodicInformInterval(上报周期)
				DNSServers = (String)paraMap.get("4");
				if (DNSServers != null){
					html += "获取上报周期配置成功...<br>";
					html += fields.get("device_name") + "的上报周期为：" + DNSServers+"<br>";
				}
				else{
					html += "获取上报周期配置失败...<br>";
					DNSServers = "";
				}
			}
		}
		cursor=null;
		paraMap=null;
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
