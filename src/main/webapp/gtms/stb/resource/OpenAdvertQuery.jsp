<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>机顶盒历史配置信息查询</title>
<lk:res />
<link href="/lims/css/css_blue.css" rel="stylesheet" type="text/css">

<link href="/lims/css2/global.css" rel="stylesheet" type="text/css">
<link href="/lims/css2/c_table.css" rel="stylesheet" type="text/css">
<%
	String vendorId = request.getParameter("vendorId");
	if(vendorId==null||"".equals(vendorId)){
		vendorId ="-1";
	}
// 	String queryFlag = request.getParameter("queryFlag");
%>
<script type="text/javascript">

	function doQuery(){
		var taskid = $("input[@name='taskId']").val();
		var taskname = $("input[@name='taskName']").val();
		if(taskid=="" && taskname==""){
			alert("任务名称、任务编号必须填一个");
			return;
		}
		document.selectForm.action = "<s:url value='/gtms/stb/resource/openAdvertQuery!queryAdvertResultCount.action'/>";
		document.selectForm.submit();
	}

$(function(){
	var vendorId = "<%=vendorId%>";
	deviceSelect_change_select("vendor",vendorId);
	
});

/*------------------------------------------------------------------------------
//函数名:		deviceSelect_change_select
//参数  :	type 
	            vendor      加载设备厂商
	            deviceModel 加载设备型号
	            devicetype  加载设备版本
//功能  :	加载页面项（厂商、型号级联）
//返回值:		
//说明  :	
//描述  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function deviceSelect_change_select(type,selectvalue){
	switch (type){
		case "vendor":
			var url = "<s:url value='/gtms/stb/resource/openAdvertQuery!getVendor.action'/>";
			$.post(url,{
			},function(ajax){
				deviceSelect_parseMessage(ajax,$("select[@name='vendorId']"),selectvalue);
				$("select[@name='deviceModelId']").html("<option value='-1'>==请先选择设备厂商==</option>");
				$("select[@name='deviceTypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
			});
			break;
		case "deviceModel":
			var url = "<s:url value='/gtms/stb/resource/openAdvertQuery!getDeviceModel.action'/>";
			var vendorId = $("select[@name='vendorId']").val();
			$.post(url,{
				vendorId:vendorId
			},function(ajax){
				deviceSelect_parseMessage(ajax,$("select[@name='modelId']"),selectvalue);
				$("select[@name='deviceTypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
			});
			break;
		default:
			alert("未知查询选项！");
			break;
	}
}
	/*------------------------------------------------------------------------------
	//函数名:		deviceSelect_parseMessage
	//参数  :	ajax 
	            	类似于XXX$XXX#XXX$XXX
	            field
	            	需要加载的jquery对象
	//功能  :	解析ajax返回参数
	//返回值:		
	//说明  :	
	//描述  :	Create 2009-12-25 of By qxq
	------------------------------------------------------------------------------*/
	//解析查询设备型号返回值的方法
	function deviceSelect_parseMessage(ajax, field, selectvalue) {
		var flag = true;
		if ("" == ajax) {
			return;
		}
		var lineData = ajax.split("#");
		if (!typeof (lineData) || !typeof (lineData.length)) {
			return false;
		}
		field.html("");
		if (selectvalue == "-1") {
			field.append("<option value='-1' selected>==请选择==</option>");
		} else {
			field.append("<option value='-1'>==请选择==</option>");
		}
		for ( var i = 0; i < lineData.length; i++) {
			var oneElement = lineData[i].split("$");
			var xValue = oneElement[0];
			var xText = oneElement[1];
			if (selectvalue == xValue) {
				flag = false;
				//根据每组value和text标记的值创建一个option对象
				option = "<option value='"+xValue+"' selected>==" + xText
						+ "==</option>";
			} else {
				//根据每组value和text标记的值创建一个option对象
				option = "<option value='"+xValue+"'>==" + xText
						+ "==</option>";
			}
			try {
				field.append(option);
			} catch (e) {
				alert("设备型号检索失败！");
			}
		}
		if (flag) {
			field.attr("value", "-1");
		}
	}
</script>
</head>
<body>
	<form id="selectForm" name="selectForm" action="" target="first"
		method="post">
		<input type="hidden" name="status" value="6">
		<table class="querytable" align="center" width="98%" id="tabs">
			<tr>
				<td class="title_1" colspan="4">开机广告下发结果统计</td>
			</tr>
			<tr>
				<td class="title_2" width="15%">任务名称：</td>
				<td><input type="text" maxlength="50" name="taskName"
					id="taskname" /></td>
				<td class="title_2" width="15%">任务编号：</td>
				<td><input type="text" maxlength="50" name="taskId"
					id="taskid" /></td>
			</tr>
			<tr>
				<td class="title_2" width="15%">厂商：</td>
				<td><select name="vendorId" class="bk"
					onchange="deviceSelect_change_select('deviceModel','-1')"
					style="width: 200px">
					<option value="-1">
						==请选择==
					</option>
				</select></td>

				<td class="title_2" width="15%">型号</td>
				<td><select name="modelId" class="bk" style="width: 200px">
						<option value="-1">==请先选择厂商==</option>
				</select></td>
			</tr>
			<tr align="right">
				<td colspan="4" class="foot" align="right">
					<div class="right">
						<button type="button" onclick="doQuery()">查询</button>
						&nbsp;&nbsp;
					</div>
				</td>
			</tr>

		</table>
	</form>


	<TABLE width="98%" border=0 cellspacing=1 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999 id="td"><iframe id="first" name="first"
					height="0" frameborder="0" scrolling="no" width="100%" src=""></iframe></TD>
		</TR>
	</TABLE>



</body>


<SCRIPT LANGUAGE="JavaScript">
	//** iframe自动适应页面 **//
	//输入你希望根据页面高度自动调整高度的iframe的名称的列表
	//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
	//定义iframe的ID
	var iframeids = [ "first" ];

	//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
	var iframehide = "yes";

	function dyniframesize() {
		var dyniframe = new Array()
		for (i = 0; i < iframeids.length; i++) {
			if (document.getElementById) {
				//自动调整iframe高度
				dyniframe[dyniframe.length] = document
						.getElementById(iframeids[i]);
				if (dyniframe[i] && !window.opera) {
					dyniframe[i].style.display = "block";
					//如果用户的浏览器是NetScape
					if (dyniframe[i].contentDocument
							&& dyniframe[i].contentDocument.body.offsetHeight)
						dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
					//如果用户的浏览器是IE
					else if (dyniframe[i].Document
							&& dyniframe[i].Document.body.scrollHeight)
						dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
				}
			}
			//根据设定的参数来处理不支持iframe的浏览器的显示问题
			if ((document.all || document.getElementById) && iframehide == "no") {
				var tempobj = document.all ? document.all[iframeids[i]]
						: document.getElementById(iframeids[i])
				tempobj.style.display = "block";
			}
		}
	}
	$(function() {
		dyniframesize();
	});

	$(window).resize(function() {
		dyniframesize();
	});
</SCRIPT>
</html>