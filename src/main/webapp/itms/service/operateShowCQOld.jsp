<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>业务查询</title>
<%
	/**
	 * e8-c业务查询
	 * 
	 * @author qixueqi(4174)
	 * @version 1.0
	 * @since 2010-09-08
	 * @category
	 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<%@ include file="../../head.jsp"%>
<script type="text/javascript">
function send(){
	var content = $("#contentTxt").val();
	var bss_sheet_id = $("#bss_sheet_id").val();
	var isHistory = $("#isHistory").val();
	/* $.ajax({
		type:"POST",
		url:"<s:url value='/itms/service/operateByHistoryCQOldQuery!send.action'/>";
		data:{"content":content},
		success:function(data){
			alert("工单返回:"+data);
		},
		error:function(xmlR,msg,other){alert("保存时异常,请与管理员联系");}
	});	  */
	
	var strategyurl = "<s:url value='/itms/service/operateByHistoryCQOldQuery!send.action'/>";

	$.post(strategyurl,{
		content:content,
		bss_sheet_id:bss_sheet_id,
		isHistory:isHistory
	},function(ajax){
		alert("工单返回:"+ajax);
	});
}
</script>
</head>
<body>
<table class="querytable">
		<TR>
			<th colspan="2">工单详细内容<input type="text" id="isHistory" value="<s:property value="operateMap['isHistory']"/>"></input>
			<input type="text" id="bss_sheet_id" value="<s:property value="operateMap['bss_sheet_id']"/>"></input>
			</th>
		</TR>
		<tr>
			<td class=column width="15%" align='right'>工单流水号</td>
			<td width="85%" id="bss_sheet_id"><s:property value="operateMap['bss_sheet_id']" /> </td>
		</tr>
		<%-- <tr>
			<td class=column width="15%" align='right'>业务类型</td>
			<td width="85%"><s:property value="operateMap['servType']" /></td>
		</tr>
		<tr>
			<td class=column width="15%" align='right'>结果详细描述</td>
			<td width="85%"><s:property value="operateMap['resultId']" /></td>
		</tr>
		<tr>
			<td class=column width="15%" align='right'>错误原因</td>
			<td width="85%"><s:property value="operateMap['returnt_context']" /></td>
		</tr> --%>
		<tr>
			<td class=column width="15%" align='right'>工单详情</td>
			<td width="85%" height="300"><textarea id="contentTxt" style="width:400 ;height: 300"><s:property value="operateMap['sheet_context']" /></textarea></td>
		</tr>
		<TR>
		<td  width="50%">
								<button onclick="javascript:send();" >&nbsp;发&nbsp;&nbsp;送&nbsp;</button>
				</td>
				<td  width="50%">
								<button onclick="javascript:window.close();" >&nbsp;关&nbsp;&nbsp;闭&nbsp;</button>
				</td>
		</TR>
</table>
</body>
<%@ include file="../../foot.jsp"%>