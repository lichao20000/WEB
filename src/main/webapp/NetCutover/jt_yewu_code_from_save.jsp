<%--
FileName	: IpLineSave.jsp
Author		: liuli
Date		: 2007-3-06
Note		: 帐号增加、修改、删除操作
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.FileSevice"/>
<%
			request.setCharacterEncoding("GBK");
			String strMsg = DeviceAct.YewuCode(request);
			String drt_name1 = request.getParameter("drt_name1");
			 try {
				  drt_name1 = new String(drt_name1.getBytes("iso8859-1"),"GBK" );
	          } catch (Exception ex) {
	           //    ex.printStackTrace();
	          }
			String str_service_id = request.getParameter("lg_id");
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function GoList(page){
	this.location = page;
}
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<TABLE width="50%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH align="center">业务代码操作提示信息</TH>
					</TR>
					<TR height="50">
						<TD align=center valign=middle class=column><%out.println(strMsg);%></TD>
					</TR>
					<TR>
						<TD class=foot align="right">
						<INPUT TYPE="button" NAME="cmdJump" onclick="GoList('jt_yewu_code.jsp?lg_id=<%=str_service_id%>&drt_name1=<%=drt_name1%>')" value=" 列 表 " class=btn>
						 <INPUT TYPE="hidden" name="lg_id" value=<%=str_service_id %>> 
						 <INPUT TYPE="hidden" name="drt_name1" value=<%=drt_name1 %>>
						
						</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
