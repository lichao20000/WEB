<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ page import="com.linkage.litms.paramConfig.ConfigDevice" %>
<%@ page import="com.linkage.litms.common.database.DataSetBean"%>
<%
String strDevice = request.getParameter("device_id");
String[] device_list = strDevice.split(",");
String device_id = "";
String type= request.getParameter("type");
String html = "";

String[] DHCPName = new String[5];


String[] value = new String[5];
value[0] = request.getParameter("url");
value[1] = request.getParameter("username");
value[2] = request.getParameter("password");
value[3] = request.getParameter("informenable");
value[4] = request.getParameter("informinterval");
String[] typeArr = new String[5];
typeArr[0] = "1";//string
typeArr[1] = "1";//string
typeArr[2] = "1";//string
typeArr[3] = "4";//unsignedInt
typeArr[4] = "3";//boolean
String msg = "设置DNS相关信息：";

if ("2".equals(type)){
	html += "该设备不支持以SNMP协议配置DNS服务<br>";
}
else{
	String sql="";
	int[] ret;
	for (int i=0;i<device_list.length;i++){
		
		device_id = device_list[i];
		Cursor cursor = DataSetBean.getCursor("select device_name from tab_gw_device where device_id='" + device_id + "'");
		Map fields = cursor.getNext();
		if (fields != null){
			
			DHCPName[0] = ConfigDevice.getParaArr("400", device_id);
			DHCPName[1] = ConfigDevice.getParaArr("401", device_id);
			DHCPName[2] = ConfigDevice.getParaArr("402", device_id);
			DHCPName[3] = ConfigDevice.getParaArr("403", device_id);
			DHCPName[4] = ConfigDevice.getParaArr("404", device_id);
			ret = ConfigDevice.setDevInfo_tr069(device_id, curUser.getUser(), DHCPName, value, typeArr);
			if (ret != null && ret.length > 0){
				//InternetGatewayDevice.ManagementServer.URL(URL)
				if (ret[0] == 1){
					html += "配置" + fields.get("device_name") + "的URL服务成功...<br>";
					msg += "设置" + fields.get("device_name") + "的URL成功，值为" + value[0] + "。<br>";
				}
				else{
					html += "配置" + fields.get("device_name") + "的URL服务失败...<br>";
					msg += "设置" + fields.get("device_name") + "的URL地址失败，值为" + value[0] + "。<br>";
				}
				//ManagementServer.Username(Username)
				if (ret[1] == 1){
					html += "配置" + fields.get("device_name") + "的用户名成功...<br>";
					msg += "设置" + fields.get("device_name") + "的用户名成功，值为" + value[1] + "。<br>";
					sql+="; update tab_gw_device set cpe_username='"+value[1]+"' where device_id='"+device_id+"'";
				}
				else{
					html += "配置" + fields.get("device_name") + "的用户名服务失败...<br>";
					msg += "设置" + fields.get("device_name") + "的用户名失败，值为" + value[1] + "。<br>";
				}
				//InternetGatewayDevice.ManagementServer.Password(Password)
				if (ret[2] == 1){
					html += "配置" + fields.get("device_name") + "的密码成功...<br>";
					msg += "设置" + fields.get("device_name") + "的密码成功，值为" + value[2] + "。<br>";
					sql+="; update tab_gw_device set cpe_passwd='"+value[2]+"' where device_id='"+device_id+"'";
				}
				else{
					html += "配置" + fields.get("device_name") + "的密码失败...<br>";
					msg += "设置" + fields.get("device_name") + "的密码失败，值为" + value[2] + "。<br>";
				}
				//InternetGatewayDevice.ManagementServer.PeriodicInformEnable(上报周期开关)
				if (ret[3] == 1){
					html += "配置" + fields.get("device_name") + "的上报周期开关成功...<br>";
					msg += "设置" + fields.get("device_name") + "的上报周期开关成功，值为" + value[3] + "。<br>";
				}
				else{
					html += "配置" + fields.get("device_name") + "的上报周期开关失败...<br>";
					msg += "设置" + fields.get("device_name") + "的上报周期开关失败，值为" + value[3] + "。<br>";
				}
				//InternetGatewayDevice.ManagementServer.PeriodicInformInterval(上报周期)
				if (ret[4] == 1){
					html += "配置" + fields.get("device_name") + "的上报周期成功...<br>";
					msg += "设置" + fields.get("device_name") + "的上报周期成功，值为" + value[4] + "。<br>";
				}
				else{
					html += "配置" + fields.get("device_name") + "的上报周期失败...<br>";
					msg += "设置" + fields.get("device_name") + "的上报周期失败，值为" + value[4] + "。<br>";
				}

			}
			else{
				html += "配置" + fields.get("device_name") + "的参数失败...<br>";
				msg += "设置" + fields.get("device_name") + "的参数失败。<br>";
			}
		}
	}
}

msg = Encoder.toISO(msg);
//记日志
LogItem.getInstance().writeItemLog_other(request, 1, device_id, msg, Encoder.toISO("成功"), "上报周期配置");


%>
<html>
<head>
<script type="text/javascript">
parent.document.all("div_ping").innerHTML = "<%=html%>";
</script>
</head>
<body></body>
</html>
