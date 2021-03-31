<%@ include file="../../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>自动化检测</title>
<link href="<s:url value="../../css/css_green.css"/>" rel="stylesheet" type="text/css">

<%
	String gw_type = request.getParameter("gw_type");
%>
 
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/commFunction.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.blockUI.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
var gw_type = "<%= gw_type%>";

function block(){   
	$.blockUI({
		overlayCSS:{ 
	        backgroundColor:'#CCCCCC', 
	        opacity:0.6 
    	},
		message:"<font size=3>正在操作，请稍后...</font>"
	});      
}

function unblock(){
	$.unblockUI();
} 

var iframeSrc=["<s:url value='/gwms/diagnostics/deviceInfo!autoCheckInfo.action?deviceId='/>","<s:url value='/gwms/diagnostics/algACT.action?deviceId='/>","<s:url value='/gwms/diagnostics/deviceDiagnostics.action?deviceId='/>"];


function winNavigate(uniformid)
{
	switch (uniformid)
	{
		case 1:
		{
			document.all("td1").className="curendtab_bbms";
			document.all("myiframe1").src = iframeSrc[0]+document.all("txtdeviceId").value + "&gw_type=" + gw_type;
			break;
		}
	}
}

function txtSelectDevice()
{		
	if("none"==document.all("deviceResult").style.display){
		document.all("deviceResult").style.display="";
		document.all("btnDevRes").value="隐藏查询";
	}else{
		document.all("deviceResult").style.display="none";
		document.all("btnDevRes").value="展开查询";
	}
}

function selectbyonclick(deviceId,deviceSn)
{	
	document.all("txtdeviceId").value=deviceId;
	$("div[@id='selectedDev']").html("<strong>待操作设备序列号:"+deviceSn+"</strong>");
	
	this.tr1.style.display="";
	this.tr2.style.display="";
	winNavigate(1);
	document.all("deviceResult").style.display="none";
	document.all("btnDevRes").value="展开查询";
	
}



//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids=["myiframe1"];

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
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
    		tempobj.style.display="block";
		}
	}
}


$(window).resize(function(){
	dyniframesize();
}); 

//if (window.addEventListener)
// 	window.addEventListener("load", dyniframesize, false)
//else if (window.attachEvent)
// 	window.attachEvent("onload", dyniframesize)
//else
//	window.onload=dyniframesize

//function iframeAutoFit(iframeObj){ 
//	setTimeout(function(){
//		if(!iframeObj) return;
//		iframeObj.height=(iframeObj.Document?iframeObj.Document.body.scrollHeight:iframeObj.contentDocument.body.offsetHeight);},200) 
//}

</SCRIPT>
<%@ include file="../../toolbar.jsp"%>
<style>
span
{
	position:static;
	border:0;
}
</style>

<jsp:include page="/share/selectDeviceJs.jsp">
	<jsp:param name="selectType" value="radio"/>
	<jsp:param name="jsFunctionName" value="selectbyonclick"/>
	<jsp:param name="jsFunctionNameBySn" value="true"/>
	<jsp:param name="byWideNet" value="6"/>
	<jsp:param name="byItv" value="0"/>
	<jsp:param name="byVoipTelNum" value="0"/>
	<jsp:param name="byDeviceno" value="2"/>
	<jsp:param name="buUsername" value="1"/>
	<jsp:param name="byCity" value="0"/>
	<jsp:param name="byImport" value="0"/>
	<jsp:param name="div_device_height" value="60"/>
</jsp:include>

</head>
<body>
<form name="form1">
<TABLE border=0 cellspacing=0 cellpadding=0 width="98%">
	<TR>
		<TD HEIGHT=18>&nbsp;</TD>
	</TR>
	<tr>
		<td >
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
				<tr>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									自动化检测
								</td>
								<td nowrap>
									<img src="../../images/attention_2.gif" width="15" height="12">
									<font color="red">提示：1.请确保接入光猫，开通宽带，语音，IPTV业务 2.确保已经接入机顶盒设备</font>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table id="deviceResult" width="100%" border=0 cellspacing=0 cellpadding=0 align="center" valign="middle" STYLE="display:">
				<TR>
					<TH colspan="4" align="center">
						设备查询
					</TH>
				</TR>
				<TR bgcolor="#FFFFFF" >
					<td colspan="4">
						<div id="selectDevice"><span>加载中....</span></div>
					</td>
				</TR>
			</table>
		</td>
	</tr>
	<TR id="tr1"  STYLE="display:none" bgcolor="#FFFFFF" >
		<td bgcolor=#999999>
			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH colspan="2" align="center">
						自动化检测
					</TH>
				</TR>
				<tr bgcolor="#FFFFFF">
					<td width="50%" >
						<div id="selectedDev">
							请查询设备！
						</div>
						<input type="hidden" name="txtdeviceId" value="" />
					</td>
					<td align="right" width="50%" >
						<input type="button" name="btnDevRes" class=btn value="隐藏查询" onclick="txtSelectDevice()"/>      
					</td>
				</tr>
			</table>
		</td>
	</TR>
	<TR>
		<TD HEIGHT=5>&nbsp;</TD>
	</TR>
	<TR  id="tr2"  STYLE="display:none" >
  		<TD>
			<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center" valign="middle">
				<TR>
					<TD>
						<TABLE width="15%" border=0 cellspacing=0 cellpadding=0>
							<TR width="14%" height=30>
								<TH class="curendtab_bbms" id="td1" width="14%"><a class="tab_A" href="javascript:winNavigate(1);">设备信息</a></TH>
							</TR>
						</TABLE>
 					</TD>
				</TR>
				<TR>
					<td>
						<DIV id=qh_con0 style="DISPLAY: block">
							<iframe id=myiframe1 src="" frameborder="0" scrolling="no" height="100%" width="100%" onload=this.height=myiframe1.document.body.scrollHeight></iframe>
						</DIV>
					</td>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
</form>
</body>
<%@ include file="../foot.jsp"%>
