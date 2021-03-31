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

String msg = "设置VLAN相关信息：";


if ("2".equals(type)){
	html += "该设备不支持以SNMP协议设置vlan信息<br>";
}
else{
	int[] ret = new ConfigDevice().setDevInfo_tr069_multi_(device_id, curUser.getUser(), VLANName, value, para_index, typeArr);

	if (ret != null && ret.length == VLANName.length){
		if (!"".equals(value[0])){
			if (ret[0] == 1){
				html += "配置VLAN ID成功...<br>";
				msg += "设置VLAN ID成功，值为" + value[0] + "；";
			}
			else{
				html += "配置VLAN ID失败...<br>";
				msg += "设置VLAN ID失败，值为" + value[0] + "；";
			}
		}
		if (!"".equals(value[1])){
			if (ret[1] == 1){
				html += "配置VLAN绑定端口列表成功...<br>";
				msg += "设置VLAN绑定端口列表成功，值为" + value[0] + "；";
			}
			else{
				html += "配置VLAN绑定端口列表失败...<br>";
				msg += "设置VLAN绑定端口列表失败，值为" + value[0] + "；";
			}
		}
		if (!"".equals(value[2])){
			if (ret[2] == 1){
				html += "配置VLAN名称成功...<br>";
				msg += "设置VLAN名称成功，值为" + value[1] + "；";
			}
			else{
				html += "配置VLAN名称失败...<br>";
				msg += "设置VLAN名称失败，值为" + value[1] + "；";
			}
		}
		if (!"".equals(value[3])){
			if (ret[3] == 1){
				html += "配置VLAN IP地址成功...<br>";
				msg += "设置VLAN IP地址成功，值为" + value[2] + "；";
			}
			else{
				html += "配置VLAN IP地址失败...<br>";
				msg += "设置VLAN IP地址失败，值为" + value[2] + "；";
			}
		}
		if (!"".equals(value[4])){
			if (ret[4] == 1){
				html += "配置VLAN子网掩码成功...<br>";
				msg += "设置VLAN子网掩码成功，值为" + value[3] + "；";
			}
			else{
				html += "配置VLAN子网掩码失败...<br>";
				msg += "设置VLAN子网掩码失败，值为" + value[3] + "；";
			}
		}
	}
	else{
		html += "配置VLAN信息失败...<br>";
	}
}

msg = Encoder.toISO(msg);
//记日志
LogItem.getInstance().writeItemLog_other(request, 1, device_id, msg, Encoder.toISO("成功"), "VLAN配置");

%>
<html>
<head>
<script type="text/javascript">
parent.document.all("div_ping").innerHTML = "<%=html%>";
</script>
</head>
<body></body>
</html>
