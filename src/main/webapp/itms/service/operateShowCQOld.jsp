<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ҵ���ѯ</title>
<%
	/**
	 * e8-cҵ���ѯ
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
			alert("��������:"+data);
		},
		error:function(xmlR,msg,other){alert("����ʱ�쳣,�������Ա��ϵ");}
	});	  */
	
	var strategyurl = "<s:url value='/itms/service/operateByHistoryCQOldQuery!send.action'/>";

	$.post(strategyurl,{
		content:content,
		bss_sheet_id:bss_sheet_id,
		isHistory:isHistory
	},function(ajax){
		alert("��������:"+ajax);
	});
}
</script>
</head>
<body>
<table class="querytable">
		<TR>
			<th colspan="2">������ϸ����<input type="text" id="isHistory" value="<s:property value="operateMap['isHistory']"/>"></input>
			<input type="text" id="bss_sheet_id" value="<s:property value="operateMap['bss_sheet_id']"/>"></input>
			</th>
		</TR>
		<tr>
			<td class=column width="15%" align='right'>������ˮ��</td>
			<td width="85%" id="bss_sheet_id"><s:property value="operateMap['bss_sheet_id']" /> </td>
		</tr>
		<%-- <tr>
			<td class=column width="15%" align='right'>ҵ������</td>
			<td width="85%"><s:property value="operateMap['servType']" /></td>
		</tr>
		<tr>
			<td class=column width="15%" align='right'>�����ϸ����</td>
			<td width="85%"><s:property value="operateMap['resultId']" /></td>
		</tr>
		<tr>
			<td class=column width="15%" align='right'>����ԭ��</td>
			<td width="85%"><s:property value="operateMap['returnt_context']" /></td>
		</tr> --%>
		<tr>
			<td class=column width="15%" align='right'>��������</td>
			<td width="85%" height="300"><textarea id="contentTxt" style="width:400 ;height: 300"><s:property value="operateMap['sheet_context']" /></textarea></td>
		</tr>
		<TR>
		<td  width="50%">
								<button onclick="javascript:send();" >&nbsp;��&nbsp;&nbsp;��&nbsp;</button>
				</td>
				<td  width="50%">
								<button onclick="javascript:window.close();" >&nbsp;��&nbsp;&nbsp;��&nbsp;</button>
				</td>
		</TR>
</table>
</body>
<%@ include file="../../foot.jsp"%>