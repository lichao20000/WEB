<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>终端活跃率</title>
<%
	/**
	 * 终端活跃率统计
	 * 
	 * @author gaoyi
	 * @version 1.0
	 * @since 2013-06-24
	 * @category
	 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
/**
	function query() {
		document.selectForm.submit();
	}
	
	function ToExcel() {
		var endtime = $("input[@name='endOpenDate']").val();
		var city_id = $("select[@name='city_id']").val();
		var device_type = $("select[@name='device_type']").val();

		var url = "<s:url value='/itms/report/activeTerminal!getActiveTerminalInfoCount.action'/>";
		$.post(url, {endOpenDate : endtime,
					 city_id : city_id,
					 device_type : device_type
					},
						function(ajax) {
							var total = parseInt(ajax);
							if (ajax > 100000) {
								alert("数据量太大不支持导出 ");
								return;
							} else {
								
								var mainForm = document
										.getElementById("selectForm");
								mainForm.action = "<s:url value='/itms/report/activeTerminal!getActiveTerminalInfoExcel.action'/>"
								mainForm.submit();
								mainForm.action = "<s:url value='/itms/report/activeTerminal!getActiveTerminalInfo.action'/>"
							}
						});
	}
	*/
	function query() {
		$("tr[@id='trData']").show();
	    $("div[@id='QueryData']").html("正在统计，请稍等....");
		$("button[@name='button']").attr("disabled", true); 
		var cityId = $.trim($("select[@name='city_id']").val());
		$("input[@name='cityId']").val(cityId);
	    var startime = $.trim($("input[@name='startOpenDate']").val());
	    var endtime = $.trim($("input[@name='endOpenDate']").val());
	    var device_type = $("select[@name='device_type']").val();
	    var url = "<s:url value='/itms/report/activeTerminal!getActiveTerminalInfo.action' />";
	    $.post(url,{
	    	city_id:cityId,
	    	startOpenDate:startime,
	    	endOpenDate:endtime,
	    	device_type:device_type
	    },function(ajax){
	    	 $("div[@id='QueryData']").html("");
	    	 $("div[@id='QueryData']").append(ajax);
	 		$("button[@name='button']").attr("disabled", false); 
	    });
	}
	
	function countBycityId(city_id){
		$("input[@name='cityId']").val(city_id);
		$("tr[@id='trData']").show();
	    $("div[@id='QueryData']").html("正在统计，请稍等....");
		var url = "<s:url value='/itms/report/activeTerminal!getActiveTerminalInfo.action' />";
		var endtime = $("input[@name='endOpenDate']").val();
		var startime = $("input[@name='startOpenDate']").val();
		var device_type = $("select[@name='device_type']").val();
		$.post(url,{
			city_id:city_id,
			device_type:device_type,
			startOpenDate:startime,
			endOpenDate:endtime
		},function(ajax){
			$("div[@id='QueryData']").html("");
			$("div[@id='QueryData']").append(ajax);
		});
	}
	
	function openDevForAll(city_id){
		var device_type = $("select[@name='device_type']").val();
		var page="<s:url value='/itms/report/activeTerminal!getDeviceListForAll.action'/>?"+ "city_id=" + city_id + "&device_type=" +device_type;
		window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
	}
	
	function openDevForTime(city_id){
		var endtime = $("input[@name='endOpenDate']").val();
		var starttime = $("input[@name='startOpenDate']").val();
		var device_type = $("select[@name='device_type']").val();
		var page="<s:url value='/itms/report/activeTerminal!getDeviceListForTime.action'/>?"+ "city_id=" + city_id + "&device_type=" +device_type+"&startOpenDate="+starttime+"&endOpenDate="+endtime;
		window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
	}
	

	function ToExcel() {
      var mainForm = document.getElementById("selectForm");
      mainForm.action = "<s:url value='/itms/report/activeTerminal!getActiveTerminalInfoExcel.action'/>";
      mainForm.submit();
      mainForm.action = "<s:url value='/itms/report/activeTerminal!getActiveTerminalInfo.action'/>";
	}
	
	
</script>
</head>

<body>
	<form id="form" name="selectForm"
		action="<s:url value='/itms/report/activeTerminal!getActiveTerminalInfo.action'/>">
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
							终端活跃率统计</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /> 活跃终端为一个月内有上线记录的终端</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">

						<TR>
							<th colspan="4">终端活跃率</th>
						</tr>

						<TR>
							<TD class=column width="15%" align='right'>开始时间</TD>
							<TD width="35%">
									<input type="text" name="startOpenDate" readonly class=bk
										value="<s:property value="startOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="选择">
							</TD>
							<TD class=column width="15%" align='right'>结束时间</TD>
							<TD width="35%">
									<input type="text" name="endOpenDate" readonly class=bk
										value="<s:property value="endOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="选择">
							</TD>
						</TR>


						<TR>

							<TD class=column width="15%" align='right'>属地</TD>
							<TD width="35%"><s:select list="cityList" name="city_id"
									headerKey="-1" headerValue="请选择属地" listKey="city_id"
									listValue="city_name" cssClass="bk"></s:select></TD>

							<TD class="column" width='15%' align="right">设备类型</td>
							<td width='35%' align="left"><select name="device_type"
								class="bk">
									<option value="-1" <s:property value='"-1".eqauals(device_type)?"selected":""'/>>==请选择==</option>
									<option value="e8-b" <s:property value='"e8-b".eqauals(device_type)?"selected":""'/>>==e8-b==</option>
									<option value="e8-c" <s:property value='"e8-c".eqauals(device_type)?"selected":""'/>>==e8-c==</option>
							</select></TD>

						</TR>
						<TR>
							<td colspan="4" align="right" class=foot>
								<button id="qy" onclick="query()"  >&nbsp;查&nbsp;询&nbsp;</button>
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
								正在统计，请稍等....
							</div>
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