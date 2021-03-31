<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>

<script language="JavaScript">


function doQuery(){
	var serviceId = $.trim($("select[@name='serviceId']").val());
	var cityId = $.trim($("select[@name='cityId']").val());
	var reportType = $.trim($("select[@name='reportType']").val());
    var stat_day = $.trim($("input[@name='stat_day']").val());

    if(cityId == "-1"){
		alert("请选择属地");
		return false;
    }
    if(stat_day==""){
    	alert("请输入统计时间");
		return false;
    }
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("正在统计，请稍等....");
    var url = '<s:url value='/bbms/report/serviceReport.action'/>'; 
	$.post(url,{
		serviceId:serviceId,
		cityId:cityId,
		reportType:reportType,
		stat_day:stat_day
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
	});
}

function countBycityId(cityId,serviceId,reportType,stat_day){

    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("正在统计，请稍等....");
    var url = '<s:url value='/bbms/report/serviceReport.action'/>'; 
	$.post(url,{
		serviceId:serviceId,
		cityId:cityId,
		reportType:reportType,
		stat_day:stat_day
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
	});
}

function ToExcel(cityId,serviceId,reportType,stat_day) {
	var page="<s:url value='/bbms/report/serviceReport!getExcel.action'/>?"
		+ "&cityId=" + cityId
		+ "&serviceId=" + serviceId
		+ "&reportType=" + reportType
		+ "&stat_day=" + stat_day;
	document.all("childFrm").src=page;
}

</script>

<br>
<TABLE>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>
						业务使用统计
					</th>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15"
							height="12">
						统计在线设备的业务使用情况
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name=frm>
				<table class="querytable">
					<tr>
						<th colspan=4>
							业务使用统计
						</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							业务种类
						</td>
						<td>
							<s:select list="serviceTypeList" name="serviceId" headerKey="-1"
								headerValue="所有业务" listKey="service_id" listValue="service_name"
								value="serviceId" cssClass="bk"></s:select>
						</td>
						<td class=column align=center width="15%">
							属 地
						</td>
						<td>
							<s:select list="cityList" name="cityId" headerKey="-1"
								headerValue="请选择属地" listKey="city_id" listValue="city_name"
								value="cityId" cssClass="bk"></s:select>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							报表类型
						</td>
						<td>
							<select name="reportType" class="bk">
								<option value="week">
									周报表
								</option>
								<option value="month">
									月报表
								</option>
							</select>
						</td>
						<td class=column align=center width="15%">
							统计时间
						</td>
						<td>
							<input type="text" name="stat_day" class='bk' readonly onclick="new WdatePicker(document.frm.stat_day,'%Y-%M-%D',true,'whyGreen')"
								value="<s:property value='stat_day'/>">
							<img
								onclick="new WdatePicker(document.frm.stat_day,'%Y-%M-%D',true,'whyGreen')"
								src="<s:url value='/images/search.gif'/>" width="15" height="12"
								border="0" alt="选择">
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button onclick="doQuery()">
								&nbsp;统 计&nbsp;
							</button>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>

	<tr id="trData" style="display: none">
		<td class="colum">
			<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
				正在统计，请稍等....
			</div>
		</td>
	</tr>
	<tr>
		<td height="20">
		</td>
	</tr>
</TABLE>

<%@ include file="/foot.jsp"%>
