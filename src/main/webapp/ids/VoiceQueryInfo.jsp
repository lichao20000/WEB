<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>语音呼叫仿真测试</title>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/CheckFormForm.js"></SCRIPT>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">

function deviceResult(callData){
	$("input[@name='gwShare_queryButton']").attr("disabled", false);
 	init();
	var returnVal = callData.split("#");
	$("td[@id='tdDeviceSn']").append(returnVal[1]);
	$("input[@name='device_id']").val(returnVal[1]);
	$("input[@name='oui']").val(returnVal[2]);
	$("input[@name='BindType']").val(returnVal[3]);
	$("input[@name='ip']").val(returnVal[4]);
	$("input[@name='city_id']").val(returnVal[5]);
	$("td[@id='tdDeviceCityName']").append(returnVal[6]);
	
}

function queryVoiceService(){
	$("td[@id='messageInfo']").css("display","");
	$("td[@id='tdData']").css("display","");
	$("input[@name='button']").attr("disabled", true);
	document.frm.submit();
}

function init(){
	$("td[@id='messageInfo']").css("display","none");
	$("td[@id='trDeviceResult']").css("display","");
	$("td[@id='tdDeviceSn']").html("");
	$("td[@id='tdDeviceCityName']").html("");
}

function ck(){
	var _column2 = $("input[@name='column2']");
	//var _column3 = $("select[@name='column3']");
	var _column4 = $("input[@name='column4']");
	//主SIP服务器地址
	if(!IsNull(_column2.val(), "被叫号码")){
		_column2.focus();
		return false;
	}
	
	var cofirmButton = document.getElementById("cofirmButton");
	var cofirmButtonValue = cofirmButton.options[cofirmButton.selectedIndex].value;
	
	if("True" == cofirmButtonValue)
	{
		if(!IsNull(_column4.val(), "拨号确认的号码")){
			_column4.focus();
			return false;
		}
	}
	return true
}

function closeMessageInfo(){
	$("td[@id='messageInfo']").css("display","none");
}

function simulationModeChange(){
	  var simulationMode = document.getElementById("simulationMode");
	  var simulationModeValue = simulationMode.options[simulationMode.selectedIndex].value;
	  if('Caller' == simulationModeValue )
	  {
		  document.getElementById("numberMode").innerHTML = "被叫号码";
	  }
	  else if ("Called" == simulationModeValue)
	  {
		  document.getElementById("numberMode").innerHTML = "主叫号码";
	  }
};

function cofirmButtonChange(){
		var _column4 = $("input[@name='column4']");
		
		var cofirmButton = document.getElementById("cofirmButton");
		var cofirmButtonValue = cofirmButton.options[cofirmButton.selectedIndex].value;
		if('True' == cofirmButtonValue )
		{
			document.getElementById("column4").value="";
		 	document.getElementById("column4").disabled = false;
		}
		else if ("False" == cofirmButtonValue)
		{
			document.getElementById("column4").value="";
		 	document.getElementById("column4").disabled = true;
		}
};


//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids = [ "dataForm" ]

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide = "yes"

function dyniframesize() {
	var dyniframe = new Array()
	for (i = 0; i < iframeids.length; i++) {
		if (document.getElementById) {
			//自动调整iframe高度
			dyniframe[dyniframe.length] = document
					.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera) {
				dyniframe[i].style.display = "block"
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
			tempobj.style.display = "block"
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
</head>
<body>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<table width="98%" border="0" align="center" cellpadding="0"
				cellspacing="0" class="text">
				<tr>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0"
							cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									语音呼叫仿真测试
								</td>
								<td nowrap>
									<img src="../images/attention_2.gif" width="15" height="12">
									单台语音呼叫仿真
								</td>
							</tr>
						</table>
					</td>
				</tr>

				<TR bgcolor="#FFFFFF">
					<td colspan="4">
						
						<ms:inArea areaCode="jl_lt"  notInMode="true">
							<%@ include file="/ids/share/idsShareDeviceWAN.jsp"%>
						</ms:inArea>
						
						<ms:inArea areaCode="jl_lt"  notInMode="false">
							<%@ include file="/ids/share/idsShareDeviceWAN_voiceDial_jllt.jsp"%>
						</ms:inArea>
					</td>
				</TR>
				<TR>
					<td id="trDeviceResult" style="display:none;">
						<FORM NAME="frm" METHOD="post" action="<s:url value='/ids/VoiceDeviceQuery!queryVoiceService.action'/>"
							target="dataForm" onsubmit="return CheckForm()">
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center" >
									<TR>
										<TD bgcolor=#999999>
												<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
														<TR bgcolor="#FFFFFF" id="trDeviceResult"
															style="display: ">
															<td nowrap align="right" class=column width="15%">
																设备属地
																<input type="hidden" name="city_id" id="city_id" value=""/>
																<input type="hidden" name="oui" id="oui" value=""/>
																<input type="hidden" name="ip" id="ip" value=""/>
																<input type="hidden" name="BindType" id="BindType" value=""/>
																<input type="hidden" name="device_id" id="device_id" value="" />
																<input type="hidden" name="wan_interface" id="wan_interface" value="" />
																<input type="hidden" name="ip_address" id="ip_address" value="" />
																<input type="hidden" name="default_gateway" id="default_gateway" value="" />
															</td>
															<td id="tdDeviceCityName" width="35%">
															</td>
															<td nowrap align="right" class=column width="15%">
																设备序列号
															</td>
															<td id="tdDeviceSn" width="35%">
															</td>
														</TR>
														
														<TR bgcolor="#FFFFFF">
															
															<TD nowrap align="right" class=column width="15%">仿真测试类型</TD>
															
															<TD width="35%">
																<ms:inArea areaCode="jl_lt"  notInMode="true">
																	<select name="column1" class="bk">
																		<option value="Caller" selected="selected">主叫仿真</option>
																	</select>
																	&nbsp;&nbsp;<font color="red">*</font>
																</ms:inArea>
																<ms:inArea areaCode="jl_lt"  notInMode="false">
																	<select name="column1" class="bk" id = "simulationMode" onchange="simulationModeChange()">
																		<option value="Caller" selected="selected">主叫仿真</option>
																		<option value="Called" >被叫仿真</option>
																	</select>
																	&nbsp;&nbsp;<font color="red">*</font>
																</ms:inArea>
															</TD>
															
															<TD id ='numberMode' nowrap align="right" class=column width="15%">被叫号码</TD>
															<TD width="35%"><input type="text" name="column2" size="20"  class="bk"  />&nbsp;&nbsp;<font color="red">*</font></TD>
														</TR>
														
														<TR bgcolor="#FFFFFF">
															<TD nowrap align="right" class=column width="15%">按键确认</TD>
															<TD width="35%">
															<select name="column3" class="bk" id = "cofirmButton" onchange="cofirmButtonChange()">
																		<option value="True" selected="selected">开启</option>
																		<option value="False">关闭</option>
															</select>
															<font color="red">*</font></TD>
															<TD nowrap align="right" class=column width="15%">拨号确认的号码</TD>
															<TD width="35%"><input type="text" name="column4" size="20"  class="bk" id ="column4"  />&nbsp;&nbsp;<font color="red">*</font></TD>
														</TR>
														
														<TR bgcolor="#FFFFFF">
															<TD colspan="5" align="right" class="green_foot">
																<INPUT TYPE="button" name="button" id="button" class=jianbian value=" 仿   真" onclick="javaScript:ck()&&queryVoiceService()">
															</TD>
														</TR>
												</TABLE>
										</TD>
									</TR>		
							</TABLE>
						</FORM>
					</td>
				</tr>
				<tr>
					<td height="10"></td>
				</tr>
				
				<TR>
					<TD id="messageInfo"  style="display:none;background-color: #E1EEEE;height: 10" >
						正在查询，请稍后...	
					</TD>
				</TR>
	
				<TR>
					<TD id="tdData" style="display:none;"  width="100%">
						<iframe id="dataForm" name="dataForm" height=0 frameborder="0" scrolling="no" width="100%" src=""></iframe>
					</TD>
				</TR>
			</table>
		</TD>
	</TR>
</TABLE>
</body>
</html>
<%@ include file="../../foot.jsp"%>