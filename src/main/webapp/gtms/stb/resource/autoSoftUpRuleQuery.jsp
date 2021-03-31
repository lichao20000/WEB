<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>机顶盒ROM版本自动升级策略查询</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT type="text/javascript" SRC="<s:url value="/Js/CheckFormForm.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>

<script type="text/javascript">
function query()
{
	document.selectForm.submit();
}

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

$(function(){
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
});

// 厂商改变触发
function vendorChange()
{
	var vendorId = $("select[@name='vendorId']").val();
	var url = "<s:url value='/gtms/stb/resource/autoSoftUpRule!getDeviceModelS.action'/>";
	
	$.post(url,{
		vendorId:vendorId
		},function(ajax){
			$("select[@name='deviceModelId']").empty();
			$("select[@name='deviceModelId']").append("<option value='-1'>请选择型号</option>");
			
			if(ajax!=""){
				var lineData = ajax.split("#");
				if(typeof(lineData)&&typeof(lineData.length)){
					
					for(var i=0;i<lineData.length;i++){
						var opv = lineData[i].split("$");
						var optionValue = "<option value='"+opv[0]+"'>"+opv[1]+"</option>";
						$("select[@name='deviceModelId']").append(optionValue);
					}
				}
			}
			
			$("select[@name='softwareversion']").empty();
			$("select[@name='softwareversion']").append("<option value='-1'>请软件版本</option>");
			
			$("select[@name='hardwareversion']").empty();
			$("select[@name='hardwareversion']").append("<option value='-1'>请选择硬件版本</option>");
		});
}

//型号改变触发
function deviceModelChange()
{
	var vendorId = $("select[@name='vendorId']").val();
	var deviceModelId = $("select[@name='deviceModelId']").val();
	
	var url = "<s:url value='/gtms/stb/resource/autoSoftUpRule!getHardwareversionS.action'/>";
	$.post(url,{
		deviceModelId:deviceModelId
		},function(ajax){
			$("select[@name='hardwareversion']").empty();
			$("select[@name='hardwareversion']").append("<option value='-1'>请选择硬件版本</option>");
			
			if(ajax!=""){
				var lineData = ajax.split("#");
				if(typeof(lineData)&&typeof(lineData.length)){
					for(var i=0;i<lineData.length;i++){
						var optionValue = "<option value='"+lineData[i]+"' >"+lineData[i]+"</option>";
						$("select[@name='hardwareversion']").append(optionValue);
					}
				}
			}
			  
			$("select[@name='softwareversion']").empty();
			$("select[@name='softwareversion']").append("<option value='-1'>请选择硬件版本</option>");
		});
}

//硬件版本改变触发
function hardwareversionChange()
{
	var vendorId = $("select[@name='vendorId']").val();
	var deviceModelId = $("select[@name='deviceModelId']").val();
	var hardwareversion = $("select[@name='hardwareversion']").val();
	var url = "<s:url value='/gtms/stb/resource/autoSoftUpRule!getSoftwareversionS.action'/>";
	
	$.post(url,{
		deviceModelId:deviceModelId,
		hardwareversion:hardwareversion
		},function(ajax){
			$("select[@name='softwareversion']").empty();
			$("select[@name='softwareversion']").append("<option value='-1'>请选择软件版本</option>");
			
			if(ajax!=""){
				var lineData = ajax.split("#");
				if(typeof(lineData)&&typeof(lineData.length)){
					for(var i=0;i<lineData.length;i++){
						var optionValue = "<option value='"+lineData[i]+"'>"+lineData[i]+"</option>";
						$("select[@name='softwareversion']").append(optionValue);
					}
				}
			}
		});
}
</script>
</head>

<body>
<form id="form" name="selectForm" target="dataForm"
	action="<s:url value='/gtms/stb/resource/autoSoftUpRule!queryRuleList.action'/>" >
	<input type="hidden" name="showType" value="<s:property value='showType'/>">
	<table>
		<tr>
			<td HEIGHT=20>&nbsp;</td>
		</tr>
		<tr>
			<td>
				<table class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">机顶盒ROM版本自动升级策略查询</td>
						<td>
							<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12" />
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table class="querytable">
					<tr>
						<th colspan="4">机顶盒ROM版本自动升级策略查询</th>
					</tr>
					<TR>
						<TD class=column width="15%" align='right'>厂商</TD>
						<TD width="35%">
							<s:select list="vendorList" name="vendorId"
								headerKey="-1" headerValue="请选择厂商" listKey="vendor_id"
								listValue="vendor_add" value="vendorId" cssClass="bk"
								onchange="vendorChange()" theme="simple">
							</s:select>
						</TD>
						<TD class=column width="15%" align='right'>型号</TD>
						<TD width="35%">
							<select name="deviceModelId" onchange="deviceModelChange()">
								<option value="-1">请选择型号</option>
							</select>
						</TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>硬件版本</TD>
						<TD width="35%">
							<select name="hardwareversion" onchange="hardwareversionChange()">
								<option value="-1">请选择硬件版本</option>
							</select>
						</TD>
						<TD class=column width="15%" align='right'>软件版本</TD>
						<TD width="35%">
							<select name="softwareversion" onchange="selectVersion()">
								<option value="-1">请选择软件版本</option>
							</select>
						</TD>
					</TR>
					<TR>
						<td colspan="4" align="right" class=foot>
							<button onclick="query()">&nbsp;查 询&nbsp;</button>
						</td> 
					</TR>
				</table>
			</td>
		</tr>
		<tr>
			<td height="25" id="resultStr"></td>
		</tr>
		<tr>
			<td>
				<iframe id="dataForm" name="dataForm" 
					height="0" frameborder="0" scrolling="no" width="100%" src=""></iframe>
			</td>
		</tr>
	</table>
</form>
</body>
</html>
