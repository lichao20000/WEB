<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>

<%
String device_id = request.getParameter("device_id");
String cpe_operationinfo = DeviceAct.getHistoryOperation(device_id);
out.print(cpe_operationinfo);
%>

