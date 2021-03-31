<%--
FileName	: IpLineSave.jsp
Author		: liuli
Date		: 2007-3-06
Note		: 帐号增加、修改、删除操作
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
	String msg_ = "配置方式：" + type_ + "；" + ("1".equals(status_) ? ("设置NTP状态：开启；设置主NTP服务器地址或域名：" + main_ntp_server_ + "；设置备NTP服务器地址或域名：" + second_ntp_server_) : "设置NTP状态：并闭");
	msg_ = Encoder.toISO(msg_);
	String result_ = "失败";

	Map map = configMgr.NTPConfigure(request);
	//Map map = null;
	//192.168.232.23
	//configMgr.testCreate("145","1.3.6.1.4.1.25506.8.22.2.1.1.1.36.172.32.42.6.3",2,"6");

	String strlist = "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
	strlist += "<tr>";
	strlist += "<td bgcolor='#FFFFFF' width='30%' nowrap>";
	strlist += "设备名称";
	strlist += "</td><td bgcolor='#FFFFFF' width='70%' nowrap>";
	strlist += "返回结果";
	strlist += "</td></tr>";
  	
	if(map==null || 0 == map.size()) {
		strlist += "<tr class='blue_foot'>";
		strlist += "<td bgcolor='#FFFFFF' width='30%' nowrap>";
		strlist += "</td>";
		strlist += "<td bgcolor='#FFFFFF' width='70%' nowrap>";
		strlist += "设置失败";
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
//				value = "设备重启成功！";
//			} else {
//				value = "设备重启失败！";
//			}
			if ("NTP配置成功".equals(value) || "启用成功".equals(value) || "禁用成功".equals(value)) {
				result_ = "成功";
			}
			strlist += "<tr class='blue_foot'>";
			strlist += "<td bgcolor='#FFFFFF' width='30%' nowrap>";
			strlist += name;
			strlist += "</td><td bgcolor='#FFFFFF' width='70%'>";
			strlist += value;
			strlist += "</td></tr>";
		}
	}
	
	LogItem.getInstance().writeItemLog_other(request, 1, device_id_, msg_, Encoder.toISO(result_), "NTP配置");

	strlist += "</table>";

%>

<body>
<SPAN ID="child"><%=strlist%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("div_ping").innerHTML = child.innerHTML;
</SCRIPT>
</body>
</html>
