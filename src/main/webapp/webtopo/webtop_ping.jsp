
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="java.util.*,com.linkage.litms.common.database.*,com.linkage.litms.webtopo.*"%>

<% 
  	request.setCharacterEncoding("GBK");
	String device_id= request.getParameter("device_id");
	String strAction= request.getParameter("action");	
	//通过DeviceResourceInfo从数据库获得设备信息
	DeviceResourceInfo deviceInfo = new DeviceResourceInfo();
	Cursor cursor = deviceInfo.getDeviceResource(device_id);
	Map myMap = cursor.getNext();
	String loopbackip= (String)(myMap == null ? "" : myMap.get("loopback_ip"));
	String strCmd = "";
	if(loopbackip.equals("") || loopbackip.equals("null")){
		loopbackip = "_NULL_";
	}
	if(strAction.equals("ping")){
		strCmd = "ping " + loopbackip + " -t";
	}else if(strAction.equals("telnet")){
		strCmd = "telnet " + loopbackip;
	}else if(strAction.equals("tracert")){
		strCmd = "tracert -h 30 " + loopbackip;
	}
%>
<script language=javascript>
  <!--
function execPing(cmd) {
	try {
		if(cmd.indexOf("_NULL_") != -1){
			alert("无法获取此设备的IP!");
			return ;
		}
		var objShell = new ActiveXObject("wscript.shell");
		objShell.Run(cmd);
		objShell = null;
	}catch (e){
		alert(e.message + " " + e.description);
		//alert('请将本站点加入安全站点，查看详细操作？');
	    var  bln   =   window.confirm("请将本站点加入安全站点，查看详细操作?");  
	    if(bln == true) {
	    	window.open ('information.jsp','newwindow','height=350,width=350,top=234,left=362,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no') 	
	    }else{
	    	return false;
	    }
	}
}
//-->
</script>
<html>
	<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onload="javascript:execPing('<%=strCmd%>');">
	</body>
</html>



