<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ page import="com.linkage.litms.paramConfig.ConfigDevice" %>
<%@ page import="com.linkage.litms.paramConfig.DHCPConfig" %>

<%
String device_id = request.getParameter("device_id");
String type= request.getParameter("type");
String html = "";

String[] DHCPOID = new String[9];
DHCPOID[0] = "377";
DHCPOID[1] = "382";
DHCPOID[2] = "385";
DHCPOID[3] = "386";
DHCPOID[4] = "378";
DHCPOID[5] = "379";
DHCPOID[6] = "380";
DHCPOID[7] = "381";
DHCPOID[8] = "383";

String[] DHCPName = new String[9];
for (int i=0;i<DHCPOID.length;i++){
	DHCPName[i] = ConfigDevice.getParaArr(DHCPOID[i], device_id);
}

String[] value = new String[9];
value[0] = request.getParameter("DHCPServerEnable");
value[1] = request.getParameter("DHCPAddressInfo");
value[2] = request.getParameter("statusArr");
value[3] = request.getParameter("DHCPListArr");
value[4] = request.getParameter("IpListArr");
value[5] = request.getParameter("SubnetMaskArr");
value[6] = request.getParameter("MinAddressArr");
value[7] = request.getParameter("MaxAddressArr");
value[8] = request.getParameter("LeaseTimeArr");


List<String[]> valueList = new ArrayList<String[]>();
valueList.add(0,value[0].split(";"));
valueList.add(1,value[1].split(";"));
valueList.add(2,value[2].split(";"));
valueList.add(3,value[3].split(";"));
valueList.add(4,value[4].split(";"));
valueList.add(5,value[5].split(";"));
valueList.add(6,value[6].split(";"));
valueList.add(7,value[7].split(";"));
valueList.add(8,value[8].split(";"));

String msg = "����DHCP�����Ϣ��";

int[] ret = null;


if ("2".equals(type)){
	DHCPConfig dhcp = new DHCPConfig();
	ret = dhcp.setDHCPInfo(device_id, valueList);
}
else{
	ret = ConfigDevice.setDevInfo_tr069_all(device_id, user, DHCPName, valueList);
}

if (ret != null && ret.length == DHCPName.length){
	if (!"".equals(value[0])){
		if (ret[0] == 1){
			html += "����DHCP����״̬�ɹ�...<br>";
			msg += "����DHCP����״̬�ɹ���ֵΪ" + value[0] + "��";
		}
		else if (ret[0] == 0){
			html += "����DHCP����״̬ʧ��...<br>";
			msg += "����DHCP����״̬ʧ�ܣ�ֵΪ" + value[0] + "��";
		}
	}
	if (!"".equals(value[1])){
		if (ret[1] == 1){
			html += "����DHCP��Ԥ����ַ�ɹ�...<br>";
			msg += "���ó�Ԥ����ַ�ɹ���ֵΪ" + value[1] + "��";
		}
		else if (ret[1] == 0){
			html += "����DHCP��Ԥ����ַʧ��...<br>";
			msg += "���ó�Ԥ����ַʧ�ܣ�ֵΪ" + value[1] + "��";
		}
	}
	if (!"".equals(value[2])){
		if (ret[2] == 1){
			html += "����DHCP��ַ������״̬�ɹ�...<br>";
			msg += "����DHCP��ַ������״̬�ɹ���ֵΪ" + value[2] + "��";
		}
		else if (ret[2] == 0){
			html += "����DHCP��ַ������״̬ʧ��...<br>";
			msg += "����DHCP��ַ������״̬ʧ�ܣ�ֵΪ" + value[2] + "��";
		}
	}
	if (!"".equals(value[3])){
		if (ret[3] == 1){
			html += "����DHCP��ַ���б���Ϣ�ɹ�...<br>";
			msg += "����DHCP��ַ���б���Ϣ�ɹ���ֵΪ" + value[3] + "��";
		}
		else if (ret[3] == 0){
			html += "����DHCP��ַ���б���Ϣʧ��...<br>";
			msg += "����DHCP��ַ���б���Ϣʧ�ܣ�ֵΪ" + value[3] + "��";
		}
	}
	if (!"".equals(value[4])){
		if (ret[4] == 1){
			html += "����DHCP��ַ�����γɹ�...<br>";
			msg += "���õ�ַ�����γɹ���ֵΪ" + value[4] + "��";
		}
		else if (ret[4] == 0){
			html += "����DHCP��ַ������ʧ��...<br>";
			msg += "���õ�ַ������ʧ�ܣ�ֵΪ" + value[4] + "��";
		}
	}
	if (!"".equals(value[5])){
		if (ret[5] == 1){
			html += "����DHCP����ɹ�...<br>";
			msg += "��������ɹ���ֵΪ" + value[5] + "��";
		}
		else if (ret[5] == 0){
			html += "����DHCP����ʧ��...<br>";
			msg += "��������ʧ�ܣ�ֵΪ" + value[5] + "��";
		}
	}
	if (!"".equals(value[6])){
		if (ret[6] == 1){
			html += "����DHCP��ʼ��ַ�ɹ�...<br>";
			msg += "������ʼ��ַ�ɹ���ֵΪ" + value[6] + "��";
		}
		else if (ret[6] == 0){
			html += "����DHCP��ʼ��ַʧ��...<br>";
			msg += "������ʼ��ַʧ�ܣ�ֵΪ" + value[6] + "��";
		}
	}
	if (!"".equals(value[7])){
		if (ret[7] == 1){
			html += "����DHCP������ַ�ɹ�...<br>";
			msg += "���ý�����ַ�ɹ���ֵΪ" + value[7] + "��";
		}
		else if (ret[7] == 0){
			html += "����DHCP������ַʧ��...<br>";
			msg += "���ý�����ַʧ�ܣ�ֵΪ" + value[7] + "��";
		}
	}
	if (!"".equals(value[8])){
		if (ret[8] == 1){
			html += "����DHCP���ڳɹ�...<br>";
			msg += "�������ڳɹ���ֵΪ" + value[8] + "��";
		}
		else if (ret[8] == 0){
			html += "����DHCP����ʧ��...<br>";
			msg += "��������ʧ�ܣ�ֵΪ" + value[8] + "��";
		}
	}
}
else{
	html += "����DHCP��Ϣʧ��...<br>";
}

msg = Encoder.toISO(msg);
//����־
LogItem.getInstance().writeItemLog_other(request, 1, device_id, msg, Encoder.toISO("�ɹ�"), "DHCP����");

%>
<html>
<head>
<script type="text/javascript">
parent.document.all("div_ping").innerHTML = "<%=html%>";
</script>
</head>
<body></body>
</html>
