<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ page import="com.linkage.litms.paramConfig.ConfigDevice" %>
<%@ page import="com.linkage.litms.common.database.DataSetBean"%>
<%
String strDevice = request.getParameter("device_id");
String[] device_list = strDevice.split(",");
String device_id = "";
String type= request.getParameter("type");
String html = "";

String[] DHCPName = new String[5];


String[] value = new String[5];
value[0] = request.getParameter("url");
value[1] = request.getParameter("username");
value[2] = request.getParameter("password");
value[3] = request.getParameter("informenable");
value[4] = request.getParameter("informinterval");
String[] typeArr = new String[5];
typeArr[0] = "1";//string
typeArr[1] = "1";//string
typeArr[2] = "1";//string
typeArr[3] = "4";//unsignedInt
typeArr[4] = "3";//boolean
String msg = "����DNS�����Ϣ��";

if ("2".equals(type)){
	html += "���豸��֧����SNMPЭ������DNS����<br>";
}
else{
	String sql="";
	int[] ret;
	for (int i=0;i<device_list.length;i++){
		
		device_id = device_list[i];
		Cursor cursor = DataSetBean.getCursor("select device_name from tab_gw_device where device_id='" + device_id + "'");
		Map fields = cursor.getNext();
		if (fields != null){
			
			DHCPName[0] = ConfigDevice.getParaArr("400", device_id);
			DHCPName[1] = ConfigDevice.getParaArr("401", device_id);
			DHCPName[2] = ConfigDevice.getParaArr("402", device_id);
			DHCPName[3] = ConfigDevice.getParaArr("403", device_id);
			DHCPName[4] = ConfigDevice.getParaArr("404", device_id);
			ret = ConfigDevice.setDevInfo_tr069(device_id, curUser.getUser(), DHCPName, value, typeArr);
			if (ret != null && ret.length > 0){
				//InternetGatewayDevice.ManagementServer.URL(URL)
				if (ret[0] == 1){
					html += "����" + fields.get("device_name") + "��URL����ɹ�...<br>";
					msg += "����" + fields.get("device_name") + "��URL�ɹ���ֵΪ" + value[0] + "��<br>";
				}
				else{
					html += "����" + fields.get("device_name") + "��URL����ʧ��...<br>";
					msg += "����" + fields.get("device_name") + "��URL��ַʧ�ܣ�ֵΪ" + value[0] + "��<br>";
				}
				//ManagementServer.Username(Username)
				if (ret[1] == 1){
					html += "����" + fields.get("device_name") + "���û����ɹ�...<br>";
					msg += "����" + fields.get("device_name") + "���û����ɹ���ֵΪ" + value[1] + "��<br>";
					sql+="; update tab_gw_device set cpe_username='"+value[1]+"' where device_id='"+device_id+"'";
				}
				else{
					html += "����" + fields.get("device_name") + "���û�������ʧ��...<br>";
					msg += "����" + fields.get("device_name") + "���û���ʧ�ܣ�ֵΪ" + value[1] + "��<br>";
				}
				//InternetGatewayDevice.ManagementServer.Password(Password)
				if (ret[2] == 1){
					html += "����" + fields.get("device_name") + "������ɹ�...<br>";
					msg += "����" + fields.get("device_name") + "������ɹ���ֵΪ" + value[2] + "��<br>";
					sql+="; update tab_gw_device set cpe_passwd='"+value[2]+"' where device_id='"+device_id+"'";
				}
				else{
					html += "����" + fields.get("device_name") + "������ʧ��...<br>";
					msg += "����" + fields.get("device_name") + "������ʧ�ܣ�ֵΪ" + value[2] + "��<br>";
				}
				//InternetGatewayDevice.ManagementServer.PeriodicInformEnable(�ϱ����ڿ���)
				if (ret[3] == 1){
					html += "����" + fields.get("device_name") + "���ϱ����ڿ��سɹ�...<br>";
					msg += "����" + fields.get("device_name") + "���ϱ����ڿ��سɹ���ֵΪ" + value[3] + "��<br>";
				}
				else{
					html += "����" + fields.get("device_name") + "���ϱ����ڿ���ʧ��...<br>";
					msg += "����" + fields.get("device_name") + "���ϱ����ڿ���ʧ�ܣ�ֵΪ" + value[3] + "��<br>";
				}
				//InternetGatewayDevice.ManagementServer.PeriodicInformInterval(�ϱ�����)
				if (ret[4] == 1){
					html += "����" + fields.get("device_name") + "���ϱ����ڳɹ�...<br>";
					msg += "����" + fields.get("device_name") + "���ϱ����ڳɹ���ֵΪ" + value[4] + "��<br>";
				}
				else{
					html += "����" + fields.get("device_name") + "���ϱ�����ʧ��...<br>";
					msg += "����" + fields.get("device_name") + "���ϱ�����ʧ�ܣ�ֵΪ" + value[4] + "��<br>";
				}

			}
			else{
				html += "����" + fields.get("device_name") + "�Ĳ���ʧ��...<br>";
				msg += "����" + fields.get("device_name") + "�Ĳ���ʧ�ܡ�<br>";
			}
		}
	}
}

msg = Encoder.toISO(msg);
//����־
LogItem.getInstance().writeItemLog_other(request, 1, device_id, msg, Encoder.toISO("�ɹ�"), "�ϱ���������");


%>
<html>
<head>
<script type="text/javascript">
parent.document.all("div_ping").innerHTML = "<%=html%>";
</script>
</head>
<body></body>
</html>
