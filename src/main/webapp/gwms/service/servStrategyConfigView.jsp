<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s"  uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%
	String gw_type = request.getParameter("gw_type");
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>策略配置查询</title>
		<%
			/**
			 * 策略配置查询
			 * 
			 * @author qixueqi(4174)
			 * @version 1.0
			 * @since 2010-05-09
			 * @category
			 */
		%>
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<script type="text/javascript"
			src="../../Js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
var gw_type = "<%= gw_type%>"
$(function(){
	taskId = "<s:property value="taskId"/>";
	if(taskId!=""&&taskId!=null){
		document.selectForm.submit();
	}else{
	setValue(1);
	}
});
	
function query(){
	var device_serialnumber = $.trim($("input[@name='device_serialnumber']").val());
	var username = $.trim($("input[@name='username']").val());
	if(""!=device_serialnumber && device_serialnumber.length<6){
		alert("请至少输入设备序列号后六位！");
		return false;
	}
	if(""==device_serialnumber && ""==username){
		alert("用户帐号或设备序列号至少输入一项!");
		return false;
	}
	document.selectForm.submit();
}

function setValue(init){
	if(1==init)
	{
		theday=new Date();
		day=theday.getDate();
		month=theday.getMonth()+1;
		year=theday.getFullYear();
		
		document.selectForm.selectType.value = "1";
		document.selectForm.time_start.value = year+"-"+month+"-"+day+" 00:00:00";
		document.selectForm.time_end.value = year+"-"+month+"-"+day+" 23:59:59";
	}else{
		document.selectForm.selectType.value = "0";
		document.selectForm.time_start.value = "";
		document.selectForm.time_end.value = "";
	}
	//document.selectForm.status.value = "";
	//document.selectForm.vendor_id.value = "";
	//document.selectForm.service_id.value = "";
}

//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids=["dataForm"]

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide="yes"

function dyniframesize() 
{
	var dyniframe=new Array()
	for (i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//自动调整iframe高度
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
     			dyniframe[i].style.display="block"
     			//如果用户的浏览器是NetScape
     			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
      				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
      			//如果用户的浏览器是IE
     			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) 
      				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
   			 }
   		}
		//根据设定的参数来处理不支持iframe的浏览器的显示问题
		if ((document.all || document.getElementById) && iframehide=="no")
		{
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
    		tempobj.style.display="block"
		}
	}
}

$(function(){
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
}); 

</script>
		<style type="text/css">
<!--
select {
	position: relative;
	font-size: 12px;
	width: 160px;
	line-height: 18px;
	border: 0px;
	color: #909993;
}
-->
</style>
	</head>

	<body>
		<form name="selectForm" action="<s:url value="/gwms/service/servStrategyView.action"/>" target="dataForm">
			<input type="hidden" name="selectType" value="0" />
			<input type="hidden" name="taskId" value="<s:property value="taskId"/>" />
			<table>
				<tr>
					<td HEIGHT=20>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table class="green_gargtd">
							<tr>
								<th>
									策略配置查询
								</th>
								<td>
									<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">页面查询时间为策略定制时间！
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table class="querytable">
							<tr leaf="simple">
								<th colspan="4">查询</th>
							</tr>

							<TR leaf="simple">
								<TD class=column width="10%" align='right'>
									用户账号
								</TD>
								<TD width="30%">
									<input type="input" name="username" size="20" class=bk />
								</TD>
								<TD class=column width="10%" align='right'>
									设备序列号
								</TD>
								<TD width="50%">
									<input type="input" name="device_serialnumber" size="20" class=bk />
									请至少输入设备序列号后六位！
								</TD>
							</TR>
							<tr leaf="high" style="display: ">
								<td class=column width="10%" align='right'>
									起始时间
								</td>
								<td width="30%">
									<input type="text" name="time_start" readonly class=bk>
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.time_start,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="选择" />
								</td>
								<td class=column width="10%" align='right'>
									截止时间
								</td>
								<td width="50%">
									<input type="text" name="time_end" readonly class=bk>
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.time_end,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="选择" />
								</td>
							</tr>
							<TR leaf="simple">
				<td class=column width="15%" align='right'>策略类型</td>
				<td width="35%">
					<select name="strategyType">
						<option value="-1">--请选择--</option>
						<option value="1" selected>业务下发</option>
						<option value="2">批量软件升级</option>
						<option value="3">批量配置</option>
						<option value="4">简单软件升级</option>
						<option value="5">恢复出厂设置</option>
					</select>
				</td>
			</TR>
							<TR>
								<td colspan="4" align="right" class=foot>
									<button onclick="query()">&nbsp;查  询&nbsp;</button>
								</td>
							</TR>
						</table>
					</td>
				</tr>
				<tr>
					<td height="25">
					</td>
				</tr>
				<tr>
					<td>
						<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
							scrolling="no" width="100%"
							src=""></iframe>
					</td>
				</tr>
			</table>
			<br>
		</form>
	</body>
</html>
<%@ include file="../foot.jsp"%>