<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>用户数统计报表</title>
<%
	/**
	 * 用户数统计报表
	 * 
	 * @author fangchao
	 * @version 1.0
	 * @since 2013-07-18
	 * @category
	 */
%>
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
		var cityId = $.trim($("select[@name='cityId']").val());
		if (cityId == "-1") {
			alert("请选择属地");
			return false;
		}
		queryByCityId(cityId);
	}

	function queryByCityId(cityId) {
		$("tr[@id='trData']").show();
		$("div[@id='QueryData']").html("正在统计，请稍等....");
		$("button[@name='button']").attr("disabled", true);
		var startTime = $.trim($("input[@name='startTime']").val());
		var endTime = $.trim($("input[@name='endTime']").val());
		var url = "<s:url value='/itms/report/userDeviceReport!queryUserDevice.action' />";
		$.post(url, {
			cityId : cityId,
			startTime : startTime,
			endTime : endTime
		}, function(ajax) {
			$("div[@id='QueryData']").html("");
			$("div[@id='QueryData']").append(ajax);
			$("button[@name='button']").attr("disabled", false);
		});
	}

	function ToExcel() {
		var mainForm = document.getElementById("selectForm");
		mainForm.action = "<s:url value='/itms/report/userDeviceReport!exportUserDevice.action'/>";
		mainForm.submit();
	}
</script>
</head>

<body>
	<form id="form" name="selectForm"
		action="<s:url value='/itms/report/mothTerminalOrder!countMothTerminalOrder.action'/>">
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								用户数统计报表</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">
						<tr>
							<th colspan="4">用户数统计报表</th>
						</tr>
						<tr>
							<td class=column width="15%" align='right'>开始时间</td>
							<td width="35%"><input type="text" name="startTime" readonly
								class=bk value="<s:property value="startTime" />"> <img
								name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.startTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen',readOnly:true})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择"></td>
							<td class=column width="15%" align='right'>结束时间</td>
							<td width="35%"><input type="text" name="endTime" readonly
								class=bk value="<s:property value="endTime" />"> <img
								name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.endTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen',readOnly:true})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择"></td>
						</tr>
						<tr>
							<td class=column width="15%" align='right'>属地</td>
							<td width="35%"><s:select list="cityList" name="cityId"
									headerKey="-1" headerValue="请选择属地" listKey="city_id"
									listValue="city_name" cssClass="bk"></s:select></td>
							<td class=column width="15%" align='right'></td>
							<td width="35%"></td>
						</tr>
						<tr>
							<td colspan="4" align="right" class=foot>
								<button onclick="query()" id="button" name="button">&nbsp;查&nbsp;询&nbsp;</button>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td height="25" id="resultStr"></td>
			</tr>
			<tr id="trData" style="display: none">
				<td class="colum">
					<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
						正在统计，请稍等....</div>
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