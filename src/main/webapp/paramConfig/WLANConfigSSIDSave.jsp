<%--
FileName	: IpLineSave.jsp
Author		: liuli
Date		: 2007-3-06
Note		: 帐号增加、修改、删除操作
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
	String all_ssid = "";
	String ssid_idx_list = "";
	String log_msg = "";
	String device_id_ = request.getParameter("device_id");
	String type_ = request.getParameter("type");
	type_ = "1".equals(type_) ? "tr069" : "snmp";
	String msg_ = "";
	String result_ = "成功";

	if ("1".equals(action_type)) {
		Map tm = configMgr.getWlanSSID(request);

		if (null != tm && 0 < tm.size()) {
			Set st = tm.keySet();
			Iterator it = st.iterator();
			String name = null;
			List<String> vl = null;
				all_ssid += "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
				all_ssid += "<tr>";
				all_ssid += "<td align='center' bgcolor='#FFFFFF' width='40' nowrap>索引";
				all_ssid += "</td><td  align='center' bgcolor='#FFFFFF' width='200' nowrap>名称";
				all_ssid += "</td><td  align='center' bgcolor='#FFFFFF' width='150' nowrap>开启/关闭";
				all_ssid += "</td><td  align='center' bgcolor='#FFFFFF' width='150' nowrap>广播/隐藏";
				all_ssid += "</td><td  align='left' style='padding-left:15' bgcolor='#FFFFFF' nowrap>是否删除";
				all_ssid += "</td></tr>";
			while(it.hasNext()){
				name = (String)it.next();
				if ("".equals(ssid_idx_list)) {
					ssid_idx_list += name;
				} else {
					ssid_idx_list += "_" + name;
				}
				vl = (List)tm.get(name);
				String ssid_name = vl.get(1);
				String ssid_status = vl.get(2);
				String ssid_hidden = vl.get(3);
				String st_open = "1".equals(ssid_status) ? "checked" : "";
				String st_close = "2".equals(ssid_status) ? "checked" : "";
				String st_cast = "2".equals(ssid_hidden) ? "checked" : "";
				String st_hide = "1".equals(ssid_hidden) ? "checked" : "";

				all_ssid += "<tr>";
				all_ssid += "<td align='center' bgcolor='#FFFFFF' nowrap>";
				all_ssid += name;
				all_ssid += "</td><td align='center' bgcolor='#FFFFFF' nowrap>";
				all_ssid += "<input type='text' name='" + name + "_4' id='" + name + "_4_1' value='" + ssid_name + "'/>";
				all_ssid += "</td><td align='center' bgcolor='#FFFFFF' nowrap>";
				all_ssid += "<input type='radio' name='" + name + "_1' id='" + name + "_1_1' " + st_open + "/><label for='" + name + "_1_1'>开启</label><input type='radio' name='" + name + "_1' id='" + name + "_1_2' " + st_close + "/><label for='" + name + "_1_2'>关闭</label>";
				all_ssid += "</td><td align='center' bgcolor='#FFFFFF' nowrap>";
				all_ssid += "<input type='radio' name='" + name + "_2' id='" + name + "_2_1' " + st_cast + "/><label for='" + name + "_2_1'>广播</label><input type='radio' name='" + name + "_2' id='" + name + "_2_2' " + st_hide + "/><label for='" + name + "_2_2'>隐藏</label>";
				all_ssid += "</td><td align='left' style='padding-left:15' bgcolor='#FFFFFF' nowrap>";
				all_ssid += "<input type='checkbox' name='" + name + "_3' id='" + name + "_3_1'/><label for='" + name + "_3_1'>删除</label>";
				all_ssid += "</td></tr>";
			}
			all_ssid += "</table>";
		}
	} else if ("2".equals(action_type)){
		log_msg = request.getParameter("log_msg");
		msg_ = "配置方式：" + type_ + "；" + log_msg;
		msg_ = Encoder.toISO(msg_);
		LogItem.getInstance().writeItemLog_other(request, 1, device_id_, msg_, Encoder.toISO(result_), "WLAN配置");
		map = configMgr.setWlanSSID(request);
	} else if ("3".equals(action_type)){
		log_msg = request.getParameter("log_msg");
		msg_ = "配置方式：" + type_ + "；" + log_msg;
		msg_ = Encoder.toISO(msg_);
		LogItem.getInstance().writeItemLog_other(request, 1, device_id_, msg_, Encoder.toISO(result_), "WLAN配置");
		map = configMgr.addWlanSSID(request);
	}

	String strlist = "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
	strlist += "<tr>";
	strlist += "<td bgcolor='#FFFFFF' width='30%' nowrap>";
	strlist += "设备名称";
	strlist += "</td><td bgcolor='#FFFFFF' width='70%' nowrap>";
	strlist += "返回结果";
	strlist += "</td></tr>";
  	
	if(map==null) {
		strlist += "<tr class='blue_foot'>";
		strlist += "<td bgcolor='#FFFFFF' width='30%' nowrap>";
		strlist += "</td>";
		strlist += "<td bgcolor='#FFFFFF' width='70%' nowrap>";
		strlist += "获取值失败";
		strlist += "</td></tr>";
	}else{
		if (map.size() == 0) {
			strlist += "<tr class='blue_foot'>";
			strlist += "<td bgcolor='#FFFFFF' width='30%' nowrap>";
			strlist += "";
			strlist += "</td><td bgcolor='#FFFFFF' width='70%'>";
			strlist += "<font color=red>设备连接不上或不支持该操作！</font>";
			strlist += "</td></tr>";
		} else {
		Set set = map.keySet();
		Iterator iterator = set.iterator();
		String name = null;
		String value = null;
		while(iterator.hasNext()){
			name = (String)iterator.next();
			value = (String)map.get(name);
			name = "索引为 " + name + " 的SSID： ";
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
	var ssid_idx_list = "<%=ssid_idx_list%>";

	parent.document.all("div_ssid").innerHTML = all_ssid;
	parent.setStatus(ssid_idx_list);
} else if (action_type == "2") {
	parent.document.all("div_ping").innerHTML = document.getElementById("child").innerHTML;
	parent.resetTo();
} else if (action_type == "3") {
	parent.document.all("div_ping").innerHTML = document.getElementById("child").innerHTML;
	parent.resetTo();
}
</SCRIPT>
</body>
</html>
