<%@page import="java.util.Locale"%>
<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>终端用户绑定记录</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
	
	$(function(){
		$("select[@name='querytype']").val("1");
		//$("select[@name='specName']").attr("disabled", true);
		$("input[@name='endOpenDate']").attr("disabled", true);
		$("select[@name='operation']").attr("disabled", true);
		//$("select[@name='specName']").val("1");
		$("input[@name='endOpenDate']").val("");
		$("select[@name='operation']").val("2");
	});
	
	function query() {
		$("tr[@id='trData']").show();
		$("div[@id='QueryData']").html("正在统计，请稍等....");
		$("button[@name='button']").attr("disabled", true);
		var cityId = $.trim($("select[@name='city_id']").val());
		//var specName=$("select[@name='specName']").val();
		var startOpenDate = $.trim($("input[@name='startOpenDate']").val());
		var endOpenDate = $.trim($("input[@name='endOpenDate']").val());
		var operation=$("select[@name='operation']").val();
		var hiddenVal = $.trim($("input[@name='hiddenVal']").val());
		var querytype = $.trim($("select[@name='querytype']").val());
		var url = "<s:url value='/itms/resource/terminalUserBindingQuery!queryTerminalUserBinding.action' />";
		$.post(url, {
			city_id : cityId,
			//specName : specName,
			startOpenDate : startOpenDate,
			endOpenDate : endOpenDate,
			operation : operation,
			querytype : querytype,
			hiddenVal : hiddenVal
		}, function(ajax) {
			$("div[@id='QueryData']").html("");
			$("div[@id='QueryData']").append(ajax);
			$("button[@name='button']").attr("disabled", false);
		});
	}

	function openCity(city_id) {
		var startOpenDate = $.trim($("input[@name='startOpenDate']").val());
		var endOpenDate = $.trim($("input[@name='endOpenDate']").val());
		//var specName=$("select[@name='specName']").val();
		var operation=$("select[@name='operation']").val();
		var hiddenVal = $.trim($("input[@name='hiddenVal']").val());
		var querytype = $.trim($("select[@name='querytype']").val());
		var filePath = "<s:url value='/downloadfile/' />";
		var url = "<s:url value='/itms/resource/terminalUserBindingQuery!downloadFile.action' />";
		var page = "<s:url value='/itms/resource/terminalUserBindingQuery!queryTerminalUserBindingDev.action'/>?city_id="
			+ city_id
			+ "&operation="
			+ operation
			+ "&hiddenVal="
			+ hiddenVal
			+ "&startOpenDate="
			+ startOpenDate + "&endOpenDate=" + endOpenDate;
		if ("1" == querytype || "2" == querytype || "3" == querytype) {
			$.post(url, {
				city_id : city_id,
				startOpenDate : startOpenDate,
				querytype : querytype,
				hiddenVal : hiddenVal
			}, function(ajax) {
				var path = ajax.split("@");
		 		if("1"==path[0]){
		 			window.location.href = filePath+path[1];
		 		}else{
		 			alert("该文件不存在，请确定该文件是否生成！");
		 		}
			});
		}
		if("4" == querytype){
		window
				.open(
						page,
						"",
						"left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
		}
	}

	function ToExcel() {
		var mainForm = document.getElementById("selectForm");
		mainForm.action = "<s:url value='/itms/resource/terminalUserBindingQuery!queryTerminalUserBindingExcel.action' />";
		mainForm.submit();
		mainForm.action = "<s:url value='/itms/resource/terminalUserBindingQuery!queryTerminalUserBinding.action' />";
	}
	
	function changeType(){
		var queryType=$("select[@name='queryType']").val();
		$("input[name='hiddenVal']").val(queryType);
		if("1" == queryType){
			$("td[@id='st']").html("受理开始时间");
			$("td[@id='ed']").html("受理结束时间");
			$("table[@id='querytable']").show();
		}else if("2" == queryType){
			$("td[@id='st']").html("注册开始时间");
			$("td[@id='ed']").html("注册结束时间");
			$("table[@id='querytable']").show();
		}else{
			$("table[@id='querytable']").hide();
			$("input[name='hiddenVal']").val("");
		}
	}
	
	function changeQuery(){
		var querytype = $.trim($("select[@name='querytype']").val());
		$("img[name='shortDateimg']").attr("onclick","").unbind("click","");
		if ("1" == querytype || "2" == querytype || "3" == querytype) {
			if ("1" == querytype){
				<%
				Calendar cal1 = Calendar.getInstance();
				cal1.add(Calendar.DATE, -1);
				cal1.set(Calendar.HOUR_OF_DAY, 0);
				cal1.set(Calendar.MINUTE, 0);
				cal1.set(Calendar.SECOND, 0);
				long tempDay = cal1.getTimeInMillis() / 1000;
				Date date1 = new Date(tempDay * 1000);
				SimpleDateFormat dayDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String time1 = dayDateFormat.format(date1);// 文件日期
				%>
				$("input[name='startOpenDate']").val('<%=time1%>');
				$("img[name='shortDateimg']").attr("onclick","").bind("click",function(){
					WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-#{%d-1}',skin:'whyGreen'});
				});
			}
			if ("2" == querytype){
				<%
				Calendar cal2 = Calendar.getInstance();
				cal2.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				cal2.add(Calendar.WEEK_OF_YEAR, -1);
				cal2.set(Calendar.HOUR_OF_DAY, 0);
				cal2.set(Calendar.MINUTE, 0);
				cal2.set(Calendar.SECOND, 0);
				long tempWeek = cal2.getTimeInMillis() / 1000;
				Date date2 = new Date(tempWeek * 1000);
				SimpleDateFormat weekDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String time2 = weekDateFormat.format(date2);// 文件日期
				%>
				$("input[name='startOpenDate']").val('<%=time2%>');
				$("img[name='shortDateimg']").attr("onclick","").bind("click",function(){
					var now = new Date();      
					var Year = now.getFullYear();
					var Month = now.getMonth() + 1;
					var Day = now.getDate()- now.getDay();
					if(now.getDay()==0){
						Day -= 7;
					}
					var beginTime = Year + "-" + Month +"-" + Day;
					WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd',maxDate:beginTime,isShowWeek:true,skin:'whyGreen'});
				});
			}
			if ("3" == querytype){
				<%
				Calendar cal3 = Calendar.getInstance();
				cal3.add(Calendar.MONTH, -1);
				cal3.set(Calendar.DAY_OF_MONTH, 1);
				cal3.set(Calendar.HOUR_OF_DAY, 0);
				cal3.set(Calendar.MINUTE, 0);
				cal3.set(Calendar.SECOND, 0);
				long tempMonth = cal3.getTimeInMillis() / 1000;
				Date date3 = new Date(tempMonth * 1000);
				SimpleDateFormat monthDateFormat = new SimpleDateFormat("yyyy-MM");
				String time3 = monthDateFormat.format(date3);// 文件日期
				%>
				$("input[name='startOpenDate']").val('<%=time3%>');
				$("img[name='shortDateimg']").attr("onclick","").bind("click",function(){
					WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM',maxDate:'%y-#{%M-1}',skin:'whyGreen'});
				});
			}
			//$("select[@name='specName']").attr("disabled", true);
			$("input[@name='endOpenDate']").attr("disabled", true);
			$("select[@name='operation']").attr("disabled", true);
			//$("select[@name='specName']").val("1");
			$("input[@name='endOpenDate']").val("");
			$("select[@name='operation']").val("2");
		}else{
			//$("select[@name='specName']").attr("disabled", false);
			$("input[@name='endOpenDate']").attr("disabled", false);
			$("select[@name='operation']").attr("disabled", false);
			if ("4" == querytype){
				<%
				GregorianCalendar now4 = new GregorianCalendar();
				SimpleDateFormat fmtrq1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
				now4.set(GregorianCalendar.DATE, 1);
				now4.set(GregorianCalendar.MONTH, 0);
				now4.set(Calendar.HOUR_OF_DAY, 0);
				now4.set(Calendar.MINUTE, 0);
				now4.set(Calendar.SECOND, 0);
				String time4 = fmtrq1.format(now4.getTime());
				%>
				$("input[name='startOpenDate']").val('<%=time4%>');
				<%
				GregorianCalendar now = new GregorianCalendar();
				SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
				String time5 = fmtrq.format(now.getTime());
				%>
				$("input[name='endOpenDate']").val('<%=time5%>');
				$("img[name='shortDateimg']").attr("onclick","").bind("click",function(){
					WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'});
				});
			}
		}
	}
</script>
</head>

<body>
	<form id="form" name="selectForm"
		action="<s:url value='/itms/resource/terminalUserBindingQuery!queryTerminalUserBinding.action'/>">
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								终端用户绑定记录</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" />周统计：周一至周日，月统计：1号到月最后一天</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">
						<TR>
							<th colspan="4">终端用户绑定记录报表</th>
						</tr>
						<TR>
							<TD colspan="3" class=column width="15%" align='right'>查询方式</TD>
							<TD width="35%"><select class="bk" name="queryType" onchange="changeType()">
									<option value="">===请选择===</option>
									<option value="1">用户查询</option>
									<option value="2" >设备查询</option>
							</select></TD>
						</TR>
					</table>
					
					<span>&nbsp;</span>
					
					<table id="querytable" class="querytable" style="display: none;">
					<input type="hidden" name="hiddenVal" value=""/>
						<TR>
							<TD class=column width="15%" align='right'>属地</TD>
							<TD width="35%"><s:select list="cityList" name="city_id"
									headerKey="00" headerValue="请选择属地" listKey="city_id"
									listValue="city_name" cssClass="bk"></s:select></TD>
							<%-- <TD class=column width="15%" align='right'>终端规格</td>
							<TD width="35%"><select name="specName" class="bk">
									<option value="">===请选择===</option>
									<option value="1">家庭网关</option>
									<option value="2">政企网关</option>
							</select></TD> --%>
							<TD class=column width="15%" align='right'>操作方式</TD>
							<TD width="35%"><select name="operation" class="bk">
									<option value="">===请选择===</option>
									<option value="1">有绑定操作</option>
									<option value="2">有解绑操作</option>
							</select></TD>
						</TR>
						<TR>
							<TD class=column width="15%" align='right' id='st'>时间</TD>
							<TD width="35%"><input type="text" name="startOpenDate"
								readonly class=bk value="<s:property value="startOpenDate" />">
								<img name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-#{%d-1}',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择"></TD>
							<TD class=column width="15%" align='right' id='ed'>时间</TD>
							<TD width="35%"><input type="text" name="endOpenDate"
								readonly class=bk value="<s:property value="endOpenDate" />">
								<img name=""
								onClick="WdatePicker({el:document.selectForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择"></TD>
						</TR>
						<TR>
							<TD class=column width="15%" align='right'>查询方式</TD>
							<TD width="35%"><select name="querytype" class="bk" onchange="changeQuery()">
									<option value="4">===请选择===</option>
									<option value="1">日</option>
									<option value="2">周</option>
									<option value="3">月</option>
									<option value="4">自定义查询</option>
							</select></TD>
							<TD class=column width="15%" align='right'></TD>
							<TD width="35%"></TD>
						</TR>

						<TR>
							<td colspan="4" align="right" class=foot>
								<button onclick="query()" id="button" name="button">&nbsp;查&nbsp;询&nbsp;</button>
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
						正在查询，请稍等....</div>
				</td>
			</tr>
			<tr>
				<td height="25"></td>
			</tr>
		</table>
		<br>
	</form>
</body>
</html>
<%@ include file="../../foot.jsp"%>