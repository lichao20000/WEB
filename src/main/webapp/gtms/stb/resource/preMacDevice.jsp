<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>MAC前缀反推机顶盒信息</title>
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<SCRIPT type="text/javascript" SRC="<s:url value="/Js/CheckFormForm.js"/>"></SCRIPT>
		<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
		<script type="text/javascript">
function query(){
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
	//setValue();
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
});


// 新增
function add(){
	var page = "<s:url value='/gtms/stb/resource/preMacDeviceAdd.jsp'/>";
	window.open(page,"","left=20,top=20,width=700,height=350,resizable=yes,scrollbars=yes");
}

// 厂商改变触发
function vendorChange(){
	var vendorId = $("select[@name='vendorId']").val();
	var url = "<s:url value='/gtms/stb/resource/PreMacDeviceACT!getDeviceModelS.action'/>";
	
	$.post(url,{
		vendorId:vendorId
		},function(ajax){
			if(ajax!=""){
				var lineData = ajax.split("#");
				if(typeof(lineData)&&typeof(lineData.length)){
					$("select[@id='deviceModelId']").empty();
					var optionValue = "<option value='-1' >请选择型号</option>";
					$("select[@id='deviceModelId']").append(optionValue);
					for(var i=0;i<lineData.length;i++){
						var oneElement = lineData[i].split("$");
						var xValue = oneElement[0];
						var xText = oneElement[1];
						var optionValue = "<option value='"+xValue+"' >"+xText+"</option>  ";
						$("select[@id='deviceModelId']").append(optionValue);
					}
				}else{
					$("select[@id='deviceModelId']").empty();
					var optionValue = "<option value='-1' >请选择型号</option>";
					$("select[@id='deviceModelId']").append(optionValue);
				}
			}else{
				$("select[@id='deviceModelId']").empty();
				var optionValue = "<option value='-1' >请选择型号</option>";
				$("select[@id='deviceModelId']").append(optionValue);
			}
			
			$("select[@id='softwareversion']").empty();
			var optionValue1 = "<option value='-1' >请软件版本</option>";
			$("select[@id='softwareversion']").append(optionValue1);
			
			$("select[@id='hardwareversion']").empty();
			var optionValue2 = "<option value='-1' >请选择硬件版本</option>";
			$("select[@id='hardwareversion']").append(optionValue2);
		});
}

// 型号改变触发
function deviceModelChange(){
	var vendorId = $("select[@name='vendorId']").val();
	var deviceModelId = $("select[@name='deviceModelId']").val();
	
	var url = "<s:url value='/gtms/stb/resource/PreMacDeviceACT!getSoftwareversionS.action'/>";
	
	$.post(url,{
		vendorId:vendorId,
		deviceModelId:deviceModelId
		},function(ajax){
			if(ajax!=""){
				var lineData = ajax.split("#");
				if(typeof(lineData)&&typeof(lineData.length)){
					$("select[@id='softwareversion']").empty();
					var optionValue = "<option value='-1' >请选择软件版本</option>";
					$("select[@id='softwareversion']").append(optionValue);
					for(var i=0;i<lineData.length;i++){
						var optionValue = "<option value='"+lineData[i]+"' >"+lineData[i]+"</option>  ";
						$("select[@id='softwareversion']").append(optionValue);
					}
				}else{
					$("select[@id='softwareversion']").empty();
					var optionValue = "<option value='-1' >请选择软件版本</option>";
					$("select[@id='softwareversion']").append(optionValue);
				}
			}else{
				$("select[@id='softwareversion']").empty();
				var optionValue = "<option value='-1' >请选择软件版本</option>";
				$("select[@id='softwareversion']").append(optionValue);
			}
			
			$("select[@id='hardwareversion']").empty();
			var optionValue1 = "<option value='-1' >请选择硬件版本</option>";
			$("select[@id='hardwareversion']").append(optionValue1);
		});
}

//软件版本改变触发
function softwareversionChange(){
	var vendorId = $("select[@name='vendorId']").val();
	var deviceModelId = $("select[@name='deviceModelId']").val();
	var softwareversion = $("select[@name='softwareversion']").val();
	var url = "<s:url value='/gtms/stb/resource/PreMacDeviceACT!getHardwareversionS.action'/>";
	
	$.post(url,{
		vendorId:vendorId,
		deviceModelId:deviceModelId,
		softwareversion:softwareversion
		},function(ajax){
			if(ajax!=""){
				var lineData = ajax.split("#");
				if(typeof(lineData)&&typeof(lineData.length)){
					$("select[@id='hardwareversion']").empty();
					var optionValue = "<option value='-1' >请选择硬件版本</option>";
					$("select[@id='hardwareversion']").append(optionValue);
					for(var i=0;i<lineData.length;i++){
						var optionValue = "<option value='"+lineData[i]+"' >"+lineData[i]+"</option>  ";
						$("select[@id='hardwareversion']").append(optionValue);
					}
				}else{
					$("select[@id='hardwareversion']").empty();
					var optionValue = "<option value='-1' >请选择硬件版本</option>";
					$("select[@id='hardwareversion']").append(optionValue);
				}
			}else{
				$("select[@id='hardwareversion']").empty();
				var optionValue = "<option value='-1' >请选择硬件版本</option>";
				$("select[@id='hardwareversion']").append(optionValue);
			}
		});
}
</script>
</head>

<body>
<input type="hidden" name="instArea" value="<s:property value="instArea"/>" />
<form id="form" name="selectForm" target="dataForm"
	action="<s:url value='/gtms/stb/resource/PreMacDeviceACT!queryMacDeviceTypeList.action'/>" >
	<table>
		<tr>
			<td HEIGHT=20>&nbsp;</td>
		</tr>
		<tr>
			<td>
				<table class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">MAC反推机顶盒信息</td>
						<td>
							<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12" />
							查询MAC前缀反推机顶盒信息
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table class="querytable">
					<tr>
						<th colspan="4">MAC前缀反推机顶盒信息管理</th>
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
							<select name="deviceModelId"  cssClass="bk"  id="deviceModelId" onchange="deviceModelChange()">
								<option value="-1">请选择型号</option>
							</select>
						</TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>软件版本</TD>
						<TD width="35%">
							<select name="softwareversion"  cssClass="bk"  id="softwareversion" onchange="softwareversionChange()">
								<option value="-1">请选择软件版本</option>
							</select>
						</TD>
						<TD class=column width="15%" align='right'>硬件版本</TD>
						<TD width="35%">
							<select name="hardwareversion"  cssClass="bk"  id="hardwareversion">
								<option value="-1">请选择硬件版本</option>
							</select>
						</TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>MAC前缀</TD>
						<TD width="35%">
							<input type="text" name="preMac" id="preMac" class="bk" value="" size="40">
						</TD>
					</TR>
					<TR>
						<td colspan="4" align="right" class=foot>
							<s:if test="instArea=='hn_lt'">
								<button onclick="query()">&nbsp;查 询&nbsp;</button>
									&nbsp;&nbsp;&nbsp;&nbsp;
							</s:if>
							<s:else>
								<button onclick="add()">&nbsp;新增&nbsp;</button>
								&nbsp;&nbsp;&nbsp;&nbsp;
								<button onclick="query()">&nbsp;查 询&nbsp;</button>
							</s:else>	
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
				<iframe id="dataForm" name="dataForm" height="0" frameborder="0" scrolling="no" width="100%" src=""></iframe>
			</td>
		</tr>
	</table>
</form>
</body>
</html>
