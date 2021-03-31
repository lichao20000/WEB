<%@ include file="../../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>网络应用</title>
		<link href="<s:url value='../../css/css_green.css'/>" rel="stylesheet"
			type="text/css">
		<script type="text/javascript"
			src="<s:url value='../../Js/jquery.js'/>"></script>
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.blockUI.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript">

//系统类型

$(function(){
	gwShare_setGaoji();
});

function block(msg){
	/**$.blockUI({
		overlayCSS:{ 
	        backgroundColor:'#CCCCCC', 
	        opacity:0.6 
    	},
		message:"<span style='font-size:14px;font-family: 宋体'>正在操作，请稍后...</span>"
	}); */
	$("tr[@id='qh_con11']").show();  
	$("tr[@id='qh_con12']").hide();        
}

function unblock(){
	//$.unblockUI();
	$("tr[@id='qh_con11']").hide();
	$("tr[@id='qh_con12']").show();    
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


function Using(){
	block("");
	//document.all("trUsing").style.display="none";
	var url = "<s:url value='/gwms/config/iptvACT!getIptvData.action?deviceId='/>";
	document.all("myiframe1").src = url+document.all("txtdeviceId").value+"&deviceSn="+document.all("deviceSn").value;
}


//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids=["myiframe1"]

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


function deviceResult(returnVal){
	

	document.all("txtdeviceId").value=returnVal[2][0][0];
	$("div[@id='selectedDev']").html("<strong>待操作设备序列号:"+returnVal[2][0][1]+" -"+returnVal[2][0][2]+"</strong>");
	
	this.tr1.style.display="";
	this.tr2.style.display="";
	document.all("deviceResult").style.display="none";
	document.all("btnDevRes").value="展开查询";
}

</SCRIPT>
		<%@ include file="../../toolbar.jsp"%>
		<style>
span {
	position: static;
	border: 0;
}
</style>


	</head>
	<body>
		<form name="form1">
			<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
				<TR>
					<TD HEIGHT=18>
						&nbsp;

					</TD>
				</TR>
				<tr>
					<td>
						<table width="100%" border="0" align="center" cellpadding="0"
							cellspacing="0" class="text">
							<tr>
								<td>
									<table width="100%" height="30" border="0" cellspacing="0"
										cellpadding="0" class="green_gargtd">
										<tr>
											<td width="162" align="center" class="title_bigwhite" nowrap>
												IPTV多终端
											</td>
											<td nowrap>
												<img src="../../images/attention_2.gif" width="15"
													height="12">
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
						<table id="deviceResult" width="100%" border=0 cellspacing=0
							cellpadding=0 align="center" valign="middle" STYLE="display: ">
							<TR bgcolor="#FFFFFF">
								<td colspan="4">
									<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
								</td>
							</TR>
						</table>
					</td>
				</tr>
				<TR id="tr1" STYLE="display: none" bgcolor="#FFFFFF">
					<td bgcolor=#999999>
						<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
							<TR>
								<TH colspan="2" align="center">
									IPTV多终端
								</TH>
							</TR>
							<tr bgcolor="#FFFFFF">
								<td width="50%">
									<div id="selectedDev">
										请查询设备！
									</div>
									<input type="hidden" name="txtdeviceId" value="" />
									<input type="hidden" name="deviceSn" value="" />
								</td>
								<td align="right" width="50%">
									<input type="button" name="btnDevRes" class=jianbian
										value="隐藏查询" onclick="txtSelectDevice()" />
								</td>
							</tr>
						</table>
					</td>
				</TR>
				<TR>
					<TD HEIGHT=5>
						&nbsp;
					</TD>
				</TR>
				<TR id="tr2" STYLE="display: none">
					<TD>
						<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
							align="center" valign="middle">
							<TR id="trUsing" STYLE="display: ">
								<td>
									<TABLE width="100%" border=0 cellspacing=0 cellpadding=0>
										<tr height="25">
											<td align="right">
												<input type="button" class=jianbian value="获取IPTV信息"
													onclick="javascript:Using();" />
											</td>
										</tr>
									</TABLE>
								</td>
							</TR>
							<tr id=qh_con11 style="display: none;">
								<td>
									<font size=3>正在操作，请稍后...</font>
								</td>
							</tr>
							<TR id=qh_con12 style="display: ">
								<td>
									<DIV id=qh_con0 style="DISPLAY: ">
										<iframe id=myiframe1 src="" frameborder="0" scrolling="no"
											height="100%" width="100%"
											onload=this.height=myiframe1.document.body.scrollHeight;></iframe>
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