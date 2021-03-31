<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>终端管理率报表</title>
<%
	/**
	 * 月终端管理率报表
	 * 
	 * @author gaoyi
	 * @version 1.0
	 * @since 2013-07-04
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
		$("tr[@id='trData']").show();
		$("div[@id='QueryData']").html("正在统计，请稍等....");
		$("button[@name='button']").attr("disabled", true);
		var cityId = $.trim($("select[@name='city_id']").val());
		$("input[@name='cityId']").val(cityId);
		var url = "<s:url value='/itms/report/mothTerminalOrder!countMothTerminalOrder.action' />";
		var isMonth = $("input[name='isMonth']").val();
		if (isMonth == "false") {
			var startOpenDate0 = $.trim($("input[@name='startOpenDate0']")
					.val());
			var endOpenDate0 = $.trim($("input[@name='endOpenDate0']").val());
			$.post(url, {
				city_id : cityId,
				startOpenDate0 : startOpenDate0,
				endOpenDate0 : endOpenDate0
			}, function(ajax) {
				$("div[@id='QueryData']").html("");
				$("div[@id='QueryData']").append(ajax);
				$("button[@name='button']").attr("disabled", false);
			});
		} else {
			var endtime = $.trim($("input[@name='endOpenDate']").val());
			$.post(url, {
				city_id : cityId,
				endOpenDate : endtime
			}, function(ajax) {
				$("div[@id='QueryData']").html("");
				$("div[@id='QueryData']").append(ajax);
				$("button[@name='button']").attr("disabled", false);
			});
		}
	}

	function countBycityId(city_id) {
		$("input[@name='cityId']").val(city_id);
		$("tr[@id='trData']").show();
		$("div[@id='QueryData']").html("正在统计，请稍等....");
		var url = "<s:url value='/itms/report/mothTerminalOrder!countMothTerminalOrder.action' />";
		var isMonth = $("input[name='isMonth']").val();
		if (isMonth == "false") {
			var startOpenDate0 = $.trim($("input[@name='startOpenDate0']")
					.val());
			var endOpenDate0 = $.trim($("input[@name='endOpenDate0']").val());
			$.post(url, {
				city_id : city_id,
				startOpenDate0 : startOpenDate0,
				endOpenDate0 : endOpenDate0
			}, function(ajax) {
				$("div[@id='QueryData']").html("");
				$("div[@id='QueryData']").append(ajax);
			});
		} else {
			var endtime = $("input[@name='endOpenDate']").val();
			$.post(url, {
				city_id : city_id,
				endOpenDate : endtime
			}, function(ajax) {
				$("div[@id='QueryData']").html("");
				$("div[@id='QueryData']").append(ajax);
			});
		}
	}

	function openDevForWbdTerminal(city_id, flag) {
		var isMonth = $("input[name='isMonth']").val();
		if (isMonth == "false") {
			var startOpenDate0 = $.trim($("input[@name='startOpenDate0']")
					.val());
			var endOpenDate0 = $.trim($("input[@name='endOpenDate0']").val());
			var page = "<s:url value='/itms/report/mothTerminalOrder!getDevListForWbdTerminal.action'/>?"
					+ "city_id="
					+ city_id
					+ "&startOpenDate0="
					+ startOpenDate0
					+ "&endOpenDate0="
					+ endOpenDate0
					+ "&flag=" + flag;
		} else {
			var endtime = $("input[@name='endOpenDate']").val();
			var page = "<s:url value='/itms/report/mothTerminalOrder!getDevListForWbdTerminal.action'/>?"
					+ "city_id="
					+ city_id
					+ "&endOpenDate="
					+ endtime
					+ "&flag=" + flag;
		}
		window
				.open(
						page,
						"",
						"left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");

	}

	function ToExcel() {
		var mainForm = document.getElementById("selectForm");
		mainForm.action = "<s:url value='/itms/report/mothTerminalOrder!countMothTerminalOrderExcel.action'/>";
		mainForm.submit();
		mainForm.action = "<s:url value='/itms/report/mothTerminalOrder!countMothTerminalOrder.action'/>";
	}

	function randomDate() {
		var myDate = new Date();
		var y = myDate.getFullYear();
		var m = myDate.getMonth() + 1;
		var d = myDate.getDate();
		if (d < 4) {
			m = m - 2;
		} else {
			m = m - 1;
		}
		return y + "-" + m;
	}
</script>
</head>

<body>
	<form id="form" name="selectForm"
		action="<s:url value='/itms/report/mothTerminalOrder!countMothTerminalOrder.action'/>">
		<input type="hidden" name="isMonth"
			value='<s:property value="isMonth" />' />
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">终端管理率报表</td>
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
							<th colspan="4"><s:if test='isMonth == "false"'>日</s:if><s:else>月</s:else>终端管理率报表统计</th>
						</tr>
						<s:if test='isMonth == "false"'>
							<TR>
								<TD class=column width="15%" align='right'>开始时间</TD>
								<TD width="35%"><input type="text" name="startOpenDate0"
									readonly class=bk value="<s:property value="startOpenDate0" />">
									<img name="shortDateimg"
									onClick="WdatePicker({el:document.selectForm.startOpenDate0,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
									src="../../images/dateButton.png" width="15" height="12"
									border="0" alt="选择"></TD>
								<TD class=column width="15%" align='right'>结束时间</TD>
								<TD width="35%"><input type="text" name="endOpenDate0"
									readonly class=bk value="<s:property value="endOpenDate0" />">
									<img name="shortDateimg"
									onClick="WdatePicker({el:document.selectForm.endOpenDate0,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
									src="../../images/dateButton.png" width="15" height="12"
									border="0" alt="选择"></TD>
							</TR>
							<TR>
								<TD class=column width="15%" align='right'>属地</TD>
								<TD width="35%"><s:select list="cityList" name="city_id"
										headerKey="-1" headerValue="请选择属地" listKey="city_id"
										listValue="city_name" cssClass="bk"></s:select></TD>
							</TR>
						</s:if>
						<s:else>
							<TR>
								<TD class=column width="15%" align='right'>开始时间</TD>
								<TD width="35%"><input type="text" name="endOpenDate"
									readonly class=bk value="<s:property value="endOpenDate" />">
									<img name="shortDateimg"
									onClick="var date=randomDate();WdatePicker({el:document.selectForm.endOpenDate,dateFmt:'yyyy-MM',skin:'whyGreen',readOnly:true,maxDate:date})"
									src="../../images/dateButton.png" width="15" height="12"
									border="0" alt="选择"></TD>
								<TD class=column width="15%" align='right'>属地</TD>
								<TD width="35%"><s:select list="cityList" name="city_id"
										headerKey="-1" headerValue="请选择属地" listKey="city_id"
										listValue="city_name" cssClass="bk"></s:select></TD>
							</TR>
						</s:else>
						<TR>
							<td colspan="4" align="right" class=foot>
								<button onclick="query()" id="button" name="button">&nbsp;查&nbsp;询&nbsp;</button>
								<input type="hidden" name="cityId" id="cityId" value="00" />
							</td>
						</TR>
					</table>
				</td>
			</tr>
			<tr>
				<td height="25" id="resultStr"></td>
			</tr>
			<!-- 
			<tr>
				<td>
				<iframe id="dataForm" name="dataForm" height="0"
						frameborder="0" scrolling="no" width="100%" src=""></iframe></td>
			</tr>
			 -->
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