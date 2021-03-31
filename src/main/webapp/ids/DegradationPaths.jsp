<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>

<script language="JavaScript">
	function doQuery() {
		var cityId = $.trim($("select[@name='cityId']").val());
		var oltip = $.trim($("input[@name='oltip']").val());
		var startTime = $.trim($("input[@name='startTime']").val());
		var endTime = $.trim($("input[@name='endTime']").val());
		if (cityId == "-1") {
			alert("请选择属地");
			return false;
		}
		$("tr[@id='trData']").show();
		$("div[@id='QueryData']").html("正在统计，请稍等....");
		$("button[@name='button']").attr("disabled", true);
		var url = "<s:url value='ids/DegradationPathsQuery!getDegradationPaths.action'/>";
		$.post(url, {
			cityId : cityId,
			oltip : oltip,
			startTime : startTime,
			endTime : endTime
		}, function(ajax) {
			$("div[@id='QueryData']").html("");
			$("div[@id='QueryData']").append(ajax);
			$("button[@name='button']").attr("disabled", false);
		});
	}
</script>

<br>
<TABLE>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>光路劣化关联</th>
					<td><img src="<s:url value="/images/attention_2.gif"/>"
						width="15" height="12"> 光路劣化关联</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name=frm>
				<table class="querytable">
					<tr>
						<th colspan=4>光路劣化关联分析</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">开始时间</td>
						<td><input type="text" name="startTime" class='bk' readonly
							value="<s:property value='startTime'/>"> <img
							name="shortDateimg"
							onClick="WdatePicker({el:document.frm.startTime,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
							src="<s:url value='/images/dateButton.png'/>" width="15"
							height="12" border="0" alt="选择" /></td>
						<td class=column align=center width="15%">结束时间</td>
						<td><input type="text" name="endTime" class='bk' readonly
							value="<s:property value='endTime'/>"> <img
							name="shortDateimg"
							onClick="WdatePicker({el:document.frm.endTime,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
							src="<s:url value='/images/dateButton.png'/>" width="15"
							height="12" border="0" alt="选择" /></td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">属 地</td>
						<td><s:select list="cityList" name="cityId" headerKey="-1"
								headerValue="请选择属地" listKey="city_id" listValue="city_name"
								value="cityId" cssClass="bk"></s:select></td>
						<td class=column align=center width="15%">OLTIP</td>
						<td width='35%' align="left"><input type="text" name="oltip"
							size="20" maxlength="30" class=bk /></td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button onclick="doQuery()" name="button">&nbsp;分
								析&nbsp;</button>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>

	<tr id="trData" style="display: none">
		<td class="colum">
			<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
				正在统计，请稍等....</div>
		</td>
	</tr>
	<tr>
		<td height="20"></td>
	</tr>
</TABLE>

<%@ include file="/foot.jsp"%>
