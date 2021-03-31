<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ page import="com.linkage.litms.paramConfig.ConfigDevice" %>
<%@ page import="com.linkage.litms.paramConfig.DHCPConfig" %>

<%
//DHCP����״̬
String DHCPServerEnable = "";
//DHCP��ַ��Ԥ����ַ
String DHCPAddressInfo = "";
//DHCP��ַ��Ԥ����ַ��ʼ��ַ
String DHCPMinAddress = "";
//DHCP��ַ��Ԥ����ַ������ַ
String DHCPMaxAddress = "";
//��ַ���Ƿ�����
String status = "";
//DHCP��ַ���б���Ϣ
String DHCPList = "";
//DHCP��ַ��������Ϣ
String IPRouters = "";
//DHCP��ַ��������Ϣ
String SubnetMask = "";
//DHCP��ַ����ʼ��ַ
String MinAddress = "";
//DHCP��ַ�ؽ�����ַ
String MaxAddress = "";
//����
String DHCPLeaseTime = "";

String device_id = request.getParameter("device_id");
String type= request.getParameter("type");
String html = "";
String dataHtml = "";

if ("2".equals(type)){
	html += "���豸��֧����SNMPЭ������DHCP��Ϣ<br>";
	DHCPConfig dhcp = new DHCPConfig();
	String[] dhcpInfo = dhcp.getDHCPInfo(device_id);
	
	if (dhcpInfo != null && dhcpInfo.length == 10){
		DHCPServerEnable = dhcpInfo[0];
		DHCPAddressInfo = dhcpInfo[1];
		String[] DHCPListArr = dhcpInfo[3].split("<br>");
		String[] IpListArr = dhcpInfo[4].split("<br>");
		String[] SubnetMaskArr = dhcpInfo[5].split("<br>");
		String[] MinAddressArr = dhcpInfo[6].split("<br>");
		String[] MaxAddressArr = dhcpInfo[7].split("<br>");
		String[] statusArr = dhcpInfo[8].split("<br>");
		
		dataHtml += "<TR bgcolor='#FFFFFF'><th>��ַ���Ƿ�����</th><th>��ַ���б���Ϣ</th><th>����</th><th>����</th><th>��ʼ��ַ</th><th>������ַ</th><th>����</th></TR>";
		for (int i=0;i<DHCPListArr.length;i++){
			//��ʾ����html
			dataHtml += "<tr bgcolor='#FFFFFF'>";
			if (statusArr.length > i){
				dataHtml += "<td><input type='text' name='statusArr' value='" + statusArr[i] + "'/></td>";
			}
			else{
				dataHtml += "<td><input type='text' name='statusArr' value=''/></td>";
			}
			dataHtml += "<td><input type='text' name='DHCPListArr' value='" + DHCPListArr[i] + "'/></td>";
			if (IpListArr.length > i){
				dataHtml += "<td><input type='text' name='IpListArr' value='" + IpListArr[i] + "'/></td>";
			}
			else{
				dataHtml += "<td><input type='text' name='IpListArr' value=''/></td>";
			}
			if (SubnetMaskArr.length > i){
				dataHtml += "<td><input type='text' name='SubnetMaskArr' value='" + SubnetMaskArr[i] + "'/></td>";
			}
			else{
				dataHtml += "<td><input type='text' name='SubnetMaskArr' value=''/></td>";
			}
			if (MinAddressArr.length > i){
				dataHtml += "<td><input type='text' name='MinAddressArr' value='" + MinAddressArr[i] + "'/></td>";
			}
			else{
				dataHtml += "<td><input type='text' name='MinAddressArr' value=''/></td>";
			}
			if (MaxAddressArr.length > i){
				dataHtml += "<td><input type='text' name='MaxAddressArr' value='" + MaxAddressArr[i] + "'/></td>";
			}
			else{
				dataHtml += "<td><input type='text' name='MaxAddressArr' value=''/></td>";
			}
			dataHtml += "<td><input type='text' name='LeaseTimeArr' value=''/></td>";
			dataHtml += "</tr>";
		}
		
		html = dhcpInfo[9];
	}
}
else{
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
	
	String vlanPara = "";
	for (int i=0;i<DHCPOID.length;i++){
		vlanPara += ConfigDevice.getParaArr(DHCPOID[i], device_id) + "#";
	}
	String[] DHCPName = vlanPara.split("#");
	
	//����̨��ȡ����ֵ
	Map paraMap = ConfigDevice.getDevInfo_tr069_all(device_id, curUser.getUser(), DHCPName);
	if (paraMap == null){
		html += "��ȡ����ֵʧ�ܣ�����ACS�����Ƿ���ȷ<br>";
	}
	else{
		DHCPServerEnable = (String)paraMap.get("0");
		DHCPAddressInfo = (String)paraMap.get("1");
		status = (String)paraMap.get("2");
		DHCPList = (String)paraMap.get("3");
		IPRouters = (String)paraMap.get("4");
		SubnetMask = (String)paraMap.get("5");
		MinAddress = (String)paraMap.get("6");
		MaxAddress = (String)paraMap.get("7");
		DHCPLeaseTime = (String)paraMap.get("8");
		//��ʾ����html
		if (DHCPServerEnable != null && !"null".equals(DHCPServerEnable)){
			html += "��ȡDHCP����״̬�ɹ�...<br>";
		}
		else{
			html += "��ȡDHCP����״̬ʧ��...<br>";
			DHCPServerEnable = "";
		}
		if (DHCPAddressInfo != null && !"null".equals(DHCPAddressInfo)){
			html += "��ȡDHCP��Ԥ����ַ�ɹ�...<br>";
		}
		else{
			html += "��ȡDHCP��Ԥ����ַʧ��...<br>";
			DHCPAddressInfo = "";
		}
		if (status != null && !"null".equals(status)){
			html += "��ȡDHCP����״̬�ɹ�...<br>";
		}
		else{
			html += "��ȡDHCP����״̬ʧ��...<br>";
			status = "";
		}
		if (DHCPList != null && !"null".equals(DHCPList)){
			html += "��ȡDHCP��ַ���б���Ϣ�ɹ�...<br>";
		}
		else{
			html += "��ȡDHCP��ַ���б���Ϣʧ��...<br>";
			DHCPList = "";
		}
		if (IPRouters != null && !"null".equals(IPRouters)){
			html += "��ȡDHCP��ַ�����γɹ�...<br>";
		}
		else{
			html += "��ȡDHCP��ַ������ʧ��...<br>";
			IPRouters = "";
		}
		if (SubnetMask != null && !"null".equals(SubnetMask)){
			html += "��ȡDHCP����ɹ�...<br>";
		}
		else{
			html += "��ȡDHCP����ʧ��...<br>";
			SubnetMask = "";
		}
		if (MinAddress != null && !"null".equals(MinAddress)){
			html += "��ȡDHCP��ʼ��ַ�ɹ�...<br>";
		}
		else{
			html += "��ȡDHCP��ʼ��ַʧ��...<br>";
			MinAddress = "";
		}
		if (MaxAddress != null && !"null".equals(MaxAddress)){
			html += "��ȡDHCP������ַ�ɹ�...<br>";
		}
		else{
			html += "��ȡDHCP������ַʧ��...<br>";
			MaxAddress = "";
		}
		if (DHCPLeaseTime != null && !"null".equals(DHCPLeaseTime)){
			html += "��ȡDHCP���ڳɹ�...<br>";
		}
		else{
			html += "��ȡDHCP����ʧ��...<br>";
			DHCPLeaseTime = "";
		}
		
		String[] statusArr = status.split("<br>");
		String[] DHCPListArr = DHCPList.split("<br>");
		String[] IpListArr = IPRouters.split("<br>");
		String[] SubnetMaskArr = SubnetMask.split("<br>");
		String[] MinAddressArr = MinAddress.split("<br>");
		String[] MaxAddressArr = MaxAddress.split("<br>");
		String[] LeaseTimeArr = DHCPLeaseTime.split("<br>");
		
		dataHtml += "<TR bgcolor='#FFFFFF'><th>��ַ���Ƿ�����</th><th>��ַ���б���Ϣ</th><th>����</th><th>����</th><th>��ʼ��ַ</th><th>������ַ</th><th>����</th></TR>";
		for (int i=0;i<IpListArr.length;i++){
			//��ʾ����html
			dataHtml += "<tr bgcolor='#FFFFFF'>";
			if (statusArr.length > i){
				dataHtml += "<td><input type='text' name='statusArr' value='" + statusArr[i] + "'/></td>";
			}
			else{
				dataHtml += "<td><input type='text' name='statusArr' value=''/></td>";
			}
			if (DHCPListArr.length > i){
				dataHtml += "<td><input type='text' name='DHCPListArr' value='" + DHCPListArr[i] + "'/></td>";
			}
			else{
				dataHtml += "<td><input type='text' name='DHCPListArr' value=''/></td>";
			}
			dataHtml += "<td><input type='text' name='IpListArr' value='" + IpListArr[i] + "'/></td>";
			if (SubnetMaskArr.length > i){
				dataHtml += "<td><input type='text' name='SubnetMaskArr' value='" + SubnetMaskArr[i] + "'/></td>";
			}
			else{
				dataHtml += "<td><input type='text' name='SubnetMaskArr' value=''/></td>";
			}
			if (MinAddressArr.length > i){
				dataHtml += "<td><input type='text' name='MinAddressArr' value='" + MinAddressArr[i] + "'/></td>";
			}
			else{
				dataHtml += "<td><input type='text' name='MinAddressArr' value=''/></td>";
			}
			if (MaxAddressArr.length > i){
				dataHtml += "<td><input type='text' name='MaxAddressArr' value='" + MaxAddressArr[i] + "'/></td>";
			}
			else{
				dataHtml += "<td><input type='text' name='MaxAddressArr' value=''/></td>";
			}
			if (LeaseTimeArr.length > i){
				dataHtml += "<td><input type='text' name='LeaseTimeArr' value='" + LeaseTimeArr[i] + "'/></td>";
			}
			else{
				dataHtml += "<td><input type='text' name='LeaseTimeArr' value=''/></td>";
			}
			dataHtml += "</tr>";
		}
		
		
		
	}
	
}

%>
<html>
<head>
<script type="text/javascript">
parent.document.all("bt_set").style.display = "";
parent.document.all("div_ping").innerHTML = "<%=html%>";

parent.document.frm.DHCPServerEnable.value = "<%=DHCPServerEnable%>";
parent.document.frm.DHCPAddressInfo.value = "<%=DHCPAddressInfo%>";
parent.document.all("para1").style.display = "";
parent.document.all("para2").style.display = "";

var dataHtml = "<%=dataHtml%>";
parent.setParaHtml(dataHtml);
</script>
</head>
<body></body>
</html>
