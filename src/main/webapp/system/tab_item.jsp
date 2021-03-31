<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
curUser = (UserRes) session.getAttribute("curUser");
String account = curUser.getUser().getAccount();

%>
<%@ include file="../head.jsp"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="com.linkage.litms.common.database.QueryPage"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@page import="java.util.HashMap"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function CheckForm(){
	var obj = document.frm;
	if(obj.tab_name.value ==""){
		alert("请输入表名！");
		obj.tab_name.focus();
		return false;
	}
	if(obj.tab_name_zh.value==""){
		alert("请输入表中文名！");
		obj.tab_name_zh.focus();
		return false;
	}
	if(obj.data_type.value=="-1"){
		alert("请选择数据表类型！");
		obj.data_type.focus();
		return false;
	}
}

function reload(){
	this.location ="tab_item.jsp";
}

//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>

<FORM NAME="frm" METHOD="post" ACTION="tab_item_save.jsp" onsubmit="return CheckForm()" target=childFrm >
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<table width="98%" align=center  height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
		<tr>
			<td width="162" align="center" class="title_bigwhite">
				创建维护表项
			</td>
		</tr>
	</table>
</TD></TR>
<TR><TD>
	<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH colspan="6" align="center">创建维护表项</TH>
					</TR>
					<TR BGCOLOR=#ffffff > 
					  <TD align=right class=column>表名</TD>
					  <TD colspan="">
						<INPUT name="tab_name" type="text" class="bk" value="">&nbsp;<font color="#FF0000">*</font>
					  </TD>
					  <TD align=right class=column>中文名</TD>
					  <TD colspan="3">
							<INPUT name="tab_name_zh" type="text" class="bk" value="">&nbsp;<font color="#FF0000">*</font>
					  </TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right">数据表类型</TD>
						<TD colspan="5">
							<select name="data_type" class="bk">
								<option value="-1">==请选择==</option>
								<option value="0">设备数据</option>
								<option value="1">用户数据</option>
								<option value="2">日志数据</option>
								<option value="3">告警数据</option>
								<option value="4">性能数据</option>
							</select>
						</TD>
					</TR>
					
					<TR>
						<TD colspan="6" align="right" CLASS=green_foot>
							<INPUT TYPE="hidden" name="action" value="add">
							<INPUT TYPE="submit" value=" 保 存 " class=btn>&nbsp;&nbsp;
							<INPUT TYPE="reset" value=" 重 写 " class=btn>
							
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD></TR>
</FORM>
<TR><TD HEIGHT=20>&nbsp;<IFRAME ID=childFrm NAME="childFrm" SRC="" STYLE="display:none"></IFRAME></TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>
