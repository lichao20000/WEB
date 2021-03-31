<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");
	String mysql="select a.*,(select group_name from tab_group where group_oid=a.group_poid) as group_pname from tab_group a ";
	// teledb
	if (DBUtil.GetDB() == 3) {
		mysql="select a.group_oid, a.group_name, (select group_name from tab_group where group_oid=a.group_poid) as group_pname " +
				" from tab_group a ";
	}
	com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(mysql);
	psql.getSQL();
	Cursor cursor=DataSetBean.getCursor(mysql);
	Map fields=cursor.getNext();
	String strData="";
	String pname="";
	if(fields!=null)
	{
		while(fields!=null)
		{   
		 //   pname = fields.get("group_name");
			strData +="<tr style='cursor:hand;background:#F4F4FF' onmouseover=\"this.style.background='#CECECE';this.style.color='#006790';\" onmouseout=\"this.style.background='#F4F4FF';this.style.color='#000000';\" ondblclick=\"javascript:CallSetValue('"+fields.get("group_oid")+"','"+fields.get("group_name")+"')\" title=\"双击选择该角色\"><td class=column1 align=center>"+fields.get("group_oid")+"</td><td class=column1 align=center>"+fields.get("group_name")+"</td><td class=column1 align=center>"+fields.get("group_pname")+"</td></tr>";
			fields=cursor.getNext();
		}
	}
	else
	{
		strData="<tr><td colspan=3 class=column1>没有获取到用户组信息</td></tr>";
	}
%>

<SCRIPT LANGUAGE="JavaScript">
<!--
	function CallSetValue(role_id,role_name)
	{
		opener.SetValue(role_id,role_name);
		window.close();	
	}
//-->
</SCRIPT>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.util.Map"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>

<%@ include file="../head.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="90%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
					<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
						<TR>
							<TD bgcolor="#ffffff" colspan="3" >用户组列表【双击选择】</TD>
						</TR>
						<TR>
							<TH>用户组编号</TH>
							<TH>用户组名称</TH>		
							<TH>父用户组名称</TH>	
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