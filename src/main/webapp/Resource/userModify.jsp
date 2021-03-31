<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");

%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/edittable.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--

//-->
</SCRIPT>

<link rel="stylesheet" href="../css/listview.css" type="text/css">
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<FORM NAME="frm" METHOD="post" ACTION="userModifyInfo.jsp">
		<TABLE width="90%" border=0 cellspacing=0 cellpadding=0 align="center">
		<tr>
			<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						设备修障
					</td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
		<td bgcolor=#999999>
		<table width="100%" border=0 cellspacing=1 cellpadding=2 align="center">
			<tr bgcolor=#FFFFFF><td colspan="3" class=column2>请输入用户帐号：</td></tr>
			<tr bgcolor=#FFFFFF>
				<td width="10%" class=column2>用户帐户：</td>
				<td width="25%" class=column2>
					<input type="text" class="bk" name="username" value="">
				</td>
				<td class=column2>
					<input type="submit" name="submit" value="下一步">
				</td>
			</tr>
		</table>
		</td>
		</tr>
		</TABLE>
	</FORM>	
</TD></TR>
</TABLE>

<%@ include file="../foot.jsp"%>