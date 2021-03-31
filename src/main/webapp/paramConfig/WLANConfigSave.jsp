<%--
FileName	: IpLineSave.jsp
Author		: liuli
Date		: 2007-3-06
Note		: �ʺ����ӡ��޸ġ�ɾ������
--%>
<html>
<%@ page import="java.util.Map,java.util.Iterator,java.util.Set,com.linkage.litms.common.util.Encoder,com.linkage.litms.system.dbimpl.LogItem"%>
<jsp:useBean id="configMgr" scope="request" class="com.linkage.litms.paramConfig.ConfigureManager"/>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");
	String action_type = request.getParameter("action_type");

	String device_id_ = request.getParameter("device_id");
	String channel_ = request.getParameter("channel");
	String power_ = request.getParameter("power");
	String status_ = request.getParameter("status");
	String model_ = request.getParameter("model");
	String type_ = request.getParameter("type");
	type_ = "1".equals(type_) ? "tr069" : "snmp";
	String msg_ = "���÷�ʽ��" + type_ + "��" + ("1".equals(status_) ? "���÷���״̬��������" : "����NTP״̬�����գ�") + ("2".equals(model_) ? "��������ģʽ��Dot11b��" : "��������ģʽ��Dot11g��") + "���������ŵ���" + channel_ + "��" + "���÷��书�ʣ�" + power_;
	msg_ = Encoder.toISO(msg_);
	String result_ = "�ɹ�";


	Map map = null;
	String wlan_status = "";
	String wlan_model = "";
	String wlan_channel = "";
	String wlan_power = "";
	String oui = "";

	if ("1".equals(action_type)) {
		map = configMgr.getWLANStatus(request);
		if (null != map && 0 < map.size()) {
			oui = (String)map.get("oui");
			wlan_status = (String)map.get("wlan_status");
			if (wlan_status == null) {
				wlan_status = "";
			}
			wlan_model = (String)map.get("wlan_model");
			if (wlan_model == null) {
				wlan_model = "";
			}
			if ("000FE2".equals(oui)) {
				wlan_channel = (String)map.get("wlan_channel");
				if (wlan_channel == null) {
					wlan_channel = "";
				}
				wlan_power = (String)map.get("wlan_power");
				if (wlan_power == null) {
					wlan_power = "";
				}
			}
		}
	} else {
		map = configMgr.WLANConfigure(request);
	}

	String strlist = "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
	strlist += "<tr>";
	strlist += "<td bgcolor='#FFFFFF' width='30%' nowrap>";
	strlist += "�豸����";
	strlist += "</td><td bgcolor='#FFFFFF' width='70%' nowrap>";
	strlist += "���ؽ��";
	strlist += "</td></tr>";
  	
	if(map==null) {
		strlist += "<tr class='blue_foot'>";
		strlist += "<td bgcolor='#FFFFFF' width='30%' nowrap>";
		strlist += "</td>";
		strlist += "<td bgcolor='#FFFFFF' width='70%' nowrap>";
		strlist += "��ȡֵʧ��";
		strlist += "</td></tr>";
		result_ = "ʧ��";
	}else{
		if (map.size() == 0) {
			strlist += "<tr class='blue_foot'>";
			strlist += "<td bgcolor='#FFFFFF' width='30%' nowrap>";
			strlist += "";
			strlist += "</td><td bgcolor='#FFFFFF' width='70%'>";
			strlist += "<font color=red>�豸���Ӳ��ϻ�֧�ָò�����</font>";
			strlist += "</td></tr>";
			result_ = "ʧ��";
		} else {
		Set set = map.keySet();
		Iterator iterator = set.iterator();
		String name = null;
		String value = null;
		while(iterator.hasNext()){
			name = (String)iterator.next();
			value = (String)map.get(name);

			name = name.substring(0,name.length() - 1);
			strlist += "<tr class='blue_foot'>";
			strlist += "<td bgcolor='#FFFFFF' width='30%' nowrap>";
			strlist += name;
			strlist += "</td><td bgcolor='#FFFFFF' width='70%'>";
			strlist += value;
			strlist += "</td></tr>";
		}
		}
	}
	
	LogItem.getInstance().writeItemLog_other(request, 1, device_id_, msg_, Encoder.toISO(result_), "WLAN����");
	strlist += "</table>";

%>

<body>
<SPAN ID="child"><%=strlist%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
var oui = "<%=oui%>";
var action_type = "<%=action_type%>";
if (action_type == "1") {
	var wlan_status = "<%=wlan_status%>";
	var wlan_model = "<%=wlan_model%>";
	var wlan_channel = "<%=wlan_channel%>";
	var wlan_power = "<%=wlan_power%>";
	var arr = new Array();
	arr[0] = oui;
	arr[1] = wlan_status;
	arr[2] = wlan_model;
	arr[3] = wlan_channel;
	arr[4] = wlan_power;

	parent.setStatus(arr);
} else {
	parent.document.all("div_ping").innerHTML = document.getElementById("child").innerHTML;
}
</SCRIPT>
</body>
</html>
