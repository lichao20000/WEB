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
	html += "���豸��֧����SNMPЭ������DNS��Ϣ";
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
				html += "��ȡ����ֵʧ�ܣ�����ACS�����Ƿ���ȷ<br>";
			}
			else{
				//InternetGatewayDevice.ManagementServer.URL(URL)
				DNSServers = (String)paraMap.get("0");
				if (DNSServers != null){
					html += "��ȡURL���óɹ�...<br>";
					html += fields.get("device_name") + "��URLΪ��" + DNSServers+"<br>";
				}
				else{
					html += "��ȡURL����ʧ��...<br>";
					DNSServers = "";
				}
				//ManagementServer.Username(Username)
				DNSServers = (String)paraMap.get("1");
				if (DNSServers != null){
					html += "��ȡUsername���óɹ�...<br>";
					html += fields.get("device_name") + "��UsernameΪ��" + DNSServers+"<br>";
				}
				else{
					html += "��ȡUsername����ʧ��...<br>";
					DNSServers = "";
				}
				//InternetGatewayDevice.ManagementServer.Password(Password)
				DNSServers = (String)paraMap.get("2");
				if (DNSServers != null){
					html += "��ȡPassword���óɹ�...<br>";
					html += fields.get("device_name") + "��PasswordΪ��" + DNSServers+"<br>";
				}
				else{
					html += "��ȡPassword����ʧ��...<br>";
					DNSServers = "";
				}
				//InternetGatewayDevice.ManagementServer.PeriodicInformEnable(�ϱ����ڿ���)
				DNSServers = (String)paraMap.get("3");
				if (DNSServers != null){
					html += "��ȡ�ϱ����ڿ������óɹ�...<br>";
					html += fields.get("device_name") + "���ϱ����ڿ���Ϊ��" + DNSServers+"<br>";
				}
				else{
					html += "��ȡ�ϱ����ڿ�������ʧ��...<br>";
					DNSServers = "";
				}
				//InternetGatewayDevice.ManagementServer.PeriodicInformInterval(�ϱ�����)
				DNSServers = (String)paraMap.get("4");
				if (DNSServers != null){
					html += "��ȡ�ϱ��������óɹ�...<br>";
					html += fields.get("device_name") + "���ϱ�����Ϊ��" + DNSServers+"<br>";
				}
				else{
					html += "��ȡ�ϱ���������ʧ��...<br>";
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
