<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />
<script language=javascript>
<%
String id = request.getParameter("id");
String refresh_id=request.getParameter("refresh_id");
if(id==null)
{
	out.println("alert('设备更新失败！');");
}
else
{	
	String[] ids = id.split(",");		
	String result=DeviceAct.setDeviceStatus_enter(user,ids);
	out.println(result);	
}
%>
parent.document.all("childFrm").src="getChildTopo.jsp?pid=<%=refresh_id%>";
window.close();
</script>