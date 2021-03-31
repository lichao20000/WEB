<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>预置设备导入数据分析</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
	function change_select() {
		$("select[@name='modelId']").attr('disabled', false);
		$("select[@name='modelId']").html("");
		var vendorId = $("select[@name='vendorId']").val();
		if ("-1" == vendorId) {
			$("select[@name='modelId']").html(
					"<option value='-1'>==请先选择厂商==</option>");
			$("select[@name='modelId']").attr('disabled', true);
			return false;
		}
		var url = "<s:url value='/itms/report/presetEquipmentAnalysisQuery!getDeviceModel.action'/>";
		$.post(url, {
			vendorId : vendorId
		}, function(ajax) {
			parseMessage(ajax, $("select[@name='modelId']"));
		});
	}

	//解析查询设备型号返回值的方法
	function parseMessage(ajax, field) {
		var flag = true;
		if ("" == ajax) {
			return;
		}
		var lineData = ajax.split("#");
		if (!typeof (lineData) || !typeof (lineData.length)) {
			return false;
		}
		field.html("");
		option = "<option value='-1' selected>==请选择==</option>";
		field.append(option);
		for ( var i = 0; i < lineData.length; i++) {
			var oneElement = lineData[i].split("$");
			var xValue = oneElement[0];
			var xText = oneElement[1];
			option = "<option value='"+xValue+"' selected>==" + xText
					+ "==</option>";
			try {
				field.append(option);
			} catch (e) {
				alert("设备型号检索失败！");
			}
		}
		if (flag) {
			field.attr("value", "-1");
		}
	}

	function query() {
		$("tr[@id='trData']").show();
		$("button[@name='button']").attr("disabled", true);
		var cityId = $.trim($("select[@name='city_id']").val());
		$("input[@name='cityId']").val(cityId);
		var url = "<s:url value='/itms/report/presetEquipmentAnalysisQuery!countPresetEquipmentAnalysis.action' />";
		var startOpenDate = $.trim($("input[@name='startOpenDate']").val());
		var endOpenDate = $.trim($("input[@name='endOpenDate']").val());
		var vendorId = $("select[@name='vendorId']").val();
		var modelId = $("select[@name='modelId']").val();
		$.post(url, {
			city_id : cityId,
			startOpenDate : startOpenDate,
			endOpenDate : endOpenDate,
			vendorId : vendorId,
			modelId : modelId
		}, function(ajax) {
			$("div[@id='QueryData']").html("");
			$("div[@id='QueryData']").append(ajax);
			$("button[@name='button']").attr("disabled", false);
		});
	}

	function openDevForWbdTerminal(status, vendorId, modelId) {
		var startOpenDate = $.trim($("input[@name='startOpenDate']").val());
		var endOpenDate = $.trim($("input[@name='endOpenDate']").val());
		var page = "<s:url value='/itms/report/presetEquipmentAnalysisQuery!getDevListForWbdTerminal.action'/>?"
				+ "status="
				+ status
				+ "&startOpenDate="
				+ startOpenDate
				+ "&endOpenDate="
				+ endOpenDate
				+ "&vendorId="
				+ vendorId
				+ "&modelId=" + modelId;
		window
				.open(
						page,
						"",
						"left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
	}

	function ToExcel() {
		var cityId = $.trim($("select[@name='city_id']").val());
		$("input[@name='cityId']").val(cityId);
		var startOpenDate = $.trim($("input[@name='startOpenDate']").val());
		var endOpenDate = $.trim($("input[@name='endOpenDate']").val());
		var vendorId = $("select[@name='vendorId']").val();
		var modelId = $("select[@name='modelId']").val();
		var page = "<s:url value='/itms/report/presetEquipmentAnalysisQuery!getExcel.action'/>?"
				+ "&startOpenDate="
				+ startOpenDate
				+ "&endOpenDate="
				+ endOpenDate
				+ "&vendorId="
				+ vendorId
				+ "&modelId="
				+ modelId
				+ "&city_id=" + cityId;
		document.all("childFrm").src = page;
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
								预置设备导入数据分析</td>
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
							<th colspan="4">预置设备导入数据分析</th>
						</tr>
						<TR>
							<TD class=column width="15%" align='right'>开始时间</TD>
							<TD width="35%"><input type="text" name="startOpenDate"
								readonly class=bk value="<s:property value="startOpenDate" />">
								<img name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择"></TD>
							<TD class=column width="15%" align='right'>结束时间</TD>
							<TD width="35%"><input type="text" name="endOpenDate"
								readonly class=bk value="<s:property value="endOpenDate" />">
								<img name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择"></TD>
						</TR>
						<TR>
							<TD class=column width="15%" align='right'>厂商</TD>
							<TD width="35%"><s:select list="vendorMap" name="vendorId"
									onchange="change_select()" headerKey="-1" headerValue="请选择厂商"
									listKey="key" listValue="value" cssClass="bk"></s:select></TD>

							<TD class="column" width='15%' align="right">设备型号</td>
							<td width='35%' align="left"><select name="modelId"
								class="bk" disabled="disabled">
									<option value="-1" selected="selected">请先选择厂商</option>
							</select></TD>
						</TR>
						<TR>
							<TD class=column width="15%" align='right'>属地</TD>
							<TD width="35%"><s:select list="cityList" name="city_id"
									headerKey="-1" headerValue="请选择属地" listKey="city_id"
									listValue="city_name" cssClass="bk"></s:select></TD>
						</TR>
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
			<tr>
				<td><iframe id="dataForm" name="dataForm" height="0"
						frameborder="0" scrolling="no" width="100%" src=""></iframe></td>
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