<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>
<%@ page contentType="text/html;charset=GBK"%>
<%
	String type = request.getParameter("type");
	String destroyUserName = request.getParameter("acc_loginname");
	if ("1".equals(type)) {
		UserMap.getInstance().destroySessionByUserName(destroyUserName);
	}

	String currentUserName = user.getAccount();
	UserAct act = new UserAct();
	Cursor cursor = act.getOnlineUserInfo(request);
	Map fields = cursor.getNext();
	DesUtils des = new DesUtils();
%>
<%@page import="com.linkage.litms.system.dbimpl.UserAct"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@ page import="com.linkage.litms.common.util.DesUtils" %>
<%@ page import="com.linkage.module.gwms.util.StringUtil" %>

<TABLE border=0 cellspacing=0 cellpadding=0 width="98%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<tr>
		<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						���߹���Ա�б�
					</td>
					<td>
						<img src="../images/attention_2.gif" width="15" height="12">
						&nbsp;&nbsp;��ǰ���߹���Ա�б�
					</td>
				</tr>
			</table>
		</td>
	</tr>

	<TR>
		<TD>
		<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="idTable">
					<TR align=center>
						<TH>�û���</TH>
						<TH>�û�ʵ��</TH>
						<TH>����</TH>
						<TH>�ֻ���</TH>
						<TH>Email</TH>
						<TH>��¼IP</TH>
						<TH>����</TH>
					</TR>
					<%
							if (null != fields) {
								String city_id = null;
							while (null != fields) {
								city_id = fields.get("per_city") == null ? null : (String)fields.get("per_city");
								out.println("<tr align = center bgcolor='#FFFFFF'>");
								String acc_loginname = fields.get("acc_loginname") == null ? "" : (String)fields.get("acc_loginname");
								out.println("<td >" + acc_loginname + "</td>");
								String per_name = fields.get("per_name") == null ? "" : (String)fields.get("per_name");
								out.println("<td >" + per_name + "</td>");
								String cityname = "";
								if(city_id!=null){
									cityname = CityDAO.getCityIdCityNameMap().get(city_id);
								}
								out.println("<td >" + cityname + "</td>");
								String per_mobile = fields.get("per_mobile") == null ? "" : (String)fields.get("per_mobile");
								out.println("<td >" + per_mobile + "</td>");
								String per_email = fields.get("per_email") == null ? "" : (String)fields.get("per_email");
								out.println("<td >" + per_email + "</td>");
								String acc_login_ip = fields.get("acc_login_ip") == null ? "" : (String)fields.get("acc_login_ip");
								out.println("<td >" + acc_login_ip + "</td>");

								if (!currentUserName.equals(fields.get("acc_loginname"))) {
							out
									.println("<td ><a href ='onlineUserLogInfo.jsp?acc_oid="
									+ des.encrypt(StringUtil.getStringValue(fields.get("acc_oid")) )
									+ "'>�鿴��־|</a>"
									+ "<a href ='onlineUserList.jsp?type=1&acc_loginname="
									+ fields.get("acc_loginname")
									+ "'>�޳����û�</a></td>");
								} else {
							out
									.println("<td ><a href ='onlineUserLogInfo.jsp?acc_oid="
									+ des.encrypt(StringUtil.getStringValue(fields.get("acc_oid")))
									+ "'>�鿴��־</a></td>");
								}

								out.println("</tr>");
								fields = cursor.getNext();
							}
						} else {
							out
							.println("<tr align = center bgcolor='#FFFFFF'><td colspan =7>û�������û�</td></tr>");
						}
					%>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
</TABLE>
<BR><BR><BR>
<%@ include file="../foot.jsp"%>

