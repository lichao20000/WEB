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
	<FORM NAME="frm" METHOD="post" ACTION="autoUserinstList.jsp">
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<tr>
			<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						�ֳ���װ
					</td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
		<td bgcolor=#999999>
		<table width="100%" border=0 cellspacing=1 cellpadding=2 align="center">
			<tr bgcolor=#FFFFFF><TH colspan="2" class=column2>�Զ���װ</TH></tr>
			<tr bgcolor=#FFFFFF>
				<td width="10%" class=column2 align="right">�û��ʻ���</td>
				<td width="25%" class=column2>
					<input type="text" class="bk" name="username" value=""> &nbsp;<input type="submit" name="submit" value=" ��һ�� ">
				</td>
			</tr>
		</table>
		</td>
		</tr>
		</TABLE>
	</FORM>	
</TD></TR>
</TABLE>

<br>
<br>
<%@ include file="../foot.jsp"%>