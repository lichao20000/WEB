<%--
FileName	: IpLineSave.jsp
Author		: liuli
Date		: 2007-3-06
Note		: �ʺ����ӡ��޸ġ�ɾ������
--%>
<html>
<%@ page import="java.util.*,com.linkage.litms.common.util.Encoder,com.linkage.litms.system.dbimpl.LogItem"%>
<jsp:useBean id="configMgr" scope="request" class="com.linkage.litms.paramConfig.ConfigureManager"/>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");
	String device_id_ = request.getParameter("device_id");
	String main_ntp_server_ = request.getParameter("main_ntp_server");
	String second_ntp_server_ = request.getParameter("second_ntp_server");
	String status_ = request.getParameter("status");
	String type_ = request.getParameter("type");
	type_ = "1".equals(type_) ? "tr069" : "snmp";
	String msg_ = "���÷�ʽ��" + type_ + "��" + ("1".equals(status_) ? ("����NTP״̬��������������NTP��������ַ��������" + main_ntp_server_ + "�����ñ�NTP��������ַ��������" + second_ntp_server_) : "����NTP״̬������");
	msg_ = Encoder.toISO(msg_);
	String result_ = "ʧ��";

	Map map = configMgr.NTPConfigure(request);
	//Map map = null;
	//192.168.232.23
	//configMgr.testCreate("145","1.3.6.1.4.1.25506.8.22.2.1.1.1.36.172.32.42.6.3",2,"6");

	String strlist = "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
	strlist += "<tr>";
	strlist += "<td bgcolor='#FFFFFF' width='30%' nowrap>";
	strlist += "�豸����";
	strlist += "</td><td bgcolor='#FFFFFF' width='70%' nowrap>";
	strlist += "���ؽ��";
	strlist += "</td></tr>";
  	
	if(map==null || 0 == map.size()) {
		strlist += "<tr class='blue_foot'>";
		strlist += "<td bgcolor='#FFFFFF' width='30%' nowrap>";
		strlist += "</td>";
		strlist += "<td bgcolor='#FFFFFF' width='70%' nowrap>";
		strlist += "����ʧ��";
		strlist += "</td></tr>";
	}else{
		Set set = map.keySet();
		Iterator iterator = set.iterator();
		String name = null;
		String value = null;	
		while(iterator.hasNext()){
			name = (String)iterator.next();
			value = (String)map.get(name);
//			if(value.equals("true")){
//				value = "�豸�����ɹ���";
//			} else {
//				value = "�豸����ʧ�ܣ�";
//			}
			if ("NTP���óɹ�".equals(value) || "���óɹ�".equals(value) || "���óɹ�".equals(value)) {
				result_ = "�ɹ�";
			}
			strlist += "<tr class='blue_foot'>";
			strlist += "<td bgcolor='#FFFFFF' width='30%' nowrap>";
			strlist += name;
			strlist += "</td><td bgcolor='#FFFFFF' width='70%'>";
			strlist += value;
			strlist += "</td></tr>";
		}
	}
	
	LogItem.getInstance().writeItemLog_other(request, 1, device_id_, msg_, Encoder.toISO(result_), "NTP����");

	strlist += "</table>";

%>

<body>
<SPAN ID="child"><%=strlist%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("div_ping").innerHTML = child.innerHTML;
</SCRIPT>
</body>
</html>
