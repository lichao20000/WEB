<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>

<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>

<% 
	String device_id = request.getParameter("device_id");
%>

<SCRIPT LANGUAGE="JavaScript">
	function checkform(){
		if(!IsNull(document.frm.paraV.value,"����")){
			document.frm.paraV.focus();
			document.frm.paraV.select();
			return false;
		}
		document.all.tableView.display = "";
		document.all.tableView.innerHTML = "<BR><BR>���ڻ�ȡ,���Ժ�...";
	}
</SCRIPT>

<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
<TR>
	<TD valign=top>
	<FORM NAME="frm" METHOD="post" ACTION="GetParaModel_Submit.jsp" onsubmit="return checkform()" target="childFrm">
	<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
		<tr>
			<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						����ģ�ͻ�ȡ
					</td>
					<td>
						<img src="../images/attention_2.gif" width="15" height="12">
						�Բ�����ģ�ͽ��л�ȡ��
					</td>
				</tr>
			</table>
			</td>
		</tr>
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH  colspan="4" align="center"><B>����ģ�ͻ�ȡ</B></TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="5%" nowrap>����</TD>
					<TD width="40%">
						<INPUT TYPE="text" NAME="paraV" class="bk" style="width:350px;">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<INPUT TYPE="checkbox" NAME="doeshavechild" class="bk" value="0" checked>�Ƿ�����¼�
					</TD>
				</TR>

				<TR >
					<TD colspan="4" align="right" class=foot>
						<INPUT TYPE="submit" name="getPara" value=" �� ȡ" class=btn>&nbsp;&nbsp;
						<INPUT TYPE="hidden" name="device_id" value="<%= device_id%>">
					</TD>
				</TR>
			</TABLE>
			</TD>
		</TR>
		<TR><TD>
		<DIV id="tableView" style="overflow:auto;display:;" align="center"></DIV>
		</TD></TR>
	</TABLE>
	</FORM>
	</TD>
</TR>
</TABLE>
<IFRAME id="childFrm" name="childFrm" align="center" style="display:none"></IFRAME>
<%@ include file="../foot.jsp"%>
