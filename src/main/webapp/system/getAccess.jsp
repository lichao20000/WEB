<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.warn.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.linkage.litms.common.database.*,com.linkage.litms.common.util.*"%>
<jsp:useBean id="deviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct" />

<%
String gather_id = request.getParameter("gather_id");
String access_flag = "";
String strSQL = "select access_flag from tab_auth where gather_id='" + gather_id + "'";
Cursor cursor = DataSetBean.getCursor(strSQL);
Map fields = cursor.getNext();
if (fields != null){
	access_flag = (String)fields.get("access_flag");
}
%>
<html>
<body>
<script language="javascript">
var flag="<%=access_flag%>";

	if (flag == '-1'){
		parent.form1.isAccess[0].checked = true;
	}
	else if (flag == '0'){
		parent.form1.isAccess[1].checked = true;
	}
	else if (flag == '1'){
		parent.form1.isAccess[2].checked = true;
	}
	else{
		parent.form1.isAccess[1].checked = true;
	}

</script>
</body>
</html>
