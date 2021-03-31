<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import ="com.linkage.litms.common.util.DateTimeUtil"%>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<%
request.setCharacterEncoding("GBK");

String start_time = new DateTimeUtil().getDate();
String end_time = new DateTimeUtil().getDate();

%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function delplan(plan_id){
	if(confirm("真的要删除该计划策略吗？\n本操作所删除的不能恢复！！！")){
		document.all("childFrm").src="sys_planSave.jsp?plan_id=" + plan_id + "&action=delete";
		return true;
	}else{
		return false;
	}		
}
function reload(){
	this.location ="sysplan_list.jsp";
}
//-->
</SCRIPT>
<form name="frm" action="sysplan_listdata.jsp" target="childFrm" method="post">
<table width="95%" border="0" cellspacing="0" cellpadding="0" align=center>
  <tr><td HEIGHT=20>&nbsp;&nbsp;</td></tr>
  <TR><TD>
		<table width="95%" align=center  height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
			<tr>
				<td width="162" align="center" class="title_bigwhite">
					维护计划策略查询
				</td>
			</tr>
		</table>
	</TD></TR>
	<TR><TD>
		<TABLE border=0 cellspacing=1 cellpadding=2 width="95%" align=center valign=middle bgcolor="#999999">
			<tr bgcolor="#FFFFFF">
				<TD nowrap class=column align="center">计划制定人</TD>
				<TD><INPUT name="acc_loginname" type="text" class="bk" 
					value="" size="25">
				</TD>
				<TD>是否启用</TD>
				<TD>
				      <INPUT name="active" type="radio" value="1">启用&nbsp;&nbsp;&nbsp;&nbsp;
					  <INPUT name="active" type="radio" value="0" checked>停止
				</TD>
			</tr>	
			<tr bgcolor="#FFFFFF">
				<td nowrap class=column align="center">
					维护项目
				</td>
				<td>
					  <SELECT name="sys_item" class="bk">
						<option value="-1">==请选择==</option>
						<option value="0">WEB服务器维护</option>
						<option value="1">ACS维护</option>
						<option value="2">数据库维护</option>
					  </SELECT>
				</td>
				<td nowrap class=column>
					是否审核
				</td>
				<td>
					<INPUT name="is_check" type="radio" value="1">审核&nbsp;&nbsp;&nbsp;&nbsp;
					<INPUT name="is_check" type="radio" value="0" checked>未审核
				</td>
			</tr>	
			<tr bgcolor="#FFFFFF" class=green_foot>
				<td nowrap align=right colspan=4>
					<input type="submit" value=" 查 询 ">
				</td>
			</tr>
		</TABLE>
	
	</TD></TR>
	<TR><TD height="20">&nbsp;
</TD></TR>
	<TR><TD>
			<div id="div_sysplan"></div>
  </TD></TR>
</TABLE>

<BR>

<iframe id="childFrm" name="childFrm" style="display:none"></iframe>
<%@ include file="../foot.jsp"%>