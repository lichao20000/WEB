<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<%@ page import="com.linkage.litms.flux.UserManagerDev"%>
<%@ page import="com.linkage.litms.flux.FluxUnit"%>
<%
	request.setCharacterEncoding("GBK");
	String[] searchKind = request.getParameterValues("kind");
	String type = request.getParameter("type");	
	FluxUnit fu=FluxUnit.getFluxUnit(session);
    double unit = fu.getFluxBase();
	UserManagerDev umd = new UserManagerDev();
	umd.setRequest(request);	
	Cursor cursor = umd.getFluxDataCursor();
	session.setAttribute("Cursors",cursor);
	HashMap map_baseFluxInfo = umd.getMap_baseFluxInfo();
	session.setAttribute("FluxInfo",map_baseFluxInfo);
    String data=umd.getFluxDataForTable(cursor,unit);
	String strkind="";
	

%>

<DIV id="idLayer">
	<table  width="98%"  border="0" cellspacing="0" cellpadding="0" align="center">
		<tr>
			<td bgcolor="#999999">
			     <table width="100%"  border="0" cellspacing="1" cellpadding="2">
				    <%=data %>
				</table>
			</td>
		</tr>
		<tr>
			<td bgcolor="#999999">
				<table width="100%"  border="0" cellspacing="1" cellpadding="2">
					<tr>
						<td class="column" width="20%" align='right'>流量类型</td>
						<td class="column">
							<table width="100%"  border="0" cellspacing="0" cellpadding="0" align="center">
								<tr>
									<%
										for (int i=0; i<searchKind.length; i++) {
										strkind = searchKind[i];
										
										if (strkind.equals("ifinoctetsbps")) {
											out.println("<td><input name=\"curvekind\" type=\"radio\" value=\"ifinoctetsbps\">流入速率</td>");
										}
										else if (strkind.equals("ifindiscardspps")) {
											out.println("<td><input name=\"curvekind\" type=\"radio\" value=\"ifindiscardspps\">流入丢包率</td>");
										}
										else if (strkind.equals("ifinerrors")) {
											out.println("<td><input name=\"curvekind\" type=\"radio\" value=\"ifinerrors\">流入错误包数</td>");
										}
										else if (strkind.equals("ifinoctetsbpsmax")) {
											out.println("<td><input name=\"curvekind\" type=\"radio\" value=\"ifinoctetsbpsmax\">流入峰值</td>");
										}
										else if (strkind.equals("ifinucastpktspps")) {
											out.println("<td><input name=\"curvekind\" type=\"radio\" value=\"ifinucastpktspps\">每秒流入单播包数</td>");
										}
										else if (strkind.equals("ifoutoctetsbps")) {
											out.println("<td><input name=\"curvekind\" type=\"radio\" value=\"ifoutoctetsbps\">流出速率</td>");
										}
										else if (strkind.equals("ifoutdiscardspps")) {
											out.println("<td><input name=\"curvekind\" type=\"radio\" value=\"ifoutdiscardspps\">流出丢包率</td>");
										}
										else if (strkind.equals("ifouterrors")) {
											out.println("<td><input name=\"curvekind\" type=\"radio\" value=\"ifouterrors\">流出错误包数</td>");
										}
										else if (strkind.equals("ifoutoctetsbpsmax")) {
											out.println("<td><input name=\"curvekind\" type=\"radio\" value=\"ifoutoctetsbpsmax\">流出峰值</td>");
										}
										else if (strkind.equals("ifinnucastpktspps")) {
											out.println("<td><input name=\"curvekind\" type=\"radio\" value=\"ifinnucastpktspps\">每秒流入非单播包数</td>");
										}
										else if (strkind.equals("ifinerrorspps")) {
											out.println("<td><input name=\"curvekind\" type=\"radio\" value=\"ifinerrorspps\">流入错包率</td>");
										}
										else if (strkind.equals("ifinoctets")) {
											out.println("<td><input name=\"curvekind\" type=\"radio\" value=\"ifinoctets\">流入字节数</td>");
										}
										else if (strkind.equals("ifindiscards")) {
											out.println("<td><input name=\"curvekind\" type=\"radio\" value=\"ifindiscards\">流入丢弃包数</td>");
										}
										else if (strkind.equals("ifoutucastpktspps")) {
											out.println("<td><input name=\"curvekind\" type=\"radio\" value=\"ifoutucastpktspps\">每秒流出单播包数</td>");
										}
										else if (strkind.equals("ifouterrorspps")) {
											out.println("<td><input name=\"curvekind\" type=\"radio\" value=\"ifouterrorspps\">流出错包率</td>");
										}
										else if (strkind.equals("ifoutoctets")) {
											out.println("<td><input name=\"curvekind\" type=\"radio\" value=\"ifoutoctets\">流出字节数</td>");
										}
										else if (strkind.equals("ifoutdiscards")) {
											out.println("<td><input name=\"curvekind\" type=\"radio\" value=\"ifoutdiscards\">流出丢弃包数</td>");
										}
										else if (strkind.equals("ifoutnucastpktspps")) {
											out.println("<td><input name=\"curvekind\" type=\"radio\" value=\"ifoutnucastpktspps\">每秒流出非单播包数</td>");
										}
										else if (strkind.equals("ifinunknownprotospps")) {
											out.println("<td><input name=\"curvekind\" type=\"radio\" value=\"ifinunknownprotospps\">每秒流入未知协议包数</td>");
										}
										else if (strkind.equals("ifoutqlenpps")) {
											out.println("<td><input name=\"curvekind\" type=\"radio\" value=\"ifoutqlenpps\">每秒流出队列大小</td>");
										}
									}
									%>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td class="green_foot" colspan="2" align="right"><input type="button" value="  图 形 " onclick="javascript:Grphis()" class="jianbian">&nbsp;&nbsp;</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</DIV>
<SCRIPT LANGUAGE="JavaScript">
<!--
if(typeof(parent.reportTabelView) == "object"){
	parent.reportTabelView.innerHTML = idLayer.innerHTML;
}
//-->
</SCRIPT>