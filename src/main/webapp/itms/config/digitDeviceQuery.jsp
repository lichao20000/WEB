<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<%@ include file="/toolbar.jsp"%>


<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript"
	SRC="<s:url value="/Js/CheckFormForm.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">

function Init(){
	// 初始化厂家
	gwShare_change_select("city","-1");
	gwShare_change_select("vendor","-1");
}


function CheckForm(){   
	 var cityId = $("select[@name='cityId']").val();
	 var vendorId = $("select[@name='vendorId']").val();
	 var deviceModelId = $("select[@name='deviceModelId']").val();

	 var device_serialnumber = $("input[@name='device_serialnumber']").val();
	 var loopback_ip = $("input[@name='loopback_ip']").val();
		var task_name = $("input[@name='task_name']").val();
	 var deviceTypeId = $("select[@name='deviceTypeId']").val();

//	 alert("device_serialnumber:"+device_serialnumber);
//	 alert("loopback_ip:"+loopback_ip);
//	 alert("cityId:"+cityId);
//	   alert("vendorId:"+vendorId);
//	   alert("deviceModelId:"+deviceModelId);
//	   alert("deviceTypeId:"+deviceTypeId);
//	   alert("task_name:"+task_name);

//&& deviceModelId=="-1"
   if(device_serialnumber == ""){
	  
	   if(loopback_ip == "" ){
		    if(cityId=="-1" && vendorId=="-1" )
			   {
			    if( task_name=="")
	                   {
	            	  
	                     alert("任务名称或者设备序列号、设备IP两项中的一项或者属地、厂商、型号三项中一项至少一个不能为空 ");
		                 return false;
		               }
		       }
     }
 }

   var loopback_ip = $.trim(document.mainForm.loopback_ip.value);
	   if(loopback_ip!=""){
			if(!IsIPAddr2(loopback_ip,"设备IP")){
				document.mainForm.loopback_ip.focus();
				return false;
			}
		}
	   return true;
}


function doQuery()
{    
trimAll();
if(!CheckForm()){
	return;
}
// 普通方式提交  南京
	var form = document.getElementById("mainForm");
	form.action = "<s:url value="/itms/config/digitDeviceACT!query.action"/>";
	form.submit();
	
}

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
function gwShare_change_select(type,selectvalue){
	switch (type){
		case "city":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getCityNextChild.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='cityId']"),selectvalue);
			});
			break;
		case "vendor":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getVendor.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='vendorId']"),selectvalue);
				$("select[@name='deviceModelId']").html("<option value='-1'>==请先选择设备厂商==</option>");
				$("select[@name='deviceTypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
			});
			break;
		case "deviceModel":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModel.action"/>";
			var vendorId = $("select[@name='vendorId']").val();
			if("-1"==vendorId){
				$("select[@name='deviceModelId']").html("<option value='-1'>==请先选择厂商==</option>");
				$("select[@name='deviceTypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='deviceModelId']"),selectvalue);
				$("select[@name='deviceTypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
			});
			break;
		case "devicetype":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getDevicetype.action"/>";
			var vendorId = $("select[@name='vendorId']").val();
			var deviceModelId = $("select[@name='deviceModelId']").val();
			if("-1"==deviceModelId){
				$("select[@name='deviceTypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId,
				gwShare_deviceModelId:deviceModelId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='deviceTypeId']"),selectvalue);
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

</SCRIPT>

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
				<div align="center" class="title_bigwhite">数图配置模板管理</div>
				</td>
				<td><img src="../../images/attention_2.gif" width="15"
					height="12">数图配置下发管理</td>
				<td>&nbsp;</td>

			</tr>
		</table>
		<!-- 查询part -->
		<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
			<tr>
				<td bgcolor=#999999>
				<table border=0 cellspacing=1 cellpadding=2 width="100%"
					align="center">
					<tr>
						<th colspan="4" id="gwShare_thTitle">数图配置下发结果查询</th>
					</tr>
					<TR bgcolor=#ffffff>
						<td class="column" width='20%' align="right">设备序列号：</td>
						<td width='30%' align="left"><input
							name="device_serialnumber" type="text" class='bk' value="">
						</td>

						<td class="column" width='20%' align="right">设备IP：</td>
						<td width='30%' align="left"><input name="loopback_ip"
							type="text" class='bk' value=""></td>
					</TR>
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<TD align="right" class=column width="20%">设备厂商：</TD>
						<TD align="left" width="30%"><select name="vendorId"
							class="bk" onchange="gwShare_change_select('deviceModel','-1')">
							<option value="-1">==请选择==</option>
						</select></TD>
						<TD align="right" class=column width="20%">设备型号：</TD>
						<TD width="30%"><select name="deviceModelId" class="bk"
							onchange="gwShare_change_select('devicetype','-1')">
							<option value="-1">请先选择厂商</option>
						</select></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<td class="column" width='20%' align="right">软件版本：</td>
						<td width='30%' align="left"><select name="deviceTypeId"
							class="bk"">
							<option value="-1">请先选择设备型号</option>
						</select></td>
						<td class="column" width='20%' align="right">设备属地：</td>
						<td width='30%' align="left"><!--<s:select list="cityList" name="cityId" headerKey="-1"
									headerValue="请选择属地" listKey="city_id" listValue="city_name"
									value="digitMap.city_id" cssClass="bk"></s:select>--> <select
							name="cityId" class="bk">
							<option value="-1">==请选择==</option>
						</select></td>

					</TR>
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<td class="column" width='20%' align="right">loid：</td>
						<td width='30%' align="left"><input name="device_id_ex"
							type="text" class='bk' value=""></td>

						<td class="column" width='20%' align="right">任务名称：</td>
						<td width='30%' align="left"><input name="task_name"
							type="text" class='bk' value=""> <font color="red">*只支持完全匹配</font>
						</td>
					</TR>

					<tr bgcolor="#FFFFFF">
						<td colspan="4" align="right" class="green_foot" width="100%">
						<input type="button" class=jianbian onclick="javascript:doQuery()"
							name="gwShare_queryButton" value=" 查 询 " /> <input type="reset"
							class=jianbian name="gwShare_reButto" value=" 重 置 " /></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</FORM>
		<!-- 展示结果part -->
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#999999 id="idData"><iframe id="dataForm"
					name="dataForm" height="0" frameborder="0" scrolling="no"
					width="100%" src=""></iframe></TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>&nbsp;</TD>
	</TR>
</TABLE>

<%@ include file="/foot.jsp"%>

<SCRIPT LANGUAGE="JavaScript">

Init();

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


</SCRIPT>
