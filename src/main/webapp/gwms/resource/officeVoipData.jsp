<%@ page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("utf-8");
	
	String cityName = request.getParameter("cityName");
	String officeName = request.getParameter("officeName");
	String proxServ = request.getParameter("proxServ");
	String proxPort = request.getParameter("proxPort");
	String proxServ2 = request.getParameter("proxServ2");
	String proxPort2 = request.getParameter("proxPort2");
	String regiServ = request.getParameter("regiServ");
	String regiPort = request.getParameter("regiPort");
	String standRegiServ = request.getParameter("standRegiServ");
	String standRegiPort = request.getParameter("standRegiPort");
	String outBoundProxy = request.getParameter("outBoundProxy");
	String outBoundPort = request.getParameter("outBoundPort");
	String standOutBoundProxy = request.getParameter("standOutBoundProxy");
	String standOutBoundPort = request.getParameter("standOutBoundPort");
	StringBuffer sb = new StringBuffer();
	sb
			.append("<table width=\"100%\"  border=0 cellspacing=1 cellpadding=1 bgcolor='#999999'>");
	// �����ͷ
	sb.append("<tr><td class=column  width=\"50%\" >����</td>");
	sb.append("<td class=column>").append(cityName).append("</td></tr>");
	sb.append("<tr><td class=column >����</td>");
	sb.append("<td class=column>").append(officeName).append("</td></tr>");
	sb.append("<tr><td class=column>SIP��������ַ</td>");
	sb.append("<td class=column>").append(proxServ).append("</td></tr>");
	sb.append("<tr><td class=column >SIP�������˿�</td>");
	sb.append("<td class=column>").append(proxPort).append("</td>");
	sb.append("<tr><td class=column >SIP���������õ�ַ</td>");
	sb.append("<td class=column>").append(proxServ2).append("</td></tr>");
	sb.append("<tr><td class=column >SIP���������ö˿�</td>");
	sb.append("<td class=column>").append(proxPort2).append("</td>");
	sb.append("<tr><td class=column >ע���������ַ</td>");
	sb.append("<td class=column>").append(regiServ).append("</td></tr>");
	sb.append("<tr><td class=column >ע��������˿�</td>");
	sb.append("<td class=column>").append(regiPort).append("</td>");
	sb.append("<tr><td class=column>����ע���������ַ</td>");
	sb.append("<td class=column>").append(standRegiServ).append("</td></tr>");
	sb.append("<tr><td class=column >����ע��������˿�</td>");
	sb.append("<td class=column>").append(standRegiPort).append("</td>");
	sb.append("<tr><td class=column>Outbound��������ַ��</td>");
	sb.append("<td class=column>").append(outBoundProxy).append("</td></tr>");
	sb.append("<tr><td class=column>Outbound���������˿�</td>");
	sb.append("<td class=column>").append(outBoundPort).append("</td>");
	sb.append("<tr><td class=column>����Outbound��������ַ</td>");
	sb.append("<td class=column>").append(standOutBoundProxy).append("</td></tr>");
	sb.append("<tr><td class=column >����Outbound�������˿�</td>");
	sb.append("<td class=column>").append(standOutBoundPort).append("</td>");
	sb.append("<tr bgcolor=#ffffff><td colspan=2 class=green_foot align=right><input type=button value=\"�ر�\" onclick=CloseDetail()>");
	sb.append("</td></tr>");
	sb.append("</table>");
	out.println(sb.toString());
%>



