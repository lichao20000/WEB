<%--
FileName	: IpLineSave.jsp
Author		: liuli
Date		: 2007-3-06
Note		: �ʺ����ӡ��޸ġ�ɾ������
--%>
<html>
<%@ page import="java.util.Map,java.util.TreeMap,java.util.List,java.util.Iterator,java.util.Set,com.linkage.litms.common.util.Encoder,com.linkage.litms.system.dbimpl.LogItem"%>
<jsp:useBean id="configMgr" scope="request" class="com.linkage.litms.paramConfig.ConfigureManager"/>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");
	String action_type = request.getParameter("action_type");

	Map map = null;
	String result = "";
	String all_ssid = "";
	String ssid_idx_list = "";
	String log_msg = "";
	String device_id_ = request.getParameter("device_id");
	String type_ = request.getParameter("type");
	type_ = "1".equals(type_) ? "tr069" : "snmp";
	String msg_ = "";
	String result_ = "�ɹ�";

	if ("1".equals(action_type)) {
		Map tm = configMgr.getWLANStatusTr069(request);
		if (null == tm || 0 == tm.size()) {
			result = "false";
		}
		if (null != tm && 0 < tm.size()) {
			Set st = tm.keySet();
			Iterator<String> it = st.iterator();
			String name = null;//wlanģ��id

				all_ssid += "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
				all_ssid += "<tr>";
				all_ssid += "<td align='center' bgcolor='#FFFFFF' class='green_title2' nowrap>ģ��ID</td>";
				all_ssid += "<td align='center' bgcolor='#FFFFFF' class='green_title2' nowrap>����ID</td>";
				all_ssid += "<td align='center' bgcolor='#FFFFFF' class='green_title2' nowrap>����(SSID)</td>";
				all_ssid += "<td align='center' bgcolor='#FFFFFF' class='green_title2' nowrap>����/�ر�</td>";
				all_ssid += "<td align='center' bgcolor='#FFFFFF' class='green_title2' nowrap>�㲥/����</td>";
				all_ssid += "<td align='center' bgcolor='#FFFFFF' class='green_title2' nowrap>�����ŵ�</td>";
				all_ssid += "<td align='center' bgcolor='#FFFFFF' class='green_title2' nowrap>����ģʽ</td>";
				all_ssid += "<td align='center' bgcolor='#FFFFFF' class='green_title2' nowrap>���书��</td>";
				all_ssid += "<td align='center' bgcolor='#FFFFFF' class='green_title2' nowrap>ɾ������</td>";
				all_ssid += "<td align='center' bgcolor='#FFFFFF' class='green_title2' nowrap>ɾ��ģ��</td>";
				all_ssid += "<td align='center' bgcolor='#FFFFFF' class='green_title2' nowrap>�½�SSID</td>";
				all_ssid += "</tr>";
			while(it.hasNext()){
				name = it.next();
				if ("".equals(ssid_idx_list)) {
					ssid_idx_list += name;
				} else {
					ssid_idx_list += "|" + name;
				}

				Map<String,Map<String,String>> ssids = (Map)tm.get(name);
				if (ssids == null) {
					all_ssid += "<tr  style=\'display:none\'>";
					all_ssid += "<td align='center' bgcolor='#FFFFFF' nowrap>";
					all_ssid += name;
					all_ssid += "</td>";
					all_ssid += "<td align='center' bgcolor='#FFFFFF' nowrap></td>";
					all_ssid += "<td align='center' bgcolor='#FFFFFF' nowrap></td>";
					all_ssid += "<td align='center' bgcolor='#FFFFFF' nowrap></td>";
					all_ssid += "<td align='center' bgcolor='#FFFFFF' nowrap></td>";
					all_ssid += "<td align='center' bgcolor='#FFFFFF' nowrap></td>";
					all_ssid += "<td align='center' bgcolor='#FFFFFF' nowrap></td>";
					all_ssid += "<td align='center' bgcolor='#FFFFFF' nowrap></td>";
					all_ssid += "<td align='center' bgcolor='#FFFFFF' nowrap></td>";
					all_ssid += "<td align='center' bgcolor='#FFFFFF' nowrap>";
					all_ssid += "<input type='checkbox' name='" + name + "_8' id='" + name + "_8_1'/><label for='" + name + "_8_1'>ɾ��</label>";
					all_ssid += "</td>";
					all_ssid += "<td align='center' bgcolor='#FFFFFF' nowrap>";
					all_ssid += "<input type='button' name='" + name + "_9' id='" + name + "_9_1' onclick='createSSID(" + name + ")' value='�½�'/></td>";
					all_ssid += "</tr>";
				} else {
					int rows = ssids.size();//ssid������
					Set<String> set = ssids.keySet();
					Iterator<String> it2 = set.iterator();
					int count = 1;

					while (it2.hasNext()) {
						String name_ = it2.next();
						String names = name + "_" + name_;

						ssid_idx_list += "," + name_;
						/*
						if (null == name) {
							ssid_idx_list += "_" + name_;
						} else {
							ssid_idx_list += "," + name_;
						}*/

						Map<String,String> values = ssids.get(name_);
						all_ssid += "<tr>";
						if (count == 1) {
							all_ssid += "<td align='center' rowspan='" + rows + "' bgcolor='#FFFFFF' nowrap>";
							all_ssid += name + "</td>";
						}
						if (null == values) {
							all_ssid += "<td align='center' bgcolor='#FFFFFF' nowrap>";
							all_ssid += name_ + "</td>";
							all_ssid += "<td align='center' bgcolor='#FFFFFF' nowrap></td>";
							all_ssid += "<td align='center' bgcolor='#FFFFFF' nowrap></td>";
							all_ssid += "<td align='center' bgcolor='#FFFFFF' nowrap></td>";
							all_ssid += "<td align='center' bgcolor='#FFFFFF' nowrap></td>";
							all_ssid += "<td align='center' bgcolor='#FFFFFF' nowrap></td>";
							all_ssid += "<td align='center' bgcolor='#FFFFFF' nowrap></td>";
							all_ssid += "<td align='center' bgcolor='#FFFFFF' nowrap>";
							all_ssid += "<input type='checkbox' name='" + names + "_7' id='" + names + "_7_1'/><label for='" + names + "_7_1'>ɾ��</label></td>";
							if (count == 1) {
								all_ssid += "<td align='center' rowspan='" + rows + "' bgcolor='#FFFFFF' nowrap>";
								all_ssid += "<input type='checkbox' name='" + name + "_8' id='" + name + "_8_1'/><label for='" + name + "_8_1'>ɾ��</label>";
								all_ssid += "</td>";
								all_ssid += "<td align='center' rowspan='" + rows + "' bgcolor='#FFFFFF' nowrap>";
								all_ssid += "<input type='button' name='" + name + "_9' id='" + name + "_9_1' onclick='createSSID(" + name + ")' value='�½�'/></td>";
							}
							all_ssid += "</tr>";
							count++;
							//name = null;
							continue;
						}
						String SSID = values.get("SSID");
						// SSID�Ƿ�����, ȡֵ��Χ(true,false)��ȱʡֵ��false
						String SSIDHide = values.get("X_CT-COM_SSIDHide");
						// ���û�رն�Ӧ SSID
						String Enable = values.get("Enable");
						// WLAN ģ�������ŵ�
						String Channel = values.get("Channel");
						// 802��11����ģʽ����a�� ��b�� ��g�� ��b,g�� (802.11b/g ���ģʽ)
						String Standard = values.get("Standard");
						// ����ģ�鹦�ʵļ��𣬱������ڶ�ʵ����Ӧ�ñ���һ�£�ȡֵ��Χ��{1,2,3,4,5},1Ϊ�����
						String Powerlevel = values.get("X_CT-COM_Powerlevel");

						String st_open = "true".equals(Enable.toLowerCase()) || "1".equals(Enable.toLowerCase()) ? "checked" : "";
						String st_close = "false".equals(Enable.toLowerCase()) || "0".equals(Enable.toLowerCase())  ? "checked" : "";

						String st_cast = "false".equals(SSIDHide.toLowerCase()) || "0".equals(SSIDHide.toLowerCase()) ? "checked" : "";
						String st_hide = "true".equals(SSIDHide.toLowerCase()) || "1".equals(SSIDHide.toLowerCase()) ? "checked" : "";

						all_ssid += "<td align='center' bgcolor='#FFFFFF' nowrap>";
						all_ssid += name_ + "</td>";

						all_ssid += "<td align='center' bgcolor='#FFFFFF' nowrap>";
						all_ssid += "<input type='text' name='" + names + "_1' id='" + names + "_1_1' value='" + SSID + "'/></td>";

						all_ssid += "<td align='center' bgcolor='#FFFFFF' nowrap>";
						all_ssid += "<input type='radio' name='" + names + "_2' id='" + names + "_2_1' " + st_open + "/><label for='" + names + "_2_1'>����</label><input type='radio' name='" + names + "_2' id='" + names + "_2_2' " + st_close + "/><label for='" + names + "_2_2'>�ر�</label></td>";

						all_ssid += "<td align='center' bgcolor='#FFFFFF' nowrap>";
						all_ssid += "<input type='radio' name='" + names + "_3' id='" + names + "_3_1' " + st_cast + "/><label for='" + names + "_3_1'>�㲥</label><input type='radio' name='" + names + "_3' id='" + names + "_3_2' " + st_hide + "/><label for='" + names + "_3_2'>����</label></td>";

						all_ssid += "<td align='center' bgcolor='#FFFFFF' nowrap>";
						all_ssid += "<input type='text' name='" + names + "_4' id='" + names + "_4_1' value='" + Channel + "'size='2' maxlength='2' /></td>";

						all_ssid += "<td align='center' bgcolor='#FFFFFF' nowrap>";
						all_ssid += "<select name='" + names + "_5' disabled='true' id='" + names + "_5_1'>";
						all_ssid += "<option value='a' " + ("a".equals(Standard) ? "selected" : "") + ">802.11a";
						all_ssid += "<option value='b' " + ("b".equals(Standard) ? "selected" : "") + ">802.11b";
						all_ssid += "<option value='g' " + ("g".equals(Standard) ? "selected" : "") + ">802.11g";
						all_ssid += "<option value='b,g' " + ("b,g".equals(Standard) ? "selected" : "") + ">802.11b/g";
						all_ssid +="</select></td>";

						all_ssid += "<td align='center' bgcolor='#FFFFFF' nowrap>";
						all_ssid += "<select name='" + names + "_6' disabled='true' id='" + names + "_6_1'>";
						all_ssid += "<option value='1' " + ("1".equals(Powerlevel) ? "selected" : "") + ">1��(���)";
						all_ssid += "<option value='2' " + ("2".equals(Powerlevel) ? "selected" : "") + ">2��";
						all_ssid += "<option value='3' " + ("3".equals(Powerlevel) ? "selected" : "") + ">3��";
						all_ssid += "<option value='4' " + ("4".equals(Powerlevel) ? "selected" : "") + ">4��";
						all_ssid += "<option value='5' " + ("5".equals(Powerlevel) ? "selected" : "") + ">5��(��С)";
						all_ssid +="</select></td>";

						all_ssid += "<td align='center' bgcolor='#FFFFFF' nowrap>";
						all_ssid += "<input type='checkbox' name='" + names + "_7' id='" + names + "_7_1'/><label for='" + names + "_7_1'>ɾ��</label></td>";

						if (count == 1) {
							all_ssid += "<td align='center' rowspan='" + rows + "' bgcolor='#FFFFFF' nowrap>";
							all_ssid += "<input type='checkbox' name='" + name + "_8' id='" + name + "_8_1'/><label for='" + name + "_8_1'>ɾ��</label>";
							all_ssid +="</td>";
							all_ssid += "<td align='center' rowspan='" + rows + "' bgcolor='#FFFFFF' nowrap>";
							all_ssid += "<input type='button' name='" + name + "_9' id='" + name + "_9_1' onclick='createSSID(" + name + ")' value='�½�'/></td>";
						}
						all_ssid += "</tr>";
						count++;
					}
				}
				//name = null;
			}			
			all_ssid += "</table>";
		}
	} else if ("2".equals(action_type)){
		log_msg = request.getParameter("log_msg");
		msg_ = "���÷�ʽ��" + type_ + "��" + log_msg;
		msg_ = Encoder.toISO(msg_);
		LogItem.getInstance().writeItemLog_other(request, 1, device_id_, msg_, Encoder.toISO(result_), "WLAN����");
		map = configMgr.setWlanStatusTr069(request);
	} else if ("3".equals(action_type)){
		log_msg = request.getParameter("log_msg");
		msg_ = "���÷�ʽ��" + type_ + "��" + log_msg;
		msg_ = Encoder.toISO(msg_);
		LogItem.getInstance().writeItemLog_other(request, 1, device_id_, msg_, Encoder.toISO(result_), "WLAN����");
		map = configMgr.addWlanSSIDTr069(request);
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
	}else{
		if (map.size() == 0) {
			strlist += "<tr class='blue_foot'>";
			strlist += "<td bgcolor='#FFFFFF' width='30%' nowrap>";
			strlist += "";
			strlist += "</td><td bgcolor='#FFFFFF' width='70%'>";
			strlist += "<font color=red>�豸���Ӳ��ϻ�֧�ָò�����</font>";
			strlist += "</td></tr>";
		} else {
		Set set = map.keySet();
		Iterator iterator = set.iterator();
		String name = null;
		String value = null;
		while(iterator.hasNext()){
			name = (String)iterator.next();
			value = (String)map.get(name);
			strlist += "<tr class='blue_foot'>";
			strlist += "<td bgcolor='#FFFFFF' width='30%' nowrap>";
			strlist += name;
			strlist += "</td><td bgcolor='#FFFFFF' width='70%'>";
			strlist += value;
			strlist += "</td></tr>";
		}
		}
	}
	strlist += "</table>";

%>

<body>
<SPAN ID="child"><%=strlist%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
var action_type = "<%=action_type%>";
if (action_type == "1") {
	var all_ssid = "<%=all_ssid%>";
	var result = "<%=result%>";
	var ssid_idx_list = "<%=ssid_idx_list%>";

	parent.document.all("div_ssid").innerHTML = all_ssid;
	parent.setStatus(ssid_idx_list, result);
} else if (action_type == "2") {
	parent.document.all("div_ping").innerHTML = document.getElementById("child").innerHTML;
//	parent.resetTo();
} else if (action_type == "3") {
	parent.document.all("div_ping").innerHTML = document.getElementById("child").innerHTML;
	parent.resetTo();
}
</SCRIPT>
</body>
</html>
