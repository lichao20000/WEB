<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="SheetExeRuleAct" scope="request"
	class="com.linkage.litms.resource.SheetExeRuleAct" />
<!--取得上一个页面提交的参数-->

<%
	request.setCharacterEncoding("GBK");
	curUser = (UserRes) session.getAttribute("curUser");
	String account = curUser.getUser().getAccount();
	String sheetArr = request.getParameter("sheetArr");
	String[] sheetIDs = null;
	if (sheetArr != null) {
		sheetIDs = sheetArr.split(",");
	}
	//String[] sheetIDs = request.getParameter("sheetArr");//{"1","2","3","4","5"};//
	boolean doseHaveSheetID = true;
	if (sheetIDs == null) {
		sheetIDs = (SheetExeRuleAct.getSheetIDs(false, "", "", request))
				.split(",");
		if (sheetIDs == null || sheetIDs.length == 0) {
			doseHaveSheetID = false;
		}
	}
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" src="../Js/LayWriter.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
function onSelect() {
}
function CheckForm() {
	var doseHaveSheetID = <%=doseHaveSheetID%>;
	var obj = document.frm;

	if(!doseHaveSheetID) {	
		alert("没有任务ID!");
		obj.sheet_id.focus();
		return false;
	}
	if (obj.sheet_id.value == null || obj.sheet_id.value =="") {
		alert("请选择任务ID!");
		obj.sheet_id.focus();
		return false;
	}
    if(!IsNull(obj.execute_time.value,'执行时间')) {
		obj.execute_time.focus();
		obj.execute_time.select();
		return false;
	}
	return true;
}

function OnEditTime() {
	var winattr="center:yes;dialogWidth:800px;dialogHeight:550px;"
	var StringPara="",pos1=0,pos2=0;
	StringPara=window.showModalDialog("./RulerTimeForm.jsp","编辑时间",winattr);
	if(StringPara!=null) {
		if(StringPara!="")
		document.frm.execute_time.value=StringPara;
	}
}

function OnAppointTime() {
	var winattr="center:yes;dialogWidth:800px;dialogHeight:550px;"
	var StringPara="",pos1=0,pos2=0;
	StringPara=window.showModalDialog("./RulerAppointTimeForm.jsp","指定时间",winattr);
	if(StringPara!=null) {
		if(StringPara!="")
		document.frm.execute_time.value=StringPara;
	}
}

//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<form action="SheetExeRuleSave.jsp" method="post" name="frm"
	onSubmit="return CheckForm()" target="childFrm">
	<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
		<TR>
			<TD valign=top>
				<TABLE width="98%" border=0 cellspacing=0 cellpadding=0
					align="center">
					<TR>
						<TD HEIGHT=20>
							&nbsp;
						</TD>
					</TR>
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162" align="center" class="title_bigwhite">
										计划任务管理
									</td>
									<td>
										<img src="../images/attention_2.gif" width="15" height="12">
										计划任务配置(带*必须填写)
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<TR>
						<TD bgcolor=#999999>

							<TABLE width="100%" border=0 cellspacing="1" cellpadding="0"
								align="center">
								<tr>
									<TH colspan="4" align="center">
										添加新任务
									</TH>
								</tr>
								<TR BGCOLOR=#ffffff>
									<TD width=80 align=right class=column>
										制定人
									</TD>
									<TD colspan="3">
										&nbsp;
										<INPUT name="acc_loginname" type="text" value="<%=account%>"
											size="25" readonly>
										&nbsp;
										<font color="#FF0000">*</font>
									</TD>
								</TR>
								<TR BGCOLOR=#ffffff>
									<TD width=80 align=right class=column>
										工单编号
									</TD>
									<TD colspan="3">
										&nbsp;
										<SELECT name="sheet_id" size="6"
											style="width: 300; height: 100" multiple>
											<%
												for (int i = 0; i < sheetIDs.length; i++) {
											%>
											<option value="<%=sheetIDs[i]%>"><%=sheetIDs[i]%></option>
											<%
												}
											%>
										</SELECT>
										&nbsp;
										<font color="#FF0000">*</font>
								</TR>
								<TR bgcolor=#ffffff>
									<TD class=column noWrap align=right>
										执行时间
									</TD>
									<TD colspan="3" align="left" noWrap valign="middle">
										&nbsp;
										<TEXTAREA name="execute_time" cols="65" readonly></TEXTAREA>
										&nbsp;&nbsp;
										<font color="#FF0000">*</font>
										<INPUT name="EditTime" type="button" onClick="OnEditTime();"
											value="编辑时间" class=btn>
										<INPUT name="appointTime" type="button"
											onClick="OnAppointTime();" value="指定时间" class=btn>
									</TD>
								</TR>
								<TR BGCOLOR=#ffffff>
									<TD align=right class=column>
										是否启用
									</TD>
									<TD colspan="3">
										&nbsp;&nbsp;启用
										<INPUT name="active" type="radio" value="1">
										&nbsp;&nbsp;&nbsp;&nbsp;停止
										<INPUT name="active" type="radio" value="0" checked>
									</TD>
								</TR>

								<TR bgcolor=#ffffff>
									<TD align=right class=column>
										任务描述
									</TD>
									<TD colspan="3">
										&nbsp;
										<TEXTAREA name="plan_desc" cols="65"></TEXTAREA>
									</TD>
								</TR>

								<TR height="23">
									<TD class=green_foot colspan=4>
										<div align="right">
											<input name="submit1" type=submit class=btn value=" 保 存 ">
											<INPUT TYPE="hidden" name="action" value="add">
											&nbsp;&nbsp;&nbsp;
											<input name="submit2" type=reset class=btn value=" 清 空 ">
											&nbsp;&nbsp;&nbsp;
											<input name="close" type=button class=btn value="浏览配置"
												onclick="javascript:window.navigate('./SheetExeRuleList.jsp');">
										</div>
									</TD>
								</TR>
							</table>
						</TD>
					</TR>
				</TABLE>
				<br>
			</TD>
		</TR>
	</TABLE>
	<iframe id="childFrm" name="childFrm" style="display: none"></iframe>

</form>
<%@ include file="../foot.jsp"%>