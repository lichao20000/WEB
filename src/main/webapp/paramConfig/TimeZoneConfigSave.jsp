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

String msg = "����ʱ�������Ϣ��";

if ("2".equals(type)){
	html += "���豸��֧����SNMPЭ������ʱ��<br>";
}
else{
	int[] ret = ConfigDevice.setDevInfo_tr069(device_id, curUser.getUser(), DHCPName, value, typeArr);
	if (ret != null && ret.length > 0){
		if (ret[0] == 1){
			html += "����ʱ���ɹ�...<br>";
			msg += "����ʱ���ɹ���ʱ��ֵΪ" + value[0] + "ʱ������ֵΪ"+timeName+"��";
		}
		else{
			html += "����ʱ��ʧ��...<br>";
			msg += "����ʱ��ʧ�ܣ�ֵΪ" + value[0] + "ʱ������ֵΪ"+timeName+"��";
		}
	}
	else{
		html += "����ʱ��ʧ��...<br>";
		msg += "����ʱ��ʧ�ܣ�ֵΪ" + value[0] + "ʱ������ֵΪ"+timeName+"��";
	}
}

msg = Encoder.toISO(msg);
//����־
LogItem.getInstance().writeItemLog_other(request, 1, device_id, msg, Encoder.toISO("�ɹ�"), "/paramConfig/TimeZoneConfig.jsp");


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
