<%--
FileName	: IpLineSave.jsp
Author		: liuli
Date		: 2007-3-06
Note		: 帐号增加、修改、删除操作
--%>
<html>
<%@ page import="java.util.Map"%>
<jsp:useBean id="configMgr" scope="request" class="com.linkage.litms.paramConfig.ConfigureManager"/>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");
	// 批量的设备ID
	String strDevice = request.getParameter("device_id");
	String[] device_list = strDevice.split(",");
	String device_id = "";
	String device_name = "";
	// 当前服务状态,是否启用 启用:true 禁用:false
	boolean flag = false;
	// 配置方式
	String type= request.getParameter("type");
	// 配置方式的描述
	String typeDesc = "1".equals(type) ? "tr069" : "snmp";
	
	String html = "";
	String tempStr = "";
	Map map = null;
	
			+ " & 配置方式:" + type 
			+ " & 配置方式描述:" + typeDesc);
	
	int deviceNum = device_list.length;
	for (int i=0;i < deviceNum;i++){
		device_id = device_list[i];
		Cursor cursor = DataSetBean.getCursor("select device_name from tab_gw_device where device_id='" + device_id + "'");
		Map fields = cursor.getNext();
		if (fields != null){
			device_name = (String)fields.get("device_name");
			map = configMgr.getNTPStatus(device_id,type);
			flag = false;
			String main_server = "";
			String second_server = "";
			if (null != map && 0 < map.size()) {
				flag = true;
				if (1 == map.size()) {
					main_server = (String)map.get("server_1");
				}
				if (2 <= map.size()) {
					main_server = (String)map.get("server_1");
					second_server = (String)map.get("server_2");
				}
			}
			tempStr = device_name + " 当前服务状态：" +(flag?"启用":"禁用")+"<br>"
					+ "&nbsp;&nbsp;  主 NTP 服务器地址或域名：" + main_server + "<br>"
					+ "&nbsp;&nbsp;  备 NTP 服务器地址或域名：" + second_server + "<br>";
			html += tempStr;	
		}
	}
	

%>

<body>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("div_ping").innerHTML = "<%=html%>";
	parent.setStatus();
</SCRIPT>
</body>
</html>
