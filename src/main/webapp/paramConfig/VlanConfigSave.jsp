<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ page import="com.linkage.litms.paramConfig.ConfigDevice" %>

<%
String device_id = request.getParameter("device_id");
String type= request.getParameter("type");
String html = "";

String[] VLANOID = new String[5];
VLANOID[0] = "371";
VLANOID[1] = "372";
VLANOID[2] = "373";
VLANOID[3] = "374";
VLANOID[4] = "375";
//VLANOID[5] = "419";
//VLANOID[6] = "420";

String vlanPara = "";
for (int i=0;i<VLANOID.length;i++){
	vlanPara += ConfigDevice.getParaArr(VLANOID[i], device_id) + "#";
}
String[] VLANName = vlanPara.split("#");
String para_index = ConfigDevice.getParaArr("376", device_id);

String[] typeArr = new String[5];
typeArr[0] = "2";
typeArr[1] = "1";
typeArr[2] = "1";
typeArr[3] = "1";
typeArr[4] = "1";
//typeArr[5] = "1";
//typeArr[6] = "1";

String[] value = new String[6];
value[0] = request.getParameter("VLANID");
value[1] = request.getParameter("VLanInterface");
value[2] = request.getParameter("VlanName");
value[3] = request.getParameter("IPInterfaceIPAddress");
value[4] = request.getParameter("IPInterfaceSubnetMask");
value[5] = request.getParameter("vlanIdx");
//value[6] = "1";

String msg = "����VLAN�����Ϣ��";


if ("2".equals(type)){
	html += "���豸��֧����SNMPЭ������vlan��Ϣ<br>";
}
else{
	int[] ret = new ConfigDevice().setDevInfo_tr069_multi_(device_id, curUser.getUser(), VLANName, value, para_index, typeArr);

	if (ret != null && ret.length == VLANName.length){
		if (!"".equals(value[0])){
			if (ret[0] == 1){
				html += "����VLAN ID�ɹ�...<br>";
				msg += "����VLAN ID�ɹ���ֵΪ" + value[0] + "��";
			}
			else{
				html += "����VLAN IDʧ��...<br>";
				msg += "����VLAN IDʧ�ܣ�ֵΪ" + value[0] + "��";
			}
		}
		if (!"".equals(value[1])){
			if (ret[1] == 1){
				html += "����VLAN�󶨶˿��б�ɹ�...<br>";
				msg += "����VLAN�󶨶˿��б�ɹ���ֵΪ" + value[0] + "��";
			}
			else{
				html += "����VLAN�󶨶˿��б�ʧ��...<br>";
				msg += "����VLAN�󶨶˿��б�ʧ�ܣ�ֵΪ" + value[0] + "��";
			}
		}
		if (!"".equals(value[2])){
			if (ret[2] == 1){
				html += "����VLAN���Ƴɹ�...<br>";
				msg += "����VLAN���Ƴɹ���ֵΪ" + value[1] + "��";
			}
			else{
				html += "����VLAN����ʧ��...<br>";
				msg += "����VLAN����ʧ�ܣ�ֵΪ" + value[1] + "��";
			}
		}
		if (!"".equals(value[3])){
			if (ret[3] == 1){
				html += "����VLAN IP��ַ�ɹ�...<br>";
				msg += "����VLAN IP��ַ�ɹ���ֵΪ" + value[2] + "��";
			}
			else{
				html += "����VLAN IP��ַʧ��...<br>";
				msg += "����VLAN IP��ַʧ�ܣ�ֵΪ" + value[2] + "��";
			}
		}
		if (!"".equals(value[4])){
			if (ret[4] == 1){
				html += "����VLAN��������ɹ�...<br>";
				msg += "����VLAN��������ɹ���ֵΪ" + value[3] + "��";
			}
			else{
				html += "����VLAN��������ʧ��...<br>";
				msg += "����VLAN��������ʧ�ܣ�ֵΪ" + value[3] + "��";
			}
		}
	}
	else{
		html += "����VLAN��Ϣʧ��...<br>";
	}
}

msg = Encoder.toISO(msg);
//����־
LogItem.getInstance().writeItemLog_other(request, 1, device_id, msg, Encoder.toISO("�ɹ�"), "VLAN����");

%>
<html>
<head>
<script type="text/javascript">
parent.document.all("div_ping").innerHTML = "<%=html%>";
</script>
</head>
<body></body>
</html>
