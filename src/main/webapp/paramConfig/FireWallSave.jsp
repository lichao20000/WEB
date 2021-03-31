<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ page import="com.linkage.litms.paramConfig.ConfigDevice" %>

<%
String strDevice = request.getParameter("device_id");
String[] device_list = strDevice.split(",");
String device_id = "";
String type= request.getParameter("type");
String html = "";

String[] FireWallName = new String[9];


String[] value = new String[9];
value[0] = request.getParameter("AppControlEnabled");
value[1] = request.getParameter("AppControlName");
value[2] = request.getParameter("SourceInterface");
value[3] = request.getParameter("TimeRange");
value[4] = request.getParameter("WeekDays");
//value[5] = request.getParameter("AppController_Apply");

value[5] = request.getParameter("Enable");
value[6] = request.getParameter("DdosEnabled");
value[7] = request.getParameter("PortscanEnabled");
//value[9] = request.getParameter("Apply");
//value[8] = request.getParameter("Capability");
value[8] = request.getParameter("AppControlSupportList");
//value[10] = request.getParameter("AppControlNumberOfEntries");


String[] typeArr = new String[9];
typeArr[0] = "4";
typeArr[1] = "1";
typeArr[2] = "1";
typeArr[3] = "1";
typeArr[4] = "1";
//typeArr[5] = "1";

typeArr[5] = "4";
typeArr[6] = "4";
typeArr[7] = "4";
//typeArr[9] = "1";
//typeArr[8] = "1";
typeArr[8] = "1";

String msg = "设置防火墙相关信息：";

if ("2".equals(type)){
	html += "该设备不支持以SNMP协议配置FireWall<br>";
}
else{
	for (int i=0;i<device_list.length;i++){
		
		device_id = device_list[i];
		Cursor cursor = DataSetBean.getCursor("select device_name from tab_gw_device where device_id='" + device_id + "'");
		Map fields = cursor.getNext();
		String Apply = null;
		String AppController_Apply = null;
		if (fields != null){
			FireWallName[0] = ConfigDevice.getParaArr("431", device_id);
			FireWallName[1] = ConfigDevice.getParaArr("432", device_id);
			FireWallName[2] = ConfigDevice.getParaArr("433", device_id);
			FireWallName[3] = ConfigDevice.getParaArr("434", device_id);
			FireWallName[4] = ConfigDevice.getParaArr("435", device_id);
			//FireWallName[5] = ConfigDevice.getParaArr("436", device_id);
			Apply = ConfigDevice.getParaArr("436", device_id);
			FireWallName[5] = ConfigDevice.getParaArr("437", device_id);
			FireWallName[6] = ConfigDevice.getParaArr("438", device_id);
			FireWallName[7] = ConfigDevice.getParaArr("439", device_id);
			//FireWallName[9] = ConfigDevice.getParaArr("440", device_id);
			AppController_Apply = ConfigDevice.getParaArr("440", device_id);
			//FireWallName[8] = ConfigDevice.getParaArr("441", device_id);
			FireWallName[8] = ConfigDevice.getParaArr("442", device_id);
			//FireWallName[10] = ConfigDevice.getParaArr("443", device_id);
			int[] ret = ConfigDevice.setDevInfo_tr069(device_id, curUser.getUser(), FireWallName, value, typeArr);

			if (ret != null && ret.length > 0){
				for(int j=0;j<ret.length;j++){
					if(ret[j] == 1){
						html += "设置" + fields.get("device_name") + "的" + FireWallName[j] + "地址成功，值为" + value[j] + "<br>";
						msg += "设置" + fields.get("device_name") + "的" + FireWallName[j] + "地址成功，值为" + value[j] + "。";
					}else{
						html += "设置" + fields.get("device_name") + "的" + FireWallName[j] + "地址成功，值为" + value[j] + "<br>";
						msg += "设置" + fields.get("device_name") + "的" + FireWallName[j] + "地址成功，值为" + value[j] + "。";
					}
				}

				//ConfigDevice.setDevInfo_tr069(device_id, curUser.getUser(), new String[]{AppController_Apply}, new String[]{"1"}, new String[]{"1"});
				//ConfigDevice.setDevInfo_tr069(device_id, curUser.getUser(), new String[]{Apply}, new String[]{"1"}, new String[]{"1"});
			}
			else{
				html += "配置" + fields.get("device_name") + "的FireWall失败...<br>";
				msg += "设置" + fields.get("device_name") + "的FireWall失败。";
			}
		}
	}
}

msg = Encoder.toISO(msg);
//记日志
//LogItem.getInstance().writeItemLog_other(request, 1, device_id, msg, Encoder.toISO("成功"), "DNS配置");


%>
<html>
<head>
<script type="text/javascript">
parent.document.all("div_ping").innerHTML = "<%=html%>";
</script>
</head>
<body></body>
</html>
