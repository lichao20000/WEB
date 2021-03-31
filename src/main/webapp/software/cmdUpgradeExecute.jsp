<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="java.util.*,com.linkage.litms.software.CmdUpgrade"%>
<%@page import="com.linkage.litms.common.util.RPCUtilHandler,com.linkage.litms.common.util.StringUtils"%>
<%@page import="com.linkage.litms.resource.DeviceAct"%>
<%@page import="java.util.HashMap"%>
<%
request.setCharacterEncoding("GBK");
String[] deviceArr = request.getParameterValues("device_id");
//由数据库中查询获得采集点，而不是从页面中
DeviceAct act = new DeviceAct();
HashMap deviceInfoMap= act.getDeviceInfo(deviceArr[0]);
String strGatherId = (String)deviceInfoMap.get("gather_id");
String devicetype_id = (String)deviceInfoMap.get("devicetype_id");

String [] cmdParamArr = request.getParameterValues("cmdParam");
String object_name="ACS_" + strGatherId;
String object_Poaname="ACS_Poa_" + strGatherId;
String strIor = user.getIor(object_name,object_Poaname);

CmdUpgrade cmpUpgrade = new CmdUpgrade(request);
//首先将前台页面传的命令从后台获取值get
Map mapDeviceParamValue = cmpUpgrade.getParamValue(strGatherId,strIor,deviceArr,cmdParamArr);
Map mapParamValue = null;
//能获取设备参数的设备id列表
List listDeviceId = new ArrayList();

%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
//存放设备对应的 参数属性和值的map对象
var deviceParamValue = new Object();
//记录用户选择的参数名称
var cmdParamArr = "<%=StringUtils.weave(Arrays.asList(cmdParamArr))%>";
var paramValue = null;
var gather_id = "<%=strGatherId%>";
<%
for(int i=0;i<deviceArr.length;i++){
	out.println("paramValue = new Object();");
	mapParamValue = (Map)mapDeviceParamValue.get(deviceArr[i]);	
	for(int j=0;j<cmdParamArr.length;j++){
		if(mapParamValue != null){
			out.println("paramValue['" + cmdParamArr[j] + "']='" + mapParamValue.get(cmdParamArr[j]) + "';");
			listDeviceId.add(deviceArr[i]);
		}else{//获取命令参数失败的设备id
			
		}
	}
}
//数组存放设备id
%>
//已经完成命令备份设备id数组，未完成的id不在数组中。
//注：和java中deviceArr设备id不一致
var deviceArr = [<%=StringUtils.weave(listDeviceId)%>];
deviceArr = $A(deviceArr);
<%
//执行工单----------------------------------------------------------------//
List listSheetId = cmpUpgrade.executeSheet(strIor,strGatherId,devicetype_id);
//执行工单结束------------------------------------------------------------//
%>
//后台正在处理的工单id数组，不包括错误工单(sheet.time=-3，设备正在操作)
var sheetArr = [<%=StringUtils.weave(listSheetId)%>];
sheetArr = $A(sheetArr);
</SCRIPT>
<br>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
	<tr>
		<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						命令备份升级
					</td>
					<td>
						<img src="../images/attention_2.gif" width="15" height="12">
						该页面会每5秒中检查工单执行状态，工单执行成功后，会进行命令恢复操作!
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr >
		<td>
			<TABLE width="100%" border=0 cellspacing=1 cellpadding=2 align="center" bgcolor=#999999>
				<tr bgcolor=#ffffff>
					<th>设备OUI</th>
					<th>设备序列号</th>
					<th>备份操作状态</th>
				</tr>
					<%
						String[] deviceInfo = null;
						//遍历用户选择的所有设备
						for(int i=0;i<deviceArr.length;i++){
						    deviceInfo = RPCUtilHandler.getDeviceResInfo(deviceArr[i]);
							out.println("<tr bgcolor=#ffffff>");
						    out.println("<td>"+ deviceInfo[0] +"</td>");
						    out.println("<td>"+ deviceInfo[1] +"</td>");
							out.println("<td><span id='devInfo_"+ deviceArr[i] +"'>-</span></td>");
							out.println("</tr>");
						}
					%>
			</TABLE>
		</td>
	</tr>
</table>
<script language="JavaScript">
<!--
var SH_FAULT_CODE = new Object();
SH_FAULT_CODE["-1"]="连接不上";
SH_FAULT_CODE["0"]="未知错误";
SH_FAULT_CODE["1"]="执行成功";
SH_FAULT_CODE["-2"]="连接超时";
SH_FAULT_CODE["-3"]="没有工单";
SH_FAULT_CODE["-4"]="没有设备";
SH_FAULT_CODE["-5"]="没有模板";
SH_FAULT_CODE["-6"]="设备正忙";
SH_FAULT_CODE["-7"]="参数错误";
function getSheetDetail(fault_code){
	var str = SH_FAULT_CODE[fault_code];
	if(str == null) str = "未知状态";
	return str;
}
function setDeviceInfo(device_id,info){
	var spDev = $("devInfo_" + device_id);
	if(spDev == null) return ;
	spDev.innerHTML = info;
	//标志位，命令备份升级成功
	//spDev.isok = 1;
}
//设置参数
function setDeviceParamValue(device_id){
	paramValue = deviceParamValue[device_id];
	if(typeof(paramValue) == "undefined"){
		alert("error:" + paramValue);
		return ;
	}	
	var pageParam = "device_id=" + device_id;
	pageParam += "&gather_id=" + gather_id;
	for(var j=0;j<cmdParamArr.length;j++){
		pageParam += "&" + cmdParamArr[j] + "=" + paramValue[cmdParamArr[j]];
	}
	pageParam += "&tt=" + new Date();
	CreateAjaxReq("setDeviceCmdUpgrade.jsp",pageParam,completeSetDevicePV,errorSetDevicePV);
}

//命令备份成功，等待工单执行完成
//for(var k=0;k<deviceArr.length;k++){
//	setDeviceInfo(deviceArr[k],"设备备份命令参数成功，正在等待工单执行完成后恢复参数命令...");
//}

//检查工单执行状态
function checkSheet(){
	var sheetStr = "";
	sheetArr.each(function(item){
		sheetStr += "," + item;
	});
	if(sheetStr == "")
		return ;
	sheetStr = sheetStr.substring(1);
	var param = "sheetId=" + sheetStr + "&tt=" + new Date().getTime();
	CreateAjaxReq("getSheetStatusOfCmdUpgrade.jsp",param,completeSheetAct,errorSheetAct);
}
//立即检查工单状态
checkSheet();
var tiger = window.setInterval("checkSheet()",5000);
//公共发出Ajax请求函数体
function CreateAjaxReq(url,param,successFunc,errorFunc){
	var myAjax
		= new Ajax.Request(
							url,
							{
								method:"post",
								parameters:param,
								onFailure:errorFunc,
								onSuccess:successFunc
							 }
						  );
}
//工单执行完成之后，修改设备状态信息并恢复命令
function completeSheetAct(response){
	var result = eval(response.responseText);//[device_id$sheet_id,device_id$sheet_id,device_id$sheet_id]
	var funstr = null;
	//alert("completeSheetAct:" + result + "==>" + response.responseText);
	//var arrDevSheet = null;
	//返回执行成功的工单
	result = $A(result);
	result.each(function(item){
		funstr = "view('" + item.sheet_id + "','" + item.receive_time + "')";
		//工单正在执行
		if(item.exec_status == 0){
			setDeviceInfo(item.device_id,"正在执行工单...<a href=javascript:"+ funstr +">查看工单</a>");
		}else if(item.exec_status == 1){//工单完成
			if(item.fault_code == 1){//工单执行成功
				setDeviceInfo(item.device_id,"工单执行成功，正在进行命令恢复操作....<a href=javascript:"+ funstr +">查看工单</a>");
				setDeviceParamValue(item.device_id);
			}else{
				setDeviceInfo(item.device_id,"工单执行失败("+ getSheetDetail(item.fault_code) + ")，备份失败!<a href=javascript:"+ funstr +">查看工单</a>");
			}
			deleteSheet(item.sheet_id);
		}else{
			alert("error:" + item);
		}
	});
	//完成后，则其他工单都完成，但有错误
	if(sheetArr.length == 0){//result.length == 0 || 
		window.clearInterval(tiger);
		tiger = null;
		//将其余设备工单信息修改为完成,但备份升级失败
		//var spArr = $$('span[isok="0"]');
		//spArr.each(function(item){
		//	item.innerHTML = "工单执行失败，该设备此次备份升级失败!";
		//});
	}
}
function view(sheet_id,receive_time){
	if(item == null){
		alert("error:" + item);
		return ;
	}
	var page = "sheet_detail.jsp?sheet_id="+ sheet_id + "&receive_time="+ receive_time;
	window.open(page,new Date().getTime(),"left=20,top=20,width=500,height=420,resizable=no,scrollbars=no");
}
function errorSheetAct(response){
	$("debug").innerHTML = response.responseText;
}
function completeSetDevicePV(response){
	var resutlDev = eval(response.responseText);
	if(typeof(resutlDev) == "object" && resutlDev.flag == true){
		setDeviceInfo(resutlDev.device_id,"工单执行完成，设备命令备份成功!");
	}
}
function errorSetDevicePV(response){
	alert(response.responseText);
}

function deleteSheet(sheet_id){
	sheetArr = sheetArr.without(sheet_id);
}

//-->
</script>
<div id=debug></div>
<%@ include file="../foot.jsp"%>