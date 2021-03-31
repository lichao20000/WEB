<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ page import="com.linkage.litms.paramConfig.ConfigDevice" %>

<%
String device_id = request.getParameter("device_id");
String type= request.getParameter("type");
String html = "";

String[] DHCPName = new String[1];
DHCPName[0] = "InternetGatewayDevice.Time.LocalTimeZone";//ConfigDevice.getParaArr("384", device_id);
//DHCPName[1] = "InternetGatewayDevice.Time.LocalTimeZoneName";
String[] value = new String[1];
value[0] = request.getParameter("timeZone").toString();
String timeName = java.net.URLDecoder.decode(request.getParameter("timeZoneName"),"UTF-8");
//value[1]=java.net.URLDecoder.decode(request.getParameter("timeZoneName"),"UTF-8");
String[] typeArr = new String[1];
typeArr[0] = "1";
//typeArr[1] = "1";

String msg = "设置时区相关信息：";

if ("2".equals(type)){
	html += "该设备不支持以SNMP协议配置时区<br>";
}
else{
	int[] ret = ConfigDevice.setDevInfo_tr069(device_id, curUser.getUser(), DHCPName, value, typeArr);
	if (ret != null && ret.length > 0){
		if (ret[0] == 1){
			html += "配置时区成功...<br>";
			msg += "设置时区成功，时区值为" + value[0] + "时区名称值为"+timeName+"。";
		}
		else{
			html += "配置时区失败...<br>";
			msg += "设置时区失败，值为" + value[0] + "时区名称值为"+timeName+"。";
		}
	}
	else{
		html += "配置时区失败...<br>";
		msg += "设置时区失败，值为" + value[0] + "时区名称值为"+timeName+"。";
	}
}

msg = Encoder.toISO(msg);
//记日志
LogItem.getInstance().writeItemLog_other(request, 1, device_id, msg, Encoder.toISO("成功"), "/paramConfig/TimeZoneConfig.jsp");


%>
<%@page import="java.net.URLDecoder"%>
<html>
<head>
<script type="text/javascript">
parent.document.all("div_ping").innerHTML = "<%=html%>";
</script>
</head>
<body></body>
</html>
