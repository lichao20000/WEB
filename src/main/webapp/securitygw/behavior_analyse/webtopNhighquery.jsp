<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%-- 
	/**
 	 * 高级查询:获取员工web浏览topN报表页面
 	 * 
 	 * @author suixz(5253) 2008-5-6
 	 * @version 1.0
 	 * @category securitygw
 	 */
--%>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<title>webTopN高级查询</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/Calendar.js"/>"></script>
<link href="<s:url value="/model_vip/css/liulu.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/model_vip/css/tablecss.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/model_vip/css/css_blue.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript">
$(function(){
	//初始化查询时间
	var d = new Date();
	$("#startDate").val(d.getYear()+"-"+(d.getMonth()+1)+"-"+d.getDate());
	$("#endDate").val(d.getYear()+"-"+(d.getMonth()+1)+"-"+d.getDate());
});
//查询
function queryData(){
//时间判断
if($("#startDate").val()=="")
			{
				alert("起始时间不能为空");
				return false;
			}else{
				var t=$("#startDate").val().replace("-","/");
				var d = new Date(t);
				var ltime=d.getTime();
				var now = new Date();
				var time = now.getTime();
				if(ltime>time){
					alert("起始时间不能超过当前时间");
					return false;
				}
			}
			if($("#endDate").val()=="")
			{
				alert("结束时间不能为空");
				return false;
			}else{
				var t=$("#endDate").val().replace("-","/");
				var d = new Date(t);
				var ltime=d.getTime();
				var now = new Date();
				var time = now.getTime();
				if(ltime>time){
					alert("结束时间不能大于当前时间");
					return false;
				}
			}
			if($("#startDate").val()>$("#endDate").val()){
				alert("开始时间不能大于结束时间");
				return false;
			}
	$("#load").show();
	//获取topN图形
	var url = "<s:url value="/securitygw/webTopNHighQuery!getWebTopNChartData.action"><s:param name="deviceId" value="deviceId"/></s:url>";
	$("#chartData").attr("src",url+"&startTime="+$("#startDate").val()+"&endTime="+$("#endDate").val());
	//动态获取报表信息
	url = "<s:url value="/securitygw/webTopNHighQuery!getWebTopNTabData.action"><s:param name="deviceId" value="deviceId"/></s:url>";
	$.post(
		url,
		{
			startTime:$("#startDate").val(),
			endTime:$("#endDate").val()
		},
		function(data){
			$("#tableData").show();
			$("#tableData").html(data);
	});
}
</script>
</head>
<body>
<form>
	<table width="99%"  border="0" cellpadding="0" cellspacing="0" class="table">
		<tr class="tab_title">
			<td class="title_white"><span>员工WEB浏览Top<s:property value="topN"/></span>&nbsp;&nbsp;
                    <input id="startDate" name="textfield" readonly="readonly" type="text" class="bk" value="<s:property value="start"/>" size="12">&nbsp;
                    <input name="stime" type="button" class="jianbian" onClick="showCalendar('day',event)" value="▼">
                       至
                    <input id="endDate" name="textfield" readonly="readonly" type="text" class="bk" value="<s:property value="start"/>" size="12">&nbsp;
                    <input name="etime" type="button" class="jianbian" onClick="showCalendar('day',event)" value="▼">
                  	<input name="query" type="button" onclick="queryData()" class="jianbian" value="查 询">
			</td>
		</tr>
		<tr>
        	<td class="trOver_blue">
                <div align="center"><span class="style2"></span>
                	<table width="100%"  border="0" cellspacing="0" cellpadding="0">
                    	<tr>
                      		<td><div align="center">高级查询报表</div></td>
                    	</tr>
                  	</table>
                </div>
           	</td>
        </tr>
        <tr>
			<td class="tr_white" colspan="2">
			<div align="center" id="load" style="display:none"><span class="style2"> 
			<img border="0" id="chartData" src="../images/loading.gif" /></span></div>
			<div align="center" id="tableData" style="display:none">
				
			</div>
		</td>
	</tr>
	</table>
</form>
</body>
</html>