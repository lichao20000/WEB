
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>机顶盒相关报表查询</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/Js/CheckFormForm.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
$(function()
{
	gwShare_change("city","-1");
	gwShare_change_select("vendor","-1");
});
function gwShare_change(type,selectvalue){
	switch (type){
		case "city":
			var url = "<s:url value="/gtms/stb/resource/stbReport!getCityNextChild.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='gwShare_cityId']"),selectvalue);
				$("select[@name='cityId']").html("<option value='-1'>==请先选择属地==</option>");
			});
			break;
		case "cityid":
			var url = "<s:url value='/gtms/stb/resource/stbReport!getCityNext.action'/>";
			var cityId = $("select[@name='gwShare_cityId']").val();
			if("-1"==cityId){
				$("select[@name='gwShare_cityId']").html("<option value='-1'>==请先选择属地==</option>");
				break;
			}
			$.post(url,{
				cityId:cityId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='citynext']"),selectvalue);
				$("select[@name='cityId']").html("<option value='-1'>==请先选择属地==</option>");
			});
			break;
		default:
			alert("未知查询选项！");
			break;
	}	
}
function gwShare_change_select(type,selectvalue){
	switch (type){
		case "city":
			var url = "<s:url value="/gtms/stb/resource/stbReport!getCityNextChild.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='gwShare_cityId']"),selectvalue);
			});
			break;
		case "vendor":
			var url = "<s:url value="/gtms/stb/resource/stbReport!getVendor.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='gwShare_vendorId']"),selectvalue);
				$("select[@name='gwShare_deviceModelId']").html("<option value='-1'>==请先选择设备厂商==</option>");
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
			});
			break;
		case "deviceModel":
			var url = "<s:url value="/gtms/stb/resource/stbReport!getDeviceModel.action"/>";
			var vendorId = $("select[@name='gwShare_vendorId']").val();
			if("-1"==vendorId){
				$("select[@name='gwShare_deviceModelId']").html("<option value='-1'>==请先选择厂商==</option>");
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='gwShare_deviceModelId']"),selectvalue);
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
			});
			break;
		case "devicetype":
			var url = "<s:url value="/gtms/stb/resource/stbReport!getDevicetype.action"/>";
			var vendorId = $("select[@name='gwShare_vendorId']").val();
			var deviceModelId = $("select[@name='gwShare_deviceModelId']").val();
			if("-1"==deviceModelId){
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId,
				gwShare_deviceModelId:deviceModelId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='gwShare_devicetypeId']"),selectvalue);
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
function gwShare_parseMessage(ajax,field,selectvalue){
	var flag = true;
	if(""==ajax){
		return;
	}
	var lineData = ajax.split("#");
	if(!typeof(lineData) || !typeof(lineData.length)){
		return false;
	}
	field.html("");
	option = "<option value='-1' selected>==请选择==</option>";
	field.append(option);
	for(var i=0;i<lineData.length;i++){
		var oneElement = lineData[i].split("$");
		var xValue = oneElement[0];
		var xText = oneElement[1];
		if(selectvalue==xValue){
			flag = false;
			//根据每组value和text标记的值创建一个option对象
			option = "<option value='"+xValue+"' selected>=="+xText+"==</option>";
		}else{
			//根据每组value和text标记的值创建一个option对象
			option = "<option value='"+xValue+"'>=="+xText+"==</option>";
		}
		try{
			field.append(option);
		}catch(e){
			alert("设备型号检索失败！");
		}
	}
	if(flag){
		field.attr("value","-1");
	}
}
function gwShare_queryDevice()
{
    var startIp = $("select[@name='startIp']").val();
    var endIp = $("select[@name='startIp']").val();
   if(startIp!=""&&startIp!=null)
	   {
    if (!checkip(startIp)) {
		alert("请输入正确的起始IP地址！");
		return false;
	}
	   }
   if(endIp!=""&&endIp!=null){
    if (!checkip(endIp)) {
		alert("请输入正确的结束IP地址！");
		return false;
	}}

    document.gwShare_selectForm.submit();
}
function checkip(str) {
	var pattern = /^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$/;
	return pattern.test(str);
}
/*------------------------------------------------------------------------------
//函数名:		trim
//参数  :	str 待检查的字符串
//功能  :	根据传入的参数进行去掉左右的空格
//返回值:		trim（str）
//说明  :	
//描述  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function trim(str){
     return str.replace(/(^\s*)|(\s*$)/g,"");
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

</SCRIPT>
<style>
span
{
	position:static;
	border:0;
}
</style>
</head>
<body>
<form name="gwShare_selectForm" id="gwShare_selectForm" action="<s:url value="/gtms/stb/resource/stbReport!query.action"/>" target="dataForm">
<table width="100%" align="center" class="querytable">
			<tr>
				<td><img height=20 src="<s:url value='/images/bite_2.gif'/>"
					width=24> 您当前的位置：机顶盒相关报表查询</td>
			</tr>
</table>
<TABLE width="100%" class="querytable" align="center">
	<tr><td colspan="4" class="title_1" id="gwShare_thTitle">机顶盒相关报表查询</td></tr>
	<TR id="gwShare_tr21" >
		<TD align="right" class="title_2" width="15%" >属    地</TD>
		<TD align="left" width="35%">
			<select name="gwShare_cityId" class="bk" onchange="gwShare_change('cityid','-1')">
				<option value="-1">==请选择==</option>
			</select>
		</TD>
		<TD align="right" class="title_2" width="15%" >下级属   地</TD>
		<TD align="left" width="35%">
			<select name="citynext" class="bk" >
				<option value="-1">请先选择属地</option>
			</select>
		</TD>
	</TR>
	<TR id="gwShare_tr22" >
		<TD align="right" class="title_2" width="15%">厂    商</TD>
		<TD width="35%">
			<select name="gwShare_vendorId" class="bk" onchange="gwShare_change_select('deviceModel','-1')">
				<option value="-1">==请选择==</option>
			</select>
		</TD>
		<TD align="right" class="title_2" width="15%">设备型号</TD>
		<TD align="left" width="35%">
			<select name="gwShare_deviceModelId" class="bk" onchange="gwShare_change_select('devicetype','-1')">
				<option value="-1">请先选择厂商</option>
			</select>
		</TD>
	</TR>
	<TR id="gwShare_tr22" >
		<TD align="right" class="title_2" width="15%">设备版本</TD>
		<TD width="35%">
			<select name="gwShare_devicetypeId" class="bk" onchange="">
				<option value="-1">请先选择设备型号</option>
			</select>
		</TD>
		<TD align="right" class="title_2" width="15%">上线状态</TD>
		<TD width="35%">
			<select name="gwShare_onlineStatus" class="bk">
				<option value="-1">==请选择==</option>
				<option value="0">下线</option>
				<option value="1">在线</option>
			</select>
		</TD>	
	</TR>
	<TR id="gwShare_tr24"  >
		<TD align="right" class="title_2" width="15%">起始IP</TD>
		<TD width="35%">
			<input type="text" name="startIp" value="" size="25" maxlength="40" class="bk"/>
		</TD>
		<TD align="right" class="title_2" width="15%">结束IP</TD>
		<TD width="35%">
			<input type="text" name="endIp" value="" size="25" maxlength="40" class="bk"/>
		</TD>
	</TR>
	<tr >
		<td colspan="4" align="right" class="foot" width="100%">
			<div align="right">
				<button onclick="javascript:gwShare_queryDevice();" 
				name="gwShare_queryButton" > 查 询 </button>
			</div>
		</td>
	</tr>
</TABLE>
		<br>
		<div>
			<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
				scrolling="no" width="100%" src=""></iframe>
		</div>
</form>
</body>
</html>