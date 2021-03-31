<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ page import="com.linkage.litms.paramConfig.ConfigDevice" %>

<%
String strDevice = request.getParameter("device_id");
String[] device_list = strDevice.split(",");
String device_id = "";
String type= request.getParameter("type");
String html = "";

String[] DHCPName = new String[1];


String[] value = new String[1];
value[0] = request.getParameter("DNSServers");

String[] typeArr = new String[1];
typeArr[0] = "1";

String msg = "设置DNS相关信息：";

if ("2".equals(type)){
	html += "该设备不支持以SNMP协议配置DNS服务<br>";
}
else{
	for (int i=0;i<device_list.length;i++){
		
		device_id = device_list[i];
		Cursor cursor = DataSetBean.getCursor("select device_name from tab_gw_device where device_id='" + device_id + "'");
		Map fields = cursor.getNext();
		if (fields != null){
			DHCPName[0] = ConfigDevice.getParaArr("384", device_id);
			//DHCPName[0] = "InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.DNSServers";
			int[] ret = ConfigDevice.setDevInfo_tr069(device_id, curUser.getUser(), DHCPName, value, typeArr);

			if (ret != null && ret.length > 0){
				if (ret[0] == 1){
					html += "配置" + fields.get("device_name") + "的DNS服务成功...<br>";
					msg += "设置" + fields.get("device_name") + "的DNS地址成功，值为" + value[0] + "。";
				}
				else{
					html += "配置" + fields.get("device_name") + "的DNS服务失败...<br>";
					msg += "设置" + fields.get("device_name") + "的DNS地址失败，值为" + value[0] + "。";
				}
			}
			else{
				html += "配置" + fields.get("device_name") + "的DNS服务失败...<br>";
				msg += "设置" + fields.get("device_name") + "的DNS地址失败，值为" + value[0] + "。";
			}
		}
	}
}

msg = Encoder.toISO(msg);
//记日志
LogItem.getInstance().writeItemLog_other(request, 1, device_id, msg, Encoder.toISO("成功"), "DNS配置");


%>
<html>
<head>
<script type="text/javascript">
parent.document.all("div_ping").innerHTML = "<%=html%>";
</script>
</head>
<body></body>
</html>
