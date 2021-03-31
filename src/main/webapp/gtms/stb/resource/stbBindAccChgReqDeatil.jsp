<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>机顶盒工单信息</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>

<style>
span
{
	position:static;
	border:0;
}
</style>
</head>

<body>
<table width="100%" class="querytable" align="center">
	<tr bgcolor="#FFFFFF"><td colspan="2" class="title_1" >机顶盒工单信息</td></tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="10%">工单ID</td>
		<td>
			<s:property value='eserverInfo.cmd_id'/>
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="10%">工单时间</td>
		<td>
			<s:property value='eserverInfo.add_time'/>
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="10%">业务账号</td>
		<td>
			<s:property value='eserverInfo.serv_account'/>
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="10%">9级用户地址</td>
		<td>
			<s:property value='eserverInfo.acc_address' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="10%">MAC地址</td>
		<td>
			<s:property value='eserverInfo.mac'/>
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="10%">网格</td>
		<td>
			<s:property value='eserverInfo.grid' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="10%">工单来源</td>
		<td align="left">
			<s:property value='eserverInfo.from_id' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
        <td align="right" class="column" width="10%">工单状态</td>
        <td align="left">
            <s:property value='eserverInfo.status_desc' />
        </td>
    </tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="10%">操作员</td>
		<td>
			<s:property value='eserverInfo.opertor' />
		</td>	
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="10%">操作记录</td>
		<td id = "operLog">
            <s:property value='eserverInfo.oper_log' />
		</td>
	</tr>
	<TR>
		<TD colspan="2" class=foot>
			<div align="center">
				<button onclick="javascript:window.close();"> 关 闭 </button>
			</div>
		</TD>
	</TR>
</table>
</body>

<SCRIPT LANGUAGE="JavaScript">

function dealOperLog()
{
   var operlog = $("#operLog")[0].innerText;
   $("#operLog")[0].innerHTML = operlog;
}

dealOperLog();

</SCRIPT>