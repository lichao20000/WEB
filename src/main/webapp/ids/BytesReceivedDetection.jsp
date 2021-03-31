<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>状态信息查询</title>
<link rel="stylesheet" href="../css3/c_table.css" type="text/css">
<link rel="stylesheet" href="../css3/global.css" type="text/css">
<link rel="stylesheet" href="../css/tab.css" type="text/css">
<script type="text/javascript" src="../Js/jquery.js"></script>
<script type="text/javascript" src="../Js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">


$(function(){
	//setValue();
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
});

//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids=["dataForm"];

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide="yes";

function dyniframesize() 
{
	var dyniframe=new Array();
	for (var i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//自动调整iframe高度
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
     			dyniframe[i].style.display="block";
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
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i]);
    		tempobj.style.display="block";
		}
	}
}


function query() {
	var deviceSerialnumber = $("input[@name='deviceSerialnumber']").val();
	var loid = $("input[@name='loid']").val();
	if(deviceSerialnumber=="" && loid==""){
		alert("设备序列号和loid至少一项不能为空");
		return;
	}
	
	$("button[@id='btn']").attr("disabled",true);
	$("tr[@id='trData']").show();
	$("div[@id='QueryData']").html("正在执行批量定制操作，请稍等....");
	var mainForm = 	document.getElementById("selectForm");
	mainForm.submit();
}

	
</script>
</head>
</html>
<body>
	<TABLE>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>用户网口流量及速率查询</th>
					<td><img src="<s:url value="/images/attention_2.gif"/>"
						width="15" height="12"></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name="frm" id="selectForm" method="post"
				action="<s:url value='/ids/bytesReceivedDetection!queryLanAndPonData.action'/>"
				target="dataForm">
				<table class="querytable">
					<tr>
						<th colspan="6">用户网口流量及速率查询</th>
					</tr>
					
					<tr leaf="simple">
							<td align="right" width="10%" class="column">开始时间</td>
							<td width="25%"><input type="text" name="starttime" readonly
								class=bk value="<s:property value="starttime" />"> <img name="shortdateimg"
								onClick="WdatePicker({el:document.selectForm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../images/dateButton.png" height="12" border="0" alt="选择" />
							</td>
							<td align="right" width="10%" class="column">结束时间</td>
							<td width="25%"><input type="text" name="endtime" readonly
								class=bk value="<s:property value="endtime" />"> <img name="shortdateimg"
								onClick="WdatePicker({el:document.selectForm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../images/dateButton.png" height="12" border="0" alt="选择" />
							</td>
							<td align="right" width="10%" class="column">查询时间类型</td>
							<td width="20%"><select id="queryTimeType" name="queryTimeType">
									<option value="0">==请选择==</option>
									<option value="1" selected="selected">==上报时间==</option>
									<option value="2">==入库时间==</option>
							</select></td>
						</tr>
						<tr leaf="simple">
							<td align="right" class="column">设备序列号</td>
							<td><input type="text" name="deviceSerialnumber" class=bk></td>
							<td align="right" class="column">LOID</td>
							<td><input type="text" name="loid" class=bk></td>
						</tr>
						<tr>
							<td colspan="6" align="right" class=foot>
								<button id="btn" onclick="query()">&nbsp;查 询&nbsp;</button>
							</td>
						</tr>
				</table>
			</form>
		</td>
	</tr>
	<tr id="trData" style="display: none">
		<td>
			<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
				正在努力为您查询，请稍等....</div>
		</td>
	</tr>
	<tr>
		<td height="80%"><iframe id="dataForm" name="dataForm"
				height="100%" frameborder="0" scrolling="no" width="100%" src=""></iframe></td>
	</tr>
	<tr>
		<td height="20"></td>
	</tr>
	
</TABLE>
</body>
</html>
<%@ include file="../foot.jsp"%>