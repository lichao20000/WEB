<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.warn.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.linkage.litms.common.database.*,com.linkage.litms.common.util.*"%>
<jsp:useBean id="deviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct" />

<%
String devicetype_id = request.getParameter("devicetype_id");
String template_id = request.getParameter("template_id");

String strSQL2 = "select oui from tab_devicetype_info where devicetype_id="+devicetype_id;
Cursor cursor2 = DataSetBean.getCursor(strSQL2);
Map fileds = cursor2.getNext();
String pos = "";
if (fileds != null){
	pos = (String)fileds.get("oui");
}
	
String strVendorList = deviceAct.getVendorList(true, pos, "");

String strSQL = "select distinct  a.devicetype_id, a.device_model + '(' + a.softwareversion + ')' as device_model,b.devicetype_id  from tab_devicetype_info a,tab_template b where a.devicetype_id=b.devicetype_id";
if (pos != null && !"".equals(pos)){
	strSQL += " and a.oui = '" + pos + "'";
}
Cursor cursor = DataSetBean.getCursor(strSQL);
String strModelList = FormUtil.createListBox(cursor,"devicetype_id", "device_model", true, devicetype_id, "");

String strSQL1 = "select distinct template_id,template_name from tab_template where devicetype_id="
			+ devicetype_id + "  order by template_id ";
Cursor cursor1 = DataSetBean.getCursor(strSQL1);
String strModelList1 = FormUtil.createListBox(cursor1,"template_id", "template_name", false, template_id, "");
	
	
String cityList = request.getParameter("cityList");
String cityHtml = com.linkage.litms.netcutover.CommonForm.getCityCheckBox(cityList);

%>
<html>
<body>
<script language="javascript">
parent.document.all("div_vendor").innerHTML = "<%=strVendorList%>";
parent.document.all("div_devicetype").innerHTML = "<%=strModelList%>";
parent.document.all("devicelist").innerHTML = "<%=strModelList1%>";
parent.document.all("cityCheck").innerHTML = "<%=cityHtml%>";
</script>
</body>
</html>
