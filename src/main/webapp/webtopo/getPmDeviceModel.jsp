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
					<td class=column1 align="center" width="20%">�豸����</td>
					<td class=column1 align="center" width="20%">�豸IP</td>
					<td class=column1 align="center" width="45%">����״̬</td>
					<td class=column1 align="center" width="15%">�Ƿ�ɼ�&nbsp;&nbsp;<input name="select_all" type="checkbox" onclick="selectAll('select_one')"></td>
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
			out.println("<td class=column1 align=center>���óɹ�</td>");
			if (collect.trim().equals("1")) {
				out.println("<td class=column1 align=center><input name=select_one type=checkbox value=" + device_id + "," + expressionid + "," + devIP + "," + devName + " checked></td>");
			}
			else {
				out.println("<td class=column1 align=center><input name=select_one type=checkbox value=" + device_id + "," + expressionid + "," + devIP + "," + devName + "></td>");
			}
			
		}
		else if (isok.trim().equals("-1")) {
			out.println("<td class=column1 align=center>û������</td>");
			out.println("<td class=column1 align=center><input name=select_one type=checkbox value=" + device_id + "," + expressionid + "," + devIP + "," + devName + ",lose" + "></td>");
		}
		else if (isok.trim().equals("0")) {
			if (remark.trim().equals("-1")) {
				out.println("<td class=column1 align=center>ʧ��ԭ��:��ʱ</td>");
			}
			else if (remark.trim().equals("-2")) {
				out.println("<td class=column1 align=center>ʧ��ԭ��:��֧��</td>");
			}
			else if (remark.trim().equals("-21")) {
				out.println("<td class=column1 align=center>ʧ��ԭ��:������һ��oid�ɼ���������</td>");
			}
			else if (remark.trim().equals("-3")) {
				out.println("<td class=column1 align=center>ʧ��ԭ��:û�вɼ���������Ϣ</td>");
			}
			else if (remark.trim().equals("-4")) {
				out.println("<td class=column1 align=center>ʧ��ԭ��:��ʶ����oid�ɼ�������������һ��</td>");
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
