<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>

<%
request.setCharacterEncoding("GBK");

Role role = new RoleSyb(Integer.parseInt(request.getParameter("role_id")));
%>
<%@ include file="../head.jsp"%>
<%@page import="com.linkage.litms.system.dbimpl.RoleSyb"%>
<%@page import="com.linkage.litms.system.Role"%>
<style>
SPAN.BT
{
	padding-right: 6px;
	padding-left: 6px;
	padding-top: 2px;
	border-top: #ffffff 1px solid;
	border-bottom: #ffffff 1px solid;
	border-right: #ffffff 1px solid;
	border-left: #ffffff 1px solid;
	cursor:hand;
}

SPAN.BTOver{
	padding-right: 6px;
	padding-left: 6px;
	padding-top: 2px;
	border-top: #316AC5 1px solid;
	border-bottom: #316AC5 1px solid;
	border-right: #316AC5 1px solid;
	border-left: #316AC5 1px solid;
	cursor:hand;
	background-color: #C1D2EE;
}
</style>
<SCRIPT LANGUAGE="JavaScript">
<!--
function CheckForm(){
	var obj = document.frm;
	if(!IsNull(obj.role_name.value,'��ɫ����')){
		obj.role_name.focus();
		obj.role_name.select();
		return false;
	}
	/*
	else if(!IsNull(obj.gu_name.value,'��ɫ�û�')){
		return false;
	}
	*/
	else if(!isChecked()){
		alert("�����Ȩ�޲���Ϊ��");
		return false;
	}
	else{
		return true;
	}	
}

function isChecked(){
	var obj=document.all("operator_id");
	for(var i=0;i<obj.length;i++)
		if(obj[i].checked)
			return true;
	return false;
}

function SelectUser(){
	var page = "/lib/UserPicker.jsp";
	var uObject = new Object();
	var obj = document.frm;
	uObject.name = obj.gu_name.value;
	var vReturnValue = window.showModalDialog(page,uObject,"dialogHeight: 470px; dialogWidth: 540px; dialogTop: px; dialogLeft: px; edge: Raised; center: Yes; help: No; resizable: No; status: No;scroll:No;");
	
	if(typeof(vReturnValue) == "object"){
		obj.gu_name.value = vReturnValue.name;
		obj.gu_oid.value  = vReturnValue.oid;
	}
}
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="./Js/tree.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="./Js/tree_maker.js"></SCRIPT>

<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
<TR>
	<TD valign=top>
	<FORM NAME="frm" METHOD="post" ACTION="RoleSave.jsp" onsubmit="return CheckForm()">
	<TABLE width="90%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR height="10" nowrap><TD></TD></TR>
		<TR>
		  <TD>
			<table border=0 cellpadding=0 cellspacing=0 width="100%" align=center class="GG">
			<tr height=24>
			<td valign=middle width=30 align=center nowrap><img src="images/attention.gif" width=15 height=12></td>
			<td>Ϊ�˺ܺõĿ����û�Ȩ�ޣ���Ϊ��ɫ����Ȩ��ʱ��չ��Ȩ�������Ȼ����̳и����Ȩ��</td>
			</tr>
			</table>
		  </TD>
		</TR>
		<TR height="10" nowrap>
		  <TD></TD>
		</TR>
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH  colspan="2" align="center"><B>�½���ɫ</B></TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" >��ɫ����</TD>
					<TD><INPUT TYPE="text" NAME="role_name" maxlength=25 class=bk size=20 value="<%=role.getRoleName()%>">&nbsp;<font color="#FF0000">*</font></TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" >��ɫ����</TD>
					<TD><INPUT TYPE="text" NAME="role_desc" maxlength=40 class=bk size=40 value="<%=role.getRoleDesc()%>"></TD>
				</TR>
				<TR style="background-color:#A0C6E5" >
					<TD colspan="2">��ɫȨ��</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" >����Ȩ��</TD>
					<TD bgcolor="#F1F1F1"><div id='idRoleTree' XMLSrc="operatorbyrole_xml.jsp?role_id=<%=role.getRoleId()%>" style="display:none"></div>
					<div id='idTree' XMLSrc="operator_xml.jsp">
					<SCRIPT LANGUAGE="JavaScript">
					<!--
					initTree();
					//-->
					</SCRIPT>
					</div></TD>
				</TR>
				<TR>
					<TD colspan="2" align="center" class=foot>
						<INPUT TYPE="submit" value=" �� �� " class=btn>&nbsp;&nbsp;
						<INPUT TYPE="hidden" name="action" value="update">
						<INPUT TYPE="hidden" name="role_id" value="<%=role.getRoleId()%>">
						<INPUT TYPE="reset" value=" �� д " class=btn>
					</TD>
				</TR>
			</TABLE>
			</TD>
		</TR>
	</TABLE>
	</FORM>
	</TD>
</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
