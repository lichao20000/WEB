<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="/toolbar.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title>Mq监听</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css"/>
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css"/>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
			src="../../Js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<s:url value="/Js/echarts-all.js"/>"></script>
<script type="text/javascript">
	function queryMqList() {
		var starttime = $.trim($("input[@name='starttime']").val());
		alert(starttime);
		var endtime = $.trim($("input[@name='endtime']").val());
		alert(endtime);
		var barScript = document.createElement("script");
		barScript.type = "text/javascript";
		barScript.src = "../../Js/lineOfMq.js";
		document.body.appendChild(barScript);
	}
function time()
{
	var starttime = $.trim($("input[@name='starttime']").val());
	var endtime = $.trim($("input[@name='endtime']").val());
	var url = "<s:url value='/itms/report/queryMq!bijiao.action' />";
	$.post(url,{
		starttime:starttime,
		endtime:endtime
    },function(ajax){
    	if(ajax==1)
    		{
    		alert(1);
    		queryMqList();
    		}
    	else
    		{
    		alert("时间跨度过长，开始时间与结束时间间隔1周内！");
    		}
    });	
}
</script>
</head>

<body>
	<TABLE>
		<tr>
			<td>
				<table class="green_gargtd">
					<tr>
						<th>MQ折线图查询</th>
						<td align="left"><img
							src="<s:url value="/images/attention_2.gif"/>" width="15"
							height="12" /></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<form name="frm" id="frm" target="dataForm">
					<table class="querytable">
						<tr>
							<th colspan=4>MQ查询</th>
						</tr>
						<tr bgcolor=#ffffff>
							<td class=column align="center" width="15%">开始时间</td>
							<td width="35%"><input type="text" name="starttime"
								class='bk' readonly value="<s:property value='starttime'/>" /> <img
								name="shortDateimg"
								onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择" /></td>
							<td class=column align="center" width="15%">结束时间</td>
							<td width="35%"><input type="text" name="endtime"
								class='bk' readonly value="<s:property value='endtime'/>" /> <img
								name="shortDateimg"
								onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择" /></td>
						</tr>
						<tr bgcolor=#ffffff>
							<td class=column align="center" width="15%">MQ设备</td>
							<td width="35%"><s:select list="mqList" name="mqId"
									listKey="id" listValue="mq_ipaddress"  cssClass="select" style="width:120px;"></s:select>
							</td>
							<td class=column align="center" width="15%">MQ主题</td>
							<td width="35%"><s:select list="topicNameList" name="topicName" 
									listKey="id" listValue="topic_name"  cssClass="select" style="width:120px;"></s:select>
							</td>
						</tr>
						<tr bgcolor=#ffffff>
							<td class=foot colspan=4 align=right>
								<button id="queryButton" onclick="time();">&nbsp;查询&nbsp;</button>
							</td>
						</tr>
					</table>
				</form>
			</td>
		</tr>
	</TABLE>
	<div class="right-chart">
					<div id="line" style="height: 540px;"></div>
				</div>
	<div class="content">
			<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
				scrolling="no" width="100%" src=""></iframe>
		</div>
</body>
<%@ include file="/foot.jsp"%>
</html>
