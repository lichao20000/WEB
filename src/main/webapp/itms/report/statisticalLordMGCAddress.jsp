<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>����MGCͳ��</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
	function query() {
		$("tr[@id='trData']").show();
		$("button[@name='button']").attr("disabled", true);
		var cityId = $.trim($("select[@name='city_id']").val());
		$("input[@name='cityId']").val(cityId);
		var url = "<s:url value='/itms/report/statisticalLordMGCAddressQuery!countMoreBroadbandBusiness.action' />";
		$.post(url, {
			city_id : cityId
		}, function(ajax) {
			$("div[@id='QueryData']").html("");
			$("div[@id='QueryData']").append(ajax);
			$("button[@name='button']").attr("disabled", false);
		});
	}
</script>
</head>

<body>
	<form id="form" name="selectForm" action="">
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								����MGCͳ��</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">
						<TR>
							<th colspan="4">����MGCͳ��</th>
						</tr>
						<TR>
							<TD class=column width="15%" align='right'>����</TD>
							<TD width="35%"><s:select list="cityList" name="city_id"
									headerKey="00" headerValue="��ѡ������" listKey="city_id"
									listValue="city_name" cssClass="bk"></s:select></TD>
							<TD class="column" align="right" width="15%"></TD>
							<TD width="35%"></TD>
						</TR>
						<TR>
							<td colspan="4" align="right" class=foot>
								<button onclick="query()" id="button" name="button">&nbsp;ͳ&nbsp;��&nbsp;</button>
								<input type="hidden" name="cityId" id="cityId" value="00" />
							</td>
						</TR>
					</table>
				</td>
			</tr>
			<tr>
				<td height="25" id="resultStr"></td>
			</tr>
			<tr id="trData" style="display: none">
				<td class="colum">
					<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
						����ͳ�ƣ����Ե�....</div>
				</td>
			</tr>
			<tr>
				<td height="25"></td>
			</tr>
			<tr>
				<td id="bssSheetInfo"></td>
			</tr>
		</table>
		<br>
	</form>
</body>
</html>
<%@ include file="../../foot.jsp"%>