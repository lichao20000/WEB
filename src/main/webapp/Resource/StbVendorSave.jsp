<%--
FileName	: VendorSave.jsp
Author		: Dolphin
Date			: 2002-9-28
Note			: �������ӡ��޸ġ�ɾ������
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
String strMsg="";
StbVendorAct act = new StbVendorAct();
boolean result = act.updateVendor(request);
String action = request.getParameter("action");
if("add".equals(action))
{
	if(result)
	{
		strMsg="�豸�������ӳɹ�!";
	}
	else
	{
		strMsg="�豸��������ʧ��!";
	}
}
else  if("delete".equals(action))
{
	if(result)
	{
		strMsg="�豸����ɾ���ɹ�!";
	}
	else
	{
		strMsg="�豸����ɾ��ʧ��!";
	}
}
else
{
	if(result)
	{
		strMsg="�豸���̱༭�ɹ�!";
	}
	else
	{
		strMsg="�豸���̱༭ʧ��!";
	}
}
%>

<%@ include file="../head.jsp"%>
<%@page import="com.linkage.litms.resource.StbVendorAct"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function GoList(page){
	this.location = page;
}
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="50%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH align="center">������Դ������ʾ��Ϣ</TH>
					</TR>
					<TR  height="50">
							<TD align=center valign=middle class=column><%out.println(strMsg);%></TD>
					</TR>					
					<TR>
						<TD class=foot align="right">
						<INPUT TYPE="button" NAME="cmdJump" onclick="GoList('StbVendorList.jsp')" value=" �� �� " class=btn>

						<INPUT TYPE="button" NAME="cmdBack" onclick="javascript:history.go(-1);" value=" �� �� " class=btn>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>