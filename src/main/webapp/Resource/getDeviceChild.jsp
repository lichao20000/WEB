<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />
<%
request.setCharacterEncoding("GBK");
String type= request.getParameter("type");
String strChildList = "";
if("1".equals(type))
{
	strChildList = DeviceAct.getDeviceModelByVendorID1(request,"","");
}
else if("2".equals(type))
{
	strChildList = DeviceAct.getDeviceSoftVersionByOUIAndDeviceModel(request,"","");
}
%>
<%=strChildList%>