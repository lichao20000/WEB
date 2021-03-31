<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import ="com.linkage.litms.common.util.DateTimeUtil"%>

<%
request.setCharacterEncoding("GBK");

String start_time = new DateTimeUtil().getDate();
String end_time = new DateTimeUtil().getDate();

%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function CheckForm(){
	var obj = document.frm;
	if(obj.start_time.value == "" || obj.end_time.value == "" || obj.start_ms.value=="" || obj.end_ms.value==""){
		alert("请选择查询时间!");
		return false;
	}
}
//-->
</SCRIPT>
<form name="frm" action="sysplan_logdata.jsp" method="post" target="childFrm"  onSubmit="return CheckForm()">
<table width="95%" border="0" cellspacing="0" cellpadding="0" align=center>
  <tr><td HEIGHT=20>&nbsp;&nbsp;</td></tr>
  <TR><TD>
		<table width="95%" align=center  height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
			<tr>
				<td width="162" align="center" class="title_bigwhite">
					系统维护历史查询
				</td>
			</tr>
		</table>
	</TD></TR>
	<TR><TD>
		<TABLE border=0 cellspacing=1 cellpadding=2 width="95%" align=center valign=middle bgcolor="#999999">
			<tr bgcolor="#FFFFFF">
					<td nowrap class=column align="right">
						起始时间
					</td>
					<td nowrap>
						<input type="text" name="start_time" id="start_time" value="<%=start_time%>" class=bk class="form_kuang" readOnly>
						<input type="button" value="▼" class=btn onClick="showCalendar('day',event)" name="button2">
						<input type="text" name="start_ms" id="start_ms" class=bk value="00:00:00" size="10">
					</td>
					<td class=column align="right">
						结束时间
					</td>
					<td nowrap>
						<input type="text" name="end_time" id="end_time"  value="<%=end_time%>" class=bk readOnly class="form_kuang">
						<input type="button" value="▼" class=btn onClick="showCalendar('day',event)" name="button2">
						<input type="text" name="end_ms" id="end_ms" class=bk  value="23:59:59" size="10">
					</td>
			</tr>	
			<tr bgcolor="#FFFFFF">
					<td nowrap class=column align="right">
						维护项目
					</td>
					<td colspan="3">
						  <SELECT name="sys_item" class="bk">
							<option value="-1">==请选择==</option>
							<option value="0">WEB服务器维护</option>
							<option value="1">ACS维护</option>
							<option value="2">数据库维护</option>
						  </SELECT>
					</td>
			</tr>
			<tr bgcolor="#FFFFFF" class=green_foot>
				<td nowrap align=right colspan=4>
					<input type=submit value=" 查 询 ">
				</td>
			</tr>
		</TABLE>
	
	</TD></TR>
	<TR><TD height="20">&nbsp;
</TD></TR>
	<TR><TD>
			<div id=divdata ></div>
  </TD></TR>
</TABLE>

<BR>

<iframe id="childFrm" name="childFrm" style="display:none"></iframe>
<%@ include file="../foot.jsp"%>