<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ page import="com.linkage.litms.paramConfig.ConfigDevice" %>
<%@ page import="com.linkage.litms.paramConfig.ParamTreeObject" %>

<%
String strDevice = request.getParameter("device_id");
String[] device_list = strDevice.split(",");
String device_id = "";
String type= request.getParameter("type");
String html = "";

String[] DHCPName = new String[2];


String[] value = new String[2];
value[0] = request.getParameter("username");
value[1] = request.getParameter("password");

String[] typeArr = new String[2];
typeArr[0] = "1";
typeArr[1] = "1";

String msg = "����DNS�����Ϣ��";

if ("2".equals(type)){
	html += "���豸��֧����SNMPЭ������PPPoe����<br>";
}
else{
	for (int i=0;i<device_list.length;i++){
		
		device_id = device_list[i];
		Cursor cursor = DataSetBean.getCursor("select device_name from tab_gw_device where device_id='" + device_id + "'");
		Map fields = cursor.getNext();
		if (fields != null){
			DHCPName[0] = ConfigDevice.getParaArr("501", device_id);
			DHCPName[1] = ConfigDevice.getParaArr("502", device_id);
			ParamTreeObject paramTreeObject = new ParamTreeObject();
			
			String gather_id = "";
			Cursor cursor1 = DataSetBean.getCursor("select gather_id from tab_gw_device where device_id='" + device_id + "'");
			Map fields1 = cursor1.getNext();
			if (fields1 != null){
				gather_id = (String)fields1.get("gather_id");
			}
			
			String ior = user.getIor("ACS_" + gather_id, "ACS_Poa_" + gather_id);
			int ret = paramTreeObject.setParaValueFlag_multi(DHCPName, ior, device_id, gather_id , value);
			
			if (ret == 1){
				html += "����" + fields.get("device_name") + "��PPPoe��Ϣ�ɹ�...<br>";
				msg += "����" + fields.get("device_name") + "��PPPoe��Ϣ�ɹ����û���Ϊ" + value[0] + "������Ϊ" + value[1];
			}
			else{
				html += "����" + fields.get("device_name") + "��PPPoe��Ϣʧ��...<br>";
				msg += "����" + fields.get("device_name") + "��PPPoe��Ϣʧ�ܣ�ֵΪ" + value[0] + "������Ϊ" + value[1];
			}
		}
	}
}

msg = Encoder.toISO(msg);
//����־
LogItem.getInstance().writeItemLog_other(request, 1, device_id, msg, Encoder.toISO("�ɹ�"), "PPPoe����");


%>
<html>
<head>
<script type="text/javascript">
parent.document.all("div_ping").innerHTML = "<%=html%>";
</script>
</head>
<body></body>
</html>
