<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.webtopo.GetPm_Expreesion, java.util.*,com.linkage.litms.common.database.Cursor"%>
<%
String key_exp = null;
String value_exp = null;
String retHtml = "";

Map feilds = new HashMap();
GetPm_Expreesion pmexp = new GetPm_Expreesion(request);

Cursor cursor = pmexp.getPm_exp();
if (null != cursor) {
	feilds = cursor.getNext();
}
String strExp = request.getParameter("expressionid");

if (strExp==null) {
	strExp = "0";
}
retHtml += "<select name=\"exp_name\" onchange=\"Pm_Name()\" class=\"bk\">";
retHtml += "<option value=0>请选择此设备的性能表达式</option>";

while (cursor != null && feilds != null) {
	key_exp = (String)feilds.get("expressionid");
	value_exp = (String)feilds.get("name");
	
	if (key_exp.trim().equals(strExp.trim())) {
		retHtml += "<option value='" + key_exp + "' selected>" + value_exp + "</option>";
	}
	else {
		retHtml += "<option value='" + key_exp + "'>" + value_exp + "</option>";
	}
	feilds = cursor.getNext();
}
retHtml += "</select>";
%>
<html>
<body>
<SPAN ID="retchild"><%=retHtml%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("pmdiv").innerHTML = retchild.innerHTML;
</SCRIPT>
</body>
</html>













