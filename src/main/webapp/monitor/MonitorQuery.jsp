<%@ include file="../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="gbk"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>主机与业务监控报表</title>
<lk:res />
<script type="text/javascript">
var actionBase="<s:url value="/liposs/monitorReport/monitorAct"/>";
var monitorControl =  <%= request.getParameter("monitorControl")%>;
var hostid = <%= request.getParameter("hostid")%>;
var currHour = <%= request.getParameter("currHour")%>;
var preHour = <%= request.getParameter("preHour")%>;

/**
*初始化链路组列表
*/
$(function(){
	//防止属地和组别控件的值不为空
	filterHostSelect();
	
	if(monitorControl == "1"){
		// 设置监控点的值
		$("#hostIDSelect").val(hostid);
		filterMonitorTypeSelect();
		$("#typeIDSelect").val(17);
		$("#hour1").val(preHour);
		$("#hour2").val(currHour);
		mrtg();
	}	
});
/**
*画mrtg图
*/
function mrtg(){
	if(checkQueryParam()){
		$("#resultShow").html("正在查询，请稍等！");
		
		var monitor_host= $("#hostIDSelect").val();
		var monitor_type = $("#typeIDSelect").val();
		var rptType=$("input[name=rptType][checked]").val();
		var rptTime = getRptTime();
		var phaseStart = $("#phaseStart").val();
		var phaseEnd = $("#phaseEnd").val();
		
		$.ajax({type:"POST",
			data:{
				monitor_host:monitor_host,
				monitor_type:monitor_type,
				rptTime:rptTime,
				rptType:rptType,
				phaseStart:phaseStart,
				phaseEnd:phaseEnd
				},
			url:actionBase+"!mrtg.action",
			dataType:"text json",
			success:function(data){
				$("#resultShow").html(data);
			},
			error:function(){
				$("#resultShow").html("获取监控结果失败！");
				alert('获取监控结果失败！');
			}
		});
	}
}

function checkQueryParam(){
	var monitor_host= $("#hostIDSelect").val();
	$("#monitor_host").val(monitor_host);
	if(monitor_host == "-1")
	{
		alert("请选择监控点！");
		return false;
	}
	
	var monitor_type = $("#typeIDSelect").val();
	$("#monitor_type").val(monitor_type);
	if(monitor_type == "-1")
	{
		alert("请选择监控类型！");
		return false;
	}
	
	return true;
}
/**
*监控点改变时，联动改变监控类型下拉选择框
*/
function filterHostSelect(){
	var url=actionBase+"!getAllMonitorIDList.action";
	$.ajaxSettings.async = false; 
	$.post(url,{},function(ajax){
		if(ajax=="{}"){
			alert('获取监控点失败');
			return;
		}
		var arr = ajax.split(",");
		var opts='';
		for(var i=0;i<arr.length;i++){
			var ar=arr[i].split("#");
			opts+='<option value="'+ar[0]+'">'+ar[1]+'</option>';
		}
		opts='<option value="-1">'+(opts==''?'没有配置监控点':'请选择')+'</option>'+opts;
		$("#hostIDSelect").html(opts);
	});
	$.ajaxSettings.async = true; 
}

/**
*监控点改变时，联动改变监控类型下拉选择框
*/
function filterMonitorTypeSelect(){
	var monitor_host= $("#hostIDSelect").val();
	if(monitor_host!="-1"){
		var url=actionBase+"!getMonitorTypeListByHostID.action";
		$.ajaxSettings.async = false; 
		$.post(url,{
			monitor_host:monitor_host
			},function(ajax){
			if(ajax=="{}"){
				alert('获取监控类型失败');
				return;
			}
			var arr = ajax.split(",");
			var opts='';
			for(var i=0;i<arr.length;i++){
				var ar=arr[i].split("#");
				opts+='<option value="'+ar[0]+'">'+ar[1]+'</option>';
			}
			opts='<option value="-1">'+(opts==''?'没有配置监控类型':'请选择')+'</option>'+opts;
			$("#typeIDSelect").html(opts);
		});
		$.ajaxSettings.async = true; 
	}
}

/**
*改变时间的格式
*/
function changeTime(type){
	$("#sp6").hide();
	$("#sp1").hide();
	$("#sp2").hide();
	$("#sp3").hide();
	//$("#sp4").hide();
	$("#sp5").hide();
	$("#sp"+type).show();
}
/**
*获得用户选择的时间,返回格式为yyyy-MM-dd的字符串
*/
function getRptTime(){
	var type=$("input[name=rptType][checked]").val();
	var rptTime="" ;
	
	var rptStartTime;
	var rptEndTime;
	if (type ==6)
	{
		rptTime = $("#time6").val();
		$("#phaseStart").val(rptTime + " " + ($("#hour1").val()<10 ? "0"+$("#hour1").val() : $("#hour1").val() )+ ":00:00");
		$("#phaseEnd").val(rptTime + " " + ($("#hour2").val()<10 ? "0"+$("#hour2").val() : $("#hour2").val()) + ":00:00");
	}else if(type ==1 )
	{
		rptTime = $("#time1").val();
	}else if (type ==2)
	{
		rptTime = $("#time2").val();
	}else if (type ==3)
	{
		rptTime = $("#time3").val()+"-01";
	}else if (type ==4)
	{
		rptTime = $("#time4").val()+"-01-01";
	}else if (type ==5)
	{
		$("#phaseStart").val($("#phase1").val());
		$("#phaseEnd").val($("#phase2").val());
	}
	return rptTime;
}

</script>
</head>
<body style="text-align: center; margin-top: 15px;">
<table class="querytable" width="98%">
	<tr>
		<td class="title_1">主机与业务监控报表</td>
	</tr>
</table>
<form id="frm" name="frm" method="post">
<input type="hidden" name="phaseStart" id="phaseStart" value="" />
<input type="hidden" name="phaseEnd" id="phaseEnd" value="" />
<input type="hidden" name="monitor_host" id="monitor_host" value="" />
<input type="hidden" name="monitor_type" id="monitor_type" value="" />
<fieldset><legend>选择时间和报表类型</legend>
<table class="querytable" width="98%">
	<tr>
		<td class="title_2" title="自然日、自然周、自然月">报表类型</td>
		<td>
			<input type="radio" name="rptType" id="rptDay" value="6" checked="checked" onclick="changeTime('6')" />
				<label for="rptDay">小时报表</label>
			<input type="radio" name="rptType" id="rptDay" value="1"  onclick="changeTime('1')" />
				<label for="rptDay">日报表</label> 
			<input type="radio" name="rptType" id="rptWeek" value="2" onclick="changeTime('2')" />
				<label for="rptWeek">周报表</label> 
			<input type="radio" name="rptType" id="rptMonth" value="3" onclick="changeTime('3')" />
				<label for="rptMonth">月报表</label>
			<!-- <input type="radio" name="rptType" id="rptYear" value="4"  onclick="changeTime('4')" />
				<label for="rptDay">年报表</label> -->
			<input type="radio" name="rptType" id="rptPhase" value="5" onclick="changeTime('5')" />
				<label for="rptDay">阶段报表</label>
		</td>
	</tr>
	<tr>
		<td class="title_2">选择时间<input type="hidden" name="rptTime" id="rptTime"/></td>
		<td>
		<span id="sp6"><lk:date name="time6" readonly="true" type="day" dateOffset="0" maxDateOffset="0" id="time6" />
						 <select name="hour1" id="hour1" class=bk>
		                      <%for(int i=0;i<24;i++){%>
		                      <option value="<%=i%>"><%=i%>点</option>
		                      <%}%>
		                 </select>
		                 -
		                 <select name="hour2" id="hour2" class=bk>
		                      <%for(int i=1;i<24;i++){%>
		                      <option value="<%=i%>"><%=i%>点</option>
		                      <%}%>
		                 </select>
		</span>
		<span id="sp1" style="display:none;"><lk:date name="time1" readonly="true" type="day" dateOffset="0" maxDateOffset="0" id="time1" /></span>
		<span id="sp2" style="display:none;"><lk:date name="time2" readonly="true" type="day" dateOffset="0" maxDateOffset="0" id="time2" /></span>
		<span id="sp3" style="display:none;"><lk:date name="time3" readonly="true" type="month" dateOffset="0" maxDateOffset="0" id="time3" /></span>				
		<!--<span id="sp4" style="display:none;"><lk:date name="time4" readonly="true" type="year" dateOffset="0" maxDateOffset="0" id="time4" /></span>-->				
		<span id="sp5" style="display:none;">
			<lk:date name="phase1" readonly="true" type="day" dateOffset="-1" maxDateOffset="-1" id="phase1" /> - 
			<lk:date name="phase2" readonly="true" type="day" dateOffset="0" maxDateOffset="0" id="phase2" />
		</span>				
		</td>
	</tr>
</table>
</fieldset>
<fieldset><legend>选择监控内容</legend>
<table class="querytable" width="98%">
	<tr>
		<td class="title_2">监控点</td>
		<td>
		<select name="hostIDSelect" id="hostIDSelect" onchange="filterMonitorTypeSelect();" style="width:10em;"></select>
		</td>
		<td class="title_2">监控类型</td>
		<td>
			<select name="typeIDSelect" id="typeIDSelect" style="width:10em;">
				<option value="-1">请选择</option>
			</select>
		</td>
	</tr>
</table>
</fieldset>
</form>
<table class="querytable" width="98%">
	<tr>
		<td class="foot" style="text-align:right">
		<button onclick="mrtg();">MRTG</button>
		</td>
	</tr>
</table>
<br/>
<div id="resultShow"></div>
<br/>
<br/>
<br/>
</body>
</html>

