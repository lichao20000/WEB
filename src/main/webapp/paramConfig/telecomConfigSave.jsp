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

String msg = "���õ���ά���ʺţ�";

if ("2".equals(type)){
	html += "���豸��֧����SNMPЭ�����õ���ά���ʺ�<br>";
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
					html += "����" + fields.get("device_name") + "���ʺ��Ƿ����óɹ�...<br>";
					msg += "����" + fields.get("device_name") + "���ʺ��Ƿ����óɹ���ֵΪ" + value[0] + "��";
				}
				else{
					html += "����" + fields.get("device_name") + "���ʺ��Ƿ�����ʧ��...<br>";
					msg += "����" + fields.get("device_name") + "���ʺ��Ƿ�����ʧ�ܣ�ֵΪ" + value[0] + "��";
				}
				if (ret[1] == 1){
					html += "����" + fields.get("device_name") + "���ʺ�����ɹ�...<br>";
					msg += "����" + fields.get("device_name") + "���ʺ�����ɹ���ֵΪ" + value[0] + "��";
				}
				else{
					html += "����" + fields.get("device_name") + "���ʺ�����ʧ��...<br>";
					msg += "����" + fields.get("device_name") + "���ʺ�����ʧ�ܣ�ֵΪ" + value[0] + "��";
				}
			}
			else{
				html += "����" + fields.get("device_name") + "�ĵ���ά���ʺ�ʧ��...<br>";
				msg += "����" + fields.get("device_name") + "�ĵ���ά���ʺ�ʧ�ܡ�";
			}
		}
	}
}

msg = Encoder.toISO(msg);
//����־
LogItem.getInstance().writeItemLog_other(request, 1, device_id, msg, Encoder.toISO("�ɹ�"), "���õ���ά���ʺ�");


%>
<html>
<head>
<script type="text/javascript">
parent.document.all("div_ping").innerHTML = "<%=html%>";
</script>
</head>
<body></body>
</html>
