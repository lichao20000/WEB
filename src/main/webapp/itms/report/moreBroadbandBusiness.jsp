<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>多宽带业务统计</title>
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
		var startOpenDate = $.trim($("input[@name='startOpenDate']").val());
		var endOpenDate = $.trim($("input[@name='endOpenDate']").val());
		if ("" == startOpenDate) {
			alert("请选择BSS受理开始时间");
			return;
		}
		if ("" == endOpenDate) {
			alert("请选择BSS受理结束时间");
			return;
		}
		$("tr[@id='trData']").show();
		$("button[@name='button']").attr("disabled", true);
		var cityId = $.trim($("select[@name='city_id']").val());
		$("input[@name='cityId']").val(cityId);
		var url = "<s:url value='/itms/report/moreBroadbandBusinessQuery!countMoreBroadbandBusiness.action' />";
		var userType = $.trim($("select[@name='userType']").val()); 
		var user = $.trim($("select[@name='user']").val()); 
		$.post(url, {
			city_id : cityId,
			startOpenDate : startOpenDate,
			endOpenDate : endOpenDate,
			user : user,
			userType : userType
		}, function(ajax) {
			$("div[@id='QueryData']").html("");
			$("div[@id='QueryData']").append(ajax);
			$("button[@name='button']").attr("disabled", false);
		});
	}

	function countBycityId(city_id, userType, user) {
		$("tr[@id='trData']").show();
		$("div[@id='QueryData']").html("正在统计，请稍等....");
		var startOpenDate = $.trim($("input[@name='startOpenDate']").val());
		var endOpenDate = $.trim($("input[@name='endOpenDate']").val());
		var url = "<s:url value='/itms/report/moreBroadbandBusinessQuery!countMoreBroadbandBusiness.action'/>";
		$.post(url, {
			city_id : city_id,
			startOpenDate : startOpenDate,
			endOpenDate : endOpenDate,
			user : user,
			userType : userType
		}, function(ajax) {
			$("div[@id='QueryData']").html("");
			$("div[@id='QueryData']").append(ajax);
		});
	}

	function openDevForWbdTerminal(city_id, userType, user) {
		var startOpenDate = $.trim($("input[@name='startOpenDate']").val());
		var endOpenDate = $.trim($("input[@name='endOpenDate']").val());
		var page = "<s:url value='/itms/report/moreBroadbandBusinessQuery!getDevListForWbdTerminal.action'/>?"
				+ "city_id="
				+ city_id
				+ "&startOpenDate="
				+ startOpenDate
				+ "&endOpenDate="
				+ endOpenDate
				+ "&userType="
				+ userType
				+ "&user=" + user;
		window
				.open(
						page,
						"",
						"left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
	}

	function ToExcel(city_id, userType, user) {
		var startOpenDate = $.trim($("input[@name='startOpenDate']").val());
		var endOpenDate = $.trim($("input[@name='endOpenDate']").val());
		var page = "<s:url value='/itms/report/moreBroadbandBusinessQuery!getExcel.action'/>?"
				+ "city_id="
				+ city_id
				+ "&startOpenDate="
				+ startOpenDate
				+ "&endOpenDate="
				+ endOpenDate
				+ "&userType="
				+ userType
				+ "&user=" + user;
		document.all("childFrm").src = page;
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
								多宽带业务统计</td>
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
							<th colspan="4">多宽带业务统计</th>
						</tr>
						<TR>
							<TD class="column" align="right" width="15%">客户类型</TD>
							<TD width="35%"><SELECT name="user" class=bk>
									<option value="">==请选择==</option>
									<option value="0">政企客户</option>
									<option value="2">家庭客户</option>
							</SELECT></TD>
							<TD class=column width="15%" align='right'>属地</TD>
							<TD width="35%"><s:select list="cityList" name="city_id"
									headerKey="-1" headerValue="请选择属地" listKey="city_id"
									listValue="city_name" cssClass="bk"></s:select></TD>
						</TR>
						<TR>
							<TD class=column width="15%" align='right'>BSS受理开始时间</TD>
							<TD width="35%"><input type="text" name="startOpenDate"
								readonly class=bk value="<s:property value="startOpenDate" />">
								<img name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择"></TD>
							<TD class=column width="15%" align='right'>BSS受理结束时间</TD>
							<TD width="35%"><input type="text" name="endOpenDate"
								readonly class=bk value="<s:property value="endOpenDate" />">
								<img name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择"></TD>
						</TR>
						<TR>
							<TD class="column" align="right" width="15%">用户终端规格</TD>
							<TD width="35%"><s:select list="userTypeMap" name="userType"
									headerKey="" headerValue="==请选择==" listKey="key"
									listValue="value" cssClass="bk"></s:select></TD>
							<TD class=column width="15%" align='right'></TD>
							<TD width="35%"></TD>
						</TR>
						<TR>
							<td colspan="4" align="right" class=foot>
								<button onclick="query()" id="button" name="button">&nbsp;统&nbsp;计&nbsp;</button>
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