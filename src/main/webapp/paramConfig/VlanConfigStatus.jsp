<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ page import="com.linkage.litms.paramConfig.ConfigDevice,java.util.Set,java.util.Map,java.util.Iterator" %>

<%
String VLANID = "";
String VLanInterface = "";
String VlanName = "";
String IPInterfaceIPAddress = "";
String IPInterfaceSubnetMask = "";
String device_id = request.getParameter("device_id");
String type= request.getParameter("type");
String html = "";
String paraHtml = "";
int vlanCount = 0;

if ("2".equals(type)){
	html += "该设备不支持以SNMP协议配置vlan信息";
}
else{
	String[] VLANOID = new String[5];
	VLANOID[0] = "371";
	VLANOID[1] = "372";
	VLANOID[2] = "373";
	VLANOID[3] = "374";
	VLANOID[4] = "375";
	
	String vlanPara = "";
	for (int i=0;i<VLANOID.length;i++){
		vlanPara += ConfigDevice.getParaArr(VLANOID[i], device_id) + "#";
	}
	String[] VLANName = vlanPara.split("#");
	String para_index = ConfigDevice.getParaArr("376", device_id);
	
	Map<String, Map<String, String>> paraMap = new ConfigDevice().getDevInfo_tr069_multi_(device_id, curUser.getUser(), VLANName, para_index);
	if (paraMap == null){
		html += "获取参数值失败，请检查ACS配置是否正确<br>";
	}
	else{
		html += "获取参数值成功<br>";
		Set set = paraMap.keySet();
		Iterator<String> it = set.iterator();
		while(it.hasNext()){
			String idx = it.next();
			Map<String, String> map = paraMap.get(idx);
			String vlanId = map.get("0");
			String vlanInterface = map.get("1");
			String vlanName = map.get("2");
			String ip = map.get("3");
			String mask = map.get("4");

			paraHtml += "<tr bgcolor='#FFFFFF'><td><input type='text' name='vlan_idx' value='" 
					+ (null == idx ? "" : idx) + "'></td><td><input type='text' name='vlan_id' value='" 
					+ (null == vlanId ? "" : vlanId) + "'></td><td><input type='text' name='VLan_Interface' value='" 
					+ (null == vlanInterface ? "" : vlanInterface) + "'></td><td><input type='text' name='vlan_name' value='" 
					+ (null == vlanName ? "" : vlanName) + "'></td><td><input type='text' name='vlan_ip' value='" 
					+ (null == ip ? "" : ip) + "'></td><td><input type='text' name='vlan_mask' value='" 
					+ (null == mask ? "" : mask) + "'></td></tr>";

			vlanCount ++;
		}
	}
	
}

%>
<html>
<head>
<script type="text/javascript"
	src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript">

parent.document.all("bt_set").style.display = "";
parent.document.all("div_ping").innerHTML = "<%=html%>";

parent.document.all("divVlanCount").style.display = "";
parent.document.frm.vlanCount.value = "<%=vlanCount%>";

var paraHtml = "<%=paraHtml%>";
parent.setParaHtml(paraHtml);
</script>
</head>
<body></body>
</html>
