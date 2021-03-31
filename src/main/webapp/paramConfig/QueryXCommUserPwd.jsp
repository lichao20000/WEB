<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="java.util.*" %>
<%@ page import="com.linkage.litms.common.util.StringUtils" %>
<%@ page import="com.linkage.litms.common.database.Cursor" %>
<%@ page import="com.linkage.litms.common.database.DataSetBean" %>
<%
request.setCharacterEncoding("GBK");
String str_device_id = request.getParameter("device_id");

Cursor cursor = new Cursor();
Map fields = null;
if(str_device_id != null){
	String[] arr_device_id = str_device_id.split(";");
	
	str_device_id = StringUtils.weave(Arrays.asList(arr_device_id));
	String sql = "select device_serialnumber,x_com_username,x_com_passwd from tab_gw_device where device_id in("+ str_device_id +")";
	cursor = DataSetBean.getCursor(sql);	
}
String strHTML = "";
fields = cursor.getNext();
if(fields == null){
	strHTML += "<tr bgcolor=#ffffff><td colspan=3>查询无数据!</td></tr>";
}
while(fields != null){
	strHTML += "<tr bgcolor=#ffffff><td>" + fields.get("device_serialnumber") + "</td>";
	strHTML += "<td>" + fields.get("x_com_username") + "</td>";
	strHTML += "<td>" + fields.get("x_com_passwd") + "</td></tr>";
	fields = cursor.getNext();
}
%>

<%@ include file="../head.jsp"%>
<br>
<TABLE border=0 cellspacing=1 cellpadding=2 width="90%" align=center bgcolor="#999999">
	<tr>
		<th>设备OUI</th><th>帐号</th><th>密码</th>
	</tr>
	<%=strHTML%>
	<tr class=green_foot>
		<td colspan=3>&nbsp;</td>
	</tr>
</TABLE>
<%@ include file="../toolbar.jsp"%>