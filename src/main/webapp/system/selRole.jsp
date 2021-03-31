<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../timelater.jsp"%>

<jsp:useBean id="roleManage" scope="request" class="com.linkage.litms.system.dbimpl.RoleManagerSyb"/>

<%
	request.setCharacterEncoding("GBK");
	
	// 获取当前登录用户的角色ID，以及子角色ID add by zhangchy 2012-09-04 ----------begin--------
	StringBuffer sbRoleId = new StringBuffer();  // 存储登录用户的角色ID及子角色ID
	Cursor cursor1 = roleManage.getAllRolesByRolePid(Integer.parseInt(String.valueOf(user.getRoleId())));
	int count = cursor1.getRecordSize();
	Map fields1 = cursor1.getNext();
	while(fields1 != null){
		sbRoleId.append(fields1.get("role_id")).append(",");
		fields1 = cursor1.getNext();
	}
	sbRoleId.append(String.valueOf(user.getRoleId()));
	// add by zhangchy 2012-09-04  ----------------end-----------
	
	// 下面的SQL也在有基础上增加了限制条件 where role_id in ("+sbRoleId.toString()+") 只能查看当前登录人所属角色及其子角色的角色
	String mysql="select role_id,role_name from tab_role where role_id in ("+sbRoleId.toString()+") order by role_id";
	Cursor cursor=DataSetBean.getCursor(mysql);
	Map fields=cursor.getNext();
	String strData="";
	if(fields!=null)
	{
		while(fields!=null)
		{
			strData +="<tr style='cursor:hand;background:#F4F4FF' onmouseover=\"this.style.background='#CECECE';this.style.color='#006790';\" onmouseout=\"this.style.background='#F4F4FF';this.style.color='#000000';\" ondblclick=\"javascript:CallSetValue('"+fields.get("ROLE_ID".toLowerCase())+"','"+fields.get("ROLE_NAME".toLowerCase())+"')\" title=\"双击选择该角色\"><td class=column1 align=center>"+fields.get("ROLE_ID".toLowerCase())+"</td><td class=column1 align=center>"+fields.get("ROLE_NAME".toLowerCase())+"</td></tr>";
			fields=cursor.getNext();
		}
	}
	else
	{
		strData="<tr><td colspan=3 class=column1>没有获取到用户角色信息</td></tr>";
	}
%>

<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.util.Map"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	function CallSetValue(role_id,role_name)
	{
		opener.SetValue(role_id,role_name);
		window.close();	
	}
//-->
</SCRIPT>
<%@ include file="../head.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="90%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
					<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
						<TR>
							<TD bgcolor="#ffffff" colspan="3" >用户角色列表【双击选择】</TD>
						</TR>
						<TR>
							<TH>角色编号</TH>
							<TH>角色名称</TH>							
						</TR>
						<%=strData%>
						<TR>
							<td class=column1 colspan=3 align=right><input type="button" name="close" value=" 关 闭 " onclick="javascript:window.close();" class="btn"></td>
						</TR>

					</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD>
</TR>
</TABLE>
<%@ include file="../foot.jsp"%>