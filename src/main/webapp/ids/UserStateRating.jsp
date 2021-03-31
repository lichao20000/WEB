<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>用户当前网络环境评级</title>
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
		document.selectForm.action="<s:url value='/ids/UserStateRating!queryPonStatusByES.action'/>";
		document.selectForm.submit();
		
	}
</script>
</head>
</html>
<body>
	<form id="form" name="selectForm" method="post" action="" target="dataForm">
		<input type="hidden" id="ctype" value="1">
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<th>用户网络状态评级</th>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">
						<tr leaf="simple">
							<td align="right" width="10%" class="column">开始时间</td>
							<td width="25%"><input type="text" name="startTime" readonly
								class=bk value="<s:property value="startDate" />"> <img name="shortdateimg"
								onClick="WdatePicker({el:document.selectForm.startTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../images/dateButton.png" height="12" border="0" alt="选择" />
							</td>
							<td align="right" width="10%" class="column">结束时间</td>
							<td width="25%"><input type="text" name="endTime" readonly
								class=bk value="<s:property value="endDate" />"> <img name="shortdateimg"
								onClick="WdatePicker({el:document.selectForm.endTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../images/dateButton.png" height="12" border="0" alt="选择" />
							</td>
							<td align="right" width="10%" class="column">查询时间类型</td>
							<td width="20%"><select id="queryTimeType" name="quertTimeType">
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
								<button onclick="query()">&nbsp;查 询&nbsp;</button>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td height="25"></td>
			</tr>
			<tr>
				<td height="5"></td>
			</tr>
			<tr>
				<td>
					<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
						scrolling="no" width="100%" src=""></iframe>
				</td>
			</tr>
		</table>
		<br>
	</form>
</body>
</html>
<%@ include file="../foot.jsp"%>