<%--
Author		: Alex.Yan (yanhj@lianchuang.com)
Date		: 2007-11-28
Desc		: TR069: devicetype_list, add;
			  SNMP:	 Ddevicetype list.
--%>

<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<html>
<head>

	<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">

<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<lk:res />
	<style type="text/css">
		table, th, td {
	    border: none;
	    border-collapse: inherit !important;
	}
	</style>
<SCRIPT LANGUAGE="JavaScript">


//-----------------ajax----------------------------------------
  var request = false;
  var portNumber=1;
  var addPortNumber=0;
   try {
     request = new XMLHttpRequest();
   } catch (trymicrosoft) {
     try {
       request = new ActiveXObject("Msxml2.XMLHTTP");
       
     } catch (othermicrosoft) {
       try {
         request = new ActiveXObject("Microsoft.XMLHTTP");
       } catch (failed) {
         request = false;
       }  
     }
   }
   if (!request)
     alert("Error initializing XMLHttpRequest!");
   
   //ajax一个通用方法
	function sendRequest(method,url,object){
		request.open(method, url, true);
		request.onreadystatechange = function(){refreshPage(object);};
		request.send(null);
	}
	function refreshPage(object){
		if (request.readyState == 4) {
    		if (request.status == 200) {
        		object.innerHTML = request.responseText;
			} else{
				alert("status is " + request.status);
			}
		}
	}

function Init(){
	// 初始化厂家
	/* 
	gwShare_queryChange("2");
	var editDeviceType = $("input[@name='editDeviceType']").val(); */
	// 普通方式提交
	var form = document.getElementById("mainForm");
	form.action = "<s:url value='/itms/resource/servTemplate!queryList.action?cm=1'/>";
	form.submit();
	dyniframesize();
}



function showChild(vendor_id,device_mode_id){
//	var oui = document.all(vendor_id).value;
//	var url = "getDeviceModel.jsp?oui=" + oui + "&device_mode=" + device_mode;
	var vendorId = document.all(vendor_id).value;
	var url = "getDeviceModel.jsp?vendor_id=" + vendorId + "&device_mode_id=" + device_mode_id;
	var divObj = document.getElementById("deviceModel");
	sendRequest("post",url,divObj);
}


$(function(){
	Init();
});
	
	
function setValue(){
	theday=new Date();
	day=theday.getDate();
	month=theday.getMonth()+1;
	year=theday.getFullYear();
	//document.getElementById("startTime").value=year+"-"+month+"-"+day+" 00:00:00";
	//document.getElementById("endTime").value=year+"-"+month+"-"+day+" 23:59:59";
	//document.selectForm.startTime.value = year+"-"+month+"-"+day+" 00:00:00";
	//document.selectForm.endTime.value = year+"-"+month+"-"+day+" 23:59:59";
	document.getElementById("startTime").value="";
	document.getElementById("endTime").value="";
}

function addDevice()
{     
	var returnVal = window.open("<s:url value='/itms/resource/servTemplate!queryDetail4Add.action?cm=1' />","","height=800,width=1500,status=yes,toolbar=no,menubar=no,location=no");
	/* if(typeof(returnVal)=='undefined'){
		console.log("==undefined");
		returnVal = window.returnValue;
		deviceResult(returnVal);
		//return;
	}else{
		console.log("!=undefined");
		deviceResult(returnVal);
	} */
}

function deviceResult(returnVal){
	Init();
}

function  deleteCurrentRow(obj)
{
  var isDelete=confirm("真的要删除吗？");  
if(isDelete){
 var tr=obj.parentNode.parentNode;   
 var tbody=tr.parentNode;   
 tbody.removeChild(tr);   
 portNumber--;
}
}

</SCRIPT>
</head>
<%-- <%@ include file="/toolbar.jsp"%>
<%@ include file="./DeviceType_Info_util.jsp"%> --%>
<body>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<FORM NAME="mainForm" id="mainForm" METHOD="post" ACTION=""
			target="dataForm">
		<input type="hidden" name="gw_type" value="<s:property value='gw_type'/>">
		<table width="98%" height="30" border="0" align="center"
			cellpadding="0" cellspacing="0" class="green_gargtd">
			<tr>
				<td width="162">
				<div align="center" class="title_bigwhite">模板管理</div>
				</td>
				<td><img src="/itms/images/attention_2.gif" width="15"
					height="12">编辑模板信息</td>
				<td align="right"></td>
			</tr>
		</table>
		<!-- 高级查询part -->
		<TABLE border=1px cellspacing=0 cellpadding=0 width="98%" align="center" style="border-bottom-width: 10px;border-top-width: 10px;border-left-width: 10px;border-right-width: 10px;border-color: red;">
			<tr>
				<td bgcolor=#999999>
					<table border=1px cellspacing=1 cellpadding=2 width="100%" style="border-bottom-width: 10px;border-top-width: 10px;border-left-width: 10px;border-right-width: 10px;border-color: red;"
						align="center">
						<tr>
							<input type="hidden" name="paraValue" id='paraValue' />
							<th colspan="4" id="gwShare_thTitle">业务模板查询</th>
						</tr>
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<TD class="column text" nowrap align="center" width="15%">模板名称:</TD>
							<TD colspan="1" class="column text" nowrap width="35%">
								<INPUT TYPE="text" NAME="name" class=bk maxlength=30 style="font-size: 12px;" size=20>
							</TD>
							<TD class="column text" nowrap align="center" width="15%">业务类型:</TD>
							<TD colspan="1" class="column text" align="left">
								<select name="serv" id="serv" class=bk style="color: black;font-size: 12px">
									<option value="" style="color: black;font-size: 12px">全 部</option>
									<option value="10" style="color: black;font-size: 12px">宽 带</option>
									<option value="11" style="color: black;font-size: 12px">IPTV</option>
									<option value="14" style="color: black;font-size: 12px">语 音</option>
									<option value="69" style="color: black;font-size: 12px">TR069</option>
								</select>
							</TD>
						</TR>
						
					
						<tr bgcolor="#FFFFFF">
							<td colspan="4" align="right" class="green_foot" width="100%">
								<input type="button" onclick="javascript:addDevice()" class=jianbian name="add_btn" value=" 新 增 " />
								<input type="button" onclick="javascript:queryDevice()" class=jianbian value=" 查 询 " /> 
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</FORM>
	
	<!-- 展示结果part -->
	<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#FFFFFF id="idData"><iframe id="dataForm"
				name="dataForm" height="0" frameborder="0" scrolling="no"
				width="100%" src=""></iframe></TD>
		</TR>
	</TABLE>
		
	</TD>
	</TR>
</TABLE>
</body>
<%@ include file="/foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">



//查询
function queryDevice()
{
	// 普通方式提交
	var form = document.getElementById("mainForm");
	form.action = "<s:url value='/itms/resource/servTemplate!queryList.action?cm=1'/>";
	form.submit();
}

function CheckForm(){   
   return true;
}

//更改设备型号
function change_model(type,selectvalue){
	switch (type){
		case "deviceModel":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModel.action"/>";
			var vendorId = $("select[@name='vendor_add']").val();
			if("-1"==vendorId){
				$("select[@name='device_model_add']").html("<option value='-1'>==请先选择设备厂商==</option>");
				//$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				//$("select[@name='device_model']").html("<option value='-1'>==请先选择设备型号==</option>");
				gwShare_parseMessage(ajax,$("select[@name='device_model_add']"),selectvalue);
			});
			break;
		default:
			alert("未知查询选项！");
			break;
	}	
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
	for (var i=0; i<iframeids.length; i++)
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
    		tempobj.style.display="block";
		}
	}
}

function disableDeviceType(tag){
	$("select[@name='rela_dev_type_add']").attr("disabled",tag);
}


//隐藏页面下面的编辑设备类型区域
function showEditDeviceTypePart(tag)
{
	if(tag){
		$("table[@id='editDeviceTypeTable']").show();
	}else{
		$("table[@id='editDeviceTypeTable']").hide();
	}
}

// 隐藏页面下面的添加区域
function showAddPart(tag)
{
	if(tag)
		$("table[@id='addTable']").show();
	else
		$("table[@id='addTable']").hide();
}
// 隐藏页面下面的审核区域
function showCheckPart(tag)
{
	if(tag)
		$("#checkTable").show();
	else
		$("#checkTable").hide();
}

$(function(){
	//setValue();
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
}); 

/** 工具方法 **/
/*LTrim(string):去除左边的空格*/
function LTrim(str){
    var whitespace = new String("　 \t\n\r");
    var s = new String(str);   

    if (whitespace.indexOf(s.charAt(0)) != -1){
        var j=0, i = s.length;
        while (j < i && whitespace.indexOf(s.charAt(j)) != -1){
            j++;
        }
        s = s.substring(j, i);
    }
    return s;
}
/*RTrim(string):去除右边的空格*/
function RTrim(str){
    var whitespace = new String("　 \t\n\r");
    var s = new String(str);
 
    if (whitespace.indexOf(s.charAt(s.length-1)) != -1){
        var i = s.length - 1;
        while (i >= 0 && whitespace.indexOf(s.charAt(i)) != -1){
            i--;
        }
        s = s.substring(0, i+1);
    }
    return s;
}
/*Trim(string):去除字符串两边的空格*/
function trim(str){
    return RTrim(LTrim(str)).toString();
}


//全部trim
function trimAll()
{
	var inputs = document.getElementsByTagName("input");
	for(var i=0;i<inputs.length;i++)
	{
		var input = inputs[i];
		if(/text/gi.test(input.type))
		{
			input.value = trim(input.value);
		}
	}
}

</SCRIPT>
</html>