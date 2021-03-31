<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ page import="com.linkage.litms.paramConfig.ConfigDevice" %>

<%
String username = "";
String password = "";
String strDevice = request.getParameter("device_id");
String[] device_list = strDevice.split(",");
String device_id = "";
String type= request.getParameter("type");
String html = "";

if ("2".equals(type)){
	html += "该设备不支持以SNMP协议配置PPPoe信息";
}
else{
	String[] DHCPName = new String[2];
	
	for (int i=0;i<device_list.length;i++){
		
		device_id = device_list[i];
		Cursor cursor = DataSetBean.getCursor("select device_name from tab_gw_device where device_id='" + device_id + "'");
		Map fields = cursor.getNext();
		if (fields != null){
			DHCPName[0] = ConfigDevice.getParaArr("501", device_id);
			DHCPName[1] = ConfigDevice.getParaArr("502", device_id);
			Map paraMap = ConfigDevice.getDevInfo_tr069(device_id, curUser.getUser(), DHCPName);
			if (paraMap == null){
				html += "获取参数值失败，请检查ACS配置是否正确<br>";
			}
			else{
				username = (String)paraMap.get("0");
				if (username != null){
					html += "获取PPPoe用户名成功...<br>";
					html += fields.get("device_name") + "的PPPoe用户名为：" + username + "<br>";
				}
				else{
					html += "获取PPPoe用户名失败...<br>";
					username = "";
				}
				password = (String)paraMap.get("1");
				if (username != null){
					html += "获取PPPoe密码成功...<br>";
					html += fields.get("device_name") + "的PPPoe密码为：" + password + "<br>";
				}
				else{
					html += "获取PPPoe密码失败...<br>";
					password = "";
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
