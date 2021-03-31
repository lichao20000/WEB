<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
DeviceAct act = new DeviceAct();
String strMsg;
boolean result =act.updateDeviceType(request);
if("delete".equals(request.getParameter("action")))
{
	if(result)
	{
		strMsg = "设备型号删除成功！";
	}
	else
	{
		strMsg="设备型号删除失败！";
	}		
}
else if("add".equals(request.getParameter("action")))
{
	if(result)
	{
		strMsg = "设备型号增加成功！";
	}
	else
	{
		strMsg="设备型号增加失败！";
	}		
}
else
{
	if(result)
	{
		strMsg = "设备型号编辑成功！";
	}
	else
	{
		strMsg="设备型号编辑失败！";
	}		
}
	
%>
<%@ include file="../head.jsp"%>
<%@page import="com.linkage.litms.resource.DeviceAct"%>
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
						<TH align="center">设备型号操作提示信息</TH>
					</TR>
					<TR  height="50">
						<TD align=center valign=middle class=column><%out.println(strMsg);%></TD>
					</TR>					
					<TR>
						<TD class=foot align="right">
						<INPUT TYPE="button" NAME="cmdJump" onclick="GoList('DeviceType_info.jsp')" value=" 列 表 " class=btn>

						<INPUT TYPE="button" NAME="cmdBack" onclick="javascript:history.go(-1);" value=" 返 回 " class=btn>
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