<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ page import="com.linkage.litms.paramConfig.ConfigDevice" %>

<%
String timeZone = "";
String timeZoneName = "";
String device_id = request.getParameter("device_id");
String type= request.getParameter("type");
String html = "";

if ("2".equals(type)){
	html += "���豸��֧����SNMPЭ������DNS��Ϣ";
}
else{
	String[] DHCPName = new String[2];
	DHCPName[0] = "InternetGatewayDevice.Time.LocalTimeZone";//ConfigDevice.getParaArr("384", device_id);
	DHCPName[1] = "InternetGatewayDevice.Time.LocalTimeZoneName";
	Map paraMap = ConfigDevice.getDevInfo_tr069(device_id, curUser.getUser(), DHCPName);
	if (paraMap == null){
		html += "��ȡ����ֵʧ�ܣ�����ACS�����Ƿ���ȷ<br>";
	}
	else{
		timeZone = (String)paraMap.get("0");
		timeZoneName = (String)paraMap.get("1");
		
		if (timeZone != null&&(timeZoneName!=null)){
			html += "��ȡʱ�����óɹ�...<br>";
		}
		else{
			html += "��ȡʱ������ʧ��...<br>";
			timeZone = "";
			timeZoneName = "";
		}
	}
	
}

%>
<html>
<head>
<script type="text/javascript">
parent.document.frm.timeZone.value = "<%=timeZone%>";
parent.document.frm.timeZoneName.value = "<%=timeZoneName%>";
parent.document.all("para1").style.display = "";
parent.document.all("para2").style.display = "";
parent.document.all("bt_set").style.display = "";
parent.document.all("div_ping").innerHTML = "<%=html%>";
</script>
</head>
<body></body>
</html>
