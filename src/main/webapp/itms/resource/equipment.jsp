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
<title>设备统计查询</title>

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
		Init();
		$("select[@name='querytype']").val("1");
		$("select[@name='deviceType']").attr("disabled", true);
		$("input[@name='endOpenDate']").attr("disabled", true);
		$("select[@name='isActive']").attr("disabled", true);
		$("select[@name='specName']").attr("disabled", true);
		$("button[@name='gaojibtn1']").attr("disabled", true);
		$("select[@name='deviceType']").val("e8-c");
		$("input[@name='endOpenDate']").val("");
		$("select[@name='isActive']").val("");
		$("select[@name='specName']").val("1");
	});
	function Init(){
		gwShare_change_select("vendor","-1");
	}
	
	function gwShare_change_select(type,selectvalue){
	switch (type){
		case "vendor":
			var url = "<s:url value="/itms/resource/gwDeviceQuery!getVendor.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='vendor']"),selectvalue);
			});
			break;
		case "deviceModel":
			var url = "<s:url value="/itms/resource/gwDeviceQuery!getDeviceModel.action"/>";
			var vendorId = $("select[@name='vendor']").val();
			$("select[@name='devicetype']").html("<option value='-1'>==请先选择设备型号==</option>");
			if("-1"==vendorId){
				$("select[@name='devicemodel']").html("<option value='-1'>==请先选择设备厂商==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='devicemodel']"),selectvalue);
			});
			break;
		}		
	}
		
	function gwShare_parseMessage(ajax,field,selectvalue){
		var flag = true;
		if(""==ajax){
			return;
		}
		var lineData = ajax.split("#");
		if(!typeof(lineData) || !typeof(lineData.length)){
			return false;
		}
		field.html("");

		option = "<option value='-1' selected>==请选择==</option>";
		field.append(option);
		for(var i=0;i<lineData.length;i++){
			var oneElement = lineData[i].split("$");
			var xValue = oneElement[0];
			var xText = oneElement[1];
			if(selectvalue==xValue){
				flag = false;
				//根据每组value和text标记的值创建一个option对象
				option = "<option value='"+xValue+"' selected>=="+xText+"==</option>";
			}else{
				//根据每组value和text标记的值创建一个option对象
				option = "<option value='"+xValue+"'>=="+xText+"==</option>";
			}
			try{
				field.append(option);
			}catch(e){
				alert("设备型号检索失败！");
			}
		}
		if(flag){
			field.attr("value","-1");
		}
	}

	function query() {
		var isActive=$("select[@name='isActive']").val();
		var startOpenDate = $.trim($("input[@name='startOpenDate']").val());
		if("" != isActive){
			if("" == startOpenDate){
				alert("选择是否活跃必须选择开始时间!");
				return false;
			}
		}
		$("tr[@id='trData']").show();
		$("div[@id='QueryData']").html("正在统计，请稍等....");
		$("button[@name='button']").attr("disabled", true);
		var temval = $("input[name='hiddenVal']").val();
		var cityId = $.trim($("select[@name='city_id']").val());
		var deviceType=$("select[@name='deviceType']").val();
		var endOpenDate = $.trim($("input[@name='endOpenDate']").val());
		var specName=$("select[@name='specName']").val();
		var vendor=$("select[@name='vendor']").val();
		var devicemodel=$("select[@name='devicemodel']").val();
		var hardVersion = $("input[name='hardVersion']").val();
		var softVersion = $("input[name='softVersion']").val();
		var querytype = $.trim($("select[@name='querytype']").val());
		var url = "<s:url value='/itms/resource/EquipmentQuery!queryEquipment.action' />";
		$.post(url, {
			city_id : cityId,
			deviceType:deviceType,
			startOpenDate : startOpenDate,
			endOpenDate : endOpenDate,
			isActive:isActive,
			specName:specName,
			vendor:vendor,
			devicemodel:devicemodel,
			hardVersion:hardVersion,
			softVersion:softVersion,
			querytype:querytype,
			temval:temval
		}, function(ajax) {
			$("div[@id='QueryData']").html("");
			$("div[@id='QueryData']").append(ajax);
			$("button[@name='button']").attr("disabled", false);
		});
	}

	
	function openCity(city_id) {
		var cityId = $.trim($("select[@name='city_id']").val());
		var temval = $("input[name='hiddenVal']").val();
		var deviceType = $("select[@name='deviceType']").val();
		var startOpenDate = $.trim($("input[@name='startOpenDate']").val());
		var endOpenDate = $.trim($("input[@name='endOpenDate']").val());
		var isActive = $("select[@name='isActive']").val();
		var specName = $("select[@name='specName']").val();
		var vendor = $("select[@name='vendor']").val();
		var devicemodel = $("select[@name='devicemodel']").val();
		var hardVersion = $("input[name='hardVersion']").val();
		var softVersion = $("input[name='softVersion']").val();
		var querytype = $.trim($("select[@name='querytype']").val());
		var filePath = "<s:url value='/downloadfile/' />";
		var url = "<s:url value='/itms/resource/EquipmentQuery!downloadFile.action' />";
		var page = "<s:url value='/itms/resource/EquipmentQuery!queryEquipmentDev.action'/>?city_id="
				+ city_id
				+ "&temval="
				+ temval
				+ "&deviceType="
				+ deviceType
				+ "&startOpenDate="
				+ startOpenDate
				+ "&endOpenDate="
				+ endOpenDate
				+ "&isActive="
				+ isActive
				+ "&specName="
				+ specName
				+ "&vendor="
				+ vendor
				+ "&devicemodel="
				+ devicemodel
				+ "&hardVersion="
				+ hardVersion
				+ "&softVersion="
				+ softVersion;
		if ("1" == querytype || "2" == querytype || "3" == querytype) {
			$.post(url, {
				city_id : city_id,
				startOpenDate : startOpenDate,
				querytype : querytype
			}, function(ajax) {
				var path = ajax.split("@");
				if ("1" == path[0]) {
					window.location.href = filePath + path[1];
				} else {
					alert("该文件不存在，请确定该文件是否生成！");
				}
			});
		}
		if ("4" == querytype) {
			window
					.open(
							page,
							"",
							"left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
		}
	}

	function ToExcel() {
		var mainForm = document.getElementById("selectForm");
		mainForm.action = "<s:url value='/itms/resource/EquipmentQuery!queryEquipmentExcel.action' />";
		mainForm.submit();
		mainForm.action = "<s:url value='/itms/resource/EquipmentQuery!queryEquipment.action' />";
	}

	function gaoji(val) {
		var tempVal = $("input[name='hiddenVal']").val();
		if (1 == val) {
			$("input[name='hiddenVal']").val(val);
			$("button[name='gaojibtn1']").css("display", "none");
			$("button[name='jiandan']").css("display", "");
			$("tr[@id='gaoji1']").css("display", "");
			$("tr[@id='gaoji2']").css("display", "");
			$("select[@name='querytype']").attr("disabled", true);
		}
		if (2 == val) {
			$("input[name='hiddenVal']").val(val);
			$("button[name='jiandan']").css("display", "none");
			$("button[name='gaojibtn1']").css("display", "");
			$("tr[@id='gaoji1']").css("display", "none");
			$("tr[@id='gaoji2']").css("display", "none");
			$("select[@name='querytype']").attr("disabled", false);
		}
	}

	function changeQuery() {
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
			$("select[@name='deviceType']").attr("disabled", true);
			$("input[@name='endOpenDate']").attr("disabled", true);
			$("select[@name='isActive']").attr("disabled", true);
			$("select[@name='specName']").attr("disabled", true);
			$("button[@name='gaojibtn1']").attr("disabled", true);
			$("select[@name='deviceType']").val("e8-c");
			$("input[@name='endOpenDate']").val("");
			$("select[@name='isActive']").val("");
			$("select[@name='specName']").val("1");
		} else {
			$("select[@name='deviceType']").attr("disabled", false);
			$("input[@name='endOpenDate']").attr("disabled", false);
			$("select[@name='isActive']").attr("disabled", false);
			$("select[@name='specName']").attr("disabled", false);
			$("button[@name='gaojibtn1']").attr("disabled", false);
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
		action="<s:url value='/itms/resource/EquipmentQuery!queryEquipment.action'/>">
		<input type="hidden" name="hiddenVal" value="2"/>
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								设备统计查询</td>
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
							<th colspan="4">设备统计查询</th>
						</tr>
						<TR>
							<TD class=column width="15%" align='right'>属地</TD>
							<TD width="35%"><s:select list="cityList" name="city_id"
									headerKey="00" headerValue="请选择属地" listKey="city_id"
									listValue="city_name" cssClass="bk"></s:select></TD>
							<TD class=column width="15%" align='right'>设备类型</td>
							<TD width="35%"><select name="deviceType" class="bk">
									<option value="">===请选择===</option>
									<option value="e8-c">E8C</option>
									<option value="e8-b">E8B</option>
							</select></TD>
						</TR>
						<TR>
							<TD class=column width="15%" align='right'>注册开始时间</TD>
							<TD width="35%"><input type="text" name="startOpenDate"
								readonly class=bk value="<s:property value="startOpenDate" />">
								<img name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-#{%d-1}',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择"></TD>
							<TD class=column width="15%" align='right'>注册结束时间</TD>
							<TD width="35%"><input type="text" name="endOpenDate"
								readonly class=bk value="<s:property value="endOpenDate" />">
								<img name=""
								onClick="WdatePicker({el:document.selectForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择"></TD>
						</TR>
						<TR>
							<TD class=column width="15%" align='right'>活跃</TD>
							<TD width="35%"><select name="isActive" class="bk">
									<option value="">===请选择===</option>
									<option value="1">是</option>
									<option value="0">否</option>
							</select></TD>
							<TD class=column width="15%" align='right'>终端规格</td>
							<TD width="35%"><select name="specName" class="bk">
									<option value="">===请选择===</option>
									<option value="1">家庭网关</option>
									<option value="2">政企网关</option>
							</select></TD>
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
							<TD class=column width="15%" align='right'></td>
							<TD width="35%"></TD>
						</TR>
						<TR id="gaoji1" style="display: none;">
							<TD class=column width="15%" align='right'>设备厂商</TD>
							<TD width="35%"><select name="vendor" class="bk"
								onchange="gwShare_change_select('deviceModel','-1')">
							</select></TD>
			
							<TD class=column width="15%" align='right'>设备型号</TD>
							<TD width="35%"><select name="devicemodel" class="bk"
								onchange="gwShare_change_select('devicetype','-1')">
								<option value="">==请选择厂商==</option>
							</select></TD>
						</TR>
						<TR id="gaoji2" style="display: none;">
							<TD class=column width="15%" align='right'>硬件版本</TD>
							<TD width="35%"><input type="text" name="hardVersion" class=bk /></TD>
							<TD class=column width="15%" align='right'>软件版本</td>
							<TD width="35%"><input type="text" name="softVersion" class=bk /></TD>
						</TR>

						<TR>
							<td colspan="4" align="right" class=foot>
								<button onclick="gaoji('1')" id="button" name="gaojibtn1">高 级 查 询 </button>
								<button onclick="gaoji('2')" id="button" name="jiandan" style="display: none;">简 单 查 询 </button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<button onclick="query()" id="button" name="button"> 查 询 </button>
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
			<tr>
				<td id="bssSheetInfo"></td>
			</tr>
		</table>
		<br>
	</form>
</body>
</html>
<%@ include file="../../foot.jsp"%>