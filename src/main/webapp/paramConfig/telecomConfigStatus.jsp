<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ page import="com.linkage.litms.paramConfig.ConfigDevice" %>

<%
String RemoteEnable = "";
String Password = "";
String strDevice = request.getParameter("device_id");
String[] device_list = strDevice.split(",");
String device_id = "";
String type= request.getParameter("type");
String html = "";

if ("2".equals(type)){
	html += "���豸��֧����SNMPЭ�����õ���ά���ʺ���Ϣ";
}
else{
	String[] DHCPName = new String[2];
	
	for (int i=0;i<device_list.length;i++){
		
		device_id = device_list[i];
		Cursor cursor = DataSetBean.getCursor("select device_name from tab_gw_device where device_id='" + device_id + "'");
		Map fields = cursor.getNext();
		if (fields != null){
			DHCPName[0] = ConfigDevice.getParaArr("415", device_id);
			DHCPName[1] = ConfigDevice.getParaArr("416", device_id);
			Map paraMap = ConfigDevice.getDevInfo_tr069(device_id, curUser.getUser(), DHCPName);
			if (paraMap == null){
				html += "��ȡ����ֵʧ�ܣ�����ACS�����Ƿ���ȷ<br>";
			}
			else{
				RemoteEnable = (String)paraMap.get("0");
				if (RemoteEnable != null){
					html += "��ȡ�ʺ��Ƿ����óɹ�...<br>";
					html += fields.get("device_name") + "���ʺ��Ƿ�����Ϊ��" + RemoteEnable + "<br>";
				}
				else{
					html += "��ȡ�ʺ��Ƿ�����ʧ��...<br>";
					RemoteEnable = "";
				}
				Password = (String)paraMap.get("1");
				if (Password != null){
					html += "��ȡ�ʺ�����ɹ�...<br>";
					html += fields.get("device_name") + "���ʺ�����Ϊ��" + Password + "<br>";
				}
				else{
					html += "��ȡ�ʺ�����ʧ��...<br>";
					RemoteEnable = "";
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
