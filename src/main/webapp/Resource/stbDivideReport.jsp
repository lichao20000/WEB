<%--
Author      : ������
Date		: 2018-06-26
Desc		: �ӱ���ͨIPTV������װͨ
--%>
<%@ include file="../timelater.jsp"%>
<jsp:useBean id="StbDeviceReport" scope="request"
	class="com.linkage.litms.resource.StbDeviceReport" /> 
<%@ page contentType="text/html;charset=GBK"%>
<SCRIPT LANGUAGE="JavaScript">
</SCRIPT>
<%@ include file="../head.jsp"%>
<TABLE boder=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD>
				<TABLE width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<TR>
						<TD width="164" align="center" class="title_bigwhite">
							�ӱ���ͨIPTV������װͨ��������
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<%=StbDeviceReport.getHtmlDeviceByLinuxAndAndroid(request)%>
					<TR>
						<TD class=column colspan="21" height="30">
							<a href="stbDivideReport_prt.jsp?" alt="������ǰҳ���ݵ�Excel��">&nbsp;&nbsp;&nbsp;������Excel</a>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD></TR></TABLE>
<br>
<br>
<%@ include file="../foot.jsp"%>
<%
StbDeviceReport = null;
%>