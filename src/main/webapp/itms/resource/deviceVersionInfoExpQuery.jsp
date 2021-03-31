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
<html>
<head>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<lk:res />
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
/**
	function sendRequest2(method, url, object,sparam){
		request.open(method, url, true);
		request.onreadystatechange = function(){
			refreshPage(object);
			doSomething(sparam);
		};
		request.send(null);
	}
	**/
//---------------------------------------------------------------

function Init(){
	// 初始化厂家
	
	gwShare_change_select("vendor","-1");

	
	// 普通方式提交
	/**
	var form = document.getElementById("mainForm");
	 setValue();
	form.action = "<s:url value="/itms/resource/deviceTypeInfo!queryList.action"/>";
	//form.target = "dataForm";
	form.submit();
	**/
}
	
	function gwShare_change_select(type,selectvalue){
		switch (type){
			case "city":
				var url = "<s:url value="/gwms/share/gwDeviceQuery!getCityNextChild.action"/>";
				$.post(url,{
				},function(ajax){
					gwShare_parseMessage(ajax,$("select[@name='gwShare_cityId']"),selectvalue);
				});
				break;
			case "vendor":
				var url = "<s:url value="/gwms/share/gwDeviceQuery!getVendor.action"/>";
				$.post(url,{
				},function(ajax){
					gwShare_parseMessage(ajax,$("select[@name='vendor']"),selectvalue);
					gwShare_parseMessage(ajax,$("select[@name='vendor_add']"),selectvalue);
					//$("select[@name='vendor']").html("<option value='-1'>==请先选择设备厂商==</option>");
					//$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
				});
				break;
			case "deviceModel":
				var url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModel.action"/>";
				var vendorId = $("select[@name='vendor']").val();
				if("-1"==vendorId){
					$("select[@name='device_model']").html("<option value='-1'>==请先选择设备厂商==</option>");
					//$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
					break;
				}
				$.post(url,{
					gwShare_vendorId:vendorId
				},function(ajax){
					//$("select[@name='device_model']").html("<option value='-1'>==请先选择设备型号==</option>");
					gwShare_parseMessage(ajax,$("select[@name='device_model']"),selectvalue);
				});
				break;
			case "devicetype":
				var url = "<s:url value="/gwms/share/gwDeviceQuery!getDevicetype.action"/>";
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
	setValue();
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
	//document.getElementById("start_time").value="";
	//document.getElementById("end_time").value="";
}


/**
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
**/

</SCRIPT>

</head>
<%@ include file="/toolbar.jsp"%>
<body>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<FORM NAME="mainForm" id="mainForm" METHOD="post" ACTION=""
			target="dataForm">
		<table width="98%" height="30" border="0" align="center"
			cellpadding="0" cellspacing="0" class="green_gargtd">
			<tr>
				<td width="162">
				<div align="center" class="title_bigwhite">设备版本库管理</div>
				</td>
				<td><img src="/itms/images/attention_2.gif" width="15"
					height="12">查询设备版本库信息</td>
			</tr>
		</table>
		<!-- 高级查询part -->
		<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
			<tr>
				<td bgcolor=#999999>
				<table border=0 cellspacing=1 cellpadding=2 width="100%"
					align="center">
					<tr>
						<th colspan="4" id="gwShare_thTitle">设备版本库查询</th>
					</tr>
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<TD align="right" class=column width="15%">设备厂商</TD>
						<TD align="left" width="35%"><select name="vendor" class="bk"
							onchange="gwShare_change_select('deviceModel','-1')">
						</select></TD>
						<TD align="right" class=column width="15%">设备型号</TD>
						<TD width="35%"><select name="device_model" class="bk">
							<option value="-1">==请选择厂商==</option>
						</select></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<TD align="right" class=column width="15%">硬件版本</TD>
						<TD align="left" width="35%"><INPUT TYPE="text"
							NAME="hard_version" maxlength=30 class=bk size=20>&nbsp;<font
							color="#FF0000"></font></TD>
						<TD align="right" class=column width="15%">软件版本</TD>
						<TD width="35%" nowrap><INPUT TYPE="text" NAME="soft_version"
							maxlength=30 class=bk size=20>&nbsp;<font color="#FF0000">支持后匹配</font>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<TD align="right" class=column width="15%">是否审核</TD>
						<TD align="left" width="35%"><select name="is_check"
							class="bk">
							<option value="-2">==请选择==</option>
							<option value="1">经过审核</option>
							<option value="-1">未测试</option>
						</select></TD>
						<TD align="right" class=column width="15%">设备类型</TD>
						<TD width="35%">
						<s:select list="devTypeMap" name="rela_dev_type"
									headerKey="-1" headerValue="请选择设备类型" listKey="type_id"
									listValue="type_name" cssClass="bk"></s:select>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<TD align="right" class=column width="15%">开始时间</TD>
						<TD align="left" width="35%"><lk:date id="start_time"
							name="start_time" type="all" /></TD>
						<TD align="right" class=column width="15%">结束时间</TD>
						<TD align="left" width="35%"><lk:date id="end_time"
							name="end_time" type="all" /></TD>
					</TR>
					
					 <TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<TD align="right" class=column width="15%">上行方式</TD>
						<TD align="left" width="35%"><select
							name="access_style_relay_id" class="bk">
							<option value="-1">==请选择==</option>
							<option value="1">ADSL</option>
							<option value="2">LAN</option>
							<option value="3">EPON光纤</option>
							<option value="4">GPON光纤</option>
						</select></TD>
						<TD align="right" class=column width="15%">终端规格</TD>
						<td width="35%">
						<s:select list="specList" name="spec_id" headerKey="-1"
								headerValue="请选择终端规格" listKey="id" listValue="spec_name"
								value="spec_id" cssClass="bk"></s:select>
						</td>
						</TD>
					</TR>
					
					
					<tr bgcolor="#FFFFFF">
						<td colspan="4" align="right" class="green_foot" width="100%">
						<input type="button" class=jianbian style="CURSOR: hand"
							style="display: none"
							onclick="javascript:gwShare_queryChange('1');"
							name="gwShare_jiadan" value="简单查询" /> <input type="button"
							class=jianbian style="CURSOR: hand" style="display:none "
							onclick="javascript:gwShare_queryChange('2');"
							name="gwShare_gaoji" value="高级查询" /> <input type="button"
							class=jianbian style="CURSOR: hand" style="display: none"
							onclick="javascript:gwShare_queryChange('3');"
							name="gwShare_import" value="导入查询" /> <input type="button"
							onclick="javascript:queryDevice()" class=jianbian
							name="gwShare_queryButton" value=" 查 询 " /> <input type="button"
							class=jianbian name="gwShare_reButto" value=" 重 置 "
							onclick="javascript:queryReset();" />
							<input type=button class=jianbian name="gwShare_reButto" value=" 导 出 "
							onclick="javascript:excel();" />
							</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</FORM>
		
		<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
			<TR>
				<TD>
					<iframe id="dataForm"
						name="dataForm" height="0" frameborder="0" scrolling="no"
						width="100%" src="">
					</iframe>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>

</TABLE>
</body>
<%@ include file="/foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">


function queryReset(){
	reset();
}

function  reset(){
	
	    document.mainForm.vendor.value="-1";
	    document.mainForm.device_model.value="-1";
		document.mainForm.hard_version.value="";
		document.mainForm.soft_version.value="";
		document.mainForm.start_time.value="";
		document.mainForm.end_time.value="";
}

//查询
function queryDevice()
{
	trimAll();
	// 进行校验
	/**
	if($("select[@name='vendor']").val() == "-1")
	{
		alert("请选择厂商！");
		$("select[@name='vendor']").focus();
		return;
	}
	
	if($("select[@name='device_model']").val() == "-1")
	{
		alert("请选择型号！");
		$("select[@name='device_model']").focus();
		return;
	}**/
	
	// 普通方式提交
	var form = document.getElementById("mainForm");
	form.action = "<s:url value="/itms/resource/deviceVersionManageExpACT!queryDeviceVersion.action"/>";
	//form.action = "<s:url value="/itms/resource/deviceTypeInfo!queryList.action"/>";
	form.submit();
}

// 导出excel
function excel()
{
	// 普通方式提交
	var form = document.getElementById("mainForm");
	form.action = "<s:url value="/itms/resource/deviceVersionManageExpACT!excel.action"/>";
	form.submit();
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