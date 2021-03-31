<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ page import="com.linkage.litms.paramConfig.ConfigDevice" %>

<%
String strDevice = request.getParameter("device_id");
String[] device_list = strDevice.split(",");
String device_id = "";
String type= request.getParameter("type");
String html = "";

String[] DHCPName = new String[2];

String[] value = new String[2];
value[0] = request.getParameter("RemoteEnable");
value[1] = request.getParameter("Password");

String[] typeArr = new String[2];
typeArr[0] = "4";
typeArr[1] = "1";

String msg = "设置电信维护帐号：";

if ("2".equals(type)){
	html += "该设备不支持以SNMP协议配置电信维护帐号<br>";
}
else{
	for (int i=0;i<device_list.length;i++){
		
		device_id = device_list[i];
		Cursor cursor = DataSetBean.getCursor("select device_name from tab_gw_device where device_id='" + device_id + "'");
		Map fields = cursor.getNext();
		if (fields != null){
			DHCPName[0] = ConfigDevice.getParaArr("415", device_id);
			DHCPName[1] = ConfigDevice.getParaArr("416", device_id);
			int[] ret = ConfigDevice.setDevInfo_tr069(device_id, curUser.getUser(), DHCPName, value, typeArr);

			if (ret != null && ret.length > 0){
				if (ret[0] == 1){
					html += "配置" + fields.get("device_name") + "的帐号是否启用成功...<br>";
					msg += "设置" + fields.get("device_name") + "的帐号是否启用成功，值为" + value[0] + "。";
				}
				else{
					html += "配置" + fields.get("device_name") + "的帐号是否启用失败...<br>";
					msg += "设置" + fields.get("device_name") + "的帐号是否启用失败，值为" + value[0] + "。";
				}
				if (ret[1] == 1){
					html += "配置" + fields.get("device_name") + "的帐号密码成功...<br>";
					msg += "设置" + fields.get("device_name") + "的帐号密码成功，值为" + value[0] + "。";
				}
				else{
					html += "配置" + fields.get("device_name") + "的帐号密码失败...<br>";
					msg += "设置" + fields.get("device_name") + "的帐号密码失败，值为" + value[0] + "。";
				}
			}
			else{
				html += "配置" + fields.get("device_name") + "的电信维护帐号失败...<br>";
				msg += "设置" + fields.get("device_name") + "的电信维护帐号失败。";
			}
		}
	}
}

msg = Encoder.toISO(msg);
//记日志
LogItem.getInstance().writeItemLog_other(request, 1, device_id, msg, Encoder.toISO("成功"), "设置电信维护帐号");


%>
<html>
<head>
<script type="text/javascript">
parent.document.all("div_ping").innerHTML = "<%=html%>";
</script>
</head>
<body></body>
</html>
