<%@page language="java" contentType="application/x-msdownload" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>白名单查询</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
	function query() 
	{
		$("tr[@id='trData']").show();
		$("div[@id='QueryData']").html("正在统计，请稍等....");
		$("button[@name='button']").attr("disabled", true);
		var sn = $.trim($("input[@name='sn']").val());
		var url = "<s:url value='/gtms/resource/deviceInit!query.action' />";
		$.post(url, {
			sn:sn
		}, function(ajax) {
			$("div[@id='QueryData']").html("");
			$("div[@id='QueryData']").append(ajax);
			$("button[@name='button']").attr("disabled", false);
		});
	}
</script>
</head>

<body>
<table>
	<tr>
		<td HEIGHT=20>&nbsp;</td>
	</tr>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">白名单查询</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table class="querytable">
				<TR>
					<th colspan="3">白名单查询</th>
				</tr>
				<TR>
					<td class=column width="15%" align='right'>设备序列号</td>
					<td width="35%">
						<input type="text" name="sn" class=bk value="">
					</td>
					<td align="right" class=foot>
						<button onclick="query()" id="button" name="button"> 查 询 </button>
					</td>
				</TR>
				<tr id="trData" style="display: none">
					<td class=column width="15%" align='right'>查询结果</td>
					<td colspan="2" class="colum">
						<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
							正在查询，请稍等....
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<br>
</body>
</html>
<%@ include file="../../foot.jsp"%>