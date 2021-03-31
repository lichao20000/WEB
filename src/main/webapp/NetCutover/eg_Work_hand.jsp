<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="ACS.Sheet,com.linkage.litms.netcutover.SheetManage"%>
<jsp:useBean id="paramManage" scope="request" class="com.linkage.litms.paramConfig.ParamManage"/>
<%
request.setCharacterEncoding("GBK");

//业务ID
String _type = request.getParameter("service_id");
int _type_ = Integer.parseInt(_type);
int reval = (new SheetManage()).updateEgwSheet(request);
String msg = "";
if (reval == -1) {
	String username = request.getParameter("username");
	String oui = request.getParameter("oui");
	String device_serialnumber = request.getParameter("device_serialnumber");
	String oui_new = request.getParameter("oui_new");
	String device_serialnumber_new = request.getParameter("device_serialnumber_new");
	response.sendRedirect("changedev_Result.jsp?username=" + username + "&oui=" + oui + "&device_serialnumber=" + device_serialnumber + "&oui_new=" + oui_new + "&device_serialnumber_new=" + device_serialnumber_new);
} else if (reval == 1) {
	msg = "调用后台接口成功！";
} else {
	msg = "调用后台接口失败！";
}

%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<FORM name="frm" action="" >
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="80%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH align="center">企业网关手工工单操作提示信息</TH>
					</TR>
						<TR  height="50">
							<TD align=center valign=middle class=column><%=msg%></TD>
						</TR>
					<TR>
						<TD class=foot align="right">
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>
</TD></TR>
	<TR>
		<TD HEIGHT=10>&nbsp;<IFRAME ID=childFrm SRC="about:blank" STYLE="display:none"></IFRAME>
		<IFRAME ID=childFrm2 SRC="about:blank" STYLE="display:none"></IFRAME>
		<IFRAME ID=childFrm3 SRC="" STYLE="display:none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
