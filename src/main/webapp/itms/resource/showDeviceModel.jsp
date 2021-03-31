<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");
//	String device_modle = request.getParameter("vendor_id");
	//String tmpSql = "select devicetype_id,device_model+'('+softwareversion+')' as device_model from tab_devicetype_info  where 1=1 and oui='"+device_modle+"' order by devicetype_id";
//	String tmpSql = "select device_model_id,device_model from gw_device_model where oui = '" + device_modle + "' order by device_model_id";

	String vendorId = request.getParameter("vendor_id");
	String tmpSql = "select device_model_id,device_model from gw_device_model where vendor_id = '" + vendorId + "' order by device_model_id";
	com.linkage.litms.common.database.Cursor cursor = com.linkage.litms.common.database.DataSetBean.getCursor(tmpSql);
	String strChildList = com.linkage.litms.common.util.FormUtil.createListBox(cursor, "device_model_id","device_model", true, "", "");
	
%>
<html>
<body>
<SPAN ID="child"><%=strChildList%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("div_device_model_id").innerHTML = child.innerHTML;
</SCRIPT>
</body>
</html>