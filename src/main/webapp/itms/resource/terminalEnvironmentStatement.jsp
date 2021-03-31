<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>终端环境感知报表</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">

	function query() {
		
		$("tr[@id='trData']").show();
	    $("div[@id='QueryData']").html("正在统计，请稍等....");
		$("button[@name='button']").attr("disabled", true); 
		var cityId = $.trim($("select[@name='city_id']").val());
	    var startOpenDate = $.trim($("input[@name='startOpenDate']").val());
	    var endOpenDate = $.trim($("input[@name='endOpenDate']").val());
	    var temperature = $.trim($("input[@name='temperature']").val());
	    var bais_current = $.trim($("input[@name='bais_current']").val());
	    var voltage = $.trim($("input[@name='voltage']").val());
	    var url = "<s:url value='/itms/resource/TerminalEnvironmentStatementQuery!queryTerminalEnvironmentStatement.action' />";
	    $.post(url,{
	    	city_id:cityId,
	    	startOpenDate:startOpenDate,
	    	endOpenDate:endOpenDate,
	    	temperature:temperature,
	    	bais_current:bais_current,
	    	voltage:voltage
	    },function(ajax){
	    	 $("div[@id='QueryData']").html("");		
	    	 $("div[@id='QueryData']").append(ajax);
	 		 $("button[@name='button']").attr("disabled", false); 
	    });	
	}
	
	function openCity(city_id){
		var startOpenDate = $.trim($("input[@name='startOpenDate']").val());
	    var endOpenDate = $.trim($("input[@name='endOpenDate']").val());
	    var temperature = $.trim($("input[@name='temperature']").val());
	    var bais_current = $.trim($("input[@name='bais_current']").val());
	    var voltage = $.trim($("input[@name='voltage']").val());
		var page="<s:url value='/itms/resource/TerminalEnvironmentStatementQuery!queryTerminalEnvironmentStatementDev.action'/>?city_id=" + city_id +"&temperature="+temperature+"&bais_current="+bais_current+"&voltage="+voltage+ "&startOpenDate=" +startOpenDate+ "&endOpenDate=" +endOpenDate;
		window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
	}
	
	function ToExcel(){
		var mainForm = document.getElementById("selectForm");
		mainForm.action = "<s:url value='/itms/resource/TerminalEnvironmentStatementQuery!queryTerminalEnvironmentStatementExcel.action' />";
		mainForm.submit();
		mainForm.action = "<s:url value='/itms/resource/TerminalEnvironmentStatementQuery!queryTerminalEnvironmentStatement.action' />";
	}
	
	function onlyNum(v){
		v.value=v.value.replace(/[^\d]/g,"");
	}
	
</script>
</head>

<body>
	<form id="form" name="selectForm"
		action="<s:url value='/itms/resource/TerminalEnvironmentStatementQuery!queryTerminalEnvironmentStatement.action'/>">
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								终端环境感知报表</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" />&nbsp;&nbsp;</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">

						<TR>
							<th colspan="4">终端环境感知报表 </th>
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
							<TD width="35%">
								<s:select list="cityList" name="city_id"
									headerKey="00" headerValue="请选择属地" listKey="city_id"
									listValue="city_name" cssClass="bk"></s:select>
							</TD>
							<TD class=column width="15%" align='right'>温度大于:</TD>
							<TD width="35%">
							<input type="text" name="temperature" size="20" maxlength="30" class=bk onkeyup="onlyNum(this);" />
							</TD>
						</TR>
						
						<TR>
							<TD class=column width="15%" align='right'>电流大于:</TD>
							<TD width="35%">
							<input type="text" name="bais_current" size="20" maxlength="30" class=bk onkeyup="onlyNum(this);" />
							</TD>
							<TD class=column width="15%" align='right'>电压大于:</TD>
							<TD width="35%">
							<input type="text" name="voltage" size="20" maxlength="30" class=bk onkeyup="onlyNum(this);" />
							</TD>
						</TR>
	
						<TR>
							<td colspan="4" align="right" class=foot>
								<button  onclick="query()" id="button" name="button" >&nbsp;查&nbsp;询&nbsp;</button>
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
								正在查询，请稍等....
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