<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript"
	SRC="<s:url value="/Js/CheckFormForm.js"/>"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>

<script language="JavaScript">

function doQuery(){  
	var queryType = $("input[@name='queryType'][@checked]").val();
	var deviceSN = $.trim($("input[@name='deviceSN']").val());
	var loopbackIp = $.trim($("input[@name='loopbackIp']").val());
	var userName = $.trim($("input[@name='userName']").val());
	var stat_time = $.trim($("input[@name='stat_time']").val());
	var end_time = $.trim($("input[@name='end_time']").val());
	if(queryType=="device"){
		if(deviceSN==""&&loopbackIp==""){
			alert("请至少输入设备序列号和设备IP中的一项！");
			$("input[@name='deviceSN']").focus();
			return false;
		}
		if(deviceSN!=""){
			if(deviceSN.length < 6){
           		alert("请输入至少最后6位设备序列号 !");
           		$("input[@name='deviceSN']").focus();
            	return false;        
	    	}
		}
		if(loopbackIp!=""){
			if(!IsIPAddr2(loopbackIp,"设备IP")){
				$("input[@name='loopbackIp']").focus();
				return false;
			}
		}
	}
	if(queryType=="user"){
		if(userName==""){
			alert("请输入用户账号！");
			$("input[@name='userName']").focus();
			return false;
		}
	}
    if(stat_time==""){
    	alert("请输入开始时间");
		return false;
    }
    if(end_time==""){
    	alert("请输入截止时间");
		return false;
    }
    
    var time1 = stat_time.split('-');
    var time2 = end_time.split('-');
    
    if((time1[0]!=time2[0]) || (time2[1]-time1[1])>0){
    	alert("查询的时间不可跨月！");
    	return false;
    }
    
    $("tr[@id='trData']").show();
    
    $("div[@id='QueryData']").html("正在统计，请稍等....");
    var url = '<s:url value='/bbms/report/deviceFluxQuery.action'/>'; 
	$.post(url,{
		queryType:queryType,
		deviceSN:deviceSN,
		loopbackIp:loopbackIp,
		userName:encodeURIComponent(userName),
		stat_time:stat_time,
		end_time:end_time
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
	});
}

function ToExcel(queryType,deviceSN,loopbackIp,userName,stat_time,end_time) {
	var page="<s:url value='/bbms/report/deviceFluxQuery!getExcel.action'/>?"
		+ "&queryType=" + queryType
		+ "&deviceSN=" + deviceSN
		+ "&loopbackIp=" + loopbackIp
		+ "&userName=" + userName
		+ "&stat_time=" + stat_time
		+ "&end_time=" + end_time;
	document.all("childFrm").src=page;
}

function queryTypeChange(){
    var queryType = $("input[@name='queryType'][@checked]").val();
	if(queryType=="device"){
		$("tr[@name='device']").show();
		$("tr[@name='user']").hide();	
	}
	if(queryType=="user"){
		$("tr[@name='user']").show();
		$("tr[@name='device']").hide();
	}
}

function strTime2Second(dateStr){
	
	var temp = dateStr.split(' ')
	var date = temp[0].split('-');   
    var time = temp[1].split(':'); 
	
	var reqReplyDate = new Date();
	reqReplyDate.setYear(date[0]);
    reqReplyDate.setMonth(date[1]-1);
    reqReplyDate.setDate(date[2]);
    reqReplyDate.setHours(time[0]);
    reqReplyDate.setMinutes(time[1]);
    reqReplyDate.setSeconds(time[2]);

	return Math.floor(reqReplyDate.getTime()/1000);
}
</script>

<br>
<TABLE>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>
						终端流量统计
					</th>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
						按设备统计一段时间的流量情况,为了不影响查询的速度，请选择的时间不可跨越。
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
							终端流量统计
						</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column width="15%">
							查询方式
						</td>
						<td colspan="3" width="85%">
							<input type="radio" name="queryType" checked value="device"
								onclick="queryTypeChange();">
							按设备查询
							<input type="radio" name="queryType" value="user"
								onclick="queryTypeChange();">
							按用户查询
						</td>
					</tr>
					<tr bgcolor=#ffffff name="device" style="display: ">
						<td class=column width="15%">
							设备序列号
						</td>
						<td width="35%">
							<input type="text" name="deviceSN" class='bk' value="">
							<font color="red">(至少最后6位)</font>
						</td>
						<td class=column width="15%">
							设备IP
						</td>
						<td width="35%">
							<input type="text" name="loopbackIp" class='bk' value="">
						</td>
					</tr>
					<tr bgcolor=#ffffff name="user" style="display: none">
						<td class=column width="15%">
							用户账号
						</td>
						<td colspan="3" width="85%">
							<input type="text" name="userName" class='bk' value="">
							<font color="red">*</font>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column width="15%">
							开始时间
						</td>
						<td width="35%">
							<input type="text" name="stat_time" class='bk' readonly value="<s:property value='stat_time'/>">
							<img onclick="new WdatePicker(document.frm.stat_time,'%Y-%M-%D',true,'whyGreen')"
								src="<s:url value='/images/search.gif'/>" width="15" height="12"
								border="0" alt="选择">
							<font color="red">*</font>
						</td>
						<td class=column width="15%">
							截止时间
						</td>
						<td width="35%">
							<input type="text" name="end_time" class='bk' readonly value="<s:property value='end_time'/>">
							<img onclick="new WdatePicker(document.frm.end_time,'%Y-%M-%D',true,'whyGreen')"
								src="<s:url value='/images/search.gif'/>" width="15" height="12"
								border="0" alt="选择">
							<font color="red">*</font>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button onclick="doQuery()">
								&nbsp;查 询&nbsp;
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
</TABLE>

<%@ include file="/foot.jsp"%>
