<%--
Author      : 朱征东
Date		: 2016-12-13
Desc		: QOE功能需求
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='../../Js/inmp/jquery.js'/>"></SCRIPT>
<link rel="stylesheet" href="<s:url value='../../css/inmp/css_green.css'/>"
	type="text/css">

<SCRIPT LANGUAGE="JavaScript">
	
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




$(window).resize(function(){
	dyniframesize();
}); 
	function getUrlParam(name) {
 var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
 var r = window.location.search.substr(1).match(reg); //匹配目标参数
 if (r != null) return unescape(r[2]); return null; //返回参数值
}
$(function(){
	var instArea = "<%=LipossGlobals.getLipossProperty("InstArea.ShortName")%>";
	dyniframesize();
	var servType =getUrlParam('servType');
	$("input[@name='servType']").val(servType);
	if("single_on"==servType){
		if("sx_dx"==instArea){
			$("#title").text("QOE软探针开启");
		}else{
			$("#title").text("QOE功能测试");
		}
		$("#QoeOff").hide();
		$("#batchOn").hide();
		$("#singleOn").show();
		$("#doButton").val("诊 断");
		$("#doButton").bind("click",QOETest);
		
	}else if("single_off"==servType){
		if("sx_dx"==instArea){
			$("#title").text("QOE软探针关闭");
		}else{
			$("#title").text("QOE功能关闭");
		}
		$("#QoeOff").show();
		$("#batchOn").hide();
		$("#singleOn").hide();
		$("#doButton").val("关 闭 业 务");
		$("#doButton").bind("click",QOEOff);
	}else if("batch_on"==servType){
		if("sx_dx"==instArea){
			$("#title").text("QOE软探针批量开启");
		}else{
			$("#title").text("批量QOE功能测试");
		}
		$("#batchOn").show();
		$("#singleOn").hide();
		$("#QoeOff").hide();
		$("#doButton").val("执 行");
		$("#doButton").bind("click",BatchQOETest);
	}else if("batch_off"==servType){
		if("sx_dx"==instArea){
			$("#title").text("QOE软探针批量关闭");
		}else{
			$("#title").text("批量QOE关闭");
		}
		$("#QoeOff").show();
		$("#batchOn").hide();
		$("#singleOn").hide();
		$("#doButton").val("执 行");
		$("#doButton").bind("click",BatchQOEOff);
	}else if(null ==servType){
		$("#title").text("QOE功能状态节点采集");
		$("#QoeOff").hide();
		$("#batchOn").hide();
		$("#singleOn").hide();
		$("#doButton").val("诊 断");
		$("#doButton").bind("click",QOEQuery);
	}
	$("input[@name='gwShare_queryResultType']").val("checkbox");
	gwShare_setImport();
});
		
	
		
var deviceIds = "";

var deviceId ;
function QOEQuery(){
	doExecute("query");
}
function QOETest(){
	doExecute("single_on");
}
function QOEOff(){
	doExecute("single_off");
}
function BatchQOETest(){
	doExecute("batch_on");
}
function BatchQOEOff(){
	doExecute("batch_off");
}


function doExecute(do_type){
	$("#doButton").attr("disabled",true);
	var deviceIds = $("input[@name='deviceIds']").val();
	var singleOn_url = $("input[@name='singleOn_url']").val();
	var batchOn_url = $("input[@name='batchOn_url']").val();
	var batchOn_exeWay = $("select[@name='batchOn_exeWay']").val();
	var exeWay_QoeOff = $("select[@name='exeWay_QoeOff']").val();
	if(CheckForm(do_type))
	{
		$("tr[@id='trData']").show();
		
	  var service_id;
	  if("single_on"==do_type||"batch_on"==do_type){
	  	service_id="2005";
	  }else if("single_off"==do_type||"batch_off"==do_type){
	  	service_id="2006";
	  }
	  $("input[@name='service_id']").val(service_id);
	  $("input[@name='do_type']").val(do_type);
	  var form = document.getElementById("frm");
	  if("query"==do_type){
	  	form.action = "<s:url value='/gtms/config/qoeFunctionAct!queryQOE.action'/>";
	  }else{
	  	form.action = "<s:url value='/gtms/config/qoeFunctionAct!doConfig.action'/>";
	  }
	  form.submit();
		}else{
			$("#doButton").attr("disabled",false);
		}
	}

function CheckForm(do_type){
	if($("input[@name='deviceIds']").val()==""){
		alert("请选择设备！");
		return false;
	}
	
		if("single_on"==do_type){
			var singleOn_url = $("input[@name='singleOn_url']").val();
			if("" == singleOn_url){
				alert("请填写测试下载URL");
				return false;
			}
		}else if("batch_on"==do_type){
			var batchOn_url = $("input[@name='batchOn_url']").val();
			if("" == batchOn_url){
				alert("请填写测试下载URL");
				return false;
			}
		}
		return true;
}

function deviceResult(returnVal){	
	deviceId="";
	$("#resultDIV").html("");
	
	var totalNum = returnVal[0];
	
	if(returnVal[0]==0)
	{
		totalNum = returnVal[2].length;
		var deviceIdArray = returnVal[2];
		for(var i=0 ;i<deviceIdArray.length;i++){
			//遍历出来的deviceId
			deviceId +=  deviceIdArray[i][0]+",";
			
		}
		if(totalNum > 2000){
			alert("设备数量超过2000台，影响到交互性能");
			$("#doButton").attr("disabled",true);
		}else{
			$("#doButton").attr("disabled",false);
		}
		$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[2].length+"</strong></font>");

	  var endIndex = deviceId.lastIndexOf(",");
		deviceId = deviceId.substring(0,endIndex);
		$("input[@name='deviceIds']").val(deviceId);
		
	}
}


</SCRIPT>
<%@ include file="../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>

			<table width="98%" border="0" align="center" cellpadding="0"
				cellspacing="1" class="text">
				<tr>
					<td>
						<table width="100%" height="30" border="0" align="center"
										cellpadding="0" cellspacing="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" id="title" class="title_bigwhite" nowrap>
									
								</td>
								<td nowrap>
									<img src="<s:url value='../../images/inmp/attention_2.gif'/>" width="15"
										height="12">

								</td>
							</tr>
						</table>
					</td>
				</tr>
				<TR bgcolor="#FFFFFF">
					<td colspan="4" id="sharePart">
						<%@ include file="../bss/gwShareDeviceQuery_QOE.jsp"%>
					</td>
				</TR>
				
				<tr>
					<td>
						<FORM NAME="frm" id="frm" METHOD="post"
							ACTION="" target="dataForm">
							<input type="hidden" name="deviceIds" value="" />
							<input type="hidden" name="param" value="" />
							<input type="hidden" name="service_id" value="" />
							<input type="hidden" name="do_type" value="" />
							<input type="hidden" id="servType" name="servType" value="" />
							<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" align="center">
								<TR>
									<TD bgcolor=#999999>
										<table border=0 cellspacing=1 cellpadding=2 width="100%"
						align="center">
											<tr bgcolor="#FFFFFF">
												<td colspan="4">
													<div id="selectedDev" >
														请查询设备！
													</div>
												</td>
											</tr>
										<tr id="singleOn" bgcolor="#FFFFFF">

												<td align="right" width="60%">
													测试下载URL：
												</td>
												<td align="left" width="30%" colspan="3">
													<input type="input" class="bk" size="35" name="singleOn_url" value="kqi.jsinfo.net"/>
												</td>
											</tr>
											<tr bgcolor="#FFFFFF" id="batchOn">

												<TD align="right" width="15%">
													策略方式:
												</TD>
												<TD align="left" width="30%">
													<select name="batchOn_exeWay" class="bk">
													<option value="1">立即执行</option>
													<option value="4">下次连接到系统</option>
													<option value="5">终端启动</option>
													</select>
												</TD>

												<td align="right" width="15%">
													测试下载URL：
												</td>
												<td align="left" width="30%">
													<input type="input" class="bk" name="batchOn_url" value="kqi.jsinfo.net"/>
												</td>
											</tr>
											<tr bgcolor="#FFFFFF" id="QoeOff">

												<TD align="right" width="15%" >
													策略方式:
												</TD>
												<TD align="left" width="75%" colspan="3">
													<select name="exeWay_QoeOff" class="bk">
													<option value="1">立即执行</option>
													<option value="4">下次连接到系统</option>
													<option value="5">终端启动</option>
													</select>
												</TD>

											</tr>
											
												
											<TR>
												<TD colspan="4" align="right" CLASS="green_foot">
													<INPUT TYPE="button" id="doButton" value="" class=btn disabled>
												</TD>
											</TR>
										</TABLE>
									</TD>
								</TR>
								
							</TABLE>
						</FORM>
						
					</td>
				</tr>
				<tr id="trData" style="display: none">
									<td class="colum">
										<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
											正在执行，请稍等....
										</div>
									</td>
								</tr>
			</table>
		</TD>
	</TR>
	<tr>
			<td>
				<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#ffffff id="idData"><iframe id="dataForm"
					name="dataForm" height="0" frameborder="0" scrolling="no"
					width="100%" src=""></iframe></TD>
			</TR>
		</TABLE>
			</td>
	</tr>

</TABLE>
<%@ include file="../foot.jsp"%>
