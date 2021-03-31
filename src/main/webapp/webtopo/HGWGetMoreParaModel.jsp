<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>

<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>

<% 
	request.setCharacterEncoding("GBK");
	String device_id = request.getParameter("device_id");
%>

<SCRIPT LANGUAGE="JavaScript">
	function checkform(){
		//modified by Alex.Yan(yanhj@lianchuang.com):2007-6-1
//		if(!IsNull(document.frm.paraV.value,"参数")){
//			document.frm.paraV.focus();
//			document.frm.paraV.select();
//			return false;
//		}
		//end:2007-6-1
		document.all.tableView.display = "";
		document.all.tableView.innerHTML = "<BR><BR>正在获取,请稍候...";
	}
</SCRIPT>

<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
<TR>
	<TD valign=top>
	<FORM NAME="frm" METHOD="post" ACTION="GetMoreParaModel_Submit.jsp" onsubmit="return checkform()" target="childFrm">
	<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD>
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162">
						<div align="center" class="title_bigwhite">参数监视</div>
						</td>
						<td><img src="../images/attention_2.gif" width="15" height="12">
						注：带<font color="#FF0000">*</font>号为必选项。</td>

					</tr>
				</table>
			</TD>
		</TR>
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH  colspan="4" align="center"><B>参数查询</B></TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="10%" nowrap>参数</TD>
					<TD width="40%">
						<INPUT TYPE="text" NAME="paraV" class="bk" style="width:350px;">&nbsp;<font color="#FF0000">*</font>
					</TD>
				</TR>

				<TR >
					<TD colspan="4" align="right" class=foot>
						<INPUT TYPE="submit" name="getPara" value=" 获 取" class=btn>&nbsp;&nbsp;
						<INPUT TYPE="hidden" name="device_id" value="<%=device_id%>">
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
