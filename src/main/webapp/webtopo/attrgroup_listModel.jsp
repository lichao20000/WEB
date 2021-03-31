<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import= "com.linkage.litms.webtopo.host.*" %>

<%
	request.setCharacterEncoding("GBK");
	String dxlx = request.getParameter("dxlx");	
	AttributeInstanceAct aia = new AttributeInstanceAct();
	Cursor cursor = aia.getAttrGroup(dxlx);
	Map feilds = cursor.getNext();
%>
<HTML>
<BODY>
<form name="frm">
<div ID="child">
  
  <table width="100%" border="0" cellspacing="1" cellpadding="2" bgcolor=#000000>
	  <tr>
		<td class=column1 align=center>±àºÅ</td>
		<td class=column1 align=center>×éÃû</td>
		<td class=column1 align=center>ÃèÊö</td>
		<td class=column1 align=center>Ñ¡Ôñ</td>	
	  </tr>
		<%
			if (feilds == null) {
				out.println("<tr>");
				out.println("<td class=column1>&nbsp;</td>");
				out.println("<td class=column1>&nbsp;</td>");
				out.println("<td class=column1>&nbsp;</td>");
				out.println("<td class=column1>&nbsp;</td>");
				out.println("</tr>");
			}
			else {
				String zbh = null;
				String zmc = null;
				String zsm = null;
				String selectfour_value = new String();

				while (feilds != null) {
					dxlx = (String)feilds.get("dxlx");
					zbh = (String)feilds.get("zbh");
					zmc = (String)feilds.get("zmc");
					zsm = (String)feilds.get("zsm");
					selectfour_value = dxlx + "#" + zbh + "#" + zmc + "#" + zsm;
					out.println("<tr>");
					out.println("<td class=column1 align=center>" + zbh + "</td>");
					out.println("<td class=column1 align=center>" + zmc + "</td>");
					out.println("<td class=column1 align=center>" + zsm + "</td>");
					out.println("<td class=column1 align=center><input name=select_four type=checkbox value='" + selectfour_value + "'></td>");
					out.println("</tr>");
					feilds = cursor.getNext();
				}
				cursor.Reset();
			}
		%>
 </table>
	
</div>
</form>
<SCRIPT LANGUAGE="JavaScript">
<!--

parent.document.all("attrgroup_list").innerHTML = child.innerHTML;
//-->
</SCRIPT>
<body>
</html>