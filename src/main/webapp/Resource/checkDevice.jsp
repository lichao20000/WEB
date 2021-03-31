<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.DataSetBean" %>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.util.Map"%>

<%
String oui = request.getParameter("oui");
String vender = request.getParameter("vender");
String gwOptType = request.getParameter("gwOptType");
String username = request.getParameter("username");
String strSql = "";
String strSql2 = "";
String device_id = "";
Cursor cursorTmp = null;
if(gwOptType != null ){
	//if("11".equals(gwOptType) || "21".equals(gwOptType)){
		
	strSql = "select device_id from tab_gw_device where oui = '" 
				+ oui + "' and device_serialnumber = '" + vender + "' and cpe_allocatedstatus=0";
	
	if("12".equals(gwOptType)){
		strSql2 = "select device_id from tab_hgwcustomer where oui = '" 
				+ oui + "' and device_serialnumber = '" + vender + "' and username = '"+username+"'" ;
	}else if("22".equals(gwOptType)){
		strSql2 = "select device_id from tab_egwcustomer where oui = '" 
				+ oui + "' and device_serialnumber = '" + vender + "' and username = '"+username+"'";
	}
}

if ((DataSetBean.getRecord(strSql) == null) && (DataSetBean.getRecord(strSql2) == null)){
	out.println("<font color='red'>该设备不存在或已被绑定</font>");
}

%>

<script language="javascript">
var device_id = "<%=device_id%>";

//window.returnValue = device_id;
parent.frm.device_id.value = device_id;

parent.frm.vender.focue();
//window.close();

</script>
