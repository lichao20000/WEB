<%--
FileName	: IpLineSave.jsp
Author		: liuli
Date		: 2007-3-06
Note		: �ʺ����ӡ��޸ġ�ɾ������
--%>
<html>
<%@ page import="java.util.Map"%>
<jsp:useBean id="configMgr" scope="request" class="com.linkage.litms.paramConfig.ConfigureManager"/>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");
	// �������豸ID
	String strDevice = request.getParameter("device_id");
	String[] device_list = strDevice.split(",");
	String device_id = "";
	String device_name = "";
	// ��ǰ����״̬,�Ƿ����� ����:true ����:false
	boolean flag = false;
	// ���÷�ʽ
	String type= request.getParameter("type");
	// ���÷�ʽ������
	String typeDesc = "1".equals(type) ? "tr069" : "snmp";
	
	String html = "";
	String tempStr = "";
	Map map = null;
	
			+ " & ���÷�ʽ:" + type 
			+ " & ���÷�ʽ����:" + typeDesc);
	
	int deviceNum = device_list.length;
	for (int i=0;i < deviceNum;i++){
		device_id = device_list[i];
		Cursor cursor = DataSetBean.getCursor("select device_name from tab_gw_device where device_id='" + device_id + "'");
		Map fields = cursor.getNext();
		if (fields != null){
			device_name = (String)fields.get("device_name");
			map = configMgr.getNTPStatus(device_id,type);
			flag = false;
			String main_server = "";
			String second_server = "";
			if (null != map && 0 < map.size()) {
				flag = true;
				if (1 == map.size()) {
					main_server = (String)map.get("server_1");
				}
				if (2 <= map.size()) {
					main_server = (String)map.get("server_1");
					second_server = (String)map.get("server_2");
				}
			}
			tempStr = device_name + " ��ǰ����״̬��" +(flag?"����":"����")+"<br>"
					+ "&nbsp;&nbsp;  �� NTP ��������ַ��������" + main_server + "<br>"
					+ "&nbsp;&nbsp;  �� NTP ��������ַ��������" + second_server + "<br>";
			html += tempStr;	
		}
	}
	

%>

<body>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("div_ping").innerHTML = "<%=html%>";
	parent.setStatus();
</SCRIPT>
</body>
</html>
