<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import= "com.linkage.litms.webtopo.common.*" %>

<%
	String expressionid = request.getParameter("expressionid");
	Pm_DevList pdl = new  Pm_DevList(request);
	Cursor cursor = pdl.getPm_mapinfo();
	Map result = cursor.getNext();
	String devName = null;
	String devIP = null;
	String collect = null;
	String isok = null;
	String remark = null;
	String device_id = null;
%>
<HTML>
<BODY>
<form name="frm">
<SPAN ID="child">
<table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0">
	<tr>
		<td bgcolor=#000000>
			<table width="100%"  border="0" cellspacing="1" cellpadding="2">
				<tr>
					<td class=column1 align="center" width="20%">设备名称</td>
					<td class=column1 align="center" width="20%">设备IP</td>
					<td class=column1 align="center" width="45%">配置状态</td>
					<td class=column1 align="center" width="15%">是否采集&nbsp;&nbsp;<input name="select_all" type="checkbox" onclick="selectAll('select_one')"></td>
				</tr>
<%
	while (result != null) {
		device_id = (String)result.get("device_id");
		devName = (String)result.get("device_name");
		devIP = (String)result.get("loopback_ip");
		collect = (String)result.get("collect");
		isok = (String)result.get("isok");
		remark = (String)result.get("remark");

		out.println("<tr>");
		out.println("<td class=column1 align=center>" + devName + "</td>");
		out.println("<td class=column1 align=center>" + devIP + "</td>");
		if (isok.trim().equals("1")) {
			out.println("<td class=column1 align=center>配置成功</td>");
			if (collect.trim().equals("1")) {
				out.println("<td class=column1 align=center><input name=select_one type=checkbox value=" + device_id + "," + expressionid + "," + devIP + "," + devName + " checked></td>");
			}
			else {
				out.println("<td class=column1 align=center><input name=select_one type=checkbox value=" + device_id + "," + expressionid + "," + devIP + "," + devName + "></td>");
			}
			
		}
		else if (isok.trim().equals("-1")) {
			out.println("<td class=column1 align=center>没有配置</td>");
			out.println("<td class=column1 align=center><input name=select_one type=checkbox value=" + device_id + "," + expressionid + "," + devIP + "," + devName + ",lose" + "></td>");
		}
		else if (isok.trim().equals("0")) {
			if (remark.trim().equals("-1")) {
				out.println("<td class=column1 align=center>失败原因:超时</td>");
			}
			else if (remark.trim().equals("-2")) {
				out.println("<td class=column1 align=center>失败原因:不支持</td>");
			}
			else if (remark.trim().equals("-21")) {
				out.println("<td class=column1 align=center>失败原因:其中有一个oid采集不到数据</td>");
			}
			else if (remark.trim().equals("-3")) {
				out.println("<td class=column1 align=center>失败原因:没有采集到描述信息</td>");
			}
			else if (remark.trim().equals("-4")) {
				out.println("<td class=column1 align=center>失败原因:标识几个oid采集到的索引数不一致</td>");
			}
			out.println("<td class=column1 align=center><input name=select_one type=checkbox value=" + device_id + "," + expressionid + "," + devIP + "," + devName + ",lose" + "></td>");
		}
		out.println("</tr>");
		result = cursor.getNext();
	}
%>
			</table>
		</td>
	</tr>
</table>
</SPAN>
</form>
<body>
</html>
<SCRIPT LANGUAGE="JavaScript">
<!--

parent.document.all("pmDevInfo").innerHTML = child.innerHTML;
//-->
</SCRIPT>
