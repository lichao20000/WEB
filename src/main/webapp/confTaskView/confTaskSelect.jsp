<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>配置任务视图</title>
<%
	/**
		 * 配置任务视图界面
		 * 
		 * @author qixueqi(4174)
		 * @version 1.0
		 * @since 2008-12-18
		 * @category
		 */
%>
<link href="<s:url value="../css/css_green.css"/>" rel="stylesheet" type="text/css">
<lk:res/>
<script type="text/javascript" src="<s:url value='/Js/jquery.js'/>"></script>
<script type="text/javascript">

function queryCustomer(){

	//parent.frames.dataForm.selectList.action = "<s:url value="/confTaskView/ConfTaskView.action"/>";
	document.selectForm.submit();
}

</script>
<style type="text/css">
<!--
select {
	position:relative;
	font-size:12px;
	width:160px;
	line-height:18px;border:0px;
	color:#909993;
}
-->
</style>
</head>

<body>
<form name="selectForm" action="<s:url value="/confTaskView/ConfTaskView.action"/>" target="dataForm">
	<table border=0 cellspacing=0 cellpadding=0 width="98%" align="center" height="35%">
		<tr><td HEIGHT=20>&nbsp;</td></tr>
		<tr>
			<td>
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							配置任务查询
						</td>
						<td>
							<img src="../images/attention_2.gif" width="15" height="12">		
						</td>
						<td align="right">
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center"
					bgcolor="#999999">
					<tr><th colspan="4">查询</th></tr>
					<tr bgcolor="#FFFFFF">
						<td class=column width="15%" align='right'>定制起始时间</td>
						<td width="35%">
							<lk:date id="order_time_start" name="order_time_start" type="day" />
						</td>
						<td class=column width="15%" align='right'>定制截止时间</td>
						<td width="35%">
							<lk:date id="order_time_end" name="order_time_end" type="day" />
						</td>
                   	</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column width="15%" align='right'>审核状态</td>
						<td  colspan="1">
							<select name="is_check" class="bk">
								<option value="">==请选择==</option>
								<option value="0">==未审核==</option>
								<option value="1">==通过==</option>
								<option value="2">==不通过==</option>
							</select>
						</td>
						<td class=column width="15%" align='right'>任务状态</td>
						<td width="35%" colspan="1">
							<select name="is_over" class="bk">
								<option value="">==请选择==</option>
								<option value="0">==尚未执行==</option>
								<option value="1">==正在执行==</option>
								<option value="2">==执行完成==</option>
								<option value="-1">==撤销==</option>
							</select>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column width="15%" align='right'>任务名称</td>
						<td >
							<input type="input" name="task_name" size="30">
						</td>
						<td colspan="2" align="center">
							
						</td>
					</tr>
					<TR>
						<TD class='green_foot' colspan='4' align='right'>
						<input type="button" value=" 查  询 " onclick="queryCustomer()">&nbsp;&nbsp;
						<INPUT TYPE="reset" VALUE=" 取 消 ">
						</TD>
					</TR>
				</table>
			</td>
		</tr>
	</table>
	<br>
	<iframe id="dataForm" name="dataForm" height="65%" frameborder="0" scrolling="no" width="100%" src="<s:url value='/confTaskView/ConfTaskView.action'/>"></iframe>
</form>
</body>
</html>