<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.DataSetBean" %>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.util.Map"%>

<%
request.setCharacterEncoding("GBK");
String tableRows = "设备厂商,设备序列号,设备型号,属地,ADSL帐号(桥接),ADSL帐号(路由),IPTV帐号";//request.getParameter("tableRows");
//tableRows = new String(tableRows.getBytes("ISO8859-1"),"UTF-8");
//tableRows =  //java.net.URLDecoder.decode(new String(tableRows.getBytes()),"GBK");

String[] rowList = tableRows.split(",");

 %>

<html>
<head></head>

<script language="javascript">

function selectRow(){
	var checkList = ',';
	var checkValue = document.all("check");
	
	for (var i=0;i<checkValue.length;i++){
		if (!checkValue[i].checked){
			checkList += checkValue[i].value + ',';
		}
	}
	
	window.returnValue = checkList;
	window.close();
}

</script>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<body>
<form>
	<table width="100%" border=0 cellspacing=1 cellpadding=2 bgcolor=#999999>
		<tr>
			<th colspan=2>请选择需要导出的列</th>
		</tr>
		<%
		if (rowList != null && rowList.length > 0){
			for (int i=0;i<rowList.length;i++){ %>
		<tr>
			<td class=column width=10%><input type="checkbox" name="check" checked value="<%=i%>"></td>
			<td class=column width=90%><%=rowList[i]%></td>
		</tr>
		<%
			}
		}%>
		<tr>
			<td class=column colspan=2>
				<input type="button" name="确定" value="确定" onclick="selectRow()">
				<input type="button" name="取消" value="取消" onclick="window.close()">
			</td>
		</tr>
	</table>
</form>
</body>
</html>

