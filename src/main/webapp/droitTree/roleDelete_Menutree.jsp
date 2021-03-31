<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%request.setCharacterEncoding("GBK");%>
<%@ page import ="com.linkage.litms.tree.Droit"%>
<%
String role_id = request.getParameter("role_id");
String strMsg = null;
//delete tree_id from role_id
Droit Droit = new Droit();

if(Droit.hasChildByRole(role_id)){
	strMsg = "该角色存在子角色，不可删除";
}else{
	boolean b = Droit.delRoleDroitByRoleId(role_id);
	boolean b1 =Droit.delAcc_RoleDroitByRoleId(role_id);
	boolean b2 =Droit.delItemDroitByRoleId(role_id);
	boolean b3 =Droit.delTreeDroitByRoleId(role_id);
//	可能角色并没有和item、tree、acc关联，所以只要判断是否删除tab_role就可以了
	if(b)
		strMsg = "删除成功";
	else
		strMsg = "删除失败";
}
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function GoList(page){
	this.location = page;
}
//-->
</SCRIPT>
<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
<TR><TD valign=top>
  <TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" width="65%" align="center" ONSELECTSTART="return false;" nowrap>
	<TR height="10" nowrap><TD></TD></TR>
	<TR>
	  <TD bgcolor="#999999">
		<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
			<TR>
				<TH align="center">操作提示信息</TH>
			</TR>
				<TR  height="50">
					<TD align=center valign=middle class=column><%out.println(strMsg);%></TD>
				</TR>
			<TR>
				<TD class=foot align="right">
				<INPUT TYPE="button" NAME="cmdJump" onclick="GoList('RoleList.jsp')" value=" 列 表 " class=btn>

				<INPUT TYPE="button" NAME="cmdBack" onclick="javascript:history.back();" value=" 返 回 " class=btn>
				</TD>
			</TR>
		</TABLE>	  
	  </TD>
	</TR>
	<TR height="20" nowrap>
	  <TD></TD>
	</TR>
  </TABLE>
</TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>