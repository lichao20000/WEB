<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="UserInstAct" scope="request"
	class="com.linkage.litms.resource.UserInstAct" />
<%
	String strAction = request.getParameter("strAction");
	String strMsg = "未知错误！";

	if ("save".equals(strAction)) {
		strMsg = UserInstAct.ipossSaveConfig(request);
	} else if ("modify".equals(strAction)) {
		strMsg = UserInstAct.ipossModifyConfig(request);
	}
%>
<%@ include file="../head.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<TABLE width="50%" border=0 cellspacing=0 cellpadding=0
				align="center">
				<TR>
					<TD bgcolor=#000000>
						<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
							<TR>
								<TH align="center">
									现场安装提示信息
								</TH>
							</TR>
							<TR height="50">
								<TD align=center valign=middle class=column>
									<%=strMsg%>
								</TD>
							</TR>
							<tr>
								<td class=column align="right">
									<input type="button" name="back" value="返回"
										onclick="history.go(-1)">
								</td>
							</tr>
						</TABLE>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
