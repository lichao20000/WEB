<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>

<% 
	request.setCharacterEncoding("GBK");
	String dxlx = request.getParameter("dxlx");
	ProcessDescAct pda = new ProcessDescAct();
	Cursor cursor = pda.getProcess_state(dxlx);
	Map feilds = cursor.getNext();
%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.util.Map"%>
<HTML>
<BODY>
<form name="frm">
<SPAN ID="child">
  
  <table width="100%" border="0" cellspacing="1" cellpadding="2" bgcolor=#000000>
	  <tr>
		<td class=column1 align=center>×´Ì¬Öµ</td>
		<td class=column1 align=center>×´Ì¬Ãû³Æ</td>		
		<td class=column1 align=center>Ñ¡Ôñ</td>	
	  </tr>
		<%
			if (feilds == null) {
				out.println("<tr>");
				out.println("<td class=column1>&nbsp;</td>");
				out.println("<td class=column1>&nbsp;</td>");
				out.println("<td class=column1>&nbsp;</td>");
				out.println("</tr>");
			}
			else {
				String ztbh = null;
				String ztmc = null;
				String selectfive_value = new String();

				while (feilds != null) {
					ztbh = (String)feilds.get("ztbh");
					ztmc = (String)feilds.get("ztmc");
					selectfive_value = dxlx + "#" + ztbh + "#" + ztmc;
					out.println("<tr>");
					out.println("<td class=column1 align=center>" + ztbh + "</td>");
					out.println("<td class=column1 align=center>" + ztmc + "</td>");				
					out.println("<td class=column1 align=center><input name=select_five type=checkbox value='" + selectfive_value + "'></td>");
					out.println("</tr>");
					feilds = cursor.getNext();
				}
				cursor.Reset();
			}
		%>
 </table>
	
</SPAN>
</form>
<SCRIPT LANGUAGE="JavaScript">
<!--

parent.document.all("process_state").innerHTML = child.innerHTML;
//-->
</SCRIPT>
<body>
</html>