<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ page import="com.linkage.litms.paramConfig.ConfigDevice" %>

<%
String ResultString = "";
String strDevice = request.getParameter("device_id");
String[] device_list = strDevice.split(",");
String device_id = "";
String type= request.getParameter("type");
String html = "";

if ("2".equals(type)){
	html += "该设备不支持以SNMP协议配置防火墙信息";
}
else{
	String[] FireWallName = new String[10];
	//Map mapFireWall = new HashMap();
	
	for (int i=0;i<device_list.length;i++){
		
		device_id = device_list[i];
		Cursor cursor = DataSetBean.getCursor("select device_name from tab_gw_device where device_id='" + device_id + "'");
		Map fields = cursor.getNext();
		if (fields != null){
			FireWallName[0] = ConfigDevice.getParaArr("431", device_id);
			FireWallName[1] = ConfigDevice.getParaArr("432", device_id);
			FireWallName[2] = ConfigDevice.getParaArr("433", device_id);
			FireWallName[3] = ConfigDevice.getParaArr("434", device_id);
			FireWallName[4] = ConfigDevice.getParaArr("435", device_id);
			//FireWallName[5] = ConfigDevice.getParaArr("436", device_id);
			FireWallName[5] = ConfigDevice.getParaArr("437", device_id);
			FireWallName[6] = ConfigDevice.getParaArr("438", device_id);
			FireWallName[7] = ConfigDevice.getParaArr("439", device_id);
			//FireWallName[9] = ConfigDevice.getParaArr("440", device_id);
			//FireWallName[8] = ConfigDevice.getParaArr("441", device_id);
			FireWallName[8] = ConfigDevice.getParaArr("442", device_id);
			FireWallName[9] = ConfigDevice.getParaArr("443", device_id);
			
			//for(int i=0;i<FireWallName.length;i++){
			//	mapFireWall.put("" + i,FireWallName[i]);
			//}

			Map paraMap = ConfigDevice.getDevInfo_tr069(device_id, curUser.getUser(), FireWallName);
			if (paraMap == null){
				html += "获取参数值失败，请检查ACS配置是否正确<br>";
			}
			else{
				for(int j=0;j<FireWallName.length;j++){
					ResultString = (String)paraMap.get("" + j);
				
					if (ResultString != null){
						html += fields.get("device_name") + "的"+ FireWallName[j] +"为：" + ResultString + "<br>";
					}
					else{
						html += "获取" + fields.get("device_name") + "的" +  FireWallName[j] + "时失败...<br>";
						ResultString = "";
					}
				}
			}
		}
	}
	
	
}

%>
<html>
<head>
<script type="text/javascript">
parent.document.all("div_ping").innerHTML = "<%=html%>";
</script>
</head>
<body></body>
</html>
